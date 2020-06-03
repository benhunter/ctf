/*    */ package org.springframework.format.number;
/*    */ 
/*    */ import java.text.DecimalFormat;
/*    */ import java.text.NumberFormat;
/*    */ import java.util.Locale;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NumberStyleFormatter
/*    */   extends AbstractNumberFormatter
/*    */ {
/*    */   @Nullable
/*    */   private String pattern;
/*    */   
/*    */   public NumberStyleFormatter() {}
/*    */   
/*    */   public NumberStyleFormatter(String pattern) {
/* 57 */     this.pattern = pattern;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setPattern(String pattern) {
/* 67 */     this.pattern = pattern;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public NumberFormat getNumberFormat(Locale locale) {
/* 73 */     NumberFormat format = NumberFormat.getInstance(locale);
/* 74 */     if (!(format instanceof DecimalFormat)) {
/* 75 */       if (this.pattern != null) {
/* 76 */         throw new IllegalStateException("Cannot support pattern for non-DecimalFormat: " + format);
/*    */       }
/* 78 */       return format;
/*    */     } 
/* 80 */     DecimalFormat decimalFormat = (DecimalFormat)format;
/* 81 */     decimalFormat.setParseBigDecimal(true);
/* 82 */     if (this.pattern != null) {
/* 83 */       decimalFormat.applyPattern(this.pattern);
/*    */     }
/* 85 */     return decimalFormat;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/number/NumberStyleFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */