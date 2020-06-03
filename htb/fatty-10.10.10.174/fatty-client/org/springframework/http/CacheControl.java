/*     */ package org.springframework.http;
/*     */ 
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class CacheControl
/*     */ {
/*  53 */   private long maxAge = -1L;
/*     */   
/*     */   private boolean noCache = false;
/*     */   
/*     */   private boolean noStore = false;
/*     */   
/*     */   private boolean mustRevalidate = false;
/*     */   
/*     */   private boolean noTransform = false;
/*     */   
/*     */   private boolean cachePublic = false;
/*     */   
/*     */   private boolean cachePrivate = false;
/*     */   
/*     */   private boolean proxyRevalidate = false;
/*     */   
/*  69 */   private long staleWhileRevalidate = -1L;
/*     */   
/*  71 */   private long staleIfError = -1L;
/*     */   
/*  73 */   private long sMaxAge = -1L;
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
/*     */   public static CacheControl empty() {
/*  91 */     return new CacheControl();
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
/*     */   public static CacheControl maxAge(long maxAge, TimeUnit unit) {
/* 109 */     CacheControl cc = new CacheControl();
/* 110 */     cc.maxAge = unit.toSeconds(maxAge);
/* 111 */     return cc;
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
/*     */   public static CacheControl noCache() {
/* 127 */     CacheControl cc = new CacheControl();
/* 128 */     cc.noCache = true;
/* 129 */     return cc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CacheControl noStore() {
/* 140 */     CacheControl cc = new CacheControl();
/* 141 */     cc.noStore = true;
/* 142 */     return cc;
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
/*     */   public CacheControl mustRevalidate() {
/* 155 */     this.mustRevalidate = true;
/* 156 */     return this;
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
/*     */   public CacheControl noTransform() {
/* 168 */     this.noTransform = true;
/* 169 */     return this;
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
/*     */   public CacheControl cachePublic() {
/* 181 */     this.cachePublic = true;
/* 182 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CacheControl cachePrivate() {
/* 193 */     this.cachePrivate = true;
/* 194 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CacheControl proxyRevalidate() {
/* 205 */     this.proxyRevalidate = true;
/* 206 */     return this;
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
/*     */   public CacheControl sMaxAge(long sMaxAge, TimeUnit unit) {
/* 219 */     this.sMaxAge = unit.toSeconds(sMaxAge);
/* 220 */     return this;
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
/*     */   public CacheControl staleWhileRevalidate(long staleWhileRevalidate, TimeUnit unit) {
/* 236 */     this.staleWhileRevalidate = unit.toSeconds(staleWhileRevalidate);
/* 237 */     return this;
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
/*     */   public CacheControl staleIfError(long staleIfError, TimeUnit unit) {
/* 250 */     this.staleIfError = unit.toSeconds(staleIfError);
/* 251 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getHeaderValue() {
/* 261 */     String headerValue = toHeaderValue();
/* 262 */     return StringUtils.hasText(headerValue) ? headerValue : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String toHeaderValue() {
/* 270 */     StringBuilder headerValue = new StringBuilder();
/* 271 */     if (this.maxAge != -1L) {
/* 272 */       appendDirective(headerValue, "max-age=" + this.maxAge);
/*     */     }
/* 274 */     if (this.noCache) {
/* 275 */       appendDirective(headerValue, "no-cache");
/*     */     }
/* 277 */     if (this.noStore) {
/* 278 */       appendDirective(headerValue, "no-store");
/*     */     }
/* 280 */     if (this.mustRevalidate) {
/* 281 */       appendDirective(headerValue, "must-revalidate");
/*     */     }
/* 283 */     if (this.noTransform) {
/* 284 */       appendDirective(headerValue, "no-transform");
/*     */     }
/* 286 */     if (this.cachePublic) {
/* 287 */       appendDirective(headerValue, "public");
/*     */     }
/* 289 */     if (this.cachePrivate) {
/* 290 */       appendDirective(headerValue, "private");
/*     */     }
/* 292 */     if (this.proxyRevalidate) {
/* 293 */       appendDirective(headerValue, "proxy-revalidate");
/*     */     }
/* 295 */     if (this.sMaxAge != -1L) {
/* 296 */       appendDirective(headerValue, "s-maxage=" + this.sMaxAge);
/*     */     }
/* 298 */     if (this.staleIfError != -1L) {
/* 299 */       appendDirective(headerValue, "stale-if-error=" + this.staleIfError);
/*     */     }
/* 301 */     if (this.staleWhileRevalidate != -1L) {
/* 302 */       appendDirective(headerValue, "stale-while-revalidate=" + this.staleWhileRevalidate);
/*     */     }
/* 304 */     return headerValue.toString();
/*     */   }
/*     */   
/*     */   private void appendDirective(StringBuilder builder, String value) {
/* 308 */     if (builder.length() > 0) {
/* 309 */       builder.append(", ");
/*     */     }
/* 311 */     builder.append(value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 317 */     return "CacheControl [" + toHeaderValue() + "]";
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/CacheControl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */