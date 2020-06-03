/*    */ package org.springframework.cache.interceptor;
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
/*    */ public class CacheEvictOperation
/*    */   extends CacheOperation
/*    */ {
/*    */   private final boolean cacheWide;
/*    */   private final boolean beforeInvocation;
/*    */   
/*    */   public CacheEvictOperation(Builder b) {
/* 38 */     super(b);
/* 39 */     this.cacheWide = b.cacheWide;
/* 40 */     this.beforeInvocation = b.beforeInvocation;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCacheWide() {
/* 45 */     return this.cacheWide;
/*    */   }
/*    */   
/*    */   public boolean isBeforeInvocation() {
/* 49 */     return this.beforeInvocation;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static class Builder
/*    */     extends CacheOperation.Builder
/*    */   {
/*    */     private boolean cacheWide = false;
/*    */ 
/*    */     
/*    */     private boolean beforeInvocation = false;
/*    */ 
/*    */     
/*    */     public void setCacheWide(boolean cacheWide) {
/* 64 */       this.cacheWide = cacheWide;
/*    */     }
/*    */     
/*    */     public void setBeforeInvocation(boolean beforeInvocation) {
/* 68 */       this.beforeInvocation = beforeInvocation;
/*    */     }
/*    */ 
/*    */     
/*    */     protected StringBuilder getOperationDescription() {
/* 73 */       StringBuilder sb = super.getOperationDescription();
/* 74 */       sb.append(",");
/* 75 */       sb.append(this.cacheWide);
/* 76 */       sb.append(",");
/* 77 */       sb.append(this.beforeInvocation);
/* 78 */       return sb;
/*    */     }
/*    */     
/*    */     public CacheEvictOperation build() {
/* 82 */       return new CacheEvictOperation(this);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/interceptor/CacheEvictOperation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */