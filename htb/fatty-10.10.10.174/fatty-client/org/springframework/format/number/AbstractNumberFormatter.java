/*    */ package org.springframework.format.number;
/*    */ 
/*    */ import java.text.NumberFormat;
/*    */ import java.text.ParseException;
/*    */ import java.text.ParsePosition;
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
/*    */ public abstract class AbstractNumberFormatter
/*    */   implements Formatter<Number>
/*    */ {
/*    */   private boolean lenient = false;
/*    */   
/*    */   public void setLenient(boolean lenient) {
/* 45 */     this.lenient = lenient;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String print(Number number, Locale locale) {
/* 51 */     return getNumberFormat(locale).format(number);
/*    */   }
/*    */ 
/*    */   
/*    */   public Number parse(String text, Locale locale) throws ParseException {
/* 56 */     NumberFormat format = getNumberFormat(locale);
/* 57 */     ParsePosition position = new ParsePosition(0);
/* 58 */     Number number = format.parse(text, position);
/* 59 */     if (position.getErrorIndex() != -1) {
/* 60 */       throw new ParseException(text, position.getIndex());
/*    */     }
/* 62 */     if (!this.lenient && 
/* 63 */       text.length() != position.getIndex())
/*    */     {
/* 65 */       throw new ParseException(text, position.getIndex());
/*    */     }
/*    */     
/* 68 */     return number;
/*    */   }
/*    */   
/*    */   protected abstract NumberFormat getNumberFormat(Locale paramLocale);
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/number/AbstractNumberFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */