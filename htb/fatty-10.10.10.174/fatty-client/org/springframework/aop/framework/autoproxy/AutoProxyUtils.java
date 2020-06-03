/*     */ package org.springframework.aop.framework.autoproxy;
/*     */ 
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.core.Conventions;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public abstract class AutoProxyUtils
/*     */ {
/*  46 */   public static final String PRESERVE_TARGET_CLASS_ATTRIBUTE = Conventions.getQualifiedAttributeName(AutoProxyUtils.class, "preserveTargetClass");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   public static final String ORIGINAL_TARGET_CLASS_ATTRIBUTE = Conventions.getQualifiedAttributeName(AutoProxyUtils.class, "originalTargetClass");
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
/*     */   public static boolean shouldProxyTargetClass(ConfigurableListableBeanFactory beanFactory, @Nullable String beanName) {
/*  71 */     if (beanName != null && beanFactory.containsBeanDefinition(beanName)) {
/*  72 */       BeanDefinition bd = beanFactory.getBeanDefinition(beanName);
/*  73 */       return Boolean.TRUE.equals(bd.getAttribute(PRESERVE_TARGET_CLASS_ATTRIBUTE));
/*     */     } 
/*  75 */     return false;
/*     */   }
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
/*     */   @Nullable
/*     */   public static Class<?> determineTargetClass(ConfigurableListableBeanFactory beanFactory, @Nullable String beanName) {
/*  91 */     if (beanName == null) {
/*  92 */       return null;
/*     */     }
/*  94 */     if (beanFactory.containsBeanDefinition(beanName)) {
/*  95 */       BeanDefinition bd = beanFactory.getMergedBeanDefinition(beanName);
/*  96 */       Class<?> targetClass = (Class)bd.getAttribute(ORIGINAL_TARGET_CLASS_ATTRIBUTE);
/*  97 */       if (targetClass != null) {
/*  98 */         return targetClass;
/*     */       }
/*     */     } 
/* 101 */     return beanFactory.getType(beanName);
/*     */   }
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
/*     */   static void exposeTargetClass(ConfigurableListableBeanFactory beanFactory, @Nullable String beanName, Class<?> targetClass) {
/* 114 */     if (beanName != null && beanFactory.containsBeanDefinition(beanName)) {
/* 115 */       beanFactory.getMergedBeanDefinition(beanName).setAttribute(ORIGINAL_TARGET_CLASS_ATTRIBUTE, targetClass);
/*     */     }
/*     */   }
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
/*     */   static boolean isOriginalInstance(String beanName, Class<?> beanClass) {
/* 129 */     if (!StringUtils.hasLength(beanName) || beanName.length() != beanClass
/* 130 */       .getName().length() + ".ORIGINAL".length()) {
/* 131 */       return false;
/*     */     }
/* 133 */     return (beanName.startsWith(beanClass.getName()) && beanName
/* 134 */       .endsWith(".ORIGINAL"));
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/autoproxy/AutoProxyUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */