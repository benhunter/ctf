/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.io.OutputStream;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Callable;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.http.server.ServletServerHttpResponse;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.context.request.WebRequest;
/*     */ import org.springframework.web.context.request.async.WebAsyncUtils;
/*     */ import org.springframework.web.filter.ShallowEtagHeaderFilter;
/*     */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*     */ import org.springframework.web.method.support.ModelAndViewContainer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StreamingResponseBodyReturnValueHandler
/*     */   implements HandlerMethodReturnValueHandler
/*     */ {
/*     */   public boolean supportsReturnType(MethodParameter returnType) {
/*  49 */     if (StreamingResponseBody.class.isAssignableFrom(returnType.getParameterType())) {
/*  50 */       return true;
/*     */     }
/*  52 */     if (ResponseEntity.class.isAssignableFrom(returnType.getParameterType())) {
/*  53 */       Class<?> bodyType = ResolvableType.forMethodParameter(returnType).getGeneric(new int[0]).resolve();
/*  54 */       return (bodyType != null && StreamingResponseBody.class.isAssignableFrom(bodyType));
/*     */     } 
/*  56 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
/*  64 */     if (returnValue == null) {
/*  65 */       mavContainer.setRequestHandled(true);
/*     */       
/*     */       return;
/*     */     } 
/*  69 */     HttpServletResponse response = (HttpServletResponse)webRequest.getNativeResponse(HttpServletResponse.class);
/*  70 */     Assert.state((response != null), "No HttpServletResponse");
/*  71 */     ServletServerHttpResponse servletServerHttpResponse = new ServletServerHttpResponse(response);
/*     */     
/*  73 */     if (returnValue instanceof ResponseEntity) {
/*  74 */       ResponseEntity<?> responseEntity = (ResponseEntity)returnValue;
/*  75 */       response.setStatus(responseEntity.getStatusCodeValue());
/*  76 */       servletServerHttpResponse.getHeaders().putAll((Map)responseEntity.getHeaders());
/*  77 */       returnValue = responseEntity.getBody();
/*  78 */       if (returnValue == null) {
/*  79 */         mavContainer.setRequestHandled(true);
/*  80 */         servletServerHttpResponse.flush();
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*  85 */     ServletRequest request = (ServletRequest)webRequest.getNativeRequest(ServletRequest.class);
/*  86 */     Assert.state((request != null), "No ServletRequest");
/*  87 */     ShallowEtagHeaderFilter.disableContentCaching(request);
/*     */     
/*  89 */     Assert.isInstanceOf(StreamingResponseBody.class, returnValue, "StreamingResponseBody expected");
/*  90 */     StreamingResponseBody streamingBody = (StreamingResponseBody)returnValue;
/*     */     
/*  92 */     Callable<Void> callable = new StreamingResponseBodyTask(servletServerHttpResponse.getBody(), streamingBody);
/*  93 */     WebAsyncUtils.getAsyncManager((WebRequest)webRequest).startCallableProcessing(callable, new Object[] { mavContainer });
/*     */   }
/*     */ 
/*     */   
/*     */   private static class StreamingResponseBodyTask
/*     */     implements Callable<Void>
/*     */   {
/*     */     private final OutputStream outputStream;
/*     */     private final StreamingResponseBody streamingBody;
/*     */     
/*     */     public StreamingResponseBodyTask(OutputStream outputStream, StreamingResponseBody streamingBody) {
/* 104 */       this.outputStream = outputStream;
/* 105 */       this.streamingBody = streamingBody;
/*     */     }
/*     */ 
/*     */     
/*     */     public Void call() throws Exception {
/* 110 */       this.streamingBody.writeTo(this.outputStream);
/* 111 */       this.outputStream.flush();
/* 112 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/StreamingResponseBodyReturnValueHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */