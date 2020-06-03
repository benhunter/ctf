/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.concurrent.ListenableFuture;
/*     */ import org.springframework.util.concurrent.ListenableFutureCallback;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.context.request.WebRequest;
/*     */ import org.springframework.web.context.request.async.DeferredResult;
/*     */ import org.springframework.web.context.request.async.WebAsyncUtils;
/*     */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*     */ import org.springframework.web.method.support.ModelAndViewContainer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DeferredResultMethodReturnValueHandler
/*     */   implements HandlerMethodReturnValueHandler
/*     */ {
/*     */   public boolean supportsReturnType(MethodParameter returnType) {
/*  44 */     Class<?> type = returnType.getParameterType();
/*  45 */     return (DeferredResult.class.isAssignableFrom(type) || ListenableFuture.class
/*  46 */       .isAssignableFrom(type) || CompletionStage.class
/*  47 */       .isAssignableFrom(type));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
/*     */     DeferredResult<?> result;
/*  54 */     if (returnValue == null) {
/*  55 */       mavContainer.setRequestHandled(true);
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  61 */     if (returnValue instanceof DeferredResult) {
/*  62 */       result = (DeferredResult)returnValue;
/*     */     }
/*  64 */     else if (returnValue instanceof ListenableFuture) {
/*  65 */       result = adaptListenableFuture((ListenableFuture)returnValue);
/*     */     }
/*  67 */     else if (returnValue instanceof CompletionStage) {
/*  68 */       result = adaptCompletionStage((CompletionStage)returnValue);
/*     */     }
/*     */     else {
/*     */       
/*  72 */       throw new IllegalStateException("Unexpected return value type: " + returnValue);
/*     */     } 
/*     */     
/*  75 */     WebAsyncUtils.getAsyncManager((WebRequest)webRequest).startDeferredResultProcessing(result, new Object[] { mavContainer });
/*     */   }
/*     */   
/*     */   private DeferredResult<Object> adaptListenableFuture(ListenableFuture<?> future) {
/*  79 */     final DeferredResult<Object> result = new DeferredResult();
/*  80 */     future.addCallback(new ListenableFutureCallback<Object>()
/*     */         {
/*     */           public void onSuccess(@Nullable Object value) {
/*  83 */             result.setResult(value);
/*     */           }
/*     */           
/*     */           public void onFailure(Throwable ex) {
/*  87 */             result.setErrorResult(ex);
/*     */           }
/*     */         });
/*  90 */     return result;
/*     */   }
/*     */   
/*     */   private DeferredResult<Object> adaptCompletionStage(CompletionStage<?> future) {
/*  94 */     DeferredResult<Object> result = new DeferredResult();
/*  95 */     future.handle((value, ex) -> {
/*     */           if (ex != null) {
/*     */             if (ex instanceof java.util.concurrent.CompletionException && ex.getCause() != null) {
/*     */               ex = ex.getCause();
/*     */             }
/*     */             
/*     */             result.setErrorResult(ex);
/*     */           } else {
/*     */             result.setResult(value);
/*     */           } 
/*     */           return null;
/*     */         });
/* 107 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/DeferredResultMethodReturnValueHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */