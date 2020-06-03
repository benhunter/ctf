/*     */ package org.springframework.aop.support;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.aop.AopInvocationException;
/*     */ import org.springframework.aop.IntroductionAdvisor;
/*     */ import org.springframework.aop.IntroductionAwareMethodMatcher;
/*     */ import org.springframework.aop.MethodMatcher;
/*     */ import org.springframework.aop.Pointcut;
/*     */ import org.springframework.aop.PointcutAdvisor;
/*     */ import org.springframework.aop.SpringProxy;
/*     */ import org.springframework.aop.TargetClassAware;
/*     */ import org.springframework.core.BridgeMethodResolver;
/*     */ import org.springframework.core.MethodIntrospector;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AopUtils
/*     */ {
/*     */   public static boolean isAopProxy(@Nullable Object object) {
/*  69 */     return (object instanceof SpringProxy && (
/*  70 */       Proxy.isProxyClass(object.getClass()) || ClassUtils.isCglibProxyClass(object.getClass())));
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
/*     */   public static boolean isJdkDynamicProxy(@Nullable Object object) {
/*  82 */     return (object instanceof SpringProxy && Proxy.isProxyClass(object.getClass()));
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
/*     */   public static boolean isCglibProxy(@Nullable Object object) {
/*  94 */     return (object instanceof SpringProxy && ClassUtils.isCglibProxy(object));
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
/*     */   public static Class<?> getTargetClass(Object candidate) {
/* 107 */     Assert.notNull(candidate, "Candidate object must not be null");
/* 108 */     Class<?> result = null;
/* 109 */     if (candidate instanceof TargetClassAware) {
/* 110 */       result = ((TargetClassAware)candidate).getTargetClass();
/*     */     }
/* 112 */     if (result == null) {
/* 113 */       result = isCglibProxy(candidate) ? candidate.getClass().getSuperclass() : candidate.getClass();
/*     */     }
/* 115 */     return result;
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
/*     */ 
/*     */   
/*     */   public static Method selectInvocableMethod(Method method, @Nullable Class<?> targetType) {
/* 131 */     if (targetType == null) {
/* 132 */       return method;
/*     */     }
/* 134 */     Method methodToUse = MethodIntrospector.selectInvocableMethod(method, targetType);
/* 135 */     if (Modifier.isPrivate(methodToUse.getModifiers()) && !Modifier.isStatic(methodToUse.getModifiers()) && SpringProxy.class
/* 136 */       .isAssignableFrom(targetType)) {
/* 137 */       throw new IllegalStateException(String.format("Need to invoke method '%s' found on proxy for target class '%s' but cannot be delegated to target bean. Switch its visibility to package or protected.", new Object[] { method
/*     */ 
/*     */               
/* 140 */               .getName(), method.getDeclaringClass().getSimpleName() }));
/*     */     }
/* 142 */     return methodToUse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEqualsMethod(@Nullable Method method) {
/* 150 */     return ReflectionUtils.isEqualsMethod(method);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isHashCodeMethod(@Nullable Method method) {
/* 158 */     return ReflectionUtils.isHashCodeMethod(method);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isToStringMethod(@Nullable Method method) {
/* 166 */     return ReflectionUtils.isToStringMethod(method);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isFinalizeMethod(@Nullable Method method) {
/* 174 */     return (method != null && method.getName().equals("finalize") && method
/* 175 */       .getParameterCount() == 0);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Method getMostSpecificMethod(Method method, @Nullable Class<?> targetClass) {
/* 195 */     Class<?> specificTargetClass = (targetClass != null) ? ClassUtils.getUserClass(targetClass) : null;
/* 196 */     Method resolvedMethod = ClassUtils.getMostSpecificMethod(method, specificTargetClass);
/*     */     
/* 198 */     return BridgeMethodResolver.findBridgedMethod(resolvedMethod);
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
/*     */   public static boolean canApply(Pointcut pc, Class<?> targetClass) {
/* 210 */     return canApply(pc, targetClass, false);
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
/*     */   public static boolean canApply(Pointcut pc, Class<?> targetClass, boolean hasIntroductions) {
/* 224 */     Assert.notNull(pc, "Pointcut must not be null");
/* 225 */     if (!pc.getClassFilter().matches(targetClass)) {
/* 226 */       return false;
/*     */     }
/*     */     
/* 229 */     MethodMatcher methodMatcher = pc.getMethodMatcher();
/* 230 */     if (methodMatcher == MethodMatcher.TRUE)
/*     */     {
/* 232 */       return true;
/*     */     }
/*     */     
/* 235 */     IntroductionAwareMethodMatcher introductionAwareMethodMatcher = null;
/* 236 */     if (methodMatcher instanceof IntroductionAwareMethodMatcher) {
/* 237 */       introductionAwareMethodMatcher = (IntroductionAwareMethodMatcher)methodMatcher;
/*     */     }
/*     */     
/* 240 */     Set<Class<?>> classes = new LinkedHashSet<>();
/* 241 */     if (!Proxy.isProxyClass(targetClass)) {
/* 242 */       classes.add(ClassUtils.getUserClass(targetClass));
/*     */     }
/* 244 */     classes.addAll(ClassUtils.getAllInterfacesForClassAsSet(targetClass));
/*     */     
/* 246 */     for (Class<?> clazz : classes) {
/* 247 */       Method[] methods = ReflectionUtils.getAllDeclaredMethods(clazz);
/* 248 */       for (Method method : methods) {
/* 249 */         if ((introductionAwareMethodMatcher != null) ? introductionAwareMethodMatcher
/* 250 */           .matches(method, targetClass, hasIntroductions) : methodMatcher
/* 251 */           .matches(method, targetClass)) {
/* 252 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 257 */     return false;
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
/*     */   public static boolean canApply(Advisor advisor, Class<?> targetClass) {
/* 269 */     return canApply(advisor, targetClass, false);
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
/*     */   public static boolean canApply(Advisor advisor, Class<?> targetClass, boolean hasIntroductions) {
/* 283 */     if (advisor instanceof IntroductionAdvisor) {
/* 284 */       return ((IntroductionAdvisor)advisor).getClassFilter().matches(targetClass);
/*     */     }
/* 286 */     if (advisor instanceof PointcutAdvisor) {
/* 287 */       PointcutAdvisor pca = (PointcutAdvisor)advisor;
/* 288 */       return canApply(pca.getPointcut(), targetClass, hasIntroductions);
/*     */     } 
/*     */ 
/*     */     
/* 292 */     return true;
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
/*     */   public static List<Advisor> findAdvisorsThatCanApply(List<Advisor> candidateAdvisors, Class<?> clazz) {
/* 305 */     if (candidateAdvisors.isEmpty()) {
/* 306 */       return candidateAdvisors;
/*     */     }
/* 308 */     List<Advisor> eligibleAdvisors = new ArrayList<>();
/* 309 */     for (Advisor candidate : candidateAdvisors) {
/* 310 */       if (candidate instanceof IntroductionAdvisor && canApply(candidate, clazz)) {
/* 311 */         eligibleAdvisors.add(candidate);
/*     */       }
/*     */     } 
/* 314 */     boolean hasIntroductions = !eligibleAdvisors.isEmpty();
/* 315 */     for (Advisor candidate : candidateAdvisors) {
/* 316 */       if (candidate instanceof IntroductionAdvisor) {
/*     */         continue;
/*     */       }
/*     */       
/* 320 */       if (canApply(candidate, clazz, hasIntroductions)) {
/* 321 */         eligibleAdvisors.add(candidate);
/*     */       }
/*     */     } 
/* 324 */     return eligibleAdvisors;
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
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static Object invokeJoinpointUsingReflection(@Nullable Object target, Method method, Object[] args) throws Throwable {
/*     */     try {
/* 342 */       ReflectionUtils.makeAccessible(method);
/* 343 */       return method.invoke(target, args);
/*     */     }
/* 345 */     catch (InvocationTargetException ex) {
/*     */ 
/*     */       
/* 348 */       throw ex.getTargetException();
/*     */     }
/* 350 */     catch (IllegalArgumentException ex) {
/* 351 */       throw new AopInvocationException("AOP configuration seems to be invalid: tried calling method [" + method + "] on target [" + target + "]", ex);
/*     */     
/*     */     }
/* 354 */     catch (IllegalAccessException ex) {
/* 355 */       throw new AopInvocationException("Could not access method [" + method + "]", ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/support/AopUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */