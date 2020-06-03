/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.core.Conventions;
/*     */ import org.springframework.core.annotation.Order;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.core.type.StandardAnnotationMetadata;
/*     */ import org.springframework.core.type.classreading.MetadataReader;
/*     */ import org.springframework.core.type.classreading.MetadataReaderFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.stereotype.Component;
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
/*     */ abstract class ConfigurationClassUtils
/*     */ {
/*     */   private static final String CONFIGURATION_CLASS_FULL = "full";
/*     */   private static final String CONFIGURATION_CLASS_LITE = "lite";
/*  55 */   private static final String CONFIGURATION_CLASS_ATTRIBUTE = Conventions.getQualifiedAttributeName(ConfigurationClassPostProcessor.class, "configurationClass");
/*     */ 
/*     */   
/*  58 */   private static final String ORDER_ATTRIBUTE = Conventions.getQualifiedAttributeName(ConfigurationClassPostProcessor.class, "order");
/*     */ 
/*     */   
/*  61 */   private static final Log logger = LogFactory.getLog(ConfigurationClassUtils.class);
/*     */   
/*  63 */   private static final Set<String> candidateIndicators = new HashSet<>(8);
/*     */   
/*     */   static {
/*  66 */     candidateIndicators.add(Component.class.getName());
/*  67 */     candidateIndicators.add(ComponentScan.class.getName());
/*  68 */     candidateIndicators.add(Import.class.getName());
/*  69 */     candidateIndicators.add(ImportResource.class.getName());
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
/*     */   public static boolean checkConfigurationClassCandidate(BeanDefinition beanDef, MetadataReaderFactory metadataReaderFactory) {
/*     */     AnnotationMetadata metadata;
/*  84 */     String className = beanDef.getBeanClassName();
/*  85 */     if (className == null || beanDef.getFactoryMethodName() != null) {
/*  86 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  90 */     if (beanDef instanceof AnnotatedBeanDefinition && className
/*  91 */       .equals(((AnnotatedBeanDefinition)beanDef).getMetadata().getClassName())) {
/*     */       
/*  93 */       metadata = ((AnnotatedBeanDefinition)beanDef).getMetadata();
/*     */     }
/*  95 */     else if (beanDef instanceof AbstractBeanDefinition && ((AbstractBeanDefinition)beanDef).hasBeanClass()) {
/*     */ 
/*     */       
/*  98 */       Class<?> beanClass = ((AbstractBeanDefinition)beanDef).getBeanClass();
/*  99 */       StandardAnnotationMetadata standardAnnotationMetadata = new StandardAnnotationMetadata(beanClass, true);
/*     */     } else {
/*     */       
/*     */       try {
/* 103 */         MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(className);
/* 104 */         metadata = metadataReader.getAnnotationMetadata();
/*     */       }
/* 106 */       catch (IOException ex) {
/* 107 */         if (logger.isDebugEnabled()) {
/* 108 */           logger.debug("Could not find class file for introspecting configuration annotations: " + className, ex);
/*     */         }
/*     */         
/* 111 */         return false;
/*     */       } 
/*     */     } 
/*     */     
/* 115 */     if (isFullConfigurationCandidate(metadata)) {
/* 116 */       beanDef.setAttribute(CONFIGURATION_CLASS_ATTRIBUTE, "full");
/*     */     }
/* 118 */     else if (isLiteConfigurationCandidate(metadata)) {
/* 119 */       beanDef.setAttribute(CONFIGURATION_CLASS_ATTRIBUTE, "lite");
/*     */     } else {
/*     */       
/* 122 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 126 */     Integer order = getOrder(metadata);
/* 127 */     if (order != null) {
/* 128 */       beanDef.setAttribute(ORDER_ATTRIBUTE, order);
/*     */     }
/*     */     
/* 131 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isConfigurationCandidate(AnnotationMetadata metadata) {
/* 142 */     return (isFullConfigurationCandidate(metadata) || isLiteConfigurationCandidate(metadata));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isFullConfigurationCandidate(AnnotationMetadata metadata) {
/* 153 */     return metadata.isAnnotated(Configuration.class.getName());
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
/*     */   public static boolean isLiteConfigurationCandidate(AnnotationMetadata metadata) {
/* 166 */     if (metadata.isInterface()) {
/* 167 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 171 */     for (String indicator : candidateIndicators) {
/* 172 */       if (metadata.isAnnotated(indicator)) {
/* 173 */         return true;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 179 */       return metadata.hasAnnotatedMethods(Bean.class.getName());
/*     */     }
/* 181 */     catch (Throwable ex) {
/* 182 */       if (logger.isDebugEnabled()) {
/* 183 */         logger.debug("Failed to introspect @Bean methods on class [" + metadata.getClassName() + "]: " + ex);
/*     */       }
/* 185 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isFullConfigurationClass(BeanDefinition beanDef) {
/* 194 */     return "full".equals(beanDef.getAttribute(CONFIGURATION_CLASS_ATTRIBUTE));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isLiteConfigurationClass(BeanDefinition beanDef) {
/* 202 */     return "lite".equals(beanDef.getAttribute(CONFIGURATION_CLASS_ATTRIBUTE));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static Integer getOrder(AnnotationMetadata metadata) {
/* 214 */     Map<String, Object> orderAttributes = metadata.getAnnotationAttributes(Order.class.getName());
/* 215 */     return (orderAttributes != null) ? (Integer)orderAttributes.get("value") : null;
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
/*     */   public static int getOrder(BeanDefinition beanDef) {
/* 227 */     Integer order = (Integer)beanDef.getAttribute(ORDER_ATTRIBUTE);
/* 228 */     return (order != null) ? order.intValue() : Integer.MAX_VALUE;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/ConfigurationClassUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */