/*     */ package org.springframework.aop.scope;
/*     */ 
/*     */ import org.springframework.aop.framework.autoproxy.AutoProxyUtils;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public abstract class ScopedProxyUtils
/*     */ {
/*     */   private static final String TARGET_NAME_PREFIX = "scopedTarget.";
/*     */   
/*     */   public static BeanDefinitionHolder createScopedProxy(BeanDefinitionHolder definition, BeanDefinitionRegistry registry, boolean proxyTargetClass) {
/*  52 */     String originalBeanName = definition.getBeanName();
/*  53 */     BeanDefinition targetDefinition = definition.getBeanDefinition();
/*  54 */     String targetBeanName = getTargetBeanName(originalBeanName);
/*     */ 
/*     */ 
/*     */     
/*  58 */     RootBeanDefinition proxyDefinition = new RootBeanDefinition(ScopedProxyFactoryBean.class);
/*  59 */     proxyDefinition.setDecoratedDefinition(new BeanDefinitionHolder(targetDefinition, targetBeanName));
/*  60 */     proxyDefinition.setOriginatingBeanDefinition(targetDefinition);
/*  61 */     proxyDefinition.setSource(definition.getSource());
/*  62 */     proxyDefinition.setRole(targetDefinition.getRole());
/*     */     
/*  64 */     proxyDefinition.getPropertyValues().add("targetBeanName", targetBeanName);
/*  65 */     if (proxyTargetClass) {
/*  66 */       targetDefinition.setAttribute(AutoProxyUtils.PRESERVE_TARGET_CLASS_ATTRIBUTE, Boolean.TRUE);
/*     */     }
/*     */     else {
/*     */       
/*  70 */       proxyDefinition.getPropertyValues().add("proxyTargetClass", Boolean.FALSE);
/*     */     } 
/*     */ 
/*     */     
/*  74 */     proxyDefinition.setAutowireCandidate(targetDefinition.isAutowireCandidate());
/*  75 */     proxyDefinition.setPrimary(targetDefinition.isPrimary());
/*  76 */     if (targetDefinition instanceof AbstractBeanDefinition) {
/*  77 */       proxyDefinition.copyQualifiersFrom((AbstractBeanDefinition)targetDefinition);
/*     */     }
/*     */ 
/*     */     
/*  81 */     targetDefinition.setAutowireCandidate(false);
/*  82 */     targetDefinition.setPrimary(false);
/*     */ 
/*     */     
/*  85 */     registry.registerBeanDefinition(targetBeanName, targetDefinition);
/*     */ 
/*     */ 
/*     */     
/*  89 */     return new BeanDefinitionHolder((BeanDefinition)proxyDefinition, originalBeanName, definition.getAliases());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getTargetBeanName(String originalBeanName) {
/*  98 */     return "scopedTarget." + originalBeanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isScopedTarget(@Nullable String beanName) {
/* 107 */     return (beanName != null && beanName.startsWith("scopedTarget."));
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/scope/ScopedProxyUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */