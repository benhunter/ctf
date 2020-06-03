/*     */ package org.springframework.format.datetime;
/*     */ 
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import org.springframework.core.convert.converter.Converter;
/*     */ import org.springframework.core.convert.converter.ConverterRegistry;
/*     */ import org.springframework.format.FormatterRegistrar;
/*     */ import org.springframework.format.FormatterRegistry;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DateFormatterRegistrar
/*     */   implements FormatterRegistrar
/*     */ {
/*     */   @Nullable
/*     */   private DateFormatter dateFormatter;
/*     */   
/*     */   public void setFormatter(DateFormatter dateFormatter) {
/*  56 */     Assert.notNull(dateFormatter, "DateFormatter must not be null");
/*  57 */     this.dateFormatter = dateFormatter;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerFormatters(FormatterRegistry registry) {
/*  63 */     addDateConverters((ConverterRegistry)registry);
/*  64 */     registry.addFormatterForFieldAnnotation(new DateTimeFormatAnnotationFormatterFactory());
/*     */ 
/*     */ 
/*     */     
/*  68 */     if (this.dateFormatter != null) {
/*  69 */       registry.addFormatter(this.dateFormatter);
/*  70 */       registry.addFormatterForFieldType(Calendar.class, this.dateFormatter);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addDateConverters(ConverterRegistry converterRegistry) {
/*  79 */     converterRegistry.addConverter(new DateToLongConverter());
/*  80 */     converterRegistry.addConverter(new DateToCalendarConverter());
/*  81 */     converterRegistry.addConverter(new CalendarToDateConverter());
/*  82 */     converterRegistry.addConverter(new CalendarToLongConverter());
/*  83 */     converterRegistry.addConverter(new LongToDateConverter());
/*  84 */     converterRegistry.addConverter(new LongToCalendarConverter());
/*     */   }
/*     */   
/*     */   private static class DateToLongConverter
/*     */     implements Converter<Date, Long> {
/*     */     private DateToLongConverter() {}
/*     */     
/*     */     public Long convert(Date source) {
/*  92 */       return Long.valueOf(source.getTime());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DateToCalendarConverter
/*     */     implements Converter<Date, Calendar> {
/*     */     private DateToCalendarConverter() {}
/*     */     
/*     */     public Calendar convert(Date source) {
/* 101 */       Calendar calendar = Calendar.getInstance();
/* 102 */       calendar.setTime(source);
/* 103 */       return calendar;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CalendarToDateConverter
/*     */     implements Converter<Calendar, Date> {
/*     */     private CalendarToDateConverter() {}
/*     */     
/*     */     public Date convert(Calendar source) {
/* 112 */       return source.getTime();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CalendarToLongConverter
/*     */     implements Converter<Calendar, Long> {
/*     */     private CalendarToLongConverter() {}
/*     */     
/*     */     public Long convert(Calendar source) {
/* 121 */       return Long.valueOf(source.getTimeInMillis());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class LongToDateConverter
/*     */     implements Converter<Long, Date> {
/*     */     private LongToDateConverter() {}
/*     */     
/*     */     public Date convert(Long source) {
/* 130 */       return new Date(source.longValue());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class LongToCalendarConverter
/*     */     implements Converter<Long, Calendar> {
/*     */     private LongToCalendarConverter() {}
/*     */     
/*     */     public Calendar convert(Long source) {
/* 139 */       Calendar calendar = Calendar.getInstance();
/* 140 */       calendar.setTimeInMillis(source.longValue());
/* 141 */       return calendar;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/datetime/DateFormatterRegistrar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */