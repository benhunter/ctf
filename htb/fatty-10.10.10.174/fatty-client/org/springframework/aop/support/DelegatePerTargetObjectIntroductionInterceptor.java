/*     */ package org.springframework.aop.support;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.aop.DynamicIntroductionAdvice;
/*     */ import org.springframework.aop.IntroductionInterceptor;
/*     */ import org.springframework.aop.ProxyMethodInvocation;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DelegatePerTargetObjectIntroductionInterceptor
/*     */   extends IntroductionInfoSupport
/*     */   implements IntroductionInterceptor
/*     */ {
/*  62 */   private final Map<Object, Object> delegateMap = new WeakHashMap<>();
/*     */   
/*     */   private Class<?> defaultImplType;
/*     */   
/*     */   private Class<?> interfaceType;
/*     */ 
/*     */   
/*     */   public DelegatePerTargetObjectIntroductionInterceptor(Class<?> defaultImplType, Class<?> interfaceType) {
/*  70 */     this.defaultImplType = defaultImplType;
/*  71 */     this.interfaceType = interfaceType;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  76 */     Object delegate = createNewDelegate();
/*  77 */     implementInterfacesOnObject(delegate);
/*  78 */     suppressInterface(IntroductionInterceptor.class);
/*  79 */     suppressInterface(DynamicIntroductionAdvice.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object invoke(MethodInvocation mi) throws Throwable {
/*  91 */     if (isMethodOnIntroducedInterface(mi)) {
/*  92 */       Object delegate = getIntroductionDelegateFor(mi.getThis());
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  97 */       Object retVal = AopUtils.invokeJoinpointUsingReflection(delegate, mi.getMethod(), mi.getArguments());
/*     */ 
/*     */ 
/*     */       
/* 101 */       if (retVal == delegate && mi instanceof ProxyMethodInvocation) {
/* 102 */         retVal = ((ProxyMethodInvocation)mi).getProxy();
/*     */       }
/* 104 */       return retVal;
/*     */     } 
/*     */     
/* 107 */     return doProceed(mi);
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
/*     */   protected Object doProceed(MethodInvocation mi) throws Throwable {
/* 119 */     return mi.proceed();
/*     */   }
/*     */   
/*     */   private Object getIntroductionDelegateFor(Object targetObject) {
/* 123 */     synchronized (this.delegateMap) {
/* 124 */       if (this.delegateMap.containsKey(targetObject)) {
/* 125 */         return this.delegateMap.get(targetObject);
/*     */       }
/*     */       
/* 128 */       Object delegate = createNewDelegate();
/* 129 */       this.delegateMap.put(targetObject, delegate);
/* 130 */       return delegate;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private Object createNewDelegate() {
/*     */     try {
/* 137 */       return ReflectionUtils.accessibleConstructor(this.defaultImplType, new Class[0]).newInstance(new Object[0]);
/*     */     }
/* 139 */     catch (Throwable ex) {
/* 140 */       throw new IllegalArgumentException("Cannot create default implementation for '" + this.interfaceType
/* 141 */           .getName() + "' mixin (" + this.defaultImplType.getName() + "): " + ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/support/DelegatePerTargetObjectIntroductionInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */