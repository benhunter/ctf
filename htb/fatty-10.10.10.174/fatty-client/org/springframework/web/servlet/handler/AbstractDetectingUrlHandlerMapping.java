/*    */ package org.springframework.web.servlet.handler;
/*    */ 
/*    */ import org.springframework.beans.BeansException;
/*    */ import org.springframework.beans.factory.BeanFactoryUtils;
/*    */ import org.springframework.beans.factory.ListableBeanFactory;
/*    */ import org.springframework.context.ApplicationContext;
/*    */ import org.springframework.context.ApplicationContextException;
/*    */ import org.springframework.util.ObjectUtils;
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
/*    */ public abstract class AbstractDetectingUrlHandlerMapping
/*    */   extends AbstractUrlHandlerMapping
/*    */ {
/*    */   private boolean detectHandlersInAncestorContexts = false;
/*    */   
/*    */   public void setDetectHandlersInAncestorContexts(boolean detectHandlersInAncestorContexts) {
/* 48 */     this.detectHandlersInAncestorContexts = detectHandlersInAncestorContexts;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void initApplicationContext() throws ApplicationContextException {
/* 58 */     super.initApplicationContext();
/* 59 */     detectHandlers();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void detectHandlers() throws BeansException {
/* 71 */     ApplicationContext applicationContext = obtainApplicationContext();
/*    */ 
/*    */     
/* 74 */     String[] beanNames = this.detectHandlersInAncestorContexts ? BeanFactoryUtils.beanNamesForTypeIncludingAncestors((ListableBeanFactory)applicationContext, Object.class) : applicationContext.getBeanNamesForType(Object.class);
/*    */ 
/*    */     
/* 77 */     for (String beanName : beanNames) {
/* 78 */       String[] urls = determineUrlsForHandler(beanName);
/* 79 */       if (!ObjectUtils.isEmpty((Object[])urls))
/*    */       {
/* 81 */         registerHandler(urls, beanName);
/*    */       }
/*    */     } 
/*    */     
/* 85 */     if ((this.logger.isDebugEnabled() && !getHandlerMap().isEmpty()) || this.logger.isTraceEnabled())
/* 86 */       this.logger.debug("Detected " + getHandlerMap().size() + " mappings in " + formatMappingName()); 
/*    */   }
/*    */   
/*    */   protected abstract String[] determineUrlsForHandler(String paramString);
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/handler/AbstractDetectingUrlHandlerMapping.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */