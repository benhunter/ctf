/*    */ package org.springframework.aop.aspectj;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.lang.reflect.Method;
/*    */ import org.aopalliance.intercept.MethodInterceptor;
/*    */ import org.aopalliance.intercept.MethodInvocation;
/*    */ import org.aspectj.lang.JoinPoint;
/*    */ import org.aspectj.lang.ProceedingJoinPoint;
/*    */ import org.aspectj.weaver.tools.JoinPointMatch;
/*    */ import org.springframework.aop.ProxyMethodInvocation;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AspectJAroundAdvice
/*    */   extends AbstractAspectJAdvice
/*    */   implements MethodInterceptor, Serializable
/*    */ {
/*    */   public AspectJAroundAdvice(Method aspectJAroundAdviceMethod, AspectJExpressionPointcut pointcut, AspectInstanceFactory aif) {
/* 43 */     super(aspectJAroundAdviceMethod, pointcut, aif);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isBeforeAdvice() {
/* 49 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isAfterAdvice() {
/* 54 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean supportsProceedingJoinPoint() {
/* 59 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object invoke(MethodInvocation mi) throws Throwable {
/* 64 */     if (!(mi instanceof ProxyMethodInvocation)) {
/* 65 */       throw new IllegalStateException("MethodInvocation is not a Spring ProxyMethodInvocation: " + mi);
/*    */     }
/* 67 */     ProxyMethodInvocation pmi = (ProxyMethodInvocation)mi;
/* 68 */     ProceedingJoinPoint pjp = lazyGetProceedingJoinPoint(pmi);
/* 69 */     JoinPointMatch jpm = getJoinPointMatch(pmi);
/* 70 */     return invokeAdviceMethod((JoinPoint)pjp, jpm, null, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected ProceedingJoinPoint lazyGetProceedingJoinPoint(ProxyMethodInvocation rmi) {
/* 81 */     return new MethodInvocationProceedingJoinPoint(rmi);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/AspectJAroundAdvice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */