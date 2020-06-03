/*    */ package org.springframework.context.annotation;
/*    */ 
/*    */ import java.util.Set;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
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
/*    */ public class AutoProxyRegistrar
/*    */   implements ImportBeanDefinitionRegistrar
/*    */ {
/* 40 */   private final Log logger = LogFactory.getLog(getClass());
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
/*    */   public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
/* 59 */     boolean candidateFound = false;
/* 60 */     Set<String> annTypes = importingClassMetadata.getAnnotationTypes();
/* 61 */     for (String annType : annTypes) {
/* 62 */       AnnotationAttributes candidate = AnnotationConfigUtils.attributesFor((AnnotatedTypeMetadata)importingClassMetadata, annType);
/* 63 */       if (candidate == null) {
/*    */         continue;
/*    */       }
/* 66 */       Object mode = candidate.get("mode");
/* 67 */       Object proxyTargetClass = candidate.get("proxyTargetClass");
/* 68 */       if (mode != null && proxyTargetClass != null && AdviceMode.class == mode.getClass() && Boolean.class == proxyTargetClass
/* 69 */         .getClass()) {
/* 70 */         candidateFound = true;
/* 71 */         if (mode == AdviceMode.PROXY) {
/* 72 */           AopConfigUtils.registerAutoProxyCreatorIfNecessary(registry);
/* 73 */           if (((Boolean)proxyTargetClass).booleanValue()) {
/* 74 */             AopConfigUtils.forceAutoProxyCreatorToUseClassProxying(registry);
/*    */             return;
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/* 80 */     if (!candidateFound && this.logger.isInfoEnabled()) {
/* 81 */       String name = getClass().getSimpleName();
/* 82 */       this.logger.info(String.format("%s was imported but no annotations were found having both 'mode' and 'proxyTargetClass' attributes of type AdviceMode and boolean respectively. This means that auto proxy creator registration and configuration may not have occurred as intended, and components may not be proxied as expected. Check to ensure that %s has been @Import'ed on the same class where these annotations are declared; otherwise remove the import of %s altogether.", new Object[] { name, name, name }));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/AutoProxyRegistrar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */