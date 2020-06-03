/*    */ package org.springframework.cache.interceptor;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import org.springframework.beans.factory.InitializingBean;
/*    */ import org.springframework.cache.Cache;
/*    */ import org.springframework.cache.CacheManager;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractCacheResolver
/*    */   implements CacheResolver, InitializingBean
/*    */ {
/*    */   @Nullable
/*    */   private CacheManager cacheManager;
/*    */   
/*    */   protected AbstractCacheResolver() {}
/*    */   
/*    */   protected AbstractCacheResolver(CacheManager cacheManager) {
/* 56 */     this.cacheManager = cacheManager;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCacheManager(CacheManager cacheManager) {
/* 64 */     this.cacheManager = cacheManager;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CacheManager getCacheManager() {
/* 71 */     Assert.state((this.cacheManager != null), "No CacheManager set");
/* 72 */     return this.cacheManager;
/*    */   }
/*    */ 
/*    */   
/*    */   public void afterPropertiesSet() {
/* 77 */     Assert.notNull(this.cacheManager, "CacheManager is required");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
/* 83 */     Collection<String> cacheNames = getCacheNames(context);
/* 84 */     if (cacheNames == null) {
/* 85 */       return Collections.emptyList();
/*    */     }
/* 87 */     Collection<Cache> result = new ArrayList<>(cacheNames.size());
/* 88 */     for (String cacheName : cacheNames) {
/* 89 */       Cache cache = getCacheManager().getCache(cacheName);
/* 90 */       if (cache == null) {
/* 91 */         throw new IllegalArgumentException("Cannot find cache named '" + cacheName + "' for " + context
/* 92 */             .getOperation());
/*    */       }
/* 94 */       result.add(cache);
/*    */     } 
/* 96 */     return result;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   protected abstract Collection<String> getCacheNames(CacheOperationInvocationContext<?> paramCacheOperationInvocationContext);
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/interceptor/AbstractCacheResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */