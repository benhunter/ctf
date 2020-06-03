/*     */ package org.springframework.format.datetime.standard;
/*     */ 
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.time.format.FormatStyle;
/*     */ import java.time.format.ResolverStyle;
/*     */ import java.util.TimeZone;
/*     */ import org.springframework.format.annotation.DateTimeFormat;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ public class DateTimeFormatterFactory
/*     */ {
/*     */   @Nullable
/*     */   private String pattern;
/*     */   @Nullable
/*     */   private DateTimeFormat.ISO iso;
/*     */   @Nullable
/*     */   private FormatStyle dateStyle;
/*     */   @Nullable
/*     */   private FormatStyle timeStyle;
/*     */   @Nullable
/*     */   private TimeZone timeZone;
/*     */   
/*     */   public DateTimeFormatterFactory() {}
/*     */   
/*     */   public DateTimeFormatterFactory(String pattern) {
/*  76 */     this.pattern = pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPattern(String pattern) {
/*  85 */     this.pattern = pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIso(DateTimeFormat.ISO iso) {
/*  93 */     this.iso = iso;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDateStyle(FormatStyle dateStyle) {
/* 100 */     this.dateStyle = dateStyle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeStyle(FormatStyle timeStyle) {
/* 107 */     this.timeStyle = timeStyle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDateTimeStyle(FormatStyle dateTimeStyle) {
/* 114 */     this.dateStyle = dateTimeStyle;
/* 115 */     this.timeStyle = dateTimeStyle;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStylePattern(String style) {
/* 135 */     Assert.isTrue((style.length() == 2), "Style pattern must consist of two characters");
/* 136 */     this.dateStyle = convertStyleCharacter(style.charAt(0));
/* 137 */     this.timeStyle = convertStyleCharacter(style.charAt(1));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private FormatStyle convertStyleCharacter(char c) {
/* 142 */     switch (c) { case 'S':
/* 143 */         return FormatStyle.SHORT;
/* 144 */       case 'M': return FormatStyle.MEDIUM;
/* 145 */       case 'L': return FormatStyle.LONG;
/* 146 */       case 'F': return FormatStyle.FULL;
/* 147 */       case '-': return null; }
/* 148 */      throw new IllegalArgumentException("Invalid style character '" + c + "'");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeZone(TimeZone timeZone) {
/* 157 */     this.timeZone = timeZone;
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
/* 169 */     return createDateTimeFormatter(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));
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
/* 181 */     DateTimeFormatter dateTimeFormatter = null;
/* 182 */     if (StringUtils.hasLength(this.pattern)) {
/*     */ 
/*     */ 
/*     */       
/* 186 */       String patternToUse = StringUtils.replace(this.pattern, "yy", "uu");
/* 187 */       dateTimeFormatter = DateTimeFormatter.ofPattern(patternToUse).withResolverStyle(ResolverStyle.STRICT);
/*     */     }
/* 189 */     else if (this.iso != null && this.iso != DateTimeFormat.ISO.NONE) {
/* 190 */       switch (this.iso) {
/*     */         case DATE:
/* 192 */           dateTimeFormatter = DateTimeFormatter.ISO_DATE;
/*     */           break;
/*     */         case TIME:
/* 195 */           dateTimeFormatter = DateTimeFormatter.ISO_TIME;
/*     */           break;
/*     */         case DATE_TIME:
/* 198 */           dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
/*     */           break;
/*     */         default:
/* 201 */           throw new IllegalStateException("Unsupported ISO format: " + this.iso);
/*     */       } 
/*     */     
/* 204 */     } else if (this.dateStyle != null && this.timeStyle != null) {
/* 205 */       dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(this.dateStyle, this.timeStyle);
/*     */     }
/* 207 */     else if (this.dateStyle != null) {
/* 208 */       dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(this.dateStyle);
/*     */     }
/* 210 */     else if (this.timeStyle != null) {
/* 211 */       dateTimeFormatter = DateTimeFormatter.ofLocalizedTime(this.timeStyle);
/*     */     } 
/*     */     
/* 214 */     if (dateTimeFormatter != null && this.timeZone != null) {
/* 215 */       dateTimeFormatter = dateTimeFormatter.withZone(this.timeZone.toZoneId());
/*     */     }
/* 217 */     return (dateTimeFormatter != null) ? dateTimeFormatter : fallbackFormatter;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/datetime/standard/DateTimeFormatterFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */