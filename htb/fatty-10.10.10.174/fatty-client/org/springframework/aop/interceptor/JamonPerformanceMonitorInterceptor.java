/*     */ package org.springframework.aop.interceptor;
/*     */ 
/*     */ import com.jamonapi.MonKey;
/*     */ import com.jamonapi.MonKeyImp;
/*     */ import com.jamonapi.Monitor;
/*     */ import com.jamonapi.MonitorFactory;
/*     */ import com.jamonapi.utils.Misc;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.apache.commons.logging.Log;
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
/*     */ public class JamonPerformanceMonitorInterceptor
/*     */   extends AbstractMonitoringInterceptor
/*     */ {
/*     */   private boolean trackAllInvocations = false;
/*     */   
/*     */   public JamonPerformanceMonitorInterceptor() {}
/*     */   
/*     */   public JamonPerformanceMonitorInterceptor(boolean useDynamicLogger) {
/*  62 */     setUseDynamicLogger(useDynamicLogger);
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
/*     */   public JamonPerformanceMonitorInterceptor(boolean useDynamicLogger, boolean trackAllInvocations) {
/*  74 */     setUseDynamicLogger(useDynamicLogger);
/*  75 */     setTrackAllInvocations(trackAllInvocations);
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
/*     */   public void setTrackAllInvocations(boolean trackAllInvocations) {
/*  87 */     this.trackAllInvocations = trackAllInvocations;
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
/*     */   protected boolean isInterceptorEnabled(MethodInvocation invocation, Log logger) {
/*  99 */     return (this.trackAllInvocations || isLogEnabled(logger));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object invokeUnderTrace(MethodInvocation invocation, Log logger) throws Throwable {
/* 110 */     String name = createInvocationTraceName(invocation);
/* 111 */     MonKeyImp monKeyImp = new MonKeyImp(name, name, "ms.");
/*     */     
/* 113 */     Monitor monitor = MonitorFactory.start((MonKey)monKeyImp);
/*     */     try {
/* 115 */       return invocation.proceed();
/*     */     }
/* 117 */     catch (Throwable ex) {
/* 118 */       trackException((MonKey)monKeyImp, ex);
/* 119 */       throw ex;
/*     */     } finally {
/*     */       
/* 122 */       monitor.stop();
/* 123 */       if (!this.trackAllInvocations || isLogEnabled(logger)) {
/* 124 */         writeToLog(logger, "JAMon performance statistics for method [" + name + "]:\n" + monitor);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void trackException(MonKey key, Throwable ex) {
/* 134 */     String stackTrace = "stackTrace=" + Misc.getExceptionTrace(ex);
/* 135 */     key.setDetails(stackTrace);
/*     */ 
/*     */     
/* 138 */     MonitorFactory.add((MonKey)new MonKeyImp(ex.getClass().getName(), stackTrace, "Exception"), 1.0D);
/*     */ 
/*     */     
/* 141 */     MonitorFactory.add((MonKey)new MonKeyImp("com.jamonapi.Exceptions", stackTrace, "Exception"), 1.0D);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/interceptor/JamonPerformanceMonitorInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */