/*     */ package org.springframework.aop.support;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.springframework.aop.ClassFilter;
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
/*     */ public abstract class ClassFilters
/*     */ {
/*     */   public static ClassFilter union(ClassFilter cf1, ClassFilter cf2) {
/*  45 */     Assert.notNull(cf1, "First ClassFilter must not be null");
/*  46 */     Assert.notNull(cf2, "Second ClassFilter must not be null");
/*  47 */     return new UnionClassFilter(new ClassFilter[] { cf1, cf2 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ClassFilter union(ClassFilter[] classFilters) {
/*  57 */     Assert.notEmpty((Object[])classFilters, "ClassFilter array must not be empty");
/*  58 */     return new UnionClassFilter(classFilters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ClassFilter intersection(ClassFilter cf1, ClassFilter cf2) {
/*  69 */     Assert.notNull(cf1, "First ClassFilter must not be null");
/*  70 */     Assert.notNull(cf2, "Second ClassFilter must not be null");
/*  71 */     return new IntersectionClassFilter(new ClassFilter[] { cf1, cf2 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ClassFilter intersection(ClassFilter[] classFilters) {
/*  81 */     Assert.notEmpty((Object[])classFilters, "ClassFilter array must not be empty");
/*  82 */     return new IntersectionClassFilter(classFilters);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class UnionClassFilter
/*     */     implements ClassFilter, Serializable
/*     */   {
/*     */     private ClassFilter[] filters;
/*     */ 
/*     */ 
/*     */     
/*     */     public UnionClassFilter(ClassFilter[] filters) {
/*  95 */       this.filters = filters;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Class<?> clazz) {
/* 100 */       for (ClassFilter filter : this.filters) {
/* 101 */         if (filter.matches(clazz)) {
/* 102 */           return true;
/*     */         }
/*     */       } 
/* 105 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 110 */       return (this == other || (other instanceof UnionClassFilter && 
/* 111 */         ObjectUtils.nullSafeEquals(this.filters, ((UnionClassFilter)other).filters)));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 116 */       return ObjectUtils.nullSafeHashCode((Object[])this.filters);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class IntersectionClassFilter
/*     */     implements ClassFilter, Serializable
/*     */   {
/*     */     private ClassFilter[] filters;
/*     */ 
/*     */ 
/*     */     
/*     */     public IntersectionClassFilter(ClassFilter[] filters) {
/* 130 */       this.filters = filters;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Class<?> clazz) {
/* 135 */       for (ClassFilter filter : this.filters) {
/* 136 */         if (!filter.matches(clazz)) {
/* 137 */           return false;
/*     */         }
/*     */       } 
/* 140 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 145 */       return (this == other || (other instanceof IntersectionClassFilter && 
/* 146 */         ObjectUtils.nullSafeEquals(this.filters, ((IntersectionClassFilter)other).filters)));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 151 */       return ObjectUtils.nullSafeHashCode((Object[])this.filters);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/support/ClassFilters.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */