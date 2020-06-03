/*     */ package org.springframework.http.server.reactive;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.List;
/*     */ import javax.servlet.AsyncContext;
/*     */ import javax.servlet.AsyncEvent;
/*     */ import javax.servlet.AsyncListener;
/*     */ import javax.servlet.ServletOutputStream;
/*     */ import javax.servlet.WriteListener;
/*     */ import javax.servlet.http.Cookie;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.reactivestreams.Processor;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.DataBufferFactory;
/*     */ import org.springframework.core.io.buffer.DataBufferUtils;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.ResponseCookie;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ class ServletServerHttpResponse
/*     */   extends AbstractListenerServerHttpResponse
/*     */ {
/*     */   private final HttpServletResponse response;
/*     */   private final ServletOutputStream outputStream;
/*     */   private final int bufferSize;
/*     */   @Nullable
/*     */   private volatile ResponseBodyFlushProcessor bodyFlushProcessor;
/*     */   @Nullable
/*     */   private volatile ResponseBodyProcessor bodyProcessor;
/*     */   private volatile boolean flushOnNext;
/*     */   private final ServletServerHttpRequest request;
/*     */   
/*     */   public ServletServerHttpResponse(HttpServletResponse response, AsyncContext asyncContext, DataBufferFactory bufferFactory, int bufferSize, ServletServerHttpRequest request) throws IOException {
/*  70 */     this(new HttpHeaders(), response, asyncContext, bufferFactory, bufferSize, request);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletServerHttpResponse(HttpHeaders headers, HttpServletResponse response, AsyncContext asyncContext, DataBufferFactory bufferFactory, int bufferSize, ServletServerHttpRequest request) throws IOException {
/*  76 */     super(bufferFactory, headers);
/*     */     
/*  78 */     Assert.notNull(response, "HttpServletResponse must not be null");
/*  79 */     Assert.notNull(bufferFactory, "DataBufferFactory must not be null");
/*  80 */     Assert.isTrue((bufferSize > 0), "Buffer size must be greater than 0");
/*     */     
/*  82 */     this.response = response;
/*  83 */     this.outputStream = response.getOutputStream();
/*  84 */     this.bufferSize = bufferSize;
/*  85 */     this.request = request;
/*     */     
/*  87 */     asyncContext.addListener(new ResponseAsyncListener());
/*     */ 
/*     */     
/*  90 */     response.getOutputStream().setWriteListener(new ResponseBodyWriteListener());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getNativeResponse() {
/*  97 */     return (T)this.response;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpStatus getStatusCode() {
/* 102 */     HttpStatus httpStatus = super.getStatusCode();
/* 103 */     return (httpStatus != null) ? httpStatus : HttpStatus.resolve(this.response.getStatus());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyStatusCode() {
/* 108 */     Integer statusCode = getStatusCodeValue();
/* 109 */     if (statusCode != null) {
/* 110 */       this.response.setStatus(statusCode.intValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyHeaders() {
/* 116 */     getHeaders().forEach((headerName, headerValues) -> {
/*     */           for (String headerValue : headerValues) {
/*     */             this.response.addHeader(headerName, headerValue);
/*     */           }
/*     */         });
/* 121 */     MediaType contentType = getHeaders().getContentType();
/* 122 */     if (this.response.getContentType() == null && contentType != null) {
/* 123 */       this.response.setContentType(contentType.toString());
/*     */     }
/* 125 */     Charset charset = (contentType != null) ? contentType.getCharset() : null;
/* 126 */     if (this.response.getCharacterEncoding() == null && charset != null) {
/* 127 */       this.response.setCharacterEncoding(charset.name());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyCookies() {
/* 133 */     for (String name : getCookies().keySet()) {
/* 134 */       for (ResponseCookie httpCookie : getCookies().get(name)) {
/* 135 */         Cookie cookie = new Cookie(name, httpCookie.getValue());
/* 136 */         if (!httpCookie.getMaxAge().isNegative()) {
/* 137 */           cookie.setMaxAge((int)httpCookie.getMaxAge().getSeconds());
/*     */         }
/* 139 */         if (httpCookie.getDomain() != null) {
/* 140 */           cookie.setDomain(httpCookie.getDomain());
/*     */         }
/* 142 */         if (httpCookie.getPath() != null) {
/* 143 */           cookie.setPath(httpCookie.getPath());
/*     */         }
/* 145 */         cookie.setSecure(httpCookie.isSecure());
/* 146 */         cookie.setHttpOnly(httpCookie.isHttpOnly());
/* 147 */         this.response.addCookie(cookie);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected Processor<? super Publisher<? extends DataBuffer>, Void> createBodyFlushProcessor() {
/* 154 */     ResponseBodyFlushProcessor processor = new ResponseBodyFlushProcessor();
/* 155 */     this.bodyFlushProcessor = processor;
/* 156 */     return processor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int writeToOutputStream(DataBuffer dataBuffer) throws IOException {
/* 166 */     ServletOutputStream outputStream = this.outputStream;
/* 167 */     InputStream input = dataBuffer.asInputStream();
/* 168 */     int bytesWritten = 0;
/* 169 */     byte[] buffer = new byte[this.bufferSize];
/*     */     int bytesRead;
/* 171 */     while (outputStream.isReady() && (bytesRead = input.read(buffer)) != -1) {
/* 172 */       outputStream.write(buffer, 0, bytesRead);
/* 173 */       bytesWritten += bytesRead;
/*     */     } 
/* 175 */     return bytesWritten;
/*     */   }
/*     */   
/*     */   private void flush() throws IOException {
/* 179 */     ServletOutputStream outputStream = this.outputStream;
/* 180 */     if (outputStream.isReady()) {
/*     */       try {
/* 182 */         outputStream.flush();
/* 183 */         this.flushOnNext = false;
/*     */       }
/* 185 */       catch (IOException ex) {
/* 186 */         this.flushOnNext = true;
/* 187 */         throw ex;
/*     */       } 
/*     */     } else {
/*     */       
/* 191 */       this.flushOnNext = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isWritePossible() {
/* 196 */     return this.outputStream.isReady();
/*     */   }
/*     */   
/*     */   private final class ResponseAsyncListener
/*     */     implements AsyncListener
/*     */   {
/*     */     private ResponseAsyncListener() {}
/*     */     
/*     */     public void onStartAsync(AsyncEvent event) {}
/*     */     
/*     */     public void onTimeout(AsyncEvent event) {
/* 207 */       Throwable ex = event.getThrowable();
/* 208 */       ex = (ex != null) ? ex : new IllegalStateException("Async operation timeout.");
/* 209 */       handleError(ex);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(AsyncEvent event) {
/* 214 */       handleError(event.getThrowable());
/*     */     }
/*     */     
/*     */     void handleError(Throwable ex) {
/* 218 */       ServletServerHttpResponse.ResponseBodyFlushProcessor flushProcessor = ServletServerHttpResponse.this.bodyFlushProcessor;
/* 219 */       if (flushProcessor != null) {
/* 220 */         flushProcessor.cancel();
/* 221 */         flushProcessor.onError(ex);
/*     */       } 
/*     */       
/* 224 */       ServletServerHttpResponse.ResponseBodyProcessor processor = ServletServerHttpResponse.this.bodyProcessor;
/* 225 */       if (processor != null) {
/* 226 */         processor.cancel();
/* 227 */         processor.onError(ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete(AsyncEvent event) {
/* 233 */       ServletServerHttpResponse.ResponseBodyFlushProcessor flushProcessor = ServletServerHttpResponse.this.bodyFlushProcessor;
/* 234 */       if (flushProcessor != null) {
/* 235 */         flushProcessor.cancel();
/* 236 */         flushProcessor.onComplete();
/*     */       } 
/*     */       
/* 239 */       ServletServerHttpResponse.ResponseBodyProcessor processor = ServletServerHttpResponse.this.bodyProcessor;
/* 240 */       if (processor != null) {
/* 241 */         processor.cancel();
/* 242 */         processor.onComplete();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private class ResponseBodyWriteListener
/*     */     implements WriteListener {
/*     */     private ResponseBodyWriteListener() {}
/*     */     
/*     */     public void onWritePossible() throws IOException {
/* 252 */       ServletServerHttpResponse.ResponseBodyProcessor processor = ServletServerHttpResponse.this.bodyProcessor;
/* 253 */       if (processor != null) {
/* 254 */         processor.onWritePossible();
/*     */       } else {
/*     */         
/* 257 */         ServletServerHttpResponse.ResponseBodyFlushProcessor flushProcessor = ServletServerHttpResponse.this.bodyFlushProcessor;
/* 258 */         if (flushProcessor != null) {
/* 259 */           flushProcessor.onFlushPossible();
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable ex) {
/* 266 */       ServletServerHttpResponse.ResponseBodyProcessor processor = ServletServerHttpResponse.this.bodyProcessor;
/* 267 */       if (processor != null) {
/* 268 */         processor.cancel();
/* 269 */         processor.onError(ex);
/*     */       } else {
/*     */         
/* 272 */         ServletServerHttpResponse.ResponseBodyFlushProcessor flushProcessor = ServletServerHttpResponse.this.bodyFlushProcessor;
/* 273 */         if (flushProcessor != null) {
/* 274 */           flushProcessor.cancel();
/* 275 */           flushProcessor.onError(ex);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private class ResponseBodyFlushProcessor
/*     */     extends AbstractListenerWriteFlushProcessor<DataBuffer>
/*     */   {
/*     */     public ResponseBodyFlushProcessor() {
/* 285 */       super(ServletServerHttpResponse.this.request.getLogPrefix());
/*     */     }
/*     */ 
/*     */     
/*     */     protected Processor<? super DataBuffer, Void> createWriteProcessor() {
/* 290 */       ServletServerHttpResponse.ResponseBodyProcessor processor = new ServletServerHttpResponse.ResponseBodyProcessor();
/* 291 */       ServletServerHttpResponse.this.bodyProcessor = processor;
/* 292 */       return processor;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void flush() throws IOException {
/* 297 */       if (rsWriteFlushLogger.isTraceEnabled()) {
/* 298 */         rsWriteFlushLogger.trace(getLogPrefix() + "Flush attempt");
/*     */       }
/* 300 */       ServletServerHttpResponse.this.flush();
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isWritePossible() {
/* 305 */       return ServletServerHttpResponse.this.isWritePossible();
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isFlushPending() {
/* 310 */       return ServletServerHttpResponse.this.flushOnNext;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class ResponseBodyProcessor
/*     */     extends AbstractListenerWriteProcessor<DataBuffer>
/*     */   {
/*     */     public ResponseBodyProcessor() {
/* 319 */       super(ServletServerHttpResponse.this.request.getLogPrefix());
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isWritePossible() {
/* 324 */       return ServletServerHttpResponse.this.isWritePossible();
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isDataEmpty(DataBuffer dataBuffer) {
/* 329 */       return (dataBuffer.readableByteCount() == 0);
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean write(DataBuffer dataBuffer) throws IOException {
/* 334 */       if (ServletServerHttpResponse.this.flushOnNext) {
/* 335 */         if (rsWriteLogger.isTraceEnabled()) {
/* 336 */           rsWriteLogger.trace(getLogPrefix() + "Flush attempt");
/*     */         }
/* 338 */         ServletServerHttpResponse.this.flush();
/*     */       } 
/*     */       
/* 341 */       boolean ready = ServletServerHttpResponse.this.isWritePossible();
/* 342 */       int remaining = dataBuffer.readableByteCount();
/* 343 */       if (ready && remaining > 0) {
/*     */         
/* 345 */         int written = ServletServerHttpResponse.this.writeToOutputStream(dataBuffer);
/* 346 */         if (ServletServerHttpResponse.this.logger.isTraceEnabled()) {
/* 347 */           ServletServerHttpResponse.this.logger.trace(getLogPrefix() + "Wrote " + written + " of " + remaining + " bytes");
/*     */         }
/* 349 */         else if (rsWriteLogger.isTraceEnabled()) {
/* 350 */           rsWriteLogger.trace(getLogPrefix() + "Wrote " + written + " of " + remaining + " bytes");
/*     */         } 
/* 352 */         if (written == remaining) {
/* 353 */           DataBufferUtils.release(dataBuffer);
/* 354 */           return true;
/*     */         }
/*     */       
/*     */       }
/* 358 */       else if (rsWriteLogger.isTraceEnabled()) {
/* 359 */         rsWriteLogger.trace(getLogPrefix() + "ready: " + ready + ", remaining: " + remaining);
/*     */       } 
/*     */ 
/*     */       
/* 363 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void writingComplete() {
/* 368 */       ServletServerHttpResponse.this.bodyProcessor = null;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void discardData(DataBuffer dataBuffer) {
/* 373 */       DataBufferUtils.release(dataBuffer);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/reactive/ServletServerHttpResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */