/*    */ package org.springframework.context.event;
/*    */ 
/*    */ import org.springframework.context.ApplicationContext;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ApplicationContextEvent
/*    */   extends ApplicationEvent
/*    */ {
/*    */   public ApplicationContextEvent(ApplicationContext source) {
/* 37 */     super(source);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final ApplicationContext getApplicationContext() {
/* 44 */     return (ApplicationContext)getSource();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/event/ApplicationContextEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */