/*     */ package org.springframework.scheduling.concurrent;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.RejectedExecutionHandler;
/*     */ import java.util.concurrent.SynchronousQueue;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.springframework.core.task.AsyncListenableTaskExecutor;
/*     */ import org.springframework.core.task.TaskDecorator;
/*     */ import org.springframework.core.task.TaskRejectedException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.scheduling.SchedulingTaskExecutor;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
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
/*     */ public class ThreadPoolTaskExecutor
/*     */   extends ExecutorConfigurationSupport
/*     */   implements AsyncListenableTaskExecutor, SchedulingTaskExecutor
/*     */ {
/*  85 */   private final Object poolSizeMonitor = new Object();
/*     */   
/*  87 */   private int corePoolSize = 1;
/*     */   
/*  89 */   private int maxPoolSize = Integer.MAX_VALUE;
/*     */   
/*  91 */   private int keepAliveSeconds = 60;
/*     */   
/*  93 */   private int queueCapacity = Integer.MAX_VALUE;
/*     */ 
/*     */   
/*     */   private boolean allowCoreThreadTimeOut = false;
/*     */   
/*     */   @Nullable
/*     */   private TaskDecorator taskDecorator;
/*     */   
/*     */   @Nullable
/*     */   private ThreadPoolExecutor threadPoolExecutor;
/*     */   
/* 104 */   private final Map<Runnable, Object> decoratedTaskMap = (Map<Runnable, Object>)new ConcurrentReferenceHashMap(16, ConcurrentReferenceHashMap.ReferenceType.WEAK);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCorePoolSize(int corePoolSize) {
/* 114 */     synchronized (this.poolSizeMonitor) {
/* 115 */       this.corePoolSize = corePoolSize;
/* 116 */       if (this.threadPoolExecutor != null) {
/* 117 */         this.threadPoolExecutor.setCorePoolSize(corePoolSize);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCorePoolSize() {
/* 126 */     synchronized (this.poolSizeMonitor) {
/* 127 */       return this.corePoolSize;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxPoolSize(int maxPoolSize) {
/* 137 */     synchronized (this.poolSizeMonitor) {
/* 138 */       this.maxPoolSize = maxPoolSize;
/* 139 */       if (this.threadPoolExecutor != null) {
/* 140 */         this.threadPoolExecutor.setMaximumPoolSize(maxPoolSize);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxPoolSize() {
/* 149 */     synchronized (this.poolSizeMonitor) {
/* 150 */       return this.maxPoolSize;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeepAliveSeconds(int keepAliveSeconds) {
/* 160 */     synchronized (this.poolSizeMonitor) {
/* 161 */       this.keepAliveSeconds = keepAliveSeconds;
/* 162 */       if (this.threadPoolExecutor != null) {
/* 163 */         this.threadPoolExecutor.setKeepAliveTime(keepAliveSeconds, TimeUnit.SECONDS);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getKeepAliveSeconds() {
/* 172 */     synchronized (this.poolSizeMonitor) {
/* 173 */       return this.keepAliveSeconds;
/*     */     } 
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
/*     */   public void setQueueCapacity(int queueCapacity) {
/* 186 */     this.queueCapacity = queueCapacity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllowCoreThreadTimeOut(boolean allowCoreThreadTimeOut) {
/* 197 */     this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
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
/*     */   public void setTaskDecorator(TaskDecorator taskDecorator) {
/* 211 */     this.taskDecorator = taskDecorator;
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
/*     */   protected ExecutorService initializeExecutor(ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
/*     */     ThreadPoolExecutor executor;
/* 225 */     BlockingQueue<Runnable> queue = createQueue(this.queueCapacity);
/*     */ 
/*     */     
/* 228 */     if (this.taskDecorator != null) {
/* 229 */       executor = new ThreadPoolExecutor(this.corePoolSize, this.maxPoolSize, this.keepAliveSeconds, TimeUnit.SECONDS, queue, threadFactory, rejectedExecutionHandler)
/*     */         {
/*     */           
/*     */           public void execute(Runnable command)
/*     */           {
/* 234 */             Runnable decorated = ThreadPoolTaskExecutor.this.taskDecorator.decorate(command);
/* 235 */             if (decorated != command) {
/* 236 */               ThreadPoolTaskExecutor.this.decoratedTaskMap.put(decorated, command);
/*     */             }
/* 238 */             super.execute(decorated);
/*     */           }
/*     */         };
/*     */     } else {
/*     */       
/* 243 */       executor = new ThreadPoolExecutor(this.corePoolSize, this.maxPoolSize, this.keepAliveSeconds, TimeUnit.SECONDS, queue, threadFactory, rejectedExecutionHandler);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 249 */     if (this.allowCoreThreadTimeOut) {
/* 250 */       executor.allowCoreThreadTimeOut(true);
/*     */     }
/*     */     
/* 253 */     this.threadPoolExecutor = executor;
/* 254 */     return executor;
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
/*     */   protected BlockingQueue<Runnable> createQueue(int queueCapacity) {
/* 267 */     if (queueCapacity > 0) {
/* 268 */       return new LinkedBlockingQueue<>(queueCapacity);
/*     */     }
/*     */     
/* 271 */     return new SynchronousQueue<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThreadPoolExecutor getThreadPoolExecutor() throws IllegalStateException {
/* 281 */     Assert.state((this.threadPoolExecutor != null), "ThreadPoolTaskExecutor not initialized");
/* 282 */     return this.threadPoolExecutor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPoolSize() {
/* 290 */     if (this.threadPoolExecutor == null)
/*     */     {
/* 292 */       return this.corePoolSize;
/*     */     }
/* 294 */     return this.threadPoolExecutor.getPoolSize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getActiveCount() {
/* 302 */     if (this.threadPoolExecutor == null)
/*     */     {
/* 304 */       return 0;
/*     */     }
/* 306 */     return this.threadPoolExecutor.getActiveCount();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(Runnable task) {
/* 312 */     Executor executor = getThreadPoolExecutor();
/*     */     try {
/* 314 */       executor.execute(task);
/*     */     }
/* 316 */     catch (RejectedExecutionException ex) {
/* 317 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute(Runnable task, long startTimeout) {
/* 323 */     execute(task);
/*     */   }
/*     */ 
/*     */   
/*     */   public Future<?> submit(Runnable task) {
/* 328 */     ExecutorService executor = getThreadPoolExecutor();
/*     */     try {
/* 330 */       return executor.submit(task);
/*     */     }
/* 332 */     catch (RejectedExecutionException ex) {
/* 333 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Future<T> submit(Callable<T> task) {
/* 339 */     ExecutorService executor = getThreadPoolExecutor();
/*     */     try {
/* 341 */       return executor.submit(task);
/*     */     }
/* 343 */     catch (RejectedExecutionException ex) {
/* 344 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ListenableFuture<?> submitListenable(Runnable task) {
/* 350 */     ExecutorService executor = getThreadPoolExecutor();
/*     */     try {
/* 352 */       ListenableFutureTask<Object> future = new ListenableFutureTask(task, null);
/* 353 */       executor.execute((Runnable)future);
/* 354 */       return (ListenableFuture<?>)future;
/*     */     }
/* 356 */     catch (RejectedExecutionException ex) {
/* 357 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
/* 363 */     ExecutorService executor = getThreadPoolExecutor();
/*     */     try {
/* 365 */       ListenableFutureTask<T> future = new ListenableFutureTask(task);
/* 366 */       executor.execute((Runnable)future);
/* 367 */       return (ListenableFuture<T>)future;
/*     */     }
/* 369 */     catch (RejectedExecutionException ex) {
/* 370 */       throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void cancelRemainingTask(Runnable task) {
/* 376 */     super.cancelRemainingTask(task);
/*     */     
/* 378 */     Object original = this.decoratedTaskMap.get(task);
/* 379 */     if (original instanceof Future)
/* 380 */       ((Future)original).cancel(true); 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/concurrent/ThreadPoolTaskExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */