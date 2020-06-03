/*    */ package org.springframework.web.method.annotation;
/*    */ 
/*    */ import javax.servlet.ServletException;
/*    */ import org.springframework.beans.factory.annotation.Value;
/*    */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class ExpressionValueMethodArgumentResolver
/*    */   extends AbstractNamedValueMethodArgumentResolver
/*    */ {
/*    */   public ExpressionValueMethodArgumentResolver(@Nullable ConfigurableBeanFactory beanFactory) {
/* 51 */     super(beanFactory);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean supportsParameter(MethodParameter parameter) {
/* 57 */     return parameter.hasParameterAnnotation(Value.class);
/*    */   }
/*    */ 
/*    */   
/*    */   protected AbstractNamedValueMethodArgumentResolver.NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
/* 62 */     Value ann = (Value)parameter.getParameterAnnotation(Value.class);
/* 63 */     Assert.state((ann != null), "No Value annotation");
/* 64 */     return new ExpressionValueNamedValueInfo(ann);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest webRequest) throws Exception {
/* 71 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void handleMissingValue(String name, MethodParameter parameter) throws ServletException {
/* 76 */     throw new UnsupportedOperationException("@Value is never required: " + parameter.getMethod());
/*    */   }
/*    */   
/*    */   private static final class ExpressionValueNamedValueInfo
/*    */     extends AbstractNamedValueMethodArgumentResolver.NamedValueInfo
/*    */   {
/*    */     private ExpressionValueNamedValueInfo(Value annotation) {
/* 83 */       super("@Value", false, annotation.value());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/method/annotation/ExpressionValueMethodArgumentResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */