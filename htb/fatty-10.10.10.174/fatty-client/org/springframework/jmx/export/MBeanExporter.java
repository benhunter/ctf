/*      */ package org.springframework.jmx.export;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import javax.management.DynamicMBean;
/*      */ import javax.management.JMException;
/*      */ import javax.management.MBeanException;
/*      */ import javax.management.MalformedObjectNameException;
/*      */ import javax.management.NotCompliantMBeanException;
/*      */ import javax.management.NotificationListener;
/*      */ import javax.management.ObjectName;
/*      */ import javax.management.StandardMBean;
/*      */ import javax.management.modelmbean.ModelMBean;
/*      */ import javax.management.modelmbean.ModelMBeanInfo;
/*      */ import javax.management.modelmbean.RequiredModelMBean;
/*      */ import org.springframework.aop.TargetSource;
/*      */ import org.springframework.aop.framework.ProxyFactory;
/*      */ import org.springframework.aop.scope.ScopedProxyUtils;
/*      */ import org.springframework.aop.support.AopUtils;
/*      */ import org.springframework.aop.target.LazyInitTargetSource;
/*      */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*      */ import org.springframework.beans.factory.BeanFactory;
/*      */ import org.springframework.beans.factory.BeanFactoryAware;
/*      */ import org.springframework.beans.factory.CannotLoadBeanClassException;
/*      */ import org.springframework.beans.factory.DisposableBean;
/*      */ import org.springframework.beans.factory.InitializingBean;
/*      */ import org.springframework.beans.factory.ListableBeanFactory;
/*      */ import org.springframework.beans.factory.SmartInitializingSingleton;
/*      */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*      */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*      */ import org.springframework.core.Constants;
/*      */ import org.springframework.jmx.export.assembler.AutodetectCapableMBeanInfoAssembler;
/*      */ import org.springframework.jmx.export.assembler.MBeanInfoAssembler;
/*      */ import org.springframework.jmx.export.assembler.SimpleReflectiveMBeanInfoAssembler;
/*      */ import org.springframework.jmx.export.naming.KeyNamingStrategy;
/*      */ import org.springframework.jmx.export.naming.ObjectNamingStrategy;
/*      */ import org.springframework.jmx.export.naming.SelfNaming;
/*      */ import org.springframework.jmx.export.notification.ModelMBeanNotificationPublisher;
/*      */ import org.springframework.jmx.export.notification.NotificationPublisher;
/*      */ import org.springframework.jmx.export.notification.NotificationPublisherAware;
/*      */ import org.springframework.jmx.support.JmxUtils;
/*      */ import org.springframework.jmx.support.MBeanRegistrationSupport;
/*      */ import org.springframework.lang.Nullable;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.CollectionUtils;
/*      */ import org.springframework.util.ObjectUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class MBeanExporter
/*      */   extends MBeanRegistrationSupport
/*      */   implements MBeanExportOperations, BeanClassLoaderAware, BeanFactoryAware, InitializingBean, SmartInitializingSingleton, DisposableBean
/*      */ {
/*      */   public static final int AUTODETECT_NONE = 0;
/*      */   public static final int AUTODETECT_MBEAN = 1;
/*      */   public static final int AUTODETECT_ASSEMBLER = 2;
/*      */   public static final int AUTODETECT_ALL = 3;
/*      */   private static final String WILDCARD = "*";
/*      */   private static final String MR_TYPE_OBJECT_REFERENCE = "ObjectReference";
/*      */   private static final String CONSTANT_PREFIX_AUTODETECT = "AUTODETECT_";
/*  141 */   private static final Constants constants = new Constants(MBeanExporter.class);
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private Map<String, Object> beans;
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private Integer autodetectMode;
/*      */ 
/*      */   
/*      */   private boolean allowEagerInit = false;
/*      */ 
/*      */   
/*  155 */   private MBeanInfoAssembler assembler = (MBeanInfoAssembler)new SimpleReflectiveMBeanInfoAssembler();
/*      */ 
/*      */   
/*  158 */   private ObjectNamingStrategy namingStrategy = (ObjectNamingStrategy)new KeyNamingStrategy();
/*      */ 
/*      */   
/*      */   private boolean ensureUniqueRuntimeObjectNames = true;
/*      */ 
/*      */   
/*      */   private boolean exposeManagedResourceClassLoader = true;
/*      */ 
/*      */   
/*  167 */   private Set<String> excludedBeans = new HashSet<>();
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private MBeanExporterListener[] listeners;
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private NotificationListenerBean[] notificationListeners;
/*      */ 
/*      */   
/*  178 */   private final Map<NotificationListenerBean, ObjectName[]> registeredNotificationListeners = (Map)new LinkedHashMap<>();
/*      */ 
/*      */   
/*      */   @Nullable
/*  182 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*      */ 
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
/*      */   private ListableBeanFactory beanFactory;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBeans(Map<String, Object> beans) {
/*  207 */     this.beans = beans;
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
/*      */   public void setAutodetect(boolean autodetect) {
/*  221 */     this.autodetectMode = Integer.valueOf(autodetect ? 3 : 0);
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
/*      */   public void setAutodetectMode(int autodetectMode) {
/*  235 */     if (!constants.getValues("AUTODETECT_").contains(Integer.valueOf(autodetectMode))) {
/*  236 */       throw new IllegalArgumentException("Only values of autodetect constants allowed");
/*      */     }
/*  238 */     this.autodetectMode = Integer.valueOf(autodetectMode);
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
/*      */   public void setAutodetectModeName(String constantName) {
/*  252 */     if (!constantName.startsWith("AUTODETECT_")) {
/*  253 */       throw new IllegalArgumentException("Only autodetect constants allowed");
/*      */     }
/*  255 */     this.autodetectMode = (Integer)constants.asNumber(constantName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAllowEagerInit(boolean allowEagerInit) {
/*  266 */     this.allowEagerInit = allowEagerInit;
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
/*      */   public void setAssembler(MBeanInfoAssembler assembler) {
/*  281 */     this.assembler = assembler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNamingStrategy(ObjectNamingStrategy namingStrategy) {
/*  291 */     this.namingStrategy = namingStrategy;
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
/*      */   public void setEnsureUniqueRuntimeObjectNames(boolean ensureUniqueRuntimeObjectNames) {
/*  304 */     this.ensureUniqueRuntimeObjectNames = ensureUniqueRuntimeObjectNames;
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
/*      */   public void setExposeManagedResourceClassLoader(boolean exposeManagedResourceClassLoader) {
/*  316 */     this.exposeManagedResourceClassLoader = exposeManagedResourceClassLoader;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setExcludedBeans(String... excludedBeans) {
/*  323 */     this.excludedBeans.clear();
/*  324 */     Collections.addAll(this.excludedBeans, excludedBeans);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addExcludedBean(String excludedBean) {
/*  331 */     Assert.notNull(excludedBean, "ExcludedBean must not be null");
/*  332 */     this.excludedBeans.add(excludedBean);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setListeners(MBeanExporterListener... listeners) {
/*  341 */     this.listeners = listeners;
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
/*      */   public void setNotificationListeners(NotificationListenerBean... notificationListeners) {
/*  353 */     this.notificationListeners = notificationListeners;
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
/*      */   public void setNotificationListenerMappings(Map<?, ? extends NotificationListener> listeners) {
/*  371 */     Assert.notNull(listeners, "'listeners' must not be null");
/*      */     
/*  373 */     List<NotificationListenerBean> notificationListeners = new ArrayList<>(listeners.size());
/*      */     
/*  375 */     listeners.forEach((key, listener) -> {
/*      */           NotificationListenerBean bean = new NotificationListenerBean(listener);
/*      */ 
/*      */           
/*      */           if (key != null && !"*".equals(key)) {
/*      */             bean.setMappedObjectName(key);
/*      */           }
/*      */           
/*      */           notificationListeners.add(bean);
/*      */         });
/*      */     
/*  386 */     this.notificationListeners = notificationListeners.<NotificationListenerBean>toArray(new NotificationListenerBean[0]);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBeanClassLoader(ClassLoader classLoader) {
/*  391 */     this.beanClassLoader = classLoader;
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
/*      */   public void setBeanFactory(BeanFactory beanFactory) {
/*  404 */     if (beanFactory instanceof ListableBeanFactory) {
/*  405 */       this.beanFactory = (ListableBeanFactory)beanFactory;
/*      */     } else {
/*      */       
/*  408 */       this.logger.debug("MBeanExporter not running in a ListableBeanFactory: autodetection of MBeans not available.");
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
/*      */   public void afterPropertiesSet() {
/*  421 */     if (this.server == null) {
/*  422 */       this.server = JmxUtils.locateMBeanServer();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void afterSingletonsInstantiated() {
/*      */     try {
/*  433 */       this.logger.debug("Registering beans for JMX exposure on startup");
/*  434 */       registerBeans();
/*  435 */       registerNotificationListeners();
/*      */     }
/*  437 */     catch (RuntimeException ex) {
/*      */       
/*  439 */       unregisterNotificationListeners();
/*  440 */       unregisterBeans();
/*  441 */       throw ex;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void destroy() {
/*  451 */     this.logger.debug("Unregistering JMX-exposed beans on shutdown");
/*  452 */     unregisterNotificationListeners();
/*  453 */     unregisterBeans();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectName registerManagedResource(Object managedResource) throws MBeanExportException {
/*      */     ObjectName objectName;
/*  463 */     Assert.notNull(managedResource, "Managed resource must not be null");
/*      */     
/*      */     try {
/*  466 */       objectName = getObjectName(managedResource, (String)null);
/*  467 */       if (this.ensureUniqueRuntimeObjectNames) {
/*  468 */         objectName = JmxUtils.appendIdentityToObjectName(objectName, managedResource);
/*      */       }
/*      */     }
/*  471 */     catch (Throwable ex) {
/*  472 */       throw new MBeanExportException("Unable to generate ObjectName for MBean [" + managedResource + "]", ex);
/*      */     } 
/*  474 */     registerManagedResource(managedResource, objectName);
/*  475 */     return objectName;
/*      */   }
/*      */ 
/*      */   
/*      */   public void registerManagedResource(Object managedResource, ObjectName objectName) throws MBeanExportException {
/*  480 */     Assert.notNull(managedResource, "Managed resource must not be null");
/*  481 */     Assert.notNull(objectName, "ObjectName must not be null");
/*      */     try {
/*  483 */       if (isMBean(managedResource.getClass())) {
/*  484 */         doRegister(managedResource, objectName);
/*      */       } else {
/*      */         
/*  487 */         ModelMBean mbean = createAndConfigureMBean(managedResource, managedResource.getClass().getName());
/*  488 */         doRegister(mbean, objectName);
/*  489 */         injectNotificationPublisherIfNecessary(managedResource, mbean, objectName);
/*      */       }
/*      */     
/*  492 */     } catch (JMException ex) {
/*  493 */       throw new UnableToRegisterMBeanException("Unable to register MBean [" + managedResource + "] with object name [" + objectName + "]", ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void unregisterManagedResource(ObjectName objectName) {
/*  500 */     Assert.notNull(objectName, "ObjectName must not be null");
/*  501 */     doUnregister(objectName);
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
/*      */   protected void registerBeans() {
/*  524 */     if (this.beans == null) {
/*  525 */       this.beans = new HashMap<>();
/*      */       
/*  527 */       if (this.autodetectMode == null) {
/*  528 */         this.autodetectMode = Integer.valueOf(3);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  533 */     int mode = (this.autodetectMode != null) ? this.autodetectMode.intValue() : 0;
/*  534 */     if (mode != 0) {
/*  535 */       if (this.beanFactory == null) {
/*  536 */         throw new MBeanExportException("Cannot autodetect MBeans if not running in a BeanFactory");
/*      */       }
/*  538 */       if (mode == 1 || mode == 3) {
/*      */         
/*  540 */         this.logger.debug("Autodetecting user-defined JMX MBeans");
/*  541 */         autodetect(this.beans, (beanClass, beanName) -> isMBean(beanClass));
/*      */       } 
/*      */       
/*  544 */       if ((mode == 2 || mode == 3) && this.assembler instanceof AutodetectCapableMBeanInfoAssembler)
/*      */       {
/*  546 */         autodetect(this.beans, (AutodetectCapableMBeanInfoAssembler)this.assembler::includeBean);
/*      */       }
/*      */     } 
/*      */     
/*  550 */     if (!this.beans.isEmpty()) {
/*  551 */       this.beans.forEach((beanName, instance) -> registerBeanNameOrInstance(instance, beanName));
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
/*      */   protected boolean isBeanDefinitionLazyInit(ListableBeanFactory beanFactory, String beanName) {
/*  563 */     return (beanFactory instanceof ConfigurableListableBeanFactory && beanFactory.containsBeanDefinition(beanName) && ((ConfigurableListableBeanFactory)beanFactory)
/*  564 */       .getBeanDefinition(beanName).isLazyInit());
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
/*      */   protected ObjectName registerBeanNameOrInstance(Object mapValue, String beanKey) throws MBeanExportException {
/*      */     try {
/*  589 */       if (mapValue instanceof String) {
/*      */         
/*  591 */         if (this.beanFactory == null) {
/*  592 */           throw new MBeanExportException("Cannot resolve bean names if not running in a BeanFactory");
/*      */         }
/*  594 */         String beanName = (String)mapValue;
/*  595 */         if (isBeanDefinitionLazyInit(this.beanFactory, beanName)) {
/*  596 */           ObjectName objectName1 = registerLazyInit(beanName, beanKey);
/*  597 */           replaceNotificationListenerBeanNameKeysIfNecessary(beanName, objectName1);
/*  598 */           return objectName1;
/*      */         } 
/*      */         
/*  601 */         Object bean = this.beanFactory.getBean(beanName);
/*  602 */         ObjectName objectName = registerBeanInstance(bean, beanKey);
/*  603 */         replaceNotificationListenerBeanNameKeysIfNecessary(beanName, objectName);
/*  604 */         return objectName;
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  609 */       if (this.beanFactory != null) {
/*      */         
/*  611 */         Map<String, ?> beansOfSameType = this.beanFactory.getBeansOfType(mapValue.getClass(), false, this.allowEagerInit);
/*  612 */         for (Map.Entry<String, ?> entry : beansOfSameType.entrySet()) {
/*  613 */           if (entry.getValue() == mapValue) {
/*  614 */             String beanName = entry.getKey();
/*  615 */             ObjectName objectName = registerBeanInstance(mapValue, beanKey);
/*  616 */             replaceNotificationListenerBeanNameKeysIfNecessary(beanName, objectName);
/*  617 */             return objectName;
/*      */           } 
/*      */         } 
/*      */       } 
/*  621 */       return registerBeanInstance(mapValue, beanKey);
/*      */     
/*      */     }
/*  624 */     catch (Throwable ex) {
/*  625 */       throw new UnableToRegisterMBeanException("Unable to register MBean [" + mapValue + "] with key '" + beanKey + "'", ex);
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
/*      */   private void replaceNotificationListenerBeanNameKeysIfNecessary(String beanName, ObjectName objectName) {
/*  638 */     if (this.notificationListeners != null) {
/*  639 */       for (NotificationListenerBean notificationListener : this.notificationListeners) {
/*  640 */         notificationListener.replaceObjectName(beanName, objectName);
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
/*      */   private ObjectName registerBeanInstance(Object bean, String beanKey) throws JMException {
/*  654 */     ObjectName objectName = getObjectName(bean, beanKey);
/*  655 */     Object mbeanToExpose = null;
/*  656 */     if (isMBean(bean.getClass())) {
/*  657 */       mbeanToExpose = bean;
/*      */     } else {
/*      */       
/*  660 */       DynamicMBean adaptedBean = adaptMBeanIfPossible(bean);
/*  661 */       if (adaptedBean != null) {
/*  662 */         mbeanToExpose = adaptedBean;
/*      */       }
/*      */     } 
/*      */     
/*  666 */     if (mbeanToExpose != null) {
/*  667 */       if (this.logger.isDebugEnabled()) {
/*  668 */         this.logger.debug("Located MBean '" + beanKey + "': registering with JMX server as MBean [" + objectName + "]");
/*      */       }
/*      */       
/*  671 */       doRegister(mbeanToExpose, objectName);
/*      */     } else {
/*      */       
/*  674 */       if (this.logger.isDebugEnabled()) {
/*  675 */         this.logger.debug("Located managed bean '" + beanKey + "': registering with JMX server as MBean [" + objectName + "]");
/*      */       }
/*      */       
/*  678 */       ModelMBean mbean = createAndConfigureMBean(bean, beanKey);
/*  679 */       doRegister(mbean, objectName);
/*  680 */       injectNotificationPublisherIfNecessary(bean, mbean, objectName);
/*      */     } 
/*      */     
/*  683 */     return objectName;
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
/*      */   private ObjectName registerLazyInit(String beanName, String beanKey) throws JMException {
/*  695 */     Assert.state((this.beanFactory != null), "No BeanFactory set");
/*      */     
/*  697 */     ProxyFactory proxyFactory = new ProxyFactory();
/*  698 */     proxyFactory.setProxyTargetClass(true);
/*  699 */     proxyFactory.setFrozen(true);
/*      */     
/*  701 */     if (isMBean(this.beanFactory.getType(beanName))) {
/*      */       
/*  703 */       LazyInitTargetSource lazyInitTargetSource = new LazyInitTargetSource();
/*  704 */       lazyInitTargetSource.setTargetBeanName(beanName);
/*  705 */       lazyInitTargetSource.setBeanFactory((BeanFactory)this.beanFactory);
/*  706 */       proxyFactory.setTargetSource((TargetSource)lazyInitTargetSource);
/*      */       
/*  708 */       Object object = proxyFactory.getProxy(this.beanClassLoader);
/*  709 */       ObjectName objectName1 = getObjectName(object, beanKey);
/*  710 */       if (this.logger.isDebugEnabled()) {
/*  711 */         this.logger.debug("Located MBean '" + beanKey + "': registering with JMX server as lazy-init MBean [" + objectName1 + "]");
/*      */       }
/*      */       
/*  714 */       doRegister(object, objectName1);
/*  715 */       return objectName1;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  720 */     NotificationPublisherAwareLazyTargetSource targetSource = new NotificationPublisherAwareLazyTargetSource();
/*  721 */     targetSource.setTargetBeanName(beanName);
/*  722 */     targetSource.setBeanFactory((BeanFactory)this.beanFactory);
/*  723 */     proxyFactory.setTargetSource((TargetSource)targetSource);
/*      */     
/*  725 */     Object proxy = proxyFactory.getProxy(this.beanClassLoader);
/*  726 */     ObjectName objectName = getObjectName(proxy, beanKey);
/*  727 */     if (this.logger.isDebugEnabled()) {
/*  728 */       this.logger.debug("Located simple bean '" + beanKey + "': registering with JMX server as lazy-init MBean [" + objectName + "]");
/*      */     }
/*      */     
/*  731 */     ModelMBean mbean = createAndConfigureMBean(proxy, beanKey);
/*  732 */     targetSource.setModelMBean(mbean);
/*  733 */     targetSource.setObjectName(objectName);
/*  734 */     doRegister(mbean, objectName);
/*  735 */     return objectName;
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
/*      */   protected ObjectName getObjectName(Object bean, @Nullable String beanKey) throws MalformedObjectNameException {
/*  751 */     if (bean instanceof SelfNaming) {
/*  752 */       return ((SelfNaming)bean).getObjectName();
/*      */     }
/*      */     
/*  755 */     return this.namingStrategy.getObjectName(bean, beanKey);
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
/*      */   protected boolean isMBean(@Nullable Class<?> beanClass) {
/*  770 */     return JmxUtils.isMBean(beanClass);
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
/*      */   @Nullable
/*      */   protected DynamicMBean adaptMBeanIfPossible(Object bean) throws JMException {
/*  784 */     Class<?> targetClass = AopUtils.getTargetClass(bean);
/*  785 */     if (targetClass != bean.getClass()) {
/*  786 */       Class<?> ifc = JmxUtils.getMXBeanInterface(targetClass);
/*  787 */       if (ifc != null) {
/*  788 */         if (!ifc.isInstance(bean)) {
/*  789 */           throw new NotCompliantMBeanException("Managed bean [" + bean + "] has a target class with an MXBean interface but does not expose it in the proxy");
/*      */         }
/*      */         
/*  792 */         return new StandardMBean((T)bean, (Class)ifc, true);
/*      */       } 
/*      */       
/*  795 */       ifc = JmxUtils.getMBeanInterface(targetClass);
/*  796 */       if (ifc != null) {
/*  797 */         if (!ifc.isInstance(bean)) {
/*  798 */           throw new NotCompliantMBeanException("Managed bean [" + bean + "] has a target class with an MBean interface but does not expose it in the proxy");
/*      */         }
/*      */         
/*  801 */         return new StandardMBean((T)bean, (Class)ifc);
/*      */       } 
/*      */     } 
/*      */     
/*  805 */     return null;
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
/*      */   protected ModelMBean createAndConfigureMBean(Object managedResource, String beanKey) throws MBeanExportException {
/*      */     try {
/*  819 */       ModelMBean mbean = createModelMBean();
/*  820 */       mbean.setModelMBeanInfo(getMBeanInfo(managedResource, beanKey));
/*  821 */       mbean.setManagedResource(managedResource, "ObjectReference");
/*  822 */       return mbean;
/*      */     }
/*  824 */     catch (Throwable ex) {
/*  825 */       throw new MBeanExportException("Could not create ModelMBean for managed resource [" + managedResource + "] with key '" + beanKey + "'", ex);
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
/*      */   protected ModelMBean createModelMBean() throws MBeanException {
/*  839 */     return this.exposeManagedResourceClassLoader ? new SpringModelMBean() : new RequiredModelMBean();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ModelMBeanInfo getMBeanInfo(Object managedBean, String beanKey) throws JMException {
/*  847 */     ModelMBeanInfo info = this.assembler.getMBeanInfo(managedBean, beanKey);
/*  848 */     if (this.logger.isInfoEnabled() && ObjectUtils.isEmpty((Object[])info.getAttributes()) && 
/*  849 */       ObjectUtils.isEmpty((Object[])info.getOperations())) {
/*  850 */       this.logger.info("Bean with key '" + beanKey + "' has been registered as an MBean but has no exposed attributes or operations");
/*      */     }
/*      */     
/*  853 */     return info;
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
/*      */   private void autodetect(Map<String, Object> beans, AutodetectCallback callback) {
/*  869 */     Assert.state((this.beanFactory != null), "No BeanFactory set");
/*  870 */     Set<String> beanNames = new LinkedHashSet<>(this.beanFactory.getBeanDefinitionCount());
/*  871 */     Collections.addAll(beanNames, this.beanFactory.getBeanDefinitionNames());
/*  872 */     if (this.beanFactory instanceof ConfigurableBeanFactory) {
/*  873 */       Collections.addAll(beanNames, ((ConfigurableBeanFactory)this.beanFactory).getSingletonNames());
/*      */     }
/*      */     
/*  876 */     for (String beanName : beanNames) {
/*  877 */       if (!isExcluded(beanName) && !isBeanDefinitionAbstract(this.beanFactory, beanName)) {
/*      */         try {
/*  879 */           Class<?> beanClass = this.beanFactory.getType(beanName);
/*  880 */           if (beanClass != null && callback.include(beanClass, beanName)) {
/*  881 */             boolean lazyInit = isBeanDefinitionLazyInit(this.beanFactory, beanName);
/*  882 */             Object beanInstance = null;
/*  883 */             if (!lazyInit) {
/*  884 */               beanInstance = this.beanFactory.getBean(beanName);
/*  885 */               if (!beanClass.isInstance(beanInstance)) {
/*      */                 continue;
/*      */               }
/*      */             } 
/*  889 */             if (!ScopedProxyUtils.isScopedTarget(beanName) && !beans.containsValue(beanName) && (beanInstance == null || 
/*      */               
/*  891 */               !CollectionUtils.containsInstance(beans.values(), beanInstance))) {
/*      */               
/*  893 */               beans.put(beanName, (beanInstance != null) ? beanInstance : beanName);
/*  894 */               if (this.logger.isDebugEnabled()) {
/*  895 */                 this.logger.debug("Bean with name '" + beanName + "' has been autodetected for JMX exposure");
/*      */               }
/*      */               continue;
/*      */             } 
/*  899 */             if (this.logger.isTraceEnabled()) {
/*  900 */               this.logger.trace("Bean with name '" + beanName + "' is already registered for JMX exposure");
/*      */             }
/*      */           }
/*      */         
/*      */         }
/*  905 */         catch (CannotLoadBeanClassException ex) {
/*  906 */           if (this.allowEagerInit) {
/*  907 */             throw ex;
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isExcluded(String beanName) {
/*  919 */     return (this.excludedBeans.contains(beanName) || (beanName
/*  920 */       .startsWith("&") && this.excludedBeans
/*  921 */       .contains(beanName.substring("&".length()))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isBeanDefinitionAbstract(ListableBeanFactory beanFactory, String beanName) {
/*  928 */     return (beanFactory instanceof ConfigurableListableBeanFactory && beanFactory.containsBeanDefinition(beanName) && ((ConfigurableListableBeanFactory)beanFactory)
/*  929 */       .getBeanDefinition(beanName).isAbstract());
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
/*      */   private void injectNotificationPublisherIfNecessary(Object managedResource, @Nullable ModelMBean modelMBean, @Nullable ObjectName objectName) {
/*  944 */     if (managedResource instanceof NotificationPublisherAware && modelMBean != null && objectName != null) {
/*  945 */       ((NotificationPublisherAware)managedResource).setNotificationPublisher((NotificationPublisher)new ModelMBeanNotificationPublisher(modelMBean, objectName, managedResource));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void registerNotificationListeners() throws MBeanExportException {
/*  955 */     if (this.notificationListeners != null) {
/*  956 */       Assert.state((this.server != null), "No MBeanServer available");
/*  957 */       for (NotificationListenerBean bean : this.notificationListeners) {
/*      */         try {
/*  959 */           ObjectName[] mappedObjectNames = bean.getResolvedObjectNames();
/*  960 */           if (mappedObjectNames == null)
/*      */           {
/*  962 */             mappedObjectNames = getRegisteredObjectNames();
/*      */           }
/*  964 */           if (this.registeredNotificationListeners.put(bean, mappedObjectNames) == null) {
/*  965 */             for (ObjectName mappedObjectName : mappedObjectNames) {
/*  966 */               this.server.addNotificationListener(mappedObjectName, bean.getNotificationListener(), bean
/*  967 */                   .getNotificationFilter(), bean.getHandback());
/*      */             }
/*      */           }
/*      */         }
/*  971 */         catch (Throwable ex) {
/*  972 */           throw new MBeanExportException("Unable to register NotificationListener", ex);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void unregisterNotificationListeners() {
/*  983 */     if (this.server != null) {
/*  984 */       this.registeredNotificationListeners.forEach((bean, mappedObjectNames) -> {
/*      */ 
/*      */             
/*      */             for (ObjectName mappedObjectName : mappedObjectNames) {
/*      */               try {
/*      */                 this.server.removeNotificationListener(mappedObjectName, bean.getNotificationListener(), bean.getNotificationFilter(), bean.getHandback());
/*  990 */               } catch (Throwable ex) {
/*      */                 if (this.logger.isDebugEnabled()) {
/*      */                   this.logger.debug("Unable to unregister NotificationListener", ex);
/*      */                 }
/*      */               } 
/*      */             } 
/*      */           });
/*      */     }
/*  998 */     this.registeredNotificationListeners.clear();
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
/*      */   protected void onRegister(ObjectName objectName) {
/* 1013 */     notifyListenersOfRegistration(objectName);
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
/*      */   protected void onUnregister(ObjectName objectName) {
/* 1028 */     notifyListenersOfUnregistration(objectName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void notifyListenersOfRegistration(ObjectName objectName) {
/* 1037 */     if (this.listeners != null) {
/* 1038 */       for (MBeanExporterListener listener : this.listeners) {
/* 1039 */         listener.mbeanRegistered(objectName);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void notifyListenersOfUnregistration(ObjectName objectName) {
/* 1049 */     if (this.listeners != null) {
/* 1050 */       for (MBeanExporterListener listener : this.listeners) {
/* 1051 */         listener.mbeanUnregistered(objectName);
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
/*      */   private class NotificationPublisherAwareLazyTargetSource
/*      */     extends LazyInitTargetSource
/*      */   {
/*      */     @Nullable
/*      */     private ModelMBean modelMBean;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     private ObjectName objectName;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private NotificationPublisherAwareLazyTargetSource() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setModelMBean(ModelMBean modelMBean) {
/* 1092 */       this.modelMBean = modelMBean;
/*      */     }
/*      */     
/*      */     public void setObjectName(ObjectName objectName) {
/* 1096 */       this.objectName = objectName;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public Object getTarget() {
/*      */       try {
/* 1103 */         return super.getTarget();
/*      */       }
/* 1105 */       catch (RuntimeException ex) {
/* 1106 */         if (this.logger.isInfoEnabled()) {
/* 1107 */           this.logger.info("Failed to retrieve target for JMX-exposed bean [" + this.objectName + "]: " + ex);
/*      */         }
/* 1109 */         throw ex;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     protected void postProcessTargetObject(Object targetObject) {
/* 1115 */       MBeanExporter.this.injectNotificationPublisherIfNecessary(targetObject, this.modelMBean, this.objectName);
/*      */     }
/*      */   }
/*      */   
/*      */   @FunctionalInterface
/*      */   private static interface AutodetectCallback {
/*      */     boolean include(Class<?> param1Class, String param1String);
/*      */   }
/*      */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/export/MBeanExporter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */