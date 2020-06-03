/*    */ package org.springframework.format.datetime.joda;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import org.joda.time.ReadablePartial;
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
/*    */ public final class ReadablePartialPrinter
/*    */   implements Printer<ReadablePartial>
/*    */ {
/*    */   private final DateTimeFormatter formatter;
/*    */   
/*    */   public ReadablePartialPrinter(DateTimeFormatter formatter) {
/* 42 */     this.formatter = formatter;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String print(ReadablePartial partial, Locale locale) {
/* 48 */     return JodaTimeContextHolder.getFormatter(this.formatter, locale).print(partial);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/datetime/joda/ReadablePartialPrinter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */