/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChildBeanDefinition
/*     */   extends AbstractBeanDefinition
/*     */ {
/*     */   @Nullable
/*     */   private String parentName;
/*     */   
/*     */   public ChildBeanDefinition(String parentName) {
/*  64 */     this.parentName = parentName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChildBeanDefinition(String parentName, MutablePropertyValues pvs) {
/*  73 */     super(null, pvs);
/*  74 */     this.parentName = parentName;
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
/*     */   public ChildBeanDefinition(String parentName, ConstructorArgumentValues cargs, MutablePropertyValues pvs) {
/*  86 */     super(cargs, pvs);
/*  87 */     this.parentName = parentName;
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
/*     */   public ChildBeanDefinition(String parentName, Class<?> beanClass, ConstructorArgumentValues cargs, MutablePropertyValues pvs) {
/* 101 */     super(cargs, pvs);
/* 102 */     this.parentName = parentName;
/* 103 */     setBeanClass(beanClass);
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
/*     */   public ChildBeanDefinition(String parentName, String beanClassName, ConstructorArgumentValues cargs, MutablePropertyValues pvs) {
/* 118 */     super(cargs, pvs);
/* 119 */     this.parentName = parentName;
/* 120 */     setBeanClassName(beanClassName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChildBeanDefinition(ChildBeanDefinition original) {
/* 129 */     super(original);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParentName(@Nullable String parentName) {
/* 135 */     this.parentName = parentName;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getParentName() {
/* 141 */     return this.parentName;
/*     */   }
/*     */ 
/*     */   
/*     */   public void validate() throws BeanDefinitionValidationException {
/* 146 */     super.validate();
/* 147 */     if (this.parentName == null) {
/* 148 */       throw new BeanDefinitionValidationException("'parentName' must be set in ChildBeanDefinition");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractBeanDefinition cloneBeanDefinition() {
/* 155 */     return new ChildBeanDefinition(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 160 */     if (this == other) {
/* 161 */       return true;
/*     */     }
/* 163 */     if (!(other instanceof ChildBeanDefinition)) {
/* 164 */       return false;
/*     */     }
/* 166 */     ChildBeanDefinition that = (ChildBeanDefinition)other;
/* 167 */     return (ObjectUtils.nullSafeEquals(this.parentName, that.parentName) && super.equals(other));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 172 */     return ObjectUtils.nullSafeHashCode(this.parentName) * 29 + super.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 177 */     return "Child bean with parent '" + this.parentName + "': " + super.toString();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/ChildBeanDefinition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */