/*    */ package org.springframework.format.datetime.standard;
/*    */ 
/*    */ import java.time.format.DateTimeFormatter;
/*    */ import java.util.Locale;
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
/*    */ public final class DateTimeContextHolder
/*    */ {
/* 34 */   private static final ThreadLocal<DateTimeContext> dateTimeContextHolder = (ThreadLocal<DateTimeContext>)new NamedThreadLocal("DateTimeContext");
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
/*    */   public static void resetDateTimeContext() {
/* 46 */     dateTimeContextHolder.remove();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void setDateTimeContext(@Nullable DateTimeContext dateTimeContext) {
/* 55 */     if (dateTimeContext == null) {
/* 56 */       resetDateTimeContext();
/*    */     } else {
/*    */       
/* 59 */       dateTimeContextHolder.set(dateTimeContext);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public static DateTimeContext getDateTimeContext() {
/* 69 */     return dateTimeContextHolder.get();
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
/* 81 */     DateTimeFormatter formatterToUse = (locale != null) ? formatter.withLocale(locale) : formatter;
/* 82 */     DateTimeContext context = getDateTimeContext();
/* 83 */     return (context != null) ? context.getFormatter(formatterToUse) : formatterToUse;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/datetime/standard/DateTimeContextHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */