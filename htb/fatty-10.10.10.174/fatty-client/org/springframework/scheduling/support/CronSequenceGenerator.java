/*     */ package org.springframework.scheduling.support;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.BitSet;
/*     */ import java.util.Calendar;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.TimeZone;
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
/*     */ public class CronSequenceGenerator
/*     */ {
/*     */   private final String expression;
/*     */   @Nullable
/*     */   private final TimeZone timeZone;
/*  64 */   private final BitSet months = new BitSet(12);
/*     */   
/*  66 */   private final BitSet daysOfMonth = new BitSet(31);
/*     */   
/*  68 */   private final BitSet daysOfWeek = new BitSet(7);
/*     */   
/*  70 */   private final BitSet hours = new BitSet(24);
/*     */   
/*  72 */   private final BitSet minutes = new BitSet(60);
/*     */   
/*  74 */   private final BitSet seconds = new BitSet(60);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CronSequenceGenerator(String expression) {
/*  85 */     this(expression, TimeZone.getDefault());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CronSequenceGenerator(String expression, TimeZone timeZone) {
/*  96 */     this.expression = expression;
/*  97 */     this.timeZone = timeZone;
/*  98 */     parse(expression);
/*     */   }
/*     */   
/*     */   private CronSequenceGenerator(String expression, String[] fields) {
/* 102 */     this.expression = expression;
/* 103 */     this.timeZone = null;
/* 104 */     doParse(fields);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String getExpression() {
/* 112 */     return this.expression;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date next(Date date) {
/* 141 */     Calendar calendar = new GregorianCalendar();
/* 142 */     calendar.setTimeZone(this.timeZone);
/* 143 */     calendar.setTime(date);
/*     */ 
/*     */     
/* 146 */     calendar.set(14, 0);
/* 147 */     long originalTimestamp = calendar.getTimeInMillis();
/* 148 */     doNext(calendar, calendar.get(1));
/*     */     
/* 150 */     if (calendar.getTimeInMillis() == originalTimestamp) {
/*     */       
/* 152 */       calendar.add(13, 1);
/* 153 */       doNext(calendar, calendar.get(1));
/*     */     } 
/*     */     
/* 156 */     return calendar.getTime();
/*     */   }
/*     */   
/*     */   private void doNext(Calendar calendar, int dot) {
/* 160 */     List<Integer> resets = new ArrayList<>();
/*     */     
/* 162 */     int second = calendar.get(13);
/* 163 */     List<Integer> emptyList = Collections.emptyList();
/* 164 */     int updateSecond = findNext(this.seconds, second, calendar, 13, 12, emptyList);
/* 165 */     if (second == updateSecond) {
/* 166 */       resets.add(Integer.valueOf(13));
/*     */     }
/*     */     
/* 169 */     int minute = calendar.get(12);
/* 170 */     int updateMinute = findNext(this.minutes, minute, calendar, 12, 11, resets);
/* 171 */     if (minute == updateMinute) {
/* 172 */       resets.add(Integer.valueOf(12));
/*     */     } else {
/*     */       
/* 175 */       doNext(calendar, dot);
/*     */     } 
/*     */     
/* 178 */     int hour = calendar.get(11);
/* 179 */     int updateHour = findNext(this.hours, hour, calendar, 11, 7, resets);
/* 180 */     if (hour == updateHour) {
/* 181 */       resets.add(Integer.valueOf(11));
/*     */     } else {
/*     */       
/* 184 */       doNext(calendar, dot);
/*     */     } 
/*     */     
/* 187 */     int dayOfWeek = calendar.get(7);
/* 188 */     int dayOfMonth = calendar.get(5);
/* 189 */     int updateDayOfMonth = findNextDay(calendar, this.daysOfMonth, dayOfMonth, this.daysOfWeek, dayOfWeek, resets);
/* 190 */     if (dayOfMonth == updateDayOfMonth) {
/* 191 */       resets.add(Integer.valueOf(5));
/*     */     } else {
/*     */       
/* 194 */       doNext(calendar, dot);
/*     */     } 
/*     */     
/* 197 */     int month = calendar.get(2);
/* 198 */     int updateMonth = findNext(this.months, month, calendar, 2, 1, resets);
/* 199 */     if (month != updateMonth) {
/* 200 */       if (calendar.get(1) - dot > 4) {
/* 201 */         throw new IllegalArgumentException("Invalid cron expression \"" + this.expression + "\" led to runaway search for next trigger");
/*     */       }
/*     */       
/* 204 */       doNext(calendar, dot);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int findNextDay(Calendar calendar, BitSet daysOfMonth, int dayOfMonth, BitSet daysOfWeek, int dayOfWeek, List<Integer> resets) {
/* 212 */     int count = 0;
/* 213 */     int max = 366;
/*     */ 
/*     */     
/* 216 */     while ((!daysOfMonth.get(dayOfMonth) || !daysOfWeek.get(dayOfWeek - 1)) && count++ < max) {
/* 217 */       calendar.add(5, 1);
/* 218 */       dayOfMonth = calendar.get(5);
/* 219 */       dayOfWeek = calendar.get(7);
/* 220 */       reset(calendar, resets);
/*     */     } 
/* 222 */     if (count >= max) {
/* 223 */       throw new IllegalArgumentException("Overflow in day for expression \"" + this.expression + "\"");
/*     */     }
/* 225 */     return dayOfMonth;
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
/*     */   private int findNext(BitSet bits, int value, Calendar calendar, int field, int nextField, List<Integer> lowerOrders) {
/* 241 */     int nextValue = bits.nextSetBit(value);
/*     */     
/* 243 */     if (nextValue == -1) {
/* 244 */       calendar.add(nextField, 1);
/* 245 */       reset(calendar, Collections.singletonList(Integer.valueOf(field)));
/* 246 */       nextValue = bits.nextSetBit(0);
/*     */     } 
/* 248 */     if (nextValue != value) {
/* 249 */       calendar.set(field, nextValue);
/* 250 */       reset(calendar, lowerOrders);
/*     */     } 
/* 252 */     return nextValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void reset(Calendar calendar, List<Integer> fields) {
/* 259 */     for (Iterator<Integer> iterator = fields.iterator(); iterator.hasNext(); ) { int field = ((Integer)iterator.next()).intValue();
/* 260 */       calendar.set(field, (field == 5) ? 1 : 0); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void parse(String expression) throws IllegalArgumentException {
/* 271 */     String[] fields = StringUtils.tokenizeToStringArray(expression, " ");
/* 272 */     if (!areValidCronFields(fields))
/* 273 */       throw new IllegalArgumentException(String.format("Cron expression must consist of 6 fields (found %d in \"%s\")", new Object[] {
/* 274 */               Integer.valueOf(fields.length), expression
/*     */             })); 
/* 276 */     doParse(fields);
/*     */   }
/*     */   
/*     */   private void doParse(String[] fields) {
/* 280 */     setNumberHits(this.seconds, fields[0], 0, 60);
/* 281 */     setNumberHits(this.minutes, fields[1], 0, 60);
/* 282 */     setNumberHits(this.hours, fields[2], 0, 24);
/* 283 */     setDaysOfMonth(this.daysOfMonth, fields[3]);
/* 284 */     setMonths(this.months, fields[4]);
/* 285 */     setDays(this.daysOfWeek, replaceOrdinals(fields[5], "SUN,MON,TUE,WED,THU,FRI,SAT"), 8);
/*     */     
/* 287 */     if (this.daysOfWeek.get(7)) {
/*     */       
/* 289 */       this.daysOfWeek.set(0);
/* 290 */       this.daysOfWeek.clear(7);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String replaceOrdinals(String value, String commaSeparatedList) {
/* 300 */     String[] list = StringUtils.commaDelimitedListToStringArray(commaSeparatedList);
/* 301 */     for (int i = 0; i < list.length; i++) {
/* 302 */       String item = list[i].toUpperCase();
/* 303 */       value = StringUtils.replace(value.toUpperCase(), item, "" + i);
/*     */     } 
/* 305 */     return value;
/*     */   }
/*     */   
/*     */   private void setDaysOfMonth(BitSet bits, String field) {
/* 309 */     int max = 31;
/*     */     
/* 311 */     setDays(bits, field, max + 1);
/*     */     
/* 313 */     bits.clear(0);
/*     */   }
/*     */   
/*     */   private void setDays(BitSet bits, String field, int max) {
/* 317 */     if (field.contains("?")) {
/* 318 */       field = "*";
/*     */     }
/* 320 */     setNumberHits(bits, field, 0, max);
/*     */   }
/*     */   
/*     */   private void setMonths(BitSet bits, String value) {
/* 324 */     int max = 12;
/* 325 */     value = replaceOrdinals(value, "FOO,JAN,FEB,MAR,APR,MAY,JUN,JUL,AUG,SEP,OCT,NOV,DEC");
/* 326 */     BitSet months = new BitSet(13);
/*     */     
/* 328 */     setNumberHits(months, value, 1, max + 1);
/*     */     
/* 330 */     for (int i = 1; i <= max; i++) {
/* 331 */       if (months.get(i)) {
/* 332 */         bits.set(i - 1);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setNumberHits(BitSet bits, String value, int min, int max) {
/* 338 */     String[] fields = StringUtils.delimitedListToStringArray(value, ",");
/* 339 */     for (String field : fields) {
/* 340 */       if (!field.contains("/")) {
/*     */         
/* 342 */         int[] range = getRange(field, min, max);
/* 343 */         bits.set(range[0], range[1] + 1);
/*     */       } else {
/*     */         
/* 346 */         String[] split = StringUtils.delimitedListToStringArray(field, "/");
/* 347 */         if (split.length > 2) {
/* 348 */           throw new IllegalArgumentException("Incrementer has more than two fields: '" + field + "' in expression \"" + this.expression + "\"");
/*     */         }
/*     */         
/* 351 */         int[] range = getRange(split[0], min, max);
/* 352 */         if (!split[0].contains("-")) {
/* 353 */           range[1] = max - 1;
/*     */         }
/* 355 */         int delta = Integer.parseInt(split[1]);
/* 356 */         if (delta <= 0) {
/* 357 */           throw new IllegalArgumentException("Incrementer delta must be 1 or higher: '" + field + "' in expression \"" + this.expression + "\"");
/*     */         }
/*     */         int i;
/* 360 */         for (i = range[0]; i <= range[1]; i += delta) {
/* 361 */           bits.set(i);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private int[] getRange(String field, int min, int max) {
/* 368 */     int[] result = new int[2];
/* 369 */     if (field.contains("*")) {
/* 370 */       result[0] = min;
/* 371 */       result[1] = max - 1;
/* 372 */       return result;
/*     */     } 
/* 374 */     if (!field.contains("-")) {
/* 375 */       result[1] = Integer.valueOf(field).intValue(); result[0] = Integer.valueOf(field).intValue();
/*     */     } else {
/*     */       
/* 378 */       String[] split = StringUtils.delimitedListToStringArray(field, "-");
/* 379 */       if (split.length > 2) {
/* 380 */         throw new IllegalArgumentException("Range has more than two fields: '" + field + "' in expression \"" + this.expression + "\"");
/*     */       }
/*     */       
/* 383 */       result[0] = Integer.valueOf(split[0]).intValue();
/* 384 */       result[1] = Integer.valueOf(split[1]).intValue();
/*     */     } 
/* 386 */     if (result[0] >= max || result[1] >= max) {
/* 387 */       throw new IllegalArgumentException("Range exceeds maximum (" + max + "): '" + field + "' in expression \"" + this.expression + "\"");
/*     */     }
/*     */     
/* 390 */     if (result[0] < min || result[1] < min) {
/* 391 */       throw new IllegalArgumentException("Range less than minimum (" + min + "): '" + field + "' in expression \"" + this.expression + "\"");
/*     */     }
/*     */     
/* 394 */     if (result[0] > result[1]) {
/* 395 */       throw new IllegalArgumentException("Invalid inverted range: '" + field + "' in expression \"" + this.expression + "\"");
/*     */     }
/*     */     
/* 398 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isValidExpression(@Nullable String expression) {
/* 409 */     if (expression == null) {
/* 410 */       return false;
/*     */     }
/* 412 */     String[] fields = StringUtils.tokenizeToStringArray(expression, " ");
/* 413 */     if (!areValidCronFields(fields)) {
/* 414 */       return false;
/*     */     }
/*     */     try {
/* 417 */       new CronSequenceGenerator(expression, fields);
/* 418 */       return true;
/*     */     }
/* 420 */     catch (IllegalArgumentException ex) {
/* 421 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean areValidCronFields(@Nullable String[] fields) {
/* 426 */     return (fields != null && fields.length == 6);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 432 */     if (this == other) {
/* 433 */       return true;
/*     */     }
/* 435 */     if (!(other instanceof CronSequenceGenerator)) {
/* 436 */       return false;
/*     */     }
/* 438 */     CronSequenceGenerator otherCron = (CronSequenceGenerator)other;
/* 439 */     return (this.months.equals(otherCron.months) && this.daysOfMonth.equals(otherCron.daysOfMonth) && this.daysOfWeek
/* 440 */       .equals(otherCron.daysOfWeek) && this.hours.equals(otherCron.hours) && this.minutes
/* 441 */       .equals(otherCron.minutes) && this.seconds.equals(otherCron.seconds));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 446 */     return 17 * this.months.hashCode() + 29 * this.daysOfMonth.hashCode() + 37 * this.daysOfWeek.hashCode() + 41 * this.hours
/* 447 */       .hashCode() + 53 * this.minutes.hashCode() + 61 * this.seconds.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 452 */     return getClass().getSimpleName() + ": " + this.expression;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/support/CronSequenceGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */