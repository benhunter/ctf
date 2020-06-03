/*     */ package org.springframework.aop.framework;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.aop.IntroductionAdvisor;
/*     */ import org.springframework.aop.IntroductionAwareMethodMatcher;
/*     */ import org.springframework.aop.MethodMatcher;
/*     */ import org.springframework.aop.PointcutAdvisor;
/*     */ import org.springframework.aop.framework.adapter.AdvisorAdapterRegistry;
/*     */ import org.springframework.aop.framework.adapter.GlobalAdvisorAdapterRegistry;
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
/*     */ public class DefaultAdvisorChainFactory
/*     */   implements AdvisorChainFactory, Serializable
/*     */ {
/*     */   public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Advised config, Method method, @Nullable Class<?> targetClass) {
/*  56 */     AdvisorAdapterRegistry registry = GlobalAdvisorAdapterRegistry.getInstance();
/*  57 */     Advisor[] advisors = config.getAdvisors();
/*  58 */     List<Object> interceptorList = new ArrayList(advisors.length);
/*  59 */     Class<?> actualClass = (targetClass != null) ? targetClass : method.getDeclaringClass();
/*  60 */     Boolean hasIntroductions = null;
/*     */     
/*  62 */     for (Advisor advisor : advisors) {
/*  63 */       if (advisor instanceof PointcutAdvisor) {
/*     */         
/*  65 */         PointcutAdvisor pointcutAdvisor = (PointcutAdvisor)advisor;
/*  66 */         if (config.isPreFiltered() || pointcutAdvisor.getPointcut().getClassFilter().matches(actualClass)) {
/*  67 */           boolean match; MethodMatcher mm = pointcutAdvisor.getPointcut().getMethodMatcher();
/*     */           
/*  69 */           if (mm instanceof IntroductionAwareMethodMatcher) {
/*  70 */             if (hasIntroductions == null) {
/*  71 */               hasIntroductions = Boolean.valueOf(hasMatchingIntroductions(advisors, actualClass));
/*     */             }
/*  73 */             match = ((IntroductionAwareMethodMatcher)mm).matches(method, actualClass, hasIntroductions.booleanValue());
/*     */           } else {
/*     */             
/*  76 */             match = mm.matches(method, actualClass);
/*     */           } 
/*  78 */           if (match) {
/*  79 */             MethodInterceptor[] interceptors = registry.getInterceptors(advisor);
/*  80 */             if (mm.isRuntime()) {
/*     */ 
/*     */               
/*  83 */               for (MethodInterceptor interceptor : interceptors) {
/*  84 */                 interceptorList.add(new InterceptorAndDynamicMethodMatcher(interceptor, mm));
/*     */               }
/*     */             } else {
/*     */               
/*  88 */               interceptorList.addAll(Arrays.asList((Object[])interceptors));
/*     */             }
/*     */           
/*     */           } 
/*     */         } 
/*  93 */       } else if (advisor instanceof IntroductionAdvisor) {
/*  94 */         IntroductionAdvisor ia = (IntroductionAdvisor)advisor;
/*  95 */         if (config.isPreFiltered() || ia.getClassFilter().matches(actualClass)) {
/*  96 */           MethodInterceptor[] arrayOfMethodInterceptor = registry.getInterceptors(advisor);
/*  97 */           interceptorList.addAll(Arrays.asList((Object[])arrayOfMethodInterceptor));
/*     */         } 
/*     */       } else {
/*     */         
/* 101 */         MethodInterceptor[] arrayOfMethodInterceptor = registry.getInterceptors(advisor);
/* 102 */         interceptorList.addAll(Arrays.asList((Object[])arrayOfMethodInterceptor));
/*     */       } 
/*     */     } 
/*     */     
/* 106 */     return interceptorList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean hasMatchingIntroductions(Advisor[] advisors, Class<?> actualClass) {
/* 113 */     for (Advisor advisor : advisors) {
/* 114 */       if (advisor instanceof IntroductionAdvisor) {
/* 115 */         IntroductionAdvisor ia = (IntroductionAdvisor)advisor;
/* 116 */         if (ia.getClassFilter().matches(actualClass)) {
/* 117 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 121 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/DefaultAdvisorChainFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */