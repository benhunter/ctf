/*     */ package org.springframework.scripting.support;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.TargetSource;
/*     */ import org.springframework.aop.framework.ProxyFactory;
/*     */ import org.springframework.aop.support.DelegatingIntroductionInterceptor;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.PropertyValue;
/*     */ import org.springframework.beans.PropertyValues;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.BeanCreationException;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionValidationException;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.beans.factory.support.GenericBeanDefinition;
/*     */ import org.springframework.cglib.core.Signature;
/*     */ import org.springframework.cglib.proxy.InterfaceMaker;
/*     */ import org.springframework.context.ResourceLoaderAware;
/*     */ import org.springframework.core.Conventions;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.core.io.DefaultResourceLoader;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.scripting.ScriptFactory;
/*     */ import org.springframework.scripting.ScriptSource;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ScriptFactoryPostProcessor
/*     */   extends InstantiationAwareBeanPostProcessorAdapter
/*     */   implements BeanClassLoaderAware, BeanFactoryAware, ResourceLoaderAware, DisposableBean, Ordered
/*     */ {
/*     */   public static final String INLINE_SCRIPT_PREFIX = "inline:";
/* 153 */   public static final String REFRESH_CHECK_DELAY_ATTRIBUTE = Conventions.getQualifiedAttributeName(ScriptFactoryPostProcessor.class, "refreshCheckDelay");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 159 */   public static final String PROXY_TARGET_CLASS_ATTRIBUTE = Conventions.getQualifiedAttributeName(ScriptFactoryPostProcessor.class, "proxyTargetClass");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 165 */   public static final String LANGUAGE_ATTRIBUTE = Conventions.getQualifiedAttributeName(ScriptFactoryPostProcessor.class, "language");
/*     */ 
/*     */   
/*     */   private static final String SCRIPT_FACTORY_NAME_PREFIX = "scriptFactory.";
/*     */ 
/*     */   
/*     */   private static final String SCRIPTED_OBJECT_NAME_PREFIX = "scriptedObject.";
/*     */ 
/*     */   
/* 174 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/* 176 */   private long defaultRefreshCheckDelay = -1L;
/*     */   
/*     */   private boolean defaultProxyTargetClass = false;
/*     */   
/*     */   @Nullable
/* 181 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */   
/*     */   @Nullable
/*     */   private ConfigurableBeanFactory beanFactory;
/*     */   
/* 186 */   private ResourceLoader resourceLoader = (ResourceLoader)new DefaultResourceLoader();
/*     */   
/* 188 */   final DefaultListableBeanFactory scriptBeanFactory = new DefaultListableBeanFactory();
/*     */ 
/*     */   
/* 191 */   private final Map<String, ScriptSource> scriptSourceCache = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultRefreshCheckDelay(long defaultRefreshCheckDelay) {
/* 203 */     this.defaultRefreshCheckDelay = defaultRefreshCheckDelay;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultProxyTargetClass(boolean defaultProxyTargetClass) {
/* 211 */     this.defaultProxyTargetClass = defaultProxyTargetClass;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/* 216 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 221 */     if (!(beanFactory instanceof ConfigurableBeanFactory)) {
/* 222 */       throw new IllegalStateException("ScriptFactoryPostProcessor doesn't work with non-ConfigurableBeanFactory: " + beanFactory
/* 223 */           .getClass());
/*     */     }
/* 225 */     this.beanFactory = (ConfigurableBeanFactory)beanFactory;
/*     */ 
/*     */     
/* 228 */     this.scriptBeanFactory.setParentBeanFactory((BeanFactory)this.beanFactory);
/*     */ 
/*     */     
/* 231 */     this.scriptBeanFactory.copyConfigurationFrom(this.beanFactory);
/*     */ 
/*     */ 
/*     */     
/* 235 */     this.scriptBeanFactory.getBeanPostProcessors().removeIf(beanPostProcessor -> beanPostProcessor instanceof org.springframework.aop.framework.AopInfrastructureBean);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResourceLoader(ResourceLoader resourceLoader) {
/* 241 */     this.resourceLoader = resourceLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/* 246 */     return Integer.MIN_VALUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> predictBeanType(Class<?> beanClass, String beanName) {
/* 254 */     if (!ScriptFactory.class.isAssignableFrom(beanClass)) {
/* 255 */       return null;
/*     */     }
/*     */     
/* 258 */     Assert.state((this.beanFactory != null), "No BeanFactory set");
/* 259 */     BeanDefinition bd = this.beanFactory.getMergedBeanDefinition(beanName);
/*     */     
/*     */     try {
/* 262 */       String scriptFactoryBeanName = "scriptFactory." + beanName;
/* 263 */       String scriptedObjectBeanName = "scriptedObject." + beanName;
/* 264 */       prepareScriptBeans(bd, scriptFactoryBeanName, scriptedObjectBeanName);
/*     */       
/* 266 */       ScriptFactory scriptFactory = (ScriptFactory)this.scriptBeanFactory.getBean(scriptFactoryBeanName, ScriptFactory.class);
/* 267 */       ScriptSource scriptSource = getScriptSource(scriptFactoryBeanName, scriptFactory.getScriptSourceLocator());
/* 268 */       Class<?>[] interfaces = scriptFactory.getScriptInterfaces();
/*     */       
/* 270 */       Class<?> scriptedType = scriptFactory.getScriptedObjectType(scriptSource);
/* 271 */       if (scriptedType != null) {
/* 272 */         return scriptedType;
/*     */       }
/* 274 */       if (!ObjectUtils.isEmpty((Object[])interfaces)) {
/* 275 */         return (interfaces.length == 1) ? interfaces[0] : createCompositeInterface(interfaces);
/*     */       }
/*     */       
/* 278 */       if (bd.isSingleton()) {
/* 279 */         return this.scriptBeanFactory.getBean(scriptedObjectBeanName).getClass();
/*     */       
/*     */       }
/*     */     }
/* 283 */     catch (Exception ex) {
/* 284 */       if (ex instanceof BeanCreationException && ((BeanCreationException)ex)
/* 285 */         .getMostSpecificCause() instanceof org.springframework.beans.factory.BeanCurrentlyInCreationException) {
/* 286 */         if (this.logger.isTraceEnabled()) {
/* 287 */           this.logger.trace("Could not determine scripted object type for bean '" + beanName + "': " + ex
/* 288 */               .getMessage());
/*     */         
/*     */         }
/*     */       }
/* 292 */       else if (this.logger.isDebugEnabled()) {
/* 293 */         this.logger.debug("Could not determine scripted object type for bean '" + beanName + "'", ex);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 298 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) {
/* 303 */     return pvs;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {
/* 309 */     if (!ScriptFactory.class.isAssignableFrom(beanClass)) {
/* 310 */       return null;
/*     */     }
/*     */     
/* 313 */     Assert.state((this.beanFactory != null), "No BeanFactory set");
/* 314 */     BeanDefinition bd = this.beanFactory.getMergedBeanDefinition(beanName);
/* 315 */     String scriptFactoryBeanName = "scriptFactory." + beanName;
/* 316 */     String scriptedObjectBeanName = "scriptedObject." + beanName;
/* 317 */     prepareScriptBeans(bd, scriptFactoryBeanName, scriptedObjectBeanName);
/*     */     
/* 319 */     ScriptFactory scriptFactory = (ScriptFactory)this.scriptBeanFactory.getBean(scriptFactoryBeanName, ScriptFactory.class);
/* 320 */     ScriptSource scriptSource = getScriptSource(scriptFactoryBeanName, scriptFactory.getScriptSourceLocator());
/* 321 */     boolean isFactoryBean = false;
/*     */     try {
/* 323 */       Class<?> scriptedObjectType = scriptFactory.getScriptedObjectType(scriptSource);
/*     */       
/* 325 */       if (scriptedObjectType != null) {
/* 326 */         isFactoryBean = FactoryBean.class.isAssignableFrom(scriptedObjectType);
/*     */       }
/*     */     }
/* 329 */     catch (Exception ex) {
/* 330 */       throw new BeanCreationException(beanName, "Could not determine scripted object type for " + scriptFactory, ex);
/*     */     } 
/*     */ 
/*     */     
/* 334 */     long refreshCheckDelay = resolveRefreshCheckDelay(bd);
/* 335 */     if (refreshCheckDelay >= 0L) {
/* 336 */       Class<?>[] interfaces = scriptFactory.getScriptInterfaces();
/* 337 */       RefreshableScriptTargetSource ts = new RefreshableScriptTargetSource((BeanFactory)this.scriptBeanFactory, scriptedObjectBeanName, scriptFactory, scriptSource, isFactoryBean);
/*     */       
/* 339 */       boolean proxyTargetClass = resolveProxyTargetClass(bd);
/* 340 */       String language = (String)bd.getAttribute(LANGUAGE_ATTRIBUTE);
/* 341 */       if (proxyTargetClass && (language == null || !language.equals("groovy"))) {
/* 342 */         throw new BeanDefinitionValidationException("Cannot use proxyTargetClass=true with script beans where language is not 'groovy': '" + language + "'");
/*     */       }
/*     */ 
/*     */       
/* 346 */       ts.setRefreshCheckDelay(refreshCheckDelay);
/* 347 */       return createRefreshableProxy((TargetSource)ts, interfaces, proxyTargetClass);
/*     */     } 
/*     */     
/* 350 */     if (isFactoryBean) {
/* 351 */       scriptedObjectBeanName = "&" + scriptedObjectBeanName;
/*     */     }
/* 353 */     return this.scriptBeanFactory.getBean(scriptedObjectBeanName);
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
/*     */   protected void prepareScriptBeans(BeanDefinition bd, String scriptFactoryBeanName, String scriptedObjectBeanName) {
/* 366 */     synchronized (this.scriptBeanFactory) {
/* 367 */       if (!this.scriptBeanFactory.containsBeanDefinition(scriptedObjectBeanName)) {
/*     */         
/* 369 */         this.scriptBeanFactory.registerBeanDefinition(scriptFactoryBeanName, 
/* 370 */             createScriptFactoryBeanDefinition(bd));
/*     */         
/* 372 */         ScriptFactory scriptFactory = (ScriptFactory)this.scriptBeanFactory.getBean(scriptFactoryBeanName, ScriptFactory.class);
/*     */         
/* 374 */         ScriptSource scriptSource = getScriptSource(scriptFactoryBeanName, scriptFactory.getScriptSourceLocator());
/* 375 */         Class<?>[] interfaces = scriptFactory.getScriptInterfaces();
/*     */         
/* 377 */         Class<?>[] scriptedInterfaces = interfaces;
/* 378 */         if (scriptFactory.requiresConfigInterface() && !bd.getPropertyValues().isEmpty()) {
/* 379 */           Class<?> configInterface = createConfigInterface(bd, interfaces);
/* 380 */           scriptedInterfaces = (Class[])ObjectUtils.addObjectToArray((Object[])interfaces, configInterface);
/*     */         } 
/*     */         
/* 383 */         BeanDefinition objectBd = createScriptedObjectBeanDefinition(bd, scriptFactoryBeanName, scriptSource, scriptedInterfaces);
/*     */         
/* 385 */         long refreshCheckDelay = resolveRefreshCheckDelay(bd);
/* 386 */         if (refreshCheckDelay >= 0L) {
/* 387 */           objectBd.setScope("prototype");
/*     */         }
/*     */         
/* 390 */         this.scriptBeanFactory.registerBeanDefinition(scriptedObjectBeanName, objectBd);
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
/*     */   protected long resolveRefreshCheckDelay(BeanDefinition beanDefinition) {
/* 406 */     long refreshCheckDelay = this.defaultRefreshCheckDelay;
/* 407 */     Object attributeValue = beanDefinition.getAttribute(REFRESH_CHECK_DELAY_ATTRIBUTE);
/* 408 */     if (attributeValue instanceof Number) {
/* 409 */       refreshCheckDelay = ((Number)attributeValue).longValue();
/*     */     }
/* 411 */     else if (attributeValue instanceof String) {
/* 412 */       refreshCheckDelay = Long.parseLong((String)attributeValue);
/*     */     }
/* 414 */     else if (attributeValue != null) {
/* 415 */       throw new BeanDefinitionStoreException("Invalid refresh check delay attribute [" + REFRESH_CHECK_DELAY_ATTRIBUTE + "] with value '" + attributeValue + "': needs to be of type Number or String");
/*     */     } 
/*     */ 
/*     */     
/* 419 */     return refreshCheckDelay;
/*     */   }
/*     */   
/*     */   protected boolean resolveProxyTargetClass(BeanDefinition beanDefinition) {
/* 423 */     boolean proxyTargetClass = this.defaultProxyTargetClass;
/* 424 */     Object attributeValue = beanDefinition.getAttribute(PROXY_TARGET_CLASS_ATTRIBUTE);
/* 425 */     if (attributeValue instanceof Boolean) {
/* 426 */       proxyTargetClass = ((Boolean)attributeValue).booleanValue();
/*     */     }
/* 428 */     else if (attributeValue instanceof String) {
/* 429 */       proxyTargetClass = Boolean.valueOf((String)attributeValue).booleanValue();
/*     */     }
/* 431 */     else if (attributeValue != null) {
/* 432 */       throw new BeanDefinitionStoreException("Invalid proxy target class attribute [" + PROXY_TARGET_CLASS_ATTRIBUTE + "] with value '" + attributeValue + "': needs to be of type Boolean or String");
/*     */     } 
/*     */ 
/*     */     
/* 436 */     return proxyTargetClass;
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
/*     */   protected BeanDefinition createScriptFactoryBeanDefinition(BeanDefinition bd) {
/* 448 */     GenericBeanDefinition scriptBd = new GenericBeanDefinition();
/* 449 */     scriptBd.setBeanClassName(bd.getBeanClassName());
/* 450 */     scriptBd.getConstructorArgumentValues().addArgumentValues(bd.getConstructorArgumentValues());
/* 451 */     return (BeanDefinition)scriptBd;
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
/*     */   protected ScriptSource getScriptSource(String beanName, String scriptSourceLocator) {
/* 463 */     synchronized (this.scriptSourceCache) {
/* 464 */       ScriptSource scriptSource = this.scriptSourceCache.get(beanName);
/* 465 */       if (scriptSource == null) {
/* 466 */         scriptSource = convertToScriptSource(beanName, scriptSourceLocator, this.resourceLoader);
/* 467 */         this.scriptSourceCache.put(beanName, scriptSource);
/*     */       } 
/* 469 */       return scriptSource;
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
/*     */   protected ScriptSource convertToScriptSource(String beanName, String scriptSourceLocator, ResourceLoader resourceLoader) {
/* 486 */     if (scriptSourceLocator.startsWith("inline:")) {
/* 487 */       return new StaticScriptSource(scriptSourceLocator.substring("inline:".length()), beanName);
/*     */     }
/*     */     
/* 490 */     return new ResourceScriptSource(resourceLoader.getResource(scriptSourceLocator));
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
/*     */   protected Class<?> createConfigInterface(BeanDefinition bd, @Nullable Class<?>[] interfaces) {
/* 509 */     InterfaceMaker maker = new InterfaceMaker();
/* 510 */     PropertyValue[] pvs = bd.getPropertyValues().getPropertyValues();
/* 511 */     for (PropertyValue pv : pvs) {
/* 512 */       String propertyName = pv.getName();
/* 513 */       Class<?> propertyType = BeanUtils.findPropertyType(propertyName, interfaces);
/* 514 */       String setterName = "set" + StringUtils.capitalize(propertyName);
/* 515 */       Signature signature = new Signature(setterName, Type.VOID_TYPE, new Type[] { Type.getType(propertyType) });
/* 516 */       maker.add(signature, new Type[0]);
/*     */     } 
/* 518 */     if (bd.getInitMethodName() != null) {
/* 519 */       Signature signature = new Signature(bd.getInitMethodName(), Type.VOID_TYPE, new Type[0]);
/* 520 */       maker.add(signature, new Type[0]);
/*     */     } 
/* 522 */     if (StringUtils.hasText(bd.getDestroyMethodName())) {
/* 523 */       Signature signature = new Signature(bd.getDestroyMethodName(), Type.VOID_TYPE, new Type[0]);
/* 524 */       maker.add(signature, new Type[0]);
/*     */     } 
/* 526 */     return maker.create();
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
/*     */   protected Class<?> createCompositeInterface(Class<?>[] interfaces) {
/* 539 */     return ClassUtils.createCompositeInterface(interfaces, this.beanClassLoader);
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
/*     */   protected BeanDefinition createScriptedObjectBeanDefinition(BeanDefinition bd, String scriptFactoryBeanName, ScriptSource scriptSource, @Nullable Class<?>[] interfaces) {
/* 556 */     GenericBeanDefinition objectBd = new GenericBeanDefinition(bd);
/* 557 */     objectBd.setFactoryBeanName(scriptFactoryBeanName);
/* 558 */     objectBd.setFactoryMethodName("getScriptedObject");
/* 559 */     objectBd.getConstructorArgumentValues().clear();
/* 560 */     objectBd.getConstructorArgumentValues().addIndexedArgumentValue(0, scriptSource);
/* 561 */     objectBd.getConstructorArgumentValues().addIndexedArgumentValue(1, interfaces);
/* 562 */     return (BeanDefinition)objectBd;
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
/*     */   protected Object createRefreshableProxy(TargetSource ts, @Nullable Class<?>[] interfaces, boolean proxyTargetClass) {
/* 574 */     ProxyFactory proxyFactory = new ProxyFactory();
/* 575 */     proxyFactory.setTargetSource(ts);
/* 576 */     ClassLoader classLoader = this.beanClassLoader;
/*     */     
/* 578 */     if (interfaces != null) {
/* 579 */       proxyFactory.setInterfaces(interfaces);
/*     */     } else {
/*     */       
/* 582 */       Class<?> targetClass = ts.getTargetClass();
/* 583 */       if (targetClass != null) {
/* 584 */         proxyFactory.setInterfaces(ClassUtils.getAllInterfacesForClass(targetClass, this.beanClassLoader));
/*     */       }
/*     */     } 
/*     */     
/* 588 */     if (proxyTargetClass) {
/* 589 */       classLoader = null;
/* 590 */       proxyFactory.setProxyTargetClass(true);
/*     */     } 
/*     */     
/* 593 */     DelegatingIntroductionInterceptor introduction = new DelegatingIntroductionInterceptor(ts);
/* 594 */     introduction.suppressInterface(TargetSource.class);
/* 595 */     proxyFactory.addAdvice((Advice)introduction);
/*     */     
/* 597 */     return proxyFactory.getProxy(classLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 605 */     this.scriptBeanFactory.destroySingletons();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scripting/support/ScriptFactoryPostProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */