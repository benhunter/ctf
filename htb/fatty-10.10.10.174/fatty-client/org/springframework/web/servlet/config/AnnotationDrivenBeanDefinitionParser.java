/*     */ package org.springframework.web.servlet.config;
/*     */ 
/*     */ import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
/*     */ import com.fasterxml.jackson.dataformat.smile.SmileFactory;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*     */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.ComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
/*     */ import org.springframework.beans.factory.support.GenericBeanDefinition;
/*     */ import org.springframework.beans.factory.support.ManagedList;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*     */ import org.springframework.beans.factory.xml.ParserContext;
/*     */ import org.springframework.beans.factory.xml.XmlReaderContext;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.format.support.FormattingConversionServiceFactoryBean;
/*     */ import org.springframework.http.converter.ByteArrayHttpMessageConverter;
/*     */ import org.springframework.http.converter.ResourceHttpMessageConverter;
/*     */ import org.springframework.http.converter.ResourceRegionHttpMessageConverter;
/*     */ import org.springframework.http.converter.StringHttpMessageConverter;
/*     */ import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
/*     */ import org.springframework.http.converter.feed.AtomFeedHttpMessageConverter;
/*     */ import org.springframework.http.converter.feed.RssChannelHttpMessageConverter;
/*     */ import org.springframework.http.converter.json.GsonHttpMessageConverter;
/*     */ import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
/*     */ import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
/*     */ import org.springframework.http.converter.smile.MappingJackson2SmileHttpMessageConverter;
/*     */ import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
/*     */ import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
/*     */ import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
/*     */ import org.springframework.http.converter.xml.SourceHttpMessageConverter;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.xml.DomUtils;
/*     */ import org.springframework.web.accept.ContentNegotiationManagerFactoryBean;
/*     */ import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
/*     */ import org.springframework.web.bind.support.WebArgumentResolver;
/*     */ import org.springframework.web.method.support.CompositeUriComponentsContributor;
/*     */ import org.springframework.web.servlet.handler.ConversionServiceExposingInterceptor;
/*     */ import org.springframework.web.servlet.handler.MappedInterceptor;
/*     */ import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;
/*     */ import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
/*     */ import org.springframework.web.servlet.mvc.method.annotation.JsonViewRequestBodyAdvice;
/*     */ import org.springframework.web.servlet.mvc.method.annotation.JsonViewResponseBodyAdvice;
/*     */ import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
/*     */ import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
/*     */ import org.springframework.web.servlet.mvc.method.annotation.ServletWebArgumentResolverAdapter;
/*     */ import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
/*     */ import org.w3c.dom.Element;
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
/*     */ class AnnotationDrivenBeanDefinitionParser
/*     */   implements BeanDefinitionParser
/*     */ {
/* 156 */   public static final String HANDLER_MAPPING_BEAN_NAME = RequestMappingHandlerMapping.class.getName();
/*     */   
/* 158 */   public static final String HANDLER_ADAPTER_BEAN_NAME = RequestMappingHandlerAdapter.class.getName();
/*     */   
/*     */   public static final String CONTENT_NEGOTIATION_MANAGER_BEAN_NAME = "mvcContentNegotiationManager";
/*     */   
/*     */   private static final boolean javaxValidationPresent;
/*     */   
/*     */   private static boolean romePresent;
/*     */   
/*     */   private static final boolean jaxb2Present;
/*     */   
/*     */   private static final boolean jackson2Present;
/*     */   
/*     */   private static final boolean jackson2XmlPresent;
/*     */   
/*     */   private static final boolean jackson2SmilePresent;
/*     */   
/*     */   private static final boolean jackson2CborPresent;
/*     */   
/*     */   private static final boolean gsonPresent;
/*     */ 
/*     */   
/*     */   static {
/* 180 */     ClassLoader classLoader = AnnotationDrivenBeanDefinitionParser.class.getClassLoader();
/* 181 */     javaxValidationPresent = ClassUtils.isPresent("javax.validation.Validator", classLoader);
/* 182 */     romePresent = ClassUtils.isPresent("com.rometools.rome.feed.WireFeed", classLoader);
/* 183 */     jaxb2Present = ClassUtils.isPresent("javax.xml.bind.Binder", classLoader);
/*     */     
/* 185 */     jackson2Present = (ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", classLoader) && ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", classLoader));
/* 186 */     jackson2XmlPresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.xml.XmlMapper", classLoader);
/* 187 */     jackson2SmilePresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.smile.SmileFactory", classLoader);
/* 188 */     jackson2CborPresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.cbor.CBORFactory", classLoader);
/* 189 */     gsonPresent = ClassUtils.isPresent("com.google.gson.Gson", classLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BeanDefinition parse(Element element, ParserContext context) {
/* 196 */     Object source = context.extractSource(element);
/* 197 */     XmlReaderContext readerContext = context.getReaderContext();
/*     */     
/* 199 */     CompositeComponentDefinition compDefinition = new CompositeComponentDefinition(element.getTagName(), source);
/* 200 */     context.pushContainingComponent(compDefinition);
/*     */     
/* 202 */     RuntimeBeanReference contentNegotiationManager = getContentNegotiationManager(element, source, context);
/*     */     
/* 204 */     RootBeanDefinition handlerMappingDef = new RootBeanDefinition(RequestMappingHandlerMapping.class);
/* 205 */     handlerMappingDef.setSource(source);
/* 206 */     handlerMappingDef.setRole(2);
/* 207 */     handlerMappingDef.getPropertyValues().add("order", Integer.valueOf(0));
/* 208 */     handlerMappingDef.getPropertyValues().add("contentNegotiationManager", contentNegotiationManager);
/*     */     
/* 210 */     if (element.hasAttribute("enable-matrix-variables")) {
/* 211 */       Boolean enableMatrixVariables = Boolean.valueOf(element.getAttribute("enable-matrix-variables"));
/* 212 */       handlerMappingDef.getPropertyValues().add("removeSemicolonContent", Boolean.valueOf(!enableMatrixVariables.booleanValue()));
/*     */     } 
/*     */     
/* 215 */     configurePathMatchingProperties(handlerMappingDef, element, context);
/* 216 */     readerContext.getRegistry().registerBeanDefinition(HANDLER_MAPPING_BEAN_NAME, (BeanDefinition)handlerMappingDef);
/*     */     
/* 218 */     RuntimeBeanReference corsRef = MvcNamespaceUtils.registerCorsConfigurations(null, context, source);
/* 219 */     handlerMappingDef.getPropertyValues().add("corsConfigurations", corsRef);
/*     */     
/* 221 */     RuntimeBeanReference conversionService = getConversionService(element, source, context);
/* 222 */     RuntimeBeanReference validator = getValidator(element, source, context);
/* 223 */     RuntimeBeanReference messageCodesResolver = getMessageCodesResolver(element);
/*     */     
/* 225 */     RootBeanDefinition bindingDef = new RootBeanDefinition(ConfigurableWebBindingInitializer.class);
/* 226 */     bindingDef.setSource(source);
/* 227 */     bindingDef.setRole(2);
/* 228 */     bindingDef.getPropertyValues().add("conversionService", conversionService);
/* 229 */     bindingDef.getPropertyValues().add("validator", validator);
/* 230 */     bindingDef.getPropertyValues().add("messageCodesResolver", messageCodesResolver);
/*     */     
/* 232 */     ManagedList<?> messageConverters = getMessageConverters(element, source, context);
/* 233 */     ManagedList<?> argumentResolvers = getArgumentResolvers(element, context);
/* 234 */     ManagedList<?> returnValueHandlers = getReturnValueHandlers(element, context);
/* 235 */     String asyncTimeout = getAsyncTimeout(element);
/* 236 */     RuntimeBeanReference asyncExecutor = getAsyncExecutor(element);
/* 237 */     ManagedList<?> callableInterceptors = getCallableInterceptors(element, source, context);
/* 238 */     ManagedList<?> deferredResultInterceptors = getDeferredResultInterceptors(element, source, context);
/*     */     
/* 240 */     RootBeanDefinition handlerAdapterDef = new RootBeanDefinition(RequestMappingHandlerAdapter.class);
/* 241 */     handlerAdapterDef.setSource(source);
/* 242 */     handlerAdapterDef.setRole(2);
/* 243 */     handlerAdapterDef.getPropertyValues().add("contentNegotiationManager", contentNegotiationManager);
/* 244 */     handlerAdapterDef.getPropertyValues().add("webBindingInitializer", bindingDef);
/* 245 */     handlerAdapterDef.getPropertyValues().add("messageConverters", messageConverters);
/* 246 */     addRequestBodyAdvice(handlerAdapterDef);
/* 247 */     addResponseBodyAdvice(handlerAdapterDef);
/*     */     
/* 249 */     if (element.hasAttribute("ignore-default-model-on-redirect")) {
/* 250 */       Boolean ignoreDefaultModel = Boolean.valueOf(element.getAttribute("ignore-default-model-on-redirect"));
/* 251 */       handlerAdapterDef.getPropertyValues().add("ignoreDefaultModelOnRedirect", ignoreDefaultModel);
/*     */     } 
/* 253 */     if (argumentResolvers != null) {
/* 254 */       handlerAdapterDef.getPropertyValues().add("customArgumentResolvers", argumentResolvers);
/*     */     }
/* 256 */     if (returnValueHandlers != null) {
/* 257 */       handlerAdapterDef.getPropertyValues().add("customReturnValueHandlers", returnValueHandlers);
/*     */     }
/* 259 */     if (asyncTimeout != null) {
/* 260 */       handlerAdapterDef.getPropertyValues().add("asyncRequestTimeout", asyncTimeout);
/*     */     }
/* 262 */     if (asyncExecutor != null) {
/* 263 */       handlerAdapterDef.getPropertyValues().add("taskExecutor", asyncExecutor);
/*     */     }
/*     */     
/* 266 */     handlerAdapterDef.getPropertyValues().add("callableInterceptors", callableInterceptors);
/* 267 */     handlerAdapterDef.getPropertyValues().add("deferredResultInterceptors", deferredResultInterceptors);
/* 268 */     readerContext.getRegistry().registerBeanDefinition(HANDLER_ADAPTER_BEAN_NAME, (BeanDefinition)handlerAdapterDef);
/*     */     
/* 270 */     RootBeanDefinition uriContributorDef = new RootBeanDefinition(CompositeUriComponentsContributorFactoryBean.class);
/*     */     
/* 272 */     uriContributorDef.setSource(source);
/* 273 */     uriContributorDef.getPropertyValues().addPropertyValue("handlerAdapter", handlerAdapterDef);
/* 274 */     uriContributorDef.getPropertyValues().addPropertyValue("conversionService", conversionService);
/* 275 */     String uriContributorName = "mvcUriComponentsContributor";
/* 276 */     readerContext.getRegistry().registerBeanDefinition(uriContributorName, (BeanDefinition)uriContributorDef);
/*     */     
/* 278 */     RootBeanDefinition csInterceptorDef = new RootBeanDefinition(ConversionServiceExposingInterceptor.class);
/* 279 */     csInterceptorDef.setSource(source);
/* 280 */     csInterceptorDef.getConstructorArgumentValues().addIndexedArgumentValue(0, conversionService);
/* 281 */     RootBeanDefinition mappedInterceptorDef = new RootBeanDefinition(MappedInterceptor.class);
/* 282 */     mappedInterceptorDef.setSource(source);
/* 283 */     mappedInterceptorDef.setRole(2);
/* 284 */     mappedInterceptorDef.getConstructorArgumentValues().addIndexedArgumentValue(0, null);
/* 285 */     mappedInterceptorDef.getConstructorArgumentValues().addIndexedArgumentValue(1, csInterceptorDef);
/* 286 */     String mappedInterceptorName = readerContext.registerWithGeneratedName((BeanDefinition)mappedInterceptorDef);
/*     */     
/* 288 */     RootBeanDefinition methodExceptionResolver = new RootBeanDefinition(ExceptionHandlerExceptionResolver.class);
/* 289 */     methodExceptionResolver.setSource(source);
/* 290 */     methodExceptionResolver.setRole(2);
/* 291 */     methodExceptionResolver.getPropertyValues().add("contentNegotiationManager", contentNegotiationManager);
/* 292 */     methodExceptionResolver.getPropertyValues().add("messageConverters", messageConverters);
/* 293 */     methodExceptionResolver.getPropertyValues().add("order", Integer.valueOf(0));
/* 294 */     addResponseBodyAdvice(methodExceptionResolver);
/* 295 */     if (argumentResolvers != null) {
/* 296 */       methodExceptionResolver.getPropertyValues().add("customArgumentResolvers", argumentResolvers);
/*     */     }
/* 298 */     if (returnValueHandlers != null) {
/* 299 */       methodExceptionResolver.getPropertyValues().add("customReturnValueHandlers", returnValueHandlers);
/*     */     }
/* 301 */     String methodExResolverName = readerContext.registerWithGeneratedName((BeanDefinition)methodExceptionResolver);
/*     */     
/* 303 */     RootBeanDefinition statusExceptionResolver = new RootBeanDefinition(ResponseStatusExceptionResolver.class);
/* 304 */     statusExceptionResolver.setSource(source);
/* 305 */     statusExceptionResolver.setRole(2);
/* 306 */     statusExceptionResolver.getPropertyValues().add("order", Integer.valueOf(1));
/* 307 */     String statusExResolverName = readerContext.registerWithGeneratedName((BeanDefinition)statusExceptionResolver);
/*     */     
/* 309 */     RootBeanDefinition defaultExceptionResolver = new RootBeanDefinition(DefaultHandlerExceptionResolver.class);
/* 310 */     defaultExceptionResolver.setSource(source);
/* 311 */     defaultExceptionResolver.setRole(2);
/* 312 */     defaultExceptionResolver.getPropertyValues().add("order", Integer.valueOf(2));
/* 313 */     String defaultExResolverName = readerContext.registerWithGeneratedName((BeanDefinition)defaultExceptionResolver);
/*     */     
/* 315 */     context.registerComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)handlerMappingDef, HANDLER_MAPPING_BEAN_NAME));
/* 316 */     context.registerComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)handlerAdapterDef, HANDLER_ADAPTER_BEAN_NAME));
/* 317 */     context.registerComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)uriContributorDef, uriContributorName));
/* 318 */     context.registerComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)mappedInterceptorDef, mappedInterceptorName));
/* 319 */     context.registerComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)methodExceptionResolver, methodExResolverName));
/* 320 */     context.registerComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)statusExceptionResolver, statusExResolverName));
/* 321 */     context.registerComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)defaultExceptionResolver, defaultExResolverName));
/*     */ 
/*     */     
/* 324 */     MvcNamespaceUtils.registerDefaultComponents(context, source);
/*     */     
/* 326 */     context.popAndRegisterContainingComponent();
/*     */     
/* 328 */     return null;
/*     */   }
/*     */   
/*     */   protected void addRequestBodyAdvice(RootBeanDefinition beanDef) {
/* 332 */     if (jackson2Present) {
/* 333 */       beanDef.getPropertyValues().add("requestBodyAdvice", new RootBeanDefinition(JsonViewRequestBodyAdvice.class));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addResponseBodyAdvice(RootBeanDefinition beanDef) {
/* 339 */     if (jackson2Present) {
/* 340 */       beanDef.getPropertyValues().add("responseBodyAdvice", new RootBeanDefinition(JsonViewResponseBodyAdvice.class));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private RuntimeBeanReference getConversionService(Element element, @Nullable Object source, ParserContext context) {
/*     */     RuntimeBeanReference conversionServiceRef;
/* 347 */     if (element.hasAttribute("conversion-service")) {
/* 348 */       conversionServiceRef = new RuntimeBeanReference(element.getAttribute("conversion-service"));
/*     */     } else {
/*     */       
/* 351 */       RootBeanDefinition conversionDef = new RootBeanDefinition(FormattingConversionServiceFactoryBean.class);
/* 352 */       conversionDef.setSource(source);
/* 353 */       conversionDef.setRole(2);
/* 354 */       String conversionName = context.getReaderContext().registerWithGeneratedName((BeanDefinition)conversionDef);
/* 355 */       context.registerComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)conversionDef, conversionName));
/* 356 */       conversionServiceRef = new RuntimeBeanReference(conversionName);
/*     */     } 
/* 358 */     return conversionServiceRef;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private RuntimeBeanReference getValidator(Element element, @Nullable Object source, ParserContext context) {
/* 363 */     if (element.hasAttribute("validator")) {
/* 364 */       return new RuntimeBeanReference(element.getAttribute("validator"));
/*     */     }
/* 366 */     if (javaxValidationPresent) {
/* 367 */       RootBeanDefinition validatorDef = new RootBeanDefinition("org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean");
/*     */       
/* 369 */       validatorDef.setSource(source);
/* 370 */       validatorDef.setRole(2);
/* 371 */       String validatorName = context.getReaderContext().registerWithGeneratedName((BeanDefinition)validatorDef);
/* 372 */       context.registerComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)validatorDef, validatorName));
/* 373 */       return new RuntimeBeanReference(validatorName);
/*     */     } 
/*     */     
/* 376 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private RuntimeBeanReference getContentNegotiationManager(Element element, @Nullable Object source, ParserContext context) {
/*     */     RuntimeBeanReference beanRef;
/* 384 */     if (element.hasAttribute("content-negotiation-manager")) {
/* 385 */       String name = element.getAttribute("content-negotiation-manager");
/* 386 */       beanRef = new RuntimeBeanReference(name);
/*     */     } else {
/*     */       
/* 389 */       RootBeanDefinition factoryBeanDef = new RootBeanDefinition(ContentNegotiationManagerFactoryBean.class);
/* 390 */       factoryBeanDef.setSource(source);
/* 391 */       factoryBeanDef.setRole(2);
/* 392 */       factoryBeanDef.getPropertyValues().add("mediaTypes", getDefaultMediaTypes());
/* 393 */       String name = "mvcContentNegotiationManager";
/* 394 */       context.getReaderContext().getRegistry().registerBeanDefinition(name, (BeanDefinition)factoryBeanDef);
/* 395 */       context.registerComponent((ComponentDefinition)new BeanComponentDefinition((BeanDefinition)factoryBeanDef, name));
/* 396 */       beanRef = new RuntimeBeanReference(name);
/*     */     } 
/* 398 */     return beanRef;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void configurePathMatchingProperties(RootBeanDefinition handlerMappingDef, Element element, ParserContext context) {
/* 404 */     Element pathMatchingElement = DomUtils.getChildElementByTagName(element, "path-matching");
/* 405 */     if (pathMatchingElement != null) {
/* 406 */       Object source = context.extractSource(element);
/*     */       
/* 408 */       if (pathMatchingElement.hasAttribute("suffix-pattern")) {
/* 409 */         Boolean useSuffixPatternMatch = Boolean.valueOf(pathMatchingElement.getAttribute("suffix-pattern"));
/* 410 */         handlerMappingDef.getPropertyValues().add("useSuffixPatternMatch", useSuffixPatternMatch);
/*     */       } 
/* 412 */       if (pathMatchingElement.hasAttribute("trailing-slash")) {
/* 413 */         Boolean useTrailingSlashMatch = Boolean.valueOf(pathMatchingElement.getAttribute("trailing-slash"));
/* 414 */         handlerMappingDef.getPropertyValues().add("useTrailingSlashMatch", useTrailingSlashMatch);
/*     */       } 
/* 416 */       if (pathMatchingElement.hasAttribute("registered-suffixes-only")) {
/* 417 */         Boolean useRegisteredSuffixPatternMatch = Boolean.valueOf(pathMatchingElement.getAttribute("registered-suffixes-only"));
/* 418 */         handlerMappingDef.getPropertyValues().add("useRegisteredSuffixPatternMatch", useRegisteredSuffixPatternMatch);
/*     */       } 
/*     */       
/* 421 */       RuntimeBeanReference pathHelperRef = null;
/* 422 */       if (pathMatchingElement.hasAttribute("path-helper")) {
/* 423 */         pathHelperRef = new RuntimeBeanReference(pathMatchingElement.getAttribute("path-helper"));
/*     */       }
/* 425 */       pathHelperRef = MvcNamespaceUtils.registerUrlPathHelper(pathHelperRef, context, source);
/* 426 */       handlerMappingDef.getPropertyValues().add("urlPathHelper", pathHelperRef);
/*     */       
/* 428 */       RuntimeBeanReference pathMatcherRef = null;
/* 429 */       if (pathMatchingElement.hasAttribute("path-matcher")) {
/* 430 */         pathMatcherRef = new RuntimeBeanReference(pathMatchingElement.getAttribute("path-matcher"));
/*     */       }
/* 432 */       pathMatcherRef = MvcNamespaceUtils.registerPathMatcher(pathMatcherRef, context, source);
/* 433 */       handlerMappingDef.getPropertyValues().add("pathMatcher", pathMatcherRef);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Properties getDefaultMediaTypes() {
/* 438 */     Properties defaultMediaTypes = new Properties();
/* 439 */     if (romePresent) {
/* 440 */       defaultMediaTypes.put("atom", "application/atom+xml");
/* 441 */       defaultMediaTypes.put("rss", "application/rss+xml");
/*     */     } 
/* 443 */     if (jaxb2Present || jackson2XmlPresent) {
/* 444 */       defaultMediaTypes.put("xml", "application/xml");
/*     */     }
/* 446 */     if (jackson2Present || gsonPresent) {
/* 447 */       defaultMediaTypes.put("json", "application/json");
/*     */     }
/* 449 */     if (jackson2SmilePresent) {
/* 450 */       defaultMediaTypes.put("smile", "application/x-jackson-smile");
/*     */     }
/* 452 */     if (jackson2CborPresent) {
/* 453 */       defaultMediaTypes.put("cbor", "application/cbor");
/*     */     }
/* 455 */     return defaultMediaTypes;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private RuntimeBeanReference getMessageCodesResolver(Element element) {
/* 460 */     if (element.hasAttribute("message-codes-resolver")) {
/* 461 */       return new RuntimeBeanReference(element.getAttribute("message-codes-resolver"));
/*     */     }
/*     */     
/* 464 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String getAsyncTimeout(Element element) {
/* 470 */     Element asyncElement = DomUtils.getChildElementByTagName(element, "async-support");
/* 471 */     return (asyncElement != null) ? asyncElement.getAttribute("default-timeout") : null;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private RuntimeBeanReference getAsyncExecutor(Element element) {
/* 476 */     Element asyncElement = DomUtils.getChildElementByTagName(element, "async-support");
/* 477 */     if (asyncElement != null && asyncElement.hasAttribute("task-executor")) {
/* 478 */       return new RuntimeBeanReference(asyncElement.getAttribute("task-executor"));
/*     */     }
/* 480 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ManagedList<?> getCallableInterceptors(Element element, @Nullable Object source, ParserContext context) {
/* 486 */     ManagedList<Object> interceptors = new ManagedList();
/* 487 */     Element asyncElement = DomUtils.getChildElementByTagName(element, "async-support");
/* 488 */     if (asyncElement != null) {
/* 489 */       Element interceptorsElement = DomUtils.getChildElementByTagName(asyncElement, "callable-interceptors");
/* 490 */       if (interceptorsElement != null) {
/* 491 */         interceptors.setSource(source);
/* 492 */         for (Element converter : DomUtils.getChildElementsByTagName(interceptorsElement, "bean")) {
/* 493 */           BeanDefinitionHolder beanDef = context.getDelegate().parseBeanDefinitionElement(converter);
/* 494 */           if (beanDef != null) {
/* 495 */             beanDef = context.getDelegate().decorateBeanDefinitionIfRequired(converter, beanDef);
/* 496 */             interceptors.add(beanDef);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 501 */     return interceptors;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ManagedList<?> getDeferredResultInterceptors(Element element, @Nullable Object source, ParserContext context) {
/* 507 */     ManagedList<Object> interceptors = new ManagedList();
/* 508 */     Element asyncElement = DomUtils.getChildElementByTagName(element, "async-support");
/* 509 */     if (asyncElement != null) {
/* 510 */       Element interceptorsElement = DomUtils.getChildElementByTagName(asyncElement, "deferred-result-interceptors");
/* 511 */       if (interceptorsElement != null) {
/* 512 */         interceptors.setSource(source);
/* 513 */         for (Element converter : DomUtils.getChildElementsByTagName(interceptorsElement, "bean")) {
/* 514 */           BeanDefinitionHolder beanDef = context.getDelegate().parseBeanDefinitionElement(converter);
/* 515 */           if (beanDef != null) {
/* 516 */             beanDef = context.getDelegate().decorateBeanDefinitionIfRequired(converter, beanDef);
/* 517 */             interceptors.add(beanDef);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 522 */     return interceptors;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private ManagedList<?> getArgumentResolvers(Element element, ParserContext context) {
/* 527 */     Element resolversElement = DomUtils.getChildElementByTagName(element, "argument-resolvers");
/* 528 */     if (resolversElement != null) {
/* 529 */       ManagedList<Object> resolvers = extractBeanSubElements(resolversElement, context);
/* 530 */       return wrapLegacyResolvers((List<Object>)resolvers, context);
/*     */     } 
/* 532 */     return null;
/*     */   }
/*     */   
/*     */   private ManagedList<Object> wrapLegacyResolvers(List<Object> list, ParserContext context) {
/* 536 */     ManagedList<Object> result = new ManagedList();
/* 537 */     for (Object object : list) {
/* 538 */       if (object instanceof BeanDefinitionHolder) {
/* 539 */         BeanDefinitionHolder beanDef = (BeanDefinitionHolder)object;
/* 540 */         String className = beanDef.getBeanDefinition().getBeanClassName();
/* 541 */         Assert.notNull(className, "No resolver class");
/* 542 */         Class<?> clazz = ClassUtils.resolveClassName(className, context.getReaderContext().getBeanClassLoader());
/* 543 */         if (WebArgumentResolver.class.isAssignableFrom(clazz)) {
/* 544 */           RootBeanDefinition adapter = new RootBeanDefinition(ServletWebArgumentResolverAdapter.class);
/* 545 */           adapter.getConstructorArgumentValues().addIndexedArgumentValue(0, beanDef);
/* 546 */           result.add(new BeanDefinitionHolder((BeanDefinition)adapter, beanDef.getBeanName() + "Adapter"));
/*     */           continue;
/*     */         } 
/*     */       } 
/* 550 */       result.add(object);
/*     */     } 
/* 552 */     return result;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private ManagedList<?> getReturnValueHandlers(Element element, ParserContext context) {
/* 557 */     Element handlers = DomUtils.getChildElementByTagName(element, "return-value-handlers");
/* 558 */     return (handlers != null) ? extractBeanSubElements(handlers, context) : null;
/*     */   }
/*     */   
/*     */   private ManagedList<?> getMessageConverters(Element element, @Nullable Object source, ParserContext context) {
/* 562 */     Element convertersElement = DomUtils.getChildElementByTagName(element, "message-converters");
/* 563 */     ManagedList<Object> messageConverters = new ManagedList();
/* 564 */     if (convertersElement != null) {
/* 565 */       messageConverters.setSource(source);
/* 566 */       for (Element beanElement : DomUtils.getChildElementsByTagName(convertersElement, new String[] { "bean", "ref" })) {
/* 567 */         Object object = context.getDelegate().parsePropertySubElement(beanElement, null);
/* 568 */         messageConverters.add(object);
/*     */       } 
/*     */     } 
/*     */     
/* 572 */     if (convertersElement == null || Boolean.valueOf(convertersElement.getAttribute("register-defaults")).booleanValue()) {
/* 573 */       messageConverters.setSource(source);
/* 574 */       messageConverters.add(createConverterDefinition(ByteArrayHttpMessageConverter.class, source));
/*     */       
/* 576 */       RootBeanDefinition stringConverterDef = createConverterDefinition(StringHttpMessageConverter.class, source);
/* 577 */       stringConverterDef.getPropertyValues().add("writeAcceptCharset", Boolean.valueOf(false));
/* 578 */       messageConverters.add(stringConverterDef);
/*     */       
/* 580 */       messageConverters.add(createConverterDefinition(ResourceHttpMessageConverter.class, source));
/* 581 */       messageConverters.add(createConverterDefinition(ResourceRegionHttpMessageConverter.class, source));
/* 582 */       messageConverters.add(createConverterDefinition(SourceHttpMessageConverter.class, source));
/* 583 */       messageConverters.add(createConverterDefinition(AllEncompassingFormHttpMessageConverter.class, source));
/*     */       
/* 585 */       if (romePresent) {
/* 586 */         messageConverters.add(createConverterDefinition(AtomFeedHttpMessageConverter.class, source));
/* 587 */         messageConverters.add(createConverterDefinition(RssChannelHttpMessageConverter.class, source));
/*     */       } 
/*     */       
/* 590 */       if (jackson2XmlPresent) {
/* 591 */         Class<?> type = MappingJackson2XmlHttpMessageConverter.class;
/* 592 */         RootBeanDefinition jacksonConverterDef = createConverterDefinition(type, source);
/* 593 */         GenericBeanDefinition jacksonFactoryDef = createObjectMapperFactoryDefinition(source);
/* 594 */         jacksonFactoryDef.getPropertyValues().add("createXmlMapper", Boolean.valueOf(true));
/* 595 */         jacksonConverterDef.getConstructorArgumentValues().addIndexedArgumentValue(0, jacksonFactoryDef);
/* 596 */         messageConverters.add(jacksonConverterDef);
/*     */       }
/* 598 */       else if (jaxb2Present) {
/* 599 */         messageConverters.add(createConverterDefinition(Jaxb2RootElementHttpMessageConverter.class, source));
/*     */       } 
/*     */       
/* 602 */       if (jackson2Present) {
/* 603 */         Class<?> type = MappingJackson2HttpMessageConverter.class;
/* 604 */         RootBeanDefinition jacksonConverterDef = createConverterDefinition(type, source);
/* 605 */         GenericBeanDefinition jacksonFactoryDef = createObjectMapperFactoryDefinition(source);
/* 606 */         jacksonConverterDef.getConstructorArgumentValues().addIndexedArgumentValue(0, jacksonFactoryDef);
/* 607 */         messageConverters.add(jacksonConverterDef);
/*     */       }
/* 609 */       else if (gsonPresent) {
/* 610 */         messageConverters.add(createConverterDefinition(GsonHttpMessageConverter.class, source));
/*     */       } 
/*     */       
/* 613 */       if (jackson2SmilePresent) {
/* 614 */         Class<?> type = MappingJackson2SmileHttpMessageConverter.class;
/* 615 */         RootBeanDefinition jacksonConverterDef = createConverterDefinition(type, source);
/* 616 */         GenericBeanDefinition jacksonFactoryDef = createObjectMapperFactoryDefinition(source);
/* 617 */         jacksonFactoryDef.getPropertyValues().add("factory", new SmileFactory());
/* 618 */         jacksonConverterDef.getConstructorArgumentValues().addIndexedArgumentValue(0, jacksonFactoryDef);
/* 619 */         messageConverters.add(jacksonConverterDef);
/*     */       } 
/* 621 */       if (jackson2CborPresent) {
/* 622 */         Class<?> type = MappingJackson2CborHttpMessageConverter.class;
/* 623 */         RootBeanDefinition jacksonConverterDef = createConverterDefinition(type, source);
/* 624 */         GenericBeanDefinition jacksonFactoryDef = createObjectMapperFactoryDefinition(source);
/* 625 */         jacksonFactoryDef.getPropertyValues().add("factory", new CBORFactory());
/* 626 */         jacksonConverterDef.getConstructorArgumentValues().addIndexedArgumentValue(0, jacksonFactoryDef);
/* 627 */         messageConverters.add(jacksonConverterDef);
/*     */       } 
/*     */     } 
/* 630 */     return messageConverters;
/*     */   }
/*     */   
/*     */   private GenericBeanDefinition createObjectMapperFactoryDefinition(@Nullable Object source) {
/* 634 */     GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
/* 635 */     beanDefinition.setBeanClass(Jackson2ObjectMapperFactoryBean.class);
/* 636 */     beanDefinition.setSource(source);
/* 637 */     beanDefinition.setRole(2);
/* 638 */     return beanDefinition;
/*     */   }
/*     */   
/*     */   private RootBeanDefinition createConverterDefinition(Class<?> converterClass, @Nullable Object source) {
/* 642 */     RootBeanDefinition beanDefinition = new RootBeanDefinition(converterClass);
/* 643 */     beanDefinition.setSource(source);
/* 644 */     beanDefinition.setRole(2);
/* 645 */     return beanDefinition;
/*     */   }
/*     */   
/*     */   private ManagedList<Object> extractBeanSubElements(Element parentElement, ParserContext context) {
/* 649 */     ManagedList<Object> list = new ManagedList();
/* 650 */     list.setSource(context.extractSource(parentElement));
/* 651 */     for (Element beanElement : DomUtils.getChildElementsByTagName(parentElement, new String[] { "bean", "ref" })) {
/* 652 */       Object object = context.getDelegate().parsePropertySubElement(beanElement, null);
/* 653 */       list.add(object);
/*     */     } 
/* 655 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class CompositeUriComponentsContributorFactoryBean
/*     */     implements FactoryBean<CompositeUriComponentsContributor>, InitializingBean
/*     */   {
/*     */     @Nullable
/*     */     private RequestMappingHandlerAdapter handlerAdapter;
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private ConversionService conversionService;
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private CompositeUriComponentsContributor uriComponentsContributor;
/*     */ 
/*     */ 
/*     */     
/*     */     public void setHandlerAdapter(RequestMappingHandlerAdapter handlerAdapter) {
/* 677 */       this.handlerAdapter = handlerAdapter;
/*     */     }
/*     */     
/*     */     public void setConversionService(ConversionService conversionService) {
/* 681 */       this.conversionService = conversionService;
/*     */     }
/*     */ 
/*     */     
/*     */     public void afterPropertiesSet() {
/* 686 */       Assert.state((this.handlerAdapter != null), "No RequestMappingHandlerAdapter set");
/* 687 */       this
/* 688 */         .uriComponentsContributor = new CompositeUriComponentsContributor(this.handlerAdapter.getArgumentResolvers(), this.conversionService);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public CompositeUriComponentsContributor getObject() {
/* 694 */       return this.uriComponentsContributor;
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?> getObjectType() {
/* 699 */       return CompositeUriComponentsContributor.class;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isSingleton() {
/* 704 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/config/AnnotationDrivenBeanDefinitionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */