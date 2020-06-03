/*      */ package org.springframework.beans.factory.support;
/*      */ 
/*      */ import java.beans.PropertyEditor;
/*      */ import java.security.AccessControlContext;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.CopyOnWriteArrayList;
/*      */ import org.springframework.beans.BeanUtils;
/*      */ import org.springframework.beans.BeanWrapper;
/*      */ import org.springframework.beans.BeansException;
/*      */ import org.springframework.beans.PropertyEditorRegistrar;
/*      */ import org.springframework.beans.PropertyEditorRegistry;
/*      */ import org.springframework.beans.PropertyEditorRegistrySupport;
/*      */ import org.springframework.beans.SimpleTypeConverter;
/*      */ import org.springframework.beans.TypeConverter;
/*      */ import org.springframework.beans.TypeMismatchException;
/*      */ import org.springframework.beans.factory.BeanCreationException;
/*      */ import org.springframework.beans.factory.BeanCurrentlyInCreationException;
/*      */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*      */ import org.springframework.beans.factory.BeanFactory;
/*      */ import org.springframework.beans.factory.BeanFactoryUtils;
/*      */ import org.springframework.beans.factory.BeanIsAbstractException;
/*      */ import org.springframework.beans.factory.BeanIsNotAFactoryException;
/*      */ import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
/*      */ import org.springframework.beans.factory.CannotLoadBeanClassException;
/*      */ import org.springframework.beans.factory.FactoryBean;
/*      */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*      */ import org.springframework.beans.factory.SmartFactoryBean;
/*      */ import org.springframework.beans.factory.config.BeanDefinition;
/*      */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*      */ import org.springframework.beans.factory.config.BeanExpressionContext;
/*      */ import org.springframework.beans.factory.config.BeanExpressionResolver;
/*      */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*      */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*      */ import org.springframework.beans.factory.config.Scope;
/*      */ import org.springframework.core.DecoratingClassLoader;
/*      */ import org.springframework.core.NamedThreadLocal;
/*      */ import org.springframework.core.ResolvableType;
/*      */ import org.springframework.core.convert.ConversionService;
/*      */ import org.springframework.lang.Nullable;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.ObjectUtils;
/*      */ import org.springframework.util.StringUtils;
/*      */ import org.springframework.util.StringValueResolver;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class AbstractBeanFactory
/*      */   extends FactoryBeanRegistrySupport
/*      */   implements ConfigurableBeanFactory
/*      */ {
/*      */   @Nullable
/*      */   private BeanFactory parentBeanFactory;
/*      */   @Nullable
/*  120 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private ClassLoader tempClassLoader;
/*      */ 
/*      */   
/*      */   private boolean cacheBeanMetadata = true;
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private BeanExpressionResolver beanExpressionResolver;
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private ConversionService conversionService;
/*      */ 
/*      */   
/*  138 */   private final Set<PropertyEditorRegistrar> propertyEditorRegistrars = new LinkedHashSet<>(4);
/*      */ 
/*      */   
/*  141 */   private final Map<Class<?>, Class<? extends PropertyEditor>> customEditors = new HashMap<>(4);
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private TypeConverter typeConverter;
/*      */ 
/*      */   
/*  148 */   private final List<StringValueResolver> embeddedValueResolvers = new CopyOnWriteArrayList<>();
/*      */ 
/*      */   
/*  151 */   private final List<BeanPostProcessor> beanPostProcessors = new CopyOnWriteArrayList<>();
/*      */ 
/*      */   
/*      */   private volatile boolean hasInstantiationAwareBeanPostProcessors;
/*      */ 
/*      */   
/*      */   private volatile boolean hasDestructionAwareBeanPostProcessors;
/*      */ 
/*      */   
/*  160 */   private final Map<String, Scope> scopes = new LinkedHashMap<>(8);
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private SecurityContextProvider securityContextProvider;
/*      */ 
/*      */   
/*  167 */   private final Map<String, RootBeanDefinition> mergedBeanDefinitions = new ConcurrentHashMap<>(256);
/*      */ 
/*      */   
/*  170 */   private final Set<String> alreadyCreated = Collections.newSetFromMap(new ConcurrentHashMap<>(256));
/*      */ 
/*      */   
/*  173 */   private final ThreadLocal<Object> prototypesCurrentlyInCreation = (ThreadLocal<Object>)new NamedThreadLocal("Prototype beans currently in creation");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AbstractBeanFactory(@Nullable BeanFactory parentBeanFactory) {
/*  189 */     this.parentBeanFactory = parentBeanFactory;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getBean(String name) throws BeansException {
/*  199 */     return doGetBean(name, (Class<?>)null, (Object[])null, false);
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
/*  204 */     return doGetBean(name, requiredType, (Object[])null, false);
/*      */   }
/*      */ 
/*      */   
/*      */   public Object getBean(String name, Object... args) throws BeansException {
/*  209 */     return doGetBean(name, (Class<?>)null, args, false);
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
/*      */   public <T> T getBean(String name, @Nullable Class<T> requiredType, @Nullable Object... args) throws BeansException {
/*  224 */     return doGetBean(name, requiredType, args, false);
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
/*      */   protected <T> T doGetBean(String name, @Nullable Class<T> requiredType, @Nullable Object[] args, boolean typeCheckOnly) throws BeansException {
/*      */     Object bean;
/*  242 */     String beanName = transformedBeanName(name);
/*      */ 
/*      */ 
/*      */     
/*  246 */     Object sharedInstance = getSingleton(beanName);
/*  247 */     if (sharedInstance != null && args == null) {
/*  248 */       if (this.logger.isTraceEnabled()) {
/*  249 */         if (isSingletonCurrentlyInCreation(beanName)) {
/*  250 */           this.logger.trace("Returning eagerly cached instance of singleton bean '" + beanName + "' that is not fully initialized yet - a consequence of a circular reference");
/*      */         }
/*      */         else {
/*      */           
/*  254 */           this.logger.trace("Returning cached instance of singleton bean '" + beanName + "'");
/*      */         } 
/*      */       }
/*  257 */       bean = getObjectForBeanInstance(sharedInstance, name, beanName, (RootBeanDefinition)null);
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/*  263 */       if (isPrototypeCurrentlyInCreation(beanName)) {
/*  264 */         throw new BeanCurrentlyInCreationException(beanName);
/*      */       }
/*      */ 
/*      */       
/*  268 */       BeanFactory parentBeanFactory = getParentBeanFactory();
/*  269 */       if (parentBeanFactory != null && !containsBeanDefinition(beanName)) {
/*      */         
/*  271 */         String nameToLookup = originalBeanName(name);
/*  272 */         if (parentBeanFactory instanceof AbstractBeanFactory) {
/*  273 */           return ((AbstractBeanFactory)parentBeanFactory).doGetBean(nameToLookup, requiredType, args, typeCheckOnly);
/*      */         }
/*      */         
/*  276 */         if (args != null)
/*      */         {
/*  278 */           return (T)parentBeanFactory.getBean(nameToLookup, args);
/*      */         }
/*  280 */         if (requiredType != null)
/*      */         {
/*  282 */           return (T)parentBeanFactory.getBean(nameToLookup, requiredType);
/*      */         }
/*      */         
/*  285 */         return (T)parentBeanFactory.getBean(nameToLookup);
/*      */       } 
/*      */ 
/*      */       
/*  289 */       if (!typeCheckOnly) {
/*  290 */         markBeanAsCreated(beanName);
/*      */       }
/*      */       
/*      */       try {
/*  294 */         RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/*  295 */         checkMergedBeanDefinition(mbd, beanName, args);
/*      */ 
/*      */         
/*  298 */         String[] dependsOn = mbd.getDependsOn();
/*  299 */         if (dependsOn != null) {
/*  300 */           for (String dep : dependsOn) {
/*  301 */             if (isDependent(beanName, dep)) {
/*  302 */               throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Circular depends-on relationship between '" + beanName + "' and '" + dep + "'");
/*      */             }
/*      */             
/*  305 */             registerDependentBean(dep, beanName);
/*      */             try {
/*  307 */               getBean(dep);
/*      */             }
/*  309 */             catch (NoSuchBeanDefinitionException ex) {
/*  310 */               throw new BeanCreationException(mbd.getResourceDescription(), beanName, "'" + beanName + "' depends on missing bean '" + dep + "'", ex);
/*      */             } 
/*      */           } 
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  317 */         if (mbd.isSingleton()) {
/*  318 */           sharedInstance = getSingleton(beanName, () -> {
/*      */                 
/*      */                 try {
/*      */                   return createBean(beanName, mbd, args);
/*  322 */                 } catch (BeansException ex) {
/*      */                   destroySingleton(beanName);
/*      */ 
/*      */                   
/*      */                   throw ex;
/*      */                 } 
/*      */               });
/*      */           
/*  330 */           bean = getObjectForBeanInstance(sharedInstance, name, beanName, mbd);
/*      */         
/*      */         }
/*  333 */         else if (mbd.isPrototype()) {
/*      */           
/*  335 */           Object prototypeInstance = null;
/*      */           try {
/*  337 */             beforePrototypeCreation(beanName);
/*  338 */             prototypeInstance = createBean(beanName, mbd, args);
/*      */           } finally {
/*      */             
/*  341 */             afterPrototypeCreation(beanName);
/*      */           } 
/*  343 */           bean = getObjectForBeanInstance(prototypeInstance, name, beanName, mbd);
/*      */         }
/*      */         else {
/*      */           
/*  347 */           String scopeName = mbd.getScope();
/*  348 */           Scope scope = this.scopes.get(scopeName);
/*  349 */           if (scope == null) {
/*  350 */             throw new IllegalStateException("No Scope registered for scope name '" + scopeName + "'");
/*      */           }
/*      */           try {
/*  353 */             Object scopedInstance = scope.get(beanName, () -> {
/*      */                   beforePrototypeCreation(beanName);
/*      */                   
/*      */                   try {
/*      */                     return createBean(beanName, mbd, args);
/*      */                   } finally {
/*      */                     afterPrototypeCreation(beanName);
/*      */                   } 
/*      */                 });
/*  362 */             bean = getObjectForBeanInstance(scopedInstance, name, beanName, mbd);
/*      */           }
/*  364 */           catch (IllegalStateException ex) {
/*  365 */             throw new BeanCreationException(beanName, "Scope '" + scopeName + "' is not active for the current thread; consider defining a scoped proxy for this bean if you intend to refer to it from a singleton", ex);
/*      */           
/*      */           }
/*      */         
/*      */         }
/*      */       
/*      */       }
/*  372 */       catch (BeansException ex) {
/*  373 */         cleanupAfterBeanCreationFailure(beanName);
/*  374 */         throw ex;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  379 */     if (requiredType != null && !requiredType.isInstance(bean)) {
/*      */       try {
/*  381 */         T convertedBean = (T)getTypeConverter().convertIfNecessary(bean, requiredType);
/*  382 */         if (convertedBean == null) {
/*  383 */           throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
/*      */         }
/*  385 */         return convertedBean;
/*      */       }
/*  387 */       catch (TypeMismatchException ex) {
/*  388 */         if (this.logger.isTraceEnabled()) {
/*  389 */           this.logger.trace("Failed to convert bean '" + name + "' to required type '" + 
/*  390 */               ClassUtils.getQualifiedName(requiredType) + "'", (Throwable)ex);
/*      */         }
/*  392 */         throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
/*      */       } 
/*      */     }
/*  395 */     return (T)bean;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsBean(String name) {
/*  400 */     String beanName = transformedBeanName(name);
/*  401 */     if (containsSingleton(beanName) || containsBeanDefinition(beanName)) {
/*  402 */       return (!BeanFactoryUtils.isFactoryDereference(name) || isFactoryBean(name));
/*      */     }
/*      */     
/*  405 */     BeanFactory parentBeanFactory = getParentBeanFactory();
/*  406 */     return (parentBeanFactory != null && parentBeanFactory.containsBean(originalBeanName(name)));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
/*  411 */     String beanName = transformedBeanName(name);
/*      */     
/*  413 */     Object beanInstance = getSingleton(beanName, false);
/*  414 */     if (beanInstance != null) {
/*  415 */       if (beanInstance instanceof FactoryBean) {
/*  416 */         return (BeanFactoryUtils.isFactoryDereference(name) || ((FactoryBean)beanInstance).isSingleton());
/*      */       }
/*      */       
/*  419 */       return !BeanFactoryUtils.isFactoryDereference(name);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  424 */     BeanFactory parentBeanFactory = getParentBeanFactory();
/*  425 */     if (parentBeanFactory != null && !containsBeanDefinition(beanName))
/*      */     {
/*  427 */       return parentBeanFactory.isSingleton(originalBeanName(name));
/*      */     }
/*      */     
/*  430 */     RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/*      */ 
/*      */     
/*  433 */     if (mbd.isSingleton()) {
/*  434 */       if (isFactoryBean(beanName, mbd)) {
/*  435 */         if (BeanFactoryUtils.isFactoryDereference(name)) {
/*  436 */           return true;
/*      */         }
/*  438 */         FactoryBean<?> factoryBean = (FactoryBean)getBean("&" + beanName);
/*  439 */         return factoryBean.isSingleton();
/*      */       } 
/*      */       
/*  442 */       return !BeanFactoryUtils.isFactoryDereference(name);
/*      */     } 
/*      */ 
/*      */     
/*  446 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
/*  452 */     String beanName = transformedBeanName(name);
/*      */     
/*  454 */     BeanFactory parentBeanFactory = getParentBeanFactory();
/*  455 */     if (parentBeanFactory != null && !containsBeanDefinition(beanName))
/*      */     {
/*  457 */       return parentBeanFactory.isPrototype(originalBeanName(name));
/*      */     }
/*      */     
/*  460 */     RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/*  461 */     if (mbd.isPrototype())
/*      */     {
/*  463 */       return (!BeanFactoryUtils.isFactoryDereference(name) || isFactoryBean(beanName, mbd));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  468 */     if (BeanFactoryUtils.isFactoryDereference(name)) {
/*  469 */       return false;
/*      */     }
/*  471 */     if (isFactoryBean(beanName, mbd)) {
/*  472 */       FactoryBean<?> fb = (FactoryBean)getBean("&" + beanName);
/*  473 */       if (System.getSecurityManager() != null) {
/*  474 */         return ((Boolean)AccessController.<Boolean>doPrivileged(() -> Boolean.valueOf(
/*  475 */               ((fb instanceof SmartFactoryBean && ((SmartFactoryBean)fb).isPrototype()) || !fb.isSingleton())), 
/*  476 */             getAccessControlContext())).booleanValue();
/*      */       }
/*      */       
/*  479 */       return ((fb instanceof SmartFactoryBean && ((SmartFactoryBean)fb).isPrototype()) || 
/*  480 */         !fb.isSingleton());
/*      */     } 
/*      */ 
/*      */     
/*  484 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException {
/*  490 */     String beanName = transformedBeanName(name);
/*      */ 
/*      */     
/*  493 */     Object beanInstance = getSingleton(beanName, false);
/*  494 */     if (beanInstance != null && beanInstance.getClass() != NullBean.class) {
/*  495 */       if (beanInstance instanceof FactoryBean) {
/*  496 */         if (!BeanFactoryUtils.isFactoryDereference(name)) {
/*  497 */           Class<?> type = getTypeForFactoryBean((FactoryBean)beanInstance);
/*  498 */           return (type != null && typeToMatch.isAssignableFrom(type));
/*      */         } 
/*      */         
/*  501 */         return typeToMatch.isInstance(beanInstance);
/*      */       } 
/*      */       
/*  504 */       if (!BeanFactoryUtils.isFactoryDereference(name)) {
/*  505 */         if (typeToMatch.isInstance(beanInstance))
/*      */         {
/*  507 */           return true;
/*      */         }
/*  509 */         if (typeToMatch.hasGenerics() && containsBeanDefinition(beanName)) {
/*      */           
/*  511 */           RootBeanDefinition rootBeanDefinition = getMergedLocalBeanDefinition(beanName);
/*  512 */           Class<?> targetType = rootBeanDefinition.getTargetType();
/*  513 */           if (targetType != null && targetType != ClassUtils.getUserClass(beanInstance)) {
/*      */             
/*  515 */             Class<?> clazz = typeToMatch.resolve();
/*  516 */             if (clazz != null && !clazz.isInstance(beanInstance)) {
/*  517 */               return false;
/*      */             }
/*  519 */             if (typeToMatch.isAssignableFrom(targetType)) {
/*  520 */               return true;
/*      */             }
/*      */           } 
/*  523 */           ResolvableType resolvableType1 = rootBeanDefinition.targetType;
/*  524 */           if (resolvableType1 == null) {
/*  525 */             resolvableType1 = rootBeanDefinition.factoryMethodReturnType;
/*      */           }
/*  527 */           return (resolvableType1 != null && typeToMatch.isAssignableFrom(resolvableType1));
/*      */         } 
/*      */       } 
/*  530 */       return false;
/*      */     } 
/*  532 */     if (containsSingleton(beanName) && !containsBeanDefinition(beanName))
/*      */     {
/*  534 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  538 */     BeanFactory parentBeanFactory = getParentBeanFactory();
/*  539 */     if (parentBeanFactory != null && !containsBeanDefinition(beanName))
/*      */     {
/*  541 */       return parentBeanFactory.isTypeMatch(originalBeanName(name), typeToMatch);
/*      */     }
/*      */ 
/*      */     
/*  545 */     RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/*      */     
/*  547 */     Class<?> classToMatch = typeToMatch.resolve();
/*  548 */     if (classToMatch == null) {
/*  549 */       classToMatch = FactoryBean.class;
/*      */     }
/*  551 */     (new Class[1])[0] = classToMatch; (new Class[2])[0] = FactoryBean.class; (new Class[2])[1] = classToMatch; Class<?>[] typesToMatch = (FactoryBean.class == classToMatch) ? new Class[1] : new Class[2];
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  556 */     BeanDefinitionHolder dbd = mbd.getDecoratedDefinition();
/*  557 */     if (dbd != null && !BeanFactoryUtils.isFactoryDereference(name)) {
/*  558 */       RootBeanDefinition tbd = getMergedBeanDefinition(dbd.getBeanName(), dbd.getBeanDefinition(), mbd);
/*  559 */       Class<?> targetClass = predictBeanType(dbd.getBeanName(), tbd, typesToMatch);
/*  560 */       if (targetClass != null && !FactoryBean.class.isAssignableFrom(targetClass)) {
/*  561 */         return typeToMatch.isAssignableFrom(targetClass);
/*      */       }
/*      */     } 
/*      */     
/*  565 */     Class<?> beanType = predictBeanType(beanName, mbd, typesToMatch);
/*  566 */     if (beanType == null) {
/*  567 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  571 */     if (FactoryBean.class.isAssignableFrom(beanType)) {
/*  572 */       if (!BeanFactoryUtils.isFactoryDereference(name) && beanInstance == null)
/*      */       {
/*  574 */         beanType = getTypeForFactoryBean(beanName, mbd);
/*  575 */         if (beanType == null) {
/*  576 */           return false;
/*      */         }
/*      */       }
/*      */     
/*  580 */     } else if (BeanFactoryUtils.isFactoryDereference(name)) {
/*      */ 
/*      */ 
/*      */       
/*  584 */       beanType = predictBeanType(beanName, mbd, new Class[] { FactoryBean.class });
/*  585 */       if (beanType == null || !FactoryBean.class.isAssignableFrom(beanType)) {
/*  586 */         return false;
/*      */       }
/*      */     } 
/*      */     
/*  590 */     ResolvableType resolvableType = mbd.targetType;
/*  591 */     if (resolvableType == null) {
/*  592 */       resolvableType = mbd.factoryMethodReturnType;
/*      */     }
/*  594 */     if (resolvableType != null && resolvableType.resolve() == beanType) {
/*  595 */       return typeToMatch.isAssignableFrom(resolvableType);
/*      */     }
/*  597 */     return typeToMatch.isAssignableFrom(beanType);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException {
/*  602 */     return isTypeMatch(name, ResolvableType.forRawClass(typeToMatch));
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
/*  608 */     String beanName = transformedBeanName(name);
/*      */ 
/*      */     
/*  611 */     Object beanInstance = getSingleton(beanName, false);
/*  612 */     if (beanInstance != null && beanInstance.getClass() != NullBean.class) {
/*  613 */       if (beanInstance instanceof FactoryBean && !BeanFactoryUtils.isFactoryDereference(name)) {
/*  614 */         return getTypeForFactoryBean((FactoryBean)beanInstance);
/*      */       }
/*      */       
/*  617 */       return beanInstance.getClass();
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  622 */     BeanFactory parentBeanFactory = getParentBeanFactory();
/*  623 */     if (parentBeanFactory != null && !containsBeanDefinition(beanName))
/*      */     {
/*  625 */       return parentBeanFactory.getType(originalBeanName(name));
/*      */     }
/*      */     
/*  628 */     RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/*      */ 
/*      */ 
/*      */     
/*  632 */     BeanDefinitionHolder dbd = mbd.getDecoratedDefinition();
/*  633 */     if (dbd != null && !BeanFactoryUtils.isFactoryDereference(name)) {
/*  634 */       RootBeanDefinition tbd = getMergedBeanDefinition(dbd.getBeanName(), dbd.getBeanDefinition(), mbd);
/*  635 */       Class<?> targetClass = predictBeanType(dbd.getBeanName(), tbd, new Class[0]);
/*  636 */       if (targetClass != null && !FactoryBean.class.isAssignableFrom(targetClass)) {
/*  637 */         return targetClass;
/*      */       }
/*      */     } 
/*      */     
/*  641 */     Class<?> beanClass = predictBeanType(beanName, mbd, new Class[0]);
/*      */ 
/*      */     
/*  644 */     if (beanClass != null && FactoryBean.class.isAssignableFrom(beanClass)) {
/*  645 */       if (!BeanFactoryUtils.isFactoryDereference(name))
/*      */       {
/*  647 */         return getTypeForFactoryBean(beanName, mbd);
/*      */       }
/*      */       
/*  650 */       return beanClass;
/*      */     } 
/*      */ 
/*      */     
/*  654 */     return !BeanFactoryUtils.isFactoryDereference(name) ? beanClass : null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getAliases(String name) {
/*  660 */     String beanName = transformedBeanName(name);
/*  661 */     List<String> aliases = new ArrayList<>();
/*  662 */     boolean factoryPrefix = name.startsWith("&");
/*  663 */     String fullBeanName = beanName;
/*  664 */     if (factoryPrefix) {
/*  665 */       fullBeanName = "&" + beanName;
/*      */     }
/*  667 */     if (!fullBeanName.equals(name)) {
/*  668 */       aliases.add(fullBeanName);
/*      */     }
/*  670 */     String[] retrievedAliases = super.getAliases(beanName);
/*  671 */     for (String retrievedAlias : retrievedAliases) {
/*  672 */       String alias = (factoryPrefix ? "&" : "") + retrievedAlias;
/*  673 */       if (!alias.equals(name)) {
/*  674 */         aliases.add(alias);
/*      */       }
/*      */     } 
/*  677 */     if (!containsSingleton(beanName) && !containsBeanDefinition(beanName)) {
/*  678 */       BeanFactory parentBeanFactory = getParentBeanFactory();
/*  679 */       if (parentBeanFactory != null) {
/*  680 */         aliases.addAll(Arrays.asList(parentBeanFactory.getAliases(fullBeanName)));
/*      */       }
/*      */     } 
/*  683 */     return StringUtils.toStringArray(aliases);
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
/*  694 */     return this.parentBeanFactory;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsLocalBean(String name) {
/*  699 */     String beanName = transformedBeanName(name);
/*  700 */     return ((containsSingleton(beanName) || containsBeanDefinition(beanName)) && (
/*  701 */       !BeanFactoryUtils.isFactoryDereference(name) || isFactoryBean(beanName)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setParentBeanFactory(@Nullable BeanFactory parentBeanFactory) {
/*  711 */     if (this.parentBeanFactory != null && this.parentBeanFactory != parentBeanFactory) {
/*  712 */       throw new IllegalStateException("Already associated with parent BeanFactory: " + this.parentBeanFactory);
/*      */     }
/*  714 */     this.parentBeanFactory = parentBeanFactory;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBeanClassLoader(@Nullable ClassLoader beanClassLoader) {
/*  719 */     this.beanClassLoader = (beanClassLoader != null) ? beanClassLoader : ClassUtils.getDefaultClassLoader();
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public ClassLoader getBeanClassLoader() {
/*  725 */     return this.beanClassLoader;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTempClassLoader(@Nullable ClassLoader tempClassLoader) {
/*  730 */     this.tempClassLoader = tempClassLoader;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public ClassLoader getTempClassLoader() {
/*  736 */     return this.tempClassLoader;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCacheBeanMetadata(boolean cacheBeanMetadata) {
/*  741 */     this.cacheBeanMetadata = cacheBeanMetadata;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isCacheBeanMetadata() {
/*  746 */     return this.cacheBeanMetadata;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBeanExpressionResolver(@Nullable BeanExpressionResolver resolver) {
/*  751 */     this.beanExpressionResolver = resolver;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public BeanExpressionResolver getBeanExpressionResolver() {
/*  757 */     return this.beanExpressionResolver;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setConversionService(@Nullable ConversionService conversionService) {
/*  762 */     this.conversionService = conversionService;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public ConversionService getConversionService() {
/*  768 */     return this.conversionService;
/*      */   }
/*      */ 
/*      */   
/*      */   public void addPropertyEditorRegistrar(PropertyEditorRegistrar registrar) {
/*  773 */     Assert.notNull(registrar, "PropertyEditorRegistrar must not be null");
/*  774 */     this.propertyEditorRegistrars.add(registrar);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<PropertyEditorRegistrar> getPropertyEditorRegistrars() {
/*  781 */     return this.propertyEditorRegistrars;
/*      */   }
/*      */ 
/*      */   
/*      */   public void registerCustomEditor(Class<?> requiredType, Class<? extends PropertyEditor> propertyEditorClass) {
/*  786 */     Assert.notNull(requiredType, "Required type must not be null");
/*  787 */     Assert.notNull(propertyEditorClass, "PropertyEditor class must not be null");
/*  788 */     this.customEditors.put(requiredType, propertyEditorClass);
/*      */   }
/*      */ 
/*      */   
/*      */   public void copyRegisteredEditorsTo(PropertyEditorRegistry registry) {
/*  793 */     registerCustomEditors(registry);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<Class<?>, Class<? extends PropertyEditor>> getCustomEditors() {
/*  800 */     return this.customEditors;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTypeConverter(TypeConverter typeConverter) {
/*  805 */     this.typeConverter = typeConverter;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected TypeConverter getCustomTypeConverter() {
/*  814 */     return this.typeConverter;
/*      */   }
/*      */ 
/*      */   
/*      */   public TypeConverter getTypeConverter() {
/*  819 */     TypeConverter customConverter = getCustomTypeConverter();
/*  820 */     if (customConverter != null) {
/*  821 */       return customConverter;
/*      */     }
/*      */ 
/*      */     
/*  825 */     SimpleTypeConverter typeConverter = new SimpleTypeConverter();
/*  826 */     typeConverter.setConversionService(getConversionService());
/*  827 */     registerCustomEditors((PropertyEditorRegistry)typeConverter);
/*  828 */     return (TypeConverter)typeConverter;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void addEmbeddedValueResolver(StringValueResolver valueResolver) {
/*  834 */     Assert.notNull(valueResolver, "StringValueResolver must not be null");
/*  835 */     this.embeddedValueResolvers.add(valueResolver);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasEmbeddedValueResolver() {
/*  840 */     return !this.embeddedValueResolvers.isEmpty();
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public String resolveEmbeddedValue(@Nullable String value) {
/*  846 */     if (value == null) {
/*  847 */       return null;
/*      */     }
/*  849 */     String result = value;
/*  850 */     for (StringValueResolver resolver : this.embeddedValueResolvers) {
/*  851 */       result = resolver.resolveStringValue(result);
/*  852 */       if (result == null) {
/*  853 */         return null;
/*      */       }
/*      */     } 
/*  856 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
/*  861 */     Assert.notNull(beanPostProcessor, "BeanPostProcessor must not be null");
/*      */     
/*  863 */     this.beanPostProcessors.remove(beanPostProcessor);
/*      */     
/*  865 */     if (beanPostProcessor instanceof org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor) {
/*  866 */       this.hasInstantiationAwareBeanPostProcessors = true;
/*      */     }
/*  868 */     if (beanPostProcessor instanceof org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor) {
/*  869 */       this.hasDestructionAwareBeanPostProcessors = true;
/*      */     }
/*      */     
/*  872 */     this.beanPostProcessors.add(beanPostProcessor);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getBeanPostProcessorCount() {
/*  877 */     return this.beanPostProcessors.size();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<BeanPostProcessor> getBeanPostProcessors() {
/*  885 */     return this.beanPostProcessors;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean hasInstantiationAwareBeanPostProcessors() {
/*  895 */     return this.hasInstantiationAwareBeanPostProcessors;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean hasDestructionAwareBeanPostProcessors() {
/*  905 */     return this.hasDestructionAwareBeanPostProcessors;
/*      */   }
/*      */ 
/*      */   
/*      */   public void registerScope(String scopeName, Scope scope) {
/*  910 */     Assert.notNull(scopeName, "Scope identifier must not be null");
/*  911 */     Assert.notNull(scope, "Scope must not be null");
/*  912 */     if ("singleton".equals(scopeName) || "prototype".equals(scopeName)) {
/*  913 */       throw new IllegalArgumentException("Cannot replace existing scopes 'singleton' and 'prototype'");
/*      */     }
/*  915 */     Scope previous = this.scopes.put(scopeName, scope);
/*  916 */     if (previous != null && previous != scope) {
/*  917 */       if (this.logger.isDebugEnabled()) {
/*  918 */         this.logger.debug("Replacing scope '" + scopeName + "' from [" + previous + "] to [" + scope + "]");
/*      */       
/*      */       }
/*      */     }
/*  922 */     else if (this.logger.isTraceEnabled()) {
/*  923 */       this.logger.trace("Registering scope '" + scopeName + "' with implementation [" + scope + "]");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getRegisteredScopeNames() {
/*  930 */     return StringUtils.toStringArray(this.scopes.keySet());
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Scope getRegisteredScope(String scopeName) {
/*  936 */     Assert.notNull(scopeName, "Scope identifier must not be null");
/*  937 */     return this.scopes.get(scopeName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSecurityContextProvider(SecurityContextProvider securityProvider) {
/*  946 */     this.securityContextProvider = securityProvider;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AccessControlContext getAccessControlContext() {
/*  955 */     return (this.securityContextProvider != null) ? this.securityContextProvider
/*  956 */       .getAccessControlContext() : 
/*  957 */       AccessController.getContext();
/*      */   }
/*      */ 
/*      */   
/*      */   public void copyConfigurationFrom(ConfigurableBeanFactory otherFactory) {
/*  962 */     Assert.notNull(otherFactory, "BeanFactory must not be null");
/*  963 */     setBeanClassLoader(otherFactory.getBeanClassLoader());
/*  964 */     setCacheBeanMetadata(otherFactory.isCacheBeanMetadata());
/*  965 */     setBeanExpressionResolver(otherFactory.getBeanExpressionResolver());
/*  966 */     setConversionService(otherFactory.getConversionService());
/*  967 */     if (otherFactory instanceof AbstractBeanFactory) {
/*  968 */       AbstractBeanFactory otherAbstractFactory = (AbstractBeanFactory)otherFactory;
/*  969 */       this.propertyEditorRegistrars.addAll(otherAbstractFactory.propertyEditorRegistrars);
/*  970 */       this.customEditors.putAll(otherAbstractFactory.customEditors);
/*  971 */       this.typeConverter = otherAbstractFactory.typeConverter;
/*  972 */       this.beanPostProcessors.addAll(otherAbstractFactory.beanPostProcessors);
/*  973 */       this.hasInstantiationAwareBeanPostProcessors = (this.hasInstantiationAwareBeanPostProcessors || otherAbstractFactory.hasInstantiationAwareBeanPostProcessors);
/*      */       
/*  975 */       this.hasDestructionAwareBeanPostProcessors = (this.hasDestructionAwareBeanPostProcessors || otherAbstractFactory.hasDestructionAwareBeanPostProcessors);
/*      */       
/*  977 */       this.scopes.putAll(otherAbstractFactory.scopes);
/*  978 */       this.securityContextProvider = otherAbstractFactory.securityContextProvider;
/*      */     } else {
/*      */       
/*  981 */       setTypeConverter(otherFactory.getTypeConverter());
/*  982 */       String[] otherScopeNames = otherFactory.getRegisteredScopeNames();
/*  983 */       for (String scopeName : otherScopeNames) {
/*  984 */         this.scopes.put(scopeName, otherFactory.getRegisteredScope(scopeName));
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
/*      */   public BeanDefinition getMergedBeanDefinition(String name) throws BeansException {
/* 1002 */     String beanName = transformedBeanName(name);
/*      */     
/* 1004 */     if (!containsBeanDefinition(beanName) && getParentBeanFactory() instanceof ConfigurableBeanFactory) {
/* 1005 */       return ((ConfigurableBeanFactory)getParentBeanFactory()).getMergedBeanDefinition(beanName);
/*      */     }
/*      */     
/* 1008 */     return getMergedLocalBeanDefinition(beanName);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFactoryBean(String name) throws NoSuchBeanDefinitionException {
/* 1013 */     String beanName = transformedBeanName(name);
/* 1014 */     Object beanInstance = getSingleton(beanName, false);
/* 1015 */     if (beanInstance != null) {
/* 1016 */       return beanInstance instanceof FactoryBean;
/*      */     }
/*      */     
/* 1019 */     if (!containsBeanDefinition(beanName) && getParentBeanFactory() instanceof ConfigurableBeanFactory)
/*      */     {
/* 1021 */       return ((ConfigurableBeanFactory)getParentBeanFactory()).isFactoryBean(name);
/*      */     }
/* 1023 */     return isFactoryBean(beanName, getMergedLocalBeanDefinition(beanName));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isActuallyInCreation(String beanName) {
/* 1028 */     return (isSingletonCurrentlyInCreation(beanName) || isPrototypeCurrentlyInCreation(beanName));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isPrototypeCurrentlyInCreation(String beanName) {
/* 1037 */     Object curVal = this.prototypesCurrentlyInCreation.get();
/* 1038 */     return (curVal != null && (curVal
/* 1039 */       .equals(beanName) || (curVal instanceof Set && ((Set)curVal).contains(beanName))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void beforePrototypeCreation(String beanName) {
/* 1050 */     Object curVal = this.prototypesCurrentlyInCreation.get();
/* 1051 */     if (curVal == null) {
/* 1052 */       this.prototypesCurrentlyInCreation.set(beanName);
/*      */     }
/* 1054 */     else if (curVal instanceof String) {
/* 1055 */       Set<String> beanNameSet = new HashSet<>(2);
/* 1056 */       beanNameSet.add((String)curVal);
/* 1057 */       beanNameSet.add(beanName);
/* 1058 */       this.prototypesCurrentlyInCreation.set(beanNameSet);
/*      */     } else {
/*      */       
/* 1061 */       Set<String> beanNameSet = (Set<String>)curVal;
/* 1062 */       beanNameSet.add(beanName);
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
/*      */   protected void afterPrototypeCreation(String beanName) {
/* 1074 */     Object curVal = this.prototypesCurrentlyInCreation.get();
/* 1075 */     if (curVal instanceof String) {
/* 1076 */       this.prototypesCurrentlyInCreation.remove();
/*      */     }
/* 1078 */     else if (curVal instanceof Set) {
/* 1079 */       Set<String> beanNameSet = (Set<String>)curVal;
/* 1080 */       beanNameSet.remove(beanName);
/* 1081 */       if (beanNameSet.isEmpty()) {
/* 1082 */         this.prototypesCurrentlyInCreation.remove();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void destroyBean(String beanName, Object beanInstance) {
/* 1089 */     destroyBean(beanName, beanInstance, getMergedLocalBeanDefinition(beanName));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void destroyBean(String beanName, Object bean, RootBeanDefinition mbd) {
/* 1100 */     (new DisposableBeanAdapter(bean, beanName, mbd, getBeanPostProcessors(), getAccessControlContext())).destroy();
/*      */   }
/*      */ 
/*      */   
/*      */   public void destroyScopedBean(String beanName) {
/* 1105 */     RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/* 1106 */     if (mbd.isSingleton() || mbd.isPrototype()) {
/* 1107 */       throw new IllegalArgumentException("Bean name '" + beanName + "' does not correspond to an object in a mutable scope");
/*      */     }
/*      */     
/* 1110 */     String scopeName = mbd.getScope();
/* 1111 */     Scope scope = this.scopes.get(scopeName);
/* 1112 */     if (scope == null) {
/* 1113 */       throw new IllegalStateException("No Scope SPI registered for scope name '" + scopeName + "'");
/*      */     }
/* 1115 */     Object bean = scope.remove(beanName);
/* 1116 */     if (bean != null) {
/* 1117 */       destroyBean(beanName, bean, mbd);
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
/*      */   protected String transformedBeanName(String name) {
/* 1133 */     return canonicalName(BeanFactoryUtils.transformedBeanName(name));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String originalBeanName(String name) {
/* 1142 */     String beanName = transformedBeanName(name);
/* 1143 */     if (name.startsWith("&")) {
/* 1144 */       beanName = "&" + beanName;
/*      */     }
/* 1146 */     return beanName;
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
/*      */   protected void initBeanWrapper(BeanWrapper bw) {
/* 1158 */     bw.setConversionService(getConversionService());
/* 1159 */     registerCustomEditors((PropertyEditorRegistry)bw);
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
/*      */   protected void registerCustomEditors(PropertyEditorRegistry registry) {
/* 1171 */     PropertyEditorRegistrySupport registrySupport = (registry instanceof PropertyEditorRegistrySupport) ? (PropertyEditorRegistrySupport)registry : null;
/*      */     
/* 1173 */     if (registrySupport != null) {
/* 1174 */       registrySupport.useConfigValueEditors();
/*      */     }
/* 1176 */     if (!this.propertyEditorRegistrars.isEmpty()) {
/* 1177 */       for (PropertyEditorRegistrar registrar : this.propertyEditorRegistrars) {
/*      */         try {
/* 1179 */           registrar.registerCustomEditors(registry);
/*      */         }
/* 1181 */         catch (BeanCreationException ex) {
/* 1182 */           Throwable rootCause = ex.getMostSpecificCause();
/* 1183 */           if (rootCause instanceof BeanCurrentlyInCreationException) {
/* 1184 */             BeanCreationException bce = (BeanCreationException)rootCause;
/* 1185 */             String bceBeanName = bce.getBeanName();
/* 1186 */             if (bceBeanName != null && isCurrentlyInCreation(bceBeanName)) {
/* 1187 */               if (this.logger.isDebugEnabled()) {
/* 1188 */                 this.logger.debug("PropertyEditorRegistrar [" + registrar.getClass().getName() + "] failed because it tried to obtain currently created bean '" + ex
/*      */                     
/* 1190 */                     .getBeanName() + "': " + ex.getMessage());
/*      */               }
/* 1192 */               onSuppressedException((Exception)ex);
/*      */               continue;
/*      */             } 
/*      */           } 
/* 1196 */           throw ex;
/*      */         } 
/*      */       } 
/*      */     }
/* 1200 */     if (!this.customEditors.isEmpty()) {
/* 1201 */       this.customEditors.forEach((requiredType, editorClass) -> registry.registerCustomEditor(requiredType, (PropertyEditor)BeanUtils.instantiateClass(editorClass)));
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
/*      */   protected RootBeanDefinition getMergedLocalBeanDefinition(String beanName) throws BeansException {
/* 1217 */     RootBeanDefinition mbd = this.mergedBeanDefinitions.get(beanName);
/* 1218 */     if (mbd != null) {
/* 1219 */       return mbd;
/*      */     }
/* 1221 */     return getMergedBeanDefinition(beanName, getBeanDefinition(beanName));
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
/*      */   protected RootBeanDefinition getMergedBeanDefinition(String beanName, BeanDefinition bd) throws BeanDefinitionStoreException {
/* 1235 */     return getMergedBeanDefinition(beanName, bd, (BeanDefinition)null);
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
/*      */   protected RootBeanDefinition getMergedBeanDefinition(String beanName, BeanDefinition bd, @Nullable BeanDefinition containingBd) throws BeanDefinitionStoreException {
/* 1252 */     synchronized (this.mergedBeanDefinitions) {
/* 1253 */       RootBeanDefinition mbd = null;
/*      */ 
/*      */       
/* 1256 */       if (containingBd == null) {
/* 1257 */         mbd = this.mergedBeanDefinitions.get(beanName);
/*      */       }
/*      */       
/* 1260 */       if (mbd == null) {
/* 1261 */         if (bd.getParentName() == null) {
/*      */           
/* 1263 */           if (bd instanceof RootBeanDefinition) {
/* 1264 */             mbd = ((RootBeanDefinition)bd).cloneBeanDefinition();
/*      */           } else {
/*      */             
/* 1267 */             mbd = new RootBeanDefinition(bd);
/*      */           } 
/*      */         } else {
/*      */           BeanDefinition pbd;
/*      */ 
/*      */           
/*      */           try {
/* 1274 */             String parentBeanName = transformedBeanName(bd.getParentName());
/* 1275 */             if (!beanName.equals(parentBeanName)) {
/* 1276 */               pbd = getMergedBeanDefinition(parentBeanName);
/*      */             } else {
/*      */               
/* 1279 */               BeanFactory parent = getParentBeanFactory();
/* 1280 */               if (parent instanceof ConfigurableBeanFactory) {
/* 1281 */                 pbd = ((ConfigurableBeanFactory)parent).getMergedBeanDefinition(parentBeanName);
/*      */               } else {
/*      */                 
/* 1284 */                 throw new NoSuchBeanDefinitionException(parentBeanName, "Parent name '" + parentBeanName + "' is equal to bean name '" + beanName + "': cannot be resolved without an AbstractBeanFactory parent");
/*      */               }
/*      */             
/*      */             }
/*      */           
/*      */           }
/* 1290 */           catch (NoSuchBeanDefinitionException ex) {
/* 1291 */             throw new BeanDefinitionStoreException(bd.getResourceDescription(), beanName, "Could not resolve parent bean definition '" + bd
/* 1292 */                 .getParentName() + "'", ex);
/*      */           } 
/*      */           
/* 1295 */           mbd = new RootBeanDefinition(pbd);
/* 1296 */           mbd.overrideFrom(bd);
/*      */         } 
/*      */ 
/*      */         
/* 1300 */         if (!StringUtils.hasLength(mbd.getScope())) {
/* 1301 */           mbd.setScope("singleton");
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1308 */         if (containingBd != null && !containingBd.isSingleton() && mbd.isSingleton()) {
/* 1309 */           mbd.setScope(containingBd.getScope());
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 1314 */         if (containingBd == null && isCacheBeanMetadata()) {
/* 1315 */           this.mergedBeanDefinitions.put(beanName, mbd);
/*      */         }
/*      */       } 
/*      */       
/* 1319 */       return mbd;
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
/*      */   protected void checkMergedBeanDefinition(RootBeanDefinition mbd, String beanName, @Nullable Object[] args) throws BeanDefinitionStoreException {
/* 1334 */     if (mbd.isAbstract()) {
/* 1335 */       throw new BeanIsAbstractException(beanName);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void clearMergedBeanDefinition(String beanName) {
/* 1345 */     this.mergedBeanDefinitions.remove(beanName);
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
/*      */   public void clearMetadataCache() {
/* 1357 */     this.mergedBeanDefinitions.keySet().removeIf(bean -> !isBeanEligibleForMetadataCaching(bean));
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
/*      */   protected Class<?> resolveBeanClass(RootBeanDefinition mbd, String beanName, Class<?>... typesToMatch) throws CannotLoadBeanClassException {
/*      */     try {
/* 1376 */       if (mbd.hasBeanClass()) {
/* 1377 */         return mbd.getBeanClass();
/*      */       }
/* 1379 */       if (System.getSecurityManager() != null) {
/* 1380 */         return AccessController.<Class<?>>doPrivileged(() -> doResolveBeanClass(mbd, typesToMatch), 
/* 1381 */             getAccessControlContext());
/*      */       }
/*      */       
/* 1384 */       return doResolveBeanClass(mbd, typesToMatch);
/*      */     
/*      */     }
/* 1387 */     catch (PrivilegedActionException pae) {
/* 1388 */       ClassNotFoundException ex = (ClassNotFoundException)pae.getException();
/* 1389 */       throw new CannotLoadBeanClassException(mbd.getResourceDescription(), beanName, mbd.getBeanClassName(), ex);
/*      */     }
/* 1391 */     catch (ClassNotFoundException ex) {
/* 1392 */       throw new CannotLoadBeanClassException(mbd.getResourceDescription(), beanName, mbd.getBeanClassName(), ex);
/*      */     }
/* 1394 */     catch (LinkageError err) {
/* 1395 */       throw new CannotLoadBeanClassException(mbd.getResourceDescription(), beanName, mbd.getBeanClassName(), err);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private Class<?> doResolveBeanClass(RootBeanDefinition mbd, Class<?>... typesToMatch) throws ClassNotFoundException {
/* 1403 */     ClassLoader beanClassLoader = getBeanClassLoader();
/* 1404 */     ClassLoader dynamicLoader = beanClassLoader;
/* 1405 */     boolean freshResolve = false;
/*      */     
/* 1407 */     if (!ObjectUtils.isEmpty((Object[])typesToMatch)) {
/*      */ 
/*      */       
/* 1410 */       ClassLoader tempClassLoader = getTempClassLoader();
/* 1411 */       if (tempClassLoader != null) {
/* 1412 */         dynamicLoader = tempClassLoader;
/* 1413 */         freshResolve = true;
/* 1414 */         if (tempClassLoader instanceof DecoratingClassLoader) {
/* 1415 */           DecoratingClassLoader dcl = (DecoratingClassLoader)tempClassLoader;
/* 1416 */           for (Class<?> typeToMatch : typesToMatch) {
/* 1417 */             dcl.excludeClass(typeToMatch.getName());
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1423 */     String className = mbd.getBeanClassName();
/* 1424 */     if (className != null) {
/* 1425 */       Object evaluated = evaluateBeanDefinitionString(className, mbd);
/* 1426 */       if (!className.equals(evaluated)) {
/*      */         
/* 1428 */         if (evaluated instanceof Class) {
/* 1429 */           return (Class)evaluated;
/*      */         }
/* 1431 */         if (evaluated instanceof String) {
/* 1432 */           className = (String)evaluated;
/* 1433 */           freshResolve = true;
/*      */         } else {
/*      */           
/* 1436 */           throw new IllegalStateException("Invalid class name expression result: " + evaluated);
/*      */         } 
/*      */       } 
/* 1439 */       if (freshResolve) {
/*      */ 
/*      */         
/* 1442 */         if (dynamicLoader != null) {
/*      */           try {
/* 1444 */             return dynamicLoader.loadClass(className);
/*      */           }
/* 1446 */           catch (ClassNotFoundException ex) {
/* 1447 */             if (this.logger.isTraceEnabled()) {
/* 1448 */               this.logger.trace("Could not load class [" + className + "] from " + dynamicLoader + ": " + ex);
/*      */             }
/*      */           } 
/*      */         }
/* 1452 */         return ClassUtils.forName(className, dynamicLoader);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1457 */     return mbd.resolveBeanClass(beanClassLoader);
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
/*      */   protected Object evaluateBeanDefinitionString(@Nullable String value, @Nullable BeanDefinition beanDefinition) {
/* 1470 */     if (this.beanExpressionResolver == null) {
/* 1471 */       return value;
/*      */     }
/*      */     
/* 1474 */     Scope scope = null;
/* 1475 */     if (beanDefinition != null) {
/* 1476 */       String scopeName = beanDefinition.getScope();
/* 1477 */       if (scopeName != null) {
/* 1478 */         scope = getRegisteredScope(scopeName);
/*      */       }
/*      */     } 
/* 1481 */     return this.beanExpressionResolver.evaluate(value, new BeanExpressionContext(this, scope));
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
/*      */   @Nullable
/*      */   protected Class<?> predictBeanType(String beanName, RootBeanDefinition mbd, Class<?>... typesToMatch) {
/* 1502 */     Class<?> targetType = mbd.getTargetType();
/* 1503 */     if (targetType != null) {
/* 1504 */       return targetType;
/*      */     }
/* 1506 */     if (mbd.getFactoryMethodName() != null) {
/* 1507 */       return null;
/*      */     }
/* 1509 */     return resolveBeanClass(mbd, beanName, typesToMatch);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isFactoryBean(String beanName, RootBeanDefinition mbd) {
/* 1518 */     Class<?> beanType = predictBeanType(beanName, mbd, new Class[] { FactoryBean.class });
/* 1519 */     return (beanType != null && FactoryBean.class.isAssignableFrom(beanType));
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
/*      */   @Nullable
/*      */   protected Class<?> getTypeForFactoryBean(String beanName, RootBeanDefinition mbd) {
/* 1539 */     if (!mbd.isSingleton()) {
/* 1540 */       return null;
/*      */     }
/*      */     try {
/* 1543 */       FactoryBean<?> factoryBean = doGetBean("&" + beanName, FactoryBean.class, (Object[])null, true);
/* 1544 */       return getTypeForFactoryBean(factoryBean);
/*      */     }
/* 1546 */     catch (BeanCreationException ex) {
/* 1547 */       if (ex.contains(BeanCurrentlyInCreationException.class)) {
/* 1548 */         if (this.logger.isTraceEnabled()) {
/* 1549 */           this.logger.trace("Bean currently in creation on FactoryBean type check: " + ex);
/*      */         }
/*      */       }
/* 1552 */       else if (mbd.isLazyInit()) {
/* 1553 */         if (this.logger.isTraceEnabled()) {
/* 1554 */           this.logger.trace("Bean creation exception on lazy FactoryBean type check: " + ex);
/*      */         
/*      */         }
/*      */       }
/* 1558 */       else if (this.logger.isDebugEnabled()) {
/* 1559 */         this.logger.debug("Bean creation exception on non-lazy FactoryBean type check: " + ex);
/*      */       } 
/*      */       
/* 1562 */       onSuppressedException((Exception)ex);
/* 1563 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void markBeanAsCreated(String beanName) {
/* 1574 */     if (!this.alreadyCreated.contains(beanName)) {
/* 1575 */       synchronized (this.mergedBeanDefinitions) {
/* 1576 */         if (!this.alreadyCreated.contains(beanName)) {
/*      */ 
/*      */           
/* 1579 */           clearMergedBeanDefinition(beanName);
/* 1580 */           this.alreadyCreated.add(beanName);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void cleanupAfterBeanCreationFailure(String beanName) {
/* 1591 */     synchronized (this.mergedBeanDefinitions) {
/* 1592 */       this.alreadyCreated.remove(beanName);
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
/*      */   protected boolean isBeanEligibleForMetadataCaching(String beanName) {
/* 1604 */     return this.alreadyCreated.contains(beanName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean removeSingletonIfCreatedForTypeCheckOnly(String beanName) {
/* 1614 */     if (!this.alreadyCreated.contains(beanName)) {
/* 1615 */       removeSingleton(beanName);
/* 1616 */       return true;
/*      */     } 
/*      */     
/* 1619 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean hasBeanCreationStarted() {
/* 1630 */     return !this.alreadyCreated.isEmpty();
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
/*      */   protected Object getObjectForBeanInstance(Object beanInstance, String name, String beanName, @Nullable RootBeanDefinition mbd) {
/* 1646 */     if (BeanFactoryUtils.isFactoryDereference(name)) {
/* 1647 */       if (beanInstance instanceof NullBean) {
/* 1648 */         return beanInstance;
/*      */       }
/* 1650 */       if (!(beanInstance instanceof FactoryBean)) {
/* 1651 */         throw new BeanIsNotAFactoryException(beanName, beanInstance.getClass());
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1658 */     if (!(beanInstance instanceof FactoryBean) || BeanFactoryUtils.isFactoryDereference(name)) {
/* 1659 */       return beanInstance;
/*      */     }
/*      */     
/* 1662 */     Object object = null;
/* 1663 */     if (mbd == null) {
/* 1664 */       object = getCachedObjectForFactoryBean(beanName);
/*      */     }
/* 1666 */     if (object == null) {
/*      */       
/* 1668 */       FactoryBean<?> factory = (FactoryBean)beanInstance;
/*      */       
/* 1670 */       if (mbd == null && containsBeanDefinition(beanName)) {
/* 1671 */         mbd = getMergedLocalBeanDefinition(beanName);
/*      */       }
/* 1673 */       boolean synthetic = (mbd != null && mbd.isSynthetic());
/* 1674 */       object = getObjectFromFactoryBean(factory, beanName, !synthetic);
/*      */     } 
/* 1676 */     return object;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isBeanNameInUse(String beanName) {
/* 1686 */     return (isAlias(beanName) || containsLocalBean(beanName) || hasDependentBean(beanName));
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
/*      */   protected boolean requiresDestruction(Object bean, RootBeanDefinition mbd) {
/* 1700 */     return (bean.getClass() != NullBean.class && (
/* 1701 */       DisposableBeanAdapter.hasDestroyMethod(bean, mbd) || (hasDestructionAwareBeanPostProcessors() && 
/* 1702 */       DisposableBeanAdapter.hasApplicableProcessors(bean, getBeanPostProcessors()))));
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
/*      */   protected void registerDisposableBeanIfNecessary(String beanName, Object bean, RootBeanDefinition mbd) {
/* 1718 */     AccessControlContext acc = (System.getSecurityManager() != null) ? getAccessControlContext() : null;
/* 1719 */     if (!mbd.isPrototype() && requiresDestruction(bean, mbd))
/* 1720 */       if (mbd.isSingleton()) {
/*      */ 
/*      */ 
/*      */         
/* 1724 */         registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, mbd, 
/* 1725 */               getBeanPostProcessors(), acc));
/*      */       }
/*      */       else {
/*      */         
/* 1729 */         Scope scope = this.scopes.get(mbd.getScope());
/* 1730 */         if (scope == null) {
/* 1731 */           throw new IllegalStateException("No Scope registered for scope name '" + mbd.getScope() + "'");
/*      */         }
/* 1733 */         scope.registerDestructionCallback(beanName, new DisposableBeanAdapter(bean, beanName, mbd, 
/* 1734 */               getBeanPostProcessors(), acc));
/*      */       }  
/*      */   }
/*      */   
/*      */   public AbstractBeanFactory() {}
/*      */   
/*      */   protected abstract boolean containsBeanDefinition(String paramString);
/*      */   
/*      */   protected abstract BeanDefinition getBeanDefinition(String paramString) throws BeansException;
/*      */   
/*      */   protected abstract Object createBean(String paramString, RootBeanDefinition paramRootBeanDefinition, @Nullable Object[] paramArrayOfObject) throws BeanCreationException;
/*      */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/AbstractBeanFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */