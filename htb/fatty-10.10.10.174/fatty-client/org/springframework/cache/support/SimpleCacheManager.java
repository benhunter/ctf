/*    */ package org.springframework.cache.support;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import org.springframework.cache.Cache;
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
/*    */ public class SimpleCacheManager
/*    */   extends AbstractCacheManager
/*    */ {
/* 33 */   private Collection<? extends Cache> caches = Collections.emptySet();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCaches(Collection<? extends Cache> caches) {
/* 40 */     this.caches = caches;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Collection<? extends Cache> loadCaches() {
/* 45 */     return this.caches;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/support/SimpleCacheManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */