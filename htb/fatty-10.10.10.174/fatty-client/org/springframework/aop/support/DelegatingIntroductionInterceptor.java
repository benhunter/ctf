/*     */ package org.springframework.aop.support;
/*     */ 
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.aop.DynamicIntroductionAdvice;
/*     */ import org.springframework.aop.IntroductionInterceptor;
/*     */ import org.springframework.aop.ProxyMethodInvocation;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DelegatingIntroductionInterceptor
/*     */   extends IntroductionInfoSupport
/*     */   implements IntroductionInterceptor
/*     */ {
/*     */   @Nullable
/*     */   private Object delegate;
/*     */   
/*     */   public DelegatingIntroductionInterceptor(Object delegate) {
/*  70 */     init(delegate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DelegatingIntroductionInterceptor() {
/*  79 */     init(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init(Object delegate) {
/*  89 */     Assert.notNull(delegate, "Delegate must not be null");
/*  90 */     this.delegate = delegate;
/*  91 */     implementInterfacesOnObject(delegate);
/*     */ 
/*     */     
/*  94 */     suppressInterface(IntroductionInterceptor.class);
/*  95 */     suppressInterface(DynamicIntroductionAdvice.class);
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
/* 107 */     if (isMethodOnIntroducedInterface(mi)) {
/*     */ 
/*     */ 
/*     */       
/* 111 */       Object retVal = AopUtils.invokeJoinpointUsingReflection(this.delegate, mi.getMethod(), mi.getArguments());
/*     */ 
/*     */ 
/*     */       
/* 115 */       if (retVal == this.delegate && mi instanceof ProxyMethodInvocation) {
/* 116 */         Object proxy = ((ProxyMethodInvocation)mi).getProxy();
/* 117 */         if (mi.getMethod().getReturnType().isInstance(proxy)) {
/* 118 */           retVal = proxy;
/*     */         }
/*     */       } 
/* 121 */       return retVal;
/*     */     } 
/*     */     
/* 124 */     return doProceed(mi);
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
/* 136 */     return mi.proceed();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/support/DelegatingIntroductionInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */