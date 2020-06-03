/*     */ package org.springframework.aop.interceptor;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.aop.support.DefaultPointcutAdvisor;
/*     */ import org.springframework.core.NamedThreadLocal;
/*     */ import org.springframework.core.PriorityOrdered;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ExposeInvocationInterceptor
/*     */   implements MethodInterceptor, PriorityOrdered, Serializable
/*     */ {
/*  47 */   public static final ExposeInvocationInterceptor INSTANCE = new ExposeInvocationInterceptor();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   public static final Advisor ADVISOR = (Advisor)new DefaultPointcutAdvisor((Advice)INSTANCE)
/*     */     {
/*     */       public String toString() {
/*  56 */         return ExposeInvocationInterceptor.class.getName() + ".ADVISOR";
/*     */       }
/*     */     };
/*     */   
/*  60 */   private static final ThreadLocal<MethodInvocation> invocation = (ThreadLocal<MethodInvocation>)new NamedThreadLocal("Current AOP method invocation");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MethodInvocation currentInvocation() throws IllegalStateException {
/*  71 */     MethodInvocation mi = invocation.get();
/*  72 */     if (mi == null) {
/*  73 */       throw new IllegalStateException("No MethodInvocation found: Check that an AOP invocation is in progress, and that the ExposeInvocationInterceptor is upfront in the interceptor chain. Specifically, note that advices with order HIGHEST_PRECEDENCE will execute before ExposeInvocationInterceptor!");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  78 */     return mi;
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
/*     */   public Object invoke(MethodInvocation mi) throws Throwable {
/*  90 */     MethodInvocation oldInvocation = invocation.get();
/*  91 */     invocation.set(mi);
/*     */     try {
/*  93 */       return mi.proceed();
/*     */     } finally {
/*     */       
/*  96 */       invocation.set(oldInvocation);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/* 102 */     return -2147483647;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object readResolve() {
/* 111 */     return INSTANCE;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/interceptor/ExposeInvocationInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */