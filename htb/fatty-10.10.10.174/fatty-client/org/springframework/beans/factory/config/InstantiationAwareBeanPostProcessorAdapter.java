/*    */ package org.springframework.beans.factory.config;
/*    */ 
/*    */ import java.beans.PropertyDescriptor;
/*    */ import java.lang.reflect.Constructor;
/*    */ import org.springframework.beans.BeansException;
/*    */ import org.springframework.beans.PropertyValues;
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
/*    */ 
/*    */ public abstract class InstantiationAwareBeanPostProcessorAdapter
/*    */   implements SmartInstantiationAwareBeanPostProcessor
/*    */ {
/*    */   @Nullable
/*    */   public Class<?> predictBeanType(Class<?> beanClass, String beanName) throws BeansException {
/* 46 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName) throws BeansException {
/* 52 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
/* 57 */     return bean;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
/* 63 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
/* 68 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
/* 75 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
/* 83 */     return pvs;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
/* 88 */     return bean;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
/* 93 */     return bean;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/InstantiationAwareBeanPostProcessorAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */