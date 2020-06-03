/*    */ package org.springframework.web.method.annotation;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.LinkedMultiValueMap;
/*    */ import org.springframework.util.MultiValueMap;
/*    */ import org.springframework.web.bind.annotation.RequestHeader;
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
/*    */ public class RequestHeaderMapMethodArgumentResolver
/*    */   implements HandlerMethodArgumentResolver
/*    */ {
/*    */   public boolean supportsParameter(MethodParameter parameter) {
/* 51 */     return (parameter.hasParameterAnnotation(RequestHeader.class) && Map.class
/* 52 */       .isAssignableFrom(parameter.getParameterType()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
/* 59 */     Class<?> paramType = parameter.getParameterType();
/* 60 */     if (MultiValueMap.class.isAssignableFrom(paramType)) {
/*    */       LinkedMultiValueMap linkedMultiValueMap;
/* 62 */       if (HttpHeaders.class.isAssignableFrom(paramType)) {
/* 63 */         HttpHeaders httpHeaders = new HttpHeaders();
/*    */       } else {
/*    */         
/* 66 */         linkedMultiValueMap = new LinkedMultiValueMap();
/*    */       } 
/* 68 */       for (Iterator<String> iterator1 = webRequest.getHeaderNames(); iterator1.hasNext(); ) {
/* 69 */         String headerName = iterator1.next();
/* 70 */         String[] headerValues = webRequest.getHeaderValues(headerName);
/* 71 */         if (headerValues != null) {
/* 72 */           for (String headerValue : headerValues) {
/* 73 */             linkedMultiValueMap.add(headerName, headerValue);
/*    */           }
/*    */         }
/*    */       } 
/* 77 */       return linkedMultiValueMap;
/*    */     } 
/*    */     
/* 80 */     Map<String, String> result = new LinkedHashMap<>();
/* 81 */     for (Iterator<String> iterator = webRequest.getHeaderNames(); iterator.hasNext(); ) {
/* 82 */       String headerName = iterator.next();
/* 83 */       String headerValue = webRequest.getHeader(headerName);
/* 84 */       if (headerValue != null) {
/* 85 */         result.put(headerName, headerValue);
/*    */       }
/*    */     } 
/* 88 */     return result;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/method/annotation/RequestHeaderMapMethodArgumentResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */