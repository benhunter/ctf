/*     */ package org.springframework.aop.framework.autoproxy;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.aop.Pointcut;
/*     */ import org.springframework.aop.TargetSource;
/*     */ import org.springframework.aop.framework.AopInfrastructureBean;
/*     */ import org.springframework.aop.framework.ProxyConfig;
/*     */ import org.springframework.aop.framework.ProxyFactory;
/*     */ import org.springframework.aop.framework.ProxyProcessorSupport;
/*     */ import org.springframework.aop.framework.adapter.AdvisorAdapterRegistry;
/*     */ import org.springframework.aop.framework.adapter.GlobalAdvisorAdapterRegistry;
/*     */ import org.springframework.aop.target.SingletonTargetSource;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.PropertyValues;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public abstract class AbstractAutoProxyCreator
/*     */   extends ProxyProcessorSupport
/*     */   implements SmartInstantiationAwareBeanPostProcessor, BeanFactoryAware
/*     */ {
/*     */   @Nullable
/* 101 */   protected static final Object[] DO_NOT_PROXY = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   protected static final Object[] PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS = new Object[0];
/*     */ 
/*     */ 
/*     */   
/* 112 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */   
/* 115 */   private AdvisorAdapterRegistry advisorAdapterRegistry = GlobalAdvisorAdapterRegistry.getInstance();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean freezeProxy = false;
/*     */ 
/*     */ 
/*     */   
/* 124 */   private String[] interceptorNames = new String[0];
/*     */   
/*     */   private boolean applyCommonInterceptorsFirst = true;
/*     */   
/*     */   @Nullable
/*     */   private TargetSourceCreator[] customTargetSourceCreators;
/*     */   
/*     */   @Nullable
/*     */   private BeanFactory beanFactory;
/*     */   
/* 134 */   private final Set<String> targetSourcedBeans = Collections.newSetFromMap(new ConcurrentHashMap<>(16));
/*     */   
/* 136 */   private final Map<Object, Object> earlyProxyReferences = new ConcurrentHashMap<>(16);
/*     */   
/* 138 */   private final Map<Object, Class<?>> proxyTypes = new ConcurrentHashMap<>(16);
/*     */   
/* 140 */   private final Map<Object, Boolean> advisedBeans = new ConcurrentHashMap<>(256);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFrozen(boolean frozen) {
/* 151 */     this.freezeProxy = frozen;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFrozen() {
/* 156 */     return this.freezeProxy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAdvisorAdapterRegistry(AdvisorAdapterRegistry advisorAdapterRegistry) {
/* 165 */     this.advisorAdapterRegistry = advisorAdapterRegistry;
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
/*     */   public void setCustomTargetSourceCreators(TargetSourceCreator... targetSourceCreators) {
/* 183 */     this.customTargetSourceCreators = targetSourceCreators;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInterceptorNames(String... interceptorNames) {
/* 194 */     this.interceptorNames = interceptorNames;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setApplyCommonInterceptorsFirst(boolean applyCommonInterceptorsFirst) {
/* 202 */     this.applyCommonInterceptorsFirst = applyCommonInterceptorsFirst;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 207 */     this.beanFactory = beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected BeanFactory getBeanFactory() {
/* 216 */     return this.beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> predictBeanType(Class<?> beanClass, String beanName) {
/* 223 */     if (this.proxyTypes.isEmpty()) {
/* 224 */       return null;
/*     */     }
/* 226 */     Object cacheKey = getCacheKey(beanClass, beanName);
/* 227 */     return this.proxyTypes.get(cacheKey);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName) {
/* 233 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getEarlyBeanReference(Object bean, String beanName) {
/* 238 */     Object cacheKey = getCacheKey(bean.getClass(), beanName);
/* 239 */     this.earlyProxyReferences.put(cacheKey, bean);
/* 240 */     return wrapIfNecessary(bean, beanName, cacheKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {
/* 245 */     Object cacheKey = getCacheKey(beanClass, beanName);
/*     */     
/* 247 */     if (!StringUtils.hasLength(beanName) || !this.targetSourcedBeans.contains(beanName)) {
/* 248 */       if (this.advisedBeans.containsKey(cacheKey)) {
/* 249 */         return null;
/*     */       }
/* 251 */       if (isInfrastructureClass(beanClass) || shouldSkip(beanClass, beanName)) {
/* 252 */         this.advisedBeans.put(cacheKey, Boolean.FALSE);
/* 253 */         return null;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 260 */     TargetSource targetSource = getCustomTargetSource(beanClass, beanName);
/* 261 */     if (targetSource != null) {
/* 262 */       if (StringUtils.hasLength(beanName)) {
/* 263 */         this.targetSourcedBeans.add(beanName);
/*     */       }
/* 265 */       Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(beanClass, beanName, targetSource);
/* 266 */       Object proxy = createProxy(beanClass, beanName, specificInterceptors, targetSource);
/* 267 */       this.proxyTypes.put(cacheKey, proxy.getClass());
/* 268 */       return proxy;
/*     */     } 
/*     */     
/* 271 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean postProcessAfterInstantiation(Object bean, String beanName) {
/* 276 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) {
/* 281 */     return pvs;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object postProcessBeforeInitialization(Object bean, String beanName) {
/* 286 */     return bean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object postProcessAfterInitialization(@Nullable Object bean, String beanName) {
/* 296 */     if (bean != null) {
/* 297 */       Object cacheKey = getCacheKey(bean.getClass(), beanName);
/* 298 */       if (this.earlyProxyReferences.remove(cacheKey) != bean) {
/* 299 */         return wrapIfNecessary(bean, beanName, cacheKey);
/*     */       }
/*     */     } 
/* 302 */     return bean;
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
/*     */   protected Object getCacheKey(Class<?> beanClass, @Nullable String beanName) {
/* 318 */     if (StringUtils.hasLength(beanName)) {
/* 319 */       return FactoryBean.class.isAssignableFrom(beanClass) ? ("&" + beanName) : beanName;
/*     */     }
/*     */ 
/*     */     
/* 323 */     return beanClass;
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
/*     */   protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {
/* 335 */     if (StringUtils.hasLength(beanName) && this.targetSourcedBeans.contains(beanName)) {
/* 336 */       return bean;
/*     */     }
/* 338 */     if (Boolean.FALSE.equals(this.advisedBeans.get(cacheKey))) {
/* 339 */       return bean;
/*     */     }
/* 341 */     if (isInfrastructureClass(bean.getClass()) || shouldSkip(bean.getClass(), beanName)) {
/* 342 */       this.advisedBeans.put(cacheKey, Boolean.FALSE);
/* 343 */       return bean;
/*     */     } 
/*     */ 
/*     */     
/* 347 */     Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(bean.getClass(), beanName, (TargetSource)null);
/* 348 */     if (specificInterceptors != DO_NOT_PROXY) {
/* 349 */       this.advisedBeans.put(cacheKey, Boolean.TRUE);
/* 350 */       Object proxy = createProxy(bean
/* 351 */           .getClass(), beanName, specificInterceptors, (TargetSource)new SingletonTargetSource(bean));
/* 352 */       this.proxyTypes.put(cacheKey, proxy.getClass());
/* 353 */       return proxy;
/*     */     } 
/*     */     
/* 356 */     this.advisedBeans.put(cacheKey, Boolean.FALSE);
/* 357 */     return bean;
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
/*     */   protected boolean isInfrastructureClass(Class<?> beanClass) {
/* 376 */     boolean retVal = (Advice.class.isAssignableFrom(beanClass) || Pointcut.class.isAssignableFrom(beanClass) || Advisor.class.isAssignableFrom(beanClass) || AopInfrastructureBean.class.isAssignableFrom(beanClass));
/* 377 */     if (retVal && this.logger.isTraceEnabled()) {
/* 378 */       this.logger.trace("Did not attempt to auto-proxy infrastructure class [" + beanClass.getName() + "]");
/*     */     }
/* 380 */     return retVal;
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
/*     */   protected boolean shouldSkip(Class<?> beanClass, String beanName) {
/* 396 */     return AutoProxyUtils.isOriginalInstance(beanName, beanClass);
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
/*     */   @Nullable
/*     */   protected TargetSource getCustomTargetSource(Class<?> beanClass, String beanName) {
/* 412 */     if (this.customTargetSourceCreators != null && this.beanFactory != null && this.beanFactory
/* 413 */       .containsBean(beanName)) {
/* 414 */       for (TargetSourceCreator tsc : this.customTargetSourceCreators) {
/* 415 */         TargetSource ts = tsc.getTargetSource(beanClass, beanName);
/* 416 */         if (ts != null) {
/*     */           
/* 418 */           if (this.logger.isTraceEnabled()) {
/* 419 */             this.logger.trace("TargetSourceCreator [" + tsc + "] found custom TargetSource for bean with name '" + beanName + "'");
/*     */           }
/*     */           
/* 422 */           return ts;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 428 */     return null;
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
/*     */   protected Object createProxy(Class<?> beanClass, @Nullable String beanName, @Nullable Object[] specificInterceptors, TargetSource targetSource) {
/* 445 */     if (this.beanFactory instanceof ConfigurableListableBeanFactory) {
/* 446 */       AutoProxyUtils.exposeTargetClass((ConfigurableListableBeanFactory)this.beanFactory, beanName, beanClass);
/*     */     }
/*     */     
/* 449 */     ProxyFactory proxyFactory = new ProxyFactory();
/* 450 */     proxyFactory.copyFrom((ProxyConfig)this);
/*     */     
/* 452 */     if (!proxyFactory.isProxyTargetClass()) {
/* 453 */       if (shouldProxyTargetClass(beanClass, beanName)) {
/* 454 */         proxyFactory.setProxyTargetClass(true);
/*     */       } else {
/*     */         
/* 457 */         evaluateProxyInterfaces(beanClass, proxyFactory);
/*     */       } 
/*     */     }
/*     */     
/* 461 */     Advisor[] advisors = buildAdvisors(beanName, specificInterceptors);
/* 462 */     proxyFactory.addAdvisors(advisors);
/* 463 */     proxyFactory.setTargetSource(targetSource);
/* 464 */     customizeProxyFactory(proxyFactory);
/*     */     
/* 466 */     proxyFactory.setFrozen(this.freezeProxy);
/* 467 */     if (advisorsPreFiltered()) {
/* 468 */       proxyFactory.setPreFiltered(true);
/*     */     }
/*     */     
/* 471 */     return proxyFactory.getProxy(getProxyClassLoader());
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
/*     */   protected boolean shouldProxyTargetClass(Class<?> beanClass, @Nullable String beanName) {
/* 484 */     return (this.beanFactory instanceof ConfigurableListableBeanFactory && 
/* 485 */       AutoProxyUtils.shouldProxyTargetClass((ConfigurableListableBeanFactory)this.beanFactory, beanName));
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
/*     */   protected boolean advisorsPreFiltered() {
/* 499 */     return false;
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
/*     */   protected Advisor[] buildAdvisors(@Nullable String beanName, @Nullable Object[] specificInterceptors) {
/* 512 */     Advisor[] commonInterceptors = resolveInterceptorNames();
/*     */     
/* 514 */     List<Object> allInterceptors = new ArrayList();
/* 515 */     if (specificInterceptors != null) {
/* 516 */       allInterceptors.addAll(Arrays.asList(specificInterceptors));
/* 517 */       if (commonInterceptors.length > 0) {
/* 518 */         if (this.applyCommonInterceptorsFirst) {
/* 519 */           allInterceptors.addAll(0, Arrays.asList((Object[])commonInterceptors));
/*     */         } else {
/*     */           
/* 522 */           allInterceptors.addAll(Arrays.asList((Object[])commonInterceptors));
/*     */         } 
/*     */       }
/*     */     } 
/* 526 */     if (this.logger.isTraceEnabled()) {
/* 527 */       int nrOfCommonInterceptors = commonInterceptors.length;
/* 528 */       int nrOfSpecificInterceptors = (specificInterceptors != null) ? specificInterceptors.length : 0;
/* 529 */       this.logger.trace("Creating implicit proxy for bean '" + beanName + "' with " + nrOfCommonInterceptors + " common interceptors and " + nrOfSpecificInterceptors + " specific interceptors");
/*     */     } 
/*     */ 
/*     */     
/* 533 */     Advisor[] advisors = new Advisor[allInterceptors.size()];
/* 534 */     for (int i = 0; i < allInterceptors.size(); i++) {
/* 535 */       advisors[i] = this.advisorAdapterRegistry.wrap(allInterceptors.get(i));
/*     */     }
/* 537 */     return advisors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Advisor[] resolveInterceptorNames() {
/* 545 */     BeanFactory bf = this.beanFactory;
/* 546 */     ConfigurableBeanFactory cbf = (bf instanceof ConfigurableBeanFactory) ? (ConfigurableBeanFactory)bf : null;
/* 547 */     List<Advisor> advisors = new ArrayList<>();
/* 548 */     for (String beanName : this.interceptorNames) {
/* 549 */       if (cbf == null || !cbf.isCurrentlyInCreation(beanName)) {
/* 550 */         Assert.state((bf != null), "BeanFactory required for resolving interceptor names");
/* 551 */         Object next = bf.getBean(beanName);
/* 552 */         advisors.add(this.advisorAdapterRegistry.wrap(next));
/*     */       } 
/*     */     } 
/* 555 */     return advisors.<Advisor>toArray(new Advisor[0]);
/*     */   }
/*     */   
/*     */   protected void customizeProxyFactory(ProxyFactory proxyFactory) {}
/*     */   
/*     */   @Nullable
/*     */   protected abstract Object[] getAdvicesAndAdvisorsForBean(Class<?> paramClass, String paramString, @Nullable TargetSource paramTargetSource) throws BeansException;
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/autoproxy/AbstractAutoProxyCreator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */