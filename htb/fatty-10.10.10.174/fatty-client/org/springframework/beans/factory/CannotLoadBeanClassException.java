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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CannotLoadBeanClassException
/*     */   extends FatalBeanException
/*     */ {
/*     */   @Nullable
/*     */   private final String resourceDescription;
/*     */   private final String beanName;
/*     */   @Nullable
/*     */   private final String beanClassName;
/*     */   
/*     */   public CannotLoadBeanClassException(@Nullable String resourceDescription, String beanName, @Nullable String beanClassName, ClassNotFoundException cause) {
/*  52 */     super("Cannot find class [" + beanClassName + "] for bean with name '" + beanName + "'" + ((resourceDescription != null) ? (" defined in " + resourceDescription) : ""), cause);
/*     */     
/*  54 */     this.resourceDescription = resourceDescription;
/*  55 */     this.beanName = beanName;
/*  56 */     this.beanClassName = beanClassName;
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
/*     */   public CannotLoadBeanClassException(@Nullable String resourceDescription, String beanName, @Nullable String beanClassName, LinkageError cause) {
/*  70 */     super("Error loading class [" + beanClassName + "] for bean with name '" + beanName + "'" + ((resourceDescription != null) ? (" defined in " + resourceDescription) : "") + ": problem with class file or dependent class", cause);
/*     */ 
/*     */     
/*  73 */     this.resourceDescription = resourceDescription;
/*  74 */     this.beanName = beanName;
/*  75 */     this.beanClassName = beanClassName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getResourceDescription() {
/*  85 */     return this.resourceDescription;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBeanName() {
/*  92 */     return this.beanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getBeanClassName() {
/* 100 */     return this.beanClassName;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/CannotLoadBeanClassException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */