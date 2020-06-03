/*     */ package org.springframework.beans.factory;
/*     */ 
/*     */ import org.springframework.beans.FatalBeanException;
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
/*     */ public class BeanDefinitionStoreException
/*     */   extends FatalBeanException
/*     */ {
/*     */   @Nullable
/*     */   private final String resourceDescription;
/*     */   @Nullable
/*     */   private final String beanName;
/*     */   
/*     */   public BeanDefinitionStoreException(String msg) {
/*  45 */     super(msg);
/*  46 */     this.resourceDescription = null;
/*  47 */     this.beanName = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionStoreException(String msg, @Nullable Throwable cause) {
/*  56 */     super(msg, cause);
/*  57 */     this.resourceDescription = null;
/*  58 */     this.beanName = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionStoreException(@Nullable String resourceDescription, String msg) {
/*  67 */     super(msg);
/*  68 */     this.resourceDescription = resourceDescription;
/*  69 */     this.beanName = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionStoreException(@Nullable String resourceDescription, String msg, @Nullable Throwable cause) {
/*  79 */     super(msg, cause);
/*  80 */     this.resourceDescription = resourceDescription;
/*  81 */     this.beanName = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionStoreException(@Nullable String resourceDescription, String beanName, String msg) {
/*  92 */     this(resourceDescription, beanName, msg, null);
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
/*     */   public BeanDefinitionStoreException(@Nullable String resourceDescription, String beanName, String msg, @Nullable Throwable cause) {
/* 106 */     super("Invalid bean definition with name '" + beanName + "' defined in " + resourceDescription + ": " + msg, cause);
/*     */     
/* 108 */     this.resourceDescription = resourceDescription;
/* 109 */     this.beanName = beanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getResourceDescription() {
/* 118 */     return this.resourceDescription;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getBeanName() {
/* 126 */     return this.beanName;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/BeanDefinitionStoreException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */