/*    */ package org.springframework.cache.interceptor;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
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
/*    */ public class CompositeCacheOperationSource
/*    */   implements CacheOperationSource, Serializable
/*    */ {
/*    */   private final CacheOperationSource[] cacheOperationSources;
/*    */   
/*    */   public CompositeCacheOperationSource(CacheOperationSource... cacheOperationSources) {
/* 46 */     Assert.notEmpty((Object[])cacheOperationSources, "CacheOperationSource array must not be empty");
/* 47 */     this.cacheOperationSources = cacheOperationSources;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final CacheOperationSource[] getCacheOperationSources() {
/* 55 */     return this.cacheOperationSources;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Collection<CacheOperation> getCacheOperations(Method method, @Nullable Class<?> targetClass) {
/* 62 */     Collection<CacheOperation> ops = null;
/* 63 */     for (CacheOperationSource source : this.cacheOperationSources) {
/* 64 */       Collection<CacheOperation> cacheOperations = source.getCacheOperations(method, targetClass);
/* 65 */       if (cacheOperations != null) {
/* 66 */         if (ops == null) {
/* 67 */           ops = new ArrayList<>();
/*    */         }
/* 69 */         ops.addAll(cacheOperations);
/*    */       } 
/*    */     } 
/* 72 */     return ops;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/interceptor/CompositeCacheOperationSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */