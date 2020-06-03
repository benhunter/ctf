/*    */ package org.springframework.cache.support;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.LinkedHashSet;
/*    */ import java.util.Set;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.ConcurrentMap;
/*    */ import org.springframework.cache.Cache;
/*    */ import org.springframework.cache.CacheManager;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ public class NoOpCacheManager
/*    */   implements CacheManager
/*    */ {
/* 44 */   private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<>(16);
/*    */   
/* 46 */   private final Set<String> cacheNames = new LinkedHashSet<>(16);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Cache getCache(String name) {
/* 56 */     Cache cache = this.caches.get(name);
/* 57 */     if (cache == null) {
/* 58 */       this.caches.computeIfAbsent(name, key -> new NoOpCache(name));
/* 59 */       synchronized (this.cacheNames) {
/* 60 */         this.cacheNames.add(name);
/*    */       } 
/*    */     } 
/*    */     
/* 64 */     return this.caches.get(name);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Collection<String> getCacheNames() {
/* 72 */     synchronized (this.cacheNames) {
/* 73 */       return Collections.unmodifiableSet(this.cacheNames);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/support/NoOpCacheManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */