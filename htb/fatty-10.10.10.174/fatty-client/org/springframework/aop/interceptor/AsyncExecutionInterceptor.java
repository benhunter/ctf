/*     */ package org.springframework.aop.interceptor;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Future;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.core.BridgeMethodResolver;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.core.task.AsyncTaskExecutor;
/*     */ import org.springframework.core.task.SimpleAsyncTaskExecutor;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ 
/*     */ 
/*     */ public class AsyncExecutionInterceptor
/*     */   extends AsyncExecutionAspectSupport
/*     */   implements MethodInterceptor, Ordered
/*     */ {
/*     */   public AsyncExecutionInterceptor(@Nullable Executor defaultExecutor) {
/*  78 */     super(defaultExecutor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AsyncExecutionInterceptor(@Nullable Executor defaultExecutor, AsyncUncaughtExceptionHandler exceptionHandler) {
/*  89 */     super(defaultExecutor, exceptionHandler);
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
/*     */   
/*     */   @Nullable
/*     */   public Object invoke(MethodInvocation invocation) throws Throwable {
/* 103 */     Class<?> targetClass = (invocation.getThis() != null) ? AopUtils.getTargetClass(invocation.getThis()) : null;
/* 104 */     Method specificMethod = ClassUtils.getMostSpecificMethod(invocation.getMethod(), targetClass);
/* 105 */     Method userDeclaredMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
/*     */     
/* 107 */     AsyncTaskExecutor executor = determineAsyncExecutor(userDeclaredMethod);
/* 108 */     if (executor == null) {
/* 109 */       throw new IllegalStateException("No executor specified and no default executor set on AsyncExecutionInterceptor either");
/*     */     }
/*     */ 
/*     */     
/* 113 */     Callable<Object> task = () -> {
/*     */         try {
/*     */           Object result = invocation.proceed();
/*     */           
/*     */           if (result instanceof Future) {
/*     */             return ((Future)result).get();
/*     */           }
/* 120 */         } catch (ExecutionException ex) {
/*     */           
/*     */           handleError(ex.getCause(), userDeclaredMethod, invocation.getArguments());
/* 123 */         } catch (Throwable ex) {
/*     */           handleError(ex, userDeclaredMethod, invocation.getArguments());
/*     */         } 
/*     */         
/*     */         return null;
/*     */       };
/* 129 */     return doSubmit(task, executor, invocation.getMethod().getReturnType());
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
/*     */   
/*     */   @Nullable
/*     */   protected String getExecutorQualifier(Method method) {
/* 143 */     return null;
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
/*     */   
/*     */   @Nullable
/*     */   protected Executor getDefaultExecutor(@Nullable BeanFactory beanFactory) {
/* 157 */     Executor defaultExecutor = super.getDefaultExecutor(beanFactory);
/* 158 */     return (defaultExecutor != null) ? defaultExecutor : (Executor)new SimpleAsyncTaskExecutor();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/* 163 */     return Integer.MIN_VALUE;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/interceptor/AsyncExecutionInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */