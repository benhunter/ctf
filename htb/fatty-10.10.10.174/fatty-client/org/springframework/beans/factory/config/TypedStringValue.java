/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import org.springframework.beans.BeanMetadataElement;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ 
/*     */ 
/*     */ public class TypedStringValue
/*     */   implements BeanMetadataElement
/*     */ {
/*     */   @Nullable
/*     */   private String value;
/*     */   @Nullable
/*     */   private volatile Object targetType;
/*     */   @Nullable
/*     */   private Object source;
/*     */   @Nullable
/*     */   private String specifiedTypeName;
/*     */   private volatile boolean dynamic;
/*     */   
/*     */   public TypedStringValue(@Nullable String value) {
/*  60 */     setValue(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedStringValue(@Nullable String value, Class<?> targetType) {
/*  70 */     setValue(value);
/*  71 */     setTargetType(targetType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedStringValue(@Nullable String value, String targetTypeName) {
/*  81 */     setValue(value);
/*  82 */     setTargetTypeName(targetTypeName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(@Nullable String value) {
/*  93 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getValue() {
/* 101 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetType(Class<?> targetType) {
/* 111 */     Assert.notNull(targetType, "'targetType' must not be null");
/* 112 */     this.targetType = targetType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getTargetType() {
/* 119 */     Object targetTypeValue = this.targetType;
/* 120 */     if (!(targetTypeValue instanceof Class)) {
/* 121 */       throw new IllegalStateException("Typed String value does not carry a resolved target type");
/*     */     }
/* 123 */     return (Class)targetTypeValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetTypeName(@Nullable String targetTypeName) {
/* 130 */     this.targetType = targetTypeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getTargetTypeName() {
/* 138 */     Object targetTypeValue = this.targetType;
/* 139 */     if (targetTypeValue instanceof Class) {
/* 140 */       return ((Class)targetTypeValue).getName();
/*     */     }
/*     */     
/* 143 */     return (String)targetTypeValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasTargetType() {
/* 151 */     return this.targetType instanceof Class;
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
/*     */   @Nullable
/*     */   public Class<?> resolveTargetType(@Nullable ClassLoader classLoader) throws ClassNotFoundException {
/* 164 */     String typeName = getTargetTypeName();
/* 165 */     if (typeName == null) {
/* 166 */       return null;
/*     */     }
/* 168 */     Class<?> resolvedClass = ClassUtils.forName(typeName, classLoader);
/* 169 */     this.targetType = resolvedClass;
/* 170 */     return resolvedClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSource(@Nullable Object source) {
/* 179 */     this.source = source;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getSource() {
/* 185 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSpecifiedTypeName(@Nullable String specifiedTypeName) {
/* 192 */     this.specifiedTypeName = specifiedTypeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getSpecifiedTypeName() {
/* 200 */     return this.specifiedTypeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDynamic() {
/* 208 */     this.dynamic = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDynamic() {
/* 215 */     return this.dynamic;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 221 */     if (this == other) {
/* 222 */       return true;
/*     */     }
/* 224 */     if (!(other instanceof TypedStringValue)) {
/* 225 */       return false;
/*     */     }
/* 227 */     TypedStringValue otherValue = (TypedStringValue)other;
/* 228 */     return (ObjectUtils.nullSafeEquals(this.value, otherValue.value) && 
/* 229 */       ObjectUtils.nullSafeEquals(this.targetType, otherValue.targetType));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 234 */     return ObjectUtils.nullSafeHashCode(this.value) * 29 + ObjectUtils.nullSafeHashCode(this.targetType);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 239 */     return "TypedStringValue: value [" + this.value + "], target type [" + this.targetType + "]";
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/TypedStringValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */