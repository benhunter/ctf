/*     */ package org.springframework.web.servlet.mvc.condition;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.AntPathMatcher;
/*     */ import org.springframework.util.PathMatcher;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.util.UrlPathHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class PatternsRequestCondition
/*     */   extends AbstractRequestCondition<PatternsRequestCondition>
/*     */ {
/*     */   private final Set<String> patterns;
/*     */   private final UrlPathHelper pathHelper;
/*     */   private final PathMatcher pathMatcher;
/*     */   private final boolean useSuffixPatternMatch;
/*     */   private final boolean useTrailingSlashMatch;
/*  55 */   private final List<String> fileExtensions = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PatternsRequestCondition(String... patterns) {
/*  64 */     this(Arrays.asList(patterns), (UrlPathHelper)null, (PathMatcher)null, true, true, (List<String>)null);
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
/*     */   public PatternsRequestCondition(String[] patterns, @Nullable UrlPathHelper urlPathHelper, @Nullable PathMatcher pathMatcher, boolean useSuffixPatternMatch, boolean useTrailingSlashMatch) {
/*  79 */     this(Arrays.asList(patterns), urlPathHelper, pathMatcher, useSuffixPatternMatch, useTrailingSlashMatch, (List<String>)null);
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
/*     */   public PatternsRequestCondition(String[] patterns, @Nullable UrlPathHelper urlPathHelper, @Nullable PathMatcher pathMatcher, boolean useSuffixPatternMatch, boolean useTrailingSlashMatch, @Nullable List<String> fileExtensions) {
/*  96 */     this(Arrays.asList(patterns), urlPathHelper, pathMatcher, useSuffixPatternMatch, useTrailingSlashMatch, fileExtensions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PatternsRequestCondition(Collection<String> patterns, @Nullable UrlPathHelper urlPathHelper, @Nullable PathMatcher pathMatcher, boolean useSuffixPatternMatch, boolean useTrailingSlashMatch, @Nullable List<String> fileExtensions) {
/* 107 */     this.patterns = Collections.unmodifiableSet(prependLeadingSlash(patterns));
/* 108 */     this.pathHelper = (urlPathHelper != null) ? urlPathHelper : new UrlPathHelper();
/* 109 */     this.pathMatcher = (pathMatcher != null) ? pathMatcher : (PathMatcher)new AntPathMatcher();
/* 110 */     this.useSuffixPatternMatch = useSuffixPatternMatch;
/* 111 */     this.useTrailingSlashMatch = useTrailingSlashMatch;
/*     */     
/* 113 */     if (fileExtensions != null) {
/* 114 */       for (String fileExtension : fileExtensions) {
/* 115 */         if (fileExtension.charAt(0) != '.') {
/* 116 */           fileExtension = "." + fileExtension;
/*     */         }
/* 118 */         this.fileExtensions.add(fileExtension);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static Set<String> prependLeadingSlash(Collection<String> patterns) {
/* 125 */     Set<String> result = new LinkedHashSet<>(patterns.size());
/* 126 */     for (String pattern : patterns) {
/* 127 */       if (StringUtils.hasLength(pattern) && !pattern.startsWith("/")) {
/* 128 */         pattern = "/" + pattern;
/*     */       }
/* 130 */       result.add(pattern);
/*     */     } 
/* 132 */     return result;
/*     */   }
/*     */   
/*     */   public Set<String> getPatterns() {
/* 136 */     return this.patterns;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Collection<String> getContent() {
/* 141 */     return this.patterns;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getToStringInfix() {
/* 146 */     return " || ";
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
/*     */   public PatternsRequestCondition combine(PatternsRequestCondition other) {
/* 161 */     Set<String> result = new LinkedHashSet<>();
/* 162 */     if (!this.patterns.isEmpty() && !other.patterns.isEmpty()) {
/* 163 */       for (String pattern1 : this.patterns) {
/* 164 */         for (String pattern2 : other.patterns) {
/* 165 */           result.add(this.pathMatcher.combine(pattern1, pattern2));
/*     */         }
/*     */       }
/*     */     
/* 169 */     } else if (!this.patterns.isEmpty()) {
/* 170 */       result.addAll(this.patterns);
/*     */     }
/* 172 */     else if (!other.patterns.isEmpty()) {
/* 173 */       result.addAll(other.patterns);
/*     */     } else {
/*     */       
/* 176 */       result.add("");
/*     */     } 
/* 178 */     return new PatternsRequestCondition(result, this.pathHelper, this.pathMatcher, this.useSuffixPatternMatch, this.useTrailingSlashMatch, this.fileExtensions);
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
/*     */   @Nullable
/*     */   public PatternsRequestCondition getMatchingCondition(HttpServletRequest request) {
/* 201 */     if (this.patterns.isEmpty()) {
/* 202 */       return this;
/*     */     }
/* 204 */     String lookupPath = this.pathHelper.getLookupPathForRequest(request);
/* 205 */     List<String> matches = getMatchingPatterns(lookupPath);
/* 206 */     return !matches.isEmpty() ? new PatternsRequestCondition(matches, this.pathHelper, this.pathMatcher, this.useSuffixPatternMatch, this.useTrailingSlashMatch, this.fileExtensions) : null;
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
/*     */   public List<String> getMatchingPatterns(String lookupPath) {
/* 221 */     List<String> matches = new ArrayList<>();
/* 222 */     for (String pattern : this.patterns) {
/* 223 */       String match = getMatchingPattern(pattern, lookupPath);
/* 224 */       if (match != null) {
/* 225 */         matches.add(match);
/*     */       }
/*     */     } 
/* 228 */     if (matches.size() > 1) {
/* 229 */       matches.sort(this.pathMatcher.getPatternComparator(lookupPath));
/*     */     }
/* 231 */     return matches;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private String getMatchingPattern(String pattern, String lookupPath) {
/* 236 */     if (pattern.equals(lookupPath)) {
/* 237 */       return pattern;
/*     */     }
/* 239 */     if (this.useSuffixPatternMatch) {
/* 240 */       if (!this.fileExtensions.isEmpty() && lookupPath.indexOf('.') != -1) {
/* 241 */         for (String extension : this.fileExtensions) {
/* 242 */           if (this.pathMatcher.match(pattern + extension, lookupPath)) {
/* 243 */             return pattern + extension;
/*     */           }
/*     */         } 
/*     */       } else {
/*     */         
/* 248 */         boolean hasSuffix = (pattern.indexOf('.') != -1);
/* 249 */         if (!hasSuffix && this.pathMatcher.match(pattern + ".*", lookupPath)) {
/* 250 */           return pattern + ".*";
/*     */         }
/*     */       } 
/*     */     }
/* 254 */     if (this.pathMatcher.match(pattern, lookupPath)) {
/* 255 */       return pattern;
/*     */     }
/* 257 */     if (this.useTrailingSlashMatch && 
/* 258 */       !pattern.endsWith("/") && this.pathMatcher.match(pattern + "/", lookupPath)) {
/* 259 */       return pattern + "/";
/*     */     }
/*     */     
/* 262 */     return null;
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
/*     */   public int compareTo(PatternsRequestCondition other, HttpServletRequest request) {
/* 278 */     String lookupPath = this.pathHelper.getLookupPathForRequest(request);
/* 279 */     Comparator<String> patternComparator = this.pathMatcher.getPatternComparator(lookupPath);
/* 280 */     Iterator<String> iterator = this.patterns.iterator();
/* 281 */     Iterator<String> iteratorOther = other.patterns.iterator();
/* 282 */     while (iterator.hasNext() && iteratorOther.hasNext()) {
/* 283 */       int result = patternComparator.compare(iterator.next(), iteratorOther.next());
/* 284 */       if (result != 0) {
/* 285 */         return result;
/*     */       }
/*     */     } 
/* 288 */     if (iterator.hasNext()) {
/* 289 */       return -1;
/*     */     }
/* 291 */     if (iteratorOther.hasNext()) {
/* 292 */       return 1;
/*     */     }
/*     */     
/* 295 */     return 0;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/condition/PatternsRequestCondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */