/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Properties;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.config.DependencyDescriptor;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class GenericTypeAwareAutowireCandidateResolver
/*     */   extends SimpleAutowireCandidateResolver
/*     */   implements BeanFactoryAware
/*     */ {
/*     */   @Nullable
/*     */   private BeanFactory beanFactory;
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/*  54 */     this.beanFactory = beanFactory;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected final BeanFactory getBeanFactory() {
/*  59 */     return this.beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAutowireCandidate(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor) {
/*  65 */     if (!super.isAutowireCandidate(bdHolder, descriptor))
/*     */     {
/*  67 */       return false;
/*     */     }
/*  69 */     return checkGenericTypeMatch(bdHolder, descriptor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean checkGenericTypeMatch(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor) {
/*  77 */     ResolvableType dependencyType = descriptor.getResolvableType();
/*  78 */     if (dependencyType.getType() instanceof Class)
/*     */     {
/*  80 */       return true;
/*     */     }
/*     */     
/*  83 */     ResolvableType targetType = null;
/*  84 */     boolean cacheType = false;
/*  85 */     RootBeanDefinition rbd = null;
/*  86 */     if (bdHolder.getBeanDefinition() instanceof RootBeanDefinition) {
/*  87 */       rbd = (RootBeanDefinition)bdHolder.getBeanDefinition();
/*     */     }
/*  89 */     if (rbd != null) {
/*  90 */       targetType = rbd.targetType;
/*  91 */       if (targetType == null) {
/*  92 */         cacheType = true;
/*     */         
/*  94 */         targetType = getReturnTypeForFactoryMethod(rbd, descriptor);
/*  95 */         if (targetType == null) {
/*  96 */           RootBeanDefinition dbd = getResolvedDecoratedDefinition(rbd);
/*  97 */           if (dbd != null) {
/*  98 */             targetType = dbd.targetType;
/*  99 */             if (targetType == null) {
/* 100 */               targetType = getReturnTypeForFactoryMethod(dbd, descriptor);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 107 */     if (targetType == null) {
/*     */       
/* 109 */       if (this.beanFactory != null) {
/* 110 */         Class<?> beanType = this.beanFactory.getType(bdHolder.getBeanName());
/* 111 */         if (beanType != null) {
/* 112 */           targetType = ResolvableType.forClass(ClassUtils.getUserClass(beanType));
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 117 */       if (targetType == null && rbd != null && rbd.hasBeanClass() && rbd.getFactoryMethodName() == null) {
/* 118 */         Class<?> beanClass = rbd.getBeanClass();
/* 119 */         if (!FactoryBean.class.isAssignableFrom(beanClass)) {
/* 120 */           targetType = ResolvableType.forClass(ClassUtils.getUserClass(beanClass));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 125 */     if (targetType == null) {
/* 126 */       return true;
/*     */     }
/* 128 */     if (cacheType) {
/* 129 */       rbd.targetType = targetType;
/*     */     }
/* 131 */     if (descriptor.fallbackMatchAllowed() && (targetType
/* 132 */       .hasUnresolvableGenerics() || targetType.resolve() == Properties.class))
/*     */     {
/*     */ 
/*     */       
/* 136 */       return true;
/*     */     }
/*     */     
/* 139 */     return dependencyType.isAssignableFrom(targetType);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected RootBeanDefinition getResolvedDecoratedDefinition(RootBeanDefinition rbd) {
/* 144 */     BeanDefinitionHolder decDef = rbd.getDecoratedDefinition();
/* 145 */     if (decDef != null && this.beanFactory instanceof ConfigurableListableBeanFactory) {
/* 146 */       ConfigurableListableBeanFactory clbf = (ConfigurableListableBeanFactory)this.beanFactory;
/* 147 */       if (clbf.containsBeanDefinition(decDef.getBeanName())) {
/* 148 */         BeanDefinition dbd = clbf.getMergedBeanDefinition(decDef.getBeanName());
/* 149 */         if (dbd instanceof RootBeanDefinition) {
/* 150 */           return (RootBeanDefinition)dbd;
/*     */         }
/*     */       } 
/*     */     } 
/* 154 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected ResolvableType getReturnTypeForFactoryMethod(RootBeanDefinition rbd, DependencyDescriptor descriptor) {
/* 161 */     ResolvableType returnType = rbd.factoryMethodReturnType;
/* 162 */     if (returnType == null) {
/* 163 */       Method factoryMethod = rbd.getResolvedFactoryMethod();
/* 164 */       if (factoryMethod != null) {
/* 165 */         returnType = ResolvableType.forMethodReturnType(factoryMethod);
/*     */       }
/*     */     } 
/* 168 */     if (returnType != null) {
/* 169 */       Class<?> resolvedClass = returnType.resolve();
/* 170 */       if (resolvedClass != null && descriptor.getDependencyType().isAssignableFrom(resolvedClass))
/*     */       {
/*     */ 
/*     */         
/* 174 */         return returnType;
/*     */       }
/*     */     } 
/* 177 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/GenericTypeAwareAutowireCandidateResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */