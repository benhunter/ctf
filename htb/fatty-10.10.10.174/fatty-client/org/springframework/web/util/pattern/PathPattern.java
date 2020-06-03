/*     */ package org.springframework.web.util.pattern;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.http.server.PathContainer;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ public class PathPattern
/*     */   implements Comparable<PathPattern>
/*     */ {
/*  74 */   private static final PathContainer EMPTY_PATH = PathContainer.parsePath("");
/*     */   public static final Comparator<PathPattern> SPECIFICITY_COMPARATOR;
/*     */   private final String patternString;
/*     */   private final PathPatternParser parser;
/*     */   private final char separator;
/*     */   private final boolean matchOptionalTrailingSeparator;
/*     */   private final boolean caseSensitive;
/*     */   @Nullable
/*     */   private final PathElement head;
/*     */   private int capturedVariableCount;
/*     */   private int normalizedLength;
/*     */   
/*     */   static {
/*  87 */     SPECIFICITY_COMPARATOR = Comparator.nullsLast(
/*     */         
/*  89 */         Comparator.<PathPattern>comparingInt(p -> p.isCatchAll() ? 1 : 0)
/*  90 */         .thenComparingInt(p -> p.isCatchAll() ? scoreByNormalizedLength(p) : 0)
/*  91 */         .thenComparingInt(PathPattern::getScore)
/*  92 */         .thenComparingInt(PathPattern::scoreByNormalizedLength));
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
/*     */   private boolean endsWithSeparatorWildcard = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int score;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean catchAll = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PathPattern(String patternText, PathPatternParser parser, @Nullable PathElement head) {
/* 146 */     this.patternString = patternText;
/* 147 */     this.parser = parser;
/* 148 */     this.separator = parser.getSeparator();
/* 149 */     this.matchOptionalTrailingSeparator = parser.isMatchOptionalTrailingSeparator();
/* 150 */     this.caseSensitive = parser.isCaseSensitive();
/* 151 */     this.head = head;
/*     */ 
/*     */     
/* 154 */     PathElement elem = head;
/* 155 */     while (elem != null) {
/* 156 */       this.capturedVariableCount += elem.getCaptureCount();
/* 157 */       this.normalizedLength += elem.getNormalizedLength();
/* 158 */       this.score += elem.getScore();
/* 159 */       if (elem instanceof CaptureTheRestPathElement || elem instanceof WildcardTheRestPathElement) {
/* 160 */         this.catchAll = true;
/*     */       }
/* 162 */       if (elem instanceof SeparatorPathElement && elem.next != null && elem.next instanceof WildcardPathElement && elem.next.next == null)
/*     */       {
/* 164 */         this.endsWithSeparatorWildcard = true;
/*     */       }
/* 166 */       elem = elem.next;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPatternString() {
/* 175 */     return this.patternString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(PathContainer pathContainer) {
/* 184 */     if (this.head == null) {
/* 185 */       return (!hasLength(pathContainer) || (this.matchOptionalTrailingSeparator && 
/* 186 */         pathContainerIsJustSeparator(pathContainer)));
/*     */     }
/* 188 */     if (!hasLength(pathContainer)) {
/* 189 */       if (this.head instanceof WildcardTheRestPathElement || this.head instanceof CaptureTheRestPathElement) {
/* 190 */         pathContainer = EMPTY_PATH;
/*     */       } else {
/*     */         
/* 193 */         return false;
/*     */       } 
/*     */     }
/* 196 */     MatchingContext matchingContext = new MatchingContext(pathContainer, false);
/* 197 */     return this.head.matches(0, matchingContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public PathMatchInfo matchAndExtract(PathContainer pathContainer) {
/* 208 */     if (this.head == null) {
/* 209 */       return (hasLength(pathContainer) && (!this.matchOptionalTrailingSeparator || 
/* 210 */         !pathContainerIsJustSeparator(pathContainer))) ? null : PathMatchInfo
/* 211 */         .EMPTY;
/*     */     }
/* 213 */     if (!hasLength(pathContainer)) {
/* 214 */       if (this.head instanceof WildcardTheRestPathElement || this.head instanceof CaptureTheRestPathElement) {
/* 215 */         pathContainer = EMPTY_PATH;
/*     */       } else {
/*     */         
/* 218 */         return null;
/*     */       } 
/*     */     }
/* 221 */     MatchingContext matchingContext = new MatchingContext(pathContainer, true);
/* 222 */     return this.head.matches(0, matchingContext) ? matchingContext.getPathMatchResult() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public PathRemainingMatchInfo matchStartOfPath(PathContainer pathContainer) {
/*     */     PathRemainingMatchInfo info;
/* 234 */     if (this.head == null) {
/* 235 */       return new PathRemainingMatchInfo(pathContainer);
/*     */     }
/* 237 */     if (!hasLength(pathContainer)) {
/* 238 */       return null;
/*     */     }
/*     */     
/* 241 */     MatchingContext matchingContext = new MatchingContext(pathContainer, true);
/* 242 */     matchingContext.setMatchAllowExtraPath();
/* 243 */     boolean matches = this.head.matches(0, matchingContext);
/* 244 */     if (!matches) {
/* 245 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 249 */     if (matchingContext.remainingPathIndex == pathContainer.elements().size()) {
/* 250 */       info = new PathRemainingMatchInfo(EMPTY_PATH, matchingContext.getPathMatchResult());
/*     */     }
/*     */     else {
/*     */       
/* 254 */       info = new PathRemainingMatchInfo(pathContainer.subPath(matchingContext.remainingPathIndex), matchingContext.getPathMatchResult());
/*     */     } 
/* 256 */     return info;
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
/*     */   public PathContainer extractPathWithinPattern(PathContainer path) {
/* 280 */     List<PathContainer.Element> pathElements = path.elements();
/* 281 */     int pathElementsCount = pathElements.size();
/*     */     
/* 283 */     int startIndex = 0;
/*     */     
/* 285 */     PathElement elem = this.head;
/* 286 */     while (elem != null && 
/* 287 */       elem.getWildcardCount() == 0 && elem.getCaptureCount() == 0) {
/*     */ 
/*     */       
/* 290 */       elem = elem.next;
/* 291 */       startIndex++;
/*     */     } 
/* 293 */     if (elem == null)
/*     */     {
/* 295 */       return PathContainer.parsePath("");
/*     */     }
/*     */ 
/*     */     
/* 299 */     while (startIndex < pathElementsCount && pathElements.get(startIndex) instanceof PathContainer.Separator) {
/* 300 */       startIndex++;
/*     */     }
/*     */     
/* 303 */     int endIndex = pathElements.size();
/*     */     
/* 305 */     while (endIndex > 0 && pathElements.get(endIndex - 1) instanceof PathContainer.Separator) {
/* 306 */       endIndex--;
/*     */     }
/*     */     
/* 309 */     boolean multipleAdjacentSeparators = false;
/* 310 */     for (int i = startIndex; i < endIndex - 1; i++) {
/* 311 */       if (pathElements.get(i) instanceof PathContainer.Separator && pathElements.get(i + 1) instanceof PathContainer.Separator) {
/* 312 */         multipleAdjacentSeparators = true;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 317 */     PathContainer resultPath = null;
/* 318 */     if (multipleAdjacentSeparators) {
/*     */       
/* 320 */       StringBuilder buf = new StringBuilder();
/* 321 */       int j = startIndex;
/* 322 */       while (j < endIndex) {
/* 323 */         PathContainer.Element e = pathElements.get(j++);
/* 324 */         buf.append(e.value());
/* 325 */         if (e instanceof PathContainer.Separator) {
/* 326 */           while (j < endIndex && pathElements.get(j) instanceof PathContainer.Separator) {
/* 327 */             j++;
/*     */           }
/*     */         }
/*     */       } 
/* 331 */       resultPath = PathContainer.parsePath(buf.toString());
/*     */     }
/* 333 */     else if (startIndex >= endIndex) {
/* 334 */       resultPath = PathContainer.parsePath("");
/*     */     } else {
/*     */       
/* 337 */       resultPath = path.subPath(startIndex, endIndex);
/*     */     } 
/* 339 */     return resultPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(@Nullable PathPattern otherPattern) {
/* 349 */     int result = SPECIFICITY_COMPARATOR.compare(this, otherPattern);
/* 350 */     return (result == 0 && otherPattern != null) ? this.patternString
/* 351 */       .compareTo(otherPattern.patternString) : result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathPattern combine(PathPattern pattern2string) {
/* 359 */     if (!StringUtils.hasLength(this.patternString)) {
/* 360 */       if (!StringUtils.hasLength(pattern2string.patternString)) {
/* 361 */         return this.parser.parse("");
/*     */       }
/*     */       
/* 364 */       return pattern2string;
/*     */     } 
/*     */     
/* 367 */     if (!StringUtils.hasLength(pattern2string.patternString)) {
/* 368 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 376 */     if (!this.patternString.equals(pattern2string.patternString) && this.capturedVariableCount == 0 && 
/* 377 */       matches(PathContainer.parsePath(pattern2string.patternString))) {
/* 378 */       return pattern2string;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 383 */     if (this.endsWithSeparatorWildcard) {
/* 384 */       return this.parser.parse(concat(this.patternString
/* 385 */             .substring(0, this.patternString.length() - 2), pattern2string.patternString));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 391 */     int starDotPos1 = this.patternString.indexOf("*.");
/* 392 */     if (this.capturedVariableCount != 0 || starDotPos1 == -1 || this.separator == '.') {
/* 393 */       return this.parser.parse(concat(this.patternString, pattern2string.patternString));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 398 */     String firstExtension = this.patternString.substring(starDotPos1 + 1);
/* 399 */     String p2string = pattern2string.patternString;
/* 400 */     int dotPos2 = p2string.indexOf('.');
/* 401 */     String file2 = (dotPos2 == -1) ? p2string : p2string.substring(0, dotPos2);
/* 402 */     String secondExtension = (dotPos2 == -1) ? "" : p2string.substring(dotPos2);
/* 403 */     boolean firstExtensionWild = (firstExtension.equals(".*") || firstExtension.equals(""));
/* 404 */     boolean secondExtensionWild = (secondExtension.equals(".*") || secondExtension.equals(""));
/* 405 */     if (!firstExtensionWild && !secondExtensionWild) {
/* 406 */       throw new IllegalArgumentException("Cannot combine patterns: " + this.patternString + " and " + pattern2string);
/*     */     }
/*     */     
/* 409 */     return this.parser.parse(file2 + (firstExtensionWild ? secondExtension : firstExtension));
/*     */   }
/*     */   
/*     */   public boolean equals(Object other) {
/* 413 */     if (!(other instanceof PathPattern)) {
/* 414 */       return false;
/*     */     }
/* 416 */     PathPattern otherPattern = (PathPattern)other;
/* 417 */     return (this.patternString.equals(otherPattern.getPatternString()) && this.separator == otherPattern
/* 418 */       .getSeparator() && this.caseSensitive == otherPattern.caseSensitive);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 423 */     return (this.patternString.hashCode() + this.separator) * 17 + (this.caseSensitive ? 1 : 0);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 427 */     return this.patternString;
/*     */   }
/*     */   
/*     */   int getScore() {
/* 431 */     return this.score;
/*     */   }
/*     */   
/*     */   boolean isCatchAll() {
/* 435 */     return this.catchAll;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getNormalizedLength() {
/* 445 */     return this.normalizedLength;
/*     */   }
/*     */   
/*     */   char getSeparator() {
/* 449 */     return this.separator;
/*     */   }
/*     */   
/*     */   int getCapturedVariableCount() {
/* 453 */     return this.capturedVariableCount;
/*     */   }
/*     */   
/*     */   String toChainString() {
/* 457 */     StringBuilder buf = new StringBuilder();
/* 458 */     PathElement pe = this.head;
/* 459 */     while (pe != null) {
/* 460 */       buf.append(pe.toString()).append(" ");
/* 461 */       pe = pe.next;
/*     */     } 
/* 463 */     return buf.toString().trim();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String computePatternString() {
/* 471 */     StringBuilder buf = new StringBuilder();
/* 472 */     PathElement pe = this.head;
/* 473 */     while (pe != null) {
/* 474 */       buf.append(pe.getChars());
/* 475 */       pe = pe.next;
/*     */     } 
/* 477 */     return buf.toString();
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   PathElement getHeadSection() {
/* 482 */     return this.head;
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
/*     */   private String concat(String path1, String path2) {
/* 494 */     boolean path1EndsWithSeparator = (path1.charAt(path1.length() - 1) == this.separator);
/* 495 */     boolean path2StartsWithSeparator = (path2.charAt(0) == this.separator);
/* 496 */     if (path1EndsWithSeparator && path2StartsWithSeparator) {
/* 497 */       return path1 + path2.substring(1);
/*     */     }
/* 499 */     if (path1EndsWithSeparator || path2StartsWithSeparator) {
/* 500 */       return path1 + path2;
/*     */     }
/*     */     
/* 503 */     return path1 + this.separator + path2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasLength(@Nullable PathContainer container) {
/* 513 */     return (container != null && container.elements().size() > 0);
/*     */   }
/*     */   
/*     */   private static int scoreByNormalizedLength(PathPattern pattern) {
/* 517 */     return -pattern.getNormalizedLength();
/*     */   }
/*     */   
/*     */   private boolean pathContainerIsJustSeparator(PathContainer pathContainer) {
/* 521 */     return (pathContainer.value().length() == 1 && pathContainer
/* 522 */       .value().charAt(0) == this.separator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class PathMatchInfo
/*     */   {
/* 531 */     private static final PathMatchInfo EMPTY = new PathMatchInfo(
/* 532 */         Collections.emptyMap(), Collections.emptyMap());
/*     */ 
/*     */     
/*     */     private final Map<String, String> uriVariables;
/*     */ 
/*     */     
/*     */     private final Map<String, MultiValueMap<String, String>> matrixVariables;
/*     */ 
/*     */ 
/*     */     
/*     */     PathMatchInfo(Map<String, String> uriVars, @Nullable Map<String, MultiValueMap<String, String>> matrixVars) {
/* 543 */       this.uriVariables = Collections.unmodifiableMap(uriVars);
/* 544 */       this
/* 545 */         .matrixVariables = (matrixVars != null) ? Collections.<String, MultiValueMap<String, String>>unmodifiableMap(matrixVars) : Collections.<String, MultiValueMap<String, String>>emptyMap();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<String, String> getUriVariables() {
/* 553 */       return this.uriVariables;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<String, MultiValueMap<String, String>> getMatrixVariables() {
/* 561 */       return this.matrixVariables;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 566 */       return "PathMatchInfo[uriVariables=" + this.uriVariables + ", matrixVariables=" + this.matrixVariables + "]";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class PathRemainingMatchInfo
/*     */   {
/*     */     private final PathContainer pathRemaining;
/*     */ 
/*     */ 
/*     */     
/*     */     private final PathPattern.PathMatchInfo pathMatchInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     PathRemainingMatchInfo(PathContainer pathRemaining) {
/* 585 */       this(pathRemaining, PathPattern.PathMatchInfo.EMPTY);
/*     */     }
/*     */     
/*     */     PathRemainingMatchInfo(PathContainer pathRemaining, PathPattern.PathMatchInfo pathMatchInfo) {
/* 589 */       this.pathRemaining = pathRemaining;
/* 590 */       this.pathMatchInfo = pathMatchInfo;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public PathContainer getPathRemaining() {
/* 597 */       return this.pathRemaining;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<String, String> getUriVariables() {
/* 605 */       return this.pathMatchInfo.getUriVariables();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<String, MultiValueMap<String, String>> getMatrixVariables() {
/* 612 */       return this.pathMatchInfo.getMatrixVariables();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   class MatchingContext
/*     */   {
/*     */     final PathContainer candidate;
/*     */ 
/*     */     
/*     */     final List<PathContainer.Element> pathElements;
/*     */ 
/*     */     
/*     */     final int pathLength;
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private Map<String, String> extractedUriVariables;
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private Map<String, MultiValueMap<String, String>> extractedMatrixVariables;
/*     */ 
/*     */     
/*     */     boolean extractingVariables;
/*     */ 
/*     */     
/*     */     boolean determineRemainingPath = false;
/*     */     
/*     */     int remainingPathIndex;
/*     */ 
/*     */     
/*     */     public MatchingContext(PathContainer pathContainer, boolean extractVariables) {
/* 646 */       this.candidate = pathContainer;
/* 647 */       this.pathElements = pathContainer.elements();
/* 648 */       this.pathLength = this.pathElements.size();
/* 649 */       this.extractingVariables = extractVariables;
/*     */     }
/*     */     
/*     */     public void setMatchAllowExtraPath() {
/* 653 */       this.determineRemainingPath = true;
/*     */     }
/*     */     
/*     */     public boolean isMatchOptionalTrailingSeparator() {
/* 657 */       return PathPattern.this.matchOptionalTrailingSeparator;
/*     */     }
/*     */     
/*     */     public void set(String key, String value, MultiValueMap<String, String> parameters) {
/* 661 */       if (this.extractedUriVariables == null) {
/* 662 */         this.extractedUriVariables = new HashMap<>();
/*     */       }
/* 664 */       this.extractedUriVariables.put(key, value);
/*     */       
/* 666 */       if (!parameters.isEmpty()) {
/* 667 */         if (this.extractedMatrixVariables == null) {
/* 668 */           this.extractedMatrixVariables = new HashMap<>();
/*     */         }
/* 670 */         this.extractedMatrixVariables.put(key, CollectionUtils.unmodifiableMultiValueMap(parameters));
/*     */       } 
/*     */     }
/*     */     
/*     */     public PathPattern.PathMatchInfo getPathMatchResult() {
/* 675 */       if (this.extractedUriVariables == null) {
/* 676 */         return PathPattern.PathMatchInfo.EMPTY;
/*     */       }
/*     */       
/* 679 */       return new PathPattern.PathMatchInfo(this.extractedUriVariables, this.extractedMatrixVariables);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean isSeparator(int pathIndex) {
/* 689 */       return this.pathElements.get(pathIndex) instanceof PathContainer.Separator;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     String pathElementValue(int pathIndex) {
/* 698 */       PathContainer.Element element = (pathIndex < this.pathLength) ? this.pathElements.get(pathIndex) : null;
/* 699 */       if (element instanceof PathContainer.PathSegment) {
/* 700 */         return ((PathContainer.PathSegment)element).valueToMatch();
/*     */       }
/* 702 */       return "";
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/pattern/PathPattern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */