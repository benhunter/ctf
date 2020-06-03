/*     */ package org.springframework.scheduling.support;
/*     */ 
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ErrorHandler;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ public abstract class TaskUtils
/*     */ {
/*  46 */   public static final ErrorHandler LOG_AND_SUPPRESS_ERROR_HANDLER = new LoggingErrorHandler();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   public static final ErrorHandler LOG_AND_PROPAGATE_ERROR_HANDLER = new PropagatingErrorHandler();
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
/*     */   public static DelegatingErrorHandlingRunnable decorateTaskWithErrorHandler(Runnable task, @Nullable ErrorHandler errorHandler, boolean isRepeatingTask) {
/*  66 */     if (task instanceof DelegatingErrorHandlingRunnable) {
/*  67 */       return (DelegatingErrorHandlingRunnable)task;
/*     */     }
/*  69 */     ErrorHandler eh = (errorHandler != null) ? errorHandler : getDefaultErrorHandler(isRepeatingTask);
/*  70 */     return new DelegatingErrorHandlingRunnable(task, eh);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ErrorHandler getDefaultErrorHandler(boolean isRepeatingTask) {
/*  80 */     return isRepeatingTask ? LOG_AND_SUPPRESS_ERROR_HANDLER : LOG_AND_PROPAGATE_ERROR_HANDLER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class LoggingErrorHandler
/*     */     implements ErrorHandler
/*     */   {
/*  91 */     private final Log logger = LogFactory.getLog(LoggingErrorHandler.class);
/*     */ 
/*     */     
/*     */     public void handleError(Throwable t) {
/*  95 */       if (this.logger.isErrorEnabled()) {
/*  96 */         this.logger.error("Unexpected error occurred in scheduled task.", t);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     private LoggingErrorHandler() {}
/*     */   }
/*     */   
/*     */   private static class PropagatingErrorHandler
/*     */     extends LoggingErrorHandler
/*     */   {
/*     */     private PropagatingErrorHandler() {}
/*     */     
/*     */     public void handleError(Throwable t) {
/* 110 */       super.handleError(t);
/* 111 */       ReflectionUtils.rethrowRuntimeException(t);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/support/TaskUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */