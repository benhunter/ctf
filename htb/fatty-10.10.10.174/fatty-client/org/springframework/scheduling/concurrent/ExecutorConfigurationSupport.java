/*     */ package org.springframework.scheduling.concurrent;
/*     */ 
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.RejectedExecutionHandler;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanNameAware;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ExecutorConfigurationSupport
/*     */   extends CustomizableThreadFactory
/*     */   implements BeanNameAware, InitializingBean, DisposableBean
/*     */ {
/*  52 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  54 */   private ThreadFactory threadFactory = this;
/*     */   
/*     */   private boolean threadNamePrefixSet = false;
/*     */   
/*  58 */   private RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();
/*     */   
/*     */   private boolean waitForTasksToCompleteOnShutdown = false;
/*     */   
/*  62 */   private int awaitTerminationSeconds = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String beanName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ExecutorService executor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThreadFactory(@Nullable ThreadFactory threadFactory) {
/*  86 */     this.threadFactory = (threadFactory != null) ? threadFactory : this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setThreadNamePrefix(@Nullable String threadNamePrefix) {
/*  91 */     super.setThreadNamePrefix(threadNamePrefix);
/*  92 */     this.threadNamePrefixSet = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRejectedExecutionHandler(@Nullable RejectedExecutionHandler rejectedExecutionHandler) {
/* 101 */     this.rejectedExecutionHandler = (rejectedExecutionHandler != null) ? rejectedExecutionHandler : new ThreadPoolExecutor.AbortPolicy();
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWaitForTasksToCompleteOnShutdown(boolean waitForJobsToCompleteOnShutdown) {
/* 121 */     this.waitForTasksToCompleteOnShutdown = waitForJobsToCompleteOnShutdown;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAwaitTerminationSeconds(int awaitTerminationSeconds) {
/* 148 */     this.awaitTerminationSeconds = awaitTerminationSeconds;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanName(String name) {
/* 153 */     this.beanName = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 163 */     initialize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initialize() {
/* 170 */     if (this.logger.isInfoEnabled()) {
/* 171 */       this.logger.info("Initializing ExecutorService" + ((this.beanName != null) ? (" '" + this.beanName + "'") : ""));
/*     */     }
/* 173 */     if (!this.threadNamePrefixSet && this.beanName != null) {
/* 174 */       setThreadNamePrefix(this.beanName + "-");
/*     */     }
/* 176 */     this.executor = initializeExecutor(this.threadFactory, this.rejectedExecutionHandler);
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
/*     */   protected abstract ExecutorService initializeExecutor(ThreadFactory paramThreadFactory, RejectedExecutionHandler paramRejectedExecutionHandler);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 198 */     shutdown();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void shutdown() {
/* 207 */     if (this.logger.isInfoEnabled()) {
/* 208 */       this.logger.info("Shutting down ExecutorService" + ((this.beanName != null) ? (" '" + this.beanName + "'") : ""));
/*     */     }
/* 210 */     if (this.executor != null) {
/* 211 */       if (this.waitForTasksToCompleteOnShutdown) {
/* 212 */         this.executor.shutdown();
/*     */       } else {
/*     */         
/* 215 */         for (Runnable remainingTask : this.executor.shutdownNow()) {
/* 216 */           cancelRemainingTask(remainingTask);
/*     */         }
/*     */       } 
/* 219 */       awaitTerminationIfNecessary(this.executor);
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
/*     */   protected void cancelRemainingTask(Runnable task) {
/* 232 */     if (task instanceof Future) {
/* 233 */       ((Future)task).cancel(true);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void awaitTerminationIfNecessary(ExecutorService executor) {
/* 242 */     if (this.awaitTerminationSeconds > 0)
/*     */       try {
/* 244 */         if (!executor.awaitTermination(this.awaitTerminationSeconds, TimeUnit.SECONDS) && 
/* 245 */           this.logger.isWarnEnabled()) {
/* 246 */           this.logger.warn("Timed out while waiting for executor" + ((this.beanName != null) ? (" '" + this.beanName + "'") : "") + " to terminate");
/*     */         
/*     */         }
/*     */       
/*     */       }
/* 251 */       catch (InterruptedException ex) {
/* 252 */         if (this.logger.isWarnEnabled()) {
/* 253 */           this.logger.warn("Interrupted while waiting for executor" + ((this.beanName != null) ? (" '" + this.beanName + "'") : "") + " to terminate");
/*     */         }
/*     */         
/* 256 */         Thread.currentThread().interrupt();
/*     */       }  
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/concurrent/ExecutorConfigurationSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */