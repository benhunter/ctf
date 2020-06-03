/*    */ package org.springframework.web.servlet.mvc.method.annotation;
/*    */ 
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.http.MediaType;
/*    */ import org.springframework.http.converter.HttpMessageConverter;
/*    */ import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
/*    */ import org.springframework.http.converter.json.MappingJacksonValue;
/*    */ import org.springframework.http.server.ServerHttpRequest;
/*    */ import org.springframework.http.server.ServerHttpResponse;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ public abstract class AbstractMappingJacksonResponseBodyAdvice
/*    */   implements ResponseBodyAdvice<Object>
/*    */ {
/*    */   public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
/* 41 */     return AbstractJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public final Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType contentType, Class<? extends HttpMessageConverter<?>> converterType, ServerHttpRequest request, ServerHttpResponse response) {
/* 50 */     if (body == null) {
/* 51 */       return null;
/*    */     }
/* 53 */     MappingJacksonValue container = getOrCreateContainer(body);
/* 54 */     beforeBodyWriteInternal(container, contentType, returnType, request, response);
/* 55 */     return container;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected MappingJacksonValue getOrCreateContainer(Object body) {
/* 63 */     return (body instanceof MappingJacksonValue) ? (MappingJacksonValue)body : new MappingJacksonValue(body);
/*    */   }
/*    */   
/*    */   protected abstract void beforeBodyWriteInternal(MappingJacksonValue paramMappingJacksonValue, MediaType paramMediaType, MethodParameter paramMethodParameter, ServerHttpRequest paramServerHttpRequest, ServerHttpResponse paramServerHttpResponse);
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/AbstractMappingJacksonResponseBodyAdvice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */