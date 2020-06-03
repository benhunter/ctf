/*    */ package org.springframework.context.i18n;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ public class SimpleLocaleContext
/*    */   implements LocaleContext
/*    */ {
/*    */   @Nullable
/*    */   private final Locale locale;
/*    */   
/*    */   public SimpleLocaleContext(@Nullable Locale locale) {
/* 45 */     this.locale = locale;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Locale getLocale() {
/* 51 */     return this.locale;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 56 */     return (this.locale != null) ? this.locale.toString() : "-";
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/i18n/SimpleLocaleContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */