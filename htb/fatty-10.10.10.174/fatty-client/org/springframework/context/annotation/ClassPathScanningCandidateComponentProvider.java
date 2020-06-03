/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
/*     */ import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
/*     */ import org.springframework.beans.factory.annotation.Lookup;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.context.ResourceLoaderAware;
/*     */ import org.springframework.context.index.CandidateComponentsIndex;
/*     */ import org.springframework.context.index.CandidateComponentsIndexLoader;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.env.EnvironmentCapable;
/*     */ import org.springframework.core.env.StandardEnvironment;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
/*     */ import org.springframework.core.io.support.ResourcePatternResolver;
/*     */ import org.springframework.core.io.support.ResourcePatternUtils;
/*     */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
/*     */ import org.springframework.core.type.classreading.MetadataReader;
/*     */ import org.springframework.core.type.classreading.MetadataReaderFactory;
/*     */ import org.springframework.core.type.filter.AnnotationTypeFilter;
/*     */ import org.springframework.core.type.filter.AssignableTypeFilter;
/*     */ import org.springframework.core.type.filter.TypeFilter;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.stereotype.Component;
/*     */ import org.springframework.stereotype.Indexed;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassPathScanningCandidateComponentProvider
/*     */   implements EnvironmentCapable, ResourceLoaderAware
/*     */ {
/*     */   static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
/*  93 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  95 */   private String resourcePattern = "**/*.class";
/*     */   
/*  97 */   private final List<TypeFilter> includeFilters = new LinkedList<>();
/*     */   
/*  99 */   private final List<TypeFilter> excludeFilters = new LinkedList<>();
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Environment environment;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ConditionEvaluator conditionEvaluator;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ResourcePatternResolver resourcePatternResolver;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private MetadataReaderFactory metadataReaderFactory;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private CandidateComponentsIndex componentsIndex;
/*     */ 
/*     */ 
/*     */   
/*     */   protected ClassPathScanningCandidateComponentProvider() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassPathScanningCandidateComponentProvider(boolean useDefaultFilters) {
/* 133 */     this(useDefaultFilters, (Environment)new StandardEnvironment());
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
/*     */   public ClassPathScanningCandidateComponentProvider(boolean useDefaultFilters, Environment environment) {
/* 146 */     if (useDefaultFilters) {
/* 147 */       registerDefaultFilters();
/*     */     }
/* 149 */     setEnvironment(environment);
/* 150 */     setResourceLoader(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResourcePattern(String resourcePattern) {
/* 161 */     Assert.notNull(resourcePattern, "'resourcePattern' must not be null");
/* 162 */     this.resourcePattern = resourcePattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addIncludeFilter(TypeFilter includeFilter) {
/* 169 */     this.includeFilters.add(includeFilter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addExcludeFilter(TypeFilter excludeFilter) {
/* 176 */     this.excludeFilters.add(0, excludeFilter);
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
/*     */   public void resetFilters(boolean useDefaultFilters) {
/* 188 */     this.includeFilters.clear();
/* 189 */     this.excludeFilters.clear();
/* 190 */     if (useDefaultFilters) {
/* 191 */       registerDefaultFilters();
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
/*     */   protected void registerDefaultFilters() {
/* 207 */     this.includeFilters.add(new AnnotationTypeFilter(Component.class));
/* 208 */     ClassLoader cl = ClassPathScanningCandidateComponentProvider.class.getClassLoader();
/*     */     try {
/* 210 */       this.includeFilters.add(new AnnotationTypeFilter(
/* 211 */             ClassUtils.forName("javax.annotation.ManagedBean", cl), false));
/* 212 */       this.logger.trace("JSR-250 'javax.annotation.ManagedBean' found and supported for component scanning");
/*     */     }
/* 214 */     catch (ClassNotFoundException classNotFoundException) {}
/*     */ 
/*     */     
/*     */     try {
/* 218 */       this.includeFilters.add(new AnnotationTypeFilter(
/* 219 */             ClassUtils.forName("javax.inject.Named", cl), false));
/* 220 */       this.logger.trace("JSR-330 'javax.inject.Named' annotation found and supported for component scanning");
/*     */     }
/* 222 */     catch (ClassNotFoundException classNotFoundException) {}
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
/*     */   public void setEnvironment(Environment environment) {
/* 234 */     Assert.notNull(environment, "Environment must not be null");
/* 235 */     this.environment = environment;
/* 236 */     this.conditionEvaluator = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Environment getEnvironment() {
/* 241 */     if (this.environment == null) {
/* 242 */       this.environment = (Environment)new StandardEnvironment();
/*     */     }
/* 244 */     return this.environment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected BeanDefinitionRegistry getRegistry() {
/* 252 */     return null;
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
/*     */   public void setResourceLoader(@Nullable ResourceLoader resourceLoader) {
/* 265 */     this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
/* 266 */     this.metadataReaderFactory = (MetadataReaderFactory)new CachingMetadataReaderFactory(resourceLoader);
/* 267 */     this.componentsIndex = CandidateComponentsIndexLoader.loadIndex(this.resourcePatternResolver.getClassLoader());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ResourceLoader getResourceLoader() {
/* 274 */     return (ResourceLoader)getResourcePatternResolver();
/*     */   }
/*     */   
/*     */   private ResourcePatternResolver getResourcePatternResolver() {
/* 278 */     if (this.resourcePatternResolver == null) {
/* 279 */       this.resourcePatternResolver = (ResourcePatternResolver)new PathMatchingResourcePatternResolver();
/*     */     }
/* 281 */     return this.resourcePatternResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMetadataReaderFactory(MetadataReaderFactory metadataReaderFactory) {
/* 292 */     this.metadataReaderFactory = metadataReaderFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final MetadataReaderFactory getMetadataReaderFactory() {
/* 299 */     if (this.metadataReaderFactory == null) {
/* 300 */       this.metadataReaderFactory = (MetadataReaderFactory)new CachingMetadataReaderFactory();
/*     */     }
/* 302 */     return this.metadataReaderFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<BeanDefinition> findCandidateComponents(String basePackage) {
/* 312 */     if (this.componentsIndex != null && indexSupportsIncludeFilters()) {
/* 313 */       return addCandidateComponentsFromIndex(this.componentsIndex, basePackage);
/*     */     }
/*     */     
/* 316 */     return scanCandidateComponents(basePackage);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean indexSupportsIncludeFilters() {
/* 327 */     for (TypeFilter includeFilter : this.includeFilters) {
/* 328 */       if (!indexSupportsIncludeFilter(includeFilter)) {
/* 329 */         return false;
/*     */       }
/*     */     } 
/* 332 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean indexSupportsIncludeFilter(TypeFilter filter) {
/* 343 */     if (filter instanceof AnnotationTypeFilter) {
/* 344 */       Class<? extends Annotation> annotation = ((AnnotationTypeFilter)filter).getAnnotationType();
/* 345 */       return (AnnotationUtils.isAnnotationDeclaredLocally(Indexed.class, annotation) || annotation
/* 346 */         .getName().startsWith("javax."));
/*     */     } 
/* 348 */     if (filter instanceof AssignableTypeFilter) {
/* 349 */       Class<?> target = ((AssignableTypeFilter)filter).getTargetType();
/* 350 */       return AnnotationUtils.isAnnotationDeclaredLocally(Indexed.class, target);
/*     */     } 
/* 352 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String extractStereotype(TypeFilter filter) {
/* 364 */     if (filter instanceof AnnotationTypeFilter) {
/* 365 */       return ((AnnotationTypeFilter)filter).getAnnotationType().getName();
/*     */     }
/* 367 */     if (filter instanceof AssignableTypeFilter) {
/* 368 */       return ((AssignableTypeFilter)filter).getTargetType().getName();
/*     */     }
/* 370 */     return null;
/*     */   }
/*     */   
/*     */   private Set<BeanDefinition> addCandidateComponentsFromIndex(CandidateComponentsIndex index, String basePackage) {
/* 374 */     Set<BeanDefinition> candidates = new LinkedHashSet<>();
/*     */     try {
/* 376 */       Set<String> types = new HashSet<>();
/* 377 */       for (TypeFilter filter : this.includeFilters) {
/* 378 */         String stereotype = extractStereotype(filter);
/* 379 */         if (stereotype == null) {
/* 380 */           throw new IllegalArgumentException("Failed to extract stereotype from " + filter);
/*     */         }
/* 382 */         types.addAll(index.getCandidateTypes(basePackage, stereotype));
/*     */       } 
/* 384 */       boolean traceEnabled = this.logger.isTraceEnabled();
/* 385 */       boolean debugEnabled = this.logger.isDebugEnabled();
/* 386 */       for (String type : types) {
/* 387 */         MetadataReader metadataReader = getMetadataReaderFactory().getMetadataReader(type);
/* 388 */         if (isCandidateComponent(metadataReader)) {
/*     */           
/* 390 */           AnnotatedGenericBeanDefinition sbd = new AnnotatedGenericBeanDefinition(metadataReader.getAnnotationMetadata());
/* 391 */           if (isCandidateComponent((AnnotatedBeanDefinition)sbd)) {
/* 392 */             if (debugEnabled) {
/* 393 */               this.logger.debug("Using candidate component class from index: " + type);
/*     */             }
/* 395 */             candidates.add(sbd);
/*     */             continue;
/*     */           } 
/* 398 */           if (debugEnabled) {
/* 399 */             this.logger.debug("Ignored because not a concrete top-level class: " + type);
/*     */           }
/*     */           
/*     */           continue;
/*     */         } 
/* 404 */         if (traceEnabled) {
/* 405 */           this.logger.trace("Ignored because matching an exclude filter: " + type);
/*     */         }
/*     */       }
/*     */     
/*     */     }
/* 410 */     catch (IOException ex) {
/* 411 */       throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
/*     */     } 
/* 413 */     return candidates;
/*     */   }
/*     */   
/*     */   private Set<BeanDefinition> scanCandidateComponents(String basePackage) {
/* 417 */     Set<BeanDefinition> candidates = new LinkedHashSet<>();
/*     */     
/*     */     try {
/* 420 */       String packageSearchPath = "classpath*:" + resolveBasePackage(basePackage) + '/' + this.resourcePattern;
/* 421 */       Resource[] resources = getResourcePatternResolver().getResources(packageSearchPath);
/* 422 */       boolean traceEnabled = this.logger.isTraceEnabled();
/* 423 */       boolean debugEnabled = this.logger.isDebugEnabled();
/* 424 */       for (Resource resource : resources) {
/* 425 */         if (traceEnabled) {
/* 426 */           this.logger.trace("Scanning " + resource);
/*     */         }
/* 428 */         if (resource.isReadable()) {
/*     */           try {
/* 430 */             MetadataReader metadataReader = getMetadataReaderFactory().getMetadataReader(resource);
/* 431 */             if (isCandidateComponent(metadataReader)) {
/* 432 */               ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(metadataReader);
/* 433 */               sbd.setResource(resource);
/* 434 */               sbd.setSource(resource);
/* 435 */               if (isCandidateComponent(sbd)) {
/* 436 */                 if (debugEnabled) {
/* 437 */                   this.logger.debug("Identified candidate component class: " + resource);
/*     */                 }
/* 439 */                 candidates.add(sbd);
/*     */               
/*     */               }
/* 442 */               else if (debugEnabled) {
/* 443 */                 this.logger.debug("Ignored because not a concrete top-level class: " + resource);
/*     */               
/*     */               }
/*     */             
/*     */             }
/* 448 */             else if (traceEnabled) {
/* 449 */               this.logger.trace("Ignored because not matching any filter: " + resource);
/*     */             }
/*     */           
/*     */           }
/* 453 */           catch (Throwable ex) {
/* 454 */             throw new BeanDefinitionStoreException("Failed to read candidate component class: " + resource, ex);
/*     */           
/*     */           }
/*     */         
/*     */         }
/* 459 */         else if (traceEnabled) {
/* 460 */           this.logger.trace("Ignored because not readable: " + resource);
/*     */         }
/*     */       
/*     */       }
/*     */     
/* 465 */     } catch (IOException ex) {
/* 466 */       throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
/*     */     } 
/* 468 */     return candidates;
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
/*     */   protected String resolveBasePackage(String basePackage) {
/* 481 */     return ClassUtils.convertClassNameToResourcePath(getEnvironment().resolveRequiredPlaceholders(basePackage));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isCandidateComponent(MetadataReader metadataReader) throws IOException {
/* 491 */     for (TypeFilter tf : this.excludeFilters) {
/* 492 */       if (tf.match(metadataReader, getMetadataReaderFactory())) {
/* 493 */         return false;
/*     */       }
/*     */     } 
/* 496 */     for (TypeFilter tf : this.includeFilters) {
/* 497 */       if (tf.match(metadataReader, getMetadataReaderFactory())) {
/* 498 */         return isConditionMatch(metadataReader);
/*     */       }
/*     */     } 
/* 501 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isConditionMatch(MetadataReader metadataReader) {
/* 511 */     if (this.conditionEvaluator == null) {
/* 512 */       this
/* 513 */         .conditionEvaluator = new ConditionEvaluator(getRegistry(), this.environment, (ResourceLoader)this.resourcePatternResolver);
/*     */     }
/* 515 */     return !this.conditionEvaluator.shouldSkip((AnnotatedTypeMetadata)metadataReader.getAnnotationMetadata());
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
/*     */   protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
/* 527 */     AnnotationMetadata metadata = beanDefinition.getMetadata();
/* 528 */     return (metadata.isIndependent() && (metadata.isConcrete() || (metadata
/* 529 */       .isAbstract() && metadata.hasAnnotatedMethods(Lookup.class.getName()))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearCache() {
/* 537 */     if (this.metadataReaderFactory instanceof CachingMetadataReaderFactory)
/*     */     {
/*     */       
/* 540 */       ((CachingMetadataReaderFactory)this.metadataReaderFactory).clearCache();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/ClassPathScanningCandidateComponentProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */