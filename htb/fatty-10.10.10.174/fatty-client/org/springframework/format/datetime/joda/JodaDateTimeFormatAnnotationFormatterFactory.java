/*     */ package org.springframework.format.datetime.joda;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Calendar;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.joda.time.LocalDate;
/*     */ import org.joda.time.LocalDateTime;
/*     */ import org.joda.time.LocalTime;
/*     */ import org.joda.time.ReadableInstant;
/*     */ import org.joda.time.ReadablePartial;
/*     */ import org.joda.time.format.DateTimeFormatter;
/*     */ import org.springframework.context.support.EmbeddedValueResolutionSupport;
/*     */ import org.springframework.format.AnnotationFormatterFactory;
/*     */ import org.springframework.format.Parser;
/*     */ import org.springframework.format.Printer;
/*     */ import org.springframework.format.annotation.DateTimeFormat;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JodaDateTimeFormatAnnotationFormatterFactory
/*     */   extends EmbeddedValueResolutionSupport
/*     */   implements AnnotationFormatterFactory<DateTimeFormat>
/*     */ {
/*     */   private static final Set<Class<?>> FIELD_TYPES;
/*     */   
/*     */   static {
/*  61 */     Set<Class<?>> fieldTypes = new HashSet<>(8);
/*  62 */     fieldTypes.add(ReadableInstant.class);
/*  63 */     fieldTypes.add(LocalDate.class);
/*  64 */     fieldTypes.add(LocalTime.class);
/*  65 */     fieldTypes.add(LocalDateTime.class);
/*  66 */     fieldTypes.add(Date.class);
/*  67 */     fieldTypes.add(Calendar.class);
/*  68 */     fieldTypes.add(Long.class);
/*  69 */     FIELD_TYPES = Collections.unmodifiableSet(fieldTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Set<Class<?>> getFieldTypes() {
/*  75 */     return FIELD_TYPES;
/*     */   }
/*     */ 
/*     */   
/*     */   public Printer<?> getPrinter(DateTimeFormat annotation, Class<?> fieldType) {
/*  80 */     DateTimeFormatter formatter = getFormatter(annotation, fieldType);
/*  81 */     if (ReadablePartial.class.isAssignableFrom(fieldType)) {
/*  82 */       return new ReadablePartialPrinter(formatter);
/*     */     }
/*  84 */     if (ReadableInstant.class.isAssignableFrom(fieldType) || Calendar.class.isAssignableFrom(fieldType))
/*     */     {
/*  86 */       return new ReadableInstantPrinter(formatter);
/*     */     }
/*     */ 
/*     */     
/*  90 */     return new MillisecondInstantPrinter(formatter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Parser<?> getParser(DateTimeFormat annotation, Class<?> fieldType) {
/*  96 */     if (LocalDate.class == fieldType) {
/*  97 */       return new LocalDateParser(getFormatter(annotation, fieldType));
/*     */     }
/*  99 */     if (LocalTime.class == fieldType) {
/* 100 */       return new LocalTimeParser(getFormatter(annotation, fieldType));
/*     */     }
/* 102 */     if (LocalDateTime.class == fieldType) {
/* 103 */       return new LocalDateTimeParser(getFormatter(annotation, fieldType));
/*     */     }
/*     */     
/* 106 */     return new DateTimeParser(getFormatter(annotation, fieldType));
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
/*     */   protected DateTimeFormatter getFormatter(DateTimeFormat annotation, Class<?> fieldType) {
/* 118 */     DateTimeFormatterFactory factory = new DateTimeFormatterFactory();
/* 119 */     String style = resolveEmbeddedValue(annotation.style());
/* 120 */     if (StringUtils.hasLength(style)) {
/* 121 */       factory.setStyle(style);
/*     */     }
/* 123 */     factory.setIso(annotation.iso());
/* 124 */     String pattern = resolveEmbeddedValue(annotation.pattern());
/* 125 */     if (StringUtils.hasLength(pattern)) {
/* 126 */       factory.setPattern(pattern);
/*     */     }
/* 128 */     return factory.createDateTimeFormatter();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/datetime/joda/JodaDateTimeFormatAnnotationFormatterFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */