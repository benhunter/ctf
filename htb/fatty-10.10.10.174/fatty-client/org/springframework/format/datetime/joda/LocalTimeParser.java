/*    */ package org.springframework.format.datetime.joda;
/*    */ 
/*    */ import java.text.ParseException;
/*    */ import java.util.Locale;
/*    */ import org.joda.time.LocalTime;
/*    */ import org.joda.time.format.DateTimeFormatter;
/*    */ import org.springframework.format.Parser;
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
/*    */ public final class LocalTimeParser
/*    */   implements Parser<LocalTime>
/*    */ {
/*    */   private final DateTimeFormatter formatter;
/*    */   
/*    */   public LocalTimeParser(DateTimeFormatter formatter) {
/* 44 */     this.formatter = formatter;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public LocalTime parse(String text, Locale locale) throws ParseException {
/* 50 */     return JodaTimeContextHolder.getFormatter(this.formatter, locale).parseLocalTime(text);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/datetime/joda/LocalTimeParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */