/*     */ package org.springframework.web.servlet.resource;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.cache.Cache;
/*     */ import org.springframework.cache.CacheManager;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class CachingResourceResolver
/*     */   extends AbstractResourceResolver
/*     */ {
/*     */   public static final String RESOLVED_RESOURCE_CACHE_KEY_PREFIX = "resolvedResource:";
/*     */   public static final String RESOLVED_URL_PATH_CACHE_KEY_PREFIX = "resolvedUrlPath:";
/*     */   private final Cache cache;
/*  58 */   private final List<String> contentCodings = new ArrayList<>(EncodedResourceResolver.DEFAULT_CODINGS);
/*     */ 
/*     */   
/*     */   public CachingResourceResolver(Cache cache) {
/*  62 */     Assert.notNull(cache, "Cache is required");
/*  63 */     this.cache = cache;
/*     */   }
/*     */   
/*     */   public CachingResourceResolver(CacheManager cacheManager, String cacheName) {
/*  67 */     Cache cache = cacheManager.getCache(cacheName);
/*  68 */     if (cache == null) {
/*  69 */       throw new IllegalArgumentException("Cache '" + cacheName + "' not found");
/*     */     }
/*  71 */     this.cache = cache;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Cache getCache() {
/*  79 */     return this.cache;
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
/*     */   public void setContentCodings(List<String> codings) {
/*  93 */     Assert.notEmpty(codings, "At least one content coding expected");
/*  94 */     this.contentCodings.clear();
/*  95 */     this.contentCodings.addAll(codings);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getContentCodings() {
/* 103 */     return Collections.unmodifiableList(this.contentCodings);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Resource resolveResourceInternal(@Nullable HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
/* 111 */     String key = computeKey(request, requestPath);
/* 112 */     Resource resource = (Resource)this.cache.get(key, Resource.class);
/*     */     
/* 114 */     if (resource != null) {
/* 115 */       if (this.logger.isTraceEnabled()) {
/* 116 */         this.logger.trace("Resource resolved from cache");
/*     */       }
/* 118 */       return resource;
/*     */     } 
/*     */     
/* 121 */     resource = chain.resolveResource(request, requestPath, locations);
/* 122 */     if (resource != null) {
/* 123 */       this.cache.put(key, resource);
/*     */     }
/*     */     
/* 126 */     return resource;
/*     */   }
/*     */   
/*     */   protected String computeKey(@Nullable HttpServletRequest request, String requestPath) {
/* 130 */     StringBuilder key = new StringBuilder("resolvedResource:");
/* 131 */     key.append(requestPath);
/* 132 */     if (request != null) {
/* 133 */       String codingKey = getContentCodingKey(request);
/* 134 */       if (StringUtils.hasText(codingKey)) {
/* 135 */         key.append("+encoding=").append(codingKey);
/*     */       }
/*     */     } 
/* 138 */     return key.toString();
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private String getContentCodingKey(HttpServletRequest request) {
/* 143 */     String header = request.getHeader("Accept-Encoding");
/* 144 */     if (!StringUtils.hasText(header)) {
/* 145 */       return null;
/*     */     }
/* 147 */     return Arrays.<String>stream(StringUtils.tokenizeToStringArray(header, ","))
/* 148 */       .map(token -> {
/*     */           int index = token.indexOf(';');
/*     */           
/*     */           return ((index >= 0) ? token.substring(0, index) : token).trim().toLowerCase();
/* 152 */         }).filter(this.contentCodings::contains)
/* 153 */       .sorted()
/* 154 */       .collect(Collectors.joining(","));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String resolveUrlPathInternal(String resourceUrlPath, List<? extends Resource> locations, ResourceResolverChain chain) {
/* 161 */     String key = "resolvedUrlPath:" + resourceUrlPath;
/* 162 */     String resolvedUrlPath = (String)this.cache.get(key, String.class);
/*     */     
/* 164 */     if (resolvedUrlPath != null) {
/* 165 */       if (this.logger.isTraceEnabled()) {
/* 166 */         this.logger.trace("Path resolved from cache");
/*     */       }
/* 168 */       return resolvedUrlPath;
/*     */     } 
/*     */     
/* 171 */     resolvedUrlPath = chain.resolveUrlPath(resourceUrlPath, locations);
/* 172 */     if (resolvedUrlPath != null) {
/* 173 */       this.cache.put(key, resolvedUrlPath);
/*     */     }
/*     */     
/* 176 */     return resolvedUrlPath;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/resource/CachingResourceResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */