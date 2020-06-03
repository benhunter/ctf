/*     */ package org.springframework.web.context.request.async;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Supplier;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DeferredResult<T>
/*     */ {
/*  56 */   private static final Object RESULT_NONE = new Object();
/*     */   
/*  58 */   private static final Log logger = LogFactory.getLog(DeferredResult.class);
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private final Long timeoutValue;
/*     */   
/*     */   private final Supplier<?> timeoutResult;
/*     */   
/*     */   private Runnable timeoutCallback;
/*     */   
/*     */   private Consumer<Throwable> errorCallback;
/*     */   
/*     */   private Runnable completionCallback;
/*     */   
/*     */   private DeferredResultHandler resultHandler;
/*     */   
/*  74 */   private volatile Object result = RESULT_NONE;
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile boolean expired = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public DeferredResult() {
/*  83 */     this((Long)null, () -> RESULT_NONE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DeferredResult(Long timeoutValue) {
/*  94 */     this(timeoutValue, () -> RESULT_NONE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DeferredResult(@Nullable Long timeoutValue, Object timeoutResult) {
/* 104 */     this.timeoutValue = timeoutValue;
/* 105 */     this.timeoutResult = (() -> timeoutResult);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DeferredResult(@Nullable Long timeoutValue, Supplier<?> timeoutResult) {
/* 116 */     this.timeoutValue = timeoutValue;
/* 117 */     this.timeoutResult = timeoutResult;
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
/*     */   public final boolean isSetOrExpired() {
/* 130 */     return (this.result != RESULT_NONE || this.expired);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasResult() {
/* 138 */     return (this.result != RESULT_NONE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getResult() {
/* 149 */     Object resultToCheck = this.result;
/* 150 */     return (resultToCheck != RESULT_NONE) ? resultToCheck : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   final Long getTimeoutValue() {
/* 158 */     return this.timeoutValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onTimeout(Runnable callback) {
/* 169 */     this.timeoutCallback = callback;
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
/*     */   public void onError(Consumer<Throwable> callback) {
/* 182 */     this.errorCallback = callback;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onCompletion(Runnable callback) {
/* 192 */     this.completionCallback = callback;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setResultHandler(DeferredResultHandler resultHandler) {
/*     */     Object resultToHandle;
/* 201 */     Assert.notNull(resultHandler, "DeferredResultHandler is required");
/*     */     
/* 203 */     if (this.expired) {
/*     */       return;
/*     */     }
/*     */     
/* 207 */     synchronized (this) {
/*     */       
/* 209 */       if (this.expired) {
/*     */         return;
/*     */       }
/* 212 */       resultToHandle = this.result;
/* 213 */       if (resultToHandle == RESULT_NONE) {
/*     */         
/* 215 */         this.resultHandler = resultHandler;
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     
/*     */     try {
/* 223 */       resultHandler.handleResult(resultToHandle);
/*     */     }
/* 225 */     catch (Throwable ex) {
/* 226 */       logger.debug("Failed to process async result", ex);
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
/*     */   public boolean setResult(T result) {
/* 238 */     return setResultInternal(result);
/*     */   }
/*     */   
/*     */   private boolean setResultInternal(Object result) {
/*     */     DeferredResultHandler resultHandlerToUse;
/* 243 */     if (isSetOrExpired()) {
/* 244 */       return false;
/*     */     }
/*     */     
/* 247 */     synchronized (this) {
/*     */       
/* 249 */       if (isSetOrExpired()) {
/* 250 */         return false;
/*     */       }
/*     */       
/* 253 */       this.result = result;
/* 254 */       resultHandlerToUse = this.resultHandler;
/* 255 */       if (resultHandlerToUse == null)
/*     */       {
/*     */         
/* 258 */         return true;
/*     */       }
/*     */ 
/*     */       
/* 262 */       this.resultHandler = null;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 267 */     resultHandlerToUse.handleResult(result);
/* 268 */     return true;
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
/*     */   public boolean setErrorResult(Object result) {
/* 282 */     return setResultInternal(result);
/*     */   }
/*     */ 
/*     */   
/*     */   final DeferredResultProcessingInterceptor getInterceptor() {
/* 287 */     return new DeferredResultProcessingInterceptor()
/*     */       {
/*     */         public <S> boolean handleTimeout(NativeWebRequest request, DeferredResult<S> deferredResult) {
/* 290 */           boolean continueProcessing = true;
/*     */           try {
/* 292 */             if (DeferredResult.this.timeoutCallback != null) {
/* 293 */               DeferredResult.this.timeoutCallback.run();
/*     */             }
/*     */           } finally {
/*     */             
/* 297 */             Object value = DeferredResult.this.timeoutResult.get();
/* 298 */             if (value != DeferredResult.RESULT_NONE) {
/* 299 */               continueProcessing = false;
/*     */               try {
/* 301 */                 DeferredResult.this.setResultInternal(value);
/*     */               }
/* 303 */               catch (Throwable ex) {
/* 304 */                 DeferredResult.logger.debug("Failed to handle timeout result", ex);
/*     */               } 
/*     */             } 
/*     */           } 
/* 308 */           return continueProcessing;
/*     */         }
/*     */         
/*     */         public <S> boolean handleError(NativeWebRequest request, DeferredResult<S> deferredResult, Throwable t) {
/*     */           try {
/* 313 */             if (DeferredResult.this.errorCallback != null) {
/* 314 */               DeferredResult.this.errorCallback.accept(t);
/*     */             }
/*     */           } finally {
/*     */             
/*     */             try {
/* 319 */               DeferredResult.this.setResultInternal(t);
/*     */             }
/* 321 */             catch (Throwable ex) {
/* 322 */               DeferredResult.logger.debug("Failed to handle error result", ex);
/*     */             } 
/*     */           } 
/* 325 */           return false;
/*     */         }
/*     */         
/*     */         public <S> void afterCompletion(NativeWebRequest request, DeferredResult<S> deferredResult) {
/* 329 */           DeferredResult.this.expired = true;
/* 330 */           if (DeferredResult.this.completionCallback != null)
/* 331 */             DeferredResult.this.completionCallback.run(); 
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface DeferredResultHandler {
/*     */     void handleResult(Object param1Object);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/request/async/DeferredResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */