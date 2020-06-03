/*     */ package org.springframework.http.server.reactive;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.DataBufferFactory;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.ResponseCookie;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ public class ServerHttpResponseDecorator
/*     */   implements ServerHttpResponse
/*     */ {
/*     */   private final ServerHttpResponse delegate;
/*     */   
/*     */   public ServerHttpResponseDecorator(ServerHttpResponse delegate) {
/*  45 */     Assert.notNull(delegate, "Delegate is required");
/*  46 */     this.delegate = delegate;
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerHttpResponse getDelegate() {
/*  51 */     return this.delegate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setStatusCode(@Nullable HttpStatus status) {
/*  59 */     return getDelegate().setStatusCode(status);
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpStatus getStatusCode() {
/*  64 */     return getDelegate().getStatusCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHeaders getHeaders() {
/*  69 */     return getDelegate().getHeaders();
/*     */   }
/*     */ 
/*     */   
/*     */   public MultiValueMap<String, ResponseCookie> getCookies() {
/*  74 */     return getDelegate().getCookies();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addCookie(ResponseCookie cookie) {
/*  79 */     getDelegate().addCookie(cookie);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataBufferFactory bufferFactory() {
/*  84 */     return getDelegate().bufferFactory();
/*     */   }
/*     */ 
/*     */   
/*     */   public void beforeCommit(Supplier<? extends Mono<Void>> action) {
/*  89 */     getDelegate().beforeCommit(action);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCommitted() {
/*  94 */     return getDelegate().isCommitted();
/*     */   }
/*     */ 
/*     */   
/*     */   public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
/*  99 */     return getDelegate().writeWith(body);
/*     */   }
/*     */ 
/*     */   
/*     */   public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
/* 104 */     return getDelegate().writeAndFlushWith(body);
/*     */   }
/*     */ 
/*     */   
/*     */   public Mono<Void> setComplete() {
/* 109 */     return getDelegate().setComplete();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 115 */     return getClass().getSimpleName() + " [delegate=" + getDelegate() + "]";
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/reactive/ServerHttpResponseDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */