/*    */ package org.springframework.aop.support.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import org.springframework.aop.ClassFilter;
/*    */ import org.springframework.core.annotation.AnnotatedElementUtils;
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
/*    */ public class AnnotationClassFilter
/*    */   implements ClassFilter
/*    */ {
/*    */   private final Class<? extends Annotation> annotationType;
/*    */   private final boolean checkInherited;
/*    */   
/*    */   public AnnotationClassFilter(Class<? extends Annotation> annotationType) {
/* 45 */     this(annotationType, false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AnnotationClassFilter(Class<? extends Annotation> annotationType, boolean checkInherited) {
/* 57 */     Assert.notNull(annotationType, "Annotation type must not be null");
/* 58 */     this.annotationType = annotationType;
/* 59 */     this.checkInherited = checkInherited;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(Class<?> clazz) {
/* 65 */     return this.checkInherited ? AnnotatedElementUtils.hasAnnotation(clazz, this.annotationType) : clazz
/* 66 */       .isAnnotationPresent(this.annotationType);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object other) {
/* 71 */     if (this == other) {
/* 72 */       return true;
/*    */     }
/* 74 */     if (!(other instanceof AnnotationClassFilter)) {
/* 75 */       return false;
/*    */     }
/* 77 */     AnnotationClassFilter otherCf = (AnnotationClassFilter)other;
/* 78 */     return (this.annotationType.equals(otherCf.annotationType) && this.checkInherited == otherCf.checkInherited);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 83 */     return this.annotationType.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 88 */     return getClass().getName() + ": " + this.annotationType;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/support/annotation/AnnotationClassFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */