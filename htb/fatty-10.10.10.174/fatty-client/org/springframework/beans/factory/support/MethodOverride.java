/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import org.springframework.beans.BeanMetadataElement;
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
/*     */ public abstract class MethodOverride
/*     */   implements BeanMetadataElement
/*     */ {
/*     */   private final String methodName;
/*     */   private boolean overloaded = true;
/*     */   @Nullable
/*     */   private Object source;
/*     */   
/*     */   protected MethodOverride(String methodName) {
/*  53 */     Assert.notNull(methodName, "Method name must not be null");
/*  54 */     this.methodName = methodName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMethodName() {
/*  62 */     return this.methodName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setOverloaded(boolean overloaded) {
/*  72 */     this.overloaded = overloaded;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isOverloaded() {
/*  80 */     return this.overloaded;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSource(@Nullable Object source) {
/*  88 */     this.source = source;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getSource() {
/*  94 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean matches(Method paramMethod);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 109 */     if (this == other) {
/* 110 */       return true;
/*     */     }
/* 112 */     if (!(other instanceof MethodOverride)) {
/* 113 */       return false;
/*     */     }
/* 115 */     MethodOverride that = (MethodOverride)other;
/* 116 */     return (ObjectUtils.nullSafeEquals(this.methodName, that.methodName) && 
/* 117 */       ObjectUtils.nullSafeEquals(this.source, that.source));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 122 */     int hashCode = ObjectUtils.nullSafeHashCode(this.methodName);
/* 123 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.source);
/* 124 */     return hashCode;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/MethodOverride.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */