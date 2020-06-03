/*     */ package org.springframework.web.server;
/*     */ 
/*     */ import java.time.Instant;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.i18n.LocaleContext;
/*     */ import org.springframework.http.codec.multipart.Part;
/*     */ import org.springframework.http.server.reactive.ServerHttpRequest;
/*     */ import org.springframework.http.server.reactive.ServerHttpResponse;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServerWebExchangeDecorator
/*     */   implements ServerWebExchange
/*     */ {
/*     */   private final ServerWebExchange delegate;
/*     */   
/*     */   protected ServerWebExchangeDecorator(ServerWebExchange delegate) {
/*  54 */     Assert.notNull(delegate, "ServerWebExchange 'delegate' is required.");
/*  55 */     this.delegate = delegate;
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerWebExchange getDelegate() {
/*  60 */     return this.delegate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServerHttpRequest getRequest() {
/*  67 */     return getDelegate().getRequest();
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerHttpResponse getResponse() {
/*  72 */     return getDelegate().getResponse();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Object> getAttributes() {
/*  77 */     return getDelegate().getAttributes();
/*     */   }
/*     */ 
/*     */   
/*     */   public Mono<WebSession> getSession() {
/*  82 */     return getDelegate().getSession();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends java.security.Principal> Mono<T> getPrincipal() {
/*  87 */     return getDelegate().getPrincipal();
/*     */   }
/*     */ 
/*     */   
/*     */   public LocaleContext getLocaleContext() {
/*  92 */     return getDelegate().getLocaleContext();
/*     */   }
/*     */ 
/*     */   
/*     */   public ApplicationContext getApplicationContext() {
/*  97 */     return getDelegate().getApplicationContext();
/*     */   }
/*     */ 
/*     */   
/*     */   public Mono<MultiValueMap<String, String>> getFormData() {
/* 102 */     return getDelegate().getFormData();
/*     */   }
/*     */ 
/*     */   
/*     */   public Mono<MultiValueMap<String, Part>> getMultipartData() {
/* 107 */     return getDelegate().getMultipartData();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isNotModified() {
/* 112 */     return getDelegate().isNotModified();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkNotModified(Instant lastModified) {
/* 117 */     return getDelegate().checkNotModified(lastModified);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkNotModified(String etag) {
/* 122 */     return getDelegate().checkNotModified(etag);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkNotModified(@Nullable String etag, Instant lastModified) {
/* 127 */     return getDelegate().checkNotModified(etag, lastModified);
/*     */   }
/*     */ 
/*     */   
/*     */   public String transformUrl(String url) {
/* 132 */     return getDelegate().transformUrl(url);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addUrlTransformer(Function<String, String> transformer) {
/* 137 */     getDelegate().addUrlTransformer(transformer);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLogPrefix() {
/* 142 */     return getDelegate().getLogPrefix();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 147 */     return getClass().getSimpleName() + " [delegate=" + getDelegate() + "]";
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/server/ServerWebExchangeDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */