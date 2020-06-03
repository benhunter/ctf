/*    */ package org.springframework.web.method.annotation;
/*    */ 
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.ui.ModelMap;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.CollectionUtils;
/*    */ import org.springframework.validation.BindingResult;
/*    */ import org.springframework.validation.Errors;
/*    */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
/*    */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
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
/*    */ 
/*    */ public class ErrorsMethodArgumentResolver
/*    */   implements HandlerMethodArgumentResolver
/*    */ {
/*    */   public boolean supportsParameter(MethodParameter parameter) {
/* 47 */     Class<?> paramType = parameter.getParameterType();
/* 48 */     return Errors.class.isAssignableFrom(paramType);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
/* 57 */     Assert.state((mavContainer != null), "Errors/BindingResult argument only supported on regular handler methods");
/*    */ 
/*    */     
/* 60 */     ModelMap model = mavContainer.getModel();
/* 61 */     String lastKey = (String)CollectionUtils.lastElement(model.keySet());
/* 62 */     if (lastKey != null && lastKey.startsWith(BindingResult.MODEL_KEY_PREFIX)) {
/* 63 */       return model.get(lastKey);
/*    */     }
/*    */     
/* 66 */     throw new IllegalStateException("An Errors/BindingResult argument is expected to be declared immediately after the model attribute, the @RequestBody or the @RequestPart arguments to which they apply: " + parameter
/*    */ 
/*    */         
/* 69 */         .getMethod());
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/method/annotation/ErrorsMethodArgumentResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */