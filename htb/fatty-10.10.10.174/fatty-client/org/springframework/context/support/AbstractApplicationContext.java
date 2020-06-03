/*      */ package org.springframework.context.support;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Date;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.atomic.AtomicBoolean;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.apache.commons.logging.LogFactory;
/*      */ import org.springframework.beans.BeansException;
/*      */ import org.springframework.beans.CachedIntrospectionResults;
/*      */ import org.springframework.beans.PropertyEditorRegistrar;
/*      */ import org.springframework.beans.factory.BeanFactory;
/*      */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*      */ import org.springframework.beans.factory.ObjectProvider;
/*      */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
/*      */ import org.springframework.beans.factory.config.BeanExpressionResolver;
/*      */ import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
/*      */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*      */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*      */ import org.springframework.beans.support.ResourceEditorRegistrar;
/*      */ import org.springframework.context.ApplicationContext;
/*      */ import org.springframework.context.ApplicationContextAware;
/*      */ import org.springframework.context.ApplicationEvent;
/*      */ import org.springframework.context.ApplicationEventPublisher;
/*      */ import org.springframework.context.ApplicationEventPublisherAware;
/*      */ import org.springframework.context.ApplicationListener;
/*      */ import org.springframework.context.ConfigurableApplicationContext;
/*      */ import org.springframework.context.EmbeddedValueResolverAware;
/*      */ import org.springframework.context.EnvironmentAware;
/*      */ import org.springframework.context.HierarchicalMessageSource;
/*      */ import org.springframework.context.LifecycleProcessor;
/*      */ import org.springframework.context.MessageSource;
/*      */ import org.springframework.context.MessageSourceAware;
/*      */ import org.springframework.context.MessageSourceResolvable;
/*      */ import org.springframework.context.NoSuchMessageException;
/*      */ import org.springframework.context.PayloadApplicationEvent;
/*      */ import org.springframework.context.ResourceLoaderAware;
/*      */ import org.springframework.context.event.ApplicationEventMulticaster;
/*      */ import org.springframework.context.event.ContextClosedEvent;
/*      */ import org.springframework.context.event.ContextRefreshedEvent;
/*      */ import org.springframework.context.event.ContextStartedEvent;
/*      */ import org.springframework.context.event.ContextStoppedEvent;
/*      */ import org.springframework.context.event.SimpleApplicationEventMulticaster;
/*      */ import org.springframework.context.expression.StandardBeanExpressionResolver;
/*      */ import org.springframework.context.weaving.LoadTimeWeaverAware;
/*      */ import org.springframework.context.weaving.LoadTimeWeaverAwareProcessor;
/*      */ import org.springframework.core.ResolvableType;
/*      */ import org.springframework.core.annotation.AnnotationUtils;
/*      */ import org.springframework.core.convert.ConversionService;
/*      */ import org.springframework.core.env.ConfigurableEnvironment;
/*      */ import org.springframework.core.env.Environment;
/*      */ import org.springframework.core.env.PropertyResolver;
/*      */ import org.springframework.core.env.StandardEnvironment;
/*      */ import org.springframework.core.io.DefaultResourceLoader;
/*      */ import org.springframework.core.io.Resource;
/*      */ import org.springframework.core.io.ResourceLoader;
/*      */ import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
/*      */ import org.springframework.core.io.support.ResourcePatternResolver;
/*      */ import org.springframework.lang.Nullable;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ObjectUtils;
/*      */ import org.springframework.util.ReflectionUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class AbstractApplicationContext
/*      */   extends DefaultResourceLoader
/*      */   implements ConfigurableApplicationContext
/*      */ {
/*      */   public static final String MESSAGE_SOURCE_BEAN_NAME = "messageSource";
/*      */   public static final String LIFECYCLE_PROCESSOR_BEAN_NAME = "lifecycleProcessor";
/*      */   public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";
/*      */   
/*      */   static {
/*  157 */     ContextClosedEvent.class.getName();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  162 */   protected final Log logger = LogFactory.getLog(getClass());
/*      */ 
/*      */   
/*  165 */   private String id = ObjectUtils.identityToString(this);
/*      */ 
/*      */   
/*  168 */   private String displayName = ObjectUtils.identityToString(this);
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private ApplicationContext parent;
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private ConfigurableEnvironment environment;
/*      */ 
/*      */   
/*  179 */   private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();
/*      */ 
/*      */   
/*      */   private long startupDate;
/*      */ 
/*      */   
/*  185 */   private final AtomicBoolean active = new AtomicBoolean();
/*      */ 
/*      */   
/*  188 */   private final AtomicBoolean closed = new AtomicBoolean();
/*      */ 
/*      */   
/*  191 */   private final Object startupShutdownMonitor = new Object();
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private Thread shutdownHook;
/*      */ 
/*      */   
/*      */   private ResourcePatternResolver resourcePatternResolver;
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private LifecycleProcessor lifecycleProcessor;
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private MessageSource messageSource;
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private ApplicationEventMulticaster applicationEventMulticaster;
/*      */ 
/*      */   
/*  213 */   private final Set<ApplicationListener<?>> applicationListeners = new LinkedHashSet<>();
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private Set<ApplicationListener<?>> earlyApplicationListeners;
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private Set<ApplicationEvent> earlyApplicationEvents;
/*      */ 
/*      */ 
/*      */   
/*      */   public AbstractApplicationContext() {
/*  228 */     this.resourcePatternResolver = getResourcePatternResolver();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AbstractApplicationContext(@Nullable ApplicationContext parent) {
/*  236 */     this();
/*  237 */     setParent(parent);
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
/*      */   public void setId(String id) {
/*  253 */     this.id = id;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getId() {
/*  258 */     return this.id;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getApplicationName() {
/*  263 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDisplayName(String displayName) {
/*  272 */     Assert.hasLength(displayName, "Display name must not be empty");
/*  273 */     this.displayName = displayName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDisplayName() {
/*  282 */     return this.displayName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public ApplicationContext getParent() {
/*  292 */     return this.parent;
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
/*      */   public void setEnvironment(ConfigurableEnvironment environment) {
/*  305 */     this.environment = environment;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConfigurableEnvironment getEnvironment() {
/*  316 */     if (this.environment == null) {
/*  317 */       this.environment = createEnvironment();
/*      */     }
/*  319 */     return this.environment;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ConfigurableEnvironment createEnvironment() {
/*  328 */     return (ConfigurableEnvironment)new StandardEnvironment();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException {
/*  338 */     return (AutowireCapableBeanFactory)getBeanFactory();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getStartupDate() {
/*  346 */     return this.startupDate;
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
/*      */   public void publishEvent(ApplicationEvent event) {
/*  359 */     publishEvent(event, (ResolvableType)null);
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
/*      */   public void publishEvent(Object event) {
/*  372 */     publishEvent(event, (ResolvableType)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void publishEvent(Object event, @Nullable ResolvableType eventType) {
/*      */     PayloadApplicationEvent payloadApplicationEvent;
/*  383 */     Assert.notNull(event, "Event must not be null");
/*      */ 
/*      */ 
/*      */     
/*  387 */     if (event instanceof ApplicationEvent) {
/*  388 */       ApplicationEvent applicationEvent = (ApplicationEvent)event;
/*      */     } else {
/*      */       
/*  391 */       payloadApplicationEvent = new PayloadApplicationEvent(this, event);
/*  392 */       if (eventType == null) {
/*  393 */         eventType = payloadApplicationEvent.getResolvableType();
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  398 */     if (this.earlyApplicationEvents != null) {
/*  399 */       this.earlyApplicationEvents.add(payloadApplicationEvent);
/*      */     } else {
/*      */       
/*  402 */       getApplicationEventMulticaster().multicastEvent((ApplicationEvent)payloadApplicationEvent, eventType);
/*      */     } 
/*      */ 
/*      */     
/*  406 */     if (this.parent != null) {
/*  407 */       if (this.parent instanceof AbstractApplicationContext) {
/*  408 */         ((AbstractApplicationContext)this.parent).publishEvent(event, eventType);
/*      */       } else {
/*      */         
/*  411 */         this.parent.publishEvent(event);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   ApplicationEventMulticaster getApplicationEventMulticaster() throws IllegalStateException {
/*  422 */     if (this.applicationEventMulticaster == null) {
/*  423 */       throw new IllegalStateException("ApplicationEventMulticaster not initialized - call 'refresh' before multicasting events via the context: " + this);
/*      */     }
/*      */     
/*  426 */     return this.applicationEventMulticaster;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   LifecycleProcessor getLifecycleProcessor() throws IllegalStateException {
/*  435 */     if (this.lifecycleProcessor == null) {
/*  436 */       throw new IllegalStateException("LifecycleProcessor not initialized - call 'refresh' before invoking lifecycle methods via the context: " + this);
/*      */     }
/*      */     
/*  439 */     return this.lifecycleProcessor;
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
/*      */   protected ResourcePatternResolver getResourcePatternResolver() {
/*  457 */     return (ResourcePatternResolver)new PathMatchingResourcePatternResolver((ResourceLoader)this);
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
/*      */   public void setParent(@Nullable ApplicationContext parent) {
/*  475 */     this.parent = parent;
/*  476 */     if (parent != null) {
/*  477 */       Environment parentEnvironment = parent.getEnvironment();
/*  478 */       if (parentEnvironment instanceof ConfigurableEnvironment) {
/*  479 */         getEnvironment().merge((ConfigurableEnvironment)parentEnvironment);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor) {
/*  486 */     Assert.notNull(postProcessor, "BeanFactoryPostProcessor must not be null");
/*  487 */     this.beanFactoryPostProcessors.add(postProcessor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors() {
/*  495 */     return this.beanFactoryPostProcessors;
/*      */   }
/*      */ 
/*      */   
/*      */   public void addApplicationListener(ApplicationListener<?> listener) {
/*  500 */     Assert.notNull(listener, "ApplicationListener must not be null");
/*  501 */     if (this.applicationEventMulticaster != null) {
/*  502 */       this.applicationEventMulticaster.addApplicationListener(listener);
/*      */     }
/*  504 */     this.applicationListeners.add(listener);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<ApplicationListener<?>> getApplicationListeners() {
/*  511 */     return this.applicationListeners;
/*      */   }
/*      */ 
/*      */   
/*      */   public void refresh() throws BeansException, IllegalStateException {
/*  516 */     synchronized (this.startupShutdownMonitor) {
/*      */       
/*  518 */       prepareRefresh();
/*      */ 
/*      */       
/*  521 */       ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();
/*      */ 
/*      */       
/*  524 */       prepareBeanFactory(beanFactory);
/*      */ 
/*      */       
/*      */       try {
/*  528 */         postProcessBeanFactory(beanFactory);
/*      */ 
/*      */         
/*  531 */         invokeBeanFactoryPostProcessors(beanFactory);
/*      */ 
/*      */         
/*  534 */         registerBeanPostProcessors(beanFactory);
/*      */ 
/*      */         
/*  537 */         initMessageSource();
/*      */ 
/*      */         
/*  540 */         initApplicationEventMulticaster();
/*      */ 
/*      */         
/*  543 */         onRefresh();
/*      */ 
/*      */         
/*  546 */         registerListeners();
/*      */ 
/*      */         
/*  549 */         finishBeanFactoryInitialization(beanFactory);
/*      */ 
/*      */         
/*  552 */         finishRefresh();
/*      */       
/*      */       }
/*  555 */       catch (BeansException ex) {
/*  556 */         if (this.logger.isWarnEnabled()) {
/*  557 */           this.logger.warn("Exception encountered during context initialization - cancelling refresh attempt: " + ex);
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  562 */         destroyBeans();
/*      */ 
/*      */         
/*  565 */         cancelRefresh(ex);
/*      */ 
/*      */         
/*  568 */         throw ex;
/*      */       
/*      */       }
/*      */       finally {
/*      */ 
/*      */         
/*  574 */         resetCommonCaches();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void prepareRefresh() {
/*  585 */     this.startupDate = System.currentTimeMillis();
/*  586 */     this.closed.set(false);
/*  587 */     this.active.set(true);
/*      */     
/*  589 */     if (this.logger.isDebugEnabled()) {
/*  590 */       if (this.logger.isTraceEnabled()) {
/*  591 */         this.logger.trace("Refreshing " + this);
/*      */       } else {
/*      */         
/*  594 */         this.logger.debug("Refreshing " + getDisplayName());
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  599 */     initPropertySources();
/*      */ 
/*      */ 
/*      */     
/*  603 */     getEnvironment().validateRequiredProperties();
/*      */ 
/*      */     
/*  606 */     if (this.earlyApplicationListeners == null) {
/*  607 */       this.earlyApplicationListeners = new LinkedHashSet<>(this.applicationListeners);
/*      */     }
/*      */     else {
/*      */       
/*  611 */       this.applicationListeners.clear();
/*  612 */       this.applicationListeners.addAll(this.earlyApplicationListeners);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  617 */     this.earlyApplicationEvents = new LinkedHashSet<>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void initPropertySources() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ConfigurableListableBeanFactory obtainFreshBeanFactory() {
/*  636 */     refreshBeanFactory();
/*  637 */     return getBeanFactory();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
/*  647 */     beanFactory.setBeanClassLoader(getClassLoader());
/*  648 */     beanFactory.setBeanExpressionResolver((BeanExpressionResolver)new StandardBeanExpressionResolver(beanFactory.getBeanClassLoader()));
/*  649 */     beanFactory.addPropertyEditorRegistrar((PropertyEditorRegistrar)new ResourceEditorRegistrar((ResourceLoader)this, (PropertyResolver)getEnvironment()));
/*      */ 
/*      */     
/*  652 */     beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
/*  653 */     beanFactory.ignoreDependencyInterface(EnvironmentAware.class);
/*  654 */     beanFactory.ignoreDependencyInterface(EmbeddedValueResolverAware.class);
/*  655 */     beanFactory.ignoreDependencyInterface(ResourceLoaderAware.class);
/*  656 */     beanFactory.ignoreDependencyInterface(ApplicationEventPublisherAware.class);
/*  657 */     beanFactory.ignoreDependencyInterface(MessageSourceAware.class);
/*  658 */     beanFactory.ignoreDependencyInterface(ApplicationContextAware.class);
/*      */ 
/*      */ 
/*      */     
/*  662 */     beanFactory.registerResolvableDependency(BeanFactory.class, beanFactory);
/*  663 */     beanFactory.registerResolvableDependency(ResourceLoader.class, this);
/*  664 */     beanFactory.registerResolvableDependency(ApplicationEventPublisher.class, this);
/*  665 */     beanFactory.registerResolvableDependency(ApplicationContext.class, this);
/*      */ 
/*      */     
/*  668 */     beanFactory.addBeanPostProcessor((BeanPostProcessor)new ApplicationListenerDetector(this));
/*      */ 
/*      */     
/*  671 */     if (beanFactory.containsBean("loadTimeWeaver")) {
/*  672 */       beanFactory.addBeanPostProcessor((BeanPostProcessor)new LoadTimeWeaverAwareProcessor((BeanFactory)beanFactory));
/*      */       
/*  674 */       beanFactory.setTempClassLoader((ClassLoader)new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
/*      */     } 
/*      */ 
/*      */     
/*  678 */     if (!beanFactory.containsLocalBean("environment")) {
/*  679 */       beanFactory.registerSingleton("environment", getEnvironment());
/*      */     }
/*  681 */     if (!beanFactory.containsLocalBean("systemProperties")) {
/*  682 */       beanFactory.registerSingleton("systemProperties", getEnvironment().getSystemProperties());
/*      */     }
/*  684 */     if (!beanFactory.containsLocalBean("systemEnvironment")) {
/*  685 */       beanFactory.registerSingleton("systemEnvironment", getEnvironment().getSystemEnvironment());
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
/*      */   protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
/*  705 */     PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory, getBeanFactoryPostProcessors());
/*      */ 
/*      */ 
/*      */     
/*  709 */     if (beanFactory.getTempClassLoader() == null && beanFactory.containsBean("loadTimeWeaver")) {
/*  710 */       beanFactory.addBeanPostProcessor((BeanPostProcessor)new LoadTimeWeaverAwareProcessor((BeanFactory)beanFactory));
/*  711 */       beanFactory.setTempClassLoader((ClassLoader)new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
/*  721 */     PostProcessorRegistrationDelegate.registerBeanPostProcessors(beanFactory, this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void initMessageSource() {
/*  729 */     ConfigurableListableBeanFactory beanFactory = getBeanFactory();
/*  730 */     if (beanFactory.containsLocalBean("messageSource")) {
/*  731 */       this.messageSource = (MessageSource)beanFactory.getBean("messageSource", MessageSource.class);
/*      */       
/*  733 */       if (this.parent != null && this.messageSource instanceof HierarchicalMessageSource) {
/*  734 */         HierarchicalMessageSource hms = (HierarchicalMessageSource)this.messageSource;
/*  735 */         if (hms.getParentMessageSource() == null)
/*      */         {
/*      */           
/*  738 */           hms.setParentMessageSource(getInternalParentMessageSource());
/*      */         }
/*      */       } 
/*  741 */       if (this.logger.isTraceEnabled()) {
/*  742 */         this.logger.trace("Using MessageSource [" + this.messageSource + "]");
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/*  747 */       DelegatingMessageSource dms = new DelegatingMessageSource();
/*  748 */       dms.setParentMessageSource(getInternalParentMessageSource());
/*  749 */       this.messageSource = (MessageSource)dms;
/*  750 */       beanFactory.registerSingleton("messageSource", this.messageSource);
/*  751 */       if (this.logger.isTraceEnabled()) {
/*  752 */         this.logger.trace("No 'messageSource' bean, using [" + this.messageSource + "]");
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void initApplicationEventMulticaster() {
/*  763 */     ConfigurableListableBeanFactory beanFactory = getBeanFactory();
/*  764 */     if (beanFactory.containsLocalBean("applicationEventMulticaster")) {
/*  765 */       this
/*  766 */         .applicationEventMulticaster = (ApplicationEventMulticaster)beanFactory.getBean("applicationEventMulticaster", ApplicationEventMulticaster.class);
/*  767 */       if (this.logger.isTraceEnabled()) {
/*  768 */         this.logger.trace("Using ApplicationEventMulticaster [" + this.applicationEventMulticaster + "]");
/*      */       }
/*      */     } else {
/*      */       
/*  772 */       this.applicationEventMulticaster = (ApplicationEventMulticaster)new SimpleApplicationEventMulticaster((BeanFactory)beanFactory);
/*  773 */       beanFactory.registerSingleton("applicationEventMulticaster", this.applicationEventMulticaster);
/*  774 */       if (this.logger.isTraceEnabled()) {
/*  775 */         this.logger.trace("No 'applicationEventMulticaster' bean, using [" + this.applicationEventMulticaster
/*  776 */             .getClass().getSimpleName() + "]");
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void initLifecycleProcessor() {
/*  787 */     ConfigurableListableBeanFactory beanFactory = getBeanFactory();
/*  788 */     if (beanFactory.containsLocalBean("lifecycleProcessor")) {
/*  789 */       this
/*  790 */         .lifecycleProcessor = (LifecycleProcessor)beanFactory.getBean("lifecycleProcessor", LifecycleProcessor.class);
/*  791 */       if (this.logger.isTraceEnabled()) {
/*  792 */         this.logger.trace("Using LifecycleProcessor [" + this.lifecycleProcessor + "]");
/*      */       }
/*      */     } else {
/*      */       
/*  796 */       DefaultLifecycleProcessor defaultProcessor = new DefaultLifecycleProcessor();
/*  797 */       defaultProcessor.setBeanFactory((BeanFactory)beanFactory);
/*  798 */       this.lifecycleProcessor = defaultProcessor;
/*  799 */       beanFactory.registerSingleton("lifecycleProcessor", this.lifecycleProcessor);
/*  800 */       if (this.logger.isTraceEnabled()) {
/*  801 */         this.logger.trace("No 'lifecycleProcessor' bean, using [" + this.lifecycleProcessor
/*  802 */             .getClass().getSimpleName() + "]");
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
/*      */   
/*      */   protected void onRefresh() throws BeansException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void registerListeners() {
/*  824 */     for (ApplicationListener<?> listener : getApplicationListeners()) {
/*  825 */       getApplicationEventMulticaster().addApplicationListener(listener);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  830 */     String[] listenerBeanNames = getBeanNamesForType(ApplicationListener.class, true, false);
/*  831 */     for (String listenerBeanName : listenerBeanNames) {
/*  832 */       getApplicationEventMulticaster().addApplicationListenerBean(listenerBeanName);
/*      */     }
/*      */ 
/*      */     
/*  836 */     Set<ApplicationEvent> earlyEventsToProcess = this.earlyApplicationEvents;
/*  837 */     this.earlyApplicationEvents = null;
/*  838 */     if (earlyEventsToProcess != null) {
/*  839 */       for (ApplicationEvent earlyEvent : earlyEventsToProcess) {
/*  840 */         getApplicationEventMulticaster().multicastEvent(earlyEvent);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {
/*  851 */     if (beanFactory.containsBean("conversionService") && beanFactory
/*  852 */       .isTypeMatch("conversionService", ConversionService.class)) {
/*  853 */       beanFactory.setConversionService((ConversionService)beanFactory
/*  854 */           .getBean("conversionService", ConversionService.class));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  860 */     if (!beanFactory.hasEmbeddedValueResolver()) {
/*  861 */       beanFactory.addEmbeddedValueResolver(strVal -> getEnvironment().resolvePlaceholders(strVal));
/*      */     }
/*      */ 
/*      */     
/*  865 */     String[] weaverAwareNames = beanFactory.getBeanNamesForType(LoadTimeWeaverAware.class, false, false);
/*  866 */     for (String weaverAwareName : weaverAwareNames) {
/*  867 */       getBean(weaverAwareName);
/*      */     }
/*      */ 
/*      */     
/*  871 */     beanFactory.setTempClassLoader(null);
/*      */ 
/*      */     
/*  874 */     beanFactory.freezeConfiguration();
/*      */ 
/*      */     
/*  877 */     beanFactory.preInstantiateSingletons();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void finishRefresh() {
/*  887 */     clearResourceCaches();
/*      */ 
/*      */     
/*  890 */     initLifecycleProcessor();
/*      */ 
/*      */     
/*  893 */     getLifecycleProcessor().onRefresh();
/*      */ 
/*      */     
/*  896 */     publishEvent((ApplicationEvent)new ContextRefreshedEvent((ApplicationContext)this));
/*      */ 
/*      */     
/*  899 */     LiveBeansView.registerApplicationContext(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void cancelRefresh(BeansException ex) {
/*  908 */     this.active.set(false);
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
/*      */   protected void resetCommonCaches() {
/*  922 */     ReflectionUtils.clearCache();
/*  923 */     AnnotationUtils.clearCache();
/*  924 */     ResolvableType.clearCache();
/*  925 */     CachedIntrospectionResults.clearClassLoader(getClassLoader());
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
/*      */   public void registerShutdownHook() {
/*  939 */     if (this.shutdownHook == null) {
/*      */       
/*  941 */       this.shutdownHook = new Thread()
/*      */         {
/*      */           public void run() {
/*  944 */             synchronized (AbstractApplicationContext.this.startupShutdownMonitor) {
/*  945 */               AbstractApplicationContext.this.doClose();
/*      */             } 
/*      */           }
/*      */         };
/*  949 */       Runtime.getRuntime().addShutdownHook(this.shutdownHook);
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
/*      */   @Deprecated
/*      */   public void destroy() {
/*  962 */     close();
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
/*      */   public void close() {
/*  974 */     synchronized (this.startupShutdownMonitor) {
/*  975 */       doClose();
/*      */ 
/*      */       
/*  978 */       if (this.shutdownHook != null) {
/*      */         try {
/*  980 */           Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
/*      */         }
/*  982 */         catch (IllegalStateException illegalStateException) {}
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void doClose() {
/* 1000 */     if (this.active.get() && this.closed.compareAndSet(false, true)) {
/* 1001 */       if (this.logger.isDebugEnabled()) {
/* 1002 */         this.logger.debug("Closing " + this);
/*      */       }
/*      */       
/* 1005 */       LiveBeansView.unregisterApplicationContext(this);
/*      */ 
/*      */       
/*      */       try {
/* 1009 */         publishEvent((ApplicationEvent)new ContextClosedEvent((ApplicationContext)this));
/*      */       }
/* 1011 */       catch (Throwable ex) {
/* 1012 */         this.logger.warn("Exception thrown from ApplicationListener handling ContextClosedEvent", ex);
/*      */       } 
/*      */ 
/*      */       
/* 1016 */       if (this.lifecycleProcessor != null) {
/*      */         try {
/* 1018 */           this.lifecycleProcessor.onClose();
/*      */         }
/* 1020 */         catch (Throwable ex) {
/* 1021 */           this.logger.warn("Exception thrown from LifecycleProcessor on context close", ex);
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/* 1026 */       destroyBeans();
/*      */ 
/*      */       
/* 1029 */       closeBeanFactory();
/*      */ 
/*      */       
/* 1032 */       onClose();
/*      */ 
/*      */       
/* 1035 */       if (this.earlyApplicationListeners != null) {
/* 1036 */         this.applicationListeners.clear();
/* 1037 */         this.applicationListeners.addAll(this.earlyApplicationListeners);
/*      */       } 
/*      */ 
/*      */       
/* 1041 */       this.active.set(false);
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
/*      */   
/*      */   protected void destroyBeans() {
/* 1057 */     getBeanFactory().destroySingletons();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void onClose() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isActive() {
/* 1074 */     return this.active.get();
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
/*      */   protected void assertBeanFactoryActive() {
/* 1087 */     if (!this.active.get()) {
/* 1088 */       if (this.closed.get()) {
/* 1089 */         throw new IllegalStateException(getDisplayName() + " has been closed already");
/*      */       }
/*      */       
/* 1092 */       throw new IllegalStateException(getDisplayName() + " has not been refreshed yet");
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
/*      */   public Object getBean(String name) throws BeansException {
/* 1104 */     assertBeanFactoryActive();
/* 1105 */     return getBeanFactory().getBean(name);
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
/* 1110 */     assertBeanFactoryActive();
/* 1111 */     return (T)getBeanFactory().getBean(name, requiredType);
/*      */   }
/*      */ 
/*      */   
/*      */   public Object getBean(String name, Object... args) throws BeansException {
/* 1116 */     assertBeanFactoryActive();
/* 1117 */     return getBeanFactory().getBean(name, args);
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> T getBean(Class<T> requiredType) throws BeansException {
/* 1122 */     assertBeanFactoryActive();
/* 1123 */     return (T)getBeanFactory().getBean(requiredType);
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> T getBean(Class<T> requiredType, Object... args) throws BeansException {
/* 1128 */     assertBeanFactoryActive();
/* 1129 */     return (T)getBeanFactory().getBean(requiredType, args);
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> ObjectProvider<T> getBeanProvider(Class<T> requiredType) {
/* 1134 */     assertBeanFactoryActive();
/* 1135 */     return getBeanFactory().getBeanProvider(requiredType);
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> ObjectProvider<T> getBeanProvider(ResolvableType requiredType) {
/* 1140 */     assertBeanFactoryActive();
/* 1141 */     return getBeanFactory().getBeanProvider(requiredType);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsBean(String name) {
/* 1146 */     return getBeanFactory().containsBean(name);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
/* 1151 */     assertBeanFactoryActive();
/* 1152 */     return getBeanFactory().isSingleton(name);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
/* 1157 */     assertBeanFactoryActive();
/* 1158 */     return getBeanFactory().isPrototype(name);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException {
/* 1163 */     assertBeanFactoryActive();
/* 1164 */     return getBeanFactory().isTypeMatch(name, typeToMatch);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException {
/* 1169 */     assertBeanFactoryActive();
/* 1170 */     return getBeanFactory().isTypeMatch(name, typeToMatch);
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
/* 1176 */     assertBeanFactoryActive();
/* 1177 */     return getBeanFactory().getType(name);
/*      */   }
/*      */ 
/*      */   
/*      */   public String[] getAliases(String name) {
/* 1182 */     return getBeanFactory().getAliases(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean containsBeanDefinition(String beanName) {
/* 1192 */     return getBeanFactory().containsBeanDefinition(beanName);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getBeanDefinitionCount() {
/* 1197 */     return getBeanFactory().getBeanDefinitionCount();
/*      */   }
/*      */ 
/*      */   
/*      */   public String[] getBeanDefinitionNames() {
/* 1202 */     return getBeanFactory().getBeanDefinitionNames();
/*      */   }
/*      */ 
/*      */   
/*      */   public String[] getBeanNamesForType(ResolvableType type) {
/* 1207 */     assertBeanFactoryActive();
/* 1208 */     return getBeanFactory().getBeanNamesForType(type);
/*      */   }
/*      */ 
/*      */   
/*      */   public String[] getBeanNamesForType(@Nullable Class<?> type) {
/* 1213 */     assertBeanFactoryActive();
/* 1214 */     return getBeanFactory().getBeanNamesForType(type);
/*      */   }
/*      */ 
/*      */   
/*      */   public String[] getBeanNamesForType(@Nullable Class<?> type, boolean includeNonSingletons, boolean allowEagerInit) {
/* 1219 */     assertBeanFactoryActive();
/* 1220 */     return getBeanFactory().getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> Map<String, T> getBeansOfType(@Nullable Class<T> type) throws BeansException {
/* 1225 */     assertBeanFactoryActive();
/* 1226 */     return getBeanFactory().getBeansOfType(type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> Map<String, T> getBeansOfType(@Nullable Class<T> type, boolean includeNonSingletons, boolean allowEagerInit) throws BeansException {
/* 1233 */     assertBeanFactoryActive();
/* 1234 */     return getBeanFactory().getBeansOfType(type, includeNonSingletons, allowEagerInit);
/*      */   }
/*      */ 
/*      */   
/*      */   public String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType) {
/* 1239 */     assertBeanFactoryActive();
/* 1240 */     return getBeanFactory().getBeanNamesForAnnotation(annotationType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) throws BeansException {
/* 1247 */     assertBeanFactoryActive();
/* 1248 */     return getBeanFactory().getBeansWithAnnotation(annotationType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType) throws NoSuchBeanDefinitionException {
/* 1256 */     assertBeanFactoryActive();
/* 1257 */     return (A)getBeanFactory().findAnnotationOnBean(beanName, annotationType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public BeanFactory getParentBeanFactory() {
/* 1268 */     return (BeanFactory)getParent();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsLocalBean(String name) {
/* 1273 */     return getBeanFactory().containsLocalBean(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected BeanFactory getInternalParentBeanFactory() {
/* 1283 */     return (getParent() instanceof ConfigurableApplicationContext) ? (BeanFactory)((ConfigurableApplicationContext)
/* 1284 */       getParent()).getBeanFactory() : (BeanFactory)getParent();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getMessage(String code, @Nullable Object[] args, @Nullable String defaultMessage, Locale locale) {
/* 1294 */     return getMessageSource().getMessage(code, args, defaultMessage, locale);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getMessage(String code, @Nullable Object[] args, Locale locale) throws NoSuchMessageException {
/* 1299 */     return getMessageSource().getMessage(code, args, locale);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
/* 1304 */     return getMessageSource().getMessage(resolvable, locale);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private MessageSource getMessageSource() throws IllegalStateException {
/* 1313 */     if (this.messageSource == null) {
/* 1314 */       throw new IllegalStateException("MessageSource not initialized - call 'refresh' before accessing messages via the context: " + this);
/*      */     }
/*      */     
/* 1317 */     return this.messageSource;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected MessageSource getInternalParentMessageSource() {
/* 1326 */     return (getParent() instanceof AbstractApplicationContext) ? ((AbstractApplicationContext)
/* 1327 */       getParent()).messageSource : (MessageSource)getParent();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Resource[] getResources(String locationPattern) throws IOException {
/* 1337 */     return this.resourcePatternResolver.getResources(locationPattern);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void start() {
/* 1347 */     getLifecycleProcessor().start();
/* 1348 */     publishEvent((ApplicationEvent)new ContextStartedEvent((ApplicationContext)this));
/*      */   }
/*      */ 
/*      */   
/*      */   public void stop() {
/* 1353 */     getLifecycleProcessor().stop();
/* 1354 */     publishEvent((ApplicationEvent)new ContextStoppedEvent((ApplicationContext)this));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isRunning() {
/* 1359 */     return (this.lifecycleProcessor != null && this.lifecycleProcessor.isRunning());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1408 */     StringBuilder sb = new StringBuilder(getDisplayName());
/* 1409 */     sb.append(", started on ").append(new Date(getStartupDate()));
/* 1410 */     ApplicationContext parent = getParent();
/* 1411 */     if (parent != null) {
/* 1412 */       sb.append(", parent: ").append(parent.getDisplayName());
/*      */     }
/* 1414 */     return sb.toString();
/*      */   }
/*      */   
/*      */   protected abstract void refreshBeanFactory() throws BeansException, IllegalStateException;
/*      */   
/*      */   protected abstract void closeBeanFactory();
/*      */   
/*      */   public abstract ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;
/*      */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/support/AbstractApplicationContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */