/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
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
/*     */ public class ReplaceOverride
/*     */   extends MethodOverride
/*     */ {
/*     */   private final String methodReplacerBeanName;
/*  41 */   private List<String> typeIdentifiers = new LinkedList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReplaceOverride(String methodName, String methodReplacerBeanName) {
/*  50 */     super(methodName);
/*  51 */     Assert.notNull(methodName, "Method replacer bean name must not be null");
/*  52 */     this.methodReplacerBeanName = methodReplacerBeanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMethodReplacerBeanName() {
/*  60 */     return this.methodReplacerBeanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTypeIdentifier(String identifier) {
/*  69 */     this.typeIdentifiers.add(identifier);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(Method method) {
/*  74 */     if (!method.getName().equals(getMethodName())) {
/*  75 */       return false;
/*     */     }
/*  77 */     if (!isOverloaded())
/*     */     {
/*  79 */       return true;
/*     */     }
/*     */     
/*  82 */     if (this.typeIdentifiers.size() != method.getParameterCount()) {
/*  83 */       return false;
/*     */     }
/*  85 */     Class<?>[] parameterTypes = method.getParameterTypes();
/*  86 */     for (int i = 0; i < this.typeIdentifiers.size(); i++) {
/*  87 */       String identifier = this.typeIdentifiers.get(i);
/*  88 */       if (!parameterTypes[i].getName().contains(identifier)) {
/*  89 */         return false;
/*     */       }
/*     */     } 
/*  92 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/*  98 */     if (!(other instanceof ReplaceOverride) || !super.equals(other)) {
/*  99 */       return false;
/*     */     }
/* 101 */     ReplaceOverride that = (ReplaceOverride)other;
/* 102 */     return (ObjectUtils.nullSafeEquals(this.methodReplacerBeanName, that.methodReplacerBeanName) && 
/* 103 */       ObjectUtils.nullSafeEquals(this.typeIdentifiers, that.typeIdentifiers));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 108 */     int hashCode = super.hashCode();
/* 109 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.methodReplacerBeanName);
/* 110 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.typeIdentifiers);
/* 111 */     return hashCode;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 116 */     return "Replace override for method '" + getMethodName() + "'";
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/ReplaceOverride.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */