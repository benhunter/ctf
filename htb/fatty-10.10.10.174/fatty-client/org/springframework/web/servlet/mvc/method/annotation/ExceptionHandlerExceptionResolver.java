/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.converter.ByteArrayHttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
/*     */ import org.springframework.http.converter.StringHttpMessageConverter;
/*     */ import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
/*     */ import org.springframework.http.converter.xml.SourceHttpMessageConverter;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.ui.ModelMap;
/*     */ import org.springframework.web.accept.ContentNegotiationManager;
/*     */ import org.springframework.web.context.request.ServletWebRequest;
/*     */ import org.springframework.web.method.ControllerAdviceBean;
/*     */ import org.springframework.web.method.HandlerMethod;
/*     */ import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
/*     */ import org.springframework.web.method.annotation.MapMethodProcessor;
/*     */ import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;
/*     */ import org.springframework.web.method.annotation.ModelMethodProcessor;
/*     */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*     */ import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
/*     */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*     */ import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
/*     */ import org.springframework.web.method.support.ModelAndViewContainer;
/*     */ import org.springframework.web.servlet.ModelAndView;
/*     */ import org.springframework.web.servlet.View;
/*     */ import org.springframework.web.servlet.handler.AbstractHandlerMethodExceptionResolver;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExceptionHandlerExceptionResolver
/*     */   extends AbstractHandlerMethodExceptionResolver
/*     */   implements ApplicationContextAware, InitializingBean
/*     */ {
/*     */   @Nullable
/*     */   private List<HandlerMethodArgumentResolver> customArgumentResolvers;
/*     */   @Nullable
/*     */   private HandlerMethodArgumentResolverComposite argumentResolvers;
/*     */   @Nullable
/*     */   private List<HandlerMethodReturnValueHandler> customReturnValueHandlers;
/*     */   @Nullable
/*     */   private HandlerMethodReturnValueHandlerComposite returnValueHandlers;
/*     */   private List<HttpMessageConverter<?>> messageConverters;
/*  93 */   private ContentNegotiationManager contentNegotiationManager = new ContentNegotiationManager();
/*     */   
/*  95 */   private final List<Object> responseBodyAdvice = new ArrayList();
/*     */   
/*     */   @Nullable
/*     */   private ApplicationContext applicationContext;
/*     */   
/* 100 */   private final Map<Class<?>, ExceptionHandlerMethodResolver> exceptionHandlerCache = new ConcurrentHashMap<>(64);
/*     */ 
/*     */   
/* 103 */   private final Map<ControllerAdviceBean, ExceptionHandlerMethodResolver> exceptionHandlerAdviceCache = new LinkedHashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   public ExceptionHandlerExceptionResolver() {
/* 108 */     StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
/* 109 */     stringHttpMessageConverter.setWriteAcceptCharset(false);
/*     */     
/* 111 */     this.messageConverters = new ArrayList<>();
/* 112 */     this.messageConverters.add(new ByteArrayHttpMessageConverter());
/* 113 */     this.messageConverters.add(stringHttpMessageConverter);
/*     */     try {
/* 115 */       this.messageConverters.add(new SourceHttpMessageConverter());
/*     */     }
/* 117 */     catch (Error error) {}
/*     */ 
/*     */     
/* 120 */     this.messageConverters.add(new AllEncompassingFormHttpMessageConverter());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCustomArgumentResolvers(@Nullable List<HandlerMethodArgumentResolver> argumentResolvers) {
/* 130 */     this.customArgumentResolvers = argumentResolvers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public List<HandlerMethodArgumentResolver> getCustomArgumentResolvers() {
/* 138 */     return this.customArgumentResolvers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setArgumentResolvers(@Nullable List<HandlerMethodArgumentResolver> argumentResolvers) {
/* 146 */     if (argumentResolvers == null) {
/* 147 */       this.argumentResolvers = null;
/*     */     } else {
/*     */       
/* 150 */       this.argumentResolvers = new HandlerMethodArgumentResolverComposite();
/* 151 */       this.argumentResolvers.addResolvers(argumentResolvers);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public HandlerMethodArgumentResolverComposite getArgumentResolvers() {
/* 161 */     return this.argumentResolvers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCustomReturnValueHandlers(@Nullable List<HandlerMethodReturnValueHandler> returnValueHandlers) {
/* 170 */     this.customReturnValueHandlers = returnValueHandlers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public List<HandlerMethodReturnValueHandler> getCustomReturnValueHandlers() {
/* 178 */     return this.customReturnValueHandlers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReturnValueHandlers(@Nullable List<HandlerMethodReturnValueHandler> returnValueHandlers) {
/* 186 */     if (returnValueHandlers == null) {
/* 187 */       this.returnValueHandlers = null;
/*     */     } else {
/*     */       
/* 190 */       this.returnValueHandlers = new HandlerMethodReturnValueHandlerComposite();
/* 191 */       this.returnValueHandlers.addHandlers(returnValueHandlers);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public HandlerMethodReturnValueHandlerComposite getReturnValueHandlers() {
/* 201 */     return this.returnValueHandlers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
/* 209 */     this.messageConverters = messageConverters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<HttpMessageConverter<?>> getMessageConverters() {
/* 216 */     return this.messageConverters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContentNegotiationManager(ContentNegotiationManager contentNegotiationManager) {
/* 224 */     this.contentNegotiationManager = contentNegotiationManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContentNegotiationManager getContentNegotiationManager() {
/* 231 */     return this.contentNegotiationManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResponseBodyAdvice(@Nullable List<ResponseBodyAdvice<?>> responseBodyAdvice) {
/* 241 */     this.responseBodyAdvice.clear();
/* 242 */     if (responseBodyAdvice != null) {
/* 243 */       this.responseBodyAdvice.addAll(responseBodyAdvice);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setApplicationContext(@Nullable ApplicationContext applicationContext) {
/* 249 */     this.applicationContext = applicationContext;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public ApplicationContext getApplicationContext() {
/* 254 */     return this.applicationContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 261 */     initExceptionHandlerAdviceCache();
/*     */     
/* 263 */     if (this.argumentResolvers == null) {
/* 264 */       List<HandlerMethodArgumentResolver> resolvers = getDefaultArgumentResolvers();
/* 265 */       this.argumentResolvers = (new HandlerMethodArgumentResolverComposite()).addResolvers(resolvers);
/*     */     } 
/* 267 */     if (this.returnValueHandlers == null) {
/* 268 */       List<HandlerMethodReturnValueHandler> handlers = getDefaultReturnValueHandlers();
/* 269 */       this.returnValueHandlers = (new HandlerMethodReturnValueHandlerComposite()).addHandlers(handlers);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void initExceptionHandlerAdviceCache() {
/* 274 */     if (getApplicationContext() == null) {
/*     */       return;
/*     */     }
/*     */     
/* 278 */     List<ControllerAdviceBean> adviceBeans = ControllerAdviceBean.findAnnotatedBeans(getApplicationContext());
/* 279 */     AnnotationAwareOrderComparator.sort(adviceBeans);
/*     */     
/* 281 */     for (ControllerAdviceBean adviceBean : adviceBeans) {
/* 282 */       Class<?> beanType = adviceBean.getBeanType();
/* 283 */       if (beanType == null) {
/* 284 */         throw new IllegalStateException("Unresolvable type for ControllerAdviceBean: " + adviceBean);
/*     */       }
/* 286 */       ExceptionHandlerMethodResolver resolver = new ExceptionHandlerMethodResolver(beanType);
/* 287 */       if (resolver.hasExceptionMappings()) {
/* 288 */         this.exceptionHandlerAdviceCache.put(adviceBean, resolver);
/*     */       }
/* 290 */       if (ResponseBodyAdvice.class.isAssignableFrom(beanType)) {
/* 291 */         this.responseBodyAdvice.add(adviceBean);
/*     */       }
/*     */     } 
/*     */     
/* 295 */     if (this.logger.isDebugEnabled()) {
/* 296 */       int handlerSize = this.exceptionHandlerAdviceCache.size();
/* 297 */       int adviceSize = this.responseBodyAdvice.size();
/* 298 */       if (handlerSize == 0 && adviceSize == 0) {
/* 299 */         this.logger.debug("ControllerAdvice beans: none");
/*     */       } else {
/*     */         
/* 302 */         this.logger.debug("ControllerAdvice beans: " + handlerSize + " @ExceptionHandler, " + adviceSize + " ResponseBodyAdvice");
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
/*     */   public Map<ControllerAdviceBean, ExceptionHandlerMethodResolver> getExceptionHandlerAdviceCache() {
/* 315 */     return Collections.unmodifiableMap(this.exceptionHandlerAdviceCache);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<HandlerMethodArgumentResolver> getDefaultArgumentResolvers() {
/* 323 */     List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();
/*     */ 
/*     */     
/* 326 */     resolvers.add(new SessionAttributeMethodArgumentResolver());
/* 327 */     resolvers.add(new RequestAttributeMethodArgumentResolver());
/*     */ 
/*     */     
/* 330 */     resolvers.add(new ServletRequestMethodArgumentResolver());
/* 331 */     resolvers.add(new ServletResponseMethodArgumentResolver());
/* 332 */     resolvers.add(new RedirectAttributesMethodArgumentResolver());
/* 333 */     resolvers.add(new ModelMethodProcessor());
/*     */ 
/*     */     
/* 336 */     if (getCustomArgumentResolvers() != null) {
/* 337 */       resolvers.addAll(getCustomArgumentResolvers());
/*     */     }
/*     */     
/* 340 */     return resolvers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<HandlerMethodReturnValueHandler> getDefaultReturnValueHandlers() {
/* 348 */     List<HandlerMethodReturnValueHandler> handlers = new ArrayList<>();
/*     */ 
/*     */     
/* 351 */     handlers.add(new ModelAndViewMethodReturnValueHandler());
/* 352 */     handlers.add(new ModelMethodProcessor());
/* 353 */     handlers.add(new ViewMethodReturnValueHandler());
/* 354 */     handlers.add(new HttpEntityMethodProcessor(
/* 355 */           getMessageConverters(), this.contentNegotiationManager, this.responseBodyAdvice));
/*     */ 
/*     */     
/* 358 */     handlers.add(new ModelAttributeMethodProcessor(false));
/* 359 */     handlers.add(new RequestResponseBodyMethodProcessor(
/* 360 */           getMessageConverters(), this.contentNegotiationManager, this.responseBodyAdvice));
/*     */ 
/*     */     
/* 363 */     handlers.add(new ViewNameMethodReturnValueHandler());
/* 364 */     handlers.add(new MapMethodProcessor());
/*     */ 
/*     */     
/* 367 */     if (getCustomReturnValueHandlers() != null) {
/* 368 */       handlers.addAll(getCustomReturnValueHandlers());
/*     */     }
/*     */ 
/*     */     
/* 372 */     handlers.add(new ModelAttributeMethodProcessor(true));
/*     */     
/* 374 */     return handlers;
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
/*     */   protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response, @Nullable HandlerMethod handlerMethod, Exception exception) {
/* 386 */     ServletInvocableHandlerMethod exceptionHandlerMethod = getExceptionHandlerMethod(handlerMethod, exception);
/* 387 */     if (exceptionHandlerMethod == null) {
/* 388 */       return null;
/*     */     }
/*     */     
/* 391 */     if (this.argumentResolvers != null) {
/* 392 */       exceptionHandlerMethod.setHandlerMethodArgumentResolvers(this.argumentResolvers);
/*     */     }
/* 394 */     if (this.returnValueHandlers != null) {
/* 395 */       exceptionHandlerMethod.setHandlerMethodReturnValueHandlers(this.returnValueHandlers);
/*     */     }
/*     */     
/* 398 */     ServletWebRequest webRequest = new ServletWebRequest(request, response);
/* 399 */     ModelAndViewContainer mavContainer = new ModelAndViewContainer();
/*     */     
/*     */     try {
/* 402 */       if (this.logger.isDebugEnabled()) {
/* 403 */         this.logger.debug("Using @ExceptionHandler " + exceptionHandlerMethod);
/*     */       }
/* 405 */       Throwable cause = exception.getCause();
/* 406 */       if (cause != null)
/*     */       {
/* 408 */         exceptionHandlerMethod.invokeAndHandle(webRequest, mavContainer, new Object[] { exception, cause, handlerMethod });
/*     */       }
/*     */       else
/*     */       {
/* 412 */         exceptionHandlerMethod.invokeAndHandle(webRequest, mavContainer, new Object[] { exception, handlerMethod });
/*     */       }
/*     */     
/* 415 */     } catch (Throwable invocationEx) {
/*     */ 
/*     */       
/* 418 */       if (invocationEx != exception && this.logger.isWarnEnabled()) {
/* 419 */         this.logger.warn("Failure in @ExceptionHandler " + exceptionHandlerMethod, invocationEx);
/*     */       }
/*     */       
/* 422 */       return null;
/*     */     } 
/*     */     
/* 425 */     if (mavContainer.isRequestHandled()) {
/* 426 */       return new ModelAndView();
/*     */     }
/*     */     
/* 429 */     ModelMap model = mavContainer.getModel();
/* 430 */     HttpStatus status = mavContainer.getStatus();
/* 431 */     ModelAndView mav = new ModelAndView(mavContainer.getViewName(), (Map)model, status);
/* 432 */     mav.setViewName(mavContainer.getViewName());
/* 433 */     if (!mavContainer.isViewReference()) {
/* 434 */       mav.setView((View)mavContainer.getView());
/*     */     }
/* 436 */     if (model instanceof RedirectAttributes) {
/* 437 */       Map<String, ?> flashAttributes = ((RedirectAttributes)model).getFlashAttributes();
/* 438 */       RequestContextUtils.getOutputFlashMap(request).putAll(flashAttributes);
/*     */     } 
/* 440 */     return mav;
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
/*     */   protected ServletInvocableHandlerMethod getExceptionHandlerMethod(@Nullable HandlerMethod handlerMethod, Exception exception) {
/* 458 */     Class<?> handlerType = null;
/*     */     
/* 460 */     if (handlerMethod != null) {
/*     */ 
/*     */       
/* 463 */       handlerType = handlerMethod.getBeanType();
/* 464 */       ExceptionHandlerMethodResolver resolver = this.exceptionHandlerCache.get(handlerType);
/* 465 */       if (resolver == null) {
/* 466 */         resolver = new ExceptionHandlerMethodResolver(handlerType);
/* 467 */         this.exceptionHandlerCache.put(handlerType, resolver);
/*     */       } 
/* 469 */       Method method = resolver.resolveMethod(exception);
/* 470 */       if (method != null) {
/* 471 */         return new ServletInvocableHandlerMethod(handlerMethod.getBean(), method);
/*     */       }
/*     */ 
/*     */       
/* 475 */       if (Proxy.isProxyClass(handlerType)) {
/* 476 */         handlerType = AopUtils.getTargetClass(handlerMethod.getBean());
/*     */       }
/*     */     } 
/*     */     
/* 480 */     for (Map.Entry<ControllerAdviceBean, ExceptionHandlerMethodResolver> entry : this.exceptionHandlerAdviceCache.entrySet()) {
/* 481 */       ControllerAdviceBean advice = entry.getKey();
/* 482 */       if (advice.isApplicableToBeanType(handlerType)) {
/* 483 */         ExceptionHandlerMethodResolver resolver = entry.getValue();
/* 484 */         Method method = resolver.resolveMethod(exception);
/* 485 */         if (method != null) {
/* 486 */           return new ServletInvocableHandlerMethod(advice.resolveBean(), method);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 491 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/ExceptionHandlerExceptionResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */