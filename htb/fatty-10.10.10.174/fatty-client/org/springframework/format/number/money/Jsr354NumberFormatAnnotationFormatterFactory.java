/*     */ package org.springframework.format.number.money;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.text.ParseException;
/*     */ import java.util.Collections;
/*     */ import java.util.Currency;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import javax.money.CurrencyUnit;
/*     */ import javax.money.Monetary;
/*     */ import javax.money.MonetaryAmount;
/*     */ import org.springframework.context.support.EmbeddedValueResolutionSupport;
/*     */ import org.springframework.format.AnnotationFormatterFactory;
/*     */ import org.springframework.format.Formatter;
/*     */ import org.springframework.format.Parser;
/*     */ import org.springframework.format.Printer;
/*     */ import org.springframework.format.annotation.NumberFormat;
/*     */ import org.springframework.format.number.CurrencyStyleFormatter;
/*     */ import org.springframework.format.number.NumberStyleFormatter;
/*     */ import org.springframework.format.number.PercentStyleFormatter;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class Jsr354NumberFormatAnnotationFormatterFactory
/*     */   extends EmbeddedValueResolutionSupport
/*     */   implements AnnotationFormatterFactory<NumberFormat>
/*     */ {
/*     */   private static final String CURRENCY_CODE_PATTERN = "¤¤";
/*     */   
/*     */   public Set<Class<?>> getFieldTypes() {
/*  57 */     return Collections.singleton(MonetaryAmount.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public Printer<MonetaryAmount> getPrinter(NumberFormat annotation, Class<?> fieldType) {
/*  62 */     return (Printer<MonetaryAmount>)configureFormatterFrom(annotation);
/*     */   }
/*     */ 
/*     */   
/*     */   public Parser<MonetaryAmount> getParser(NumberFormat annotation, Class<?> fieldType) {
/*  67 */     return (Parser<MonetaryAmount>)configureFormatterFrom(annotation);
/*     */   }
/*     */ 
/*     */   
/*     */   private Formatter<MonetaryAmount> configureFormatterFrom(NumberFormat annotation) {
/*  72 */     String pattern = resolveEmbeddedValue(annotation.pattern());
/*  73 */     if (StringUtils.hasLength(pattern)) {
/*  74 */       return new PatternDecoratingFormatter(pattern);
/*     */     }
/*     */     
/*  77 */     NumberFormat.Style style = annotation.style();
/*  78 */     if (style == NumberFormat.Style.NUMBER) {
/*  79 */       return new NumberDecoratingFormatter((Formatter<Number>)new NumberStyleFormatter());
/*     */     }
/*  81 */     if (style == NumberFormat.Style.PERCENT) {
/*  82 */       return new NumberDecoratingFormatter((Formatter<Number>)new PercentStyleFormatter());
/*     */     }
/*     */     
/*  85 */     return new NumberDecoratingFormatter((Formatter<Number>)new CurrencyStyleFormatter());
/*     */   }
/*     */ 
/*     */   
/*     */   private static class NumberDecoratingFormatter
/*     */     implements Formatter<MonetaryAmount>
/*     */   {
/*     */     private final Formatter<Number> numberFormatter;
/*     */ 
/*     */     
/*     */     public NumberDecoratingFormatter(Formatter<Number> numberFormatter) {
/*  96 */       this.numberFormatter = numberFormatter;
/*     */     }
/*     */ 
/*     */     
/*     */     public String print(MonetaryAmount object, Locale locale) {
/* 101 */       return this.numberFormatter.print(object.getNumber(), locale);
/*     */     }
/*     */ 
/*     */     
/*     */     public MonetaryAmount parse(String text, Locale locale) throws ParseException {
/* 106 */       CurrencyUnit currencyUnit = Monetary.getCurrency(locale, new String[0]);
/* 107 */       Number numberValue = (Number)this.numberFormatter.parse(text, locale);
/* 108 */       return Monetary.getDefaultAmountFactory().setNumber(numberValue).setCurrency(currencyUnit).create();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class PatternDecoratingFormatter
/*     */     implements Formatter<MonetaryAmount>
/*     */   {
/*     */     private final String pattern;
/*     */     
/*     */     public PatternDecoratingFormatter(String pattern) {
/* 118 */       this.pattern = pattern;
/*     */     }
/*     */ 
/*     */     
/*     */     public String print(MonetaryAmount object, Locale locale) {
/* 123 */       CurrencyStyleFormatter formatter = new CurrencyStyleFormatter();
/* 124 */       formatter.setCurrency(Currency.getInstance(object.getCurrency().getCurrencyCode()));
/* 125 */       formatter.setPattern(this.pattern);
/* 126 */       return formatter.print((Number)object.getNumber(), locale);
/*     */     }
/*     */ 
/*     */     
/*     */     public MonetaryAmount parse(String text, Locale locale) throws ParseException {
/* 131 */       CurrencyStyleFormatter formatter = new CurrencyStyleFormatter();
/* 132 */       Currency currency = determineCurrency(text, locale);
/* 133 */       CurrencyUnit currencyUnit = Monetary.getCurrency(currency.getCurrencyCode(), new String[0]);
/* 134 */       formatter.setCurrency(currency);
/* 135 */       formatter.setPattern(this.pattern);
/* 136 */       Number numberValue = formatter.parse(text, locale);
/* 137 */       return Monetary.getDefaultAmountFactory().setNumber(numberValue).setCurrency(currencyUnit).create();
/*     */     }
/*     */     
/*     */     private Currency determineCurrency(String text, Locale locale) {
/*     */       try {
/* 142 */         if (text.length() < 3)
/*     */         {
/*     */           
/* 145 */           return Currency.getInstance(locale);
/*     */         }
/* 147 */         if (this.pattern.startsWith("¤¤")) {
/* 148 */           return Currency.getInstance(text.substring(0, 3));
/*     */         }
/* 150 */         if (this.pattern.endsWith("¤¤")) {
/* 151 */           return Currency.getInstance(text.substring(text.length() - 3));
/*     */         }
/*     */ 
/*     */         
/* 155 */         return Currency.getInstance(locale);
/*     */       
/*     */       }
/* 158 */       catch (IllegalArgumentException ex) {
/* 159 */         throw new IllegalArgumentException("Cannot determine currency for number value [" + text + "]", ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/number/money/Jsr354NumberFormatAnnotationFormatterFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */