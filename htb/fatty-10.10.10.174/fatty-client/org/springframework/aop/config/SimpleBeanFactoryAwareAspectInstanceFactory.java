/*    */ package org.springframework.aop.config;
/*    */ 
/*    */ import org.springframework.aop.aspectj.AspectInstanceFactory;
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.beans.factory.BeanFactoryAware;
/*    */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*    */ import org.springframework.core.Ordered;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ public class SimpleBeanFactoryAwareAspectInstanceFactory
/*    */   implements AspectInstanceFactory, BeanFactoryAware
/*    */ {
/*    */   @Nullable
/*    */   private String aspectBeanName;
/*    */   @Nullable
/*    */   private BeanFactory beanFactory;
/*    */   
/*    */   public void setAspectBeanName(String aspectBeanName) {
/* 50 */     this.aspectBeanName = aspectBeanName;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setBeanFactory(BeanFactory beanFactory) {
/* 55 */     this.beanFactory = beanFactory;
/* 56 */     Assert.notNull(this.aspectBeanName, "'aspectBeanName' is required");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getAspectInstance() {
/* 66 */     Assert.state((this.beanFactory != null), "No BeanFactory set");
/* 67 */     Assert.state((this.aspectBeanName != null), "No 'aspectBeanName' set");
/* 68 */     return this.beanFactory.getBean(this.aspectBeanName);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public ClassLoader getAspectClassLoader() {
/* 74 */     if (this.beanFactory instanceof ConfigurableBeanFactory) {
/* 75 */       return ((ConfigurableBeanFactory)this.beanFactory).getBeanClassLoader();
/*    */     }
/*    */     
/* 78 */     return ClassUtils.getDefaultClassLoader();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getOrder() {
/* 84 */     if (this.beanFactory != null && this.aspectBeanName != null && this.beanFactory
/* 85 */       .isSingleton(this.aspectBeanName) && this.beanFactory
/* 86 */       .isTypeMatch(this.aspectBeanName, Ordered.class)) {
/* 87 */       return ((Ordered)this.beanFactory.getBean(this.aspectBeanName)).getOrder();
/*    */     }
/* 89 */     return Integer.MAX_VALUE;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/config/SimpleBeanFactoryAwareAspectInstanceFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */