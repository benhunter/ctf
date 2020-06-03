/*    */ package org.springframework.util.concurrent;
/*    */ 
/*    */ import java.time.Duration;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
/*    */ import reactor.core.publisher.Mono;
/*    */ import reactor.core.publisher.MonoProcessor;
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
/*    */ public class MonoToListenableFutureAdapter<T>
/*    */   implements ListenableFuture<T>
/*    */ {
/*    */   private final MonoProcessor<T> processor;
/* 40 */   private final ListenableFutureCallbackRegistry<T> registry = new ListenableFutureCallbackRegistry<>();
/*    */ 
/*    */   
/*    */   public MonoToListenableFutureAdapter(Mono<T> mono) {
/* 44 */     Assert.notNull(mono, "Mono must not be null");
/* 45 */     this
/*    */ 
/*    */       
/* 48 */       .processor = mono.doOnSuccess(this.registry::success).doOnError(this.registry::failure).toProcessor();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public T get() {
/* 55 */     return (T)this.processor.block();
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public T get(long timeout, TimeUnit unit) {
/* 61 */     Assert.notNull(unit, "TimeUnit must not be null");
/* 62 */     Duration duration = Duration.ofMillis(TimeUnit.MILLISECONDS.convert(timeout, unit));
/* 63 */     return (T)this.processor.block(duration);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean cancel(boolean mayInterruptIfRunning) {
/* 68 */     if (isCancelled()) {
/* 69 */       return false;
/*    */     }
/* 71 */     this.processor.cancel();
/*    */     
/* 73 */     return this.processor.isCancelled();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCancelled() {
/* 78 */     return this.processor.isCancelled();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDone() {
/* 83 */     return this.processor.isTerminated();
/*    */   }
/*    */ 
/*    */   
/*    */   public void addCallback(ListenableFutureCallback<? super T> callback) {
/* 88 */     this.registry.addCallback(callback);
/*    */   }
/*    */ 
/*    */   
/*    */   public void addCallback(SuccessCallback<? super T> success, FailureCallback failure) {
/* 93 */     this.registry.addSuccessCallback(success);
/* 94 */     this.registry.addFailureCallback(failure);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/concurrent/MonoToListenableFutureAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */