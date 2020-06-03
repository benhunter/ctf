/*     */ package org.springframework.cache.concurrent;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.cache.Cache;
/*     */ import org.springframework.cache.CacheManager;
/*     */ import org.springframework.core.serializer.support.SerializationDelegate;
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
/*     */ public class ConcurrentMapCacheManager
/*     */   implements CacheManager, BeanClassLoaderAware
/*     */ {
/*  51 */   private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>(16);
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean dynamic = true;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean allowNullValues = true;
/*     */ 
/*     */   
/*     */   private boolean storeByValue = false;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private SerializationDelegate serialization;
/*     */ 
/*     */ 
/*     */   
/*     */   public ConcurrentMapCacheManager() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public ConcurrentMapCacheManager(String... cacheNames) {
/*  75 */     setCacheNames(Arrays.asList(cacheNames));
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
/*     */   public void setCacheNames(@Nullable Collection<String> cacheNames) {
/*  87 */     if (cacheNames != null) {
/*  88 */       for (String name : cacheNames) {
/*  89 */         this.cacheMap.put(name, createConcurrentMapCache(name));
/*     */       }
/*  91 */       this.dynamic = false;
/*     */     } else {
/*     */       
/*  94 */       this.dynamic = true;
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
/*     */   public void setAllowNullValues(boolean allowNullValues) {
/* 107 */     if (allowNullValues != this.allowNullValues) {
/* 108 */       this.allowNullValues = allowNullValues;
/*     */       
/* 110 */       recreateCaches();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAllowNullValues() {
/* 119 */     return this.allowNullValues;
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
/*     */   public void setStoreByValue(boolean storeByValue) {
/* 132 */     if (storeByValue != this.storeByValue) {
/* 133 */       this.storeByValue = storeByValue;
/*     */       
/* 135 */       recreateCaches();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStoreByValue() {
/* 146 */     return this.storeByValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/* 151 */     this.serialization = new SerializationDelegate(classLoader);
/*     */     
/* 153 */     if (isStoreByValue()) {
/* 154 */       recreateCaches();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<String> getCacheNames() {
/* 161 */     return Collections.unmodifiableSet(this.cacheMap.keySet());
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Cache getCache(String name) {
/* 167 */     Cache cache = this.cacheMap.get(name);
/* 168 */     if (cache == null && this.dynamic) {
/* 169 */       synchronized (this.cacheMap) {
/* 170 */         cache = this.cacheMap.get(name);
/* 171 */         if (cache == null) {
/* 172 */           cache = createConcurrentMapCache(name);
/* 173 */           this.cacheMap.put(name, cache);
/*     */         } 
/*     */       } 
/*     */     }
/* 177 */     return cache;
/*     */   }
/*     */   
/*     */   private void recreateCaches() {
/* 181 */     for (Map.Entry<String, Cache> entry : this.cacheMap.entrySet()) {
/* 182 */       entry.setValue(createConcurrentMapCache(entry.getKey()));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Cache createConcurrentMapCache(String name) {
/* 192 */     SerializationDelegate actualSerialization = isStoreByValue() ? this.serialization : null;
/* 193 */     return (Cache)new ConcurrentMapCache(name, new ConcurrentHashMap<>(256), 
/* 194 */         isAllowNullValues(), actualSerialization);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/concurrent/ConcurrentMapCacheManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */