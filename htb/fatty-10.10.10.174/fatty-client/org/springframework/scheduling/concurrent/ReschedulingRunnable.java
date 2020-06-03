/*     */ package org.springframework.scheduling.concurrent;
/*     */ 
/*     */ import java.util.Date;
/*     */ import java.util.concurrent.Delayed;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.scheduling.Trigger;
/*     */ import org.springframework.scheduling.TriggerContext;
/*     */ import org.springframework.scheduling.support.DelegatingErrorHandlingRunnable;
/*     */ import org.springframework.scheduling.support.SimpleTriggerContext;
/*     */ import org.springframework.util.Assert;
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
/*     */ class ReschedulingRunnable
/*     */   extends DelegatingErrorHandlingRunnable
/*     */   implements ScheduledFuture<Object>
/*     */ {
/*     */   private final Trigger trigger;
/*  50 */   private final SimpleTriggerContext triggerContext = new SimpleTriggerContext();
/*     */   
/*     */   private final ScheduledExecutorService executor;
/*     */   
/*     */   @Nullable
/*     */   private ScheduledFuture<?> currentFuture;
/*     */   
/*     */   @Nullable
/*     */   private Date scheduledExecutionTime;
/*     */   
/*  60 */   private final Object triggerContextMonitor = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReschedulingRunnable(Runnable delegate, Trigger trigger, ScheduledExecutorService executor, ErrorHandler errorHandler) {
/*  66 */     super(delegate, errorHandler);
/*  67 */     this.trigger = trigger;
/*  68 */     this.executor = executor;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ScheduledFuture<?> schedule() {
/*  74 */     synchronized (this.triggerContextMonitor) {
/*  75 */       this.scheduledExecutionTime = this.trigger.nextExecutionTime((TriggerContext)this.triggerContext);
/*  76 */       if (this.scheduledExecutionTime == null) {
/*  77 */         return null;
/*     */       }
/*  79 */       long initialDelay = this.scheduledExecutionTime.getTime() - System.currentTimeMillis();
/*  80 */       this.currentFuture = this.executor.schedule((Runnable)this, initialDelay, TimeUnit.MILLISECONDS);
/*  81 */       return this;
/*     */     } 
/*     */   }
/*     */   
/*     */   private ScheduledFuture<?> obtainCurrentFuture() {
/*  86 */     Assert.state((this.currentFuture != null), "No scheduled future");
/*  87 */     return this.currentFuture;
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*  92 */     Date actualExecutionTime = new Date();
/*  93 */     super.run();
/*  94 */     Date completionTime = new Date();
/*  95 */     synchronized (this.triggerContextMonitor) {
/*  96 */       Assert.state((this.scheduledExecutionTime != null), "No scheduled execution");
/*  97 */       this.triggerContext.update(this.scheduledExecutionTime, actualExecutionTime, completionTime);
/*  98 */       if (!obtainCurrentFuture().isCancelled()) {
/*  99 */         schedule();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean cancel(boolean mayInterruptIfRunning) {
/* 107 */     synchronized (this.triggerContextMonitor) {
/* 108 */       return obtainCurrentFuture().cancel(mayInterruptIfRunning);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCancelled() {
/* 114 */     synchronized (this.triggerContextMonitor) {
/* 115 */       return obtainCurrentFuture().isCancelled();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDone() {
/* 121 */     synchronized (this.triggerContextMonitor) {
/* 122 */       return obtainCurrentFuture().isDone();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Object get() throws InterruptedException, ExecutionException {
/*     */     ScheduledFuture<?> curr;
/* 129 */     synchronized (this.triggerContextMonitor) {
/* 130 */       curr = obtainCurrentFuture();
/*     */     } 
/* 132 */     return curr.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/*     */     ScheduledFuture<?> curr;
/* 138 */     synchronized (this.triggerContextMonitor) {
/* 139 */       curr = obtainCurrentFuture();
/*     */     } 
/* 141 */     return curr.get(timeout, unit);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getDelay(TimeUnit unit) {
/*     */     ScheduledFuture<?> curr;
/* 147 */     synchronized (this.triggerContextMonitor) {
/* 148 */       curr = obtainCurrentFuture();
/*     */     } 
/* 150 */     return curr.getDelay(unit);
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(Delayed other) {
/* 155 */     if (this == other) {
/* 156 */       return 0;
/*     */     }
/* 158 */     long diff = getDelay(TimeUnit.MILLISECONDS) - other.getDelay(TimeUnit.MILLISECONDS);
/* 159 */     return (diff == 0L) ? 0 : ((diff < 0L) ? -1 : 1);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/concurrent/ReschedulingRunnable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */