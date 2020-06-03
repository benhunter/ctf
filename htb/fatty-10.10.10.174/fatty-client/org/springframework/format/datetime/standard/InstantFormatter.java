/*    */ package org.springframework.format.datetime.standard;
/*    */ 
/*    */ import java.text.ParseException;
/*    */ import java.time.Instant;
/*    */ import java.time.format.DateTimeFormatter;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InstantFormatter
/*    */   implements Formatter<Instant>
/*    */ {
/*    */   public Instant parse(String text, Locale locale) throws ParseException {
/* 43 */     if (text.length() > 0 && Character.isDigit(text.charAt(0)))
/*    */     {
/* 45 */       return Instant.parse(text);
/*    */     }
/*    */ 
/*    */     
/* 49 */     return Instant.from(DateTimeFormatter.RFC_1123_DATE_TIME.parse(text));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String print(Instant object, Locale locale) {
/* 55 */     return object.toString();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/datetime/standard/InstantFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */