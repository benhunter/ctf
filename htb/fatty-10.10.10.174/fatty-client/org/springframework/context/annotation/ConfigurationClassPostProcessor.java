/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.framework.autoproxy.AutoProxyUtils;
/*     */ import org.springframework.beans.PropertyValues;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
/*     */ import org.springframework.beans.factory.config.SingletonBeanRegistry;
/*     */ import org.springframework.beans.factory.parsing.FailFastProblemReporter;
/*     */ import org.springframework.beans.factory.parsing.PassThroughSourceExtractor;
/*     */ import org.springframework.beans.factory.parsing.ProblemReporter;
/*     */ import org.springframework.beans.factory.parsing.SourceExtractor;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
/*     */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*     */ import org.springframework.context.EnvironmentAware;
/*     */ import org.springframework.context.ResourceLoaderAware;
/*     */ import org.springframework.core.PriorityOrdered;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.env.StandardEnvironment;
/*     */ import org.springframework.core.io.DefaultResourceLoader;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
/*     */ import org.springframework.core.type.classreading.MetadataReaderFactory;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class ConfigurationClassPostProcessor
/*     */   implements BeanDefinitionRegistryPostProcessor, PriorityOrdered, ResourceLoaderAware, BeanClassLoaderAware, EnvironmentAware
/*     */ {
/*  89 */   private static final String IMPORT_REGISTRY_BEAN_NAME = ConfigurationClassPostProcessor.class
/*  90 */     .getName() + ".importRegistry";
/*     */ 
/*     */   
/*  93 */   private final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  95 */   private SourceExtractor sourceExtractor = (SourceExtractor)new PassThroughSourceExtractor();
/*     */   
/*  97 */   private ProblemReporter problemReporter = (ProblemReporter)new FailFastProblemReporter();
/*     */   
/*     */   @Nullable
/*     */   private Environment environment;
/*     */   
/* 102 */   private ResourceLoader resourceLoader = (ResourceLoader)new DefaultResourceLoader();
/*     */   
/*     */   @Nullable
/* 105 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */   
/* 107 */   private MetadataReaderFactory metadataReaderFactory = (MetadataReaderFactory)new CachingMetadataReaderFactory();
/*     */   
/*     */   private boolean setMetadataReaderFactoryCalled = false;
/*     */   
/* 111 */   private final Set<Integer> registriesPostProcessed = new HashSet<>();
/*     */   
/* 113 */   private final Set<Integer> factoriesPostProcessed = new HashSet<>();
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ConfigurationClassBeanDefinitionReader reader;
/*     */   
/*     */   private boolean localBeanNameGeneratorSet = false;
/*     */   
/* 121 */   private BeanNameGenerator componentScanBeanNameGenerator = new AnnotationBeanNameGenerator();
/*     */ 
/*     */   
/* 124 */   private BeanNameGenerator importBeanNameGenerator = new AnnotationBeanNameGenerator()
/*     */     {
/*     */       protected String buildDefaultBeanName(BeanDefinition definition) {
/* 127 */         String beanClassName = definition.getBeanClassName();
/* 128 */         Assert.state((beanClassName != null), "No bean class name set");
/* 129 */         return beanClassName;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOrder() {
/* 136 */     return Integer.MAX_VALUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSourceExtractor(@Nullable SourceExtractor sourceExtractor) {
/* 144 */     this.sourceExtractor = (sourceExtractor != null) ? sourceExtractor : (SourceExtractor)new PassThroughSourceExtractor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProblemReporter(@Nullable ProblemReporter problemReporter) {
/* 154 */     this.problemReporter = (problemReporter != null) ? problemReporter : (ProblemReporter)new FailFastProblemReporter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMetadataReaderFactory(MetadataReaderFactory metadataReaderFactory) {
/* 163 */     Assert.notNull(metadataReaderFactory, "MetadataReaderFactory must not be null");
/* 164 */     this.metadataReaderFactory = metadataReaderFactory;
/* 165 */     this.setMetadataReaderFactoryCalled = true;
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
/*     */   public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator) {
/* 186 */     Assert.notNull(beanNameGenerator, "BeanNameGenerator must not be null");
/* 187 */     this.localBeanNameGeneratorSet = true;
/* 188 */     this.componentScanBeanNameGenerator = beanNameGenerator;
/* 189 */     this.importBeanNameGenerator = beanNameGenerator;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEnvironment(Environment environment) {
/* 194 */     Assert.notNull(environment, "Environment must not be null");
/* 195 */     this.environment = environment;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setResourceLoader(ResourceLoader resourceLoader) {
/* 200 */     Assert.notNull(resourceLoader, "ResourceLoader must not be null");
/* 201 */     this.resourceLoader = resourceLoader;
/* 202 */     if (!this.setMetadataReaderFactoryCalled) {
/* 203 */       this.metadataReaderFactory = (MetadataReaderFactory)new CachingMetadataReaderFactory(resourceLoader);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader beanClassLoader) {
/* 209 */     this.beanClassLoader = beanClassLoader;
/* 210 */     if (!this.setMetadataReaderFactoryCalled) {
/* 211 */       this.metadataReaderFactory = (MetadataReaderFactory)new CachingMetadataReaderFactory(beanClassLoader);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
/* 221 */     int registryId = System.identityHashCode(registry);
/* 222 */     if (this.registriesPostProcessed.contains(Integer.valueOf(registryId))) {
/* 223 */       throw new IllegalStateException("postProcessBeanDefinitionRegistry already called on this post-processor against " + registry);
/*     */     }
/*     */     
/* 226 */     if (this.factoriesPostProcessed.contains(Integer.valueOf(registryId))) {
/* 227 */       throw new IllegalStateException("postProcessBeanFactory already called on this post-processor against " + registry);
/*     */     }
/*     */     
/* 230 */     this.registriesPostProcessed.add(Integer.valueOf(registryId));
/*     */     
/* 232 */     processConfigBeanDefinitions(registry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
/* 241 */     int factoryId = System.identityHashCode(beanFactory);
/* 242 */     if (this.factoriesPostProcessed.contains(Integer.valueOf(factoryId))) {
/* 243 */       throw new IllegalStateException("postProcessBeanFactory already called on this post-processor against " + beanFactory);
/*     */     }
/*     */     
/* 246 */     this.factoriesPostProcessed.add(Integer.valueOf(factoryId));
/* 247 */     if (!this.registriesPostProcessed.contains(Integer.valueOf(factoryId)))
/*     */     {
/*     */       
/* 250 */       processConfigBeanDefinitions((BeanDefinitionRegistry)beanFactory);
/*     */     }
/*     */     
/* 253 */     enhanceConfigurationClasses(beanFactory);
/* 254 */     beanFactory.addBeanPostProcessor((BeanPostProcessor)new ImportAwareBeanPostProcessor((BeanFactory)beanFactory));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void processConfigBeanDefinitions(BeanDefinitionRegistry registry) {
/* 262 */     List<BeanDefinitionHolder> configCandidates = new ArrayList<>();
/* 263 */     String[] candidateNames = registry.getBeanDefinitionNames();
/*     */     
/* 265 */     for (String beanName : candidateNames) {
/* 266 */       BeanDefinition beanDef = registry.getBeanDefinition(beanName);
/* 267 */       if (ConfigurationClassUtils.isFullConfigurationClass(beanDef) || 
/* 268 */         ConfigurationClassUtils.isLiteConfigurationClass(beanDef)) {
/* 269 */         if (this.logger.isDebugEnabled()) {
/* 270 */           this.logger.debug("Bean definition has already been processed as a configuration class: " + beanDef);
/*     */         }
/*     */       }
/* 273 */       else if (ConfigurationClassUtils.checkConfigurationClassCandidate(beanDef, this.metadataReaderFactory)) {
/* 274 */         configCandidates.add(new BeanDefinitionHolder(beanDef, beanName));
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 279 */     if (configCandidates.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 284 */     configCandidates.sort((bd1, bd2) -> {
/*     */           int i1 = ConfigurationClassUtils.getOrder(bd1.getBeanDefinition());
/*     */           
/*     */           int i2 = ConfigurationClassUtils.getOrder(bd2.getBeanDefinition());
/*     */           
/*     */           return Integer.compare(i1, i2);
/*     */         });
/* 291 */     SingletonBeanRegistry sbr = null;
/* 292 */     if (registry instanceof SingletonBeanRegistry) {
/* 293 */       sbr = (SingletonBeanRegistry)registry;
/* 294 */       if (!this.localBeanNameGeneratorSet) {
/* 295 */         BeanNameGenerator generator = (BeanNameGenerator)sbr.getSingleton("org.springframework.context.annotation.internalConfigurationBeanNameGenerator");
/* 296 */         if (generator != null) {
/* 297 */           this.componentScanBeanNameGenerator = generator;
/* 298 */           this.importBeanNameGenerator = generator;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 303 */     if (this.environment == null) {
/* 304 */       this.environment = (Environment)new StandardEnvironment();
/*     */     }
/*     */ 
/*     */     
/* 308 */     ConfigurationClassParser parser = new ConfigurationClassParser(this.metadataReaderFactory, this.problemReporter, this.environment, this.resourceLoader, this.componentScanBeanNameGenerator, registry);
/*     */ 
/*     */ 
/*     */     
/* 312 */     Set<BeanDefinitionHolder> candidates = new LinkedHashSet<>(configCandidates);
/* 313 */     Set<ConfigurationClass> alreadyParsed = new HashSet<>(configCandidates.size());
/*     */     do {
/* 315 */       parser.parse(candidates);
/* 316 */       parser.validate();
/*     */       
/* 318 */       Set<ConfigurationClass> configClasses = new LinkedHashSet<>(parser.getConfigurationClasses());
/* 319 */       configClasses.removeAll(alreadyParsed);
/*     */ 
/*     */       
/* 322 */       if (this.reader == null) {
/* 323 */         this
/*     */           
/* 325 */           .reader = new ConfigurationClassBeanDefinitionReader(registry, this.sourceExtractor, this.resourceLoader, this.environment, this.importBeanNameGenerator, parser.getImportRegistry());
/*     */       }
/* 327 */       this.reader.loadBeanDefinitions(configClasses);
/* 328 */       alreadyParsed.addAll(configClasses);
/*     */       
/* 330 */       candidates.clear();
/* 331 */       if (registry.getBeanDefinitionCount() <= candidateNames.length)
/* 332 */         continue;  String[] newCandidateNames = registry.getBeanDefinitionNames();
/* 333 */       Set<String> oldCandidateNames = new HashSet<>(Arrays.asList(candidateNames));
/* 334 */       Set<String> alreadyParsedClasses = new HashSet<>();
/* 335 */       for (ConfigurationClass configurationClass : alreadyParsed) {
/* 336 */         alreadyParsedClasses.add(configurationClass.getMetadata().getClassName());
/*     */       }
/* 338 */       for (String candidateName : newCandidateNames) {
/* 339 */         if (!oldCandidateNames.contains(candidateName)) {
/* 340 */           BeanDefinition bd = registry.getBeanDefinition(candidateName);
/* 341 */           if (ConfigurationClassUtils.checkConfigurationClassCandidate(bd, this.metadataReaderFactory) && 
/* 342 */             !alreadyParsedClasses.contains(bd.getBeanClassName())) {
/* 343 */             candidates.add(new BeanDefinitionHolder(bd, candidateName));
/*     */           }
/*     */         } 
/*     */       } 
/* 347 */       candidateNames = newCandidateNames;
/*     */     
/*     */     }
/* 350 */     while (!candidates.isEmpty());
/*     */ 
/*     */     
/* 353 */     if (sbr != null && !sbr.containsSingleton(IMPORT_REGISTRY_BEAN_NAME)) {
/* 354 */       sbr.registerSingleton(IMPORT_REGISTRY_BEAN_NAME, parser.getImportRegistry());
/*     */     }
/*     */     
/* 357 */     if (this.metadataReaderFactory instanceof CachingMetadataReaderFactory)
/*     */     {
/*     */       
/* 360 */       ((CachingMetadataReaderFactory)this.metadataReaderFactory).clearCache();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enhanceConfigurationClasses(ConfigurableListableBeanFactory beanFactory) {
/* 371 */     Map<String, AbstractBeanDefinition> configBeanDefs = new LinkedHashMap<>();
/* 372 */     for (String beanName : beanFactory.getBeanDefinitionNames()) {
/* 373 */       BeanDefinition beanDef = beanFactory.getBeanDefinition(beanName);
/* 374 */       if (ConfigurationClassUtils.isFullConfigurationClass(beanDef)) {
/* 375 */         if (!(beanDef instanceof AbstractBeanDefinition)) {
/* 376 */           throw new BeanDefinitionStoreException("Cannot enhance @Configuration bean definition '" + beanName + "' since it is not stored in an AbstractBeanDefinition subclass");
/*     */         }
/*     */         
/* 379 */         if (this.logger.isInfoEnabled() && beanFactory.containsSingleton(beanName)) {
/* 380 */           this.logger.info("Cannot enhance @Configuration bean definition '" + beanName + "' since its singleton instance has been created too early. The typical cause is a non-static @Bean method with a BeanDefinitionRegistryPostProcessor return type: Consider declaring such methods as 'static'.");
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 385 */         configBeanDefs.put(beanName, (AbstractBeanDefinition)beanDef);
/*     */       } 
/*     */     } 
/* 388 */     if (configBeanDefs.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 393 */     ConfigurationClassEnhancer enhancer = new ConfigurationClassEnhancer();
/* 394 */     for (Map.Entry<String, AbstractBeanDefinition> entry : configBeanDefs.entrySet()) {
/* 395 */       AbstractBeanDefinition beanDef = entry.getValue();
/*     */       
/* 397 */       beanDef.setAttribute(AutoProxyUtils.PRESERVE_TARGET_CLASS_ATTRIBUTE, Boolean.TRUE);
/*     */       
/*     */       try {
/* 400 */         Class<?> configClass = beanDef.resolveBeanClass(this.beanClassLoader);
/* 401 */         if (configClass != null) {
/* 402 */           Class<?> enhancedClass = enhancer.enhance(configClass, this.beanClassLoader);
/* 403 */           if (configClass != enhancedClass) {
/* 404 */             if (this.logger.isTraceEnabled()) {
/* 405 */               this.logger.trace(String.format("Replacing bean definition '%s' existing class '%s' with enhanced class '%s'", new Object[] { entry
/* 406 */                       .getKey(), configClass.getName(), enhancedClass.getName() }));
/*     */             }
/* 408 */             beanDef.setBeanClass(enhancedClass);
/*     */           }
/*     */         
/*     */         } 
/* 412 */       } catch (Throwable ex) {
/* 413 */         throw new IllegalStateException("Cannot load configuration class: " + beanDef.getBeanClassName(), ex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static class ImportAwareBeanPostProcessor
/*     */     extends InstantiationAwareBeanPostProcessorAdapter
/*     */   {
/*     */     private final BeanFactory beanFactory;
/*     */     
/*     */     public ImportAwareBeanPostProcessor(BeanFactory beanFactory) {
/* 424 */       this.beanFactory = beanFactory;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public PropertyValues postProcessProperties(@Nullable PropertyValues pvs, Object bean, String beanName) {
/* 431 */       if (bean instanceof ConfigurationClassEnhancer.EnhancedConfiguration) {
/* 432 */         ((ConfigurationClassEnhancer.EnhancedConfiguration)bean).setBeanFactory(this.beanFactory);
/*     */       }
/* 434 */       return pvs;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object postProcessBeforeInitialization(Object bean, String beanName) {
/* 439 */       if (bean instanceof ImportAware) {
/* 440 */         ImportRegistry ir = (ImportRegistry)this.beanFactory.getBean(ConfigurationClassPostProcessor.IMPORT_REGISTRY_BEAN_NAME, ImportRegistry.class);
/* 441 */         AnnotationMetadata importingClass = ir.getImportingClassFor(bean.getClass().getSuperclass().getName());
/* 442 */         if (importingClass != null) {
/* 443 */           ((ImportAware)bean).setImportMetadata(importingClass);
/*     */         }
/*     */       } 
/* 446 */       return bean;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/ConfigurationClassPostProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */