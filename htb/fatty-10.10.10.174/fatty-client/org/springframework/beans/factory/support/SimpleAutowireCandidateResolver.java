/*    */ package org.springframework.beans.factory.support;
/*    */ 
/*    */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*    */ import org.springframework.beans.factory.config.DependencyDescriptor;
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
/*    */ public class SimpleAutowireCandidateResolver
/*    */   implements AutowireCandidateResolver
/*    */ {
/*    */   public boolean isAutowireCandidate(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor) {
/* 35 */     return bdHolder.getBeanDefinition().isAutowireCandidate();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isRequired(DependencyDescriptor descriptor) {
/* 40 */     return descriptor.isRequired();
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object getSuggestedValue(DependencyDescriptor descriptor) {
/* 46 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object getLazyResolutionProxyIfNecessary(DependencyDescriptor descriptor, @Nullable String beanName) {
/* 52 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/SimpleAutowireCandidateResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */