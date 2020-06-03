/*     */ package org.springframework.aop.config;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
/*     */ import org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator;
/*     */ import org.springframework.aop.framework.autoproxy.InfrastructureAdvisorAutoProxyCreator;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
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
/*     */ public abstract class AopConfigUtils
/*     */ {
/*     */   public static final String AUTO_PROXY_CREATOR_BEAN_NAME = "org.springframework.aop.config.internalAutoProxyCreator";
/*  57 */   private static final List<Class<?>> APC_PRIORITY_LIST = new ArrayList<>(3);
/*     */ 
/*     */   
/*     */   static {
/*  61 */     APC_PRIORITY_LIST.add(InfrastructureAdvisorAutoProxyCreator.class);
/*  62 */     APC_PRIORITY_LIST.add(AspectJAwareAdvisorAutoProxyCreator.class);
/*  63 */     APC_PRIORITY_LIST.add(AnnotationAwareAspectJAutoProxyCreator.class);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static BeanDefinition registerAutoProxyCreatorIfNecessary(BeanDefinitionRegistry registry) {
/*  69 */     return registerAutoProxyCreatorIfNecessary(registry, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static BeanDefinition registerAutoProxyCreatorIfNecessary(BeanDefinitionRegistry registry, @Nullable Object source) {
/*  76 */     return registerOrEscalateApcAsRequired(InfrastructureAdvisorAutoProxyCreator.class, registry, source);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static BeanDefinition registerAspectJAutoProxyCreatorIfNecessary(BeanDefinitionRegistry registry) {
/*  81 */     return registerAspectJAutoProxyCreatorIfNecessary(registry, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static BeanDefinition registerAspectJAutoProxyCreatorIfNecessary(BeanDefinitionRegistry registry, @Nullable Object source) {
/*  88 */     return registerOrEscalateApcAsRequired(AspectJAwareAdvisorAutoProxyCreator.class, registry, source);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static BeanDefinition registerAspectJAnnotationAutoProxyCreatorIfNecessary(BeanDefinitionRegistry registry) {
/*  93 */     return registerAspectJAnnotationAutoProxyCreatorIfNecessary(registry, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static BeanDefinition registerAspectJAnnotationAutoProxyCreatorIfNecessary(BeanDefinitionRegistry registry, @Nullable Object source) {
/* 100 */     return registerOrEscalateApcAsRequired(AnnotationAwareAspectJAutoProxyCreator.class, registry, source);
/*     */   }
/*     */   
/*     */   public static void forceAutoProxyCreatorToUseClassProxying(BeanDefinitionRegistry registry) {
/* 104 */     if (registry.containsBeanDefinition("org.springframework.aop.config.internalAutoProxyCreator")) {
/* 105 */       BeanDefinition definition = registry.getBeanDefinition("org.springframework.aop.config.internalAutoProxyCreator");
/* 106 */       definition.getPropertyValues().add("proxyTargetClass", Boolean.TRUE);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void forceAutoProxyCreatorToExposeProxy(BeanDefinitionRegistry registry) {
/* 111 */     if (registry.containsBeanDefinition("org.springframework.aop.config.internalAutoProxyCreator")) {
/* 112 */       BeanDefinition definition = registry.getBeanDefinition("org.springframework.aop.config.internalAutoProxyCreator");
/* 113 */       definition.getPropertyValues().add("exposeProxy", Boolean.TRUE);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static BeanDefinition registerOrEscalateApcAsRequired(Class<?> cls, BeanDefinitionRegistry registry, @Nullable Object source) {
/* 121 */     Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
/*     */     
/* 123 */     if (registry.containsBeanDefinition("org.springframework.aop.config.internalAutoProxyCreator")) {
/* 124 */       BeanDefinition apcDefinition = registry.getBeanDefinition("org.springframework.aop.config.internalAutoProxyCreator");
/* 125 */       if (!cls.getName().equals(apcDefinition.getBeanClassName())) {
/* 126 */         int currentPriority = findPriorityForClass(apcDefinition.getBeanClassName());
/* 127 */         int requiredPriority = findPriorityForClass(cls);
/* 128 */         if (currentPriority < requiredPriority) {
/* 129 */           apcDefinition.setBeanClassName(cls.getName());
/*     */         }
/*     */       } 
/* 132 */       return null;
/*     */     } 
/*     */     
/* 135 */     RootBeanDefinition beanDefinition = new RootBeanDefinition(cls);
/* 136 */     beanDefinition.setSource(source);
/* 137 */     beanDefinition.getPropertyValues().add("order", Integer.valueOf(-2147483648));
/* 138 */     beanDefinition.setRole(2);
/* 139 */     registry.registerBeanDefinition("org.springframework.aop.config.internalAutoProxyCreator", (BeanDefinition)beanDefinition);
/* 140 */     return (BeanDefinition)beanDefinition;
/*     */   }
/*     */   
/*     */   private static int findPriorityForClass(Class<?> clazz) {
/* 144 */     return APC_PRIORITY_LIST.indexOf(clazz);
/*     */   }
/*     */   
/*     */   private static int findPriorityForClass(@Nullable String className) {
/* 148 */     for (int i = 0; i < APC_PRIORITY_LIST.size(); i++) {
/* 149 */       Class<?> clazz = APC_PRIORITY_LIST.get(i);
/* 150 */       if (clazz.getName().equals(className)) {
/* 151 */         return i;
/*     */       }
/*     */     } 
/* 154 */     throw new IllegalArgumentException("Class name [" + className + "] is not a known auto-proxy creator class");
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/config/AopConfigUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */