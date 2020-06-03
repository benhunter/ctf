/*      */ package org.springframework.web.servlet.config.annotation;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.function.Predicate;
/*      */ import javax.servlet.ServletContext;
/*      */ import org.springframework.beans.BeanUtils;
/*      */ import org.springframework.beans.factory.BeanFactoryUtils;
/*      */ import org.springframework.beans.factory.BeanInitializationException;
/*      */ import org.springframework.beans.factory.ListableBeanFactory;
/*      */ import org.springframework.context.ApplicationContext;
/*      */ import org.springframework.context.ApplicationContextAware;
/*      */ import org.springframework.context.MessageSource;
/*      */ import org.springframework.context.annotation.Bean;
/*      */ import org.springframework.context.annotation.Lazy;
/*      */ import org.springframework.core.convert.ConversionService;
/*      */ import org.springframework.format.FormatterRegistry;
/*      */ import org.springframework.format.support.DefaultFormattingConversionService;
/*      */ import org.springframework.format.support.FormattingConversionService;
/*      */ import org.springframework.http.MediaType;
/*      */ import org.springframework.http.converter.ByteArrayHttpMessageConverter;
/*      */ import org.springframework.http.converter.HttpMessageConverter;
/*      */ import org.springframework.http.converter.ResourceHttpMessageConverter;
/*      */ import org.springframework.http.converter.ResourceRegionHttpMessageConverter;
/*      */ import org.springframework.http.converter.StringHttpMessageConverter;
/*      */ import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
/*      */ import org.springframework.http.converter.feed.AtomFeedHttpMessageConverter;
/*      */ import org.springframework.http.converter.feed.RssChannelHttpMessageConverter;
/*      */ import org.springframework.http.converter.json.GsonHttpMessageConverter;
/*      */ import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
/*      */ import org.springframework.http.converter.json.JsonbHttpMessageConverter;
/*      */ import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
/*      */ import org.springframework.http.converter.smile.MappingJackson2SmileHttpMessageConverter;
/*      */ import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
/*      */ import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
/*      */ import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
/*      */ import org.springframework.http.converter.xml.SourceHttpMessageConverter;
/*      */ import org.springframework.lang.Nullable;
/*      */ import org.springframework.util.AntPathMatcher;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.PathMatcher;
/*      */ import org.springframework.validation.Errors;
/*      */ import org.springframework.validation.MessageCodesResolver;
/*      */ import org.springframework.validation.Validator;
/*      */ import org.springframework.web.accept.ContentNegotiationManager;
/*      */ import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
/*      */ import org.springframework.web.bind.support.WebBindingInitializer;
/*      */ import org.springframework.web.context.ServletContextAware;
/*      */ import org.springframework.web.cors.CorsConfiguration;
/*      */ import org.springframework.web.method.support.CompositeUriComponentsContributor;
/*      */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*      */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*      */ import org.springframework.web.servlet.HandlerExceptionResolver;
/*      */ import org.springframework.web.servlet.HandlerInterceptor;
/*      */ import org.springframework.web.servlet.HandlerMapping;
/*      */ import org.springframework.web.servlet.ViewResolver;
/*      */ import org.springframework.web.servlet.handler.AbstractHandlerMapping;
/*      */ import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
/*      */ import org.springframework.web.servlet.handler.ConversionServiceExposingInterceptor;
/*      */ import org.springframework.web.servlet.handler.HandlerExceptionResolverComposite;
/*      */ import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
/*      */ import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
/*      */ import org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter;
/*      */ import org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter;
/*      */ import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;
/*      */ import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
/*      */ import org.springframework.web.servlet.mvc.method.annotation.JsonViewRequestBodyAdvice;
/*      */ import org.springframework.web.servlet.mvc.method.annotation.JsonViewResponseBodyAdvice;
/*      */ import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
/*      */ import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
/*      */ import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
/*      */ import org.springframework.web.servlet.resource.ResourceUrlProvider;
/*      */ import org.springframework.web.servlet.resource.ResourceUrlProviderExposingInterceptor;
/*      */ import org.springframework.web.servlet.view.InternalResourceViewResolver;
/*      */ import org.springframework.web.servlet.view.ViewResolverComposite;
/*      */ import org.springframework.web.util.UrlPathHelper;
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
/*      */ public class WebMvcConfigurationSupport
/*      */   implements ApplicationContextAware, ServletContextAware
/*      */ {
/*      */   private static final boolean romePresent;
/*      */   private static final boolean jaxb2Present;
/*      */   private static final boolean jackson2Present;
/*      */   private static final boolean jackson2XmlPresent;
/*      */   private static final boolean jackson2SmilePresent;
/*      */   private static final boolean jackson2CborPresent;
/*      */   private static final boolean gsonPresent;
/*      */   private static final boolean jsonbPresent;
/*      */   @Nullable
/*      */   private ApplicationContext applicationContext;
/*      */   @Nullable
/*      */   private ServletContext servletContext;
/*      */   @Nullable
/*      */   private List<Object> interceptors;
/*      */   @Nullable
/*      */   private PathMatchConfigurer pathMatchConfigurer;
/*      */   @Nullable
/*      */   private ContentNegotiationManager contentNegotiationManager;
/*      */   @Nullable
/*      */   private List<HandlerMethodArgumentResolver> argumentResolvers;
/*      */   @Nullable
/*      */   private List<HandlerMethodReturnValueHandler> returnValueHandlers;
/*      */   @Nullable
/*      */   private List<HttpMessageConverter<?>> messageConverters;
/*      */   @Nullable
/*      */   private Map<String, CorsConfiguration> corsConfigurations;
/*      */   
/*      */   static {
/*  192 */     ClassLoader classLoader = WebMvcConfigurationSupport.class.getClassLoader();
/*  193 */     romePresent = ClassUtils.isPresent("com.rometools.rome.feed.WireFeed", classLoader);
/*  194 */     jaxb2Present = ClassUtils.isPresent("javax.xml.bind.Binder", classLoader);
/*      */     
/*  196 */     jackson2Present = (ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", classLoader) && ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", classLoader));
/*  197 */     jackson2XmlPresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.xml.XmlMapper", classLoader);
/*  198 */     jackson2SmilePresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.smile.SmileFactory", classLoader);
/*  199 */     jackson2CborPresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.cbor.CBORFactory", classLoader);
/*  200 */     gsonPresent = ClassUtils.isPresent("com.google.gson.Gson", classLoader);
/*  201 */     jsonbPresent = ClassUtils.isPresent("javax.json.bind.Jsonb", classLoader);
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
/*      */   public void setApplicationContext(@Nullable ApplicationContext applicationContext) {
/*  238 */     this.applicationContext = applicationContext;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public final ApplicationContext getApplicationContext() {
/*  247 */     return this.applicationContext;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setServletContext(@Nullable ServletContext servletContext) {
/*  256 */     this.servletContext = servletContext;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public final ServletContext getServletContext() {
/*  265 */     return this.servletContext;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Bean
/*      */   public RequestMappingHandlerMapping requestMappingHandlerMapping() {
/*  275 */     RequestMappingHandlerMapping mapping = createRequestMappingHandlerMapping();
/*  276 */     mapping.setOrder(0);
/*  277 */     mapping.setInterceptors(getInterceptors());
/*  278 */     mapping.setContentNegotiationManager(mvcContentNegotiationManager());
/*  279 */     mapping.setCorsConfigurations(getCorsConfigurations());
/*      */     
/*  281 */     PathMatchConfigurer configurer = getPathMatchConfigurer();
/*      */     
/*  283 */     Boolean useSuffixPatternMatch = configurer.isUseSuffixPatternMatch();
/*  284 */     if (useSuffixPatternMatch != null) {
/*  285 */       mapping.setUseSuffixPatternMatch(useSuffixPatternMatch.booleanValue());
/*      */     }
/*  287 */     Boolean useRegisteredSuffixPatternMatch = configurer.isUseRegisteredSuffixPatternMatch();
/*  288 */     if (useRegisteredSuffixPatternMatch != null) {
/*  289 */       mapping.setUseRegisteredSuffixPatternMatch(useRegisteredSuffixPatternMatch.booleanValue());
/*      */     }
/*  291 */     Boolean useTrailingSlashMatch = configurer.isUseTrailingSlashMatch();
/*  292 */     if (useTrailingSlashMatch != null) {
/*  293 */       mapping.setUseTrailingSlashMatch(useTrailingSlashMatch.booleanValue());
/*      */     }
/*      */     
/*  296 */     UrlPathHelper pathHelper = configurer.getUrlPathHelper();
/*  297 */     if (pathHelper != null) {
/*  298 */       mapping.setUrlPathHelper(pathHelper);
/*      */     }
/*  300 */     PathMatcher pathMatcher = configurer.getPathMatcher();
/*  301 */     if (pathMatcher != null) {
/*  302 */       mapping.setPathMatcher(pathMatcher);
/*      */     }
/*  304 */     Map<String, Predicate<Class<?>>> pathPrefixes = configurer.getPathPrefixes();
/*  305 */     if (pathPrefixes != null) {
/*  306 */       mapping.setPathPrefixes(pathPrefixes);
/*      */     }
/*      */     
/*  309 */     return mapping;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
/*  318 */     return new RequestMappingHandlerMapping();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final Object[] getInterceptors() {
/*  327 */     if (this.interceptors == null) {
/*  328 */       InterceptorRegistry registry = new InterceptorRegistry();
/*  329 */       addInterceptors(registry);
/*  330 */       registry.addInterceptor((HandlerInterceptor)new ConversionServiceExposingInterceptor((ConversionService)mvcConversionService()));
/*  331 */       registry.addInterceptor((HandlerInterceptor)new ResourceUrlProviderExposingInterceptor(mvcResourceUrlProvider()));
/*  332 */       this.interceptors = registry.getInterceptors();
/*      */     } 
/*  334 */     return this.interceptors.toArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addInterceptors(InterceptorRegistry registry) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected PathMatchConfigurer getPathMatchConfigurer() {
/*  351 */     if (this.pathMatchConfigurer == null) {
/*  352 */       this.pathMatchConfigurer = new PathMatchConfigurer();
/*  353 */       configurePathMatch(this.pathMatchConfigurer);
/*      */     } 
/*  355 */     return this.pathMatchConfigurer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void configurePathMatch(PathMatchConfigurer configurer) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Bean
/*      */   public PathMatcher mvcPathMatcher() {
/*  375 */     PathMatcher pathMatcher = getPathMatchConfigurer().getPathMatcher();
/*  376 */     return (pathMatcher != null) ? pathMatcher : (PathMatcher)new AntPathMatcher();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Bean
/*      */   public UrlPathHelper mvcUrlPathHelper() {
/*  388 */     UrlPathHelper pathHelper = getPathMatchConfigurer().getUrlPathHelper();
/*  389 */     return (pathHelper != null) ? pathHelper : new UrlPathHelper();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Bean
/*      */   public ContentNegotiationManager mvcContentNegotiationManager() {
/*  398 */     if (this.contentNegotiationManager == null) {
/*  399 */       ContentNegotiationConfigurer configurer = new ContentNegotiationConfigurer(this.servletContext);
/*  400 */       configurer.mediaTypes(getDefaultMediaTypes());
/*  401 */       configureContentNegotiation(configurer);
/*  402 */       this.contentNegotiationManager = configurer.buildContentNegotiationManager();
/*      */     } 
/*  404 */     return this.contentNegotiationManager;
/*      */   }
/*      */   
/*      */   protected Map<String, MediaType> getDefaultMediaTypes() {
/*  408 */     Map<String, MediaType> map = new HashMap<>(4);
/*  409 */     if (romePresent) {
/*  410 */       map.put("atom", MediaType.APPLICATION_ATOM_XML);
/*  411 */       map.put("rss", MediaType.APPLICATION_RSS_XML);
/*      */     } 
/*  413 */     if (jaxb2Present || jackson2XmlPresent) {
/*  414 */       map.put("xml", MediaType.APPLICATION_XML);
/*      */     }
/*  416 */     if (jackson2Present || gsonPresent || jsonbPresent) {
/*  417 */       map.put("json", MediaType.APPLICATION_JSON);
/*      */     }
/*  419 */     if (jackson2SmilePresent) {
/*  420 */       map.put("smile", MediaType.valueOf("application/x-jackson-smile"));
/*      */     }
/*  422 */     if (jackson2CborPresent) {
/*  423 */       map.put("cbor", MediaType.valueOf("application/cbor"));
/*      */     }
/*  425 */     return map;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void configureContentNegotiation(ContentNegotiationConfigurer configurer) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Bean
/*      */   @Nullable
/*      */   public HandlerMapping viewControllerHandlerMapping() {
/*  443 */     ViewControllerRegistry registry = new ViewControllerRegistry(this.applicationContext);
/*  444 */     addViewControllers(registry);
/*      */     
/*  446 */     SimpleUrlHandlerMapping simpleUrlHandlerMapping = registry.buildHandlerMapping();
/*  447 */     if (simpleUrlHandlerMapping == null) {
/*  448 */       return null;
/*      */     }
/*  450 */     simpleUrlHandlerMapping.setPathMatcher(mvcPathMatcher());
/*  451 */     simpleUrlHandlerMapping.setUrlPathHelper(mvcUrlPathHelper());
/*  452 */     simpleUrlHandlerMapping.setInterceptors(getInterceptors());
/*  453 */     simpleUrlHandlerMapping.setCorsConfigurations(getCorsConfigurations());
/*  454 */     return (HandlerMapping)simpleUrlHandlerMapping;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addViewControllers(ViewControllerRegistry registry) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Bean
/*      */   public BeanNameUrlHandlerMapping beanNameHandlerMapping() {
/*  470 */     BeanNameUrlHandlerMapping mapping = new BeanNameUrlHandlerMapping();
/*  471 */     mapping.setOrder(2);
/*  472 */     mapping.setInterceptors(getInterceptors());
/*  473 */     mapping.setCorsConfigurations(getCorsConfigurations());
/*  474 */     return mapping;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Bean
/*      */   @Nullable
/*      */   public HandlerMapping resourceHandlerMapping() {
/*  485 */     Assert.state((this.applicationContext != null), "No ApplicationContext set");
/*  486 */     Assert.state((this.servletContext != null), "No ServletContext set");
/*      */ 
/*      */     
/*  489 */     ResourceHandlerRegistry registry = new ResourceHandlerRegistry(this.applicationContext, this.servletContext, mvcContentNegotiationManager(), mvcUrlPathHelper());
/*  490 */     addResourceHandlers(registry);
/*      */     
/*  492 */     AbstractHandlerMapping handlerMapping = registry.getHandlerMapping();
/*  493 */     if (handlerMapping == null) {
/*  494 */       return null;
/*      */     }
/*  496 */     handlerMapping.setPathMatcher(mvcPathMatcher());
/*  497 */     handlerMapping.setUrlPathHelper(mvcUrlPathHelper());
/*  498 */     handlerMapping.setInterceptors(getInterceptors());
/*  499 */     handlerMapping.setCorsConfigurations(getCorsConfigurations());
/*  500 */     return (HandlerMapping)handlerMapping;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addResourceHandlers(ResourceHandlerRegistry registry) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Bean
/*      */   public ResourceUrlProvider mvcResourceUrlProvider() {
/*  516 */     ResourceUrlProvider urlProvider = new ResourceUrlProvider();
/*  517 */     UrlPathHelper pathHelper = getPathMatchConfigurer().getUrlPathHelper();
/*  518 */     if (pathHelper != null) {
/*  519 */       urlProvider.setUrlPathHelper(pathHelper);
/*      */     }
/*  521 */     PathMatcher pathMatcher = getPathMatchConfigurer().getPathMatcher();
/*  522 */     if (pathMatcher != null) {
/*  523 */       urlProvider.setPathMatcher(pathMatcher);
/*      */     }
/*  525 */     return urlProvider;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Bean
/*      */   @Nullable
/*      */   public HandlerMapping defaultServletHandlerMapping() {
/*  536 */     Assert.state((this.servletContext != null), "No ServletContext set");
/*  537 */     DefaultServletHandlerConfigurer configurer = new DefaultServletHandlerConfigurer(this.servletContext);
/*  538 */     configureDefaultServletHandling(configurer);
/*  539 */     return (HandlerMapping)configurer.buildHandlerMapping();
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
/*      */   protected void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Bean
/*      */   public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
/*  561 */     RequestMappingHandlerAdapter adapter = createRequestMappingHandlerAdapter();
/*  562 */     adapter.setContentNegotiationManager(mvcContentNegotiationManager());
/*  563 */     adapter.setMessageConverters(getMessageConverters());
/*  564 */     adapter.setWebBindingInitializer((WebBindingInitializer)getConfigurableWebBindingInitializer());
/*  565 */     adapter.setCustomArgumentResolvers(getArgumentResolvers());
/*  566 */     adapter.setCustomReturnValueHandlers(getReturnValueHandlers());
/*      */     
/*  568 */     if (jackson2Present) {
/*  569 */       adapter.setRequestBodyAdvice(Collections.singletonList(new JsonViewRequestBodyAdvice()));
/*  570 */       adapter.setResponseBodyAdvice(Collections.singletonList(new JsonViewResponseBodyAdvice()));
/*      */     } 
/*      */     
/*  573 */     AsyncSupportConfigurer configurer = new AsyncSupportConfigurer();
/*  574 */     configureAsyncSupport(configurer);
/*  575 */     if (configurer.getTaskExecutor() != null) {
/*  576 */       adapter.setTaskExecutor(configurer.getTaskExecutor());
/*      */     }
/*  578 */     if (configurer.getTimeout() != null) {
/*  579 */       adapter.setAsyncRequestTimeout(configurer.getTimeout().longValue());
/*      */     }
/*  581 */     adapter.setCallableInterceptors(configurer.getCallableInterceptors());
/*  582 */     adapter.setDeferredResultInterceptors(configurer.getDeferredResultInterceptors());
/*      */     
/*  584 */     return adapter;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected RequestMappingHandlerAdapter createRequestMappingHandlerAdapter() {
/*  593 */     return new RequestMappingHandlerAdapter();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ConfigurableWebBindingInitializer getConfigurableWebBindingInitializer() {
/*  601 */     ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
/*  602 */     initializer.setConversionService((ConversionService)mvcConversionService());
/*  603 */     initializer.setValidator(mvcValidator());
/*  604 */     MessageCodesResolver messageCodesResolver = getMessageCodesResolver();
/*  605 */     if (messageCodesResolver != null) {
/*  606 */       initializer.setMessageCodesResolver(messageCodesResolver);
/*      */     }
/*  608 */     return initializer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected MessageCodesResolver getMessageCodesResolver() {
/*  616 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void configureAsyncSupport(AsyncSupportConfigurer configurer) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Bean
/*      */   public FormattingConversionService mvcConversionService() {
/*  632 */     DefaultFormattingConversionService defaultFormattingConversionService = new DefaultFormattingConversionService();
/*  633 */     addFormatters((FormatterRegistry)defaultFormattingConversionService);
/*  634 */     return (FormattingConversionService)defaultFormattingConversionService;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addFormatters(FormatterRegistry registry) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Bean
/*      */   public Validator mvcValidator() {
/*  655 */     Validator validator = getValidator();
/*  656 */     if (validator == null) {
/*  657 */       if (ClassUtils.isPresent("javax.validation.Validator", getClass().getClassLoader())) {
/*      */         Class<?> clazz;
/*      */         try {
/*  660 */           String className = "org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean";
/*  661 */           clazz = ClassUtils.forName(className, WebMvcConfigurationSupport.class.getClassLoader());
/*      */         }
/*  663 */         catch (ClassNotFoundException|LinkageError ex) {
/*  664 */           throw new BeanInitializationException("Failed to resolve default validator class", ex);
/*      */         } 
/*  666 */         validator = (Validator)BeanUtils.instantiateClass(clazz);
/*      */       } else {
/*      */         
/*  669 */         validator = new NoOpValidator();
/*      */       } 
/*      */     }
/*  672 */     return validator;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected Validator getValidator() {
/*  680 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final List<HandlerMethodArgumentResolver> getArgumentResolvers() {
/*  690 */     if (this.argumentResolvers == null) {
/*  691 */       this.argumentResolvers = new ArrayList<>();
/*  692 */       addArgumentResolvers(this.argumentResolvers);
/*      */     } 
/*  694 */     return this.argumentResolvers;
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
/*      */   protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final List<HandlerMethodReturnValueHandler> getReturnValueHandlers() {
/*  716 */     if (this.returnValueHandlers == null) {
/*  717 */       this.returnValueHandlers = new ArrayList<>();
/*  718 */       addReturnValueHandlers(this.returnValueHandlers);
/*      */     } 
/*  720 */     return this.returnValueHandlers;
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
/*      */   protected void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final List<HttpMessageConverter<?>> getMessageConverters() {
/*  743 */     if (this.messageConverters == null) {
/*  744 */       this.messageConverters = new ArrayList<>();
/*  745 */       configureMessageConverters(this.messageConverters);
/*  746 */       if (this.messageConverters.isEmpty()) {
/*  747 */         addDefaultHttpMessageConverters(this.messageConverters);
/*      */       }
/*  749 */       extendMessageConverters(this.messageConverters);
/*      */     } 
/*  751 */     return this.messageConverters;
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
/*      */   protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void addDefaultHttpMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
/*  782 */     StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
/*  783 */     stringHttpMessageConverter.setWriteAcceptCharset(false);
/*      */     
/*  785 */     messageConverters.add(new ByteArrayHttpMessageConverter());
/*  786 */     messageConverters.add(stringHttpMessageConverter);
/*  787 */     messageConverters.add(new ResourceHttpMessageConverter());
/*  788 */     messageConverters.add(new ResourceRegionHttpMessageConverter());
/*      */     try {
/*  790 */       messageConverters.add(new SourceHttpMessageConverter());
/*      */     }
/*  792 */     catch (Throwable throwable) {}
/*      */ 
/*      */     
/*  795 */     messageConverters.add(new AllEncompassingFormHttpMessageConverter());
/*      */     
/*  797 */     if (romePresent) {
/*  798 */       messageConverters.add(new AtomFeedHttpMessageConverter());
/*  799 */       messageConverters.add(new RssChannelHttpMessageConverter());
/*      */     } 
/*      */     
/*  802 */     if (jackson2XmlPresent) {
/*  803 */       Jackson2ObjectMapperBuilder builder = Jackson2ObjectMapperBuilder.xml();
/*  804 */       if (this.applicationContext != null) {
/*  805 */         builder.applicationContext(this.applicationContext);
/*      */       }
/*  807 */       messageConverters.add(new MappingJackson2XmlHttpMessageConverter(builder.build()));
/*      */     }
/*  809 */     else if (jaxb2Present) {
/*  810 */       messageConverters.add(new Jaxb2RootElementHttpMessageConverter());
/*      */     } 
/*      */     
/*  813 */     if (jackson2Present) {
/*  814 */       Jackson2ObjectMapperBuilder builder = Jackson2ObjectMapperBuilder.json();
/*  815 */       if (this.applicationContext != null) {
/*  816 */         builder.applicationContext(this.applicationContext);
/*      */       }
/*  818 */       messageConverters.add(new MappingJackson2HttpMessageConverter(builder.build()));
/*      */     }
/*  820 */     else if (gsonPresent) {
/*  821 */       messageConverters.add(new GsonHttpMessageConverter());
/*      */     }
/*  823 */     else if (jsonbPresent) {
/*  824 */       messageConverters.add(new JsonbHttpMessageConverter());
/*      */     } 
/*      */     
/*  827 */     if (jackson2SmilePresent) {
/*  828 */       Jackson2ObjectMapperBuilder builder = Jackson2ObjectMapperBuilder.smile();
/*  829 */       if (this.applicationContext != null) {
/*  830 */         builder.applicationContext(this.applicationContext);
/*      */       }
/*  832 */       messageConverters.add(new MappingJackson2SmileHttpMessageConverter(builder.build()));
/*      */     } 
/*  834 */     if (jackson2CborPresent) {
/*  835 */       Jackson2ObjectMapperBuilder builder = Jackson2ObjectMapperBuilder.cbor();
/*  836 */       if (this.applicationContext != null) {
/*  837 */         builder.applicationContext(this.applicationContext);
/*      */       }
/*  839 */       messageConverters.add(new MappingJackson2CborHttpMessageConverter(builder.build()));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Bean
/*      */   public CompositeUriComponentsContributor mvcUriComponentsContributor() {
/*  850 */     return new CompositeUriComponentsContributor(
/*  851 */         requestMappingHandlerAdapter().getArgumentResolvers(), (ConversionService)mvcConversionService());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Bean
/*      */   public HttpRequestHandlerAdapter httpRequestHandlerAdapter() {
/*  860 */     return new HttpRequestHandlerAdapter();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Bean
/*      */   public SimpleControllerHandlerAdapter simpleControllerHandlerAdapter() {
/*  869 */     return new SimpleControllerHandlerAdapter();
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
/*      */   @Bean
/*      */   public HandlerExceptionResolver handlerExceptionResolver() {
/*  882 */     List<HandlerExceptionResolver> exceptionResolvers = new ArrayList<>();
/*  883 */     configureHandlerExceptionResolvers(exceptionResolvers);
/*  884 */     if (exceptionResolvers.isEmpty()) {
/*  885 */       addDefaultHandlerExceptionResolvers(exceptionResolvers);
/*      */     }
/*  887 */     extendHandlerExceptionResolvers(exceptionResolvers);
/*  888 */     HandlerExceptionResolverComposite composite = new HandlerExceptionResolverComposite();
/*  889 */     composite.setOrder(0);
/*  890 */     composite.setExceptionResolvers(exceptionResolvers);
/*  891 */     return (HandlerExceptionResolver)composite;
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
/*      */   protected void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {}
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
/*      */   protected void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {}
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
/*      */   protected final void addDefaultHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
/*  929 */     ExceptionHandlerExceptionResolver exceptionHandlerResolver = createExceptionHandlerExceptionResolver();
/*  930 */     exceptionHandlerResolver.setContentNegotiationManager(mvcContentNegotiationManager());
/*  931 */     exceptionHandlerResolver.setMessageConverters(getMessageConverters());
/*  932 */     exceptionHandlerResolver.setCustomArgumentResolvers(getArgumentResolvers());
/*  933 */     exceptionHandlerResolver.setCustomReturnValueHandlers(getReturnValueHandlers());
/*  934 */     if (jackson2Present) {
/*  935 */       exceptionHandlerResolver.setResponseBodyAdvice(
/*  936 */           Collections.singletonList(new JsonViewResponseBodyAdvice()));
/*      */     }
/*  938 */     if (this.applicationContext != null) {
/*  939 */       exceptionHandlerResolver.setApplicationContext(this.applicationContext);
/*      */     }
/*  941 */     exceptionHandlerResolver.afterPropertiesSet();
/*  942 */     exceptionResolvers.add(exceptionHandlerResolver);
/*      */     
/*  944 */     ResponseStatusExceptionResolver responseStatusResolver = new ResponseStatusExceptionResolver();
/*  945 */     responseStatusResolver.setMessageSource((MessageSource)this.applicationContext);
/*  946 */     exceptionResolvers.add(responseStatusResolver);
/*      */     
/*  948 */     exceptionResolvers.add(new DefaultHandlerExceptionResolver());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ExceptionHandlerExceptionResolver createExceptionHandlerExceptionResolver() {
/*  957 */     return new ExceptionHandlerExceptionResolver();
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
/*      */   @Bean
/*      */   public ViewResolver mvcViewResolver() {
/*  975 */     ViewResolverRegistry registry = new ViewResolverRegistry(mvcContentNegotiationManager(), this.applicationContext);
/*  976 */     configureViewResolvers(registry);
/*      */     
/*  978 */     if (registry.getViewResolvers().isEmpty() && this.applicationContext != null) {
/*  979 */       String[] names = BeanFactoryUtils.beanNamesForTypeIncludingAncestors((ListableBeanFactory)this.applicationContext, ViewResolver.class, true, false);
/*      */       
/*  981 */       if (names.length == 1) {
/*  982 */         registry.getViewResolvers().add(new InternalResourceViewResolver());
/*      */       }
/*      */     } 
/*      */     
/*  986 */     ViewResolverComposite composite = new ViewResolverComposite();
/*  987 */     composite.setOrder(registry.getOrder());
/*  988 */     composite.setViewResolvers(registry.getViewResolvers());
/*  989 */     if (this.applicationContext != null) {
/*  990 */       composite.setApplicationContext(this.applicationContext);
/*      */     }
/*  992 */     if (this.servletContext != null) {
/*  993 */       composite.setServletContext(this.servletContext);
/*      */     }
/*  995 */     return (ViewResolver)composite;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void configureViewResolvers(ViewResolverRegistry registry) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final Map<String, CorsConfiguration> getCorsConfigurations() {
/* 1011 */     if (this.corsConfigurations == null) {
/* 1012 */       CorsRegistry registry = new CorsRegistry();
/* 1013 */       addCorsMappings(registry);
/* 1014 */       this.corsConfigurations = registry.getCorsConfigurations();
/*      */     } 
/* 1016 */     return this.corsConfigurations;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addCorsMappings(CorsRegistry registry) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Bean
/*      */   @Lazy
/*      */   public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
/* 1030 */     return new HandlerMappingIntrospector();
/*      */   }
/*      */   
/*      */   private static final class NoOpValidator
/*      */     implements Validator {
/*      */     private NoOpValidator() {}
/*      */     
/*      */     public boolean supports(Class<?> clazz) {
/* 1038 */       return false;
/*      */     }
/*      */     
/*      */     public void validate(@Nullable Object target, Errors errors) {}
/*      */   }
/*      */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/config/annotation/WebMvcConfigurationSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */