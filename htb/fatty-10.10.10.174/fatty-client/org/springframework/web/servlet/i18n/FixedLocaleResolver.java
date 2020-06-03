/*     */ package org.springframework.web.servlet.i18n;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.context.i18n.LocaleContext;
/*     */ import org.springframework.context.i18n.TimeZoneAwareLocaleContext;
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
/*     */ public class FixedLocaleResolver
/*     */   extends AbstractLocaleContextResolver
/*     */ {
/*     */   public FixedLocaleResolver() {
/*  50 */     setDefaultLocale(Locale.getDefault());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FixedLocaleResolver(Locale locale) {
/*  58 */     setDefaultLocale(locale);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FixedLocaleResolver(Locale locale, TimeZone timeZone) {
/*  67 */     setDefaultLocale(locale);
/*  68 */     setDefaultTimeZone(timeZone);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Locale resolveLocale(HttpServletRequest request) {
/*  74 */     Locale locale = getDefaultLocale();
/*  75 */     if (locale == null) {
/*  76 */       locale = Locale.getDefault();
/*     */     }
/*  78 */     return locale;
/*     */   }
/*     */ 
/*     */   
/*     */   public LocaleContext resolveLocaleContext(HttpServletRequest request) {
/*  83 */     return (LocaleContext)new TimeZoneAwareLocaleContext()
/*     */       {
/*     */         @Nullable
/*     */         public Locale getLocale() {
/*  87 */           return FixedLocaleResolver.this.getDefaultLocale();
/*     */         }
/*     */         
/*     */         public TimeZone getTimeZone() {
/*  91 */           return FixedLocaleResolver.this.getDefaultTimeZone();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocaleContext(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable LocaleContext localeContext) {
/* 100 */     throw new UnsupportedOperationException("Cannot change fixed locale - use a different locale resolution strategy");
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/i18n/FixedLocaleResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */