/*     */ package org.springframework.http.codec.multipart;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.Channels;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.function.Consumer;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.codec.Hints;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.DataBufferFactory;
/*     */ import org.springframework.core.io.buffer.DataBufferUtils;
/*     */ import org.springframework.core.io.buffer.DefaultDataBufferFactory;
/*     */ import org.springframework.core.log.LogFormatUtils;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.ReactiveHttpInputMessage;
/*     */ import org.springframework.http.codec.HttpMessageReader;
/*     */ import org.springframework.http.codec.LoggingCodecSupport;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.synchronoss.cloud.nio.multipart.DefaultPartBodyStreamStorageFactory;
/*     */ import org.synchronoss.cloud.nio.multipart.Multipart;
/*     */ import org.synchronoss.cloud.nio.multipart.MultipartContext;
/*     */ import org.synchronoss.cloud.nio.multipart.MultipartUtils;
/*     */ import org.synchronoss.cloud.nio.multipart.NioMultipartParser;
/*     */ import org.synchronoss.cloud.nio.multipart.NioMultipartParserListener;
/*     */ import org.synchronoss.cloud.nio.multipart.PartBodyStreamStorageFactory;
/*     */ import org.synchronoss.cloud.nio.stream.storage.StreamStorage;
/*     */ import reactor.core.publisher.Flux;
/*     */ import reactor.core.publisher.FluxSink;
/*     */ import reactor.core.publisher.Mono;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SynchronossPartHttpMessageReader
/*     */   extends LoggingCodecSupport
/*     */   implements HttpMessageReader<Part>
/*     */ {
/*  78 */   private final DataBufferFactory bufferFactory = (DataBufferFactory)new DefaultDataBufferFactory();
/*     */   
/*  80 */   private final PartBodyStreamStorageFactory streamStorageFactory = (PartBodyStreamStorageFactory)new DefaultPartBodyStreamStorageFactory();
/*     */ 
/*     */ 
/*     */   
/*     */   public List<MediaType> getReadableMediaTypes() {
/*  85 */     return Collections.singletonList(MediaType.MULTIPART_FORM_DATA);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canRead(ResolvableType elementType, @Nullable MediaType mediaType) {
/*  90 */     return (Part.class.equals(elementType.toClass()) && (mediaType == null || MediaType.MULTIPART_FORM_DATA
/*  91 */       .isCompatibleWith(mediaType)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Flux<Part> read(ResolvableType elementType, ReactiveHttpInputMessage message, Map<String, Object> hints) {
/*  97 */     return Flux.create(new SynchronossPartGenerator(message, this.bufferFactory, this.streamStorageFactory))
/*  98 */       .doOnNext(part -> {
/*     */           if (!Hints.isLoggingSuppressed(hints)) {
/*     */             LogFormatUtils.traceDebug(this.logger, ());
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mono<Part> readMono(ResolvableType elementType, ReactiveHttpInputMessage message, Map<String, Object> hints) {
/* 111 */     return Mono.error(new UnsupportedOperationException("Cannot read multipart request body into single Part"));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SynchronossPartGenerator
/*     */     implements Consumer<FluxSink<Part>>
/*     */   {
/*     */     private final ReactiveHttpInputMessage inputMessage;
/*     */ 
/*     */     
/*     */     private final DataBufferFactory bufferFactory;
/*     */ 
/*     */     
/*     */     private final PartBodyStreamStorageFactory streamStorageFactory;
/*     */ 
/*     */ 
/*     */     
/*     */     SynchronossPartGenerator(ReactiveHttpInputMessage inputMessage, DataBufferFactory bufferFactory, PartBodyStreamStorageFactory streamStorageFactory) {
/* 130 */       this.inputMessage = inputMessage;
/* 131 */       this.bufferFactory = bufferFactory;
/* 132 */       this.streamStorageFactory = streamStorageFactory;
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept(FluxSink<Part> emitter) {
/* 137 */       HttpHeaders headers = this.inputMessage.getHeaders();
/* 138 */       MediaType mediaType = headers.getContentType();
/* 139 */       Assert.state((mediaType != null), "No content type set");
/*     */       
/* 141 */       int length = getContentLength(headers);
/* 142 */       Charset charset = Optional.<Charset>ofNullable(mediaType.getCharset()).orElse(StandardCharsets.UTF_8);
/* 143 */       MultipartContext context = new MultipartContext(mediaType.toString(), length, charset.name());
/*     */       
/* 145 */       NioMultipartParserListener listener = new SynchronossPartHttpMessageReader.FluxSinkAdapterListener(emitter, this.bufferFactory, context);
/*     */ 
/*     */ 
/*     */       
/* 149 */       NioMultipartParser parser = Multipart.multipart(context).usePartBodyStreamStorageFactory(this.streamStorageFactory).forNIO(listener);
/*     */       
/* 151 */       this.inputMessage.getBody().subscribe(buffer -> {
/*     */             byte[] resultBytes = new byte[buffer.readableByteCount()];
/*     */             
/*     */             buffer.read(resultBytes);
/*     */             try {
/*     */               parser.write(resultBytes);
/* 157 */             } catch (IOException ex) {
/*     */               listener.onError("Exception thrown providing input to the parser", ex);
/*     */             } finally {
/*     */               DataBufferUtils.release(buffer);
/*     */             } 
/*     */           }ex -> {
/*     */             try {
/*     */               listener.onError("Request body input error", ex);
/*     */ 
/*     */               
/*     */               parser.close();
/* 168 */             } catch (IOException ex2) {
/*     */               
/*     */               listener.onError("Exception thrown while closing the parser", ex2);
/*     */             } 
/*     */           }() -> {
/*     */             try {
/*     */               parser.close();
/* 175 */             } catch (IOException ex) {
/*     */               listener.onError("Exception thrown while closing the parser", ex);
/*     */             } 
/*     */           });
/*     */     }
/*     */ 
/*     */     
/*     */     private int getContentLength(HttpHeaders headers) {
/* 183 */       long length = headers.getContentLength();
/* 184 */       return ((int)length == length) ? (int)length : -1;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FluxSinkAdapterListener
/*     */     implements NioMultipartParserListener
/*     */   {
/*     */     private final FluxSink<Part> sink;
/*     */ 
/*     */     
/*     */     private final DataBufferFactory bufferFactory;
/*     */     
/*     */     private final MultipartContext context;
/*     */     
/* 200 */     private final AtomicInteger terminated = new AtomicInteger(0);
/*     */     
/*     */     FluxSinkAdapterListener(FluxSink<Part> sink, DataBufferFactory factory, MultipartContext context) {
/* 203 */       this.sink = sink;
/* 204 */       this.bufferFactory = factory;
/* 205 */       this.context = context;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onPartFinished(StreamStorage storage, Map<String, List<String>> headers) {
/* 210 */       HttpHeaders httpHeaders = new HttpHeaders();
/* 211 */       httpHeaders.putAll(headers);
/* 212 */       this.sink.next(createPart(storage, httpHeaders));
/*     */     }
/*     */     
/*     */     private Part createPart(StreamStorage storage, HttpHeaders httpHeaders) {
/* 216 */       String filename = MultipartUtils.getFileName((Map)httpHeaders);
/* 217 */       if (filename != null) {
/* 218 */         return new SynchronossPartHttpMessageReader.SynchronossFilePart(httpHeaders, filename, storage, this.bufferFactory);
/*     */       }
/* 220 */       if (MultipartUtils.isFormField((Map)httpHeaders, this.context)) {
/* 221 */         String value = MultipartUtils.readFormParameterValue(storage, (Map)httpHeaders);
/* 222 */         return new SynchronossPartHttpMessageReader.SynchronossFormFieldPart(httpHeaders, this.bufferFactory, value);
/*     */       } 
/*     */       
/* 225 */       return new SynchronossPartHttpMessageReader.SynchronossPart(httpHeaders, storage, this.bufferFactory);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onError(String message, Throwable cause) {
/* 231 */       if (this.terminated.getAndIncrement() == 0) {
/* 232 */         this.sink.error(new RuntimeException(message, cause));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onAllPartsFinished() {
/* 238 */       if (this.terminated.getAndIncrement() == 0) {
/* 239 */         this.sink.complete();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNestedPartStarted(Map<String, List<String>> headersFromParentPart) {}
/*     */ 
/*     */     
/*     */     public void onNestedPartFinished() {}
/*     */   }
/*     */ 
/*     */   
/*     */   private static abstract class AbstractSynchronossPart
/*     */     implements Part
/*     */   {
/*     */     private final String name;
/*     */     
/*     */     private final HttpHeaders headers;
/*     */     
/*     */     private final DataBufferFactory bufferFactory;
/*     */ 
/*     */     
/*     */     AbstractSynchronossPart(HttpHeaders headers, DataBufferFactory bufferFactory) {
/* 262 */       Assert.notNull(headers, "HttpHeaders is required");
/* 263 */       Assert.notNull(bufferFactory, "DataBufferFactory is required");
/* 264 */       this.name = MultipartUtils.getFieldName((Map)headers);
/* 265 */       this.headers = headers;
/* 266 */       this.bufferFactory = bufferFactory;
/*     */     }
/*     */ 
/*     */     
/*     */     public String name() {
/* 271 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public HttpHeaders headers() {
/* 276 */       return this.headers;
/*     */     }
/*     */     
/*     */     DataBufferFactory getBufferFactory() {
/* 280 */       return this.bufferFactory;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 285 */       return "Part '" + this.name + "', headers=" + this.headers;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SynchronossPart
/*     */     extends AbstractSynchronossPart
/*     */   {
/*     */     private final StreamStorage storage;
/*     */     
/*     */     SynchronossPart(HttpHeaders headers, StreamStorage storage, DataBufferFactory factory) {
/* 295 */       super(headers, factory);
/* 296 */       Assert.notNull(storage, "StreamStorage is required");
/* 297 */       this.storage = storage;
/*     */     }
/*     */ 
/*     */     
/*     */     public Flux<DataBuffer> content() {
/* 302 */       return DataBufferUtils.readInputStream(getStorage()::getInputStream, getBufferFactory(), 4096);
/*     */     }
/*     */     
/*     */     protected StreamStorage getStorage() {
/* 306 */       return this.storage;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SynchronossFilePart
/*     */     extends SynchronossPart
/*     */     implements FilePart {
/* 313 */     private static final OpenOption[] FILE_CHANNEL_OPTIONS = new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE };
/*     */     
/*     */     private final String filename;
/*     */ 
/*     */     
/*     */     SynchronossFilePart(HttpHeaders headers, String filename, StreamStorage storage, DataBufferFactory factory) {
/* 319 */       super(headers, storage, factory);
/* 320 */       this.filename = filename;
/*     */     }
/*     */ 
/*     */     
/*     */     public String filename() {
/* 325 */       return this.filename;
/*     */     }
/*     */ 
/*     */     
/*     */     public Mono<Void> transferTo(Path dest) {
/* 330 */       ReadableByteChannel input = null;
/* 331 */       FileChannel output = null;
/*     */       try {
/* 333 */         input = Channels.newChannel(getStorage().getInputStream());
/* 334 */         output = FileChannel.open(dest, FILE_CHANNEL_OPTIONS);
/* 335 */         long size = (input instanceof FileChannel) ? ((FileChannel)input).size() : Long.MAX_VALUE;
/* 336 */         long totalWritten = 0L;
/* 337 */         while (totalWritten < size) {
/* 338 */           long written = output.transferFrom(input, totalWritten, size - totalWritten);
/* 339 */           if (written <= 0L) {
/*     */             break;
/*     */           }
/* 342 */           totalWritten += written;
/*     */         }
/*     */       
/* 345 */       } catch (IOException ex) {
/* 346 */         return Mono.error(ex);
/*     */       } finally {
/*     */         
/* 349 */         if (input != null) {
/*     */           try {
/* 351 */             input.close();
/*     */           }
/* 353 */           catch (IOException iOException) {}
/*     */         }
/*     */         
/* 356 */         if (output != null) {
/*     */           try {
/* 358 */             output.close();
/*     */           }
/* 360 */           catch (IOException iOException) {}
/*     */         }
/*     */       } 
/*     */       
/* 364 */       return Mono.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 369 */       return "Part '" + name() + "', filename='" + this.filename + "'";
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SynchronossFormFieldPart
/*     */     extends AbstractSynchronossPart
/*     */     implements FormFieldPart {
/*     */     private final String content;
/*     */     
/*     */     SynchronossFormFieldPart(HttpHeaders headers, DataBufferFactory bufferFactory, String content) {
/* 379 */       super(headers, bufferFactory);
/* 380 */       this.content = content;
/*     */     }
/*     */ 
/*     */     
/*     */     public String value() {
/* 385 */       return this.content;
/*     */     }
/*     */ 
/*     */     
/*     */     public Flux<DataBuffer> content() {
/* 390 */       byte[] bytes = this.content.getBytes(getCharset());
/* 391 */       DataBuffer buffer = getBufferFactory().allocateBuffer(bytes.length);
/* 392 */       buffer.write(bytes);
/* 393 */       return Flux.just(buffer);
/*     */     }
/*     */     
/*     */     private Charset getCharset() {
/* 397 */       String name = MultipartUtils.getCharEncoding((Map)headers());
/* 398 */       return (name != null) ? Charset.forName(name) : StandardCharsets.UTF_8;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 403 */       return "Part '" + name() + "=" + this.content + "'";
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/multipart/SynchronossPartHttpMessageReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */