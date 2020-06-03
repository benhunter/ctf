/*    */ package org.springframework.cache.interceptor;
/*    */ 
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
/*    */ public class CacheableOperation
/*    */   extends CacheOperation
/*    */ {
/*    */   @Nullable
/*    */   private final String unless;
/*    */   private final boolean sync;
/*    */   
/*    */   public CacheableOperation(Builder b) {
/* 42 */     super(b);
/* 43 */     this.unless = b.unless;
/* 44 */     this.sync = b.sync;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getUnless() {
/* 50 */     return this.unless;
/*    */   }
/*    */   
/*    */   public boolean isSync() {
/* 54 */     return this.sync;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static class Builder
/*    */     extends CacheOperation.Builder
/*    */   {
/*    */     @Nullable
/*    */     private String unless;
/*    */ 
/*    */     
/*    */     private boolean sync;
/*    */ 
/*    */     
/*    */     public void setUnless(String unless) {
/* 70 */       this.unless = unless;
/*    */     }
/*    */     
/*    */     public void setSync(boolean sync) {
/* 74 */       this.sync = sync;
/*    */     }
/*    */ 
/*    */     
/*    */     protected StringBuilder getOperationDescription() {
/* 79 */       StringBuilder sb = super.getOperationDescription();
/* 80 */       sb.append(" | unless='");
/* 81 */       sb.append(this.unless);
/* 82 */       sb.append("'");
/* 83 */       sb.append(" | sync='");
/* 84 */       sb.append(this.sync);
/* 85 */       sb.append("'");
/* 86 */       return sb;
/*    */     }
/*    */ 
/*    */     
/*    */     public CacheableOperation build() {
/* 91 */       return new CacheableOperation(this);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/interceptor/CacheableOperation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */