/*     */ package org.springframework.format.datetime.standard;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.time.LocalDate;
/*     */ import java.time.LocalDateTime;
/*     */ import java.time.LocalTime;
/*     */ import java.time.OffsetDateTime;
/*     */ import java.time.OffsetTime;
/*     */ import java.time.ZonedDateTime;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
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
/*     */ public class Jsr310DateTimeFormatAnnotationFormatterFactory
/*     */   extends EmbeddedValueResolutionSupport
/*     */   implements AnnotationFormatterFactory<DateTimeFormat>
/*     */ {
/*     */   private static final Set<Class<?>> FIELD_TYPES;
/*     */   
/*     */   static {
/*  53 */     Set<Class<?>> fieldTypes = new HashSet<>(8);
/*  54 */     fieldTypes.add(LocalDate.class);
/*  55 */     fieldTypes.add(LocalTime.class);
/*  56 */     fieldTypes.add(LocalDateTime.class);
/*  57 */     fieldTypes.add(ZonedDateTime.class);
/*  58 */     fieldTypes.add(OffsetDateTime.class);
/*  59 */     fieldTypes.add(OffsetTime.class);
/*  60 */     FIELD_TYPES = Collections.unmodifiableSet(fieldTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Set<Class<?>> getFieldTypes() {
/*  66 */     return FIELD_TYPES;
/*     */   }
/*     */ 
/*     */   
/*     */   public Printer<?> getPrinter(DateTimeFormat annotation, Class<?> fieldType) {
/*  71 */     DateTimeFormatter formatter = getFormatter(annotation, fieldType);
/*     */ 
/*     */     
/*  74 */     if (formatter == DateTimeFormatter.ISO_DATE) {
/*  75 */       if (isLocal(fieldType)) {
/*  76 */         formatter = DateTimeFormatter.ISO_LOCAL_DATE;
/*     */       }
/*     */     }
/*  79 */     else if (formatter == DateTimeFormatter.ISO_TIME) {
/*  80 */       if (isLocal(fieldType)) {
/*  81 */         formatter = DateTimeFormatter.ISO_LOCAL_TIME;
/*     */       }
/*     */     }
/*  84 */     else if (formatter == DateTimeFormatter.ISO_DATE_TIME && 
/*  85 */       isLocal(fieldType)) {
/*  86 */       formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
/*     */     } 
/*     */ 
/*     */     
/*  90 */     return new TemporalAccessorPrinter(formatter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Parser<?> getParser(DateTimeFormat annotation, Class<?> fieldType) {
/*  96 */     DateTimeFormatter formatter = getFormatter(annotation, fieldType);
/*  97 */     return new TemporalAccessorParser((Class)fieldType, formatter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DateTimeFormatter getFormatter(DateTimeFormat annotation, Class<?> fieldType) {
/* 107 */     DateTimeFormatterFactory factory = new DateTimeFormatterFactory();
/* 108 */     String style = resolveEmbeddedValue(annotation.style());
/* 109 */     if (StringUtils.hasLength(style)) {
/* 110 */       factory.setStylePattern(style);
/*     */     }
/* 112 */     factory.setIso(annotation.iso());
/* 113 */     String pattern = resolveEmbeddedValue(annotation.pattern());
/* 114 */     if (StringUtils.hasLength(pattern)) {
/* 115 */       factory.setPattern(pattern);
/*     */     }
/* 117 */     return factory.createDateTimeFormatter();
/*     */   }
/*     */   
/*     */   private boolean isLocal(Class<?> fieldType) {
/* 121 */     return fieldType.getSimpleName().startsWith("Local");
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/datetime/standard/Jsr310DateTimeFormatAnnotationFormatterFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */