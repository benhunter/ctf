/*    */ package org.springframework.jmx.export;
/*    */ 
/*    */ import javax.management.NotificationListener;
/*    */ import org.springframework.beans.factory.InitializingBean;
/*    */ import org.springframework.jmx.support.NotificationListenerHolder;
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
/*    */ public class NotificationListenerBean
/*    */   extends NotificationListenerHolder
/*    */   implements InitializingBean
/*    */ {
/*    */   public NotificationListenerBean() {}
/*    */   
/*    */   public NotificationListenerBean(NotificationListener notificationListener) {
/* 58 */     Assert.notNull(notificationListener, "NotificationListener must not be null");
/* 59 */     setNotificationListener(notificationListener);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void afterPropertiesSet() {
/* 65 */     if (getNotificationListener() == null) {
/* 66 */       throw new IllegalArgumentException("Property 'notificationListener' is required");
/*    */     }
/*    */   }
/*    */   
/*    */   void replaceObjectName(Object originalName, Object newName) {
/* 71 */     if (this.mappedObjectNames != null && this.mappedObjectNames.contains(originalName)) {
/* 72 */       this.mappedObjectNames.remove(originalName);
/* 73 */       this.mappedObjectNames.add(newName);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/export/NotificationListenerBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */