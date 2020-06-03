/*     */ package org.springframework.remoting.support;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
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
/*     */ public abstract class RemoteInvocationBasedExporter
/*     */   extends RemoteExporter
/*     */ {
/*  35 */   private RemoteInvocationExecutor remoteInvocationExecutor = new DefaultRemoteInvocationExecutor();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemoteInvocationExecutor(RemoteInvocationExecutor remoteInvocationExecutor) {
/*  45 */     this.remoteInvocationExecutor = remoteInvocationExecutor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RemoteInvocationExecutor getRemoteInvocationExecutor() {
/*  52 */     return this.remoteInvocationExecutor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object invoke(RemoteInvocation invocation, Object targetObject) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/*  74 */     if (this.logger.isTraceEnabled()) {
/*  75 */       this.logger.trace("Executing " + invocation);
/*     */     }
/*     */     try {
/*  78 */       return getRemoteInvocationExecutor().invoke(invocation, targetObject);
/*     */     }
/*  80 */     catch (NoSuchMethodException ex) {
/*  81 */       if (this.logger.isDebugEnabled()) {
/*  82 */         this.logger.debug("Could not find target method for " + invocation, ex);
/*     */       }
/*  84 */       throw ex;
/*     */     }
/*  86 */     catch (IllegalAccessException ex) {
/*  87 */       if (this.logger.isDebugEnabled()) {
/*  88 */         this.logger.debug("Could not access target method for " + invocation, ex);
/*     */       }
/*  90 */       throw ex;
/*     */     }
/*  92 */     catch (InvocationTargetException ex) {
/*  93 */       if (this.logger.isDebugEnabled()) {
/*  94 */         this.logger.debug("Target method failed for " + invocation, ex.getTargetException());
/*     */       }
/*  96 */       throw ex;
/*     */     } 
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
/*     */   protected RemoteInvocationResult invokeAndCreateResult(RemoteInvocation invocation, Object targetObject) {
/*     */     try {
/* 114 */       Object value = invoke(invocation, targetObject);
/* 115 */       return new RemoteInvocationResult(value);
/*     */     }
/* 117 */     catch (Throwable ex) {
/* 118 */       return new RemoteInvocationResult(ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/support/RemoteInvocationBasedExporter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */