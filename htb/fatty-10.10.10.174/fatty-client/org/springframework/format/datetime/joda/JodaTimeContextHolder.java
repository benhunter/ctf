/*    */ package org.springframework.format.datetime.joda;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import org.joda.time.format.DateTimeFormatter;
/*    */ import org.springframework.core.NamedThreadLocal;
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
/*    */ public final class JodaTimeContextHolder
/*    */ {
/* 37 */   private static final ThreadLocal<JodaTimeContext> jodaTimeContextHolder = (ThreadLocal<JodaTimeContext>)new NamedThreadLocal("JodaTimeContext");
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
/*    */   public static void resetJodaTimeContext() {
/* 49 */     jodaTimeContextHolder.remove();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void setJodaTimeContext(@Nullable JodaTimeContext jodaTimeContext) {
/* 58 */     if (jodaTimeContext == null) {
/* 59 */       resetJodaTimeContext();
/*    */     } else {
/*    */       
/* 62 */       jodaTimeContextHolder.set(jodaTimeContext);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public static JodaTimeContext getJodaTimeContext() {
/* 72 */     return jodaTimeContextHolder.get();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static DateTimeFormatter getFormatter(DateTimeFormatter formatter, @Nullable Locale locale) {
/* 84 */     DateTimeFormatter formatterToUse = (locale != null) ? formatter.withLocale(locale) : formatter;
/* 85 */     JodaTimeContext context = getJodaTimeContext();
/* 86 */     return (context != null) ? context.getFormatter(formatterToUse) : formatterToUse;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/datetime/joda/JodaTimeContextHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */