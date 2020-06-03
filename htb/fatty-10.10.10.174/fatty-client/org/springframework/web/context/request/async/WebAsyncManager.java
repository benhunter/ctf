/*     */ package org.springframework.web.context.request.async;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.task.AsyncTaskExecutor;
/*     */ import org.springframework.core.task.SimpleAsyncTaskExecutor;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public final class WebAsyncManager
/*     */ {
/*  63 */   private static final Object RESULT_NONE = new Object();
/*     */   
/*  65 */   private static final AsyncTaskExecutor DEFAULT_TASK_EXECUTOR = (AsyncTaskExecutor)new SimpleAsyncTaskExecutor(WebAsyncManager.class
/*  66 */       .getSimpleName());
/*     */   
/*  68 */   private static final Log logger = LogFactory.getLog(WebAsyncManager.class);
/*     */   
/*  70 */   private static final CallableProcessingInterceptor timeoutCallableInterceptor = new TimeoutCallableProcessingInterceptor();
/*     */ 
/*     */   
/*  73 */   private static final DeferredResultProcessingInterceptor timeoutDeferredResultInterceptor = new TimeoutDeferredResultProcessingInterceptor();
/*     */ 
/*     */   
/*  76 */   private static Boolean taskExecutorWarning = Boolean.valueOf(true);
/*     */ 
/*     */   
/*     */   private AsyncWebRequest asyncWebRequest;
/*     */   
/*  81 */   private AsyncTaskExecutor taskExecutor = DEFAULT_TASK_EXECUTOR;
/*     */   
/*  83 */   private volatile Object concurrentResult = RESULT_NONE;
/*     */   
/*     */   private volatile Object[] concurrentResultContext;
/*     */   
/*  87 */   private final Map<Object, CallableProcessingInterceptor> callableInterceptors = new LinkedHashMap<>();
/*     */   
/*  89 */   private final Map<Object, DeferredResultProcessingInterceptor> deferredResultInterceptors = new LinkedHashMap<>();
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
/*     */   public void setAsyncWebRequest(AsyncWebRequest asyncWebRequest) {
/* 111 */     Assert.notNull(asyncWebRequest, "AsyncWebRequest must not be null");
/* 112 */     this.asyncWebRequest = asyncWebRequest;
/* 113 */     this.asyncWebRequest.addCompletionHandler(() -> asyncWebRequest.removeAttribute(WebAsyncUtils.WEB_ASYNC_MANAGER_ATTRIBUTE, 0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTaskExecutor(AsyncTaskExecutor taskExecutor) {
/* 123 */     this.taskExecutor = taskExecutor;
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
/*     */   public boolean isConcurrentHandlingStarted() {
/* 135 */     return (this.asyncWebRequest != null && this.asyncWebRequest.isAsyncStarted());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasConcurrentResult() {
/* 142 */     return (this.concurrentResult != RESULT_NONE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getConcurrentResult() {
/* 152 */     return this.concurrentResult;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getConcurrentResultContext() {
/* 161 */     return this.concurrentResultContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public CallableProcessingInterceptor getCallableInterceptor(Object key) {
/* 171 */     return this.callableInterceptors.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public DeferredResultProcessingInterceptor getDeferredResultInterceptor(Object key) {
/* 181 */     return this.deferredResultInterceptors.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerCallableInterceptor(Object key, CallableProcessingInterceptor interceptor) {
/* 190 */     Assert.notNull(key, "Key is required");
/* 191 */     Assert.notNull(interceptor, "CallableProcessingInterceptor  is required");
/* 192 */     this.callableInterceptors.put(key, interceptor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerCallableInterceptors(CallableProcessingInterceptor... interceptors) {
/* 201 */     Assert.notNull(interceptors, "A CallableProcessingInterceptor is required");
/* 202 */     for (CallableProcessingInterceptor interceptor : interceptors) {
/* 203 */       String key = interceptor.getClass().getName() + ":" + interceptor.hashCode();
/* 204 */       this.callableInterceptors.put(key, interceptor);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerDeferredResultInterceptor(Object key, DeferredResultProcessingInterceptor interceptor) {
/* 214 */     Assert.notNull(key, "Key is required");
/* 215 */     Assert.notNull(interceptor, "DeferredResultProcessingInterceptor is required");
/* 216 */     this.deferredResultInterceptors.put(key, interceptor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerDeferredResultInterceptors(DeferredResultProcessingInterceptor... interceptors) {
/* 225 */     Assert.notNull(interceptors, "A DeferredResultProcessingInterceptor is required");
/* 226 */     for (DeferredResultProcessingInterceptor interceptor : interceptors) {
/* 227 */       String key = interceptor.getClass().getName() + ":" + interceptor.hashCode();
/* 228 */       this.deferredResultInterceptors.put(key, interceptor);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearConcurrentResult() {
/* 237 */     synchronized (this) {
/* 238 */       this.concurrentResult = RESULT_NONE;
/* 239 */       this.concurrentResultContext = null;
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
/*     */ 
/*     */   
/*     */   public void startCallableProcessing(Callable<?> callable, Object... processingContext) throws Exception {
/* 258 */     Assert.notNull(callable, "Callable must not be null");
/* 259 */     startCallableProcessing(new WebAsyncTask(callable), processingContext);
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
/*     */   public void startCallableProcessing(WebAsyncTask<?> webAsyncTask, Object... processingContext) throws Exception {
/* 274 */     Assert.notNull(webAsyncTask, "WebAsyncTask must not be null");
/* 275 */     Assert.state((this.asyncWebRequest != null), "AsyncWebRequest must not be null");
/*     */     
/* 277 */     Long timeout = webAsyncTask.getTimeout();
/* 278 */     if (timeout != null) {
/* 279 */       this.asyncWebRequest.setTimeout(timeout);
/*     */     }
/*     */     
/* 282 */     AsyncTaskExecutor executor = webAsyncTask.getExecutor();
/* 283 */     if (executor != null) {
/* 284 */       this.taskExecutor = executor;
/*     */     } else {
/*     */       
/* 287 */       logExecutorWarning();
/*     */     } 
/*     */     
/* 290 */     List<CallableProcessingInterceptor> interceptors = new ArrayList<>();
/* 291 */     interceptors.add(webAsyncTask.getInterceptor());
/* 292 */     interceptors.addAll(this.callableInterceptors.values());
/* 293 */     interceptors.add(timeoutCallableInterceptor);
/*     */     
/* 295 */     Callable<?> callable = webAsyncTask.getCallable();
/* 296 */     CallableInterceptorChain interceptorChain = new CallableInterceptorChain(interceptors);
/*     */     
/* 298 */     this.asyncWebRequest.addTimeoutHandler(() -> {
/*     */           if (logger.isDebugEnabled()) {
/*     */             logger.debug("Async request timeout for " + formatRequestUri());
/*     */           }
/*     */           
/*     */           Object result = interceptorChain.triggerAfterTimeout(this.asyncWebRequest, callable);
/*     */           if (result != CallableProcessingInterceptor.RESULT_NONE) {
/*     */             setConcurrentResultAndDispatch(result);
/*     */           }
/*     */         });
/* 308 */     this.asyncWebRequest.addErrorHandler(ex -> {
/*     */           if (logger.isDebugEnabled()) {
/*     */             logger.debug("Async request error for " + formatRequestUri() + ": " + ex);
/*     */           }
/*     */           
/*     */           Object result = interceptorChain.triggerAfterError(this.asyncWebRequest, callable, ex);
/*     */           result = (result != CallableProcessingInterceptor.RESULT_NONE) ? result : ex;
/*     */           setConcurrentResultAndDispatch(result);
/*     */         });
/* 317 */     this.asyncWebRequest.addCompletionHandler(() -> interceptorChain.triggerAfterCompletion(this.asyncWebRequest, callable));
/*     */ 
/*     */     
/* 320 */     interceptorChain.applyBeforeConcurrentHandling(this.asyncWebRequest, callable);
/* 321 */     startAsyncProcessing(processingContext);
/*     */     try {
/* 323 */       Future<?> future = this.taskExecutor.submit(() -> {
/*     */             Object result = null;
/*     */             
/*     */             try {
/*     */               interceptorChain.applyPreProcess(this.asyncWebRequest, callable);
/*     */               result = callable.call();
/* 329 */             } catch (Throwable ex) {
/*     */               result = ex;
/*     */             } finally {
/*     */               result = interceptorChain.applyPostProcess(this.asyncWebRequest, callable, result);
/*     */             } 
/*     */             
/*     */             setConcurrentResultAndDispatch(result);
/*     */           });
/* 337 */       interceptorChain.setTaskFuture(future);
/*     */     }
/* 339 */     catch (RejectedExecutionException ex) {
/* 340 */       Object result = interceptorChain.applyPostProcess(this.asyncWebRequest, callable, ex);
/* 341 */       setConcurrentResultAndDispatch(result);
/* 342 */       throw ex;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void logExecutorWarning() {
/* 347 */     if (taskExecutorWarning.booleanValue() && logger.isWarnEnabled()) {
/* 348 */       synchronized (DEFAULT_TASK_EXECUTOR) {
/* 349 */         AsyncTaskExecutor executor = this.taskExecutor;
/* 350 */         if (taskExecutorWarning.booleanValue() && (executor instanceof SimpleAsyncTaskExecutor || executor instanceof org.springframework.core.task.SyncTaskExecutor)) {
/*     */           
/* 352 */           String executorTypeName = executor.getClass().getSimpleName();
/* 353 */           logger.warn("\n!!!\nAn Executor is required to handle java.util.concurrent.Callable return values.\nPlease, configure a TaskExecutor in the MVC config under \"async support\".\nThe " + executorTypeName + " currently in use is not suitable under load.\n-------------------------------\nRequest URI: '" + 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 358 */               formatRequestUri() + "'\n!!!");
/*     */           
/* 360 */           taskExecutorWarning = Boolean.valueOf(false);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private String formatRequestUri() {
/* 367 */     HttpServletRequest request = (HttpServletRequest)this.asyncWebRequest.getNativeRequest(HttpServletRequest.class);
/* 368 */     return (request != null) ? request.getRequestURI() : "servlet container";
/*     */   }
/*     */   
/*     */   private void setConcurrentResultAndDispatch(Object result) {
/* 372 */     synchronized (this) {
/* 373 */       if (this.concurrentResult != RESULT_NONE) {
/*     */         return;
/*     */       }
/* 376 */       this.concurrentResult = result;
/*     */     } 
/*     */     
/* 379 */     if (this.asyncWebRequest.isAsyncComplete()) {
/* 380 */       if (logger.isDebugEnabled()) {
/* 381 */         logger.debug("Async result set but request already complete: " + formatRequestUri());
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 386 */     if (logger.isDebugEnabled()) {
/* 387 */       boolean isError = result instanceof Throwable;
/* 388 */       logger.debug("Async " + (isError ? "error" : "result set") + ", dispatch to " + formatRequestUri());
/*     */     } 
/* 390 */     this.asyncWebRequest.dispatch();
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
/*     */   public void startDeferredResultProcessing(DeferredResult<?> deferredResult, Object... processingContext) throws Exception {
/* 410 */     Assert.notNull(deferredResult, "DeferredResult must not be null");
/* 411 */     Assert.state((this.asyncWebRequest != null), "AsyncWebRequest must not be null");
/*     */     
/* 413 */     Long timeout = deferredResult.getTimeoutValue();
/* 414 */     if (timeout != null) {
/* 415 */       this.asyncWebRequest.setTimeout(timeout);
/*     */     }
/*     */     
/* 418 */     List<DeferredResultProcessingInterceptor> interceptors = new ArrayList<>();
/* 419 */     interceptors.add(deferredResult.getInterceptor());
/* 420 */     interceptors.addAll(this.deferredResultInterceptors.values());
/* 421 */     interceptors.add(timeoutDeferredResultInterceptor);
/*     */     
/* 423 */     DeferredResultInterceptorChain interceptorChain = new DeferredResultInterceptorChain(interceptors);
/*     */     
/* 425 */     this.asyncWebRequest.addTimeoutHandler(() -> {
/*     */           
/*     */           try {
/*     */             interceptorChain.triggerAfterTimeout(this.asyncWebRequest, deferredResult);
/* 429 */           } catch (Throwable ex) {
/*     */             setConcurrentResultAndDispatch(ex);
/*     */           } 
/*     */         });
/*     */     
/* 434 */     this.asyncWebRequest.addErrorHandler(ex -> {
/*     */           try {
/*     */             if (!interceptorChain.triggerAfterError(this.asyncWebRequest, deferredResult, ex)) {
/*     */               return;
/*     */             }
/*     */             
/*     */             deferredResult.setErrorResult(ex);
/* 441 */           } catch (Throwable interceptorEx) {
/*     */             setConcurrentResultAndDispatch(interceptorEx);
/*     */           } 
/*     */         });
/*     */     
/* 446 */     this.asyncWebRequest.addCompletionHandler(() -> interceptorChain.triggerAfterCompletion(this.asyncWebRequest, deferredResult));
/*     */ 
/*     */     
/* 449 */     interceptorChain.applyBeforeConcurrentHandling(this.asyncWebRequest, deferredResult);
/* 450 */     startAsyncProcessing(processingContext);
/*     */     
/*     */     try {
/* 453 */       interceptorChain.applyPreProcess(this.asyncWebRequest, deferredResult);
/* 454 */       deferredResult.setResultHandler(result -> {
/*     */             result = interceptorChain.applyPostProcess(this.asyncWebRequest, deferredResult, result);
/*     */             
/*     */             setConcurrentResultAndDispatch(result);
/*     */           });
/* 459 */     } catch (Throwable ex) {
/* 460 */       setConcurrentResultAndDispatch(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void startAsyncProcessing(Object[] processingContext) {
/* 465 */     synchronized (this) {
/* 466 */       this.concurrentResult = RESULT_NONE;
/* 467 */       this.concurrentResultContext = processingContext;
/*     */     } 
/* 469 */     this.asyncWebRequest.startAsync();
/*     */     
/* 471 */     if (logger.isDebugEnabled())
/* 472 */       logger.debug("Started async request"); 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/request/async/WebAsyncManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */