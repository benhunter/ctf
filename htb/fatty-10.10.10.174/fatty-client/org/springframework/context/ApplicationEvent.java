/*    */ package org.springframework.context;
/*    */ 
/*    */ import java.util.EventObject;
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
/*    */ public abstract class ApplicationEvent
/*    */   extends EventObject
/*    */ {
/*    */   private static final long serialVersionUID = 7099057708183571937L;
/*    */   private final long timestamp;
/*    */   
/*    */   public ApplicationEvent(Object source) {
/* 42 */     super(source);
/* 43 */     this.timestamp = System.currentTimeMillis();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final long getTimestamp() {
/* 51 */     return this.timestamp;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/ApplicationEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */