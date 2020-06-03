/*     */ package org.springframework.jmx.export.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Collection;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.annotation.AnnotationBeanUtils;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.beans.factory.config.EmbeddedValueResolver;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.jmx.export.metadata.InvalidMetadataException;
/*     */ import org.springframework.jmx.export.metadata.JmxAttributeSource;
/*     */ import org.springframework.jmx.export.metadata.ManagedAttribute;
/*     */ import org.springframework.jmx.export.metadata.ManagedMetric;
/*     */ import org.springframework.jmx.export.metadata.ManagedNotification;
/*     */ import org.springframework.jmx.export.metadata.ManagedOperation;
/*     */ import org.springframework.jmx.export.metadata.ManagedOperationParameter;
/*     */ import org.springframework.jmx.export.metadata.ManagedResource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.StringValueResolver;
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
/*     */ public class AnnotationJmxAttributeSource
/*     */   implements JmxAttributeSource, BeanFactoryAware
/*     */ {
/*     */   @Nullable
/*     */   private StringValueResolver embeddedValueResolver;
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/*  59 */     if (beanFactory instanceof ConfigurableBeanFactory) {
/*  60 */       this.embeddedValueResolver = (StringValueResolver)new EmbeddedValueResolver((ConfigurableBeanFactory)beanFactory);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ManagedResource getManagedResource(Class<?> beanClass) throws InvalidMetadataException {
/*  68 */     ManagedResource ann = (ManagedResource)AnnotationUtils.findAnnotation(beanClass, ManagedResource.class);
/*  69 */     if (ann == null) {
/*  70 */       return null;
/*     */     }
/*  72 */     Class<?> declaringClass = AnnotationUtils.findAnnotationDeclaringClass(ManagedResource.class, beanClass);
/*  73 */     Class<?> target = (declaringClass != null && !declaringClass.isInterface()) ? declaringClass : beanClass;
/*  74 */     if (!Modifier.isPublic(target.getModifiers())) {
/*  75 */       throw new InvalidMetadataException("@ManagedResource class '" + target.getName() + "' must be public");
/*     */     }
/*  77 */     ManagedResource managedResource = new ManagedResource();
/*  78 */     AnnotationBeanUtils.copyPropertiesToBean(ann, managedResource, this.embeddedValueResolver, new String[0]);
/*  79 */     return managedResource;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ManagedAttribute getManagedAttribute(Method method) throws InvalidMetadataException {
/*  85 */     ManagedAttribute ann = (ManagedAttribute)AnnotationUtils.findAnnotation(method, ManagedAttribute.class);
/*  86 */     if (ann == null) {
/*  87 */       return null;
/*     */     }
/*  89 */     ManagedAttribute managedAttribute = new ManagedAttribute();
/*  90 */     AnnotationBeanUtils.copyPropertiesToBean(ann, managedAttribute, new String[] { "defaultValue" });
/*  91 */     if (ann.defaultValue().length() > 0) {
/*  92 */       managedAttribute.setDefaultValue(ann.defaultValue());
/*     */     }
/*  94 */     return managedAttribute;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ManagedMetric getManagedMetric(Method method) throws InvalidMetadataException {
/* 100 */     ManagedMetric ann = (ManagedMetric)AnnotationUtils.findAnnotation(method, ManagedMetric.class);
/* 101 */     return copyPropertiesToBean(ann, ManagedMetric.class);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ManagedOperation getManagedOperation(Method method) throws InvalidMetadataException {
/* 107 */     ManagedOperation ann = (ManagedOperation)AnnotationUtils.findAnnotation(method, ManagedOperation.class);
/* 108 */     return copyPropertiesToBean(ann, ManagedOperation.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ManagedOperationParameter[] getManagedOperationParameters(Method method) throws InvalidMetadataException {
/* 115 */     Set<ManagedOperationParameter> anns = AnnotationUtils.getRepeatableAnnotations(method, ManagedOperationParameter.class, ManagedOperationParameters.class);
/*     */     
/* 117 */     return copyPropertiesToBeanArray((Collection)anns, ManagedOperationParameter.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ManagedNotification[] getManagedNotifications(Class<?> clazz) throws InvalidMetadataException {
/* 124 */     Set<ManagedNotification> anns = AnnotationUtils.getRepeatableAnnotations(clazz, ManagedNotification.class, ManagedNotifications.class);
/*     */     
/* 126 */     return copyPropertiesToBeanArray((Collection)anns, ManagedNotification.class);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> T[] copyPropertiesToBeanArray(Collection<? extends Annotation> anns, Class<T> beanClass) {
/* 132 */     T[] beans = (T[])Array.newInstance(beanClass, anns.size());
/* 133 */     int i = 0;
/* 134 */     for (Annotation ann : anns) {
/* 135 */       beans[i++] = copyPropertiesToBean(ann, beanClass);
/*     */     }
/* 137 */     return beans;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static <T> T copyPropertiesToBean(@Nullable Annotation ann, Class<T> beanClass) {
/* 142 */     if (ann == null) {
/* 143 */       return null;
/*     */     }
/* 145 */     T bean = (T)BeanUtils.instantiateClass(beanClass);
/* 146 */     AnnotationBeanUtils.copyPropertiesToBean(ann, bean, new String[0]);
/* 147 */     return bean;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/export/annotation/AnnotationJmxAttributeSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */