/*     */ package org.springframework.web.servlet.config.annotation;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.function.Predicate;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.PathMatcher;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PathMatchConfigurer
/*     */ {
/*     */   @Nullable
/*     */   private Boolean suffixPatternMatch;
/*     */   @Nullable
/*     */   private Boolean trailingSlashMatch;
/*     */   @Nullable
/*     */   private Boolean registeredSuffixPatternMatch;
/*     */   @Nullable
/*     */   private UrlPathHelper urlPathHelper;
/*     */   @Nullable
/*     */   private PathMatcher pathMatcher;
/*     */   @Nullable
/*     */   private Map<String, Predicate<Class<?>>> pathPrefixes;
/*     */   
/*     */   public PathMatchConfigurer setUseSuffixPatternMatch(Boolean suffixPatternMatch) {
/*  71 */     this.suffixPatternMatch = suffixPatternMatch;
/*  72 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathMatchConfigurer setUseTrailingSlashMatch(Boolean trailingSlashMatch) {
/*  81 */     this.trailingSlashMatch = trailingSlashMatch;
/*  82 */     return this;
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
/*     */   public PathMatchConfigurer setUseRegisteredSuffixPatternMatch(Boolean registeredSuffixPatternMatch) {
/*  95 */     this.registeredSuffixPatternMatch = registeredSuffixPatternMatch;
/*  96 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathMatchConfigurer setUrlPathHelper(UrlPathHelper urlPathHelper) {
/* 106 */     this.urlPathHelper = urlPathHelper;
/* 107 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathMatchConfigurer setPathMatcher(PathMatcher pathMatcher) {
/* 116 */     this.pathMatcher = pathMatcher;
/* 117 */     return this;
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
/*     */   public PathMatchConfigurer addPathPrefix(String prefix, Predicate<Class<?>> predicate) {
/* 132 */     if (this.pathPrefixes == null) {
/* 133 */       this.pathPrefixes = new LinkedHashMap<>();
/*     */     }
/* 135 */     this.pathPrefixes.put(prefix, predicate);
/* 136 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Boolean isUseSuffixPatternMatch() {
/* 142 */     return this.suffixPatternMatch;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Boolean isUseTrailingSlashMatch() {
/* 147 */     return this.trailingSlashMatch;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Boolean isUseRegisteredSuffixPatternMatch() {
/* 152 */     return this.registeredSuffixPatternMatch;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public UrlPathHelper getUrlPathHelper() {
/* 157 */     return this.urlPathHelper;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public PathMatcher getPathMatcher() {
/* 162 */     return this.pathMatcher;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected Map<String, Predicate<Class<?>>> getPathPrefixes() {
/* 167 */     return this.pathPrefixes;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/config/annotation/PathMatchConfigurer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */