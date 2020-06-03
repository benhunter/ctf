/*     */ package org.springframework.format.datetime.joda;
/*     */ 
/*     */ import java.util.TimeZone;
/*     */ import org.joda.time.DateTimeZone;
/*     */ import org.joda.time.format.DateTimeFormat;
/*     */ import org.joda.time.format.DateTimeFormatter;
/*     */ import org.joda.time.format.ISODateTimeFormat;
/*     */ import org.springframework.format.annotation.DateTimeFormat;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class DateTimeFormatterFactory
/*     */ {
/*     */   @Nullable
/*     */   private String pattern;
/*     */   @Nullable
/*     */   private DateTimeFormat.ISO iso;
/*     */   @Nullable
/*     */   private String style;
/*     */   @Nullable
/*     */   private TimeZone timeZone;
/*     */   
/*     */   public DateTimeFormatterFactory() {}
/*     */   
/*     */   public DateTimeFormatterFactory(String pattern) {
/*  72 */     this.pattern = pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPattern(String pattern) {
/*  81 */     this.pattern = pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIso(DateTimeFormat.ISO iso) {
/*  89 */     this.iso = iso;
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
/*     */ 
/*     */   
/*     */   public void setStyle(String style) {
/* 106 */     this.style = style;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeZone(TimeZone timeZone) {
/* 114 */     this.timeZone = timeZone;
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
/*     */   public DateTimeFormatter createDateTimeFormatter() {
/* 126 */     return createDateTimeFormatter(DateTimeFormat.mediumDateTime());
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
/*     */   public DateTimeFormatter createDateTimeFormatter(DateTimeFormatter fallbackFormatter) {
/* 138 */     DateTimeFormatter dateTimeFormatter = null;
/* 139 */     if (StringUtils.hasLength(this.pattern)) {
/* 140 */       dateTimeFormatter = DateTimeFormat.forPattern(this.pattern);
/*     */     }
/* 142 */     else if (this.iso != null && this.iso != DateTimeFormat.ISO.NONE) {
/* 143 */       switch (this.iso) {
/*     */         case DATE:
/* 145 */           dateTimeFormatter = ISODateTimeFormat.date();
/*     */           break;
/*     */         case TIME:
/* 148 */           dateTimeFormatter = ISODateTimeFormat.time();
/*     */           break;
/*     */         case DATE_TIME:
/* 151 */           dateTimeFormatter = ISODateTimeFormat.dateTime();
/*     */           break;
/*     */         default:
/* 154 */           throw new IllegalStateException("Unsupported ISO format: " + this.iso);
/*     */       } 
/*     */     
/* 157 */     } else if (StringUtils.hasLength(this.style)) {
/* 158 */       dateTimeFormatter = DateTimeFormat.forStyle(this.style);
/*     */     } 
/*     */     
/* 161 */     if (dateTimeFormatter != null && this.timeZone != null) {
/* 162 */       dateTimeFormatter = dateTimeFormatter.withZone(DateTimeZone.forTimeZone(this.timeZone));
/*     */     }
/* 164 */     return (dateTimeFormatter != null) ? dateTimeFormatter : fallbackFormatter;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/datetime/joda/DateTimeFormatterFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */