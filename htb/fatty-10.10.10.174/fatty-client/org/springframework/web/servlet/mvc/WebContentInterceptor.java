/*     */ package org.springframework.web.servlet.mvc;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.http.CacheControl;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.AntPathMatcher;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.PathMatcher;
/*     */ import org.springframework.web.servlet.HandlerInterceptor;
/*     */ import org.springframework.web.servlet.ModelAndView;
/*     */ import org.springframework.web.servlet.support.WebContentGenerator;
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
/*     */ public class WebContentInterceptor
/*     */   extends WebContentGenerator
/*     */   implements HandlerInterceptor
/*     */ {
/*  54 */   private UrlPathHelper urlPathHelper = new UrlPathHelper();
/*     */   
/*  56 */   private PathMatcher pathMatcher = (PathMatcher)new AntPathMatcher();
/*     */   
/*  58 */   private Map<String, Integer> cacheMappings = new HashMap<>();
/*     */   
/*  60 */   private Map<String, CacheControl> cacheControlMappings = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebContentInterceptor() {
/*  66 */     super(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAlwaysUseFullPath(boolean alwaysUseFullPath) {
/*  77 */     this.urlPathHelper.setAlwaysUseFullPath(alwaysUseFullPath);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUrlDecode(boolean urlDecode) {
/*  87 */     this.urlPathHelper.setUrlDecode(urlDecode);
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
/*     */   public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
/* 100 */     Assert.notNull(urlPathHelper, "UrlPathHelper must not be null");
/* 101 */     this.urlPathHelper = urlPathHelper;
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
/*     */   public void setCacheMappings(Properties cacheMappings) {
/* 120 */     this.cacheMappings.clear();
/* 121 */     Enumeration<?> propNames = cacheMappings.propertyNames();
/* 122 */     while (propNames.hasMoreElements()) {
/* 123 */       String path = (String)propNames.nextElement();
/* 124 */       int cacheSeconds = Integer.parseInt(cacheMappings.getProperty(path));
/* 125 */       this.cacheMappings.put(path, Integer.valueOf(cacheSeconds));
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
/*     */   public void addCacheMapping(CacheControl cacheControl, String... paths) {
/* 147 */     for (String path : paths) {
/* 148 */       this.cacheControlMappings.put(path, cacheControl);
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
/*     */   public void setPathMatcher(PathMatcher pathMatcher) {
/* 161 */     Assert.notNull(pathMatcher, "PathMatcher must not be null");
/* 162 */     this.pathMatcher = pathMatcher;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
/* 170 */     checkRequest(request);
/*     */     
/* 172 */     String lookupPath = this.urlPathHelper.getLookupPathForRequest(request);
/*     */     
/* 174 */     CacheControl cacheControl = lookupCacheControl(lookupPath);
/* 175 */     Integer cacheSeconds = lookupCacheSeconds(lookupPath);
/* 176 */     if (cacheControl != null) {
/* 177 */       if (this.logger.isTraceEnabled()) {
/* 178 */         this.logger.trace("Applying " + cacheControl);
/*     */       }
/* 180 */       applyCacheControl(response, cacheControl);
/*     */     }
/* 182 */     else if (cacheSeconds != null) {
/* 183 */       if (this.logger.isTraceEnabled()) {
/* 184 */         this.logger.trace("Applying cacheSeconds " + cacheSeconds);
/*     */       }
/* 186 */       applyCacheSeconds(response, cacheSeconds.intValue());
/*     */     } else {
/*     */       
/* 189 */       if (this.logger.isTraceEnabled()) {
/* 190 */         this.logger.trace("Applying default cacheSeconds");
/*     */       }
/* 192 */       prepareResponse(response);
/*     */     } 
/*     */     
/* 195 */     return true;
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
/*     */   @Nullable
/*     */   protected CacheControl lookupCacheControl(String urlPath) {
/* 210 */     CacheControl cacheControl = this.cacheControlMappings.get(urlPath);
/* 211 */     if (cacheControl != null) {
/* 212 */       return cacheControl;
/*     */     }
/*     */     
/* 215 */     for (String registeredPath : this.cacheControlMappings.keySet()) {
/* 216 */       if (this.pathMatcher.match(registeredPath, urlPath)) {
/* 217 */         return this.cacheControlMappings.get(registeredPath);
/*     */       }
/*     */     } 
/* 220 */     return null;
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
/*     */   @Nullable
/*     */   protected Integer lookupCacheSeconds(String urlPath) {
/* 235 */     Integer cacheSeconds = this.cacheMappings.get(urlPath);
/* 236 */     if (cacheSeconds != null) {
/* 237 */       return cacheSeconds;
/*     */     }
/*     */     
/* 240 */     for (String registeredPath : this.cacheMappings.keySet()) {
/* 241 */       if (this.pathMatcher.match(registeredPath, urlPath)) {
/* 242 */         return this.cacheMappings.get(registeredPath);
/*     */       }
/*     */     } 
/* 245 */     return null;
/*     */   }
/*     */   
/*     */   public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {}
/*     */   
/*     */   public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {}
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/WebContentInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */