/*     */ package org.springframework.format.support;
/*     */ 
/*     */ import org.springframework.core.convert.converter.ConverterRegistry;
/*     */ import org.springframework.core.convert.support.DefaultConversionService;
/*     */ import org.springframework.format.AnnotationFormatterFactory;
/*     */ import org.springframework.format.Formatter;
/*     */ import org.springframework.format.FormatterRegistry;
/*     */ import org.springframework.format.datetime.DateFormatterRegistrar;
/*     */ import org.springframework.format.datetime.joda.JodaTimeFormatterRegistrar;
/*     */ import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
/*     */ import org.springframework.format.number.NumberFormatAnnotationFormatterFactory;
/*     */ import org.springframework.format.number.money.CurrencyUnitFormatter;
/*     */ import org.springframework.format.number.money.Jsr354NumberFormatAnnotationFormatterFactory;
/*     */ import org.springframework.format.number.money.MonetaryAmountFormatter;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StringValueResolver;
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
/*     */ public class DefaultFormattingConversionService
/*     */   extends FormattingConversionService
/*     */ {
/*     */   private static final boolean jsr354Present;
/*     */   private static final boolean jodaTimePresent;
/*     */   
/*     */   static {
/*  55 */     ClassLoader classLoader = DefaultFormattingConversionService.class.getClassLoader();
/*  56 */     jsr354Present = ClassUtils.isPresent("javax.money.MonetaryAmount", classLoader);
/*  57 */     jodaTimePresent = ClassUtils.isPresent("org.joda.time.LocalDate", classLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultFormattingConversionService() {
/*  67 */     this(null, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultFormattingConversionService(boolean registerDefaultFormatters) {
/*  78 */     this(null, registerDefaultFormatters);
/*     */   }
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
/*     */   public DefaultFormattingConversionService(@Nullable StringValueResolver embeddedValueResolver, boolean registerDefaultFormatters) {
/*  93 */     if (embeddedValueResolver != null) {
/*  94 */       setEmbeddedValueResolver(embeddedValueResolver);
/*     */     }
/*  96 */     DefaultConversionService.addDefaultConverters((ConverterRegistry)this);
/*  97 */     if (registerDefaultFormatters) {
/*  98 */       addDefaultFormatters(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addDefaultFormatters(FormatterRegistry formatterRegistry) {
/* 111 */     formatterRegistry.addFormatterForFieldAnnotation((AnnotationFormatterFactory)new NumberFormatAnnotationFormatterFactory());
/*     */ 
/*     */     
/* 114 */     if (jsr354Present) {
/* 115 */       formatterRegistry.addFormatter((Formatter)new CurrencyUnitFormatter());
/* 116 */       formatterRegistry.addFormatter((Formatter)new MonetaryAmountFormatter());
/* 117 */       formatterRegistry.addFormatterForFieldAnnotation((AnnotationFormatterFactory)new Jsr354NumberFormatAnnotationFormatterFactory());
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 123 */     (new DateTimeFormatterRegistrar()).registerFormatters(formatterRegistry);
/*     */     
/* 125 */     if (jodaTimePresent) {
/*     */       
/* 127 */       (new JodaTimeFormatterRegistrar()).registerFormatters(formatterRegistry);
/*     */     }
/*     */     else {
/*     */       
/* 131 */       (new DateFormatterRegistrar()).registerFormatters(formatterRegistry);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/support/DefaultFormattingConversionService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */