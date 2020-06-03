/*    */ package org.springframework.web.servlet.mvc.method.annotation;
/*    */ 
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.ui.ModelMap;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.validation.DataBinder;
/*    */ import org.springframework.web.bind.WebDataBinder;
/*    */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
/*    */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*    */ import org.springframework.web.method.support.ModelAndViewContainer;
/*    */ import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/*    */ import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RedirectAttributesMethodArgumentResolver
/*    */   implements HandlerMethodArgumentResolver
/*    */ {
/*    */   public boolean supportsParameter(MethodParameter parameter) {
/* 51 */     return RedirectAttributes.class.isAssignableFrom(parameter.getParameterType());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
/*    */     RedirectAttributesModelMap redirectAttributesModelMap;
/* 58 */     Assert.state((mavContainer != null), "RedirectAttributes argument only supported on regular handler methods");
/*    */ 
/*    */     
/* 61 */     if (binderFactory != null) {
/* 62 */       WebDataBinder webDataBinder = binderFactory.createBinder(webRequest, null, "target");
/* 63 */       redirectAttributesModelMap = new RedirectAttributesModelMap((DataBinder)webDataBinder);
/*    */     } else {
/*    */       
/* 66 */       redirectAttributesModelMap = new RedirectAttributesModelMap();
/*    */     } 
/* 68 */     mavContainer.setRedirectModel((ModelMap)redirectAttributesModelMap);
/* 69 */     return redirectAttributesModelMap;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/RedirectAttributesMethodArgumentResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */