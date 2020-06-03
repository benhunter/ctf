/*     */ package org.springframework.scheduling.support;
/*     */ 
/*     */ import java.util.Date;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.scheduling.Trigger;
/*     */ import org.springframework.scheduling.TriggerContext;
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
/*     */ 
/*     */ 
/*     */ public class PeriodicTrigger
/*     */   implements Trigger
/*     */ {
/*     */   private final long period;
/*     */   private final TimeUnit timeUnit;
/*  53 */   private volatile long initialDelay = 0L;
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile boolean fixedRate = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public PeriodicTrigger(long period) {
/*  62 */     this(period, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PeriodicTrigger(long period, @Nullable TimeUnit timeUnit) {
/*  71 */     Assert.isTrue((period >= 0L), "period must not be negative");
/*  72 */     this.timeUnit = (timeUnit != null) ? timeUnit : TimeUnit.MILLISECONDS;
/*  73 */     this.period = this.timeUnit.toMillis(period);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getPeriod() {
/*  82 */     return this.period;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeUnit getTimeUnit() {
/*  90 */     return this.timeUnit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInitialDelay(long initialDelay) {
/*  99 */     this.initialDelay = this.timeUnit.toMillis(initialDelay);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getInitialDelay() {
/* 107 */     return this.initialDelay;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFixedRate(boolean fixedRate) {
/* 116 */     this.fixedRate = fixedRate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFixedRate() {
/* 125 */     return this.fixedRate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date nextExecutionTime(TriggerContext triggerContext) {
/* 134 */     Date lastExecution = triggerContext.lastScheduledExecutionTime();
/* 135 */     Date lastCompletion = triggerContext.lastCompletionTime();
/* 136 */     if (lastExecution == null || lastCompletion == null) {
/* 137 */       return new Date(System.currentTimeMillis() + this.initialDelay);
/*     */     }
/* 139 */     if (this.fixedRate) {
/* 140 */       return new Date(lastExecution.getTime() + this.period);
/*     */     }
/* 142 */     return new Date(lastCompletion.getTime() + this.period);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 148 */     if (this == other) {
/* 149 */       return true;
/*     */     }
/* 151 */     if (!(other instanceof PeriodicTrigger)) {
/* 152 */       return false;
/*     */     }
/* 154 */     PeriodicTrigger otherTrigger = (PeriodicTrigger)other;
/* 155 */     return (this.fixedRate == otherTrigger.fixedRate && this.initialDelay == otherTrigger.initialDelay && this.period == otherTrigger.period);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 161 */     return (this.fixedRate ? 17 : 29) + (int)(37L * this.period) + (int)(41L * this.initialDelay);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/support/PeriodicTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */