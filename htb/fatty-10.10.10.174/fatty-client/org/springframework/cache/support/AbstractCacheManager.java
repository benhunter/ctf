/*     */ package org.springframework.cache.support;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.cache.Cache;
/*     */ import org.springframework.cache.CacheManager;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractCacheManager
/*     */   implements CacheManager, InitializingBean
/*     */ {
/*  42 */   private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>(16);
/*     */   
/*  44 */   private volatile Set<String> cacheNames = Collections.emptySet();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/*  51 */     initializeCaches();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initializeCaches() {
/*  62 */     Collection<? extends Cache> caches = loadCaches();
/*     */     
/*  64 */     synchronized (this.cacheMap) {
/*  65 */       this.cacheNames = Collections.emptySet();
/*  66 */       this.cacheMap.clear();
/*  67 */       Set<String> cacheNames = new LinkedHashSet<>(caches.size());
/*  68 */       for (Cache cache : caches) {
/*  69 */         String name = cache.getName();
/*  70 */         this.cacheMap.put(name, decorateCache(cache));
/*  71 */         cacheNames.add(name);
/*     */       } 
/*  73 */       this.cacheNames = Collections.unmodifiableSet(cacheNames);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Collection<? extends Cache> loadCaches();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Cache getCache(String name) {
/*  90 */     Cache cache = this.cacheMap.get(name);
/*  91 */     if (cache != null) {
/*  92 */       return cache;
/*     */     }
/*     */ 
/*     */     
/*  96 */     synchronized (this.cacheMap) {
/*  97 */       cache = this.cacheMap.get(name);
/*  98 */       if (cache == null) {
/*  99 */         cache = getMissingCache(name);
/* 100 */         if (cache != null) {
/* 101 */           cache = decorateCache(cache);
/* 102 */           this.cacheMap.put(name, cache);
/* 103 */           updateCacheNames(name);
/*     */         } 
/*     */       } 
/* 106 */       return cache;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<String> getCacheNames() {
/* 113 */     return this.cacheNames;
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
/*     */   @Nullable
/*     */   protected final Cache lookupCache(String name) {
/* 131 */     return this.cacheMap.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected final void addCache(Cache cache) {
/* 141 */     String name = cache.getName();
/* 142 */     synchronized (this.cacheMap) {
/* 143 */       if (this.cacheMap.put(name, decorateCache(cache)) == null) {
/* 144 */         updateCacheNames(name);
/*     */       }
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
/*     */   private void updateCacheNames(String name) {
/* 157 */     Set<String> cacheNames = new LinkedHashSet<>(this.cacheNames.size() + 1);
/* 158 */     cacheNames.addAll(this.cacheNames);
/* 159 */     cacheNames.add(name);
/* 160 */     this.cacheNames = Collections.unmodifiableSet(cacheNames);
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
/*     */   protected Cache decorateCache(Cache cache) {
/* 173 */     return cache;
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
/*     */   @Nullable
/*     */   protected Cache getMissingCache(String name) {
/* 191 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/support/AbstractCacheManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */