/*     */ package org.springframework.cache.annotation;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.springframework.cache.interceptor.CacheEvictOperation;
/*     */ import org.springframework.cache.interceptor.CacheOperation;
/*     */ import org.springframework.cache.interceptor.CachePutOperation;
/*     */ import org.springframework.cache.interceptor.CacheableOperation;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class SpringCacheAnnotationParser
/*     */   implements CacheAnnotationParser, Serializable
/*     */ {
/*  51 */   private static final Set<Class<? extends Annotation>> CACHE_OPERATION_ANNOTATIONS = new LinkedHashSet<>(8);
/*     */   
/*     */   static {
/*  54 */     CACHE_OPERATION_ANNOTATIONS.add(Cacheable.class);
/*  55 */     CACHE_OPERATION_ANNOTATIONS.add(CacheEvict.class);
/*  56 */     CACHE_OPERATION_ANNOTATIONS.add(CachePut.class);
/*  57 */     CACHE_OPERATION_ANNOTATIONS.add(Caching.class);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Collection<CacheOperation> parseCacheAnnotations(Class<?> type) {
/*  64 */     DefaultCacheConfig defaultConfig = new DefaultCacheConfig(type);
/*  65 */     return parseCacheAnnotations(defaultConfig, type);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Collection<CacheOperation> parseCacheAnnotations(Method method) {
/*  71 */     DefaultCacheConfig defaultConfig = new DefaultCacheConfig(method.getDeclaringClass());
/*  72 */     return parseCacheAnnotations(defaultConfig, method);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Collection<CacheOperation> parseCacheAnnotations(DefaultCacheConfig cachingConfig, AnnotatedElement ae) {
/*  77 */     Collection<CacheOperation> ops = parseCacheAnnotations(cachingConfig, ae, false);
/*  78 */     if (ops != null && ops.size() > 1) {
/*     */       
/*  80 */       Collection<CacheOperation> localOps = parseCacheAnnotations(cachingConfig, ae, true);
/*  81 */       if (localOps != null) {
/*  82 */         return localOps;
/*     */       }
/*     */     } 
/*  85 */     return ops;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Collection<CacheOperation> parseCacheAnnotations(DefaultCacheConfig cachingConfig, AnnotatedElement ae, boolean localOnly) {
/*  94 */     Collection<? extends Annotation> anns = localOnly ? AnnotatedElementUtils.getAllMergedAnnotations(ae, CACHE_OPERATION_ANNOTATIONS) : AnnotatedElementUtils.findAllMergedAnnotations(ae, CACHE_OPERATION_ANNOTATIONS);
/*  95 */     if (anns.isEmpty()) {
/*  96 */       return null;
/*     */     }
/*     */     
/*  99 */     Collection<CacheOperation> ops = new ArrayList<>(1);
/* 100 */     anns.stream().filter(ann -> ann instanceof Cacheable).forEach(ann -> ops.add(parseCacheableAnnotation(ae, cachingConfig, (Cacheable)ann)));
/*     */     
/* 102 */     anns.stream().filter(ann -> ann instanceof CacheEvict).forEach(ann -> ops.add(parseEvictAnnotation(ae, cachingConfig, (CacheEvict)ann)));
/*     */     
/* 104 */     anns.stream().filter(ann -> ann instanceof CachePut).forEach(ann -> ops.add(parsePutAnnotation(ae, cachingConfig, (CachePut)ann)));
/*     */     
/* 106 */     anns.stream().filter(ann -> ann instanceof Caching).forEach(ann -> parseCachingAnnotation(ae, cachingConfig, (Caching)ann, ops));
/*     */     
/* 108 */     return ops;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private CacheableOperation parseCacheableAnnotation(AnnotatedElement ae, DefaultCacheConfig defaultConfig, Cacheable cacheable) {
/* 114 */     CacheableOperation.Builder builder = new CacheableOperation.Builder();
/*     */     
/* 116 */     builder.setName(ae.toString());
/* 117 */     builder.setCacheNames(cacheable.cacheNames());
/* 118 */     builder.setCondition(cacheable.condition());
/* 119 */     builder.setUnless(cacheable.unless());
/* 120 */     builder.setKey(cacheable.key());
/* 121 */     builder.setKeyGenerator(cacheable.keyGenerator());
/* 122 */     builder.setCacheManager(cacheable.cacheManager());
/* 123 */     builder.setCacheResolver(cacheable.cacheResolver());
/* 124 */     builder.setSync(cacheable.sync());
/*     */     
/* 126 */     defaultConfig.applyDefault((CacheOperation.Builder)builder);
/* 127 */     CacheableOperation op = builder.build();
/* 128 */     validateCacheOperation(ae, (CacheOperation)op);
/*     */     
/* 130 */     return op;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private CacheEvictOperation parseEvictAnnotation(AnnotatedElement ae, DefaultCacheConfig defaultConfig, CacheEvict cacheEvict) {
/* 136 */     CacheEvictOperation.Builder builder = new CacheEvictOperation.Builder();
/*     */     
/* 138 */     builder.setName(ae.toString());
/* 139 */     builder.setCacheNames(cacheEvict.cacheNames());
/* 140 */     builder.setCondition(cacheEvict.condition());
/* 141 */     builder.setKey(cacheEvict.key());
/* 142 */     builder.setKeyGenerator(cacheEvict.keyGenerator());
/* 143 */     builder.setCacheManager(cacheEvict.cacheManager());
/* 144 */     builder.setCacheResolver(cacheEvict.cacheResolver());
/* 145 */     builder.setCacheWide(cacheEvict.allEntries());
/* 146 */     builder.setBeforeInvocation(cacheEvict.beforeInvocation());
/*     */     
/* 148 */     defaultConfig.applyDefault((CacheOperation.Builder)builder);
/* 149 */     CacheEvictOperation op = builder.build();
/* 150 */     validateCacheOperation(ae, (CacheOperation)op);
/*     */     
/* 152 */     return op;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private CacheOperation parsePutAnnotation(AnnotatedElement ae, DefaultCacheConfig defaultConfig, CachePut cachePut) {
/* 158 */     CachePutOperation.Builder builder = new CachePutOperation.Builder();
/*     */     
/* 160 */     builder.setName(ae.toString());
/* 161 */     builder.setCacheNames(cachePut.cacheNames());
/* 162 */     builder.setCondition(cachePut.condition());
/* 163 */     builder.setUnless(cachePut.unless());
/* 164 */     builder.setKey(cachePut.key());
/* 165 */     builder.setKeyGenerator(cachePut.keyGenerator());
/* 166 */     builder.setCacheManager(cachePut.cacheManager());
/* 167 */     builder.setCacheResolver(cachePut.cacheResolver());
/*     */     
/* 169 */     defaultConfig.applyDefault((CacheOperation.Builder)builder);
/* 170 */     CachePutOperation op = builder.build();
/* 171 */     validateCacheOperation(ae, (CacheOperation)op);
/*     */     
/* 173 */     return (CacheOperation)op;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void parseCachingAnnotation(AnnotatedElement ae, DefaultCacheConfig defaultConfig, Caching caching, Collection<CacheOperation> ops) {
/* 179 */     Cacheable[] cacheables = caching.cacheable();
/* 180 */     for (Cacheable cacheable : cacheables) {
/* 181 */       ops.add(parseCacheableAnnotation(ae, defaultConfig, cacheable));
/*     */     }
/* 183 */     CacheEvict[] cacheEvicts = caching.evict();
/* 184 */     for (CacheEvict cacheEvict : cacheEvicts) {
/* 185 */       ops.add(parseEvictAnnotation(ae, defaultConfig, cacheEvict));
/*     */     }
/* 187 */     CachePut[] cachePuts = caching.put();
/* 188 */     for (CachePut cachePut : cachePuts) {
/* 189 */       ops.add(parsePutAnnotation(ae, defaultConfig, cachePut));
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
/*     */   private void validateCacheOperation(AnnotatedElement ae, CacheOperation operation) {
/* 203 */     if (StringUtils.hasText(operation.getKey()) && StringUtils.hasText(operation.getKeyGenerator())) {
/* 204 */       throw new IllegalStateException("Invalid cache annotation configuration on '" + ae
/* 205 */           .toString() + "'. Both 'key' and 'keyGenerator' attributes have been set. These attributes are mutually exclusive: either set the SpEL expression used tocompute the key at runtime or set the name of the KeyGenerator bean to use.");
/*     */     }
/*     */ 
/*     */     
/* 209 */     if (StringUtils.hasText(operation.getCacheManager()) && StringUtils.hasText(operation.getCacheResolver())) {
/* 210 */       throw new IllegalStateException("Invalid cache annotation configuration on '" + ae
/* 211 */           .toString() + "'. Both 'cacheManager' and 'cacheResolver' attributes have been set. These attributes are mutually exclusive: the cache manager is used to configure adefault cache resolver if none is set. If a cache resolver is set, the cache managerwon't be used.");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 220 */     return (this == other || other instanceof SpringCacheAnnotationParser);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 225 */     return SpringCacheAnnotationParser.class.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class DefaultCacheConfig
/*     */   {
/*     */     private final Class<?> target;
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private String[] cacheNames;
/*     */     
/*     */     @Nullable
/*     */     private String keyGenerator;
/*     */     
/*     */     @Nullable
/*     */     private String cacheManager;
/*     */     
/*     */     @Nullable
/*     */     private String cacheResolver;
/*     */     
/*     */     private boolean initialized = false;
/*     */ 
/*     */     
/*     */     public DefaultCacheConfig(Class<?> target) {
/* 251 */       this.target = target;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void applyDefault(CacheOperation.Builder builder) {
/* 259 */       if (!this.initialized) {
/* 260 */         CacheConfig annotation = (CacheConfig)AnnotatedElementUtils.findMergedAnnotation(this.target, CacheConfig.class);
/* 261 */         if (annotation != null) {
/* 262 */           this.cacheNames = annotation.cacheNames();
/* 263 */           this.keyGenerator = annotation.keyGenerator();
/* 264 */           this.cacheManager = annotation.cacheManager();
/* 265 */           this.cacheResolver = annotation.cacheResolver();
/*     */         } 
/* 267 */         this.initialized = true;
/*     */       } 
/*     */       
/* 270 */       if (builder.getCacheNames().isEmpty() && this.cacheNames != null) {
/* 271 */         builder.setCacheNames(this.cacheNames);
/*     */       }
/* 273 */       if (!StringUtils.hasText(builder.getKey()) && !StringUtils.hasText(builder.getKeyGenerator()) && 
/* 274 */         StringUtils.hasText(this.keyGenerator)) {
/* 275 */         builder.setKeyGenerator(this.keyGenerator);
/*     */       }
/*     */       
/* 278 */       if (!StringUtils.hasText(builder.getCacheManager()) && !StringUtils.hasText(builder.getCacheResolver()))
/*     */       {
/*     */         
/* 281 */         if (StringUtils.hasText(this.cacheResolver)) {
/* 282 */           builder.setCacheResolver(this.cacheResolver);
/*     */         }
/* 284 */         else if (StringUtils.hasText(this.cacheManager)) {
/* 285 */           builder.setCacheManager(this.cacheManager);
/*     */         } 
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/annotation/SpringCacheAnnotationParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */