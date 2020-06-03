/*     */ package org.springframework.http.client.reactive;
/*     */ 
/*     */ import java.util.concurrent.Executor;
/*     */ import org.eclipse.jetty.io.ByteBufferPool;
/*     */ import org.eclipse.jetty.io.MappedByteBufferPool;
/*     */ import org.eclipse.jetty.util.ProcessorUtils;
/*     */ import org.eclipse.jetty.util.component.LifeCycle;
/*     */ import org.eclipse.jetty.util.thread.QueuedThreadPool;
/*     */ import org.eclipse.jetty.util.thread.ScheduledExecutorScheduler;
/*     */ import org.eclipse.jetty.util.thread.Scheduler;
/*     */ import org.eclipse.jetty.util.thread.ThreadPool;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JettyResourceFactory
/*     */   implements InitializingBean, DisposableBean
/*     */ {
/*     */   @Nullable
/*     */   private Executor executor;
/*     */   @Nullable
/*     */   private ByteBufferPool byteBufferPool;
/*     */   @Nullable
/*     */   private Scheduler scheduler;
/*  58 */   private String threadPrefix = "jetty-http";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExecutor(@Nullable Executor executor) {
/*  67 */     this.executor = executor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setByteBufferPool(@Nullable ByteBufferPool byteBufferPool) {
/*  76 */     this.byteBufferPool = byteBufferPool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScheduler(@Nullable Scheduler scheduler) {
/*  85 */     this.scheduler = scheduler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThreadPrefix(String threadPrefix) {
/*  96 */     Assert.notNull(threadPrefix, "Thread prefix is required");
/*  97 */     this.threadPrefix = threadPrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Executor getExecutor() {
/* 105 */     return this.executor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ByteBufferPool getByteBufferPool() {
/* 113 */     return this.byteBufferPool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Scheduler getScheduler() {
/* 121 */     return this.scheduler;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws Exception {
/* 126 */     String name = this.threadPrefix + "@" + Integer.toHexString(hashCode());
/* 127 */     if (this.executor == null) {
/* 128 */       QueuedThreadPool threadPool = new QueuedThreadPool();
/* 129 */       threadPool.setName(name);
/* 130 */       this.executor = (Executor)threadPool;
/*     */     } 
/* 132 */     if (this.byteBufferPool == null) {
/* 133 */       this
/*     */ 
/*     */         
/* 136 */         .byteBufferPool = (ByteBufferPool)new MappedByteBufferPool(2048, (this.executor instanceof ThreadPool.SizedThreadPool) ? (((ThreadPool.SizedThreadPool)this.executor).getMaxThreads() / 2) : (ProcessorUtils.availableProcessors() * 2));
/*     */     }
/* 138 */     if (this.scheduler == null) {
/* 139 */       this.scheduler = (Scheduler)new ScheduledExecutorScheduler(name + "-scheduler", false);
/*     */     }
/*     */     
/* 142 */     if (this.executor instanceof LifeCycle) {
/* 143 */       ((LifeCycle)this.executor).start();
/*     */     }
/* 145 */     this.scheduler.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroy() throws Exception {
/*     */     try {
/* 151 */       if (this.executor instanceof LifeCycle) {
/* 152 */         ((LifeCycle)this.executor).stop();
/*     */       }
/*     */     }
/* 155 */     catch (Throwable throwable) {}
/*     */ 
/*     */     
/*     */     try {
/* 159 */       if (this.scheduler != null) {
/* 160 */         this.scheduler.stop();
/*     */       }
/*     */     }
/* 163 */     catch (Throwable throwable) {}
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/reactive/JettyResourceFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */