/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.concurrent.Callable;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.annotation.SynthesizingMethodParameter;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.bind.annotation.ResponseBody;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.context.request.ServletWebRequest;
/*     */ import org.springframework.web.filter.ShallowEtagHeaderFilter;
/*     */ import org.springframework.web.method.HandlerMethod;
/*     */ import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
/*     */ import org.springframework.web.method.support.InvocableHandlerMethod;
/*     */ import org.springframework.web.method.support.ModelAndViewContainer;
/*     */ import org.springframework.web.servlet.View;
/*     */ import org.springframework.web.util.NestedServletException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServletInvocableHandlerMethod
/*     */   extends InvocableHandlerMethod
/*     */ {
/*  64 */   private static final Method CALLABLE_METHOD = ClassUtils.getMethod(Callable.class, "call", new Class[0]);
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private HandlerMethodReturnValueHandlerComposite returnValueHandlers;
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletInvocableHandlerMethod(Object handler, Method method) {
/*  74 */     super(handler, method);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletInvocableHandlerMethod(HandlerMethod handlerMethod) {
/*  81 */     super(handlerMethod);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHandlerMethodReturnValueHandlers(HandlerMethodReturnValueHandlerComposite returnValueHandlers) {
/*  90 */     this.returnValueHandlers = returnValueHandlers;
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
/*     */   public void invokeAndHandle(ServletWebRequest webRequest, ModelAndViewContainer mavContainer, Object... providedArgs) throws Exception {
/* 104 */     Object returnValue = invokeForRequest((NativeWebRequest)webRequest, mavContainer, providedArgs);
/* 105 */     setResponseStatus(webRequest);
/*     */     
/* 107 */     if (returnValue == null) {
/* 108 */       if (isRequestNotModified(webRequest) || getResponseStatus() != null || mavContainer.isRequestHandled()) {
/* 109 */         disableContentCachingIfNecessary(webRequest);
/* 110 */         mavContainer.setRequestHandled(true);
/*     */         
/*     */         return;
/*     */       } 
/* 114 */     } else if (StringUtils.hasText(getResponseStatusReason())) {
/* 115 */       mavContainer.setRequestHandled(true);
/*     */       
/*     */       return;
/*     */     } 
/* 119 */     mavContainer.setRequestHandled(false);
/* 120 */     Assert.state((this.returnValueHandlers != null), "No return value handlers");
/*     */     try {
/* 122 */       this.returnValueHandlers.handleReturnValue(returnValue, 
/* 123 */           getReturnValueType(returnValue), mavContainer, (NativeWebRequest)webRequest);
/*     */     }
/* 125 */     catch (Exception ex) {
/* 126 */       if (this.logger.isTraceEnabled()) {
/* 127 */         this.logger.trace(formatErrorForReturnValue(returnValue), ex);
/*     */       }
/* 129 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setResponseStatus(ServletWebRequest webRequest) throws IOException {
/* 137 */     HttpStatus status = getResponseStatus();
/* 138 */     if (status == null) {
/*     */       return;
/*     */     }
/*     */     
/* 142 */     HttpServletResponse response = webRequest.getResponse();
/* 143 */     if (response != null) {
/* 144 */       String reason = getResponseStatusReason();
/* 145 */       if (StringUtils.hasText(reason)) {
/* 146 */         response.sendError(status.value(), reason);
/*     */       } else {
/*     */         
/* 149 */         response.setStatus(status.value());
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 154 */     webRequest.getRequest().setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, status);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isRequestNotModified(ServletWebRequest webRequest) {
/* 163 */     return webRequest.isNotModified();
/*     */   }
/*     */   
/*     */   private void disableContentCachingIfNecessary(ServletWebRequest webRequest) {
/* 167 */     if (!isRequestNotModified(webRequest)) {
/* 168 */       HttpServletResponse response = (HttpServletResponse)webRequest.getNativeResponse(HttpServletResponse.class);
/* 169 */       Assert.notNull(response, "Expected HttpServletResponse");
/* 170 */       if (StringUtils.hasText(response.getHeader("ETag"))) {
/* 171 */         HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/* 172 */         Assert.notNull(request, "Expected HttpServletRequest");
/* 173 */         ShallowEtagHeaderFilter.disableContentCaching((ServletRequest)request);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private String formatErrorForReturnValue(@Nullable Object returnValue) {
/* 179 */     return "Error handling return value=[" + returnValue + "]" + ((returnValue != null) ? (", type=" + returnValue
/* 180 */       .getClass().getName()) : "") + " in " + 
/* 181 */       toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ServletInvocableHandlerMethod wrapConcurrentResult(Object result) {
/* 191 */     return new ConcurrentResultHandlerMethod(result, new ConcurrentResultMethodParameter(result));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class ConcurrentResultHandlerMethod
/*     */     extends ServletInvocableHandlerMethod
/*     */   {
/*     */     private final MethodParameter returnType;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ConcurrentResultHandlerMethod(Object result, ServletInvocableHandlerMethod.ConcurrentResultMethodParameter returnType) {
/* 206 */       super(() -> {
/*     */             if (result instanceof Exception) {
/*     */               throw (Exception)result;
/*     */             }
/*     */             if (result instanceof Throwable) {
/*     */               throw new NestedServletException("Async processing failed", (Throwable)result);
/*     */             }
/*     */             return result;
/* 214 */           }ServletInvocableHandlerMethod.CALLABLE_METHOD);
/*     */       
/* 216 */       if (ServletInvocableHandlerMethod.this.returnValueHandlers != null) {
/* 217 */         setHandlerMethodReturnValueHandlers(ServletInvocableHandlerMethod.this.returnValueHandlers);
/*     */       }
/* 219 */       this.returnType = (MethodParameter)returnType;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Class<?> getBeanType() {
/* 227 */       return ServletInvocableHandlerMethod.this.getBeanType();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MethodParameter getReturnValueType(@Nullable Object returnValue) {
/* 236 */       return this.returnType;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <A extends java.lang.annotation.Annotation> A getMethodAnnotation(Class<A> annotationType) {
/* 244 */       return (A)ServletInvocableHandlerMethod.this.getMethodAnnotation(annotationType);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <A extends java.lang.annotation.Annotation> boolean hasMethodAnnotation(Class<A> annotationType) {
/* 252 */       return ServletInvocableHandlerMethod.this.hasMethodAnnotation(annotationType);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class ConcurrentResultMethodParameter
/*     */     extends HandlerMethod.HandlerMethodParameter
/*     */   {
/*     */     @Nullable
/*     */     private final Object returnValue;
/*     */ 
/*     */     
/*     */     private final ResolvableType returnType;
/*     */ 
/*     */ 
/*     */     
/*     */     public ConcurrentResultMethodParameter(Object returnValue) {
/* 270 */       super((HandlerMethod)ServletInvocableHandlerMethod.this, -1);
/* 271 */       this.returnValue = returnValue;
/* 272 */       this
/*     */         
/* 274 */         .returnType = (returnValue instanceof ReactiveTypeHandler.CollectedValuesList) ? ((ReactiveTypeHandler.CollectedValuesList)returnValue).getReturnType() : ResolvableType.forType(super.getGenericParameterType()).getGeneric(new int[0]);
/*     */     }
/*     */     
/*     */     public ConcurrentResultMethodParameter(ConcurrentResultMethodParameter original) {
/* 278 */       super((HandlerMethod)ServletInvocableHandlerMethod.this, original);
/* 279 */       this.returnValue = original.returnValue;
/* 280 */       this.returnType = original.returnType;
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?> getParameterType() {
/* 285 */       if (this.returnValue != null) {
/* 286 */         return this.returnValue.getClass();
/*     */       }
/* 288 */       if (!ResolvableType.NONE.equals(this.returnType)) {
/* 289 */         return this.returnType.toClass();
/*     */       }
/* 291 */       return super.getParameterType();
/*     */     }
/*     */ 
/*     */     
/*     */     public Type getGenericParameterType() {
/* 296 */       return this.returnType.getType();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <T extends java.lang.annotation.Annotation> boolean hasMethodAnnotation(Class<T> annotationType) {
/* 303 */       return (super.hasMethodAnnotation(annotationType) || (annotationType == ResponseBody.class && this.returnValue instanceof ReactiveTypeHandler.CollectedValuesList));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ConcurrentResultMethodParameter clone() {
/* 310 */       return new ConcurrentResultMethodParameter(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/ServletInvocableHandlerMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */