/*     */ package org.springframework.http;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.time.ZonedDateTime;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.time.format.DateTimeParseException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ContentDisposition
/*     */ {
/*     */   @Nullable
/*     */   private final String type;
/*     */   @Nullable
/*     */   private final String name;
/*     */   @Nullable
/*     */   private final String filename;
/*     */   @Nullable
/*     */   private final Charset charset;
/*     */   @Nullable
/*     */   private final Long size;
/*     */   @Nullable
/*     */   private final ZonedDateTime creationDate;
/*     */   @Nullable
/*     */   private final ZonedDateTime modificationDate;
/*     */   @Nullable
/*     */   private final ZonedDateTime readDate;
/*     */   
/*     */   private ContentDisposition(@Nullable String type, @Nullable String name, @Nullable String filename, @Nullable Charset charset, @Nullable Long size, @Nullable ZonedDateTime creationDate, @Nullable ZonedDateTime modificationDate, @Nullable ZonedDateTime readDate) {
/*  77 */     this.type = type;
/*  78 */     this.name = name;
/*  79 */     this.filename = filename;
/*  80 */     this.charset = charset;
/*  81 */     this.size = size;
/*  82 */     this.creationDate = creationDate;
/*  83 */     this.modificationDate = modificationDate;
/*  84 */     this.readDate = readDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getType() {
/*  94 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getName() {
/* 102 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getFilename() {
/* 111 */     return this.filename;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Charset getCharset() {
/* 119 */     return this.charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Long getSize() {
/* 127 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ZonedDateTime getCreationDate() {
/* 135 */     return this.creationDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ZonedDateTime getModificationDate() {
/* 143 */     return this.modificationDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ZonedDateTime getReadDate() {
/* 151 */     return this.readDate;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 157 */     if (this == other) {
/* 158 */       return true;
/*     */     }
/* 160 */     if (!(other instanceof ContentDisposition)) {
/* 161 */       return false;
/*     */     }
/* 163 */     ContentDisposition otherCd = (ContentDisposition)other;
/* 164 */     return (ObjectUtils.nullSafeEquals(this.type, otherCd.type) && 
/* 165 */       ObjectUtils.nullSafeEquals(this.name, otherCd.name) && 
/* 166 */       ObjectUtils.nullSafeEquals(this.filename, otherCd.filename) && 
/* 167 */       ObjectUtils.nullSafeEquals(this.charset, otherCd.charset) && 
/* 168 */       ObjectUtils.nullSafeEquals(this.size, otherCd.size) && 
/* 169 */       ObjectUtils.nullSafeEquals(this.creationDate, otherCd.creationDate) && 
/* 170 */       ObjectUtils.nullSafeEquals(this.modificationDate, otherCd.modificationDate) && 
/* 171 */       ObjectUtils.nullSafeEquals(this.readDate, otherCd.readDate));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 176 */     int result = ObjectUtils.nullSafeHashCode(this.type);
/* 177 */     result = 31 * result + ObjectUtils.nullSafeHashCode(this.name);
/* 178 */     result = 31 * result + ObjectUtils.nullSafeHashCode(this.filename);
/* 179 */     result = 31 * result + ObjectUtils.nullSafeHashCode(this.charset);
/* 180 */     result = 31 * result + ObjectUtils.nullSafeHashCode(this.size);
/* 181 */     result = 31 * result + ((this.creationDate != null) ? this.creationDate.hashCode() : 0);
/* 182 */     result = 31 * result + ((this.modificationDate != null) ? this.modificationDate.hashCode() : 0);
/* 183 */     result = 31 * result + ((this.readDate != null) ? this.readDate.hashCode() : 0);
/* 184 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 193 */     StringBuilder sb = new StringBuilder();
/* 194 */     if (this.type != null) {
/* 195 */       sb.append(this.type);
/*     */     }
/* 197 */     if (this.name != null) {
/* 198 */       sb.append("; name=\"");
/* 199 */       sb.append(this.name).append('"');
/*     */     } 
/* 201 */     if (this.filename != null) {
/* 202 */       if (this.charset == null || StandardCharsets.US_ASCII.equals(this.charset)) {
/* 203 */         sb.append("; filename=\"");
/* 204 */         sb.append(this.filename).append('"');
/*     */       } else {
/*     */         
/* 207 */         sb.append("; filename*=");
/* 208 */         sb.append(encodeHeaderFieldParam(this.filename, this.charset));
/*     */       } 
/*     */     }
/* 211 */     if (this.size != null) {
/* 212 */       sb.append("; size=");
/* 213 */       sb.append(this.size);
/*     */     } 
/* 215 */     if (this.creationDate != null) {
/* 216 */       sb.append("; creation-date=\"");
/* 217 */       sb.append(DateTimeFormatter.RFC_1123_DATE_TIME.format(this.creationDate));
/* 218 */       sb.append('"');
/*     */     } 
/* 220 */     if (this.modificationDate != null) {
/* 221 */       sb.append("; modification-date=\"");
/* 222 */       sb.append(DateTimeFormatter.RFC_1123_DATE_TIME.format(this.modificationDate));
/* 223 */       sb.append('"');
/*     */     } 
/* 225 */     if (this.readDate != null) {
/* 226 */       sb.append("; read-date=\"");
/* 227 */       sb.append(DateTimeFormatter.RFC_1123_DATE_TIME.format(this.readDate));
/* 228 */       sb.append('"');
/*     */     } 
/* 230 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Builder builder(String type) {
/* 241 */     return new BuilderImpl(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ContentDisposition empty() {
/* 248 */     return new ContentDisposition("", null, null, null, null, null, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ContentDisposition parse(String contentDisposition) {
/* 258 */     List<String> parts = tokenize(contentDisposition);
/* 259 */     String type = parts.get(0);
/* 260 */     String name = null;
/* 261 */     String filename = null;
/* 262 */     Charset charset = null;
/* 263 */     Long size = null;
/* 264 */     ZonedDateTime creationDate = null;
/* 265 */     ZonedDateTime modificationDate = null;
/* 266 */     ZonedDateTime readDate = null;
/* 267 */     for (int i = 1; i < parts.size(); i++) {
/* 268 */       String part = parts.get(i);
/* 269 */       int eqIndex = part.indexOf('=');
/* 270 */       if (eqIndex != -1) {
/* 271 */         String attribute = part.substring(0, eqIndex);
/*     */ 
/*     */         
/* 274 */         String value = (part.startsWith("\"", eqIndex + 1) && part.endsWith("\"")) ? part.substring(eqIndex + 2, part.length() - 1) : part.substring(eqIndex + 1, part.length());
/* 275 */         if (attribute.equals("name")) {
/* 276 */           name = value;
/*     */         }
/* 278 */         else if (attribute.equals("filename*")) {
/* 279 */           filename = decodeHeaderFieldParam(value);
/* 280 */           charset = Charset.forName(value.substring(0, value.indexOf('\'')));
/* 281 */           Assert.isTrue((StandardCharsets.UTF_8.equals(charset) || StandardCharsets.ISO_8859_1.equals(charset)), "Charset should be UTF-8 or ISO-8859-1");
/*     */         
/*     */         }
/* 284 */         else if (attribute.equals("filename") && filename == null) {
/* 285 */           filename = value;
/*     */         }
/* 287 */         else if (attribute.equals("size")) {
/* 288 */           size = Long.valueOf(Long.parseLong(value));
/*     */         }
/* 290 */         else if (attribute.equals("creation-date")) {
/*     */           try {
/* 292 */             creationDate = ZonedDateTime.parse(value, DateTimeFormatter.RFC_1123_DATE_TIME);
/*     */           }
/* 294 */           catch (DateTimeParseException dateTimeParseException) {}
/*     */ 
/*     */         
/*     */         }
/* 298 */         else if (attribute.equals("modification-date")) {
/*     */           try {
/* 300 */             modificationDate = ZonedDateTime.parse(value, DateTimeFormatter.RFC_1123_DATE_TIME);
/*     */           }
/* 302 */           catch (DateTimeParseException dateTimeParseException) {}
/*     */ 
/*     */         
/*     */         }
/* 306 */         else if (attribute.equals("read-date")) {
/*     */           try {
/* 308 */             readDate = ZonedDateTime.parse(value, DateTimeFormatter.RFC_1123_DATE_TIME);
/*     */           }
/* 310 */           catch (DateTimeParseException dateTimeParseException) {}
/*     */         }
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 316 */         throw new IllegalArgumentException("Invalid content disposition format");
/*     */       } 
/*     */     } 
/* 319 */     return new ContentDisposition(type, name, filename, charset, size, creationDate, modificationDate, readDate);
/*     */   }
/*     */   
/*     */   private static List<String> tokenize(String headerValue) {
/* 323 */     int index = headerValue.indexOf(';');
/* 324 */     String type = ((index >= 0) ? headerValue.substring(0, index) : headerValue).trim();
/* 325 */     if (type.isEmpty()) {
/* 326 */       throw new IllegalArgumentException("Content-Disposition header must not be empty");
/*     */     }
/* 328 */     List<String> parts = new ArrayList<>();
/* 329 */     parts.add(type);
/* 330 */     if (index >= 0) {
/*     */       do {
/* 332 */         int nextIndex = index + 1;
/* 333 */         boolean quoted = false;
/* 334 */         boolean escaped = false;
/* 335 */         while (nextIndex < headerValue.length()) {
/* 336 */           char ch = headerValue.charAt(nextIndex);
/* 337 */           if (ch == ';') {
/* 338 */             if (!quoted) {
/*     */               break;
/*     */             }
/*     */           }
/* 342 */           else if (!escaped && ch == '"') {
/* 343 */             quoted = !quoted;
/*     */           } 
/* 345 */           escaped = (!escaped && ch == '\\');
/* 346 */           nextIndex++;
/*     */         } 
/* 348 */         String part = headerValue.substring(index + 1, nextIndex).trim();
/* 349 */         if (!part.isEmpty()) {
/* 350 */           parts.add(part);
/*     */         }
/* 352 */         index = nextIndex;
/*     */       }
/* 354 */       while (index < headerValue.length());
/*     */     }
/* 356 */     return parts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String decodeHeaderFieldParam(String input) {
/* 367 */     Assert.notNull(input, "Input String should not be null");
/* 368 */     int firstQuoteIndex = input.indexOf('\'');
/* 369 */     int secondQuoteIndex = input.indexOf('\'', firstQuoteIndex + 1);
/*     */     
/* 371 */     if (firstQuoteIndex == -1 || secondQuoteIndex == -1) {
/* 372 */       return input;
/*     */     }
/* 374 */     Charset charset = Charset.forName(input.substring(0, firstQuoteIndex));
/* 375 */     Assert.isTrue((StandardCharsets.UTF_8.equals(charset) || StandardCharsets.ISO_8859_1.equals(charset)), "Charset should be UTF-8 or ISO-8859-1");
/*     */     
/* 377 */     byte[] value = input.substring(secondQuoteIndex + 1, input.length()).getBytes(charset);
/* 378 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 379 */     int index = 0;
/* 380 */     while (index < value.length) {
/* 381 */       byte b = value[index];
/* 382 */       if (isRFC5987AttrChar(b)) {
/* 383 */         bos.write((char)b);
/* 384 */         index++; continue;
/*     */       } 
/* 386 */       if (b == 37) {
/* 387 */         char[] array = { (char)value[index + 1], (char)value[index + 2] };
/* 388 */         bos.write(Integer.parseInt(String.valueOf(array), 16));
/* 389 */         index += 3;
/*     */         continue;
/*     */       } 
/* 392 */       throw new IllegalArgumentException("Invalid header field parameter format (as defined in RFC 5987)");
/*     */     } 
/*     */     
/* 395 */     return new String(bos.toByteArray(), charset);
/*     */   }
/*     */   
/*     */   private static boolean isRFC5987AttrChar(byte c) {
/* 399 */     return ((c >= 48 && c <= 57) || (c >= 97 && c <= 122) || (c >= 65 && c <= 90) || c == 33 || c == 35 || c == 36 || c == 38 || c == 43 || c == 45 || c == 46 || c == 94 || c == 95 || c == 96 || c == 124 || c == 126);
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
/*     */   private static String encodeHeaderFieldParam(String input, Charset charset) {
/* 413 */     Assert.notNull(input, "Input String should not be null");
/* 414 */     Assert.notNull(charset, "Charset should not be null");
/* 415 */     if (StandardCharsets.US_ASCII.equals(charset)) {
/* 416 */       return input;
/*     */     }
/* 418 */     Assert.isTrue((StandardCharsets.UTF_8.equals(charset) || StandardCharsets.ISO_8859_1.equals(charset)), "Charset should be UTF-8 or ISO-8859-1");
/*     */     
/* 420 */     byte[] source = input.getBytes(charset);
/* 421 */     int len = source.length;
/* 422 */     StringBuilder sb = new StringBuilder(len << 1);
/* 423 */     sb.append(charset.name());
/* 424 */     sb.append("''");
/* 425 */     for (byte b : source) {
/* 426 */       if (isRFC5987AttrChar(b)) {
/* 427 */         sb.append((char)b);
/*     */       } else {
/*     */         
/* 430 */         sb.append('%');
/* 431 */         char hex1 = Character.toUpperCase(Character.forDigit(b >> 4 & 0xF, 16));
/* 432 */         char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
/* 433 */         sb.append(hex1);
/* 434 */         sb.append(hex2);
/*     */       } 
/*     */     } 
/* 437 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class BuilderImpl
/*     */     implements Builder
/*     */   {
/*     */     private String type;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private String name;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private String filename;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private Charset charset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private Long size;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private ZonedDateTime creationDate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private ZonedDateTime modificationDate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private ZonedDateTime readDate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public BuilderImpl(String type) {
/* 521 */       Assert.hasText(type, "'type' must not be not empty");
/* 522 */       this.type = type;
/*     */     }
/*     */ 
/*     */     
/*     */     public ContentDisposition.Builder name(String name) {
/* 527 */       this.name = name;
/* 528 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ContentDisposition.Builder filename(String filename) {
/* 533 */       this.filename = filename;
/* 534 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ContentDisposition.Builder filename(String filename, Charset charset) {
/* 539 */       this.filename = filename;
/* 540 */       this.charset = charset;
/* 541 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ContentDisposition.Builder size(Long size) {
/* 546 */       this.size = size;
/* 547 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ContentDisposition.Builder creationDate(ZonedDateTime creationDate) {
/* 552 */       this.creationDate = creationDate;
/* 553 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ContentDisposition.Builder modificationDate(ZonedDateTime modificationDate) {
/* 558 */       this.modificationDate = modificationDate;
/* 559 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ContentDisposition.Builder readDate(ZonedDateTime readDate) {
/* 564 */       this.readDate = readDate;
/* 565 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ContentDisposition build() {
/* 570 */       return new ContentDisposition(this.type, this.name, this.filename, this.charset, this.size, this.creationDate, this.modificationDate, this.readDate);
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface Builder {
/*     */     Builder name(String param1String);
/*     */     
/*     */     Builder filename(String param1String);
/*     */     
/*     */     Builder filename(String param1String, Charset param1Charset);
/*     */     
/*     */     Builder size(Long param1Long);
/*     */     
/*     */     Builder creationDate(ZonedDateTime param1ZonedDateTime);
/*     */     
/*     */     Builder modificationDate(ZonedDateTime param1ZonedDateTime);
/*     */     
/*     */     Builder readDate(ZonedDateTime param1ZonedDateTime);
/*     */     
/*     */     ContentDisposition build();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/ContentDisposition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */