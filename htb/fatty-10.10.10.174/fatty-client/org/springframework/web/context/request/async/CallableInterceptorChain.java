/*     */ package org.springframework.web.context.request.async;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Future;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
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
/*     */ class CallableInterceptorChain
/*     */ {
/*  37 */   private static final Log logger = LogFactory.getLog(CallableInterceptorChain.class);
/*     */   
/*     */   private final List<CallableProcessingInterceptor> interceptors;
/*     */   
/*  41 */   private int preProcessIndex = -1;
/*     */   
/*     */   private volatile Future<?> taskFuture;
/*     */ 
/*     */   
/*     */   public CallableInterceptorChain(List<CallableProcessingInterceptor> interceptors) {
/*  47 */     this.interceptors = interceptors;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTaskFuture(Future<?> taskFuture) {
/*  52 */     this.taskFuture = taskFuture;
/*     */   }
/*     */ 
/*     */   
/*     */   public void applyBeforeConcurrentHandling(NativeWebRequest request, Callable<?> task) throws Exception {
/*  57 */     for (CallableProcessingInterceptor interceptor : this.interceptors) {
/*  58 */       interceptor.beforeConcurrentHandling(request, task);
/*     */     }
/*     */   }
/*     */   
/*     */   public void applyPreProcess(NativeWebRequest request, Callable<?> task) throws Exception {
/*  63 */     for (CallableProcessingInterceptor interceptor : this.interceptors) {
/*  64 */       interceptor.preProcess(request, task);
/*  65 */       this.preProcessIndex++;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object applyPostProcess(NativeWebRequest request, Callable<?> task, Object concurrentResult) {
/*  70 */     Throwable exceptionResult = null;
/*  71 */     for (int i = this.preProcessIndex; i >= 0; i--) {
/*     */       try {
/*  73 */         ((CallableProcessingInterceptor)this.interceptors.get(i)).postProcess(request, task, concurrentResult);
/*     */       }
/*  75 */       catch (Throwable ex) {
/*     */         
/*  77 */         if (exceptionResult != null) {
/*  78 */           if (logger.isTraceEnabled()) {
/*  79 */             logger.trace("Ignoring failure in postProcess method", ex);
/*     */           }
/*     */         } else {
/*     */           
/*  83 */           exceptionResult = ex;
/*     */         } 
/*     */       } 
/*     */     } 
/*  87 */     return (exceptionResult != null) ? exceptionResult : concurrentResult;
/*     */   }
/*     */   
/*     */   public Object triggerAfterTimeout(NativeWebRequest request, Callable<?> task) {
/*  91 */     cancelTask();
/*  92 */     for (CallableProcessingInterceptor interceptor : this.interceptors) {
/*     */       try {
/*  94 */         Object result = interceptor.handleTimeout(request, task);
/*  95 */         if (result == CallableProcessingInterceptor.RESPONSE_HANDLED) {
/*     */           break;
/*     */         }
/*  98 */         if (result != CallableProcessingInterceptor.RESULT_NONE) {
/*  99 */           return result;
/*     */         }
/*     */       }
/* 102 */       catch (Throwable ex) {
/* 103 */         return ex;
/*     */       } 
/*     */     } 
/* 106 */     return CallableProcessingInterceptor.RESULT_NONE;
/*     */   }
/*     */   
/*     */   private void cancelTask() {
/* 110 */     Future<?> future = this.taskFuture;
/* 111 */     if (future != null) {
/*     */       try {
/* 113 */         future.cancel(true);
/*     */       }
/* 115 */       catch (Throwable throwable) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object triggerAfterError(NativeWebRequest request, Callable<?> task, Throwable throwable) {
/* 122 */     cancelTask();
/* 123 */     for (CallableProcessingInterceptor interceptor : this.interceptors) {
/*     */       try {
/* 125 */         Object result = interceptor.handleError(request, task, throwable);
/* 126 */         if (result == CallableProcessingInterceptor.RESPONSE_HANDLED) {
/*     */           break;
/*     */         }
/* 129 */         if (result != CallableProcessingInterceptor.RESULT_NONE) {
/* 130 */           return result;
/*     */         }
/*     */       }
/* 133 */       catch (Throwable ex) {
/* 134 */         return ex;
/*     */       } 
/*     */     } 
/* 137 */     return CallableProcessingInterceptor.RESULT_NONE;
/*     */   }
/*     */   
/*     */   public void triggerAfterCompletion(NativeWebRequest request, Callable<?> task) {
/* 141 */     for (int i = this.interceptors.size() - 1; i >= 0; i--) {
/*     */       try {
/* 143 */         ((CallableProcessingInterceptor)this.interceptors.get(i)).afterCompletion(request, task);
/*     */       }
/* 145 */       catch (Throwable ex) {
/* 146 */         if (logger.isTraceEnabled())
/* 147 */           logger.trace("Ignoring failure in afterCompletion method", ex); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/request/async/CallableInterceptorChain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */