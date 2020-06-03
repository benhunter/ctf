/*     */ package org.springframework.format.datetime;
/*     */ 
/*     */ import java.text.DateFormat;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.EnumMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ import org.springframework.format.Formatter;
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
/*     */ public class DateFormatter
/*     */   implements Formatter<Date>
/*     */ {
/*  46 */   private static final TimeZone UTC = TimeZone.getTimeZone("UTC");
/*     */   
/*     */   private static final Map<DateTimeFormat.ISO, String> ISO_PATTERNS;
/*     */   
/*     */   static {
/*  51 */     Map<DateTimeFormat.ISO, String> formats = new EnumMap<>(DateTimeFormat.ISO.class);
/*  52 */     formats.put(DateTimeFormat.ISO.DATE, "yyyy-MM-dd");
/*  53 */     formats.put(DateTimeFormat.ISO.TIME, "HH:mm:ss.SSSXXX");
/*  54 */     formats.put(DateTimeFormat.ISO.DATE_TIME, "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
/*  55 */     ISO_PATTERNS = Collections.unmodifiableMap(formats);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String pattern;
/*     */   
/*  62 */   private int style = 2;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String stylePattern;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private DateTimeFormat.ISO iso;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private TimeZone timeZone;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean lenient = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public DateFormatter(String pattern) {
/*  86 */     this.pattern = pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPattern(String pattern) {
/*  95 */     this.pattern = pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIso(DateTimeFormat.ISO iso) {
/* 104 */     this.iso = iso;
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
/*     */   public void setStyle(int style) {
/* 117 */     this.style = style;
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
/*     */   public void setStylePattern(String stylePattern) {
/* 135 */     this.stylePattern = stylePattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeZone(TimeZone timeZone) {
/* 142 */     this.timeZone = timeZone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLenient(boolean lenient) {
/* 151 */     this.lenient = lenient;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String print(Date date, Locale locale) {
/* 157 */     return getDateFormat(locale).format(date);
/*     */   }
/*     */ 
/*     */   
/*     */   public Date parse(String text, Locale locale) throws ParseException {
/* 162 */     return getDateFormat(locale).parse(text);
/*     */   }
/*     */ 
/*     */   
/*     */   protected DateFormat getDateFormat(Locale locale) {
/* 167 */     DateFormat dateFormat = createDateFormat(locale);
/* 168 */     if (this.timeZone != null) {
/* 169 */       dateFormat.setTimeZone(this.timeZone);
/*     */     }
/* 171 */     dateFormat.setLenient(this.lenient);
/* 172 */     return dateFormat;
/*     */   }
/*     */   
/*     */   private DateFormat createDateFormat(Locale locale) {
/* 176 */     if (StringUtils.hasLength(this.pattern)) {
/* 177 */       return new SimpleDateFormat(this.pattern, locale);
/*     */     }
/* 179 */     if (this.iso != null && this.iso != DateTimeFormat.ISO.NONE) {
/* 180 */       String pattern = ISO_PATTERNS.get(this.iso);
/* 181 */       if (pattern == null) {
/* 182 */         throw new IllegalStateException("Unsupported ISO format " + this.iso);
/*     */       }
/* 184 */       SimpleDateFormat format = new SimpleDateFormat(pattern);
/* 185 */       format.setTimeZone(UTC);
/* 186 */       return format;
/*     */     } 
/* 188 */     if (StringUtils.hasLength(this.stylePattern)) {
/* 189 */       int dateStyle = getStylePatternForChar(0);
/* 190 */       int timeStyle = getStylePatternForChar(1);
/* 191 */       if (dateStyle != -1 && timeStyle != -1) {
/* 192 */         return DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
/*     */       }
/* 194 */       if (dateStyle != -1) {
/* 195 */         return DateFormat.getDateInstance(dateStyle, locale);
/*     */       }
/* 197 */       if (timeStyle != -1) {
/* 198 */         return DateFormat.getTimeInstance(timeStyle, locale);
/*     */       }
/* 200 */       throw new IllegalStateException("Unsupported style pattern '" + this.stylePattern + "'");
/*     */     } 
/*     */     
/* 203 */     return DateFormat.getDateInstance(this.style, locale);
/*     */   }
/*     */   
/*     */   private int getStylePatternForChar(int index) {
/* 207 */     if (this.stylePattern != null && this.stylePattern.length() > index) {
/* 208 */       switch (this.stylePattern.charAt(index)) { case 'S':
/* 209 */           return 3;
/* 210 */         case 'M': return 2;
/* 211 */         case 'L': return 1;
/* 212 */         case 'F': return 0;
/* 213 */         case '-': return -1; }
/*     */     
/*     */     }
/* 216 */     throw new IllegalStateException("Unsupported style pattern '" + this.stylePattern + "'");
/*     */   }
/*     */   
/*     */   public DateFormatter() {}
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/datetime/DateFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */