/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import javax.servlet.http.Cookie;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CookieGenerator
/*     */ {
/*     */   public static final String DEFAULT_COOKIE_PATH = "/";
/*  51 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   @Nullable
/*     */   private String cookieName;
/*     */   
/*     */   @Nullable
/*     */   private String cookieDomain;
/*     */   
/*  59 */   private String cookiePath = "/";
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Integer cookieMaxAge;
/*     */ 
/*     */   
/*     */   private boolean cookieSecure = false;
/*     */ 
/*     */   
/*     */   private boolean cookieHttpOnly = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCookieName(@Nullable String cookieName) {
/*  74 */     this.cookieName = cookieName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getCookieName() {
/*  82 */     return this.cookieName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCookieDomain(@Nullable String cookieDomain) {
/*  91 */     this.cookieDomain = cookieDomain;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getCookieDomain() {
/*  99 */     return this.cookieDomain;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCookiePath(String cookiePath) {
/* 108 */     this.cookiePath = cookiePath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCookiePath() {
/* 115 */     return this.cookiePath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCookieMaxAge(@Nullable Integer cookieMaxAge) {
/* 126 */     this.cookieMaxAge = cookieMaxAge;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Integer getCookieMaxAge() {
/* 134 */     return this.cookieMaxAge;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCookieSecure(boolean cookieSecure) {
/* 145 */     this.cookieSecure = cookieSecure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCookieSecure() {
/* 153 */     return this.cookieSecure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCookieHttpOnly(boolean cookieHttpOnly) {
/* 162 */     this.cookieHttpOnly = cookieHttpOnly;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCookieHttpOnly() {
/* 169 */     return this.cookieHttpOnly;
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
/*     */   public void addCookie(HttpServletResponse response, String cookieValue) {
/* 185 */     Assert.notNull(response, "HttpServletResponse must not be null");
/* 186 */     Cookie cookie = createCookie(cookieValue);
/* 187 */     Integer maxAge = getCookieMaxAge();
/* 188 */     if (maxAge != null) {
/* 189 */       cookie.setMaxAge(maxAge.intValue());
/*     */     }
/* 191 */     if (isCookieSecure()) {
/* 192 */       cookie.setSecure(true);
/*     */     }
/* 194 */     if (isCookieHttpOnly()) {
/* 195 */       cookie.setHttpOnly(true);
/*     */     }
/* 197 */     response.addCookie(cookie);
/* 198 */     if (this.logger.isTraceEnabled()) {
/* 199 */       this.logger.trace("Added cookie [" + getCookieName() + "=" + cookieValue + "]");
/*     */     }
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
/*     */   public void removeCookie(HttpServletResponse response) {
/* 213 */     Assert.notNull(response, "HttpServletResponse must not be null");
/* 214 */     Cookie cookie = createCookie("");
/* 215 */     cookie.setMaxAge(0);
/* 216 */     if (isCookieSecure()) {
/* 217 */       cookie.setSecure(true);
/*     */     }
/* 219 */     if (isCookieHttpOnly()) {
/* 220 */       cookie.setHttpOnly(true);
/*     */     }
/* 222 */     response.addCookie(cookie);
/* 223 */     if (this.logger.isTraceEnabled()) {
/* 224 */       this.logger.trace("Removed cookie '" + getCookieName() + "'");
/*     */     }
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
/*     */   protected Cookie createCookie(String cookieValue) {
/* 238 */     Cookie cookie = new Cookie(getCookieName(), cookieValue);
/* 239 */     if (getCookieDomain() != null) {
/* 240 */       cookie.setDomain(getCookieDomain());
/*     */     }
/* 242 */     cookie.setPath(getCookiePath());
/* 243 */     return cookie;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/CookieGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */