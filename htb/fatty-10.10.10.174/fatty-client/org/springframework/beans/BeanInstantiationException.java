/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
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
/*     */ public class BeanInstantiationException
/*     */   extends FatalBeanException
/*     */ {
/*     */   private final Class<?> beanClass;
/*     */   @Nullable
/*     */   private final Constructor<?> constructor;
/*     */   @Nullable
/*     */   private final Method constructingMethod;
/*     */   
/*     */   public BeanInstantiationException(Class<?> beanClass, String msg) {
/*  49 */     this(beanClass, msg, (Throwable)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanInstantiationException(Class<?> beanClass, String msg, @Nullable Throwable cause) {
/*  59 */     super("Failed to instantiate [" + beanClass.getName() + "]: " + msg, cause);
/*  60 */     this.beanClass = beanClass;
/*  61 */     this.constructor = null;
/*  62 */     this.constructingMethod = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanInstantiationException(Constructor<?> constructor, String msg, @Nullable Throwable cause) {
/*  73 */     super("Failed to instantiate [" + constructor.getDeclaringClass().getName() + "]: " + msg, cause);
/*  74 */     this.beanClass = constructor.getDeclaringClass();
/*  75 */     this.constructor = constructor;
/*  76 */     this.constructingMethod = null;
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
/*     */   public BeanInstantiationException(Method constructingMethod, String msg, @Nullable Throwable cause) {
/*  88 */     super("Failed to instantiate [" + constructingMethod.getReturnType().getName() + "]: " + msg, cause);
/*  89 */     this.beanClass = constructingMethod.getReturnType();
/*  90 */     this.constructor = null;
/*  91 */     this.constructingMethod = constructingMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getBeanClass() {
/* 100 */     return this.beanClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Constructor<?> getConstructor() {
/* 111 */     return this.constructor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Method getConstructingMethod() {
/* 122 */     return this.constructingMethod;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/BeanInstantiationException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */