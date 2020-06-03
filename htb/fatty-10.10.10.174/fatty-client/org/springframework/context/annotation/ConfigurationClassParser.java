/*      */ package org.springframework.context.annotation;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.util.ArrayDeque;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.Deque;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.apache.commons.logging.LogFactory;
/*      */ import org.springframework.beans.BeanUtils;
/*      */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*      */ import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
/*      */ import org.springframework.beans.factory.config.BeanDefinition;
/*      */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*      */ import org.springframework.beans.factory.parsing.Location;
/*      */ import org.springframework.beans.factory.parsing.Problem;
/*      */ import org.springframework.beans.factory.parsing.ProblemReporter;
/*      */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*      */ import org.springframework.beans.factory.support.BeanDefinitionReader;
/*      */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*      */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*      */ import org.springframework.core.NestedIOException;
/*      */ import org.springframework.core.OrderComparator;
/*      */ import org.springframework.core.Ordered;
/*      */ import org.springframework.core.annotation.AnnotationAttributes;
/*      */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*      */ import org.springframework.core.annotation.AnnotationUtils;
/*      */ import org.springframework.core.env.CompositePropertySource;
/*      */ import org.springframework.core.env.ConfigurableEnvironment;
/*      */ import org.springframework.core.env.Environment;
/*      */ import org.springframework.core.env.MutablePropertySources;
/*      */ import org.springframework.core.env.PropertySource;
/*      */ import org.springframework.core.io.Resource;
/*      */ import org.springframework.core.io.ResourceLoader;
/*      */ import org.springframework.core.io.support.DefaultPropertySourceFactory;
/*      */ import org.springframework.core.io.support.EncodedResource;
/*      */ import org.springframework.core.io.support.PropertySourceFactory;
/*      */ import org.springframework.core.io.support.ResourcePropertySource;
/*      */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*      */ import org.springframework.core.type.AnnotationMetadata;
/*      */ import org.springframework.core.type.MethodMetadata;
/*      */ import org.springframework.core.type.StandardAnnotationMetadata;
/*      */ import org.springframework.core.type.classreading.MetadataReader;
/*      */ import org.springframework.core.type.classreading.MetadataReaderFactory;
/*      */ import org.springframework.core.type.filter.AssignableTypeFilter;
/*      */ import org.springframework.lang.Nullable;
/*      */ import org.springframework.stereotype.Component;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.CollectionUtils;
/*      */ import org.springframework.util.LinkedMultiValueMap;
/*      */ import org.springframework.util.MultiValueMap;
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
/*      */ class ConfigurationClassParser
/*      */ {
/*  110 */   private static final PropertySourceFactory DEFAULT_PROPERTY_SOURCE_FACTORY = (PropertySourceFactory)new DefaultPropertySourceFactory();
/*      */   static {
/*  112 */     DEFERRED_IMPORT_COMPARATOR = ((o1, o2) -> AnnotationAwareOrderComparator.INSTANCE.compare(o1.getImportSelector(), o2.getImportSelector()));
/*      */   }
/*      */   
/*      */   private static final Comparator<DeferredImportSelectorHolder> DEFERRED_IMPORT_COMPARATOR;
/*  116 */   private final Log logger = LogFactory.getLog(getClass());
/*      */   
/*      */   private final MetadataReaderFactory metadataReaderFactory;
/*      */   
/*      */   private final ProblemReporter problemReporter;
/*      */   
/*      */   private final Environment environment;
/*      */   
/*      */   private final ResourceLoader resourceLoader;
/*      */   
/*      */   private final BeanDefinitionRegistry registry;
/*      */   
/*      */   private final ComponentScanAnnotationParser componentScanParser;
/*      */   
/*      */   private final ConditionEvaluator conditionEvaluator;
/*      */   
/*  132 */   private final Map<ConfigurationClass, ConfigurationClass> configurationClasses = new LinkedHashMap<>();
/*      */   
/*  134 */   private final Map<String, ConfigurationClass> knownSuperclasses = new HashMap<>();
/*      */   
/*  136 */   private final List<String> propertySourceNames = new ArrayList<>();
/*      */   
/*  138 */   private final ImportStack importStack = new ImportStack();
/*      */   
/*  140 */   private final DeferredImportSelectorHandler deferredImportSelectorHandler = new DeferredImportSelectorHandler();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConfigurationClassParser(MetadataReaderFactory metadataReaderFactory, ProblemReporter problemReporter, Environment environment, ResourceLoader resourceLoader, BeanNameGenerator componentScanBeanNameGenerator, BeanDefinitionRegistry registry) {
/*  151 */     this.metadataReaderFactory = metadataReaderFactory;
/*  152 */     this.problemReporter = problemReporter;
/*  153 */     this.environment = environment;
/*  154 */     this.resourceLoader = resourceLoader;
/*  155 */     this.registry = registry;
/*  156 */     this.componentScanParser = new ComponentScanAnnotationParser(environment, resourceLoader, componentScanBeanNameGenerator, registry);
/*      */     
/*  158 */     this.conditionEvaluator = new ConditionEvaluator(registry, environment, resourceLoader);
/*      */   }
/*      */ 
/*      */   
/*      */   public void parse(Set<BeanDefinitionHolder> configCandidates) {
/*  163 */     for (BeanDefinitionHolder holder : configCandidates) {
/*  164 */       BeanDefinition bd = holder.getBeanDefinition();
/*      */       try {
/*  166 */         if (bd instanceof AnnotatedBeanDefinition) {
/*  167 */           parse(((AnnotatedBeanDefinition)bd).getMetadata(), holder.getBeanName()); continue;
/*      */         } 
/*  169 */         if (bd instanceof AbstractBeanDefinition && ((AbstractBeanDefinition)bd).hasBeanClass()) {
/*  170 */           parse(((AbstractBeanDefinition)bd).getBeanClass(), holder.getBeanName());
/*      */           continue;
/*      */         } 
/*  173 */         parse(bd.getBeanClassName(), holder.getBeanName());
/*      */       
/*      */       }
/*  176 */       catch (BeanDefinitionStoreException ex) {
/*  177 */         throw ex;
/*      */       }
/*  179 */       catch (Throwable ex) {
/*  180 */         throw new BeanDefinitionStoreException("Failed to parse configuration class [" + bd
/*  181 */             .getBeanClassName() + "]", ex);
/*      */       } 
/*      */     } 
/*      */     
/*  185 */     this.deferredImportSelectorHandler.process();
/*      */   }
/*      */   
/*      */   protected final void parse(@Nullable String className, String beanName) throws IOException {
/*  189 */     Assert.notNull(className, "No bean class name for configuration class bean definition");
/*  190 */     MetadataReader reader = this.metadataReaderFactory.getMetadataReader(className);
/*  191 */     processConfigurationClass(new ConfigurationClass(reader, beanName));
/*      */   }
/*      */   
/*      */   protected final void parse(Class<?> clazz, String beanName) throws IOException {
/*  195 */     processConfigurationClass(new ConfigurationClass(clazz, beanName));
/*      */   }
/*      */   
/*      */   protected final void parse(AnnotationMetadata metadata, String beanName) throws IOException {
/*  199 */     processConfigurationClass(new ConfigurationClass(metadata, beanName));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void validate() {
/*  207 */     for (ConfigurationClass configClass : this.configurationClasses.keySet()) {
/*  208 */       configClass.validate(this.problemReporter);
/*      */     }
/*      */   }
/*      */   
/*      */   public Set<ConfigurationClass> getConfigurationClasses() {
/*  213 */     return this.configurationClasses.keySet();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void processConfigurationClass(ConfigurationClass configClass) throws IOException {
/*  218 */     if (this.conditionEvaluator.shouldSkip((AnnotatedTypeMetadata)configClass.getMetadata(), ConfigurationCondition.ConfigurationPhase.PARSE_CONFIGURATION)) {
/*      */       return;
/*      */     }
/*      */     
/*  222 */     ConfigurationClass existingClass = this.configurationClasses.get(configClass);
/*  223 */     if (existingClass != null) {
/*  224 */       if (configClass.isImported()) {
/*  225 */         if (existingClass.isImported()) {
/*  226 */           existingClass.mergeImportedBy(configClass);
/*      */         }
/*      */ 
/*      */         
/*      */         return;
/*      */       } 
/*      */ 
/*      */       
/*  234 */       this.configurationClasses.remove(configClass);
/*  235 */       this.knownSuperclasses.values().removeIf(configClass::equals);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  240 */     SourceClass sourceClass = asSourceClass(configClass);
/*      */     do {
/*  242 */       sourceClass = doProcessConfigurationClass(configClass, sourceClass);
/*      */     }
/*  244 */     while (sourceClass != null);
/*      */     
/*  246 */     this.configurationClasses.put(configClass, configClass);
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
/*      */   @Nullable
/*      */   protected final SourceClass doProcessConfigurationClass(ConfigurationClass configClass, SourceClass sourceClass) throws IOException {
/*  261 */     if (configClass.getMetadata().isAnnotated(Component.class.getName()))
/*      */     {
/*  263 */       processMemberClasses(configClass, sourceClass);
/*      */     }
/*      */ 
/*      */     
/*  267 */     for (AnnotationAttributes propertySource : AnnotationConfigUtils.attributesForRepeatable(sourceClass
/*  268 */         .getMetadata(), PropertySources.class, PropertySource.class)) {
/*      */       
/*  270 */       if (this.environment instanceof ConfigurableEnvironment) {
/*  271 */         processPropertySource(propertySource);
/*      */         continue;
/*      */       } 
/*  274 */       this.logger.info("Ignoring @PropertySource annotation on [" + sourceClass.getMetadata().getClassName() + "]. Reason: Environment must implement ConfigurableEnvironment");
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  280 */     Set<AnnotationAttributes> componentScans = AnnotationConfigUtils.attributesForRepeatable(sourceClass
/*  281 */         .getMetadata(), ComponentScans.class, ComponentScan.class);
/*  282 */     if (!componentScans.isEmpty() && 
/*  283 */       !this.conditionEvaluator.shouldSkip((AnnotatedTypeMetadata)sourceClass.getMetadata(), ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN)) {
/*  284 */       for (AnnotationAttributes componentScan : componentScans) {
/*      */ 
/*      */         
/*  287 */         Set<BeanDefinitionHolder> scannedBeanDefinitions = this.componentScanParser.parse(componentScan, sourceClass.getMetadata().getClassName());
/*      */         
/*  289 */         for (BeanDefinitionHolder holder : scannedBeanDefinitions) {
/*  290 */           BeanDefinition bdCand = holder.getBeanDefinition().getOriginatingBeanDefinition();
/*  291 */           if (bdCand == null) {
/*  292 */             bdCand = holder.getBeanDefinition();
/*      */           }
/*  294 */           if (ConfigurationClassUtils.checkConfigurationClassCandidate(bdCand, this.metadataReaderFactory)) {
/*  295 */             parse(bdCand.getBeanClassName(), holder.getBeanName());
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  302 */     processImports(configClass, sourceClass, getImports(sourceClass), true);
/*      */ 
/*      */ 
/*      */     
/*  306 */     AnnotationAttributes importResource = AnnotationConfigUtils.attributesFor((AnnotatedTypeMetadata)sourceClass.getMetadata(), ImportResource.class);
/*  307 */     if (importResource != null) {
/*  308 */       String[] resources = importResource.getStringArray("locations");
/*  309 */       Class<? extends BeanDefinitionReader> readerClass = importResource.getClass("reader");
/*  310 */       for (String resource : resources) {
/*  311 */         String resolvedResource = this.environment.resolveRequiredPlaceholders(resource);
/*  312 */         configClass.addImportedResource(resolvedResource, readerClass);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  317 */     Set<MethodMetadata> beanMethods = retrieveBeanMethodMetadata(sourceClass);
/*  318 */     for (MethodMetadata methodMetadata : beanMethods) {
/*  319 */       configClass.addBeanMethod(new BeanMethod(methodMetadata, configClass));
/*      */     }
/*      */ 
/*      */     
/*  323 */     processInterfaces(configClass, sourceClass);
/*      */ 
/*      */     
/*  326 */     if (sourceClass.getMetadata().hasSuperClass()) {
/*  327 */       String superclass = sourceClass.getMetadata().getSuperClassName();
/*  328 */       if (superclass != null && !superclass.startsWith("java") && 
/*  329 */         !this.knownSuperclasses.containsKey(superclass)) {
/*  330 */         this.knownSuperclasses.put(superclass, configClass);
/*      */         
/*  332 */         return sourceClass.getSuperClass();
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  337 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void processMemberClasses(ConfigurationClass configClass, SourceClass sourceClass) throws IOException {
/*  344 */     Collection<SourceClass> memberClasses = sourceClass.getMemberClasses();
/*  345 */     if (!memberClasses.isEmpty()) {
/*  346 */       List<SourceClass> candidates = new ArrayList<>(memberClasses.size());
/*  347 */       for (SourceClass memberClass : memberClasses) {
/*  348 */         if (ConfigurationClassUtils.isConfigurationCandidate(memberClass.getMetadata()) && 
/*  349 */           !memberClass.getMetadata().getClassName().equals(configClass.getMetadata().getClassName())) {
/*  350 */           candidates.add(memberClass);
/*      */         }
/*      */       } 
/*  353 */       OrderComparator.sort(candidates);
/*  354 */       for (SourceClass candidate : candidates) {
/*  355 */         if (this.importStack.contains(configClass)) {
/*  356 */           this.problemReporter.error(new CircularImportProblem(configClass, this.importStack));
/*      */           continue;
/*      */         } 
/*  359 */         this.importStack.push(configClass);
/*      */         try {
/*  361 */           processConfigurationClass(candidate.asConfigClass(configClass));
/*      */         } finally {
/*      */           
/*  364 */           this.importStack.pop();
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void processInterfaces(ConfigurationClass configClass, SourceClass sourceClass) throws IOException {
/*  375 */     for (SourceClass ifc : sourceClass.getInterfaces()) {
/*  376 */       Set<MethodMetadata> beanMethods = retrieveBeanMethodMetadata(ifc);
/*  377 */       for (MethodMetadata methodMetadata : beanMethods) {
/*  378 */         if (!methodMetadata.isAbstract())
/*      */         {
/*  380 */           configClass.addBeanMethod(new BeanMethod(methodMetadata, configClass));
/*      */         }
/*      */       } 
/*  383 */       processInterfaces(configClass, ifc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Set<MethodMetadata> retrieveBeanMethodMetadata(SourceClass sourceClass) {
/*  391 */     AnnotationMetadata original = sourceClass.getMetadata();
/*  392 */     Set<MethodMetadata> beanMethods = original.getAnnotatedMethods(Bean.class.getName());
/*  393 */     if (beanMethods.size() > 1 && original instanceof StandardAnnotationMetadata) {
/*      */       
/*      */       try {
/*      */ 
/*      */ 
/*      */         
/*  399 */         AnnotationMetadata asm = this.metadataReaderFactory.getMetadataReader(original.getClassName()).getAnnotationMetadata();
/*  400 */         Set<MethodMetadata> asmMethods = asm.getAnnotatedMethods(Bean.class.getName());
/*  401 */         if (asmMethods.size() >= beanMethods.size()) {
/*  402 */           Set<MethodMetadata> selectedMethods = new LinkedHashSet<>(asmMethods.size());
/*  403 */           for (MethodMetadata asmMethod : asmMethods) {
/*  404 */             for (MethodMetadata beanMethod : beanMethods) {
/*  405 */               if (beanMethod.getMethodName().equals(asmMethod.getMethodName())) {
/*  406 */                 selectedMethods.add(beanMethod);
/*      */               }
/*      */             } 
/*      */           } 
/*      */           
/*  411 */           if (selectedMethods.size() == beanMethods.size())
/*      */           {
/*  413 */             beanMethods = selectedMethods;
/*      */           }
/*      */         }
/*      */       
/*  417 */       } catch (IOException ex) {
/*  418 */         this.logger.debug("Failed to read class file via ASM for determining @Bean method order", ex);
/*      */       } 
/*      */     }
/*      */     
/*  422 */     return beanMethods;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void processPropertySource(AnnotationAttributes propertySource) throws IOException {
/*  432 */     String name = propertySource.getString("name");
/*  433 */     if (!StringUtils.hasLength(name)) {
/*  434 */       name = null;
/*      */     }
/*  436 */     String encoding = propertySource.getString("encoding");
/*  437 */     if (!StringUtils.hasLength(encoding)) {
/*  438 */       encoding = null;
/*      */     }
/*  440 */     String[] locations = propertySource.getStringArray("value");
/*  441 */     Assert.isTrue((locations.length > 0), "At least one @PropertySource(value) location is required");
/*  442 */     boolean ignoreResourceNotFound = propertySource.getBoolean("ignoreResourceNotFound");
/*      */     
/*  444 */     Class<? extends PropertySourceFactory> factoryClass = propertySource.getClass("factory");
/*      */     
/*  446 */     PropertySourceFactory factory = (factoryClass == PropertySourceFactory.class) ? DEFAULT_PROPERTY_SOURCE_FACTORY : (PropertySourceFactory)BeanUtils.instantiateClass(factoryClass);
/*      */     
/*  448 */     for (String location : locations) {
/*      */       try {
/*  450 */         String resolvedLocation = this.environment.resolveRequiredPlaceholders(location);
/*  451 */         Resource resource = this.resourceLoader.getResource(resolvedLocation);
/*  452 */         addPropertySource(factory.createPropertySource(name, new EncodedResource(resource, encoding)));
/*      */       }
/*  454 */       catch (IllegalArgumentException|java.io.FileNotFoundException|java.net.UnknownHostException ex) {
/*      */         
/*  456 */         if (ignoreResourceNotFound) {
/*  457 */           if (this.logger.isInfoEnabled()) {
/*  458 */             this.logger.info("Properties location [" + location + "] not resolvable: " + ex.getMessage());
/*      */           }
/*      */         } else {
/*      */           
/*  462 */           throw ex;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void addPropertySource(PropertySource<?> propertySource) {
/*  469 */     String name = propertySource.getName();
/*  470 */     MutablePropertySources propertySources = ((ConfigurableEnvironment)this.environment).getPropertySources();
/*      */     
/*  472 */     if (this.propertySourceNames.contains(name)) {
/*      */       
/*  474 */       PropertySource<?> existing = propertySources.get(name);
/*  475 */       if (existing != null) {
/*      */         
/*  477 */         PropertySource<?> newSource = (propertySource instanceof ResourcePropertySource) ? (PropertySource<?>)((ResourcePropertySource)propertySource).withResourceName() : propertySource;
/*  478 */         if (existing instanceof CompositePropertySource) {
/*  479 */           ((CompositePropertySource)existing).addFirstPropertySource(newSource);
/*      */         } else {
/*      */           ResourcePropertySource resourcePropertySource;
/*  482 */           if (existing instanceof ResourcePropertySource) {
/*  483 */             resourcePropertySource = ((ResourcePropertySource)existing).withResourceName();
/*      */           }
/*  485 */           CompositePropertySource composite = new CompositePropertySource(name);
/*  486 */           composite.addPropertySource(newSource);
/*  487 */           composite.addPropertySource((PropertySource)resourcePropertySource);
/*  488 */           propertySources.replace(name, (PropertySource)composite);
/*      */         } 
/*      */         
/*      */         return;
/*      */       } 
/*      */     } 
/*  494 */     if (this.propertySourceNames.isEmpty()) {
/*  495 */       propertySources.addLast(propertySource);
/*      */     } else {
/*      */       
/*  498 */       String firstProcessed = this.propertySourceNames.get(this.propertySourceNames.size() - 1);
/*  499 */       propertySources.addBefore(firstProcessed, propertySource);
/*      */     } 
/*  501 */     this.propertySourceNames.add(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Set<SourceClass> getImports(SourceClass sourceClass) throws IOException {
/*  509 */     Set<SourceClass> imports = new LinkedHashSet<>();
/*  510 */     Set<SourceClass> visited = new LinkedHashSet<>();
/*  511 */     collectImports(sourceClass, imports, visited);
/*  512 */     return imports;
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
/*      */   private void collectImports(SourceClass sourceClass, Set<SourceClass> imports, Set<SourceClass> visited) throws IOException {
/*  531 */     if (visited.add(sourceClass)) {
/*  532 */       for (SourceClass annotation : sourceClass.getAnnotations()) {
/*  533 */         String annName = annotation.getMetadata().getClassName();
/*  534 */         if (!annName.equals(Import.class.getName())) {
/*  535 */           collectImports(annotation, imports, visited);
/*      */         }
/*      */       } 
/*  538 */       imports.addAll(sourceClass.getAnnotationAttributes(Import.class.getName(), "value"));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void processImports(ConfigurationClass configClass, SourceClass currentSourceClass, Collection<SourceClass> importCandidates, boolean checkForCircularImports) {
/*  545 */     if (importCandidates.isEmpty()) {
/*      */       return;
/*      */     }
/*      */     
/*  549 */     if (checkForCircularImports && isChainedImportOnStack(configClass)) {
/*  550 */       this.problemReporter.error(new CircularImportProblem(configClass, this.importStack));
/*      */     } else {
/*      */       
/*  553 */       this.importStack.push(configClass);
/*      */       try {
/*  555 */         for (SourceClass candidate : importCandidates) {
/*  556 */           if (candidate.isAssignable(ImportSelector.class)) {
/*      */             
/*  558 */             Class<?> candidateClass = candidate.loadClass();
/*  559 */             ImportSelector selector = (ImportSelector)BeanUtils.instantiateClass(candidateClass, ImportSelector.class);
/*  560 */             ParserStrategyUtils.invokeAwareMethods(selector, this.environment, this.resourceLoader, this.registry);
/*      */             
/*  562 */             if (selector instanceof DeferredImportSelector) {
/*  563 */               this.deferredImportSelectorHandler.handle(configClass, (DeferredImportSelector)selector);
/*      */               continue;
/*      */             } 
/*  566 */             String[] importClassNames = selector.selectImports(currentSourceClass.getMetadata());
/*  567 */             Collection<SourceClass> importSourceClasses = asSourceClasses(importClassNames);
/*  568 */             processImports(configClass, currentSourceClass, importSourceClasses, false);
/*      */             continue;
/*      */           } 
/*  571 */           if (candidate.isAssignable(ImportBeanDefinitionRegistrar.class)) {
/*      */ 
/*      */             
/*  574 */             Class<?> candidateClass = candidate.loadClass();
/*      */             
/*  576 */             ImportBeanDefinitionRegistrar registrar = (ImportBeanDefinitionRegistrar)BeanUtils.instantiateClass(candidateClass, ImportBeanDefinitionRegistrar.class);
/*  577 */             ParserStrategyUtils.invokeAwareMethods(registrar, this.environment, this.resourceLoader, this.registry);
/*      */             
/*  579 */             configClass.addImportBeanDefinitionRegistrar(registrar, currentSourceClass.getMetadata());
/*      */             
/*      */             continue;
/*      */           } 
/*      */           
/*  584 */           this.importStack.registerImport(currentSourceClass
/*  585 */               .getMetadata(), candidate.getMetadata().getClassName());
/*  586 */           processConfigurationClass(candidate.asConfigClass(configClass));
/*      */         }
/*      */       
/*      */       }
/*  590 */       catch (BeanDefinitionStoreException ex) {
/*  591 */         throw ex;
/*      */       }
/*  593 */       catch (Throwable ex) {
/*  594 */         throw new BeanDefinitionStoreException("Failed to process import candidates for configuration class [" + configClass
/*      */             
/*  596 */             .getMetadata().getClassName() + "]", ex);
/*      */       } finally {
/*      */         
/*  599 */         this.importStack.pop();
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean isChainedImportOnStack(ConfigurationClass configClass) {
/*  605 */     if (this.importStack.contains(configClass)) {
/*  606 */       String configClassName = configClass.getMetadata().getClassName();
/*  607 */       AnnotationMetadata importingClass = this.importStack.getImportingClassFor(configClassName);
/*  608 */       while (importingClass != null) {
/*  609 */         if (configClassName.equals(importingClass.getClassName())) {
/*  610 */           return true;
/*      */         }
/*  612 */         importingClass = this.importStack.getImportingClassFor(importingClass.getClassName());
/*      */       } 
/*      */     } 
/*  615 */     return false;
/*      */   }
/*      */   
/*      */   ImportRegistry getImportRegistry() {
/*  619 */     return this.importStack;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private SourceClass asSourceClass(ConfigurationClass configurationClass) throws IOException {
/*  627 */     AnnotationMetadata metadata = configurationClass.getMetadata();
/*  628 */     if (metadata instanceof StandardAnnotationMetadata) {
/*  629 */       return asSourceClass(((StandardAnnotationMetadata)metadata).getIntrospectedClass());
/*      */     }
/*  631 */     return asSourceClass(metadata.getClassName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   SourceClass asSourceClass(@Nullable Class<?> classType) throws IOException {
/*  638 */     if (classType == null) {
/*  639 */       return new SourceClass(Object.class);
/*      */     }
/*      */ 
/*      */     
/*      */     try {
/*  644 */       for (Annotation ann : classType.getAnnotations()) {
/*  645 */         AnnotationUtils.validateAnnotation(ann);
/*      */       }
/*  647 */       return new SourceClass(classType);
/*      */     }
/*  649 */     catch (Throwable ex) {
/*      */       
/*  651 */       return asSourceClass(classType.getName());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Collection<SourceClass> asSourceClasses(String... classNames) throws IOException {
/*  659 */     List<SourceClass> annotatedClasses = new ArrayList<>(classNames.length);
/*  660 */     for (String className : classNames) {
/*  661 */       annotatedClasses.add(asSourceClass(className));
/*      */     }
/*  663 */     return annotatedClasses;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   SourceClass asSourceClass(@Nullable String className) throws IOException {
/*  670 */     if (className == null) {
/*  671 */       return new SourceClass(Object.class);
/*      */     }
/*  673 */     if (className.startsWith("java")) {
/*      */       
/*      */       try {
/*  676 */         return new SourceClass(ClassUtils.forName(className, this.resourceLoader.getClassLoader()));
/*      */       }
/*  678 */       catch (ClassNotFoundException ex) {
/*  679 */         throw new NestedIOException("Failed to load class [" + className + "]", ex);
/*      */       } 
/*      */     }
/*  682 */     return new SourceClass(this.metadataReaderFactory.getMetadataReader(className));
/*      */   }
/*      */   
/*      */   private static class ImportStack
/*      */     extends ArrayDeque<ConfigurationClass>
/*      */     implements ImportRegistry
/*      */   {
/*  689 */     private final MultiValueMap<String, AnnotationMetadata> imports = (MultiValueMap<String, AnnotationMetadata>)new LinkedMultiValueMap();
/*      */     
/*      */     public void registerImport(AnnotationMetadata importingClass, String importedClass) {
/*  692 */       this.imports.add(importedClass, importingClass);
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public AnnotationMetadata getImportingClassFor(String importedClass) {
/*  698 */       return (AnnotationMetadata)CollectionUtils.lastElement((List)this.imports.get(importedClass));
/*      */     }
/*      */ 
/*      */     
/*      */     public void removeImportingClass(String importingClass) {
/*  703 */       for (List<AnnotationMetadata> list : (Iterable<List<AnnotationMetadata>>)this.imports.values()) {
/*  704 */         Iterator<AnnotationMetadata> iterator; for (iterator = list.iterator(); iterator.hasNext();) {
/*  705 */           if (((AnnotationMetadata)iterator.next()).getClassName().equals(importingClass)) {
/*  706 */             iterator.remove();
/*      */           }
/*      */         } 
/*      */       } 
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
/*      */     public String toString() {
/*  724 */       StringBuilder builder = new StringBuilder("[");
/*  725 */       Iterator<ConfigurationClass> iterator = iterator();
/*  726 */       while (iterator.hasNext()) {
/*  727 */         builder.append(((ConfigurationClass)iterator.next()).getSimpleName());
/*  728 */         if (iterator.hasNext()) {
/*  729 */           builder.append("->");
/*      */         }
/*      */       } 
/*  732 */       return builder.append(']').toString();
/*      */     }
/*      */     
/*      */     private ImportStack() {} }
/*      */   
/*      */   private class DeferredImportSelectorHandler {
/*      */     @Nullable
/*  739 */     private List<ConfigurationClassParser.DeferredImportSelectorHolder> deferredImportSelectors = new ArrayList<>();
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
/*      */     public void handle(ConfigurationClass configClass, DeferredImportSelector importSelector) {
/*  751 */       ConfigurationClassParser.DeferredImportSelectorHolder holder = new ConfigurationClassParser.DeferredImportSelectorHolder(configClass, importSelector);
/*      */       
/*  753 */       if (this.deferredImportSelectors == null) {
/*  754 */         ConfigurationClassParser.DeferredImportSelectorGroupingHandler handler = new ConfigurationClassParser.DeferredImportSelectorGroupingHandler();
/*  755 */         handler.register(holder);
/*  756 */         handler.processGroupImports();
/*      */       } else {
/*      */         
/*  759 */         this.deferredImportSelectors.add(holder);
/*      */       } 
/*      */     }
/*      */     
/*      */     public void process() {
/*  764 */       List<ConfigurationClassParser.DeferredImportSelectorHolder> deferredImports = this.deferredImportSelectors;
/*  765 */       this.deferredImportSelectors = null;
/*      */       try {
/*  767 */         if (deferredImports != null) {
/*  768 */           ConfigurationClassParser.DeferredImportSelectorGroupingHandler handler = new ConfigurationClassParser.DeferredImportSelectorGroupingHandler();
/*  769 */           deferredImports.sort(ConfigurationClassParser.DEFERRED_IMPORT_COMPARATOR);
/*  770 */           deferredImports.forEach(handler::register);
/*  771 */           handler.processGroupImports();
/*      */         } 
/*      */       } finally {
/*      */         
/*  775 */         this.deferredImportSelectors = new ArrayList<>();
/*      */       } 
/*      */     }
/*      */     
/*      */     private DeferredImportSelectorHandler() {}
/*      */   }
/*      */   
/*      */   private class DeferredImportSelectorGroupingHandler
/*      */   {
/*  784 */     private final Map<Object, ConfigurationClassParser.DeferredImportSelectorGrouping> groupings = new LinkedHashMap<>();
/*      */     
/*  786 */     private final Map<AnnotationMetadata, ConfigurationClass> configurationClasses = new HashMap<>();
/*      */ 
/*      */     
/*      */     public void register(ConfigurationClassParser.DeferredImportSelectorHolder deferredImport) {
/*  790 */       Class<? extends DeferredImportSelector.Group> group = deferredImport.getImportSelector().getImportGroup();
/*  791 */       ConfigurationClassParser.DeferredImportSelectorGrouping grouping = this.groupings.computeIfAbsent((group != null) ? group : deferredImport, key -> new ConfigurationClassParser.DeferredImportSelectorGrouping(createGroup(group)));
/*      */ 
/*      */       
/*  794 */       grouping.add(deferredImport);
/*  795 */       this.configurationClasses.put(deferredImport.getConfigurationClass().getMetadata(), deferredImport
/*  796 */           .getConfigurationClass());
/*      */     }
/*      */     
/*      */     public void processGroupImports() {
/*  800 */       for (Iterator<ConfigurationClassParser.DeferredImportSelectorGrouping> iterator = this.groupings.values().iterator(); iterator.hasNext(); ) { ConfigurationClassParser.DeferredImportSelectorGrouping grouping = iterator.next();
/*  801 */         grouping.getImports().forEach(entry -> {
/*      */               ConfigurationClass configurationClass = this.configurationClasses.get(entry.getMetadata());
/*      */ 
/*      */ 
/*      */               
/*      */               try {
/*      */                 ConfigurationClassParser.this.processImports(configurationClass, ConfigurationClassParser.this.asSourceClass(configurationClass), ConfigurationClassParser.this.asSourceClasses(new String[] { entry.getImportClassName() }, ), false);
/*  808 */               } catch (BeanDefinitionStoreException ex) {
/*      */                 
/*      */                 throw ex;
/*  811 */               } catch (Throwable ex) {
/*      */                 throw new BeanDefinitionStoreException("Failed to process import candidates for configuration class [" + configurationClass.getMetadata().getClassName() + "]", ex);
/*      */               } 
/*      */             }); }
/*      */     
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private DeferredImportSelector.Group createGroup(@Nullable Class<? extends DeferredImportSelector.Group> type) {
/*  821 */       Class<? extends DeferredImportSelector.Group> effectiveType = (type != null) ? type : (Class)ConfigurationClassParser.DefaultDeferredImportSelectorGroup.class;
/*      */       
/*  823 */       DeferredImportSelector.Group group = (DeferredImportSelector.Group)BeanUtils.instantiateClass(effectiveType);
/*  824 */       ParserStrategyUtils.invokeAwareMethods(group, ConfigurationClassParser.this
/*  825 */           .environment, ConfigurationClassParser.this
/*  826 */           .resourceLoader, ConfigurationClassParser.this
/*  827 */           .registry);
/*  828 */       return group;
/*      */     }
/*      */ 
/*      */     
/*      */     private DeferredImportSelectorGroupingHandler() {}
/*      */   }
/*      */   
/*      */   private static class DeferredImportSelectorHolder
/*      */   {
/*      */     private final ConfigurationClass configurationClass;
/*      */     private final DeferredImportSelector importSelector;
/*      */     
/*      */     public DeferredImportSelectorHolder(ConfigurationClass configClass, DeferredImportSelector selector) {
/*  841 */       this.configurationClass = configClass;
/*  842 */       this.importSelector = selector;
/*      */     }
/*      */     
/*      */     public ConfigurationClass getConfigurationClass() {
/*  846 */       return this.configurationClass;
/*      */     }
/*      */     
/*      */     public DeferredImportSelector getImportSelector() {
/*  850 */       return this.importSelector;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class DeferredImportSelectorGrouping
/*      */   {
/*      */     private final DeferredImportSelector.Group group;
/*      */     
/*  859 */     private final List<ConfigurationClassParser.DeferredImportSelectorHolder> deferredImports = new ArrayList<>();
/*      */     
/*      */     DeferredImportSelectorGrouping(DeferredImportSelector.Group group) {
/*  862 */       this.group = group;
/*      */     }
/*      */     
/*      */     public void add(ConfigurationClassParser.DeferredImportSelectorHolder deferredImport) {
/*  866 */       this.deferredImports.add(deferredImport);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Iterable<DeferredImportSelector.Group.Entry> getImports() {
/*  874 */       for (ConfigurationClassParser.DeferredImportSelectorHolder deferredImport : this.deferredImports) {
/*  875 */         this.group.process(deferredImport.getConfigurationClass().getMetadata(), deferredImport
/*  876 */             .getImportSelector());
/*      */       }
/*  878 */       return this.group.selectImports();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class DefaultDeferredImportSelectorGroup
/*      */     implements DeferredImportSelector.Group
/*      */   {
/*  885 */     private final List<DeferredImportSelector.Group.Entry> imports = new ArrayList<>();
/*      */ 
/*      */     
/*      */     public void process(AnnotationMetadata metadata, DeferredImportSelector selector) {
/*  889 */       for (String importClassName : selector.selectImports(metadata)) {
/*  890 */         this.imports.add(new DeferredImportSelector.Group.Entry(metadata, importClassName));
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterable<DeferredImportSelector.Group.Entry> selectImports() {
/*  896 */       return this.imports;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private class SourceClass
/*      */     implements Ordered
/*      */   {
/*      */     private final Object source;
/*      */ 
/*      */     
/*      */     private final AnnotationMetadata metadata;
/*      */ 
/*      */     
/*      */     public SourceClass(Object source) {
/*  912 */       this.source = source;
/*  913 */       if (source instanceof Class) {
/*  914 */         this.metadata = (AnnotationMetadata)new StandardAnnotationMetadata((Class)source, true);
/*      */       } else {
/*      */         
/*  917 */         this.metadata = ((MetadataReader)source).getAnnotationMetadata();
/*      */       } 
/*      */     }
/*      */     
/*      */     public final AnnotationMetadata getMetadata() {
/*  922 */       return this.metadata;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getOrder() {
/*  927 */       Integer order = ConfigurationClassUtils.getOrder(this.metadata);
/*  928 */       return (order != null) ? order.intValue() : Integer.MAX_VALUE;
/*      */     }
/*      */     
/*      */     public Class<?> loadClass() throws ClassNotFoundException {
/*  932 */       if (this.source instanceof Class) {
/*  933 */         return (Class)this.source;
/*      */       }
/*  935 */       String className = ((MetadataReader)this.source).getClassMetadata().getClassName();
/*  936 */       return ClassUtils.forName(className, ConfigurationClassParser.this.resourceLoader.getClassLoader());
/*      */     }
/*      */     
/*      */     public boolean isAssignable(Class<?> clazz) throws IOException {
/*  940 */       if (this.source instanceof Class) {
/*  941 */         return clazz.isAssignableFrom((Class)this.source);
/*      */       }
/*  943 */       return (new AssignableTypeFilter(clazz)).match((MetadataReader)this.source, ConfigurationClassParser.this.metadataReaderFactory);
/*      */     }
/*      */     
/*      */     public ConfigurationClass asConfigClass(ConfigurationClass importedBy) {
/*  947 */       if (this.source instanceof Class) {
/*  948 */         return new ConfigurationClass((Class)this.source, importedBy);
/*      */       }
/*  950 */       return new ConfigurationClass((MetadataReader)this.source, importedBy);
/*      */     }
/*      */     
/*      */     public Collection<SourceClass> getMemberClasses() throws IOException {
/*  954 */       Object sourceToProcess = this.source;
/*  955 */       if (sourceToProcess instanceof Class) {
/*  956 */         Class<?> sourceClass = (Class)sourceToProcess;
/*      */         try {
/*  958 */           Class<?>[] declaredClasses = sourceClass.getDeclaredClasses();
/*  959 */           List<SourceClass> list = new ArrayList<>(declaredClasses.length);
/*  960 */           for (Class<?> declaredClass : declaredClasses) {
/*  961 */             list.add(ConfigurationClassParser.this.asSourceClass(declaredClass));
/*      */           }
/*  963 */           return list;
/*      */         }
/*  965 */         catch (NoClassDefFoundError err) {
/*      */ 
/*      */           
/*  968 */           sourceToProcess = ConfigurationClassParser.this.metadataReaderFactory.getMetadataReader(sourceClass.getName());
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  973 */       MetadataReader sourceReader = (MetadataReader)sourceToProcess;
/*  974 */       String[] memberClassNames = sourceReader.getClassMetadata().getMemberClassNames();
/*  975 */       List<SourceClass> members = new ArrayList<>(memberClassNames.length);
/*  976 */       for (String memberClassName : memberClassNames) {
/*      */         try {
/*  978 */           members.add(ConfigurationClassParser.this.asSourceClass(memberClassName));
/*      */         }
/*  980 */         catch (IOException ex) {
/*      */           
/*  982 */           if (ConfigurationClassParser.this.logger.isDebugEnabled()) {
/*  983 */             ConfigurationClassParser.this.logger.debug("Failed to resolve member class [" + memberClassName + "] - not considering it as a configuration class candidate");
/*      */           }
/*      */         } 
/*      */       } 
/*      */       
/*  988 */       return members;
/*      */     }
/*      */     
/*      */     public SourceClass getSuperClass() throws IOException {
/*  992 */       if (this.source instanceof Class) {
/*  993 */         return ConfigurationClassParser.this.asSourceClass(((Class)this.source).getSuperclass());
/*      */       }
/*  995 */       return ConfigurationClassParser.this.asSourceClass(((MetadataReader)this.source).getClassMetadata().getSuperClassName());
/*      */     }
/*      */     
/*      */     public Set<SourceClass> getInterfaces() throws IOException {
/*  999 */       Set<SourceClass> result = new LinkedHashSet<>();
/* 1000 */       if (this.source instanceof Class) {
/* 1001 */         Class<?> sourceClass = (Class)this.source;
/* 1002 */         for (Class<?> ifcClass : sourceClass.getInterfaces()) {
/* 1003 */           result.add(ConfigurationClassParser.this.asSourceClass(ifcClass));
/*      */         }
/*      */       } else {
/*      */         
/* 1007 */         for (String className : this.metadata.getInterfaceNames()) {
/* 1008 */           result.add(ConfigurationClassParser.this.asSourceClass(className));
/*      */         }
/*      */       } 
/* 1011 */       return result;
/*      */     }
/*      */     
/*      */     public Set<SourceClass> getAnnotations() {
/* 1015 */       Set<SourceClass> result = new LinkedHashSet<>();
/* 1016 */       if (this.source instanceof Class) {
/* 1017 */         Class<?> sourceClass = (Class)this.source;
/* 1018 */         for (Annotation ann : sourceClass.getAnnotations()) {
/* 1019 */           Class<?> annType = ann.annotationType();
/* 1020 */           if (!annType.getName().startsWith("java")) {
/*      */             try {
/* 1022 */               result.add(ConfigurationClassParser.this.asSourceClass(annType));
/*      */             }
/* 1024 */             catch (Throwable throwable) {}
/*      */           
/*      */           }
/*      */         }
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 1032 */         for (String className : this.metadata.getAnnotationTypes()) {
/* 1033 */           if (!className.startsWith("java")) {
/*      */             try {
/* 1035 */               result.add(getRelated(className));
/*      */             }
/* 1037 */             catch (Throwable throwable) {}
/*      */           }
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1044 */       return result;
/*      */     }
/*      */     
/*      */     public Collection<SourceClass> getAnnotationAttributes(String annType, String attribute) throws IOException {
/* 1048 */       Map<String, Object> annotationAttributes = this.metadata.getAnnotationAttributes(annType, true);
/* 1049 */       if (annotationAttributes == null || !annotationAttributes.containsKey(attribute)) {
/* 1050 */         return Collections.emptySet();
/*      */       }
/* 1052 */       String[] classNames = (String[])annotationAttributes.get(attribute);
/* 1053 */       Set<SourceClass> result = new LinkedHashSet<>();
/* 1054 */       for (String className : classNames) {
/* 1055 */         result.add(getRelated(className));
/*      */       }
/* 1057 */       return result;
/*      */     }
/*      */     
/*      */     private SourceClass getRelated(String className) throws IOException {
/* 1061 */       if (this.source instanceof Class) {
/*      */         try {
/* 1063 */           Class<?> clazz = ClassUtils.forName(className, ((Class)this.source).getClassLoader());
/* 1064 */           return ConfigurationClassParser.this.asSourceClass(clazz);
/*      */         }
/* 1066 */         catch (ClassNotFoundException ex) {
/*      */           
/* 1068 */           if (className.startsWith("java")) {
/* 1069 */             throw new NestedIOException("Failed to load class [" + className + "]", ex);
/*      */           }
/* 1071 */           return new SourceClass(ConfigurationClassParser.this.metadataReaderFactory.getMetadataReader(className));
/*      */         } 
/*      */       }
/* 1074 */       return ConfigurationClassParser.this.asSourceClass(className);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object other) {
/* 1079 */       return (this == other || (other instanceof SourceClass && this.metadata
/* 1080 */         .getClassName().equals(((SourceClass)other).metadata.getClassName())));
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1085 */       return this.metadata.getClassName().hashCode();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1090 */       return this.metadata.getClassName();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class CircularImportProblem
/*      */     extends Problem
/*      */   {
/*      */     public CircularImportProblem(ConfigurationClass attemptedImport, Deque<ConfigurationClass> importStack) {
/* 1101 */       super(String.format("A circular @Import has been detected: Illegal attempt by @Configuration class '%s' to import class '%s' as '%s' is already present in the current import stack %s", new Object[] { ((ConfigurationClass)importStack
/*      */               
/* 1103 */               .element()).getSimpleName(), attemptedImport
/* 1104 */               .getSimpleName(), attemptedImport.getSimpleName(), importStack
/* 1105 */             }), new Location(((ConfigurationClass)importStack.element()).getResource(), attemptedImport.getMetadata()));
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/ConfigurationClassParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */