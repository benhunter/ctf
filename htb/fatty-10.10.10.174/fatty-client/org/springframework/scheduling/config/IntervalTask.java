/*    */ package org.springframework.scheduling.config;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IntervalTask
/*    */   extends Task
/*    */ {
/*    */   private final long interval;
/*    */   private final long initialDelay;
/*    */   
/*    */   public IntervalTask(Runnable runnable, long interval, long initialDelay) {
/* 43 */     super(runnable);
/* 44 */     this.interval = interval;
/* 45 */     this.initialDelay = initialDelay;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public IntervalTask(Runnable runnable, long interval) {
/* 54 */     this(runnable, interval, 0L);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long getInterval() {
/* 62 */     return this.interval;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long getInitialDelay() {
/* 69 */     return this.initialDelay;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/config/IntervalTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */