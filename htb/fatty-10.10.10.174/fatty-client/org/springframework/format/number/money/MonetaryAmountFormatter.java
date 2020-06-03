/*    */ package org.springframework.format.number.money;
/*    */ 
/*    */ import java.text.ParseException;
/*    */ import java.util.Locale;
/*    */ import javax.money.MonetaryAmount;
/*    */ import javax.money.format.MonetaryAmountFormat;
/*    */ import javax.money.format.MonetaryFormats;
/*    */ import org.springframework.format.Formatter;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MonetaryAmountFormatter
/*    */   implements Formatter<MonetaryAmount>
/*    */ {
/*    */   @Nullable
/*    */   private String formatName;
/*    */   
/*    */   public MonetaryAmountFormatter() {}
/*    */   
/*    */   public MonetaryAmountFormatter(String formatName) {
/* 54 */     this.formatName = formatName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setFormatName(String formatName) {
/* 65 */     this.formatName = formatName;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String print(MonetaryAmount object, Locale locale) {
/* 71 */     return getMonetaryAmountFormat(locale).format(object);
/*    */   }
/*    */ 
/*    */   
/*    */   public MonetaryAmount parse(String text, Locale locale) {
/* 76 */     return getMonetaryAmountFormat(locale).parse(text);
/*    */   }
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
/*    */   protected MonetaryAmountFormat getMonetaryAmountFormat(Locale locale) {
/* 90 */     if (this.formatName != null) {
/* 91 */       return MonetaryFormats.getAmountFormat(this.formatName, new String[0]);
/*    */     }
/*    */     
/* 94 */     return MonetaryFormats.getAmountFormat(locale, new String[0]);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/number/money/MonetaryAmountFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */