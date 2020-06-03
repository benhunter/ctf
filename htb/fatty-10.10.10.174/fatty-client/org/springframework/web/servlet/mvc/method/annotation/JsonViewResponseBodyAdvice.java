/*    */ package org.springframework.web.servlet.mvc.method.annotation;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonView;
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.http.MediaType;
/*    */ import org.springframework.http.converter.HttpMessageConverter;
/*    */ import org.springframework.http.converter.json.MappingJacksonValue;
/*    */ import org.springframework.http.server.ServerHttpRequest;
/*    */ import org.springframework.http.server.ServerHttpResponse;
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
/*    */ public class JsonViewResponseBodyAdvice
/*    */   extends AbstractMappingJacksonResponseBodyAdvice
/*    */ {
/*    */   public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
/* 51 */     return (super.supports(returnType, converterType) && returnType.hasMethodAnnotation(JsonView.class));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType, MethodParameter returnType, ServerHttpRequest request, ServerHttpResponse response) {
/* 58 */     JsonView ann = (JsonView)returnType.getMethodAnnotation(JsonView.class);
/* 59 */     Assert.state((ann != null), "No JsonView annotation");
/*    */     
/* 61 */     Class<?>[] classes = ann.value();
/* 62 */     if (classes.length != 1) {
/* 63 */       throw new IllegalArgumentException("@JsonView only supported for response body advice with exactly 1 class argument: " + returnType);
/*    */     }
/*    */ 
/*    */     
/* 67 */     bodyContainer.setSerializationView(classes[0]);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/JsonViewResponseBodyAdvice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */