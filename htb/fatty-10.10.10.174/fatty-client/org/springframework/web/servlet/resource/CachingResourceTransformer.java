/*    */ package org.springframework.web.servlet.resource;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.cache.Cache;
/*    */ import org.springframework.cache.CacheManager;
/*    */ import org.springframework.core.io.Resource;
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
/*    */ public class CachingResourceTransformer
/*    */   implements ResourceTransformer
/*    */ {
/* 41 */   private static final Log logger = LogFactory.getLog(CachingResourceTransformer.class);
/*    */   
/*    */   private final Cache cache;
/*    */ 
/*    */   
/*    */   public CachingResourceTransformer(Cache cache) {
/* 47 */     Assert.notNull(cache, "Cache is required");
/* 48 */     this.cache = cache;
/*    */   }
/*    */   
/*    */   public CachingResourceTransformer(CacheManager cacheManager, String cacheName) {
/* 52 */     Cache cache = cacheManager.getCache(cacheName);
/* 53 */     if (cache == null) {
/* 54 */       throw new IllegalArgumentException("Cache '" + cacheName + "' not found");
/*    */     }
/* 56 */     this.cache = cache;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Cache getCache() {
/* 64 */     return this.cache;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Resource transform(HttpServletRequest request, Resource resource, ResourceTransformerChain transformerChain) throws IOException {
/* 72 */     Resource transformed = (Resource)this.cache.get(resource, Resource.class);
/* 73 */     if (transformed != null) {
/* 74 */       logger.trace("Resource resolved from cache");
/* 75 */       return transformed;
/*    */     } 
/*    */     
/* 78 */     transformed = transformerChain.transform(request, resource);
/* 79 */     this.cache.put(resource, transformed);
/*    */     
/* 81 */     return transformed;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/resource/CachingResourceTransformer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */