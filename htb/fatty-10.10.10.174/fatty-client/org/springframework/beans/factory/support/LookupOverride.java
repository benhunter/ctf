/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class LookupOverride
/*     */   extends MethodOverride
/*     */ {
/*     */   @Nullable
/*     */   private final String beanName;
/*     */   @Nullable
/*     */   private Method method;
/*     */   
/*     */   public LookupOverride(String methodName, @Nullable String beanName) {
/*  50 */     super(methodName);
/*  51 */     this.beanName = beanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LookupOverride(Method method, @Nullable String beanName) {
/*  61 */     super(method.getName());
/*  62 */     this.method = method;
/*  63 */     this.beanName = beanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getBeanName() {
/*  72 */     return this.beanName;
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
/*     */   public boolean matches(Method method) {
/*  85 */     if (this.method != null) {
/*  86 */       return method.equals(this.method);
/*     */     }
/*     */     
/*  89 */     return (method.getName().equals(getMethodName()) && (!isOverloaded() || 
/*  90 */       Modifier.isAbstract(method.getModifiers()) || method.getParameterCount() == 0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/*  97 */     if (!(other instanceof LookupOverride) || !super.equals(other)) {
/*  98 */       return false;
/*     */     }
/* 100 */     LookupOverride that = (LookupOverride)other;
/* 101 */     return (ObjectUtils.nullSafeEquals(this.method, that.method) && 
/* 102 */       ObjectUtils.nullSafeEquals(this.beanName, that.beanName));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 107 */     return 29 * super.hashCode() + ObjectUtils.nullSafeHashCode(this.beanName);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 112 */     return "LookupOverride for method '" + getMethodName() + "'";
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/LookupOverride.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */