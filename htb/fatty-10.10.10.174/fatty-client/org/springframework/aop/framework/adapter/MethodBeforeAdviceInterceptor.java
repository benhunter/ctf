/*    */ package org.springframework.aop.framework.adapter;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.aopalliance.intercept.MethodInterceptor;
/*    */ import org.aopalliance.intercept.MethodInvocation;
/*    */ import org.springframework.aop.BeforeAdvice;
/*    */ import org.springframework.aop.MethodBeforeAdvice;
/*    */ import org.springframework.util.Assert;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MethodBeforeAdviceInterceptor
/*    */   implements MethodInterceptor, BeforeAdvice, Serializable
/*    */ {
/*    */   private final MethodBeforeAdvice advice;
/*    */   
/*    */   public MethodBeforeAdviceInterceptor(MethodBeforeAdvice advice) {
/* 48 */     Assert.notNull(advice, "Advice must not be null");
/* 49 */     this.advice = advice;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object invoke(MethodInvocation mi) throws Throwable {
/* 55 */     this.advice.before(mi.getMethod(), mi.getArguments(), mi.getThis());
/* 56 */     return mi.proceed();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/adapter/MethodBeforeAdviceInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */