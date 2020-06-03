/*    */ package org.springframework.scheduling.annotation;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.concurrent.Executor;
/*    */ import org.springframework.aop.interceptor.AsyncExecutionInterceptor;
/*    */ import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
/*    */ import org.springframework.core.annotation.AnnotatedElementUtils;
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
/*    */ public class AnnotationAsyncExecutionInterceptor
/*    */   extends AsyncExecutionInterceptor
/*    */ {
/*    */   public AnnotationAsyncExecutionInterceptor(@Nullable Executor defaultExecutor) {
/* 50 */     super(defaultExecutor);
/*    */   }
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
/*    */   public AnnotationAsyncExecutionInterceptor(@Nullable Executor defaultExecutor, AsyncUncaughtExceptionHandler exceptionHandler) {
/* 63 */     super(defaultExecutor, exceptionHandler);
/*    */   }
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
/*    */   @Nullable
/*    */   protected String getExecutorQualifier(Method method) {
/* 83 */     Async async = (Async)AnnotatedElementUtils.findMergedAnnotation(method, Async.class);
/* 84 */     if (async == null) {
/* 85 */       async = (Async)AnnotatedElementUtils.findMergedAnnotation(method.getDeclaringClass(), Async.class);
/*    */     }
/* 87 */     return (async != null) ? async.value() : null;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/annotation/AnnotationAsyncExecutionInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */