/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AttributeAccessorSupport
/*     */   implements AttributeAccessor, Serializable
/*     */ {
/*  41 */   private final Map<String, Object> attributes = new LinkedHashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttribute(String name, @Nullable Object value) {
/*  46 */     Assert.notNull(name, "Name must not be null");
/*  47 */     if (value != null) {
/*  48 */       this.attributes.put(name, value);
/*     */     } else {
/*     */       
/*  51 */       removeAttribute(name);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getAttribute(String name) {
/*  58 */     Assert.notNull(name, "Name must not be null");
/*  59 */     return this.attributes.get(name);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object removeAttribute(String name) {
/*  65 */     Assert.notNull(name, "Name must not be null");
/*  66 */     return this.attributes.remove(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAttribute(String name) {
/*  71 */     Assert.notNull(name, "Name must not be null");
/*  72 */     return this.attributes.containsKey(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] attributeNames() {
/*  77 */     return StringUtils.toStringArray(this.attributes.keySet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void copyAttributesFrom(AttributeAccessor source) {
/*  86 */     Assert.notNull(source, "Source must not be null");
/*  87 */     String[] attributeNames = source.attributeNames();
/*  88 */     for (String attributeName : attributeNames) {
/*  89 */       setAttribute(attributeName, source.getAttribute(attributeName));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/*  96 */     return (this == other || (other instanceof AttributeAccessorSupport && this.attributes
/*  97 */       .equals(((AttributeAccessorSupport)other).attributes)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 102 */     return this.attributes.hashCode();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/AttributeAccessorSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */