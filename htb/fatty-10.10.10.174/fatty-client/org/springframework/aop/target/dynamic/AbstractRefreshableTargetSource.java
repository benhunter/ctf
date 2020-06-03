/*     */ package org.springframework.aop.target.dynamic;
/*     */ 
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.TargetSource;
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
/*     */ public abstract class AbstractRefreshableTargetSource
/*     */   implements TargetSource, Refreshable
/*     */ {
/*  43 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   @Nullable
/*     */   protected Object targetObject;
/*     */   
/*  48 */   private long refreshCheckDelay = -1L;
/*     */   
/*  50 */   private long lastRefreshCheck = -1L;
/*     */   
/*  52 */   private long lastRefreshTime = -1L;
/*     */   
/*  54 */   private long refreshCount = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRefreshCheckDelay(long refreshCheckDelay) {
/*  64 */     this.refreshCheckDelay = refreshCheckDelay;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Class<?> getTargetClass() {
/*  70 */     if (this.targetObject == null) {
/*  71 */       refresh();
/*     */     }
/*  73 */     return this.targetObject.getClass();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStatic() {
/*  81 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final synchronized Object getTarget() {
/*  87 */     if ((refreshCheckDelayElapsed() && requiresRefresh()) || this.targetObject == null) {
/*  88 */       refresh();
/*     */     }
/*  90 */     return this.targetObject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseTarget(Object object) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized void refresh() {
/* 103 */     this.logger.debug("Attempting to refresh target");
/*     */     
/* 105 */     this.targetObject = freshTarget();
/* 106 */     this.refreshCount++;
/* 107 */     this.lastRefreshTime = System.currentTimeMillis();
/*     */     
/* 109 */     this.logger.debug("Target refreshed successfully");
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized long getRefreshCount() {
/* 114 */     return this.refreshCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized long getLastRefreshTime() {
/* 119 */     return this.lastRefreshTime;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean refreshCheckDelayElapsed() {
/* 124 */     if (this.refreshCheckDelay < 0L) {
/* 125 */       return false;
/*     */     }
/*     */     
/* 128 */     long currentTimeMillis = System.currentTimeMillis();
/*     */     
/* 130 */     if (this.lastRefreshCheck < 0L || currentTimeMillis - this.lastRefreshCheck > this.refreshCheckDelay) {
/*     */       
/* 132 */       this.lastRefreshCheck = currentTimeMillis;
/* 133 */       this.logger.debug("Refresh check delay elapsed - checking whether refresh is required");
/* 134 */       return true;
/*     */     } 
/*     */     
/* 137 */     return false;
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
/*     */   protected boolean requiresRefresh() {
/* 150 */     return true;
/*     */   }
/*     */   
/*     */   protected abstract Object freshTarget();
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/target/dynamic/AbstractRefreshableTargetSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */