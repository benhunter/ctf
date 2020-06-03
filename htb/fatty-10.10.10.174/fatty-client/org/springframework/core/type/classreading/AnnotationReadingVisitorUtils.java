/*     */ package org.springframework.core.type.classreading;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AnnotationReadingVisitorUtils
/*     */ {
/*     */   public static AnnotationAttributes convertClassValues(Object annotatedElement, @Nullable ClassLoader classLoader, AnnotationAttributes original, boolean classValuesAsString) {
/*  50 */     AnnotationAttributes result = new AnnotationAttributes(original);
/*  51 */     AnnotationUtils.postProcessAnnotationAttributes(annotatedElement, result, classValuesAsString);
/*     */     
/*  53 */     for (Map.Entry<String, Object> entry : (Iterable<Map.Entry<String, Object>>)result.entrySet()) {
/*     */       try {
/*  55 */         Object value = entry.getValue();
/*  56 */         if (value instanceof AnnotationAttributes) {
/*  57 */           value = convertClassValues(annotatedElement, classLoader, (AnnotationAttributes)value, classValuesAsString);
/*     */         
/*     */         }
/*  60 */         else if (value instanceof AnnotationAttributes[]) {
/*  61 */           AnnotationAttributes[] values = (AnnotationAttributes[])value;
/*  62 */           for (int i = 0; i < values.length; i++) {
/*  63 */             values[i] = convertClassValues(annotatedElement, classLoader, values[i], classValuesAsString);
/*     */           }
/*  65 */           value = values;
/*     */         }
/*  67 */         else if (value instanceof Type) {
/*     */           
/*  69 */           value = classValuesAsString ? ((Type)value).getClassName() : ClassUtils.forName(((Type)value).getClassName(), classLoader);
/*     */         }
/*  71 */         else if (value instanceof Type[]) {
/*  72 */           Type[] array = (Type[])value;
/*  73 */           Object[] convArray = classValuesAsString ? (Object[])new String[array.length] : (Object[])new Class[array.length];
/*     */           
/*  75 */           for (int i = 0; i < array.length; i++) {
/*  76 */             convArray[i] = classValuesAsString ? array[i].getClassName() : 
/*  77 */               ClassUtils.forName(array[i].getClassName(), classLoader);
/*     */           }
/*  79 */           value = convArray;
/*     */         }
/*  81 */         else if (classValuesAsString) {
/*  82 */           if (value instanceof Class) {
/*  83 */             value = ((Class)value).getName();
/*     */           }
/*  85 */           else if (value instanceof Class[]) {
/*  86 */             Class<?>[] clazzArray = (Class[])value;
/*  87 */             String[] newValue = new String[clazzArray.length];
/*  88 */             for (int i = 0; i < clazzArray.length; i++) {
/*  89 */               newValue[i] = clazzArray[i].getName();
/*     */             }
/*  91 */             value = newValue;
/*     */           } 
/*     */         } 
/*  94 */         entry.setValue(value);
/*     */       }
/*  96 */       catch (Throwable ex) {
/*     */         
/*  98 */         result.put(entry.getKey(), ex);
/*     */       } 
/*     */     } 
/*     */     
/* 102 */     return result;
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
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static AnnotationAttributes getMergedAnnotationAttributes(LinkedMultiValueMap<String, AnnotationAttributes> attributesMap, Map<String, Set<String>> metaAnnotationMap, String annotationName) {
/* 127 */     List<AnnotationAttributes> attributesList = attributesMap.get(annotationName);
/* 128 */     if (CollectionUtils.isEmpty(attributesList)) {
/* 129 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 135 */     AnnotationAttributes result = new AnnotationAttributes(attributesList.get(0));
/*     */     
/* 137 */     Set<String> overridableAttributeNames = new HashSet<>(result.keySet());
/* 138 */     overridableAttributeNames.remove("value");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 143 */     List<String> annotationTypes = new ArrayList<>(attributesMap.keySet());
/* 144 */     Collections.reverse(annotationTypes);
/*     */ 
/*     */     
/* 147 */     annotationTypes.remove(annotationName);
/*     */     
/* 149 */     for (String currentAnnotationType : annotationTypes) {
/* 150 */       List<AnnotationAttributes> currentAttributesList = attributesMap.get(currentAnnotationType);
/* 151 */       if (!ObjectUtils.isEmpty(currentAttributesList)) {
/* 152 */         Set<String> metaAnns = metaAnnotationMap.get(currentAnnotationType);
/* 153 */         if (metaAnns != null && metaAnns.contains(annotationName)) {
/* 154 */           AnnotationAttributes currentAttributes = currentAttributesList.get(0);
/* 155 */           for (String overridableAttributeName : overridableAttributeNames) {
/* 156 */             Object value = currentAttributes.get(overridableAttributeName);
/* 157 */             if (value != null)
/*     */             {
/*     */               
/* 160 */               result.put(overridableAttributeName, value);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 167 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/type/classreading/AnnotationReadingVisitorUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */