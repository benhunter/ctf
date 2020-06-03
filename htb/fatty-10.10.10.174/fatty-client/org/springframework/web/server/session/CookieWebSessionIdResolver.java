/*     */ package org.springframework.web.server.session;
/*     */ 
/*     */ import java.time.Duration;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Collectors;
/*     */ import org.springframework.http.HttpCookie;
/*     */ import org.springframework.http.ResponseCookie;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.web.server.ServerWebExchange;
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
/*     */ public class CookieWebSessionIdResolver
/*     */   implements WebSessionIdResolver
/*     */ {
/*  41 */   private String cookieName = "SESSION";
/*     */   
/*  43 */   private Duration cookieMaxAge = Duration.ofSeconds(-1L);
/*     */   @Nullable
/*  45 */   private Consumer<ResponseCookie.ResponseCookieBuilder> cookieInitializer = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCookieName(String cookieName) {
/*  55 */     Assert.hasText(cookieName, "'cookieName' must not be empty");
/*  56 */     this.cookieName = cookieName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCookieName() {
/*  63 */     return this.cookieName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCookieMaxAge(Duration maxAge) {
/*  73 */     this.cookieMaxAge = maxAge;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Duration getCookieMaxAge() {
/*  80 */     return this.cookieMaxAge;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCookieInitializer(Consumer<ResponseCookie.ResponseCookieBuilder> initializer) {
/*  90 */     this
/*  91 */       .cookieInitializer = (this.cookieInitializer != null) ? this.cookieInitializer.andThen(initializer) : initializer;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> resolveSessionIds(ServerWebExchange exchange) {
/*  97 */     MultiValueMap<String, HttpCookie> cookieMap = exchange.getRequest().getCookies();
/*  98 */     List<HttpCookie> cookies = (List<HttpCookie>)cookieMap.get(getCookieName());
/*  99 */     if (cookies == null) {
/* 100 */       return Collections.emptyList();
/*     */     }
/* 102 */     return (List<String>)cookies.stream().map(HttpCookie::getValue).collect(Collectors.toList());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSessionId(ServerWebExchange exchange, String id) {
/* 107 */     Assert.notNull(id, "'id' is required");
/* 108 */     ResponseCookie cookie = initSessionCookie(exchange, id, getCookieMaxAge());
/* 109 */     exchange.getResponse().getCookies().set(this.cookieName, cookie);
/*     */   }
/*     */ 
/*     */   
/*     */   public void expireSession(ServerWebExchange exchange) {
/* 114 */     ResponseCookie cookie = initSessionCookie(exchange, "", Duration.ZERO);
/* 115 */     exchange.getResponse().getCookies().set(this.cookieName, cookie);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ResponseCookie initSessionCookie(ServerWebExchange exchange, String id, Duration maxAge) {
/* 126 */     ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from(this.cookieName, id).path(exchange.getRequest().getPath().contextPath().value() + "/").maxAge(maxAge).httpOnly(true).secure("https".equalsIgnoreCase(exchange.getRequest().getURI().getScheme())).sameSite("Lax");
/*     */     
/* 128 */     if (this.cookieInitializer != null) {
/* 129 */       this.cookieInitializer.accept(cookieBuilder);
/*     */     }
/*     */     
/* 132 */     return cookieBuilder.build();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/server/session/CookieWebSessionIdResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */