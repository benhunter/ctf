/*    */ package org.springframework.aop.framework.autoproxy;
/*    */ 
/*    */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ public class InfrastructureAdvisorAutoProxyCreator
/*    */   extends AbstractAdvisorAutoProxyCreator
/*    */ {
/*    */   @Nullable
/*    */   private ConfigurableListableBeanFactory beanFactory;
/*    */   
/*    */   protected void initBeanFactory(ConfigurableListableBeanFactory beanFactory) {
/* 39 */     super.initBeanFactory(beanFactory);
/* 40 */     this.beanFactory = beanFactory;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isEligibleAdvisorBean(String beanName) {
/* 45 */     return (this.beanFactory != null && this.beanFactory.containsBeanDefinition(beanName) && this.beanFactory
/* 46 */       .getBeanDefinition(beanName).getRole() == 2);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/autoproxy/InfrastructureAdvisorAutoProxyCreator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */