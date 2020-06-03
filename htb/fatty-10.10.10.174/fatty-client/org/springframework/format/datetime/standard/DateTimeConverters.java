/*     */ package org.springframework.format.datetime.standard;
/*     */ 
/*     */ import java.time.Instant;
/*     */ import java.time.LocalDate;
/*     */ import java.time.LocalDateTime;
/*     */ import java.time.LocalTime;
/*     */ import java.time.OffsetDateTime;
/*     */ import java.time.ZonedDateTime;
/*     */ import java.util.Calendar;
/*     */ import java.util.GregorianCalendar;
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
/*     */ final class DateTimeConverters
/*     */ {
/*     */   public static void registerConverters(ConverterRegistry registry) {
/*  55 */     DateFormatterRegistrar.addDateConverters(registry);
/*     */     
/*  57 */     registry.addConverter(new LocalDateTimeToLocalDateConverter());
/*  58 */     registry.addConverter(new LocalDateTimeToLocalTimeConverter());
/*  59 */     registry.addConverter(new ZonedDateTimeToLocalDateConverter());
/*  60 */     registry.addConverter(new ZonedDateTimeToLocalTimeConverter());
/*  61 */     registry.addConverter(new ZonedDateTimeToLocalDateTimeConverter());
/*  62 */     registry.addConverter(new ZonedDateTimeToOffsetDateTimeConverter());
/*  63 */     registry.addConverter(new ZonedDateTimeToInstantConverter());
/*  64 */     registry.addConverter(new OffsetDateTimeToLocalDateConverter());
/*  65 */     registry.addConverter(new OffsetDateTimeToLocalTimeConverter());
/*  66 */     registry.addConverter(new OffsetDateTimeToLocalDateTimeConverter());
/*  67 */     registry.addConverter(new OffsetDateTimeToZonedDateTimeConverter());
/*  68 */     registry.addConverter(new OffsetDateTimeToInstantConverter());
/*  69 */     registry.addConverter(new CalendarToZonedDateTimeConverter());
/*  70 */     registry.addConverter(new CalendarToOffsetDateTimeConverter());
/*  71 */     registry.addConverter(new CalendarToLocalDateConverter());
/*  72 */     registry.addConverter(new CalendarToLocalTimeConverter());
/*  73 */     registry.addConverter(new CalendarToLocalDateTimeConverter());
/*  74 */     registry.addConverter(new CalendarToInstantConverter());
/*  75 */     registry.addConverter(new LongToInstantConverter());
/*  76 */     registry.addConverter(new InstantToLongConverter());
/*     */   }
/*     */   
/*     */   private static ZonedDateTime calendarToZonedDateTime(Calendar source) {
/*  80 */     if (source instanceof GregorianCalendar) {
/*  81 */       return ((GregorianCalendar)source).toZonedDateTime();
/*     */     }
/*     */     
/*  84 */     return ZonedDateTime.ofInstant(Instant.ofEpochMilli(source.getTimeInMillis()), source
/*  85 */         .getTimeZone().toZoneId());
/*     */   }
/*     */   
/*     */   private static class LocalDateTimeToLocalDateConverter
/*     */     implements Converter<LocalDateTime, LocalDate>
/*     */   {
/*     */     private LocalDateTimeToLocalDateConverter() {}
/*     */     
/*     */     public LocalDate convert(LocalDateTime source) {
/*  94 */       return source.toLocalDate();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class LocalDateTimeToLocalTimeConverter
/*     */     implements Converter<LocalDateTime, LocalTime> {
/*     */     private LocalDateTimeToLocalTimeConverter() {}
/*     */     
/*     */     public LocalTime convert(LocalDateTime source) {
/* 103 */       return source.toLocalTime();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ZonedDateTimeToLocalDateConverter
/*     */     implements Converter<ZonedDateTime, LocalDate> {
/*     */     private ZonedDateTimeToLocalDateConverter() {}
/*     */     
/*     */     public LocalDate convert(ZonedDateTime source) {
/* 112 */       return source.toLocalDate();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ZonedDateTimeToLocalTimeConverter
/*     */     implements Converter<ZonedDateTime, LocalTime> {
/*     */     private ZonedDateTimeToLocalTimeConverter() {}
/*     */     
/*     */     public LocalTime convert(ZonedDateTime source) {
/* 121 */       return source.toLocalTime();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ZonedDateTimeToLocalDateTimeConverter
/*     */     implements Converter<ZonedDateTime, LocalDateTime> {
/*     */     private ZonedDateTimeToLocalDateTimeConverter() {}
/*     */     
/*     */     public LocalDateTime convert(ZonedDateTime source) {
/* 130 */       return source.toLocalDateTime();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ZonedDateTimeToOffsetDateTimeConverter implements Converter<ZonedDateTime, OffsetDateTime> {
/*     */     private ZonedDateTimeToOffsetDateTimeConverter() {}
/*     */     
/*     */     public OffsetDateTime convert(ZonedDateTime source) {
/* 138 */       return source.toOffsetDateTime();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ZonedDateTimeToInstantConverter
/*     */     implements Converter<ZonedDateTime, Instant> {
/*     */     private ZonedDateTimeToInstantConverter() {}
/*     */     
/*     */     public Instant convert(ZonedDateTime source) {
/* 147 */       return source.toInstant();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class OffsetDateTimeToLocalDateConverter
/*     */     implements Converter<OffsetDateTime, LocalDate> {
/*     */     private OffsetDateTimeToLocalDateConverter() {}
/*     */     
/*     */     public LocalDate convert(OffsetDateTime source) {
/* 156 */       return source.toLocalDate();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class OffsetDateTimeToLocalTimeConverter
/*     */     implements Converter<OffsetDateTime, LocalTime> {
/*     */     private OffsetDateTimeToLocalTimeConverter() {}
/*     */     
/*     */     public LocalTime convert(OffsetDateTime source) {
/* 165 */       return source.toLocalTime();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class OffsetDateTimeToLocalDateTimeConverter
/*     */     implements Converter<OffsetDateTime, LocalDateTime> {
/*     */     private OffsetDateTimeToLocalDateTimeConverter() {}
/*     */     
/*     */     public LocalDateTime convert(OffsetDateTime source) {
/* 174 */       return source.toLocalDateTime();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class OffsetDateTimeToZonedDateTimeConverter
/*     */     implements Converter<OffsetDateTime, ZonedDateTime> {
/*     */     private OffsetDateTimeToZonedDateTimeConverter() {}
/*     */     
/*     */     public ZonedDateTime convert(OffsetDateTime source) {
/* 183 */       return source.toZonedDateTime();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class OffsetDateTimeToInstantConverter
/*     */     implements Converter<OffsetDateTime, Instant> {
/*     */     private OffsetDateTimeToInstantConverter() {}
/*     */     
/*     */     public Instant convert(OffsetDateTime source) {
/* 192 */       return source.toInstant();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CalendarToZonedDateTimeConverter
/*     */     implements Converter<Calendar, ZonedDateTime> {
/*     */     private CalendarToZonedDateTimeConverter() {}
/*     */     
/*     */     public ZonedDateTime convert(Calendar source) {
/* 201 */       return DateTimeConverters.calendarToZonedDateTime(source);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CalendarToOffsetDateTimeConverter
/*     */     implements Converter<Calendar, OffsetDateTime> {
/*     */     private CalendarToOffsetDateTimeConverter() {}
/*     */     
/*     */     public OffsetDateTime convert(Calendar source) {
/* 210 */       return DateTimeConverters.calendarToZonedDateTime(source).toOffsetDateTime();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CalendarToLocalDateConverter
/*     */     implements Converter<Calendar, LocalDate> {
/*     */     private CalendarToLocalDateConverter() {}
/*     */     
/*     */     public LocalDate convert(Calendar source) {
/* 219 */       return DateTimeConverters.calendarToZonedDateTime(source).toLocalDate();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CalendarToLocalTimeConverter
/*     */     implements Converter<Calendar, LocalTime> {
/*     */     private CalendarToLocalTimeConverter() {}
/*     */     
/*     */     public LocalTime convert(Calendar source) {
/* 228 */       return DateTimeConverters.calendarToZonedDateTime(source).toLocalTime();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CalendarToLocalDateTimeConverter
/*     */     implements Converter<Calendar, LocalDateTime> {
/*     */     private CalendarToLocalDateTimeConverter() {}
/*     */     
/*     */     public LocalDateTime convert(Calendar source) {
/* 237 */       return DateTimeConverters.calendarToZonedDateTime(source).toLocalDateTime();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CalendarToInstantConverter
/*     */     implements Converter<Calendar, Instant> {
/*     */     private CalendarToInstantConverter() {}
/*     */     
/*     */     public Instant convert(Calendar source) {
/* 246 */       return DateTimeConverters.calendarToZonedDateTime(source).toInstant();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class LongToInstantConverter
/*     */     implements Converter<Long, Instant> {
/*     */     private LongToInstantConverter() {}
/*     */     
/*     */     public Instant convert(Long source) {
/* 255 */       return Instant.ofEpochMilli(source.longValue());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class InstantToLongConverter
/*     */     implements Converter<Instant, Long> {
/*     */     private InstantToLongConverter() {}
/*     */     
/*     */     public Long convert(Instant source) {
/* 264 */       return Long.valueOf(source.toEpochMilli());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/datetime/standard/DateTimeConverters.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */