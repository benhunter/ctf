/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Arrays;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.beans.factory.support.SimpleInstantiationStrategy;
/*     */ import org.springframework.cglib.core.ClassGenerator;
/*     */ import org.springframework.cglib.core.DefaultGeneratorStrategy;
/*     */ import org.springframework.cglib.core.GeneratorStrategy;
/*     */ import org.springframework.cglib.core.NamingPolicy;
/*     */ import org.springframework.cglib.core.SpringNamingPolicy;
/*     */ import org.springframework.cglib.proxy.Callback;
/*     */ import org.springframework.cglib.proxy.CallbackFilter;
/*     */ import org.springframework.cglib.proxy.Enhancer;
/*     */ import org.springframework.cglib.proxy.Factory;
/*     */ import org.springframework.cglib.proxy.MethodInterceptor;
/*     */ import org.springframework.cglib.proxy.MethodProxy;
/*     */ import org.springframework.cglib.proxy.NoOp;
/*     */ import org.springframework.cglib.transform.ClassEmitterTransformer;
/*     */ import org.springframework.cglib.transform.ClassTransformer;
/*     */ import org.springframework.cglib.transform.TransformingClassGenerator;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.objenesis.ObjenesisException;
/*     */ import org.springframework.objenesis.SpringObjenesis;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ConfigurationClassEnhancer
/*     */ {
/*  76 */   private static final Callback[] CALLBACKS = new Callback[] { new BeanMethodInterceptor(), new BeanFactoryAwareMethodInterceptor(), (Callback)NoOp.INSTANCE };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   private static final ConditionalCallbackFilter CALLBACK_FILTER = new ConditionalCallbackFilter(CALLBACKS);
/*     */ 
/*     */   
/*     */   private static final String BEAN_FACTORY_FIELD = "$$beanFactory";
/*     */   
/*  87 */   private static final Log logger = LogFactory.getLog(ConfigurationClassEnhancer.class);
/*     */   
/*  89 */   private static final SpringObjenesis objenesis = new SpringObjenesis();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> enhance(Class<?> configClass, @Nullable ClassLoader classLoader) {
/*  98 */     if (EnhancedConfiguration.class.isAssignableFrom(configClass)) {
/*  99 */       if (logger.isDebugEnabled()) {
/* 100 */         logger.debug(String.format("Ignoring request to enhance %s as it has already been enhanced. This usually indicates that more than one ConfigurationClassPostProcessor has been registered (e.g. via <context:annotation-config>). This is harmless, but you may want check your configuration and remove one CCPP if possible", new Object[] { configClass
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 105 */                 .getName() }));
/*     */       }
/* 107 */       return configClass;
/*     */     } 
/* 109 */     Class<?> enhancedClass = createClass(newEnhancer(configClass, classLoader));
/* 110 */     if (logger.isTraceEnabled()) {
/* 111 */       logger.trace(String.format("Successfully enhanced %s; enhanced class name is: %s", new Object[] { configClass
/* 112 */               .getName(), enhancedClass.getName() }));
/*     */     }
/* 114 */     return enhancedClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Enhancer newEnhancer(Class<?> configSuperClass, @Nullable ClassLoader classLoader) {
/* 121 */     Enhancer enhancer = new Enhancer();
/* 122 */     enhancer.setSuperclass(configSuperClass);
/* 123 */     enhancer.setInterfaces(new Class[] { EnhancedConfiguration.class });
/* 124 */     enhancer.setUseFactory(false);
/* 125 */     enhancer.setNamingPolicy((NamingPolicy)SpringNamingPolicy.INSTANCE);
/* 126 */     enhancer.setStrategy((GeneratorStrategy)new BeanFactoryAwareGeneratorStrategy(classLoader));
/* 127 */     enhancer.setCallbackFilter(CALLBACK_FILTER);
/* 128 */     enhancer.setCallbackTypes(CALLBACK_FILTER.getCallbackTypes());
/* 129 */     return enhancer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Class<?> createClass(Enhancer enhancer) {
/* 137 */     Class<?> subclass = enhancer.createClass();
/*     */ 
/*     */     
/* 140 */     Enhancer.registerStaticCallbacks(subclass, CALLBACKS);
/* 141 */     return subclass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface EnhancedConfiguration
/*     */     extends BeanFactoryAware {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static interface ConditionalCallback
/*     */     extends Callback
/*     */   {
/*     */     boolean isMatch(Method param1Method);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ConditionalCallbackFilter
/*     */     implements CallbackFilter
/*     */   {
/*     */     private final Callback[] callbacks;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Class<?>[] callbackTypes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ConditionalCallbackFilter(Callback[] callbacks) {
/* 181 */       this.callbacks = callbacks;
/* 182 */       this.callbackTypes = new Class[callbacks.length];
/* 183 */       for (int i = 0; i < callbacks.length; i++) {
/* 184 */         this.callbackTypes[i] = callbacks[i].getClass();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public int accept(Method method) {
/* 190 */       for (int i = 0; i < this.callbacks.length; i++) {
/* 191 */         Callback callback = this.callbacks[i];
/* 192 */         if (!(callback instanceof ConfigurationClassEnhancer.ConditionalCallback) || ((ConfigurationClassEnhancer.ConditionalCallback)callback).isMatch(method)) {
/* 193 */           return i;
/*     */         }
/*     */       } 
/* 196 */       throw new IllegalStateException("No callback available for method " + method.getName());
/*     */     }
/*     */     
/*     */     public Class<?>[] getCallbackTypes() {
/* 200 */       return this.callbackTypes;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class BeanFactoryAwareGeneratorStrategy
/*     */     extends DefaultGeneratorStrategy
/*     */   {
/*     */     @Nullable
/*     */     private final ClassLoader classLoader;
/*     */ 
/*     */ 
/*     */     
/*     */     public BeanFactoryAwareGeneratorStrategy(@Nullable ClassLoader classLoader) {
/* 216 */       this.classLoader = classLoader;
/*     */     }
/*     */ 
/*     */     
/*     */     protected ClassGenerator transform(ClassGenerator cg) throws Exception {
/* 221 */       ClassEmitterTransformer transformer = new ClassEmitterTransformer()
/*     */         {
/*     */           public void end_class() {
/* 224 */             declare_field(1, "$$beanFactory", Type.getType(BeanFactory.class), null);
/* 225 */             super.end_class();
/*     */           }
/*     */         };
/* 228 */       return (ClassGenerator)new TransformingClassGenerator(cg, (ClassTransformer)transformer);
/*     */     }
/*     */     
/*     */     public byte[] generate(ClassGenerator cg) throws Exception {
/*     */       ClassLoader threadContextClassLoader;
/* 233 */       if (this.classLoader == null) {
/* 234 */         return super.generate(cg);
/*     */       }
/*     */       
/* 237 */       Thread currentThread = Thread.currentThread();
/*     */       
/*     */       try {
/* 240 */         threadContextClassLoader = currentThread.getContextClassLoader();
/*     */       }
/* 242 */       catch (Throwable ex) {
/*     */         
/* 244 */         return super.generate(cg);
/*     */       } 
/*     */       
/* 247 */       boolean overrideClassLoader = !this.classLoader.equals(threadContextClassLoader);
/* 248 */       if (overrideClassLoader) {
/* 249 */         currentThread.setContextClassLoader(this.classLoader);
/*     */       }
/*     */       try {
/* 252 */         return super.generate(cg);
/*     */       } finally {
/*     */         
/* 255 */         if (overrideClassLoader)
/*     */         {
/* 257 */           currentThread.setContextClassLoader(threadContextClassLoader);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class BeanFactoryAwareMethodInterceptor
/*     */     implements MethodInterceptor, ConditionalCallback
/*     */   {
/*     */     private BeanFactoryAwareMethodInterceptor() {}
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
/* 274 */       Field field = ReflectionUtils.findField(obj.getClass(), "$$beanFactory");
/* 275 */       Assert.state((field != null), "Unable to find generated BeanFactory field");
/* 276 */       field.set(obj, args[0]);
/*     */ 
/*     */ 
/*     */       
/* 280 */       if (BeanFactoryAware.class.isAssignableFrom(ClassUtils.getUserClass(obj.getClass().getSuperclass()))) {
/* 281 */         return proxy.invokeSuper(obj, args);
/*     */       }
/* 283 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isMatch(Method candidateMethod) {
/* 288 */       return isSetBeanFactory(candidateMethod);
/*     */     }
/*     */     
/*     */     public static boolean isSetBeanFactory(Method candidateMethod) {
/* 292 */       return (candidateMethod.getName().equals("setBeanFactory") && candidateMethod
/* 293 */         .getParameterCount() == 1 && BeanFactory.class == candidateMethod
/* 294 */         .getParameterTypes()[0] && BeanFactoryAware.class
/* 295 */         .isAssignableFrom(candidateMethod.getDeclaringClass()));
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
/*     */   private static class BeanMethodInterceptor
/*     */     implements MethodInterceptor, ConditionalCallback
/*     */   {
/*     */     private BeanMethodInterceptor() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Object intercept(Object enhancedConfigInstance, Method beanMethod, Object[] beanMethodArgs, MethodProxy cglibMethodProxy) throws Throwable {
/* 319 */       ConfigurableBeanFactory beanFactory = getBeanFactory(enhancedConfigInstance);
/* 320 */       String beanName = BeanAnnotationHelper.determineBeanNameFor(beanMethod);
/*     */ 
/*     */       
/* 323 */       if (BeanAnnotationHelper.isScopedProxy(beanMethod)) {
/* 324 */         String scopedBeanName = ScopedProxyCreator.getTargetBeanName(beanName);
/* 325 */         if (beanFactory.isCurrentlyInCreation(scopedBeanName)) {
/* 326 */           beanName = scopedBeanName;
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 337 */       if (factoryContainsBean(beanFactory, "&" + beanName) && 
/* 338 */         factoryContainsBean(beanFactory, beanName)) {
/* 339 */         Object factoryBean = beanFactory.getBean("&" + beanName);
/* 340 */         if (!(factoryBean instanceof org.springframework.aop.scope.ScopedProxyFactoryBean))
/*     */         {
/*     */ 
/*     */ 
/*     */           
/* 345 */           return enhanceFactoryBean(factoryBean, beanMethod.getReturnType(), beanFactory, beanName);
/*     */         }
/*     */       } 
/*     */       
/* 349 */       if (isCurrentlyInvokedFactoryMethod(beanMethod)) {
/*     */ 
/*     */ 
/*     */         
/* 353 */         if (ConfigurationClassEnhancer.logger.isInfoEnabled() && BeanFactoryPostProcessor.class
/* 354 */           .isAssignableFrom(beanMethod.getReturnType())) {
/* 355 */           ConfigurationClassEnhancer.logger.info(String.format("@Bean method %s.%s is non-static and returns an object assignable to Spring's BeanFactoryPostProcessor interface. This will result in a failure to process annotations such as @Autowired, @Resource and @PostConstruct within the method's declaring @Configuration class. Add the 'static' modifier to this method to avoid these container lifecycle issues; see @Bean javadoc for complete details.", new Object[] { beanMethod
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/* 361 */                   .getDeclaringClass().getSimpleName(), beanMethod.getName() }));
/*     */         }
/* 363 */         return cglibMethodProxy.invokeSuper(enhancedConfigInstance, beanMethodArgs);
/*     */       } 
/*     */       
/* 366 */       return resolveBeanReference(beanMethod, beanMethodArgs, beanFactory, beanName);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Object resolveBeanReference(Method beanMethod, Object[] beanMethodArgs, ConfigurableBeanFactory beanFactory, String beanName) {
/* 376 */       boolean alreadyInCreation = beanFactory.isCurrentlyInCreation(beanName);
/*     */       try {
/* 378 */         if (alreadyInCreation) {
/* 379 */           beanFactory.setCurrentlyInCreation(beanName, false);
/*     */         }
/* 381 */         boolean useArgs = !ObjectUtils.isEmpty(beanMethodArgs);
/* 382 */         if (useArgs && beanFactory.isSingleton(beanName))
/*     */         {
/*     */ 
/*     */           
/* 386 */           for (Object arg : beanMethodArgs) {
/* 387 */             if (arg == null) {
/* 388 */               useArgs = false;
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         }
/* 394 */         Object beanInstance = useArgs ? beanFactory.getBean(beanName, beanMethodArgs) : beanFactory.getBean(beanName);
/* 395 */         if (!ClassUtils.isAssignableValue(beanMethod.getReturnType(), beanInstance))
/*     */         {
/* 397 */           if (beanInstance.equals(null)) {
/* 398 */             if (ConfigurationClassEnhancer.logger.isDebugEnabled()) {
/* 399 */               ConfigurationClassEnhancer.logger.debug(String.format("@Bean method %s.%s called as bean reference for type [%s] returned null bean; resolving to null value.", new Object[] { beanMethod
/*     */                       
/* 401 */                       .getDeclaringClass().getSimpleName(), beanMethod.getName(), beanMethod
/* 402 */                       .getReturnType().getName() }));
/*     */             }
/* 404 */             beanInstance = null;
/*     */           } else {
/*     */             
/* 407 */             String msg = String.format("@Bean method %s.%s called as bean reference for type [%s] but overridden by non-compatible bean instance of type [%s].", new Object[] { beanMethod
/*     */                   
/* 409 */                   .getDeclaringClass().getSimpleName(), beanMethod.getName(), beanMethod
/* 410 */                   .getReturnType().getName(), beanInstance.getClass().getName() });
/*     */             try {
/* 412 */               BeanDefinition beanDefinition = beanFactory.getMergedBeanDefinition(beanName);
/* 413 */               msg = msg + " Overriding bean of same name declared in: " + beanDefinition.getResourceDescription();
/*     */             }
/* 415 */             catch (NoSuchBeanDefinitionException noSuchBeanDefinitionException) {}
/*     */ 
/*     */             
/* 418 */             throw new IllegalStateException(msg);
/*     */           } 
/*     */         }
/* 421 */         Method currentlyInvoked = SimpleInstantiationStrategy.getCurrentlyInvokedFactoryMethod();
/* 422 */         if (currentlyInvoked != null) {
/* 423 */           String outerBeanName = BeanAnnotationHelper.determineBeanNameFor(currentlyInvoked);
/* 424 */           beanFactory.registerDependentBean(beanName, outerBeanName);
/*     */         } 
/* 426 */         return beanInstance;
/*     */       } finally {
/*     */         
/* 429 */         if (alreadyInCreation) {
/* 430 */           beanFactory.setCurrentlyInCreation(beanName, true);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isMatch(Method candidateMethod) {
/* 437 */       return (candidateMethod.getDeclaringClass() != Object.class && 
/* 438 */         !ConfigurationClassEnhancer.BeanFactoryAwareMethodInterceptor.isSetBeanFactory(candidateMethod) && 
/* 439 */         BeanAnnotationHelper.isBeanAnnotated(candidateMethod));
/*     */     }
/*     */     
/*     */     private ConfigurableBeanFactory getBeanFactory(Object enhancedConfigInstance) {
/* 443 */       Field field = ReflectionUtils.findField(enhancedConfigInstance.getClass(), "$$beanFactory");
/* 444 */       Assert.state((field != null), "Unable to find generated bean factory field");
/* 445 */       Object beanFactory = ReflectionUtils.getField(field, enhancedConfigInstance);
/* 446 */       Assert.state((beanFactory != null), "BeanFactory has not been injected into @Configuration class");
/* 447 */       Assert.state(beanFactory instanceof ConfigurableBeanFactory, "Injected BeanFactory is not a ConfigurableBeanFactory");
/*     */       
/* 449 */       return (ConfigurableBeanFactory)beanFactory;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean factoryContainsBean(ConfigurableBeanFactory beanFactory, String beanName) {
/* 466 */       return (beanFactory.containsBean(beanName) && !beanFactory.isCurrentlyInCreation(beanName));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean isCurrentlyInvokedFactoryMethod(Method method) {
/* 476 */       Method currentlyInvoked = SimpleInstantiationStrategy.getCurrentlyInvokedFactoryMethod();
/* 477 */       return (currentlyInvoked != null && method.getName().equals(currentlyInvoked.getName()) && 
/* 478 */         Arrays.equals((Object[])method.getParameterTypes(), (Object[])currentlyInvoked.getParameterTypes()));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Object enhanceFactoryBean(Object factoryBean, Class<?> exposedType, ConfigurableBeanFactory beanFactory, String beanName) {
/*     */       try {
/* 492 */         Class<?> clazz = factoryBean.getClass();
/* 493 */         boolean finalClass = Modifier.isFinal(clazz.getModifiers());
/* 494 */         boolean finalMethod = Modifier.isFinal(clazz.getMethod("getObject", new Class[0]).getModifiers());
/* 495 */         if (finalClass || finalMethod) {
/* 496 */           if (exposedType.isInterface()) {
/* 497 */             if (ConfigurationClassEnhancer.logger.isTraceEnabled()) {
/* 498 */               ConfigurationClassEnhancer.logger.trace("Creating interface proxy for FactoryBean '" + beanName + "' of type [" + clazz
/* 499 */                   .getName() + "] for use within another @Bean method because its " + (finalClass ? "implementation class" : "getObject() method") + " is final: Otherwise a getObject() call would not be routed to the factory.");
/*     */             }
/*     */ 
/*     */             
/* 503 */             return createInterfaceProxyForFactoryBean(factoryBean, exposedType, beanFactory, beanName);
/*     */           } 
/*     */           
/* 506 */           if (ConfigurationClassEnhancer.logger.isDebugEnabled()) {
/* 507 */             ConfigurationClassEnhancer.logger.debug("Unable to proxy FactoryBean '" + beanName + "' of type [" + clazz
/* 508 */                 .getName() + "] for use within another @Bean method because its " + (finalClass ? "implementation class" : "getObject() method") + " is final: A getObject() call will NOT be routed to the factory. Consider declaring the return type as a FactoryBean interface.");
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 513 */           return factoryBean;
/*     */         }
/*     */       
/*     */       }
/* 517 */       catch (NoSuchMethodException noSuchMethodException) {}
/*     */ 
/*     */ 
/*     */       
/* 521 */       return createCglibProxyForFactoryBean(factoryBean, beanFactory, beanName);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private Object createInterfaceProxyForFactoryBean(Object factoryBean, Class<?> interfaceType, ConfigurableBeanFactory beanFactory, String beanName) {
/* 527 */       return Proxy.newProxyInstance(factoryBean
/* 528 */           .getClass().getClassLoader(), new Class[] { interfaceType
/*     */           },
/* 530 */           (proxy, method, args) -> (method.getName().equals("getObject") && args == null) ? beanFactory.getBean(beanName) : ReflectionUtils.invokeMethod(method, factoryBean, args));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Object createCglibProxyForFactoryBean(Object factoryBean, ConfigurableBeanFactory beanFactory, String beanName) {
/* 540 */       Enhancer enhancer = new Enhancer();
/* 541 */       enhancer.setSuperclass(factoryBean.getClass());
/* 542 */       enhancer.setNamingPolicy((NamingPolicy)SpringNamingPolicy.INSTANCE);
/* 543 */       enhancer.setCallbackType(MethodInterceptor.class);
/*     */ 
/*     */ 
/*     */       
/* 547 */       Class<?> fbClass = enhancer.createClass();
/* 548 */       Object fbProxy = null;
/*     */       
/* 550 */       if (ConfigurationClassEnhancer.objenesis.isWorthTrying()) {
/*     */         try {
/* 552 */           fbProxy = ConfigurationClassEnhancer.objenesis.newInstance(fbClass, enhancer.getUseCache());
/*     */         }
/* 554 */         catch (ObjenesisException ex) {
/* 555 */           ConfigurationClassEnhancer.logger.debug("Unable to instantiate enhanced FactoryBean using Objenesis, falling back to regular construction", (Throwable)ex);
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 560 */       if (fbProxy == null) {
/*     */         try {
/* 562 */           fbProxy = ReflectionUtils.accessibleConstructor(fbClass, new Class[0]).newInstance(new Object[0]);
/*     */         }
/* 564 */         catch (Throwable ex) {
/* 565 */           throw new IllegalStateException("Unable to instantiate enhanced FactoryBean using Objenesis, and regular FactoryBean instantiation via default constructor fails as well", ex);
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 570 */       ((Factory)fbProxy).setCallback(0, (Callback)((obj, method, args, proxy) -> 
/* 571 */           (method.getName().equals("getObject") && args.length == 0) ? beanFactory.getBean(beanName) : proxy.invoke(factoryBean, args)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 577 */       return fbProxy;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/ConfigurationClassEnhancer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */