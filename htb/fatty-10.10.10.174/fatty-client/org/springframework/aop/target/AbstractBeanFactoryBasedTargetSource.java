/*     */ package org.springframework.aop.target;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.TargetSource;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
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
/*     */ public abstract class AbstractBeanFactoryBasedTargetSource
/*     */   implements TargetSource, BeanFactoryAware, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -4721607536018568393L;
/*  57 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String targetBeanName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile Class<?> targetClass;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BeanFactory beanFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetBeanName(String targetBeanName) {
/*  82 */     this.targetBeanName = targetBeanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTargetBeanName() {
/*  89 */     return this.targetBeanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetClass(Class<?> targetClass) {
/*  99 */     this.targetClass = targetClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 108 */     if (this.targetBeanName == null) {
/* 109 */       throw new IllegalStateException("Property 'targetBeanName' is required");
/*     */     }
/* 111 */     this.beanFactory = beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanFactory getBeanFactory() {
/* 118 */     return this.beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getTargetClass() {
/* 124 */     Class<?> targetClass = this.targetClass;
/* 125 */     if (targetClass != null) {
/* 126 */       return targetClass;
/*     */     }
/* 128 */     synchronized (this) {
/*     */       
/* 130 */       targetClass = this.targetClass;
/* 131 */       if (targetClass == null && this.beanFactory != null) {
/*     */         
/* 133 */         targetClass = this.beanFactory.getType(this.targetBeanName);
/* 134 */         if (targetClass == null) {
/* 135 */           if (this.logger.isTraceEnabled()) {
/* 136 */             this.logger.trace("Getting bean with name '" + this.targetBeanName + "' for type determination");
/*     */           }
/* 138 */           Object beanInstance = this.beanFactory.getBean(this.targetBeanName);
/* 139 */           targetClass = beanInstance.getClass();
/*     */         } 
/* 141 */         this.targetClass = targetClass;
/*     */       } 
/* 143 */       return targetClass;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStatic() {
/* 149 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseTarget(Object target) throws Exception {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void copyFrom(AbstractBeanFactoryBasedTargetSource other) {
/* 164 */     this.targetBeanName = other.targetBeanName;
/* 165 */     this.targetClass = other.targetClass;
/* 166 */     this.beanFactory = other.beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 172 */     if (this == other) {
/* 173 */       return true;
/*     */     }
/* 175 */     if (other == null || getClass() != other.getClass()) {
/* 176 */       return false;
/*     */     }
/* 178 */     AbstractBeanFactoryBasedTargetSource otherTargetSource = (AbstractBeanFactoryBasedTargetSource)other;
/* 179 */     return (ObjectUtils.nullSafeEquals(this.beanFactory, otherTargetSource.beanFactory) && 
/* 180 */       ObjectUtils.nullSafeEquals(this.targetBeanName, otherTargetSource.targetBeanName));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 185 */     int hashCode = getClass().hashCode();
/* 186 */     hashCode = 13 * hashCode + ObjectUtils.nullSafeHashCode(this.beanFactory);
/* 187 */     hashCode = 13 * hashCode + ObjectUtils.nullSafeHashCode(this.targetBeanName);
/* 188 */     return hashCode;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 193 */     StringBuilder sb = new StringBuilder(getClass().getSimpleName());
/* 194 */     sb.append(" for target bean '").append(this.targetBeanName).append("'");
/* 195 */     if (this.targetClass != null) {
/* 196 */       sb.append(" of type [").append(this.targetClass.getName()).append("]");
/*     */     }
/* 198 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/target/AbstractBeanFactoryBasedTargetSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */