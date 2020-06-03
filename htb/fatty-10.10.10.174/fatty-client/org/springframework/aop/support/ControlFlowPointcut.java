/*     */ package org.springframework.aop.support;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Method;
/*     */ import org.springframework.aop.ClassFilter;
/*     */ import org.springframework.aop.MethodMatcher;
/*     */ import org.springframework.aop.Pointcut;
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
/*     */ public class ControlFlowPointcut
/*     */   implements Pointcut, ClassFilter, MethodMatcher, Serializable
/*     */ {
/*     */   private Class<?> clazz;
/*     */   @Nullable
/*     */   private String methodName;
/*     */   private volatile int evaluations;
/*     */   
/*     */   public ControlFlowPointcut(Class<?> clazz) {
/*  54 */     this(clazz, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ControlFlowPointcut(Class<?> clazz, @Nullable String methodName) {
/*  65 */     Assert.notNull(clazz, "Class must not be null");
/*  66 */     this.clazz = clazz;
/*  67 */     this.methodName = methodName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(Class<?> clazz) {
/*  76 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(Method method, Class<?> targetClass) {
/*  84 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRuntime() {
/*  89 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(Method method, Class<?> targetClass, Object... args) {
/*  94 */     this.evaluations++;
/*     */     
/*  96 */     for (StackTraceElement element : (new Throwable()).getStackTrace()) {
/*  97 */       if (element.getClassName().equals(this.clazz.getName()) && (this.methodName == null || element
/*  98 */         .getMethodName().equals(this.methodName))) {
/*  99 */         return true;
/*     */       }
/*     */     } 
/* 102 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEvaluations() {
/* 109 */     return this.evaluations;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassFilter getClassFilter() {
/* 115 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public MethodMatcher getMethodMatcher() {
/* 120 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 126 */     if (this == other) {
/* 127 */       return true;
/*     */     }
/* 129 */     if (!(other instanceof ControlFlowPointcut)) {
/* 130 */       return false;
/*     */     }
/* 132 */     ControlFlowPointcut that = (ControlFlowPointcut)other;
/* 133 */     return (this.clazz.equals(that.clazz) && ObjectUtils.nullSafeEquals(this.methodName, that.methodName));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 138 */     int code = this.clazz.hashCode();
/* 139 */     if (this.methodName != null) {
/* 140 */       code = 37 * code + this.methodName.hashCode();
/*     */     }
/* 142 */     return code;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/support/ControlFlowPointcut.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */