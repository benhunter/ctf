/*    */ package org.springframework.format.datetime.standard;
/*    */ 
/*    */ import java.text.ParseException;
/*    */ import java.time.Duration;
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
/*    */ class DurationFormatter
/*    */   implements Formatter<Duration>
/*    */ {
/*    */   public Duration parse(String text, Locale locale) throws ParseException {
/* 37 */     return Duration.parse(text);
/*    */   }
/*    */ 
/*    */   
/*    */   public String print(Duration object, Locale locale) {
/* 42 */     return object.toString();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/datetime/standard/DurationFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */