/*     */ package org.springframework.aop.framework;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.aopalliance.intercept.Interceptor;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.aop.TargetSource;
/*     */ import org.springframework.aop.framework.adapter.AdvisorAdapterRegistry;
/*     */ import org.springframework.aop.framework.adapter.GlobalAdvisorAdapterRegistry;
/*     */ import org.springframework.aop.framework.adapter.UnknownAdviceTypeException;
/*     */ import org.springframework.aop.target.SingletonTargetSource;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.FactoryBeanNotInitializedException;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProxyFactoryBean
/*     */   extends ProxyCreatorSupport
/*     */   implements FactoryBean<Object>, BeanClassLoaderAware, BeanFactoryAware
/*     */ {
/*     */   public static final String GLOBAL_SUFFIX = "*";
/* 103 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   @Nullable
/*     */   private String[] interceptorNames;
/*     */   
/*     */   @Nullable
/*     */   private String targetName;
/*     */   
/*     */   private boolean autodetectInterfaces = true;
/*     */   
/*     */   private boolean singleton = true;
/*     */   
/* 115 */   private AdvisorAdapterRegistry advisorAdapterRegistry = GlobalAdvisorAdapterRegistry.getInstance();
/*     */   
/*     */   private boolean freezeProxy = false;
/*     */   
/*     */   @Nullable
/* 120 */   private transient ClassLoader proxyClassLoader = ClassUtils.getDefaultClassLoader();
/*     */ 
/*     */ 
/*     */   
/*     */   private transient boolean classLoaderConfigured = false;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private transient BeanFactory beanFactory;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean advisorChainInitialized = false;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Object singletonInstance;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProxyInterfaces(Class<?>[] proxyInterfaces) throws ClassNotFoundException {
/* 144 */     setInterfaces(proxyInterfaces);
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
/*     */   public void setInterceptorNames(String... interceptorNames) {
/* 165 */     this.interceptorNames = interceptorNames;
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
/*     */   public void setTargetName(String targetName) {
/* 178 */     this.targetName = targetName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutodetectInterfaces(boolean autodetectInterfaces) {
/* 188 */     this.autodetectInterfaces = autodetectInterfaces;
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
/*     */   public void setSingleton(boolean singleton) {
/* 200 */     this.singleton = singleton;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAdvisorAdapterRegistry(AdvisorAdapterRegistry advisorAdapterRegistry) {
/* 209 */     this.advisorAdapterRegistry = advisorAdapterRegistry;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFrozen(boolean frozen) {
/* 214 */     this.freezeProxy = frozen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProxyClassLoader(@Nullable ClassLoader classLoader) {
/* 224 */     this.proxyClassLoader = classLoader;
/* 225 */     this.classLoaderConfigured = (classLoader != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/* 230 */     if (!this.classLoaderConfigured) {
/* 231 */       this.proxyClassLoader = classLoader;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 237 */     this.beanFactory = beanFactory;
/* 238 */     checkInterceptorNames();
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
/*     */   public Object getObject() throws BeansException {
/* 252 */     initializeAdvisorChain();
/* 253 */     if (isSingleton()) {
/* 254 */       return getSingletonInstance();
/*     */     }
/*     */     
/* 257 */     if (this.targetName == null) {
/* 258 */       this.logger.info("Using non-singleton proxies with singleton targets is often undesirable. Enable prototype proxies by setting the 'targetName' property.");
/*     */     }
/*     */     
/* 261 */     return newPrototypeInstance();
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
/*     */   public Class<?> getObjectType() {
/* 273 */     synchronized (this) {
/* 274 */       if (this.singletonInstance != null) {
/* 275 */         return this.singletonInstance.getClass();
/*     */       }
/*     */     } 
/* 278 */     Class<?>[] ifcs = getProxiedInterfaces();
/* 279 */     if (ifcs.length == 1) {
/* 280 */       return ifcs[0];
/*     */     }
/* 282 */     if (ifcs.length > 1) {
/* 283 */       return createCompositeInterface(ifcs);
/*     */     }
/* 285 */     if (this.targetName != null && this.beanFactory != null) {
/* 286 */       return this.beanFactory.getType(this.targetName);
/*     */     }
/*     */     
/* 289 */     return getTargetClass();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 295 */     return this.singleton;
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
/*     */   protected Class<?> createCompositeInterface(Class<?>[] interfaces) {
/* 309 */     return ClassUtils.createCompositeInterface(interfaces, this.proxyClassLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized Object getSingletonInstance() {
/* 318 */     if (this.singletonInstance == null) {
/* 319 */       this.targetSource = freshTargetSource();
/* 320 */       if (this.autodetectInterfaces && (getProxiedInterfaces()).length == 0 && !isProxyTargetClass()) {
/*     */         
/* 322 */         Class<?> targetClass = getTargetClass();
/* 323 */         if (targetClass == null) {
/* 324 */           throw new FactoryBeanNotInitializedException("Cannot determine target class for proxy");
/*     */         }
/* 326 */         setInterfaces(ClassUtils.getAllInterfacesForClass(targetClass, this.proxyClassLoader));
/*     */       } 
/*     */       
/* 329 */       super.setFrozen(this.freezeProxy);
/* 330 */       this.singletonInstance = getProxy(createAopProxy());
/*     */     } 
/* 332 */     return this.singletonInstance;
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
/*     */   private synchronized Object newPrototypeInstance() {
/* 345 */     if (this.logger.isTraceEnabled()) {
/* 346 */       this.logger.trace("Creating copy of prototype ProxyFactoryBean config: " + this);
/*     */     }
/*     */     
/* 349 */     ProxyCreatorSupport copy = new ProxyCreatorSupport(getAopProxyFactory());
/*     */     
/* 351 */     TargetSource targetSource = freshTargetSource();
/* 352 */     copy.copyConfigurationFrom(this, targetSource, freshAdvisorChain());
/* 353 */     if (this.autodetectInterfaces && (getProxiedInterfaces()).length == 0 && !isProxyTargetClass()) {
/*     */       
/* 355 */       Class<?> targetClass = targetSource.getTargetClass();
/* 356 */       if (targetClass != null) {
/* 357 */         copy.setInterfaces(ClassUtils.getAllInterfacesForClass(targetClass, this.proxyClassLoader));
/*     */       }
/*     */     } 
/* 360 */     copy.setFrozen(this.freezeProxy);
/*     */     
/* 362 */     if (this.logger.isTraceEnabled()) {
/* 363 */       this.logger.trace("Using ProxyCreatorSupport copy: " + copy);
/*     */     }
/* 365 */     return getProxy(copy.createAopProxy());
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
/*     */   protected Object getProxy(AopProxy aopProxy) {
/* 378 */     return aopProxy.getProxy(this.proxyClassLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkInterceptorNames() {
/* 386 */     if (!ObjectUtils.isEmpty((Object[])this.interceptorNames)) {
/* 387 */       String finalName = this.interceptorNames[this.interceptorNames.length - 1];
/* 388 */       if (this.targetName == null && this.targetSource == EMPTY_TARGET_SOURCE)
/*     */       {
/*     */         
/* 391 */         if (!finalName.endsWith("*") && !isNamedBeanAnAdvisorOrAdvice(finalName)) {
/*     */           
/* 393 */           this.targetName = finalName;
/* 394 */           if (this.logger.isDebugEnabled()) {
/* 395 */             this.logger.debug("Bean with name '" + finalName + "' concluding interceptor chain is not an advisor class: treating it as a target or TargetSource");
/*     */           }
/*     */           
/* 398 */           String[] newNames = new String[this.interceptorNames.length - 1];
/* 399 */           System.arraycopy(this.interceptorNames, 0, newNames, 0, newNames.length);
/* 400 */           this.interceptorNames = newNames;
/*     */         } 
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
/*     */   private boolean isNamedBeanAnAdvisorOrAdvice(String beanName) {
/* 414 */     Assert.state((this.beanFactory != null), "No BeanFactory set");
/* 415 */     Class<?> namedBeanClass = this.beanFactory.getType(beanName);
/* 416 */     if (namedBeanClass != null) {
/* 417 */       return (Advisor.class.isAssignableFrom(namedBeanClass) || Advice.class.isAssignableFrom(namedBeanClass));
/*     */     }
/*     */     
/* 420 */     if (this.logger.isDebugEnabled()) {
/* 421 */       this.logger.debug("Could not determine type of bean with name '" + beanName + "' - assuming it is neither an Advisor nor an Advice");
/*     */     }
/*     */     
/* 424 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized void initializeAdvisorChain() throws AopConfigException, BeansException {
/* 434 */     if (this.advisorChainInitialized) {
/*     */       return;
/*     */     }
/*     */     
/* 438 */     if (!ObjectUtils.isEmpty((Object[])this.interceptorNames)) {
/* 439 */       if (this.beanFactory == null) {
/* 440 */         throw new IllegalStateException("No BeanFactory available anymore (probably due to serialization) - cannot resolve interceptor names " + 
/* 441 */             Arrays.asList(this.interceptorNames));
/*     */       }
/*     */ 
/*     */       
/* 445 */       if (this.interceptorNames[this.interceptorNames.length - 1].endsWith("*") && this.targetName == null && this.targetSource == EMPTY_TARGET_SOURCE)
/*     */       {
/* 447 */         throw new AopConfigException("Target required after globals");
/*     */       }
/*     */ 
/*     */       
/* 451 */       for (String name : this.interceptorNames) {
/* 452 */         if (this.logger.isTraceEnabled()) {
/* 453 */           this.logger.trace("Configuring advisor or advice '" + name + "'");
/*     */         }
/*     */         
/* 456 */         if (name.endsWith("*")) {
/* 457 */           if (!(this.beanFactory instanceof ListableBeanFactory)) {
/* 458 */             throw new AopConfigException("Can only use global advisors or interceptors with a ListableBeanFactory");
/*     */           }
/*     */           
/* 461 */           addGlobalAdvisor((ListableBeanFactory)this.beanFactory, name
/* 462 */               .substring(0, name.length() - "*".length()));
/*     */         } else {
/*     */           Object advice;
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 469 */           if (this.singleton || this.beanFactory.isSingleton(name)) {
/*     */             
/* 471 */             advice = this.beanFactory.getBean(name);
/*     */           
/*     */           }
/*     */           else {
/*     */             
/* 476 */             advice = new PrototypePlaceholderAdvisor(name);
/*     */           } 
/* 478 */           addAdvisorOnChainCreation(advice, name);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 483 */     this.advisorChainInitialized = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<Advisor> freshAdvisorChain() {
/* 493 */     Advisor[] advisors = getAdvisors();
/* 494 */     List<Advisor> freshAdvisors = new ArrayList<>(advisors.length);
/* 495 */     for (Advisor advisor : advisors) {
/* 496 */       if (advisor instanceof PrototypePlaceholderAdvisor) {
/* 497 */         PrototypePlaceholderAdvisor pa = (PrototypePlaceholderAdvisor)advisor;
/* 498 */         if (this.logger.isDebugEnabled()) {
/* 499 */           this.logger.debug("Refreshing bean named '" + pa.getBeanName() + "'");
/*     */         }
/*     */ 
/*     */         
/* 503 */         if (this.beanFactory == null) {
/* 504 */           throw new IllegalStateException("No BeanFactory available anymore (probably due to serialization) - cannot resolve prototype advisor '" + pa
/* 505 */               .getBeanName() + "'");
/*     */         }
/* 507 */         Object bean = this.beanFactory.getBean(pa.getBeanName());
/* 508 */         Advisor refreshedAdvisor = namedBeanToAdvisor(bean);
/* 509 */         freshAdvisors.add(refreshedAdvisor);
/*     */       }
/*     */       else {
/*     */         
/* 513 */         freshAdvisors.add(advisor);
/*     */       } 
/*     */     } 
/* 516 */     return freshAdvisors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addGlobalAdvisor(ListableBeanFactory beanFactory, String prefix) {
/* 524 */     String[] globalAdvisorNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, Advisor.class);
/*     */     
/* 526 */     String[] globalInterceptorNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, Interceptor.class);
/* 527 */     List<Object> beans = new ArrayList(globalAdvisorNames.length + globalInterceptorNames.length);
/* 528 */     Map<Object, String> names = new HashMap<>(beans.size());
/* 529 */     for (String name : globalAdvisorNames) {
/* 530 */       Object bean = beanFactory.getBean(name);
/* 531 */       beans.add(bean);
/* 532 */       names.put(bean, name);
/*     */     } 
/* 534 */     for (String name : globalInterceptorNames) {
/* 535 */       Object bean = beanFactory.getBean(name);
/* 536 */       beans.add(bean);
/* 537 */       names.put(bean, name);
/*     */     } 
/* 539 */     AnnotationAwareOrderComparator.sort(beans);
/* 540 */     for (Object bean : beans) {
/* 541 */       String name = names.get(bean);
/* 542 */       if (name.startsWith(prefix)) {
/* 543 */         addAdvisorOnChainCreation(bean, name);
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
/*     */   private void addAdvisorOnChainCreation(Object next, String name) {
/* 560 */     Advisor advisor = namedBeanToAdvisor(next);
/* 561 */     if (this.logger.isTraceEnabled()) {
/* 562 */       this.logger.trace("Adding advisor with name '" + name + "'");
/*     */     }
/* 564 */     addAdvisor(advisor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TargetSource freshTargetSource() {
/* 574 */     if (this.targetName == null) {
/* 575 */       if (this.logger.isTraceEnabled()) {
/* 576 */         this.logger.trace("Not refreshing target: Bean name not specified in 'interceptorNames'.");
/*     */       }
/* 578 */       return this.targetSource;
/*     */     } 
/*     */     
/* 581 */     if (this.beanFactory == null) {
/* 582 */       throw new IllegalStateException("No BeanFactory available anymore (probably due to serialization) - cannot resolve target with name '" + this.targetName + "'");
/*     */     }
/*     */     
/* 585 */     if (this.logger.isDebugEnabled()) {
/* 586 */       this.logger.debug("Refreshing target with name '" + this.targetName + "'");
/*     */     }
/* 588 */     Object target = this.beanFactory.getBean(this.targetName);
/* 589 */     return (target instanceof TargetSource) ? (TargetSource)target : (TargetSource)new SingletonTargetSource(target);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Advisor namedBeanToAdvisor(Object next) {
/*     */     try {
/* 599 */       return this.advisorAdapterRegistry.wrap(next);
/*     */     }
/* 601 */     catch (UnknownAdviceTypeException ex) {
/*     */ 
/*     */       
/* 604 */       throw new AopConfigException("Unknown advisor type " + next.getClass() + "; Can only include Advisor or Advice type beans in interceptorNames chain except for last entry,which may also be target or TargetSource", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void adviceChanged() {
/* 615 */     super.adviceChanged();
/* 616 */     if (this.singleton) {
/* 617 */       this.logger.debug("Advice has changed; recaching singleton instance");
/* 618 */       synchronized (this) {
/* 619 */         this.singletonInstance = null;
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
/*     */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 631 */     ois.defaultReadObject();
/*     */ 
/*     */     
/* 634 */     this.proxyClassLoader = ClassUtils.getDefaultClassLoader();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class PrototypePlaceholderAdvisor
/*     */     implements Advisor, Serializable
/*     */   {
/*     */     private final String beanName;
/*     */ 
/*     */     
/*     */     private final String message;
/*     */ 
/*     */     
/*     */     public PrototypePlaceholderAdvisor(String beanName) {
/* 649 */       this.beanName = beanName;
/* 650 */       this.message = "Placeholder for prototype Advisor/Advice with bean name '" + beanName + "'";
/*     */     }
/*     */     
/*     */     public String getBeanName() {
/* 654 */       return this.beanName;
/*     */     }
/*     */ 
/*     */     
/*     */     public Advice getAdvice() {
/* 659 */       throw new UnsupportedOperationException("Cannot invoke methods: " + this.message);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isPerInstance() {
/* 664 */       throw new UnsupportedOperationException("Cannot invoke methods: " + this.message);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 669 */       return this.message;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/ProxyFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */