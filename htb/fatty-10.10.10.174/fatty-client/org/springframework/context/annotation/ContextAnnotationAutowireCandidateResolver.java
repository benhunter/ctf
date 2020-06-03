/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.aop.TargetSource;
/*     */ import org.springframework.aop.framework.ProxyFactory;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.annotation.QualifierAnnotationAutowireCandidateResolver;
/*     */ import org.springframework.beans.factory.config.DependencyDescriptor;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
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
/*     */ public class ContextAnnotationAutowireCandidateResolver
/*     */   extends QualifierAnnotationAutowireCandidateResolver
/*     */ {
/*     */   @Nullable
/*     */   public Object getLazyResolutionProxyIfNecessary(DependencyDescriptor descriptor, @Nullable String beanName) {
/*  52 */     return isLazy(descriptor) ? buildLazyResolutionProxy(descriptor, beanName) : null;
/*     */   }
/*     */   
/*     */   protected boolean isLazy(DependencyDescriptor descriptor) {
/*  56 */     for (Annotation ann : descriptor.getAnnotations()) {
/*  57 */       Lazy lazy = (Lazy)AnnotationUtils.getAnnotation(ann, Lazy.class);
/*  58 */       if (lazy != null && lazy.value()) {
/*  59 */         return true;
/*     */       }
/*     */     } 
/*  62 */     MethodParameter methodParam = descriptor.getMethodParameter();
/*  63 */     if (methodParam != null) {
/*  64 */       Method method = methodParam.getMethod();
/*  65 */       if (method == null || void.class == method.getReturnType()) {
/*  66 */         Lazy lazy = (Lazy)AnnotationUtils.getAnnotation(methodParam.getAnnotatedElement(), Lazy.class);
/*  67 */         if (lazy != null && lazy.value()) {
/*  68 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*  72 */     return false;
/*     */   }
/*     */   
/*     */   protected Object buildLazyResolutionProxy(final DependencyDescriptor descriptor, @Nullable final String beanName) {
/*  76 */     Assert.state(getBeanFactory() instanceof DefaultListableBeanFactory, "BeanFactory needs to be a DefaultListableBeanFactory");
/*     */     
/*  78 */     final DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)getBeanFactory();
/*  79 */     TargetSource ts = new TargetSource()
/*     */       {
/*     */         public Class<?> getTargetClass() {
/*  82 */           return descriptor.getDependencyType();
/*     */         }
/*     */         
/*     */         public boolean isStatic() {
/*  86 */           return false;
/*     */         }
/*     */         
/*     */         public Object getTarget() {
/*  90 */           Object target = beanFactory.doResolveDependency(descriptor, beanName, null, null);
/*  91 */           if (target == null) {
/*  92 */             Class<?> type = getTargetClass();
/*  93 */             if (Map.class == type) {
/*  94 */               return Collections.emptyMap();
/*     */             }
/*  96 */             if (List.class == type) {
/*  97 */               return Collections.emptyList();
/*     */             }
/*  99 */             if (Set.class == type || Collection.class == type) {
/* 100 */               return Collections.emptySet();
/*     */             }
/* 102 */             throw new NoSuchBeanDefinitionException(descriptor.getResolvableType(), "Optional dependency not present for lazy injection point");
/*     */           } 
/*     */           
/* 105 */           return target;
/*     */         }
/*     */ 
/*     */         
/*     */         public void releaseTarget(Object target) {}
/*     */       };
/* 111 */     ProxyFactory pf = new ProxyFactory();
/* 112 */     pf.setTargetSource(ts);
/* 113 */     Class<?> dependencyType = descriptor.getDependencyType();
/* 114 */     if (dependencyType.isInterface()) {
/* 115 */       pf.addInterface(dependencyType);
/*     */     }
/* 117 */     return pf.getProxy(beanFactory.getBeanClassLoader());
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/ContextAnnotationAutowireCandidateResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */