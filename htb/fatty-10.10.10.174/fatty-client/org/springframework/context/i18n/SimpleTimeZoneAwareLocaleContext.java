/*    */ package org.springframework.context.i18n;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import java.util.TimeZone;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SimpleTimeZoneAwareLocaleContext
/*    */   extends SimpleLocaleContext
/*    */   implements TimeZoneAwareLocaleContext
/*    */ {
/*    */   @Nullable
/*    */   private final TimeZone timeZone;
/*    */   
/*    */   public SimpleTimeZoneAwareLocaleContext(@Nullable Locale locale, @Nullable TimeZone timeZone) {
/* 51 */     super(locale);
/* 52 */     this.timeZone = timeZone;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public TimeZone getTimeZone() {
/* 59 */     return this.timeZone;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 64 */     return super.toString() + " " + ((this.timeZone != null) ? this.timeZone.toString() : "-");
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/i18n/SimpleTimeZoneAwareLocaleContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */