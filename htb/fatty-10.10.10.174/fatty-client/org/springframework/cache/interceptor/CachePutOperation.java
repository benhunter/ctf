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
/*    */ public class CachePutOperation
/*    */   extends CacheOperation
/*    */ {
/*    */   @Nullable
/*    */   private final String unless;
/*    */   
/*    */   public CachePutOperation(Builder b) {
/* 40 */     super(b);
/* 41 */     this.unless = b.unless;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getUnless() {
/* 47 */     return this.unless;
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
/*    */     
/*    */     public void setUnless(String unless) {
/* 61 */       this.unless = unless;
/*    */     }
/*    */ 
/*    */     
/*    */     protected StringBuilder getOperationDescription() {
/* 66 */       StringBuilder sb = super.getOperationDescription();
/* 67 */       sb.append(" | unless='");
/* 68 */       sb.append(this.unless);
/* 69 */       sb.append("'");
/* 70 */       return sb;
/*    */     }
/*    */     
/*    */     public CachePutOperation build() {
/* 74 */       return new CachePutOperation(this);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/interceptor/CachePutOperation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */