/*    */ package org.springframework.cache.interceptor;
/*    */ 
/*    */ import java.util.Collection;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SimpleCacheResolver
/*    */   extends AbstractCacheResolver
/*    */ {
/*    */   public SimpleCacheResolver() {}
/*    */   
/*    */   public SimpleCacheResolver(CacheManager cacheManager) {
/* 49 */     super(cacheManager);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
/* 55 */     return context.getOperation().getCacheNames();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   static SimpleCacheResolver of(@Nullable CacheManager cacheManager) {
/* 67 */     return (cacheManager != null) ? new SimpleCacheResolver(cacheManager) : null;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/interceptor/SimpleCacheResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */