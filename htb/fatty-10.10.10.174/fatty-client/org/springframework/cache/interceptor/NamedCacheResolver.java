/*    */ package org.springframework.cache.interceptor;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
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
/*    */ public class NamedCacheResolver
/*    */   extends AbstractCacheResolver
/*    */ {
/*    */   @Nullable
/*    */   private Collection<String> cacheNames;
/*    */   
/*    */   public NamedCacheResolver() {}
/*    */   
/*    */   public NamedCacheResolver(CacheManager cacheManager, String... cacheNames) {
/* 43 */     super(cacheManager);
/* 44 */     this.cacheNames = new ArrayList<>(Arrays.asList(cacheNames));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCacheNames(Collection<String> cacheNames) {
/* 52 */     this.cacheNames = cacheNames;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
/* 57 */     return this.cacheNames;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/interceptor/NamedCacheResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */