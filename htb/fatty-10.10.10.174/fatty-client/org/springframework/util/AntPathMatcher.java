/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AntPathMatcher
/*     */   implements PathMatcher
/*     */ {
/*     */   public static final String DEFAULT_PATH_SEPARATOR = "/";
/*     */   private static final int CACHE_TURNOFF_THRESHOLD = 65536;
/*  80 */   private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{[^/]+?\\}");
/*     */   
/*  82 */   private static final char[] WILDCARD_CHARS = new char[] { '*', '?', '{' };
/*     */ 
/*     */   
/*     */   private String pathSeparator;
/*     */   
/*     */   private PathSeparatorPatternCache pathSeparatorPatternCache;
/*     */   
/*     */   private boolean caseSensitive = true;
/*     */   
/*     */   private boolean trimTokens = false;
/*     */   
/*     */   @Nullable
/*     */   private volatile Boolean cachePatterns;
/*     */   
/*  96 */   private final Map<String, String[]> tokenizedPatternCache = (Map)new ConcurrentHashMap<>(256);
/*     */   
/*  98 */   final Map<String, AntPathStringMatcher> stringMatcherCache = new ConcurrentHashMap<>(256);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AntPathMatcher() {
/* 105 */     this.pathSeparator = "/";
/* 106 */     this.pathSeparatorPatternCache = new PathSeparatorPatternCache("/");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AntPathMatcher(String pathSeparator) {
/* 115 */     Assert.notNull(pathSeparator, "'pathSeparator' is required");
/* 116 */     this.pathSeparator = pathSeparator;
/* 117 */     this.pathSeparatorPatternCache = new PathSeparatorPatternCache(pathSeparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPathSeparator(@Nullable String pathSeparator) {
/* 126 */     this.pathSeparator = (pathSeparator != null) ? pathSeparator : "/";
/* 127 */     this.pathSeparatorPatternCache = new PathSeparatorPatternCache(this.pathSeparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCaseSensitive(boolean caseSensitive) {
/* 136 */     this.caseSensitive = caseSensitive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTrimTokens(boolean trimTokens) {
/* 144 */     this.trimTokens = trimTokens;
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
/*     */   public void setCachePatterns(boolean cachePatterns) {
/* 160 */     this.cachePatterns = Boolean.valueOf(cachePatterns);
/*     */   }
/*     */   
/*     */   private void deactivatePatternCache() {
/* 164 */     this.cachePatterns = Boolean.valueOf(false);
/* 165 */     this.tokenizedPatternCache.clear();
/* 166 */     this.stringMatcherCache.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPattern(String path) {
/* 172 */     boolean uriVar = false;
/* 173 */     for (int i = 0; i < path.length(); i++) {
/* 174 */       char c = path.charAt(i);
/* 175 */       if (c == '*' || c == '?') {
/* 176 */         return true;
/*     */       }
/* 178 */       if (c == '{') {
/* 179 */         uriVar = true;
/*     */       
/*     */       }
/* 182 */       else if (c == '}' && uriVar) {
/* 183 */         return true;
/*     */       } 
/*     */     } 
/* 186 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean match(String pattern, String path) {
/* 191 */     return doMatch(pattern, path, true, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matchStart(String pattern, String path) {
/* 196 */     return doMatch(pattern, path, false, null);
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
/*     */   protected boolean doMatch(String pattern, String path, boolean fullMatch, @Nullable Map<String, String> uriTemplateVariables) {
/* 210 */     if (path.startsWith(this.pathSeparator) != pattern.startsWith(this.pathSeparator)) {
/* 211 */       return false;
/*     */     }
/*     */     
/* 214 */     String[] pattDirs = tokenizePattern(pattern);
/* 215 */     if (fullMatch && this.caseSensitive && !isPotentialMatch(path, pattDirs)) {
/* 216 */       return false;
/*     */     }
/*     */     
/* 219 */     String[] pathDirs = tokenizePath(path);
/*     */     
/* 221 */     int pattIdxStart = 0;
/* 222 */     int pattIdxEnd = pattDirs.length - 1;
/* 223 */     int pathIdxStart = 0;
/* 224 */     int pathIdxEnd = pathDirs.length - 1;
/*     */ 
/*     */     
/* 227 */     while (pattIdxStart <= pattIdxEnd && pathIdxStart <= pathIdxEnd) {
/* 228 */       String pattDir = pattDirs[pattIdxStart];
/* 229 */       if ("**".equals(pattDir)) {
/*     */         break;
/*     */       }
/* 232 */       if (!matchStrings(pattDir, pathDirs[pathIdxStart], uriTemplateVariables)) {
/* 233 */         return false;
/*     */       }
/* 235 */       pattIdxStart++;
/* 236 */       pathIdxStart++;
/*     */     } 
/*     */     
/* 239 */     if (pathIdxStart > pathIdxEnd) {
/*     */       
/* 241 */       if (pattIdxStart > pattIdxEnd) {
/* 242 */         return (pattern.endsWith(this.pathSeparator) == path.endsWith(this.pathSeparator));
/*     */       }
/* 244 */       if (!fullMatch) {
/* 245 */         return true;
/*     */       }
/* 247 */       if (pattIdxStart == pattIdxEnd && pattDirs[pattIdxStart].equals("*") && path.endsWith(this.pathSeparator)) {
/* 248 */         return true;
/*     */       }
/* 250 */       for (int j = pattIdxStart; j <= pattIdxEnd; j++) {
/* 251 */         if (!pattDirs[j].equals("**")) {
/* 252 */           return false;
/*     */         }
/*     */       } 
/* 255 */       return true;
/*     */     } 
/* 257 */     if (pattIdxStart > pattIdxEnd)
/*     */     {
/* 259 */       return false;
/*     */     }
/* 261 */     if (!fullMatch && "**".equals(pattDirs[pattIdxStart]))
/*     */     {
/* 263 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 267 */     while (pattIdxStart <= pattIdxEnd && pathIdxStart <= pathIdxEnd) {
/* 268 */       String pattDir = pattDirs[pattIdxEnd];
/* 269 */       if (pattDir.equals("**")) {
/*     */         break;
/*     */       }
/* 272 */       if (!matchStrings(pattDir, pathDirs[pathIdxEnd], uriTemplateVariables)) {
/* 273 */         return false;
/*     */       }
/* 275 */       pattIdxEnd--;
/* 276 */       pathIdxEnd--;
/*     */     } 
/* 278 */     if (pathIdxStart > pathIdxEnd) {
/*     */       
/* 280 */       for (int j = pattIdxStart; j <= pattIdxEnd; j++) {
/* 281 */         if (!pattDirs[j].equals("**")) {
/* 282 */           return false;
/*     */         }
/*     */       } 
/* 285 */       return true;
/*     */     } 
/*     */     
/* 288 */     while (pattIdxStart != pattIdxEnd && pathIdxStart <= pathIdxEnd) {
/* 289 */       int patIdxTmp = -1;
/* 290 */       for (int j = pattIdxStart + 1; j <= pattIdxEnd; j++) {
/* 291 */         if (pattDirs[j].equals("**")) {
/* 292 */           patIdxTmp = j;
/*     */           break;
/*     */         } 
/*     */       } 
/* 296 */       if (patIdxTmp == pattIdxStart + 1) {
/*     */         
/* 298 */         pattIdxStart++;
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 303 */       int patLength = patIdxTmp - pattIdxStart - 1;
/* 304 */       int strLength = pathIdxEnd - pathIdxStart + 1;
/* 305 */       int foundIdx = -1;
/*     */ 
/*     */       
/* 308 */       for (int k = 0; k <= strLength - patLength; ) {
/* 309 */         for (int m = 0; m < patLength; m++) {
/* 310 */           String subPat = pattDirs[pattIdxStart + m + 1];
/* 311 */           String subStr = pathDirs[pathIdxStart + k + m];
/* 312 */           if (!matchStrings(subPat, subStr, uriTemplateVariables)) {
/*     */             k++; continue;
/*     */           } 
/*     */         } 
/* 316 */         foundIdx = pathIdxStart + k;
/*     */       } 
/*     */ 
/*     */       
/* 320 */       if (foundIdx == -1) {
/* 321 */         return false;
/*     */       }
/*     */       
/* 324 */       pattIdxStart = patIdxTmp;
/* 325 */       pathIdxStart = foundIdx + patLength;
/*     */     } 
/*     */     
/* 328 */     for (int i = pattIdxStart; i <= pattIdxEnd; i++) {
/* 329 */       if (!pattDirs[i].equals("**")) {
/* 330 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 334 */     return true;
/*     */   }
/*     */   
/*     */   private boolean isPotentialMatch(String path, String[] pattDirs) {
/* 338 */     if (!this.trimTokens) {
/* 339 */       int pos = 0;
/* 340 */       for (String pattDir : pattDirs) {
/* 341 */         int skipped = skipSeparator(path, pos, this.pathSeparator);
/* 342 */         pos += skipped;
/* 343 */         skipped = skipSegment(path, pos, pattDir);
/* 344 */         if (skipped < pattDir.length()) {
/* 345 */           return (skipped > 0 || (pattDir.length() > 0 && isWildcardChar(pattDir.charAt(0))));
/*     */         }
/* 347 */         pos += skipped;
/*     */       } 
/*     */     } 
/* 350 */     return true;
/*     */   }
/*     */   
/*     */   private int skipSegment(String path, int pos, String prefix) {
/* 354 */     int skipped = 0;
/* 355 */     for (int i = 0; i < prefix.length(); i++) {
/* 356 */       char c = prefix.charAt(i);
/* 357 */       if (isWildcardChar(c)) {
/* 358 */         return skipped;
/*     */       }
/* 360 */       int currPos = pos + skipped;
/* 361 */       if (currPos >= path.length()) {
/* 362 */         return 0;
/*     */       }
/* 364 */       if (c == path.charAt(currPos)) {
/* 365 */         skipped++;
/*     */       }
/*     */     } 
/* 368 */     return skipped;
/*     */   }
/*     */   
/*     */   private int skipSeparator(String path, int pos, String separator) {
/* 372 */     int skipped = 0;
/* 373 */     while (path.startsWith(separator, pos + skipped)) {
/* 374 */       skipped += separator.length();
/*     */     }
/* 376 */     return skipped;
/*     */   }
/*     */   
/*     */   private boolean isWildcardChar(char c) {
/* 380 */     for (char candidate : WILDCARD_CHARS) {
/* 381 */       if (c == candidate) {
/* 382 */         return true;
/*     */       }
/*     */     } 
/* 385 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String[] tokenizePattern(String pattern) {
/* 396 */     String[] tokenized = null;
/* 397 */     Boolean cachePatterns = this.cachePatterns;
/* 398 */     if (cachePatterns == null || cachePatterns.booleanValue()) {
/* 399 */       tokenized = this.tokenizedPatternCache.get(pattern);
/*     */     }
/* 401 */     if (tokenized == null) {
/* 402 */       tokenized = tokenizePath(pattern);
/* 403 */       if (cachePatterns == null && this.tokenizedPatternCache.size() >= 65536) {
/*     */ 
/*     */ 
/*     */         
/* 407 */         deactivatePatternCache();
/* 408 */         return tokenized;
/*     */       } 
/* 410 */       if (cachePatterns == null || cachePatterns.booleanValue()) {
/* 411 */         this.tokenizedPatternCache.put(pattern, tokenized);
/*     */       }
/*     */     } 
/* 414 */     return tokenized;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String[] tokenizePath(String path) {
/* 423 */     return StringUtils.tokenizeToStringArray(path, this.pathSeparator, this.trimTokens, true);
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
/*     */   private boolean matchStrings(String pattern, String str, @Nullable Map<String, String> uriTemplateVariables) {
/* 435 */     return getStringMatcher(pattern).matchStrings(str, uriTemplateVariables);
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
/*     */   protected AntPathStringMatcher getStringMatcher(String pattern) {
/* 452 */     AntPathStringMatcher matcher = null;
/* 453 */     Boolean cachePatterns = this.cachePatterns;
/* 454 */     if (cachePatterns == null || cachePatterns.booleanValue()) {
/* 455 */       matcher = this.stringMatcherCache.get(pattern);
/*     */     }
/* 457 */     if (matcher == null) {
/* 458 */       matcher = new AntPathStringMatcher(pattern, this.caseSensitive);
/* 459 */       if (cachePatterns == null && this.stringMatcherCache.size() >= 65536) {
/*     */ 
/*     */ 
/*     */         
/* 463 */         deactivatePatternCache();
/* 464 */         return matcher;
/*     */       } 
/* 466 */       if (cachePatterns == null || cachePatterns.booleanValue()) {
/* 467 */         this.stringMatcherCache.put(pattern, matcher);
/*     */       }
/*     */     } 
/* 470 */     return matcher;
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
/*     */   public String extractPathWithinPattern(String pattern, String path) {
/* 488 */     String[] patternParts = StringUtils.tokenizeToStringArray(pattern, this.pathSeparator, this.trimTokens, true);
/* 489 */     String[] pathParts = StringUtils.tokenizeToStringArray(path, this.pathSeparator, this.trimTokens, true);
/* 490 */     StringBuilder builder = new StringBuilder();
/* 491 */     boolean pathStarted = false;
/*     */     
/* 493 */     for (int segment = 0; segment < patternParts.length; segment++) {
/* 494 */       String patternPart = patternParts[segment];
/* 495 */       if (patternPart.indexOf('*') > -1 || patternPart.indexOf('?') > -1) {
/* 496 */         for (; segment < pathParts.length; segment++) {
/* 497 */           if (pathStarted || (segment == 0 && !pattern.startsWith(this.pathSeparator))) {
/* 498 */             builder.append(this.pathSeparator);
/*     */           }
/* 500 */           builder.append(pathParts[segment]);
/* 501 */           pathStarted = true;
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 506 */     return builder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> extractUriTemplateVariables(String pattern, String path) {
/* 511 */     Map<String, String> variables = new LinkedHashMap<>();
/* 512 */     boolean result = doMatch(pattern, path, true, variables);
/* 513 */     if (!result) {
/* 514 */       throw new IllegalStateException("Pattern \"" + pattern + "\" is not a match for \"" + path + "\"");
/*     */     }
/* 516 */     return variables;
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
/*     */ 
/*     */   
/*     */   public String combine(String pattern1, String pattern2) {
/* 549 */     if (!StringUtils.hasText(pattern1) && !StringUtils.hasText(pattern2)) {
/* 550 */       return "";
/*     */     }
/* 552 */     if (!StringUtils.hasText(pattern1)) {
/* 553 */       return pattern2;
/*     */     }
/* 555 */     if (!StringUtils.hasText(pattern2)) {
/* 556 */       return pattern1;
/*     */     }
/*     */     
/* 559 */     boolean pattern1ContainsUriVar = (pattern1.indexOf('{') != -1);
/* 560 */     if (!pattern1.equals(pattern2) && !pattern1ContainsUriVar && match(pattern1, pattern2))
/*     */     {
/*     */       
/* 563 */       return pattern2;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 568 */     if (pattern1.endsWith(this.pathSeparatorPatternCache.getEndsOnWildCard())) {
/* 569 */       return concat(pattern1.substring(0, pattern1.length() - 2), pattern2);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 574 */     if (pattern1.endsWith(this.pathSeparatorPatternCache.getEndsOnDoubleWildCard())) {
/* 575 */       return concat(pattern1, pattern2);
/*     */     }
/*     */     
/* 578 */     int starDotPos1 = pattern1.indexOf("*.");
/* 579 */     if (pattern1ContainsUriVar || starDotPos1 == -1 || this.pathSeparator.equals("."))
/*     */     {
/* 581 */       return concat(pattern1, pattern2);
/*     */     }
/*     */     
/* 584 */     String ext1 = pattern1.substring(starDotPos1 + 1);
/* 585 */     int dotPos2 = pattern2.indexOf('.');
/* 586 */     String file2 = (dotPos2 == -1) ? pattern2 : pattern2.substring(0, dotPos2);
/* 587 */     String ext2 = (dotPos2 == -1) ? "" : pattern2.substring(dotPos2);
/* 588 */     boolean ext1All = (ext1.equals(".*") || ext1.isEmpty());
/* 589 */     boolean ext2All = (ext2.equals(".*") || ext2.isEmpty());
/* 590 */     if (!ext1All && !ext2All) {
/* 591 */       throw new IllegalArgumentException("Cannot combine patterns: " + pattern1 + " vs " + pattern2);
/*     */     }
/* 593 */     String ext = ext1All ? ext2 : ext1;
/* 594 */     return file2 + ext;
/*     */   }
/*     */   
/*     */   private String concat(String path1, String path2) {
/* 598 */     boolean path1EndsWithSeparator = path1.endsWith(this.pathSeparator);
/* 599 */     boolean path2StartsWithSeparator = path2.startsWith(this.pathSeparator);
/*     */     
/* 601 */     if (path1EndsWithSeparator && path2StartsWithSeparator) {
/* 602 */       return path1 + path2.substring(1);
/*     */     }
/* 604 */     if (path1EndsWithSeparator || path2StartsWithSeparator) {
/* 605 */       return path1 + path2;
/*     */     }
/*     */     
/* 608 */     return path1 + this.pathSeparator + path2;
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
/*     */   public Comparator<String> getPatternComparator(String path) {
/* 630 */     return new AntPatternComparator(path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class AntPathStringMatcher
/*     */   {
/* 641 */     private static final Pattern GLOB_PATTERN = Pattern.compile("\\?|\\*|\\{((?:\\{[^/]+?\\}|[^/{}]|\\\\[{}])+?)\\}");
/*     */     
/*     */     private static final String DEFAULT_VARIABLE_PATTERN = "(.*)";
/*     */     
/*     */     private final Pattern pattern;
/*     */     
/* 647 */     private final List<String> variableNames = new LinkedList<>();
/*     */     
/*     */     public AntPathStringMatcher(String pattern) {
/* 650 */       this(pattern, true);
/*     */     }
/*     */     
/*     */     public AntPathStringMatcher(String pattern, boolean caseSensitive) {
/* 654 */       StringBuilder patternBuilder = new StringBuilder();
/* 655 */       Matcher matcher = GLOB_PATTERN.matcher(pattern);
/* 656 */       int end = 0;
/* 657 */       while (matcher.find()) {
/* 658 */         patternBuilder.append(quote(pattern, end, matcher.start()));
/* 659 */         String match = matcher.group();
/* 660 */         if ("?".equals(match)) {
/* 661 */           patternBuilder.append('.');
/*     */         }
/* 663 */         else if ("*".equals(match)) {
/* 664 */           patternBuilder.append(".*");
/*     */         }
/* 666 */         else if (match.startsWith("{") && match.endsWith("}")) {
/* 667 */           int colonIdx = match.indexOf(':');
/* 668 */           if (colonIdx == -1) {
/* 669 */             patternBuilder.append("(.*)");
/* 670 */             this.variableNames.add(matcher.group(1));
/*     */           } else {
/*     */             
/* 673 */             String variablePattern = match.substring(colonIdx + 1, match.length() - 1);
/* 674 */             patternBuilder.append('(');
/* 675 */             patternBuilder.append(variablePattern);
/* 676 */             patternBuilder.append(')');
/* 677 */             String variableName = match.substring(1, colonIdx);
/* 678 */             this.variableNames.add(variableName);
/*     */           } 
/*     */         } 
/* 681 */         end = matcher.end();
/*     */       } 
/* 683 */       patternBuilder.append(quote(pattern, end, pattern.length()));
/* 684 */       this
/* 685 */         .pattern = caseSensitive ? Pattern.compile(patternBuilder.toString()) : Pattern.compile(patternBuilder.toString(), 2);
/*     */     }
/*     */     
/*     */     private String quote(String s, int start, int end) {
/* 689 */       if (start == end) {
/* 690 */         return "";
/*     */       }
/* 692 */       return Pattern.quote(s.substring(start, end));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean matchStrings(String str, @Nullable Map<String, String> uriTemplateVariables) {
/* 700 */       Matcher matcher = this.pattern.matcher(str);
/* 701 */       if (matcher.matches()) {
/* 702 */         if (uriTemplateVariables != null) {
/*     */           
/* 704 */           if (this.variableNames.size() != matcher.groupCount()) {
/* 705 */             throw new IllegalArgumentException("The number of capturing groups in the pattern segment " + this.pattern + " does not match the number of URI template variables it defines, which can occur if capturing groups are used in a URI template regex. Use non-capturing groups instead.");
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 710 */           for (int i = 1; i <= matcher.groupCount(); i++) {
/* 711 */             String name = this.variableNames.get(i - 1);
/* 712 */             String value = matcher.group(i);
/* 713 */             uriTemplateVariables.put(name, value);
/*     */           } 
/*     */         } 
/* 716 */         return true;
/*     */       } 
/*     */       
/* 719 */       return false;
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
/*     */   protected static class AntPatternComparator
/*     */     implements Comparator<String>
/*     */   {
/*     */     private final String path;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public AntPatternComparator(String path) {
/* 743 */       this.path = path;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int compare(String pattern1, String pattern2) {
/* 754 */       PatternInfo info1 = new PatternInfo(pattern1);
/* 755 */       PatternInfo info2 = new PatternInfo(pattern2);
/*     */       
/* 757 */       if (info1.isLeastSpecific() && info2.isLeastSpecific()) {
/* 758 */         return 0;
/*     */       }
/* 760 */       if (info1.isLeastSpecific()) {
/* 761 */         return 1;
/*     */       }
/* 763 */       if (info2.isLeastSpecific()) {
/* 764 */         return -1;
/*     */       }
/*     */       
/* 767 */       boolean pattern1EqualsPath = pattern1.equals(this.path);
/* 768 */       boolean pattern2EqualsPath = pattern2.equals(this.path);
/* 769 */       if (pattern1EqualsPath && pattern2EqualsPath) {
/* 770 */         return 0;
/*     */       }
/* 772 */       if (pattern1EqualsPath) {
/* 773 */         return -1;
/*     */       }
/* 775 */       if (pattern2EqualsPath) {
/* 776 */         return 1;
/*     */       }
/*     */       
/* 779 */       if (info1.isPrefixPattern() && info2.getDoubleWildcards() == 0) {
/* 780 */         return 1;
/*     */       }
/* 782 */       if (info2.isPrefixPattern() && info1.getDoubleWildcards() == 0) {
/* 783 */         return -1;
/*     */       }
/*     */       
/* 786 */       if (info1.getTotalCount() != info2.getTotalCount()) {
/* 787 */         return info1.getTotalCount() - info2.getTotalCount();
/*     */       }
/*     */       
/* 790 */       if (info1.getLength() != info2.getLength()) {
/* 791 */         return info2.getLength() - info1.getLength();
/*     */       }
/*     */       
/* 794 */       if (info1.getSingleWildcards() < info2.getSingleWildcards()) {
/* 795 */         return -1;
/*     */       }
/* 797 */       if (info2.getSingleWildcards() < info1.getSingleWildcards()) {
/* 798 */         return 1;
/*     */       }
/*     */       
/* 801 */       if (info1.getUriVars() < info2.getUriVars()) {
/* 802 */         return -1;
/*     */       }
/* 804 */       if (info2.getUriVars() < info1.getUriVars()) {
/* 805 */         return 1;
/*     */       }
/*     */       
/* 808 */       return 0;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private static class PatternInfo
/*     */     {
/*     */       @Nullable
/*     */       private final String pattern;
/*     */ 
/*     */       
/*     */       private int uriVars;
/*     */ 
/*     */       
/*     */       private int singleWildcards;
/*     */       
/*     */       private int doubleWildcards;
/*     */       
/*     */       private boolean catchAllPattern;
/*     */       
/*     */       private boolean prefixPattern;
/*     */       
/*     */       @Nullable
/*     */       private Integer length;
/*     */ 
/*     */       
/*     */       public PatternInfo(@Nullable String pattern) {
/* 835 */         this.pattern = pattern;
/* 836 */         if (this.pattern != null) {
/* 837 */           initCounters();
/* 838 */           this.catchAllPattern = this.pattern.equals("/**");
/* 839 */           this.prefixPattern = (!this.catchAllPattern && this.pattern.endsWith("/**"));
/*     */         } 
/* 841 */         if (this.uriVars == 0) {
/* 842 */           this.length = Integer.valueOf((this.pattern != null) ? this.pattern.length() : 0);
/*     */         }
/*     */       }
/*     */       
/*     */       protected void initCounters() {
/* 847 */         int pos = 0;
/* 848 */         if (this.pattern != null) {
/* 849 */           while (pos < this.pattern.length()) {
/* 850 */             if (this.pattern.charAt(pos) == '{') {
/* 851 */               this.uriVars++;
/* 852 */               pos++; continue;
/*     */             } 
/* 854 */             if (this.pattern.charAt(pos) == '*') {
/* 855 */               if (pos + 1 < this.pattern.length() && this.pattern.charAt(pos + 1) == '*') {
/* 856 */                 this.doubleWildcards++;
/* 857 */                 pos += 2; continue;
/*     */               } 
/* 859 */               if (pos > 0 && !this.pattern.substring(pos - 1).equals(".*")) {
/* 860 */                 this.singleWildcards++;
/* 861 */                 pos++;
/*     */                 continue;
/*     */               } 
/* 864 */               pos++;
/*     */               
/*     */               continue;
/*     */             } 
/* 868 */             pos++;
/*     */           } 
/*     */         }
/*     */       }
/*     */ 
/*     */       
/*     */       public int getUriVars() {
/* 875 */         return this.uriVars;
/*     */       }
/*     */       
/*     */       public int getSingleWildcards() {
/* 879 */         return this.singleWildcards;
/*     */       }
/*     */       
/*     */       public int getDoubleWildcards() {
/* 883 */         return this.doubleWildcards;
/*     */       }
/*     */       
/*     */       public boolean isLeastSpecific() {
/* 887 */         return (this.pattern == null || this.catchAllPattern);
/*     */       }
/*     */       
/*     */       public boolean isPrefixPattern() {
/* 891 */         return this.prefixPattern;
/*     */       }
/*     */       
/*     */       public int getTotalCount() {
/* 895 */         return this.uriVars + this.singleWildcards + 2 * this.doubleWildcards;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public int getLength() {
/* 902 */         if (this.length == null) {
/* 903 */           this.length = Integer.valueOf((this.pattern != null) ? AntPathMatcher
/* 904 */               .VARIABLE_PATTERN.matcher(this.pattern).replaceAll("#").length() : 0);
/*     */         }
/* 906 */         return this.length.intValue();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class PathSeparatorPatternCache
/*     */   {
/*     */     private final String endsOnWildCard;
/*     */ 
/*     */     
/*     */     private final String endsOnDoubleWildCard;
/*     */ 
/*     */     
/*     */     public PathSeparatorPatternCache(String pathSeparator) {
/* 922 */       this.endsOnWildCard = pathSeparator + "*";
/* 923 */       this.endsOnDoubleWildCard = pathSeparator + "**";
/*     */     }
/*     */     
/*     */     public String getEndsOnWildCard() {
/* 927 */       return this.endsOnWildCard;
/*     */     }
/*     */     
/*     */     public String getEndsOnDoubleWildCard() {
/* 931 */       return this.endsOnDoubleWildCard;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/AntPathMatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */