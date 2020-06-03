/*    */ package org.springframework.scheduling.support;
/*    */ 
/*    */ import java.util.Date;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.scheduling.TriggerContext;
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
/*    */ 
/*    */ 
/*    */ public class SimpleTriggerContext
/*    */   implements TriggerContext
/*    */ {
/*    */   @Nullable
/*    */   private volatile Date lastScheduledExecutionTime;
/*    */   @Nullable
/*    */   private volatile Date lastActualExecutionTime;
/*    */   @Nullable
/*    */   private volatile Date lastCompletionTime;
/*    */   
/*    */   public SimpleTriggerContext() {}
/*    */   
/*    */   public SimpleTriggerContext(Date lastScheduledExecutionTime, Date lastActualExecutionTime, Date lastCompletionTime) {
/* 55 */     this.lastScheduledExecutionTime = lastScheduledExecutionTime;
/* 56 */     this.lastActualExecutionTime = lastActualExecutionTime;
/* 57 */     this.lastCompletionTime = lastCompletionTime;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void update(Date lastScheduledExecutionTime, Date lastActualExecutionTime, Date lastCompletionTime) {
/* 68 */     this.lastScheduledExecutionTime = lastScheduledExecutionTime;
/* 69 */     this.lastActualExecutionTime = lastActualExecutionTime;
/* 70 */     this.lastCompletionTime = lastCompletionTime;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Date lastScheduledExecutionTime() {
/* 77 */     return this.lastScheduledExecutionTime;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Date lastActualExecutionTime() {
/* 83 */     return this.lastActualExecutionTime;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Date lastCompletionTime() {
/* 89 */     return this.lastCompletionTime;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/support/SimpleTriggerContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */