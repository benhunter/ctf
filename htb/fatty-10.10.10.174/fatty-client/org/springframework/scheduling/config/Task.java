/*    */ package org.springframework.scheduling.config;
/*    */ 
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
/*    */ public class Task
/*    */ {
/*    */   private final Runnable runnable;
/*    */   
/*    */   public Task(Runnable runnable) {
/* 39 */     Assert.notNull(runnable, "Runnable must not be null");
/* 40 */     this.runnable = runnable;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Runnable getRunnable() {
/* 48 */     return this.runnable;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 54 */     return this.runnable.toString();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/config/Task.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */