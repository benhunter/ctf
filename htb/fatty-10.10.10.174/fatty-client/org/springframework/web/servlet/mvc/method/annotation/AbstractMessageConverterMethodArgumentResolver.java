/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PushbackInputStream;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.core.log.LogFormatUtils;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.HttpRequest;
/*     */ import org.springframework.http.InvalidMediaTypeException;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.GenericHttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageNotReadableException;
/*     */ import org.springframework.http.server.ServletServerHttpRequest;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StreamUtils;
/*     */ import org.springframework.validation.Errors;
/*     */ import org.springframework.validation.annotation.Validated;
/*     */ import org.springframework.web.HttpMediaTypeNotSupportedException;
/*     */ import org.springframework.web.bind.WebDataBinder;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
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
/*     */ public abstract class AbstractMessageConverterMethodArgumentResolver
/*     */   implements HandlerMethodArgumentResolver
/*     */ {
/*  73 */   private static final Set<HttpMethod> SUPPORTED_METHODS = EnumSet.of(HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH);
/*     */   
/*  75 */   private static final Object NO_VALUE = new Object();
/*     */ 
/*     */   
/*  78 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */   
/*     */   protected final List<HttpMessageConverter<?>> messageConverters;
/*     */ 
/*     */   
/*     */   protected final List<MediaType> allSupportedMediaTypes;
/*     */ 
/*     */   
/*     */   private final RequestResponseBodyAdviceChain advice;
/*     */ 
/*     */   
/*     */   public AbstractMessageConverterMethodArgumentResolver(List<HttpMessageConverter<?>> converters) {
/*  91 */     this(converters, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractMessageConverterMethodArgumentResolver(List<HttpMessageConverter<?>> converters, @Nullable List<Object> requestResponseBodyAdvice) {
/* 101 */     Assert.notEmpty(converters, "'messageConverters' must not be empty");
/* 102 */     this.messageConverters = converters;
/* 103 */     this.allSupportedMediaTypes = getAllSupportedMediaTypes(converters);
/* 104 */     this.advice = new RequestResponseBodyAdviceChain(requestResponseBodyAdvice);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static List<MediaType> getAllSupportedMediaTypes(List<HttpMessageConverter<?>> messageConverters) {
/* 113 */     Set<MediaType> allSupportedMediaTypes = new LinkedHashSet<>();
/* 114 */     for (HttpMessageConverter<?> messageConverter : messageConverters) {
/* 115 */       allSupportedMediaTypes.addAll(messageConverter.getSupportedMediaTypes());
/*     */     }
/* 117 */     List<MediaType> result = new ArrayList<>(allSupportedMediaTypes);
/* 118 */     MediaType.sortBySpecificity(result);
/* 119 */     return Collections.unmodifiableList(result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   RequestResponseBodyAdviceChain getAdvice() {
/* 129 */     return this.advice;
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
/*     */   
/*     */   @Nullable
/*     */   protected <T> Object readWithMessageConverters(NativeWebRequest webRequest, MethodParameter parameter, Type paramType) throws IOException, HttpMediaTypeNotSupportedException, HttpMessageNotReadableException {
/* 147 */     ServletServerHttpRequest servletServerHttpRequest = createInputMessage(webRequest);
/* 148 */     return readWithMessageConverters((HttpInputMessage)servletServerHttpRequest, parameter, paramType);
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
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected <T> Object readWithMessageConverters(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType) throws IOException, HttpMediaTypeNotSupportedException, HttpMessageNotReadableException {
/*     */     MediaType contentType;
/*     */     EmptyBodyCheckingHttpInputMessage message;
/* 169 */     boolean noContentType = false;
/*     */     try {
/* 171 */       contentType = inputMessage.getHeaders().getContentType();
/*     */     }
/* 173 */     catch (InvalidMediaTypeException ex) {
/* 174 */       throw new HttpMediaTypeNotSupportedException(ex.getMessage());
/*     */     } 
/* 176 */     if (contentType == null) {
/* 177 */       noContentType = true;
/* 178 */       contentType = MediaType.APPLICATION_OCTET_STREAM;
/*     */     } 
/*     */     
/* 181 */     Class<?> contextClass = parameter.getContainingClass();
/* 182 */     Class<T> targetClass = (targetType instanceof Class) ? (Class<T>)targetType : null;
/* 183 */     if (targetClass == null) {
/* 184 */       ResolvableType resolvableType = ResolvableType.forMethodParameter(parameter);
/* 185 */       targetClass = resolvableType.resolve();
/*     */     } 
/*     */     
/* 188 */     HttpMethod httpMethod = (inputMessage instanceof HttpRequest) ? ((HttpRequest)inputMessage).getMethod() : null;
/* 189 */     Object body = NO_VALUE;
/*     */ 
/*     */     
/*     */     try {
/* 193 */       message = new EmptyBodyCheckingHttpInputMessage(inputMessage);
/*     */       
/* 195 */       for (HttpMessageConverter<?> converter : this.messageConverters) {
/* 196 */         Class<HttpMessageConverter<?>> converterType = (Class)converter.getClass();
/* 197 */         GenericHttpMessageConverter<?> genericConverter = (converter instanceof GenericHttpMessageConverter) ? (GenericHttpMessageConverter)converter : null;
/*     */         
/* 199 */         if ((genericConverter != null) ? genericConverter.canRead(targetType, contextClass, contentType) : (targetClass != null && converter
/* 200 */           .canRead(targetClass, contentType))) {
/* 201 */           if (message.hasBody()) {
/*     */             
/* 203 */             HttpInputMessage msgToUse = getAdvice().beforeBodyRead(message, parameter, targetType, converterType);
/*     */             
/* 205 */             body = (genericConverter != null) ? genericConverter.read(targetType, contextClass, msgToUse) : converter.read(targetClass, msgToUse);
/* 206 */             body = getAdvice().afterBodyRead(body, msgToUse, parameter, targetType, converterType);
/*     */             break;
/*     */           } 
/* 209 */           body = getAdvice().handleEmptyBody(null, message, parameter, targetType, converterType);
/*     */ 
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 215 */     } catch (IOException ex) {
/* 216 */       throw new HttpMessageNotReadableException("I/O error while reading input message", ex, inputMessage);
/*     */     } 
/*     */     
/* 219 */     if (body == NO_VALUE) {
/* 220 */       if (httpMethod == null || !SUPPORTED_METHODS.contains(httpMethod) || (noContentType && 
/* 221 */         !message.hasBody())) {
/* 222 */         return null;
/*     */       }
/* 224 */       throw new HttpMediaTypeNotSupportedException(contentType, this.allSupportedMediaTypes);
/*     */     } 
/*     */     
/* 227 */     MediaType selectedContentType = contentType;
/* 228 */     Object theBody = body;
/* 229 */     LogFormatUtils.traceDebug(this.logger, traceOn -> {
/*     */           String formatted = LogFormatUtils.formatValue(theBody, !traceOn.booleanValue());
/*     */           
/*     */           return "Read \"" + selectedContentType + "\" to [" + formatted + "]";
/*     */         });
/* 234 */     return body;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ServletServerHttpRequest createInputMessage(NativeWebRequest webRequest) {
/* 243 */     HttpServletRequest servletRequest = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/* 244 */     Assert.state((servletRequest != null), "No HttpServletRequest");
/* 245 */     return new ServletServerHttpRequest(servletRequest);
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
/*     */   protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
/* 259 */     Annotation[] annotations = parameter.getParameterAnnotations();
/* 260 */     for (Annotation ann : annotations) {
/* 261 */       Validated validatedAnn = (Validated)AnnotationUtils.getAnnotation(ann, Validated.class);
/* 262 */       if (validatedAnn != null || ann.annotationType().getSimpleName().startsWith("Valid")) {
/* 263 */         Object hints = (validatedAnn != null) ? validatedAnn.value() : AnnotationUtils.getValue(ann);
/* 264 */         (new Object[1])[0] = hints; Object[] validationHints = (hints instanceof Object[]) ? (Object[])hints : new Object[1];
/* 265 */         binder.validate(validationHints);
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter parameter) {
/* 279 */     int i = parameter.getParameterIndex();
/* 280 */     Class<?>[] paramTypes = parameter.getExecutable().getParameterTypes();
/* 281 */     boolean hasBindingResult = (paramTypes.length > i + 1 && Errors.class.isAssignableFrom(paramTypes[i + 1]));
/* 282 */     return !hasBindingResult;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object adaptArgumentIfNecessary(@Nullable Object arg, MethodParameter parameter) {
/* 294 */     if (parameter.getParameterType() == Optional.class) {
/* 295 */       if (arg == null || (arg instanceof Collection && ((Collection)arg).isEmpty()) || (arg instanceof Object[] && ((Object[])arg).length == 0))
/*     */       {
/* 297 */         return Optional.empty();
/*     */       }
/*     */       
/* 300 */       return Optional.of(arg);
/*     */     } 
/*     */     
/* 303 */     return arg;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class EmptyBodyCheckingHttpInputMessage
/*     */     implements HttpInputMessage
/*     */   {
/*     */     private final HttpHeaders headers;
/*     */     @Nullable
/*     */     private final InputStream body;
/*     */     
/*     */     public EmptyBodyCheckingHttpInputMessage(HttpInputMessage inputMessage) throws IOException {
/* 315 */       this.headers = inputMessage.getHeaders();
/* 316 */       InputStream inputStream = inputMessage.getBody();
/* 317 */       if (inputStream.markSupported()) {
/* 318 */         inputStream.mark(1);
/* 319 */         this.body = (inputStream.read() != -1) ? inputStream : null;
/* 320 */         inputStream.reset();
/*     */       } else {
/*     */         
/* 323 */         PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream);
/* 324 */         int b = pushbackInputStream.read();
/* 325 */         if (b == -1) {
/* 326 */           this.body = null;
/*     */         } else {
/*     */           
/* 329 */           this.body = pushbackInputStream;
/* 330 */           pushbackInputStream.unread(b);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public HttpHeaders getHeaders() {
/* 337 */       return this.headers;
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream getBody() {
/* 342 */       return (this.body != null) ? this.body : StreamUtils.emptyInput();
/*     */     }
/*     */     
/*     */     public boolean hasBody() {
/* 346 */       return (this.body != null);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/AbstractMessageConverterMethodArgumentResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */