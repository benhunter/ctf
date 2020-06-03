/*     */ package org.springframework.context.i18n;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ import org.springframework.core.NamedInheritableThreadLocal;
/*     */ import org.springframework.core.NamedThreadLocal;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class LocaleContextHolder
/*     */ {
/*  47 */   private static final ThreadLocal<LocaleContext> localeContextHolder = (ThreadLocal<LocaleContext>)new NamedThreadLocal("LocaleContext");
/*     */ 
/*     */   
/*  50 */   private static final ThreadLocal<LocaleContext> inheritableLocaleContextHolder = (ThreadLocal<LocaleContext>)new NamedInheritableThreadLocal("LocaleContext");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static Locale defaultLocale;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static TimeZone defaultTimeZone;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void resetLocaleContext() {
/*  70 */     localeContextHolder.remove();
/*  71 */     inheritableLocaleContextHolder.remove();
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
/*     */   public static void setLocaleContext(@Nullable LocaleContext localeContext) {
/*  85 */     setLocaleContext(localeContext, false);
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
/*     */   public static void setLocaleContext(@Nullable LocaleContext localeContext, boolean inheritable) {
/* 100 */     if (localeContext == null) {
/* 101 */       resetLocaleContext();
/*     */     
/*     */     }
/* 104 */     else if (inheritable) {
/* 105 */       inheritableLocaleContextHolder.set(localeContext);
/* 106 */       localeContextHolder.remove();
/*     */     } else {
/*     */       
/* 109 */       localeContextHolder.set(localeContext);
/* 110 */       inheritableLocaleContextHolder.remove();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static LocaleContext getLocaleContext() {
/* 121 */     LocaleContext localeContext = localeContextHolder.get();
/* 122 */     if (localeContext == null) {
/* 123 */       localeContext = inheritableLocaleContextHolder.get();
/*     */     }
/* 125 */     return localeContext;
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
/*     */   public static void setLocale(@Nullable Locale locale) {
/* 139 */     setLocale(locale, false);
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
/*     */   public static void setLocale(@Nullable Locale locale, boolean inheritable) {
/* 154 */     LocaleContext localeContext = getLocaleContext();
/*     */     
/* 156 */     TimeZone timeZone = (localeContext instanceof TimeZoneAwareLocaleContext) ? ((TimeZoneAwareLocaleContext)localeContext).getTimeZone() : null;
/* 157 */     if (timeZone != null) {
/* 158 */       localeContext = new SimpleTimeZoneAwareLocaleContext(locale, timeZone);
/*     */     }
/* 160 */     else if (locale != null) {
/* 161 */       localeContext = new SimpleLocaleContext(locale);
/*     */     } else {
/*     */       
/* 164 */       localeContext = null;
/*     */     } 
/* 166 */     setLocaleContext(localeContext, inheritable);
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
/*     */   public static void setDefaultLocale(@Nullable Locale locale) {
/* 184 */     defaultLocale = locale;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static Locale getLocale() {
/* 205 */     return getLocale(getLocaleContext());
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
/*     */   public static Locale getLocale(@Nullable LocaleContext localeContext) {
/* 223 */     if (localeContext != null) {
/* 224 */       Locale locale = localeContext.getLocale();
/* 225 */       if (locale != null) {
/* 226 */         return locale;
/*     */       }
/*     */     } 
/* 229 */     return (defaultLocale != null) ? defaultLocale : Locale.getDefault();
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
/*     */   public static void setTimeZone(@Nullable TimeZone timeZone) {
/* 243 */     setTimeZone(timeZone, false);
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
/*     */   public static void setTimeZone(@Nullable TimeZone timeZone, boolean inheritable) {
/* 258 */     LocaleContext localeContext = getLocaleContext();
/* 259 */     Locale locale = (localeContext != null) ? localeContext.getLocale() : null;
/* 260 */     if (timeZone != null) {
/* 261 */       localeContext = new SimpleTimeZoneAwareLocaleContext(locale, timeZone);
/*     */     }
/* 263 */     else if (locale != null) {
/* 264 */       localeContext = new SimpleLocaleContext(locale);
/*     */     } else {
/*     */       
/* 267 */       localeContext = null;
/*     */     } 
/* 269 */     setLocaleContext(localeContext, inheritable);
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
/*     */   public static void setDefaultTimeZone(@Nullable TimeZone timeZone) {
/* 287 */     defaultTimeZone = timeZone;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TimeZone getTimeZone() {
/* 309 */     return getTimeZone(getLocaleContext());
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
/*     */   public static TimeZone getTimeZone(@Nullable LocaleContext localeContext) {
/* 327 */     if (localeContext instanceof TimeZoneAwareLocaleContext) {
/* 328 */       TimeZone timeZone = ((TimeZoneAwareLocaleContext)localeContext).getTimeZone();
/* 329 */       if (timeZone != null) {
/* 330 */         return timeZone;
/*     */       }
/*     */     } 
/* 333 */     return (defaultTimeZone != null) ? defaultTimeZone : TimeZone.getDefault();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/i18n/LocaleContextHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */