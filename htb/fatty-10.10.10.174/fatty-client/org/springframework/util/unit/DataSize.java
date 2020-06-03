/*     */ package org.springframework.util.unit;
/*     */ 
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public final class DataSize
/*     */   implements Comparable<DataSize>
/*     */ {
/*  39 */   private static final Pattern PATTERN = Pattern.compile("^([+\\-]?\\d+)([a-zA-Z]{0,2})$");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  44 */   private static long BYTES_PER_KB = 1024L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   private static long BYTES_PER_MB = BYTES_PER_KB * 1024L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  54 */   private static long BYTES_PER_GB = BYTES_PER_MB * 1024L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   private static long BYTES_PER_TB = BYTES_PER_GB * 1024L;
/*     */ 
/*     */   
/*     */   private final long bytes;
/*     */ 
/*     */   
/*     */   private DataSize(long bytes) {
/*  66 */     this.bytes = bytes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DataSize ofBytes(long bytes) {
/*  76 */     return new DataSize(bytes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DataSize ofKilobytes(long kilobytes) {
/*  85 */     return new DataSize(Math.multiplyExact(kilobytes, BYTES_PER_KB));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DataSize ofMegabytes(long megabytes) {
/*  94 */     return new DataSize(Math.multiplyExact(megabytes, BYTES_PER_MB));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DataSize ofGigabytes(long gigabytes) {
/* 103 */     return new DataSize(Math.multiplyExact(gigabytes, BYTES_PER_GB));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DataSize ofTerabytes(long terabytes) {
/* 112 */     return new DataSize(Math.multiplyExact(terabytes, BYTES_PER_TB));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DataSize of(long amount, DataUnit unit) {
/* 122 */     Assert.notNull(unit, "Unit must not be null");
/* 123 */     return new DataSize(Math.multiplyExact(amount, unit.size().toBytes()));
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
/*     */   public static DataSize parse(CharSequence text) {
/* 141 */     return parse(text, null);
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
/*     */   public static DataSize parse(CharSequence text, @Nullable DataUnit defaultUnit) {
/* 161 */     Assert.notNull(text, "Text must not be null");
/*     */     try {
/* 163 */       Matcher matcher = PATTERN.matcher(text);
/* 164 */       Assert.state(matcher.matches(), "Does not match data size pattern");
/* 165 */       DataUnit unit = determineDataUnit(matcher.group(2), defaultUnit);
/* 166 */       long amount = Long.parseLong(matcher.group(1));
/* 167 */       return of(amount, unit);
/*     */     }
/* 169 */     catch (Exception ex) {
/* 170 */       throw new IllegalArgumentException("'" + text + "' is not a valid data size", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static DataUnit determineDataUnit(String suffix, @Nullable DataUnit defaultUnit) {
/* 175 */     DataUnit defaultUnitToUse = (defaultUnit != null) ? defaultUnit : DataUnit.BYTES;
/* 176 */     return StringUtils.hasLength(suffix) ? DataUnit.fromSuffix(suffix) : defaultUnitToUse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNegative() {
/* 184 */     return (this.bytes < 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long toBytes() {
/* 192 */     return this.bytes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long toKilobytes() {
/* 200 */     return this.bytes / BYTES_PER_KB;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long toMegabytes() {
/* 208 */     return this.bytes / BYTES_PER_MB;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long toGigabytes() {
/* 216 */     return this.bytes / BYTES_PER_GB;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long toTerabytes() {
/* 224 */     return this.bytes / BYTES_PER_TB;
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(DataSize other) {
/* 229 */     return Long.compare(this.bytes, other.bytes);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 234 */     return String.format("%dB", new Object[] { Long.valueOf(this.bytes) });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 240 */     if (this == other) {
/* 241 */       return true;
/*     */     }
/* 243 */     if (other == null || getClass() != other.getClass()) {
/* 244 */       return false;
/*     */     }
/* 246 */     DataSize otherSize = (DataSize)other;
/* 247 */     return (this.bytes == otherSize.bytes);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 252 */     return Long.hashCode(this.bytes);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/unit/DataSize.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */