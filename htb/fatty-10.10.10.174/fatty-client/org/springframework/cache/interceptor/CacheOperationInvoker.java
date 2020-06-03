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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface CacheOperationInvoker
/*    */ {
/*    */   Object invoke() throws ThrowableWrapper;
/*    */   
/*    */   public static class ThrowableWrapper
/*    */     extends RuntimeException
/*    */   {
/*    */     private final Throwable original;
/*    */     
/*    */     public ThrowableWrapper(Throwable original) {
/* 51 */       super(original.getMessage(), original);
/* 52 */       this.original = original;
/*    */     }
/*    */     
/*    */     public Throwable getOriginal() {
/* 56 */       return this.original;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/interceptor/CacheOperationInvoker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */