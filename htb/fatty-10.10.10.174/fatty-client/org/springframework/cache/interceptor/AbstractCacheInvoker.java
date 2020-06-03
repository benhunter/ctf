/*     */ package org.springframework.cache.interceptor;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import org.springframework.cache.Cache;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.function.SingletonSupplier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractCacheInvoker
/*     */ {
/*     */   protected SingletonSupplier<CacheErrorHandler> errorHandler;
/*     */   
/*     */   protected AbstractCacheInvoker() {
/*  38 */     this.errorHandler = SingletonSupplier.of(SimpleCacheErrorHandler::new);
/*     */   }
/*     */   
/*     */   protected AbstractCacheInvoker(CacheErrorHandler errorHandler) {
/*  42 */     this.errorHandler = SingletonSupplier.of(errorHandler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setErrorHandler(CacheErrorHandler errorHandler) {
/*  52 */     this.errorHandler = SingletonSupplier.of(errorHandler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CacheErrorHandler getErrorHandler() {
/*  59 */     return (CacheErrorHandler)this.errorHandler.obtain();
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
/*     */   @Nullable
/*     */   protected Cache.ValueWrapper doGet(Cache cache, Object key) {
/*     */     try {
/*  73 */       return cache.get(key);
/*     */     }
/*  75 */     catch (RuntimeException ex) {
/*  76 */       getErrorHandler().handleCacheGetError(ex, cache, key);
/*  77 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doPut(Cache cache, Object key, @Nullable Object result) {
/*     */     try {
/*  87 */       cache.put(key, result);
/*     */     }
/*  89 */     catch (RuntimeException ex) {
/*  90 */       getErrorHandler().handleCachePutError(ex, cache, key, result);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doEvict(Cache cache, Object key) {
/*     */     try {
/* 100 */       cache.evict(key);
/*     */     }
/* 102 */     catch (RuntimeException ex) {
/* 103 */       getErrorHandler().handleCacheEvictError(ex, cache, key);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doClear(Cache cache) {
/*     */     try {
/* 113 */       cache.clear();
/*     */     }
/* 115 */     catch (RuntimeException ex) {
/* 116 */       getErrorHandler().handleCacheClearError(ex, cache);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/interceptor/AbstractCacheInvoker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */