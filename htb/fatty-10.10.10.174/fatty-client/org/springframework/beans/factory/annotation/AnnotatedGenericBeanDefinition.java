/*     */ package org.springframework.beans.factory.annotation;
/*     */ 
/*     */ import org.springframework.beans.factory.support.GenericBeanDefinition;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.core.type.MethodMetadata;
/*     */ import org.springframework.core.type.StandardAnnotationMetadata;
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
/*     */ public class AnnotatedGenericBeanDefinition
/*     */   extends GenericBeanDefinition
/*     */   implements AnnotatedBeanDefinition
/*     */ {
/*     */   private final AnnotationMetadata metadata;
/*     */   @Nullable
/*     */   private MethodMetadata factoryMethodMetadata;
/*     */   
/*     */   public AnnotatedGenericBeanDefinition(Class<?> beanClass) {
/*  57 */     setBeanClass(beanClass);
/*  58 */     this.metadata = (AnnotationMetadata)new StandardAnnotationMetadata(beanClass, true);
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
/*     */   public AnnotatedGenericBeanDefinition(AnnotationMetadata metadata) {
/*  72 */     Assert.notNull(metadata, "AnnotationMetadata must not be null");
/*  73 */     if (metadata instanceof StandardAnnotationMetadata) {
/*  74 */       setBeanClass(((StandardAnnotationMetadata)metadata).getIntrospectedClass());
/*     */     } else {
/*     */       
/*  77 */       setBeanClassName(metadata.getClassName());
/*     */     } 
/*  79 */     this.metadata = metadata;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotatedGenericBeanDefinition(AnnotationMetadata metadata, MethodMetadata factoryMethodMetadata) {
/*  90 */     this(metadata);
/*  91 */     Assert.notNull(factoryMethodMetadata, "MethodMetadata must not be null");
/*  92 */     setFactoryMethodName(factoryMethodMetadata.getMethodName());
/*  93 */     this.factoryMethodMetadata = factoryMethodMetadata;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final AnnotationMetadata getMetadata() {
/*  99 */     return this.metadata;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final MethodMetadata getFactoryMethodMetadata() {
/* 105 */     return this.factoryMethodMetadata;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/annotation/AnnotatedGenericBeanDefinition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */