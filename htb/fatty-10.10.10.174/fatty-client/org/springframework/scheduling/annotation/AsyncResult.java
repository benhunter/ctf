/*     */ package org.springframework.scheduling.annotation;
/*     */ 
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.concurrent.FailureCallback;
/*     */ import org.springframework.util.concurrent.ListenableFuture;
/*     */ import org.springframework.util.concurrent.ListenableFutureCallback;
/*     */ import org.springframework.util.concurrent.SuccessCallback;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AsyncResult<V>
/*     */   implements ListenableFuture<V>
/*     */ {
/*     */   @Nullable
/*     */   private final V value;
/*     */   @Nullable
/*     */   private final Throwable executionException;
/*     */   
/*     */   public AsyncResult(@Nullable V value) {
/*  63 */     this(value, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AsyncResult(@Nullable V value, @Nullable Throwable ex) {
/*  71 */     this.value = value;
/*  72 */     this.executionException = ex;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean cancel(boolean mayInterruptIfRunning) {
/*  78 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCancelled() {
/*  83 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDone() {
/*  88 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public V get() throws ExecutionException {
/*  94 */     if (this.executionException != null) {
/*  95 */       throw (this.executionException instanceof ExecutionException) ? (ExecutionException)this.executionException : new ExecutionException(this.executionException);
/*     */     }
/*     */ 
/*     */     
/*  99 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public V get(long timeout, TimeUnit unit) throws ExecutionException {
/* 105 */     return get();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addCallback(ListenableFutureCallback<? super V> callback) {
/* 110 */     addCallback((SuccessCallback<? super V>)callback, (FailureCallback)callback);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addCallback(SuccessCallback<? super V> successCallback, FailureCallback failureCallback) {
/*     */     try {
/* 116 */       if (this.executionException != null) {
/* 117 */         failureCallback.onFailure(exposedException(this.executionException));
/*     */       } else {
/*     */         
/* 120 */         successCallback.onSuccess(this.value);
/*     */       }
/*     */     
/* 123 */     } catch (Throwable throwable) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompletableFuture<V> completable() {
/* 130 */     if (this.executionException != null) {
/* 131 */       CompletableFuture<V> completable = new CompletableFuture<>();
/* 132 */       completable.completeExceptionally(exposedException(this.executionException));
/* 133 */       return completable;
/*     */     } 
/*     */     
/* 136 */     return CompletableFuture.completedFuture(this.value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <V> ListenableFuture<V> forValue(V value) {
/* 148 */     return new AsyncResult<>(value, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <V> ListenableFuture<V> forExecutionException(Throwable ex) {
/* 160 */     return new AsyncResult<>(null, ex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Throwable exposedException(Throwable original) {
/* 170 */     if (original instanceof ExecutionException) {
/* 171 */       Throwable cause = original.getCause();
/* 172 */       if (cause != null) {
/* 173 */         return cause;
/*     */       }
/*     */     } 
/* 176 */     return original;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/annotation/AsyncResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */