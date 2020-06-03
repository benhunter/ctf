/*      */ package org.springframework.aop.framework;
/*      */ 
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.lang.reflect.UndeclaredThrowableException;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.WeakHashMap;
/*      */ import org.aopalliance.aop.Advice;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.apache.commons.logging.LogFactory;
/*      */ import org.springframework.aop.Advisor;
/*      */ import org.springframework.aop.AopInvocationException;
/*      */ import org.springframework.aop.PointcutAdvisor;
/*      */ import org.springframework.aop.RawTargetAccess;
/*      */ import org.springframework.aop.TargetSource;
/*      */ import org.springframework.aop.support.AopUtils;
/*      */ import org.springframework.cglib.core.ClassGenerator;
/*      */ import org.springframework.cglib.core.CodeGenerationException;
/*      */ import org.springframework.cglib.core.GeneratorStrategy;
/*      */ import org.springframework.cglib.core.NamingPolicy;
/*      */ import org.springframework.cglib.core.SpringNamingPolicy;
/*      */ import org.springframework.cglib.proxy.Callback;
/*      */ import org.springframework.cglib.proxy.CallbackFilter;
/*      */ import org.springframework.cglib.proxy.Dispatcher;
/*      */ import org.springframework.cglib.proxy.Enhancer;
/*      */ import org.springframework.cglib.proxy.Factory;
/*      */ import org.springframework.cglib.proxy.MethodInterceptor;
/*      */ import org.springframework.cglib.proxy.MethodProxy;
/*      */ import org.springframework.cglib.proxy.NoOp;
/*      */ import org.springframework.cglib.transform.impl.UndeclaredThrowableStrategy;
/*      */ import org.springframework.core.SmartClassLoader;
/*      */ import org.springframework.lang.Nullable;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ClassUtils;
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
/*      */ class CglibAopProxy
/*      */   implements AopProxy, Serializable
/*      */ {
/*      */   private static final int AOP_PROXY = 0;
/*      */   private static final int INVOKE_TARGET = 1;
/*      */   private static final int NO_OVERRIDE = 2;
/*      */   private static final int DISPATCH_TARGET = 3;
/*      */   private static final int DISPATCH_ADVISED = 4;
/*      */   private static final int INVOKE_EQUALS = 5;
/*      */   private static final int INVOKE_HASHCODE = 6;
/*   97 */   protected static final Log logger = LogFactory.getLog(CglibAopProxy.class);
/*      */ 
/*      */   
/*  100 */   private static final Map<Class<?>, Boolean> validatedClasses = new WeakHashMap<>();
/*      */ 
/*      */   
/*      */   protected final AdvisedSupport advised;
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected Object[] constructorArgs;
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected Class<?>[] constructorArgTypes;
/*      */   
/*      */   private final transient AdvisedDispatcher advisedDispatcher;
/*      */   
/*  115 */   private transient Map<String, Integer> fixedInterceptorMap = Collections.emptyMap();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private transient int fixedInterceptorOffset;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CglibAopProxy(AdvisedSupport config) throws AopConfigException {
/*  127 */     Assert.notNull(config, "AdvisedSupport must not be null");
/*  128 */     if ((config.getAdvisors()).length == 0 && config.getTargetSource() == AdvisedSupport.EMPTY_TARGET_SOURCE) {
/*  129 */       throw new AopConfigException("No advisors and no TargetSource specified");
/*      */     }
/*  131 */     this.advised = config;
/*  132 */     this.advisedDispatcher = new AdvisedDispatcher(this.advised);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setConstructorArguments(@Nullable Object[] constructorArgs, @Nullable Class<?>[] constructorArgTypes) {
/*  141 */     if (constructorArgs == null || constructorArgTypes == null) {
/*  142 */       throw new IllegalArgumentException("Both 'constructorArgs' and 'constructorArgTypes' need to be specified");
/*      */     }
/*  144 */     if (constructorArgs.length != constructorArgTypes.length) {
/*  145 */       throw new IllegalArgumentException("Number of 'constructorArgs' (" + constructorArgs.length + ") must match number of 'constructorArgTypes' (" + constructorArgTypes.length + ")");
/*      */     }
/*      */     
/*  148 */     this.constructorArgs = constructorArgs;
/*  149 */     this.constructorArgTypes = constructorArgTypes;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getProxy() {
/*  155 */     return getProxy(null);
/*      */   }
/*      */ 
/*      */   
/*      */   public Object getProxy(@Nullable ClassLoader classLoader) {
/*  160 */     if (logger.isTraceEnabled()) {
/*  161 */       logger.trace("Creating CGLIB proxy: " + this.advised.getTargetSource());
/*      */     }
/*      */     
/*      */     try {
/*  165 */       Class<?> rootClass = this.advised.getTargetClass();
/*  166 */       Assert.state((rootClass != null), "Target class must be available for creating a CGLIB proxy");
/*      */       
/*  168 */       Class<?> proxySuperClass = rootClass;
/*  169 */       if (ClassUtils.isCglibProxyClass(rootClass)) {
/*  170 */         proxySuperClass = rootClass.getSuperclass();
/*  171 */         Class<?>[] additionalInterfaces = rootClass.getInterfaces();
/*  172 */         for (Class<?> additionalInterface : additionalInterfaces) {
/*  173 */           this.advised.addInterface(additionalInterface);
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/*  178 */       validateClassIfNecessary(proxySuperClass, classLoader);
/*      */ 
/*      */       
/*  181 */       Enhancer enhancer = createEnhancer();
/*  182 */       if (classLoader != null) {
/*  183 */         enhancer.setClassLoader(classLoader);
/*  184 */         if (classLoader instanceof SmartClassLoader && ((SmartClassLoader)classLoader)
/*  185 */           .isClassReloadable(proxySuperClass)) {
/*  186 */           enhancer.setUseCache(false);
/*      */         }
/*      */       } 
/*  189 */       enhancer.setSuperclass(proxySuperClass);
/*  190 */       enhancer.setInterfaces(AopProxyUtils.completeProxiedInterfaces(this.advised));
/*  191 */       enhancer.setNamingPolicy((NamingPolicy)SpringNamingPolicy.INSTANCE);
/*  192 */       enhancer.setStrategy((GeneratorStrategy)new ClassLoaderAwareUndeclaredThrowableStrategy(classLoader));
/*      */       
/*  194 */       Callback[] callbacks = getCallbacks(rootClass);
/*  195 */       Class<?>[] types = new Class[callbacks.length];
/*  196 */       for (int x = 0; x < types.length; x++) {
/*  197 */         types[x] = callbacks[x].getClass();
/*      */       }
/*      */       
/*  200 */       enhancer.setCallbackFilter(new ProxyCallbackFilter(this.advised
/*  201 */             .getConfigurationOnlyCopy(), this.fixedInterceptorMap, this.fixedInterceptorOffset));
/*  202 */       enhancer.setCallbackTypes(types);
/*      */ 
/*      */       
/*  205 */       return createProxyClassAndInstance(enhancer, callbacks);
/*      */     }
/*  207 */     catch (CodeGenerationException|IllegalArgumentException ex) {
/*  208 */       throw new AopConfigException("Could not generate CGLIB subclass of " + this.advised.getTargetClass() + ": Common causes of this problem include using a final class or a non-visible class", ex);
/*      */ 
/*      */     
/*      */     }
/*  212 */     catch (Throwable ex) {
/*      */       
/*  214 */       throw new AopConfigException("Unexpected AOP exception", ex);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected Object createProxyClassAndInstance(Enhancer enhancer, Callback[] callbacks) {
/*  219 */     enhancer.setInterceptDuringConstruction(false);
/*  220 */     enhancer.setCallbacks(callbacks);
/*  221 */     return (this.constructorArgs != null && this.constructorArgTypes != null) ? enhancer
/*  222 */       .create(this.constructorArgTypes, this.constructorArgs) : enhancer
/*  223 */       .create();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Enhancer createEnhancer() {
/*  231 */     return new Enhancer();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void validateClassIfNecessary(Class<?> proxySuperClass, @Nullable ClassLoader proxyClassLoader) {
/*  239 */     if (logger.isWarnEnabled()) {
/*  240 */       synchronized (validatedClasses) {
/*  241 */         if (!validatedClasses.containsKey(proxySuperClass)) {
/*  242 */           doValidateClass(proxySuperClass, proxyClassLoader, 
/*  243 */               ClassUtils.getAllInterfacesForClassAsSet(proxySuperClass));
/*  244 */           validatedClasses.put(proxySuperClass, Boolean.TRUE);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void doValidateClass(Class<?> proxySuperClass, @Nullable ClassLoader proxyClassLoader, Set<Class<?>> ifcs) {
/*  255 */     if (proxySuperClass != Object.class) {
/*  256 */       Method[] methods = proxySuperClass.getDeclaredMethods();
/*  257 */       for (Method method : methods) {
/*  258 */         int mod = method.getModifiers();
/*  259 */         if (!Modifier.isStatic(mod) && !Modifier.isPrivate(mod)) {
/*  260 */           if (Modifier.isFinal(mod)) {
/*  261 */             if (implementsInterface(method, ifcs)) {
/*  262 */               logger.info("Unable to proxy interface-implementing method [" + method + "] because it is marked as final: Consider using interface-based JDK proxies instead!");
/*      */             }
/*      */             
/*  265 */             logger.debug("Final method [" + method + "] cannot get proxied via CGLIB: Calls to this method will NOT be routed to the target instance and might lead to NPEs against uninitialized fields in the proxy instance.");
/*      */ 
/*      */           
/*      */           }
/*  269 */           else if (!Modifier.isPublic(mod) && !Modifier.isProtected(mod) && proxyClassLoader != null && proxySuperClass
/*  270 */             .getClassLoader() != proxyClassLoader) {
/*  271 */             logger.debug("Method [" + method + "] is package-visible across different ClassLoaders and cannot get proxied via CGLIB: Declare this method as public or protected if you need to support invocations through the proxy.");
/*      */           } 
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/*  277 */       doValidateClass(proxySuperClass.getSuperclass(), proxyClassLoader, ifcs);
/*      */     } 
/*      */   }
/*      */   
/*      */   private Callback[] getCallbacks(Class<?> rootClass) throws Exception {
/*      */     Callback targetInterceptor, callbacks[];
/*  283 */     boolean exposeProxy = this.advised.isExposeProxy();
/*  284 */     boolean isFrozen = this.advised.isFrozen();
/*  285 */     boolean isStatic = this.advised.getTargetSource().isStatic();
/*      */ 
/*      */     
/*  288 */     DynamicAdvisedInterceptor dynamicAdvisedInterceptor = new DynamicAdvisedInterceptor(this.advised);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  293 */     if (exposeProxy) {
/*      */ 
/*      */       
/*  296 */       targetInterceptor = isStatic ? (Callback)new StaticUnadvisedExposedInterceptor(this.advised.getTargetSource().getTarget()) : (Callback)new DynamicUnadvisedExposedInterceptor(this.advised.getTargetSource());
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  301 */       targetInterceptor = isStatic ? (Callback)new StaticUnadvisedInterceptor(this.advised.getTargetSource().getTarget()) : (Callback)new DynamicUnadvisedInterceptor(this.advised.getTargetSource());
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  307 */     Callback targetDispatcher = isStatic ? (Callback)new StaticDispatcher(this.advised.getTargetSource().getTarget()) : (Callback)new SerializableNoOp();
/*      */     
/*  309 */     Callback[] mainCallbacks = { (Callback)dynamicAdvisedInterceptor, targetInterceptor, (Callback)new SerializableNoOp(), targetDispatcher, (Callback)this.advisedDispatcher, (Callback)new EqualsInterceptor(this.advised), (Callback)new HashCodeInterceptor(this.advised) };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  323 */     if (isStatic && isFrozen) {
/*  324 */       Method[] methods = rootClass.getMethods();
/*  325 */       Callback[] fixedCallbacks = new Callback[methods.length];
/*  326 */       this.fixedInterceptorMap = new HashMap<>(methods.length);
/*      */ 
/*      */       
/*  329 */       for (int x = 0; x < methods.length; x++) {
/*  330 */         List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(methods[x], rootClass);
/*  331 */         fixedCallbacks[x] = (Callback)new FixedChainStaticTargetInterceptor(chain, this.advised
/*  332 */             .getTargetSource().getTarget(), this.advised.getTargetClass());
/*  333 */         this.fixedInterceptorMap.put(methods[x].toString(), Integer.valueOf(x));
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  338 */       callbacks = new Callback[mainCallbacks.length + fixedCallbacks.length];
/*  339 */       System.arraycopy(mainCallbacks, 0, callbacks, 0, mainCallbacks.length);
/*  340 */       System.arraycopy(fixedCallbacks, 0, callbacks, mainCallbacks.length, fixedCallbacks.length);
/*  341 */       this.fixedInterceptorOffset = mainCallbacks.length;
/*      */     } else {
/*      */       
/*  344 */       callbacks = mainCallbacks;
/*      */     } 
/*  346 */     return callbacks;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object other) {
/*  352 */     return (this == other || (other instanceof CglibAopProxy && 
/*  353 */       AopProxyUtils.equalsInProxy(this.advised, ((CglibAopProxy)other).advised)));
/*      */   }
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  358 */     return CglibAopProxy.class.hashCode() * 13 + this.advised.getTargetSource().hashCode();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean implementsInterface(Method method, Set<Class<?>> ifcs) {
/*  366 */     for (Class<?> ifc : ifcs) {
/*  367 */       if (ClassUtils.hasMethod(ifc, method.getName(), method.getParameterTypes())) {
/*  368 */         return true;
/*      */       }
/*      */     } 
/*  371 */     return false;
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
/*      */   private static Object processReturnType(Object proxy, @Nullable Object target, Method method, @Nullable Object returnValue) {
/*  383 */     if (returnValue != null && returnValue == target && 
/*  384 */       !RawTargetAccess.class.isAssignableFrom(method.getDeclaringClass()))
/*      */     {
/*      */       
/*  387 */       returnValue = proxy;
/*      */     }
/*  389 */     Class<?> returnType = method.getReturnType();
/*  390 */     if (returnValue == null && returnType != void.class && returnType.isPrimitive()) {
/*  391 */       throw new AopInvocationException("Null return value from advice does not match primitive return type for: " + method);
/*      */     }
/*      */     
/*  394 */     return returnValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class SerializableNoOp
/*      */     implements NoOp, Serializable {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class StaticUnadvisedInterceptor
/*      */     implements MethodInterceptor, Serializable
/*      */   {
/*      */     @Nullable
/*      */     private final Object target;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public StaticUnadvisedInterceptor(@Nullable Object target) {
/*  418 */       this.target = target;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
/*  424 */       Object retVal = methodProxy.invoke(this.target, args);
/*  425 */       return CglibAopProxy.processReturnType(proxy, this.target, method, retVal);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class StaticUnadvisedExposedInterceptor
/*      */     implements MethodInterceptor, Serializable
/*      */   {
/*      */     @Nullable
/*      */     private final Object target;
/*      */ 
/*      */ 
/*      */     
/*      */     public StaticUnadvisedExposedInterceptor(@Nullable Object target) {
/*  440 */       this.target = target;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
/*  446 */       Object oldProxy = null;
/*      */       try {
/*  448 */         oldProxy = AopContext.setCurrentProxy(proxy);
/*  449 */         Object retVal = methodProxy.invoke(this.target, args);
/*  450 */         return CglibAopProxy.processReturnType(proxy, this.target, method, retVal);
/*      */       } finally {
/*      */         
/*  453 */         AopContext.setCurrentProxy(oldProxy);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class DynamicUnadvisedInterceptor
/*      */     implements MethodInterceptor, Serializable
/*      */   {
/*      */     private final TargetSource targetSource;
/*      */ 
/*      */ 
/*      */     
/*      */     public DynamicUnadvisedInterceptor(TargetSource targetSource) {
/*  469 */       this.targetSource = targetSource;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
/*  475 */       Object target = this.targetSource.getTarget();
/*      */       try {
/*  477 */         Object retVal = methodProxy.invoke(target, args);
/*  478 */         return CglibAopProxy.processReturnType(proxy, target, method, retVal);
/*      */       } finally {
/*      */         
/*  481 */         if (target != null) {
/*  482 */           this.targetSource.releaseTarget(target);
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class DynamicUnadvisedExposedInterceptor
/*      */     implements MethodInterceptor, Serializable
/*      */   {
/*      */     private final TargetSource targetSource;
/*      */ 
/*      */     
/*      */     public DynamicUnadvisedExposedInterceptor(TargetSource targetSource) {
/*  497 */       this.targetSource = targetSource;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
/*  503 */       Object oldProxy = null;
/*  504 */       Object target = this.targetSource.getTarget();
/*      */       try {
/*  506 */         oldProxy = AopContext.setCurrentProxy(proxy);
/*  507 */         Object retVal = methodProxy.invoke(target, args);
/*  508 */         return CglibAopProxy.processReturnType(proxy, target, method, retVal);
/*      */       } finally {
/*      */         
/*  511 */         AopContext.setCurrentProxy(oldProxy);
/*  512 */         if (target != null) {
/*  513 */           this.targetSource.releaseTarget(target);
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class StaticDispatcher
/*      */     implements Dispatcher, Serializable
/*      */   {
/*      */     @Nullable
/*      */     private Object target;
/*      */ 
/*      */ 
/*      */     
/*      */     public StaticDispatcher(@Nullable Object target) {
/*  531 */       this.target = target;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public Object loadObject() {
/*  537 */       return this.target;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class AdvisedDispatcher
/*      */     implements Dispatcher, Serializable
/*      */   {
/*      */     private final AdvisedSupport advised;
/*      */ 
/*      */     
/*      */     public AdvisedDispatcher(AdvisedSupport advised) {
/*  550 */       this.advised = advised;
/*      */     }
/*      */ 
/*      */     
/*      */     public Object loadObject() throws Exception {
/*  555 */       return this.advised;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class EqualsInterceptor
/*      */     implements MethodInterceptor, Serializable
/*      */   {
/*      */     private final AdvisedSupport advised;
/*      */ 
/*      */ 
/*      */     
/*      */     public EqualsInterceptor(AdvisedSupport advised) {
/*  569 */       this.advised = advised;
/*      */     }
/*      */ 
/*      */     
/*      */     public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) {
/*  574 */       Object other = args[0];
/*  575 */       if (proxy == other) {
/*  576 */         return Boolean.valueOf(true);
/*      */       }
/*  578 */       if (other instanceof Factory) {
/*  579 */         Callback callback = ((Factory)other).getCallback(5);
/*  580 */         if (!(callback instanceof EqualsInterceptor)) {
/*  581 */           return Boolean.valueOf(false);
/*      */         }
/*  583 */         AdvisedSupport otherAdvised = ((EqualsInterceptor)callback).advised;
/*  584 */         return Boolean.valueOf(AopProxyUtils.equalsInProxy(this.advised, otherAdvised));
/*      */       } 
/*      */       
/*  587 */       return Boolean.valueOf(false);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class HashCodeInterceptor
/*      */     implements MethodInterceptor, Serializable
/*      */   {
/*      */     private final AdvisedSupport advised;
/*      */ 
/*      */ 
/*      */     
/*      */     public HashCodeInterceptor(AdvisedSupport advised) {
/*  602 */       this.advised = advised;
/*      */     }
/*      */ 
/*      */     
/*      */     public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) {
/*  607 */       return Integer.valueOf(CglibAopProxy.class.hashCode() * 13 + this.advised.getTargetSource().hashCode());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class FixedChainStaticTargetInterceptor
/*      */     implements MethodInterceptor, Serializable
/*      */   {
/*      */     private final List<Object> adviceChain;
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     private final Object target;
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     private final Class<?> targetClass;
/*      */ 
/*      */     
/*      */     public FixedChainStaticTargetInterceptor(List<Object> adviceChain, @Nullable Object target, @Nullable Class<?> targetClass) {
/*  628 */       this.adviceChain = adviceChain;
/*  629 */       this.target = target;
/*  630 */       this.targetClass = targetClass;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
/*  636 */       CglibAopProxy.CglibMethodInvocation cglibMethodInvocation = new CglibAopProxy.CglibMethodInvocation(proxy, this.target, method, args, this.targetClass, this.adviceChain, methodProxy);
/*      */ 
/*      */       
/*  639 */       Object retVal = cglibMethodInvocation.proceed();
/*  640 */       retVal = CglibAopProxy.processReturnType(proxy, this.target, method, retVal);
/*  641 */       return retVal;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class DynamicAdvisedInterceptor
/*      */     implements MethodInterceptor, Serializable
/*      */   {
/*      */     private final AdvisedSupport advised;
/*      */ 
/*      */ 
/*      */     
/*      */     public DynamicAdvisedInterceptor(AdvisedSupport advised) {
/*  655 */       this.advised = advised;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
/*  661 */       Object oldProxy = null;
/*  662 */       boolean setProxyContext = false;
/*  663 */       Object target = null;
/*  664 */       TargetSource targetSource = this.advised.getTargetSource();
/*      */       try {
/*  666 */         if (this.advised.exposeProxy) {
/*      */           
/*  668 */           oldProxy = AopContext.setCurrentProxy(proxy);
/*  669 */           setProxyContext = true;
/*      */         } 
/*      */         
/*  672 */         target = targetSource.getTarget();
/*  673 */         Class<?> targetClass = (target != null) ? target.getClass() : null;
/*  674 */         List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
/*      */ 
/*      */ 
/*      */         
/*  678 */         if (chain.isEmpty() && Modifier.isPublic(method.getModifiers())) {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  683 */           Object[] argsToUse = AopProxyUtils.adaptArgumentsIfNecessary(method, args);
/*  684 */           retVal = methodProxy.invoke(target, argsToUse);
/*      */         }
/*      */         else {
/*      */           
/*  688 */           retVal = (new CglibAopProxy.CglibMethodInvocation(proxy, target, method, args, targetClass, chain, methodProxy)).proceed();
/*      */         } 
/*  690 */         Object retVal = CglibAopProxy.processReturnType(proxy, target, method, retVal);
/*  691 */         return retVal;
/*      */       } finally {
/*      */         
/*  694 */         if (target != null && !targetSource.isStatic()) {
/*  695 */           targetSource.releaseTarget(target);
/*      */         }
/*  697 */         if (setProxyContext)
/*      */         {
/*  699 */           AopContext.setCurrentProxy(oldProxy);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object other) {
/*  706 */       return (this == other || (other instanceof DynamicAdvisedInterceptor && this.advised
/*      */         
/*  708 */         .equals(((DynamicAdvisedInterceptor)other).advised)));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  716 */       return this.advised.hashCode();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class CglibMethodInvocation
/*      */     extends ReflectiveMethodInvocation
/*      */   {
/*      */     @Nullable
/*      */     private final MethodProxy methodProxy;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public CglibMethodInvocation(Object proxy, @Nullable Object target, Method method, Object[] arguments, @Nullable Class<?> targetClass, List<Object> interceptorsAndDynamicMethodMatchers, MethodProxy methodProxy) {
/*  733 */       super(proxy, target, method, arguments, targetClass, interceptorsAndDynamicMethodMatchers);
/*      */ 
/*      */       
/*  736 */       this
/*      */         
/*  738 */         .methodProxy = (Modifier.isPublic(method.getModifiers()) && method.getDeclaringClass() != Object.class && !AopUtils.isEqualsMethod(method) && !AopUtils.isHashCodeMethod(method) && !AopUtils.isToStringMethod(method)) ? methodProxy : null;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected Object invokeJoinpoint() throws Throwable {
/*  748 */       if (this.methodProxy != null) {
/*  749 */         return this.methodProxy.invoke(this.target, this.arguments);
/*      */       }
/*      */       
/*  752 */       return super.invokeJoinpoint();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class ProxyCallbackFilter
/*      */     implements CallbackFilter
/*      */   {
/*      */     private final AdvisedSupport advised;
/*      */ 
/*      */     
/*      */     private final Map<String, Integer> fixedInterceptorMap;
/*      */ 
/*      */     
/*      */     private final int fixedInterceptorOffset;
/*      */ 
/*      */ 
/*      */     
/*      */     public ProxyCallbackFilter(AdvisedSupport advised, Map<String, Integer> fixedInterceptorMap, int fixedInterceptorOffset) {
/*  772 */       this.advised = advised;
/*  773 */       this.fixedInterceptorMap = fixedInterceptorMap;
/*  774 */       this.fixedInterceptorOffset = fixedInterceptorOffset;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int accept(Method method) {
/*  815 */       if (AopUtils.isFinalizeMethod(method)) {
/*  816 */         CglibAopProxy.logger.trace("Found finalize() method - using NO_OVERRIDE");
/*  817 */         return 2;
/*      */       } 
/*  819 */       if (!this.advised.isOpaque() && method.getDeclaringClass().isInterface() && method
/*  820 */         .getDeclaringClass().isAssignableFrom(Advised.class)) {
/*  821 */         if (CglibAopProxy.logger.isTraceEnabled()) {
/*  822 */           CglibAopProxy.logger.trace("Method is declared on Advised interface: " + method);
/*      */         }
/*  824 */         return 4;
/*      */       } 
/*      */       
/*  827 */       if (AopUtils.isEqualsMethod(method)) {
/*  828 */         if (CglibAopProxy.logger.isTraceEnabled()) {
/*  829 */           CglibAopProxy.logger.trace("Found 'equals' method: " + method);
/*      */         }
/*  831 */         return 5;
/*      */       } 
/*      */       
/*  834 */       if (AopUtils.isHashCodeMethod(method)) {
/*  835 */         if (CglibAopProxy.logger.isTraceEnabled()) {
/*  836 */           CglibAopProxy.logger.trace("Found 'hashCode' method: " + method);
/*      */         }
/*  838 */         return 6;
/*      */       } 
/*  840 */       Class<?> targetClass = this.advised.getTargetClass();
/*      */       
/*  842 */       List<?> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
/*  843 */       boolean haveAdvice = !chain.isEmpty();
/*  844 */       boolean exposeProxy = this.advised.isExposeProxy();
/*  845 */       boolean isStatic = this.advised.getTargetSource().isStatic();
/*  846 */       boolean isFrozen = this.advised.isFrozen();
/*  847 */       if (haveAdvice || !isFrozen) {
/*      */         
/*  849 */         if (exposeProxy) {
/*  850 */           if (CglibAopProxy.logger.isTraceEnabled()) {
/*  851 */             CglibAopProxy.logger.trace("Must expose proxy on advised method: " + method);
/*      */           }
/*  853 */           return 0;
/*      */         } 
/*  855 */         String key = method.toString();
/*      */ 
/*      */         
/*  858 */         if (isStatic && isFrozen && this.fixedInterceptorMap.containsKey(key)) {
/*  859 */           if (CglibAopProxy.logger.isTraceEnabled()) {
/*  860 */             CglibAopProxy.logger.trace("Method has advice and optimizations are enabled: " + method);
/*      */           }
/*      */           
/*  863 */           int index = ((Integer)this.fixedInterceptorMap.get(key)).intValue();
/*  864 */           return index + this.fixedInterceptorOffset;
/*      */         } 
/*      */         
/*  867 */         if (CglibAopProxy.logger.isTraceEnabled()) {
/*  868 */           CglibAopProxy.logger.trace("Unable to apply any optimizations to advised method: " + method);
/*      */         }
/*  870 */         return 0;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  879 */       if (exposeProxy || !isStatic) {
/*  880 */         return 1;
/*      */       }
/*  882 */       Class<?> returnType = method.getReturnType();
/*  883 */       if (targetClass != null && returnType.isAssignableFrom(targetClass)) {
/*  884 */         if (CglibAopProxy.logger.isTraceEnabled()) {
/*  885 */           CglibAopProxy.logger.trace("Method return type is assignable from target type and may therefore return 'this' - using INVOKE_TARGET: " + method);
/*      */         }
/*      */         
/*  888 */         return 1;
/*      */       } 
/*      */       
/*  891 */       if (CglibAopProxy.logger.isTraceEnabled()) {
/*  892 */         CglibAopProxy.logger.trace("Method return type ensures 'this' cannot be returned - using DISPATCH_TARGET: " + method);
/*      */       }
/*      */       
/*  895 */       return 3;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object other) {
/*  902 */       if (this == other) {
/*  903 */         return true;
/*      */       }
/*  905 */       if (!(other instanceof ProxyCallbackFilter)) {
/*  906 */         return false;
/*      */       }
/*  908 */       ProxyCallbackFilter otherCallbackFilter = (ProxyCallbackFilter)other;
/*  909 */       AdvisedSupport otherAdvised = otherCallbackFilter.advised;
/*  910 */       if (this.advised.isFrozen() != otherAdvised.isFrozen()) {
/*  911 */         return false;
/*      */       }
/*  913 */       if (this.advised.isExposeProxy() != otherAdvised.isExposeProxy()) {
/*  914 */         return false;
/*      */       }
/*  916 */       if (this.advised.getTargetSource().isStatic() != otherAdvised.getTargetSource().isStatic()) {
/*  917 */         return false;
/*      */       }
/*  919 */       if (!AopProxyUtils.equalsProxiedInterfaces(this.advised, otherAdvised)) {
/*  920 */         return false;
/*      */       }
/*      */ 
/*      */       
/*  924 */       Advisor[] thisAdvisors = this.advised.getAdvisors();
/*  925 */       Advisor[] thatAdvisors = otherAdvised.getAdvisors();
/*  926 */       if (thisAdvisors.length != thatAdvisors.length) {
/*  927 */         return false;
/*      */       }
/*  929 */       for (int i = 0; i < thisAdvisors.length; i++) {
/*  930 */         Advisor thisAdvisor = thisAdvisors[i];
/*  931 */         Advisor thatAdvisor = thatAdvisors[i];
/*  932 */         if (!equalsAdviceClasses(thisAdvisor, thatAdvisor)) {
/*  933 */           return false;
/*      */         }
/*  935 */         if (!equalsPointcuts(thisAdvisor, thatAdvisor)) {
/*  936 */           return false;
/*      */         }
/*      */       } 
/*  939 */       return true;
/*      */     }
/*      */     
/*      */     private boolean equalsAdviceClasses(Advisor a, Advisor b) {
/*  943 */       return (a.getAdvice().getClass() == b.getAdvice().getClass());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean equalsPointcuts(Advisor a, Advisor b) {
/*  949 */       return (!(a instanceof PointcutAdvisor) || (b instanceof PointcutAdvisor && 
/*      */         
/*  951 */         ObjectUtils.nullSafeEquals(((PointcutAdvisor)a).getPointcut(), ((PointcutAdvisor)b).getPointcut())));
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  956 */       int hashCode = 0;
/*  957 */       Advisor[] advisors = this.advised.getAdvisors();
/*  958 */       for (Advisor advisor : advisors) {
/*  959 */         Advice advice = advisor.getAdvice();
/*  960 */         hashCode = 13 * hashCode + advice.getClass().hashCode();
/*      */       } 
/*  962 */       hashCode = 13 * hashCode + (this.advised.isFrozen() ? 1 : 0);
/*  963 */       hashCode = 13 * hashCode + (this.advised.isExposeProxy() ? 1 : 0);
/*  964 */       hashCode = 13 * hashCode + (this.advised.isOptimize() ? 1 : 0);
/*  965 */       hashCode = 13 * hashCode + (this.advised.isOpaque() ? 1 : 0);
/*  966 */       return hashCode;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class ClassLoaderAwareUndeclaredThrowableStrategy
/*      */     extends UndeclaredThrowableStrategy
/*      */   {
/*      */     @Nullable
/*      */     private final ClassLoader classLoader;
/*      */ 
/*      */ 
/*      */     
/*      */     public ClassLoaderAwareUndeclaredThrowableStrategy(@Nullable ClassLoader classLoader) {
/*  982 */       super(UndeclaredThrowableException.class);
/*  983 */       this.classLoader = classLoader;
/*      */     }
/*      */     
/*      */     public byte[] generate(ClassGenerator cg) throws Exception {
/*      */       ClassLoader threadContextClassLoader;
/*  988 */       if (this.classLoader == null) {
/*  989 */         return super.generate(cg);
/*      */       }
/*      */       
/*  992 */       Thread currentThread = Thread.currentThread();
/*      */       
/*      */       try {
/*  995 */         threadContextClassLoader = currentThread.getContextClassLoader();
/*      */       }
/*  997 */       catch (Throwable ex) {
/*      */         
/*  999 */         return super.generate(cg);
/*      */       } 
/*      */       
/* 1002 */       boolean overrideClassLoader = !this.classLoader.equals(threadContextClassLoader);
/* 1003 */       if (overrideClassLoader) {
/* 1004 */         currentThread.setContextClassLoader(this.classLoader);
/*      */       }
/*      */       try {
/* 1007 */         return super.generate(cg);
/*      */       } finally {
/*      */         
/* 1010 */         if (overrideClassLoader)
/*      */         {
/* 1012 */           currentThread.setContextClassLoader(threadContextClassLoader);
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/CglibAopProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */