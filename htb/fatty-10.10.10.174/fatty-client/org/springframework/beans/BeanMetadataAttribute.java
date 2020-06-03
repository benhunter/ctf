/*     */ package org.springframework.beans;
/*     */ 
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
/*     */ public class BeanMetadataAttribute
/*     */   implements BeanMetadataElement
/*     */ {
/*     */   private final String name;
/*     */   @Nullable
/*     */   private final Object value;
/*     */   @Nullable
/*     */   private Object source;
/*     */   
/*     */   public BeanMetadataAttribute(String name, @Nullable Object value) {
/*  47 */     Assert.notNull(name, "Name must not be null");
/*  48 */     this.name = name;
/*  49 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  57 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getValue() {
/*  65 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSource(@Nullable Object source) {
/*  73 */     this.source = source;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getSource() {
/*  79 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/*  85 */     if (this == other) {
/*  86 */       return true;
/*     */     }
/*  88 */     if (!(other instanceof BeanMetadataAttribute)) {
/*  89 */       return false;
/*     */     }
/*  91 */     BeanMetadataAttribute otherMa = (BeanMetadataAttribute)other;
/*  92 */     return (this.name.equals(otherMa.name) && 
/*  93 */       ObjectUtils.nullSafeEquals(this.value, otherMa.value) && 
/*  94 */       ObjectUtils.nullSafeEquals(this.source, otherMa.source));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  99 */     return this.name.hashCode() * 29 + ObjectUtils.nullSafeHashCode(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 104 */     return "metadata attribute '" + this.name + "'";
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/BeanMetadataAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */