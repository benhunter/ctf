/*     */ package org.springframework.format.datetime.joda;
/*     */ 
/*     */ import java.util.TimeZone;
/*     */ import org.joda.time.Chronology;
/*     */ import org.joda.time.DateTimeZone;
/*     */ import org.joda.time.format.DateTimeFormatter;
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
/*     */ 
/*     */ 
/*     */ public class JodaTimeContext
/*     */ {
/*     */   @Nullable
/*     */   private Chronology chronology;
/*     */   @Nullable
/*     */   private DateTimeZone timeZone;
/*     */   
/*     */   public void setChronology(@Nullable Chronology chronology) {
/*  53 */     this.chronology = chronology;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Chronology getChronology() {
/*  61 */     return this.chronology;
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
/*     */   public void setTimeZone(@Nullable DateTimeZone timeZone) {
/*  73 */     this.timeZone = timeZone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public DateTimeZone getTimeZone() {
/*  81 */     return this.timeZone;
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
/*  93 */     if (this.chronology != null) {
/*  94 */       formatter = formatter.withChronology(this.chronology);
/*     */     }
/*  96 */     if (this.timeZone != null) {
/*  97 */       formatter = formatter.withZone(this.timeZone);
/*     */     } else {
/*     */       
/* 100 */       LocaleContext localeContext = LocaleContextHolder.getLocaleContext();
/* 101 */       if (localeContext instanceof TimeZoneAwareLocaleContext) {
/* 102 */         TimeZone timeZone = ((TimeZoneAwareLocaleContext)localeContext).getTimeZone();
/* 103 */         if (timeZone != null) {
/* 104 */           formatter = formatter.withZone(DateTimeZone.forTimeZone(timeZone));
/*     */         }
/*     */       } 
/*     */     } 
/* 108 */     return formatter;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/datetime/joda/JodaTimeContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */