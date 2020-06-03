/*    */ package org.springframework.web.context;
/*    */ 
/*    */ import java.util.Enumeration;
/*    */ import javax.servlet.ServletContext;
/*    */ import javax.servlet.ServletContextEvent;
/*    */ import javax.servlet.ServletContextListener;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.beans.factory.DisposableBean;
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
/*    */ public class ContextCleanupListener
/*    */   implements ServletContextListener
/*    */ {
/* 43 */   private static final Log logger = LogFactory.getLog(ContextCleanupListener.class);
/*    */ 
/*    */ 
/*    */   
/*    */   public void contextInitialized(ServletContextEvent event) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void contextDestroyed(ServletContextEvent event) {
/* 52 */     cleanupAttributes(event.getServletContext());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static void cleanupAttributes(ServletContext servletContext) {
/* 63 */     Enumeration<String> attrNames = servletContext.getAttributeNames();
/* 64 */     while (attrNames.hasMoreElements()) {
/* 65 */       String attrName = attrNames.nextElement();
/* 66 */       if (attrName.startsWith("org.springframework.")) {
/* 67 */         Object attrValue = servletContext.getAttribute(attrName);
/* 68 */         if (attrValue instanceof DisposableBean)
/*    */           try {
/* 70 */             ((DisposableBean)attrValue).destroy();
/*    */           }
/* 72 */           catch (Throwable ex) {
/* 73 */             if (logger.isWarnEnabled())
/* 74 */               logger.warn("Invocation of destroy method failed on ServletContext attribute with name '" + attrName + "'", ex); 
/*    */           }  
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/ContextCleanupListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */