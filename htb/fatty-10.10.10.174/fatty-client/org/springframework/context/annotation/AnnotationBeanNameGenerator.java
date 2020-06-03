/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.beans.Introspector;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnnotationBeanNameGenerator
/*     */   implements BeanNameGenerator
/*     */ {
/*     */   private static final String COMPONENT_ANNOTATION_CLASSNAME = "org.springframework.stereotype.Component";
/*     */   
/*     */   public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
/*  71 */     if (definition instanceof AnnotatedBeanDefinition) {
/*  72 */       String beanName = determineBeanNameFromAnnotation((AnnotatedBeanDefinition)definition);
/*  73 */       if (StringUtils.hasText(beanName))
/*     */       {
/*  75 */         return beanName;
/*     */       }
/*     */     } 
/*     */     
/*  79 */     return buildDefaultBeanName(definition, registry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String determineBeanNameFromAnnotation(AnnotatedBeanDefinition annotatedDef) {
/*  89 */     AnnotationMetadata amd = annotatedDef.getMetadata();
/*  90 */     Set<String> types = amd.getAnnotationTypes();
/*  91 */     String beanName = null;
/*  92 */     for (String type : types) {
/*  93 */       AnnotationAttributes attributes = AnnotationConfigUtils.attributesFor((AnnotatedTypeMetadata)amd, type);
/*  94 */       if (attributes != null && isStereotypeWithNameValue(type, amd.getMetaAnnotationTypes(type), (Map<String, Object>)attributes)) {
/*  95 */         Object value = attributes.get("value");
/*  96 */         if (value instanceof String) {
/*  97 */           String strVal = (String)value;
/*  98 */           if (StringUtils.hasLength(strVal)) {
/*  99 */             if (beanName != null && !strVal.equals(beanName)) {
/* 100 */               throw new IllegalStateException("Stereotype annotations suggest inconsistent component names: '" + beanName + "' versus '" + strVal + "'");
/*     */             }
/*     */             
/* 103 */             beanName = strVal;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 108 */     return beanName;
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
/*     */ 
/*     */   
/*     */   protected boolean isStereotypeWithNameValue(String annotationType, Set<String> metaAnnotationTypes, @Nullable Map<String, Object> attributes) {
/* 125 */     boolean isStereotype = (annotationType.equals("org.springframework.stereotype.Component") || metaAnnotationTypes.contains("org.springframework.stereotype.Component") || annotationType.equals("javax.annotation.ManagedBean") || annotationType.equals("javax.inject.Named"));
/*     */     
/* 127 */     return (isStereotype && attributes != null && attributes.containsKey("value"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String buildDefaultBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
/* 138 */     return buildDefaultBeanName(definition);
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
/*     */   protected String buildDefaultBeanName(BeanDefinition definition) {
/* 152 */     String beanClassName = definition.getBeanClassName();
/* 153 */     Assert.state((beanClassName != null), "No bean class name set");
/* 154 */     String shortClassName = ClassUtils.getShortName(beanClassName);
/* 155 */     return Introspector.decapitalize(shortClassName);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/AnnotationBeanNameGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */