/*     */ package org.springframework.web.method.support;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class HandlerMethodReturnValueHandlerComposite
/*     */   implements HandlerMethodReturnValueHandler
/*     */ {
/*  39 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  41 */   private final List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<HandlerMethodReturnValueHandler> getHandlers() {
/*  48 */     return Collections.unmodifiableList(this.returnValueHandlers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportsReturnType(MethodParameter returnType) {
/*  57 */     return (getReturnValueHandler(returnType) != null);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private HandlerMethodReturnValueHandler getReturnValueHandler(MethodParameter returnType) {
/*  62 */     for (HandlerMethodReturnValueHandler handler : this.returnValueHandlers) {
/*  63 */       if (handler.supportsReturnType(returnType)) {
/*  64 */         return handler;
/*     */       }
/*     */     } 
/*  67 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
/*  78 */     HandlerMethodReturnValueHandler handler = selectHandler(returnValue, returnType);
/*  79 */     if (handler == null) {
/*  80 */       throw new IllegalArgumentException("Unknown return value type: " + returnType.getParameterType().getName());
/*     */     }
/*  82 */     handler.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private HandlerMethodReturnValueHandler selectHandler(@Nullable Object value, MethodParameter returnType) {
/*  87 */     boolean isAsyncValue = isAsyncReturnValue(value, returnType);
/*  88 */     for (HandlerMethodReturnValueHandler handler : this.returnValueHandlers) {
/*  89 */       if (isAsyncValue && !(handler instanceof AsyncHandlerMethodReturnValueHandler)) {
/*     */         continue;
/*     */       }
/*  92 */       if (handler.supportsReturnType(returnType)) {
/*  93 */         return handler;
/*     */       }
/*     */     } 
/*  96 */     return null;
/*     */   }
/*     */   
/*     */   private boolean isAsyncReturnValue(@Nullable Object value, MethodParameter returnType) {
/* 100 */     for (HandlerMethodReturnValueHandler handler : this.returnValueHandlers) {
/* 101 */       if (handler instanceof AsyncHandlerMethodReturnValueHandler && ((AsyncHandlerMethodReturnValueHandler)handler)
/* 102 */         .isAsyncReturnValue(value, returnType)) {
/* 103 */         return true;
/*     */       }
/*     */     } 
/* 106 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HandlerMethodReturnValueHandlerComposite addHandler(HandlerMethodReturnValueHandler handler) {
/* 113 */     this.returnValueHandlers.add(handler);
/* 114 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HandlerMethodReturnValueHandlerComposite addHandlers(@Nullable List<? extends HandlerMethodReturnValueHandler> handlers) {
/* 123 */     if (handlers != null) {
/* 124 */       this.returnValueHandlers.addAll(handlers);
/*     */     }
/* 126 */     return this;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/method/support/HandlerMethodReturnValueHandlerComposite.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */