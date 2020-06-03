/*     */ package org.springframework.cache.interceptor;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.core.MethodClassKey;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractFallbackCacheOperationSource
/*     */   implements CacheOperationSource
/*     */ {
/*  59 */   private static final Collection<CacheOperation> NULL_CACHING_ATTRIBUTE = Collections.emptyList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  74 */   private final Map<Object, Collection<CacheOperation>> attributeCache = new ConcurrentHashMap<>(1024);
/*     */ 
/*     */ 
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
/*     */   public Collection<CacheOperation> getCacheOperations(Method method, @Nullable Class<?> targetClass) {
/*  88 */     if (method.getDeclaringClass() == Object.class) {
/*  89 */       return null;
/*     */     }
/*     */     
/*  92 */     Object cacheKey = getCacheKey(method, targetClass);
/*  93 */     Collection<CacheOperation> cached = this.attributeCache.get(cacheKey);
/*     */     
/*  95 */     if (cached != null) {
/*  96 */       return (cached != NULL_CACHING_ATTRIBUTE) ? cached : null;
/*     */     }
/*     */     
/*  99 */     Collection<CacheOperation> cacheOps = computeCacheOperations(method, targetClass);
/* 100 */     if (cacheOps != null) {
/* 101 */       if (this.logger.isTraceEnabled()) {
/* 102 */         this.logger.trace("Adding cacheable method '" + method.getName() + "' with attribute: " + cacheOps);
/*     */       }
/* 104 */       this.attributeCache.put(cacheKey, cacheOps);
/*     */     } else {
/*     */       
/* 107 */       this.attributeCache.put(cacheKey, NULL_CACHING_ATTRIBUTE);
/*     */     } 
/* 109 */     return cacheOps;
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
/*     */   protected Object getCacheKey(Method method, @Nullable Class<?> targetClass) {
/* 122 */     return new MethodClassKey(method, targetClass);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Collection<CacheOperation> computeCacheOperations(Method method, @Nullable Class<?> targetClass) {
/* 128 */     if (allowPublicMethodsOnly() && !Modifier.isPublic(method.getModifiers())) {
/* 129 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 134 */     Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
/*     */ 
/*     */     
/* 137 */     Collection<CacheOperation> opDef = findCacheOperations(specificMethod);
/* 138 */     if (opDef != null) {
/* 139 */       return opDef;
/*     */     }
/*     */ 
/*     */     
/* 143 */     opDef = findCacheOperations(specificMethod.getDeclaringClass());
/* 144 */     if (opDef != null && ClassUtils.isUserLevelMethod(method)) {
/* 145 */       return opDef;
/*     */     }
/*     */     
/* 148 */     if (specificMethod != method) {
/*     */       
/* 150 */       opDef = findCacheOperations(method);
/* 151 */       if (opDef != null) {
/* 152 */         return opDef;
/*     */       }
/*     */       
/* 155 */       opDef = findCacheOperations(method.getDeclaringClass());
/* 156 */       if (opDef != null && ClassUtils.isUserLevelMethod(method)) {
/* 157 */         return opDef;
/*     */       }
/*     */     } 
/*     */     
/* 161 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected abstract Collection<CacheOperation> findCacheOperations(Class<?> paramClass);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected abstract Collection<CacheOperation> findCacheOperations(Method paramMethod);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean allowPublicMethodsOnly() {
/* 188 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/interceptor/AbstractFallbackCacheOperationSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */