/*    */ package org.springframework.web.servlet.mvc.method.annotation;
/*    */ 
/*    */ import java.util.Map;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.http.server.ServletServerHttpResponse;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
/*    */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
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
/*    */ public class HttpHeadersReturnValueHandler
/*    */   implements HandlerMethodReturnValueHandler
/*    */ {
/*    */   public boolean supportsReturnType(MethodParameter returnType) {
/* 40 */     return HttpHeaders.class.isAssignableFrom(returnType.getParameterType());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
/* 48 */     mavContainer.setRequestHandled(true);
/*    */     
/* 50 */     Assert.state(returnValue instanceof HttpHeaders, "HttpHeaders expected");
/* 51 */     HttpHeaders headers = (HttpHeaders)returnValue;
/*    */     
/* 53 */     if (!headers.isEmpty()) {
/* 54 */       HttpServletResponse servletResponse = (HttpServletResponse)webRequest.getNativeResponse(HttpServletResponse.class);
/* 55 */       Assert.state((servletResponse != null), "No HttpServletResponse");
/* 56 */       ServletServerHttpResponse outputMessage = new ServletServerHttpResponse(servletResponse);
/* 57 */       outputMessage.getHeaders().putAll((Map)headers);
/* 58 */       outputMessage.getBody();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/HttpHeadersReturnValueHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */