/*     */ package org.springframework.core.type.filter;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.Inherited;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.core.type.classreading.MetadataReader;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnnotationTypeFilter
/*     */   extends AbstractTypeHierarchyTraversingFilter
/*     */ {
/*     */   private final Class<? extends Annotation> annotationType;
/*     */   private final boolean considerMetaAnnotations;
/*     */   
/*     */   public AnnotationTypeFilter(Class<? extends Annotation> annotationType) {
/*  55 */     this(annotationType, true, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationTypeFilter(Class<? extends Annotation> annotationType, boolean considerMetaAnnotations) {
/*  65 */     this(annotationType, considerMetaAnnotations, false);
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
/*     */   public AnnotationTypeFilter(Class<? extends Annotation> annotationType, boolean considerMetaAnnotations, boolean considerInterfaces) {
/*  77 */     super(annotationType.isAnnotationPresent((Class)Inherited.class), considerInterfaces);
/*  78 */     this.annotationType = annotationType;
/*  79 */     this.considerMetaAnnotations = considerMetaAnnotations;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Class<? extends Annotation> getAnnotationType() {
/*  88 */     return this.annotationType;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean matchSelf(MetadataReader metadataReader) {
/*  93 */     AnnotationMetadata metadata = metadataReader.getAnnotationMetadata();
/*  94 */     return (metadata.hasAnnotation(this.annotationType.getName()) || (this.considerMetaAnnotations && metadata
/*  95 */       .hasMetaAnnotation(this.annotationType.getName())));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Boolean matchSuperClass(String superClassName) {
/* 101 */     return hasAnnotation(superClassName);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Boolean matchInterface(String interfaceName) {
/* 107 */     return hasAnnotation(interfaceName);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected Boolean hasAnnotation(String typeName) {
/* 112 */     if (Object.class.getName().equals(typeName)) {
/* 113 */       return Boolean.valueOf(false);
/*     */     }
/* 115 */     if (typeName.startsWith("java")) {
/* 116 */       if (!this.annotationType.getName().startsWith("java"))
/*     */       {
/*     */         
/* 119 */         return Boolean.valueOf(false);
/*     */       }
/*     */       try {
/* 122 */         Class<?> clazz = ClassUtils.forName(typeName, getClass().getClassLoader());
/* 123 */         return Boolean.valueOf(
/* 124 */             ((this.considerMetaAnnotations ? AnnotationUtils.getAnnotation(clazz, this.annotationType) : clazz.<Annotation>getAnnotation(this.annotationType)) != null));
/*     */       }
/* 126 */       catch (Throwable throwable) {}
/*     */     } 
/*     */ 
/*     */     
/* 130 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/type/filter/AnnotationTypeFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */