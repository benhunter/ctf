/*    */ package org.springframework.scheduling.config;
/*    */ 
/*    */ import java.util.concurrent.ScheduledFuture;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ public final class ScheduledTask
/*    */ {
/*    */   private final Task task;
/*    */   @Nullable
/*    */   volatile ScheduledFuture<?> future;
/*    */   
/*    */   ScheduledTask(Task task) {
/* 42 */     this.task = task;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Task getTask() {
/* 52 */     return this.task;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void cancel() {
/* 59 */     ScheduledFuture<?> future = this.future;
/* 60 */     if (future != null) {
/* 61 */       future.cancel(true);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 67 */     return this.task.toString();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/config/ScheduledTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */