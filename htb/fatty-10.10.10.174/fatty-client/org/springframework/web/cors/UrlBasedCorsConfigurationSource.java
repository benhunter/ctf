/*     */ package org.springframework.web.cors;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.AntPathMatcher;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class UrlBasedCorsConfigurationSource
/*     */   implements CorsConfigurationSource
/*     */ {
/*  42 */   private final Map<String, CorsConfiguration> corsConfigurations = new LinkedHashMap<>();
/*     */   
/*  44 */   private PathMatcher pathMatcher = (PathMatcher)new AntPathMatcher();
/*     */   
/*  46 */   private UrlPathHelper urlPathHelper = new UrlPathHelper();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPathMatcher(PathMatcher pathMatcher) {
/*  55 */     Assert.notNull(pathMatcher, "PathMatcher must not be null");
/*  56 */     this.pathMatcher = pathMatcher;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAlwaysUseFullPath(boolean alwaysUseFullPath) {
/*  64 */     this.urlPathHelper.setAlwaysUseFullPath(alwaysUseFullPath);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUrlDecode(boolean urlDecode) {
/*  72 */     this.urlPathHelper.setUrlDecode(urlDecode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemoveSemicolonContent(boolean removeSemicolonContent) {
/*  80 */     this.urlPathHelper.setRemoveSemicolonContent(removeSemicolonContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
/*  88 */     Assert.notNull(urlPathHelper, "UrlPathHelper must not be null");
/*  89 */     this.urlPathHelper = urlPathHelper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCorsConfigurations(@Nullable Map<String, CorsConfiguration> corsConfigurations) {
/*  96 */     this.corsConfigurations.clear();
/*  97 */     if (corsConfigurations != null) {
/*  98 */       this.corsConfigurations.putAll(corsConfigurations);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, CorsConfiguration> getCorsConfigurations() {
/* 106 */     return Collections.unmodifiableMap(this.corsConfigurations);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerCorsConfiguration(String path, CorsConfiguration config) {
/* 113 */     this.corsConfigurations.put(path, config);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
/* 120 */     String lookupPath = this.urlPathHelper.getLookupPathForRequest(request);
/* 121 */     for (Map.Entry<String, CorsConfiguration> entry : this.corsConfigurations.entrySet()) {
/* 122 */       if (this.pathMatcher.match(entry.getKey(), lookupPath)) {
/* 123 */         return entry.getValue();
/*     */       }
/*     */     } 
/* 126 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/cors/UrlBasedCorsConfigurationSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */