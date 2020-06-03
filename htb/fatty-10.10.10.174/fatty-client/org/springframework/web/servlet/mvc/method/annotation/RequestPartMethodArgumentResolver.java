/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.validation.BindingResult;
/*     */ import org.springframework.web.bind.MethodArgumentNotValidException;
/*     */ import org.springframework.web.bind.WebDataBinder;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ import org.springframework.web.bind.annotation.RequestPart;
/*     */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.method.support.ModelAndViewContainer;
/*     */ import org.springframework.web.multipart.MultipartException;
/*     */ import org.springframework.web.multipart.support.MissingServletRequestPartException;
/*     */ import org.springframework.web.multipart.support.MultipartResolutionDelegate;
/*     */ import org.springframework.web.multipart.support.RequestPartServletServerHttpRequest;
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
/*     */ public class RequestPartMethodArgumentResolver
/*     */   extends AbstractMessageConverterMethodArgumentResolver
/*     */ {
/*     */   public RequestPartMethodArgumentResolver(List<HttpMessageConverter<?>> messageConverters) {
/*  75 */     super(messageConverters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RequestPartMethodArgumentResolver(List<HttpMessageConverter<?>> messageConverters, List<Object> requestResponseBodyAdvice) {
/*  85 */     super(messageConverters, requestResponseBodyAdvice);
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
/*     */ 
/*     */   
/*     */   public boolean supportsParameter(MethodParameter parameter) {
/* 101 */     if (parameter.hasParameterAnnotation(RequestPart.class)) {
/* 102 */       return true;
/*     */     }
/*     */     
/* 105 */     if (parameter.hasParameterAnnotation(RequestParam.class)) {
/* 106 */       return false;
/*     */     }
/* 108 */     return MultipartResolutionDelegate.isMultipartArgument(parameter.nestedIfOptional());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest request, @Nullable WebDataBinderFactory binderFactory) throws Exception {
/* 117 */     HttpServletRequest servletRequest = (HttpServletRequest)request.getNativeRequest(HttpServletRequest.class);
/* 118 */     Assert.state((servletRequest != null), "No HttpServletRequest");
/*     */     
/* 120 */     RequestPart requestPart = (RequestPart)parameter.getParameterAnnotation(RequestPart.class);
/* 121 */     boolean isRequired = ((requestPart == null || requestPart.required()) && !parameter.isOptional());
/*     */     
/* 123 */     String name = getPartName(parameter, requestPart);
/* 124 */     parameter = parameter.nestedIfOptional();
/* 125 */     Object arg = null;
/*     */     
/* 127 */     Object mpArg = MultipartResolutionDelegate.resolveMultipartArgument(name, parameter, servletRequest);
/* 128 */     if (mpArg != MultipartResolutionDelegate.UNRESOLVABLE) {
/* 129 */       arg = mpArg;
/*     */     } else {
/*     */       
/*     */       try {
/* 133 */         RequestPartServletServerHttpRequest requestPartServletServerHttpRequest = new RequestPartServletServerHttpRequest(servletRequest, name);
/* 134 */         arg = readWithMessageConverters((HttpInputMessage)requestPartServletServerHttpRequest, parameter, parameter.getNestedGenericParameterType());
/* 135 */         if (binderFactory != null) {
/* 136 */           WebDataBinder binder = binderFactory.createBinder(request, arg, name);
/* 137 */           if (arg != null) {
/* 138 */             validateIfApplicable(binder, parameter);
/* 139 */             if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
/* 140 */               throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
/*     */             }
/*     */           } 
/* 143 */           if (mavContainer != null) {
/* 144 */             mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());
/*     */           }
/*     */         }
/*     */       
/* 148 */       } catch (MissingServletRequestPartException|MultipartException ex) {
/* 149 */         if (isRequired) {
/* 150 */           throw ex;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 155 */     if (arg == null && isRequired) {
/* 156 */       if (!MultipartResolutionDelegate.isMultipartRequest(servletRequest)) {
/* 157 */         throw new MultipartException("Current request is not a multipart request");
/*     */       }
/*     */       
/* 160 */       throw new MissingServletRequestPartException(name);
/*     */     } 
/*     */     
/* 163 */     return adaptArgumentIfNecessary(arg, parameter);
/*     */   }
/*     */   
/*     */   private String getPartName(MethodParameter methodParam, @Nullable RequestPart requestPart) {
/* 167 */     String partName = (requestPart != null) ? requestPart.name() : "";
/* 168 */     if (partName.isEmpty()) {
/* 169 */       partName = methodParam.getParameterName();
/* 170 */       if (partName == null) {
/* 171 */         throw new IllegalArgumentException("Request part name for argument type [" + methodParam
/* 172 */             .getNestedParameterType().getName() + "] not specified, and parameter name information not found in class file either.");
/*     */       }
/*     */     } 
/*     */     
/* 176 */     return partName;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/RequestPartMethodArgumentResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */