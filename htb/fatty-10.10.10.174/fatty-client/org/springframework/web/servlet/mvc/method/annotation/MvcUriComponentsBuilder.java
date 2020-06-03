/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.framework.ProxyFactory;
/*     */ import org.springframework.aop.target.EmptyTargetSource;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.cglib.core.NamingPolicy;
/*     */ import org.springframework.cglib.core.SpringNamingPolicy;
/*     */ import org.springframework.cglib.proxy.Callback;
/*     */ import org.springframework.cglib.proxy.Enhancer;
/*     */ import org.springframework.cglib.proxy.Factory;
/*     */ import org.springframework.cglib.proxy.MethodInterceptor;
/*     */ import org.springframework.cglib.proxy.MethodProxy;
/*     */ import org.springframework.core.DefaultParameterNameDiscoverer;
/*     */ import org.springframework.core.MethodIntrospector;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.core.annotation.SynthesizingMethodParameter;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.objenesis.ObjenesisException;
/*     */ import org.springframework.objenesis.SpringObjenesis;
/*     */ import org.springframework.util.AntPathMatcher;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.PathMatcher;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.context.WebApplicationContext;
/*     */ import org.springframework.web.context.request.RequestAttributes;
/*     */ import org.springframework.web.context.request.RequestContextHolder;
/*     */ import org.springframework.web.context.request.ServletRequestAttributes;
/*     */ import org.springframework.web.method.HandlerMethod;
/*     */ import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
/*     */ import org.springframework.web.method.support.CompositeUriComponentsContributor;
/*     */ import org.springframework.web.method.support.UriComponentsContributor;
/*     */ import org.springframework.web.servlet.DispatcherServlet;
/*     */ import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
/*     */ import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
/*     */ import org.springframework.web.util.UriComponentsBuilder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MvcUriComponentsBuilder
/*     */ {
/*     */   public static final String MVC_URI_COMPONENTS_CONTRIBUTOR_BEAN_NAME = "mvcUriComponentsContributor";
/* 105 */   private static final Log logger = LogFactory.getLog(MvcUriComponentsBuilder.class);
/*     */   
/* 107 */   private static final SpringObjenesis objenesis = new SpringObjenesis();
/*     */   
/* 109 */   private static final PathMatcher pathMatcher = (PathMatcher)new AntPathMatcher();
/*     */   
/* 111 */   private static final ParameterNameDiscoverer parameterNameDiscoverer = (ParameterNameDiscoverer)new DefaultParameterNameDiscoverer();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 116 */   private static final CompositeUriComponentsContributor defaultUriComponentsContributor = new CompositeUriComponentsContributor(new UriComponentsContributor[] { new PathVariableMethodArgumentResolver(), (UriComponentsContributor)new RequestParamMethodArgumentResolver(false) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final UriComponentsBuilder baseUrl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MvcUriComponentsBuilder(UriComponentsBuilder baseUrl) {
/* 132 */     Assert.notNull(baseUrl, "'baseUrl' is required");
/* 133 */     this.baseUrl = baseUrl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MvcUriComponentsBuilder relativeTo(UriComponentsBuilder baseUrl) {
/* 143 */     return new MvcUriComponentsBuilder(baseUrl);
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
/*     */   public static UriComponentsBuilder fromController(Class<?> controllerType) {
/* 157 */     return fromController(null, controllerType);
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
/*     */   public static UriComponentsBuilder fromController(@Nullable UriComponentsBuilder builder, Class<?> controllerType) {
/* 175 */     builder = getBaseUrlToUse(builder);
/*     */ 
/*     */     
/* 178 */     String prefix = getPathPrefix(controllerType);
/* 179 */     builder.path(prefix);
/*     */     
/* 181 */     String mapping = getClassMapping(controllerType);
/* 182 */     builder.path(mapping);
/*     */     
/* 184 */     return builder;
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
/*     */   
/*     */   public static UriComponentsBuilder fromMethodName(Class<?> controllerType, String methodName, Object... args) {
/* 203 */     Method method = getMethod(controllerType, methodName, args);
/* 204 */     return fromMethodInternal(null, controllerType, method, args);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UriComponentsBuilder fromMethodName(UriComponentsBuilder builder, Class<?> controllerType, String methodName, Object... args) {
/* 226 */     Method method = getMethod(controllerType, methodName, args);
/* 227 */     return fromMethodInternal(builder, controllerType, method, args);
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
/*     */ 
/*     */   
/*     */   public static UriComponentsBuilder fromMethod(Class<?> controllerType, Method method, Object... args) {
/* 247 */     return fromMethodInternal(null, controllerType, method, args);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UriComponentsBuilder fromMethod(UriComponentsBuilder baseUrl, @Nullable Class<?> controllerType, Method method, Object... args) {
/* 269 */     return fromMethodInternal(baseUrl, (controllerType != null) ? controllerType : method
/* 270 */         .getDeclaringClass(), method, args);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UriComponentsBuilder fromMethodCall(Object info) {
/* 313 */     Assert.isInstanceOf(MethodInvocationInfo.class, info, "MethodInvocationInfo required");
/* 314 */     MethodInvocationInfo invocationInfo = (MethodInvocationInfo)info;
/* 315 */     Class<?> controllerType = invocationInfo.getControllerType();
/* 316 */     Method method = invocationInfo.getControllerMethod();
/* 317 */     Object[] arguments = invocationInfo.getArgumentValues();
/* 318 */     return fromMethodInternal(null, controllerType, method, arguments);
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
/*     */   public static UriComponentsBuilder fromMethodCall(UriComponentsBuilder builder, Object info) {
/* 335 */     Assert.isInstanceOf(MethodInvocationInfo.class, info, "MethodInvocationInfo required");
/* 336 */     MethodInvocationInfo invocationInfo = (MethodInvocationInfo)info;
/* 337 */     Class<?> controllerType = invocationInfo.getControllerType();
/* 338 */     Method method = invocationInfo.getControllerMethod();
/* 339 */     Object[] arguments = invocationInfo.getArgumentValues();
/* 340 */     return fromMethodInternal(builder, controllerType, method, arguments);
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
/*     */   
/*     */   public static <T> T on(Class<T> controllerType) {
/* 359 */     return controller(controllerType);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T controller(Class<T> controllerType) {
/* 383 */     Assert.notNull(controllerType, "'controllerType' must not be null");
/* 384 */     return ControllerMethodInvocationInterceptor.initProxy(controllerType, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MethodArgumentBuilder fromMappingName(String mappingName) {
/* 433 */     return fromMappingName(null, mappingName);
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
/*     */   
/*     */   public static MethodArgumentBuilder fromMappingName(@Nullable UriComponentsBuilder builder, String name) {
/* 452 */     WebApplicationContext wac = getWebApplicationContext();
/* 453 */     Assert.notNull(wac, "No WebApplicationContext. ");
/* 454 */     Map<String, RequestMappingInfoHandlerMapping> map = wac.getBeansOfType(RequestMappingInfoHandlerMapping.class);
/* 455 */     List<HandlerMethod> handlerMethods = null;
/* 456 */     for (RequestMappingInfoHandlerMapping mapping : map.values()) {
/* 457 */       handlerMethods = mapping.getHandlerMethodsForMappingName(name);
/* 458 */       if (handlerMethods != null) {
/*     */         break;
/*     */       }
/*     */     } 
/* 462 */     if (handlerMethods == null) {
/* 463 */       throw new IllegalArgumentException("Mapping not found: " + name);
/*     */     }
/* 465 */     if (handlerMethods.size() != 1) {
/* 466 */       throw new IllegalArgumentException("No unique match for mapping " + name + ": " + handlerMethods);
/*     */     }
/*     */     
/* 469 */     HandlerMethod handlerMethod = handlerMethods.get(0);
/* 470 */     Class<?> controllerType = handlerMethod.getBeanType();
/* 471 */     Method method = handlerMethod.getMethod();
/* 472 */     return new MethodArgumentBuilder(builder, controllerType, method);
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
/*     */   public UriComponentsBuilder withController(Class<?> controllerType) {
/* 487 */     return fromController(this.baseUrl, controllerType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UriComponentsBuilder withMethodName(Class<?> controllerType, String methodName, Object... args) {
/* 498 */     return fromMethodName(this.baseUrl, controllerType, methodName, args);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UriComponentsBuilder withMethodCall(Object invocationInfo) {
/* 509 */     return fromMethodCall(this.baseUrl, invocationInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodArgumentBuilder withMappingName(String mappingName) {
/* 520 */     return fromMappingName(this.baseUrl, mappingName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UriComponentsBuilder withMethod(Class<?> controllerType, Method method, Object... args) {
/* 531 */     return fromMethod(this.baseUrl, controllerType, method, args);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static UriComponentsBuilder fromMethodInternal(@Nullable UriComponentsBuilder builder, Class<?> controllerType, Method method, Object... args) {
/* 538 */     builder = getBaseUrlToUse(builder);
/*     */ 
/*     */     
/* 541 */     String prefix = getPathPrefix(controllerType);
/* 542 */     builder.path(prefix);
/*     */     
/* 544 */     String typePath = getClassMapping(controllerType);
/* 545 */     String methodPath = getMethodMapping(method);
/* 546 */     String path = pathMatcher.combine(typePath, methodPath);
/* 547 */     builder.path(path);
/*     */     
/* 549 */     return applyContributors(builder, method, args);
/*     */   }
/*     */   
/*     */   private static UriComponentsBuilder getBaseUrlToUse(@Nullable UriComponentsBuilder baseUrl) {
/* 553 */     return (baseUrl == null) ? 
/* 554 */       (UriComponentsBuilder)ServletUriComponentsBuilder.fromCurrentServletMapping() : baseUrl
/* 555 */       .cloneBuilder();
/*     */   }
/*     */   
/*     */   private static String getPathPrefix(Class<?> controllerType) {
/* 559 */     WebApplicationContext wac = getWebApplicationContext();
/* 560 */     if (wac != null) {
/* 561 */       Map<String, RequestMappingHandlerMapping> map = wac.getBeansOfType(RequestMappingHandlerMapping.class);
/* 562 */       for (RequestMappingHandlerMapping mapping : map.values()) {
/* 563 */         if (mapping.isHandler(controllerType)) {
/* 564 */           String prefix = mapping.getPathPrefix(controllerType);
/* 565 */           if (prefix != null) {
/* 566 */             return prefix;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 571 */     return "";
/*     */   }
/*     */   
/*     */   private static String getClassMapping(Class<?> controllerType) {
/* 575 */     Assert.notNull(controllerType, "'controllerType' must not be null");
/* 576 */     RequestMapping mapping = (RequestMapping)AnnotatedElementUtils.findMergedAnnotation(controllerType, RequestMapping.class);
/* 577 */     if (mapping == null) {
/* 578 */       return "/";
/*     */     }
/* 580 */     String[] paths = mapping.path();
/* 581 */     if (ObjectUtils.isEmpty((Object[])paths) || !StringUtils.hasLength(paths[0])) {
/* 582 */       return "/";
/*     */     }
/* 584 */     if (paths.length > 1 && logger.isTraceEnabled()) {
/* 585 */       logger.trace("Using first of multiple paths on " + controllerType.getName());
/*     */     }
/* 587 */     return paths[0];
/*     */   }
/*     */   
/*     */   private static String getMethodMapping(Method method) {
/* 591 */     Assert.notNull(method, "'method' must not be null");
/* 592 */     RequestMapping requestMapping = (RequestMapping)AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
/* 593 */     if (requestMapping == null) {
/* 594 */       throw new IllegalArgumentException("No @RequestMapping on: " + method.toGenericString());
/*     */     }
/* 596 */     String[] paths = requestMapping.path();
/* 597 */     if (ObjectUtils.isEmpty((Object[])paths) || !StringUtils.hasLength(paths[0])) {
/* 598 */       return "/";
/*     */     }
/* 600 */     if (paths.length > 1 && logger.isTraceEnabled()) {
/* 601 */       logger.trace("Using first of multiple paths on " + method.toGenericString());
/*     */     }
/* 603 */     return paths[0];
/*     */   }
/*     */   
/*     */   private static Method getMethod(Class<?> controllerType, String methodName, Object... args) {
/* 607 */     ReflectionUtils.MethodFilter selector = method -> {
/*     */         String name = method.getName();
/*     */         int argLength = method.getParameterCount();
/* 610 */         return (name.equals(methodName) && argLength == args.length);
/*     */       };
/* 612 */     Set<Method> methods = MethodIntrospector.selectMethods(controllerType, selector);
/* 613 */     if (methods.size() == 1) {
/* 614 */       return methods.iterator().next();
/*     */     }
/* 616 */     if (methods.size() > 1) {
/* 617 */       throw new IllegalArgumentException(String.format("Found two methods named '%s' accepting arguments %s in controller %s: [%s]", new Object[] { methodName, 
/*     */               
/* 619 */               Arrays.asList(args), controllerType.getName(), methods }));
/*     */     }
/*     */     
/* 622 */     throw new IllegalArgumentException("No method named '" + methodName + "' with " + args.length + " arguments found in controller " + controllerType
/* 623 */         .getName());
/*     */   }
/*     */ 
/*     */   
/*     */   private static UriComponentsBuilder applyContributors(UriComponentsBuilder builder, Method method, Object... args) {
/* 628 */     CompositeUriComponentsContributor contributor = getUriComponentsContributor();
/*     */     
/* 630 */     int paramCount = method.getParameterCount();
/* 631 */     int argCount = args.length;
/* 632 */     if (paramCount != argCount) {
/* 633 */       throw new IllegalArgumentException("Number of method parameters " + paramCount + " does not match number of argument values " + argCount);
/*     */     }
/*     */ 
/*     */     
/* 637 */     Map<String, Object> uriVars = new HashMap<>();
/* 638 */     for (int i = 0; i < paramCount; i++) {
/* 639 */       SynthesizingMethodParameter synthesizingMethodParameter = new SynthesizingMethodParameter(method, i);
/* 640 */       synthesizingMethodParameter.initParameterNameDiscovery(parameterNameDiscoverer);
/* 641 */       contributor.contributeMethodArgument((MethodParameter)synthesizingMethodParameter, args[i], builder, uriVars);
/*     */     } 
/*     */ 
/*     */     
/* 645 */     return builder.uriVariables(uriVars);
/*     */   }
/*     */   
/*     */   private static CompositeUriComponentsContributor getUriComponentsContributor() {
/* 649 */     WebApplicationContext wac = getWebApplicationContext();
/* 650 */     if (wac != null) {
/*     */       try {
/* 652 */         return (CompositeUriComponentsContributor)wac.getBean("mvcUriComponentsContributor", CompositeUriComponentsContributor.class);
/*     */       }
/* 654 */       catch (NoSuchBeanDefinitionException noSuchBeanDefinitionException) {}
/*     */     }
/*     */ 
/*     */     
/* 658 */     return defaultUriComponentsContributor;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static WebApplicationContext getWebApplicationContext() {
/* 663 */     RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
/* 664 */     if (requestAttributes == null) {
/* 665 */       return null;
/*     */     }
/* 667 */     HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
/* 668 */     String attributeName = DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE;
/* 669 */     WebApplicationContext wac = (WebApplicationContext)request.getAttribute(attributeName);
/* 670 */     if (wac == null) {
/* 671 */       return null;
/*     */     }
/* 673 */     return wac;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface MethodInvocationInfo
/*     */   {
/*     */     Class<?> getControllerType();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Method getControllerMethod();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Object[] getArgumentValues();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ControllerMethodInvocationInterceptor
/*     */     implements MethodInterceptor, MethodInterceptor, MethodInvocationInfo
/*     */   {
/*     */     private final Class<?> controllerType;
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private Method controllerMethod;
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private Object[] argumentValues;
/*     */ 
/*     */ 
/*     */     
/*     */     ControllerMethodInvocationInterceptor(Class<?> controllerType) {
/* 712 */       this.controllerType = controllerType;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Object intercept(Object obj, Method method, Object[] args, @Nullable MethodProxy proxy) {
/* 718 */       if (method.getName().equals("getControllerType")) {
/* 719 */         return this.controllerType;
/*     */       }
/* 721 */       if (method.getName().equals("getControllerMethod")) {
/* 722 */         return this.controllerMethod;
/*     */       }
/* 724 */       if (method.getName().equals("getArgumentValues")) {
/* 725 */         return this.argumentValues;
/*     */       }
/* 727 */       if (ReflectionUtils.isObjectMethod(method)) {
/* 728 */         return ReflectionUtils.invokeMethod(method, obj, args);
/*     */       }
/*     */       
/* 731 */       this.controllerMethod = method;
/* 732 */       this.argumentValues = args;
/* 733 */       Class<?> returnType = method.getReturnType();
/*     */       try {
/* 735 */         return (returnType == void.class) ? null : returnType.cast(initProxy(returnType, this));
/*     */       }
/* 737 */       catch (Throwable ex) {
/* 738 */         throw new IllegalStateException("Failed to create proxy for controller method return type: " + method, ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Object invoke(MethodInvocation inv) throws Throwable {
/* 747 */       return intercept(inv.getThis(), inv.getMethod(), inv.getArguments(), null);
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?> getControllerType() {
/* 752 */       return this.controllerType;
/*     */     }
/*     */ 
/*     */     
/*     */     public Method getControllerMethod() {
/* 757 */       Assert.state((this.controllerMethod != null), "Not initialized yet");
/* 758 */       return this.controllerMethod;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object[] getArgumentValues() {
/* 763 */       Assert.state((this.argumentValues != null), "Not initialized yet");
/* 764 */       return this.argumentValues;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static <T> T initProxy(Class<?> controllerType, @Nullable ControllerMethodInvocationInterceptor interceptor) {
/* 772 */       interceptor = (interceptor != null) ? interceptor : new ControllerMethodInvocationInterceptor(controllerType);
/*     */ 
/*     */       
/* 775 */       if (controllerType == Object.class) {
/* 776 */         return (T)interceptor;
/*     */       }
/*     */       
/* 779 */       if (controllerType.isInterface()) {
/* 780 */         ProxyFactory factory = new ProxyFactory(EmptyTargetSource.INSTANCE);
/* 781 */         factory.addInterface(controllerType);
/* 782 */         factory.addInterface(MvcUriComponentsBuilder.MethodInvocationInfo.class);
/* 783 */         factory.addAdvice((Advice)interceptor);
/* 784 */         return (T)factory.getProxy();
/*     */       } 
/*     */ 
/*     */       
/* 788 */       Enhancer enhancer = new Enhancer();
/* 789 */       enhancer.setSuperclass(controllerType);
/* 790 */       enhancer.setInterfaces(new Class[] { MvcUriComponentsBuilder.MethodInvocationInfo.class });
/* 791 */       enhancer.setNamingPolicy((NamingPolicy)SpringNamingPolicy.INSTANCE);
/* 792 */       enhancer.setCallbackType(MethodInterceptor.class);
/*     */       
/* 794 */       Class<?> proxyClass = enhancer.createClass();
/* 795 */       Object proxy = null;
/*     */       
/* 797 */       if (MvcUriComponentsBuilder.objenesis.isWorthTrying()) {
/*     */         try {
/* 799 */           proxy = MvcUriComponentsBuilder.objenesis.newInstance(proxyClass, enhancer.getUseCache());
/*     */         }
/* 801 */         catch (ObjenesisException ex) {
/* 802 */           MvcUriComponentsBuilder.logger.debug("Failed to create controller proxy, falling back on default constructor", (Throwable)ex);
/*     */         } 
/*     */       }
/*     */       
/* 806 */       if (proxy == null) {
/*     */         try {
/* 808 */           proxy = ReflectionUtils.accessibleConstructor(proxyClass, new Class[0]).newInstance(new Object[0]);
/*     */         }
/* 810 */         catch (Throwable ex) {
/* 811 */           throw new IllegalStateException("Failed to create controller proxy or use default constructor", ex);
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 816 */       ((Factory)proxy).setCallbacks(new Callback[] { (Callback)interceptor });
/* 817 */       return (T)proxy;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class MethodArgumentBuilder
/*     */   {
/*     */     private final Class<?> controllerType;
/*     */ 
/*     */ 
/*     */     
/*     */     private final Method method;
/*     */ 
/*     */     
/*     */     private final Object[] argumentValues;
/*     */ 
/*     */     
/*     */     private final UriComponentsBuilder baseUrl;
/*     */ 
/*     */ 
/*     */     
/*     */     public MethodArgumentBuilder(Class<?> controllerType, Method method) {
/* 841 */       this(null, controllerType, method);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MethodArgumentBuilder(@Nullable UriComponentsBuilder baseUrl, Class<?> controllerType, Method method) {
/* 849 */       Assert.notNull(controllerType, "'controllerType' is required");
/* 850 */       Assert.notNull(method, "'method' is required");
/* 851 */       this.baseUrl = (baseUrl != null) ? baseUrl : UriComponentsBuilder.fromPath(getPath());
/* 852 */       this.controllerType = controllerType;
/* 853 */       this.method = method;
/* 854 */       this.argumentValues = new Object[method.getParameterCount()];
/* 855 */       for (int i = 0; i < this.argumentValues.length; i++) {
/* 856 */         this.argumentValues[i] = null;
/*     */       }
/*     */     }
/*     */     
/*     */     private static String getPath() {
/* 861 */       ServletUriComponentsBuilder servletUriComponentsBuilder = ServletUriComponentsBuilder.fromCurrentServletMapping();
/* 862 */       String path = servletUriComponentsBuilder.build().getPath();
/* 863 */       return (path != null) ? path : "";
/*     */     }
/*     */     
/*     */     public MethodArgumentBuilder arg(int index, Object value) {
/* 867 */       this.argumentValues[index] = value;
/* 868 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MethodArgumentBuilder encode() {
/* 877 */       this.baseUrl.encode();
/* 878 */       return this;
/*     */     }
/*     */     
/*     */     public String build() {
/* 882 */       return MvcUriComponentsBuilder.fromMethodInternal(this.baseUrl, this.controllerType, this.method, this.argumentValues)
/* 883 */         .build().encode().toUriString();
/*     */     }
/*     */     
/*     */     public String buildAndExpand(Object... uriVars) {
/* 887 */       return MvcUriComponentsBuilder.fromMethodInternal(this.baseUrl, this.controllerType, this.method, this.argumentValues)
/* 888 */         .buildAndExpand(uriVars).encode().toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/MvcUriComponentsBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */