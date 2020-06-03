/*    */ package org.springframework.format.datetime.joda;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import org.joda.time.format.DateTimeFormatter;
/*    */ import org.springframework.format.Printer;
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
/*    */ public final class MillisecondInstantPrinter
/*    */   implements Printer<Long>
/*    */ {
/*    */   private final DateTimeFormatter formatter;
/*    */   
/*    */   public MillisecondInstantPrinter(DateTimeFormatter formatter) {
/* 41 */     this.formatter = formatter;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String print(Long instant, Locale locale) {
/* 47 */     return JodaTimeContextHolder.getFormatter(this.formatter, locale).print(instant.longValue());
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/datetime/joda/MillisecondInstantPrinter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */