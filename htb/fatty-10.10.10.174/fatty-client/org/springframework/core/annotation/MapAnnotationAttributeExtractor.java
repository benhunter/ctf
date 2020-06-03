/*     */ package org.springframework.core.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class MapAnnotationAttributeExtractor
/*     */   extends AbstractAliasAwareAnnotationAttributeExtractor<Map<String, Object>>
/*     */ {
/*     */   MapAnnotationAttributeExtractor(Map<String, Object> attributes, Class<? extends Annotation> annotationType, @Nullable AnnotatedElement annotatedElement) {
/*  58 */     super(annotationType, annotatedElement, enrichAndValidateAttributes(attributes, annotationType));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object getRawAttributeValue(Method attributeMethod) {
/*  65 */     return getRawAttributeValue(attributeMethod.getName());
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object getRawAttributeValue(String attributeName) {
/*  71 */     return getSource().get(attributeName);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Map<String, Object> enrichAndValidateAttributes(Map<String, Object> originalAttributes, Class<? extends Annotation> annotationType) {
/*  93 */     Map<String, Object> attributes = new LinkedHashMap<>(originalAttributes);
/*  94 */     Map<String, List<String>> attributeAliasMap = AnnotationUtils.getAttributeAliasMap(annotationType);
/*     */     
/*  96 */     for (Iterator<Method> iterator = AnnotationUtils.getAttributeMethods(annotationType).iterator(); iterator.hasNext(); ) { Method attributeMethod = iterator.next();
/*  97 */       String attributeName = attributeMethod.getName();
/*  98 */       Object attributeValue = attributes.get(attributeName);
/*     */ 
/*     */       
/* 101 */       if (attributeValue == null) {
/* 102 */         List<String> aliasNames = attributeAliasMap.get(attributeName);
/* 103 */         if (aliasNames != null) {
/* 104 */           for (String aliasName : aliasNames) {
/* 105 */             Object aliasValue = attributes.get(aliasName);
/* 106 */             if (aliasValue != null) {
/* 107 */               attributeValue = aliasValue;
/* 108 */               attributes.put(attributeName, attributeValue);
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
/*     */       
/* 116 */       if (attributeValue == null) {
/* 117 */         Object defaultValue = AnnotationUtils.getDefaultValue(annotationType, attributeName);
/* 118 */         if (defaultValue != null) {
/* 119 */           attributeValue = defaultValue;
/* 120 */           attributes.put(attributeName, attributeValue);
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 125 */       Assert.notNull(attributeValue, () -> String.format("Attributes map %s returned null for required attribute '%s' defined by annotation type [%s].", new Object[] { attributes, attributeName, annotationType.getName() }));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 130 */       Class<?> requiredReturnType = attributeMethod.getReturnType();
/* 131 */       Class<?> actualReturnType = attributeValue.getClass();
/*     */       
/* 133 */       if (!ClassUtils.isAssignable(requiredReturnType, actualReturnType)) {
/* 134 */         boolean converted = false;
/*     */ 
/*     */         
/* 137 */         if (requiredReturnType.isArray() && requiredReturnType.getComponentType() == actualReturnType) {
/* 138 */           Object array = Array.newInstance(requiredReturnType.getComponentType(), 1);
/* 139 */           Array.set(array, 0, attributeValue);
/* 140 */           attributes.put(attributeName, array);
/* 141 */           converted = true;
/*     */ 
/*     */         
/*     */         }
/* 145 */         else if (Annotation.class.isAssignableFrom(requiredReturnType) && Map.class
/* 146 */           .isAssignableFrom(actualReturnType)) {
/* 147 */           Class<? extends Annotation> nestedAnnotationType = (Class)requiredReturnType;
/*     */           
/* 149 */           Map<String, Object> map = (Map<String, Object>)attributeValue;
/* 150 */           attributes.put(attributeName, AnnotationUtils.synthesizeAnnotation(map, nestedAnnotationType, null));
/* 151 */           converted = true;
/*     */ 
/*     */         
/*     */         }
/* 155 */         else if (requiredReturnType.isArray() && actualReturnType.isArray() && Annotation.class
/* 156 */           .isAssignableFrom(requiredReturnType.getComponentType()) && Map.class
/* 157 */           .isAssignableFrom(actualReturnType.getComponentType())) {
/*     */           
/* 159 */           Class<? extends Annotation> nestedAnnotationType = (Class)requiredReturnType.getComponentType();
/* 160 */           Map[] arrayOfMap = (Map[])attributeValue;
/* 161 */           attributes.put(attributeName, AnnotationUtils.synthesizeAnnotationArray((Map<String, Object>[])arrayOfMap, nestedAnnotationType));
/* 162 */           converted = true;
/*     */         } 
/*     */         
/* 165 */         Assert.isTrue(converted, () -> String.format("Attributes map %s returned a value of type [%s] for attribute '%s', but a value of type [%s] is required as defined by annotation type [%s].", new Object[] { attributes, actualReturnType.getName(), attributeName, requiredReturnType.getName(), annotationType.getName() }));
/*     */       }  }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 173 */     return attributes;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/annotation/MapAnnotationAttributeExtractor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */