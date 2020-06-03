/*    */ package org.springframework.web.servlet.mvc.method.annotation;
/*    */ 
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
/*    */ import org.springframework.web.context.request.WebRequest;
/*    */ import org.springframework.web.context.request.async.WebAsyncTask;
/*    */ import org.springframework.web.context.request.async.WebAsyncUtils;
/*    */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*    */ import org.springframework.web.method.support.ModelAndViewContainer;
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
/*    */ public class AsyncTaskMethodReturnValueHandler
/*    */   implements HandlerMethodReturnValueHandler
/*    */ {
/*    */   @Nullable
/*    */   private final BeanFactory beanFactory;
/*    */   
/*    */   public AsyncTaskMethodReturnValueHandler(@Nullable BeanFactory beanFactory) {
/* 41 */     this.beanFactory = beanFactory;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean supportsReturnType(MethodParameter returnType) {
/* 47 */     return WebAsyncTask.class.isAssignableFrom(returnType.getParameterType());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
/* 54 */     if (returnValue == null) {
/* 55 */       mavContainer.setRequestHandled(true);
/*    */       
/*    */       return;
/*    */     } 
/* 59 */     WebAsyncTask<?> webAsyncTask = (WebAsyncTask)returnValue;
/* 60 */     if (this.beanFactory != null) {
/* 61 */       webAsyncTask.setBeanFactory(this.beanFactory);
/*    */     }
/* 63 */     WebAsyncUtils.getAsyncManager((WebRequest)webRequest).startCallableProcessing(webAsyncTask, new Object[] { mavContainer });
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/AsyncTaskMethodReturnValueHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */