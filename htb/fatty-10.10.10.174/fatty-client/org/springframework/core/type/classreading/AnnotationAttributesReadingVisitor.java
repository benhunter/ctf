/*     */ package org.springframework.core.type.classreading;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ final class AnnotationAttributesReadingVisitor
/*     */   extends RecursiveAnnotationAttributesVisitor
/*     */ {
/*     */   private final MultiValueMap<String, AnnotationAttributes> attributesMap;
/*     */   private final Map<String, Set<String>> metaAnnotationMap;
/*     */   
/*     */   public AnnotationAttributesReadingVisitor(String annotationType, MultiValueMap<String, AnnotationAttributes> attributesMap, Map<String, Set<String>> metaAnnotationMap, @Nullable ClassLoader classLoader) {
/*  56 */     super(annotationType, new AnnotationAttributes(annotationType, classLoader), classLoader);
/*  57 */     this.attributesMap = attributesMap;
/*  58 */     this.metaAnnotationMap = metaAnnotationMap;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitEnd() {
/*  64 */     super.visitEnd();
/*     */     
/*  66 */     Class<? extends Annotation> annotationClass = this.attributes.annotationType();
/*  67 */     if (annotationClass != null) {
/*  68 */       List<AnnotationAttributes> attributeList = (List<AnnotationAttributes>)this.attributesMap.get(this.annotationType);
/*  69 */       if (attributeList == null) {
/*  70 */         this.attributesMap.add(this.annotationType, this.attributes);
/*     */       } else {
/*     */         
/*  73 */         attributeList.add(0, this.attributes);
/*     */       } 
/*  75 */       if (!AnnotationUtils.isInJavaLangAnnotationPackage(annotationClass.getName())) {
/*     */         try {
/*  77 */           Annotation[] metaAnnotations = annotationClass.getAnnotations();
/*  78 */           if (!ObjectUtils.isEmpty((Object[])metaAnnotations)) {
/*  79 */             Set<Annotation> visited = new LinkedHashSet<>();
/*  80 */             for (Annotation metaAnnotation : metaAnnotations) {
/*  81 */               recursivelyCollectMetaAnnotations(visited, metaAnnotation);
/*     */             }
/*  83 */             if (!visited.isEmpty()) {
/*  84 */               Set<String> metaAnnotationTypeNames = new LinkedHashSet<>(visited.size());
/*  85 */               for (Annotation ann : visited) {
/*  86 */                 metaAnnotationTypeNames.add(ann.annotationType().getName());
/*     */               }
/*  88 */               this.metaAnnotationMap.put(annotationClass.getName(), metaAnnotationTypeNames);
/*     */             }
/*     */           
/*     */           } 
/*  92 */         } catch (Throwable ex) {
/*  93 */           if (this.logger.isDebugEnabled()) {
/*  94 */             this.logger.debug("Failed to introspect meta-annotations on " + annotationClass + ": " + ex);
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void recursivelyCollectMetaAnnotations(Set<Annotation> visited, Annotation annotation) {
/* 102 */     Class<? extends Annotation> annotationType = annotation.annotationType();
/* 103 */     String annotationName = annotationType.getName();
/* 104 */     if (!AnnotationUtils.isInJavaLangAnnotationPackage(annotationName) && visited.add(annotation))
/*     */       
/*     */       try {
/*     */ 
/*     */         
/* 109 */         if (Modifier.isPublic(annotationType.getModifiers())) {
/* 110 */           this.attributesMap.add(annotationName, 
/* 111 */               AnnotationUtils.getAnnotationAttributes(annotation, false, true));
/*     */         }
/* 113 */         for (Annotation metaMetaAnnotation : annotationType.getAnnotations()) {
/* 114 */           recursivelyCollectMetaAnnotations(visited, metaMetaAnnotation);
/*     */         }
/*     */       }
/* 117 */       catch (Throwable ex) {
/* 118 */         if (this.logger.isDebugEnabled())
/* 119 */           this.logger.debug("Failed to introspect meta-annotations on " + annotation + ": " + ex); 
/*     */       }  
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/type/classreading/AnnotationAttributesReadingVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */