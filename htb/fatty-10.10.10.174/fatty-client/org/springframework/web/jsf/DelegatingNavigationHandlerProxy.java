/*     */ package org.springframework.web.jsf;
/*     */ 
/*     */ import javax.faces.application.NavigationHandler;
/*     */ import javax.faces.context.FacesContext;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.web.context.WebApplicationContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DelegatingNavigationHandlerProxy
/*     */   extends NavigationHandler
/*     */ {
/*     */   public static final String DEFAULT_TARGET_BEAN_NAME = "jsfNavigationHandler";
/*     */   @Nullable
/*     */   private NavigationHandler originalNavigationHandler;
/*     */   
/*     */   public DelegatingNavigationHandlerProxy() {}
/*     */   
/*     */   public DelegatingNavigationHandlerProxy(NavigationHandler originalNavigationHandler) {
/*  97 */     this.originalNavigationHandler = originalNavigationHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleNavigation(FacesContext facesContext, String fromAction, String outcome) {
/* 112 */     NavigationHandler handler = getDelegate(facesContext);
/* 113 */     if (handler instanceof DecoratingNavigationHandler) {
/* 114 */       ((DecoratingNavigationHandler)handler).handleNavigation(facesContext, fromAction, outcome, this.originalNavigationHandler);
/*     */     }
/*     */     else {
/*     */       
/* 118 */       handler.handleNavigation(facesContext, fromAction, outcome);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected NavigationHandler getDelegate(FacesContext facesContext) {
/* 132 */     String targetBeanName = getTargetBeanName(facesContext);
/* 133 */     return (NavigationHandler)getBeanFactory(facesContext).getBean(targetBeanName, NavigationHandler.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getTargetBeanName(FacesContext facesContext) {
/* 143 */     return "jsfNavigationHandler";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanFactory getBeanFactory(FacesContext facesContext) {
/* 156 */     return (BeanFactory)getWebApplicationContext(facesContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected WebApplicationContext getWebApplicationContext(FacesContext facesContext) {
/* 167 */     return FacesContextUtils.getRequiredWebApplicationContext(facesContext);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/jsf/DelegatingNavigationHandlerProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */