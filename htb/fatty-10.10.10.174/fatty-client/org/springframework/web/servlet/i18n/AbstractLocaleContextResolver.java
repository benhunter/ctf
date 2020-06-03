/*    */ package org.springframework.web.servlet.i18n;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import java.util.TimeZone;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.context.i18n.LocaleContext;
/*    */ import org.springframework.context.i18n.SimpleLocaleContext;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.web.servlet.LocaleContextResolver;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractLocaleContextResolver
/*    */   extends AbstractLocaleResolver
/*    */   implements LocaleContextResolver
/*    */ {
/*    */   @Nullable
/*    */   private TimeZone defaultTimeZone;
/*    */   
/*    */   public void setDefaultTimeZone(@Nullable TimeZone defaultTimeZone) {
/* 50 */     this.defaultTimeZone = defaultTimeZone;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public TimeZone getDefaultTimeZone() {
/* 58 */     return this.defaultTimeZone;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Locale resolveLocale(HttpServletRequest request) {
/* 64 */     Locale locale = resolveLocaleContext(request).getLocale();
/* 65 */     return (locale != null) ? locale : request.getLocale();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setLocale(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Locale locale) {
/* 70 */     setLocaleContext(request, response, (locale != null) ? (LocaleContext)new SimpleLocaleContext(locale) : null);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/i18n/AbstractLocaleContextResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */