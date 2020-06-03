/*     */ package org.springframework.format.datetime.joda;
/*     */ 
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import org.joda.time.DateMidnight;
/*     */ import org.joda.time.DateTime;
/*     */ import org.joda.time.Instant;
/*     */ import org.joda.time.LocalDate;
/*     */ import org.joda.time.LocalDateTime;
/*     */ import org.joda.time.LocalTime;
/*     */ import org.joda.time.MutableDateTime;
/*     */ import org.joda.time.ReadableInstant;
/*     */ import org.springframework.core.convert.converter.Converter;
/*     */ import org.springframework.core.convert.converter.ConverterRegistry;
/*     */ import org.springframework.format.datetime.DateFormatterRegistrar;
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
/*     */ 
/*     */ 
/*     */ final class JodaTimeConverters
/*     */ {
/*     */   public static void registerConverters(ConverterRegistry registry) {
/*  59 */     DateFormatterRegistrar.addDateConverters(registry);
/*     */     
/*  61 */     registry.addConverter(new DateTimeToLocalDateConverter());
/*  62 */     registry.addConverter(new DateTimeToLocalTimeConverter());
/*  63 */     registry.addConverter(new DateTimeToLocalDateTimeConverter());
/*  64 */     registry.addConverter(new DateTimeToDateMidnightConverter());
/*  65 */     registry.addConverter(new DateTimeToMutableDateTimeConverter());
/*  66 */     registry.addConverter(new DateTimeToInstantConverter());
/*  67 */     registry.addConverter(new DateTimeToDateConverter());
/*  68 */     registry.addConverter(new DateTimeToCalendarConverter());
/*  69 */     registry.addConverter(new DateTimeToLongConverter());
/*  70 */     registry.addConverter(new DateToReadableInstantConverter());
/*  71 */     registry.addConverter(new CalendarToReadableInstantConverter());
/*  72 */     registry.addConverter(new LongToReadableInstantConverter());
/*  73 */     registry.addConverter(new LocalDateTimeToLocalDateConverter());
/*  74 */     registry.addConverter(new LocalDateTimeToLocalTimeConverter());
/*     */   }
/*     */   
/*     */   private static class DateTimeToLocalDateConverter
/*     */     implements Converter<DateTime, LocalDate> {
/*     */     private DateTimeToLocalDateConverter() {}
/*     */     
/*     */     public LocalDate convert(DateTime source) {
/*  82 */       return source.toLocalDate();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DateTimeToLocalTimeConverter
/*     */     implements Converter<DateTime, LocalTime> {
/*     */     private DateTimeToLocalTimeConverter() {}
/*     */     
/*     */     public LocalTime convert(DateTime source) {
/*  91 */       return source.toLocalTime();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DateTimeToLocalDateTimeConverter
/*     */     implements Converter<DateTime, LocalDateTime> {
/*     */     private DateTimeToLocalDateTimeConverter() {}
/*     */     
/*     */     public LocalDateTime convert(DateTime source) {
/* 100 */       return source.toLocalDateTime();
/*     */     }
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   private static class DateTimeToDateMidnightConverter
/*     */     implements Converter<DateTime, DateMidnight> {
/*     */     private DateTimeToDateMidnightConverter() {}
/*     */     
/*     */     public DateMidnight convert(DateTime source) {
/* 110 */       return source.toDateMidnight();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DateTimeToMutableDateTimeConverter
/*     */     implements Converter<DateTime, MutableDateTime> {
/*     */     private DateTimeToMutableDateTimeConverter() {}
/*     */     
/*     */     public MutableDateTime convert(DateTime source) {
/* 119 */       return source.toMutableDateTime();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DateTimeToInstantConverter
/*     */     implements Converter<DateTime, Instant> {
/*     */     private DateTimeToInstantConverter() {}
/*     */     
/*     */     public Instant convert(DateTime source) {
/* 128 */       return source.toInstant();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DateTimeToDateConverter
/*     */     implements Converter<DateTime, Date> {
/*     */     private DateTimeToDateConverter() {}
/*     */     
/*     */     public Date convert(DateTime source) {
/* 137 */       return source.toDate();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DateTimeToCalendarConverter
/*     */     implements Converter<DateTime, Calendar> {
/*     */     private DateTimeToCalendarConverter() {}
/*     */     
/*     */     public Calendar convert(DateTime source) {
/* 146 */       return source.toGregorianCalendar();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DateTimeToLongConverter
/*     */     implements Converter<DateTime, Long> {
/*     */     private DateTimeToLongConverter() {}
/*     */     
/*     */     public Long convert(DateTime source) {
/* 155 */       return Long.valueOf(source.getMillis());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class DateToReadableInstantConverter
/*     */     implements Converter<Date, ReadableInstant>
/*     */   {
/*     */     private DateToReadableInstantConverter() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public ReadableInstant convert(Date source) {
/* 169 */       return (ReadableInstant)new DateTime(source);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class CalendarToReadableInstantConverter
/*     */     implements Converter<Calendar, ReadableInstant>
/*     */   {
/*     */     private CalendarToReadableInstantConverter() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public ReadableInstant convert(Calendar source) {
/* 183 */       return (ReadableInstant)new DateTime(source);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class LongToReadableInstantConverter
/*     */     implements Converter<Long, ReadableInstant>
/*     */   {
/*     */     private LongToReadableInstantConverter() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public ReadableInstant convert(Long source) {
/* 197 */       return (ReadableInstant)new DateTime(source.longValue());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class LocalDateTimeToLocalDateConverter
/*     */     implements Converter<LocalDateTime, LocalDate> {
/*     */     private LocalDateTimeToLocalDateConverter() {}
/*     */     
/*     */     public LocalDate convert(LocalDateTime source) {
/* 206 */       return source.toLocalDate();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class LocalDateTimeToLocalTimeConverter
/*     */     implements Converter<LocalDateTime, LocalTime> {
/*     */     private LocalDateTimeToLocalTimeConverter() {}
/*     */     
/*     */     public LocalTime convert(LocalDateTime source) {
/* 215 */       return source.toLocalTime();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/datetime/joda/JodaTimeConverters.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */