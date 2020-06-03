/*     */ package org.springframework.format.datetime.standard;
/*     */ 
/*     */ import java.time.ZoneId;
/*     */ import java.time.chrono.Chronology;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.util.TimeZone;
/*     */ import org.springframework.context.i18n.LocaleContext;
/*     */ import org.springframework.context.i18n.LocaleContextHolder;
/*     */ import org.springframework.context.i18n.TimeZoneAwareLocaleContext;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DateTimeContext
/*     */ {
/*     */   @Nullable
/*     */   private Chronology chronology;
/*     */   @Nullable
/*     */   private ZoneId timeZone;
/*     */   
/*     */   public void setChronology(@Nullable Chronology chronology) {
/*  51 */     this.chronology = chronology;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Chronology getChronology() {
/*  59 */     return this.chronology;
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
/*     */   public void setTimeZone(@Nullable ZoneId timeZone) {
/*  71 */     this.timeZone = timeZone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ZoneId getTimeZone() {
/*  79 */     return this.timeZone;
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
/*     */   public DateTimeFormatter getFormatter(DateTimeFormatter formatter) {
/*  91 */     if (this.chronology != null) {
/*  92 */       formatter = formatter.withChronology(this.chronology);
/*     */     }
/*  94 */     if (this.timeZone != null) {
/*  95 */       formatter = formatter.withZone(this.timeZone);
/*     */     } else {
/*     */       
/*  98 */       LocaleContext localeContext = LocaleContextHolder.getLocaleContext();
/*  99 */       if (localeContext instanceof TimeZoneAwareLocaleContext) {
/* 100 */         TimeZone timeZone = ((TimeZoneAwareLocaleContext)localeContext).getTimeZone();
/* 101 */         if (timeZone != null) {
/* 102 */           formatter = formatter.withZone(timeZone.toZoneId());
/*     */         }
/*     */       } 
/*     */     } 
/* 106 */     return formatter;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/datetime/standard/DateTimeContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */