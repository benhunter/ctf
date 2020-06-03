/*     */ package org.springframework.format.datetime.standard;
/*     */ 
/*     */ import java.time.Duration;
/*     */ import java.time.Instant;
/*     */ import java.time.LocalDate;
/*     */ import java.time.LocalDateTime;
/*     */ import java.time.LocalTime;
/*     */ import java.time.Month;
/*     */ import java.time.MonthDay;
/*     */ import java.time.OffsetDateTime;
/*     */ import java.time.OffsetTime;
/*     */ import java.time.Period;
/*     */ import java.time.Year;
/*     */ import java.time.YearMonth;
/*     */ import java.time.ZonedDateTime;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.time.format.FormatStyle;
/*     */ import java.util.EnumMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.core.convert.converter.ConverterRegistry;
/*     */ import org.springframework.format.FormatterRegistrar;
/*     */ import org.springframework.format.FormatterRegistry;
/*     */ import org.springframework.format.annotation.DateTimeFormat;
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
/*     */ public class DateTimeFormatterRegistrar
/*     */   implements FormatterRegistrar
/*     */ {
/*     */   private enum Type
/*     */   {
/*  57 */     DATE, TIME, DATE_TIME;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   private final Map<Type, DateTimeFormatter> formatters = new EnumMap<>(Type.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   private final Map<Type, DateTimeFormatterFactory> factories = new EnumMap<>(Type.class);
/*     */ 
/*     */   
/*     */   public DateTimeFormatterRegistrar() {
/*  72 */     for (Type type : Type.values()) {
/*  73 */       this.factories.put(type, new DateTimeFormatterFactory());
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
/*     */   public void setUseIsoFormat(boolean useIsoFormat) {
/*  85 */     ((DateTimeFormatterFactory)this.factories.get(Type.DATE)).setIso(useIsoFormat ? DateTimeFormat.ISO.DATE : DateTimeFormat.ISO.NONE);
/*  86 */     ((DateTimeFormatterFactory)this.factories.get(Type.TIME)).setIso(useIsoFormat ? DateTimeFormat.ISO.TIME : DateTimeFormat.ISO.NONE);
/*  87 */     ((DateTimeFormatterFactory)this.factories.get(Type.DATE_TIME)).setIso(useIsoFormat ? DateTimeFormat.ISO.DATE_TIME : DateTimeFormat.ISO.NONE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDateStyle(FormatStyle dateStyle) {
/*  95 */     ((DateTimeFormatterFactory)this.factories.get(Type.DATE)).setDateStyle(dateStyle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeStyle(FormatStyle timeStyle) {
/* 103 */     ((DateTimeFormatterFactory)this.factories.get(Type.TIME)).setTimeStyle(timeStyle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDateTimeStyle(FormatStyle dateTimeStyle) {
/* 111 */     ((DateTimeFormatterFactory)this.factories.get(Type.DATE_TIME)).setDateTimeStyle(dateTimeStyle);
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
/*     */   public void setDateFormatter(DateTimeFormatter formatter) {
/* 124 */     this.formatters.put(Type.DATE, formatter);
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
/*     */   public void setTimeFormatter(DateTimeFormatter formatter) {
/* 137 */     this.formatters.put(Type.TIME, formatter);
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
/*     */   public void setDateTimeFormatter(DateTimeFormatter formatter) {
/* 151 */     this.formatters.put(Type.DATE_TIME, formatter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerFormatters(FormatterRegistry registry) {
/* 157 */     DateTimeConverters.registerConverters((ConverterRegistry)registry);
/*     */     
/* 159 */     DateTimeFormatter df = getFormatter(Type.DATE);
/* 160 */     DateTimeFormatter tf = getFormatter(Type.TIME);
/* 161 */     DateTimeFormatter dtf = getFormatter(Type.DATE_TIME);
/*     */ 
/*     */ 
/*     */     
/* 165 */     registry.addFormatterForFieldType(LocalDate.class, new TemporalAccessorPrinter((df == DateTimeFormatter.ISO_DATE) ? DateTimeFormatter.ISO_LOCAL_DATE : df), new TemporalAccessorParser((Class)LocalDate.class, df));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 170 */     registry.addFormatterForFieldType(LocalTime.class, new TemporalAccessorPrinter((tf == DateTimeFormatter.ISO_TIME) ? DateTimeFormatter.ISO_LOCAL_TIME : tf), new TemporalAccessorParser((Class)LocalTime.class, tf));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 175 */     registry.addFormatterForFieldType(LocalDateTime.class, new TemporalAccessorPrinter((dtf == DateTimeFormatter.ISO_DATE_TIME) ? DateTimeFormatter.ISO_LOCAL_DATE_TIME : dtf), new TemporalAccessorParser((Class)LocalDateTime.class, dtf));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 180 */     registry.addFormatterForFieldType(ZonedDateTime.class, new TemporalAccessorPrinter(dtf), new TemporalAccessorParser((Class)ZonedDateTime.class, dtf));
/*     */ 
/*     */ 
/*     */     
/* 184 */     registry.addFormatterForFieldType(OffsetDateTime.class, new TemporalAccessorPrinter(dtf), new TemporalAccessorParser((Class)OffsetDateTime.class, dtf));
/*     */ 
/*     */ 
/*     */     
/* 188 */     registry.addFormatterForFieldType(OffsetTime.class, new TemporalAccessorPrinter(tf), new TemporalAccessorParser((Class)OffsetTime.class, tf));
/*     */ 
/*     */ 
/*     */     
/* 192 */     registry.addFormatterForFieldType(Instant.class, new InstantFormatter());
/* 193 */     registry.addFormatterForFieldType(Period.class, new PeriodFormatter());
/* 194 */     registry.addFormatterForFieldType(Duration.class, new DurationFormatter());
/* 195 */     registry.addFormatterForFieldType(Year.class, new YearFormatter());
/* 196 */     registry.addFormatterForFieldType(Month.class, new MonthFormatter());
/* 197 */     registry.addFormatterForFieldType(YearMonth.class, new YearMonthFormatter());
/* 198 */     registry.addFormatterForFieldType(MonthDay.class, new MonthDayFormatter());
/*     */     
/* 200 */     registry.addFormatterForFieldAnnotation(new Jsr310DateTimeFormatAnnotationFormatterFactory());
/*     */   }
/*     */   
/*     */   private DateTimeFormatter getFormatter(Type type) {
/* 204 */     DateTimeFormatter formatter = this.formatters.get(type);
/* 205 */     if (formatter != null) {
/* 206 */       return formatter;
/*     */     }
/* 208 */     DateTimeFormatter fallbackFormatter = getFallbackFormatter(type);
/* 209 */     return ((DateTimeFormatterFactory)this.factories.get(type)).createDateTimeFormatter(fallbackFormatter);
/*     */   }
/*     */   
/*     */   private DateTimeFormatter getFallbackFormatter(Type type) {
/* 213 */     switch (type) { case DATE:
/* 214 */         return DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
/* 215 */       case TIME: return DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT); }
/* 216 */      return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/datetime/standard/DateTimeFormatterRegistrar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */