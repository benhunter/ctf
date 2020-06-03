/*     */ package org.springframework.http.server.reactive;
/*     */ 
/*     */ import io.undertow.server.HttpServerExchange;
/*     */ import io.undertow.server.handlers.CookieImpl;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import org.reactivestreams.Processor;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.DataBufferFactory;
/*     */ import org.springframework.core.io.buffer.DataBufferUtils;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.ResponseCookie;
/*     */ import org.springframework.http.ZeroCopyHttpOutputMessage;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.xnio.channels.StreamSinkChannel;
/*     */ import reactor.core.publisher.Mono;
/*     */ import reactor.core.publisher.MonoSink;
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
/*     */ class UndertowServerHttpResponse
/*     */   extends AbstractListenerServerHttpResponse
/*     */   implements ZeroCopyHttpOutputMessage
/*     */ {
/*     */   private final HttpServerExchange exchange;
/*     */   private final UndertowServerHttpRequest request;
/*     */   @Nullable
/*     */   private StreamSinkChannel responseChannel;
/*     */   
/*     */   UndertowServerHttpResponse(HttpServerExchange exchange, DataBufferFactory bufferFactory, UndertowServerHttpRequest request) {
/*  65 */     super(bufferFactory, createHeaders(exchange));
/*  66 */     Assert.notNull(exchange, "HttpServerExchange must not be null");
/*  67 */     this.exchange = exchange;
/*  68 */     this.request = request;
/*     */   }
/*     */   
/*     */   private static HttpHeaders createHeaders(HttpServerExchange exchange) {
/*  72 */     UndertowHeadersAdapter headersMap = new UndertowHeadersAdapter(exchange.getResponseHeaders());
/*  73 */     return new HttpHeaders(headersMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getNativeResponse() {
/*  80 */     return (T)this.exchange;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpStatus getStatusCode() {
/*  85 */     HttpStatus httpStatus = super.getStatusCode();
/*  86 */     return (httpStatus != null) ? httpStatus : HttpStatus.resolve(this.exchange.getStatusCode());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void applyStatusCode() {
/*  92 */     Integer statusCode = getStatusCodeValue();
/*  93 */     if (statusCode != null) {
/*  94 */       this.exchange.setStatusCode(statusCode.intValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void applyHeaders() {}
/*     */ 
/*     */   
/*     */   protected void applyCookies() {
/* 104 */     for (String name : getCookies().keySet()) {
/* 105 */       for (ResponseCookie httpCookie : getCookies().get(name)) {
/* 106 */         CookieImpl cookieImpl = new CookieImpl(name, httpCookie.getValue());
/* 107 */         if (!httpCookie.getMaxAge().isNegative()) {
/* 108 */           cookieImpl.setMaxAge(Integer.valueOf((int)httpCookie.getMaxAge().getSeconds()));
/*     */         }
/* 110 */         if (httpCookie.getDomain() != null) {
/* 111 */           cookieImpl.setDomain(httpCookie.getDomain());
/*     */         }
/* 113 */         if (httpCookie.getPath() != null) {
/* 114 */           cookieImpl.setPath(httpCookie.getPath());
/*     */         }
/* 116 */         cookieImpl.setSecure(httpCookie.isSecure());
/* 117 */         cookieImpl.setHttpOnly(httpCookie.isHttpOnly());
/* 118 */         this.exchange.getResponseCookies().putIfAbsent(name, cookieImpl);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Mono<Void> writeWith(Path file, long position, long count) {
/* 125 */     return doCommit(() -> Mono.create(()));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Processor<? super Publisher<? extends DataBuffer>, Void> createBodyFlushProcessor() {
/* 147 */     return new ResponseBodyFlushProcessor();
/*     */   }
/*     */   
/*     */   private ResponseBodyProcessor createBodyProcessor() {
/* 151 */     if (this.responseChannel == null) {
/* 152 */       this.responseChannel = this.exchange.getResponseChannel();
/*     */     }
/* 154 */     return new ResponseBodyProcessor(this.responseChannel);
/*     */   }
/*     */ 
/*     */   
/*     */   private class ResponseBodyProcessor
/*     */     extends AbstractListenerWriteProcessor<DataBuffer>
/*     */   {
/*     */     private final StreamSinkChannel channel;
/*     */     
/*     */     @Nullable
/*     */     private volatile ByteBuffer byteBuffer;
/*     */     
/*     */     private volatile boolean writePossible;
/*     */ 
/*     */     
/*     */     public ResponseBodyProcessor(StreamSinkChannel channel) {
/* 170 */       super(UndertowServerHttpResponse.this.request.getLogPrefix());
/* 171 */       Assert.notNull(channel, "StreamSinkChannel must not be null");
/* 172 */       this.channel = channel;
/* 173 */       this.channel.getWriteSetter().set(c -> {
/*     */             this.writePossible = true;
/*     */             onWritePossible();
/*     */           });
/* 177 */       this.channel.suspendWrites();
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isWritePossible() {
/* 182 */       this.channel.resumeWrites();
/* 183 */       return this.writePossible;
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean write(DataBuffer dataBuffer) throws IOException {
/* 188 */       ByteBuffer buffer = this.byteBuffer;
/* 189 */       if (buffer == null) {
/* 190 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 194 */       this.writePossible = false;
/*     */ 
/*     */       
/* 197 */       int total = buffer.remaining();
/* 198 */       int written = writeByteBuffer(buffer);
/*     */       
/* 200 */       if (UndertowServerHttpResponse.this.logger.isTraceEnabled()) {
/* 201 */         UndertowServerHttpResponse.this.logger.trace(getLogPrefix() + "Wrote " + written + " of " + total + " bytes");
/*     */       }
/* 203 */       else if (rsWriteLogger.isTraceEnabled()) {
/* 204 */         rsWriteLogger.trace(getLogPrefix() + "Wrote " + written + " of " + total + " bytes");
/*     */       } 
/* 206 */       if (written != total) {
/* 207 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 211 */       this.writePossible = true;
/*     */       
/* 213 */       DataBufferUtils.release(dataBuffer);
/* 214 */       this.byteBuffer = null;
/* 215 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     private int writeByteBuffer(ByteBuffer byteBuffer) throws IOException {
/* 220 */       int written, totalWritten = 0;
/*     */       do {
/* 222 */         written = this.channel.write(byteBuffer);
/* 223 */         totalWritten += written;
/*     */       }
/* 225 */       while (byteBuffer.hasRemaining() && written > 0);
/* 226 */       return totalWritten;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void dataReceived(DataBuffer dataBuffer) {
/* 231 */       super.dataReceived(dataBuffer);
/* 232 */       this.byteBuffer = dataBuffer.asByteBuffer();
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isDataEmpty(DataBuffer dataBuffer) {
/* 237 */       return (dataBuffer.readableByteCount() == 0);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void writingComplete() {
/* 242 */       this.channel.getWriteSetter().set(null);
/* 243 */       this.channel.resumeWrites();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void writingFailed(Throwable ex) {
/* 248 */       cancel();
/* 249 */       onError(ex);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void discardData(DataBuffer dataBuffer) {
/* 254 */       DataBufferUtils.release(dataBuffer);
/*     */     }
/*     */   }
/*     */   
/*     */   private class ResponseBodyFlushProcessor
/*     */     extends AbstractListenerWriteFlushProcessor<DataBuffer>
/*     */   {
/*     */     public ResponseBodyFlushProcessor() {
/* 262 */       super(UndertowServerHttpResponse.this.request.getLogPrefix());
/*     */     }
/*     */ 
/*     */     
/*     */     protected Processor<? super DataBuffer, Void> createWriteProcessor() {
/* 267 */       return UndertowServerHttpResponse.this.createBodyProcessor();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void flush() throws IOException {
/* 272 */       StreamSinkChannel channel = UndertowServerHttpResponse.this.responseChannel;
/* 273 */       if (channel != null) {
/* 274 */         if (rsWriteFlushLogger.isTraceEnabled()) {
/* 275 */           rsWriteFlushLogger.trace(getLogPrefix() + "flush");
/*     */         }
/* 277 */         channel.flush();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected void flushingFailed(Throwable t) {
/* 283 */       cancel();
/* 284 */       onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isWritePossible() {
/* 289 */       StreamSinkChannel channel = UndertowServerHttpResponse.this.responseChannel;
/* 290 */       if (channel != null) {
/*     */         
/* 292 */         channel.resumeWrites();
/* 293 */         return true;
/*     */       } 
/* 295 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isFlushPending() {
/* 300 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class TransferBodyListener
/*     */   {
/*     */     private final FileChannel source;
/*     */     
/*     */     private final MonoSink<Void> sink;
/*     */     
/*     */     private long position;
/*     */     
/*     */     private long count;
/*     */ 
/*     */     
/*     */     public TransferBodyListener(FileChannel source, long position, long count, MonoSink<Void> sink) {
/* 317 */       this.source = source;
/* 318 */       this.sink = sink;
/* 319 */       this.position = position;
/* 320 */       this.count = count;
/*     */     }
/*     */     
/*     */     public void transfer(StreamSinkChannel destination) {
/*     */       try {
/* 325 */         while (this.count > 0L) {
/* 326 */           long len = destination.transferFrom(this.source, this.position, this.count);
/* 327 */           if (len != 0L) {
/* 328 */             this.position += len;
/* 329 */             this.count -= len;
/*     */             continue;
/*     */           } 
/* 332 */           destination.resumeWrites();
/*     */           
/*     */           return;
/*     */         } 
/* 336 */         this.sink.success();
/*     */       }
/* 338 */       catch (IOException ex) {
/* 339 */         this.sink.error(ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void closeSource() {
/*     */       try {
/* 346 */         this.source.close();
/*     */       }
/* 348 */       catch (IOException iOException) {}
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/reactive/UndertowServerHttpResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */