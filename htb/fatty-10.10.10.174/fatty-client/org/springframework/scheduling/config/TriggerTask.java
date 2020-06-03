/*    */ package org.springframework.scheduling.config;
/*    */ 
/*    */ import org.springframework.scheduling.Trigger;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class TriggerTask
/*    */   extends Task
/*    */ {
/*    */   private final Trigger trigger;
/*    */   
/*    */   public TriggerTask(Runnable runnable, Trigger trigger) {
/* 42 */     super(runnable);
/* 43 */     Assert.notNull(trigger, "Trigger must not be null");
/* 44 */     this.trigger = trigger;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Trigger getTrigger() {
/* 52 */     return this.trigger;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/config/TriggerTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */