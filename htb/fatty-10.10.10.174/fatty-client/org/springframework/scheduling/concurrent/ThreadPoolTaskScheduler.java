/*     */ package org.springframework.scheduling.concurrent;
/*     */ 
/*     */ import java.util.Date;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.RejectedExecutionHandler;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.ScheduledThreadPoolExecutor;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.springframework.core.task.AsyncListenableTaskExecutor;
/*     */ import org.springframework.core.task.TaskRejectedException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.scheduling.SchedulingTaskExecutor;
/*     */ import org.springframework.scheduling.TaskScheduler;
/*     */ import org.springframework.scheduling.Trigger;
/*     */ import org.springframework.scheduling.support.TaskUtils;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
/*     */ import org.springframework.util.ErrorHandler;
/*     */ import org.springframework.util.concurrent.ListenableFuture;
/*     */ import org.springframework.util.concurrent.ListenableFutureTask;
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
/*     */ public class ThreadPoolTaskScheduler
/*     */   extends ExecutorConfigurationSupport
/*     */   implements AsyncListenableTaskExecutor, SchedulingTaskExecutor, TaskScheduler
/*     */ {
/*  62 */   private volatile int poolSize = 1;
/*     */ 
/*     */   
/*     */   private volatile boolean removeOnCancelPolicy = false;
/*     */   
/*     */   @Nullable
/*     */   private volatile ErrorHandler errorHandler;
/*     */   
/*     */   @Nullable
/*     */   private ScheduledExecutorService scheduledExecutor;
/*     */   
/*  73 */   private final Map<Object, ListenableFuture<?>> listenableFutureMap = (Map<Object, ListenableFuture<?>>)new ConcurrentReferenceHashMap(16, ConcurrentReferenceHashMap.ReferenceType.WEAK);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPoolSize(int poolSize) {
/*  83 */     Assert.isTrue((poolSize > 0), "'poolSize' must be 1 or higher");
/*  84 */     this.poolSize = poolSize;
/*  85 */     if (this.scheduledExecutor instanceof ScheduledThreadPoolExecutor) {
/*  86 */       ((ScheduledThreadPoolExecutor)this.scheduledExecutor).setCorePoolSize(poolSize);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemoveOnCancelPolicy(boolean removeOnCancelPolicy) {
/*  97 */     this.removeOnCancelPolicy = removeOnCancelPolicy;
/*  98 */     if (this.scheduledExecutor instanceof ScheduledThreadPoolExecutor) {
/*  99 */       ((ScheduledThreadPoolExecutor)this.scheduledExecutor).setRemoveOnCancelPolicy(removeOnCancelPolicy);
/*     */     }
/* 101 */     else if (removeOnCancelPolicy && this.scheduledExecutor != null) {
/* 102 */       this.logger.debug("Could not apply remove-on-cancel policy - not a ScheduledThreadPoolExecutor");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setErrorHandler(ErrorHandler errorHandler) {
/* 110 */     this.errorHandler = errorHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ExecutorService initializeExecutor(ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
/* 118 */     this.scheduledExecutor = createExecutor(this.poolSize, threadFactory, rejectedExecutionHandler);
/*     */     
/* 120 */     if (this.removeOnCancelPolicy) {
/* 121 */       if (this.scheduledExecutor instanceof ScheduledThreadPoolExecutor) {
/* 122 */         ((ScheduledThreadPoolExecutor)this.scheduledExecutor).setRemoveOnCancelPolicy(true);
/*     */       } else {
/*     */         
/* 125 */         this.logger.debug("Could not apply remove-on-cancel policy - not a ScheduledThreadPoolExecutor");
/*     */       } 
/*     */     }
/*     */     
/* 129 */     return this.scheduledExecutor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ScheduledExecutorService createExecutor(int poolSize, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
/* 146 */     return new ScheduledThreadPoolExecutor(poolSize, threadFactory, rejectedExecutionHandler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ScheduledExecutorService getScheduledExecutor() throws IllegalStateException {
/* 155 */     Assert.state((this.scheduledExecutor != null), "ThreadPoolTaskScheduler not initialized");
/* 156 */     return this.scheduledExecutor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ScheduledThreadPoolExecutor getScheduledThreadPoolExecutor() throws IllegalStateException {
/* 167 */     Assert.state(this.scheduledExecutor instanceof ScheduledThreadPoolExecutor, "No ScheduledThreadPoolExecutor available");
/*     */     
/* 169 */     return (ScheduledThreadPoolExecutor)this.scheduledExecutor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPoolSize() {
/* 179 */     if (this.scheduledExecutor == null)
/*     */     {
/* 181 */       return this.poolSize;
/*     */     }
/* 183 */     return getScheduledThreadPoolExecutor().getPoolSize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRemoveOnCancelPolicy() {
/* 191 */     if (this.scheduledExecutor == null)
/*     */     {
/* 193 */       return this.removeOnCancelPolicy;
/*     */     }
/* 195 */     return getScheduledThreadPoolExecutor().getRemoveOnCancelPolicy();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getActiveCount() {
/* 205 */     if (this.scheduledExecutor == null)
/*     */     {
/* 207 */       return 0;
/*     */     }
/* 209 */     return getScheduledThreadPoolExecutor().getActiveCount();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(Runnable task) {
/* 217 */     Executor executor = getScheduledExecutor();
/*     */     try {
/* 219 */       executor.execute(errorHandlingTask(task, false));
/*     */     }
/* 221 */     catch (RejectedExecutionException ex) {
/* 222 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute(Runnable task, long startTimeout) {
/* 228 */     execute(task);
/*     */   }
/*     */ 
/*     */   
/*     */   public Future<?> submit(Runnable task) {
/* 233 */     ExecutorService executor = getScheduledExecutor();
/*     */     try {
/* 235 */       return executor.submit(errorHandlingTask(task, false));
/*     */     }
/* 237 */     catch (RejectedExecutionException ex) {
/* 238 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Future<T> submit(Callable<T> task) {
/* 244 */     ExecutorService executor = getScheduledExecutor();
/*     */     try {
/* 246 */       Callable<T> taskToUse = task;
/* 247 */       ErrorHandler errorHandler = this.errorHandler;
/* 248 */       if (errorHandler != null) {
/* 249 */         taskToUse = new DelegatingErrorHandlingCallable<>(task, errorHandler);
/*     */       }
/* 251 */       return executor.submit(taskToUse);
/*     */     }
/* 253 */     catch (RejectedExecutionException ex) {
/* 254 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ListenableFuture<?> submitListenable(Runnable task) {
/* 260 */     ExecutorService executor = getScheduledExecutor();
/*     */     try {
/* 262 */       ListenableFutureTask<Object> listenableFuture = new ListenableFutureTask(task, null);
/* 263 */       executeAndTrack(executor, listenableFuture);
/* 264 */       return (ListenableFuture<?>)listenableFuture;
/*     */     }
/* 266 */     catch (RejectedExecutionException ex) {
/* 267 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
/* 273 */     ExecutorService executor = getScheduledExecutor();
/*     */     try {
/* 275 */       ListenableFutureTask<T> listenableFuture = new ListenableFutureTask(task);
/* 276 */       executeAndTrack(executor, listenableFuture);
/* 277 */       return (ListenableFuture<T>)listenableFuture;
/*     */     }
/* 279 */     catch (RejectedExecutionException ex) {
/* 280 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void executeAndTrack(ExecutorService executor, ListenableFutureTask<?> listenableFuture) {
/* 285 */     Future<?> scheduledFuture = executor.submit(errorHandlingTask((Runnable)listenableFuture, false));
/* 286 */     this.listenableFutureMap.put(scheduledFuture, listenableFuture);
/* 287 */     listenableFuture.addCallback(result -> (ListenableFuture)this.listenableFutureMap.remove(scheduledFuture), ex -> (ListenableFuture)this.listenableFutureMap.remove(scheduledFuture));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void cancelRemainingTask(Runnable task) {
/* 293 */     super.cancelRemainingTask(task);
/*     */     
/* 295 */     ListenableFuture<?> listenableFuture = this.listenableFutureMap.get(task);
/* 296 */     if (listenableFuture != null) {
/* 297 */       listenableFuture.cancel(true);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ScheduledFuture<?> schedule(Runnable task, Trigger trigger) {
/* 307 */     ScheduledExecutorService executor = getScheduledExecutor();
/*     */     try {
/* 309 */       ErrorHandler errorHandler = this.errorHandler;
/* 310 */       if (errorHandler == null) {
/* 311 */         errorHandler = TaskUtils.getDefaultErrorHandler(true);
/*     */       }
/* 313 */       return (new ReschedulingRunnable(task, trigger, executor, errorHandler)).schedule();
/*     */     }
/* 315 */     catch (RejectedExecutionException ex) {
/* 316 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ScheduledFuture<?> schedule(Runnable task, Date startTime) {
/* 322 */     ScheduledExecutorService executor = getScheduledExecutor();
/* 323 */     long initialDelay = startTime.getTime() - System.currentTimeMillis();
/*     */     try {
/* 325 */       return executor.schedule(errorHandlingTask(task, false), initialDelay, TimeUnit.MILLISECONDS);
/*     */     }
/* 327 */     catch (RejectedExecutionException ex) {
/* 328 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Date startTime, long period) {
/* 334 */     ScheduledExecutorService executor = getScheduledExecutor();
/* 335 */     long initialDelay = startTime.getTime() - System.currentTimeMillis();
/*     */     try {
/* 337 */       return executor.scheduleAtFixedRate(errorHandlingTask(task, true), initialDelay, period, TimeUnit.MILLISECONDS);
/*     */     }
/* 339 */     catch (RejectedExecutionException ex) {
/* 340 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long period) {
/* 346 */     ScheduledExecutorService executor = getScheduledExecutor();
/*     */     try {
/* 348 */       return executor.scheduleAtFixedRate(errorHandlingTask(task, true), 0L, period, TimeUnit.MILLISECONDS);
/*     */     }
/* 350 */     catch (RejectedExecutionException ex) {
/* 351 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, Date startTime, long delay) {
/* 357 */     ScheduledExecutorService executor = getScheduledExecutor();
/* 358 */     long initialDelay = startTime.getTime() - System.currentTimeMillis();
/*     */     try {
/* 360 */       return executor.scheduleWithFixedDelay(errorHandlingTask(task, true), initialDelay, delay, TimeUnit.MILLISECONDS);
/*     */     }
/* 362 */     catch (RejectedExecutionException ex) {
/* 363 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, long delay) {
/* 369 */     ScheduledExecutorService executor = getScheduledExecutor();
/*     */     try {
/* 371 */       return executor.scheduleWithFixedDelay(errorHandlingTask(task, true), 0L, delay, TimeUnit.MILLISECONDS);
/*     */     }
/* 373 */     catch (RejectedExecutionException ex) {
/* 374 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private Runnable errorHandlingTask(Runnable task, boolean isRepeatingTask) {
/* 380 */     return (Runnable)TaskUtils.decorateTaskWithErrorHandler(task, this.errorHandler, isRepeatingTask);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class DelegatingErrorHandlingCallable<V>
/*     */     implements Callable<V>
/*     */   {
/*     */     private final Callable<V> delegate;
/*     */     private final ErrorHandler errorHandler;
/*     */     
/*     */     public DelegatingErrorHandlingCallable(Callable<V> delegate, ErrorHandler errorHandler) {
/* 391 */       this.delegate = delegate;
/* 392 */       this.errorHandler = errorHandler;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public V call() throws Exception {
/*     */       try {
/* 399 */         return this.delegate.call();
/*     */       }
/* 401 */       catch (Throwable ex) {
/* 402 */         this.errorHandler.handleError(ex);
/* 403 */         return null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/concurrent/ThreadPoolTaskScheduler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */