/*    */ package org.springframework.web.servlet.mvc.method.annotation;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonView;
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Type;
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.http.HttpInputMessage;
/*    */ import org.springframework.http.converter.HttpMessageConverter;
/*    */ import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
/*    */ import org.springframework.http.converter.json.MappingJacksonInputMessage;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class JsonViewRequestBodyAdvice
/*    */   extends RequestBodyAdviceAdapter
/*    */ {
/*    */   public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
/* 55 */     return (AbstractJackson2HttpMessageConverter.class.isAssignableFrom(converterType) && methodParameter
/* 56 */       .getParameterAnnotation(JsonView.class) != null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> selectedConverterType) throws IOException {
/* 63 */     JsonView ann = (JsonView)methodParameter.getParameterAnnotation(JsonView.class);
/* 64 */     Assert.state((ann != null), "No JsonView annotation");
/*    */     
/* 66 */     Class<?>[] classes = ann.value();
/* 67 */     if (classes.length != 1) {
/* 68 */       throw new IllegalArgumentException("@JsonView only supported for request body advice with exactly 1 class argument: " + methodParameter);
/*    */     }
/*    */ 
/*    */     
/* 72 */     return (HttpInputMessage)new MappingJacksonInputMessage(inputMessage.getBody(), inputMessage.getHeaders(), classes[0]);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/JsonViewRequestBodyAdvice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */