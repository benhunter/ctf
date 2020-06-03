/*      */ package org.springframework.beans.factory.support;
/*      */ 
/*      */ import java.beans.PropertyDescriptor;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import java.util.TreeSet;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.function.Supplier;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.springframework.beans.BeanUtils;
/*      */ import org.springframework.beans.BeanWrapper;
/*      */ import org.springframework.beans.BeanWrapperImpl;
/*      */ import org.springframework.beans.BeansException;
/*      */ import org.springframework.beans.MutablePropertyValues;
/*      */ import org.springframework.beans.PropertyAccessorUtils;
/*      */ import org.springframework.beans.PropertyValue;
/*      */ import org.springframework.beans.PropertyValues;
/*      */ import org.springframework.beans.TypeConverter;
/*      */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*      */ import org.springframework.beans.factory.BeanCreationException;
/*      */ import org.springframework.beans.factory.BeanCurrentlyInCreationException;
/*      */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*      */ import org.springframework.beans.factory.BeanFactory;
/*      */ import org.springframework.beans.factory.BeanFactoryAware;
/*      */ import org.springframework.beans.factory.BeanNameAware;
/*      */ import org.springframework.beans.factory.FactoryBean;
/*      */ import org.springframework.beans.factory.InitializingBean;
/*      */ import org.springframework.beans.factory.InjectionPoint;
/*      */ import org.springframework.beans.factory.UnsatisfiedDependencyException;
/*      */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
/*      */ import org.springframework.beans.factory.config.BeanDefinition;
/*      */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*      */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*      */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*      */ import org.springframework.beans.factory.config.DependencyDescriptor;
/*      */ import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
/*      */ import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
/*      */ import org.springframework.beans.factory.config.TypedStringValue;
/*      */ import org.springframework.core.DefaultParameterNameDiscoverer;
/*      */ import org.springframework.core.GenericTypeResolver;
/*      */ import org.springframework.core.MethodParameter;
/*      */ import org.springframework.core.NamedThreadLocal;
/*      */ import org.springframework.core.ParameterNameDiscoverer;
/*      */ import org.springframework.core.PriorityOrdered;
/*      */ import org.springframework.core.ResolvableType;
/*      */ import org.springframework.lang.Nullable;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.ObjectUtils;
/*      */ import org.springframework.util.ReflectionUtils;
/*      */ import org.springframework.util.StringUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class AbstractAutowireCapableBeanFactory
/*      */   extends AbstractBeanFactory
/*      */   implements AutowireCapableBeanFactory
/*      */ {
/*  125 */   private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();
/*      */   
/*      */   @Nullable
/*  128 */   private ParameterNameDiscoverer parameterNameDiscoverer = (ParameterNameDiscoverer)new DefaultParameterNameDiscoverer();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean allowCircularReferences = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean allowRawInjectionDespiteWrapping = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  144 */   private final Set<Class<?>> ignoredDependencyTypes = new HashSet<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  150 */   private final Set<Class<?>> ignoredDependencyInterfaces = new HashSet<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  156 */   private final NamedThreadLocal<String> currentlyCreatedBean = new NamedThreadLocal("Currently created bean");
/*      */ 
/*      */   
/*  159 */   private final ConcurrentMap<String, BeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<>();
/*      */ 
/*      */   
/*  162 */   private final ConcurrentMap<Class<?>, Method[]> factoryMethodCandidateCache = (ConcurrentMap)new ConcurrentHashMap<>();
/*      */ 
/*      */   
/*  165 */   private final ConcurrentMap<Class<?>, PropertyDescriptor[]> filteredPropertyDescriptorsCache = (ConcurrentMap)new ConcurrentHashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AbstractAutowireCapableBeanFactory() {
/*  174 */     ignoreDependencyInterface(BeanNameAware.class);
/*  175 */     ignoreDependencyInterface(BeanFactoryAware.class);
/*  176 */     ignoreDependencyInterface(BeanClassLoaderAware.class);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AbstractAutowireCapableBeanFactory(@Nullable BeanFactory parentBeanFactory) {
/*  184 */     this();
/*  185 */     setParentBeanFactory(parentBeanFactory);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
/*  195 */     this.instantiationStrategy = instantiationStrategy;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected InstantiationStrategy getInstantiationStrategy() {
/*  202 */     return this.instantiationStrategy;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setParameterNameDiscoverer(@Nullable ParameterNameDiscoverer parameterNameDiscoverer) {
/*  211 */     this.parameterNameDiscoverer = parameterNameDiscoverer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected ParameterNameDiscoverer getParameterNameDiscoverer() {
/*  220 */     return this.parameterNameDiscoverer;
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
/*      */   public void setAllowCircularReferences(boolean allowCircularReferences) {
/*  237 */     this.allowCircularReferences = allowCircularReferences;
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
/*      */   public void setAllowRawInjectionDespiteWrapping(boolean allowRawInjectionDespiteWrapping) {
/*  255 */     this.allowRawInjectionDespiteWrapping = allowRawInjectionDespiteWrapping;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void ignoreDependencyType(Class<?> type) {
/*  263 */     this.ignoredDependencyTypes.add(type);
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
/*      */   public void ignoreDependencyInterface(Class<?> ifc) {
/*  277 */     this.ignoredDependencyInterfaces.add(ifc);
/*      */   }
/*      */ 
/*      */   
/*      */   public void copyConfigurationFrom(ConfigurableBeanFactory otherFactory) {
/*  282 */     super.copyConfigurationFrom(otherFactory);
/*  283 */     if (otherFactory instanceof AbstractAutowireCapableBeanFactory) {
/*  284 */       AbstractAutowireCapableBeanFactory otherAutowireFactory = (AbstractAutowireCapableBeanFactory)otherFactory;
/*      */       
/*  286 */       this.instantiationStrategy = otherAutowireFactory.instantiationStrategy;
/*  287 */       this.allowCircularReferences = otherAutowireFactory.allowCircularReferences;
/*  288 */       this.ignoredDependencyTypes.addAll(otherAutowireFactory.ignoredDependencyTypes);
/*  289 */       this.ignoredDependencyInterfaces.addAll(otherAutowireFactory.ignoredDependencyInterfaces);
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
/*      */   public <T> T createBean(Class<T> beanClass) throws BeansException {
/*  302 */     RootBeanDefinition bd = new RootBeanDefinition(beanClass);
/*  303 */     bd.setScope("prototype");
/*  304 */     bd.allowCaching = ClassUtils.isCacheSafe(beanClass, getBeanClassLoader());
/*  305 */     return (T)createBean(beanClass.getName(), bd, (Object[])null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void autowireBean(Object existingBean) {
/*  311 */     RootBeanDefinition bd = new RootBeanDefinition(ClassUtils.getUserClass(existingBean));
/*  312 */     bd.setScope("prototype");
/*  313 */     bd.allowCaching = ClassUtils.isCacheSafe(bd.getBeanClass(), getBeanClassLoader());
/*  314 */     BeanWrapperImpl beanWrapperImpl = new BeanWrapperImpl(existingBean);
/*  315 */     initBeanWrapper((BeanWrapper)beanWrapperImpl);
/*  316 */     populateBean(bd.getBeanClass().getName(), bd, (BeanWrapper)beanWrapperImpl);
/*      */   }
/*      */ 
/*      */   
/*      */   public Object configureBean(Object existingBean, String beanName) throws BeansException {
/*  321 */     markBeanAsCreated(beanName);
/*  322 */     BeanDefinition mbd = getMergedBeanDefinition(beanName);
/*  323 */     RootBeanDefinition bd = null;
/*  324 */     if (mbd instanceof RootBeanDefinition) {
/*  325 */       RootBeanDefinition rbd = (RootBeanDefinition)mbd;
/*  326 */       bd = rbd.isPrototype() ? rbd : rbd.cloneBeanDefinition();
/*      */     } 
/*  328 */     if (bd == null) {
/*  329 */       bd = new RootBeanDefinition(mbd);
/*      */     }
/*  331 */     if (!bd.isPrototype()) {
/*  332 */       bd.setScope("prototype");
/*  333 */       bd.allowCaching = ClassUtils.isCacheSafe(ClassUtils.getUserClass(existingBean), getBeanClassLoader());
/*      */     } 
/*  335 */     BeanWrapperImpl beanWrapperImpl = new BeanWrapperImpl(existingBean);
/*  336 */     initBeanWrapper((BeanWrapper)beanWrapperImpl);
/*  337 */     populateBean(beanName, bd, (BeanWrapper)beanWrapperImpl);
/*  338 */     return initializeBean(beanName, existingBean, bd);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object createBean(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws BeansException {
/*  349 */     RootBeanDefinition bd = new RootBeanDefinition(beanClass, autowireMode, dependencyCheck);
/*  350 */     bd.setScope("prototype");
/*  351 */     return createBean(beanClass.getName(), bd, (Object[])null);
/*      */   }
/*      */ 
/*      */   
/*      */   public Object autowire(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws BeansException {
/*      */     Object bean;
/*  357 */     RootBeanDefinition bd = new RootBeanDefinition(beanClass, autowireMode, dependencyCheck);
/*  358 */     bd.setScope("prototype");
/*  359 */     if (bd.getResolvedAutowireMode() == 3) {
/*  360 */       return autowireConstructor(beanClass.getName(), bd, (Constructor<?>[])null, (Object[])null).getWrappedInstance();
/*      */     }
/*      */ 
/*      */     
/*  364 */     AbstractAutowireCapableBeanFactory abstractAutowireCapableBeanFactory = this;
/*  365 */     if (System.getSecurityManager() != null) {
/*  366 */       bean = AccessController.doPrivileged(() -> getInstantiationStrategy().instantiate(bd, null, parent), 
/*      */           
/*  368 */           getAccessControlContext());
/*      */     } else {
/*      */       
/*  371 */       bean = getInstantiationStrategy().instantiate(bd, null, (BeanFactory)abstractAutowireCapableBeanFactory);
/*      */     } 
/*  373 */     populateBean(beanClass.getName(), bd, (BeanWrapper)new BeanWrapperImpl(bean));
/*  374 */     return bean;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void autowireBeanProperties(Object existingBean, int autowireMode, boolean dependencyCheck) throws BeansException {
/*  382 */     if (autowireMode == 3) {
/*  383 */       throw new IllegalArgumentException("AUTOWIRE_CONSTRUCTOR not supported for existing bean instance");
/*      */     }
/*      */ 
/*      */     
/*  387 */     RootBeanDefinition bd = new RootBeanDefinition(ClassUtils.getUserClass(existingBean), autowireMode, dependencyCheck);
/*  388 */     bd.setScope("prototype");
/*  389 */     BeanWrapperImpl beanWrapperImpl = new BeanWrapperImpl(existingBean);
/*  390 */     initBeanWrapper((BeanWrapper)beanWrapperImpl);
/*  391 */     populateBean(bd.getBeanClass().getName(), bd, (BeanWrapper)beanWrapperImpl);
/*      */   }
/*      */ 
/*      */   
/*      */   public void applyBeanPropertyValues(Object existingBean, String beanName) throws BeansException {
/*  396 */     markBeanAsCreated(beanName);
/*  397 */     BeanDefinition bd = getMergedBeanDefinition(beanName);
/*  398 */     BeanWrapperImpl beanWrapperImpl = new BeanWrapperImpl(existingBean);
/*  399 */     initBeanWrapper((BeanWrapper)beanWrapperImpl);
/*  400 */     applyPropertyValues(beanName, bd, (BeanWrapper)beanWrapperImpl, (PropertyValues)bd.getPropertyValues());
/*      */   }
/*      */ 
/*      */   
/*      */   public Object initializeBean(Object existingBean, String beanName) {
/*  405 */     return initializeBean(beanName, existingBean, (RootBeanDefinition)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
/*  412 */     Object result = existingBean;
/*  413 */     for (BeanPostProcessor processor : getBeanPostProcessors()) {
/*  414 */       Object current = processor.postProcessBeforeInitialization(result, beanName);
/*  415 */       if (current == null) {
/*  416 */         return result;
/*      */       }
/*  418 */       result = current;
/*      */     } 
/*  420 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
/*  427 */     Object result = existingBean;
/*  428 */     for (BeanPostProcessor processor : getBeanPostProcessors()) {
/*  429 */       Object current = processor.postProcessAfterInitialization(result, beanName);
/*  430 */       if (current == null) {
/*  431 */         return result;
/*      */       }
/*  433 */       result = current;
/*      */     } 
/*  435 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void destroyBean(Object existingBean) {
/*  440 */     (new DisposableBeanAdapter(existingBean, getBeanPostProcessors(), getAccessControlContext())).destroy();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object resolveBeanByName(String name, DependencyDescriptor descriptor) {
/*  450 */     InjectionPoint previousInjectionPoint = ConstructorResolver.setCurrentInjectionPoint((InjectionPoint)descriptor);
/*      */     try {
/*  452 */       return getBean(name, descriptor.getDependencyType());
/*      */     } finally {
/*      */       
/*  455 */       ConstructorResolver.setCurrentInjectionPoint(previousInjectionPoint);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Object resolveDependency(DependencyDescriptor descriptor, @Nullable String requestingBeanName) throws BeansException {
/*  462 */     return resolveDependency(descriptor, requestingBeanName, null, null);
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
/*      */   protected Object createBean(String beanName, RootBeanDefinition mbd, @Nullable Object[] args) throws BeanCreationException {
/*  479 */     if (this.logger.isTraceEnabled()) {
/*  480 */       this.logger.trace("Creating instance of bean '" + beanName + "'");
/*      */     }
/*  482 */     RootBeanDefinition mbdToUse = mbd;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  487 */     Class<?> resolvedClass = resolveBeanClass(mbd, beanName, new Class[0]);
/*  488 */     if (resolvedClass != null && !mbd.hasBeanClass() && mbd.getBeanClassName() != null) {
/*  489 */       mbdToUse = new RootBeanDefinition(mbd);
/*  490 */       mbdToUse.setBeanClass(resolvedClass);
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/*  495 */       mbdToUse.prepareMethodOverrides();
/*      */     }
/*  497 */     catch (BeanDefinitionValidationException ex) {
/*  498 */       throw new BeanDefinitionStoreException(mbdToUse.getResourceDescription(), beanName, "Validation of method overrides failed", ex);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  504 */       Object bean = resolveBeforeInstantiation(beanName, mbdToUse);
/*  505 */       if (bean != null) {
/*  506 */         return bean;
/*      */       }
/*      */     }
/*  509 */     catch (Throwable ex) {
/*  510 */       throw new BeanCreationException(mbdToUse.getResourceDescription(), beanName, "BeanPostProcessor before instantiation of bean failed", ex);
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/*  515 */       Object beanInstance = doCreateBean(beanName, mbdToUse, args);
/*  516 */       if (this.logger.isTraceEnabled()) {
/*  517 */         this.logger.trace("Finished creating instance of bean '" + beanName + "'");
/*      */       }
/*  519 */       return beanInstance;
/*      */     }
/*  521 */     catch (BeanCreationException|ImplicitlyAppearedSingletonException ex) {
/*      */ 
/*      */       
/*  524 */       throw ex;
/*      */     }
/*  526 */     catch (Throwable ex) {
/*  527 */       throw new BeanCreationException(mbdToUse
/*  528 */           .getResourceDescription(), beanName, "Unexpected exception during bean creation", ex);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object doCreateBean(String beanName, RootBeanDefinition mbd, @Nullable Object[] args) throws BeanCreationException {
/*  550 */     BeanWrapper instanceWrapper = null;
/*  551 */     if (mbd.isSingleton()) {
/*  552 */       instanceWrapper = this.factoryBeanInstanceCache.remove(beanName);
/*      */     }
/*  554 */     if (instanceWrapper == null) {
/*  555 */       instanceWrapper = createBeanInstance(beanName, mbd, args);
/*      */     }
/*  557 */     Object bean = instanceWrapper.getWrappedInstance();
/*  558 */     Class<?> beanType = instanceWrapper.getWrappedClass();
/*  559 */     if (beanType != NullBean.class) {
/*  560 */       mbd.resolvedTargetType = beanType;
/*      */     }
/*      */ 
/*      */     
/*  564 */     synchronized (mbd.postProcessingLock) {
/*  565 */       if (!mbd.postProcessed) {
/*      */         try {
/*  567 */           applyMergedBeanDefinitionPostProcessors(mbd, beanType, beanName);
/*      */         }
/*  569 */         catch (Throwable ex) {
/*  570 */           throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Post-processing of merged bean definition failed", ex);
/*      */         } 
/*      */         
/*  573 */         mbd.postProcessed = true;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  580 */     boolean earlySingletonExposure = (mbd.isSingleton() && this.allowCircularReferences && isSingletonCurrentlyInCreation(beanName));
/*  581 */     if (earlySingletonExposure) {
/*  582 */       if (this.logger.isTraceEnabled()) {
/*  583 */         this.logger.trace("Eagerly caching bean '" + beanName + "' to allow for resolving potential circular references");
/*      */       }
/*      */       
/*  586 */       addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, mbd, bean));
/*      */     } 
/*      */ 
/*      */     
/*  590 */     Object exposedObject = bean;
/*      */     try {
/*  592 */       populateBean(beanName, mbd, instanceWrapper);
/*  593 */       exposedObject = initializeBean(beanName, exposedObject, mbd);
/*      */     }
/*  595 */     catch (Throwable ex) {
/*  596 */       if (ex instanceof BeanCreationException && beanName.equals(((BeanCreationException)ex).getBeanName())) {
/*  597 */         throw (BeanCreationException)ex;
/*      */       }
/*      */       
/*  600 */       throw new BeanCreationException(mbd
/*  601 */           .getResourceDescription(), beanName, "Initialization of bean failed", ex);
/*      */     } 
/*      */ 
/*      */     
/*  605 */     if (earlySingletonExposure) {
/*  606 */       Object earlySingletonReference = getSingleton(beanName, false);
/*  607 */       if (earlySingletonReference != null) {
/*  608 */         if (exposedObject == bean) {
/*  609 */           exposedObject = earlySingletonReference;
/*      */         }
/*  611 */         else if (!this.allowRawInjectionDespiteWrapping && hasDependentBean(beanName)) {
/*  612 */           String[] dependentBeans = getDependentBeans(beanName);
/*  613 */           Set<String> actualDependentBeans = new LinkedHashSet<>(dependentBeans.length);
/*  614 */           for (String dependentBean : dependentBeans) {
/*  615 */             if (!removeSingletonIfCreatedForTypeCheckOnly(dependentBean)) {
/*  616 */               actualDependentBeans.add(dependentBean);
/*      */             }
/*      */           } 
/*  619 */           if (!actualDependentBeans.isEmpty()) {
/*  620 */             throw new BeanCurrentlyInCreationException(beanName, "Bean with name '" + beanName + "' has been injected into other beans [" + 
/*      */                 
/*  622 */                 StringUtils.collectionToCommaDelimitedString(actualDependentBeans) + "] in its raw version as part of a circular reference, but has eventually been wrapped. This means that said other beans do not use the final version of the bean. This is often the result of over-eager type matching - consider using 'getBeanNamesOfType' with the 'allowEagerInit' flag turned off, for example.");
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  634 */       registerDisposableBeanIfNecessary(beanName, bean, mbd);
/*      */     }
/*  636 */     catch (BeanDefinitionValidationException ex) {
/*  637 */       throw new BeanCreationException(mbd
/*  638 */           .getResourceDescription(), beanName, "Invalid destruction signature", ex);
/*      */     } 
/*      */     
/*  641 */     return exposedObject;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected Class<?> predictBeanType(String beanName, RootBeanDefinition mbd, Class<?>... typesToMatch) {
/*  647 */     Class<?> targetType = determineTargetType(beanName, mbd, typesToMatch);
/*      */ 
/*      */ 
/*      */     
/*  651 */     if (targetType != null && !mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {
/*  652 */       for (BeanPostProcessor bp : getBeanPostProcessors()) {
/*  653 */         if (bp instanceof SmartInstantiationAwareBeanPostProcessor) {
/*  654 */           SmartInstantiationAwareBeanPostProcessor ibp = (SmartInstantiationAwareBeanPostProcessor)bp;
/*  655 */           Class<?> predicted = ibp.predictBeanType(targetType, beanName);
/*  656 */           if (predicted != null && (typesToMatch.length != 1 || FactoryBean.class != typesToMatch[0] || FactoryBean.class
/*  657 */             .isAssignableFrom(predicted))) {
/*  658 */             return predicted;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/*  663 */     return targetType;
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
/*      */   protected Class<?> determineTargetType(String beanName, RootBeanDefinition mbd, Class<?>... typesToMatch) {
/*  676 */     Class<?> targetType = mbd.getTargetType();
/*  677 */     if (targetType == null) {
/*      */ 
/*      */       
/*  680 */       targetType = (mbd.getFactoryMethodName() != null) ? getTypeForFactoryMethod(beanName, mbd, typesToMatch) : resolveBeanClass(mbd, beanName, typesToMatch);
/*  681 */       if (ObjectUtils.isEmpty((Object[])typesToMatch) || getTempClassLoader() == null) {
/*  682 */         mbd.resolvedTargetType = targetType;
/*      */       }
/*      */     } 
/*  685 */     return targetType;
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
/*      */   @Nullable
/*      */   protected Class<?> getTypeForFactoryMethod(String beanName, RootBeanDefinition mbd, Class<?>... typesToMatch) {
/*  704 */     ResolvableType cachedReturnType = mbd.factoryMethodReturnType;
/*  705 */     if (cachedReturnType != null) {
/*  706 */       return cachedReturnType.resolve();
/*      */     }
/*      */ 
/*      */     
/*  710 */     boolean isStatic = true;
/*      */     
/*  712 */     String factoryBeanName = mbd.getFactoryBeanName();
/*  713 */     if (factoryBeanName != null) {
/*  714 */       if (factoryBeanName.equals(beanName)) {
/*  715 */         throw new BeanDefinitionStoreException(mbd.getResourceDescription(), beanName, "factory-bean reference points back to the same bean definition");
/*      */       }
/*      */ 
/*      */       
/*  719 */       factoryClass = getType(factoryBeanName);
/*  720 */       isStatic = false;
/*      */     }
/*      */     else {
/*      */       
/*  724 */       factoryClass = resolveBeanClass(mbd, beanName, typesToMatch);
/*      */     } 
/*      */     
/*  727 */     if (factoryClass == null) {
/*  728 */       return null;
/*      */     }
/*  730 */     Class<?> factoryClass = ClassUtils.getUserClass(factoryClass);
/*      */ 
/*      */ 
/*      */     
/*  734 */     Class<?> commonType = null;
/*  735 */     Method uniqueCandidate = null;
/*      */     
/*  737 */     int minNrOfArgs = mbd.hasConstructorArgumentValues() ? mbd.getConstructorArgumentValues().getArgumentCount() : 0;
/*  738 */     Method[] candidates = this.factoryMethodCandidateCache.computeIfAbsent(factoryClass, ReflectionUtils::getUniqueDeclaredMethods);
/*      */ 
/*      */     
/*  741 */     for (Method candidate : candidates) {
/*  742 */       if (Modifier.isStatic(candidate.getModifiers()) == isStatic && mbd.isFactoryMethod(candidate) && candidate
/*  743 */         .getParameterCount() >= minNrOfArgs)
/*      */       {
/*  745 */         if ((candidate.getTypeParameters()).length > 0) {
/*      */           
/*      */           try {
/*  748 */             Class<?>[] paramTypes = candidate.getParameterTypes();
/*  749 */             String[] paramNames = null;
/*  750 */             ParameterNameDiscoverer pnd = getParameterNameDiscoverer();
/*  751 */             if (pnd != null) {
/*  752 */               paramNames = pnd.getParameterNames(candidate);
/*      */             }
/*  754 */             ConstructorArgumentValues cav = mbd.getConstructorArgumentValues();
/*  755 */             Set<ConstructorArgumentValues.ValueHolder> usedValueHolders = new HashSet<>(paramTypes.length);
/*  756 */             Object[] args = new Object[paramTypes.length];
/*  757 */             for (int i = 0; i < args.length; i++) {
/*  758 */               ConstructorArgumentValues.ValueHolder valueHolder = cav.getArgumentValue(i, paramTypes[i], (paramNames != null) ? paramNames[i] : null, usedValueHolders);
/*      */               
/*  760 */               if (valueHolder == null) {
/*  761 */                 valueHolder = cav.getGenericArgumentValue(null, null, usedValueHolders);
/*      */               }
/*  763 */               if (valueHolder != null) {
/*  764 */                 args[i] = valueHolder.getValue();
/*  765 */                 usedValueHolders.add(valueHolder);
/*      */               } 
/*      */             } 
/*  768 */             Class<?> returnType = AutowireUtils.resolveReturnTypeForFactoryMethod(candidate, args, 
/*  769 */                 getBeanClassLoader());
/*  770 */             uniqueCandidate = (commonType == null && returnType == candidate.getReturnType()) ? candidate : null;
/*      */             
/*  772 */             commonType = ClassUtils.determineCommonAncestor(returnType, commonType);
/*  773 */             if (commonType == null)
/*      */             {
/*  775 */               return null;
/*      */             }
/*      */           }
/*  778 */           catch (Throwable ex) {
/*  779 */             if (this.logger.isDebugEnabled()) {
/*  780 */               this.logger.debug("Failed to resolve generic return type for factory method: " + ex);
/*      */             }
/*      */           } 
/*      */         } else {
/*      */           
/*  785 */           uniqueCandidate = (commonType == null) ? candidate : null;
/*  786 */           commonType = ClassUtils.determineCommonAncestor(candidate.getReturnType(), commonType);
/*  787 */           if (commonType == null)
/*      */           {
/*  789 */             return null;
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/*  795 */     mbd.factoryMethodToIntrospect = uniqueCandidate;
/*  796 */     if (commonType == null) {
/*  797 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  802 */     cachedReturnType = (uniqueCandidate != null) ? ResolvableType.forMethodReturnType(uniqueCandidate) : ResolvableType.forClass(commonType);
/*  803 */     mbd.factoryMethodReturnType = cachedReturnType;
/*  804 */     return cachedReturnType.resolve();
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
/*      */   @Nullable
/*      */   protected Class<?> getTypeForFactoryBean(String beanName, RootBeanDefinition mbd) {
/*  821 */     if (mbd.getInstanceSupplier() != null) {
/*  822 */       ResolvableType targetType = mbd.targetType;
/*  823 */       if (targetType != null) {
/*  824 */         Class<?> result = targetType.as(FactoryBean.class).getGeneric(new int[0]).resolve();
/*  825 */         if (result != null) {
/*  826 */           return result;
/*      */         }
/*      */       } 
/*  829 */       if (mbd.hasBeanClass()) {
/*  830 */         Class<?> result = GenericTypeResolver.resolveTypeArgument(mbd.getBeanClass(), FactoryBean.class);
/*  831 */         if (result != null) {
/*  832 */           return result;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  837 */     String factoryBeanName = mbd.getFactoryBeanName();
/*  838 */     String factoryMethodName = mbd.getFactoryMethodName();
/*      */     
/*  840 */     if (factoryBeanName != null) {
/*  841 */       if (factoryMethodName != null) {
/*      */ 
/*      */         
/*  844 */         BeanDefinition fbDef = getBeanDefinition(factoryBeanName);
/*  845 */         if (fbDef instanceof AbstractBeanDefinition) {
/*  846 */           AbstractBeanDefinition afbDef = (AbstractBeanDefinition)fbDef;
/*  847 */           if (afbDef.hasBeanClass()) {
/*  848 */             Class<?> result = getTypeForFactoryBeanFromMethod(afbDef.getBeanClass(), factoryMethodName);
/*  849 */             if (result != null) {
/*  850 */               return result;
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  858 */       if (!isBeanEligibleForMetadataCaching(factoryBeanName)) {
/*  859 */         return null;
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  866 */     FactoryBean<?> fb = mbd.isSingleton() ? getSingletonFactoryBeanForTypeCheck(beanName, mbd) : getNonSingletonFactoryBeanForTypeCheck(beanName, mbd);
/*      */     
/*  868 */     if (fb != null) {
/*      */       
/*  870 */       Class<?> result = getTypeForFactoryBean(fb);
/*  871 */       if (result != null) {
/*  872 */         return result;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  877 */       return super.getTypeForFactoryBean(beanName, mbd);
/*      */     } 
/*      */ 
/*      */     
/*  881 */     if (factoryBeanName == null && mbd.hasBeanClass()) {
/*      */ 
/*      */       
/*  884 */       if (factoryMethodName != null) {
/*  885 */         return getTypeForFactoryBeanFromMethod(mbd.getBeanClass(), factoryMethodName);
/*      */       }
/*      */       
/*  888 */       return GenericTypeResolver.resolveTypeArgument(mbd.getBeanClass(), FactoryBean.class);
/*      */     } 
/*      */ 
/*      */     
/*  892 */     return null;
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
/*      */   private Class<?> getTypeForFactoryBeanFromMethod(Class<?> beanClass, String factoryMethodName) {
/*      */     class Holder
/*      */     {
/*      */       @Nullable
/*  910 */       Class<?> value = null;
/*      */     };
/*      */ 
/*      */     
/*  914 */     Holder objectType = new Holder();
/*      */ 
/*      */     
/*  917 */     Class<?> fbClass = ClassUtils.getUserClass(beanClass);
/*      */ 
/*      */ 
/*      */     
/*  921 */     ReflectionUtils.doWithMethods(fbClass, method -> {
/*      */           if (method.getName().equals(factoryMethodName) && FactoryBean.class.isAssignableFrom(method.getReturnType())) {
/*      */             Class<?> currentType = GenericTypeResolver.resolveReturnTypeArgument(method, FactoryBean.class);
/*      */             
/*      */             if (currentType != null) {
/*      */               objectType.value = ClassUtils.determineCommonAncestor(currentType, objectType.value);
/*      */             }
/*      */           } 
/*      */         });
/*      */     
/*  931 */     return (objectType.value != null && Object.class != objectType.value) ? objectType.value : null;
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
/*      */   protected Object getEarlyBeanReference(String beanName, RootBeanDefinition mbd, Object bean) {
/*  943 */     Object exposedObject = bean;
/*  944 */     if (!mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {
/*  945 */       for (BeanPostProcessor bp : getBeanPostProcessors()) {
/*  946 */         if (bp instanceof SmartInstantiationAwareBeanPostProcessor) {
/*  947 */           SmartInstantiationAwareBeanPostProcessor ibp = (SmartInstantiationAwareBeanPostProcessor)bp;
/*  948 */           exposedObject = ibp.getEarlyBeanReference(exposedObject, beanName);
/*      */         } 
/*      */       } 
/*      */     }
/*  952 */     return exposedObject;
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
/*      */   private FactoryBean<?> getSingletonFactoryBeanForTypeCheck(String beanName, RootBeanDefinition mbd) {
/*  970 */     synchronized (getSingletonMutex()) {
/*  971 */       Object instance; BeanWrapper bw = this.factoryBeanInstanceCache.get(beanName);
/*  972 */       if (bw != null) {
/*  973 */         return (FactoryBean)bw.getWrappedInstance();
/*      */       }
/*  975 */       Object beanInstance = getSingleton(beanName, false);
/*  976 */       if (beanInstance instanceof FactoryBean) {
/*  977 */         return (FactoryBean)beanInstance;
/*      */       }
/*  979 */       if (isSingletonCurrentlyInCreation(beanName) || (mbd
/*  980 */         .getFactoryBeanName() != null && isSingletonCurrentlyInCreation(mbd.getFactoryBeanName()))) {
/*  981 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/*  987 */         beforeSingletonCreation(beanName);
/*      */         
/*  989 */         instance = resolveBeforeInstantiation(beanName, mbd);
/*  990 */         if (instance == null) {
/*  991 */           bw = createBeanInstance(beanName, mbd, (Object[])null);
/*  992 */           instance = bw.getWrappedInstance();
/*      */         }
/*      */       
/*  995 */       } catch (UnsatisfiedDependencyException ex) {
/*      */         
/*  997 */         throw ex;
/*      */       }
/*  999 */       catch (BeanCreationException ex) {
/*      */         
/* 1001 */         if (this.logger.isDebugEnabled()) {
/* 1002 */           this.logger.debug("Bean creation exception on singleton FactoryBean type check: " + ex);
/*      */         }
/* 1004 */         onSuppressedException((Exception)ex);
/* 1005 */         return null;
/*      */       }
/*      */       finally {
/*      */         
/* 1009 */         afterSingletonCreation(beanName);
/*      */       } 
/*      */       
/* 1012 */       FactoryBean<?> fb = getFactoryBean(beanName, instance);
/* 1013 */       if (bw != null) {
/* 1014 */         this.factoryBeanInstanceCache.put(beanName, bw);
/*      */       }
/* 1016 */       return fb;
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
/*      */   private FactoryBean<?> getNonSingletonFactoryBeanForTypeCheck(String beanName, RootBeanDefinition mbd) {
/*      */     Object instance;
/* 1030 */     if (isPrototypeCurrentlyInCreation(beanName)) {
/* 1031 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1037 */       beforePrototypeCreation(beanName);
/*      */       
/* 1039 */       instance = resolveBeforeInstantiation(beanName, mbd);
/* 1040 */       if (instance == null) {
/* 1041 */         BeanWrapper bw = createBeanInstance(beanName, mbd, (Object[])null);
/* 1042 */         instance = bw.getWrappedInstance();
/*      */       }
/*      */     
/* 1045 */     } catch (UnsatisfiedDependencyException ex) {
/*      */       
/* 1047 */       throw ex;
/*      */     }
/* 1049 */     catch (BeanCreationException ex) {
/*      */       
/* 1051 */       if (this.logger.isDebugEnabled()) {
/* 1052 */         this.logger.debug("Bean creation exception on non-singleton FactoryBean type check: " + ex);
/*      */       }
/* 1054 */       onSuppressedException((Exception)ex);
/* 1055 */       return null;
/*      */     }
/*      */     finally {
/*      */       
/* 1059 */       afterPrototypeCreation(beanName);
/*      */     } 
/*      */     
/* 1062 */     return getFactoryBean(beanName, instance);
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
/*      */   protected void applyMergedBeanDefinitionPostProcessors(RootBeanDefinition mbd, Class<?> beanType, String beanName) {
/* 1074 */     for (BeanPostProcessor bp : getBeanPostProcessors()) {
/* 1075 */       if (bp instanceof MergedBeanDefinitionPostProcessor) {
/* 1076 */         MergedBeanDefinitionPostProcessor bdp = (MergedBeanDefinitionPostProcessor)bp;
/* 1077 */         bdp.postProcessMergedBeanDefinition(mbd, beanType, beanName);
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
/*      */   @Nullable
/*      */   protected Object resolveBeforeInstantiation(String beanName, RootBeanDefinition mbd) {
/* 1091 */     Object bean = null;
/* 1092 */     if (!Boolean.FALSE.equals(mbd.beforeInstantiationResolved)) {
/*      */       
/* 1094 */       if (!mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {
/* 1095 */         Class<?> targetType = determineTargetType(beanName, mbd, new Class[0]);
/* 1096 */         if (targetType != null) {
/* 1097 */           bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);
/* 1098 */           if (bean != null) {
/* 1099 */             bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
/*      */           }
/*      */         } 
/*      */       } 
/* 1103 */       mbd.beforeInstantiationResolved = Boolean.valueOf((bean != null));
/*      */     } 
/* 1105 */     return bean;
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
/*      */   @Nullable
/*      */   protected Object applyBeanPostProcessorsBeforeInstantiation(Class<?> beanClass, String beanName) {
/* 1121 */     for (BeanPostProcessor bp : getBeanPostProcessors()) {
/* 1122 */       if (bp instanceof InstantiationAwareBeanPostProcessor) {
/* 1123 */         InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor)bp;
/* 1124 */         Object result = ibp.postProcessBeforeInstantiation(beanClass, beanName);
/* 1125 */         if (result != null) {
/* 1126 */           return result;
/*      */         }
/*      */       } 
/*      */     } 
/* 1130 */     return null;
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
/*      */   protected BeanWrapper createBeanInstance(String beanName, RootBeanDefinition mbd, @Nullable Object[] args) {
/* 1147 */     Class<?> beanClass = resolveBeanClass(mbd, beanName, new Class[0]);
/*      */     
/* 1149 */     if (beanClass != null && !Modifier.isPublic(beanClass.getModifiers()) && !mbd.isNonPublicAccessAllowed()) {
/* 1150 */       throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Bean class isn't public, and non-public access not allowed: " + beanClass
/* 1151 */           .getName());
/*      */     }
/*      */     
/* 1154 */     Supplier<?> instanceSupplier = mbd.getInstanceSupplier();
/* 1155 */     if (instanceSupplier != null) {
/* 1156 */       return obtainFromSupplier(instanceSupplier, beanName);
/*      */     }
/*      */     
/* 1159 */     if (mbd.getFactoryMethodName() != null) {
/* 1160 */       return instantiateUsingFactoryMethod(beanName, mbd, args);
/*      */     }
/*      */ 
/*      */     
/* 1164 */     boolean resolved = false;
/* 1165 */     boolean autowireNecessary = false;
/* 1166 */     if (args == null) {
/* 1167 */       synchronized (mbd.constructorArgumentLock) {
/* 1168 */         if (mbd.resolvedConstructorOrFactoryMethod != null) {
/* 1169 */           resolved = true;
/* 1170 */           autowireNecessary = mbd.constructorArgumentsResolved;
/*      */         } 
/*      */       } 
/*      */     }
/* 1174 */     if (resolved) {
/* 1175 */       if (autowireNecessary) {
/* 1176 */         return autowireConstructor(beanName, mbd, (Constructor<?>[])null, (Object[])null);
/*      */       }
/*      */       
/* 1179 */       return instantiateBean(beanName, mbd);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1184 */     Constructor[] arrayOfConstructor = (Constructor[])determineConstructorsFromBeanPostProcessors(beanClass, beanName);
/* 1185 */     if (arrayOfConstructor != null || mbd.getResolvedAutowireMode() == 3 || mbd
/* 1186 */       .hasConstructorArgumentValues() || !ObjectUtils.isEmpty(args)) {
/* 1187 */       return autowireConstructor(beanName, mbd, (Constructor<?>[])arrayOfConstructor, args);
/*      */     }
/*      */ 
/*      */     
/* 1191 */     arrayOfConstructor = (Constructor[])mbd.getPreferredConstructors();
/* 1192 */     if (arrayOfConstructor != null) {
/* 1193 */       return autowireConstructor(beanName, mbd, (Constructor<?>[])arrayOfConstructor, (Object[])null);
/*      */     }
/*      */ 
/*      */     
/* 1197 */     return instantiateBean(beanName, mbd);
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
/*      */   protected BeanWrapper obtainFromSupplier(Supplier<?> instanceSupplier, String beanName) {
/*      */     Object instance;
/* 1211 */     String outerBean = (String)this.currentlyCreatedBean.get();
/* 1212 */     this.currentlyCreatedBean.set(beanName);
/*      */     try {
/* 1214 */       instance = instanceSupplier.get();
/*      */     } finally {
/*      */       
/* 1217 */       if (outerBean != null) {
/* 1218 */         this.currentlyCreatedBean.set(outerBean);
/*      */       } else {
/*      */         
/* 1221 */         this.currentlyCreatedBean.remove();
/*      */       } 
/*      */     } 
/*      */     
/* 1225 */     if (instance == null) {
/* 1226 */       instance = new NullBean();
/*      */     }
/* 1228 */     BeanWrapperImpl beanWrapperImpl = new BeanWrapperImpl(instance);
/* 1229 */     initBeanWrapper((BeanWrapper)beanWrapperImpl);
/* 1230 */     return (BeanWrapper)beanWrapperImpl;
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
/*      */   protected Object getObjectForBeanInstance(Object beanInstance, String name, String beanName, @Nullable RootBeanDefinition mbd) {
/* 1244 */     String currentlyCreatedBean = (String)this.currentlyCreatedBean.get();
/* 1245 */     if (currentlyCreatedBean != null) {
/* 1246 */       registerDependentBean(beanName, currentlyCreatedBean);
/*      */     }
/*      */     
/* 1249 */     return super.getObjectForBeanInstance(beanInstance, name, beanName, mbd);
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
/*      */   @Nullable
/*      */   protected Constructor<?>[] determineConstructorsFromBeanPostProcessors(@Nullable Class<?> beanClass, String beanName) throws BeansException {
/* 1265 */     if (beanClass != null && hasInstantiationAwareBeanPostProcessors()) {
/* 1266 */       for (BeanPostProcessor bp : getBeanPostProcessors()) {
/* 1267 */         if (bp instanceof SmartInstantiationAwareBeanPostProcessor) {
/* 1268 */           SmartInstantiationAwareBeanPostProcessor ibp = (SmartInstantiationAwareBeanPostProcessor)bp;
/* 1269 */           Constructor[] arrayOfConstructor = ibp.determineCandidateConstructors(beanClass, beanName);
/* 1270 */           if (arrayOfConstructor != null) {
/* 1271 */             return (Constructor<?>[])arrayOfConstructor;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/* 1276 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected BeanWrapper instantiateBean(String beanName, RootBeanDefinition mbd) {
/*      */     try {
/*      */       Object beanInstance;
/* 1288 */       AbstractAutowireCapableBeanFactory abstractAutowireCapableBeanFactory = this;
/* 1289 */       if (System.getSecurityManager() != null) {
/* 1290 */         beanInstance = AccessController.doPrivileged(() -> getInstantiationStrategy().instantiate(mbd, beanName, parent), 
/*      */             
/* 1292 */             getAccessControlContext());
/*      */       } else {
/*      */         
/* 1295 */         beanInstance = getInstantiationStrategy().instantiate(mbd, beanName, (BeanFactory)abstractAutowireCapableBeanFactory);
/*      */       } 
/* 1297 */       BeanWrapperImpl beanWrapperImpl = new BeanWrapperImpl(beanInstance);
/* 1298 */       initBeanWrapper((BeanWrapper)beanWrapperImpl);
/* 1299 */       return (BeanWrapper)beanWrapperImpl;
/*      */     }
/* 1301 */     catch (Throwable ex) {
/* 1302 */       throw new BeanCreationException(mbd
/* 1303 */           .getResourceDescription(), beanName, "Instantiation of bean failed", ex);
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
/*      */   
/*      */   protected BeanWrapper instantiateUsingFactoryMethod(String beanName, RootBeanDefinition mbd, @Nullable Object[] explicitArgs) {
/* 1321 */     return (new ConstructorResolver(this)).instantiateUsingFactoryMethod(beanName, mbd, explicitArgs);
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
/*      */   protected BeanWrapper autowireConstructor(String beanName, RootBeanDefinition mbd, @Nullable Constructor<?>[] ctors, @Nullable Object[] explicitArgs) {
/* 1341 */     return (new ConstructorResolver(this)).autowireConstructor(beanName, mbd, ctors, explicitArgs);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void populateBean(String beanName, RootBeanDefinition mbd, @Nullable BeanWrapper bw) {
/*      */     PropertyValues propertyValues;
/* 1353 */     if (bw == null) {
/* 1354 */       if (mbd.hasPropertyValues()) {
/* 1355 */         throw new BeanCreationException(mbd
/* 1356 */             .getResourceDescription(), beanName, "Cannot apply property values to null instance");
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1367 */     boolean continueWithPropertyPopulation = true;
/*      */     
/* 1369 */     if (!mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {
/* 1370 */       for (BeanPostProcessor bp : getBeanPostProcessors()) {
/* 1371 */         if (bp instanceof InstantiationAwareBeanPostProcessor) {
/* 1372 */           InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor)bp;
/* 1373 */           if (!ibp.postProcessAfterInstantiation(bw.getWrappedInstance(), beanName)) {
/* 1374 */             continueWithPropertyPopulation = false;
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/* 1381 */     if (!continueWithPropertyPopulation) {
/*      */       return;
/*      */     }
/*      */     
/* 1385 */     MutablePropertyValues mutablePropertyValues = mbd.hasPropertyValues() ? mbd.getPropertyValues() : null;
/*      */     
/* 1387 */     if (mbd.getResolvedAutowireMode() == 1 || mbd.getResolvedAutowireMode() == 2) {
/* 1388 */       MutablePropertyValues newPvs = new MutablePropertyValues((PropertyValues)mutablePropertyValues);
/*      */       
/* 1390 */       if (mbd.getResolvedAutowireMode() == 1) {
/* 1391 */         autowireByName(beanName, mbd, bw, newPvs);
/*      */       }
/*      */       
/* 1394 */       if (mbd.getResolvedAutowireMode() == 2) {
/* 1395 */         autowireByType(beanName, mbd, bw, newPvs);
/*      */       }
/* 1397 */       mutablePropertyValues = newPvs;
/*      */     } 
/*      */     
/* 1400 */     boolean hasInstAwareBpps = hasInstantiationAwareBeanPostProcessors();
/* 1401 */     boolean needsDepCheck = (mbd.getDependencyCheck() != 0);
/*      */     
/* 1403 */     PropertyDescriptor[] filteredPds = null;
/* 1404 */     if (hasInstAwareBpps) {
/* 1405 */       if (mutablePropertyValues == null) {
/* 1406 */         mutablePropertyValues = mbd.getPropertyValues();
/*      */       }
/* 1408 */       for (BeanPostProcessor bp : getBeanPostProcessors()) {
/* 1409 */         if (bp instanceof InstantiationAwareBeanPostProcessor) {
/* 1410 */           InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor)bp;
/* 1411 */           PropertyValues pvsToUse = ibp.postProcessProperties((PropertyValues)mutablePropertyValues, bw.getWrappedInstance(), beanName);
/* 1412 */           if (pvsToUse == null) {
/* 1413 */             if (filteredPds == null) {
/* 1414 */               filteredPds = filterPropertyDescriptorsForDependencyCheck(bw, mbd.allowCaching);
/*      */             }
/* 1416 */             pvsToUse = ibp.postProcessPropertyValues((PropertyValues)mutablePropertyValues, filteredPds, bw.getWrappedInstance(), beanName);
/* 1417 */             if (pvsToUse == null) {
/*      */               return;
/*      */             }
/*      */           } 
/* 1421 */           propertyValues = pvsToUse;
/*      */         } 
/*      */       } 
/*      */     } 
/* 1425 */     if (needsDepCheck) {
/* 1426 */       if (filteredPds == null) {
/* 1427 */         filteredPds = filterPropertyDescriptorsForDependencyCheck(bw, mbd.allowCaching);
/*      */       }
/* 1429 */       checkDependencies(beanName, mbd, filteredPds, propertyValues);
/*      */     } 
/*      */     
/* 1432 */     if (propertyValues != null) {
/* 1433 */       applyPropertyValues(beanName, mbd, bw, propertyValues);
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
/*      */   protected void autowireByName(String beanName, AbstractBeanDefinition mbd, BeanWrapper bw, MutablePropertyValues pvs) {
/* 1449 */     String[] propertyNames = unsatisfiedNonSimpleProperties(mbd, bw);
/* 1450 */     for (String propertyName : propertyNames) {
/* 1451 */       if (containsBean(propertyName)) {
/* 1452 */         Object bean = getBean(propertyName);
/* 1453 */         pvs.add(propertyName, bean);
/* 1454 */         registerDependentBean(propertyName, beanName);
/* 1455 */         if (this.logger.isTraceEnabled()) {
/* 1456 */           this.logger.trace("Added autowiring by name from bean name '" + beanName + "' via property '" + propertyName + "' to bean named '" + propertyName + "'");
/*      */         
/*      */         }
/*      */       
/*      */       }
/* 1461 */       else if (this.logger.isTraceEnabled()) {
/* 1462 */         this.logger.trace("Not autowiring property '" + propertyName + "' of bean '" + beanName + "' by name: no matching bean found");
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
/*      */ 
/*      */   
/*      */   protected void autowireByType(String beanName, AbstractBeanDefinition mbd, BeanWrapper bw, MutablePropertyValues pvs) {
/*      */     BeanWrapper beanWrapper;
/* 1483 */     TypeConverter converter = getCustomTypeConverter();
/* 1484 */     if (converter == null) {
/* 1485 */       beanWrapper = bw;
/*      */     }
/*      */     
/* 1488 */     Set<String> autowiredBeanNames = new LinkedHashSet<>(4);
/* 1489 */     String[] propertyNames = unsatisfiedNonSimpleProperties(mbd, bw);
/* 1490 */     for (String propertyName : propertyNames) {
/*      */       try {
/* 1492 */         PropertyDescriptor pd = bw.getPropertyDescriptor(propertyName);
/*      */ 
/*      */         
/* 1495 */         if (Object.class != pd.getPropertyType()) {
/* 1496 */           MethodParameter methodParam = BeanUtils.getWriteMethodParameter(pd);
/*      */           
/* 1498 */           boolean eager = !PriorityOrdered.class.isInstance(bw.getWrappedInstance());
/* 1499 */           DependencyDescriptor desc = new AutowireByTypeDependencyDescriptor(methodParam, eager);
/* 1500 */           Object autowiredArgument = resolveDependency(desc, beanName, autowiredBeanNames, (TypeConverter)beanWrapper);
/* 1501 */           if (autowiredArgument != null) {
/* 1502 */             pvs.add(propertyName, autowiredArgument);
/*      */           }
/* 1504 */           for (String autowiredBeanName : autowiredBeanNames) {
/* 1505 */             registerDependentBean(autowiredBeanName, beanName);
/* 1506 */             if (this.logger.isTraceEnabled()) {
/* 1507 */               this.logger.trace("Autowiring by type from bean name '" + beanName + "' via property '" + propertyName + "' to bean named '" + autowiredBeanName + "'");
/*      */             }
/*      */           } 
/*      */           
/* 1511 */           autowiredBeanNames.clear();
/*      */         }
/*      */       
/* 1514 */       } catch (BeansException ex) {
/* 1515 */         throw new UnsatisfiedDependencyException(mbd.getResourceDescription(), beanName, propertyName, ex);
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
/*      */   protected String[] unsatisfiedNonSimpleProperties(AbstractBeanDefinition mbd, BeanWrapper bw) {
/* 1531 */     Set<String> result = new TreeSet<>();
/* 1532 */     MutablePropertyValues mutablePropertyValues = mbd.getPropertyValues();
/* 1533 */     PropertyDescriptor[] pds = bw.getPropertyDescriptors();
/* 1534 */     for (PropertyDescriptor pd : pds) {
/* 1535 */       if (pd.getWriteMethod() != null && !isExcludedFromDependencyCheck(pd) && !mutablePropertyValues.contains(pd.getName()) && 
/* 1536 */         !BeanUtils.isSimpleProperty(pd.getPropertyType())) {
/* 1537 */         result.add(pd.getName());
/*      */       }
/*      */     } 
/* 1540 */     return StringUtils.toStringArray(result);
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
/*      */   protected PropertyDescriptor[] filterPropertyDescriptorsForDependencyCheck(BeanWrapper bw, boolean cache) {
/* 1553 */     PropertyDescriptor[] filtered = this.filteredPropertyDescriptorsCache.get(bw.getWrappedClass());
/* 1554 */     if (filtered == null) {
/* 1555 */       filtered = filterPropertyDescriptorsForDependencyCheck(bw);
/* 1556 */       if (cache) {
/*      */         
/* 1558 */         PropertyDescriptor[] existing = this.filteredPropertyDescriptorsCache.putIfAbsent(bw.getWrappedClass(), filtered);
/* 1559 */         if (existing != null) {
/* 1560 */           filtered = existing;
/*      */         }
/*      */       } 
/*      */     } 
/* 1564 */     return filtered;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected PropertyDescriptor[] filterPropertyDescriptorsForDependencyCheck(BeanWrapper bw) {
/* 1575 */     List<PropertyDescriptor> pds = new ArrayList<>(Arrays.asList(bw.getPropertyDescriptors()));
/* 1576 */     pds.removeIf(this::isExcludedFromDependencyCheck);
/* 1577 */     return pds.<PropertyDescriptor>toArray(new PropertyDescriptor[0]);
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
/*      */   protected boolean isExcludedFromDependencyCheck(PropertyDescriptor pd) {
/* 1591 */     return (AutowireUtils.isExcludedFromDependencyCheck(pd) || this.ignoredDependencyTypes
/* 1592 */       .contains(pd.getPropertyType()) || 
/* 1593 */       AutowireUtils.isSetterDefinedInInterface(pd, this.ignoredDependencyInterfaces));
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
/*      */   protected void checkDependencies(String beanName, AbstractBeanDefinition mbd, PropertyDescriptor[] pds, @Nullable PropertyValues pvs) throws UnsatisfiedDependencyException {
/* 1610 */     int dependencyCheck = mbd.getDependencyCheck();
/* 1611 */     for (PropertyDescriptor pd : pds) {
/* 1612 */       if (pd.getWriteMethod() != null && (pvs == null || !pvs.contains(pd.getName()))) {
/* 1613 */         boolean isSimple = BeanUtils.isSimpleProperty(pd.getPropertyType());
/* 1614 */         boolean unsatisfied = (dependencyCheck == 3 || (isSimple && dependencyCheck == 2) || (!isSimple && dependencyCheck == 1));
/*      */ 
/*      */         
/* 1617 */         if (unsatisfied) {
/* 1618 */           throw new UnsatisfiedDependencyException(mbd.getResourceDescription(), beanName, pd.getName(), "Set this property value or disable dependency checking for this bean.");
/*      */         }
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
/*      */   protected void applyPropertyValues(String beanName, BeanDefinition mbd, BeanWrapper bw, PropertyValues pvs) {
/*      */     List<PropertyValue> original;
/*      */     BeanWrapper beanWrapper;
/* 1635 */     if (pvs.isEmpty()) {
/*      */       return;
/*      */     }
/*      */     
/* 1639 */     if (System.getSecurityManager() != null && bw instanceof BeanWrapperImpl) {
/* 1640 */       ((BeanWrapperImpl)bw).setSecurityContext(getAccessControlContext());
/*      */     }
/*      */     
/* 1643 */     MutablePropertyValues mpvs = null;
/*      */ 
/*      */     
/* 1646 */     if (pvs instanceof MutablePropertyValues) {
/* 1647 */       mpvs = (MutablePropertyValues)pvs;
/* 1648 */       if (mpvs.isConverted()) {
/*      */         
/*      */         try {
/* 1651 */           bw.setPropertyValues((PropertyValues)mpvs);
/*      */           
/*      */           return;
/* 1654 */         } catch (BeansException ex) {
/* 1655 */           throw new BeanCreationException(mbd
/* 1656 */               .getResourceDescription(), beanName, "Error setting property values", ex);
/*      */         } 
/*      */       }
/* 1659 */       original = mpvs.getPropertyValueList();
/*      */     } else {
/*      */       
/* 1662 */       original = Arrays.asList(pvs.getPropertyValues());
/*      */     } 
/*      */     
/* 1665 */     TypeConverter converter = getCustomTypeConverter();
/* 1666 */     if (converter == null) {
/* 1667 */       beanWrapper = bw;
/*      */     }
/* 1669 */     BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this, beanName, mbd, (TypeConverter)beanWrapper);
/*      */ 
/*      */     
/* 1672 */     List<PropertyValue> deepCopy = new ArrayList<>(original.size());
/* 1673 */     boolean resolveNecessary = false;
/* 1674 */     for (PropertyValue pv : original) {
/* 1675 */       if (pv.isConverted()) {
/* 1676 */         deepCopy.add(pv);
/*      */         continue;
/*      */       } 
/* 1679 */       String propertyName = pv.getName();
/* 1680 */       Object originalValue = pv.getValue();
/* 1681 */       Object resolvedValue = valueResolver.resolveValueIfNecessary(pv, originalValue);
/* 1682 */       Object convertedValue = resolvedValue;
/*      */       
/* 1684 */       boolean convertible = (bw.isWritableProperty(propertyName) && !PropertyAccessorUtils.isNestedOrIndexedProperty(propertyName));
/* 1685 */       if (convertible) {
/* 1686 */         convertedValue = convertForProperty(resolvedValue, propertyName, bw, (TypeConverter)beanWrapper);
/*      */       }
/*      */ 
/*      */       
/* 1690 */       if (resolvedValue == originalValue) {
/* 1691 */         if (convertible) {
/* 1692 */           pv.setConvertedValue(convertedValue);
/*      */         }
/* 1694 */         deepCopy.add(pv); continue;
/*      */       } 
/* 1696 */       if (convertible && originalValue instanceof TypedStringValue && 
/* 1697 */         !((TypedStringValue)originalValue).isDynamic() && !(convertedValue instanceof java.util.Collection) && 
/* 1698 */         !ObjectUtils.isArray(convertedValue)) {
/* 1699 */         pv.setConvertedValue(convertedValue);
/* 1700 */         deepCopy.add(pv);
/*      */         continue;
/*      */       } 
/* 1703 */       resolveNecessary = true;
/* 1704 */       deepCopy.add(new PropertyValue(pv, convertedValue));
/*      */     } 
/*      */ 
/*      */     
/* 1708 */     if (mpvs != null && !resolveNecessary) {
/* 1709 */       mpvs.setConverted();
/*      */     }
/*      */ 
/*      */     
/*      */     try {
/* 1714 */       bw.setPropertyValues((PropertyValues)new MutablePropertyValues(deepCopy));
/*      */     }
/* 1716 */     catch (BeansException ex) {
/* 1717 */       throw new BeanCreationException(mbd
/* 1718 */           .getResourceDescription(), beanName, "Error setting property values", ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private Object convertForProperty(@Nullable Object value, String propertyName, BeanWrapper bw, TypeConverter converter) {
/* 1729 */     if (converter instanceof BeanWrapperImpl) {
/* 1730 */       return ((BeanWrapperImpl)converter).convertForProperty(value, propertyName);
/*      */     }
/*      */     
/* 1733 */     PropertyDescriptor pd = bw.getPropertyDescriptor(propertyName);
/* 1734 */     MethodParameter methodParam = BeanUtils.getWriteMethodParameter(pd);
/* 1735 */     return converter.convertIfNecessary(value, pd.getPropertyType(), methodParam);
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
/*      */   protected Object initializeBean(String beanName, Object bean, @Nullable RootBeanDefinition mbd) {
/* 1758 */     if (System.getSecurityManager() != null) {
/* 1759 */       AccessController.doPrivileged(() -> {
/*      */             invokeAwareMethods(beanName, bean);
/*      */             return null;
/* 1762 */           }getAccessControlContext());
/*      */     } else {
/*      */       
/* 1765 */       invokeAwareMethods(beanName, bean);
/*      */     } 
/*      */     
/* 1768 */     Object wrappedBean = bean;
/* 1769 */     if (mbd == null || !mbd.isSynthetic()) {
/* 1770 */       wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
/*      */     }
/*      */     
/*      */     try {
/* 1774 */       invokeInitMethods(beanName, wrappedBean, mbd);
/*      */     }
/* 1776 */     catch (Throwable ex) {
/* 1777 */       throw new BeanCreationException((mbd != null) ? mbd
/* 1778 */           .getResourceDescription() : null, beanName, "Invocation of init method failed", ex);
/*      */     } 
/*      */     
/* 1781 */     if (mbd == null || !mbd.isSynthetic()) {
/* 1782 */       wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
/*      */     }
/*      */     
/* 1785 */     return wrappedBean;
/*      */   }
/*      */   
/*      */   private void invokeAwareMethods(String beanName, Object bean) {
/* 1789 */     if (bean instanceof org.springframework.beans.factory.Aware) {
/* 1790 */       if (bean instanceof BeanNameAware) {
/* 1791 */         ((BeanNameAware)bean).setBeanName(beanName);
/*      */       }
/* 1793 */       if (bean instanceof BeanClassLoaderAware) {
/* 1794 */         ClassLoader bcl = getBeanClassLoader();
/* 1795 */         if (bcl != null) {
/* 1796 */           ((BeanClassLoaderAware)bean).setBeanClassLoader(bcl);
/*      */         }
/*      */       } 
/* 1799 */       if (bean instanceof BeanFactoryAware) {
/* 1800 */         ((BeanFactoryAware)bean).setBeanFactory((BeanFactory)this);
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
/*      */ 
/*      */   
/*      */   protected void invokeInitMethods(String beanName, Object bean, @Nullable RootBeanDefinition mbd) throws Throwable {
/* 1820 */     boolean isInitializingBean = bean instanceof InitializingBean;
/* 1821 */     if (isInitializingBean && (mbd == null || !mbd.isExternallyManagedInitMethod("afterPropertiesSet"))) {
/* 1822 */       if (this.logger.isTraceEnabled()) {
/* 1823 */         this.logger.trace("Invoking afterPropertiesSet() on bean with name '" + beanName + "'");
/*      */       }
/* 1825 */       if (System.getSecurityManager() != null) {
/*      */         try {
/* 1827 */           AccessController.doPrivileged(() -> {
/*      */                 ((InitializingBean)bean).afterPropertiesSet();
/*      */                 return null;
/* 1830 */               }getAccessControlContext());
/*      */         }
/* 1832 */         catch (PrivilegedActionException pae) {
/* 1833 */           throw pae.getException();
/*      */         } 
/*      */       } else {
/*      */         
/* 1837 */         ((InitializingBean)bean).afterPropertiesSet();
/*      */       } 
/*      */     } 
/*      */     
/* 1841 */     if (mbd != null && bean.getClass() != NullBean.class) {
/* 1842 */       String initMethodName = mbd.getInitMethodName();
/* 1843 */       if (StringUtils.hasLength(initMethodName) && (!isInitializingBean || 
/* 1844 */         !"afterPropertiesSet".equals(initMethodName)) && 
/* 1845 */         !mbd.isExternallyManagedInitMethod(initMethodName)) {
/* 1846 */         invokeCustomInitMethod(beanName, bean, mbd);
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
/*      */   protected void invokeCustomInitMethod(String beanName, Object bean, RootBeanDefinition mbd) throws Throwable {
/* 1861 */     String initMethodName = mbd.getInitMethodName();
/* 1862 */     Assert.state((initMethodName != null), "No init method set");
/*      */ 
/*      */     
/* 1865 */     Method initMethod = mbd.isNonPublicAccessAllowed() ? BeanUtils.findMethod(bean.getClass(), initMethodName, new Class[0]) : ClassUtils.getMethodIfAvailable(bean.getClass(), initMethodName, new Class[0]);
/*      */     
/* 1867 */     if (initMethod == null) {
/* 1868 */       if (mbd.isEnforceInitMethod()) {
/* 1869 */         throw new BeanDefinitionValidationException("Could not find an init method named '" + initMethodName + "' on bean with name '" + beanName + "'");
/*      */       }
/*      */ 
/*      */       
/* 1873 */       if (this.logger.isTraceEnabled()) {
/* 1874 */         this.logger.trace("No default init method named '" + initMethodName + "' found on bean with name '" + beanName + "'");
/*      */       }
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */ 
/*      */     
/* 1882 */     if (this.logger.isTraceEnabled()) {
/* 1883 */       this.logger.trace("Invoking init method  '" + initMethodName + "' on bean with name '" + beanName + "'");
/*      */     }
/* 1885 */     Method methodToInvoke = ClassUtils.getInterfaceMethodIfPossible(initMethod);
/*      */     
/* 1887 */     if (System.getSecurityManager() != null) {
/* 1888 */       AccessController.doPrivileged(() -> {
/*      */             ReflectionUtils.makeAccessible(methodToInvoke);
/*      */             return null;
/*      */           });
/*      */       try {
/* 1893 */         AccessController.doPrivileged(() -> methodToInvoke.invoke(bean, new Object[0]), 
/* 1894 */             getAccessControlContext());
/*      */       }
/* 1896 */       catch (PrivilegedActionException pae) {
/* 1897 */         InvocationTargetException ex = (InvocationTargetException)pae.getException();
/* 1898 */         throw ex.getTargetException();
/*      */       } 
/*      */     } else {
/*      */       
/*      */       try {
/* 1903 */         ReflectionUtils.makeAccessible(methodToInvoke);
/* 1904 */         methodToInvoke.invoke(bean, new Object[0]);
/*      */       }
/* 1906 */       catch (InvocationTargetException ex) {
/* 1907 */         throw ex.getTargetException();
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
/*      */   protected Object postProcessObjectFromFactoryBean(Object object, String beanName) {
/* 1921 */     return applyBeanPostProcessorsAfterInitialization(object, beanName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void removeSingleton(String beanName) {
/* 1929 */     synchronized (getSingletonMutex()) {
/* 1930 */       super.removeSingleton(beanName);
/* 1931 */       this.factoryBeanInstanceCache.remove(beanName);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void clearSingletonCache() {
/* 1940 */     synchronized (getSingletonMutex()) {
/* 1941 */       super.clearSingletonCache();
/* 1942 */       this.factoryBeanInstanceCache.clear();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Log getLogger() {
/* 1951 */     return this.logger;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class AutowireByTypeDependencyDescriptor
/*      */     extends DependencyDescriptor
/*      */   {
/*      */     public AutowireByTypeDependencyDescriptor(MethodParameter methodParameter, boolean eager) {
/* 1963 */       super(methodParameter, false, eager);
/*      */     }
/*      */ 
/*      */     
/*      */     public String getDependencyName() {
/* 1968 */       return null;
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/AbstractAutowireCapableBeanFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */