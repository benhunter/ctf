/*     */ package org.springframework.format.datetime.joda;
/*     */ 
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.EnumMap;
/*     */ import java.util.Map;
/*     */ import org.joda.time.Duration;
/*     */ import org.joda.time.LocalDate;
/*     */ import org.joda.time.LocalDateTime;
/*     */ import org.joda.time.LocalTime;
/*     */ import org.joda.time.MonthDay;
/*     */ import org.joda.time.Period;
/*     */ import org.joda.time.ReadableInstant;
/*     */ import org.joda.time.YearMonth;
/*     */ import org.joda.time.format.DateTimeFormat;
/*     */ import org.joda.time.format.DateTimeFormatter;
/*     */ import org.springframework.core.convert.converter.ConverterRegistry;
/*     */ import org.springframework.format.FormatterRegistrar;
/*     */ import org.springframework.format.FormatterRegistry;
/*     */ import org.springframework.format.Parser;
/*     */ import org.springframework.format.Printer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JodaTimeFormatterRegistrar
/*     */   implements FormatterRegistrar
/*     */ {
/*     */   private enum Type
/*     */   {
/*  61 */     DATE, TIME, DATE_TIME;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   private final Map<Type, DateTimeFormatter> formatters = new EnumMap<>(Type.class);
/*     */ 
/*     */ 
/*     */   
/*     */   private final Map<Type, DateTimeFormatterFactory> factories;
/*     */ 
/*     */ 
/*     */   
/*     */   public JodaTimeFormatterRegistrar() {
/*  76 */     this.factories = new EnumMap<>(Type.class);
/*  77 */     for (Type type : Type.values()) {
/*  78 */       this.factories.put(type, new DateTimeFormatterFactory());
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
/*  90 */     ((DateTimeFormatterFactory)this.factories.get(Type.DATE)).setIso(useIsoFormat ? DateTimeFormat.ISO.DATE : DateTimeFormat.ISO.NONE);
/*  91 */     ((DateTimeFormatterFactory)this.factories.get(Type.TIME)).setIso(useIsoFormat ? DateTimeFormat.ISO.TIME : DateTimeFormat.ISO.NONE);
/*  92 */     ((DateTimeFormatterFactory)this.factories.get(Type.DATE_TIME)).setIso(useIsoFormat ? DateTimeFormat.ISO.DATE_TIME : DateTimeFormat.ISO.NONE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDateStyle(String dateStyle) {
/* 100 */     ((DateTimeFormatterFactory)this.factories.get(Type.DATE)).setStyle(dateStyle + "-");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeStyle(String timeStyle) {
/* 108 */     ((DateTimeFormatterFactory)this.factories.get(Type.TIME)).setStyle("-" + timeStyle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDateTimeStyle(String dateTimeStyle) {
/* 117 */     ((DateTimeFormatterFactory)this.factories.get(Type.DATE_TIME)).setStyle(dateTimeStyle);
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
/*     */   public void setDateFormatter(DateTimeFormatter formatter) {
/* 131 */     this.formatters.put(Type.DATE, formatter);
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
/*     */   public void setTimeFormatter(DateTimeFormatter formatter) {
/* 145 */     this.formatters.put(Type.TIME, formatter);
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
/*     */   public void setDateTimeFormatter(DateTimeFormatter formatter) {
/* 160 */     this.formatters.put(Type.DATE_TIME, formatter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerFormatters(FormatterRegistry registry) {
/* 166 */     JodaTimeConverters.registerConverters((ConverterRegistry)registry);
/*     */     
/* 168 */     DateTimeFormatter dateFormatter = getFormatter(Type.DATE);
/* 169 */     DateTimeFormatter timeFormatter = getFormatter(Type.TIME);
/* 170 */     DateTimeFormatter dateTimeFormatter = getFormatter(Type.DATE_TIME);
/*     */     
/* 172 */     addFormatterForFields(registry, new ReadablePartialPrinter(dateFormatter), new LocalDateParser(dateFormatter), new Class[] { LocalDate.class });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 177 */     addFormatterForFields(registry, new ReadablePartialPrinter(timeFormatter), new LocalTimeParser(timeFormatter), new Class[] { LocalTime.class });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 182 */     addFormatterForFields(registry, new ReadablePartialPrinter(dateTimeFormatter), new LocalDateTimeParser(dateTimeFormatter), new Class[] { LocalDateTime.class });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 187 */     addFormatterForFields(registry, new ReadableInstantPrinter(dateTimeFormatter), new DateTimeParser(dateTimeFormatter), new Class[] { ReadableInstant.class });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 194 */     if (this.formatters.containsKey(Type.DATE_TIME)) {
/* 195 */       addFormatterForFields(registry, new ReadableInstantPrinter(dateTimeFormatter), new DateTimeParser(dateTimeFormatter), new Class[] { Date.class, Calendar.class });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 201 */     registry.addFormatterForFieldType(Period.class, new PeriodFormatter());
/* 202 */     registry.addFormatterForFieldType(Duration.class, new DurationFormatter());
/* 203 */     registry.addFormatterForFieldType(YearMonth.class, new YearMonthFormatter());
/* 204 */     registry.addFormatterForFieldType(MonthDay.class, new MonthDayFormatter());
/*     */     
/* 206 */     registry.addFormatterForFieldAnnotation(new JodaDateTimeFormatAnnotationFormatterFactory());
/*     */   }
/*     */   
/*     */   private DateTimeFormatter getFormatter(Type type) {
/* 210 */     DateTimeFormatter formatter = this.formatters.get(type);
/* 211 */     if (formatter != null) {
/* 212 */       return formatter;
/*     */     }
/* 214 */     DateTimeFormatter fallbackFormatter = getFallbackFormatter(type);
/* 215 */     return ((DateTimeFormatterFactory)this.factories.get(type)).createDateTimeFormatter(fallbackFormatter);
/*     */   }
/*     */   
/*     */   private DateTimeFormatter getFallbackFormatter(Type type) {
/* 219 */     switch (type) { case DATE:
/* 220 */         return DateTimeFormat.shortDate();
/* 221 */       case TIME: return DateTimeFormat.shortTime(); }
/* 222 */      return DateTimeFormat.shortDateTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addFormatterForFields(FormatterRegistry registry, Printer<?> printer, Parser<?> parser, Class<?>... fieldTypes) {
/* 229 */     for (Class<?> fieldType : fieldTypes)
/* 230 */       registry.addFormatterForFieldType(fieldType, printer, parser); 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/datetime/joda/JodaTimeFormatterRegistrar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */