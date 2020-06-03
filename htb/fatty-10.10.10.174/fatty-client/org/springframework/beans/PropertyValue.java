/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.springframework.core.AttributeAccessor;
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
/*     */ public class PropertyValue
/*     */   extends BeanMetadataAttributeAccessor
/*     */   implements Serializable
/*     */ {
/*     */   private final String name;
/*     */   @Nullable
/*     */   private final Object value;
/*     */   private boolean optional = false;
/*     */   private boolean converted = false;
/*     */   @Nullable
/*     */   private Object convertedValue;
/*     */   @Nullable
/*     */   volatile Boolean conversionNecessary;
/*     */   @Nullable
/*     */   volatile transient Object resolvedTokens;
/*     */   
/*     */   public PropertyValue(String name, @Nullable Object value) {
/*  72 */     Assert.notNull(name, "Name must not be null");
/*  73 */     this.name = name;
/*  74 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyValue(PropertyValue original) {
/*  82 */     Assert.notNull(original, "Original must not be null");
/*  83 */     this.name = original.getName();
/*  84 */     this.value = original.getValue();
/*  85 */     this.optional = original.isOptional();
/*  86 */     this.converted = original.converted;
/*  87 */     this.convertedValue = original.convertedValue;
/*  88 */     this.conversionNecessary = original.conversionNecessary;
/*  89 */     this.resolvedTokens = original.resolvedTokens;
/*  90 */     setSource(original.getSource());
/*  91 */     copyAttributesFrom((AttributeAccessor)original);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyValue(PropertyValue original, @Nullable Object newValue) {
/* 101 */     Assert.notNull(original, "Original must not be null");
/* 102 */     this.name = original.getName();
/* 103 */     this.value = newValue;
/* 104 */     this.optional = original.isOptional();
/* 105 */     this.conversionNecessary = original.conversionNecessary;
/* 106 */     this.resolvedTokens = original.resolvedTokens;
/* 107 */     setSource(original);
/* 108 */     copyAttributesFrom((AttributeAccessor)original);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 116 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getValue() {
/* 127 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyValue getOriginalPropertyValue() {
/* 136 */     PropertyValue original = this;
/* 137 */     Object source = getSource();
/* 138 */     while (source instanceof PropertyValue && source != original) {
/* 139 */       original = (PropertyValue)source;
/* 140 */       source = original.getSource();
/*     */     } 
/* 142 */     return original;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOptional(boolean optional) {
/* 151 */     this.optional = optional;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOptional() {
/* 160 */     return this.optional;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean isConverted() {
/* 168 */     return this.converted;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setConvertedValue(@Nullable Object value) {
/* 176 */     this.converted = true;
/* 177 */     this.convertedValue = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public synchronized Object getConvertedValue() {
/* 186 */     return this.convertedValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 192 */     if (this == other) {
/* 193 */       return true;
/*     */     }
/* 195 */     if (!(other instanceof PropertyValue)) {
/* 196 */       return false;
/*     */     }
/* 198 */     PropertyValue otherPv = (PropertyValue)other;
/* 199 */     return (this.name.equals(otherPv.name) && 
/* 200 */       ObjectUtils.nullSafeEquals(this.value, otherPv.value) && 
/* 201 */       ObjectUtils.nullSafeEquals(getSource(), otherPv.getSource()));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 206 */     return this.name.hashCode() * 29 + ObjectUtils.nullSafeHashCode(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 211 */     return "bean property '" + this.name + "'";
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/PropertyValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */