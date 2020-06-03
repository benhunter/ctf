/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.core.Conventions;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageNotReadableException;
/*     */ import org.springframework.http.converter.HttpMessageNotWritableException;
/*     */ import org.springframework.http.server.ServletServerHttpRequest;
/*     */ import org.springframework.http.server.ServletServerHttpResponse;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.validation.BindingResult;
/*     */ import org.springframework.web.HttpMediaTypeNotAcceptableException;
/*     */ import org.springframework.web.HttpMediaTypeNotSupportedException;
/*     */ import org.springframework.web.accept.ContentNegotiationManager;
/*     */ import org.springframework.web.bind.MethodArgumentNotValidException;
/*     */ import org.springframework.web.bind.WebDataBinder;
/*     */ import org.springframework.web.bind.annotation.RequestBody;
/*     */ import org.springframework.web.bind.annotation.ResponseBody;
/*     */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RequestResponseBodyMethodProcessor
/*     */   extends AbstractMessageConverterMethodProcessor
/*     */ {
/*     */   public RequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> converters) {
/*  70 */     super(converters);
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
/*     */   public RequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> converters, @Nullable ContentNegotiationManager manager) {
/*  82 */     super(converters, manager);
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
/*     */   public RequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> converters, @Nullable List<Object> requestResponseBodyAdvice) {
/*  94 */     super(converters, (ContentNegotiationManager)null, requestResponseBodyAdvice);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> converters, @Nullable ContentNegotiationManager manager, @Nullable List<Object> requestResponseBodyAdvice) {
/* 104 */     super(converters, manager, requestResponseBodyAdvice);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportsParameter(MethodParameter parameter) {
/* 110 */     return parameter.hasParameterAnnotation(RequestBody.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsReturnType(MethodParameter returnType) {
/* 115 */     return (AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ResponseBody.class) || returnType
/* 116 */       .hasMethodAnnotation(ResponseBody.class));
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
/*     */   public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
/* 129 */     parameter = parameter.nestedIfOptional();
/* 130 */     Object arg = readWithMessageConverters(webRequest, parameter, parameter.getNestedGenericParameterType());
/* 131 */     String name = Conventions.getVariableNameForParameter(parameter);
/*     */     
/* 133 */     if (binderFactory != null) {
/* 134 */       WebDataBinder binder = binderFactory.createBinder(webRequest, arg, name);
/* 135 */       if (arg != null) {
/* 136 */         validateIfApplicable(binder, parameter);
/* 137 */         if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
/* 138 */           throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
/*     */         }
/*     */       } 
/* 141 */       if (mavContainer != null) {
/* 142 */         mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());
/*     */       }
/*     */     } 
/*     */     
/* 146 */     return adaptArgumentIfNecessary(arg, parameter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T> Object readWithMessageConverters(NativeWebRequest webRequest, MethodParameter parameter, Type paramType) throws IOException, HttpMediaTypeNotSupportedException, HttpMessageNotReadableException {
/* 153 */     HttpServletRequest servletRequest = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/* 154 */     Assert.state((servletRequest != null), "No HttpServletRequest");
/* 155 */     ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(servletRequest);
/*     */     
/* 157 */     Object arg = readWithMessageConverters((HttpInputMessage)inputMessage, parameter, paramType);
/* 158 */     if (arg == null && checkRequired(parameter)) {
/* 159 */       throw new HttpMessageNotReadableException("Required request body is missing: " + parameter
/* 160 */           .getExecutable().toGenericString(), inputMessage);
/*     */     }
/* 162 */     return arg;
/*     */   }
/*     */   
/*     */   protected boolean checkRequired(MethodParameter parameter) {
/* 166 */     RequestBody requestBody = (RequestBody)parameter.getParameterAnnotation(RequestBody.class);
/* 167 */     return (requestBody != null && requestBody.required() && !parameter.isOptional());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException {
/* 175 */     mavContainer.setRequestHandled(true);
/* 176 */     ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
/* 177 */     ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);
/*     */ 
/*     */     
/* 180 */     writeWithMessageConverters(returnValue, returnType, inputMessage, outputMessage);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/RequestResponseBodyMethodProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */