/*    */ package org.springframework.context.annotation;
/*    */ 
/*    */ import org.springframework.aop.scope.ScopedProxyUtils;
/*    */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
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
/*    */ final class ScopedProxyCreator
/*    */ {
/*    */   public static BeanDefinitionHolder createScopedProxy(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry, boolean proxyTargetClass) {
/* 40 */     return ScopedProxyUtils.createScopedProxy(definitionHolder, registry, proxyTargetClass);
/*    */   }
/*    */   
/*    */   public static String getTargetBeanName(String originalBeanName) {
/* 44 */     return ScopedProxyUtils.getTargetBeanName(originalBeanName);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/ScopedProxyCreator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */