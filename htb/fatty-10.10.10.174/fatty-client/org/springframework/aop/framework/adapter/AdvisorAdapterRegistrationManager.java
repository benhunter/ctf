/*    */ package org.springframework.aop.framework.adapter;
/*    */ 
/*    */ import org.springframework.beans.BeansException;
/*    */ import org.springframework.beans.factory.config.BeanPostProcessor;
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
/*    */ public class AdvisorAdapterRegistrationManager
/*    */   implements BeanPostProcessor
/*    */ {
/* 38 */   private AdvisorAdapterRegistry advisorAdapterRegistry = GlobalAdvisorAdapterRegistry.getInstance();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setAdvisorAdapterRegistry(AdvisorAdapterRegistry advisorAdapterRegistry) {
/* 47 */     this.advisorAdapterRegistry = advisorAdapterRegistry;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
/* 53 */     return bean;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
/* 58 */     if (bean instanceof AdvisorAdapter) {
/* 59 */       this.advisorAdapterRegistry.registerAdvisorAdapter((AdvisorAdapter)bean);
/*    */     }
/* 61 */     return bean;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/adapter/AdvisorAdapterRegistrationManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */