/*     */ package org.springframework.aop.support.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import org.springframework.aop.ClassFilter;
/*     */ import org.springframework.aop.MethodMatcher;
/*     */ import org.springframework.aop.Pointcut;
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
/*     */ public class AnnotationMatchingPointcut
/*     */   implements Pointcut
/*     */ {
/*     */   private final ClassFilter classFilter;
/*     */   private final MethodMatcher methodMatcher;
/*     */   
/*     */   public AnnotationMatchingPointcut(Class<? extends Annotation> classAnnotationType) {
/*  49 */     this(classAnnotationType, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationMatchingPointcut(Class<? extends Annotation> classAnnotationType, boolean checkInherited) {
/*  60 */     this.classFilter = new AnnotationClassFilter(classAnnotationType, checkInherited);
/*  61 */     this.methodMatcher = MethodMatcher.TRUE;
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
/*     */   public AnnotationMatchingPointcut(@Nullable Class<? extends Annotation> classAnnotationType, @Nullable Class<? extends Annotation> methodAnnotationType) {
/*  74 */     this(classAnnotationType, methodAnnotationType, false);
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
/*     */   public AnnotationMatchingPointcut(@Nullable Class<? extends Annotation> classAnnotationType, @Nullable Class<? extends Annotation> methodAnnotationType, boolean checkInherited) {
/*  92 */     Assert.isTrue((classAnnotationType != null || methodAnnotationType != null), "Either Class annotation type or Method annotation type needs to be specified (or both)");
/*     */ 
/*     */     
/*  95 */     if (classAnnotationType != null) {
/*  96 */       this.classFilter = new AnnotationClassFilter(classAnnotationType, checkInherited);
/*     */     } else {
/*     */       
/*  99 */       this.classFilter = ClassFilter.TRUE;
/*     */     } 
/*     */     
/* 102 */     if (methodAnnotationType != null) {
/* 103 */       this.methodMatcher = (MethodMatcher)new AnnotationMethodMatcher(methodAnnotationType, checkInherited);
/*     */     } else {
/*     */       
/* 106 */       this.methodMatcher = MethodMatcher.TRUE;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassFilter getClassFilter() {
/* 113 */     return this.classFilter;
/*     */   }
/*     */ 
/*     */   
/*     */   public MethodMatcher getMethodMatcher() {
/* 118 */     return this.methodMatcher;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 123 */     if (this == other) {
/* 124 */       return true;
/*     */     }
/* 126 */     if (!(other instanceof AnnotationMatchingPointcut)) {
/* 127 */       return false;
/*     */     }
/* 129 */     AnnotationMatchingPointcut otherPointcut = (AnnotationMatchingPointcut)other;
/* 130 */     return (this.classFilter.equals(otherPointcut.classFilter) && this.methodMatcher
/* 131 */       .equals(otherPointcut.methodMatcher));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 136 */     return this.classFilter.hashCode() * 37 + this.methodMatcher.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 141 */     return "AnnotationMatchingPointcut: " + this.classFilter + ", " + this.methodMatcher;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static AnnotationMatchingPointcut forClassAnnotation(Class<? extends Annotation> annotationType) {
/* 152 */     Assert.notNull(annotationType, "Annotation type must not be null");
/* 153 */     return new AnnotationMatchingPointcut(annotationType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static AnnotationMatchingPointcut forMethodAnnotation(Class<? extends Annotation> annotationType) {
/* 163 */     Assert.notNull(annotationType, "Annotation type must not be null");
/* 164 */     return new AnnotationMatchingPointcut(null, annotationType);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/support/annotation/AnnotationMatchingPointcut.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */