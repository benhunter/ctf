/*    */ package org.springframework.aop.framework.autoproxy;
/*    */ 
/*    */ import org.springframework.aop.framework.AbstractAdvisingBeanPostProcessor;
/*    */ import org.springframework.aop.framework.ProxyFactory;
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.beans.factory.BeanFactoryAware;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractBeanFactoryAwareAdvisingPostProcessor
/*    */   extends AbstractAdvisingBeanPostProcessor
/*    */   implements BeanFactoryAware
/*    */ {
/*    */   @Nullable
/*    */   private ConfigurableListableBeanFactory beanFactory;
/*    */   
/*    */   public void setBeanFactory(BeanFactory beanFactory) {
/* 49 */     this.beanFactory = (beanFactory instanceof ConfigurableListableBeanFactory) ? (ConfigurableListableBeanFactory)beanFactory : null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected ProxyFactory prepareProxyFactory(Object bean, String beanName) {
/* 55 */     if (this.beanFactory != null) {
/* 56 */       AutoProxyUtils.exposeTargetClass(this.beanFactory, beanName, bean.getClass());
/*    */     }
/*    */     
/* 59 */     ProxyFactory proxyFactory = super.prepareProxyFactory(bean, beanName);
/* 60 */     if (!proxyFactory.isProxyTargetClass() && this.beanFactory != null && 
/* 61 */       AutoProxyUtils.shouldProxyTargetClass(this.beanFactory, beanName)) {
/* 62 */       proxyFactory.setProxyTargetClass(true);
/*    */     }
/* 64 */     return proxyFactory;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isEligible(Object bean, String beanName) {
/* 69 */     return (!AutoProxyUtils.isOriginalInstance(beanName, bean.getClass()) && super
/* 70 */       .isEligible(bean, beanName));
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/autoproxy/AbstractBeanFactoryAwareAdvisingPostProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */