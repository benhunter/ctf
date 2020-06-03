/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.ObjectFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class ObjectFactoryCreatingFactoryBean
/*     */   extends AbstractFactoryBean<ObjectFactory<Object>>
/*     */ {
/*     */   @Nullable
/*     */   private String targetBeanName;
/*     */   
/*     */   public void setTargetBeanName(String targetBeanName) {
/* 112 */     this.targetBeanName = targetBeanName;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws Exception {
/* 117 */     Assert.hasText(this.targetBeanName, "Property 'targetBeanName' is required");
/* 118 */     super.afterPropertiesSet();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/* 124 */     return ObjectFactory.class;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ObjectFactory<Object> createInstance() {
/* 129 */     BeanFactory beanFactory = getBeanFactory();
/* 130 */     Assert.state((beanFactory != null), "No BeanFactory available");
/* 131 */     Assert.state((this.targetBeanName != null), "No target bean name specified");
/* 132 */     return new TargetBeanObjectFactory(beanFactory, this.targetBeanName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class TargetBeanObjectFactory
/*     */     implements ObjectFactory<Object>, Serializable
/*     */   {
/*     */     private final BeanFactory beanFactory;
/*     */ 
/*     */     
/*     */     private final String targetBeanName;
/*     */ 
/*     */     
/*     */     public TargetBeanObjectFactory(BeanFactory beanFactory, String targetBeanName) {
/* 147 */       this.beanFactory = beanFactory;
/* 148 */       this.targetBeanName = targetBeanName;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getObject() throws BeansException {
/* 153 */       return this.beanFactory.getBean(this.targetBeanName);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/ObjectFactoryCreatingFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */