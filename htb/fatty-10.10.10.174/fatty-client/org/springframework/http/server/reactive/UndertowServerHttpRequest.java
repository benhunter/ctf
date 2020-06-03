/*     */ package org.springframework.http.server.reactive;
/*     */ 
/*     */ import io.undertow.connector.ByteBufferPool;
/*     */ import io.undertow.connector.PooledByteBuffer;
/*     */ import io.undertow.server.ExchangeCompletionListener;
/*     */ import io.undertow.server.HttpServerExchange;
/*     */ import io.undertow.server.handlers.Cookie;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.function.IntPredicate;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.DataBufferFactory;
/*     */ import org.springframework.core.io.buffer.DataBufferUtils;
/*     */ import org.springframework.core.io.buffer.PooledDataBuffer;
/*     */ import org.springframework.http.HttpCookie;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.xnio.channels.StreamSourceChannel;
/*     */ import reactor.core.publisher.Flux;
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
/*     */ class UndertowServerHttpRequest
/*     */   extends AbstractServerHttpRequest
/*     */ {
/*     */   private final HttpServerExchange exchange;
/*     */   private final RequestBodyPublisher body;
/*     */   
/*     */   public UndertowServerHttpRequest(HttpServerExchange exchange, DataBufferFactory bufferFactory) throws URISyntaxException {
/*  68 */     super(initUri(exchange), "", initHeaders(exchange));
/*  69 */     this.exchange = exchange;
/*  70 */     this.body = new RequestBodyPublisher(exchange, bufferFactory);
/*  71 */     this.body.registerListeners(exchange);
/*     */   }
/*     */   
/*     */   private static URI initUri(HttpServerExchange exchange) throws URISyntaxException {
/*  75 */     Assert.notNull(exchange, "HttpServerExchange is required");
/*  76 */     String requestURL = exchange.getRequestURL();
/*  77 */     String query = exchange.getQueryString();
/*  78 */     String requestUriAndQuery = StringUtils.hasLength(query) ? (requestURL + "?" + query) : requestURL;
/*  79 */     return new URI(requestUriAndQuery);
/*     */   }
/*     */   
/*     */   private static HttpHeaders initHeaders(HttpServerExchange exchange) {
/*  83 */     return new HttpHeaders(new UndertowHeadersAdapter(exchange.getRequestHeaders()));
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMethodValue() {
/*  88 */     return this.exchange.getRequestMethod().toString();
/*     */   }
/*     */ 
/*     */   
/*     */   protected MultiValueMap<String, HttpCookie> initCookies() {
/*  93 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap();
/*  94 */     for (String name : this.exchange.getRequestCookies().keySet()) {
/*  95 */       Cookie cookie = (Cookie)this.exchange.getRequestCookies().get(name);
/*  96 */       HttpCookie httpCookie = new HttpCookie(name, cookie.getValue());
/*  97 */       linkedMultiValueMap.add(name, httpCookie);
/*     */     } 
/*  99 */     return (MultiValueMap<String, HttpCookie>)linkedMultiValueMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress getRemoteAddress() {
/* 104 */     return this.exchange.getSourceAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected SslInfo initSslInfo() {
/* 110 */     SSLSession session = this.exchange.getConnection().getSslSession();
/* 111 */     if (session != null) {
/* 112 */       return new DefaultSslInfo(session);
/*     */     }
/* 114 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Flux<DataBuffer> getBody() {
/* 119 */     return Flux.from(this.body);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getNativeRequest() {
/* 125 */     return (T)this.exchange;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String initId() {
/* 130 */     return ObjectUtils.getIdentityHexString(this.exchange.getConnection());
/*     */   }
/*     */ 
/*     */   
/*     */   private class RequestBodyPublisher
/*     */     extends AbstractListenerReadPublisher<DataBuffer>
/*     */   {
/*     */     private final StreamSourceChannel channel;
/*     */     
/*     */     private final DataBufferFactory bufferFactory;
/*     */     private final ByteBufferPool byteBufferPool;
/*     */     
/*     */     public RequestBodyPublisher(HttpServerExchange exchange, DataBufferFactory bufferFactory) {
/* 143 */       super(UndertowServerHttpRequest.this.getLogPrefix());
/* 144 */       this.channel = exchange.getRequestChannel();
/* 145 */       this.bufferFactory = bufferFactory;
/* 146 */       this.byteBufferPool = exchange.getConnection().getByteBufferPool();
/*     */     }
/*     */     
/*     */     private void registerListeners(HttpServerExchange exchange) {
/* 150 */       exchange.addExchangeCompleteListener((ex, next) -> {
/*     */             onAllDataRead();
/*     */             next.proceed();
/*     */           });
/* 154 */       this.channel.getReadSetter().set(c -> onDataAvailable());
/* 155 */       this.channel.getCloseSetter().set(c -> onAllDataRead());
/* 156 */       this.channel.resumeReads();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void checkOnDataAvailable() {
/* 161 */       this.channel.resumeReads();
/*     */       
/* 163 */       onDataAvailable();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void readingPaused() {
/* 168 */       this.channel.suspendReads();
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     protected DataBuffer read() throws IOException {
/* 174 */       PooledByteBuffer pooledByteBuffer = this.byteBufferPool.allocate();
/* 175 */       boolean release = true;
/*     */       try {
/* 177 */         ByteBuffer byteBuffer = pooledByteBuffer.getBuffer();
/* 178 */         int read = this.channel.read(byteBuffer);
/*     */         
/* 180 */         if (rsReadLogger.isTraceEnabled()) {
/* 181 */           rsReadLogger.trace(getLogPrefix() + "Read " + read + ((read != -1) ? " bytes" : ""));
/*     */         }
/*     */         
/* 184 */         if (read > 0) {
/* 185 */           byteBuffer.flip();
/* 186 */           DataBuffer dataBuffer = this.bufferFactory.wrap(byteBuffer);
/* 187 */           release = false;
/* 188 */           return (DataBuffer)new UndertowServerHttpRequest.UndertowDataBuffer(dataBuffer, pooledByteBuffer);
/*     */         } 
/* 190 */         if (read == -1) {
/* 191 */           onAllDataRead();
/*     */         }
/* 193 */         return null;
/*     */       } finally {
/*     */         
/* 196 */         if (release && pooledByteBuffer.isOpen()) {
/* 197 */           pooledByteBuffer.close();
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected void discardData() {}
/*     */   }
/*     */ 
/*     */   
/*     */   private static class UndertowDataBuffer
/*     */     implements PooledDataBuffer
/*     */   {
/*     */     private final DataBuffer dataBuffer;
/*     */     
/*     */     private final PooledByteBuffer pooledByteBuffer;
/*     */     
/*     */     private final AtomicInteger refCount;
/*     */ 
/*     */     
/*     */     public UndertowDataBuffer(DataBuffer dataBuffer, PooledByteBuffer pooledByteBuffer) {
/* 218 */       this.dataBuffer = dataBuffer;
/* 219 */       this.pooledByteBuffer = pooledByteBuffer;
/* 220 */       this.refCount = new AtomicInteger(1);
/*     */     }
/*     */ 
/*     */     
/*     */     private UndertowDataBuffer(DataBuffer dataBuffer, PooledByteBuffer pooledByteBuffer, AtomicInteger refCount) {
/* 225 */       this.refCount = refCount;
/* 226 */       this.dataBuffer = dataBuffer;
/* 227 */       this.pooledByteBuffer = pooledByteBuffer;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isAllocated() {
/* 232 */       return (this.refCount.get() > 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public PooledDataBuffer retain() {
/* 237 */       this.refCount.incrementAndGet();
/* 238 */       DataBufferUtils.retain(this.dataBuffer);
/* 239 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean release() {
/* 244 */       int refCount = this.refCount.decrementAndGet();
/* 245 */       if (refCount == 0) {
/*     */         try {
/* 247 */           return DataBufferUtils.release(this.dataBuffer);
/*     */         } finally {
/*     */           
/* 250 */           this.pooledByteBuffer.close();
/*     */         } 
/*     */       }
/* 253 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public DataBufferFactory factory() {
/* 258 */       return this.dataBuffer.factory();
/*     */     }
/*     */ 
/*     */     
/*     */     public int indexOf(IntPredicate predicate, int fromIndex) {
/* 263 */       return this.dataBuffer.indexOf(predicate, fromIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public int lastIndexOf(IntPredicate predicate, int fromIndex) {
/* 268 */       return this.dataBuffer.lastIndexOf(predicate, fromIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public int readableByteCount() {
/* 273 */       return this.dataBuffer.readableByteCount();
/*     */     }
/*     */ 
/*     */     
/*     */     public int writableByteCount() {
/* 278 */       return this.dataBuffer.writableByteCount();
/*     */     }
/*     */ 
/*     */     
/*     */     public int readPosition() {
/* 283 */       return this.dataBuffer.readPosition();
/*     */     }
/*     */ 
/*     */     
/*     */     public DataBuffer readPosition(int readPosition) {
/* 288 */       return this.dataBuffer.readPosition(readPosition);
/*     */     }
/*     */ 
/*     */     
/*     */     public int writePosition() {
/* 293 */       return this.dataBuffer.writePosition();
/*     */     }
/*     */ 
/*     */     
/*     */     public DataBuffer writePosition(int writePosition) {
/* 298 */       this.dataBuffer.writePosition(writePosition);
/* 299 */       return (DataBuffer)this;
/*     */     }
/*     */ 
/*     */     
/*     */     public int capacity() {
/* 304 */       return this.dataBuffer.capacity();
/*     */     }
/*     */ 
/*     */     
/*     */     public DataBuffer capacity(int newCapacity) {
/* 309 */       this.dataBuffer.capacity(newCapacity);
/* 310 */       return (DataBuffer)this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DataBuffer ensureCapacity(int capacity) {
/* 315 */       this.dataBuffer.ensureCapacity(capacity);
/* 316 */       return (DataBuffer)this;
/*     */     }
/*     */ 
/*     */     
/*     */     public byte getByte(int index) {
/* 321 */       return this.dataBuffer.getByte(index);
/*     */     }
/*     */ 
/*     */     
/*     */     public byte read() {
/* 326 */       return this.dataBuffer.read();
/*     */     }
/*     */ 
/*     */     
/*     */     public DataBuffer read(byte[] destination) {
/* 331 */       this.dataBuffer.read(destination);
/* 332 */       return (DataBuffer)this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DataBuffer read(byte[] destination, int offset, int length) {
/* 337 */       this.dataBuffer.read(destination, offset, length);
/* 338 */       return (DataBuffer)this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DataBuffer write(byte b) {
/* 343 */       this.dataBuffer.write(b);
/* 344 */       return (DataBuffer)this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DataBuffer write(byte[] source) {
/* 349 */       this.dataBuffer.write(source);
/* 350 */       return (DataBuffer)this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DataBuffer write(byte[] source, int offset, int length) {
/* 355 */       this.dataBuffer.write(source, offset, length);
/* 356 */       return (DataBuffer)this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DataBuffer write(DataBuffer... buffers) {
/* 361 */       this.dataBuffer.write(buffers);
/* 362 */       return (DataBuffer)this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DataBuffer write(ByteBuffer... byteBuffers) {
/* 367 */       this.dataBuffer.write(byteBuffers);
/* 368 */       return (DataBuffer)this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DataBuffer write(CharSequence charSequence, Charset charset) {
/* 373 */       this.dataBuffer.write(charSequence, charset);
/* 374 */       return (DataBuffer)this;
/*     */     }
/*     */ 
/*     */     
/*     */     public DataBuffer slice(int index, int length) {
/* 379 */       DataBuffer slice = this.dataBuffer.slice(index, length);
/* 380 */       return (DataBuffer)new UndertowDataBuffer(slice, this.pooledByteBuffer, this.refCount);
/*     */     }
/*     */ 
/*     */     
/*     */     public ByteBuffer asByteBuffer() {
/* 385 */       return this.dataBuffer.asByteBuffer();
/*     */     }
/*     */ 
/*     */     
/*     */     public ByteBuffer asByteBuffer(int index, int length) {
/* 390 */       return this.dataBuffer.asByteBuffer(index, length);
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream asInputStream() {
/* 395 */       return this.dataBuffer.asInputStream();
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream asInputStream(boolean releaseOnClose) {
/* 400 */       return this.dataBuffer.asInputStream(releaseOnClose);
/*     */     }
/*     */ 
/*     */     
/*     */     public OutputStream asOutputStream() {
/* 405 */       return this.dataBuffer.asOutputStream();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/reactive/UndertowServerHttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */