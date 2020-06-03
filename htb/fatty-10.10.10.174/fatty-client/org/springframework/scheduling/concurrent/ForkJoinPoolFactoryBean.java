/*     */ package org.springframework.scheduling.concurrent;
/*     */ 
/*     */ import java.util.concurrent.ForkJoinPool;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.FactoryBean;
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
/*     */ public class ForkJoinPoolFactoryBean
/*     */   implements FactoryBean<ForkJoinPool>, InitializingBean, DisposableBean
/*     */ {
/*     */   private boolean commonPool = false;
/*  37 */   private int parallelism = Runtime.getRuntime().availableProcessors();
/*     */   
/*  39 */   private ForkJoinPool.ForkJoinWorkerThreadFactory threadFactory = ForkJoinPool.defaultForkJoinWorkerThreadFactory;
/*     */   
/*     */   @Nullable
/*     */   private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
/*     */   
/*     */   private boolean asyncMode = false;
/*     */   
/*  46 */   private int awaitTerminationSeconds = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ForkJoinPool forkJoinPool;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCommonPool(boolean commonPool) {
/*  66 */     this.commonPool = commonPool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParallelism(int parallelism) {
/*  73 */     this.parallelism = parallelism;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThreadFactory(ForkJoinPool.ForkJoinWorkerThreadFactory threadFactory) {
/*  81 */     this.threadFactory = threadFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
/*  89 */     this.uncaughtExceptionHandler = uncaughtExceptionHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAsyncMode(boolean asyncMode) {
/*  99 */     this.asyncMode = asyncMode;
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
/*     */   public void setAwaitTerminationSeconds(int awaitTerminationSeconds) {
/* 120 */     this.awaitTerminationSeconds = awaitTerminationSeconds;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 125 */     this.forkJoinPool = this.commonPool ? ForkJoinPool.commonPool() : new ForkJoinPool(this.parallelism, this.threadFactory, this.uncaughtExceptionHandler, this.asyncMode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ForkJoinPool getObject() {
/* 133 */     return this.forkJoinPool;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/* 138 */     return ForkJoinPool.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 143 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 149 */     if (this.forkJoinPool != null) {
/*     */       
/* 151 */       this.forkJoinPool.shutdown();
/*     */ 
/*     */       
/* 154 */       if (this.awaitTerminationSeconds > 0)
/*     */         try {
/* 156 */           this.forkJoinPool.awaitTermination(this.awaitTerminationSeconds, TimeUnit.SECONDS);
/*     */         }
/* 158 */         catch (InterruptedException ex) {
/* 159 */           Thread.currentThread().interrupt();
/*     */         }  
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/concurrent/ForkJoinPoolFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */