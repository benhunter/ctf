/*    */ package org.springframework.cache.support;
/*    */ 
/*    */ import java.util.concurrent.Callable;
/*    */ import org.springframework.cache.Cache;
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
/*    */ public class NoOpCache
/*    */   implements Cache
/*    */ {
/*    */   private final String name;
/*    */   
/*    */   public NoOpCache(String name) {
/* 44 */     Assert.notNull(name, "Cache name must not be null");
/* 45 */     this.name = name;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 51 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getNativeCache() {
/* 56 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Cache.ValueWrapper get(Object key) {
/* 62 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public <T> T get(Object key, @Nullable Class<T> type) {
/* 68 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public <T> T get(Object key, Callable<T> valueLoader) {
/*    */     try {
/* 75 */       return valueLoader.call();
/*    */     }
/* 77 */     catch (Exception ex) {
/* 78 */       throw new Cache.ValueRetrievalException(key, valueLoader, ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void put(Object key, @Nullable Object value) {}
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Cache.ValueWrapper putIfAbsent(Object key, @Nullable Object value) {
/* 89 */     return null;
/*    */   }
/*    */   
/*    */   public void evict(Object key) {}
/*    */   
/*    */   public void clear() {}
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/support/NoOpCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */