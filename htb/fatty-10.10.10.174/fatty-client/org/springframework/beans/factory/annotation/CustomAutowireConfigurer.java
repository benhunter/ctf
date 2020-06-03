/*     */ package org.springframework.beans.factory.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.support.AutowireCandidateResolver;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.core.Ordered;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CustomAutowireConfigurer
/*     */   implements BeanFactoryPostProcessor, BeanClassLoaderAware, Ordered
/*     */ {
/*  52 */   private int order = Integer.MAX_VALUE;
/*     */   
/*     */   @Nullable
/*     */   private Set<?> customQualifierTypes;
/*     */   
/*     */   @Nullable
/*  58 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */ 
/*     */   
/*     */   public void setOrder(int order) {
/*  62 */     this.order = order;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/*  67 */     return this.order;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(@Nullable ClassLoader beanClassLoader) {
/*  72 */     this.beanClassLoader = beanClassLoader;
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
/*     */   public void setCustomQualifierTypes(Set<?> customQualifierTypes) {
/*  86 */     this.customQualifierTypes = customQualifierTypes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
/*  93 */     if (this.customQualifierTypes != null) {
/*  94 */       if (!(beanFactory instanceof DefaultListableBeanFactory)) {
/*  95 */         throw new IllegalStateException("CustomAutowireConfigurer needs to operate on a DefaultListableBeanFactory");
/*     */       }
/*     */       
/*  98 */       DefaultListableBeanFactory dlbf = (DefaultListableBeanFactory)beanFactory;
/*  99 */       if (!(dlbf.getAutowireCandidateResolver() instanceof QualifierAnnotationAutowireCandidateResolver)) {
/* 100 */         dlbf.setAutowireCandidateResolver((AutowireCandidateResolver)new QualifierAnnotationAutowireCandidateResolver());
/*     */       }
/*     */       
/* 103 */       QualifierAnnotationAutowireCandidateResolver resolver = (QualifierAnnotationAutowireCandidateResolver)dlbf.getAutowireCandidateResolver();
/* 104 */       for (Object value : this.customQualifierTypes) {
/* 105 */         Class<? extends Annotation> customType = null;
/* 106 */         if (value instanceof Class) {
/* 107 */           customType = (Class<? extends Annotation>)value;
/*     */         }
/* 109 */         else if (value instanceof String) {
/* 110 */           String className = (String)value;
/* 111 */           customType = ClassUtils.resolveClassName(className, this.beanClassLoader);
/*     */         } else {
/*     */           
/* 114 */           throw new IllegalArgumentException("Invalid value [" + value + "] for custom qualifier type: needs to be Class or String.");
/*     */         } 
/*     */         
/* 117 */         if (!Annotation.class.isAssignableFrom(customType)) {
/* 118 */           throw new IllegalArgumentException("Qualifier type [" + customType
/* 119 */               .getName() + "] needs to be annotation type");
/*     */         }
/* 121 */         resolver.addQualifierType(customType);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/annotation/CustomAutowireConfigurer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */