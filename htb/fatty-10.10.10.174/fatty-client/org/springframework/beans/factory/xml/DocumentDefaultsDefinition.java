/*     */ package org.springframework.beans.factory.xml;
/*     */ 
/*     */ import org.springframework.beans.factory.parsing.DefaultsDefinition;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DocumentDefaultsDefinition
/*     */   implements DefaultsDefinition
/*     */ {
/*     */   @Nullable
/*     */   private String lazyInit;
/*     */   @Nullable
/*     */   private String merge;
/*     */   @Nullable
/*     */   private String autowire;
/*     */   @Nullable
/*     */   private String autowireCandidates;
/*     */   @Nullable
/*     */   private String initMethod;
/*     */   @Nullable
/*     */   private String destroyMethod;
/*     */   @Nullable
/*     */   private Object source;
/*     */   
/*     */   public void setLazyInit(@Nullable String lazyInit) {
/*  58 */     this.lazyInit = lazyInit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getLazyInit() {
/*  66 */     return this.lazyInit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMerge(@Nullable String merge) {
/*  73 */     this.merge = merge;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getMerge() {
/*  81 */     return this.merge;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutowire(@Nullable String autowire) {
/*  88 */     this.autowire = autowire;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getAutowire() {
/*  96 */     return this.autowire;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutowireCandidates(@Nullable String autowireCandidates) {
/* 104 */     this.autowireCandidates = autowireCandidates;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getAutowireCandidates() {
/* 113 */     return this.autowireCandidates;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInitMethod(@Nullable String initMethod) {
/* 120 */     this.initMethod = initMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getInitMethod() {
/* 128 */     return this.initMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDestroyMethod(@Nullable String destroyMethod) {
/* 135 */     this.destroyMethod = destroyMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getDestroyMethod() {
/* 143 */     return this.destroyMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSource(@Nullable Object source) {
/* 151 */     this.source = source;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getSource() {
/* 157 */     return this.source;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/xml/DocumentDefaultsDefinition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */