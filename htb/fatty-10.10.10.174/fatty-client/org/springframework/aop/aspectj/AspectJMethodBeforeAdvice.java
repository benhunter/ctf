/*    */ package org.springframework.aop.aspectj;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.aop.MethodBeforeAdvice;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ public class AspectJMethodBeforeAdvice
/*    */   extends AbstractAspectJAdvice
/*    */   implements MethodBeforeAdvice, Serializable
/*    */ {
/*    */   public AspectJMethodBeforeAdvice(Method aspectJBeforeAdviceMethod, AspectJExpressionPointcut pointcut, AspectInstanceFactory aif) {
/* 38 */     super(aspectJBeforeAdviceMethod, pointcut, aif);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void before(Method method, Object[] args, @Nullable Object target) throws Throwable {
/* 44 */     invokeAdviceMethod(getJoinPointMatch(), null, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBeforeAdvice() {
/* 49 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isAfterAdvice() {
/* 54 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/AspectJMethodBeforeAdvice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */