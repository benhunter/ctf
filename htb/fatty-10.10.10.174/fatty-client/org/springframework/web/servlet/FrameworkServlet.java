/*      */ package org.springframework.web.servlet;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.security.Principal;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.List;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.stream.Collectors;
/*      */ import javax.servlet.DispatcherType;
/*      */ import javax.servlet.ServletException;
/*      */ import javax.servlet.ServletRequest;
/*      */ import javax.servlet.http.HttpServletRequest;
/*      */ import javax.servlet.http.HttpServletResponse;
/*      */ import javax.servlet.http.HttpServletResponseWrapper;
/*      */ import org.springframework.beans.BeanUtils;
/*      */ import org.springframework.context.ApplicationContext;
/*      */ import org.springframework.context.ApplicationContextAware;
/*      */ import org.springframework.context.ApplicationContextException;
/*      */ import org.springframework.context.ApplicationContextInitializer;
/*      */ import org.springframework.context.ApplicationEvent;
/*      */ import org.springframework.context.ApplicationListener;
/*      */ import org.springframework.context.ConfigurableApplicationContext;
/*      */ import org.springframework.context.event.ContextRefreshedEvent;
/*      */ import org.springframework.context.event.SourceFilteringListener;
/*      */ import org.springframework.context.i18n.LocaleContext;
/*      */ import org.springframework.context.i18n.LocaleContextHolder;
/*      */ import org.springframework.context.i18n.SimpleLocaleContext;
/*      */ import org.springframework.core.GenericTypeResolver;
/*      */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*      */ import org.springframework.core.env.ConfigurableEnvironment;
/*      */ import org.springframework.http.HttpMethod;
/*      */ import org.springframework.http.HttpStatus;
/*      */ import org.springframework.lang.Nullable;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.ObjectUtils;
/*      */ import org.springframework.util.StringUtils;
/*      */ import org.springframework.web.context.ConfigurableWebApplicationContext;
/*      */ import org.springframework.web.context.ConfigurableWebEnvironment;
/*      */ import org.springframework.web.context.WebApplicationContext;
/*      */ import org.springframework.web.context.request.NativeWebRequest;
/*      */ import org.springframework.web.context.request.RequestAttributes;
/*      */ import org.springframework.web.context.request.RequestContextHolder;
/*      */ import org.springframework.web.context.request.ServletRequestAttributes;
/*      */ import org.springframework.web.context.request.async.CallableProcessingInterceptor;
/*      */ import org.springframework.web.context.request.async.WebAsyncManager;
/*      */ import org.springframework.web.context.request.async.WebAsyncUtils;
/*      */ import org.springframework.web.context.support.ServletRequestHandledEvent;
/*      */ import org.springframework.web.context.support.WebApplicationContextUtils;
/*      */ import org.springframework.web.context.support.XmlWebApplicationContext;
/*      */ import org.springframework.web.cors.CorsUtils;
/*      */ import org.springframework.web.util.NestedServletException;
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
/*      */ public abstract class FrameworkServlet
/*      */   extends HttpServletBean
/*      */   implements ApplicationContextAware
/*      */ {
/*      */   public static final String DEFAULT_NAMESPACE_SUFFIX = "-servlet";
/*  155 */   public static final Class<?> DEFAULT_CONTEXT_CLASS = XmlWebApplicationContext.class;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  161 */   public static final String SERVLET_CONTEXT_PREFIX = FrameworkServlet.class.getName() + ".CONTEXT.";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String INIT_PARAM_DELIMITERS = ",; \t\n";
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private String contextAttribute;
/*      */ 
/*      */ 
/*      */   
/*  175 */   private Class<?> contextClass = DEFAULT_CONTEXT_CLASS;
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private String contextId;
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private String namespace;
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private String contextConfigLocation;
/*      */ 
/*      */   
/*  190 */   private final List<ApplicationContextInitializer<ConfigurableApplicationContext>> contextInitializers = new ArrayList<>();
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private String contextInitializerClasses;
/*      */ 
/*      */   
/*      */   private boolean publishContext = true;
/*      */ 
/*      */   
/*      */   private boolean publishEvents = true;
/*      */ 
/*      */   
/*      */   private boolean threadContextInheritable = false;
/*      */ 
/*      */   
/*      */   private boolean dispatchOptionsRequest = false;
/*      */ 
/*      */   
/*      */   private boolean dispatchTraceRequest = false;
/*      */ 
/*      */   
/*      */   private boolean enableLoggingRequestDetails = false;
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private WebApplicationContext webApplicationContext;
/*      */ 
/*      */   
/*      */   private boolean webApplicationContextInjected = false;
/*      */ 
/*      */   
/*      */   private volatile boolean refreshEventReceived = false;
/*      */ 
/*      */   
/*  226 */   private final Object onRefreshMonitor = new Object();
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
/*      */   public FrameworkServlet(WebApplicationContext webApplicationContext) {
/*  290 */     this.webApplicationContext = webApplicationContext;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setContextAttribute(@Nullable String contextAttribute) {
/*  299 */     this.contextAttribute = contextAttribute;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public String getContextAttribute() {
/*  308 */     return this.contextAttribute;
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
/*      */   public void setContextClass(Class<?> contextClass) {
/*  321 */     this.contextClass = contextClass;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Class<?> getContextClass() {
/*  328 */     return this.contextClass;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setContextId(@Nullable String contextId) {
/*  336 */     this.contextId = contextId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public String getContextId() {
/*  344 */     return this.contextId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNamespace(String namespace) {
/*  352 */     this.namespace = namespace;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getNamespace() {
/*  360 */     return (this.namespace != null) ? this.namespace : (getServletName() + "-servlet");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setContextConfigLocation(@Nullable String contextConfigLocation) {
/*  369 */     this.contextConfigLocation = contextConfigLocation;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public String getContextConfigLocation() {
/*  377 */     return this.contextConfigLocation;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setContextInitializers(@Nullable ApplicationContextInitializer<?>... initializers) {
/*  388 */     if (initializers != null) {
/*  389 */       for (ApplicationContextInitializer<?> initializer : initializers) {
/*  390 */         this.contextInitializers.add(initializer);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setContextInitializerClasses(String contextInitializerClasses) {
/*  402 */     this.contextInitializerClasses = contextInitializerClasses;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPublishContext(boolean publishContext) {
/*  412 */     this.publishContext = publishContext;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPublishEvents(boolean publishEvents) {
/*  422 */     this.publishEvents = publishEvents;
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
/*      */   public void setThreadContextInheritable(boolean threadContextInheritable) {
/*  438 */     this.threadContextInheritable = threadContextInheritable;
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
/*      */   public void setDispatchOptionsRequest(boolean dispatchOptionsRequest) {
/*  458 */     this.dispatchOptionsRequest = dispatchOptionsRequest;
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
/*      */   public void setDispatchTraceRequest(boolean dispatchTraceRequest) {
/*  475 */     this.dispatchTraceRequest = dispatchTraceRequest;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEnableLoggingRequestDetails(boolean enable) {
/*  486 */     this.enableLoggingRequestDetails = enable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEnableLoggingRequestDetails() {
/*  495 */     return this.enableLoggingRequestDetails;
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
/*      */   public void setApplicationContext(ApplicationContext applicationContext) {
/*  509 */     if (this.webApplicationContext == null && applicationContext instanceof WebApplicationContext) {
/*  510 */       this.webApplicationContext = (WebApplicationContext)applicationContext;
/*  511 */       this.webApplicationContextInjected = true;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void initServletBean() throws ServletException {
/*  522 */     getServletContext().log("Initializing Spring " + getClass().getSimpleName() + " '" + getServletName() + "'");
/*  523 */     if (this.logger.isInfoEnabled()) {
/*  524 */       this.logger.info("Initializing Servlet '" + getServletName() + "'");
/*      */     }
/*  526 */     long startTime = System.currentTimeMillis();
/*      */     
/*      */     try {
/*  529 */       this.webApplicationContext = initWebApplicationContext();
/*  530 */       initFrameworkServlet();
/*      */     }
/*  532 */     catch (ServletException|RuntimeException ex) {
/*  533 */       this.logger.error("Context initialization failed", ex);
/*  534 */       throw ex;
/*      */     } 
/*      */     
/*  537 */     if (this.logger.isDebugEnabled()) {
/*  538 */       String value = this.enableLoggingRequestDetails ? "shown which may lead to unsafe logging of potentially sensitive data" : "masked to prevent unsafe logging of potentially sensitive data";
/*      */ 
/*      */       
/*  541 */       this.logger.debug("enableLoggingRequestDetails='" + this.enableLoggingRequestDetails + "': request parameters and headers will be " + value);
/*      */     } 
/*      */ 
/*      */     
/*  545 */     if (this.logger.isInfoEnabled()) {
/*  546 */       this.logger.info("Completed initialization in " + (System.currentTimeMillis() - startTime) + " ms");
/*      */     }
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
/*      */   protected WebApplicationContext initWebApplicationContext() {
/*  561 */     WebApplicationContext rootContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
/*  562 */     WebApplicationContext wac = null;
/*      */     
/*  564 */     if (this.webApplicationContext != null) {
/*      */       
/*  566 */       wac = this.webApplicationContext;
/*  567 */       if (wac instanceof ConfigurableWebApplicationContext) {
/*  568 */         ConfigurableWebApplicationContext cwac = (ConfigurableWebApplicationContext)wac;
/*  569 */         if (!cwac.isActive()) {
/*      */ 
/*      */           
/*  572 */           if (cwac.getParent() == null)
/*      */           {
/*      */             
/*  575 */             cwac.setParent((ApplicationContext)rootContext);
/*      */           }
/*  577 */           configureAndRefreshWebApplicationContext(cwac);
/*      */         } 
/*      */       } 
/*      */     } 
/*  581 */     if (wac == null)
/*      */     {
/*      */ 
/*      */ 
/*      */       
/*  586 */       wac = findWebApplicationContext();
/*      */     }
/*  588 */     if (wac == null)
/*      */     {
/*  590 */       wac = createWebApplicationContext(rootContext);
/*      */     }
/*      */     
/*  593 */     if (!this.refreshEventReceived)
/*      */     {
/*      */ 
/*      */       
/*  597 */       synchronized (this.onRefreshMonitor) {
/*  598 */         onRefresh((ApplicationContext)wac);
/*      */       } 
/*      */     }
/*      */     
/*  602 */     if (this.publishContext) {
/*      */       
/*  604 */       String attrName = getServletContextAttributeName();
/*  605 */       getServletContext().setAttribute(attrName, wac);
/*      */     } 
/*      */     
/*  608 */     return wac;
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
/*      */   @Nullable
/*      */   protected WebApplicationContext findWebApplicationContext() {
/*  623 */     String attrName = getContextAttribute();
/*  624 */     if (attrName == null) {
/*  625 */       return null;
/*      */     }
/*      */     
/*  628 */     WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(getServletContext(), attrName);
/*  629 */     if (wac == null) {
/*  630 */       throw new IllegalStateException("No WebApplicationContext found: initializer not registered?");
/*      */     }
/*  632 */     return wac;
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
/*      */   protected WebApplicationContext createWebApplicationContext(@Nullable ApplicationContext parent) {
/*  651 */     Class<?> contextClass = getContextClass();
/*  652 */     if (!ConfigurableWebApplicationContext.class.isAssignableFrom(contextClass)) {
/*  653 */       throw new ApplicationContextException("Fatal initialization error in servlet with name '" + 
/*  654 */           getServletName() + "': custom WebApplicationContext class [" + contextClass
/*  655 */           .getName() + "] is not of type ConfigurableWebApplicationContext");
/*      */     }
/*      */ 
/*      */     
/*  659 */     ConfigurableWebApplicationContext wac = (ConfigurableWebApplicationContext)BeanUtils.instantiateClass(contextClass);
/*      */     
/*  661 */     wac.setEnvironment(getEnvironment());
/*  662 */     wac.setParent(parent);
/*  663 */     String configLocation = getContextConfigLocation();
/*  664 */     if (configLocation != null) {
/*  665 */       wac.setConfigLocation(configLocation);
/*      */     }
/*  667 */     configureAndRefreshWebApplicationContext(wac);
/*      */     
/*  669 */     return (WebApplicationContext)wac;
/*      */   }
/*      */   
/*      */   protected void configureAndRefreshWebApplicationContext(ConfigurableWebApplicationContext wac) {
/*  673 */     if (ObjectUtils.identityToString(wac).equals(wac.getId()))
/*      */     {
/*      */       
/*  676 */       if (this.contextId != null) {
/*  677 */         wac.setId(this.contextId);
/*      */       }
/*      */       else {
/*      */         
/*  681 */         wac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX + 
/*  682 */             ObjectUtils.getDisplayString(getServletContext().getContextPath()) + '/' + getServletName());
/*      */       } 
/*      */     }
/*      */     
/*  686 */     wac.setServletContext(getServletContext());
/*  687 */     wac.setServletConfig(getServletConfig());
/*  688 */     wac.setNamespace(getNamespace());
/*  689 */     wac.addApplicationListener((ApplicationListener)new SourceFilteringListener(wac, new ContextRefreshListener()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  694 */     ConfigurableEnvironment env = wac.getEnvironment();
/*  695 */     if (env instanceof ConfigurableWebEnvironment) {
/*  696 */       ((ConfigurableWebEnvironment)env).initPropertySources(getServletContext(), getServletConfig());
/*      */     }
/*      */     
/*  699 */     postProcessWebApplicationContext(wac);
/*  700 */     applyInitializers((ConfigurableApplicationContext)wac);
/*  701 */     wac.refresh();
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
/*      */   protected WebApplicationContext createWebApplicationContext(@Nullable WebApplicationContext parent) {
/*  715 */     return createWebApplicationContext((ApplicationContext)parent);
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
/*      */   protected void postProcessWebApplicationContext(ConfigurableWebApplicationContext wac) {}
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
/*      */   protected void applyInitializers(ConfigurableApplicationContext wac) {
/*  748 */     String globalClassNames = getServletContext().getInitParameter("globalInitializerClasses");
/*  749 */     if (globalClassNames != null) {
/*  750 */       for (String className : StringUtils.tokenizeToStringArray(globalClassNames, ",; \t\n")) {
/*  751 */         this.contextInitializers.add(loadInitializer(className, wac));
/*      */       }
/*      */     }
/*      */     
/*  755 */     if (this.contextInitializerClasses != null) {
/*  756 */       for (String className : StringUtils.tokenizeToStringArray(this.contextInitializerClasses, ",; \t\n")) {
/*  757 */         this.contextInitializers.add(loadInitializer(className, wac));
/*      */       }
/*      */     }
/*      */     
/*  761 */     AnnotationAwareOrderComparator.sort(this.contextInitializers);
/*  762 */     for (ApplicationContextInitializer<ConfigurableApplicationContext> initializer : this.contextInitializers) {
/*  763 */       initializer.initialize(wac);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private ApplicationContextInitializer<ConfigurableApplicationContext> loadInitializer(String className, ConfigurableApplicationContext wac) {
/*      */     try {
/*  771 */       Class<?> initializerClass = ClassUtils.forName(className, wac.getClassLoader());
/*      */       
/*  773 */       Class<?> initializerContextClass = GenericTypeResolver.resolveTypeArgument(initializerClass, ApplicationContextInitializer.class);
/*  774 */       if (initializerContextClass != null && !initializerContextClass.isInstance(wac)) {
/*  775 */         throw new ApplicationContextException(String.format("Could not apply context initializer [%s] since its generic parameter [%s] is not assignable from the type of application context used by this framework servlet: [%s]", new Object[] { initializerClass
/*      */ 
/*      */                 
/*  778 */                 .getName(), initializerContextClass.getName(), wac
/*  779 */                 .getClass().getName() }));
/*      */       }
/*  781 */       return (ApplicationContextInitializer<ConfigurableApplicationContext>)BeanUtils.instantiateClass(initializerClass, ApplicationContextInitializer.class);
/*      */     }
/*  783 */     catch (ClassNotFoundException ex) {
/*  784 */       throw new ApplicationContextException(String.format("Could not load class [%s] specified via 'contextInitializerClasses' init-param", new Object[] { className }), ex);
/*      */     } 
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
/*      */   public String getServletContextAttributeName() {
/*  797 */     return SERVLET_CONTEXT_PREFIX + getServletName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public final WebApplicationContext getWebApplicationContext() {
/*  805 */     return this.webApplicationContext;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void initFrameworkServlet() throws ServletException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void refresh() {
/*  825 */     WebApplicationContext wac = getWebApplicationContext();
/*  826 */     if (!(wac instanceof ConfigurableApplicationContext)) {
/*  827 */       throw new IllegalStateException("WebApplicationContext does not support refresh: " + wac);
/*      */     }
/*  829 */     ((ConfigurableApplicationContext)wac).refresh();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void onApplicationEvent(ContextRefreshedEvent event) {
/*  839 */     this.refreshEventReceived = true;
/*  840 */     synchronized (this.onRefreshMonitor) {
/*  841 */       onRefresh(event.getApplicationContext());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void onRefresh(ApplicationContext context) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void destroy() {
/*  862 */     getServletContext().log("Destroying Spring FrameworkServlet '" + getServletName() + "'");
/*      */     
/*  864 */     if (this.webApplicationContext instanceof ConfigurableApplicationContext && !this.webApplicationContextInjected) {
/*  865 */       ((ConfigurableApplicationContext)this.webApplicationContext).close();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/*  877 */     HttpMethod httpMethod = HttpMethod.resolve(request.getMethod());
/*  878 */     if (httpMethod == HttpMethod.PATCH || httpMethod == null) {
/*  879 */       processRequest(request, response);
/*      */     } else {
/*      */       
/*  882 */       super.service(request, response);
/*      */     } 
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
/*      */   protected final void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/*  897 */     processRequest(request, response);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/*  908 */     processRequest(request, response);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/*  919 */     processRequest(request, response);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/*  930 */     processRequest(request, response);
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
/*      */   protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/*  943 */     if (this.dispatchOptionsRequest || CorsUtils.isPreFlightRequest(request)) {
/*  944 */       processRequest(request, response);
/*  945 */       if (response.containsHeader("Allow")) {
/*      */         return;
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  952 */     super.doOptions(request, (HttpServletResponse)new HttpServletResponseWrapper(response)
/*      */         {
/*      */           public void setHeader(String name, String value) {
/*  955 */             if ("Allow".equals(name)) {
/*  956 */               value = (StringUtils.hasLength(value) ? (value + ", ") : "") + HttpMethod.PATCH.name();
/*      */             }
/*  958 */             super.setHeader(name, value);
/*      */           }
/*      */         });
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
/*      */   protected void doTrace(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/*  972 */     if (this.dispatchTraceRequest) {
/*  973 */       processRequest(request, response);
/*  974 */       if ("message/http".equals(response.getContentType())) {
/*      */         return;
/*      */       }
/*      */     } 
/*      */     
/*  979 */     super.doTrace(request, response);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/*  990 */     long startTime = System.currentTimeMillis();
/*  991 */     Throwable failureCause = null;
/*      */     
/*  993 */     LocaleContext previousLocaleContext = LocaleContextHolder.getLocaleContext();
/*  994 */     LocaleContext localeContext = buildLocaleContext(request);
/*      */     
/*  996 */     RequestAttributes previousAttributes = RequestContextHolder.getRequestAttributes();
/*  997 */     ServletRequestAttributes requestAttributes = buildRequestAttributes(request, response, previousAttributes);
/*      */     
/*  999 */     WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager((ServletRequest)request);
/* 1000 */     asyncManager.registerCallableInterceptor(FrameworkServlet.class.getName(), new RequestBindingInterceptor());
/*      */     
/* 1002 */     initContextHolders(request, localeContext, (RequestAttributes)requestAttributes);
/*      */     
/*      */     try {
/* 1005 */       doService(request, response);
/*      */     }
/* 1007 */     catch (ServletException|IOException ex) {
/* 1008 */       failureCause = ex;
/* 1009 */       throw ex;
/*      */     }
/* 1011 */     catch (Throwable ex) {
/* 1012 */       failureCause = ex;
/* 1013 */       throw new NestedServletException("Request processing failed", ex);
/*      */     }
/*      */     finally {
/*      */       
/* 1017 */       resetContextHolders(request, previousLocaleContext, previousAttributes);
/* 1018 */       if (requestAttributes != null) {
/* 1019 */         requestAttributes.requestCompleted();
/*      */       }
/* 1021 */       logResult(request, response, failureCause, asyncManager);
/* 1022 */       publishRequestHandledEvent(request, response, startTime, failureCause);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected LocaleContext buildLocaleContext(HttpServletRequest request) {
/* 1035 */     return (LocaleContext)new SimpleLocaleContext(request.getLocale());
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
/*      */   @Nullable
/*      */   protected ServletRequestAttributes buildRequestAttributes(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable RequestAttributes previousAttributes) {
/* 1053 */     if (previousAttributes == null || previousAttributes instanceof ServletRequestAttributes) {
/* 1054 */       return new ServletRequestAttributes(request, response);
/*      */     }
/*      */     
/* 1057 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void initContextHolders(HttpServletRequest request, @Nullable LocaleContext localeContext, @Nullable RequestAttributes requestAttributes) {
/* 1064 */     if (localeContext != null) {
/* 1065 */       LocaleContextHolder.setLocaleContext(localeContext, this.threadContextInheritable);
/*      */     }
/* 1067 */     if (requestAttributes != null) {
/* 1068 */       RequestContextHolder.setRequestAttributes(requestAttributes, this.threadContextInheritable);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void resetContextHolders(HttpServletRequest request, @Nullable LocaleContext prevLocaleContext, @Nullable RequestAttributes previousAttributes) {
/* 1075 */     LocaleContextHolder.setLocaleContext(prevLocaleContext, this.threadContextInheritable);
/* 1076 */     RequestContextHolder.setRequestAttributes(previousAttributes, this.threadContextInheritable);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void logResult(HttpServletRequest request, HttpServletResponse response, @Nullable Throwable failureCause, WebAsyncManager asyncManager) {
/* 1082 */     if (!this.logger.isDebugEnabled()) {
/*      */       return;
/*      */     }
/*      */     
/* 1086 */     String dispatchType = request.getDispatcherType().name();
/* 1087 */     boolean initialDispatch = request.getDispatcherType().equals(DispatcherType.REQUEST);
/*      */     
/* 1089 */     if (failureCause != null) {
/* 1090 */       if (!initialDispatch) {
/*      */         
/* 1092 */         if (this.logger.isDebugEnabled()) {
/* 1093 */           this.logger.debug("Unresolved failure from \"" + dispatchType + "\" dispatch: " + failureCause);
/*      */         }
/*      */       }
/* 1096 */       else if (this.logger.isTraceEnabled()) {
/* 1097 */         this.logger.trace("Failed to complete request", failureCause);
/*      */       } else {
/*      */         
/* 1100 */         this.logger.debug("Failed to complete request: " + failureCause);
/*      */       } 
/*      */       
/*      */       return;
/*      */     } 
/* 1105 */     if (asyncManager.isConcurrentHandlingStarted()) {
/* 1106 */       this.logger.debug("Exiting but response remains open for further handling");
/*      */       
/*      */       return;
/*      */     } 
/* 1110 */     int status = response.getStatus();
/* 1111 */     String headers = "";
/*      */     
/* 1113 */     if (this.logger.isTraceEnabled()) {
/* 1114 */       Collection<String> names = response.getHeaderNames();
/* 1115 */       if (this.enableLoggingRequestDetails) {
/*      */         
/* 1117 */         headers = names.stream().map(name -> name + ":" + response.getHeaders(name)).collect(Collectors.joining(", "));
/*      */       } else {
/*      */         
/* 1120 */         headers = names.isEmpty() ? "" : "masked";
/*      */       } 
/* 1122 */       headers = ", headers={" + headers + "}";
/*      */     } 
/*      */     
/* 1125 */     if (!initialDispatch) {
/* 1126 */       this.logger.debug("Exiting from \"" + dispatchType + "\" dispatch, status " + status + headers);
/*      */     } else {
/*      */       
/* 1129 */       HttpStatus httpStatus = HttpStatus.resolve(status);
/* 1130 */       this.logger.debug("Completed " + ((httpStatus != null) ? httpStatus : Integer.valueOf(status)) + headers);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void publishRequestHandledEvent(HttpServletRequest request, HttpServletResponse response, long startTime, @Nullable Throwable failureCause) {
/* 1137 */     if (this.publishEvents && this.webApplicationContext != null) {
/*      */       
/* 1139 */       long processingTime = System.currentTimeMillis() - startTime;
/* 1140 */       this.webApplicationContext.publishEvent((ApplicationEvent)new ServletRequestHandledEvent(this, request
/*      */             
/* 1142 */             .getRequestURI(), request.getRemoteAddr(), request
/* 1143 */             .getMethod(), getServletConfig().getServletName(), 
/* 1144 */             WebUtils.getSessionId(request), getUsernameForRequest(request), processingTime, failureCause, response
/* 1145 */             .getStatus()));
/*      */     } 
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
/*      */   protected String getUsernameForRequest(HttpServletRequest request) {
/* 1159 */     Principal userPrincipal = request.getUserPrincipal();
/* 1160 */     return (userPrincipal != null) ? userPrincipal.getName() : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FrameworkServlet() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract void doService(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse) throws Exception;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class ContextRefreshListener
/*      */     implements ApplicationListener<ContextRefreshedEvent>
/*      */   {
/*      */     private ContextRefreshListener() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void onApplicationEvent(ContextRefreshedEvent event) {
/* 1189 */       FrameworkServlet.this.onApplicationEvent(event);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private class RequestBindingInterceptor
/*      */     implements CallableProcessingInterceptor
/*      */   {
/*      */     private RequestBindingInterceptor() {}
/*      */ 
/*      */     
/*      */     public <T> void preProcess(NativeWebRequest webRequest, Callable<T> task) {
/* 1202 */       HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/* 1203 */       if (request != null) {
/* 1204 */         HttpServletResponse response = (HttpServletResponse)webRequest.getNativeResponse(HttpServletResponse.class);
/* 1205 */         FrameworkServlet.this.initContextHolders(request, FrameworkServlet.this.buildLocaleContext(request), (RequestAttributes)FrameworkServlet.this
/* 1206 */             .buildRequestAttributes(request, response, (RequestAttributes)null));
/*      */       } 
/*      */     }
/*      */     
/*      */     public <T> void postProcess(NativeWebRequest webRequest, Callable<T> task, Object concurrentResult) {
/* 1211 */       HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/* 1212 */       if (request != null)
/* 1213 */         FrameworkServlet.this.resetContextHolders(request, (LocaleContext)null, (RequestAttributes)null); 
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/FrameworkServlet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */