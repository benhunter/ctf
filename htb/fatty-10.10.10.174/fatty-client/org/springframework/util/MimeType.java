/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.BitSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.TreeSet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MimeType
/*     */   implements Comparable<MimeType>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 4085923477777865903L;
/*     */   protected static final String WILDCARD_TYPE = "*";
/*     */   private static final String PARAM_CHARSET = "charset";
/*     */   
/*     */   static {
/*  66 */     BitSet ctl = new BitSet(128);
/*  67 */     for (int i = 0; i <= 31; i++) {
/*  68 */       ctl.set(i);
/*     */     }
/*  70 */     ctl.set(127);
/*     */     
/*  72 */     BitSet separators = new BitSet(128);
/*  73 */     separators.set(40);
/*  74 */     separators.set(41);
/*  75 */     separators.set(60);
/*  76 */     separators.set(62);
/*  77 */     separators.set(64);
/*  78 */     separators.set(44);
/*  79 */     separators.set(59);
/*  80 */     separators.set(58);
/*  81 */     separators.set(92);
/*  82 */     separators.set(34);
/*  83 */     separators.set(47);
/*  84 */     separators.set(91);
/*  85 */     separators.set(93);
/*  86 */     separators.set(63);
/*  87 */     separators.set(61);
/*  88 */     separators.set(123);
/*  89 */     separators.set(125);
/*  90 */     separators.set(32);
/*  91 */     separators.set(9);
/*     */   }
/*  93 */   private static final BitSet TOKEN = new BitSet(128); static {
/*  94 */     TOKEN.set(0, 128);
/*  95 */     TOKEN.andNot(ctl);
/*  96 */     TOKEN.andNot(separators);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String type;
/*     */ 
/*     */ 
/*     */   
/*     */   private final String subtype;
/*     */ 
/*     */ 
/*     */   
/*     */   private final Map<String, String> parameters;
/*     */ 
/*     */ 
/*     */   
/*     */   public MimeType(String type) {
/* 115 */     this(type, "*");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MimeType(String type, String subtype) {
/* 126 */     this(type, subtype, Collections.emptyMap());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MimeType(String type, String subtype, Charset charset) {
/* 137 */     this(type, subtype, Collections.singletonMap("charset", charset.name()));
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
/*     */   public MimeType(MimeType other, Charset charset) {
/* 149 */     this(other.getType(), other.getSubtype(), addCharsetParameter(charset, other.getParameters()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MimeType(MimeType other, @Nullable Map<String, String> parameters) {
/* 160 */     this(other.getType(), other.getSubtype(), parameters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MimeType(String type, String subtype, @Nullable Map<String, String> parameters) {
/* 171 */     Assert.hasLength(type, "'type' must not be empty");
/* 172 */     Assert.hasLength(subtype, "'subtype' must not be empty");
/* 173 */     checkToken(type);
/* 174 */     checkToken(subtype);
/* 175 */     this.type = type.toLowerCase(Locale.ENGLISH);
/* 176 */     this.subtype = subtype.toLowerCase(Locale.ENGLISH);
/* 177 */     if (!CollectionUtils.isEmpty(parameters)) {
/* 178 */       Map<String, String> map = new LinkedCaseInsensitiveMap<>(parameters.size(), Locale.ENGLISH);
/* 179 */       parameters.forEach((attribute, value) -> {
/*     */             checkParameters(attribute, value);
/*     */             map.put(attribute, value);
/*     */           });
/* 183 */       this.parameters = Collections.unmodifiableMap(map);
/*     */     } else {
/*     */       
/* 186 */       this.parameters = Collections.emptyMap();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkToken(String token) {
/* 197 */     for (int i = 0; i < token.length(); i++) {
/* 198 */       char ch = token.charAt(i);
/* 199 */       if (!TOKEN.get(ch)) {
/* 200 */         throw new IllegalArgumentException("Invalid token character '" + ch + "' in token \"" + token + "\"");
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void checkParameters(String attribute, String value) {
/* 206 */     Assert.hasLength(attribute, "'attribute' must not be empty");
/* 207 */     Assert.hasLength(value, "'value' must not be empty");
/* 208 */     checkToken(attribute);
/* 209 */     if ("charset".equals(attribute)) {
/* 210 */       value = unquote(value);
/* 211 */       Charset.forName(value);
/*     */     }
/* 213 */     else if (!isQuotedString(value)) {
/* 214 */       checkToken(value);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isQuotedString(String s) {
/* 219 */     if (s.length() < 2) {
/* 220 */       return false;
/*     */     }
/*     */     
/* 223 */     return ((s.startsWith("\"") && s.endsWith("\"")) || (s.startsWith("'") && s.endsWith("'")));
/*     */   }
/*     */ 
/*     */   
/*     */   protected String unquote(String s) {
/* 228 */     return isQuotedString(s) ? s.substring(1, s.length() - 1) : s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWildcardType() {
/* 236 */     return "*".equals(getType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWildcardSubtype() {
/* 246 */     return ("*".equals(getSubtype()) || getSubtype().startsWith("*+"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConcrete() {
/* 255 */     return (!isWildcardType() && !isWildcardSubtype());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/* 262 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSubtype() {
/* 269 */     return this.subtype;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Charset getCharset() {
/* 279 */     String charset = getParameter("charset");
/* 280 */     return (charset != null) ? Charset.forName(unquote(charset)) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getParameter(String name) {
/* 290 */     return this.parameters.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> getParameters() {
/* 298 */     return this.parameters;
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
/*     */   public boolean includes(@Nullable MimeType other) {
/* 311 */     if (other == null) {
/* 312 */       return false;
/*     */     }
/* 314 */     if (isWildcardType())
/*     */     {
/* 316 */       return true;
/*     */     }
/* 318 */     if (getType().equals(other.getType())) {
/* 319 */       if (getSubtype().equals(other.getSubtype())) {
/* 320 */         return true;
/*     */       }
/* 322 */       if (isWildcardSubtype()) {
/*     */         
/* 324 */         int thisPlusIdx = getSubtype().lastIndexOf('+');
/* 325 */         if (thisPlusIdx == -1) {
/* 326 */           return true;
/*     */         }
/*     */ 
/*     */         
/* 330 */         int otherPlusIdx = other.getSubtype().lastIndexOf('+');
/* 331 */         if (otherPlusIdx != -1) {
/* 332 */           String thisSubtypeNoSuffix = getSubtype().substring(0, thisPlusIdx);
/* 333 */           String thisSubtypeSuffix = getSubtype().substring(thisPlusIdx + 1);
/* 334 */           String otherSubtypeSuffix = other.getSubtype().substring(otherPlusIdx + 1);
/* 335 */           if (thisSubtypeSuffix.equals(otherSubtypeSuffix) && "*".equals(thisSubtypeNoSuffix)) {
/* 336 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 342 */     return false;
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
/*     */   public boolean isCompatibleWith(@Nullable MimeType other) {
/* 355 */     if (other == null) {
/* 356 */       return false;
/*     */     }
/* 358 */     if (isWildcardType() || other.isWildcardType()) {
/* 359 */       return true;
/*     */     }
/* 361 */     if (getType().equals(other.getType())) {
/* 362 */       if (getSubtype().equals(other.getSubtype())) {
/* 363 */         return true;
/*     */       }
/*     */       
/* 366 */       if (isWildcardSubtype() || other.isWildcardSubtype()) {
/* 367 */         int thisPlusIdx = getSubtype().lastIndexOf('+');
/* 368 */         int otherPlusIdx = other.getSubtype().lastIndexOf('+');
/* 369 */         if (thisPlusIdx == -1 && otherPlusIdx == -1) {
/* 370 */           return true;
/*     */         }
/* 372 */         if (thisPlusIdx != -1 && otherPlusIdx != -1) {
/* 373 */           String thisSubtypeNoSuffix = getSubtype().substring(0, thisPlusIdx);
/* 374 */           String otherSubtypeNoSuffix = other.getSubtype().substring(0, otherPlusIdx);
/* 375 */           String thisSubtypeSuffix = getSubtype().substring(thisPlusIdx + 1);
/* 376 */           String otherSubtypeSuffix = other.getSubtype().substring(otherPlusIdx + 1);
/* 377 */           if (thisSubtypeSuffix.equals(otherSubtypeSuffix) && ("*"
/* 378 */             .equals(thisSubtypeNoSuffix) || "*".equals(otherSubtypeNoSuffix))) {
/* 379 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 384 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equalsTypeAndSubtype(@Nullable MimeType other) {
/* 395 */     if (other == null) {
/* 396 */       return false;
/*     */     }
/* 398 */     return (this.type.equalsIgnoreCase(other.type) && this.subtype.equalsIgnoreCase(other.subtype));
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
/*     */   public boolean isPresentIn(Collection<? extends MimeType> mimeTypes) {
/* 410 */     for (MimeType mimeType : mimeTypes) {
/* 411 */       if (mimeType.equalsTypeAndSubtype(this)) {
/* 412 */         return true;
/*     */       }
/*     */     } 
/* 415 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 421 */     if (this == other) {
/* 422 */       return true;
/*     */     }
/* 424 */     if (!(other instanceof MimeType)) {
/* 425 */       return false;
/*     */     }
/* 427 */     MimeType otherType = (MimeType)other;
/* 428 */     return (this.type.equalsIgnoreCase(otherType.type) && this.subtype
/* 429 */       .equalsIgnoreCase(otherType.subtype) && 
/* 430 */       parametersAreEqual(otherType));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean parametersAreEqual(MimeType other) {
/* 440 */     if (this.parameters.size() != other.parameters.size()) {
/* 441 */       return false;
/*     */     }
/*     */     
/* 444 */     for (Map.Entry<String, String> entry : this.parameters.entrySet()) {
/* 445 */       String key = entry.getKey();
/* 446 */       if (!other.parameters.containsKey(key)) {
/* 447 */         return false;
/*     */       }
/* 449 */       if ("charset".equals(key)) {
/* 450 */         if (!ObjectUtils.nullSafeEquals(getCharset(), other.getCharset()))
/* 451 */           return false; 
/*     */         continue;
/*     */       } 
/* 454 */       if (!ObjectUtils.nullSafeEquals(entry.getValue(), other.parameters.get(key))) {
/* 455 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 459 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 464 */     int result = this.type.hashCode();
/* 465 */     result = 31 * result + this.subtype.hashCode();
/* 466 */     result = 31 * result + this.parameters.hashCode();
/* 467 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 472 */     StringBuilder builder = new StringBuilder();
/* 473 */     appendTo(builder);
/* 474 */     return builder.toString();
/*     */   }
/*     */   
/*     */   protected void appendTo(StringBuilder builder) {
/* 478 */     builder.append(this.type);
/* 479 */     builder.append('/');
/* 480 */     builder.append(this.subtype);
/* 481 */     appendTo(this.parameters, builder);
/*     */   }
/*     */   
/*     */   private void appendTo(Map<String, String> map, StringBuilder builder) {
/* 485 */     map.forEach((key, val) -> {
/*     */           builder.append(';');
/*     */           builder.append(key);
/*     */           builder.append('=');
/*     */           builder.append(val);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(MimeType other) {
/* 500 */     int comp = getType().compareToIgnoreCase(other.getType());
/* 501 */     if (comp != 0) {
/* 502 */       return comp;
/*     */     }
/* 504 */     comp = getSubtype().compareToIgnoreCase(other.getSubtype());
/* 505 */     if (comp != 0) {
/* 506 */       return comp;
/*     */     }
/* 508 */     comp = getParameters().size() - other.getParameters().size();
/* 509 */     if (comp != 0) {
/* 510 */       return comp;
/*     */     }
/*     */     
/* 513 */     TreeSet<String> thisAttributes = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
/* 514 */     thisAttributes.addAll(getParameters().keySet());
/* 515 */     TreeSet<String> otherAttributes = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
/* 516 */     otherAttributes.addAll(other.getParameters().keySet());
/* 517 */     Iterator<String> thisAttributesIterator = thisAttributes.iterator();
/* 518 */     Iterator<String> otherAttributesIterator = otherAttributes.iterator();
/*     */     
/* 520 */     while (thisAttributesIterator.hasNext()) {
/* 521 */       String thisAttribute = thisAttributesIterator.next();
/* 522 */       String otherAttribute = otherAttributesIterator.next();
/* 523 */       comp = thisAttribute.compareToIgnoreCase(otherAttribute);
/* 524 */       if (comp != 0) {
/* 525 */         return comp;
/*     */       }
/* 527 */       if ("charset".equals(thisAttribute)) {
/* 528 */         Charset thisCharset = getCharset();
/* 529 */         Charset otherCharset = other.getCharset();
/* 530 */         if (thisCharset != otherCharset) {
/* 531 */           if (thisCharset == null) {
/* 532 */             return -1;
/*     */           }
/* 534 */           if (otherCharset == null) {
/* 535 */             return 1;
/*     */           }
/* 537 */           comp = thisCharset.compareTo(otherCharset);
/* 538 */           if (comp != 0) {
/* 539 */             return comp;
/*     */           }
/*     */         } 
/*     */         continue;
/*     */       } 
/* 544 */       String thisValue = getParameters().get(thisAttribute);
/* 545 */       String otherValue = other.getParameters().get(otherAttribute);
/* 546 */       if (otherValue == null) {
/* 547 */         otherValue = "";
/*     */       }
/* 549 */       comp = thisValue.compareTo(otherValue);
/* 550 */       if (comp != 0) {
/* 551 */         return comp;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 556 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MimeType valueOf(String value) {
/* 567 */     return MimeTypeUtils.parseMimeType(value);
/*     */   }
/*     */   
/*     */   private static Map<String, String> addCharsetParameter(Charset charset, Map<String, String> parameters) {
/* 571 */     Map<String, String> map = new LinkedHashMap<>(parameters);
/* 572 */     map.put("charset", charset.name());
/* 573 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class SpecificityComparator<T extends MimeType>
/*     */     implements Comparator<T>
/*     */   {
/*     */     public int compare(T mimeType1, T mimeType2) {
/* 586 */       if (mimeType1.isWildcardType() && !mimeType2.isWildcardType()) {
/* 587 */         return 1;
/*     */       }
/* 589 */       if (mimeType2.isWildcardType() && !mimeType1.isWildcardType()) {
/* 590 */         return -1;
/*     */       }
/* 592 */       if (!mimeType1.getType().equals(mimeType2.getType())) {
/* 593 */         return 0;
/*     */       }
/*     */       
/* 596 */       if (mimeType1.isWildcardSubtype() && !mimeType2.isWildcardSubtype()) {
/* 597 */         return 1;
/*     */       }
/* 599 */       if (mimeType2.isWildcardSubtype() && !mimeType1.isWildcardSubtype()) {
/* 600 */         return -1;
/*     */       }
/* 602 */       if (!mimeType1.getSubtype().equals(mimeType2.getSubtype())) {
/* 603 */         return 0;
/*     */       }
/*     */       
/* 606 */       return compareParameters(mimeType1, mimeType2);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected int compareParameters(T mimeType1, T mimeType2) {
/* 612 */       int paramsSize1 = mimeType1.getParameters().size();
/* 613 */       int paramsSize2 = mimeType2.getParameters().size();
/* 614 */       return Integer.compare(paramsSize2, paramsSize1);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/MimeType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */