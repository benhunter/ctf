/*    */ package org.springframework.web.util;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import javax.servlet.http.HttpSessionEvent;
/*    */ import javax.servlet.http.HttpSessionListener;
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
/*    */ public class HttpSessionMutexListener
/*    */   implements HttpSessionListener
/*    */ {
/*    */   public void sessionCreated(HttpSessionEvent event) {
/* 48 */     event.getSession().setAttribute(WebUtils.SESSION_MUTEX_ATTRIBUTE, new Mutex());
/*    */   }
/*    */ 
/*    */   
/*    */   public void sessionDestroyed(HttpSessionEvent event) {
/* 53 */     event.getSession().removeAttribute(WebUtils.SESSION_MUTEX_ATTRIBUTE);
/*    */   }
/*    */   
/*    */   private static class Mutex implements Serializable {
/*    */     private Mutex() {}
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/HttpSessionMutexListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */