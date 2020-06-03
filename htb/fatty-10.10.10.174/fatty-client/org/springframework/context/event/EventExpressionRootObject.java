/*    */ package org.springframework.context.event;
/*    */ 
/*    */ import org.springframework.context.ApplicationEvent;
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
/*    */ class EventExpressionRootObject
/*    */ {
/*    */   private final ApplicationEvent event;
/*    */   private final Object[] args;
/*    */   
/*    */   public EventExpressionRootObject(ApplicationEvent event, Object[] args) {
/* 34 */     this.event = event;
/* 35 */     this.args = args;
/*    */   }
/*    */   
/*    */   public ApplicationEvent getEvent() {
/* 39 */     return this.event;
/*    */   }
/*    */   
/*    */   public Object[] getArgs() {
/* 43 */     return this.args;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/event/EventExpressionRootObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */