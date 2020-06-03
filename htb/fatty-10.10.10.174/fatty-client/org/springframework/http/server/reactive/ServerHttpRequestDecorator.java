/*     */ package org.springframework.http.server.reactive;
/*     */ 
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.URI;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.http.HttpCookie;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.server.RequestPath;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ public class ServerHttpRequestDecorator
/*     */   implements ServerHttpRequest
/*     */ {
/*     */   private final ServerHttpRequest delegate;
/*     */   
/*     */   public ServerHttpRequestDecorator(ServerHttpRequest delegate) {
/*  46 */     Assert.notNull(delegate, "Delegate is required");
/*  47 */     this.delegate = delegate;
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerHttpRequest getDelegate() {
/*  52 */     return this.delegate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/*  60 */     return getDelegate().getId();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public HttpMethod getMethod() {
/*  66 */     return getDelegate().getMethod();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMethodValue() {
/*  71 */     return getDelegate().getMethodValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public URI getURI() {
/*  76 */     return getDelegate().getURI();
/*     */   }
/*     */ 
/*     */   
/*     */   public RequestPath getPath() {
/*  81 */     return getDelegate().getPath();
/*     */   }
/*     */ 
/*     */   
/*     */   public MultiValueMap<String, String> getQueryParams() {
/*  86 */     return getDelegate().getQueryParams();
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHeaders getHeaders() {
/*  91 */     return getDelegate().getHeaders();
/*     */   }
/*     */ 
/*     */   
/*     */   public MultiValueMap<String, HttpCookie> getCookies() {
/*  96 */     return getDelegate().getCookies();
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress getRemoteAddress() {
/* 101 */     return getDelegate().getRemoteAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public SslInfo getSslInfo() {
/* 107 */     return getDelegate().getSslInfo();
/*     */   }
/*     */ 
/*     */   
/*     */   public Flux<DataBuffer> getBody() {
/* 112 */     return getDelegate().getBody();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 118 */     return getClass().getSimpleName() + " [delegate=" + getDelegate() + "]";
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/reactive/ServerHttpRequestDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */