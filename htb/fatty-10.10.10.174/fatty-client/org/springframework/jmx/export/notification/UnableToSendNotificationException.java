/*    */ package org.springframework.jmx.export.notification;
/*    */ 
/*    */ import org.springframework.jmx.JmxException;
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
/*    */ public class UnableToSendNotificationException
/*    */   extends JmxException
/*    */ {
/*    */   public UnableToSendNotificationException(String msg) {
/* 40 */     super(msg);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public UnableToSendNotificationException(String msg, Throwable cause) {
/* 50 */     super(msg, cause);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/export/notification/UnableToSendNotificationException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */