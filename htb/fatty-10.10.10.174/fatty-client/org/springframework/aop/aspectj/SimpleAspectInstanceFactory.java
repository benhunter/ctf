/*     */ package org.springframework.aop.aspectj;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import org.springframework.aop.framework.AopConfigException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleAspectInstanceFactory
/*     */   implements AspectInstanceFactory
/*     */ {
/*     */   private final Class<?> aspectClass;
/*     */   
/*     */   public SimpleAspectInstanceFactory(Class<?> aspectClass) {
/*  44 */     Assert.notNull(aspectClass, "Aspect class must not be null");
/*  45 */     this.aspectClass = aspectClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Class<?> getAspectClass() {
/*  53 */     return this.aspectClass;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Object getAspectInstance() {
/*     */     try {
/*  59 */       return ReflectionUtils.accessibleConstructor(this.aspectClass, new Class[0]).newInstance(new Object[0]);
/*     */     }
/*  61 */     catch (NoSuchMethodException ex) {
/*  62 */       throw new AopConfigException("No default constructor on aspect class: " + this.aspectClass
/*  63 */           .getName(), ex);
/*     */     }
/*  65 */     catch (InstantiationException ex) {
/*  66 */       throw new AopConfigException("Unable to instantiate aspect class: " + this.aspectClass
/*  67 */           .getName(), ex);
/*     */     }
/*  69 */     catch (IllegalAccessException ex) {
/*  70 */       throw new AopConfigException("Could not access aspect constructor: " + this.aspectClass
/*  71 */           .getName(), ex);
/*     */     }
/*  73 */     catch (InvocationTargetException ex) {
/*  74 */       throw new AopConfigException("Failed to invoke aspect constructor: " + this.aspectClass
/*  75 */           .getName(), ex.getTargetException());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ClassLoader getAspectClassLoader() {
/*  82 */     return this.aspectClass.getClassLoader();
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
/*     */   public int getOrder() {
/*  95 */     return getOrderForAspectClass(this.aspectClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getOrderForAspectClass(Class<?> aspectClass) {
/* 106 */     return Integer.MAX_VALUE;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/SimpleAspectInstanceFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */