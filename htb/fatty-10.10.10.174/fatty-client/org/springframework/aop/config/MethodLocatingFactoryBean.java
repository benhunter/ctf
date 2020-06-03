/*    */ package org.springframework.aop.config;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.beans.BeanUtils;
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.beans.factory.BeanFactoryAware;
/*    */ import org.springframework.beans.factory.FactoryBean;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ public class MethodLocatingFactoryBean
/*    */   implements FactoryBean<Method>, BeanFactoryAware
/*    */ {
/*    */   @Nullable
/*    */   private String targetBeanName;
/*    */   @Nullable
/*    */   private String methodName;
/*    */   @Nullable
/*    */   private Method method;
/*    */   
/*    */   public void setTargetBeanName(String targetBeanName) {
/* 52 */     this.targetBeanName = targetBeanName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setMethodName(String methodName) {
/* 61 */     this.methodName = methodName;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setBeanFactory(BeanFactory beanFactory) {
/* 66 */     if (!StringUtils.hasText(this.targetBeanName)) {
/* 67 */       throw new IllegalArgumentException("Property 'targetBeanName' is required");
/*    */     }
/* 69 */     if (!StringUtils.hasText(this.methodName)) {
/* 70 */       throw new IllegalArgumentException("Property 'methodName' is required");
/*    */     }
/*    */     
/* 73 */     Class<?> beanClass = beanFactory.getType(this.targetBeanName);
/* 74 */     if (beanClass == null) {
/* 75 */       throw new IllegalArgumentException("Can't determine type of bean with name '" + this.targetBeanName + "'");
/*    */     }
/* 77 */     this.method = BeanUtils.resolveSignature(this.methodName, beanClass);
/*    */     
/* 79 */     if (this.method == null) {
/* 80 */       throw new IllegalArgumentException("Unable to locate method [" + this.methodName + "] on bean [" + this.targetBeanName + "]");
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Method getObject() throws Exception {
/* 89 */     return this.method;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<Method> getObjectType() {
/* 94 */     return Method.class;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSingleton() {
/* 99 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/config/MethodLocatingFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */