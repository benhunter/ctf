/*     */ package org.springframework.format.number;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.RoundingMode;
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.NumberFormat;
/*     */ import java.text.ParseException;
/*     */ import java.util.Currency;
/*     */ import java.util.Locale;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CurrencyStyleFormatter
/*     */   extends AbstractNumberFormatter
/*     */ {
/*  44 */   private int fractionDigits = 2;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private RoundingMode roundingMode;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Currency currency;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String pattern;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFractionDigits(int fractionDigits) {
/*  61 */     this.fractionDigits = fractionDigits;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRoundingMode(RoundingMode roundingMode) {
/*  69 */     this.roundingMode = roundingMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCurrency(Currency currency) {
/*  76 */     this.currency = currency;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPattern(String pattern) {
/*  85 */     this.pattern = pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BigDecimal parse(String text, Locale locale) throws ParseException {
/*  91 */     BigDecimal decimal = (BigDecimal)super.parse(text, locale);
/*  92 */     if (this.roundingMode != null) {
/*  93 */       decimal = decimal.setScale(this.fractionDigits, this.roundingMode);
/*     */     } else {
/*     */       
/*  96 */       decimal = decimal.setScale(this.fractionDigits);
/*     */     } 
/*  98 */     return decimal;
/*     */   }
/*     */ 
/*     */   
/*     */   protected NumberFormat getNumberFormat(Locale locale) {
/* 103 */     DecimalFormat format = (DecimalFormat)NumberFormat.getCurrencyInstance(locale);
/* 104 */     format.setParseBigDecimal(true);
/* 105 */     format.setMaximumFractionDigits(this.fractionDigits);
/* 106 */     format.setMinimumFractionDigits(this.fractionDigits);
/* 107 */     if (this.roundingMode != null) {
/* 108 */       format.setRoundingMode(this.roundingMode);
/*     */     }
/* 110 */     if (this.currency != null) {
/* 111 */       format.setCurrency(this.currency);
/*     */     }
/* 113 */     if (this.pattern != null) {
/* 114 */       format.applyPattern(this.pattern);
/*     */     }
/* 116 */     return format;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/number/CurrencyStyleFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */