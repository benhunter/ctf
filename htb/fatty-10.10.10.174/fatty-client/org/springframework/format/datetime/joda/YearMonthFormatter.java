/*    */ package org.springframework.format.datetime.joda;
/*    */ 
/*    */ import java.text.ParseException;
/*    */ import java.util.Locale;
/*    */ import org.joda.time.YearMonth;
/*    */ import org.springframework.format.Formatter;
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
/*    */ class YearMonthFormatter
/*    */   implements Formatter<YearMonth>
/*    */ {
/*    */   public YearMonth parse(String text, Locale locale) throws ParseException {
/* 38 */     return YearMonth.parse(text);
/*    */   }
/*    */ 
/*    */   
/*    */   public String print(YearMonth object, Locale locale) {
/* 43 */     return object.toString();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/datetime/joda/YearMonthFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */