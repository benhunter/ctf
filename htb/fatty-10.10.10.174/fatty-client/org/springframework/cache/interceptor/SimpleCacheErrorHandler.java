/*    */ package org.springframework.cache.interceptor;
/*    */ 
/*    */ import org.springframework.cache.Cache;
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
/*    */ public class SimpleCacheErrorHandler
/*    */   implements CacheErrorHandler
/*    */ {
/*    */   public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
/* 33 */     throw exception;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handleCachePutError(RuntimeException exception, Cache cache, Object key, @Nullable Object value) {
/* 38 */     throw exception;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
/* 43 */     throw exception;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handleCacheClearError(RuntimeException exception, Cache cache) {
/* 48 */     throw exception;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/interceptor/SimpleCacheErrorHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */