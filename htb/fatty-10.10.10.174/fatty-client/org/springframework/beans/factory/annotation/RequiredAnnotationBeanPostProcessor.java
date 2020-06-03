/*     */ package org.springframework.beans.factory.annotation;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.beans.PropertyValues;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.BeanInitializationException;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
/*     */ import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.core.Conventions;
/*     */ import org.springframework.core.PriorityOrdered;
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
/*     */ @Deprecated
/*     */ public class RequiredAnnotationBeanPostProcessor
/*     */   extends InstantiationAwareBeanPostProcessorAdapter
/*     */   implements MergedBeanDefinitionPostProcessor, PriorityOrdered, BeanFactoryAware
/*     */ {
/*  87 */   public static final String SKIP_REQUIRED_CHECK_ATTRIBUTE = Conventions.getQualifiedAttributeName(RequiredAnnotationBeanPostProcessor.class, "skipRequiredCheck");
/*     */ 
/*     */   
/*  90 */   private Class<? extends Annotation> requiredAnnotationType = (Class)Required.class;
/*     */   
/*  92 */   private int order = 2147483646;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ConfigurableListableBeanFactory beanFactory;
/*     */ 
/*     */   
/* 100 */   private final Set<String> validatedBeanNames = Collections.newSetFromMap(new ConcurrentHashMap<>(64));
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
/*     */   public void setRequiredAnnotationType(Class<? extends Annotation> requiredAnnotationType) {
/* 113 */     Assert.notNull(requiredAnnotationType, "'requiredAnnotationType' must not be null");
/* 114 */     this.requiredAnnotationType = requiredAnnotationType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<? extends Annotation> getRequiredAnnotationType() {
/* 121 */     return this.requiredAnnotationType;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 126 */     if (beanFactory instanceof ConfigurableListableBeanFactory) {
/* 127 */       this.beanFactory = (ConfigurableListableBeanFactory)beanFactory;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setOrder(int order) {
/* 132 */     this.order = order;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/* 137 */     return this.order;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) {
/* 149 */     if (!this.validatedBeanNames.contains(beanName)) {
/* 150 */       if (!shouldSkip(this.beanFactory, beanName)) {
/* 151 */         List<String> invalidProperties = new ArrayList<>();
/* 152 */         for (PropertyDescriptor pd : pds) {
/* 153 */           if (isRequiredProperty(pd) && !pvs.contains(pd.getName())) {
/* 154 */             invalidProperties.add(pd.getName());
/*     */           }
/*     */         } 
/* 157 */         if (!invalidProperties.isEmpty()) {
/* 158 */           throw new BeanInitializationException(buildExceptionMessage(invalidProperties, beanName));
/*     */         }
/*     */       } 
/* 161 */       this.validatedBeanNames.add(beanName);
/*     */     } 
/* 163 */     return pvs;
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
/*     */   protected boolean shouldSkip(@Nullable ConfigurableListableBeanFactory beanFactory, String beanName) {
/* 178 */     if (beanFactory == null || !beanFactory.containsBeanDefinition(beanName)) {
/* 179 */       return false;
/*     */     }
/* 181 */     BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
/* 182 */     if (beanDefinition.getFactoryBeanName() != null) {
/* 183 */       return true;
/*     */     }
/* 185 */     Object value = beanDefinition.getAttribute(SKIP_REQUIRED_CHECK_ATTRIBUTE);
/* 186 */     return (value != null && (Boolean.TRUE.equals(value) || Boolean.valueOf(value.toString()).booleanValue()));
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
/*     */   protected boolean isRequiredProperty(PropertyDescriptor propertyDescriptor) {
/* 199 */     Method setter = propertyDescriptor.getWriteMethod();
/* 200 */     return (setter != null && AnnotationUtils.getAnnotation(setter, getRequiredAnnotationType()) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String buildExceptionMessage(List<String> invalidProperties, String beanName) {
/* 210 */     int size = invalidProperties.size();
/* 211 */     StringBuilder sb = new StringBuilder();
/* 212 */     sb.append((size == 1) ? "Property" : "Properties");
/* 213 */     for (int i = 0; i < size; i++) {
/* 214 */       String propertyName = invalidProperties.get(i);
/* 215 */       if (i > 0) {
/* 216 */         if (i == size - 1) {
/* 217 */           sb.append(" and");
/*     */         } else {
/*     */           
/* 220 */           sb.append(",");
/*     */         } 
/*     */       }
/* 223 */       sb.append(" '").append(propertyName).append("'");
/*     */     } 
/* 225 */     sb.append((size == 1) ? " is" : " are");
/* 226 */     sb.append(" required for bean '").append(beanName).append("'");
/* 227 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/annotation/RequiredAnnotationBeanPostProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */