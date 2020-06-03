/*     */ package org.springframework.web.servlet.config.annotation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.springframework.cache.Cache;
/*     */ import org.springframework.http.CacheControl;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourceHandlerRegistration
/*     */ {
/*     */   private final String[] pathPatterns;
/*  42 */   private final List<String> locationValues = new ArrayList<>();
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Integer cachePeriod;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private CacheControl cacheControl;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ResourceChainRegistration resourceChainRegistration;
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceHandlerRegistration(String... pathPatterns) {
/*  59 */     Assert.notEmpty((Object[])pathPatterns, "At least one path pattern is required for resource handling.");
/*  60 */     this.pathPatterns = pathPatterns;
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
/*     */   public ResourceHandlerRegistration addResourceLocations(String... resourceLocations) {
/*  83 */     this.locationValues.addAll(Arrays.asList(resourceLocations));
/*  84 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceHandlerRegistration setCachePeriod(Integer cachePeriod) {
/*  95 */     this.cachePeriod = cachePeriod;
/*  96 */     return this;
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
/*     */   public ResourceHandlerRegistration setCacheControl(CacheControl cacheControl) {
/* 108 */     this.cacheControl = cacheControl;
/* 109 */     return this;
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
/*     */   public ResourceChainRegistration resourceChain(boolean cacheResources) {
/* 125 */     this.resourceChainRegistration = new ResourceChainRegistration(cacheResources);
/* 126 */     return this.resourceChainRegistration;
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
/*     */   public ResourceChainRegistration resourceChain(boolean cacheResources, Cache cache) {
/* 147 */     this.resourceChainRegistration = new ResourceChainRegistration(cacheResources, cache);
/* 148 */     return this.resourceChainRegistration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String[] getPathPatterns() {
/* 156 */     return this.pathPatterns;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ResourceHttpRequestHandler getRequestHandler() {
/* 163 */     ResourceHttpRequestHandler handler = new ResourceHttpRequestHandler();
/* 164 */     if (this.resourceChainRegistration != null) {
/* 165 */       handler.setResourceResolvers(this.resourceChainRegistration.getResourceResolvers());
/* 166 */       handler.setResourceTransformers(this.resourceChainRegistration.getResourceTransformers());
/*     */     } 
/* 168 */     handler.setLocationValues(this.locationValues);
/* 169 */     if (this.cacheControl != null) {
/* 170 */       handler.setCacheControl(this.cacheControl);
/*     */     }
/* 172 */     else if (this.cachePeriod != null) {
/* 173 */       handler.setCacheSeconds(this.cachePeriod.intValue());
/*     */     } 
/* 175 */     return handler;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/config/annotation/ResourceHandlerRegistration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */