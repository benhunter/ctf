/*     */ package org.springframework.aop.support;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Method;
/*     */ import org.springframework.aop.ClassFilter;
/*     */ import org.springframework.aop.IntroductionAwareMethodMatcher;
/*     */ import org.springframework.aop.MethodMatcher;
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
/*     */ public abstract class MethodMatchers
/*     */ {
/*     */   public static MethodMatcher union(MethodMatcher mm1, MethodMatcher mm2) {
/*  51 */     return (mm1 instanceof IntroductionAwareMethodMatcher || mm2 instanceof IntroductionAwareMethodMatcher) ? new UnionIntroductionAwareMethodMatcher(mm1, mm2) : new UnionMethodMatcher(mm1, mm2);
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
/*     */   static MethodMatcher union(MethodMatcher mm1, ClassFilter cf1, MethodMatcher mm2, ClassFilter cf2) {
/*  65 */     return (mm1 instanceof IntroductionAwareMethodMatcher || mm2 instanceof IntroductionAwareMethodMatcher) ? new ClassFilterAwareUnionIntroductionAwareMethodMatcher(mm1, cf1, mm2, cf2) : new ClassFilterAwareUnionMethodMatcher(mm1, cf1, mm2, cf2);
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
/*     */   public static MethodMatcher intersection(MethodMatcher mm1, MethodMatcher mm2) {
/*  78 */     return (mm1 instanceof IntroductionAwareMethodMatcher || mm2 instanceof IntroductionAwareMethodMatcher) ? new IntersectionIntroductionAwareMethodMatcher(mm1, mm2) : new IntersectionMethodMatcher(mm1, mm2);
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
/*     */   public static boolean matches(MethodMatcher mm, Method method, Class<?> targetClass, boolean hasIntroductions) {
/*  94 */     Assert.notNull(mm, "MethodMatcher must not be null");
/*  95 */     return (mm instanceof IntroductionAwareMethodMatcher) ? ((IntroductionAwareMethodMatcher)mm)
/*  96 */       .matches(method, targetClass, hasIntroductions) : mm
/*  97 */       .matches(method, targetClass);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class UnionMethodMatcher
/*     */     implements MethodMatcher, Serializable
/*     */   {
/*     */     protected final MethodMatcher mm1;
/*     */ 
/*     */     
/*     */     protected final MethodMatcher mm2;
/*     */ 
/*     */     
/*     */     public UnionMethodMatcher(MethodMatcher mm1, MethodMatcher mm2) {
/* 112 */       Assert.notNull(mm1, "First MethodMatcher must not be null");
/* 113 */       Assert.notNull(mm2, "Second MethodMatcher must not be null");
/* 114 */       this.mm1 = mm1;
/* 115 */       this.mm2 = mm2;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Method method, Class<?> targetClass) {
/* 120 */       return ((matchesClass1(targetClass) && this.mm1.matches(method, targetClass)) || (
/* 121 */         matchesClass2(targetClass) && this.mm2.matches(method, targetClass)));
/*     */     }
/*     */     
/*     */     protected boolean matchesClass1(Class<?> targetClass) {
/* 125 */       return true;
/*     */     }
/*     */     
/*     */     protected boolean matchesClass2(Class<?> targetClass) {
/* 129 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isRuntime() {
/* 134 */       return (this.mm1.isRuntime() || this.mm2.isRuntime());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Method method, Class<?> targetClass, Object... args) {
/* 139 */       return (this.mm1.matches(method, targetClass, args) || this.mm2.matches(method, targetClass, args));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 144 */       if (this == other) {
/* 145 */         return true;
/*     */       }
/* 147 */       if (!(other instanceof UnionMethodMatcher)) {
/* 148 */         return false;
/*     */       }
/* 150 */       UnionMethodMatcher that = (UnionMethodMatcher)other;
/* 151 */       return (this.mm1.equals(that.mm1) && this.mm2.equals(that.mm2));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 156 */       return 37 * this.mm1.hashCode() + this.mm2.hashCode();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class UnionIntroductionAwareMethodMatcher
/*     */     extends UnionMethodMatcher
/*     */     implements IntroductionAwareMethodMatcher
/*     */   {
/*     */     public UnionIntroductionAwareMethodMatcher(MethodMatcher mm1, MethodMatcher mm2) {
/* 171 */       super(mm1, mm2);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Method method, Class<?> targetClass, boolean hasIntroductions) {
/* 176 */       return ((matchesClass1(targetClass) && MethodMatchers.matches(this.mm1, method, targetClass, hasIntroductions)) || (
/* 177 */         matchesClass2(targetClass) && MethodMatchers.matches(this.mm2, method, targetClass, hasIntroductions)));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ClassFilterAwareUnionMethodMatcher
/*     */     extends UnionMethodMatcher
/*     */   {
/*     */     private final ClassFilter cf1;
/*     */ 
/*     */     
/*     */     private final ClassFilter cf2;
/*     */ 
/*     */ 
/*     */     
/*     */     public ClassFilterAwareUnionMethodMatcher(MethodMatcher mm1, ClassFilter cf1, MethodMatcher mm2, ClassFilter cf2) {
/* 194 */       super(mm1, mm2);
/* 195 */       this.cf1 = cf1;
/* 196 */       this.cf2 = cf2;
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean matchesClass1(Class<?> targetClass) {
/* 201 */       return this.cf1.matches(targetClass);
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean matchesClass2(Class<?> targetClass) {
/* 206 */       return this.cf2.matches(targetClass);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 211 */       if (this == other) {
/* 212 */         return true;
/*     */       }
/* 214 */       if (!super.equals(other)) {
/* 215 */         return false;
/*     */       }
/* 217 */       ClassFilter otherCf1 = ClassFilter.TRUE;
/* 218 */       ClassFilter otherCf2 = ClassFilter.TRUE;
/* 219 */       if (other instanceof ClassFilterAwareUnionMethodMatcher) {
/* 220 */         ClassFilterAwareUnionMethodMatcher cfa = (ClassFilterAwareUnionMethodMatcher)other;
/* 221 */         otherCf1 = cfa.cf1;
/* 222 */         otherCf2 = cfa.cf2;
/*     */       } 
/* 224 */       return (this.cf1.equals(otherCf1) && this.cf2.equals(otherCf2));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 230 */       return super.hashCode();
/*     */     }
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
/*     */   private static class ClassFilterAwareUnionIntroductionAwareMethodMatcher
/*     */     extends ClassFilterAwareUnionMethodMatcher
/*     */     implements IntroductionAwareMethodMatcher
/*     */   {
/*     */     public ClassFilterAwareUnionIntroductionAwareMethodMatcher(MethodMatcher mm1, ClassFilter cf1, MethodMatcher mm2, ClassFilter cf2) {
/* 248 */       super(mm1, cf1, mm2, cf2);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Method method, Class<?> targetClass, boolean hasIntroductions) {
/* 253 */       return ((matchesClass1(targetClass) && MethodMatchers.matches(this.mm1, method, targetClass, hasIntroductions)) || (
/* 254 */         matchesClass2(targetClass) && MethodMatchers.matches(this.mm2, method, targetClass, hasIntroductions)));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class IntersectionMethodMatcher
/*     */     implements MethodMatcher, Serializable
/*     */   {
/*     */     protected final MethodMatcher mm1;
/*     */ 
/*     */     
/*     */     protected final MethodMatcher mm2;
/*     */ 
/*     */     
/*     */     public IntersectionMethodMatcher(MethodMatcher mm1, MethodMatcher mm2) {
/* 270 */       Assert.notNull(mm1, "First MethodMatcher must not be null");
/* 271 */       Assert.notNull(mm2, "Second MethodMatcher must not be null");
/* 272 */       this.mm1 = mm1;
/* 273 */       this.mm2 = mm2;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Method method, Class<?> targetClass) {
/* 278 */       return (this.mm1.matches(method, targetClass) && this.mm2.matches(method, targetClass));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isRuntime() {
/* 283 */       return (this.mm1.isRuntime() || this.mm2.isRuntime());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean matches(Method method, Class<?> targetClass, Object... args) {
/* 292 */       boolean aMatches = this.mm1.isRuntime() ? this.mm1.matches(method, targetClass, args) : this.mm1.matches(method, targetClass);
/*     */       
/* 294 */       boolean bMatches = this.mm2.isRuntime() ? this.mm2.matches(method, targetClass, args) : this.mm2.matches(method, targetClass);
/* 295 */       return (aMatches && bMatches);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 300 */       if (this == other) {
/* 301 */         return true;
/*     */       }
/* 303 */       if (!(other instanceof IntersectionMethodMatcher)) {
/* 304 */         return false;
/*     */       }
/* 306 */       IntersectionMethodMatcher that = (IntersectionMethodMatcher)other;
/* 307 */       return (this.mm1.equals(that.mm1) && this.mm2.equals(that.mm2));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 312 */       return 37 * this.mm1.hashCode() + this.mm2.hashCode();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class IntersectionIntroductionAwareMethodMatcher
/*     */     extends IntersectionMethodMatcher
/*     */     implements IntroductionAwareMethodMatcher
/*     */   {
/*     */     public IntersectionIntroductionAwareMethodMatcher(MethodMatcher mm1, MethodMatcher mm2) {
/* 327 */       super(mm1, mm2);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Method method, Class<?> targetClass, boolean hasIntroductions) {
/* 332 */       return (MethodMatchers.matches(this.mm1, method, targetClass, hasIntroductions) && 
/* 333 */         MethodMatchers.matches(this.mm2, method, targetClass, hasIntroductions));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/support/MethodMatchers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */