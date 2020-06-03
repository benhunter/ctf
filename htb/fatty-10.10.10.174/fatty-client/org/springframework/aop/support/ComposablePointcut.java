/*     */ package org.springframework.aop.support;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.springframework.aop.ClassFilter;
/*     */ import org.springframework.aop.MethodMatcher;
/*     */ import org.springframework.aop.Pointcut;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ComposablePointcut
/*     */   implements Pointcut, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -2743223737633663832L;
/*     */   private ClassFilter classFilter;
/*     */   private MethodMatcher methodMatcher;
/*     */   
/*     */   public ComposablePointcut() {
/*  55 */     this.classFilter = ClassFilter.TRUE;
/*  56 */     this.methodMatcher = MethodMatcher.TRUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ComposablePointcut(Pointcut pointcut) {
/*  64 */     Assert.notNull(pointcut, "Pointcut must not be null");
/*  65 */     this.classFilter = pointcut.getClassFilter();
/*  66 */     this.methodMatcher = pointcut.getMethodMatcher();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ComposablePointcut(ClassFilter classFilter) {
/*  75 */     Assert.notNull(classFilter, "ClassFilter must not be null");
/*  76 */     this.classFilter = classFilter;
/*  77 */     this.methodMatcher = MethodMatcher.TRUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ComposablePointcut(MethodMatcher methodMatcher) {
/*  86 */     Assert.notNull(methodMatcher, "MethodMatcher must not be null");
/*  87 */     this.classFilter = ClassFilter.TRUE;
/*  88 */     this.methodMatcher = methodMatcher;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ComposablePointcut(ClassFilter classFilter, MethodMatcher methodMatcher) {
/*  97 */     Assert.notNull(classFilter, "ClassFilter must not be null");
/*  98 */     Assert.notNull(methodMatcher, "MethodMatcher must not be null");
/*  99 */     this.classFilter = classFilter;
/* 100 */     this.methodMatcher = methodMatcher;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ComposablePointcut union(ClassFilter other) {
/* 110 */     this.classFilter = ClassFilters.union(this.classFilter, other);
/* 111 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ComposablePointcut intersection(ClassFilter other) {
/* 120 */     this.classFilter = ClassFilters.intersection(this.classFilter, other);
/* 121 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ComposablePointcut union(MethodMatcher other) {
/* 130 */     this.methodMatcher = MethodMatchers.union(this.methodMatcher, other);
/* 131 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ComposablePointcut intersection(MethodMatcher other) {
/* 140 */     this.methodMatcher = MethodMatchers.intersection(this.methodMatcher, other);
/* 141 */     return this;
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
/*     */   public ComposablePointcut union(Pointcut other) {
/* 154 */     this.methodMatcher = MethodMatchers.union(this.methodMatcher, this.classFilter, other
/* 155 */         .getMethodMatcher(), other.getClassFilter());
/* 156 */     this.classFilter = ClassFilters.union(this.classFilter, other.getClassFilter());
/* 157 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ComposablePointcut intersection(Pointcut other) {
/* 166 */     this.classFilter = ClassFilters.intersection(this.classFilter, other.getClassFilter());
/* 167 */     this.methodMatcher = MethodMatchers.intersection(this.methodMatcher, other.getMethodMatcher());
/* 168 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassFilter getClassFilter() {
/* 174 */     return this.classFilter;
/*     */   }
/*     */ 
/*     */   
/*     */   public MethodMatcher getMethodMatcher() {
/* 179 */     return this.methodMatcher;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 184 */     if (this == other) {
/* 185 */       return true;
/*     */     }
/* 187 */     if (!(other instanceof ComposablePointcut)) {
/* 188 */       return false;
/*     */     }
/* 190 */     ComposablePointcut otherPointcut = (ComposablePointcut)other;
/* 191 */     return (this.classFilter.equals(otherPointcut.classFilter) && this.methodMatcher
/* 192 */       .equals(otherPointcut.methodMatcher));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 197 */     return this.classFilter.hashCode() * 37 + this.methodMatcher.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 202 */     return "ComposablePointcut: " + this.classFilter + ", " + this.methodMatcher;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/support/ComposablePointcut.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */