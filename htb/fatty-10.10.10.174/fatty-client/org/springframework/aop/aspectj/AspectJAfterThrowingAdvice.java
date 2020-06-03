/*    */ package org.springframework.aop.aspectj;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.lang.reflect.Method;
/*    */ import org.aopalliance.intercept.MethodInterceptor;
/*    */ import org.aopalliance.intercept.MethodInvocation;
/*    */ import org.springframework.aop.AfterAdvice;
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
/*    */ public class AspectJAfterThrowingAdvice
/*    */   extends AbstractAspectJAdvice
/*    */   implements MethodInterceptor, AfterAdvice, Serializable
/*    */ {
/*    */   public AspectJAfterThrowingAdvice(Method aspectJBeforeAdviceMethod, AspectJExpressionPointcut pointcut, AspectInstanceFactory aif) {
/* 40 */     super(aspectJBeforeAdviceMethod, pointcut, aif);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isBeforeAdvice() {
/* 46 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isAfterAdvice() {
/* 51 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setThrowingName(String name) {
/* 56 */     setThrowingNameNoCheck(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object invoke(MethodInvocation mi) throws Throwable {
/*    */     try {
/* 62 */       return mi.proceed();
/*    */     }
/* 64 */     catch (Throwable ex) {
/* 65 */       if (shouldInvokeOnThrowing(ex)) {
/* 66 */         invokeAdviceMethod(getJoinPointMatch(), null, ex);
/*    */       }
/* 68 */       throw ex;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean shouldInvokeOnThrowing(Throwable ex) {
/* 77 */     return getDiscoveredThrowingType().isAssignableFrom(ex.getClass());
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/AspectJAfterThrowingAdvice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */