/*    */ package org.springframework.beans.factory.config;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import javax.inject.Provider;
/*    */ import org.springframework.beans.BeansException;
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ public class ProviderCreatingFactoryBean
/*    */   extends AbstractFactoryBean<Provider<Object>>
/*    */ {
/*    */   @Nullable
/*    */   private String targetBeanName;
/*    */   
/*    */   public void setTargetBeanName(String targetBeanName) {
/* 57 */     this.targetBeanName = targetBeanName;
/*    */   }
/*    */ 
/*    */   
/*    */   public void afterPropertiesSet() throws Exception {
/* 62 */     Assert.hasText(this.targetBeanName, "Property 'targetBeanName' is required");
/* 63 */     super.afterPropertiesSet();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Class<?> getObjectType() {
/* 69 */     return Provider.class;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Provider<Object> createInstance() {
/* 74 */     BeanFactory beanFactory = getBeanFactory();
/* 75 */     Assert.state((beanFactory != null), "No BeanFactory available");
/* 76 */     Assert.state((this.targetBeanName != null), "No target bean name specified");
/* 77 */     return new TargetBeanProvider(beanFactory, this.targetBeanName);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static class TargetBeanProvider
/*    */     implements Provider<Object>, Serializable
/*    */   {
/*    */     private final BeanFactory beanFactory;
/*    */ 
/*    */     
/*    */     private final String targetBeanName;
/*    */ 
/*    */     
/*    */     public TargetBeanProvider(BeanFactory beanFactory, String targetBeanName) {
/* 92 */       this.beanFactory = beanFactory;
/* 93 */       this.targetBeanName = targetBeanName;
/*    */     }
/*    */ 
/*    */     
/*    */     public Object get() throws BeansException {
/* 98 */       return this.beanFactory.getBean(this.targetBeanName);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/ProviderCreatingFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */