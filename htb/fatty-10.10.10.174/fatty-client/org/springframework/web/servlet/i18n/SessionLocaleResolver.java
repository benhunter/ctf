/*     */ package org.springframework.web.servlet.i18n;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.context.i18n.LocaleContext;
/*     */ import org.springframework.context.i18n.TimeZoneAwareLocaleContext;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SessionLocaleResolver
/*     */   extends AbstractLocaleContextResolver
/*     */ {
/*  70 */   public static final String LOCALE_SESSION_ATTRIBUTE_NAME = SessionLocaleResolver.class.getName() + ".LOCALE";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   public static final String TIME_ZONE_SESSION_ATTRIBUTE_NAME = SessionLocaleResolver.class.getName() + ".TIME_ZONE";
/*     */ 
/*     */   
/*  83 */   private String localeAttributeName = LOCALE_SESSION_ATTRIBUTE_NAME;
/*     */   
/*  85 */   private String timeZoneAttributeName = TIME_ZONE_SESSION_ATTRIBUTE_NAME;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocaleAttributeName(String localeAttributeName) {
/*  95 */     this.localeAttributeName = localeAttributeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeZoneAttributeName(String timeZoneAttributeName) {
/* 105 */     this.timeZoneAttributeName = timeZoneAttributeName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Locale resolveLocale(HttpServletRequest request) {
/* 111 */     Locale locale = (Locale)WebUtils.getSessionAttribute(request, this.localeAttributeName);
/* 112 */     if (locale == null) {
/* 113 */       locale = determineDefaultLocale(request);
/*     */     }
/* 115 */     return locale;
/*     */   }
/*     */ 
/*     */   
/*     */   public LocaleContext resolveLocaleContext(final HttpServletRequest request) {
/* 120 */     return (LocaleContext)new TimeZoneAwareLocaleContext()
/*     */       {
/*     */         public Locale getLocale() {
/* 123 */           Locale locale = (Locale)WebUtils.getSessionAttribute(request, SessionLocaleResolver.this.localeAttributeName);
/* 124 */           if (locale == null) {
/* 125 */             locale = SessionLocaleResolver.this.determineDefaultLocale(request);
/*     */           }
/* 127 */           return locale;
/*     */         }
/*     */         
/*     */         @Nullable
/*     */         public TimeZone getTimeZone() {
/* 132 */           TimeZone timeZone = (TimeZone)WebUtils.getSessionAttribute(request, SessionLocaleResolver.this.timeZoneAttributeName);
/* 133 */           if (timeZone == null) {
/* 134 */             timeZone = SessionLocaleResolver.this.determineDefaultTimeZone(request);
/*     */           }
/* 136 */           return timeZone;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocaleContext(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable LocaleContext localeContext) {
/* 145 */     Locale locale = null;
/* 146 */     TimeZone timeZone = null;
/* 147 */     if (localeContext != null) {
/* 148 */       locale = localeContext.getLocale();
/* 149 */       if (localeContext instanceof TimeZoneAwareLocaleContext) {
/* 150 */         timeZone = ((TimeZoneAwareLocaleContext)localeContext).getTimeZone();
/*     */       }
/*     */     } 
/* 153 */     WebUtils.setSessionAttribute(request, this.localeAttributeName, locale);
/* 154 */     WebUtils.setSessionAttribute(request, this.timeZoneAttributeName, timeZone);
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
/*     */   protected Locale determineDefaultLocale(HttpServletRequest request) {
/* 169 */     Locale defaultLocale = getDefaultLocale();
/* 170 */     if (defaultLocale == null) {
/* 171 */       defaultLocale = request.getLocale();
/*     */     }
/* 173 */     return defaultLocale;
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
/* 187 */     return getDefaultTimeZone();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/i18n/SessionLocaleResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */