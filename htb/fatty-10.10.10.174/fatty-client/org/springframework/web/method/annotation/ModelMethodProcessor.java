/*    */ package org.springframework.web.method.annotation;
/*    */ 
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.ui.Model;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
/*    */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ModelMethodProcessor
/*    */   implements HandlerMethodArgumentResolver, HandlerMethodReturnValueHandler
/*    */ {
/*    */   public boolean supportsParameter(MethodParameter parameter) {
/* 44 */     return Model.class.isAssignableFrom(parameter.getParameterType());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
/* 52 */     Assert.state((mavContainer != null), "ModelAndViewContainer is required for model exposure");
/* 53 */     return mavContainer.getModel();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean supportsReturnType(MethodParameter returnType) {
/* 58 */     return Model.class.isAssignableFrom(returnType.getParameterType());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
/* 65 */     if (returnValue == null) {
/*    */       return;
/*    */     }
/* 68 */     if (returnValue instanceof Model) {
/* 69 */       mavContainer.addAllAttributes(((Model)returnValue).asMap());
/*    */     }
/*    */     else {
/*    */       
/* 73 */       throw new UnsupportedOperationException("Unexpected return type: " + returnType
/* 74 */           .getParameterType().getName() + " in method: " + returnType.getMethod());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/method/annotation/ModelMethodProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */