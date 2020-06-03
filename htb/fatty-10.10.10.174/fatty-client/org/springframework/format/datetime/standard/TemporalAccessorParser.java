/*    */ package org.springframework.format.datetime.standard;
/*    */ 
/*    */ import java.text.ParseException;
/*    */ import java.time.LocalDate;
/*    */ import java.time.LocalDateTime;
/*    */ import java.time.LocalTime;
/*    */ import java.time.OffsetDateTime;
/*    */ import java.time.OffsetTime;
/*    */ import java.time.ZonedDateTime;
/*    */ import java.time.format.DateTimeFormatter;
/*    */ import java.time.temporal.TemporalAccessor;
/*    */ import java.util.Locale;
/*    */ import org.springframework.format.Parser;
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
/*    */ public final class TemporalAccessorParser
/*    */   implements Parser<TemporalAccessor>
/*    */ {
/*    */   private final Class<? extends TemporalAccessor> temporalAccessorType;
/*    */   private final DateTimeFormatter formatter;
/*    */   
/*    */   public TemporalAccessorParser(Class<? extends TemporalAccessor> temporalAccessorType, DateTimeFormatter formatter) {
/* 60 */     this.temporalAccessorType = temporalAccessorType;
/* 61 */     this.formatter = formatter;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TemporalAccessor parse(String text, Locale locale) throws ParseException {
/* 67 */     DateTimeFormatter formatterToUse = DateTimeContextHolder.getFormatter(this.formatter, locale);
/* 68 */     if (LocalDate.class == this.temporalAccessorType) {
/* 69 */       return LocalDate.parse(text, formatterToUse);
/*    */     }
/* 71 */     if (LocalTime.class == this.temporalAccessorType) {
/* 72 */       return LocalTime.parse(text, formatterToUse);
/*    */     }
/* 74 */     if (LocalDateTime.class == this.temporalAccessorType) {
/* 75 */       return LocalDateTime.parse(text, formatterToUse);
/*    */     }
/* 77 */     if (ZonedDateTime.class == this.temporalAccessorType) {
/* 78 */       return ZonedDateTime.parse(text, formatterToUse);
/*    */     }
/* 80 */     if (OffsetDateTime.class == this.temporalAccessorType) {
/* 81 */       return OffsetDateTime.parse(text, formatterToUse);
/*    */     }
/* 83 */     if (OffsetTime.class == this.temporalAccessorType) {
/* 84 */       return OffsetTime.parse(text, formatterToUse);
/*    */     }
/*    */     
/* 87 */     throw new IllegalStateException("Unsupported TemporalAccessor type: " + this.temporalAccessorType);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/datetime/standard/TemporalAccessorParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */