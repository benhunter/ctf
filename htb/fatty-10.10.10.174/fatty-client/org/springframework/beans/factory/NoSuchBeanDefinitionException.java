/*     */ package org.springframework.beans.factory;
/*     */ 
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.core.ResolvableType;
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
/*     */ public class NoSuchBeanDefinitionException
/*     */   extends BeansException
/*     */ {
/*     */   @Nullable
/*     */   private final String beanName;
/*     */   @Nullable
/*     */   private final ResolvableType resolvableType;
/*     */   
/*     */   public NoSuchBeanDefinitionException(String name) {
/*  50 */     super("No bean named '" + name + "' available");
/*  51 */     this.beanName = name;
/*  52 */     this.resolvableType = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NoSuchBeanDefinitionException(String name, String message) {
/*  61 */     super("No bean named '" + name + "' available: " + message);
/*  62 */     this.beanName = name;
/*  63 */     this.resolvableType = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NoSuchBeanDefinitionException(Class<?> type) {
/*  71 */     this(ResolvableType.forClass(type));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NoSuchBeanDefinitionException(Class<?> type, String message) {
/*  80 */     this(ResolvableType.forClass(type), message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NoSuchBeanDefinitionException(ResolvableType type) {
/*  89 */     super("No qualifying bean of type '" + type + "' available");
/*  90 */     this.beanName = null;
/*  91 */     this.resolvableType = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NoSuchBeanDefinitionException(ResolvableType type, String message) {
/* 101 */     super("No qualifying bean of type '" + type + "' available: " + message);
/* 102 */     this.beanName = null;
/* 103 */     this.resolvableType = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getBeanName() {
/* 112 */     return this.beanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getBeanType() {
/* 121 */     return (this.resolvableType != null) ? this.resolvableType.resolve() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ResolvableType getResolvableType() {
/* 131 */     return this.resolvableType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumberOfBeansFound() {
/* 140 */     return 0;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/NoSuchBeanDefinitionException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */