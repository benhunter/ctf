/*    */ package org.springframework.aop.interceptor;
/*    */ 
/*    */ import org.aopalliance.intercept.MethodInvocation;
/*    */ import org.apache.commons.logging.Log;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SimpleTraceInterceptor
/*    */   extends AbstractTraceInterceptor
/*    */ {
/*    */   public SimpleTraceInterceptor() {}
/*    */   
/*    */   public SimpleTraceInterceptor(boolean useDynamicLogger) {
/* 51 */     setUseDynamicLogger(useDynamicLogger);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Object invokeUnderTrace(MethodInvocation invocation, Log logger) throws Throwable {
/* 57 */     String invocationDescription = getInvocationDescription(invocation);
/* 58 */     writeToLog(logger, "Entering " + invocationDescription);
/*    */     try {
/* 60 */       Object rval = invocation.proceed();
/* 61 */       writeToLog(logger, "Exiting " + invocationDescription);
/* 62 */       return rval;
/*    */     }
/* 64 */     catch (Throwable ex) {
/* 65 */       writeToLog(logger, "Exception thrown in " + invocationDescription, ex);
/* 66 */       throw ex;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected String getInvocationDescription(MethodInvocation invocation) {
/* 76 */     return "method '" + invocation.getMethod().getName() + "' of class [" + invocation
/* 77 */       .getThis().getClass().getName() + "]";
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/interceptor/SimpleTraceInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */