/*     */ package org.springframework.web.server.session;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.server.ServerWebExchange;
/*     */ import org.springframework.web.server.WebSession;
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
/*     */ public class DefaultWebSessionManager
/*     */   implements WebSessionManager
/*     */ {
/*  39 */   private WebSessionIdResolver sessionIdResolver = new CookieWebSessionIdResolver();
/*     */   
/*  41 */   private WebSessionStore sessionStore = new InMemoryWebSessionStore();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSessionIdResolver(WebSessionIdResolver sessionIdResolver) {
/*  50 */     Assert.notNull(sessionIdResolver, "WebSessionIdResolver is required");
/*  51 */     this.sessionIdResolver = sessionIdResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebSessionIdResolver getSessionIdResolver() {
/*  58 */     return this.sessionIdResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSessionStore(WebSessionStore sessionStore) {
/*  67 */     Assert.notNull(sessionStore, "WebSessionStore is required");
/*  68 */     this.sessionStore = sessionStore;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebSessionStore getSessionStore() {
/*  75 */     return this.sessionStore;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Mono<WebSession> getSession(ServerWebExchange exchange) {
/*  81 */     return Mono.defer(() -> retrieveSession(exchange).switchIfEmpty(this.sessionStore.createWebSession()).doOnNext(()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Mono<WebSession> retrieveSession(ServerWebExchange exchange) {
/*  87 */     return Flux.fromIterable(getSessionIdResolver().resolveSessionIds(exchange))
/*  88 */       .concatMap(this.sessionStore::retrieveSession)
/*  89 */       .next();
/*     */   }
/*     */   
/*     */   private Mono<Void> save(ServerWebExchange exchange, WebSession session) {
/*  93 */     List<String> ids = getSessionIdResolver().resolveSessionIds(exchange);
/*     */     
/*  95 */     if (!session.isStarted() || session.isExpired()) {
/*  96 */       if (!ids.isEmpty())
/*     */       {
/*  98 */         this.sessionIdResolver.expireSession(exchange);
/*     */       }
/* 100 */       return Mono.empty();
/*     */     } 
/*     */     
/* 103 */     if (ids.isEmpty() || !session.getId().equals(ids.get(0))) {
/* 104 */       this.sessionIdResolver.setSessionId(exchange, session.getId());
/*     */     }
/*     */     
/* 107 */     return session.save();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/server/session/DefaultWebSessionManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */