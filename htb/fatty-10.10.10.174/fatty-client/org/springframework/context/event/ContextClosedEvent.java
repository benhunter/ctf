/*    */ package org.springframework.context.event;
/*    */ 
/*    */ import org.springframework.context.ApplicationContext;
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
/*    */ public class ContextClosedEvent
/*    */   extends ApplicationContextEvent
/*    */ {
/*    */   public ContextClosedEvent(ApplicationContext source) {
/* 37 */     super(source);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/event/ContextClosedEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */