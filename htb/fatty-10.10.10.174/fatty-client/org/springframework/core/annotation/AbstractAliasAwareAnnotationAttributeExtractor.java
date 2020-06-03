/*     */ package org.springframework.core.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractAliasAwareAnnotationAttributeExtractor<S>
/*     */   implements AnnotationAttributeExtractor<S>
/*     */ {
/*     */   private final Class<? extends Annotation> annotationType;
/*     */   @Nullable
/*     */   private final Object annotatedElement;
/*     */   private final S source;
/*     */   private final Map<String, List<String>> attributeAliasMap;
/*     */   
/*     */   AbstractAliasAwareAnnotationAttributeExtractor(Class<? extends Annotation> annotationType, @Nullable Object annotatedElement, S source) {
/*  62 */     Assert.notNull(annotationType, "annotationType must not be null");
/*  63 */     Assert.notNull(source, "source must not be null");
/*  64 */     this.annotationType = annotationType;
/*  65 */     this.annotatedElement = annotatedElement;
/*  66 */     this.source = source;
/*  67 */     this.attributeAliasMap = AnnotationUtils.getAttributeAliasMap(annotationType);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Class<? extends Annotation> getAnnotationType() {
/*  73 */     return this.annotationType;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final Object getAnnotatedElement() {
/*  79 */     return this.annotatedElement;
/*     */   }
/*     */ 
/*     */   
/*     */   public final S getSource() {
/*  84 */     return this.source;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final Object getAttributeValue(Method attributeMethod) {
/*  90 */     String attributeName = attributeMethod.getName();
/*  91 */     Object attributeValue = getRawAttributeValue(attributeMethod);
/*     */     
/*  93 */     List<String> aliasNames = this.attributeAliasMap.get(attributeName);
/*  94 */     if (aliasNames != null) {
/*  95 */       Object defaultValue = AnnotationUtils.getDefaultValue(this.annotationType, attributeName);
/*  96 */       for (String aliasName : aliasNames) {
/*  97 */         Object aliasValue = getRawAttributeValue(aliasName);
/*     */         
/*  99 */         if (!ObjectUtils.nullSafeEquals(attributeValue, aliasValue) && 
/* 100 */           !ObjectUtils.nullSafeEquals(attributeValue, defaultValue) && 
/* 101 */           !ObjectUtils.nullSafeEquals(aliasValue, defaultValue)) {
/* 102 */           String elementName = (this.annotatedElement != null) ? this.annotatedElement.toString() : "unknown element";
/* 103 */           throw new AnnotationConfigurationException(String.format("In annotation [%s] declared on %s and synthesized from [%s], attribute '%s' and its alias '%s' are present with values of [%s] and [%s], but only one is permitted.", new Object[] { this.annotationType
/*     */ 
/*     */                   
/* 106 */                   .getName(), elementName, this.source, attributeName, aliasName, 
/* 107 */                   ObjectUtils.nullSafeToString(attributeValue), ObjectUtils.nullSafeToString(aliasValue) }));
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 112 */         if (ObjectUtils.nullSafeEquals(attributeValue, defaultValue)) {
/* 113 */           attributeValue = aliasValue;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 118 */     return attributeValue;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected abstract Object getRawAttributeValue(Method paramMethod);
/*     */   
/*     */   @Nullable
/*     */   protected abstract Object getRawAttributeValue(String paramString);
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/annotation/AbstractAliasAwareAnnotationAttributeExtractor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */