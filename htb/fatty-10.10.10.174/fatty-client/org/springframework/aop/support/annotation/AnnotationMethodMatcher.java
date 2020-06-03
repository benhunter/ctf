/*     */ package org.springframework.aop.support.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.aop.support.StaticMethodMatcher;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
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
/*     */ public class AnnotationMethodMatcher
/*     */   extends StaticMethodMatcher
/*     */ {
/*     */   private final Class<? extends Annotation> annotationType;
/*     */   private final boolean checkInherited;
/*     */   
/*     */   public AnnotationMethodMatcher(Class<? extends Annotation> annotationType) {
/*  49 */     this(annotationType, false);
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
/*     */   public AnnotationMethodMatcher(Class<? extends Annotation> annotationType, boolean checkInherited) {
/*  62 */     Assert.notNull(annotationType, "Annotation type must not be null");
/*  63 */     this.annotationType = annotationType;
/*  64 */     this.checkInherited = checkInherited;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(Method method, Class<?> targetClass) {
/*  71 */     if (matchesMethod(method)) {
/*  72 */       return true;
/*     */     }
/*     */     
/*  75 */     if (Proxy.isProxyClass(targetClass)) {
/*  76 */       return false;
/*     */     }
/*     */     
/*  79 */     Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
/*  80 */     return (specificMethod != method && matchesMethod(specificMethod));
/*     */   }
/*     */   
/*     */   private boolean matchesMethod(Method method) {
/*  84 */     return this.checkInherited ? AnnotatedElementUtils.hasAnnotation(method, this.annotationType) : method
/*  85 */       .isAnnotationPresent(this.annotationType);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/*  90 */     if (this == other) {
/*  91 */       return true;
/*     */     }
/*  93 */     if (!(other instanceof AnnotationMethodMatcher)) {
/*  94 */       return false;
/*     */     }
/*  96 */     AnnotationMethodMatcher otherMm = (AnnotationMethodMatcher)other;
/*  97 */     return this.annotationType.equals(otherMm.annotationType);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 102 */     return this.annotationType.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 107 */     return getClass().getName() + ": " + this.annotationType;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/support/annotation/AnnotationMethodMatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */