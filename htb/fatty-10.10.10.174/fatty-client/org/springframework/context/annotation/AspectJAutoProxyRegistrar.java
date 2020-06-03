/*    */ package org.springframework.context.annotation;
/*    */ 
/*    */ import org.springframework.aop.config.AopConfigUtils;
/*    */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*    */ import org.springframework.core.annotation.AnnotationAttributes;
/*    */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*    */ import org.springframework.core.type.AnnotationMetadata;
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
/*    */ class AspectJAutoProxyRegistrar
/*    */   implements ImportBeanDefinitionRegistrar
/*    */ {
/*    */   public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
/* 45 */     AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(registry);
/*    */ 
/*    */     
/* 48 */     AnnotationAttributes enableAspectJAutoProxy = AnnotationConfigUtils.attributesFor((AnnotatedTypeMetadata)importingClassMetadata, EnableAspectJAutoProxy.class);
/* 49 */     if (enableAspectJAutoProxy != null) {
/* 50 */       if (enableAspectJAutoProxy.getBoolean("proxyTargetClass")) {
/* 51 */         AopConfigUtils.forceAutoProxyCreatorToUseClassProxying(registry);
/*    */       }
/* 53 */       if (enableAspectJAutoProxy.getBoolean("exposeProxy"))
/* 54 */         AopConfigUtils.forceAutoProxyCreatorToExposeProxy(registry); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/AspectJAutoProxyRegistrar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */