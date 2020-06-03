/*    */ package org.springframework.format.datetime.standard;
/*    */ 
/*    */ import java.text.ParseException;
/*    */ import java.time.YearMonth;
/*    */ import java.util.Locale;
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
/*    */ class YearMonthFormatter
/*    */   implements Formatter<YearMonth>
/*    */ {
/*    */   public YearMonth parse(String text, Locale locale) throws ParseException {
/* 37 */     return YearMonth.parse(text);
/*    */   }
/*    */ 
/*    */   
/*    */   public String print(YearMonth object, Locale locale) {
/* 42 */     return object.toString();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/datetime/standard/YearMonthFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */