/*     */ package org.springframework.web.context;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextException;
/*     */ import org.springframework.context.ApplicationContextInitializer;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.core.GenericTypeResolver;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.core.io.ClassPathResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.support.PropertiesLoaderUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ContextLoader
/*     */ {
/*     */   public static final String CONTEXT_ID_PARAM = "contextId";
/*     */   public static final String CONFIG_LOCATION_PARAM = "contextConfigLocation";
/*     */   public static final String CONTEXT_CLASS_PARAM = "contextClass";
/*     */   public static final String CONTEXT_INITIALIZER_CLASSES_PARAM = "contextInitializerClasses";
/*     */   public static final String GLOBAL_INITIALIZER_CLASSES_PARAM = "globalInitializerClasses";
/*     */   private static final String INIT_PARAM_DELIMITERS = ",; \t\n";
/*     */   private static final String DEFAULT_STRATEGIES_PATH = "ContextLoader.properties";
/*     */   private static final Properties defaultStrategies;
/*     */   
/*     */   static {
/*     */     try {
/* 142 */       ClassPathResource resource = new ClassPathResource("ContextLoader.properties", ContextLoader.class);
/* 143 */       defaultStrategies = PropertiesLoaderUtils.loadProperties((Resource)resource);
/*     */     }
/* 145 */     catch (IOException ex) {
/* 146 */       throw new IllegalStateException("Could not load 'ContextLoader.properties': " + ex.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 154 */   private static final Map<ClassLoader, WebApplicationContext> currentContextPerThread = new ConcurrentHashMap<>(1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static volatile WebApplicationContext currentContext;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private WebApplicationContext context;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 172 */   private final List<ApplicationContextInitializer<ConfigurableApplicationContext>> contextInitializers = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContextLoader(WebApplicationContext context) {
/* 228 */     this.context = context;
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
/*     */   public void setContextInitializers(@Nullable ApplicationContextInitializer<?>... initializers) {
/* 241 */     if (initializers != null) {
/* 242 */       for (ApplicationContextInitializer<?> initializer : initializers) {
/* 243 */         this.contextInitializers.add(initializer);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebApplicationContext initWebApplicationContext(ServletContext servletContext) {
/* 261 */     if (servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE) != null) {
/* 262 */       throw new IllegalStateException("Cannot initialize context because there is already a root application context present - check whether you have multiple ContextLoader* definitions in your web.xml!");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 267 */     servletContext.log("Initializing Spring root WebApplicationContext");
/* 268 */     Log logger = LogFactory.getLog(ContextLoader.class);
/* 269 */     if (logger.isInfoEnabled()) {
/* 270 */       logger.info("Root WebApplicationContext: initialization started");
/*     */     }
/* 272 */     long startTime = System.currentTimeMillis();
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 277 */       if (this.context == null) {
/* 278 */         this.context = createWebApplicationContext(servletContext);
/*     */       }
/* 280 */       if (this.context instanceof ConfigurableWebApplicationContext) {
/* 281 */         ConfigurableWebApplicationContext cwac = (ConfigurableWebApplicationContext)this.context;
/* 282 */         if (!cwac.isActive()) {
/*     */ 
/*     */           
/* 285 */           if (cwac.getParent() == null) {
/*     */ 
/*     */             
/* 288 */             ApplicationContext parent = loadParentContext(servletContext);
/* 289 */             cwac.setParent(parent);
/*     */           } 
/* 291 */           configureAndRefreshWebApplicationContext(cwac, servletContext);
/*     */         } 
/*     */       } 
/* 294 */       servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.context);
/*     */       
/* 296 */       ClassLoader ccl = Thread.currentThread().getContextClassLoader();
/* 297 */       if (ccl == ContextLoader.class.getClassLoader()) {
/* 298 */         currentContext = this.context;
/*     */       }
/* 300 */       else if (ccl != null) {
/* 301 */         currentContextPerThread.put(ccl, this.context);
/*     */       } 
/*     */       
/* 304 */       if (logger.isInfoEnabled()) {
/* 305 */         long elapsedTime = System.currentTimeMillis() - startTime;
/* 306 */         logger.info("Root WebApplicationContext initialized in " + elapsedTime + " ms");
/*     */       } 
/*     */       
/* 309 */       return this.context;
/*     */     }
/* 311 */     catch (RuntimeException|Error ex) {
/* 312 */       logger.error("Context initialization failed", ex);
/* 313 */       servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, ex);
/* 314 */       throw ex;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected WebApplicationContext createWebApplicationContext(ServletContext sc) {
/* 331 */     Class<?> contextClass = determineContextClass(sc);
/* 332 */     if (!ConfigurableWebApplicationContext.class.isAssignableFrom(contextClass)) {
/* 333 */       throw new ApplicationContextException("Custom context class [" + contextClass.getName() + "] is not of type [" + ConfigurableWebApplicationContext.class
/* 334 */           .getName() + "]");
/*     */     }
/* 336 */     return (ConfigurableWebApplicationContext)BeanUtils.instantiateClass(contextClass);
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
/*     */   protected Class<?> determineContextClass(ServletContext servletContext) {
/* 348 */     String contextClassName = servletContext.getInitParameter("contextClass");
/* 349 */     if (contextClassName != null) {
/*     */       try {
/* 351 */         return ClassUtils.forName(contextClassName, ClassUtils.getDefaultClassLoader());
/*     */       }
/* 353 */       catch (ClassNotFoundException ex) {
/* 354 */         throw new ApplicationContextException("Failed to load custom context class [" + contextClassName + "]", ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 359 */     contextClassName = defaultStrategies.getProperty(WebApplicationContext.class.getName());
/*     */     try {
/* 361 */       return ClassUtils.forName(contextClassName, ContextLoader.class.getClassLoader());
/*     */     }
/* 363 */     catch (ClassNotFoundException ex) {
/* 364 */       throw new ApplicationContextException("Failed to load default context class [" + contextClassName + "]", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void configureAndRefreshWebApplicationContext(ConfigurableWebApplicationContext wac, ServletContext sc) {
/* 371 */     if (ObjectUtils.identityToString(wac).equals(wac.getId())) {
/*     */ 
/*     */       
/* 374 */       String idParam = sc.getInitParameter("contextId");
/* 375 */       if (idParam != null) {
/* 376 */         wac.setId(idParam);
/*     */       }
/*     */       else {
/*     */         
/* 380 */         wac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX + 
/* 381 */             ObjectUtils.getDisplayString(sc.getContextPath()));
/*     */       } 
/*     */     } 
/*     */     
/* 385 */     wac.setServletContext(sc);
/* 386 */     String configLocationParam = sc.getInitParameter("contextConfigLocation");
/* 387 */     if (configLocationParam != null) {
/* 388 */       wac.setConfigLocation(configLocationParam);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 394 */     ConfigurableEnvironment env = wac.getEnvironment();
/* 395 */     if (env instanceof ConfigurableWebEnvironment) {
/* 396 */       ((ConfigurableWebEnvironment)env).initPropertySources(sc, null);
/*     */     }
/*     */     
/* 399 */     customizeContext(sc, wac);
/* 400 */     wac.refresh();
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
/*     */   protected void customizeContext(ServletContext sc, ConfigurableWebApplicationContext wac) {
/* 422 */     List<Class<ApplicationContextInitializer<ConfigurableApplicationContext>>> initializerClasses = determineContextInitializerClasses(sc);
/*     */     
/* 424 */     for (Class<ApplicationContextInitializer<ConfigurableApplicationContext>> initializerClass : initializerClasses) {
/*     */       
/* 426 */       Class<?> initializerContextClass = GenericTypeResolver.resolveTypeArgument(initializerClass, ApplicationContextInitializer.class);
/* 427 */       if (initializerContextClass != null && !initializerContextClass.isInstance(wac)) {
/* 428 */         throw new ApplicationContextException(String.format("Could not apply context initializer [%s] since its generic parameter [%s] is not assignable from the type of application context used by this context loader: [%s]", new Object[] { initializerClass
/*     */ 
/*     */                 
/* 431 */                 .getName(), initializerContextClass.getName(), wac
/* 432 */                 .getClass().getName() }));
/*     */       }
/* 434 */       this.contextInitializers.add(BeanUtils.instantiateClass(initializerClass));
/*     */     } 
/*     */     
/* 437 */     AnnotationAwareOrderComparator.sort(this.contextInitializers);
/* 438 */     for (ApplicationContextInitializer<ConfigurableApplicationContext> initializer : this.contextInitializers) {
/* 439 */       initializer.initialize(wac);
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
/*     */   
/*     */   protected List<Class<ApplicationContextInitializer<ConfigurableApplicationContext>>> determineContextInitializerClasses(ServletContext servletContext) {
/* 452 */     List<Class<ApplicationContextInitializer<ConfigurableApplicationContext>>> classes = new ArrayList<>();
/*     */ 
/*     */     
/* 455 */     String globalClassNames = servletContext.getInitParameter("globalInitializerClasses");
/* 456 */     if (globalClassNames != null) {
/* 457 */       for (String className : StringUtils.tokenizeToStringArray(globalClassNames, ",; \t\n")) {
/* 458 */         classes.add(loadInitializerClass(className));
/*     */       }
/*     */     }
/*     */     
/* 462 */     String localClassNames = servletContext.getInitParameter("contextInitializerClasses");
/* 463 */     if (localClassNames != null) {
/* 464 */       for (String className : StringUtils.tokenizeToStringArray(localClassNames, ",; \t\n")) {
/* 465 */         classes.add(loadInitializerClass(className));
/*     */       }
/*     */     }
/*     */     
/* 469 */     return classes;
/*     */   }
/*     */ 
/*     */   
/*     */   private Class<ApplicationContextInitializer<ConfigurableApplicationContext>> loadInitializerClass(String className) {
/*     */     try {
/* 475 */       Class<?> clazz = ClassUtils.forName(className, ClassUtils.getDefaultClassLoader());
/* 476 */       if (!ApplicationContextInitializer.class.isAssignableFrom(clazz)) {
/* 477 */         throw new ApplicationContextException("Initializer class does not implement ApplicationContextInitializer interface: " + clazz);
/*     */       }
/*     */       
/* 480 */       return (Class)clazz;
/*     */     }
/* 482 */     catch (ClassNotFoundException ex) {
/* 483 */       throw new ApplicationContextException("Failed to load context initializer class [" + className + "]", ex);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected ApplicationContext loadParentContext(ServletContext servletContext) {
/* 503 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void closeWebApplicationContext(ServletContext servletContext) {
/* 513 */     servletContext.log("Closing Spring root WebApplicationContext");
/*     */     try {
/* 515 */       if (this.context instanceof ConfigurableWebApplicationContext) {
/* 516 */         ((ConfigurableWebApplicationContext)this.context).close();
/*     */       }
/*     */     } finally {
/*     */       
/* 520 */       ClassLoader ccl = Thread.currentThread().getContextClassLoader();
/* 521 */       if (ccl == ContextLoader.class.getClassLoader()) {
/* 522 */         currentContext = null;
/*     */       }
/* 524 */       else if (ccl != null) {
/* 525 */         currentContextPerThread.remove(ccl);
/*     */       } 
/* 527 */       servletContext.removeAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
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
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static WebApplicationContext getCurrentWebApplicationContext() {
/* 542 */     ClassLoader ccl = Thread.currentThread().getContextClassLoader();
/* 543 */     if (ccl != null) {
/* 544 */       WebApplicationContext ccpt = currentContextPerThread.get(ccl);
/* 545 */       if (ccpt != null) {
/* 546 */         return ccpt;
/*     */       }
/*     */     } 
/* 549 */     return currentContext;
/*     */   }
/*     */   
/*     */   public ContextLoader() {}
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/ContextLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */