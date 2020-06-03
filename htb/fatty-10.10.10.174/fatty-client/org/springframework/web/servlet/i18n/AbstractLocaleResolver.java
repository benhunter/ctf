/*    */ package org.springframework.web.servlet.i18n;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.web.servlet.LocaleResolver;
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
/*    */ public abstract class AbstractLocaleResolver
/*    */   implements LocaleResolver
/*    */ {
/*    */   @Nullable
/*    */   private Locale defaultLocale;
/*    */   
/*    */   public void setDefaultLocale(@Nullable Locale defaultLocale) {
/* 42 */     this.defaultLocale = defaultLocale;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   protected Locale getDefaultLocale() {
/* 50 */     return this.defaultLocale;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/i18n/AbstractLocaleResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */