/*    */ package org.springframework.format.datetime.standard;
/*    */ 
/*    */ import java.text.ParseException;
/*    */ import java.time.Period;
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
/*    */ class PeriodFormatter
/*    */   implements Formatter<Period>
/*    */ {
/*    */   public Period parse(String text, Locale locale) throws ParseException {
/* 37 */     return Period.parse(text);
/*    */   }
/*    */ 
/*    */   
/*    */   public String print(Period object, Locale locale) {
/* 42 */     return object.toString();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/datetime/standard/PeriodFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */