/*     */ package org.springframework.beans.factory.annotation;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.PropertyValues;
/*     */ import org.springframework.beans.TypeConverter;
/*     */ import org.springframework.beans.factory.BeanCreationException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.InjectionPoint;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.UnsatisfiedDependencyException;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.config.DependencyDescriptor;
/*     */ import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
/*     */ import org.springframework.beans.factory.support.LookupOverride;
/*     */ import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
/*     */ import org.springframework.beans.factory.support.MethodOverride;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.core.BridgeMethodResolver;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.PriorityOrdered;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ public class AutowiredAnnotationBeanPostProcessor
/*     */   extends InstantiationAwareBeanPostProcessorAdapter
/*     */   implements MergedBeanDefinitionPostProcessor, PriorityOrdered, BeanFactoryAware
/*     */ {
/* 121 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/* 123 */   private final Set<Class<? extends Annotation>> autowiredAnnotationTypes = new LinkedHashSet<>(4);
/*     */   
/* 125 */   private String requiredParameterName = "required";
/*     */   
/*     */   private boolean requiredParameterValue = true;
/*     */   
/* 129 */   private int order = 2147483645;
/*     */   
/*     */   @Nullable
/*     */   private ConfigurableListableBeanFactory beanFactory;
/*     */   
/* 134 */   private final Set<String> lookupMethodsChecked = Collections.newSetFromMap(new ConcurrentHashMap<>(256));
/*     */   
/* 136 */   private final Map<Class<?>, Constructor<?>[]> candidateConstructorsCache = (Map)new ConcurrentHashMap<>(256);
/*     */   
/* 138 */   private final Map<String, InjectionMetadata> injectionMetadataCache = new ConcurrentHashMap<>(256);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AutowiredAnnotationBeanPostProcessor() {
/* 148 */     this.autowiredAnnotationTypes.add(Autowired.class);
/* 149 */     this.autowiredAnnotationTypes.add(Value.class);
/*     */     try {
/* 151 */       this.autowiredAnnotationTypes.add(
/* 152 */           ClassUtils.forName("javax.inject.Inject", AutowiredAnnotationBeanPostProcessor.class.getClassLoader()));
/* 153 */       this.logger.trace("JSR-330 'javax.inject.Inject' annotation found and supported for autowiring");
/*     */     }
/* 155 */     catch (ClassNotFoundException classNotFoundException) {}
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
/*     */   public void setAutowiredAnnotationType(Class<? extends Annotation> autowiredAnnotationType) {
/* 171 */     Assert.notNull(autowiredAnnotationType, "'autowiredAnnotationType' must not be null");
/* 172 */     this.autowiredAnnotationTypes.clear();
/* 173 */     this.autowiredAnnotationTypes.add(autowiredAnnotationType);
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
/*     */   public void setAutowiredAnnotationTypes(Set<Class<? extends Annotation>> autowiredAnnotationTypes) {
/* 186 */     Assert.notEmpty(autowiredAnnotationTypes, "'autowiredAnnotationTypes' must not be empty");
/* 187 */     this.autowiredAnnotationTypes.clear();
/* 188 */     this.autowiredAnnotationTypes.addAll(autowiredAnnotationTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRequiredParameterName(String requiredParameterName) {
/* 196 */     this.requiredParameterName = requiredParameterName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRequiredParameterValue(boolean requiredParameterValue) {
/* 206 */     this.requiredParameterValue = requiredParameterValue;
/*     */   }
/*     */   
/*     */   public void setOrder(int order) {
/* 210 */     this.order = order;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/* 215 */     return this.order;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 220 */     if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
/* 221 */       throw new IllegalArgumentException("AutowiredAnnotationBeanPostProcessor requires a ConfigurableListableBeanFactory: " + beanFactory);
/*     */     }
/*     */     
/* 224 */     this.beanFactory = (ConfigurableListableBeanFactory)beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
/* 230 */     InjectionMetadata metadata = findAutowiringMetadata(beanName, beanType, null);
/* 231 */     metadata.checkConfigMembers(beanDefinition);
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetBeanDefinition(String beanName) {
/* 236 */     this.lookupMethodsChecked.remove(beanName);
/* 237 */     this.injectionMetadataCache.remove(beanName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName) throws BeanCreationException {
/* 246 */     if (!this.lookupMethodsChecked.contains(beanName)) {
/*     */       try {
/* 248 */         ReflectionUtils.doWithMethods(beanClass, method -> {
/*     */               Lookup lookup = method.<Lookup>getAnnotation(Lookup.class);
/*     */               
/*     */               if (lookup != null) {
/*     */                 Assert.state((this.beanFactory != null), "No BeanFactory available");
/*     */                 LookupOverride override = new LookupOverride(method, lookup.value());
/*     */                 try {
/*     */                   RootBeanDefinition mbd = (RootBeanDefinition)this.beanFactory.getMergedBeanDefinition(beanName);
/*     */                   mbd.getMethodOverrides().addOverride((MethodOverride)override);
/* 257 */                 } catch (NoSuchBeanDefinitionException ex) {
/*     */                   
/*     */                   throw new BeanCreationException(beanName, "Cannot apply @Lookup to beans without corresponding bean definition");
/*     */                 }
/*     */               
/*     */               } 
/*     */             });
/* 264 */       } catch (IllegalStateException ex) {
/* 265 */         throw new BeanCreationException(beanName, "Lookup method resolution failed", ex);
/*     */       } 
/* 267 */       this.lookupMethodsChecked.add(beanName);
/*     */     } 
/*     */ 
/*     */     
/* 271 */     Constructor[] arrayOfConstructor = (Constructor[])this.candidateConstructorsCache.get(beanClass);
/* 272 */     if (arrayOfConstructor == null)
/*     */     {
/* 274 */       synchronized (this.candidateConstructorsCache) {
/* 275 */         arrayOfConstructor = (Constructor[])this.candidateConstructorsCache.get(beanClass);
/* 276 */         if (arrayOfConstructor == null) {
/*     */           Constructor[] arrayOfConstructor1;
/*     */           try {
/* 279 */             arrayOfConstructor1 = (Constructor[])beanClass.getDeclaredConstructors();
/*     */           }
/* 281 */           catch (Throwable ex) {
/* 282 */             throw new BeanCreationException(beanName, "Resolution of declared constructors on bean Class [" + beanClass
/* 283 */                 .getName() + "] from ClassLoader [" + beanClass
/* 284 */                 .getClassLoader() + "] failed", ex);
/*     */           } 
/* 286 */           List<Constructor<?>> candidates = new ArrayList<>(arrayOfConstructor1.length);
/* 287 */           Constructor<?> requiredConstructor = null;
/* 288 */           Constructor<?> defaultConstructor = null;
/* 289 */           Constructor<?> primaryConstructor = BeanUtils.findPrimaryConstructor(beanClass);
/* 290 */           int nonSyntheticConstructors = 0;
/* 291 */           for (Constructor<?> candidate : arrayOfConstructor1) {
/* 292 */             if (!candidate.isSynthetic()) {
/* 293 */               nonSyntheticConstructors++;
/*     */             }
/* 295 */             else if (primaryConstructor != null) {
/*     */               continue;
/*     */             } 
/* 298 */             AnnotationAttributes ann = findAutowiredAnnotation(candidate);
/* 299 */             if (ann == null) {
/* 300 */               Class<?> userClass = ClassUtils.getUserClass(beanClass);
/* 301 */               if (userClass != beanClass) {
/*     */                 
/*     */                 try {
/* 304 */                   Constructor<?> superCtor = userClass.getDeclaredConstructor(candidate.getParameterTypes());
/* 305 */                   ann = findAutowiredAnnotation(superCtor);
/*     */                 }
/* 307 */                 catch (NoSuchMethodException noSuchMethodException) {}
/*     */               }
/*     */             } 
/*     */ 
/*     */             
/* 312 */             if (ann != null) {
/* 313 */               if (requiredConstructor != null) {
/* 314 */                 throw new BeanCreationException(beanName, "Invalid autowire-marked constructor: " + candidate + ". Found constructor with 'required' Autowired annotation already: " + requiredConstructor);
/*     */               }
/*     */ 
/*     */ 
/*     */               
/* 319 */               boolean required = determineRequiredStatus(ann);
/* 320 */               if (required) {
/* 321 */                 if (!candidates.isEmpty()) {
/* 322 */                   throw new BeanCreationException(beanName, "Invalid autowire-marked constructors: " + candidates + ". Found constructor with 'required' Autowired annotation: " + candidate);
/*     */                 }
/*     */ 
/*     */ 
/*     */                 
/* 327 */                 requiredConstructor = candidate;
/*     */               } 
/* 329 */               candidates.add(candidate);
/*     */             }
/* 331 */             else if (candidate.getParameterCount() == 0) {
/* 332 */               defaultConstructor = candidate;
/*     */             }  continue;
/*     */           } 
/* 335 */           if (!candidates.isEmpty()) {
/*     */             
/* 337 */             if (requiredConstructor == null) {
/* 338 */               if (defaultConstructor != null) {
/* 339 */                 candidates.add(defaultConstructor);
/*     */               }
/* 341 */               else if (candidates.size() == 1 && this.logger.isInfoEnabled()) {
/* 342 */                 this.logger.info("Inconsistent constructor declaration on bean with name '" + beanName + "': single autowire-marked constructor flagged as optional - this constructor is effectively required since there is no default constructor to fall back to: " + candidates
/*     */ 
/*     */                     
/* 345 */                     .get(0));
/*     */               } 
/*     */             }
/* 348 */             arrayOfConstructor = candidates.<Constructor>toArray(new Constructor[0]);
/*     */           }
/* 350 */           else if (arrayOfConstructor1.length == 1 && arrayOfConstructor1[0].getParameterCount() > 0) {
/* 351 */             arrayOfConstructor = new Constructor[] { arrayOfConstructor1[0] };
/*     */           }
/* 353 */           else if (nonSyntheticConstructors == 2 && primaryConstructor != null && defaultConstructor != null && 
/* 354 */             !primaryConstructor.equals(defaultConstructor)) {
/* 355 */             arrayOfConstructor = new Constructor[] { primaryConstructor, defaultConstructor };
/*     */           }
/* 357 */           else if (nonSyntheticConstructors == 1 && primaryConstructor != null) {
/* 358 */             arrayOfConstructor = new Constructor[] { primaryConstructor };
/*     */           } else {
/*     */             
/* 361 */             arrayOfConstructor = new Constructor[0];
/*     */           } 
/* 363 */           this.candidateConstructorsCache.put(beanClass, arrayOfConstructor);
/*     */         } 
/*     */       } 
/*     */     }
/* 367 */     return (arrayOfConstructor.length > 0) ? (Constructor<?>[])arrayOfConstructor : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) {
/* 372 */     InjectionMetadata metadata = findAutowiringMetadata(beanName, bean.getClass(), pvs);
/*     */     try {
/* 374 */       metadata.inject(bean, beanName, pvs);
/*     */     }
/* 376 */     catch (BeanCreationException ex) {
/* 377 */       throw ex;
/*     */     }
/* 379 */     catch (Throwable ex) {
/* 380 */       throw new BeanCreationException(beanName, "Injection of autowired dependencies failed", ex);
/*     */     } 
/* 382 */     return pvs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) {
/* 390 */     return postProcessProperties(pvs, bean, beanName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void processInjection(Object bean) throws BeanCreationException {
/* 400 */     Class<?> clazz = bean.getClass();
/* 401 */     InjectionMetadata metadata = findAutowiringMetadata(clazz.getName(), clazz, null);
/*     */     try {
/* 403 */       metadata.inject(bean, null, null);
/*     */     }
/* 405 */     catch (BeanCreationException ex) {
/* 406 */       throw ex;
/*     */     }
/* 408 */     catch (Throwable ex) {
/* 409 */       throw new BeanCreationException("Injection of autowired dependencies failed for class [" + clazz + "]", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private InjectionMetadata findAutowiringMetadata(String beanName, Class<?> clazz, @Nullable PropertyValues pvs) {
/* 417 */     String cacheKey = StringUtils.hasLength(beanName) ? beanName : clazz.getName();
/*     */     
/* 419 */     InjectionMetadata metadata = this.injectionMetadataCache.get(cacheKey);
/* 420 */     if (InjectionMetadata.needsRefresh(metadata, clazz)) {
/* 421 */       synchronized (this.injectionMetadataCache) {
/* 422 */         metadata = this.injectionMetadataCache.get(cacheKey);
/* 423 */         if (InjectionMetadata.needsRefresh(metadata, clazz)) {
/* 424 */           if (metadata != null) {
/* 425 */             metadata.clear(pvs);
/*     */           }
/* 427 */           metadata = buildAutowiringMetadata(clazz);
/* 428 */           this.injectionMetadataCache.put(cacheKey, metadata);
/*     */         } 
/*     */       } 
/*     */     }
/* 432 */     return metadata;
/*     */   }
/*     */   
/*     */   private InjectionMetadata buildAutowiringMetadata(Class<?> clazz) {
/* 436 */     List<InjectionMetadata.InjectedElement> elements = new ArrayList<>();
/* 437 */     Class<?> targetClass = clazz;
/*     */     
/*     */     do {
/* 440 */       List<InjectionMetadata.InjectedElement> currElements = new ArrayList<>();
/*     */       
/* 442 */       ReflectionUtils.doWithLocalFields(targetClass, field -> {
/*     */             AnnotationAttributes ann = findAutowiredAnnotation(field);
/*     */             
/*     */             if (ann != null) {
/*     */               if (Modifier.isStatic(field.getModifiers())) {
/*     */                 if (this.logger.isInfoEnabled()) {
/*     */                   this.logger.info("Autowired annotation is not supported on static fields: " + field);
/*     */                 }
/*     */                 return;
/*     */               } 
/*     */               boolean required = determineRequiredStatus(ann);
/*     */               currElements.add(new AutowiredFieldElement(field, required));
/*     */             } 
/*     */           });
/* 456 */       ReflectionUtils.doWithLocalMethods(targetClass, method -> {
/*     */             Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
/*     */             
/*     */             if (!BridgeMethodResolver.isVisibilityBridgeMethodPair(method, bridgedMethod)) {
/*     */               return;
/*     */             }
/*     */             
/*     */             AnnotationAttributes ann = findAutowiredAnnotation(bridgedMethod);
/*     */             
/*     */             if (ann != null && method.equals(ClassUtils.getMostSpecificMethod(method, clazz))) {
/*     */               if (Modifier.isStatic(method.getModifiers())) {
/*     */                 if (this.logger.isInfoEnabled()) {
/*     */                   this.logger.info("Autowired annotation is not supported on static methods: " + method);
/*     */                 }
/*     */                 
/*     */                 return;
/*     */               } 
/*     */               if (method.getParameterCount() == 0 && this.logger.isInfoEnabled()) {
/*     */                 this.logger.info("Autowired annotation should only be used on methods with parameters: " + method);
/*     */               }
/*     */               boolean required = determineRequiredStatus(ann);
/*     */               PropertyDescriptor pd = BeanUtils.findPropertyForMethod(bridgedMethod, clazz);
/*     */               currElements.add(new AutowiredMethodElement(method, required, pd));
/*     */             } 
/*     */           });
/* 481 */       elements.addAll(0, currElements);
/* 482 */       targetClass = targetClass.getSuperclass();
/*     */     }
/* 484 */     while (targetClass != null && targetClass != Object.class);
/*     */     
/* 486 */     return new InjectionMetadata(clazz, elements);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private AnnotationAttributes findAutowiredAnnotation(AccessibleObject ao) {
/* 491 */     if ((ao.getAnnotations()).length > 0) {
/* 492 */       for (Class<? extends Annotation> type : this.autowiredAnnotationTypes) {
/* 493 */         AnnotationAttributes attributes = AnnotatedElementUtils.getMergedAnnotationAttributes(ao, type);
/* 494 */         if (attributes != null) {
/* 495 */           return attributes;
/*     */         }
/*     */       } 
/*     */     }
/* 499 */     return null;
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
/*     */   protected boolean determineRequiredStatus(AnnotationAttributes ann) {
/* 511 */     return (!ann.containsKey(this.requiredParameterName) || this.requiredParameterValue == ann
/* 512 */       .getBoolean(this.requiredParameterName));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T> Map<String, T> findAutowireCandidates(Class<T> type) throws BeansException {
/* 522 */     if (this.beanFactory == null) {
/* 523 */       throw new IllegalStateException("No BeanFactory configured - override the getBeanOfType method or specify the 'beanFactory' property");
/*     */     }
/*     */     
/* 526 */     return BeanFactoryUtils.beansOfTypeIncludingAncestors((ListableBeanFactory)this.beanFactory, type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void registerDependentBeans(@Nullable String beanName, Set<String> autowiredBeanNames) {
/* 533 */     if (beanName != null) {
/* 534 */       for (String autowiredBeanName : autowiredBeanNames) {
/* 535 */         if (this.beanFactory != null && this.beanFactory.containsBean(autowiredBeanName)) {
/* 536 */           this.beanFactory.registerDependentBean(autowiredBeanName, beanName);
/*     */         }
/* 538 */         if (this.logger.isTraceEnabled()) {
/* 539 */           this.logger.trace("Autowiring by type from bean name '" + beanName + "' to bean named '" + autowiredBeanName + "'");
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Object resolvedCachedArgument(@Nullable String beanName, @Nullable Object cachedArgument) {
/* 551 */     if (cachedArgument instanceof DependencyDescriptor) {
/* 552 */       DependencyDescriptor descriptor = (DependencyDescriptor)cachedArgument;
/* 553 */       Assert.state((this.beanFactory != null), "No BeanFactory available");
/* 554 */       return this.beanFactory.resolveDependency(descriptor, beanName, null, null);
/*     */     } 
/*     */     
/* 557 */     return cachedArgument;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class AutowiredFieldElement
/*     */     extends InjectionMetadata.InjectedElement
/*     */   {
/*     */     private final boolean required;
/*     */ 
/*     */     
/*     */     private volatile boolean cached = false;
/*     */     
/*     */     @Nullable
/*     */     private volatile Object cachedFieldValue;
/*     */ 
/*     */     
/*     */     public AutowiredFieldElement(Field field, boolean required) {
/* 575 */       super(field, null);
/* 576 */       this.required = required;
/*     */     }
/*     */     
/*     */     protected void inject(Object bean, @Nullable String beanName, @Nullable PropertyValues pvs) throws Throwable {
/*     */       Object value;
/* 581 */       Field field = (Field)this.member;
/*     */       
/* 583 */       if (this.cached) {
/* 584 */         value = AutowiredAnnotationBeanPostProcessor.this.resolvedCachedArgument(beanName, this.cachedFieldValue);
/*     */       } else {
/*     */         
/* 587 */         DependencyDescriptor desc = new DependencyDescriptor(field, this.required);
/* 588 */         desc.setContainingClass(bean.getClass());
/* 589 */         Set<String> autowiredBeanNames = new LinkedHashSet<>(1);
/* 590 */         Assert.state((AutowiredAnnotationBeanPostProcessor.this.beanFactory != null), "No BeanFactory available");
/* 591 */         TypeConverter typeConverter = AutowiredAnnotationBeanPostProcessor.this.beanFactory.getTypeConverter();
/*     */         try {
/* 593 */           value = AutowiredAnnotationBeanPostProcessor.this.beanFactory.resolveDependency(desc, beanName, autowiredBeanNames, typeConverter);
/*     */         }
/* 595 */         catch (BeansException ex) {
/* 596 */           throw new UnsatisfiedDependencyException(null, beanName, new InjectionPoint(field), ex);
/*     */         } 
/* 598 */         synchronized (this) {
/* 599 */           if (!this.cached) {
/* 600 */             if (value != null || this.required) {
/* 601 */               this.cachedFieldValue = desc;
/* 602 */               AutowiredAnnotationBeanPostProcessor.this.registerDependentBeans(beanName, autowiredBeanNames);
/* 603 */               if (autowiredBeanNames.size() == 1) {
/* 604 */                 String autowiredBeanName = autowiredBeanNames.iterator().next();
/* 605 */                 if (AutowiredAnnotationBeanPostProcessor.this.beanFactory.containsBean(autowiredBeanName) && AutowiredAnnotationBeanPostProcessor.this
/* 606 */                   .beanFactory.isTypeMatch(autowiredBeanName, field.getType())) {
/* 607 */                   this
/* 608 */                     .cachedFieldValue = new AutowiredAnnotationBeanPostProcessor.ShortcutDependencyDescriptor(desc, autowiredBeanName, field.getType());
/*     */                 }
/*     */               } 
/*     */             } else {
/*     */               
/* 613 */               this.cachedFieldValue = null;
/*     */             } 
/* 615 */             this.cached = true;
/*     */           } 
/*     */         } 
/*     */       } 
/* 619 */       if (value != null) {
/* 620 */         ReflectionUtils.makeAccessible(field);
/* 621 */         field.set(bean, value);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class AutowiredMethodElement
/*     */     extends InjectionMetadata.InjectedElement
/*     */   {
/*     */     private final boolean required;
/*     */     
/*     */     private volatile boolean cached = false;
/*     */     
/*     */     @Nullable
/*     */     private volatile Object[] cachedMethodArguments;
/*     */ 
/*     */     
/*     */     public AutowiredMethodElement(Method method, @Nullable boolean required, PropertyDescriptor pd) {
/* 640 */       super(method, pd);
/* 641 */       this.required = required;
/*     */     }
/*     */     
/*     */     protected void inject(Object bean, @Nullable String beanName, @Nullable PropertyValues pvs) throws Throwable {
/*     */       Object[] arguments;
/* 646 */       if (checkPropertySkipping(pvs)) {
/*     */         return;
/*     */       }
/* 649 */       Method method = (Method)this.member;
/*     */       
/* 651 */       if (this.cached) {
/*     */         
/* 653 */         arguments = resolveCachedArguments(beanName);
/*     */       } else {
/*     */         
/* 656 */         Class<?>[] paramTypes = method.getParameterTypes();
/* 657 */         arguments = new Object[paramTypes.length];
/* 658 */         DependencyDescriptor[] descriptors = new DependencyDescriptor[paramTypes.length];
/* 659 */         Set<String> autowiredBeans = new LinkedHashSet<>(paramTypes.length);
/* 660 */         Assert.state((AutowiredAnnotationBeanPostProcessor.this.beanFactory != null), "No BeanFactory available");
/* 661 */         TypeConverter typeConverter = AutowiredAnnotationBeanPostProcessor.this.beanFactory.getTypeConverter();
/* 662 */         for (int i = 0; i < arguments.length; i++) {
/* 663 */           MethodParameter methodParam = new MethodParameter(method, i);
/* 664 */           DependencyDescriptor currDesc = new DependencyDescriptor(methodParam, this.required);
/* 665 */           currDesc.setContainingClass(bean.getClass());
/* 666 */           descriptors[i] = currDesc;
/*     */           try {
/* 668 */             Object arg = AutowiredAnnotationBeanPostProcessor.this.beanFactory.resolveDependency(currDesc, beanName, autowiredBeans, typeConverter);
/* 669 */             if (arg == null && !this.required) {
/* 670 */               arguments = null;
/*     */               break;
/*     */             } 
/* 673 */             arguments[i] = arg;
/*     */           }
/* 675 */           catch (BeansException ex) {
/* 676 */             throw new UnsatisfiedDependencyException(null, beanName, new InjectionPoint(methodParam), ex);
/*     */           } 
/*     */         } 
/* 679 */         synchronized (this) {
/* 680 */           if (!this.cached) {
/* 681 */             if (arguments != null) {
/* 682 */               Object[] cachedMethodArguments = new Object[paramTypes.length];
/* 683 */               System.arraycopy(descriptors, 0, cachedMethodArguments, 0, arguments.length);
/* 684 */               AutowiredAnnotationBeanPostProcessor.this.registerDependentBeans(beanName, autowiredBeans);
/* 685 */               if (autowiredBeans.size() == paramTypes.length) {
/* 686 */                 Iterator<String> it = autowiredBeans.iterator();
/* 687 */                 for (int j = 0; j < paramTypes.length; j++) {
/* 688 */                   String autowiredBeanName = it.next();
/* 689 */                   if (AutowiredAnnotationBeanPostProcessor.this.beanFactory.containsBean(autowiredBeanName) && AutowiredAnnotationBeanPostProcessor.this
/* 690 */                     .beanFactory.isTypeMatch(autowiredBeanName, paramTypes[j])) {
/* 691 */                     cachedMethodArguments[j] = new AutowiredAnnotationBeanPostProcessor.ShortcutDependencyDescriptor(descriptors[j], autowiredBeanName, paramTypes[j]);
/*     */                   }
/*     */                 } 
/*     */               } 
/*     */               
/* 696 */               this.cachedMethodArguments = cachedMethodArguments;
/*     */             } else {
/*     */               
/* 699 */               this.cachedMethodArguments = null;
/*     */             } 
/* 701 */             this.cached = true;
/*     */           } 
/*     */         } 
/*     */       } 
/* 705 */       if (arguments != null) {
/*     */         try {
/* 707 */           ReflectionUtils.makeAccessible(method);
/* 708 */           method.invoke(bean, arguments);
/*     */         }
/* 710 */         catch (InvocationTargetException ex) {
/* 711 */           throw ex.getTargetException();
/*     */         } 
/*     */       }
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     private Object[] resolveCachedArguments(@Nullable String beanName) {
/* 718 */       Object[] cachedMethodArguments = this.cachedMethodArguments;
/* 719 */       if (cachedMethodArguments == null) {
/* 720 */         return null;
/*     */       }
/* 722 */       Object[] arguments = new Object[cachedMethodArguments.length];
/* 723 */       for (int i = 0; i < arguments.length; i++) {
/* 724 */         arguments[i] = AutowiredAnnotationBeanPostProcessor.this.resolvedCachedArgument(beanName, cachedMethodArguments[i]);
/*     */       }
/* 726 */       return arguments;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ShortcutDependencyDescriptor
/*     */     extends DependencyDescriptor
/*     */   {
/*     */     private final String shortcut;
/*     */ 
/*     */     
/*     */     private final Class<?> requiredType;
/*     */ 
/*     */     
/*     */     public ShortcutDependencyDescriptor(DependencyDescriptor original, String shortcut, Class<?> requiredType) {
/* 742 */       super(original);
/* 743 */       this.shortcut = shortcut;
/* 744 */       this.requiredType = requiredType;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object resolveShortcut(BeanFactory beanFactory) {
/* 749 */       return beanFactory.getBean(this.shortcut, this.requiredType);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/annotation/AutowiredAnnotationBeanPostProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */