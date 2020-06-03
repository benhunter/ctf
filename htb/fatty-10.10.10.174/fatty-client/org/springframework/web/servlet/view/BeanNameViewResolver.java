/*    */ package org.springframework.web.servlet.view;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import org.springframework.beans.BeansException;
/*    */ import org.springframework.context.ApplicationContext;
/*    */ import org.springframework.core.Ordered;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.web.context.support.WebApplicationObjectSupport;
/*    */ import org.springframework.web.servlet.View;
/*    */ import org.springframework.web.servlet.ViewResolver;
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
/*    */ public class BeanNameViewResolver
/*    */   extends WebApplicationObjectSupport
/*    */   implements ViewResolver, Ordered
/*    */ {
/* 57 */   private int order = Integer.MAX_VALUE;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setOrder(int order) {
/* 66 */     this.order = order;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getOrder() {
/* 71 */     return this.order;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public View resolveViewName(String viewName, Locale locale) throws BeansException {
/* 78 */     ApplicationContext context = obtainApplicationContext();
/* 79 */     if (!context.containsBean(viewName))
/*    */     {
/* 81 */       return null;
/*    */     }
/* 83 */     if (!context.isTypeMatch(viewName, View.class)) {
/* 84 */       if (this.logger.isDebugEnabled()) {
/* 85 */         this.logger.debug("Found bean named '" + viewName + "' but it does not implement View");
/*    */       }
/*    */ 
/*    */       
/* 89 */       return null;
/*    */     } 
/* 91 */     return (View)context.getBean(viewName, View.class);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/BeanNameViewResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */