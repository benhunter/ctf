/*     */ package org.springframework.http;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Collectors;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.InvalidMimeTypeException;
/*     */ import org.springframework.util.MimeType;
/*     */ import org.springframework.util.MimeTypeUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MediaType
/*     */   extends MimeType
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 2069937152339670231L;
/*     */   public static final String ALL_VALUE = "*/*";
/* 327 */   public static final MediaType ALL = valueOf("*/*"); public static final String APPLICATION_ATOM_XML_VALUE = "application/atom+xml";
/* 328 */   public static final MediaType APPLICATION_ATOM_XML = valueOf("application/atom+xml");
/* 329 */   public static final MediaType APPLICATION_FORM_URLENCODED = valueOf("application/x-www-form-urlencoded"); public static final String APPLICATION_FORM_URLENCODED_VALUE = "application/x-www-form-urlencoded";
/* 330 */   public static final MediaType APPLICATION_JSON = valueOf("application/json"); public static final String APPLICATION_JSON_VALUE = "application/json";
/* 331 */   public static final MediaType APPLICATION_JSON_UTF8 = valueOf("application/json;charset=UTF-8"); public static final String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";
/* 332 */   public static final MediaType APPLICATION_OCTET_STREAM = valueOf("application/octet-stream"); public static final String APPLICATION_OCTET_STREAM_VALUE = "application/octet-stream";
/* 333 */   public static final MediaType APPLICATION_PDF = valueOf("application/pdf"); public static final String APPLICATION_PDF_VALUE = "application/pdf";
/* 334 */   public static final MediaType APPLICATION_PROBLEM_JSON = valueOf("application/problem+json"); public static final String APPLICATION_PROBLEM_JSON_VALUE = "application/problem+json";
/* 335 */   public static final MediaType APPLICATION_PROBLEM_JSON_UTF8 = valueOf("application/problem+json;charset=UTF-8"); public static final String APPLICATION_PROBLEM_JSON_UTF8_VALUE = "application/problem+json;charset=UTF-8";
/* 336 */   public static final MediaType APPLICATION_PROBLEM_XML = valueOf("application/problem+xml"); public static final String APPLICATION_PROBLEM_XML_VALUE = "application/problem+xml";
/* 337 */   public static final MediaType APPLICATION_RSS_XML = valueOf("application/rss+xml"); public static final String APPLICATION_RSS_XML_VALUE = "application/rss+xml";
/* 338 */   public static final MediaType APPLICATION_STREAM_JSON = valueOf("application/stream+json"); public static final String APPLICATION_STREAM_JSON_VALUE = "application/stream+json";
/* 339 */   public static final MediaType APPLICATION_XHTML_XML = valueOf("application/xhtml+xml"); public static final String APPLICATION_XHTML_XML_VALUE = "application/xhtml+xml";
/* 340 */   public static final MediaType APPLICATION_XML = valueOf("application/xml"); public static final String APPLICATION_XML_VALUE = "application/xml";
/* 341 */   public static final MediaType IMAGE_GIF = valueOf("image/gif"); public static final String IMAGE_GIF_VALUE = "image/gif";
/* 342 */   public static final MediaType IMAGE_JPEG = valueOf("image/jpeg"); public static final String IMAGE_JPEG_VALUE = "image/jpeg";
/* 343 */   public static final MediaType IMAGE_PNG = valueOf("image/png"); public static final String IMAGE_PNG_VALUE = "image/png";
/* 344 */   public static final MediaType MULTIPART_FORM_DATA = valueOf("multipart/form-data"); public static final String MULTIPART_FORM_DATA_VALUE = "multipart/form-data";
/* 345 */   public static final MediaType TEXT_EVENT_STREAM = valueOf("text/event-stream"); public static final String TEXT_EVENT_STREAM_VALUE = "text/event-stream";
/* 346 */   public static final MediaType TEXT_HTML = valueOf("text/html"); public static final String TEXT_HTML_VALUE = "text/html";
/* 347 */   public static final MediaType TEXT_MARKDOWN = valueOf("text/markdown"); public static final String TEXT_MARKDOWN_VALUE = "text/markdown";
/* 348 */   public static final MediaType TEXT_PLAIN = valueOf("text/plain"); public static final String TEXT_PLAIN_VALUE = "text/plain";
/* 349 */   public static final MediaType TEXT_XML = valueOf("text/xml");
/*     */ 
/*     */   
/*     */   public static final String TEXT_XML_VALUE = "text/xml";
/*     */   
/*     */   private static final String PARAM_QUALITY_FACTOR = "q";
/*     */   
/*     */   public static final Comparator<MediaType> QUALITY_VALUE_COMPARATOR;
/*     */ 
/*     */   
/*     */   public MediaType(String type) {
/* 360 */     super(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaType(String type, String subtype) {
/* 371 */     super(type, subtype, Collections.emptyMap());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaType(String type, String subtype, Charset charset) {
/* 382 */     super(type, subtype, charset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaType(String type, String subtype, double qualityValue) {
/* 393 */     this(type, subtype, Collections.singletonMap("q", Double.toString(qualityValue)));
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
/*     */   public MediaType(MediaType other, Charset charset) {
/* 405 */     super(other, charset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaType(MediaType other, @Nullable Map<String, String> parameters) {
/* 416 */     super(other.getType(), other.getSubtype(), parameters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaType(String type, String subtype, @Nullable Map<String, String> parameters) {
/* 427 */     super(type, subtype, parameters);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkParameters(String attribute, String value) {
/* 433 */     super.checkParameters(attribute, value);
/* 434 */     if ("q".equals(attribute)) {
/* 435 */       value = unquote(value);
/* 436 */       double d = Double.parseDouble(value);
/* 437 */       Assert.isTrue((d >= 0.0D && d <= 1.0D), "Invalid quality value \"" + value + "\": should be between 0.0 and 1.0");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getQualityValue() {
/* 448 */     String qualityFactor = getParameter("q");
/* 449 */     return (qualityFactor != null) ? Double.parseDouble(unquote(qualityFactor)) : 1.0D;
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
/*     */   public boolean includes(@Nullable MediaType other) {
/* 464 */     return includes(other);
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
/*     */   public boolean isCompatibleWith(@Nullable MediaType other) {
/* 479 */     return isCompatibleWith(other);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaType copyQualityValue(MediaType mediaType) {
/* 488 */     if (!mediaType.getParameters().containsKey("q")) {
/* 489 */       return this;
/*     */     }
/* 491 */     Map<String, String> params = new LinkedHashMap<>(getParameters());
/* 492 */     params.put("q", (String)mediaType.getParameters().get("q"));
/* 493 */     return new MediaType(this, params);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaType removeQualityValue() {
/* 502 */     if (!getParameters().containsKey("q")) {
/* 503 */       return this;
/*     */     }
/* 505 */     Map<String, String> params = new LinkedHashMap<>(getParameters());
/* 506 */     params.remove("q");
/* 507 */     return new MediaType(this, params);
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
/*     */   public static MediaType valueOf(String value) {
/* 520 */     return parseMediaType(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MediaType parseMediaType(String mediaType) {
/*     */     MimeType type;
/*     */     try {
/* 532 */       type = MimeTypeUtils.parseMimeType(mediaType);
/*     */     }
/* 534 */     catch (InvalidMimeTypeException ex) {
/* 535 */       throw new InvalidMediaTypeException(ex);
/*     */     } 
/*     */     try {
/* 538 */       return new MediaType(type.getType(), type.getSubtype(), type.getParameters());
/*     */     }
/* 540 */     catch (IllegalArgumentException ex) {
/* 541 */       throw new InvalidMediaTypeException(mediaType, ex.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<MediaType> parseMediaTypes(@Nullable String mediaTypes) {
/* 553 */     if (!StringUtils.hasLength(mediaTypes)) {
/* 554 */       return Collections.emptyList();
/*     */     }
/* 556 */     return (List<MediaType>)MimeTypeUtils.tokenize(mediaTypes).stream()
/* 557 */       .filter(StringUtils::hasText)
/* 558 */       .map(MediaType::parseMediaType)
/* 559 */       .collect(Collectors.toList());
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
/*     */   public static List<MediaType> parseMediaTypes(@Nullable List<String> mediaTypes) {
/* 572 */     if (CollectionUtils.isEmpty(mediaTypes)) {
/* 573 */       return Collections.emptyList();
/*     */     }
/* 575 */     if (mediaTypes.size() == 1) {
/* 576 */       return parseMediaTypes(mediaTypes.get(0));
/*     */     }
/*     */     
/* 579 */     List<MediaType> result = new ArrayList<>(8);
/* 580 */     for (String mediaType : mediaTypes) {
/* 581 */       result.addAll(parseMediaTypes(mediaType));
/*     */     }
/* 583 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<MediaType> asMediaTypes(List<MimeType> mimeTypes) {
/* 592 */     return (List<MediaType>)mimeTypes.stream().map(MediaType::asMediaType).collect(Collectors.toList());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MediaType asMediaType(MimeType mimeType) {
/* 600 */     if (mimeType instanceof MediaType) {
/* 601 */       return (MediaType)mimeType;
/*     */     }
/* 603 */     return new MediaType(mimeType.getType(), mimeType.getSubtype(), mimeType.getParameters());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toString(Collection<MediaType> mediaTypes) {
/* 613 */     return MimeTypeUtils.toString(mediaTypes);
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
/*     */ 
/*     */   
/*     */   public static void sortBySpecificity(List<MediaType> mediaTypes) {
/* 644 */     Assert.notNull(mediaTypes, "'mediaTypes' must not be null");
/* 645 */     if (mediaTypes.size() > 1) {
/* 646 */       mediaTypes.sort(SPECIFICITY_COMPARATOR);
/*     */     }
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
/*     */   public static void sortByQualityValue(List<MediaType> mediaTypes) {
/* 671 */     Assert.notNull(mediaTypes, "'mediaTypes' must not be null");
/* 672 */     if (mediaTypes.size() > 1) {
/* 673 */       mediaTypes.sort(QUALITY_VALUE_COMPARATOR);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortBySpecificityAndQuality(List<MediaType> mediaTypes) {
/* 684 */     Assert.notNull(mediaTypes, "'mediaTypes' must not be null");
/* 685 */     if (mediaTypes.size() > 1) {
/* 686 */       mediaTypes.sort(SPECIFICITY_COMPARATOR.thenComparing(QUALITY_VALUE_COMPARATOR));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 694 */     QUALITY_VALUE_COMPARATOR = ((mediaType1, mediaType2) -> {
/*     */         double quality1 = mediaType1.getQualityValue();
/*     */         double quality2 = mediaType2.getQualityValue();
/*     */         int qualityComparison = Double.compare(quality2, quality1);
/*     */         if (qualityComparison != 0) {
/*     */           return qualityComparison;
/*     */         }
/*     */         if (mediaType1.isWildcardType() && !mediaType2.isWildcardType()) {
/*     */           return 1;
/*     */         }
/*     */         if (mediaType2.isWildcardType() && !mediaType1.isWildcardType()) {
/*     */           return -1;
/*     */         }
/*     */         if (!mediaType1.getType().equals(mediaType2.getType())) {
/*     */           return 0;
/*     */         }
/*     */         if (mediaType1.isWildcardSubtype() && !mediaType2.isWildcardSubtype()) {
/*     */           return 1;
/*     */         }
/*     */         if (mediaType2.isWildcardSubtype() && !mediaType1.isWildcardSubtype()) {
/*     */           return -1;
/*     */         }
/*     */         if (!mediaType1.getSubtype().equals(mediaType2.getSubtype())) {
/*     */           return 0;
/*     */         }
/*     */         int paramsSize1 = mediaType1.getParameters().size();
/*     */         int paramsSize2 = mediaType2.getParameters().size();
/*     */         return Integer.compare(paramsSize2, paramsSize1);
/*     */       });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 732 */   public static final Comparator<MediaType> SPECIFICITY_COMPARATOR = (Comparator<MediaType>)new MimeType.SpecificityComparator<MediaType>()
/*     */     {
/*     */       protected int compareParameters(MediaType mediaType1, MediaType mediaType2)
/*     */       {
/* 736 */         double quality1 = mediaType1.getQualityValue();
/* 737 */         double quality2 = mediaType2.getQualityValue();
/* 738 */         int qualityComparison = Double.compare(quality2, quality1);
/* 739 */         if (qualityComparison != 0) {
/* 740 */           return qualityComparison;
/*     */         }
/* 742 */         return super.compareParameters(mediaType1, mediaType2);
/*     */       }
/*     */     };
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/MediaType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */