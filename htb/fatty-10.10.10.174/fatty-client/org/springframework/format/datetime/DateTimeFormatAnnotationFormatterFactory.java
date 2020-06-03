/*    */ package org.springframework.format.datetime;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.util.Calendar;
/*    */ import java.util.Collections;
/*    */ import java.util.Date;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import org.springframework.context.support.EmbeddedValueResolutionSupport;
/*    */ import org.springframework.format.AnnotationFormatterFactory;
/*    */ import org.springframework.format.Formatter;
/*    */ import org.springframework.format.Parser;
/*    */ import org.springframework.format.Printer;
/*    */ import org.springframework.format.annotation.DateTimeFormat;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ public class DateTimeFormatAnnotationFormatterFactory
/*    */   extends EmbeddedValueResolutionSupport
/*    */   implements AnnotationFormatterFactory<DateTimeFormat>
/*    */ {
/*    */   private static final Set<Class<?>> FIELD_TYPES;
/*    */   
/*    */   static {
/* 46 */     Set<Class<?>> fieldTypes = new HashSet<>(4);
/* 47 */     fieldTypes.add(Date.class);
/* 48 */     fieldTypes.add(Calendar.class);
/* 49 */     fieldTypes.add(Long.class);
/* 50 */     FIELD_TYPES = Collections.unmodifiableSet(fieldTypes);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<Class<?>> getFieldTypes() {
/* 56 */     return FIELD_TYPES;
/*    */   }
/*    */ 
/*    */   
/*    */   public Printer<?> getPrinter(DateTimeFormat annotation, Class<?> fieldType) {
/* 61 */     return (Printer<?>)getFormatter(annotation, fieldType);
/*    */   }
/*    */ 
/*    */   
/*    */   public Parser<?> getParser(DateTimeFormat annotation, Class<?> fieldType) {
/* 66 */     return (Parser<?>)getFormatter(annotation, fieldType);
/*    */   }
/*    */   
/*    */   protected Formatter<Date> getFormatter(DateTimeFormat annotation, Class<?> fieldType) {
/* 70 */     DateFormatter formatter = new DateFormatter();
/* 71 */     String style = resolveEmbeddedValue(annotation.style());
/* 72 */     if (StringUtils.hasLength(style)) {
/* 73 */       formatter.setStylePattern(style);
/*    */     }
/* 75 */     formatter.setIso(annotation.iso());
/* 76 */     String pattern = resolveEmbeddedValue(annotation.pattern());
/* 77 */     if (StringUtils.hasLength(pattern)) {
/* 78 */       formatter.setPattern(pattern);
/*    */     }
/* 80 */     return formatter;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/datetime/DateTimeFormatAnnotationFormatterFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */