/*      */ package org.springframework.web.servlet.mvc.method.annotation;
/*      */ 
/*      */ import java.lang.reflect.Method;
/*      */ import java.util.ArrayList;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import javax.servlet.ServletRequest;
/*      */ import javax.servlet.http.HttpServletRequest;
/*      */ import javax.servlet.http.HttpServletResponse;
/*      */ import javax.servlet.http.HttpSession;
/*      */ import org.springframework.beans.factory.BeanFactory;
/*      */ import org.springframework.beans.factory.BeanFactoryAware;
/*      */ import org.springframework.beans.factory.InitializingBean;
/*      */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*      */ import org.springframework.core.DefaultParameterNameDiscoverer;
/*      */ import org.springframework.core.MethodIntrospector;
/*      */ import org.springframework.core.ParameterNameDiscoverer;
/*      */ import org.springframework.core.ReactiveAdapterRegistry;
/*      */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*      */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*      */ import org.springframework.core.log.LogFormatUtils;
/*      */ import org.springframework.core.task.AsyncTaskExecutor;
/*      */ import org.springframework.core.task.SimpleAsyncTaskExecutor;
/*      */ import org.springframework.core.task.TaskExecutor;
/*      */ import org.springframework.http.converter.ByteArrayHttpMessageConverter;
/*      */ import org.springframework.http.converter.HttpMessageConverter;
/*      */ import org.springframework.http.converter.StringHttpMessageConverter;
/*      */ import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
/*      */ import org.springframework.http.converter.xml.SourceHttpMessageConverter;
/*      */ import org.springframework.lang.Nullable;
/*      */ import org.springframework.ui.ModelMap;
/*      */ import org.springframework.util.CollectionUtils;
/*      */ import org.springframework.util.ReflectionUtils;
/*      */ import org.springframework.web.accept.ContentNegotiationManager;
/*      */ import org.springframework.web.bind.annotation.InitBinder;
/*      */ import org.springframework.web.bind.annotation.ModelAttribute;
/*      */ import org.springframework.web.bind.annotation.RequestMapping;
/*      */ import org.springframework.web.bind.support.DefaultDataBinderFactory;
/*      */ import org.springframework.web.bind.support.DefaultSessionAttributeStore;
/*      */ import org.springframework.web.bind.support.SessionAttributeStore;
/*      */ import org.springframework.web.bind.support.WebBindingInitializer;
/*      */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*      */ import org.springframework.web.context.request.NativeWebRequest;
/*      */ import org.springframework.web.context.request.ServletWebRequest;
/*      */ import org.springframework.web.context.request.async.AsyncWebRequest;
/*      */ import org.springframework.web.context.request.async.CallableProcessingInterceptor;
/*      */ import org.springframework.web.context.request.async.DeferredResultProcessingInterceptor;
/*      */ import org.springframework.web.context.request.async.WebAsyncManager;
/*      */ import org.springframework.web.context.request.async.WebAsyncUtils;
/*      */ import org.springframework.web.method.ControllerAdviceBean;
/*      */ import org.springframework.web.method.HandlerMethod;
/*      */ import org.springframework.web.method.annotation.ErrorsMethodArgumentResolver;
/*      */ import org.springframework.web.method.annotation.ExpressionValueMethodArgumentResolver;
/*      */ import org.springframework.web.method.annotation.InitBinderDataBinderFactory;
/*      */ import org.springframework.web.method.annotation.MapMethodProcessor;
/*      */ import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;
/*      */ import org.springframework.web.method.annotation.ModelFactory;
/*      */ import org.springframework.web.method.annotation.ModelMethodProcessor;
/*      */ import org.springframework.web.method.annotation.RequestHeaderMapMethodArgumentResolver;
/*      */ import org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver;
/*      */ import org.springframework.web.method.annotation.RequestParamMapMethodArgumentResolver;
/*      */ import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
/*      */ import org.springframework.web.method.annotation.SessionAttributesHandler;
/*      */ import org.springframework.web.method.annotation.SessionStatusMethodArgumentResolver;
/*      */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*      */ import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
/*      */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*      */ import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
/*      */ import org.springframework.web.method.support.InvocableHandlerMethod;
/*      */ import org.springframework.web.method.support.ModelAndViewContainer;
/*      */ import org.springframework.web.servlet.ModelAndView;
/*      */ import org.springframework.web.servlet.View;
/*      */ import org.springframework.web.servlet.mvc.annotation.ModelAndViewResolver;
/*      */ import org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter;
/*      */ import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/*      */ import org.springframework.web.servlet.support.RequestContextUtils;
/*      */ import org.springframework.web.util.WebUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class RequestMappingHandlerAdapter
/*      */   extends AbstractHandlerMethodAdapter
/*      */   implements BeanFactoryAware, InitializingBean
/*      */ {
/*      */   public static final ReflectionUtils.MethodFilter INIT_BINDER_METHODS;
/*      */   public static final ReflectionUtils.MethodFilter MODEL_ATTRIBUTE_METHODS;
/*      */   @Nullable
/*      */   private List<HandlerMethodArgumentResolver> customArgumentResolvers;
/*      */   @Nullable
/*      */   private HandlerMethodArgumentResolverComposite argumentResolvers;
/*      */   @Nullable
/*      */   private HandlerMethodArgumentResolverComposite initBinderArgumentResolvers;
/*      */   @Nullable
/*      */   private List<HandlerMethodReturnValueHandler> customReturnValueHandlers;
/*      */   @Nullable
/*      */   private HandlerMethodReturnValueHandlerComposite returnValueHandlers;
/*      */   @Nullable
/*      */   private List<ModelAndViewResolver> modelAndViewResolvers;
/*      */   
/*      */   static {
/*  121 */     INIT_BINDER_METHODS = (method -> AnnotatedElementUtils.hasAnnotation(method, InitBinder.class));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  127 */     MODEL_ATTRIBUTE_METHODS = (method -> 
/*  128 */       (!AnnotatedElementUtils.hasAnnotation(method, RequestMapping.class) && AnnotatedElementUtils.hasAnnotation(method, ModelAttribute.class)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  150 */   private ContentNegotiationManager contentNegotiationManager = new ContentNegotiationManager();
/*      */   
/*      */   private List<HttpMessageConverter<?>> messageConverters;
/*      */   
/*  154 */   private List<Object> requestResponseBodyAdvice = new ArrayList();
/*      */   
/*      */   @Nullable
/*      */   private WebBindingInitializer webBindingInitializer;
/*      */   
/*  159 */   private AsyncTaskExecutor taskExecutor = (AsyncTaskExecutor)new SimpleAsyncTaskExecutor("MvcAsync");
/*      */   
/*      */   @Nullable
/*      */   private Long asyncRequestTimeout;
/*      */   
/*  164 */   private CallableProcessingInterceptor[] callableInterceptors = new CallableProcessingInterceptor[0];
/*      */   
/*  166 */   private DeferredResultProcessingInterceptor[] deferredResultInterceptors = new DeferredResultProcessingInterceptor[0];
/*      */   
/*  168 */   private ReactiveAdapterRegistry reactiveAdapterRegistry = ReactiveAdapterRegistry.getSharedInstance();
/*      */   
/*      */   private boolean ignoreDefaultModelOnRedirect = false;
/*      */   
/*  172 */   private int cacheSecondsForSessionAttributeHandlers = 0;
/*      */   
/*      */   private boolean synchronizeOnSession = false;
/*      */   
/*  176 */   private SessionAttributeStore sessionAttributeStore = (SessionAttributeStore)new DefaultSessionAttributeStore();
/*      */   
/*  178 */   private ParameterNameDiscoverer parameterNameDiscoverer = (ParameterNameDiscoverer)new DefaultParameterNameDiscoverer();
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private ConfigurableBeanFactory beanFactory;
/*      */   
/*  184 */   private final Map<Class<?>, SessionAttributesHandler> sessionAttributesHandlerCache = new ConcurrentHashMap<>(64);
/*      */   
/*  186 */   private final Map<Class<?>, Set<Method>> initBinderCache = new ConcurrentHashMap<>(64);
/*      */   
/*  188 */   private final Map<ControllerAdviceBean, Set<Method>> initBinderAdviceCache = new LinkedHashMap<>();
/*      */   
/*  190 */   private final Map<Class<?>, Set<Method>> modelAttributeCache = new ConcurrentHashMap<>(64);
/*      */   
/*  192 */   private final Map<ControllerAdviceBean, Set<Method>> modelAttributeAdviceCache = new LinkedHashMap<>();
/*      */ 
/*      */   
/*      */   public RequestMappingHandlerAdapter() {
/*  196 */     StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
/*  197 */     stringHttpMessageConverter.setWriteAcceptCharset(false);
/*      */     
/*  199 */     this.messageConverters = new ArrayList<>(4);
/*  200 */     this.messageConverters.add(new ByteArrayHttpMessageConverter());
/*  201 */     this.messageConverters.add(stringHttpMessageConverter);
/*      */     try {
/*  203 */       this.messageConverters.add(new SourceHttpMessageConverter());
/*      */     }
/*  205 */     catch (Error error) {}
/*      */ 
/*      */     
/*  208 */     this.messageConverters.add(new AllEncompassingFormHttpMessageConverter());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCustomArgumentResolvers(@Nullable List<HandlerMethodArgumentResolver> argumentResolvers) {
/*  218 */     this.customArgumentResolvers = argumentResolvers;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public List<HandlerMethodArgumentResolver> getCustomArgumentResolvers() {
/*  226 */     return this.customArgumentResolvers;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setArgumentResolvers(@Nullable List<HandlerMethodArgumentResolver> argumentResolvers) {
/*  234 */     if (argumentResolvers == null) {
/*  235 */       this.argumentResolvers = null;
/*      */     } else {
/*      */       
/*  238 */       this.argumentResolvers = new HandlerMethodArgumentResolverComposite();
/*  239 */       this.argumentResolvers.addResolvers(argumentResolvers);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public List<HandlerMethodArgumentResolver> getArgumentResolvers() {
/*  249 */     return (this.argumentResolvers != null) ? this.argumentResolvers.getResolvers() : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInitBinderArgumentResolvers(@Nullable List<HandlerMethodArgumentResolver> argumentResolvers) {
/*  256 */     if (argumentResolvers == null) {
/*  257 */       this.initBinderArgumentResolvers = null;
/*      */     } else {
/*      */       
/*  260 */       this.initBinderArgumentResolvers = new HandlerMethodArgumentResolverComposite();
/*  261 */       this.initBinderArgumentResolvers.addResolvers(argumentResolvers);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public List<HandlerMethodArgumentResolver> getInitBinderArgumentResolvers() {
/*  271 */     return (this.initBinderArgumentResolvers != null) ? this.initBinderArgumentResolvers.getResolvers() : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCustomReturnValueHandlers(@Nullable List<HandlerMethodReturnValueHandler> returnValueHandlers) {
/*  280 */     this.customReturnValueHandlers = returnValueHandlers;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public List<HandlerMethodReturnValueHandler> getCustomReturnValueHandlers() {
/*  288 */     return this.customReturnValueHandlers;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setReturnValueHandlers(@Nullable List<HandlerMethodReturnValueHandler> returnValueHandlers) {
/*  296 */     if (returnValueHandlers == null) {
/*  297 */       this.returnValueHandlers = null;
/*      */     } else {
/*      */       
/*  300 */       this.returnValueHandlers = new HandlerMethodReturnValueHandlerComposite();
/*  301 */       this.returnValueHandlers.addHandlers(returnValueHandlers);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public List<HandlerMethodReturnValueHandler> getReturnValueHandlers() {
/*  311 */     return (this.returnValueHandlers != null) ? this.returnValueHandlers.getHandlers() : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setModelAndViewResolvers(@Nullable List<ModelAndViewResolver> modelAndViewResolvers) {
/*  329 */     this.modelAndViewResolvers = modelAndViewResolvers;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public List<ModelAndViewResolver> getModelAndViewResolvers() {
/*  337 */     return this.modelAndViewResolvers;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setContentNegotiationManager(ContentNegotiationManager contentNegotiationManager) {
/*  345 */     this.contentNegotiationManager = contentNegotiationManager;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
/*  354 */     this.messageConverters = messageConverters;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<HttpMessageConverter<?>> getMessageConverters() {
/*  361 */     return this.messageConverters;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRequestBodyAdvice(@Nullable List<RequestBodyAdvice> requestBodyAdvice) {
/*  370 */     if (requestBodyAdvice != null) {
/*  371 */       this.requestResponseBodyAdvice.addAll(requestBodyAdvice);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setResponseBodyAdvice(@Nullable List<ResponseBodyAdvice<?>> responseBodyAdvice) {
/*  381 */     if (responseBodyAdvice != null) {
/*  382 */       this.requestResponseBodyAdvice.addAll(responseBodyAdvice);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setWebBindingInitializer(@Nullable WebBindingInitializer webBindingInitializer) {
/*  391 */     this.webBindingInitializer = webBindingInitializer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public WebBindingInitializer getWebBindingInitializer() {
/*  399 */     return this.webBindingInitializer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTaskExecutor(AsyncTaskExecutor taskExecutor) {
/*  411 */     this.taskExecutor = taskExecutor;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAsyncRequestTimeout(long timeout) {
/*  424 */     this.asyncRequestTimeout = Long.valueOf(timeout);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCallableInterceptors(List<CallableProcessingInterceptor> interceptors) {
/*  432 */     this.callableInterceptors = interceptors.<CallableProcessingInterceptor>toArray(new CallableProcessingInterceptor[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDeferredResultInterceptors(List<DeferredResultProcessingInterceptor> interceptors) {
/*  440 */     this.deferredResultInterceptors = interceptors.<DeferredResultProcessingInterceptor>toArray(new DeferredResultProcessingInterceptor[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setReactiveAdapterRegistry(ReactiveAdapterRegistry reactiveAdapterRegistry) {
/*  449 */     this.reactiveAdapterRegistry = reactiveAdapterRegistry;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ReactiveAdapterRegistry getReactiveAdapterRegistry() {
/*  457 */     return this.reactiveAdapterRegistry;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIgnoreDefaultModelOnRedirect(boolean ignoreDefaultModelOnRedirect) {
/*  475 */     this.ignoreDefaultModelOnRedirect = ignoreDefaultModelOnRedirect;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSessionAttributeStore(SessionAttributeStore sessionAttributeStore) {
/*  485 */     this.sessionAttributeStore = sessionAttributeStore;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCacheSecondsForSessionAttributeHandlers(int cacheSecondsForSessionAttributeHandlers) {
/*  505 */     this.cacheSecondsForSessionAttributeHandlers = cacheSecondsForSessionAttributeHandlers;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSynchronizeOnSession(boolean synchronizeOnSession) {
/*  527 */     this.synchronizeOnSession = synchronizeOnSession;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer) {
/*  536 */     this.parameterNameDiscoverer = parameterNameDiscoverer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBeanFactory(BeanFactory beanFactory) {
/*  545 */     if (beanFactory instanceof ConfigurableBeanFactory) {
/*  546 */       this.beanFactory = (ConfigurableBeanFactory)beanFactory;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected ConfigurableBeanFactory getBeanFactory() {
/*  555 */     return this.beanFactory;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void afterPropertiesSet() {
/*  562 */     initControllerAdviceCache();
/*      */     
/*  564 */     if (this.argumentResolvers == null) {
/*  565 */       List<HandlerMethodArgumentResolver> resolvers = getDefaultArgumentResolvers();
/*  566 */       this.argumentResolvers = (new HandlerMethodArgumentResolverComposite()).addResolvers(resolvers);
/*      */     } 
/*  568 */     if (this.initBinderArgumentResolvers == null) {
/*  569 */       List<HandlerMethodArgumentResolver> resolvers = getDefaultInitBinderArgumentResolvers();
/*  570 */       this.initBinderArgumentResolvers = (new HandlerMethodArgumentResolverComposite()).addResolvers(resolvers);
/*      */     } 
/*  572 */     if (this.returnValueHandlers == null) {
/*  573 */       List<HandlerMethodReturnValueHandler> handlers = getDefaultReturnValueHandlers();
/*  574 */       this.returnValueHandlers = (new HandlerMethodReturnValueHandlerComposite()).addHandlers(handlers);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void initControllerAdviceCache() {
/*  579 */     if (getApplicationContext() == null) {
/*      */       return;
/*      */     }
/*      */     
/*  583 */     List<ControllerAdviceBean> adviceBeans = ControllerAdviceBean.findAnnotatedBeans(getApplicationContext());
/*  584 */     AnnotationAwareOrderComparator.sort(adviceBeans);
/*      */     
/*  586 */     List<Object> requestResponseBodyAdviceBeans = new ArrayList();
/*      */     
/*  588 */     for (ControllerAdviceBean adviceBean : adviceBeans) {
/*  589 */       Class<?> beanType = adviceBean.getBeanType();
/*  590 */       if (beanType == null) {
/*  591 */         throw new IllegalStateException("Unresolvable type for ControllerAdviceBean: " + adviceBean);
/*      */       }
/*  593 */       Set<Method> attrMethods = MethodIntrospector.selectMethods(beanType, MODEL_ATTRIBUTE_METHODS);
/*  594 */       if (!attrMethods.isEmpty()) {
/*  595 */         this.modelAttributeAdviceCache.put(adviceBean, attrMethods);
/*      */       }
/*  597 */       Set<Method> binderMethods = MethodIntrospector.selectMethods(beanType, INIT_BINDER_METHODS);
/*  598 */       if (!binderMethods.isEmpty()) {
/*  599 */         this.initBinderAdviceCache.put(adviceBean, binderMethods);
/*      */       }
/*  601 */       if (RequestBodyAdvice.class.isAssignableFrom(beanType) || ResponseBodyAdvice.class.isAssignableFrom(beanType)) {
/*  602 */         requestResponseBodyAdviceBeans.add(adviceBean);
/*      */       }
/*      */     } 
/*      */     
/*  606 */     if (!requestResponseBodyAdviceBeans.isEmpty()) {
/*  607 */       this.requestResponseBodyAdvice.addAll(0, requestResponseBodyAdviceBeans);
/*      */     }
/*      */     
/*  610 */     if (this.logger.isDebugEnabled()) {
/*  611 */       int modelSize = this.modelAttributeAdviceCache.size();
/*  612 */       int binderSize = this.initBinderAdviceCache.size();
/*  613 */       int reqCount = getBodyAdviceCount(RequestBodyAdvice.class);
/*  614 */       int resCount = getBodyAdviceCount(ResponseBodyAdvice.class);
/*  615 */       if (modelSize == 0 && binderSize == 0 && reqCount == 0 && resCount == 0) {
/*  616 */         this.logger.debug("ControllerAdvice beans: none");
/*      */       } else {
/*      */         
/*  619 */         this.logger.debug("ControllerAdvice beans: " + modelSize + " @ModelAttribute, " + binderSize + " @InitBinder, " + reqCount + " RequestBodyAdvice, " + resCount + " ResponseBodyAdvice");
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int getBodyAdviceCount(Class<?> adviceType) {
/*  628 */     List<Object> advice = this.requestResponseBodyAdvice;
/*  629 */     return RequestBodyAdvice.class.isAssignableFrom(adviceType) ? 
/*  630 */       RequestResponseBodyAdviceChain.<RequestBodyAdvice>getAdviceByType(advice, RequestBodyAdvice.class).size() : 
/*  631 */       RequestResponseBodyAdviceChain.<ResponseBodyAdvice>getAdviceByType(advice, ResponseBodyAdvice.class).size();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private List<HandlerMethodArgumentResolver> getDefaultArgumentResolvers() {
/*  639 */     List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();
/*      */ 
/*      */     
/*  642 */     resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(), false));
/*  643 */     resolvers.add(new RequestParamMapMethodArgumentResolver());
/*  644 */     resolvers.add(new PathVariableMethodArgumentResolver());
/*  645 */     resolvers.add(new PathVariableMapMethodArgumentResolver());
/*  646 */     resolvers.add(new MatrixVariableMethodArgumentResolver());
/*  647 */     resolvers.add(new MatrixVariableMapMethodArgumentResolver());
/*  648 */     resolvers.add(new ServletModelAttributeMethodProcessor(false));
/*  649 */     resolvers.add(new RequestResponseBodyMethodProcessor(getMessageConverters(), this.requestResponseBodyAdvice));
/*  650 */     resolvers.add(new RequestPartMethodArgumentResolver(getMessageConverters(), this.requestResponseBodyAdvice));
/*  651 */     resolvers.add(new RequestHeaderMethodArgumentResolver(getBeanFactory()));
/*  652 */     resolvers.add(new RequestHeaderMapMethodArgumentResolver());
/*  653 */     resolvers.add(new ServletCookieValueMethodArgumentResolver(getBeanFactory()));
/*  654 */     resolvers.add(new ExpressionValueMethodArgumentResolver(getBeanFactory()));
/*  655 */     resolvers.add(new SessionAttributeMethodArgumentResolver());
/*  656 */     resolvers.add(new RequestAttributeMethodArgumentResolver());
/*      */ 
/*      */     
/*  659 */     resolvers.add(new ServletRequestMethodArgumentResolver());
/*  660 */     resolvers.add(new ServletResponseMethodArgumentResolver());
/*  661 */     resolvers.add(new HttpEntityMethodProcessor(getMessageConverters(), this.requestResponseBodyAdvice));
/*  662 */     resolvers.add(new RedirectAttributesMethodArgumentResolver());
/*  663 */     resolvers.add(new ModelMethodProcessor());
/*  664 */     resolvers.add(new MapMethodProcessor());
/*  665 */     resolvers.add(new ErrorsMethodArgumentResolver());
/*  666 */     resolvers.add(new SessionStatusMethodArgumentResolver());
/*  667 */     resolvers.add(new UriComponentsBuilderMethodArgumentResolver());
/*      */ 
/*      */     
/*  670 */     if (getCustomArgumentResolvers() != null) {
/*  671 */       resolvers.addAll(getCustomArgumentResolvers());
/*      */     }
/*      */ 
/*      */     
/*  675 */     resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(), true));
/*  676 */     resolvers.add(new ServletModelAttributeMethodProcessor(true));
/*      */     
/*  678 */     return resolvers;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private List<HandlerMethodArgumentResolver> getDefaultInitBinderArgumentResolvers() {
/*  686 */     List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();
/*      */ 
/*      */     
/*  689 */     resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(), false));
/*  690 */     resolvers.add(new RequestParamMapMethodArgumentResolver());
/*  691 */     resolvers.add(new PathVariableMethodArgumentResolver());
/*  692 */     resolvers.add(new PathVariableMapMethodArgumentResolver());
/*  693 */     resolvers.add(new MatrixVariableMethodArgumentResolver());
/*  694 */     resolvers.add(new MatrixVariableMapMethodArgumentResolver());
/*  695 */     resolvers.add(new ExpressionValueMethodArgumentResolver(getBeanFactory()));
/*  696 */     resolvers.add(new SessionAttributeMethodArgumentResolver());
/*  697 */     resolvers.add(new RequestAttributeMethodArgumentResolver());
/*      */ 
/*      */     
/*  700 */     resolvers.add(new ServletRequestMethodArgumentResolver());
/*  701 */     resolvers.add(new ServletResponseMethodArgumentResolver());
/*      */ 
/*      */     
/*  704 */     if (getCustomArgumentResolvers() != null) {
/*  705 */       resolvers.addAll(getCustomArgumentResolvers());
/*      */     }
/*      */ 
/*      */     
/*  709 */     resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(), true));
/*      */     
/*  711 */     return resolvers;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private List<HandlerMethodReturnValueHandler> getDefaultReturnValueHandlers() {
/*  719 */     List<HandlerMethodReturnValueHandler> handlers = new ArrayList<>();
/*      */ 
/*      */     
/*  722 */     handlers.add(new ModelAndViewMethodReturnValueHandler());
/*  723 */     handlers.add(new ModelMethodProcessor());
/*  724 */     handlers.add(new ViewMethodReturnValueHandler());
/*  725 */     handlers.add(new ResponseBodyEmitterReturnValueHandler(getMessageConverters(), this.reactiveAdapterRegistry, (TaskExecutor)this.taskExecutor, this.contentNegotiationManager));
/*      */     
/*  727 */     handlers.add(new StreamingResponseBodyReturnValueHandler());
/*  728 */     handlers.add(new HttpEntityMethodProcessor(getMessageConverters(), this.contentNegotiationManager, this.requestResponseBodyAdvice));
/*      */     
/*  730 */     handlers.add(new HttpHeadersReturnValueHandler());
/*  731 */     handlers.add(new CallableMethodReturnValueHandler());
/*  732 */     handlers.add(new DeferredResultMethodReturnValueHandler());
/*  733 */     handlers.add(new AsyncTaskMethodReturnValueHandler((BeanFactory)this.beanFactory));
/*      */ 
/*      */     
/*  736 */     handlers.add(new ModelAttributeMethodProcessor(false));
/*  737 */     handlers.add(new RequestResponseBodyMethodProcessor(getMessageConverters(), this.contentNegotiationManager, this.requestResponseBodyAdvice));
/*      */ 
/*      */ 
/*      */     
/*  741 */     handlers.add(new ViewNameMethodReturnValueHandler());
/*  742 */     handlers.add(new MapMethodProcessor());
/*      */ 
/*      */     
/*  745 */     if (getCustomReturnValueHandlers() != null) {
/*  746 */       handlers.addAll(getCustomReturnValueHandlers());
/*      */     }
/*      */ 
/*      */     
/*  750 */     if (!CollectionUtils.isEmpty(getModelAndViewResolvers())) {
/*  751 */       handlers.add(new ModelAndViewResolverMethodReturnValueHandler(getModelAndViewResolvers()));
/*      */     } else {
/*      */       
/*  754 */       handlers.add(new ModelAttributeMethodProcessor(true));
/*      */     } 
/*      */     
/*  757 */     return handlers;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean supportsInternal(HandlerMethod handlerMethod) {
/*  771 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ModelAndView handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
/*      */     ModelAndView mav;
/*  779 */     checkRequest(request);
/*      */ 
/*      */     
/*  782 */     if (this.synchronizeOnSession) {
/*  783 */       HttpSession session = request.getSession(false);
/*  784 */       if (session != null) {
/*  785 */         Object mutex = WebUtils.getSessionMutex(session);
/*  786 */         synchronized (mutex) {
/*  787 */           mav = invokeHandlerMethod(request, response, handlerMethod);
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/*  792 */         mav = invokeHandlerMethod(request, response, handlerMethod);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  797 */       mav = invokeHandlerMethod(request, response, handlerMethod);
/*      */     } 
/*      */     
/*  800 */     if (!response.containsHeader("Cache-Control")) {
/*  801 */       if (getSessionAttributesHandler(handlerMethod).hasSessionAttributes()) {
/*  802 */         applyCacheSeconds(response, this.cacheSecondsForSessionAttributeHandlers);
/*      */       } else {
/*      */         
/*  805 */         prepareResponse(response);
/*      */       } 
/*      */     }
/*      */     
/*  809 */     return mav;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected long getLastModifiedInternal(HttpServletRequest request, HandlerMethod handlerMethod) {
/*  819 */     return -1L;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private SessionAttributesHandler getSessionAttributesHandler(HandlerMethod handlerMethod) {
/*  828 */     Class<?> handlerType = handlerMethod.getBeanType();
/*  829 */     SessionAttributesHandler sessionAttrHandler = this.sessionAttributesHandlerCache.get(handlerType);
/*  830 */     if (sessionAttrHandler == null) {
/*  831 */       synchronized (this.sessionAttributesHandlerCache) {
/*  832 */         sessionAttrHandler = this.sessionAttributesHandlerCache.get(handlerType);
/*  833 */         if (sessionAttrHandler == null) {
/*  834 */           sessionAttrHandler = new SessionAttributesHandler(handlerType, this.sessionAttributeStore);
/*  835 */           this.sessionAttributesHandlerCache.put(handlerType, sessionAttrHandler);
/*      */         } 
/*      */       } 
/*      */     }
/*  839 */     return sessionAttrHandler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected ModelAndView invokeHandlerMethod(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
/*  852 */     ServletWebRequest webRequest = new ServletWebRequest(request, response);
/*      */     try {
/*  854 */       WebDataBinderFactory binderFactory = getDataBinderFactory(handlerMethod);
/*  855 */       ModelFactory modelFactory = getModelFactory(handlerMethod, binderFactory);
/*      */       
/*  857 */       ServletInvocableHandlerMethod invocableMethod = createInvocableHandlerMethod(handlerMethod);
/*  858 */       if (this.argumentResolvers != null) {
/*  859 */         invocableMethod.setHandlerMethodArgumentResolvers(this.argumentResolvers);
/*      */       }
/*  861 */       if (this.returnValueHandlers != null) {
/*  862 */         invocableMethod.setHandlerMethodReturnValueHandlers(this.returnValueHandlers);
/*      */       }
/*  864 */       invocableMethod.setDataBinderFactory(binderFactory);
/*  865 */       invocableMethod.setParameterNameDiscoverer(this.parameterNameDiscoverer);
/*      */       
/*  867 */       ModelAndViewContainer mavContainer = new ModelAndViewContainer();
/*  868 */       mavContainer.addAllAttributes(RequestContextUtils.getInputFlashMap(request));
/*  869 */       modelFactory.initModel((NativeWebRequest)webRequest, mavContainer, (HandlerMethod)invocableMethod);
/*  870 */       mavContainer.setIgnoreDefaultModelOnRedirect(this.ignoreDefaultModelOnRedirect);
/*      */       
/*  872 */       AsyncWebRequest asyncWebRequest = WebAsyncUtils.createAsyncWebRequest(request, response);
/*  873 */       asyncWebRequest.setTimeout(this.asyncRequestTimeout);
/*      */       
/*  875 */       WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager((ServletRequest)request);
/*  876 */       asyncManager.setTaskExecutor(this.taskExecutor);
/*  877 */       asyncManager.setAsyncWebRequest(asyncWebRequest);
/*  878 */       asyncManager.registerCallableInterceptors(this.callableInterceptors);
/*  879 */       asyncManager.registerDeferredResultInterceptors(this.deferredResultInterceptors);
/*      */       
/*  881 */       if (asyncManager.hasConcurrentResult()) {
/*  882 */         Object result = asyncManager.getConcurrentResult();
/*  883 */         mavContainer = (ModelAndViewContainer)asyncManager.getConcurrentResultContext()[0];
/*  884 */         asyncManager.clearConcurrentResult();
/*  885 */         LogFormatUtils.traceDebug(this.logger, traceOn -> {
/*      */               String formatted = LogFormatUtils.formatValue(result, !traceOn.booleanValue());
/*      */               return "Resume with async result [" + formatted + "]";
/*      */             });
/*  889 */         invocableMethod = invocableMethod.wrapConcurrentResult(result);
/*      */       } 
/*      */       
/*  892 */       invocableMethod.invokeAndHandle(webRequest, mavContainer, new Object[0]);
/*  893 */       if (asyncManager.isConcurrentHandlingStarted()) {
/*  894 */         return null;
/*      */       }
/*      */       
/*  897 */       return getModelAndView(mavContainer, modelFactory, (NativeWebRequest)webRequest);
/*      */     } finally {
/*      */       
/*  900 */       webRequest.requestCompleted();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ServletInvocableHandlerMethod createInvocableHandlerMethod(HandlerMethod handlerMethod) {
/*  911 */     return new ServletInvocableHandlerMethod(handlerMethod);
/*      */   }
/*      */   
/*      */   private ModelFactory getModelFactory(HandlerMethod handlerMethod, WebDataBinderFactory binderFactory) {
/*  915 */     SessionAttributesHandler sessionAttrHandler = getSessionAttributesHandler(handlerMethod);
/*  916 */     Class<?> handlerType = handlerMethod.getBeanType();
/*  917 */     Set<Method> methods = this.modelAttributeCache.get(handlerType);
/*  918 */     if (methods == null) {
/*  919 */       methods = MethodIntrospector.selectMethods(handlerType, MODEL_ATTRIBUTE_METHODS);
/*  920 */       this.modelAttributeCache.put(handlerType, methods);
/*      */     } 
/*  922 */     List<InvocableHandlerMethod> attrMethods = new ArrayList<>();
/*      */     
/*  924 */     this.modelAttributeAdviceCache.forEach((clazz, methodSet) -> {
/*      */           if (clazz.isApplicableToBeanType(handlerType)) {
/*      */             Object bean = clazz.resolveBean();
/*      */             for (Method method : methodSet) {
/*      */               attrMethods.add(createModelAttributeMethod(binderFactory, bean, method));
/*      */             }
/*      */           } 
/*      */         });
/*  932 */     for (Method method : methods) {
/*  933 */       Object bean = handlerMethod.getBean();
/*  934 */       attrMethods.add(createModelAttributeMethod(binderFactory, bean, method));
/*      */     } 
/*  936 */     return new ModelFactory(attrMethods, binderFactory, sessionAttrHandler);
/*      */   }
/*      */   
/*      */   private InvocableHandlerMethod createModelAttributeMethod(WebDataBinderFactory factory, Object bean, Method method) {
/*  940 */     InvocableHandlerMethod attrMethod = new InvocableHandlerMethod(bean, method);
/*  941 */     if (this.argumentResolvers != null) {
/*  942 */       attrMethod.setHandlerMethodArgumentResolvers(this.argumentResolvers);
/*      */     }
/*  944 */     attrMethod.setParameterNameDiscoverer(this.parameterNameDiscoverer);
/*  945 */     attrMethod.setDataBinderFactory(factory);
/*  946 */     return attrMethod;
/*      */   }
/*      */   
/*      */   private WebDataBinderFactory getDataBinderFactory(HandlerMethod handlerMethod) throws Exception {
/*  950 */     Class<?> handlerType = handlerMethod.getBeanType();
/*  951 */     Set<Method> methods = this.initBinderCache.get(handlerType);
/*  952 */     if (methods == null) {
/*  953 */       methods = MethodIntrospector.selectMethods(handlerType, INIT_BINDER_METHODS);
/*  954 */       this.initBinderCache.put(handlerType, methods);
/*      */     } 
/*  956 */     List<InvocableHandlerMethod> initBinderMethods = new ArrayList<>();
/*      */     
/*  958 */     this.initBinderAdviceCache.forEach((clazz, methodSet) -> {
/*      */           if (clazz.isApplicableToBeanType(handlerType)) {
/*      */             Object bean = clazz.resolveBean();
/*      */             for (Method method : methodSet) {
/*      */               initBinderMethods.add(createInitBinderMethod(bean, method));
/*      */             }
/*      */           } 
/*      */         });
/*  966 */     for (Method method : methods) {
/*  967 */       Object bean = handlerMethod.getBean();
/*  968 */       initBinderMethods.add(createInitBinderMethod(bean, method));
/*      */     } 
/*  970 */     return (WebDataBinderFactory)createDataBinderFactory(initBinderMethods);
/*      */   }
/*      */   
/*      */   private InvocableHandlerMethod createInitBinderMethod(Object bean, Method method) {
/*  974 */     InvocableHandlerMethod binderMethod = new InvocableHandlerMethod(bean, method);
/*  975 */     if (this.initBinderArgumentResolvers != null) {
/*  976 */       binderMethod.setHandlerMethodArgumentResolvers(this.initBinderArgumentResolvers);
/*      */     }
/*  978 */     binderMethod.setDataBinderFactory((WebDataBinderFactory)new DefaultDataBinderFactory(this.webBindingInitializer));
/*  979 */     binderMethod.setParameterNameDiscoverer(this.parameterNameDiscoverer);
/*  980 */     return binderMethod;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected InitBinderDataBinderFactory createDataBinderFactory(List<InvocableHandlerMethod> binderMethods) throws Exception {
/*  994 */     return new ServletRequestDataBinderFactory(binderMethods, getWebBindingInitializer());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private ModelAndView getModelAndView(ModelAndViewContainer mavContainer, ModelFactory modelFactory, NativeWebRequest webRequest) throws Exception {
/* 1001 */     modelFactory.updateModel(webRequest, mavContainer);
/* 1002 */     if (mavContainer.isRequestHandled()) {
/* 1003 */       return null;
/*      */     }
/* 1005 */     ModelMap model = mavContainer.getModel();
/* 1006 */     ModelAndView mav = new ModelAndView(mavContainer.getViewName(), (Map)model, mavContainer.getStatus());
/* 1007 */     if (!mavContainer.isViewReference()) {
/* 1008 */       mav.setView((View)mavContainer.getView());
/*      */     }
/* 1010 */     if (model instanceof RedirectAttributes) {
/* 1011 */       Map<String, ?> flashAttributes = ((RedirectAttributes)model).getFlashAttributes();
/* 1012 */       HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/* 1013 */       if (request != null) {
/* 1014 */         RequestContextUtils.getOutputFlashMap(request).putAll(flashAttributes);
/*      */       }
/*      */     } 
/* 1017 */     return mav;
/*      */   }
/*      */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/RequestMappingHandlerAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */