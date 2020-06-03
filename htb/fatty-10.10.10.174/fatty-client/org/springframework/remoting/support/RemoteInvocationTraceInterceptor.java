/*    */ package org.springframework.remoting.support;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import org.aopalliance.intercept.MethodInterceptor;
/*    */ import org.aopalliance.intercept.MethodInvocation;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ public class RemoteInvocationTraceInterceptor
/*    */   implements MethodInterceptor
/*    */ {
/* 48 */   protected static final Log logger = LogFactory.getLog(RemoteInvocationTraceInterceptor.class);
/*    */ 
/*    */ 
/*    */   
/*    */   private final String exporterNameClause;
/*    */ 
/*    */ 
/*    */   
/*    */   public RemoteInvocationTraceInterceptor() {
/* 57 */     this.exporterNameClause = "";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RemoteInvocationTraceInterceptor(String exporterName) {
/* 66 */     this.exporterNameClause = exporterName + " ";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object invoke(MethodInvocation invocation) throws Throwable {
/* 72 */     Method method = invocation.getMethod();
/* 73 */     if (logger.isDebugEnabled()) {
/* 74 */       logger.debug("Incoming " + this.exporterNameClause + "remote call: " + 
/* 75 */           ClassUtils.getQualifiedMethodName(method));
/*    */     }
/*    */     try {
/* 78 */       Object retVal = invocation.proceed();
/* 79 */       if (logger.isDebugEnabled()) {
/* 80 */         logger.debug("Finished processing of " + this.exporterNameClause + "remote call: " + 
/* 81 */             ClassUtils.getQualifiedMethodName(method));
/*    */       }
/* 83 */       return retVal;
/*    */     }
/* 85 */     catch (Throwable ex) {
/* 86 */       if (ex instanceof RuntimeException || ex instanceof Error) {
/* 87 */         if (logger.isWarnEnabled()) {
/* 88 */           logger.warn("Processing of " + this.exporterNameClause + "remote call resulted in fatal exception: " + 
/* 89 */               ClassUtils.getQualifiedMethodName(method), ex);
/*    */         
/*    */         }
/*    */       }
/* 93 */       else if (logger.isInfoEnabled()) {
/* 94 */         logger.info("Processing of " + this.exporterNameClause + "remote call resulted in exception: " + 
/* 95 */             ClassUtils.getQualifiedMethodName(method), ex);
/*    */       } 
/*    */       
/* 98 */       throw ex;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/support/RemoteInvocationTraceInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */