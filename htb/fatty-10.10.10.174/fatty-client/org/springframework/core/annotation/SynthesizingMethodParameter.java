/*     */ package org.springframework.core.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Executable;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Parameter;
/*     */ import org.springframework.core.MethodParameter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SynthesizingMethodParameter
/*     */   extends MethodParameter
/*     */ {
/*     */   public SynthesizingMethodParameter(Method method, int parameterIndex) {
/*  48 */     super(method, parameterIndex);
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
/*     */   public SynthesizingMethodParameter(Method method, int parameterIndex, int nestingLevel) {
/*  62 */     super(method, parameterIndex, nestingLevel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SynthesizingMethodParameter(Constructor<?> constructor, int parameterIndex) {
/*  72 */     super(constructor, parameterIndex);
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
/*     */   public SynthesizingMethodParameter(Constructor<?> constructor, int parameterIndex, int nestingLevel) {
/*  84 */     super(constructor, parameterIndex, nestingLevel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SynthesizingMethodParameter(SynthesizingMethodParameter original) {
/*  93 */     super(original);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected <A extends Annotation> A adaptAnnotation(A annotation) {
/*  99 */     return AnnotationUtils.synthesizeAnnotation(annotation, getAnnotatedElement());
/*     */   }
/*     */ 
/*     */   
/*     */   protected Annotation[] adaptAnnotationArray(Annotation[] annotations) {
/* 104 */     return AnnotationUtils.synthesizeAnnotationArray(annotations, getAnnotatedElement());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SynthesizingMethodParameter clone() {
/* 110 */     return new SynthesizingMethodParameter(this);
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
/*     */   public static SynthesizingMethodParameter forExecutable(Executable executable, int parameterIndex) {
/* 124 */     if (executable instanceof Method) {
/* 125 */       return new SynthesizingMethodParameter((Method)executable, parameterIndex);
/*     */     }
/* 127 */     if (executable instanceof Constructor) {
/* 128 */       return new SynthesizingMethodParameter((Constructor)executable, parameterIndex);
/*     */     }
/*     */     
/* 131 */     throw new IllegalArgumentException("Not a Method/Constructor: " + executable);
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
/*     */   public static SynthesizingMethodParameter forParameter(Parameter parameter) {
/* 144 */     return forExecutable(parameter.getDeclaringExecutable(), findParameterIndex(parameter));
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/annotation/SynthesizingMethodParameter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */