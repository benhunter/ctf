/*     */ package org.springframework.http;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.springframework.core.io.InputStreamResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.support.ResourceRegion;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ public abstract class HttpRange
/*     */ {
/*     */   private static final int MAX_RANGES = 100;
/*     */   private static final String BYTE_RANGE_PREFIX = "bytes=";
/*     */   
/*     */   public ResourceRegion toResourceRegion(Resource resource) {
/*  63 */     Assert.isTrue((resource.getClass() != InputStreamResource.class), "Cannot convert an InputStreamResource to a ResourceRegion");
/*     */     
/*  65 */     long contentLength = getLengthFor(resource);
/*  66 */     long start = getRangeStart(contentLength);
/*  67 */     long end = getRangeEnd(contentLength);
/*  68 */     return new ResourceRegion(resource, start, end - start + 1L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract long getRangeStart(long paramLong);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract long getRangeEnd(long paramLong);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpRange createByteRange(long firstBytePos) {
/*  93 */     return new ByteRange(firstBytePos, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpRange createByteRange(long firstBytePos, long lastBytePos) {
/* 104 */     return new ByteRange(firstBytePos, Long.valueOf(lastBytePos));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpRange createSuffixRange(long suffixLength) {
/* 114 */     return new SuffixByteRange(suffixLength);
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
/*     */   public static List<HttpRange> parseRanges(@Nullable String ranges) {
/* 126 */     if (!StringUtils.hasLength(ranges)) {
/* 127 */       return Collections.emptyList();
/*     */     }
/* 129 */     if (!ranges.startsWith("bytes=")) {
/* 130 */       throw new IllegalArgumentException("Range '" + ranges + "' does not start with 'bytes='");
/*     */     }
/* 132 */     ranges = ranges.substring("bytes=".length());
/*     */     
/* 134 */     String[] tokens = StringUtils.tokenizeToStringArray(ranges, ",");
/* 135 */     if (tokens.length > 100) {
/* 136 */       throw new IllegalArgumentException("Too many ranges: " + tokens.length);
/*     */     }
/* 138 */     List<HttpRange> result = new ArrayList<>(tokens.length);
/* 139 */     for (String token : tokens) {
/* 140 */       result.add(parseRange(token));
/*     */     }
/* 142 */     return result;
/*     */   }
/*     */   
/*     */   private static HttpRange parseRange(String range) {
/* 146 */     Assert.hasLength(range, "Range String must not be empty");
/* 147 */     int dashIdx = range.indexOf('-');
/* 148 */     if (dashIdx > 0) {
/* 149 */       long firstPos = Long.parseLong(range.substring(0, dashIdx));
/* 150 */       if (dashIdx < range.length() - 1) {
/* 151 */         Long lastPos = Long.valueOf(Long.parseLong(range.substring(dashIdx + 1)));
/* 152 */         return new ByteRange(firstPos, lastPos);
/*     */       } 
/*     */       
/* 155 */       return new ByteRange(firstPos, null);
/*     */     } 
/*     */     
/* 158 */     if (dashIdx == 0) {
/* 159 */       long suffixLength = Long.parseLong(range.substring(1));
/* 160 */       return new SuffixByteRange(suffixLength);
/*     */     } 
/*     */     
/* 163 */     throw new IllegalArgumentException("Range '" + range + "' does not contain \"-\"");
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
/*     */   public static List<ResourceRegion> toResourceRegions(List<HttpRange> ranges, Resource resource) {
/* 177 */     if (CollectionUtils.isEmpty(ranges)) {
/* 178 */       return Collections.emptyList();
/*     */     }
/* 180 */     List<ResourceRegion> regions = new ArrayList<>(ranges.size());
/* 181 */     for (HttpRange range : ranges) {
/* 182 */       regions.add(range.toResourceRegion(resource));
/*     */     }
/* 184 */     if (ranges.size() > 1) {
/* 185 */       long length = getLengthFor(resource);
/* 186 */       long total = 0L;
/* 187 */       for (ResourceRegion region : regions) {
/* 188 */         total += region.getCount();
/*     */       }
/* 190 */       if (total >= length) {
/* 191 */         throw new IllegalArgumentException("The sum of all ranges (" + total + ") should be less than the resource length (" + length + ")");
/*     */       }
/*     */     } 
/*     */     
/* 195 */     return regions;
/*     */   }
/*     */   
/*     */   private static long getLengthFor(Resource resource) {
/*     */     try {
/* 200 */       long contentLength = resource.contentLength();
/* 201 */       Assert.isTrue((contentLength > 0L), "Resource content length should be > 0");
/* 202 */       return contentLength;
/*     */     }
/* 204 */     catch (IOException ex) {
/* 205 */       throw new IllegalArgumentException("Failed to obtain Resource content length", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toString(Collection<HttpRange> ranges) {
/* 216 */     Assert.notEmpty(ranges, "Ranges Collection must not be empty");
/* 217 */     StringBuilder builder = new StringBuilder("bytes=");
/* 218 */     for (Iterator<HttpRange> iterator = ranges.iterator(); iterator.hasNext(); ) {
/* 219 */       HttpRange range = iterator.next();
/* 220 */       builder.append(range);
/* 221 */       if (iterator.hasNext()) {
/* 222 */         builder.append(", ");
/*     */       }
/*     */     } 
/* 225 */     return builder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ByteRange
/*     */     extends HttpRange
/*     */   {
/*     */     private final long firstPos;
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private final Long lastPos;
/*     */ 
/*     */ 
/*     */     
/*     */     public ByteRange(long firstPos, @Nullable Long lastPos) {
/* 243 */       assertPositions(firstPos, lastPos);
/* 244 */       this.firstPos = firstPos;
/* 245 */       this.lastPos = lastPos;
/*     */     }
/*     */     
/*     */     private void assertPositions(long firstBytePos, @Nullable Long lastBytePos) {
/* 249 */       if (firstBytePos < 0L) {
/* 250 */         throw new IllegalArgumentException("Invalid first byte position: " + firstBytePos);
/*     */       }
/* 252 */       if (lastBytePos != null && lastBytePos.longValue() < firstBytePos) {
/* 253 */         throw new IllegalArgumentException("firstBytePosition=" + firstBytePos + " should be less then or equal to lastBytePosition=" + lastBytePos);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public long getRangeStart(long length) {
/* 260 */       return this.firstPos;
/*     */     }
/*     */ 
/*     */     
/*     */     public long getRangeEnd(long length) {
/* 265 */       if (this.lastPos != null && this.lastPos.longValue() < length) {
/* 266 */         return this.lastPos.longValue();
/*     */       }
/*     */       
/* 269 */       return length - 1L;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 275 */       if (this == other) {
/* 276 */         return true;
/*     */       }
/* 278 */       if (!(other instanceof ByteRange)) {
/* 279 */         return false;
/*     */       }
/* 281 */       ByteRange otherRange = (ByteRange)other;
/* 282 */       return (this.firstPos == otherRange.firstPos && 
/* 283 */         ObjectUtils.nullSafeEquals(this.lastPos, otherRange.lastPos));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 288 */       return ObjectUtils.nullSafeHashCode(Long.valueOf(this.firstPos)) * 31 + 
/* 289 */         ObjectUtils.nullSafeHashCode(this.lastPos);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 294 */       StringBuilder builder = new StringBuilder();
/* 295 */       builder.append(this.firstPos);
/* 296 */       builder.append('-');
/* 297 */       if (this.lastPos != null) {
/* 298 */         builder.append(this.lastPos);
/*     */       }
/* 300 */       return builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SuffixByteRange
/*     */     extends HttpRange
/*     */   {
/*     */     private final long suffixLength;
/*     */ 
/*     */ 
/*     */     
/*     */     public SuffixByteRange(long suffixLength) {
/* 315 */       if (suffixLength < 0L) {
/* 316 */         throw new IllegalArgumentException("Invalid suffix length: " + suffixLength);
/*     */       }
/* 318 */       this.suffixLength = suffixLength;
/*     */     }
/*     */ 
/*     */     
/*     */     public long getRangeStart(long length) {
/* 323 */       if (this.suffixLength < length) {
/* 324 */         return length - this.suffixLength;
/*     */       }
/*     */       
/* 327 */       return 0L;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public long getRangeEnd(long length) {
/* 333 */       return length - 1L;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 338 */       if (this == other) {
/* 339 */         return true;
/*     */       }
/* 341 */       if (!(other instanceof SuffixByteRange)) {
/* 342 */         return false;
/*     */       }
/* 344 */       SuffixByteRange otherRange = (SuffixByteRange)other;
/* 345 */       return (this.suffixLength == otherRange.suffixLength);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 350 */       return Long.hashCode(this.suffixLength);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 355 */       return "-" + this.suffixLength;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/HttpRange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */