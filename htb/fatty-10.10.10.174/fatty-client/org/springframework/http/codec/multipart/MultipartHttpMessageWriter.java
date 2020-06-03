/*     */ package org.springframework.http.codec.multipart;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.ResolvableTypeProvider;
/*     */ import org.springframework.core.codec.CharSequenceEncoder;
/*     */ import org.springframework.core.codec.CodecException;
/*     */ import org.springframework.core.codec.Encoder;
/*     */ import org.springframework.core.codec.Hints;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.DataBufferFactory;
/*     */ import org.springframework.core.io.buffer.PooledDataBuffer;
/*     */ import org.springframework.core.log.LogFormatUtils;
/*     */ import org.springframework.http.HttpEntity;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.ReactiveHttpOutputMessage;
/*     */ import org.springframework.http.codec.EncoderHttpMessageWriter;
/*     */ import org.springframework.http.codec.FormHttpMessageWriter;
/*     */ import org.springframework.http.codec.HttpMessageWriter;
/*     */ import org.springframework.http.codec.LoggingCodecSupport;
/*     */ import org.springframework.http.codec.ResourceHttpMessageWriter;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.MimeTypeUtils;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import reactor.core.publisher.Flux;
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
/*     */ public class MultipartHttpMessageWriter
/*     */   extends LoggingCodecSupport
/*     */   implements HttpMessageWriter<MultiValueMap<String, ?>>
/*     */ {
/*  87 */   public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
/*     */ 
/*     */   
/*  90 */   private static final Map<String, Object> DEFAULT_HINTS = Hints.from(Hints.SUPPRESS_LOGGING_HINT, Boolean.valueOf(true));
/*     */ 
/*     */   
/*     */   private final List<HttpMessageWriter<?>> partWriters;
/*     */   
/*     */   @Nullable
/*     */   private final HttpMessageWriter<MultiValueMap<String, String>> formWriter;
/*     */   
/*  98 */   private Charset charset = DEFAULT_CHARSET;
/*     */ 
/*     */ 
/*     */   
/*     */   private final List<MediaType> supportedMediaTypes;
/*     */ 
/*     */ 
/*     */   
/*     */   public MultipartHttpMessageWriter() {
/* 107 */     this(Arrays.asList((HttpMessageWriter<?>[])new HttpMessageWriter[] { (HttpMessageWriter)new EncoderHttpMessageWriter(
/* 108 */               (Encoder)CharSequenceEncoder.textPlainOnly()), (HttpMessageWriter)new ResourceHttpMessageWriter() }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MultipartHttpMessageWriter(List<HttpMessageWriter<?>> partWriters) {
/* 117 */     this(partWriters, (HttpMessageWriter<MultiValueMap<String, String>>)new FormHttpMessageWriter());
/*     */   }
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
/*     */   public MultipartHttpMessageWriter(List<HttpMessageWriter<?>> partWriters, @Nullable HttpMessageWriter<MultiValueMap<String, String>> formWriter) {
/* 130 */     this.partWriters = partWriters;
/* 131 */     this.formWriter = formWriter;
/* 132 */     this.supportedMediaTypes = initMediaTypes(formWriter);
/*     */   }
/*     */   
/*     */   private static List<MediaType> initMediaTypes(@Nullable HttpMessageWriter<?> formWriter) {
/* 136 */     List<MediaType> result = new ArrayList<>();
/* 137 */     result.add(MediaType.MULTIPART_FORM_DATA);
/* 138 */     if (formWriter != null) {
/* 139 */       result.addAll(formWriter.getWritableMediaTypes());
/*     */     }
/* 141 */     return Collections.unmodifiableList(result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<HttpMessageWriter<?>> getPartWriters() {
/* 150 */     return Collections.unmodifiableList(this.partWriters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCharset(Charset charset) {
/* 159 */     Assert.notNull(charset, "Charset must not be null");
/* 160 */     this.charset = charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Charset getCharset() {
/* 167 */     return this.charset;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<MediaType> getWritableMediaTypes() {
/* 173 */     return this.supportedMediaTypes;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canWrite(ResolvableType elementType, @Nullable MediaType mediaType) {
/* 178 */     return (MultiValueMap.class.isAssignableFrom(elementType.toClass()) && (mediaType == null || this.supportedMediaTypes
/*     */       
/* 180 */       .stream().anyMatch(element -> element.isCompatibleWith(mediaType))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mono<Void> write(Publisher<? extends MultiValueMap<String, ?>> inputStream, ResolvableType elementType, @Nullable MediaType mediaType, ReactiveHttpOutputMessage outputMessage, Map<String, Object> hints) {
/* 188 */     return Mono.from(inputStream)
/* 189 */       .flatMap(map -> {
/*     */           if (this.formWriter == null || isMultipart(map, mediaType)) {
/*     */             return writeMultipart(map, outputMessage, hints);
/*     */           }
/*     */           Mono<MultiValueMap<String, String>> input = Mono.just(map);
/*     */           return this.formWriter.write((Publisher)input, elementType, mediaType, outputMessage, hints);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isMultipart(MultiValueMap<String, ?> map, @Nullable MediaType contentType) {
/* 202 */     if (contentType != null) {
/* 203 */       return MediaType.MULTIPART_FORM_DATA.includes(contentType);
/*     */     }
/* 205 */     for (List<?> values : (Iterable<List<?>>)map.values()) {
/* 206 */       for (Object value : values) {
/* 207 */         if (value != null && !(value instanceof String)) {
/* 208 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 212 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Mono<Void> writeMultipart(MultiValueMap<String, ?> map, ReactiveHttpOutputMessage outputMessage, Map<String, Object> hints) {
/* 218 */     byte[] boundary = generateMultipartBoundary();
/*     */     
/* 220 */     Map<String, String> params = new HashMap<>(2);
/* 221 */     params.put("boundary", new String(boundary, StandardCharsets.US_ASCII));
/* 222 */     params.put("charset", getCharset().name());
/*     */     
/* 224 */     outputMessage.getHeaders().setContentType(new MediaType(MediaType.MULTIPART_FORM_DATA, params));
/*     */     
/* 226 */     LogFormatUtils.traceDebug(this.logger, traceOn -> Hints.getLogPrefix(hints) + "Encoding " + (isEnableLoggingRequestDetails() ? LogFormatUtils.formatValue(map, !traceOn.booleanValue()) : ("parts " + map.keySet() + " (content masked)")));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 231 */     DataBufferFactory bufferFactory = outputMessage.bufferFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 236 */     Flux<DataBuffer> body = Flux.fromIterable(map.entrySet()).concatMap(entry -> encodePartValues(boundary, (String)entry.getKey(), (List)entry.getValue(), bufferFactory)).concatWith((Publisher)generateLastLine(boundary, bufferFactory)).doOnDiscard(PooledDataBuffer.class, PooledDataBuffer::release);
/*     */     
/* 238 */     return outputMessage.writeWith((Publisher)body);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected byte[] generateMultipartBoundary() {
/* 246 */     return MimeTypeUtils.generateMultipartBoundary();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Flux<DataBuffer> encodePartValues(byte[] boundary, String name, List<?> values, DataBufferFactory bufferFactory) {
/* 252 */     return Flux.concat((Iterable)values.stream().map(v -> encodePart(boundary, name, v, bufferFactory))
/* 253 */         .collect(Collectors.toList()));
/*     */   }
/*     */   private <T> Flux<DataBuffer> encodePart(byte[] boundary, String name, T value, DataBufferFactory bufferFactory) {
/*     */     T body;
/*     */     Mono mono;
/* 258 */     MultipartHttpOutputMessage outputMessage = new MultipartHttpOutputMessage(bufferFactory, getCharset());
/* 259 */     HttpHeaders outputHeaders = outputMessage.getHeaders();
/*     */ 
/*     */     
/* 262 */     ResolvableType resolvableType = null;
/* 263 */     if (value instanceof HttpEntity) {
/* 264 */       HttpEntity<T> httpEntity = (HttpEntity<T>)value;
/* 265 */       outputHeaders.putAll((Map)httpEntity.getHeaders());
/* 266 */       body = (T)httpEntity.getBody();
/* 267 */       Assert.state((body != null), "MultipartHttpMessageWriter only supports HttpEntity with body");
/* 268 */       if (httpEntity instanceof ResolvableTypeProvider) {
/* 269 */         resolvableType = ((ResolvableTypeProvider)httpEntity).getResolvableType();
/*     */       }
/*     */     } else {
/*     */       
/* 273 */       body = value;
/*     */     } 
/* 275 */     if (resolvableType == null) {
/* 276 */       resolvableType = ResolvableType.forClass(body.getClass());
/*     */     }
/*     */     
/* 279 */     if (!outputHeaders.containsKey("Content-Disposition")) {
/* 280 */       if (body instanceof Resource) {
/* 281 */         outputHeaders.setContentDispositionFormData(name, ((Resource)body).getFilename());
/*     */       }
/* 283 */       else if (resolvableType.resolve() == Resource.class) {
/* 284 */         mono = Mono.from((Publisher)body).doOnNext(o -> outputHeaders.setContentDispositionFormData(name, ((Resource)o).getFilename()));
/*     */       }
/*     */       else {
/*     */         
/* 288 */         outputHeaders.setContentDispositionFormData(name, null);
/*     */       } 
/*     */     }
/*     */     
/* 292 */     MediaType contentType = outputHeaders.getContentType();
/*     */     
/* 294 */     ResolvableType finalBodyType = resolvableType;
/*     */ 
/*     */     
/* 297 */     Optional<HttpMessageWriter<?>> writer = this.partWriters.stream().filter(partWriter -> partWriter.canWrite(finalBodyType, contentType)).findFirst();
/*     */     
/* 299 */     if (!writer.isPresent()) {
/* 300 */       return Flux.error((Throwable)new CodecException("No suitable writer found for part: " + name));
/*     */     }
/*     */ 
/*     */     
/* 304 */     Publisher<T> bodyPublisher = (mono instanceof Publisher) ? (Publisher<T>)mono : (Publisher<T>)Mono.just(mono);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 310 */     Mono<Void> partContentReady = ((HttpMessageWriter)writer.get()).write(bodyPublisher, resolvableType, contentType, outputMessage, DEFAULT_HINTS);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 315 */     Flux<DataBuffer> partContent = partContentReady.thenMany((Publisher)Flux.defer(outputMessage::getBody));
/*     */     
/* 317 */     return Flux.concat(new Publisher[] { (Publisher)
/* 318 */           generateBoundaryLine(boundary, bufferFactory), (Publisher)partContent, (Publisher)
/*     */           
/* 320 */           generateNewLine(bufferFactory) });
/*     */   }
/*     */ 
/*     */   
/*     */   private Mono<DataBuffer> generateBoundaryLine(byte[] boundary, DataBufferFactory bufferFactory) {
/* 325 */     return Mono.fromCallable(() -> {
/*     */           DataBuffer buffer = bufferFactory.allocateBuffer(boundary.length + 4);
/*     */           buffer.write((byte)45);
/*     */           buffer.write((byte)45);
/*     */           buffer.write(boundary);
/*     */           buffer.write((byte)13);
/*     */           buffer.write((byte)10);
/*     */           return buffer;
/*     */         });
/*     */   }
/*     */   
/*     */   private Mono<DataBuffer> generateNewLine(DataBufferFactory bufferFactory) {
/* 337 */     return Mono.fromCallable(() -> {
/*     */           DataBuffer buffer = bufferFactory.allocateBuffer(2);
/*     */           buffer.write((byte)13);
/*     */           buffer.write((byte)10);
/*     */           return buffer;
/*     */         });
/*     */   }
/*     */   
/*     */   private Mono<DataBuffer> generateLastLine(byte[] boundary, DataBufferFactory bufferFactory) {
/* 346 */     return Mono.fromCallable(() -> {
/*     */           DataBuffer buffer = bufferFactory.allocateBuffer(boundary.length + 6);
/*     */           buffer.write((byte)45);
/*     */           buffer.write((byte)45);
/*     */           buffer.write(boundary);
/*     */           buffer.write((byte)45);
/*     */           buffer.write((byte)45);
/*     */           buffer.write((byte)13);
/*     */           buffer.write((byte)10);
/*     */           return buffer;
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private static class MultipartHttpOutputMessage
/*     */     implements ReactiveHttpOutputMessage
/*     */   {
/*     */     private final DataBufferFactory bufferFactory;
/*     */     
/*     */     private final Charset charset;
/* 366 */     private final HttpHeaders headers = new HttpHeaders();
/*     */     
/* 368 */     private final AtomicBoolean committed = new AtomicBoolean();
/*     */     
/*     */     @Nullable
/*     */     private Flux<DataBuffer> body;
/*     */     
/*     */     public MultipartHttpOutputMessage(DataBufferFactory bufferFactory, Charset charset) {
/* 374 */       this.bufferFactory = bufferFactory;
/* 375 */       this.charset = charset;
/*     */     }
/*     */ 
/*     */     
/*     */     public HttpHeaders getHeaders() {
/* 380 */       return (this.body != null) ? HttpHeaders.readOnlyHttpHeaders(this.headers) : this.headers;
/*     */     }
/*     */ 
/*     */     
/*     */     public DataBufferFactory bufferFactory() {
/* 385 */       return this.bufferFactory;
/*     */     }
/*     */ 
/*     */     
/*     */     public void beforeCommit(Supplier<? extends Mono<Void>> action) {
/* 390 */       this.committed.set(true);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isCommitted() {
/* 395 */       return this.committed.get();
/*     */     }
/*     */ 
/*     */     
/*     */     public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
/* 400 */       if (this.body != null) {
/* 401 */         return Mono.error(new IllegalStateException("Multiple calls to writeWith() not supported"));
/*     */       }
/* 403 */       this.body = generateHeaders().concatWith(body);
/*     */ 
/*     */       
/* 406 */       return Mono.empty();
/*     */     }
/*     */     
/*     */     private Mono<DataBuffer> generateHeaders() {
/* 410 */       return Mono.fromCallable(() -> {
/*     */             DataBuffer buffer = this.bufferFactory.allocateBuffer();
/*     */             for (Map.Entry<String, List<String>> entry : (Iterable<Map.Entry<String, List<String>>>)this.headers.entrySet()) {
/*     */               byte[] headerName = ((String)entry.getKey()).getBytes(this.charset);
/*     */               for (String headerValueString : entry.getValue()) {
/*     */                 byte[] headerValue = headerValueString.getBytes(this.charset);
/*     */                 buffer.write(headerName);
/*     */                 buffer.write((byte)58);
/*     */                 buffer.write((byte)32);
/*     */                 buffer.write(headerValue);
/*     */                 buffer.write((byte)13);
/*     */                 buffer.write((byte)10);
/*     */               } 
/*     */             } 
/*     */             buffer.write((byte)13);
/*     */             buffer.write((byte)10);
/*     */             return buffer;
/*     */           });
/*     */     }
/*     */ 
/*     */     
/*     */     public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
/* 432 */       return Mono.error(new UnsupportedOperationException());
/*     */     }
/*     */     
/*     */     public Flux<DataBuffer> getBody() {
/* 436 */       return (this.body != null) ? this.body : 
/* 437 */         Flux.error(new IllegalStateException("Body has not been written yet"));
/*     */     }
/*     */ 
/*     */     
/*     */     public Mono<Void> setComplete() {
/* 442 */       return Mono.error(new UnsupportedOperationException());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/multipart/MultipartHttpMessageWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */