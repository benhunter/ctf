/*     */ package org.springframework.cache.support;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ public class CompositeCacheManager
/*     */   implements CacheManager, InitializingBean
/*     */ {
/*  56 */   private final List<CacheManager> cacheManagers = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean fallbackToNoOpCache = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompositeCacheManager() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompositeCacheManager(CacheManager... cacheManagers) {
/*  73 */     setCacheManagers(Arrays.asList(cacheManagers));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCacheManagers(Collection<CacheManager> cacheManagers) {
/*  81 */     this.cacheManagers.addAll(cacheManagers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFallbackToNoOpCache(boolean fallbackToNoOpCache) {
/*  90 */     this.fallbackToNoOpCache = fallbackToNoOpCache;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/*  95 */     if (this.fallbackToNoOpCache) {
/*  96 */       this.cacheManagers.add(new NoOpCacheManager());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Cache getCache(String name) {
/* 104 */     for (CacheManager cacheManager : this.cacheManagers) {
/* 105 */       Cache cache = cacheManager.getCache(name);
/* 106 */       if (cache != null) {
/* 107 */         return cache;
/*     */       }
/*     */     } 
/* 110 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<String> getCacheNames() {
/* 115 */     Set<String> names = new LinkedHashSet<>();
/* 116 */     for (CacheManager manager : this.cacheManagers) {
/* 117 */       names.addAll(manager.getCacheNames());
/*     */     }
/* 119 */     return Collections.unmodifiableSet(names);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/support/CompositeCacheManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */