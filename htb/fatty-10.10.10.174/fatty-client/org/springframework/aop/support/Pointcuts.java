/*     */ package org.springframework.aop.support;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Method;
/*     */ import org.springframework.aop.MethodMatcher;
/*     */ import org.springframework.aop.Pointcut;
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
/*     */ public abstract class Pointcuts
/*     */ {
/*  38 */   public static final Pointcut SETTERS = SetterPointcut.INSTANCE;
/*     */ 
/*     */   
/*  41 */   public static final Pointcut GETTERS = GetterPointcut.INSTANCE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Pointcut union(Pointcut pc1, Pointcut pc2) {
/*  52 */     return (new ComposablePointcut(pc1)).union(pc2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Pointcut intersection(Pointcut pc1, Pointcut pc2) {
/*  63 */     return (new ComposablePointcut(pc1)).intersection(pc2);
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
/*     */   public static boolean matches(Pointcut pointcut, Method method, Class<?> targetClass, Object... args) {
/*  75 */     Assert.notNull(pointcut, "Pointcut must not be null");
/*  76 */     if (pointcut == Pointcut.TRUE) {
/*  77 */       return true;
/*     */     }
/*  79 */     if (pointcut.getClassFilter().matches(targetClass)) {
/*     */       
/*  81 */       MethodMatcher mm = pointcut.getMethodMatcher();
/*  82 */       if (mm.matches(method, targetClass))
/*     */       {
/*  84 */         return (!mm.isRuntime() || mm.matches(method, targetClass, args));
/*     */       }
/*     */     } 
/*  87 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SetterPointcut
/*     */     extends StaticMethodMatcherPointcut
/*     */     implements Serializable
/*     */   {
/*  97 */     public static final SetterPointcut INSTANCE = new SetterPointcut();
/*     */ 
/*     */     
/*     */     public boolean matches(Method method, Class<?> targetClass) {
/* 101 */       return (method.getName().startsWith("set") && method
/* 102 */         .getParameterCount() == 1 && method
/* 103 */         .getReturnType() == void.class);
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 107 */       return INSTANCE;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class GetterPointcut
/*     */     extends StaticMethodMatcherPointcut
/*     */     implements Serializable
/*     */   {
/* 118 */     public static final GetterPointcut INSTANCE = new GetterPointcut();
/*     */ 
/*     */     
/*     */     public boolean matches(Method method, Class<?> targetClass) {
/* 122 */       return (method.getName().startsWith("get") && method
/* 123 */         .getParameterCount() == 0);
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 127 */       return INSTANCE;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/support/Pointcuts.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */