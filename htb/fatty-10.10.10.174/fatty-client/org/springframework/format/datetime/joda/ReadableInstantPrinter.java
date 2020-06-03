/*    */ package org.springframework.format.datetime.joda;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import org.joda.time.ReadableInstant;
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
/*    */ public final class ReadableInstantPrinter
/*    */   implements Printer<ReadableInstant>
/*    */ {
/*    */   private final DateTimeFormatter formatter;
/*    */   
/*    */   public ReadableInstantPrinter(DateTimeFormatter formatter) {
/* 42 */     this.formatter = formatter;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String print(ReadableInstant instant, Locale locale) {
/* 48 */     return JodaTimeContextHolder.getFormatter(this.formatter, locale).print(instant);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/datetime/joda/ReadableInstantPrinter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */