/*    */ package org.springframework.web.servlet.mvc.method.annotation;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.io.Writer;
/*    */ import javax.servlet.ServletResponse;
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ServletResponseMethodArgumentResolver
/*    */   implements HandlerMethodArgumentResolver
/*    */ {
/*    */   public boolean supportsParameter(MethodParameter parameter) {
/* 49 */     Class<?> paramType = parameter.getParameterType();
/* 50 */     return (ServletResponse.class.isAssignableFrom(paramType) || OutputStream.class
/* 51 */       .isAssignableFrom(paramType) || Writer.class
/* 52 */       .isAssignableFrom(paramType));
/*    */   }
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
/*    */   public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
/* 65 */     if (mavContainer != null) {
/* 66 */       mavContainer.setRequestHandled(true);
/*    */     }
/*    */     
/* 69 */     Class<?> paramType = parameter.getParameterType();
/*    */ 
/*    */     
/* 72 */     if (ServletResponse.class.isAssignableFrom(paramType)) {
/* 73 */       return resolveNativeResponse(webRequest, paramType);
/*    */     }
/*    */ 
/*    */     
/* 77 */     return resolveArgument(paramType, resolveNativeResponse(webRequest, ServletResponse.class));
/*    */   }
/*    */   
/*    */   private <T> T resolveNativeResponse(NativeWebRequest webRequest, Class<T> requiredType) {
/* 81 */     T nativeResponse = (T)webRequest.getNativeResponse(requiredType);
/* 82 */     if (nativeResponse == null) {
/* 83 */       throw new IllegalStateException("Current response is not of type [" + requiredType
/* 84 */           .getName() + "]: " + webRequest);
/*    */     }
/* 86 */     return nativeResponse;
/*    */   }
/*    */   
/*    */   private Object resolveArgument(Class<?> paramType, ServletResponse response) throws IOException {
/* 90 */     if (OutputStream.class.isAssignableFrom(paramType)) {
/* 91 */       return response.getOutputStream();
/*    */     }
/* 93 */     if (Writer.class.isAssignableFrom(paramType)) {
/* 94 */       return response.getWriter();
/*    */     }
/*    */ 
/*    */     
/* 98 */     throw new UnsupportedOperationException("Unknown parameter type: " + paramType);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/ServletResponseMethodArgumentResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */