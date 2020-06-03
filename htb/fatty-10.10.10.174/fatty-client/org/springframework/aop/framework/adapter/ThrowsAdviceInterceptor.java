/*     */ package org.springframework.aop.framework.adapter;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.AfterAdvice;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class ThrowsAdviceInterceptor
/*     */   implements MethodInterceptor, AfterAdvice
/*     */ {
/*     */   private static final String AFTER_THROWING = "afterThrowing";
/*  61 */   private static final Log logger = LogFactory.getLog(ThrowsAdviceInterceptor.class);
/*     */ 
/*     */   
/*     */   private final Object throwsAdvice;
/*     */ 
/*     */   
/*  67 */   private final Map<Class<?>, Method> exceptionHandlerMap = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThrowsAdviceInterceptor(Object throwsAdvice) {
/*  76 */     Assert.notNull(throwsAdvice, "Advice must not be null");
/*  77 */     this.throwsAdvice = throwsAdvice;
/*     */     
/*  79 */     Method[] methods = throwsAdvice.getClass().getMethods();
/*  80 */     for (Method method : methods) {
/*  81 */       if (method.getName().equals("afterThrowing") && (method
/*  82 */         .getParameterCount() == 1 || method.getParameterCount() == 4)) {
/*  83 */         Class<?> throwableParam = method.getParameterTypes()[method.getParameterCount() - 1];
/*  84 */         if (Throwable.class.isAssignableFrom(throwableParam)) {
/*     */           
/*  86 */           this.exceptionHandlerMap.put(throwableParam, method);
/*  87 */           if (logger.isDebugEnabled()) {
/*  88 */             logger.debug("Found exception handler method on throws advice: " + method);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  94 */     if (this.exceptionHandlerMap.isEmpty()) {
/*  95 */       throw new IllegalArgumentException("At least one handler method must be found in class [" + throwsAdvice
/*  96 */           .getClass() + "]");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getHandlerMethodCount() {
/* 105 */     return this.exceptionHandlerMap.size();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object invoke(MethodInvocation mi) throws Throwable {
/*     */     try {
/* 112 */       return mi.proceed();
/*     */     }
/* 114 */     catch (Throwable ex) {
/* 115 */       Method handlerMethod = getExceptionHandler(ex);
/* 116 */       if (handlerMethod != null) {
/* 117 */         invokeHandlerMethod(mi, ex, handlerMethod);
/*     */       }
/* 119 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Method getExceptionHandler(Throwable exception) {
/* 130 */     Class<?> exceptionClass = exception.getClass();
/* 131 */     if (logger.isTraceEnabled()) {
/* 132 */       logger.trace("Trying to find handler for exception of type [" + exceptionClass.getName() + "]");
/*     */     }
/* 134 */     Method handler = this.exceptionHandlerMap.get(exceptionClass);
/* 135 */     while (handler == null && exceptionClass != Throwable.class) {
/* 136 */       exceptionClass = exceptionClass.getSuperclass();
/* 137 */       handler = this.exceptionHandlerMap.get(exceptionClass);
/*     */     } 
/* 139 */     if (handler != null && logger.isTraceEnabled()) {
/* 140 */       logger.trace("Found handler for exception of type [" + exceptionClass.getName() + "]: " + handler);
/*     */     }
/* 142 */     return handler;
/*     */   }
/*     */   
/*     */   private void invokeHandlerMethod(MethodInvocation mi, Throwable ex, Method method) throws Throwable {
/*     */     Object[] handlerArgs;
/* 147 */     if (method.getParameterCount() == 1) {
/* 148 */       handlerArgs = new Object[] { ex };
/*     */     } else {
/*     */       
/* 151 */       handlerArgs = new Object[] { mi.getMethod(), mi.getArguments(), mi.getThis(), ex };
/*     */     } 
/*     */     try {
/* 154 */       method.invoke(this.throwsAdvice, handlerArgs);
/*     */     }
/* 156 */     catch (InvocationTargetException targetEx) {
/* 157 */       throw targetEx.getTargetException();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/adapter/ThrowsAdviceInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */