/*    */ package org.springframework.scheduling.config;
/*    */ 
/*    */ import org.springframework.scheduling.Trigger;
/*    */ import org.springframework.scheduling.support.CronTrigger;
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
/*    */ public class CronTask
/*    */   extends TriggerTask
/*    */ {
/*    */   private final String expression;
/*    */   
/*    */   public CronTask(Runnable runnable, String expression) {
/* 42 */     this(runnable, new CronTrigger(expression));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CronTask(Runnable runnable, CronTrigger cronTrigger) {
/* 51 */     super(runnable, (Trigger)cronTrigger);
/* 52 */     this.expression = cronTrigger.getExpression();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getExpression() {
/* 60 */     return this.expression;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/config/CronTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */