/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class BeanDefinitionDefaults
/*     */ {
/*     */   private boolean lazyInit;
/*  33 */   private int autowireMode = 0;
/*     */   
/*  35 */   private int dependencyCheck = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String initMethodName;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String destroyMethodName;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLazyInit(boolean lazyInit) {
/*  50 */     this.lazyInit = lazyInit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLazyInit() {
/*  59 */     return this.lazyInit;
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
/*     */   public void setAutowireMode(int autowireMode) {
/*  71 */     this.autowireMode = autowireMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAutowireMode() {
/*  78 */     return this.autowireMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDependencyCheck(int dependencyCheck) {
/*  87 */     this.dependencyCheck = dependencyCheck;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDependencyCheck() {
/*  94 */     return this.dependencyCheck;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInitMethodName(@Nullable String initMethodName) {
/* 101 */     this.initMethodName = StringUtils.hasText(initMethodName) ? initMethodName : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getInitMethodName() {
/* 109 */     return this.initMethodName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDestroyMethodName(@Nullable String destroyMethodName) {
/* 116 */     this.destroyMethodName = StringUtils.hasText(destroyMethodName) ? destroyMethodName : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getDestroyMethodName() {
/* 124 */     return this.destroyMethodName;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/BeanDefinitionDefaults.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */