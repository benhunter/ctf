/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Consumer;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ReactiveAdapterRegistry;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.task.TaskExecutor;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
/*     */ import org.springframework.http.server.ServerHttpResponse;
/*     */ import org.springframework.http.server.ServletServerHttpResponse;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.accept.ContentNegotiationManager;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.context.request.WebRequest;
/*     */ import org.springframework.web.context.request.async.DeferredResult;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResponseBodyEmitterReturnValueHandler
/*     */   implements HandlerMethodReturnValueHandler
/*     */ {
/*     */   private final List<HttpMessageConverter<?>> messageConverters;
/*     */   private final ReactiveTypeHandler reactiveHandler;
/*     */   
/*     */   public ResponseBodyEmitterReturnValueHandler(List<HttpMessageConverter<?>> messageConverters) {
/*  72 */     Assert.notEmpty(messageConverters, "HttpMessageConverter List must not be empty");
/*  73 */     this.messageConverters = messageConverters;
/*  74 */     this.reactiveHandler = new ReactiveTypeHandler();
/*     */   }
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
/*     */   public ResponseBodyEmitterReturnValueHandler(List<HttpMessageConverter<?>> messageConverters, ReactiveAdapterRegistry registry, TaskExecutor executor, ContentNegotiationManager manager) {
/*  88 */     Assert.notEmpty(messageConverters, "HttpMessageConverter List must not be empty");
/*  89 */     this.messageConverters = messageConverters;
/*  90 */     this.reactiveHandler = new ReactiveTypeHandler(registry, executor, manager);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportsReturnType(MethodParameter returnType) {
/*  98 */     Class<?> bodyType = ResponseEntity.class.isAssignableFrom(returnType.getParameterType()) ? ResolvableType.forMethodParameter(returnType).getGeneric(new int[0]).resolve() : returnType.getParameterType();
/*     */     
/* 100 */     return (bodyType != null && (ResponseBodyEmitter.class.isAssignableFrom(bodyType) || this.reactiveHandler
/* 101 */       .isReactiveType(bodyType)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
/*     */     ResponseBodyEmitter emitter;
/* 109 */     if (returnValue == null) {
/* 110 */       mavContainer.setRequestHandled(true);
/*     */       
/*     */       return;
/*     */     } 
/* 114 */     HttpServletResponse response = (HttpServletResponse)webRequest.getNativeResponse(HttpServletResponse.class);
/* 115 */     Assert.state((response != null), "No HttpServletResponse");
/* 116 */     ServletServerHttpResponse servletServerHttpResponse = new ServletServerHttpResponse(response);
/*     */     
/* 118 */     if (returnValue instanceof ResponseEntity) {
/* 119 */       ResponseEntity<?> responseEntity = (ResponseEntity)returnValue;
/* 120 */       response.setStatus(responseEntity.getStatusCodeValue());
/* 121 */       servletServerHttpResponse.getHeaders().putAll((Map)responseEntity.getHeaders());
/* 122 */       returnValue = responseEntity.getBody();
/* 123 */       returnType = returnType.nested();
/* 124 */       if (returnValue == null) {
/* 125 */         mavContainer.setRequestHandled(true);
/* 126 */         servletServerHttpResponse.flush();
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 131 */     ServletRequest request = (ServletRequest)webRequest.getNativeRequest(ServletRequest.class);
/* 132 */     Assert.state((request != null), "No ServletRequest");
/*     */ 
/*     */     
/* 135 */     if (returnValue instanceof ResponseBodyEmitter) {
/* 136 */       emitter = (ResponseBodyEmitter)returnValue;
/*     */     } else {
/*     */       
/* 139 */       emitter = this.reactiveHandler.handleValue(returnValue, returnType, mavContainer, webRequest);
/* 140 */       if (emitter == null) {
/*     */         
/* 142 */         servletServerHttpResponse.getHeaders().forEach((headerName, headerValues) -> {
/*     */               for (String headerValue : headerValues) {
/*     */                 response.addHeader(headerName, headerValue);
/*     */               }
/*     */             });
/*     */         return;
/*     */       } 
/*     */     } 
/* 150 */     emitter.extendResponse((ServerHttpResponse)servletServerHttpResponse);
/*     */ 
/*     */     
/* 153 */     ShallowEtagHeaderFilter.disableContentCaching(request);
/*     */ 
/*     */     
/* 156 */     servletServerHttpResponse.getBody();
/* 157 */     servletServerHttpResponse.flush();
/* 158 */     StreamingServletServerHttpResponse streamingServletServerHttpResponse = new StreamingServletServerHttpResponse((ServerHttpResponse)servletServerHttpResponse);
/*     */     
/* 160 */     DeferredResult<?> deferredResult = new DeferredResult(emitter.getTimeout());
/* 161 */     WebAsyncUtils.getAsyncManager((WebRequest)webRequest).startDeferredResultProcessing(deferredResult, new Object[] { mavContainer });
/*     */     
/* 163 */     HttpMessageConvertingHandler handler = new HttpMessageConvertingHandler(streamingServletServerHttpResponse, deferredResult);
/* 164 */     emitter.initialize(handler);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class HttpMessageConvertingHandler
/*     */     implements ResponseBodyEmitter.Handler
/*     */   {
/*     */     private final ServerHttpResponse outputMessage;
/*     */     
/*     */     private final DeferredResult<?> deferredResult;
/*     */ 
/*     */     
/*     */     public HttpMessageConvertingHandler(ServerHttpResponse outputMessage, DeferredResult<?> deferredResult) {
/* 178 */       this.outputMessage = outputMessage;
/* 179 */       this.deferredResult = deferredResult;
/*     */     }
/*     */ 
/*     */     
/*     */     public void send(Object data, @Nullable MediaType mediaType) throws IOException {
/* 184 */       sendInternal(data, mediaType);
/*     */     }
/*     */ 
/*     */     
/*     */     private <T> void sendInternal(T data, @Nullable MediaType mediaType) throws IOException {
/* 189 */       for (HttpMessageConverter<?> converter : (Iterable<HttpMessageConverter<?>>)ResponseBodyEmitterReturnValueHandler.this.messageConverters) {
/* 190 */         if (converter.canWrite(data.getClass(), mediaType)) {
/* 191 */           converter.write(data, mediaType, (HttpOutputMessage)this.outputMessage);
/* 192 */           this.outputMessage.flush();
/*     */           return;
/*     */         } 
/*     */       } 
/* 196 */       throw new IllegalArgumentException("No suitable converter for " + data.getClass());
/*     */     }
/*     */ 
/*     */     
/*     */     public void complete() {
/* 201 */       this.deferredResult.setResult(null);
/*     */     }
/*     */ 
/*     */     
/*     */     public void completeWithError(Throwable failure) {
/* 206 */       this.deferredResult.setErrorResult(failure);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onTimeout(Runnable callback) {
/* 211 */       this.deferredResult.onTimeout(callback);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Consumer<Throwable> callback) {
/* 216 */       this.deferredResult.onError(callback);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onCompletion(Runnable callback) {
/* 221 */       this.deferredResult.onCompletion(callback);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class StreamingServletServerHttpResponse
/*     */     implements ServerHttpResponse
/*     */   {
/*     */     private final ServerHttpResponse delegate;
/*     */ 
/*     */     
/* 234 */     private final HttpHeaders mutableHeaders = new HttpHeaders();
/*     */     
/*     */     public StreamingServletServerHttpResponse(ServerHttpResponse delegate) {
/* 237 */       this.delegate = delegate;
/* 238 */       this.mutableHeaders.putAll((Map)delegate.getHeaders());
/*     */     }
/*     */ 
/*     */     
/*     */     public void setStatusCode(HttpStatus status) {
/* 243 */       this.delegate.setStatusCode(status);
/*     */     }
/*     */ 
/*     */     
/*     */     public HttpHeaders getHeaders() {
/* 248 */       return this.mutableHeaders;
/*     */     }
/*     */ 
/*     */     
/*     */     public OutputStream getBody() throws IOException {
/* 253 */       return this.delegate.getBody();
/*     */     }
/*     */ 
/*     */     
/*     */     public void flush() throws IOException {
/* 258 */       this.delegate.flush();
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() {
/* 263 */       this.delegate.close();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/ResponseBodyEmitterReturnValueHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */