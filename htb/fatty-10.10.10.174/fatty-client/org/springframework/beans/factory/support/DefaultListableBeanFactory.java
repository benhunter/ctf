/*      */ package org.springframework.beans.factory.support;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.NotSerializableException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectStreamException;
/*      */ import java.io.Serializable;
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.lang.ref.Reference;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.lang.reflect.Method;
/*      */ import java.security.AccessController;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.IdentityHashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.stream.Stream;
/*      */ import javax.inject.Provider;
/*      */ import org.springframework.beans.BeanUtils;
/*      */ import org.springframework.beans.BeansException;
/*      */ import org.springframework.beans.TypeConverter;
/*      */ import org.springframework.beans.factory.BeanCreationException;
/*      */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*      */ import org.springframework.beans.factory.BeanFactory;
/*      */ import org.springframework.beans.factory.BeanFactoryAware;
/*      */ import org.springframework.beans.factory.BeanFactoryUtils;
/*      */ import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
/*      */ import org.springframework.beans.factory.FactoryBean;
/*      */ import org.springframework.beans.factory.InjectionPoint;
/*      */ import org.springframework.beans.factory.ListableBeanFactory;
/*      */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*      */ import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
/*      */ import org.springframework.beans.factory.ObjectFactory;
/*      */ import org.springframework.beans.factory.ObjectProvider;
/*      */ import org.springframework.beans.factory.SmartFactoryBean;
/*      */ import org.springframework.beans.factory.SmartInitializingSingleton;
/*      */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
/*      */ import org.springframework.beans.factory.config.BeanDefinition;
/*      */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*      */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*      */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*      */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*      */ import org.springframework.beans.factory.config.DependencyDescriptor;
/*      */ import org.springframework.beans.factory.config.NamedBeanHolder;
/*      */ import org.springframework.core.OrderComparator;
/*      */ import org.springframework.core.ResolvableType;
/*      */ import org.springframework.core.annotation.AnnotationUtils;
/*      */ import org.springframework.lang.Nullable;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.CompositeIterator;
/*      */ import org.springframework.util.ObjectUtils;
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
/*      */ public class DefaultListableBeanFactory
/*      */   extends AbstractAutowireCapableBeanFactory
/*      */   implements ConfigurableListableBeanFactory, BeanDefinitionRegistry, Serializable
/*      */ {
/*      */   @Nullable
/*      */   private static Class<?> javaxInjectProviderClass;
/*      */   
/*      */   static {
/*      */     try {
/*  128 */       javaxInjectProviderClass = ClassUtils.forName("javax.inject.Provider", DefaultListableBeanFactory.class.getClassLoader());
/*      */     }
/*  130 */     catch (ClassNotFoundException ex) {
/*      */       
/*  132 */       javaxInjectProviderClass = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  138 */   private static final Map<String, Reference<DefaultListableBeanFactory>> serializableFactories = new ConcurrentHashMap<>(8);
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private String serializationId;
/*      */ 
/*      */   
/*      */   private boolean allowBeanDefinitionOverriding = true;
/*      */ 
/*      */   
/*      */   private boolean allowEagerClassLoading = true;
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private Comparator<Object> dependencyComparator;
/*      */ 
/*      */   
/*  156 */   private AutowireCandidateResolver autowireCandidateResolver = new SimpleAutowireCandidateResolver();
/*      */ 
/*      */   
/*  159 */   private final Map<Class<?>, Object> resolvableDependencies = new ConcurrentHashMap<>(16);
/*      */ 
/*      */   
/*  162 */   private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
/*      */ 
/*      */   
/*  165 */   private final Map<Class<?>, String[]> allBeanNamesByType = (Map)new ConcurrentHashMap<>(64);
/*      */ 
/*      */   
/*  168 */   private final Map<Class<?>, String[]> singletonBeanNamesByType = (Map)new ConcurrentHashMap<>(64);
/*      */ 
/*      */   
/*  171 */   private volatile List<String> beanDefinitionNames = new ArrayList<>(256);
/*      */ 
/*      */   
/*  174 */   private volatile Set<String> manualSingletonNames = new LinkedHashSet<>(16);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private volatile String[] frozenBeanDefinitionNames;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private volatile boolean configurationFrozen = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DefaultListableBeanFactory(@Nullable BeanFactory parentBeanFactory) {
/*  196 */     super(parentBeanFactory);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSerializationId(@Nullable String serializationId) {
/*  205 */     if (serializationId != null) {
/*  206 */       serializableFactories.put(serializationId, new WeakReference<>(this));
/*      */     }
/*  208 */     else if (this.serializationId != null) {
/*  209 */       serializableFactories.remove(this.serializationId);
/*      */     } 
/*  211 */     this.serializationId = serializationId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public String getSerializationId() {
/*  221 */     return this.serializationId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAllowBeanDefinitionOverriding(boolean allowBeanDefinitionOverriding) {
/*  232 */     this.allowBeanDefinitionOverriding = allowBeanDefinitionOverriding;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAllowBeanDefinitionOverriding() {
/*  241 */     return this.allowBeanDefinitionOverriding;
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
/*      */   public void setAllowEagerClassLoading(boolean allowEagerClassLoading) {
/*  255 */     this.allowEagerClassLoading = allowEagerClassLoading;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAllowEagerClassLoading() {
/*  264 */     return this.allowEagerClassLoading;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDependencyComparator(@Nullable Comparator<Object> dependencyComparator) {
/*  274 */     this.dependencyComparator = dependencyComparator;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Comparator<Object> getDependencyComparator() {
/*  283 */     return this.dependencyComparator;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAutowireCandidateResolver(AutowireCandidateResolver autowireCandidateResolver) {
/*  292 */     Assert.notNull(autowireCandidateResolver, "AutowireCandidateResolver must not be null");
/*  293 */     if (autowireCandidateResolver instanceof BeanFactoryAware) {
/*  294 */       if (System.getSecurityManager() != null) {
/*  295 */         AccessController.doPrivileged(() -> {
/*      */               ((BeanFactoryAware)autowireCandidateResolver).setBeanFactory((BeanFactory)this);
/*      */               return null;
/*  298 */             }getAccessControlContext());
/*      */       } else {
/*      */         
/*  301 */         ((BeanFactoryAware)autowireCandidateResolver).setBeanFactory((BeanFactory)this);
/*      */       } 
/*      */     }
/*  304 */     this.autowireCandidateResolver = autowireCandidateResolver;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AutowireCandidateResolver getAutowireCandidateResolver() {
/*  311 */     return this.autowireCandidateResolver;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void copyConfigurationFrom(ConfigurableBeanFactory otherFactory) {
/*  317 */     super.copyConfigurationFrom(otherFactory);
/*  318 */     if (otherFactory instanceof DefaultListableBeanFactory) {
/*  319 */       DefaultListableBeanFactory otherListableFactory = (DefaultListableBeanFactory)otherFactory;
/*  320 */       this.allowBeanDefinitionOverriding = otherListableFactory.allowBeanDefinitionOverriding;
/*  321 */       this.allowEagerClassLoading = otherListableFactory.allowEagerClassLoading;
/*  322 */       this.dependencyComparator = otherListableFactory.dependencyComparator;
/*      */       
/*  324 */       setAutowireCandidateResolver((AutowireCandidateResolver)BeanUtils.instantiateClass(getAutowireCandidateResolver().getClass()));
/*      */       
/*  326 */       this.resolvableDependencies.putAll(otherListableFactory.resolvableDependencies);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T getBean(Class<T> requiredType) throws BeansException {
/*  337 */     return getBean(requiredType, (Object[])null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T getBean(Class<T> requiredType, @Nullable Object... args) throws BeansException {
/*  343 */     Assert.notNull(requiredType, "Required type must not be null");
/*  344 */     Object resolved = resolveBean(ResolvableType.forRawClass(requiredType), args, false);
/*  345 */     if (resolved == null) {
/*  346 */       throw new NoSuchBeanDefinitionException(requiredType);
/*      */     }
/*  348 */     return (T)resolved;
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> ObjectProvider<T> getBeanProvider(Class<T> requiredType) throws BeansException {
/*  353 */     Assert.notNull(requiredType, "Required type must not be null");
/*  354 */     return getBeanProvider(ResolvableType.forRawClass(requiredType));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> ObjectProvider<T> getBeanProvider(final ResolvableType requiredType) {
/*  360 */     return new BeanObjectProvider<T>()
/*      */       {
/*      */         public T getObject() throws BeansException {
/*  363 */           T resolved = DefaultListableBeanFactory.this.resolveBean(requiredType, (Object[])null, false);
/*  364 */           if (resolved == null) {
/*  365 */             throw new NoSuchBeanDefinitionException(requiredType);
/*      */           }
/*  367 */           return resolved;
/*      */         }
/*      */         
/*      */         public T getObject(Object... args) throws BeansException {
/*  371 */           T resolved = DefaultListableBeanFactory.this.resolveBean(requiredType, args, false);
/*  372 */           if (resolved == null) {
/*  373 */             throw new NoSuchBeanDefinitionException(requiredType);
/*      */           }
/*  375 */           return resolved;
/*      */         }
/*      */         
/*      */         @Nullable
/*      */         public T getIfAvailable() throws BeansException {
/*  380 */           return DefaultListableBeanFactory.this.resolveBean(requiredType, (Object[])null, false);
/*      */         }
/*      */         
/*      */         @Nullable
/*      */         public T getIfUnique() throws BeansException {
/*  385 */           return DefaultListableBeanFactory.this.resolveBean(requiredType, (Object[])null, true);
/*      */         }
/*      */         
/*      */         public Stream<T> stream() {
/*  389 */           return Arrays.<String>stream(DefaultListableBeanFactory.this.getBeanNamesForTypedStream(requiredType))
/*  390 */             .map(name -> DefaultListableBeanFactory.this.getBean(name))
/*  391 */             .filter(bean -> !(bean instanceof NullBean));
/*      */         }
/*      */         
/*      */         public Stream<T> orderedStream() {
/*  395 */           String[] beanNames = DefaultListableBeanFactory.this.getBeanNamesForTypedStream(requiredType);
/*  396 */           Map<String, T> matchingBeans = new LinkedHashMap<>(beanNames.length);
/*  397 */           for (String beanName : beanNames) {
/*  398 */             Object beanInstance = DefaultListableBeanFactory.this.getBean(beanName);
/*  399 */             if (!(beanInstance instanceof NullBean)) {
/*  400 */               matchingBeans.put(beanName, (T)beanInstance);
/*      */             }
/*      */           } 
/*  403 */           Stream<T> stream = matchingBeans.values().stream();
/*  404 */           return stream.sorted((Comparator)DefaultListableBeanFactory.this.adaptOrderComparator(matchingBeans));
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private <T> T resolveBean(ResolvableType requiredType, @Nullable Object[] args, boolean nonUniqueAsNull) {
/*  411 */     NamedBeanHolder<T> namedBean = resolveNamedBean(requiredType, args, nonUniqueAsNull);
/*  412 */     if (namedBean != null) {
/*  413 */       return (T)namedBean.getBeanInstance();
/*      */     }
/*  415 */     BeanFactory parent = getParentBeanFactory();
/*  416 */     if (parent instanceof DefaultListableBeanFactory) {
/*  417 */       return ((DefaultListableBeanFactory)parent).resolveBean(requiredType, args, nonUniqueAsNull);
/*      */     }
/*  419 */     if (parent != null) {
/*  420 */       ObjectProvider<T> parentProvider = parent.getBeanProvider(requiredType);
/*  421 */       if (args != null) {
/*  422 */         return (T)parentProvider.getObject(args);
/*      */       }
/*      */       
/*  425 */       return nonUniqueAsNull ? (T)parentProvider.getIfUnique() : (T)parentProvider.getIfAvailable();
/*      */     } 
/*      */     
/*  428 */     return null;
/*      */   }
/*      */   
/*      */   private String[] getBeanNamesForTypedStream(ResolvableType requiredType) {
/*  432 */     return BeanFactoryUtils.beanNamesForTypeIncludingAncestors((ListableBeanFactory)this, requiredType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean containsBeanDefinition(String beanName) {
/*  442 */     Assert.notNull(beanName, "Bean name must not be null");
/*  443 */     return this.beanDefinitionMap.containsKey(beanName);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getBeanDefinitionCount() {
/*  448 */     return this.beanDefinitionMap.size();
/*      */   }
/*      */ 
/*      */   
/*      */   public String[] getBeanDefinitionNames() {
/*  453 */     String[] frozenNames = this.frozenBeanDefinitionNames;
/*  454 */     if (frozenNames != null) {
/*  455 */       return (String[])frozenNames.clone();
/*      */     }
/*      */     
/*  458 */     return StringUtils.toStringArray(this.beanDefinitionNames);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getBeanNamesForType(ResolvableType type) {
/*  464 */     Class<?> resolved = type.resolve();
/*  465 */     if (resolved != null && !type.hasGenerics()) {
/*  466 */       return getBeanNamesForType(resolved, true, true);
/*      */     }
/*      */     
/*  469 */     return doGetBeanNamesForType(type, true, true);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getBeanNamesForType(@Nullable Class<?> type) {
/*  475 */     return getBeanNamesForType(type, true, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public String[] getBeanNamesForType(@Nullable Class<?> type, boolean includeNonSingletons, boolean allowEagerInit) {
/*  480 */     if (!isConfigurationFrozen() || type == null || !allowEagerInit) {
/*  481 */       return doGetBeanNamesForType(ResolvableType.forRawClass(type), includeNonSingletons, allowEagerInit);
/*      */     }
/*  483 */     Map<Class<?>, String[]> cache = includeNonSingletons ? this.allBeanNamesByType : this.singletonBeanNamesByType;
/*      */     
/*  485 */     String[] resolvedBeanNames = cache.get(type);
/*  486 */     if (resolvedBeanNames != null) {
/*  487 */       return resolvedBeanNames;
/*      */     }
/*  489 */     resolvedBeanNames = doGetBeanNamesForType(ResolvableType.forRawClass(type), includeNonSingletons, true);
/*  490 */     if (ClassUtils.isCacheSafe(type, getBeanClassLoader())) {
/*  491 */       cache.put(type, resolvedBeanNames);
/*      */     }
/*  493 */     return resolvedBeanNames;
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
/*      */   private String[] doGetBeanNamesForType(ResolvableType type, boolean includeNonSingletons, boolean allowEagerInit) {
/*      */     // Byte code:
/*      */     //   0: new java/util/ArrayList
/*      */     //   3: dup
/*      */     //   4: invokespecial <init> : ()V
/*      */     //   7: astore #4
/*      */     //   9: aload_0
/*      */     //   10: getfield beanDefinitionNames : Ljava/util/List;
/*      */     //   13: invokeinterface iterator : ()Ljava/util/Iterator;
/*      */     //   18: astore #5
/*      */     //   20: aload #5
/*      */     //   22: invokeinterface hasNext : ()Z
/*      */     //   27: ifeq -> 411
/*      */     //   30: aload #5
/*      */     //   32: invokeinterface next : ()Ljava/lang/Object;
/*      */     //   37: checkcast java/lang/String
/*      */     //   40: astore #6
/*      */     //   42: aload_0
/*      */     //   43: aload #6
/*      */     //   45: invokevirtual isAlias : (Ljava/lang/String;)Z
/*      */     //   48: ifne -> 408
/*      */     //   51: aload_0
/*      */     //   52: aload #6
/*      */     //   54: invokevirtual getMergedLocalBeanDefinition : (Ljava/lang/String;)Lorg/springframework/beans/factory/support/RootBeanDefinition;
/*      */     //   57: astore #7
/*      */     //   59: aload #7
/*      */     //   61: invokevirtual isAbstract : ()Z
/*      */     //   64: ifne -> 276
/*      */     //   67: iload_3
/*      */     //   68: ifne -> 106
/*      */     //   71: aload #7
/*      */     //   73: invokevirtual hasBeanClass : ()Z
/*      */     //   76: ifne -> 94
/*      */     //   79: aload #7
/*      */     //   81: invokevirtual isLazyInit : ()Z
/*      */     //   84: ifeq -> 94
/*      */     //   87: aload_0
/*      */     //   88: invokevirtual isAllowEagerClassLoading : ()Z
/*      */     //   91: ifeq -> 276
/*      */     //   94: aload_0
/*      */     //   95: aload #7
/*      */     //   97: invokevirtual getFactoryBeanName : ()Ljava/lang/String;
/*      */     //   100: invokespecial requiresEagerInitForType : (Ljava/lang/String;)Z
/*      */     //   103: ifne -> 276
/*      */     //   106: aload_0
/*      */     //   107: aload #6
/*      */     //   109: aload #7
/*      */     //   111: invokevirtual isFactoryBean : (Ljava/lang/String;Lorg/springframework/beans/factory/support/RootBeanDefinition;)Z
/*      */     //   114: istore #8
/*      */     //   116: aload #7
/*      */     //   118: invokevirtual getDecoratedDefinition : ()Lorg/springframework/beans/factory/config/BeanDefinitionHolder;
/*      */     //   121: astore #9
/*      */     //   123: iload_3
/*      */     //   124: ifne -> 154
/*      */     //   127: iload #8
/*      */     //   129: ifeq -> 154
/*      */     //   132: aload #9
/*      */     //   134: ifnull -> 145
/*      */     //   137: aload #7
/*      */     //   139: invokevirtual isLazyInit : ()Z
/*      */     //   142: ifeq -> 154
/*      */     //   145: aload_0
/*      */     //   146: aload #6
/*      */     //   148: invokevirtual containsSingleton : (Ljava/lang/String;)Z
/*      */     //   151: ifeq -> 197
/*      */     //   154: iload_2
/*      */     //   155: ifne -> 183
/*      */     //   158: aload #9
/*      */     //   160: ifnull -> 174
/*      */     //   163: aload #7
/*      */     //   165: invokevirtual isSingleton : ()Z
/*      */     //   168: ifeq -> 197
/*      */     //   171: goto -> 183
/*      */     //   174: aload_0
/*      */     //   175: aload #6
/*      */     //   177: invokevirtual isSingleton : (Ljava/lang/String;)Z
/*      */     //   180: ifeq -> 197
/*      */     //   183: aload_0
/*      */     //   184: aload #6
/*      */     //   186: aload_1
/*      */     //   187: invokevirtual isTypeMatch : (Ljava/lang/String;Lorg/springframework/core/ResolvableType;)Z
/*      */     //   190: ifeq -> 197
/*      */     //   193: iconst_1
/*      */     //   194: goto -> 198
/*      */     //   197: iconst_0
/*      */     //   198: istore #10
/*      */     //   200: iload #10
/*      */     //   202: ifne -> 261
/*      */     //   205: iload #8
/*      */     //   207: ifeq -> 261
/*      */     //   210: new java/lang/StringBuilder
/*      */     //   213: dup
/*      */     //   214: invokespecial <init> : ()V
/*      */     //   217: ldc '&'
/*      */     //   219: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   222: aload #6
/*      */     //   224: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   227: invokevirtual toString : ()Ljava/lang/String;
/*      */     //   230: astore #6
/*      */     //   232: iload_2
/*      */     //   233: ifne -> 244
/*      */     //   236: aload #7
/*      */     //   238: invokevirtual isSingleton : ()Z
/*      */     //   241: ifeq -> 258
/*      */     //   244: aload_0
/*      */     //   245: aload #6
/*      */     //   247: aload_1
/*      */     //   248: invokevirtual isTypeMatch : (Ljava/lang/String;Lorg/springframework/core/ResolvableType;)Z
/*      */     //   251: ifeq -> 258
/*      */     //   254: iconst_1
/*      */     //   255: goto -> 259
/*      */     //   258: iconst_0
/*      */     //   259: istore #10
/*      */     //   261: iload #10
/*      */     //   263: ifeq -> 276
/*      */     //   266: aload #4
/*      */     //   268: aload #6
/*      */     //   270: invokeinterface add : (Ljava/lang/Object;)Z
/*      */     //   275: pop
/*      */     //   276: goto -> 408
/*      */     //   279: astore #7
/*      */     //   281: iload_3
/*      */     //   282: ifeq -> 288
/*      */     //   285: aload #7
/*      */     //   287: athrow
/*      */     //   288: aload_0
/*      */     //   289: getfield logger : Lorg/apache/commons/logging/Log;
/*      */     //   292: invokeinterface isTraceEnabled : ()Z
/*      */     //   297: ifeq -> 336
/*      */     //   300: aload_0
/*      */     //   301: getfield logger : Lorg/apache/commons/logging/Log;
/*      */     //   304: new java/lang/StringBuilder
/*      */     //   307: dup
/*      */     //   308: invokespecial <init> : ()V
/*      */     //   311: ldc 'Ignoring bean class loading failure for bean ''
/*      */     //   313: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   316: aload #6
/*      */     //   318: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   321: ldc '''
/*      */     //   323: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   326: invokevirtual toString : ()Ljava/lang/String;
/*      */     //   329: aload #7
/*      */     //   331: invokeinterface trace : (Ljava/lang/Object;Ljava/lang/Throwable;)V
/*      */     //   336: aload_0
/*      */     //   337: aload #7
/*      */     //   339: invokevirtual onSuppressedException : (Ljava/lang/Exception;)V
/*      */     //   342: goto -> 408
/*      */     //   345: astore #7
/*      */     //   347: iload_3
/*      */     //   348: ifeq -> 354
/*      */     //   351: aload #7
/*      */     //   353: athrow
/*      */     //   354: aload_0
/*      */     //   355: getfield logger : Lorg/apache/commons/logging/Log;
/*      */     //   358: invokeinterface isTraceEnabled : ()Z
/*      */     //   363: ifeq -> 402
/*      */     //   366: aload_0
/*      */     //   367: getfield logger : Lorg/apache/commons/logging/Log;
/*      */     //   370: new java/lang/StringBuilder
/*      */     //   373: dup
/*      */     //   374: invokespecial <init> : ()V
/*      */     //   377: ldc 'Ignoring unresolvable metadata in bean definition ''
/*      */     //   379: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   382: aload #6
/*      */     //   384: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   387: ldc '''
/*      */     //   389: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   392: invokevirtual toString : ()Ljava/lang/String;
/*      */     //   395: aload #7
/*      */     //   397: invokeinterface trace : (Ljava/lang/Object;Ljava/lang/Throwable;)V
/*      */     //   402: aload_0
/*      */     //   403: aload #7
/*      */     //   405: invokevirtual onSuppressedException : (Ljava/lang/Exception;)V
/*      */     //   408: goto -> 20
/*      */     //   411: aload_0
/*      */     //   412: getfield manualSingletonNames : Ljava/util/Set;
/*      */     //   415: invokeinterface iterator : ()Ljava/util/Iterator;
/*      */     //   420: astore #5
/*      */     //   422: aload #5
/*      */     //   424: invokeinterface hasNext : ()Z
/*      */     //   429: ifeq -> 587
/*      */     //   432: aload #5
/*      */     //   434: invokeinterface next : ()Ljava/lang/Object;
/*      */     //   439: checkcast java/lang/String
/*      */     //   442: astore #6
/*      */     //   444: aload_0
/*      */     //   445: aload #6
/*      */     //   447: invokevirtual isFactoryBean : (Ljava/lang/String;)Z
/*      */     //   450: ifeq -> 511
/*      */     //   453: iload_2
/*      */     //   454: ifne -> 466
/*      */     //   457: aload_0
/*      */     //   458: aload #6
/*      */     //   460: invokevirtual isSingleton : (Ljava/lang/String;)Z
/*      */     //   463: ifeq -> 489
/*      */     //   466: aload_0
/*      */     //   467: aload #6
/*      */     //   469: aload_1
/*      */     //   470: invokevirtual isTypeMatch : (Ljava/lang/String;Lorg/springframework/core/ResolvableType;)Z
/*      */     //   473: ifeq -> 489
/*      */     //   476: aload #4
/*      */     //   478: aload #6
/*      */     //   480: invokeinterface add : (Ljava/lang/Object;)Z
/*      */     //   485: pop
/*      */     //   486: goto -> 422
/*      */     //   489: new java/lang/StringBuilder
/*      */     //   492: dup
/*      */     //   493: invokespecial <init> : ()V
/*      */     //   496: ldc '&'
/*      */     //   498: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   501: aload #6
/*      */     //   503: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   506: invokevirtual toString : ()Ljava/lang/String;
/*      */     //   509: astore #6
/*      */     //   511: aload_0
/*      */     //   512: aload #6
/*      */     //   514: aload_1
/*      */     //   515: invokevirtual isTypeMatch : (Ljava/lang/String;Lorg/springframework/core/ResolvableType;)Z
/*      */     //   518: ifeq -> 531
/*      */     //   521: aload #4
/*      */     //   523: aload #6
/*      */     //   525: invokeinterface add : (Ljava/lang/Object;)Z
/*      */     //   530: pop
/*      */     //   531: goto -> 584
/*      */     //   534: astore #7
/*      */     //   536: aload_0
/*      */     //   537: getfield logger : Lorg/apache/commons/logging/Log;
/*      */     //   540: invokeinterface isTraceEnabled : ()Z
/*      */     //   545: ifeq -> 584
/*      */     //   548: aload_0
/*      */     //   549: getfield logger : Lorg/apache/commons/logging/Log;
/*      */     //   552: new java/lang/StringBuilder
/*      */     //   555: dup
/*      */     //   556: invokespecial <init> : ()V
/*      */     //   559: ldc 'Failed to check manually registered singleton with name ''
/*      */     //   561: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   564: aload #6
/*      */     //   566: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   569: ldc '''
/*      */     //   571: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   574: invokevirtual toString : ()Ljava/lang/String;
/*      */     //   577: aload #7
/*      */     //   579: invokeinterface trace : (Ljava/lang/Object;Ljava/lang/Throwable;)V
/*      */     //   584: goto -> 422
/*      */     //   587: aload #4
/*      */     //   589: invokestatic toStringArray : (Ljava/util/Collection;)[Ljava/lang/String;
/*      */     //   592: areturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #497	-> 0
/*      */     //   #500	-> 9
/*      */     //   #503	-> 42
/*      */     //   #505	-> 51
/*      */     //   #507	-> 59
/*      */     //   #508	-> 73
/*      */     //   #509	-> 97
/*      */     //   #511	-> 106
/*      */     //   #512	-> 116
/*      */     //   #513	-> 123
/*      */     //   #515	-> 139
/*      */     //   #517	-> 165
/*      */     //   #518	-> 187
/*      */     //   #519	-> 200
/*      */     //   #521	-> 210
/*      */     //   #522	-> 232
/*      */     //   #524	-> 261
/*      */     //   #525	-> 266
/*      */     //   #548	-> 276
/*      */     //   #529	-> 279
/*      */     //   #530	-> 281
/*      */     //   #531	-> 285
/*      */     //   #534	-> 288
/*      */     //   #535	-> 300
/*      */     //   #537	-> 336
/*      */     //   #548	-> 342
/*      */     //   #539	-> 345
/*      */     //   #540	-> 347
/*      */     //   #541	-> 351
/*      */     //   #544	-> 354
/*      */     //   #545	-> 366
/*      */     //   #547	-> 402
/*      */     //   #550	-> 408
/*      */     //   #553	-> 411
/*      */     //   #556	-> 444
/*      */     //   #557	-> 453
/*      */     //   #558	-> 476
/*      */     //   #560	-> 486
/*      */     //   #563	-> 489
/*      */     //   #566	-> 511
/*      */     //   #567	-> 521
/*      */     //   #575	-> 531
/*      */     //   #570	-> 534
/*      */     //   #572	-> 536
/*      */     //   #573	-> 548
/*      */     //   #576	-> 584
/*      */     //   #578	-> 587
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   116	160	8	isFactoryBean	Z
/*      */     //   123	153	9	dbd	Lorg/springframework/beans/factory/config/BeanDefinitionHolder;
/*      */     //   200	76	10	matchFound	Z
/*      */     //   59	217	7	mbd	Lorg/springframework/beans/factory/support/RootBeanDefinition;
/*      */     //   281	61	7	ex	Lorg/springframework/beans/factory/CannotLoadBeanClassException;
/*      */     //   347	61	7	ex	Lorg/springframework/beans/factory/BeanDefinitionStoreException;
/*      */     //   42	366	6	beanName	Ljava/lang/String;
/*      */     //   536	48	7	ex	Lorg/springframework/beans/factory/NoSuchBeanDefinitionException;
/*      */     //   444	140	6	beanName	Ljava/lang/String;
/*      */     //   0	593	0	this	Lorg/springframework/beans/factory/support/DefaultListableBeanFactory;
/*      */     //   0	593	1	type	Lorg/springframework/core/ResolvableType;
/*      */     //   0	593	2	includeNonSingletons	Z
/*      */     //   0	593	3	allowEagerInit	Z
/*      */     //   9	584	4	result	Ljava/util/List;
/*      */     // Local variable type table:
/*      */     //   start	length	slot	name	signature
/*      */     //   9	584	4	result	Ljava/util/List<Ljava/lang/String;>;
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   51	276	279	org/springframework/beans/factory/CannotLoadBeanClassException
/*      */     //   51	276	345	org/springframework/beans/factory/BeanDefinitionStoreException
/*      */     //   444	486	534	org/springframework/beans/factory/NoSuchBeanDefinitionException
/*      */     //   489	531	534	org/springframework/beans/factory/NoSuchBeanDefinitionException
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
/*      */   private boolean requiresEagerInitForType(@Nullable String factoryBeanName) {
/*  589 */     return (factoryBeanName != null && isFactoryBean(factoryBeanName) && !containsSingleton(factoryBeanName));
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> Map<String, T> getBeansOfType(@Nullable Class<T> type) throws BeansException {
/*  594 */     return getBeansOfType(type, true, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> Map<String, T> getBeansOfType(@Nullable Class<T> type, boolean includeNonSingletons, boolean allowEagerInit) throws BeansException {
/*  602 */     String[] beanNames = getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
/*  603 */     Map<String, T> result = new LinkedHashMap<>(beanNames.length);
/*  604 */     for (String beanName : beanNames) {
/*      */       try {
/*  606 */         Object beanInstance = getBean(beanName);
/*  607 */         if (!(beanInstance instanceof NullBean)) {
/*  608 */           result.put(beanName, (T)beanInstance);
/*      */         }
/*      */       }
/*  611 */       catch (BeanCreationException ex) {
/*  612 */         Throwable rootCause = ex.getMostSpecificCause();
/*  613 */         if (rootCause instanceof org.springframework.beans.factory.BeanCurrentlyInCreationException)
/*  614 */         { BeanCreationException bce = (BeanCreationException)rootCause;
/*  615 */           String exBeanName = bce.getBeanName();
/*  616 */           if (exBeanName != null && isCurrentlyInCreation(exBeanName))
/*  617 */           { if (this.logger.isTraceEnabled()) {
/*  618 */               this.logger.trace("Ignoring match to currently created bean '" + exBeanName + "': " + ex
/*  619 */                   .getMessage());
/*      */             }
/*  621 */             onSuppressedException((Exception)ex);
/*      */              }
/*      */           
/*      */           else
/*      */           
/*      */           { 
/*  627 */             throw ex; }  } else { throw ex; }
/*      */       
/*      */       } 
/*  630 */     }  return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType) {
/*  635 */     List<String> result = new ArrayList<>();
/*  636 */     for (String beanName : this.beanDefinitionNames) {
/*  637 */       BeanDefinition beanDefinition = getBeanDefinition(beanName);
/*  638 */       if (!beanDefinition.isAbstract() && findAnnotationOnBean(beanName, annotationType) != null) {
/*  639 */         result.add(beanName);
/*      */       }
/*      */     } 
/*  642 */     for (String beanName : this.manualSingletonNames) {
/*  643 */       if (!result.contains(beanName) && findAnnotationOnBean(beanName, annotationType) != null) {
/*  644 */         result.add(beanName);
/*      */       }
/*      */     } 
/*  647 */     return StringUtils.toStringArray(result);
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) {
/*  652 */     String[] beanNames = getBeanNamesForAnnotation(annotationType);
/*  653 */     Map<String, Object> result = new LinkedHashMap<>(beanNames.length);
/*  654 */     for (String beanName : beanNames) {
/*  655 */       Object beanInstance = getBean(beanName);
/*  656 */       if (!(beanInstance instanceof NullBean)) {
/*  657 */         result.put(beanName, beanInstance);
/*      */       }
/*      */     } 
/*  660 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType) throws NoSuchBeanDefinitionException {
/*      */     Annotation annotation;
/*  668 */     A ann = null;
/*  669 */     Class<?> beanType = getType(beanName);
/*  670 */     if (beanType != null) {
/*  671 */       annotation = AnnotationUtils.findAnnotation(beanType, annotationType);
/*      */     }
/*  673 */     if (annotation == null && containsBeanDefinition(beanName)) {
/*      */       
/*  675 */       RootBeanDefinition bd = getMergedLocalBeanDefinition(beanName);
/*  676 */       if (bd.hasBeanClass()) {
/*  677 */         Class<?> beanClass = bd.getBeanClass();
/*  678 */         if (beanClass != beanType) {
/*  679 */           annotation = AnnotationUtils.findAnnotation(beanClass, annotationType);
/*      */         }
/*      */       } 
/*      */     } 
/*  683 */     return (A)annotation;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void registerResolvableDependency(Class<?> dependencyType, @Nullable Object autowiredValue) {
/*  693 */     Assert.notNull(dependencyType, "Dependency type must not be null");
/*  694 */     if (autowiredValue != null) {
/*  695 */       if (!(autowiredValue instanceof ObjectFactory) && !dependencyType.isInstance(autowiredValue)) {
/*  696 */         throw new IllegalArgumentException("Value [" + autowiredValue + "] does not implement specified dependency type [" + dependencyType
/*  697 */             .getName() + "]");
/*      */       }
/*  699 */       this.resolvableDependencies.put(dependencyType, autowiredValue);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAutowireCandidate(String beanName, DependencyDescriptor descriptor) throws NoSuchBeanDefinitionException {
/*  707 */     return isAutowireCandidate(beanName, descriptor, getAutowireCandidateResolver());
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
/*      */   protected boolean isAutowireCandidate(String beanName, DependencyDescriptor descriptor, AutowireCandidateResolver resolver) throws NoSuchBeanDefinitionException {
/*  721 */     String beanDefinitionName = BeanFactoryUtils.transformedBeanName(beanName);
/*  722 */     if (containsBeanDefinition(beanDefinitionName)) {
/*  723 */       return isAutowireCandidate(beanName, getMergedLocalBeanDefinition(beanDefinitionName), descriptor, resolver);
/*      */     }
/*  725 */     if (containsSingleton(beanName)) {
/*  726 */       return isAutowireCandidate(beanName, new RootBeanDefinition(getType(beanName)), descriptor, resolver);
/*      */     }
/*      */     
/*  729 */     BeanFactory parent = getParentBeanFactory();
/*  730 */     if (parent instanceof DefaultListableBeanFactory)
/*      */     {
/*  732 */       return ((DefaultListableBeanFactory)parent).isAutowireCandidate(beanName, descriptor, resolver);
/*      */     }
/*  734 */     if (parent instanceof ConfigurableListableBeanFactory)
/*      */     {
/*  736 */       return ((ConfigurableListableBeanFactory)parent).isAutowireCandidate(beanName, descriptor);
/*      */     }
/*      */     
/*  739 */     return true;
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
/*      */   protected boolean isAutowireCandidate(String beanName, RootBeanDefinition mbd, DependencyDescriptor descriptor, AutowireCandidateResolver resolver) {
/*  755 */     String beanDefinitionName = BeanFactoryUtils.transformedBeanName(beanName);
/*  756 */     resolveBeanClass(mbd, beanDefinitionName, new Class[0]);
/*  757 */     if (mbd.isFactoryMethodUnique && mbd.factoryMethodToIntrospect == null) {
/*  758 */       (new ConstructorResolver(this)).resolveFactoryMethodIfPossible(mbd);
/*      */     }
/*  760 */     return resolver.isAutowireCandidate(new BeanDefinitionHolder(mbd, beanName, 
/*  761 */           getAliases(beanDefinitionName)), descriptor);
/*      */   }
/*      */ 
/*      */   
/*      */   public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
/*  766 */     BeanDefinition bd = this.beanDefinitionMap.get(beanName);
/*  767 */     if (bd == null) {
/*  768 */       if (this.logger.isTraceEnabled()) {
/*  769 */         this.logger.trace("No bean named '" + beanName + "' found in " + this);
/*      */       }
/*  771 */       throw new NoSuchBeanDefinitionException(beanName);
/*      */     } 
/*  773 */     return bd;
/*      */   }
/*      */ 
/*      */   
/*      */   public Iterator<String> getBeanNamesIterator() {
/*  778 */     CompositeIterator<String> iterator = new CompositeIterator();
/*  779 */     iterator.add(this.beanDefinitionNames.iterator());
/*  780 */     iterator.add(this.manualSingletonNames.iterator());
/*  781 */     return (Iterator<String>)iterator;
/*      */   }
/*      */ 
/*      */   
/*      */   public void clearMetadataCache() {
/*  786 */     super.clearMetadataCache();
/*  787 */     clearByTypeCache();
/*      */   }
/*      */ 
/*      */   
/*      */   public void freezeConfiguration() {
/*  792 */     this.configurationFrozen = true;
/*  793 */     this.frozenBeanDefinitionNames = StringUtils.toStringArray(this.beanDefinitionNames);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isConfigurationFrozen() {
/*  798 */     return this.configurationFrozen;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isBeanEligibleForMetadataCaching(String beanName) {
/*  808 */     return (this.configurationFrozen || super.isBeanEligibleForMetadataCaching(beanName));
/*      */   }
/*      */ 
/*      */   
/*      */   public void preInstantiateSingletons() throws BeansException {
/*  813 */     if (this.logger.isTraceEnabled()) {
/*  814 */       this.logger.trace("Pre-instantiating singletons in " + this);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  819 */     List<String> beanNames = new ArrayList<>(this.beanDefinitionNames);
/*      */ 
/*      */     
/*  822 */     for (String beanName : beanNames) {
/*  823 */       RootBeanDefinition bd = getMergedLocalBeanDefinition(beanName);
/*  824 */       if (!bd.isAbstract() && bd.isSingleton() && !bd.isLazyInit()) {
/*  825 */         if (isFactoryBean(beanName)) {
/*  826 */           Object bean = getBean("&" + beanName);
/*  827 */           if (bean instanceof FactoryBean) {
/*  828 */             boolean isEagerInit; FactoryBean<?> factory = (FactoryBean)bean;
/*      */             
/*  830 */             if (System.getSecurityManager() != null && factory instanceof SmartFactoryBean) {
/*  831 */               isEagerInit = ((Boolean)AccessController.<Boolean>doPrivileged((SmartFactoryBean)factory::isEagerInit, 
/*      */                   
/*  833 */                   getAccessControlContext())).booleanValue();
/*      */             }
/*      */             else {
/*      */               
/*  837 */               isEagerInit = (factory instanceof SmartFactoryBean && ((SmartFactoryBean)factory).isEagerInit());
/*      */             } 
/*  839 */             if (isEagerInit) {
/*  840 */               getBean(beanName);
/*      */             }
/*      */           } 
/*      */           continue;
/*      */         } 
/*  845 */         getBean(beanName);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  851 */     for (String beanName : beanNames) {
/*  852 */       Object singletonInstance = getSingleton(beanName);
/*  853 */       if (singletonInstance instanceof SmartInitializingSingleton) {
/*  854 */         SmartInitializingSingleton smartSingleton = (SmartInitializingSingleton)singletonInstance;
/*  855 */         if (System.getSecurityManager() != null) {
/*  856 */           AccessController.doPrivileged(() -> {
/*      */                 smartSingleton.afterSingletonsInstantiated();
/*      */                 return null;
/*  859 */               }getAccessControlContext());
/*      */           continue;
/*      */         } 
/*  862 */         smartSingleton.afterSingletonsInstantiated();
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
/*      */   public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException {
/*  877 */     Assert.hasText(beanName, "Bean name must not be empty");
/*  878 */     Assert.notNull(beanDefinition, "BeanDefinition must not be null");
/*      */     
/*  880 */     if (beanDefinition instanceof AbstractBeanDefinition) {
/*      */       try {
/*  882 */         ((AbstractBeanDefinition)beanDefinition).validate();
/*      */       }
/*  884 */       catch (BeanDefinitionValidationException ex) {
/*  885 */         throw new BeanDefinitionStoreException(beanDefinition.getResourceDescription(), beanName, "Validation of bean definition failed", ex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  890 */     BeanDefinition existingDefinition = this.beanDefinitionMap.get(beanName);
/*  891 */     if (existingDefinition != null) {
/*  892 */       if (!isAllowBeanDefinitionOverriding()) {
/*  893 */         throw new BeanDefinitionOverrideException(beanName, beanDefinition, existingDefinition);
/*      */       }
/*  895 */       if (existingDefinition.getRole() < beanDefinition.getRole()) {
/*      */         
/*  897 */         if (this.logger.isInfoEnabled()) {
/*  898 */           this.logger.info("Overriding user-defined bean definition for bean '" + beanName + "' with a framework-generated bean definition: replacing [" + existingDefinition + "] with [" + beanDefinition + "]");
/*      */         
/*      */         }
/*      */       
/*      */       }
/*  903 */       else if (!beanDefinition.equals(existingDefinition)) {
/*  904 */         if (this.logger.isDebugEnabled()) {
/*  905 */           this.logger.debug("Overriding bean definition for bean '" + beanName + "' with a different definition: replacing [" + existingDefinition + "] with [" + beanDefinition + "]");
/*      */ 
/*      */         
/*      */         }
/*      */       
/*      */       }
/*  911 */       else if (this.logger.isTraceEnabled()) {
/*  912 */         this.logger.trace("Overriding bean definition for bean '" + beanName + "' with an equivalent definition: replacing [" + existingDefinition + "] with [" + beanDefinition + "]");
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  917 */       this.beanDefinitionMap.put(beanName, beanDefinition);
/*      */     } else {
/*      */       
/*  920 */       if (hasBeanCreationStarted()) {
/*      */         
/*  922 */         synchronized (this.beanDefinitionMap) {
/*  923 */           this.beanDefinitionMap.put(beanName, beanDefinition);
/*  924 */           List<String> updatedDefinitions = new ArrayList<>(this.beanDefinitionNames.size() + 1);
/*  925 */           updatedDefinitions.addAll(this.beanDefinitionNames);
/*  926 */           updatedDefinitions.add(beanName);
/*  927 */           this.beanDefinitionNames = updatedDefinitions;
/*  928 */           removeManualSingletonName(beanName);
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/*  933 */         this.beanDefinitionMap.put(beanName, beanDefinition);
/*  934 */         this.beanDefinitionNames.add(beanName);
/*  935 */         removeManualSingletonName(beanName);
/*      */       } 
/*  937 */       this.frozenBeanDefinitionNames = null;
/*      */     } 
/*      */     
/*  940 */     if (existingDefinition != null || containsSingleton(beanName)) {
/*  941 */       resetBeanDefinition(beanName);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
/*  947 */     Assert.hasText(beanName, "'beanName' must not be empty");
/*      */     
/*  949 */     BeanDefinition bd = this.beanDefinitionMap.remove(beanName);
/*  950 */     if (bd == null) {
/*  951 */       if (this.logger.isTraceEnabled()) {
/*  952 */         this.logger.trace("No bean named '" + beanName + "' found in " + this);
/*      */       }
/*  954 */       throw new NoSuchBeanDefinitionException(beanName);
/*      */     } 
/*      */     
/*  957 */     if (hasBeanCreationStarted()) {
/*      */       
/*  959 */       synchronized (this.beanDefinitionMap) {
/*  960 */         List<String> updatedDefinitions = new ArrayList<>(this.beanDefinitionNames);
/*  961 */         updatedDefinitions.remove(beanName);
/*  962 */         this.beanDefinitionNames = updatedDefinitions;
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  967 */       this.beanDefinitionNames.remove(beanName);
/*      */     } 
/*  969 */     this.frozenBeanDefinitionNames = null;
/*      */     
/*  971 */     resetBeanDefinition(beanName);
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
/*      */   protected void resetBeanDefinition(String beanName) {
/*  987 */     clearMergedBeanDefinition(beanName);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  992 */     destroySingleton(beanName);
/*      */ 
/*      */     
/*  995 */     for (BeanPostProcessor processor : getBeanPostProcessors()) {
/*  996 */       if (processor instanceof MergedBeanDefinitionPostProcessor) {
/*  997 */         ((MergedBeanDefinitionPostProcessor)processor).resetBeanDefinition(beanName);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1002 */     for (String bdName : this.beanDefinitionNames) {
/* 1003 */       if (!beanName.equals(bdName)) {
/* 1004 */         BeanDefinition bd = this.beanDefinitionMap.get(bdName);
/* 1005 */         if (beanName.equals(bd.getParentName())) {
/* 1006 */           resetBeanDefinition(bdName);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean allowAliasOverriding() {
/* 1017 */     return isAllowBeanDefinitionOverriding();
/*      */   }
/*      */ 
/*      */   
/*      */   public void registerSingleton(String beanName, Object singletonObject) throws IllegalStateException {
/* 1022 */     super.registerSingleton(beanName, singletonObject);
/* 1023 */     updateManualSingletonNames(set -> set.add(beanName), set -> !this.beanDefinitionMap.containsKey(beanName));
/* 1024 */     clearByTypeCache();
/*      */   }
/*      */ 
/*      */   
/*      */   public void destroySingletons() {
/* 1029 */     super.destroySingletons();
/* 1030 */     updateManualSingletonNames(Set::clear, set -> !set.isEmpty());
/* 1031 */     clearByTypeCache();
/*      */   }
/*      */ 
/*      */   
/*      */   public void destroySingleton(String beanName) {
/* 1036 */     super.destroySingleton(beanName);
/* 1037 */     removeManualSingletonName(beanName);
/* 1038 */     clearByTypeCache();
/*      */   }
/*      */   
/*      */   private void removeManualSingletonName(String beanName) {
/* 1042 */     updateManualSingletonNames(set -> set.remove(beanName), set -> set.contains(beanName));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void updateManualSingletonNames(Consumer<Set<String>> action, Predicate<Set<String>> condition) {
/* 1052 */     if (hasBeanCreationStarted()) {
/*      */       
/* 1054 */       synchronized (this.beanDefinitionMap) {
/* 1055 */         if (condition.test(this.manualSingletonNames)) {
/* 1056 */           Set<String> updatedSingletons = new LinkedHashSet<>(this.manualSingletonNames);
/* 1057 */           action.accept(updatedSingletons);
/* 1058 */           this.manualSingletonNames = updatedSingletons;
/*      */         }
/*      */       
/*      */       }
/*      */     
/*      */     }
/* 1064 */     else if (condition.test(this.manualSingletonNames)) {
/* 1065 */       action.accept(this.manualSingletonNames);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void clearByTypeCache() {
/* 1074 */     this.allBeanNamesByType.clear();
/* 1075 */     this.singletonBeanNamesByType.clear();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> NamedBeanHolder<T> resolveNamedBean(Class<T> requiredType) throws BeansException {
/* 1085 */     Assert.notNull(requiredType, "Required type must not be null");
/* 1086 */     NamedBeanHolder<T> namedBean = resolveNamedBean(ResolvableType.forRawClass(requiredType), (Object[])null, false);
/* 1087 */     if (namedBean != null) {
/* 1088 */       return namedBean;
/*      */     }
/* 1090 */     BeanFactory parent = getParentBeanFactory();
/* 1091 */     if (parent instanceof AutowireCapableBeanFactory) {
/* 1092 */       return ((AutowireCapableBeanFactory)parent).resolveNamedBean(requiredType);
/*      */     }
/* 1094 */     throw new NoSuchBeanDefinitionException(requiredType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private <T> NamedBeanHolder<T> resolveNamedBean(ResolvableType requiredType, @Nullable Object[] args, boolean nonUniqueAsNull) throws BeansException {
/* 1102 */     Assert.notNull(requiredType, "Required type must not be null");
/* 1103 */     String[] candidateNames = getBeanNamesForType(requiredType);
/*      */     
/* 1105 */     if (candidateNames.length > 1) {
/* 1106 */       List<String> autowireCandidates = new ArrayList<>(candidateNames.length);
/* 1107 */       for (String beanName : candidateNames) {
/* 1108 */         if (!containsBeanDefinition(beanName) || getBeanDefinition(beanName).isAutowireCandidate()) {
/* 1109 */           autowireCandidates.add(beanName);
/*      */         }
/*      */       } 
/* 1112 */       if (!autowireCandidates.isEmpty()) {
/* 1113 */         candidateNames = StringUtils.toStringArray(autowireCandidates);
/*      */       }
/*      */     } 
/*      */     
/* 1117 */     if (candidateNames.length == 1) {
/* 1118 */       String beanName = candidateNames[0];
/* 1119 */       return new NamedBeanHolder(beanName, getBean(beanName, requiredType.toClass(), args));
/*      */     } 
/* 1121 */     if (candidateNames.length > 1) {
/* 1122 */       Map<String, Object> candidates = new LinkedHashMap<>(candidateNames.length);
/* 1123 */       for (String beanName : candidateNames) {
/* 1124 */         if (containsSingleton(beanName) && args == null) {
/* 1125 */           Object beanInstance = getBean(beanName);
/* 1126 */           candidates.put(beanName, (beanInstance instanceof NullBean) ? null : beanInstance);
/*      */         } else {
/*      */           
/* 1129 */           candidates.put(beanName, getType(beanName));
/*      */         } 
/*      */       } 
/* 1132 */       String candidateName = determinePrimaryCandidate(candidates, requiredType.toClass());
/* 1133 */       if (candidateName == null) {
/* 1134 */         candidateName = determineHighestPriorityCandidate(candidates, requiredType.toClass());
/*      */       }
/* 1136 */       if (candidateName != null) {
/* 1137 */         Object beanInstance = candidates.get(candidateName);
/* 1138 */         if (beanInstance == null || beanInstance instanceof Class) {
/* 1139 */           beanInstance = getBean(candidateName, requiredType.toClass(), args);
/*      */         }
/* 1141 */         return new NamedBeanHolder(candidateName, beanInstance);
/*      */       } 
/* 1143 */       if (!nonUniqueAsNull) {
/* 1144 */         throw new NoUniqueBeanDefinitionException(requiredType, candidates.keySet());
/*      */       }
/*      */     } 
/*      */     
/* 1148 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Object resolveDependency(DependencyDescriptor descriptor, @Nullable String requestingBeanName, @Nullable Set<String> autowiredBeanNames, @Nullable TypeConverter typeConverter) throws BeansException {
/* 1156 */     descriptor.initParameterNameDiscovery(getParameterNameDiscoverer());
/* 1157 */     if (Optional.class == descriptor.getDependencyType()) {
/* 1158 */       return createOptionalDependency(descriptor, requestingBeanName, new Object[0]);
/*      */     }
/* 1160 */     if (ObjectFactory.class == descriptor.getDependencyType() || ObjectProvider.class == descriptor
/* 1161 */       .getDependencyType()) {
/* 1162 */       return new DependencyObjectProvider(descriptor, requestingBeanName);
/*      */     }
/* 1164 */     if (javaxInjectProviderClass == descriptor.getDependencyType()) {
/* 1165 */       return (new Jsr330Factory()).createDependencyProvider(descriptor, requestingBeanName);
/*      */     }
/*      */     
/* 1168 */     Object result = getAutowireCandidateResolver().getLazyResolutionProxyIfNecessary(descriptor, requestingBeanName);
/*      */     
/* 1170 */     if (result == null) {
/* 1171 */       result = doResolveDependency(descriptor, requestingBeanName, autowiredBeanNames, typeConverter);
/*      */     }
/* 1173 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Object doResolveDependency(DependencyDescriptor descriptor, @Nullable String beanName, @Nullable Set<String> autowiredBeanNames, @Nullable TypeConverter typeConverter) throws BeansException {
/* 1181 */     InjectionPoint previousInjectionPoint = ConstructorResolver.setCurrentInjectionPoint((InjectionPoint)descriptor);
/*      */     try {
/* 1183 */       Object autowiredBeanName, instanceCandidate, shortcut = descriptor.resolveShortcut((BeanFactory)this);
/* 1184 */       if (shortcut != null) {
/* 1185 */         return shortcut;
/*      */       }
/*      */       
/* 1188 */       Class<?> type = descriptor.getDependencyType();
/* 1189 */       Object value = getAutowireCandidateResolver().getSuggestedValue(descriptor);
/* 1190 */       if (value != null) {
/* 1191 */         if (value instanceof String) {
/* 1192 */           String strVal = resolveEmbeddedValue((String)value);
/*      */           
/* 1194 */           BeanDefinition bd = (beanName != null && containsBean(beanName)) ? getMergedBeanDefinition(beanName) : null;
/* 1195 */           value = evaluateBeanDefinitionString(strVal, bd);
/*      */         } 
/* 1197 */         TypeConverter converter = (typeConverter != null) ? typeConverter : getTypeConverter();
/*      */         try {
/* 1199 */           return converter.convertIfNecessary(value, type, descriptor.getTypeDescriptor());
/*      */         }
/* 1201 */         catch (UnsupportedOperationException ex) {
/*      */ 
/*      */ 
/*      */           
/* 1205 */           autowiredBeanName = (descriptor.getField() != null) ? converter.convertIfNecessary(value, type, descriptor.getField()) : converter.convertIfNecessary(value, type, descriptor.getMethodParameter());
/*      */           return autowiredBeanName;
/*      */         } 
/*      */       } 
/* 1209 */       Object multipleBeans = resolveMultipleBeans(descriptor, beanName, autowiredBeanNames, typeConverter);
/* 1210 */       if (multipleBeans != null) {
/* 1211 */         return multipleBeans;
/*      */       }
/*      */       
/* 1214 */       Map<String, Object> matchingBeans = findAutowireCandidates(beanName, type, descriptor);
/* 1215 */       if (matchingBeans.isEmpty()) {
/* 1216 */         if (isRequired(descriptor)) {
/* 1217 */           raiseNoMatchingBeanFound(type, descriptor.getResolvableType(), descriptor);
/*      */         }
/* 1219 */         autowiredBeanName = null; return autowiredBeanName;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1225 */       if (matchingBeans.size() > 1) {
/* 1226 */         autowiredBeanName = determineAutowireCandidate(matchingBeans, descriptor);
/* 1227 */         if (autowiredBeanName == null) {
/* 1228 */           if (isRequired(descriptor) || !indicatesMultipleBeans(type)) {
/* 1229 */             return descriptor.resolveNotUnique(descriptor.getResolvableType(), matchingBeans);
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1235 */           return null;
/*      */         } 
/*      */         
/* 1238 */         instanceCandidate = matchingBeans.get(autowiredBeanName);
/*      */       }
/*      */       else {
/*      */         
/* 1242 */         Map.Entry<String, Object> entry = matchingBeans.entrySet().iterator().next();
/* 1243 */         autowiredBeanName = entry.getKey();
/* 1244 */         instanceCandidate = entry.getValue();
/*      */       } 
/*      */       
/* 1247 */       if (autowiredBeanNames != null) {
/* 1248 */         autowiredBeanNames.add(autowiredBeanName);
/*      */       }
/* 1250 */       if (instanceCandidate instanceof Class) {
/* 1251 */         instanceCandidate = descriptor.resolveCandidate((String)autowiredBeanName, type, (BeanFactory)this);
/*      */       }
/* 1253 */       Object result = instanceCandidate;
/* 1254 */       if (result instanceof NullBean) {
/* 1255 */         if (isRequired(descriptor)) {
/* 1256 */           raiseNoMatchingBeanFound(type, descriptor.getResolvableType(), descriptor);
/*      */         }
/* 1258 */         result = null;
/*      */       } 
/* 1260 */       if (!ClassUtils.isAssignableValue(type, result)) {
/* 1261 */         throw new BeanNotOfRequiredTypeException(autowiredBeanName, type, instanceCandidate.getClass());
/*      */       }
/* 1263 */       return result;
/*      */     } finally {
/*      */       
/* 1266 */       ConstructorResolver.setCurrentInjectionPoint(previousInjectionPoint);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private Object resolveMultipleBeans(DependencyDescriptor descriptor, @Nullable String beanName, @Nullable Set<String> autowiredBeanNames, @Nullable TypeConverter typeConverter) {
/* 1274 */     Class<?> type = descriptor.getDependencyType();
/*      */     
/* 1276 */     if (descriptor instanceof StreamDependencyDescriptor) {
/* 1277 */       Map<String, Object> matchingBeans = findAutowireCandidates(beanName, type, descriptor);
/* 1278 */       if (autowiredBeanNames != null) {
/* 1279 */         autowiredBeanNames.addAll(matchingBeans.keySet());
/*      */       }
/*      */ 
/*      */       
/* 1283 */       Stream<Object> stream = matchingBeans.keySet().stream().map(name -> descriptor.resolveCandidate(name, type, (BeanFactory)this)).filter(bean -> !(bean instanceof NullBean));
/* 1284 */       if (((StreamDependencyDescriptor)descriptor).isOrdered()) {
/* 1285 */         stream = stream.sorted(adaptOrderComparator(matchingBeans));
/*      */       }
/* 1287 */       return stream;
/*      */     } 
/* 1289 */     if (type.isArray()) {
/* 1290 */       Class<?> componentType = type.getComponentType();
/* 1291 */       ResolvableType resolvableType = descriptor.getResolvableType();
/* 1292 */       Class<?> resolvedArrayType = resolvableType.resolve(type);
/* 1293 */       if (resolvedArrayType != type) {
/* 1294 */         componentType = resolvableType.getComponentType().resolve();
/*      */       }
/* 1296 */       if (componentType == null) {
/* 1297 */         return null;
/*      */       }
/* 1299 */       Map<String, Object> matchingBeans = findAutowireCandidates(beanName, componentType, new MultiElementDescriptor(descriptor));
/*      */       
/* 1301 */       if (matchingBeans.isEmpty()) {
/* 1302 */         return null;
/*      */       }
/* 1304 */       if (autowiredBeanNames != null) {
/* 1305 */         autowiredBeanNames.addAll(matchingBeans.keySet());
/*      */       }
/* 1307 */       TypeConverter converter = (typeConverter != null) ? typeConverter : getTypeConverter();
/* 1308 */       Object result = converter.convertIfNecessary(matchingBeans.values(), resolvedArrayType);
/* 1309 */       if (result instanceof Object[]) {
/* 1310 */         Comparator<Object> comparator = adaptDependencyComparator(matchingBeans);
/* 1311 */         if (comparator != null) {
/* 1312 */           Arrays.sort((Object[])result, comparator);
/*      */         }
/*      */       } 
/* 1315 */       return result;
/*      */     } 
/* 1317 */     if (Collection.class.isAssignableFrom(type) && type.isInterface()) {
/* 1318 */       Class<?> elementType = descriptor.getResolvableType().asCollection().resolveGeneric(new int[0]);
/* 1319 */       if (elementType == null) {
/* 1320 */         return null;
/*      */       }
/* 1322 */       Map<String, Object> matchingBeans = findAutowireCandidates(beanName, elementType, new MultiElementDescriptor(descriptor));
/*      */       
/* 1324 */       if (matchingBeans.isEmpty()) {
/* 1325 */         return null;
/*      */       }
/* 1327 */       if (autowiredBeanNames != null) {
/* 1328 */         autowiredBeanNames.addAll(matchingBeans.keySet());
/*      */       }
/* 1330 */       TypeConverter converter = (typeConverter != null) ? typeConverter : getTypeConverter();
/* 1331 */       Object result = converter.convertIfNecessary(matchingBeans.values(), type);
/* 1332 */       if (result instanceof List) {
/* 1333 */         Comparator<Object> comparator = adaptDependencyComparator(matchingBeans);
/* 1334 */         if (comparator != null) {
/* 1335 */           ((List<Object>)result).sort(comparator);
/*      */         }
/*      */       } 
/* 1338 */       return result;
/*      */     } 
/* 1340 */     if (Map.class == type) {
/* 1341 */       ResolvableType mapType = descriptor.getResolvableType().asMap();
/* 1342 */       Class<?> keyType = mapType.resolveGeneric(new int[] { 0 });
/* 1343 */       if (String.class != keyType) {
/* 1344 */         return null;
/*      */       }
/* 1346 */       Class<?> valueType = mapType.resolveGeneric(new int[] { 1 });
/* 1347 */       if (valueType == null) {
/* 1348 */         return null;
/*      */       }
/* 1350 */       Map<String, Object> matchingBeans = findAutowireCandidates(beanName, valueType, new MultiElementDescriptor(descriptor));
/*      */       
/* 1352 */       if (matchingBeans.isEmpty()) {
/* 1353 */         return null;
/*      */       }
/* 1355 */       if (autowiredBeanNames != null) {
/* 1356 */         autowiredBeanNames.addAll(matchingBeans.keySet());
/*      */       }
/* 1358 */       return matchingBeans;
/*      */     } 
/*      */     
/* 1361 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isRequired(DependencyDescriptor descriptor) {
/* 1366 */     return getAutowireCandidateResolver().isRequired(descriptor);
/*      */   }
/*      */   
/*      */   private boolean indicatesMultipleBeans(Class<?> type) {
/* 1370 */     return (type.isArray() || (type.isInterface() && (Collection.class
/* 1371 */       .isAssignableFrom(type) || Map.class.isAssignableFrom(type))));
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private Comparator<Object> adaptDependencyComparator(Map<String, ?> matchingBeans) {
/* 1376 */     Comparator<Object> comparator = getDependencyComparator();
/* 1377 */     if (comparator instanceof OrderComparator) {
/* 1378 */       return ((OrderComparator)comparator).withSourceProvider(
/* 1379 */           createFactoryAwareOrderSourceProvider(matchingBeans));
/*      */     }
/*      */     
/* 1382 */     return comparator;
/*      */   }
/*      */ 
/*      */   
/*      */   private Comparator<Object> adaptOrderComparator(Map<String, ?> matchingBeans) {
/* 1387 */     Comparator<Object> dependencyComparator = getDependencyComparator();
/* 1388 */     OrderComparator comparator = (dependencyComparator instanceof OrderComparator) ? (OrderComparator)dependencyComparator : OrderComparator.INSTANCE;
/*      */     
/* 1390 */     return comparator.withSourceProvider(createFactoryAwareOrderSourceProvider(matchingBeans));
/*      */   }
/*      */   
/*      */   private OrderComparator.OrderSourceProvider createFactoryAwareOrderSourceProvider(Map<String, ?> beans) {
/* 1394 */     IdentityHashMap<Object, String> instancesToBeanNames = new IdentityHashMap<>();
/* 1395 */     beans.forEach((beanName, instance) -> (String)instancesToBeanNames.put(instance, beanName));
/* 1396 */     return new FactoryAwareOrderSourceProvider(instancesToBeanNames);
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
/*      */   protected Map<String, Object> findAutowireCandidates(@Nullable String beanName, Class<?> requiredType, DependencyDescriptor descriptor) {
/* 1415 */     String[] candidateNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors((ListableBeanFactory)this, requiredType, true, descriptor
/* 1416 */         .isEager());
/* 1417 */     Map<String, Object> result = new LinkedHashMap<>(candidateNames.length);
/* 1418 */     for (Map.Entry<Class<?>, Object> classObjectEntry : this.resolvableDependencies.entrySet()) {
/* 1419 */       Class<?> autowiringType = classObjectEntry.getKey();
/* 1420 */       if (autowiringType.isAssignableFrom(requiredType)) {
/* 1421 */         Object autowiringValue = classObjectEntry.getValue();
/* 1422 */         autowiringValue = AutowireUtils.resolveAutowiringValue(autowiringValue, requiredType);
/* 1423 */         if (requiredType.isInstance(autowiringValue)) {
/* 1424 */           result.put(ObjectUtils.identityToString(autowiringValue), autowiringValue);
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/* 1429 */     for (String candidate : candidateNames) {
/* 1430 */       if (!isSelfReference(beanName, candidate) && isAutowireCandidate(candidate, descriptor)) {
/* 1431 */         addCandidateEntry(result, candidate, descriptor, requiredType);
/*      */       }
/*      */     } 
/* 1434 */     if (result.isEmpty()) {
/* 1435 */       boolean multiple = indicatesMultipleBeans(requiredType);
/*      */       
/* 1437 */       DependencyDescriptor fallbackDescriptor = descriptor.forFallbackMatch();
/* 1438 */       for (String candidate : candidateNames) {
/* 1439 */         if (!isSelfReference(beanName, candidate) && isAutowireCandidate(candidate, fallbackDescriptor) && (!multiple || 
/* 1440 */           getAutowireCandidateResolver().hasQualifier(descriptor))) {
/* 1441 */           addCandidateEntry(result, candidate, descriptor, requiredType);
/*      */         }
/*      */       } 
/* 1444 */       if (result.isEmpty() && !multiple)
/*      */       {
/*      */         
/* 1447 */         for (String candidate : candidateNames) {
/* 1448 */           if (isSelfReference(beanName, candidate) && (!(descriptor instanceof MultiElementDescriptor) || 
/* 1449 */             !beanName.equals(candidate)) && 
/* 1450 */             isAutowireCandidate(candidate, fallbackDescriptor)) {
/* 1451 */             addCandidateEntry(result, candidate, descriptor, requiredType);
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/* 1456 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addCandidateEntry(Map<String, Object> candidates, String candidateName, DependencyDescriptor descriptor, Class<?> requiredType) {
/* 1466 */     if (descriptor instanceof MultiElementDescriptor) {
/* 1467 */       Object beanInstance = descriptor.resolveCandidate(candidateName, requiredType, (BeanFactory)this);
/* 1468 */       if (!(beanInstance instanceof NullBean)) {
/* 1469 */         candidates.put(candidateName, beanInstance);
/*      */       }
/*      */     }
/* 1472 */     else if (containsSingleton(candidateName) || (descriptor instanceof StreamDependencyDescriptor && ((StreamDependencyDescriptor)descriptor)
/* 1473 */       .isOrdered())) {
/* 1474 */       Object beanInstance = descriptor.resolveCandidate(candidateName, requiredType, (BeanFactory)this);
/* 1475 */       candidates.put(candidateName, (beanInstance instanceof NullBean) ? null : beanInstance);
/*      */     } else {
/*      */       
/* 1478 */       candidates.put(candidateName, getType(candidateName));
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
/*      */   protected String determineAutowireCandidate(Map<String, Object> candidates, DependencyDescriptor descriptor) {
/* 1492 */     Class<?> requiredType = descriptor.getDependencyType();
/* 1493 */     String primaryCandidate = determinePrimaryCandidate(candidates, requiredType);
/* 1494 */     if (primaryCandidate != null) {
/* 1495 */       return primaryCandidate;
/*      */     }
/* 1497 */     String priorityCandidate = determineHighestPriorityCandidate(candidates, requiredType);
/* 1498 */     if (priorityCandidate != null) {
/* 1499 */       return priorityCandidate;
/*      */     }
/*      */     
/* 1502 */     for (Map.Entry<String, Object> entry : candidates.entrySet()) {
/* 1503 */       String candidateName = entry.getKey();
/* 1504 */       Object beanInstance = entry.getValue();
/* 1505 */       if ((beanInstance != null && this.resolvableDependencies.containsValue(beanInstance)) || 
/* 1506 */         matchesBeanName(candidateName, descriptor.getDependencyName())) {
/* 1507 */         return candidateName;
/*      */       }
/*      */     } 
/* 1510 */     return null;
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
/*      */   protected String determinePrimaryCandidate(Map<String, Object> candidates, Class<?> requiredType) {
/* 1523 */     String primaryBeanName = null;
/* 1524 */     for (Map.Entry<String, Object> entry : candidates.entrySet()) {
/* 1525 */       String candidateBeanName = entry.getKey();
/* 1526 */       Object beanInstance = entry.getValue();
/* 1527 */       if (isPrimary(candidateBeanName, beanInstance)) {
/* 1528 */         if (primaryBeanName != null) {
/* 1529 */           boolean candidateLocal = containsBeanDefinition(candidateBeanName);
/* 1530 */           boolean primaryLocal = containsBeanDefinition(primaryBeanName);
/* 1531 */           if (candidateLocal && primaryLocal) {
/* 1532 */             throw new NoUniqueBeanDefinitionException(requiredType, candidates.size(), "more than one 'primary' bean found among candidates: " + candidates
/* 1533 */                 .keySet());
/*      */           }
/* 1535 */           if (candidateLocal) {
/* 1536 */             primaryBeanName = candidateBeanName;
/*      */           }
/*      */           continue;
/*      */         } 
/* 1540 */         primaryBeanName = candidateBeanName;
/*      */       } 
/*      */     } 
/*      */     
/* 1544 */     return primaryBeanName;
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
/*      */   protected String determineHighestPriorityCandidate(Map<String, Object> candidates, Class<?> requiredType) {
/* 1561 */     String highestPriorityBeanName = null;
/* 1562 */     Integer highestPriority = null;
/* 1563 */     for (Map.Entry<String, Object> entry : candidates.entrySet()) {
/* 1564 */       String candidateBeanName = entry.getKey();
/* 1565 */       Object beanInstance = entry.getValue();
/* 1566 */       if (beanInstance != null) {
/* 1567 */         Integer candidatePriority = getPriority(beanInstance);
/* 1568 */         if (candidatePriority != null) {
/* 1569 */           if (highestPriorityBeanName != null) {
/* 1570 */             if (candidatePriority.equals(highestPriority)) {
/* 1571 */               throw new NoUniqueBeanDefinitionException(requiredType, candidates.size(), "Multiple beans found with the same priority ('" + highestPriority + "') among candidates: " + candidates
/*      */                   
/* 1573 */                   .keySet());
/*      */             }
/* 1575 */             if (candidatePriority.intValue() < highestPriority.intValue()) {
/* 1576 */               highestPriorityBeanName = candidateBeanName;
/* 1577 */               highestPriority = candidatePriority;
/*      */             } 
/*      */             continue;
/*      */           } 
/* 1581 */           highestPriorityBeanName = candidateBeanName;
/* 1582 */           highestPriority = candidatePriority;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1587 */     return highestPriorityBeanName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isPrimary(String beanName, Object beanInstance) {
/* 1598 */     if (containsBeanDefinition(beanName)) {
/* 1599 */       return getMergedLocalBeanDefinition(beanName).isPrimary();
/*      */     }
/* 1601 */     BeanFactory parent = getParentBeanFactory();
/* 1602 */     return (parent instanceof DefaultListableBeanFactory && ((DefaultListableBeanFactory)parent)
/* 1603 */       .isPrimary(beanName, beanInstance));
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
/*      */   protected Integer getPriority(Object beanInstance) {
/* 1620 */     Comparator<Object> comparator = getDependencyComparator();
/* 1621 */     if (comparator instanceof OrderComparator) {
/* 1622 */       return ((OrderComparator)comparator).getPriority(beanInstance);
/*      */     }
/* 1624 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean matchesBeanName(String beanName, @Nullable String candidateName) {
/* 1632 */     return (candidateName != null && (candidateName
/* 1633 */       .equals(beanName) || ObjectUtils.containsElement((Object[])getAliases(beanName), candidateName)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isSelfReference(@Nullable String beanName, @Nullable String candidateName) {
/* 1642 */     return (beanName != null && candidateName != null && (beanName
/* 1643 */       .equals(candidateName) || (containsBeanDefinition(candidateName) && beanName
/* 1644 */       .equals(getMergedLocalBeanDefinition(candidateName).getFactoryBeanName()))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void raiseNoMatchingBeanFound(Class<?> type, ResolvableType resolvableType, DependencyDescriptor descriptor) throws BeansException {
/* 1654 */     checkBeanNotOfRequiredType(type, descriptor);
/*      */     
/* 1656 */     throw new NoSuchBeanDefinitionException(resolvableType, "expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: " + 
/*      */         
/* 1658 */         ObjectUtils.nullSafeToString(descriptor.getAnnotations()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkBeanNotOfRequiredType(Class<?> type, DependencyDescriptor descriptor) {
/* 1666 */     for (String beanName : this.beanDefinitionNames) {
/* 1667 */       RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
/* 1668 */       Class<?> targetType = mbd.getTargetType();
/* 1669 */       if (targetType != null && type.isAssignableFrom(targetType) && 
/* 1670 */         isAutowireCandidate(beanName, mbd, descriptor, getAutowireCandidateResolver())) {
/*      */         
/* 1672 */         Object beanInstance = getSingleton(beanName, false);
/*      */         
/* 1674 */         Class<?> beanType = (beanInstance != null && beanInstance.getClass() != NullBean.class) ? beanInstance.getClass() : predictBeanType(beanName, mbd, new Class[0]);
/* 1675 */         if (beanType != null && !type.isAssignableFrom(beanType)) {
/* 1676 */           throw new BeanNotOfRequiredTypeException(beanName, type, beanType);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1681 */     BeanFactory parent = getParentBeanFactory();
/* 1682 */     if (parent instanceof DefaultListableBeanFactory) {
/* 1683 */       ((DefaultListableBeanFactory)parent).checkBeanNotOfRequiredType(type, descriptor);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Optional<?> createOptionalDependency(DependencyDescriptor descriptor, @Nullable String beanName, Object... args) {
/* 1693 */     DependencyDescriptor descriptorToUse = new NestedDependencyDescriptor(descriptor)
/*      */       {
/*      */         public boolean isRequired() {
/* 1696 */           return false;
/*      */         }
/*      */         
/*      */         public Object resolveCandidate(String beanName, Class<?> requiredType, BeanFactory beanFactory) {
/* 1700 */           return !ObjectUtils.isEmpty(args) ? beanFactory.getBean(beanName, args) : super
/* 1701 */             .resolveCandidate(beanName, requiredType, beanFactory);
/*      */         }
/*      */       };
/* 1704 */     Object result = doResolveDependency(descriptorToUse, beanName, (Set<String>)null, (TypeConverter)null);
/* 1705 */     return (result instanceof Optional) ? (Optional)result : Optional.ofNullable(result);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1711 */     StringBuilder sb = new StringBuilder(ObjectUtils.identityToString(this));
/* 1712 */     sb.append(": defining beans [");
/* 1713 */     sb.append(StringUtils.collectionToCommaDelimitedString(this.beanDefinitionNames));
/* 1714 */     sb.append("]; ");
/* 1715 */     BeanFactory parent = getParentBeanFactory();
/* 1716 */     if (parent == null) {
/* 1717 */       sb.append("root of factory hierarchy");
/*      */     } else {
/*      */       
/* 1720 */       sb.append("parent: ").append(ObjectUtils.identityToString(parent));
/*      */     } 
/* 1722 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 1731 */     throw new NotSerializableException("DefaultListableBeanFactory itself is not deserializable - just a SerializedBeanFactoryReference is");
/*      */   }
/*      */ 
/*      */   
/*      */   protected Object writeReplace() throws ObjectStreamException {
/* 1736 */     if (this.serializationId != null) {
/* 1737 */       return new SerializedBeanFactoryReference(this.serializationId);
/*      */     }
/*      */     
/* 1740 */     throw new NotSerializableException("DefaultListableBeanFactory has no serialization id");
/*      */   }
/*      */ 
/*      */   
/*      */   public DefaultListableBeanFactory() {}
/*      */ 
/*      */   
/*      */   private static class SerializedBeanFactoryReference
/*      */     implements Serializable
/*      */   {
/*      */     private final String id;
/*      */ 
/*      */     
/*      */     public SerializedBeanFactoryReference(String id) {
/* 1754 */       this.id = id;
/*      */     }
/*      */     
/*      */     private Object readResolve() {
/* 1758 */       Reference<?> ref = (Reference)DefaultListableBeanFactory.serializableFactories.get(this.id);
/* 1759 */       if (ref != null) {
/* 1760 */         Object result = ref.get();
/* 1761 */         if (result != null) {
/* 1762 */           return result;
/*      */         }
/*      */       } 
/*      */       
/* 1766 */       DefaultListableBeanFactory dummyFactory = new DefaultListableBeanFactory();
/* 1767 */       dummyFactory.serializationId = this.id;
/* 1768 */       return dummyFactory;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class NestedDependencyDescriptor
/*      */     extends DependencyDescriptor
/*      */   {
/*      */     public NestedDependencyDescriptor(DependencyDescriptor original) {
/* 1779 */       super(original);
/* 1780 */       increaseNestingLevel();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class MultiElementDescriptor
/*      */     extends NestedDependencyDescriptor
/*      */   {
/*      */     public MultiElementDescriptor(DependencyDescriptor original) {
/* 1791 */       super(original);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class StreamDependencyDescriptor
/*      */     extends DependencyDescriptor
/*      */   {
/*      */     private final boolean ordered;
/*      */ 
/*      */     
/*      */     public StreamDependencyDescriptor(DependencyDescriptor original, boolean ordered) {
/* 1804 */       super(original);
/* 1805 */       this.ordered = ordered;
/*      */     }
/*      */     
/*      */     public boolean isOrdered() {
/* 1809 */       return this.ordered;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static interface BeanObjectProvider<T>
/*      */     extends ObjectProvider<T>, Serializable {}
/*      */ 
/*      */   
/*      */   private class DependencyObjectProvider
/*      */     implements BeanObjectProvider<Object>
/*      */   {
/*      */     private final DependencyDescriptor descriptor;
/*      */     
/*      */     private final boolean optional;
/*      */     
/*      */     @Nullable
/*      */     private final String beanName;
/*      */ 
/*      */     
/*      */     public DependencyObjectProvider(@Nullable DependencyDescriptor descriptor, String beanName) {
/* 1831 */       this.descriptor = new DefaultListableBeanFactory.NestedDependencyDescriptor(descriptor);
/* 1832 */       this.optional = (this.descriptor.getDependencyType() == Optional.class);
/* 1833 */       this.beanName = beanName;
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getObject() throws BeansException {
/* 1838 */       if (this.optional) {
/* 1839 */         return DefaultListableBeanFactory.this.createOptionalDependency(this.descriptor, this.beanName, new Object[0]);
/*      */       }
/*      */       
/* 1842 */       Object result = DefaultListableBeanFactory.this.doResolveDependency(this.descriptor, this.beanName, (Set<String>)null, (TypeConverter)null);
/* 1843 */       if (result == null) {
/* 1844 */         throw new NoSuchBeanDefinitionException(this.descriptor.getResolvableType());
/*      */       }
/* 1846 */       return result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Object getObject(Object... args) throws BeansException {
/* 1852 */       if (this.optional) {
/* 1853 */         return DefaultListableBeanFactory.this.createOptionalDependency(this.descriptor, this.beanName, args);
/*      */       }
/*      */       
/* 1856 */       DependencyDescriptor descriptorToUse = new DependencyDescriptor(this.descriptor)
/*      */         {
/*      */           public Object resolveCandidate(String beanName, Class<?> requiredType, BeanFactory beanFactory) {
/* 1859 */             return beanFactory.getBean(beanName, args);
/*      */           }
/*      */         };
/* 1862 */       Object result = DefaultListableBeanFactory.this.doResolveDependency(descriptorToUse, this.beanName, (Set<String>)null, (TypeConverter)null);
/* 1863 */       if (result == null) {
/* 1864 */         throw new NoSuchBeanDefinitionException(this.descriptor.getResolvableType());
/*      */       }
/* 1866 */       return result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public Object getIfAvailable() throws BeansException {
/* 1873 */       if (this.optional) {
/* 1874 */         return DefaultListableBeanFactory.this.createOptionalDependency(this.descriptor, this.beanName, new Object[0]);
/*      */       }
/*      */       
/* 1877 */       DependencyDescriptor descriptorToUse = new DependencyDescriptor(this.descriptor)
/*      */         {
/*      */           public boolean isRequired() {
/* 1880 */             return false;
/*      */           }
/*      */         };
/* 1883 */       return DefaultListableBeanFactory.this.doResolveDependency(descriptorToUse, this.beanName, (Set<String>)null, (TypeConverter)null);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public Object getIfUnique() throws BeansException {
/* 1890 */       DependencyDescriptor descriptorToUse = new DependencyDescriptor(this.descriptor)
/*      */         {
/*      */           public boolean isRequired() {
/* 1893 */             return false;
/*      */           }
/*      */           
/*      */           @Nullable
/*      */           public Object resolveNotUnique(ResolvableType type, Map<String, Object> matchingBeans) {
/* 1898 */             return null;
/*      */           }
/*      */         };
/* 1901 */       if (this.optional) {
/* 1902 */         return DefaultListableBeanFactory.this.createOptionalDependency(descriptorToUse, this.beanName, new Object[0]);
/*      */       }
/*      */       
/* 1905 */       return DefaultListableBeanFactory.this.doResolveDependency(descriptorToUse, this.beanName, (Set<String>)null, (TypeConverter)null);
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     protected Object getValue() throws BeansException {
/* 1911 */       if (this.optional) {
/* 1912 */         return DefaultListableBeanFactory.this.createOptionalDependency(this.descriptor, this.beanName, new Object[0]);
/*      */       }
/*      */       
/* 1915 */       return DefaultListableBeanFactory.this.doResolveDependency(this.descriptor, this.beanName, (Set<String>)null, (TypeConverter)null);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Stream<Object> stream() {
/* 1921 */       return resolveStream(false);
/*      */     }
/*      */ 
/*      */     
/*      */     public Stream<Object> orderedStream() {
/* 1926 */       return resolveStream(true);
/*      */     }
/*      */ 
/*      */     
/*      */     private Stream<Object> resolveStream(boolean ordered) {
/* 1931 */       DependencyDescriptor descriptorToUse = new DefaultListableBeanFactory.StreamDependencyDescriptor(this.descriptor, ordered);
/* 1932 */       Object result = DefaultListableBeanFactory.this.doResolveDependency(descriptorToUse, this.beanName, (Set<String>)null, (TypeConverter)null);
/* 1933 */       return (result instanceof Stream) ? (Stream<Object>)result : Stream.<Object>of(result);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private class Jsr330Factory
/*      */     implements Serializable
/*      */   {
/*      */     private Jsr330Factory() {}
/*      */ 
/*      */     
/*      */     public Object createDependencyProvider(DependencyDescriptor descriptor, @Nullable String beanName) {
/* 1946 */       return new Jsr330Provider(descriptor, beanName);
/*      */     }
/*      */     
/*      */     private class Jsr330Provider
/*      */       extends DefaultListableBeanFactory.DependencyObjectProvider implements Provider<Object> {
/*      */       public Jsr330Provider(@Nullable DependencyDescriptor descriptor, String beanName) {
/* 1952 */         super(descriptor, beanName);
/*      */       }
/*      */ 
/*      */       
/*      */       @Nullable
/*      */       public Object get() throws BeansException {
/* 1958 */         return getValue();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class FactoryAwareOrderSourceProvider
/*      */     implements OrderComparator.OrderSourceProvider
/*      */   {
/*      */     private final Map<Object, String> instancesToBeanNames;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public FactoryAwareOrderSourceProvider(Map<Object, String> instancesToBeanNames) {
/* 1976 */       this.instancesToBeanNames = instancesToBeanNames;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public Object getOrderSource(Object obj) {
/* 1982 */       String beanName = this.instancesToBeanNames.get(obj);
/* 1983 */       if (beanName == null || !DefaultListableBeanFactory.this.containsBeanDefinition(beanName)) {
/* 1984 */         return null;
/*      */       }
/* 1986 */       RootBeanDefinition beanDefinition = DefaultListableBeanFactory.this.getMergedLocalBeanDefinition(beanName);
/* 1987 */       List<Object> sources = new ArrayList(2);
/* 1988 */       Method factoryMethod = beanDefinition.getResolvedFactoryMethod();
/* 1989 */       if (factoryMethod != null) {
/* 1990 */         sources.add(factoryMethod);
/*      */       }
/* 1992 */       Class<?> targetType = beanDefinition.getTargetType();
/* 1993 */       if (targetType != null && targetType != obj.getClass()) {
/* 1994 */         sources.add(targetType);
/*      */       }
/* 1996 */       return sources.toArray();
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/DefaultListableBeanFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */