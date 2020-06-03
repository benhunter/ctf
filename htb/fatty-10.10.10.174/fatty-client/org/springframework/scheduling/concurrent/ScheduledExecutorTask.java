/*     */ package org.springframework.scheduling.concurrent;
/*     */ 
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ScheduledExecutorTask
/*     */ {
/*     */   @Nullable
/*     */   private Runnable runnable;
/*  47 */   private long delay = 0L;
/*     */   
/*  49 */   private long period = -1L;
/*     */   
/*  51 */   private TimeUnit timeUnit = TimeUnit.MILLISECONDS;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean fixedRate = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ScheduledExecutorTask() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ScheduledExecutorTask(Runnable executorTask) {
/*  72 */     this.runnable = executorTask;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ScheduledExecutorTask(Runnable executorTask, long delay) {
/*  82 */     this.runnable = executorTask;
/*  83 */     this.delay = delay;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ScheduledExecutorTask(Runnable executorTask, long delay, long period, boolean fixedRate) {
/*  94 */     this.runnable = executorTask;
/*  95 */     this.delay = delay;
/*  96 */     this.period = period;
/*  97 */     this.fixedRate = fixedRate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRunnable(Runnable executorTask) {
/* 105 */     this.runnable = executorTask;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Runnable getRunnable() {
/* 112 */     Assert.state((this.runnable != null), "No Runnable set");
/* 113 */     return this.runnable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDelay(long delay) {
/* 122 */     this.delay = delay;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getDelay() {
/* 129 */     return this.delay;
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
/*     */   public void setPeriod(long period) {
/* 147 */     this.period = period;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getPeriod() {
/* 154 */     return this.period;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOneTimeTask() {
/* 163 */     return (this.period <= 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeUnit(@Nullable TimeUnit timeUnit) {
/* 173 */     this.timeUnit = (timeUnit != null) ? timeUnit : TimeUnit.MILLISECONDS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeUnit getTimeUnit() {
/* 180 */     return this.timeUnit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFixedRate(boolean fixedRate) {
/* 191 */     this.fixedRate = fixedRate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFixedRate() {
/* 198 */     return this.fixedRate;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/concurrent/ScheduledExecutorTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */