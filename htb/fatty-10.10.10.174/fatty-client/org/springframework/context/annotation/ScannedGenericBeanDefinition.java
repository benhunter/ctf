/*    */ package org.springframework.context.annotation;
/*    */ 
/*    */ import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
/*    */ import org.springframework.beans.factory.support.GenericBeanDefinition;
/*    */ import org.springframework.core.type.AnnotationMetadata;
/*    */ import org.springframework.core.type.MethodMetadata;
/*    */ import org.springframework.core.type.classreading.MetadataReader;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ScannedGenericBeanDefinition
/*    */   extends GenericBeanDefinition
/*    */   implements AnnotatedBeanDefinition
/*    */ {
/*    */   private final AnnotationMetadata metadata;
/*    */   
/*    */   public ScannedGenericBeanDefinition(MetadataReader metadataReader) {
/* 60 */     Assert.notNull(metadataReader, "MetadataReader must not be null");
/* 61 */     this.metadata = metadataReader.getAnnotationMetadata();
/* 62 */     setBeanClassName(this.metadata.getClassName());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public final AnnotationMetadata getMetadata() {
/* 68 */     return this.metadata;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public MethodMetadata getFactoryMethodMetadata() {
/* 74 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/ScannedGenericBeanDefinition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */