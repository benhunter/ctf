/*     */ package org.springframework.beans.factory;
/*     */ 
/*     */ import org.springframework.beans.BeansException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UnsatisfiedDependencyException
/*     */   extends BeanCreationException
/*     */ {
/*     */   @Nullable
/*     */   private final InjectionPoint injectionPoint;
/*     */   
/*     */   public UnsatisfiedDependencyException(@Nullable String resourceDescription, @Nullable String beanName, String propertyName, String msg) {
/*  49 */     super(resourceDescription, beanName, "Unsatisfied dependency expressed through bean property '" + propertyName + "'" + (
/*     */         
/*  51 */         StringUtils.hasLength(msg) ? (": " + msg) : ""));
/*  52 */     this.injectionPoint = null;
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
/*     */   public UnsatisfiedDependencyException(@Nullable String resourceDescription, @Nullable String beanName, String propertyName, BeansException ex) {
/*  65 */     this(resourceDescription, beanName, propertyName, "");
/*  66 */     initCause((Throwable)ex);
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
/*     */   public UnsatisfiedDependencyException(@Nullable String resourceDescription, @Nullable String beanName, @Nullable InjectionPoint injectionPoint, String msg) {
/*  80 */     super(resourceDescription, beanName, "Unsatisfied dependency expressed through " + injectionPoint + (
/*     */         
/*  82 */         StringUtils.hasLength(msg) ? (": " + msg) : ""));
/*  83 */     this.injectionPoint = injectionPoint;
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
/*     */   public UnsatisfiedDependencyException(@Nullable String resourceDescription, @Nullable String beanName, @Nullable InjectionPoint injectionPoint, BeansException ex) {
/*  97 */     this(resourceDescription, beanName, injectionPoint, "");
/*  98 */     initCause((Throwable)ex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public InjectionPoint getInjectionPoint() {
/* 108 */     return this.injectionPoint;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/UnsatisfiedDependencyException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */