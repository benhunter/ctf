/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
/*     */ import org.springframework.http.server.ServerHttpRequest;
/*     */ import org.springframework.http.server.ServerHttpResponse;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.web.method.ControllerAdviceBean;
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
/*     */ class RequestResponseBodyAdviceChain
/*     */   implements RequestBodyAdvice, ResponseBodyAdvice<Object>
/*     */ {
/*  45 */   private final List<Object> requestBodyAdvice = new ArrayList(4);
/*     */   
/*  47 */   private final List<Object> responseBodyAdvice = new ArrayList(4);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RequestResponseBodyAdviceChain(@Nullable List<Object> requestResponseBodyAdvice) {
/*  55 */     this.requestBodyAdvice.addAll(getAdviceByType(requestResponseBodyAdvice, RequestBodyAdvice.class));
/*  56 */     this.responseBodyAdvice.addAll(getAdviceByType(requestResponseBodyAdvice, ResponseBodyAdvice.class));
/*     */   }
/*     */ 
/*     */   
/*     */   static <T> List<T> getAdviceByType(@Nullable List<Object> requestResponseBodyAdvice, Class<T> adviceType) {
/*  61 */     if (requestResponseBodyAdvice != null) {
/*  62 */       List<T> result = new ArrayList<>();
/*  63 */       for (Object advice : requestResponseBodyAdvice) {
/*     */         
/*  65 */         Class<?> beanType = (advice instanceof ControllerAdviceBean) ? ((ControllerAdviceBean)advice).getBeanType() : advice.getClass();
/*  66 */         if (beanType != null && adviceType.isAssignableFrom(beanType)) {
/*  67 */           result.add((T)advice);
/*     */         }
/*     */       } 
/*  70 */       return result;
/*     */     } 
/*  72 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supports(MethodParameter param, Type type, Class<? extends HttpMessageConverter<?>> converterType) {
/*  78 */     throw new UnsupportedOperationException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
/*  83 */     throw new UnsupportedOperationException("Not implemented");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpInputMessage beforeBodyRead(HttpInputMessage request, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
/*  90 */     for (RequestBodyAdvice advice : getMatchingAdvice(parameter, RequestBodyAdvice.class)) {
/*  91 */       if (advice.supports(parameter, targetType, converterType)) {
/*  92 */         request = advice.beforeBodyRead(request, parameter, targetType, converterType);
/*     */       }
/*     */     } 
/*  95 */     return request;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
/* 102 */     for (RequestBodyAdvice advice : getMatchingAdvice(parameter, RequestBodyAdvice.class)) {
/* 103 */       if (advice.supports(parameter, targetType, converterType)) {
/* 104 */         body = advice.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
/*     */       }
/*     */     } 
/* 107 */     return body;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType contentType, Class<? extends HttpMessageConverter<?>> converterType, ServerHttpRequest request, ServerHttpResponse response) {
/* 116 */     return processBody(body, returnType, contentType, converterType, request, response);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object handleEmptyBody(@Nullable Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
/* 124 */     for (RequestBodyAdvice advice : getMatchingAdvice(parameter, RequestBodyAdvice.class)) {
/* 125 */       if (advice.supports(parameter, targetType, converterType)) {
/* 126 */         body = advice.handleEmptyBody(body, inputMessage, parameter, targetType, converterType);
/*     */       }
/*     */     } 
/* 129 */     return body;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private <T> Object processBody(@Nullable Object body, MethodParameter returnType, MediaType contentType, Class<? extends HttpMessageConverter<?>> converterType, ServerHttpRequest request, ServerHttpResponse response) {
/* 139 */     for (ResponseBodyAdvice<?> advice : getMatchingAdvice(returnType, ResponseBodyAdvice.class)) {
/* 140 */       if (advice.supports(returnType, converterType)) {
/* 141 */         body = advice.beforeBodyWrite(body, returnType, contentType, converterType, request, response);
/*     */       }
/*     */     } 
/*     */     
/* 145 */     return body;
/*     */   }
/*     */ 
/*     */   
/*     */   private <A> List<A> getMatchingAdvice(MethodParameter parameter, Class<? extends A> adviceType) {
/* 150 */     List<Object> availableAdvice = getAdvice(adviceType);
/* 151 */     if (CollectionUtils.isEmpty(availableAdvice)) {
/* 152 */       return Collections.emptyList();
/*     */     }
/* 154 */     List<A> result = new ArrayList<>(availableAdvice.size());
/* 155 */     for (Object advice : availableAdvice) {
/* 156 */       if (advice instanceof ControllerAdviceBean) {
/* 157 */         ControllerAdviceBean adviceBean = (ControllerAdviceBean)advice;
/* 158 */         if (!adviceBean.isApplicableToBeanType(parameter.getContainingClass())) {
/*     */           continue;
/*     */         }
/* 161 */         advice = adviceBean.resolveBean();
/*     */       } 
/* 163 */       if (adviceType.isAssignableFrom(advice.getClass())) {
/* 164 */         result.add((A)advice);
/*     */       }
/*     */     } 
/* 167 */     return result;
/*     */   }
/*     */   
/*     */   private List<Object> getAdvice(Class<?> adviceType) {
/* 171 */     if (RequestBodyAdvice.class == adviceType) {
/* 172 */       return this.requestBodyAdvice;
/*     */     }
/* 174 */     if (ResponseBodyAdvice.class == adviceType) {
/* 175 */       return this.responseBodyAdvice;
/*     */     }
/*     */     
/* 178 */     throw new IllegalArgumentException("Unexpected adviceType: " + adviceType);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/RequestResponseBodyAdviceChain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */