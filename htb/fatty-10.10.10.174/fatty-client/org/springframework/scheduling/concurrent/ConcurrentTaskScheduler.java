/*     */ package org.springframework.scheduling.concurrent;
/*     */ 
/*     */ import java.util.Date;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.enterprise.concurrent.LastExecution;
/*     */ import javax.enterprise.concurrent.ManagedScheduledExecutorService;
/*     */ import javax.enterprise.concurrent.Trigger;
/*     */ import org.springframework.core.task.TaskRejectedException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.scheduling.TaskScheduler;
/*     */ import org.springframework.scheduling.Trigger;
/*     */ import org.springframework.scheduling.TriggerContext;
/*     */ import org.springframework.scheduling.support.DelegatingErrorHandlingRunnable;
/*     */ import org.springframework.scheduling.support.SimpleTriggerContext;
/*     */ import org.springframework.scheduling.support.TaskUtils;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ErrorHandler;
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
/*     */ public class ConcurrentTaskScheduler
/*     */   extends ConcurrentTaskExecutor
/*     */   implements TaskScheduler
/*     */ {
/*     */   @Nullable
/*     */   private static Class<?> managedScheduledExecutorServiceClass;
/*     */   private ScheduledExecutorService scheduledExecutor;
/*     */   
/*     */   static {
/*     */     try {
/*  73 */       managedScheduledExecutorServiceClass = ClassUtils.forName("javax.enterprise.concurrent.ManagedScheduledExecutorService", ConcurrentTaskScheduler.class
/*     */           
/*  75 */           .getClassLoader());
/*     */     }
/*  77 */     catch (ClassNotFoundException ex) {
/*     */       
/*  79 */       managedScheduledExecutorServiceClass = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean enterpriseConcurrentScheduler = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ErrorHandler errorHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConcurrentTaskScheduler() {
/*  99 */     this.scheduledExecutor = initScheduledExecutor(null);
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
/*     */   public ConcurrentTaskScheduler(ScheduledExecutorService scheduledExecutor) {
/* 113 */     super(scheduledExecutor);
/* 114 */     this.scheduledExecutor = initScheduledExecutor(scheduledExecutor);
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
/*     */   public ConcurrentTaskScheduler(Executor concurrentExecutor, ScheduledExecutorService scheduledExecutor) {
/* 129 */     super(concurrentExecutor);
/* 130 */     this.scheduledExecutor = initScheduledExecutor(scheduledExecutor);
/*     */   }
/*     */ 
/*     */   
/*     */   private ScheduledExecutorService initScheduledExecutor(@Nullable ScheduledExecutorService scheduledExecutor) {
/* 135 */     if (scheduledExecutor != null) {
/* 136 */       this.scheduledExecutor = scheduledExecutor;
/* 137 */       this
/* 138 */         .enterpriseConcurrentScheduler = (managedScheduledExecutorServiceClass != null && managedScheduledExecutorServiceClass.isInstance(scheduledExecutor));
/*     */     } else {
/*     */       
/* 141 */       this.scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
/* 142 */       this.enterpriseConcurrentScheduler = false;
/*     */     } 
/* 144 */     return this.scheduledExecutor;
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
/*     */   public void setScheduledExecutor(@Nullable ScheduledExecutorService scheduledExecutor) {
/* 159 */     initScheduledExecutor(scheduledExecutor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setErrorHandler(ErrorHandler errorHandler) {
/* 166 */     Assert.notNull(errorHandler, "ErrorHandler must not be null");
/* 167 */     this.errorHandler = errorHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ScheduledFuture<?> schedule(Runnable task, Trigger trigger) {
/*     */     try {
/* 175 */       if (this.enterpriseConcurrentScheduler) {
/* 176 */         return (new EnterpriseConcurrentTriggerScheduler()).schedule(decorateTask(task, true), trigger);
/*     */       }
/*     */ 
/*     */       
/* 180 */       ErrorHandler errorHandler = (this.errorHandler != null) ? this.errorHandler : TaskUtils.getDefaultErrorHandler(true);
/* 181 */       return (new ReschedulingRunnable(task, trigger, this.scheduledExecutor, errorHandler)).schedule();
/*     */     
/*     */     }
/* 184 */     catch (RejectedExecutionException ex) {
/* 185 */       throw new TaskRejectedException("Executor [" + this.scheduledExecutor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ScheduledFuture<?> schedule(Runnable task, Date startTime) {
/* 191 */     long initialDelay = startTime.getTime() - System.currentTimeMillis();
/*     */     try {
/* 193 */       return this.scheduledExecutor.schedule(decorateTask(task, false), initialDelay, TimeUnit.MILLISECONDS);
/*     */     }
/* 195 */     catch (RejectedExecutionException ex) {
/* 196 */       throw new TaskRejectedException("Executor [" + this.scheduledExecutor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Date startTime, long period) {
/* 202 */     long initialDelay = startTime.getTime() - System.currentTimeMillis();
/*     */     try {
/* 204 */       return this.scheduledExecutor.scheduleAtFixedRate(decorateTask(task, true), initialDelay, period, TimeUnit.MILLISECONDS);
/*     */     }
/* 206 */     catch (RejectedExecutionException ex) {
/* 207 */       throw new TaskRejectedException("Executor [" + this.scheduledExecutor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long period) {
/*     */     try {
/* 214 */       return this.scheduledExecutor.scheduleAtFixedRate(decorateTask(task, true), 0L, period, TimeUnit.MILLISECONDS);
/*     */     }
/* 216 */     catch (RejectedExecutionException ex) {
/* 217 */       throw new TaskRejectedException("Executor [" + this.scheduledExecutor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, Date startTime, long delay) {
/* 223 */     long initialDelay = startTime.getTime() - System.currentTimeMillis();
/*     */     try {
/* 225 */       return this.scheduledExecutor.scheduleWithFixedDelay(decorateTask(task, true), initialDelay, delay, TimeUnit.MILLISECONDS);
/*     */     }
/* 227 */     catch (RejectedExecutionException ex) {
/* 228 */       throw new TaskRejectedException("Executor [" + this.scheduledExecutor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, long delay) {
/*     */     try {
/* 235 */       return this.scheduledExecutor.scheduleWithFixedDelay(decorateTask(task, true), 0L, delay, TimeUnit.MILLISECONDS);
/*     */     }
/* 237 */     catch (RejectedExecutionException ex) {
/* 238 */       throw new TaskRejectedException("Executor [" + this.scheduledExecutor + "] did not accept task: " + task, ex);
/*     */     } 
/*     */   }
/*     */   private Runnable decorateTask(Runnable task, boolean isRepeatingTask) {
/*     */     Runnable runnable;
/* 243 */     DelegatingErrorHandlingRunnable delegatingErrorHandlingRunnable = TaskUtils.decorateTaskWithErrorHandler(task, this.errorHandler, isRepeatingTask);
/* 244 */     if (this.enterpriseConcurrentScheduler) {
/* 245 */       runnable = ConcurrentTaskExecutor.ManagedTaskBuilder.buildManagedTask((Runnable)delegatingErrorHandlingRunnable, task.toString());
/*     */     }
/* 247 */     return runnable;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class EnterpriseConcurrentTriggerScheduler
/*     */   {
/*     */     private EnterpriseConcurrentTriggerScheduler() {}
/*     */ 
/*     */     
/*     */     public ScheduledFuture<?> schedule(Runnable task, final Trigger trigger) {
/* 258 */       ManagedScheduledExecutorService executor = (ManagedScheduledExecutorService)ConcurrentTaskScheduler.this.scheduledExecutor;
/* 259 */       return executor.schedule(task, new Trigger()
/*     */           {
/*     */             @Nullable
/*     */             public Date getNextRunTime(@Nullable LastExecution le, Date taskScheduledTime) {
/* 263 */               return trigger.nextExecutionTime((le != null) ? (TriggerContext)new SimpleTriggerContext(le
/* 264 */                     .getScheduledStart(), le.getRunStart(), le.getRunEnd()) : (TriggerContext)new SimpleTriggerContext());
/*     */             }
/*     */ 
/*     */             
/*     */             public boolean skipRun(LastExecution lastExecution, Date scheduledRunTime) {
/* 269 */               return false;
/*     */             }
/*     */           });
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/concurrent/ConcurrentTaskScheduler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */