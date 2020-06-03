/*     */ package org.springframework.beans.factory.wiring;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BeanWiringInfo
/*     */ {
/*     */   public static final int AUTOWIRE_BY_NAME = 1;
/*     */   public static final int AUTOWIRE_BY_TYPE = 2;
/*     */   @Nullable
/*     */   private String beanName;
/*     */   private boolean isDefaultBeanName = false;
/*  57 */   private int autowireMode = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean dependencyCheck = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanWiringInfo() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanWiringInfo(String beanName) {
/*  76 */     this(beanName, false);
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
/*     */   public BeanWiringInfo(String beanName, boolean isDefaultBeanName) {
/*  88 */     Assert.hasText(beanName, "'beanName' must not be empty");
/*  89 */     this.beanName = beanName;
/*  90 */     this.isDefaultBeanName = isDefaultBeanName;
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
/*     */   public BeanWiringInfo(int autowireMode, boolean dependencyCheck) {
/* 105 */     if (autowireMode != 1 && autowireMode != 2) {
/* 106 */       throw new IllegalArgumentException("Only constants AUTOWIRE_BY_NAME and AUTOWIRE_BY_TYPE supported");
/*     */     }
/* 108 */     this.autowireMode = autowireMode;
/* 109 */     this.dependencyCheck = dependencyCheck;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean indicatesAutowiring() {
/* 117 */     return (this.beanName == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getBeanName() {
/* 125 */     return this.beanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDefaultBeanName() {
/* 133 */     return this.isDefaultBeanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAutowireMode() {
/* 141 */     return this.autowireMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getDependencyCheck() {
/* 149 */     return this.dependencyCheck;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/wiring/BeanWiringInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */