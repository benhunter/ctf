/*    */ package org.springframework.web.method.annotation;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.web.bind.MissingRequestHeaderException;
/*    */ import org.springframework.web.bind.ServletRequestBindingException;
/*    */ import org.springframework.web.bind.annotation.RequestHeader;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RequestHeaderMethodArgumentResolver
/*    */   extends AbstractNamedValueMethodArgumentResolver
/*    */ {
/*    */   public RequestHeaderMethodArgumentResolver(@Nullable ConfigurableBeanFactory beanFactory) {
/* 56 */     super(beanFactory);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean supportsParameter(MethodParameter parameter) {
/* 62 */     return (parameter.hasParameterAnnotation(RequestHeader.class) && 
/* 63 */       !Map.class.isAssignableFrom(parameter.nestedIfOptional().getNestedParameterType()));
/*    */   }
/*    */ 
/*    */   
/*    */   protected AbstractNamedValueMethodArgumentResolver.NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
/* 68 */     RequestHeader ann = (RequestHeader)parameter.getParameterAnnotation(RequestHeader.class);
/* 69 */     Assert.state((ann != null), "No RequestHeader annotation");
/* 70 */     return new RequestHeaderNamedValueInfo(ann);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
/* 76 */     String[] headerValues = request.getHeaderValues(name);
/* 77 */     if (headerValues != null) {
/* 78 */       return (headerValues.length == 1) ? headerValues[0] : headerValues;
/*    */     }
/*    */     
/* 81 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void handleMissingValue(String name, MethodParameter parameter) throws ServletRequestBindingException {
/* 87 */     throw new MissingRequestHeaderException(name, parameter);
/*    */   }
/*    */   
/*    */   private static final class RequestHeaderNamedValueInfo
/*    */     extends AbstractNamedValueMethodArgumentResolver.NamedValueInfo
/*    */   {
/*    */     private RequestHeaderNamedValueInfo(RequestHeader annotation) {
/* 94 */       super(annotation.name(), annotation.required(), annotation.defaultValue());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/method/annotation/RequestHeaderMethodArgumentResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */