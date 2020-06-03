/*     */ package org.springframework.core.type;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StandardAnnotationMetadata
/*     */   extends StandardClassMetadata
/*     */   implements AnnotationMetadata
/*     */ {
/*     */   private final Annotation[] annotations;
/*     */   private final boolean nestedAnnotationsAsMap;
/*     */   
/*     */   public StandardAnnotationMetadata(Class<?> introspectedClass) {
/*  54 */     this(introspectedClass, false);
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
/*     */   public StandardAnnotationMetadata(Class<?> introspectedClass, boolean nestedAnnotationsAsMap) {
/*  69 */     super(introspectedClass);
/*  70 */     this.annotations = introspectedClass.getAnnotations();
/*  71 */     this.nestedAnnotationsAsMap = nestedAnnotationsAsMap;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getAnnotationTypes() {
/*  77 */     Set<String> types = new LinkedHashSet<>();
/*  78 */     for (Annotation ann : this.annotations) {
/*  79 */       types.add(ann.annotationType().getName());
/*     */     }
/*  81 */     return types;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getMetaAnnotationTypes(String annotationName) {
/*  86 */     return (this.annotations.length > 0) ? 
/*  87 */       AnnotatedElementUtils.getMetaAnnotationTypes(getIntrospectedClass(), annotationName) : 
/*  88 */       Collections.<String>emptySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAnnotation(String annotationName) {
/*  93 */     for (Annotation ann : this.annotations) {
/*  94 */       if (ann.annotationType().getName().equals(annotationName)) {
/*  95 */         return true;
/*     */       }
/*     */     } 
/*  98 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasMetaAnnotation(String annotationName) {
/* 103 */     return (this.annotations.length > 0 && 
/* 104 */       AnnotatedElementUtils.hasMetaAnnotationTypes(getIntrospectedClass(), annotationName));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAnnotated(String annotationName) {
/* 109 */     return (this.annotations.length > 0 && 
/* 110 */       AnnotatedElementUtils.isAnnotated(getIntrospectedClass(), annotationName));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Object> getAnnotationAttributes(String annotationName) {
/* 115 */     return getAnnotationAttributes(annotationName, false);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Map<String, Object> getAnnotationAttributes(String annotationName, boolean classValuesAsString) {
/* 121 */     return (this.annotations.length > 0) ? (Map<String, Object>)AnnotatedElementUtils.getMergedAnnotationAttributes(
/* 122 */         getIntrospectedClass(), annotationName, classValuesAsString, this.nestedAnnotationsAsMap) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationName) {
/* 128 */     return getAllAnnotationAttributes(annotationName, false);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public MultiValueMap<String, Object> getAllAnnotationAttributes(String annotationName, boolean classValuesAsString) {
/* 134 */     return (this.annotations.length > 0) ? AnnotatedElementUtils.getAllAnnotationAttributes(
/* 135 */         getIntrospectedClass(), annotationName, classValuesAsString, this.nestedAnnotationsAsMap) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAnnotatedMethods(String annotationName) {
/*     */     try {
/* 141 */       Method[] methods = getIntrospectedClass().getDeclaredMethods();
/* 142 */       for (Method method : methods) {
/* 143 */         if (!method.isBridge() && (method.getAnnotations()).length > 0 && 
/* 144 */           AnnotatedElementUtils.isAnnotated(method, annotationName)) {
/* 145 */           return true;
/*     */         }
/*     */       } 
/* 148 */       return false;
/*     */     }
/* 150 */     catch (Throwable ex) {
/* 151 */       throw new IllegalStateException("Failed to introspect annotated methods on " + getIntrospectedClass(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<MethodMetadata> getAnnotatedMethods(String annotationName) {
/*     */     try {
/* 158 */       Method[] methods = getIntrospectedClass().getDeclaredMethods();
/* 159 */       Set<MethodMetadata> annotatedMethods = new LinkedHashSet<>(4);
/* 160 */       for (Method method : methods) {
/* 161 */         if (!method.isBridge() && (method.getAnnotations()).length > 0 && 
/* 162 */           AnnotatedElementUtils.isAnnotated(method, annotationName)) {
/* 163 */           annotatedMethods.add(new StandardMethodMetadata(method, this.nestedAnnotationsAsMap));
/*     */         }
/*     */       } 
/* 166 */       return annotatedMethods;
/*     */     }
/* 168 */     catch (Throwable ex) {
/* 169 */       throw new IllegalStateException("Failed to introspect annotated methods on " + getIntrospectedClass(), ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/type/StandardAnnotationMetadata.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */