/*     */ package org.springframework.web.server;
/*     */ 
/*     */ import java.security.Principal;
/*     */ import java.util.function.Consumer;
/*     */ import org.springframework.http.server.reactive.ServerHttpRequest;
/*     */ import org.springframework.http.server.reactive.ServerHttpResponse;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ class DefaultServerWebExchangeBuilder
/*     */   implements ServerWebExchange.Builder
/*     */ {
/*     */   private final ServerWebExchange delegate;
/*     */   @Nullable
/*     */   private ServerHttpRequest request;
/*     */   @Nullable
/*     */   private ServerHttpResponse response;
/*     */   @Nullable
/*     */   private Mono<Principal> principalMono;
/*     */   
/*     */   DefaultServerWebExchangeBuilder(ServerWebExchange delegate) {
/*  50 */     Assert.notNull(delegate, "Delegate is required");
/*  51 */     this.delegate = delegate;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ServerWebExchange.Builder request(Consumer<ServerHttpRequest.Builder> consumer) {
/*  57 */     ServerHttpRequest.Builder builder = this.delegate.getRequest().mutate();
/*  58 */     consumer.accept(builder);
/*  59 */     return request(builder.build());
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerWebExchange.Builder request(ServerHttpRequest request) {
/*  64 */     this.request = request;
/*  65 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerWebExchange.Builder response(ServerHttpResponse response) {
/*  70 */     this.response = response;
/*  71 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerWebExchange.Builder principal(Mono<Principal> principalMono) {
/*  76 */     this.principalMono = principalMono;
/*  77 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerWebExchange build() {
/*  82 */     return new MutativeDecorator(this.delegate, this.request, this.response, this.principalMono);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class MutativeDecorator
/*     */     extends ServerWebExchangeDecorator
/*     */   {
/*     */     @Nullable
/*     */     private final ServerHttpRequest request;
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private final ServerHttpResponse response;
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private final Mono<Principal> principalMono;
/*     */ 
/*     */ 
/*     */     
/*     */     public MutativeDecorator(ServerWebExchange delegate, @Nullable ServerHttpRequest request, @Nullable ServerHttpResponse response, @Nullable Mono<Principal> principalMono) {
/* 104 */       super(delegate);
/* 105 */       this.request = request;
/* 106 */       this.response = response;
/* 107 */       this.principalMono = principalMono;
/*     */     }
/*     */ 
/*     */     
/*     */     public ServerHttpRequest getRequest() {
/* 112 */       return (this.request != null) ? this.request : getDelegate().getRequest();
/*     */     }
/*     */ 
/*     */     
/*     */     public ServerHttpResponse getResponse() {
/* 117 */       return (this.response != null) ? this.response : getDelegate().getResponse();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T extends Principal> Mono<T> getPrincipal() {
/* 123 */       return (this.principalMono != null) ? (Mono)this.principalMono : getDelegate().<T>getPrincipal();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/server/DefaultServerWebExchangeBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */