/*     */ package org.springframework.web.servlet.config.annotation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.cache.Cache;
/*     */ import org.springframework.cache.concurrent.ConcurrentMapCache;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.web.servlet.resource.CachingResourceResolver;
/*     */ import org.springframework.web.servlet.resource.CachingResourceTransformer;
/*     */ import org.springframework.web.servlet.resource.CssLinkResourceTransformer;
/*     */ import org.springframework.web.servlet.resource.PathResourceResolver;
/*     */ import org.springframework.web.servlet.resource.ResourceResolver;
/*     */ import org.springframework.web.servlet.resource.ResourceTransformer;
/*     */ import org.springframework.web.servlet.resource.WebJarsResourceResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourceChainRegistration
/*     */ {
/*     */   private static final String DEFAULT_CACHE_NAME = "spring-resource-chain-cache";
/*  46 */   private static final boolean isWebJarsAssetLocatorPresent = ClassUtils.isPresent("org.webjars.WebJarAssetLocator", ResourceChainRegistration.class
/*  47 */       .getClassLoader());
/*     */ 
/*     */   
/*  50 */   private final List<ResourceResolver> resolvers = new ArrayList<>(4);
/*     */   
/*  52 */   private final List<ResourceTransformer> transformers = new ArrayList<>(4);
/*     */   
/*     */   private boolean hasVersionResolver;
/*     */   
/*     */   private boolean hasPathResolver;
/*     */   
/*     */   private boolean hasCssLinkTransformer;
/*     */   
/*     */   private boolean hasWebjarsResolver;
/*     */ 
/*     */   
/*     */   public ResourceChainRegistration(boolean cacheResources) {
/*  64 */     this(cacheResources, cacheResources ? (Cache)new ConcurrentMapCache("spring-resource-chain-cache") : null);
/*     */   }
/*     */   
/*     */   public ResourceChainRegistration(boolean cacheResources, @Nullable Cache cache) {
/*  68 */     Assert.isTrue((!cacheResources || cache != null), "'cache' is required when cacheResources=true");
/*  69 */     if (cacheResources) {
/*  70 */       this.resolvers.add(new CachingResourceResolver(cache));
/*  71 */       this.transformers.add(new CachingResourceTransformer(cache));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceChainRegistration addResolver(ResourceResolver resolver) {
/*  82 */     Assert.notNull(resolver, "The provided ResourceResolver should not be null");
/*  83 */     this.resolvers.add(resolver);
/*  84 */     if (resolver instanceof org.springframework.web.servlet.resource.VersionResourceResolver) {
/*  85 */       this.hasVersionResolver = true;
/*     */     }
/*  87 */     else if (resolver instanceof PathResourceResolver) {
/*  88 */       this.hasPathResolver = true;
/*     */     }
/*  90 */     else if (resolver instanceof WebJarsResourceResolver) {
/*  91 */       this.hasWebjarsResolver = true;
/*     */     } 
/*  93 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceChainRegistration addTransformer(ResourceTransformer transformer) {
/* 102 */     Assert.notNull(transformer, "The provided ResourceTransformer should not be null");
/* 103 */     this.transformers.add(transformer);
/* 104 */     if (transformer instanceof CssLinkResourceTransformer) {
/* 105 */       this.hasCssLinkTransformer = true;
/*     */     }
/* 107 */     return this;
/*     */   }
/*     */   
/*     */   protected List<ResourceResolver> getResourceResolvers() {
/* 111 */     if (!this.hasPathResolver) {
/* 112 */       List<ResourceResolver> result = new ArrayList<>(this.resolvers);
/* 113 */       if (isWebJarsAssetLocatorPresent && !this.hasWebjarsResolver) {
/* 114 */         result.add(new WebJarsResourceResolver());
/*     */       }
/* 116 */       result.add(new PathResourceResolver());
/* 117 */       return result;
/*     */     } 
/* 119 */     return this.resolvers;
/*     */   }
/*     */   
/*     */   protected List<ResourceTransformer> getResourceTransformers() {
/* 123 */     if (this.hasVersionResolver && !this.hasCssLinkTransformer) {
/* 124 */       List<ResourceTransformer> result = new ArrayList<>(this.transformers);
/* 125 */       boolean hasTransformers = !this.transformers.isEmpty();
/* 126 */       boolean hasCaching = (hasTransformers && this.transformers.get(0) instanceof CachingResourceTransformer);
/* 127 */       result.add(hasCaching ? 1 : 0, new CssLinkResourceTransformer());
/* 128 */       return result;
/*     */     } 
/* 130 */     return this.transformers;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/config/annotation/ResourceChainRegistration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */