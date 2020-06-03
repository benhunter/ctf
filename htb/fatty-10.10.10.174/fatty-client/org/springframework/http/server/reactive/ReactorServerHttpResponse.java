/*     */ package org.springframework.http.server.reactive;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.handler.codec.http.cookie.Cookie;
/*     */ import io.netty.handler.codec.http.cookie.DefaultCookie;
/*     */ import java.nio.file.Path;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.DataBufferFactory;
/*     */ import org.springframework.core.io.buffer.NettyDataBufferFactory;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.ResponseCookie;
/*     */ import org.springframework.http.ZeroCopyHttpOutputMessage;
/*     */ import org.springframework.util.Assert;
/*     */ import reactor.core.publisher.Flux;
/*     */ import reactor.core.publisher.Mono;
/*     */ import reactor.netty.http.server.HttpServerResponse;
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
/*     */ class ReactorServerHttpResponse
/*     */   extends AbstractServerHttpResponse
/*     */   implements ZeroCopyHttpOutputMessage
/*     */ {
/*     */   private final HttpServerResponse response;
/*     */   
/*     */   public ReactorServerHttpResponse(HttpServerResponse response, DataBufferFactory bufferFactory) {
/*  51 */     super(bufferFactory, new HttpHeaders(new NettyHeadersAdapter(response.responseHeaders())));
/*  52 */     Assert.notNull(response, "HttpServerResponse must not be null");
/*  53 */     this.response = response;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getNativeResponse() {
/*  60 */     return (T)this.response;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpStatus getStatusCode() {
/*  65 */     HttpStatus httpStatus = super.getStatusCode();
/*  66 */     return (httpStatus != null) ? httpStatus : HttpStatus.resolve(this.response.status().code());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void applyStatusCode() {
/*  72 */     Integer statusCode = getStatusCodeValue();
/*  73 */     if (statusCode != null) {
/*  74 */       this.response.status(statusCode.intValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected Mono<Void> writeWithInternal(Publisher<? extends DataBuffer> publisher) {
/*  80 */     return this.response.send(toByteBufs(publisher)).then();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Mono<Void> writeAndFlushWithInternal(Publisher<? extends Publisher<? extends DataBuffer>> publisher) {
/*  85 */     return this.response.sendGroups((Publisher)Flux.from(publisher).map(this::toByteBufs)).then();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void applyHeaders() {}
/*     */ 
/*     */   
/*     */   protected void applyCookies() {
/*  94 */     for (String name : getCookies().keySet()) {
/*  95 */       for (ResponseCookie httpCookie : getCookies().get(name)) {
/*  96 */         DefaultCookie defaultCookie = new DefaultCookie(name, httpCookie.getValue());
/*  97 */         if (!httpCookie.getMaxAge().isNegative()) {
/*  98 */           defaultCookie.setMaxAge(httpCookie.getMaxAge().getSeconds());
/*     */         }
/* 100 */         if (httpCookie.getDomain() != null) {
/* 101 */           defaultCookie.setDomain(httpCookie.getDomain());
/*     */         }
/* 103 */         if (httpCookie.getPath() != null) {
/* 104 */           defaultCookie.setPath(httpCookie.getPath());
/*     */         }
/* 106 */         defaultCookie.setSecure(httpCookie.isSecure());
/* 107 */         defaultCookie.setHttpOnly(httpCookie.isHttpOnly());
/* 108 */         this.response.addCookie((Cookie)defaultCookie);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Mono<Void> writeWith(Path file, long position, long count) {
/* 115 */     return doCommit(() -> this.response.sendFile(file, position, count).then());
/*     */   }
/*     */   
/*     */   private Publisher<ByteBuf> toByteBufs(Publisher<? extends DataBuffer> dataBuffers) {
/* 119 */     return (dataBuffers instanceof Mono) ? 
/* 120 */       (Publisher<ByteBuf>)Mono.from(dataBuffers).map(NettyDataBufferFactory::toByteBuf) : 
/* 121 */       (Publisher<ByteBuf>)Flux.from(dataBuffers).map(NettyDataBufferFactory::toByteBuf);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/reactive/ReactorServerHttpResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */