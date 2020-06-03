/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.http.HttpEntity;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.RequestEntity;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
/*     */ import org.springframework.http.server.ServletServerHttpRequest;
/*     */ import org.springframework.http.server.ServletServerHttpResponse;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.ui.ModelMap;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.HttpMediaTypeNotSupportedException;
/*     */ import org.springframework.web.accept.ContentNegotiationManager;
/*     */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.context.request.ServletWebRequest;
/*     */ import org.springframework.web.filter.ShallowEtagHeaderFilter;
/*     */ import org.springframework.web.method.support.ModelAndViewContainer;
/*     */ import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/*     */ import org.springframework.web.servlet.support.RequestContextUtils;
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
/*     */ public class HttpEntityMethodProcessor
/*     */   extends AbstractMessageConverterMethodProcessor
/*     */ {
/*  72 */   private static final Set<HttpMethod> SAFE_METHODS = EnumSet.of(HttpMethod.GET, HttpMethod.HEAD);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpEntityMethodProcessor(List<HttpMessageConverter<?>> converters) {
/*  80 */     super(converters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpEntityMethodProcessor(List<HttpMessageConverter<?>> converters, ContentNegotiationManager manager) {
/*  91 */     super(converters, manager);
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
/*     */   public HttpEntityMethodProcessor(List<HttpMessageConverter<?>> converters, List<Object> requestResponseBodyAdvice) {
/* 103 */     super(converters, (ContentNegotiationManager)null, requestResponseBodyAdvice);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpEntityMethodProcessor(List<HttpMessageConverter<?>> converters, @Nullable ContentNegotiationManager manager, List<Object> requestResponseBodyAdvice) {
/* 113 */     super(converters, manager, requestResponseBodyAdvice);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportsParameter(MethodParameter parameter) {
/* 119 */     return (HttpEntity.class == parameter.getParameterType() || RequestEntity.class == parameter
/* 120 */       .getParameterType());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsReturnType(MethodParameter returnType) {
/* 125 */     return (HttpEntity.class.isAssignableFrom(returnType.getParameterType()) && 
/* 126 */       !RequestEntity.class.isAssignableFrom(returnType.getParameterType()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws IOException, HttpMediaTypeNotSupportedException {
/* 135 */     ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
/* 136 */     Type paramType = getHttpEntityType(parameter);
/* 137 */     if (paramType == null) {
/* 138 */       throw new IllegalArgumentException("HttpEntity parameter '" + parameter.getParameterName() + "' in method " + parameter
/* 139 */           .getMethod() + " is not parameterized");
/*     */     }
/*     */     
/* 142 */     Object body = readWithMessageConverters(webRequest, parameter, paramType);
/* 143 */     if (RequestEntity.class == parameter.getParameterType()) {
/* 144 */       return new RequestEntity(body, (MultiValueMap)inputMessage.getHeaders(), inputMessage
/* 145 */           .getMethod(), inputMessage.getURI());
/*     */     }
/*     */     
/* 148 */     return new HttpEntity(body, (MultiValueMap)inputMessage.getHeaders());
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Type getHttpEntityType(MethodParameter parameter) {
/* 154 */     Assert.isAssignable(HttpEntity.class, parameter.getParameterType());
/* 155 */     Type parameterType = parameter.getGenericParameterType();
/* 156 */     if (parameterType instanceof ParameterizedType) {
/* 157 */       ParameterizedType type = (ParameterizedType)parameterType;
/* 158 */       if ((type.getActualTypeArguments()).length != 1) {
/* 159 */         throw new IllegalArgumentException("Expected single generic parameter on '" + parameter
/* 160 */             .getParameterName() + "' in method " + parameter.getMethod());
/*     */       }
/* 162 */       return type.getActualTypeArguments()[0];
/*     */     } 
/* 164 */     if (parameterType instanceof Class) {
/* 165 */       return Object.class;
/*     */     }
/*     */     
/* 168 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
/* 176 */     mavContainer.setRequestHandled(true);
/* 177 */     if (returnValue == null) {
/*     */       return;
/*     */     }
/*     */     
/* 181 */     ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
/* 182 */     ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);
/*     */     
/* 184 */     Assert.isInstanceOf(HttpEntity.class, returnValue);
/* 185 */     HttpEntity<?> responseEntity = (HttpEntity)returnValue;
/*     */     
/* 187 */     HttpHeaders outputHeaders = outputMessage.getHeaders();
/* 188 */     HttpHeaders entityHeaders = responseEntity.getHeaders();
/* 189 */     if (!entityHeaders.isEmpty()) {
/* 190 */       entityHeaders.forEach((key, value) -> {
/*     */             if ("Vary".equals(key) && outputHeaders.containsKey("Vary")) {
/*     */               List<String> values = getVaryRequestHeadersToAdd(outputHeaders, entityHeaders);
/*     */               
/*     */               if (!values.isEmpty()) {
/*     */                 outputHeaders.setVary(values);
/*     */               }
/*     */             } else {
/*     */               outputHeaders.put(key, value);
/*     */             } 
/*     */           });
/*     */     }
/*     */     
/* 203 */     if (responseEntity instanceof ResponseEntity) {
/* 204 */       int returnStatus = ((ResponseEntity)responseEntity).getStatusCodeValue();
/* 205 */       outputMessage.getServletResponse().setStatus(returnStatus);
/* 206 */       if (returnStatus == 200) {
/* 207 */         if (SAFE_METHODS.contains(inputMessage.getMethod()) && 
/* 208 */           isResourceNotModified(inputMessage, outputMessage)) {
/*     */           
/* 210 */           outputMessage.flush();
/* 211 */           ShallowEtagHeaderFilter.disableContentCaching((ServletRequest)inputMessage.getServletRequest());
/*     */ 
/*     */           
/*     */           return;
/*     */         } 
/* 216 */       } else if (returnStatus / 100 == 3) {
/* 217 */         String location = outputHeaders.getFirst("location");
/* 218 */         if (location != null) {
/* 219 */           saveFlashAttributes(mavContainer, webRequest, location);
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 225 */     writeWithMessageConverters(responseEntity.getBody(), returnType, inputMessage, outputMessage);
/*     */ 
/*     */     
/* 228 */     outputMessage.flush();
/*     */   }
/*     */   
/*     */   private List<String> getVaryRequestHeadersToAdd(HttpHeaders responseHeaders, HttpHeaders entityHeaders) {
/* 232 */     List<String> entityHeadersVary = entityHeaders.getVary();
/* 233 */     List<String> vary = responseHeaders.get("Vary");
/* 234 */     if (vary != null) {
/* 235 */       List<String> result = new ArrayList<>(entityHeadersVary);
/* 236 */       for (String header : vary) {
/* 237 */         for (String existing : StringUtils.tokenizeToStringArray(header, ",")) {
/* 238 */           if ("*".equals(existing)) {
/* 239 */             return Collections.emptyList();
/*     */           }
/* 241 */           for (String value : entityHeadersVary) {
/* 242 */             if (value.equalsIgnoreCase(existing)) {
/* 243 */               result.remove(value);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/* 248 */       return result;
/*     */     } 
/* 250 */     return entityHeadersVary;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isResourceNotModified(ServletServerHttpRequest request, ServletServerHttpResponse response) {
/* 255 */     ServletWebRequest servletWebRequest = new ServletWebRequest(request.getServletRequest(), response.getServletResponse());
/* 256 */     HttpHeaders responseHeaders = response.getHeaders();
/* 257 */     String etag = responseHeaders.getETag();
/* 258 */     long lastModifiedTimestamp = responseHeaders.getLastModified();
/* 259 */     if (request.getMethod() == HttpMethod.GET || request.getMethod() == HttpMethod.HEAD) {
/* 260 */       responseHeaders.remove("ETag");
/* 261 */       responseHeaders.remove("Last-Modified");
/*     */     } 
/*     */     
/* 264 */     return servletWebRequest.checkNotModified(etag, lastModifiedTimestamp);
/*     */   }
/*     */   
/*     */   private void saveFlashAttributes(ModelAndViewContainer mav, NativeWebRequest request, String location) {
/* 268 */     mav.setRedirectModelScenario(true);
/* 269 */     ModelMap model = mav.getModel();
/* 270 */     if (model instanceof RedirectAttributes) {
/* 271 */       Map<String, ?> flashAttributes = ((RedirectAttributes)model).getFlashAttributes();
/* 272 */       if (!CollectionUtils.isEmpty(flashAttributes)) {
/* 273 */         HttpServletRequest req = (HttpServletRequest)request.getNativeRequest(HttpServletRequest.class);
/* 274 */         HttpServletResponse res = (HttpServletResponse)request.getNativeResponse(HttpServletResponse.class);
/* 275 */         if (req != null) {
/* 276 */           RequestContextUtils.getOutputFlashMap(req).putAll(flashAttributes);
/* 277 */           if (res != null) {
/* 278 */             RequestContextUtils.saveOutputFlashMap(location, req, res);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected Class<?> getReturnValueType(@Nullable Object returnValue, MethodParameter returnType) {
/* 287 */     if (returnValue != null) {
/* 288 */       return returnValue.getClass();
/*     */     }
/*     */     
/* 291 */     Type type = getHttpEntityType(returnType);
/* 292 */     type = (type != null) ? type : Object.class;
/* 293 */     return ResolvableType.forMethodParameter(returnType, type).toClass();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/HttpEntityMethodProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */