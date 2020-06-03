/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.beans.Introspector;
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.annotation.PostConstruct;
/*     */ import javax.annotation.PreDestroy;
/*     */ import javax.annotation.Resource;
/*     */ import javax.ejb.EJB;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.Service;
/*     */ import javax.xml.ws.WebServiceClient;
/*     */ import javax.xml.ws.WebServiceRef;
/*     */ import org.springframework.aop.TargetSource;
/*     */ import org.springframework.aop.framework.ProxyFactory;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.PropertyValues;
/*     */ import org.springframework.beans.factory.BeanCreationException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.annotation.InitDestroyAnnotationBeanPostProcessor;
/*     */ import org.springframework.beans.factory.annotation.InjectionMetadata;
/*     */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.beans.factory.config.DependencyDescriptor;
/*     */ import org.springframework.beans.factory.config.EmbeddedValueResolver;
/*     */ import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.core.BridgeMethodResolver;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.jndi.support.SimpleJndiBeanFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.util.StringValueResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CommonAnnotationBeanPostProcessor
/*     */   extends InitDestroyAnnotationBeanPostProcessor
/*     */   implements InstantiationAwareBeanPostProcessor, BeanFactoryAware, Serializable
/*     */ {
/*     */   @Nullable
/*     */   private static Class<? extends Annotation> webServiceRefClass;
/*     */   @Nullable
/*     */   private static Class<? extends Annotation> ejbRefClass;
/*     */   
/*     */   static {
/*     */     try {
/* 156 */       Class<? extends Annotation> clazz = ClassUtils.forName("javax.xml.ws.WebServiceRef", CommonAnnotationBeanPostProcessor.class.getClassLoader());
/* 157 */       webServiceRefClass = clazz;
/*     */     }
/* 159 */     catch (ClassNotFoundException ex) {
/* 160 */       webServiceRefClass = null;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 165 */       Class<? extends Annotation> clazz = ClassUtils.forName("javax.ejb.EJB", CommonAnnotationBeanPostProcessor.class.getClassLoader());
/* 166 */       ejbRefClass = clazz;
/*     */     }
/* 168 */     catch (ClassNotFoundException ex) {
/* 169 */       ejbRefClass = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 174 */   private final Set<String> ignoredResourceTypes = new HashSet<>(1);
/*     */   
/*     */   private boolean fallbackToDefaultTypeMatch = true;
/*     */   
/*     */   private boolean alwaysUseJndiLookup = false;
/*     */   
/* 180 */   private transient BeanFactory jndiFactory = (BeanFactory)new SimpleJndiBeanFactory();
/*     */   
/*     */   @Nullable
/*     */   private transient BeanFactory resourceFactory;
/*     */   
/*     */   @Nullable
/*     */   private transient BeanFactory beanFactory;
/*     */   
/*     */   @Nullable
/*     */   private transient StringValueResolver embeddedValueResolver;
/*     */   
/* 191 */   private final transient Map<String, InjectionMetadata> injectionMetadataCache = new ConcurrentHashMap<>(256);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CommonAnnotationBeanPostProcessor() {
/* 201 */     setOrder(2147483644);
/* 202 */     setInitAnnotationType(PostConstruct.class);
/* 203 */     setDestroyAnnotationType(PreDestroy.class);
/* 204 */     ignoreResourceType("javax.xml.ws.WebServiceContext");
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
/*     */   public void ignoreResourceType(String resourceType) {
/* 216 */     Assert.notNull(resourceType, "Ignored resource type must not be null");
/* 217 */     this.ignoredResourceTypes.add(resourceType);
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
/*     */   public void setFallbackToDefaultTypeMatch(boolean fallbackToDefaultTypeMatch) {
/* 231 */     this.fallbackToDefaultTypeMatch = fallbackToDefaultTypeMatch;
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
/*     */   public void setAlwaysUseJndiLookup(boolean alwaysUseJndiLookup) {
/* 245 */     this.alwaysUseJndiLookup = alwaysUseJndiLookup;
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
/*     */   public void setJndiFactory(BeanFactory jndiFactory) {
/* 260 */     Assert.notNull(jndiFactory, "BeanFactory must not be null");
/* 261 */     this.jndiFactory = jndiFactory;
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
/*     */   public void setResourceFactory(BeanFactory resourceFactory) {
/* 278 */     Assert.notNull(resourceFactory, "BeanFactory must not be null");
/* 279 */     this.resourceFactory = resourceFactory;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 284 */     Assert.notNull(beanFactory, "BeanFactory must not be null");
/* 285 */     this.beanFactory = beanFactory;
/* 286 */     if (this.resourceFactory == null) {
/* 287 */       this.resourceFactory = beanFactory;
/*     */     }
/* 289 */     if (beanFactory instanceof ConfigurableBeanFactory) {
/* 290 */       this.embeddedValueResolver = (StringValueResolver)new EmbeddedValueResolver((ConfigurableBeanFactory)beanFactory);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
/* 297 */     super.postProcessMergedBeanDefinition(beanDefinition, beanType, beanName);
/* 298 */     InjectionMetadata metadata = findResourceMetadata(beanName, beanType, (PropertyValues)null);
/* 299 */     metadata.checkConfigMembers(beanDefinition);
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetBeanDefinition(String beanName) {
/* 304 */     this.injectionMetadataCache.remove(beanName);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {
/* 309 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean postProcessAfterInstantiation(Object bean, String beanName) {
/* 314 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) {
/* 319 */     InjectionMetadata metadata = findResourceMetadata(beanName, bean.getClass(), pvs);
/*     */     try {
/* 321 */       metadata.inject(bean, beanName, pvs);
/*     */     }
/* 323 */     catch (Throwable ex) {
/* 324 */       throw new BeanCreationException(beanName, "Injection of resource dependencies failed", ex);
/*     */     } 
/* 326 */     return pvs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) {
/* 334 */     return postProcessProperties(pvs, bean, beanName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private InjectionMetadata findResourceMetadata(String beanName, Class<?> clazz, @Nullable PropertyValues pvs) {
/* 340 */     String cacheKey = StringUtils.hasLength(beanName) ? beanName : clazz.getName();
/*     */     
/* 342 */     InjectionMetadata metadata = this.injectionMetadataCache.get(cacheKey);
/* 343 */     if (InjectionMetadata.needsRefresh(metadata, clazz)) {
/* 344 */       synchronized (this.injectionMetadataCache) {
/* 345 */         metadata = this.injectionMetadataCache.get(cacheKey);
/* 346 */         if (InjectionMetadata.needsRefresh(metadata, clazz)) {
/* 347 */           if (metadata != null) {
/* 348 */             metadata.clear(pvs);
/*     */           }
/* 350 */           metadata = buildResourceMetadata(clazz);
/* 351 */           this.injectionMetadataCache.put(cacheKey, metadata);
/*     */         } 
/*     */       } 
/*     */     }
/* 355 */     return metadata;
/*     */   }
/*     */   
/*     */   private InjectionMetadata buildResourceMetadata(Class<?> clazz) {
/* 359 */     List<InjectionMetadata.InjectedElement> elements = new ArrayList<>();
/* 360 */     Class<?> targetClass = clazz;
/*     */     
/*     */     do {
/* 363 */       List<InjectionMetadata.InjectedElement> currElements = new ArrayList<>();
/*     */       
/* 365 */       ReflectionUtils.doWithLocalFields(targetClass, field -> {
/*     */             if (webServiceRefClass != null && field.isAnnotationPresent(webServiceRefClass)) {
/*     */               if (Modifier.isStatic(field.getModifiers())) {
/*     */                 throw new IllegalStateException("@WebServiceRef annotation is not supported on static fields");
/*     */               }
/*     */               
/*     */               currElements.add(new WebServiceRefElement(field, field, null));
/*     */             } else if (ejbRefClass != null && field.isAnnotationPresent(ejbRefClass)) {
/*     */               if (Modifier.isStatic(field.getModifiers())) {
/*     */                 throw new IllegalStateException("@EJB annotation is not supported on static fields");
/*     */               }
/*     */               
/*     */               currElements.add(new EjbRefElement(field, field, null));
/*     */             } else if (field.isAnnotationPresent((Class)Resource.class)) {
/*     */               if (Modifier.isStatic(field.getModifiers())) {
/*     */                 throw new IllegalStateException("@Resource annotation is not supported on static fields");
/*     */               }
/*     */               
/*     */               if (!this.ignoredResourceTypes.contains(field.getType().getName())) {
/*     */                 currElements.add(new ResourceElement(field, field, null));
/*     */               }
/*     */             } 
/*     */           });
/* 388 */       ReflectionUtils.doWithLocalMethods(targetClass, method -> {
/*     */             Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
/*     */             
/*     */             if (!BridgeMethodResolver.isVisibilityBridgeMethodPair(method, bridgedMethod)) {
/*     */               return;
/*     */             }
/*     */             
/*     */             if (method.equals(ClassUtils.getMostSpecificMethod(method, clazz))) {
/*     */               if (webServiceRefClass != null && bridgedMethod.isAnnotationPresent(webServiceRefClass)) {
/*     */                 if (Modifier.isStatic(method.getModifiers())) {
/*     */                   throw new IllegalStateException("@WebServiceRef annotation is not supported on static methods");
/*     */                 }
/*     */                 
/*     */                 if (method.getParameterCount() != 1) {
/*     */                   throw new IllegalStateException("@WebServiceRef annotation requires a single-arg method: " + method);
/*     */                 }
/*     */                 PropertyDescriptor pd = BeanUtils.findPropertyForMethod(bridgedMethod, clazz);
/*     */                 currElements.add(new WebServiceRefElement(method, bridgedMethod, pd));
/*     */               } else if (ejbRefClass != null && bridgedMethod.isAnnotationPresent(ejbRefClass)) {
/*     */                 if (Modifier.isStatic(method.getModifiers())) {
/*     */                   throw new IllegalStateException("@EJB annotation is not supported on static methods");
/*     */                 }
/*     */                 if (method.getParameterCount() != 1) {
/*     */                   throw new IllegalStateException("@EJB annotation requires a single-arg method: " + method);
/*     */                 }
/*     */                 PropertyDescriptor pd = BeanUtils.findPropertyForMethod(bridgedMethod, clazz);
/*     */                 currElements.add(new EjbRefElement(method, bridgedMethod, pd));
/*     */               } else if (bridgedMethod.isAnnotationPresent((Class)Resource.class)) {
/*     */                 if (Modifier.isStatic(method.getModifiers())) {
/*     */                   throw new IllegalStateException("@Resource annotation is not supported on static methods");
/*     */                 }
/*     */                 Class<?>[] paramTypes = method.getParameterTypes();
/*     */                 if (paramTypes.length != 1) {
/*     */                   throw new IllegalStateException("@Resource annotation requires a single-arg method: " + method);
/*     */                 }
/*     */                 if (!this.ignoredResourceTypes.contains(paramTypes[0].getName())) {
/*     */                   PropertyDescriptor pd = BeanUtils.findPropertyForMethod(bridgedMethod, clazz);
/*     */                   currElements.add(new ResourceElement(method, bridgedMethod, pd));
/*     */                 } 
/*     */               } 
/*     */             }
/*     */           });
/* 430 */       elements.addAll(0, currElements);
/* 431 */       targetClass = targetClass.getSuperclass();
/*     */     }
/* 433 */     while (targetClass != null && targetClass != Object.class);
/*     */     
/* 435 */     return new InjectionMetadata(clazz, elements);
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
/*     */   protected Object buildLazyResourceProxy(final LookupElement element, @Nullable final String requestingBeanName) {
/* 449 */     TargetSource ts = new TargetSource()
/*     */       {
/*     */         public Class<?> getTargetClass() {
/* 452 */           return element.lookupType;
/*     */         }
/*     */         
/*     */         public boolean isStatic() {
/* 456 */           return false;
/*     */         }
/*     */         
/*     */         public Object getTarget() {
/* 460 */           return CommonAnnotationBeanPostProcessor.this.getResource(element, requestingBeanName);
/*     */         }
/*     */ 
/*     */         
/*     */         public void releaseTarget(Object target) {}
/*     */       };
/* 466 */     ProxyFactory pf = new ProxyFactory();
/* 467 */     pf.setTargetSource(ts);
/* 468 */     if (element.lookupType.isInterface()) {
/* 469 */       pf.addInterface(element.lookupType);
/*     */     }
/*     */     
/* 472 */     ClassLoader classLoader = (this.beanFactory instanceof ConfigurableBeanFactory) ? ((ConfigurableBeanFactory)this.beanFactory).getBeanClassLoader() : null;
/* 473 */     return pf.getProxy(classLoader);
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
/*     */   protected Object getResource(LookupElement element, @Nullable String requestingBeanName) throws NoSuchBeanDefinitionException {
/* 486 */     if (StringUtils.hasLength(element.mappedName)) {
/* 487 */       return this.jndiFactory.getBean(element.mappedName, element.lookupType);
/*     */     }
/* 489 */     if (this.alwaysUseJndiLookup) {
/* 490 */       return this.jndiFactory.getBean(element.name, element.lookupType);
/*     */     }
/* 492 */     if (this.resourceFactory == null) {
/* 493 */       throw new NoSuchBeanDefinitionException(element.lookupType, "No resource factory configured - specify the 'resourceFactory' property");
/*     */     }
/*     */     
/* 496 */     return autowireResource(this.resourceFactory, element, requestingBeanName);
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
/*     */   protected Object autowireResource(BeanFactory factory, LookupElement element, @Nullable String requestingBeanName) throws NoSuchBeanDefinitionException {
/*     */     Object resource;
/*     */     Set<String> autowiredBeanNames;
/* 513 */     String name = element.name;
/*     */     
/* 515 */     if (factory instanceof AutowireCapableBeanFactory) {
/* 516 */       AutowireCapableBeanFactory beanFactory = (AutowireCapableBeanFactory)factory;
/* 517 */       DependencyDescriptor descriptor = element.getDependencyDescriptor();
/* 518 */       if (this.fallbackToDefaultTypeMatch && element.isDefaultName && !factory.containsBean(name)) {
/* 519 */         autowiredBeanNames = new LinkedHashSet<>();
/* 520 */         resource = beanFactory.resolveDependency(descriptor, requestingBeanName, autowiredBeanNames, null);
/* 521 */         if (resource == null) {
/* 522 */           throw new NoSuchBeanDefinitionException(element.getLookupType(), "No resolvable resource object");
/*     */         }
/*     */       } else {
/*     */         
/* 526 */         resource = beanFactory.resolveBeanByName(name, descriptor);
/* 527 */         autowiredBeanNames = Collections.singleton(name);
/*     */       } 
/*     */     } else {
/*     */       
/* 531 */       resource = factory.getBean(name, element.lookupType);
/* 532 */       autowiredBeanNames = Collections.singleton(name);
/*     */     } 
/*     */     
/* 535 */     if (factory instanceof ConfigurableBeanFactory) {
/* 536 */       ConfigurableBeanFactory beanFactory = (ConfigurableBeanFactory)factory;
/* 537 */       for (String autowiredBeanName : autowiredBeanNames) {
/* 538 */         if (requestingBeanName != null && beanFactory.containsBean(autowiredBeanName)) {
/* 539 */           beanFactory.registerDependentBean(autowiredBeanName, requestingBeanName);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 544 */     return resource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract class LookupElement
/*     */     extends InjectionMetadata.InjectedElement
/*     */   {
/* 554 */     protected String name = "";
/*     */     
/*     */     protected boolean isDefaultName = false;
/*     */     
/* 558 */     protected Class<?> lookupType = Object.class;
/*     */     
/*     */     @Nullable
/*     */     protected String mappedName;
/*     */     
/*     */     public LookupElement(@Nullable Member member, PropertyDescriptor pd) {
/* 564 */       super(member, pd);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final String getName() {
/* 571 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final Class<?> getLookupType() {
/* 578 */       return this.lookupType;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final DependencyDescriptor getDependencyDescriptor() {
/* 585 */       if (this.isField) {
/* 586 */         return new CommonAnnotationBeanPostProcessor.LookupDependencyDescriptor((Field)this.member, this.lookupType);
/*     */       }
/*     */       
/* 589 */       return new CommonAnnotationBeanPostProcessor.LookupDependencyDescriptor((Method)this.member, this.lookupType);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class ResourceElement
/*     */     extends LookupElement
/*     */   {
/*     */     private final boolean lazyLookup;
/*     */ 
/*     */ 
/*     */     
/*     */     public ResourceElement(Member member, @Nullable AnnotatedElement ae, PropertyDescriptor pd) {
/* 604 */       super(member, pd);
/* 605 */       Resource resource = ae.<Resource>getAnnotation(Resource.class);
/* 606 */       String resourceName = resource.name();
/* 607 */       Class<?> resourceType = resource.type();
/* 608 */       this.isDefaultName = !StringUtils.hasLength(resourceName);
/* 609 */       if (this.isDefaultName) {
/* 610 */         resourceName = this.member.getName();
/* 611 */         if (this.member instanceof Method && resourceName.startsWith("set") && resourceName.length() > 3) {
/* 612 */           resourceName = Introspector.decapitalize(resourceName.substring(3));
/*     */         }
/*     */       }
/* 615 */       else if (CommonAnnotationBeanPostProcessor.this.embeddedValueResolver != null) {
/* 616 */         resourceName = CommonAnnotationBeanPostProcessor.this.embeddedValueResolver.resolveStringValue(resourceName);
/*     */       } 
/* 618 */       if (Object.class != resourceType) {
/* 619 */         checkResourceType(resourceType);
/*     */       }
/*     */       else {
/*     */         
/* 623 */         resourceType = getResourceType();
/*     */       } 
/* 625 */       this.name = (resourceName != null) ? resourceName : "";
/* 626 */       this.lookupType = resourceType;
/* 627 */       String lookupValue = resource.lookup();
/* 628 */       this.mappedName = StringUtils.hasLength(lookupValue) ? lookupValue : resource.mappedName();
/* 629 */       Lazy lazy = ae.<Lazy>getAnnotation(Lazy.class);
/* 630 */       this.lazyLookup = (lazy != null && lazy.value());
/*     */     }
/*     */ 
/*     */     
/*     */     protected Object getResourceToInject(Object target, @Nullable String requestingBeanName) {
/* 635 */       return this.lazyLookup ? CommonAnnotationBeanPostProcessor.this.buildLazyResourceProxy(this, requestingBeanName) : CommonAnnotationBeanPostProcessor.this
/* 636 */         .getResource(this, requestingBeanName);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class WebServiceRefElement
/*     */     extends LookupElement
/*     */   {
/*     */     private final Class<?> elementType;
/*     */ 
/*     */     
/*     */     private final String wsdlLocation;
/*     */ 
/*     */     
/*     */     public WebServiceRefElement(Member member, @Nullable AnnotatedElement ae, PropertyDescriptor pd) {
/* 652 */       super(member, pd);
/* 653 */       WebServiceRef resource = ae.<WebServiceRef>getAnnotation(WebServiceRef.class);
/* 654 */       String resourceName = resource.name();
/* 655 */       Class<?> resourceType = resource.type();
/* 656 */       this.isDefaultName = !StringUtils.hasLength(resourceName);
/* 657 */       if (this.isDefaultName) {
/* 658 */         resourceName = this.member.getName();
/* 659 */         if (this.member instanceof Method && resourceName.startsWith("set") && resourceName.length() > 3) {
/* 660 */           resourceName = Introspector.decapitalize(resourceName.substring(3));
/*     */         }
/*     */       } 
/* 663 */       if (Object.class != resourceType) {
/* 664 */         checkResourceType(resourceType);
/*     */       }
/*     */       else {
/*     */         
/* 668 */         resourceType = getResourceType();
/*     */       } 
/* 670 */       this.name = resourceName;
/* 671 */       this.elementType = resourceType;
/* 672 */       if (Service.class.isAssignableFrom(resourceType)) {
/* 673 */         this.lookupType = resourceType;
/*     */       } else {
/*     */         
/* 676 */         this.lookupType = resource.value();
/*     */       } 
/* 678 */       this.mappedName = resource.mappedName();
/* 679 */       this.wsdlLocation = resource.wsdlLocation();
/*     */     }
/*     */ 
/*     */     
/*     */     protected Object getResourceToInject(Object target, @Nullable String requestingBeanName) {
/*     */       Service service;
/*     */       try {
/* 686 */         service = (Service)CommonAnnotationBeanPostProcessor.this.getResource(this, requestingBeanName);
/*     */       }
/* 688 */       catch (NoSuchBeanDefinitionException notFound) {
/*     */         
/* 690 */         if (Service.class == this.lookupType) {
/* 691 */           throw new IllegalStateException("No resource with name '" + this.name + "' found in context, and no specific JAX-WS Service subclass specified. The typical solution is to either specify a LocalJaxWsServiceFactoryBean with the given name or to specify the (generated) Service subclass as @WebServiceRef(...) value.");
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 696 */         if (StringUtils.hasLength(this.wsdlLocation)) {
/*     */           try {
/* 698 */             Constructor<?> ctor = this.lookupType.getConstructor(new Class[] { URL.class, QName.class });
/* 699 */             WebServiceClient clientAnn = this.lookupType.<WebServiceClient>getAnnotation(WebServiceClient.class);
/* 700 */             if (clientAnn == null) {
/* 701 */               throw new IllegalStateException("JAX-WS Service class [" + this.lookupType.getName() + "] does not carry a WebServiceClient annotation");
/*     */             }
/*     */             
/* 704 */             service = (Service)BeanUtils.instantiateClass(ctor, new Object[] { new URL(this.wsdlLocation), new QName(clientAnn
/* 705 */                     .targetNamespace(), clientAnn.name()) });
/*     */           }
/* 707 */           catch (NoSuchMethodException ex) {
/* 708 */             throw new IllegalStateException("JAX-WS Service class [" + this.lookupType.getName() + "] does not have a (URL, QName) constructor. Cannot apply specified WSDL location [" + this.wsdlLocation + "].");
/*     */ 
/*     */           
/*     */           }
/* 712 */           catch (MalformedURLException ex) {
/* 713 */             throw new IllegalArgumentException("Specified WSDL location [" + this.wsdlLocation + "] isn't a valid URL");
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 718 */           service = (Service)BeanUtils.instantiateClass(this.lookupType);
/*     */         } 
/*     */       } 
/* 721 */       return service.getPort(this.elementType);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class EjbRefElement
/*     */     extends LookupElement
/*     */   {
/*     */     private final String beanName;
/*     */ 
/*     */ 
/*     */     
/*     */     public EjbRefElement(Member member, @Nullable AnnotatedElement ae, PropertyDescriptor pd) {
/* 735 */       super(member, pd);
/* 736 */       EJB resource = ae.<EJB>getAnnotation(EJB.class);
/* 737 */       String resourceBeanName = resource.beanName();
/* 738 */       String resourceName = resource.name();
/* 739 */       this.isDefaultName = !StringUtils.hasLength(resourceName);
/* 740 */       if (this.isDefaultName) {
/* 741 */         resourceName = this.member.getName();
/* 742 */         if (this.member instanceof Method && resourceName.startsWith("set") && resourceName.length() > 3) {
/* 743 */           resourceName = Introspector.decapitalize(resourceName.substring(3));
/*     */         }
/*     */       } 
/* 746 */       Class<?> resourceType = resource.beanInterface();
/* 747 */       if (Object.class != resourceType) {
/* 748 */         checkResourceType(resourceType);
/*     */       }
/*     */       else {
/*     */         
/* 752 */         resourceType = getResourceType();
/*     */       } 
/* 754 */       this.beanName = resourceBeanName;
/* 755 */       this.name = resourceName;
/* 756 */       this.lookupType = resourceType;
/* 757 */       this.mappedName = resource.mappedName();
/*     */     }
/*     */ 
/*     */     
/*     */     protected Object getResourceToInject(Object target, @Nullable String requestingBeanName) {
/* 762 */       if (StringUtils.hasLength(this.beanName)) {
/* 763 */         if (CommonAnnotationBeanPostProcessor.this.beanFactory != null && CommonAnnotationBeanPostProcessor.this.beanFactory.containsBean(this.beanName)) {
/*     */           
/* 765 */           Object bean = CommonAnnotationBeanPostProcessor.this.beanFactory.getBean(this.beanName, this.lookupType);
/* 766 */           if (requestingBeanName != null && CommonAnnotationBeanPostProcessor.this.beanFactory instanceof ConfigurableBeanFactory) {
/* 767 */             ((ConfigurableBeanFactory)CommonAnnotationBeanPostProcessor.this.beanFactory).registerDependentBean(this.beanName, requestingBeanName);
/*     */           }
/* 769 */           return bean;
/*     */         } 
/* 771 */         if (this.isDefaultName && !StringUtils.hasLength(this.mappedName)) {
/* 772 */           throw new NoSuchBeanDefinitionException(this.beanName, "Cannot resolve 'beanName' in local BeanFactory. Consider specifying a general 'name' value instead.");
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 777 */       return CommonAnnotationBeanPostProcessor.this.getResource(this, requestingBeanName);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class LookupDependencyDescriptor
/*     */     extends DependencyDescriptor
/*     */   {
/*     */     private final Class<?> lookupType;
/*     */ 
/*     */ 
/*     */     
/*     */     public LookupDependencyDescriptor(Field field, Class<?> lookupType) {
/* 791 */       super(field, true);
/* 792 */       this.lookupType = lookupType;
/*     */     }
/*     */     
/*     */     public LookupDependencyDescriptor(Method method, Class<?> lookupType) {
/* 796 */       super(new MethodParameter(method, 0), true);
/* 797 */       this.lookupType = lookupType;
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?> getDependencyType() {
/* 802 */       return this.lookupType;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/CommonAnnotationBeanPostProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */