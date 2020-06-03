/*     */ package org.springframework.core.task.support;
/*     */ 
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.FutureTask;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import org.springframework.core.task.AsyncListenableTaskExecutor;
/*     */ import org.springframework.core.task.TaskDecorator;
/*     */ import org.springframework.core.task.TaskRejectedException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class TaskExecutorAdapter
/*     */   implements AsyncListenableTaskExecutor
/*     */ {
/*     */   private final Executor concurrentExecutor;
/*     */   @Nullable
/*     */   private TaskDecorator taskDecorator;
/*     */   
/*     */   public TaskExecutorAdapter(Executor concurrentExecutor) {
/*  60 */     Assert.notNull(concurrentExecutor, "Executor must not be null");
/*  61 */     this.concurrentExecutor = concurrentExecutor;
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
/*     */   public final void setTaskDecorator(TaskDecorator taskDecorator) {
/*  76 */     this.taskDecorator = taskDecorator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(Runnable task) {
/*     */     try {
/*  87 */       doExecute(this.concurrentExecutor, this.taskDecorator, task);
/*     */     }
/*  89 */     catch (RejectedExecutionException ex) {
/*  90 */       throw new TaskRejectedException("Executor [" + this.concurrentExecutor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(Runnable task, long startTimeout) {
/*  97 */     execute(task);
/*     */   }
/*     */ 
/*     */   
/*     */   public Future<?> submit(Runnable task) {
/*     */     try {
/* 103 */       if (this.taskDecorator == null && this.concurrentExecutor instanceof ExecutorService) {
/* 104 */         return ((ExecutorService)this.concurrentExecutor).submit(task);
/*     */       }
/*     */       
/* 107 */       FutureTask<Object> future = new FutureTask(task, null);
/* 108 */       doExecute(this.concurrentExecutor, this.taskDecorator, future);
/* 109 */       return future;
/*     */     
/*     */     }
/* 112 */     catch (RejectedExecutionException ex) {
/* 113 */       throw new TaskRejectedException("Executor [" + this.concurrentExecutor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> Future<T> submit(Callable<T> task) {
/*     */     try {
/* 121 */       if (this.taskDecorator == null && this.concurrentExecutor instanceof ExecutorService) {
/* 122 */         return ((ExecutorService)this.concurrentExecutor).submit(task);
/*     */       }
/*     */       
/* 125 */       FutureTask<T> future = new FutureTask<>(task);
/* 126 */       doExecute(this.concurrentExecutor, this.taskDecorator, future);
/* 127 */       return future;
/*     */     
/*     */     }
/* 130 */     catch (RejectedExecutionException ex) {
/* 131 */       throw new TaskRejectedException("Executor [" + this.concurrentExecutor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ListenableFuture<?> submitListenable(Runnable task) {
/*     */     try {
/* 139 */       ListenableFutureTask<Object> future = new ListenableFutureTask(task, null);
/* 140 */       doExecute(this.concurrentExecutor, this.taskDecorator, (Runnable)future);
/* 141 */       return (ListenableFuture<?>)future;
/*     */     }
/* 143 */     catch (RejectedExecutionException ex) {
/* 144 */       throw new TaskRejectedException("Executor [" + this.concurrentExecutor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
/*     */     try {
/* 152 */       ListenableFutureTask<T> future = new ListenableFutureTask(task);
/* 153 */       doExecute(this.concurrentExecutor, this.taskDecorator, (Runnable)future);
/* 154 */       return (ListenableFuture<T>)future;
/*     */     }
/* 156 */     catch (RejectedExecutionException ex) {
/* 157 */       throw new TaskRejectedException("Executor [" + this.concurrentExecutor + "] did not accept task: " + task, ex);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doExecute(Executor concurrentExecutor, @Nullable TaskDecorator taskDecorator, Runnable runnable) throws RejectedExecutionException {
/* 175 */     concurrentExecutor.execute((taskDecorator != null) ? taskDecorator.decorate(runnable) : runnable);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/task/support/TaskExecutorAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */