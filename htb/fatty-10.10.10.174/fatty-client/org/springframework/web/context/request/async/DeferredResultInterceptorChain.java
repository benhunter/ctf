/*     */ package org.springframework.web.context.request.async;
/*     */ 
/*     */ import java.util.List;
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
/*     */ class DeferredResultInterceptorChain
/*     */ {
/*  34 */   private static final Log logger = LogFactory.getLog(DeferredResultInterceptorChain.class);
/*     */   
/*     */   private final List<DeferredResultProcessingInterceptor> interceptors;
/*     */   
/*  38 */   private int preProcessingIndex = -1;
/*     */ 
/*     */   
/*     */   public DeferredResultInterceptorChain(List<DeferredResultProcessingInterceptor> interceptors) {
/*  42 */     this.interceptors = interceptors;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void applyBeforeConcurrentHandling(NativeWebRequest request, DeferredResult<?> deferredResult) throws Exception {
/*  48 */     for (DeferredResultProcessingInterceptor interceptor : this.interceptors) {
/*  49 */       interceptor.beforeConcurrentHandling(request, deferredResult);
/*     */     }
/*     */   }
/*     */   
/*     */   public void applyPreProcess(NativeWebRequest request, DeferredResult<?> deferredResult) throws Exception {
/*  54 */     for (DeferredResultProcessingInterceptor interceptor : this.interceptors) {
/*  55 */       interceptor.preProcess(request, deferredResult);
/*  56 */       this.preProcessingIndex++;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object applyPostProcess(NativeWebRequest request, DeferredResult<?> deferredResult, Object concurrentResult) {
/*     */     try {
/*  64 */       for (int i = this.preProcessingIndex; i >= 0; i--) {
/*  65 */         ((DeferredResultProcessingInterceptor)this.interceptors.get(i)).postProcess(request, deferredResult, concurrentResult);
/*     */       }
/*     */     }
/*  68 */     catch (Throwable ex) {
/*  69 */       return ex;
/*     */     } 
/*  71 */     return concurrentResult;
/*     */   }
/*     */   
/*     */   public void triggerAfterTimeout(NativeWebRequest request, DeferredResult<?> deferredResult) throws Exception {
/*  75 */     for (DeferredResultProcessingInterceptor interceptor : this.interceptors) {
/*  76 */       if (deferredResult.isSetOrExpired()) {
/*     */         return;
/*     */       }
/*  79 */       if (!interceptor.handleTimeout(request, deferredResult)) {
/*     */         break;
/*     */       }
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
/*     */   public boolean triggerAfterError(NativeWebRequest request, DeferredResult<?> deferredResult, Throwable ex) throws Exception {
/*  93 */     for (DeferredResultProcessingInterceptor interceptor : this.interceptors) {
/*  94 */       if (deferredResult.isSetOrExpired()) {
/*  95 */         return false;
/*     */       }
/*  97 */       if (!interceptor.handleError(request, deferredResult, ex)) {
/*  98 */         return false;
/*     */       }
/*     */     } 
/* 101 */     return true;
/*     */   }
/*     */   
/*     */   public void triggerAfterCompletion(NativeWebRequest request, DeferredResult<?> deferredResult) {
/* 105 */     for (int i = this.preProcessingIndex; i >= 0; i--) {
/*     */       try {
/* 107 */         ((DeferredResultProcessingInterceptor)this.interceptors.get(i)).afterCompletion(request, deferredResult);
/*     */       }
/* 109 */       catch (Throwable ex) {
/* 110 */         logger.trace("Ignoring failure in afterCompletion method", ex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/request/async/DeferredResultInterceptorChain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */