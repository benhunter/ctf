/*     */ package org.springframework.scheduling.concurrent;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import javax.enterprise.concurrent.ManagedExecutors;
/*     */ import org.springframework.core.task.AsyncListenableTaskExecutor;
/*     */ import org.springframework.core.task.TaskDecorator;
/*     */ import org.springframework.core.task.support.TaskExecutorAdapter;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.scheduling.SchedulingAwareRunnable;
/*     */ import org.springframework.scheduling.SchedulingTaskExecutor;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.concurrent.ListenableFuture;
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
/*     */ public class ConcurrentTaskExecutor
/*     */   implements AsyncListenableTaskExecutor, SchedulingTaskExecutor
/*     */ {
/*     */   @Nullable
/*     */   private static Class<?> managedExecutorServiceClass;
/*     */   private Executor concurrentExecutor;
/*     */   private TaskExecutorAdapter adaptedExecutor;
/*     */   
/*     */   static {
/*     */     try {
/*  71 */       managedExecutorServiceClass = ClassUtils.forName("javax.enterprise.concurrent.ManagedExecutorService", ConcurrentTaskScheduler.class
/*     */           
/*  73 */           .getClassLoader());
/*     */     }
/*  75 */     catch (ClassNotFoundException ex) {
/*     */       
/*  77 */       managedExecutorServiceClass = null;
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
/*     */   public ConcurrentTaskExecutor() {
/*  91 */     this.concurrentExecutor = Executors.newSingleThreadExecutor();
/*  92 */     this.adaptedExecutor = new TaskExecutorAdapter(this.concurrentExecutor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConcurrentTaskExecutor(@Nullable Executor executor) {
/* 102 */     this.concurrentExecutor = (executor != null) ? executor : Executors.newSingleThreadExecutor();
/* 103 */     this.adaptedExecutor = getAdaptedExecutor(this.concurrentExecutor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setConcurrentExecutor(@Nullable Executor executor) {
/* 113 */     this.concurrentExecutor = (executor != null) ? executor : Executors.newSingleThreadExecutor();
/* 114 */     this.adaptedExecutor = getAdaptedExecutor(this.concurrentExecutor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Executor getConcurrentExecutor() {
/* 121 */     return this.concurrentExecutor;
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
/*     */   public final void setTaskDecorator(TaskDecorator taskDecorator) {
/* 135 */     this.adaptedExecutor.setTaskDecorator(taskDecorator);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(Runnable task) {
/* 141 */     this.adaptedExecutor.execute(task);
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute(Runnable task, long startTimeout) {
/* 146 */     this.adaptedExecutor.execute(task, startTimeout);
/*     */   }
/*     */ 
/*     */   
/*     */   public Future<?> submit(Runnable task) {
/* 151 */     return this.adaptedExecutor.submit(task);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Future<T> submit(Callable<T> task) {
/* 156 */     return this.adaptedExecutor.submit(task);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListenableFuture<?> submitListenable(Runnable task) {
/* 161 */     return this.adaptedExecutor.submitListenable(task);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
/* 166 */     return this.adaptedExecutor.submitListenable(task);
/*     */   }
/*     */ 
/*     */   
/*     */   private static TaskExecutorAdapter getAdaptedExecutor(Executor concurrentExecutor) {
/* 171 */     if (managedExecutorServiceClass != null && managedExecutorServiceClass.isInstance(concurrentExecutor)) {
/* 172 */       return new ManagedTaskExecutorAdapter(concurrentExecutor);
/*     */     }
/* 174 */     return new TaskExecutorAdapter(concurrentExecutor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ManagedTaskExecutorAdapter
/*     */     extends TaskExecutorAdapter
/*     */   {
/*     */     public ManagedTaskExecutorAdapter(Executor concurrentExecutor) {
/* 187 */       super(concurrentExecutor);
/*     */     }
/*     */ 
/*     */     
/*     */     public void execute(Runnable task) {
/* 192 */       super.execute(ConcurrentTaskExecutor.ManagedTaskBuilder.buildManagedTask(task, task.toString()));
/*     */     }
/*     */ 
/*     */     
/*     */     public Future<?> submit(Runnable task) {
/* 197 */       return super.submit(ConcurrentTaskExecutor.ManagedTaskBuilder.buildManagedTask(task, task.toString()));
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> Future<T> submit(Callable<T> task) {
/* 202 */       return super.submit(ConcurrentTaskExecutor.ManagedTaskBuilder.buildManagedTask(task, task.toString()));
/*     */     }
/*     */ 
/*     */     
/*     */     public ListenableFuture<?> submitListenable(Runnable task) {
/* 207 */       return super.submitListenable(ConcurrentTaskExecutor.ManagedTaskBuilder.buildManagedTask(task, task.toString()));
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
/* 212 */       return super.submitListenable(ConcurrentTaskExecutor.ManagedTaskBuilder.buildManagedTask(task, task.toString()));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class ManagedTaskBuilder
/*     */   {
/*     */     public static Runnable buildManagedTask(Runnable task, String identityName) {
/*     */       Map<String, String> properties;
/* 226 */       if (task instanceof SchedulingAwareRunnable) {
/* 227 */         properties = new HashMap<>(4);
/* 228 */         properties.put("javax.enterprise.concurrent.LONGRUNNING_HINT", 
/* 229 */             Boolean.toString(((SchedulingAwareRunnable)task).isLongLived()));
/*     */       } else {
/*     */         
/* 232 */         properties = new HashMap<>(2);
/*     */       } 
/* 234 */       properties.put("javax.enterprise.concurrent.IDENTITY_NAME", identityName);
/* 235 */       return ManagedExecutors.managedTask(task, properties, null);
/*     */     }
/*     */     
/*     */     public static <T> Callable<T> buildManagedTask(Callable<T> task, String identityName) {
/* 239 */       Map<String, String> properties = new HashMap<>(2);
/* 240 */       properties.put("javax.enterprise.concurrent.IDENTITY_NAME", identityName);
/* 241 */       return ManagedExecutors.managedTask(task, properties, null);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/concurrent/ConcurrentTaskExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */