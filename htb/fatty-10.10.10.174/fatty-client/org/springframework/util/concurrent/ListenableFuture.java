/*    */ package org.springframework.util.concurrent;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Future;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ListenableFuture<T>
/*    */   extends Future<T>
/*    */ {
/*    */   void addCallback(ListenableFutureCallback<? super T> paramListenableFutureCallback);
/*    */   
/*    */   void addCallback(SuccessCallback<? super T> paramSuccessCallback, FailureCallback paramFailureCallback);
/*    */   
/*    */   default CompletableFuture<T> completable() {
/* 57 */     CompletableFuture<T> completable = new DelegatingCompletableFuture<>(this);
/* 58 */     addCallback(completable::complete, completable::completeExceptionally);
/* 59 */     return completable;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/concurrent/ListenableFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */