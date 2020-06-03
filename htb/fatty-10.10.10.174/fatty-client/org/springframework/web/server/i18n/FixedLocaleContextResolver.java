/*    */ package org.springframework.web.server.i18n;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import java.util.TimeZone;
/*    */ import org.springframework.context.i18n.LocaleContext;
/*    */ import org.springframework.context.i18n.TimeZoneAwareLocaleContext;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.web.server.ServerWebExchange;
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
/*    */ 
/*    */ 
/*    */ public class FixedLocaleContextResolver
/*    */   implements LocaleContextResolver
/*    */ {
/*    */   private final Locale locale;
/*    */   @Nullable
/*    */   private final TimeZone timeZone;
/*    */   
/*    */   public FixedLocaleContextResolver() {
/* 51 */     this(Locale.getDefault());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FixedLocaleContextResolver(Locale locale) {
/* 59 */     this(locale, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FixedLocaleContextResolver(Locale locale, @Nullable TimeZone timeZone) {
/* 68 */     Assert.notNull(locale, "Locale must not be null");
/* 69 */     this.locale = locale;
/* 70 */     this.timeZone = timeZone;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public LocaleContext resolveLocaleContext(ServerWebExchange exchange) {
/* 76 */     return (LocaleContext)new TimeZoneAwareLocaleContext()
/*    */       {
/*    */         public Locale getLocale() {
/* 79 */           return FixedLocaleContextResolver.this.locale;
/*    */         }
/*    */         
/*    */         @Nullable
/*    */         public TimeZone getTimeZone() {
/* 84 */           return FixedLocaleContextResolver.this.timeZone;
/*    */         }
/*    */       };
/*    */   }
/*    */ 
/*    */   
/*    */   public void setLocaleContext(ServerWebExchange exchange, @Nullable LocaleContext localeContext) {
/* 91 */     throw new UnsupportedOperationException("Cannot change fixed locale - use a different locale context resolution strategy");
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/server/i18n/FixedLocaleContextResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */