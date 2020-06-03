/*     */ package org.springframework.scheduling.concurrent;
/*     */ 
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.RejectedExecutionHandler;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledThreadPoolExecutor;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.scheduling.support.DelegatingErrorHandlingRunnable;
/*     */ import org.springframework.scheduling.support.TaskUtils;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ 
/*     */ 
/*     */ public class ScheduledExecutorFactoryBean
/*     */   extends ExecutorConfigurationSupport
/*     */   implements FactoryBean<ScheduledExecutorService>
/*     */ {
/*  77 */   private int poolSize = 1;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ScheduledExecutorTask[] scheduledExecutorTasks;
/*     */ 
/*     */   
/*     */   private boolean removeOnCancelPolicy = false;
/*     */ 
/*     */   
/*     */   private boolean continueScheduledExecutionAfterException = false;
/*     */ 
/*     */   
/*     */   private boolean exposeUnconfigurableExecutor = false;
/*     */   
/*     */   @Nullable
/*     */   private ScheduledExecutorService exposedExecutor;
/*     */ 
/*     */   
/*     */   public void setPoolSize(int poolSize) {
/*  97 */     Assert.isTrue((poolSize > 0), "'poolSize' must be 1 or higher");
/*  98 */     this.poolSize = poolSize;
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
/*     */   public void setScheduledExecutorTasks(ScheduledExecutorTask... scheduledExecutorTasks) {
/* 110 */     this.scheduledExecutorTasks = scheduledExecutorTasks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemoveOnCancelPolicy(boolean removeOnCancelPolicy) {
/* 119 */     this.removeOnCancelPolicy = removeOnCancelPolicy;
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
/*     */   public void setContinueScheduledExecutionAfterException(boolean continueScheduledExecutionAfterException) {
/* 132 */     this.continueScheduledExecutionAfterException = continueScheduledExecutionAfterException;
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
/*     */   public void setExposeUnconfigurableExecutor(boolean exposeUnconfigurableExecutor) {
/* 144 */     this.exposeUnconfigurableExecutor = exposeUnconfigurableExecutor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ExecutorService initializeExecutor(ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
/* 153 */     ScheduledExecutorService executor = createExecutor(this.poolSize, threadFactory, rejectedExecutionHandler);
/*     */     
/* 155 */     if (this.removeOnCancelPolicy) {
/* 156 */       if (executor instanceof ScheduledThreadPoolExecutor) {
/* 157 */         ((ScheduledThreadPoolExecutor)executor).setRemoveOnCancelPolicy(true);
/*     */       } else {
/*     */         
/* 160 */         this.logger.debug("Could not apply remove-on-cancel policy - not a ScheduledThreadPoolExecutor");
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 165 */     if (!ObjectUtils.isEmpty((Object[])this.scheduledExecutorTasks)) {
/* 166 */       registerTasks(this.scheduledExecutorTasks, executor);
/*     */     }
/*     */ 
/*     */     
/* 170 */     this
/* 171 */       .exposedExecutor = this.exposeUnconfigurableExecutor ? Executors.unconfigurableScheduledExecutorService(executor) : executor;
/*     */     
/* 173 */     return executor;
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
/* 190 */     return new ScheduledThreadPoolExecutor(poolSize, threadFactory, rejectedExecutionHandler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerTasks(ScheduledExecutorTask[] tasks, ScheduledExecutorService executor) {
/* 200 */     for (ScheduledExecutorTask task : tasks) {
/* 201 */       Runnable runnable = getRunnableToSchedule(task);
/* 202 */       if (task.isOneTimeTask()) {
/* 203 */         executor.schedule(runnable, task.getDelay(), task.getTimeUnit());
/*     */       
/*     */       }
/* 206 */       else if (task.isFixedRate()) {
/* 207 */         executor.scheduleAtFixedRate(runnable, task.getDelay(), task.getPeriod(), task.getTimeUnit());
/*     */       } else {
/*     */         
/* 210 */         executor.scheduleWithFixedDelay(runnable, task.getDelay(), task.getPeriod(), task.getTimeUnit());
/*     */       } 
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
/*     */   protected Runnable getRunnableToSchedule(ScheduledExecutorTask task) {
/* 228 */     return this.continueScheduledExecutionAfterException ? (Runnable)new DelegatingErrorHandlingRunnable(task
/* 229 */         .getRunnable(), TaskUtils.LOG_AND_SUPPRESS_ERROR_HANDLER) : (Runnable)new DelegatingErrorHandlingRunnable(task
/* 230 */         .getRunnable(), TaskUtils.LOG_AND_PROPAGATE_ERROR_HANDLER);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ScheduledExecutorService getObject() {
/* 237 */     return this.exposedExecutor;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<? extends ScheduledExecutorService> getObjectType() {
/* 242 */     return (this.exposedExecutor != null) ? (Class)this.exposedExecutor.getClass() : ScheduledExecutorService.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 247 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/concurrent/ScheduledExecutorFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */