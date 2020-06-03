/*     */ package org.springframework.web.servlet.i18n;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ import javax.servlet.http.Cookie;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.context.i18n.LocaleContext;
/*     */ import org.springframework.context.i18n.SimpleLocaleContext;
/*     */ import org.springframework.context.i18n.TimeZoneAwareLocaleContext;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.servlet.LocaleContextResolver;
/*     */ import org.springframework.web.util.CookieGenerator;
/*     */ import org.springframework.web.util.WebUtils;
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
/*     */ 
/*     */ public class CookieLocaleResolver
/*     */   extends CookieGenerator
/*     */   implements LocaleContextResolver
/*     */ {
/*  67 */   public static final String LOCALE_REQUEST_ATTRIBUTE_NAME = CookieLocaleResolver.class.getName() + ".LOCALE";
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
/*  78 */   public static final String TIME_ZONE_REQUEST_ATTRIBUTE_NAME = CookieLocaleResolver.class.getName() + ".TIME_ZONE";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   public static final String DEFAULT_COOKIE_NAME = CookieLocaleResolver.class.getName() + ".LOCALE";
/*     */ 
/*     */   
/*     */   private boolean languageTagCompliant = true;
/*     */ 
/*     */   
/*     */   private boolean rejectInvalidCookies = true;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Locale defaultLocale;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private TimeZone defaultTimeZone;
/*     */ 
/*     */ 
/*     */   
/*     */   public CookieLocaleResolver() {
/* 102 */     setCookieName(DEFAULT_COOKIE_NAME);
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
/*     */   public void setLanguageTagCompliant(boolean languageTagCompliant) {
/* 120 */     this.languageTagCompliant = languageTagCompliant;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLanguageTagCompliant() {
/* 129 */     return this.languageTagCompliant;
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
/*     */   public void setRejectInvalidCookies(boolean rejectInvalidCookies) {
/* 143 */     this.rejectInvalidCookies = rejectInvalidCookies;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRejectInvalidCookies() {
/* 151 */     return this.rejectInvalidCookies;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultLocale(@Nullable Locale defaultLocale) {
/* 158 */     this.defaultLocale = defaultLocale;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Locale getDefaultLocale() {
/* 167 */     return this.defaultLocale;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultTimeZone(@Nullable TimeZone defaultTimeZone) {
/* 175 */     this.defaultTimeZone = defaultTimeZone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected TimeZone getDefaultTimeZone() {
/* 185 */     return this.defaultTimeZone;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Locale resolveLocale(HttpServletRequest request) {
/* 191 */     parseLocaleCookieIfNecessary(request);
/* 192 */     return (Locale)request.getAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME);
/*     */   }
/*     */ 
/*     */   
/*     */   public LocaleContext resolveLocaleContext(final HttpServletRequest request) {
/* 197 */     parseLocaleCookieIfNecessary(request);
/* 198 */     return (LocaleContext)new TimeZoneAwareLocaleContext()
/*     */       {
/*     */         @Nullable
/*     */         public Locale getLocale() {
/* 202 */           return (Locale)request.getAttribute(CookieLocaleResolver.LOCALE_REQUEST_ATTRIBUTE_NAME);
/*     */         }
/*     */         
/*     */         @Nullable
/*     */         public TimeZone getTimeZone() {
/* 207 */           return (TimeZone)request.getAttribute(CookieLocaleResolver.TIME_ZONE_REQUEST_ATTRIBUTE_NAME);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private void parseLocaleCookieIfNecessary(HttpServletRequest request) {
/* 213 */     if (request.getAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME) == null) {
/* 214 */       Locale locale = null;
/* 215 */       TimeZone timeZone = null;
/*     */ 
/*     */       
/* 218 */       String cookieName = getCookieName();
/* 219 */       if (cookieName != null) {
/* 220 */         Cookie cookie = WebUtils.getCookie(request, cookieName);
/* 221 */         if (cookie != null) {
/* 222 */           String value = cookie.getValue();
/* 223 */           String localePart = value;
/* 224 */           String timeZonePart = null;
/* 225 */           int separatorIndex = localePart.indexOf('/');
/* 226 */           if (separatorIndex == -1)
/*     */           {
/* 228 */             separatorIndex = localePart.indexOf(' ');
/*     */           }
/* 230 */           if (separatorIndex >= 0) {
/* 231 */             localePart = value.substring(0, separatorIndex);
/* 232 */             timeZonePart = value.substring(separatorIndex + 1);
/*     */           } 
/*     */           try {
/* 235 */             locale = !"-".equals(localePart) ? parseLocaleValue(localePart) : null;
/* 236 */             if (timeZonePart != null) {
/* 237 */               timeZone = StringUtils.parseTimeZoneString(timeZonePart);
/*     */             }
/*     */           }
/* 240 */           catch (IllegalArgumentException ex) {
/* 241 */             if (isRejectInvalidCookies() && request
/* 242 */               .getAttribute("javax.servlet.error.exception") == null) {
/* 243 */               throw new IllegalStateException("Encountered invalid locale cookie '" + cookieName + "': [" + value + "] due to: " + ex
/* 244 */                   .getMessage());
/*     */             }
/*     */ 
/*     */             
/* 248 */             if (this.logger.isDebugEnabled()) {
/* 249 */               this.logger.debug("Ignoring invalid locale cookie '" + cookieName + "': [" + value + "] due to: " + ex
/* 250 */                   .getMessage());
/*     */             }
/*     */           } 
/*     */           
/* 254 */           if (this.logger.isTraceEnabled()) {
/* 255 */             this.logger.trace("Parsed cookie value [" + cookie.getValue() + "] into locale '" + locale + "'" + ((timeZone != null) ? (" and time zone '" + timeZone
/* 256 */                 .getID() + "'") : ""));
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 261 */       request.setAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME, (locale != null) ? locale : 
/* 262 */           determineDefaultLocale(request));
/* 263 */       request.setAttribute(TIME_ZONE_REQUEST_ATTRIBUTE_NAME, (timeZone != null) ? timeZone : 
/* 264 */           determineDefaultTimeZone(request));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLocale(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Locale locale) {
/* 270 */     setLocaleContext(request, response, (locale != null) ? (LocaleContext)new SimpleLocaleContext(locale) : null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocaleContext(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable LocaleContext localeContext) {
/* 277 */     Assert.notNull(response, "HttpServletResponse is required for CookieLocaleResolver");
/*     */     
/* 279 */     Locale locale = null;
/* 280 */     TimeZone timeZone = null;
/* 281 */     if (localeContext != null) {
/* 282 */       locale = localeContext.getLocale();
/* 283 */       if (localeContext instanceof TimeZoneAwareLocaleContext) {
/* 284 */         timeZone = ((TimeZoneAwareLocaleContext)localeContext).getTimeZone();
/*     */       }
/* 286 */       addCookie(response, ((locale != null) ? 
/* 287 */           toLocaleValue(locale) : "-") + ((timeZone != null) ? ('/' + timeZone.getID()) : ""));
/*     */     } else {
/*     */       
/* 290 */       removeCookie(response);
/*     */     } 
/* 292 */     request.setAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME, (locale != null) ? locale : 
/* 293 */         determineDefaultLocale(request));
/* 294 */     request.setAttribute(TIME_ZONE_REQUEST_ATTRIBUTE_NAME, (timeZone != null) ? timeZone : 
/* 295 */         determineDefaultTimeZone(request));
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
/*     */   @Nullable
/*     */   protected Locale parseLocaleValue(String localeValue) {
/* 310 */     return StringUtils.parseLocale(localeValue);
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
/*     */   protected String toLocaleValue(Locale locale) {
/* 324 */     return isLanguageTagCompliant() ? locale.toLanguageTag() : locale.toString();
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
/*     */   @Nullable
/*     */   protected Locale determineDefaultLocale(HttpServletRequest request) {
/* 339 */     Locale defaultLocale = getDefaultLocale();
/* 340 */     if (defaultLocale == null) {
/* 341 */       defaultLocale = request.getLocale();
/*     */     }
/* 343 */     return defaultLocale;
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
/*     */   @Nullable
/*     */   protected TimeZone determineDefaultTimeZone(HttpServletRequest request) {
/* 357 */     return getDefaultTimeZone();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/i18n/CookieLocaleResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */