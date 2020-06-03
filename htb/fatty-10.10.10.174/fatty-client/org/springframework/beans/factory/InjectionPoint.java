/*     */ package org.springframework.beans.factory;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import org.springframework.core.MethodParameter;
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
/*     */ public class InjectionPoint
/*     */ {
/*     */   @Nullable
/*     */   protected MethodParameter methodParameter;
/*     */   @Nullable
/*     */   protected Field field;
/*     */   @Nullable
/*     */   private volatile Annotation[] fieldAnnotations;
/*     */   
/*     */   public InjectionPoint(MethodParameter methodParameter) {
/*  57 */     Assert.notNull(methodParameter, "MethodParameter must not be null");
/*  58 */     this.methodParameter = methodParameter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InjectionPoint(Field field) {
/*  66 */     Assert.notNull(field, "Field must not be null");
/*  67 */     this.field = field;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected InjectionPoint(InjectionPoint original) {
/*  75 */     this.methodParameter = (original.methodParameter != null) ? new MethodParameter(original.methodParameter) : null;
/*     */     
/*  77 */     this.field = original.field;
/*  78 */     this.fieldAnnotations = original.fieldAnnotations;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected InjectionPoint() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public MethodParameter getMethodParameter() {
/*  95 */     return this.methodParameter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Field getField() {
/* 105 */     return this.field;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final MethodParameter obtainMethodParameter() {
/* 115 */     Assert.state((this.methodParameter != null), "Neither Field nor MethodParameter");
/* 116 */     return this.methodParameter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Annotation[] getAnnotations() {
/* 123 */     if (this.field != null) {
/* 124 */       Annotation[] fieldAnnotations = this.fieldAnnotations;
/* 125 */       if (fieldAnnotations == null) {
/* 126 */         fieldAnnotations = this.field.getAnnotations();
/* 127 */         this.fieldAnnotations = fieldAnnotations;
/*     */       } 
/* 129 */       return fieldAnnotations;
/*     */     } 
/*     */     
/* 132 */     return obtainMethodParameter().getParameterAnnotations();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
/* 144 */     return (this.field != null) ? this.field.<A>getAnnotation(annotationType) : (A)
/* 145 */       obtainMethodParameter().getParameterAnnotation(annotationType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getDeclaredType() {
/* 153 */     return (this.field != null) ? this.field.getType() : obtainMethodParameter().getParameterType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Member getMember() {
/* 161 */     return (this.field != null) ? this.field : obtainMethodParameter().getMember();
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
/*     */   public AnnotatedElement getAnnotatedElement() {
/* 174 */     return (this.field != null) ? this.field : obtainMethodParameter().getAnnotatedElement();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 180 */     if (this == other) {
/* 181 */       return true;
/*     */     }
/* 183 */     if (other == null || getClass() != other.getClass()) {
/* 184 */       return false;
/*     */     }
/* 186 */     InjectionPoint otherPoint = (InjectionPoint)other;
/* 187 */     return (ObjectUtils.nullSafeEquals(this.field, otherPoint.field) && 
/* 188 */       ObjectUtils.nullSafeEquals(this.methodParameter, otherPoint.methodParameter));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 193 */     return (this.field != null) ? this.field.hashCode() : ObjectUtils.nullSafeHashCode(this.methodParameter);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 198 */     return (this.field != null) ? ("field '" + this.field.getName() + "'") : String.valueOf(this.methodParameter);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/InjectionPoint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */