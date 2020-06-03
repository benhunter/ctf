/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import java.security.SecureRandom;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.stream.Collectors;
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
/*     */ public abstract class MimeTypeUtils
/*     */ {
/*  46 */   private static final byte[] BOUNDARY_CHARS = new byte[] { 45, 95, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   public static final Comparator<MimeType> SPECIFICITY_COMPARATOR = new MimeType.SpecificityComparator<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 162 */   public static final MimeType ALL = MimeType.valueOf("*/*"); public static final String ALL_VALUE = "*/*";
/* 163 */   public static final MimeType APPLICATION_JSON = MimeType.valueOf("application/json"); public static final String APPLICATION_JSON_VALUE = "application/json";
/* 164 */   public static final MimeType APPLICATION_OCTET_STREAM = MimeType.valueOf("application/octet-stream"); public static final String APPLICATION_OCTET_STREAM_VALUE = "application/octet-stream";
/* 165 */   public static final MimeType APPLICATION_XML = MimeType.valueOf("application/xml"); public static final String APPLICATION_XML_VALUE = "application/xml";
/* 166 */   public static final MimeType IMAGE_GIF = MimeType.valueOf("image/gif"); public static final String IMAGE_GIF_VALUE = "image/gif";
/* 167 */   public static final MimeType IMAGE_JPEG = MimeType.valueOf("image/jpeg"); public static final String IMAGE_JPEG_VALUE = "image/jpeg";
/* 168 */   public static final MimeType IMAGE_PNG = MimeType.valueOf("image/png"); public static final String IMAGE_PNG_VALUE = "image/png";
/* 169 */   public static final MimeType TEXT_HTML = MimeType.valueOf("text/html"); public static final String TEXT_HTML_VALUE = "text/html";
/* 170 */   public static final MimeType TEXT_PLAIN = MimeType.valueOf("text/plain"); public static final String TEXT_PLAIN_VALUE = "text/plain";
/* 171 */   public static final MimeType TEXT_XML = MimeType.valueOf("text/xml");
/*     */ 
/*     */   
/*     */   public static final String TEXT_XML_VALUE = "text/xml";
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static volatile Random random;
/*     */ 
/*     */   
/*     */   public static MimeType parseMimeType(String mimeType) {
/* 182 */     if (!StringUtils.hasLength(mimeType)) {
/* 183 */       throw new InvalidMimeTypeException(mimeType, "'mimeType' must not be empty");
/*     */     }
/*     */     
/* 186 */     int index = mimeType.indexOf(';');
/* 187 */     String fullType = ((index >= 0) ? mimeType.substring(0, index) : mimeType).trim();
/* 188 */     if (fullType.isEmpty()) {
/* 189 */       throw new InvalidMimeTypeException(mimeType, "'mimeType' must not be empty");
/*     */     }
/*     */ 
/*     */     
/* 193 */     if ("*".equals(fullType)) {
/* 194 */       fullType = "*/*";
/*     */     }
/* 196 */     int subIndex = fullType.indexOf('/');
/* 197 */     if (subIndex == -1) {
/* 198 */       throw new InvalidMimeTypeException(mimeType, "does not contain '/'");
/*     */     }
/* 200 */     if (subIndex == fullType.length() - 1) {
/* 201 */       throw new InvalidMimeTypeException(mimeType, "does not contain subtype after '/'");
/*     */     }
/* 203 */     String type = fullType.substring(0, subIndex);
/* 204 */     String subtype = fullType.substring(subIndex + 1, fullType.length());
/* 205 */     if ("*".equals(type) && !"*".equals(subtype)) {
/* 206 */       throw new InvalidMimeTypeException(mimeType, "wildcard type is legal only in '*/*' (all mime types)");
/*     */     }
/*     */     
/* 209 */     Map<String, String> parameters = null;
/*     */     do {
/* 211 */       int nextIndex = index + 1;
/* 212 */       boolean quoted = false;
/* 213 */       while (nextIndex < mimeType.length()) {
/* 214 */         char ch = mimeType.charAt(nextIndex);
/* 215 */         if (ch == ';') {
/* 216 */           if (!quoted) {
/*     */             break;
/*     */           }
/*     */         }
/* 220 */         else if (ch == '"') {
/* 221 */           quoted = !quoted;
/*     */         } 
/* 223 */         nextIndex++;
/*     */       } 
/* 225 */       String parameter = mimeType.substring(index + 1, nextIndex).trim();
/* 226 */       if (parameter.length() > 0) {
/* 227 */         if (parameters == null) {
/* 228 */           parameters = new LinkedHashMap<>(4);
/*     */         }
/* 230 */         int eqIndex = parameter.indexOf('=');
/* 231 */         if (eqIndex >= 0) {
/* 232 */           String attribute = parameter.substring(0, eqIndex).trim();
/* 233 */           String value = parameter.substring(eqIndex + 1, parameter.length()).trim();
/* 234 */           parameters.put(attribute, value);
/*     */         } 
/*     */       } 
/* 237 */       index = nextIndex;
/*     */     }
/* 239 */     while (index < mimeType.length());
/*     */     
/*     */     try {
/* 242 */       return new MimeType(type, subtype, parameters);
/*     */     }
/* 244 */     catch (UnsupportedCharsetException ex) {
/* 245 */       throw new InvalidMimeTypeException(mimeType, "unsupported charset '" + ex.getCharsetName() + "'");
/*     */     }
/* 247 */     catch (IllegalArgumentException ex) {
/* 248 */       throw new InvalidMimeTypeException(mimeType, ex.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<MimeType> parseMimeTypes(String mimeTypes) {
/* 259 */     if (!StringUtils.hasLength(mimeTypes)) {
/* 260 */       return Collections.emptyList();
/*     */     }
/* 262 */     return (List<MimeType>)tokenize(mimeTypes).stream()
/* 263 */       .filter(StringUtils::hasText)
/* 264 */       .map(MimeTypeUtils::parseMimeType)
/* 265 */       .collect(Collectors.toList());
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
/*     */   public static List<String> tokenize(String mimeTypes) {
/* 277 */     if (!StringUtils.hasLength(mimeTypes)) {
/* 278 */       return Collections.emptyList();
/*     */     }
/* 280 */     List<String> tokens = new ArrayList<>();
/* 281 */     boolean inQuotes = false;
/* 282 */     int startIndex = 0;
/* 283 */     int i = 0;
/* 284 */     while (i < mimeTypes.length()) {
/* 285 */       switch (mimeTypes.charAt(i)) {
/*     */         case '"':
/* 287 */           inQuotes = !inQuotes;
/*     */           break;
/*     */         case ',':
/* 290 */           if (!inQuotes) {
/* 291 */             tokens.add(mimeTypes.substring(startIndex, i));
/* 292 */             startIndex = i + 1;
/*     */           } 
/*     */           break;
/*     */         case '\\':
/* 296 */           i++;
/*     */           break;
/*     */       } 
/* 299 */       i++;
/*     */     } 
/* 301 */     tokens.add(mimeTypes.substring(startIndex));
/* 302 */     return tokens;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toString(Collection<? extends MimeType> mimeTypes) {
/* 312 */     StringBuilder builder = new StringBuilder();
/* 313 */     for (Iterator<? extends MimeType> iterator = mimeTypes.iterator(); iterator.hasNext(); ) {
/* 314 */       MimeType mimeType = iterator.next();
/* 315 */       mimeType.appendTo(builder);
/* 316 */       if (iterator.hasNext()) {
/* 317 */         builder.append(", ");
/*     */       }
/*     */     } 
/* 320 */     return builder.toString();
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
/*     */   public static void sortBySpecificity(List<MimeType> mimeTypes) {
/* 348 */     Assert.notNull(mimeTypes, "'mimeTypes' must not be null");
/* 349 */     if (mimeTypes.size() > 1) {
/* 350 */       mimeTypes.sort(SPECIFICITY_COMPARATOR);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Random initRandom() {
/* 359 */     Random randomToUse = random;
/* 360 */     if (randomToUse == null) {
/* 361 */       synchronized (MimeTypeUtils.class) {
/* 362 */         randomToUse = random;
/* 363 */         if (randomToUse == null) {
/* 364 */           randomToUse = new SecureRandom();
/* 365 */           random = randomToUse;
/*     */         } 
/*     */       } 
/*     */     }
/* 369 */     return randomToUse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] generateMultipartBoundary() {
/* 376 */     Random randomToUse = initRandom();
/* 377 */     byte[] boundary = new byte[randomToUse.nextInt(11) + 30];
/* 378 */     for (int i = 0; i < boundary.length; i++) {
/* 379 */       boundary[i] = BOUNDARY_CHARS[randomToUse.nextInt(BOUNDARY_CHARS.length)];
/*     */     }
/* 381 */     return boundary;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String generateMultipartBoundaryString() {
/* 388 */     return new String(generateMultipartBoundary(), StandardCharsets.US_ASCII);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/MimeTypeUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */