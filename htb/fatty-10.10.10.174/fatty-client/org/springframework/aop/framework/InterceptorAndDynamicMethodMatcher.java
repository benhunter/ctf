/*    */ package org.springframework.aop.framework;
/*    */ 
/*    */ import org.aopalliance.intercept.MethodInterceptor;
/*    */ import org.springframework.aop.MethodMatcher;
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
/*    */ class InterceptorAndDynamicMethodMatcher
/*    */ {
/*    */   final MethodInterceptor interceptor;
/*    */   final MethodMatcher methodMatcher;
/*    */   
/*    */   public InterceptorAndDynamicMethodMatcher(MethodInterceptor interceptor, MethodMatcher methodMatcher) {
/* 36 */     this.interceptor = interceptor;
/* 37 */     this.methodMatcher = methodMatcher;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/InterceptorAndDynamicMethodMatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */