/*     */ package org.springframework.scheduling.concurrent;
/*     */ 
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.RejectedExecutionHandler;
/*     */ import java.util.concurrent.SynchronousQueue;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ThreadPoolExecutorFactoryBean
/*     */   extends ExecutorConfigurationSupport
/*     */   implements FactoryBean<ExecutorService>, InitializingBean, DisposableBean
/*     */ {
/*  70 */   private int corePoolSize = 1;
/*     */   
/*  72 */   private int maxPoolSize = Integer.MAX_VALUE;
/*     */   
/*  74 */   private int keepAliveSeconds = 60;
/*     */   
/*     */   private boolean allowCoreThreadTimeOut = false;
/*     */   
/*  78 */   private int queueCapacity = Integer.MAX_VALUE;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean exposeUnconfigurableExecutor = false;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ExecutorService exposedExecutor;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCorePoolSize(int corePoolSize) {
/*  91 */     this.corePoolSize = corePoolSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxPoolSize(int maxPoolSize) {
/*  99 */     this.maxPoolSize = maxPoolSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeepAliveSeconds(int keepAliveSeconds) {
/* 107 */     this.keepAliveSeconds = keepAliveSeconds;
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
/* 118 */     this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
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
/* 130 */     this.queueCapacity = queueCapacity;
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
/* 142 */     this.exposeUnconfigurableExecutor = exposeUnconfigurableExecutor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ExecutorService initializeExecutor(ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
/* 150 */     BlockingQueue<Runnable> queue = createQueue(this.queueCapacity);
/* 151 */     ThreadPoolExecutor executor = createExecutor(this.corePoolSize, this.maxPoolSize, this.keepAliveSeconds, queue, threadFactory, rejectedExecutionHandler);
/*     */     
/* 153 */     if (this.allowCoreThreadTimeOut) {
/* 154 */       executor.allowCoreThreadTimeOut(true);
/*     */     }
/*     */ 
/*     */     
/* 158 */     this
/* 159 */       .exposedExecutor = this.exposeUnconfigurableExecutor ? Executors.unconfigurableExecutorService(executor) : executor;
/*     */     
/* 161 */     return executor;
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
/*     */   protected ThreadPoolExecutor createExecutor(int corePoolSize, int maxPoolSize, int keepAliveSeconds, BlockingQueue<Runnable> queue, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
/* 181 */     return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveSeconds, TimeUnit.SECONDS, queue, threadFactory, rejectedExecutionHandler);
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
/*     */   protected BlockingQueue<Runnable> createQueue(int queueCapacity) {
/* 195 */     if (queueCapacity > 0) {
/* 196 */       return new LinkedBlockingQueue<>(queueCapacity);
/*     */     }
/*     */     
/* 199 */     return new SynchronousQueue<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ExecutorService getObject() {
/* 207 */     return this.exposedExecutor;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<? extends ExecutorService> getObjectType() {
/* 212 */     return (this.exposedExecutor != null) ? (Class)this.exposedExecutor.getClass() : ExecutorService.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 217 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/concurrent/ThreadPoolExecutorFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */