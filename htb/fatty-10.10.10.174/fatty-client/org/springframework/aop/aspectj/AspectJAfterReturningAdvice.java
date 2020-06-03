/*     */ package org.springframework.aop.aspectj;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import org.springframework.aop.AfterAdvice;
/*     */ import org.springframework.aop.AfterReturningAdvice;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.TypeUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AspectJAfterReturningAdvice
/*     */   extends AbstractAspectJAdvice
/*     */   implements AfterReturningAdvice, AfterAdvice, Serializable
/*     */ {
/*     */   public AspectJAfterReturningAdvice(Method aspectJBeforeAdviceMethod, AspectJExpressionPointcut pointcut, AspectInstanceFactory aif) {
/*  44 */     super(aspectJBeforeAdviceMethod, pointcut, aif);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBeforeAdvice() {
/*  50 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAfterAdvice() {
/*  55 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setReturningName(String name) {
/*  60 */     setReturningNameNoCheck(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterReturning(@Nullable Object returnValue, Method method, Object[] args, @Nullable Object target) throws Throwable {
/*  65 */     if (shouldInvokeOnReturnValueOf(method, returnValue)) {
/*  66 */       invokeAdviceMethod(getJoinPointMatch(), returnValue, null);
/*     */     }
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
/*     */   private boolean shouldInvokeOnReturnValueOf(Method method, @Nullable Object returnValue) {
/*  80 */     Class<?> type = getDiscoveredReturningType();
/*  81 */     Type genericType = getDiscoveredReturningGenericType();
/*     */     
/*  83 */     return (matchesReturnValue(type, method, returnValue) && (genericType == null || genericType == type || 
/*     */       
/*  85 */       TypeUtils.isAssignable(genericType, method.getGenericReturnType())));
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
/*     */   private boolean matchesReturnValue(Class<?> type, Method method, @Nullable Object returnValue) {
/*  99 */     if (returnValue != null) {
/* 100 */       return ClassUtils.isAssignableValue(type, returnValue);
/*     */     }
/* 102 */     if (Object.class == type && void.class == method.getReturnType()) {
/* 103 */       return true;
/*     */     }
/*     */     
/* 106 */     return ClassUtils.isAssignable(type, method.getReturnType());
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/AspectJAfterReturningAdvice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */