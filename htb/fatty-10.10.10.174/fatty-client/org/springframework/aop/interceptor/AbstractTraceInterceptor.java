/*     */ package org.springframework.aop.interceptor;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public abstract class AbstractTraceInterceptor
/*     */   implements MethodInterceptor, Serializable
/*     */ {
/*     */   @Nullable
/*  55 */   protected transient Log defaultLogger = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hideProxyClassNames = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean logExceptionStackTrace = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUseDynamicLogger(boolean useDynamicLogger) {
/*  82 */     this.defaultLogger = useDynamicLogger ? null : LogFactory.getLog(getClass());
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
/*     */ 
/*     */   
/*     */   public void setLoggerName(String loggerName) {
/*  97 */     this.defaultLogger = LogFactory.getLog(loggerName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHideProxyClassNames(boolean hideProxyClassNames) {
/* 105 */     this.hideProxyClassNames = hideProxyClassNames;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLogExceptionStackTrace(boolean logExceptionStackTrace) {
/* 116 */     this.logExceptionStackTrace = logExceptionStackTrace;
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
/*     */   @Nullable
/*     */   public Object invoke(MethodInvocation invocation) throws Throwable {
/* 129 */     Log logger = getLoggerForInvocation(invocation);
/* 130 */     if (isInterceptorEnabled(invocation, logger)) {
/* 131 */       return invokeUnderTrace(invocation, logger);
/*     */     }
/*     */     
/* 134 */     return invocation.proceed();
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
/*     */ 
/*     */   
/*     */   protected Log getLoggerForInvocation(MethodInvocation invocation) {
/* 149 */     if (this.defaultLogger != null) {
/* 150 */       return this.defaultLogger;
/*     */     }
/*     */     
/* 153 */     Object target = invocation.getThis();
/* 154 */     return LogFactory.getLog(getClassForLogging(target));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<?> getClassForLogging(Object target) {
/* 165 */     return this.hideProxyClassNames ? AopUtils.getTargetClass(target) : target.getClass();
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
/*     */ 
/*     */   
/*     */   protected boolean isInterceptorEnabled(MethodInvocation invocation, Log logger) {
/* 180 */     return isLogEnabled(logger);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isLogEnabled(Log logger) {
/* 190 */     return logger.isTraceEnabled();
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
/*     */   protected void writeToLog(Log logger, String message) {
/* 202 */     writeToLog(logger, message, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeToLog(Log logger, String message, @Nullable Throwable ex) {
/* 219 */     if (ex != null && this.logExceptionStackTrace) {
/* 220 */       logger.trace(message, ex);
/*     */     } else {
/*     */       
/* 223 */       logger.trace(message);
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected abstract Object invokeUnderTrace(MethodInvocation paramMethodInvocation, Log paramLog) throws Throwable;
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/interceptor/AbstractTraceInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */