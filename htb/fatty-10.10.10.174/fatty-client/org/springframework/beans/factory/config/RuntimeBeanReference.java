/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class RuntimeBeanReference
/*     */   implements BeanReference
/*     */ {
/*     */   private final String beanName;
/*     */   private final boolean toParent;
/*     */   @Nullable
/*     */   private Object source;
/*     */   
/*     */   public RuntimeBeanReference(String beanName) {
/*  46 */     this(beanName, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RuntimeBeanReference(String beanName, boolean toParent) {
/*  57 */     Assert.hasText(beanName, "'beanName' must not be empty");
/*  58 */     this.beanName = beanName;
/*  59 */     this.toParent = toParent;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBeanName() {
/*  65 */     return this.beanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isToParent() {
/*  72 */     return this.toParent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSource(@Nullable Object source) {
/*  80 */     this.source = source;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getSource() {
/*  86 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/*  92 */     if (this == other) {
/*  93 */       return true;
/*     */     }
/*  95 */     if (!(other instanceof RuntimeBeanReference)) {
/*  96 */       return false;
/*     */     }
/*  98 */     RuntimeBeanReference that = (RuntimeBeanReference)other;
/*  99 */     return (this.beanName.equals(that.beanName) && this.toParent == that.toParent);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 104 */     int result = this.beanName.hashCode();
/* 105 */     result = 29 * result + (this.toParent ? 1 : 0);
/* 106 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 111 */     return '<' + getBeanName() + '>';
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/RuntimeBeanReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */