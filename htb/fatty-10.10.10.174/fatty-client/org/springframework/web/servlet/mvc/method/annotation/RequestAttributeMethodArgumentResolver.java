/*    */ package org.springframework.web.servlet.mvc.method.annotation;
/*    */ 
/*    */ import javax.servlet.ServletException;
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.web.bind.ServletRequestBindingException;
/*    */ import org.springframework.web.bind.annotation.RequestAttribute;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
/*    */ import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;
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
/*    */ public class RequestAttributeMethodArgumentResolver
/*    */   extends AbstractNamedValueMethodArgumentResolver
/*    */ {
/*    */   public boolean supportsParameter(MethodParameter parameter) {
/* 41 */     return parameter.hasParameterAnnotation(RequestAttribute.class);
/*    */   }
/*    */ 
/*    */   
/*    */   protected AbstractNamedValueMethodArgumentResolver.NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
/* 46 */     RequestAttribute ann = (RequestAttribute)parameter.getParameterAnnotation(RequestAttribute.class);
/* 47 */     Assert.state((ann != null), "No RequestAttribute annotation");
/* 48 */     return new AbstractNamedValueMethodArgumentResolver.NamedValueInfo(ann.name(), ann.required(), "\n\t\t\n\t\t\n\n\t\t\t\t\n");
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) {
/* 54 */     return request.getAttribute(name, 0);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void handleMissingValue(String name, MethodParameter parameter) throws ServletException {
/* 59 */     throw new ServletRequestBindingException("Missing request attribute '" + name + "' of type " + parameter
/* 60 */         .getNestedParameterType().getSimpleName());
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/RequestAttributeMethodArgumentResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */