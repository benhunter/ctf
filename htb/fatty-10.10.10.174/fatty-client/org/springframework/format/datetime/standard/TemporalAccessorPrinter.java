/*    */ package org.springframework.format.datetime.standard;
/*    */ 
/*    */ import java.time.format.DateTimeFormatter;
/*    */ import java.time.temporal.TemporalAccessor;
/*    */ import java.util.Locale;
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
/*    */ 
/*    */ 
/*    */ public final class TemporalAccessorPrinter
/*    */   implements Printer<TemporalAccessor>
/*    */ {
/*    */   private final DateTimeFormatter formatter;
/*    */   
/*    */   public TemporalAccessorPrinter(DateTimeFormatter formatter) {
/* 44 */     this.formatter = formatter;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String print(TemporalAccessor partial, Locale locale) {
/* 50 */     return DateTimeContextHolder.getFormatter(this.formatter, locale).format(partial);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/datetime/standard/TemporalAccessorPrinter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */