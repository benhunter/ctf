/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
/*     */ import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
/*     */ import org.springframework.beans.factory.annotation.Autowire;
/*     */ import org.springframework.beans.factory.annotation.RequiredAnnotationBeanPostProcessor;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.groovy.GroovyBeanDefinitionReader;
/*     */ import org.springframework.beans.factory.parsing.SourceExtractor;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinitionReader;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionReader;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.core.type.MethodMetadata;
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
/*     */ class ConfigurationClassBeanDefinitionReader
/*     */ {
/*  73 */   private static final Log logger = LogFactory.getLog(ConfigurationClassBeanDefinitionReader.class);
/*     */   
/*  75 */   private static final ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();
/*     */ 
/*     */   
/*     */   private final BeanDefinitionRegistry registry;
/*     */ 
/*     */   
/*     */   private final SourceExtractor sourceExtractor;
/*     */ 
/*     */   
/*     */   private final ResourceLoader resourceLoader;
/*     */ 
/*     */   
/*     */   private final Environment environment;
/*     */ 
/*     */   
/*     */   private final BeanNameGenerator importBeanNameGenerator;
/*     */ 
/*     */   
/*     */   private final ImportRegistry importRegistry;
/*     */ 
/*     */   
/*     */   private final ConditionEvaluator conditionEvaluator;
/*     */ 
/*     */   
/*     */   ConfigurationClassBeanDefinitionReader(BeanDefinitionRegistry registry, SourceExtractor sourceExtractor, ResourceLoader resourceLoader, Environment environment, BeanNameGenerator importBeanNameGenerator, ImportRegistry importRegistry) {
/* 100 */     this.registry = registry;
/* 101 */     this.sourceExtractor = sourceExtractor;
/* 102 */     this.resourceLoader = resourceLoader;
/* 103 */     this.environment = environment;
/* 104 */     this.importBeanNameGenerator = importBeanNameGenerator;
/* 105 */     this.importRegistry = importRegistry;
/* 106 */     this.conditionEvaluator = new ConditionEvaluator(registry, environment, resourceLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadBeanDefinitions(Set<ConfigurationClass> configurationModel) {
/* 115 */     TrackedConditionEvaluator trackedConditionEvaluator = new TrackedConditionEvaluator();
/* 116 */     for (ConfigurationClass configClass : configurationModel) {
/* 117 */       loadBeanDefinitionsForConfigurationClass(configClass, trackedConditionEvaluator);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void loadBeanDefinitionsForConfigurationClass(ConfigurationClass configClass, TrackedConditionEvaluator trackedConditionEvaluator) {
/* 128 */     if (trackedConditionEvaluator.shouldSkip(configClass)) {
/* 129 */       String beanName = configClass.getBeanName();
/* 130 */       if (StringUtils.hasLength(beanName) && this.registry.containsBeanDefinition(beanName)) {
/* 131 */         this.registry.removeBeanDefinition(beanName);
/*     */       }
/* 133 */       this.importRegistry.removeImportingClass(configClass.getMetadata().getClassName());
/*     */       
/*     */       return;
/*     */     } 
/* 137 */     if (configClass.isImported()) {
/* 138 */       registerBeanDefinitionForImportedConfigurationClass(configClass);
/*     */     }
/* 140 */     for (BeanMethod beanMethod : configClass.getBeanMethods()) {
/* 141 */       loadBeanDefinitionsForBeanMethod(beanMethod);
/*     */     }
/*     */     
/* 144 */     loadBeanDefinitionsFromImportedResources(configClass.getImportedResources());
/* 145 */     loadBeanDefinitionsFromRegistrars(configClass.getImportBeanDefinitionRegistrars());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void registerBeanDefinitionForImportedConfigurationClass(ConfigurationClass configClass) {
/* 152 */     AnnotationMetadata metadata = configClass.getMetadata();
/* 153 */     AnnotatedGenericBeanDefinition configBeanDef = new AnnotatedGenericBeanDefinition(metadata);
/*     */     
/* 155 */     ScopeMetadata scopeMetadata = scopeMetadataResolver.resolveScopeMetadata((BeanDefinition)configBeanDef);
/* 156 */     configBeanDef.setScope(scopeMetadata.getScopeName());
/* 157 */     String configBeanName = this.importBeanNameGenerator.generateBeanName((BeanDefinition)configBeanDef, this.registry);
/* 158 */     AnnotationConfigUtils.processCommonDefinitionAnnotations((AnnotatedBeanDefinition)configBeanDef, (AnnotatedTypeMetadata)metadata);
/*     */     
/* 160 */     BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder((BeanDefinition)configBeanDef, configBeanName);
/* 161 */     definitionHolder = AnnotationConfigUtils.applyScopedProxyMode(scopeMetadata, definitionHolder, this.registry);
/* 162 */     this.registry.registerBeanDefinition(definitionHolder.getBeanName(), definitionHolder.getBeanDefinition());
/* 163 */     configClass.setBeanName(configBeanName);
/*     */     
/* 165 */     if (logger.isTraceEnabled()) {
/* 166 */       logger.trace("Registered bean definition for imported class '" + configBeanName + "'");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void loadBeanDefinitionsForBeanMethod(BeanMethod beanMethod) {
/* 176 */     ConfigurationClass configClass = beanMethod.getConfigurationClass();
/* 177 */     MethodMetadata metadata = beanMethod.getMetadata();
/* 178 */     String methodName = metadata.getMethodName();
/*     */ 
/*     */     
/* 181 */     if (this.conditionEvaluator.shouldSkip((AnnotatedTypeMetadata)metadata, ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN)) {
/* 182 */       configClass.skippedBeanMethods.add(methodName);
/*     */       return;
/*     */     } 
/* 185 */     if (configClass.skippedBeanMethods.contains(methodName)) {
/*     */       return;
/*     */     }
/*     */     
/* 189 */     AnnotationAttributes bean = AnnotationConfigUtils.attributesFor((AnnotatedTypeMetadata)metadata, Bean.class);
/* 190 */     Assert.state((bean != null), "No @Bean annotation attributes");
/*     */ 
/*     */     
/* 193 */     List<String> names = new ArrayList<>(Arrays.asList(bean.getStringArray("name")));
/* 194 */     String beanName = !names.isEmpty() ? names.remove(0) : methodName;
/*     */ 
/*     */     
/* 197 */     for (String alias : names) {
/* 198 */       this.registry.registerAlias(beanName, alias);
/*     */     }
/*     */ 
/*     */     
/* 202 */     if (isOverriddenByExistingDefinition(beanMethod, beanName)) {
/* 203 */       if (beanName.equals(beanMethod.getConfigurationClass().getBeanName())) {
/* 204 */         throw new BeanDefinitionStoreException(beanMethod.getConfigurationClass().getResource().getDescription(), beanName, "Bean name derived from @Bean method '" + beanMethod
/* 205 */             .getMetadata().getMethodName() + "' clashes with bean name for containing configuration class; please make those names unique!");
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 211 */     ConfigurationClassBeanDefinition beanDef = new ConfigurationClassBeanDefinition(configClass, metadata);
/* 212 */     beanDef.setResource(configClass.getResource());
/* 213 */     beanDef.setSource(this.sourceExtractor.extractSource(metadata, configClass.getResource()));
/*     */     
/* 215 */     if (metadata.isStatic()) {
/*     */       
/* 217 */       beanDef.setBeanClassName(configClass.getMetadata().getClassName());
/* 218 */       beanDef.setFactoryMethodName(methodName);
/*     */     }
/*     */     else {
/*     */       
/* 222 */       beanDef.setFactoryBeanName(configClass.getBeanName());
/* 223 */       beanDef.setUniqueFactoryMethodName(methodName);
/*     */     } 
/* 225 */     beanDef.setAutowireMode(3);
/* 226 */     beanDef.setAttribute(RequiredAnnotationBeanPostProcessor.SKIP_REQUIRED_CHECK_ATTRIBUTE, Boolean.TRUE);
/*     */ 
/*     */     
/* 229 */     AnnotationConfigUtils.processCommonDefinitionAnnotations(beanDef, (AnnotatedTypeMetadata)metadata);
/*     */     
/* 231 */     Autowire autowire = (Autowire)bean.getEnum("autowire");
/* 232 */     if (autowire.isAutowire()) {
/* 233 */       beanDef.setAutowireMode(autowire.value());
/*     */     }
/*     */     
/* 236 */     boolean autowireCandidate = bean.getBoolean("autowireCandidate");
/* 237 */     if (!autowireCandidate) {
/* 238 */       beanDef.setAutowireCandidate(false);
/*     */     }
/*     */     
/* 241 */     String initMethodName = bean.getString("initMethod");
/* 242 */     if (StringUtils.hasText(initMethodName)) {
/* 243 */       beanDef.setInitMethodName(initMethodName);
/*     */     }
/*     */     
/* 246 */     String destroyMethodName = bean.getString("destroyMethod");
/* 247 */     beanDef.setDestroyMethodName(destroyMethodName);
/*     */ 
/*     */     
/* 250 */     ScopedProxyMode proxyMode = ScopedProxyMode.NO;
/* 251 */     AnnotationAttributes attributes = AnnotationConfigUtils.attributesFor((AnnotatedTypeMetadata)metadata, Scope.class);
/* 252 */     if (attributes != null) {
/* 253 */       beanDef.setScope(attributes.getString("value"));
/* 254 */       proxyMode = (ScopedProxyMode)attributes.getEnum("proxyMode");
/* 255 */       if (proxyMode == ScopedProxyMode.DEFAULT) {
/* 256 */         proxyMode = ScopedProxyMode.NO;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 261 */     ConfigurationClassBeanDefinition configurationClassBeanDefinition1 = beanDef;
/* 262 */     if (proxyMode != ScopedProxyMode.NO) {
/* 263 */       BeanDefinitionHolder proxyDef = ScopedProxyCreator.createScopedProxy(new BeanDefinitionHolder((BeanDefinition)beanDef, beanName), this.registry, (proxyMode == ScopedProxyMode.TARGET_CLASS));
/*     */ 
/*     */ 
/*     */       
/* 267 */       configurationClassBeanDefinition1 = new ConfigurationClassBeanDefinition((RootBeanDefinition)proxyDef.getBeanDefinition(), configClass, metadata);
/*     */     } 
/*     */     
/* 270 */     if (logger.isTraceEnabled()) {
/* 271 */       logger.trace(String.format("Registering bean definition for @Bean method %s.%s()", new Object[] { configClass
/* 272 */               .getMetadata().getClassName(), beanName }));
/*     */     }
/* 274 */     this.registry.registerBeanDefinition(beanName, (BeanDefinition)configurationClassBeanDefinition1);
/*     */   }
/*     */   
/*     */   protected boolean isOverriddenByExistingDefinition(BeanMethod beanMethod, String beanName) {
/* 278 */     if (!this.registry.containsBeanDefinition(beanName)) {
/* 279 */       return false;
/*     */     }
/* 281 */     BeanDefinition existingBeanDef = this.registry.getBeanDefinition(beanName);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 287 */     if (existingBeanDef instanceof ConfigurationClassBeanDefinition) {
/* 288 */       ConfigurationClassBeanDefinition ccbd = (ConfigurationClassBeanDefinition)existingBeanDef;
/* 289 */       return ccbd.getMetadata().getClassName().equals(beanMethod
/* 290 */           .getConfigurationClass().getMetadata().getClassName());
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 295 */     if (existingBeanDef instanceof ScannedGenericBeanDefinition) {
/* 296 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 301 */     if (existingBeanDef.getRole() > 0) {
/* 302 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 307 */     if (this.registry instanceof DefaultListableBeanFactory && 
/* 308 */       !((DefaultListableBeanFactory)this.registry).isAllowBeanDefinitionOverriding()) {
/* 309 */       throw new BeanDefinitionStoreException(beanMethod.getConfigurationClass().getResource().getDescription(), beanName, "@Bean definition illegally overridden by existing bean definition: " + existingBeanDef);
/*     */     }
/*     */     
/* 312 */     if (logger.isDebugEnabled()) {
/* 313 */       logger.debug(String.format("Skipping bean definition for %s: a definition for bean '%s' already exists. This top-level bean definition is considered as an override.", new Object[] { beanMethod, beanName }));
/*     */     }
/*     */ 
/*     */     
/* 317 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void loadBeanDefinitionsFromImportedResources(Map<String, Class<? extends BeanDefinitionReader>> importedResources) {
/* 323 */     Map<Class<?>, BeanDefinitionReader> readerInstanceCache = new HashMap<>();
/*     */     
/* 325 */     importedResources.forEach((resource, readerClass) -> {
/*     */           Class<XmlBeanDefinitionReader> clazz;
/*     */           
/*     */           if (BeanDefinitionReader.class == readerClass) {
/*     */             if (StringUtils.endsWithIgnoreCase(resource, ".groovy")) {
/*     */               Class<GroovyBeanDefinitionReader> clazz1 = GroovyBeanDefinitionReader.class;
/*     */             } else {
/*     */               clazz = XmlBeanDefinitionReader.class;
/*     */             } 
/*     */           }
/*     */           
/*     */           BeanDefinitionReader reader = (BeanDefinitionReader)readerInstanceCache.get(clazz);
/*     */           
/*     */           if (reader == null) {
/*     */             try {
/*     */               reader = clazz.getConstructor(new Class[] { BeanDefinitionRegistry.class }).newInstance(new Object[] { this.registry });
/*     */               
/*     */               if (reader instanceof AbstractBeanDefinitionReader) {
/*     */                 AbstractBeanDefinitionReader abdr = (AbstractBeanDefinitionReader)reader;
/*     */                 
/*     */                 abdr.setResourceLoader(this.resourceLoader);
/*     */                 
/*     */                 abdr.setEnvironment(this.environment);
/*     */               } 
/*     */               
/*     */               readerInstanceCache.put(clazz, reader);
/* 351 */             } catch (Throwable ex) {
/*     */               throw new IllegalStateException("Could not instantiate BeanDefinitionReader class [" + clazz.getName() + "]");
/*     */             } 
/*     */           }
/*     */           reader.loadBeanDefinitions(resource);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void loadBeanDefinitionsFromRegistrars(Map<ImportBeanDefinitionRegistrar, AnnotationMetadata> registrars) {
/* 363 */     registrars.forEach((registrar, metadata) -> registrar.registerBeanDefinitions(metadata, this.registry));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ConfigurationClassBeanDefinition
/*     */     extends RootBeanDefinition
/*     */     implements AnnotatedBeanDefinition
/*     */   {
/*     */     private final AnnotationMetadata annotationMetadata;
/*     */ 
/*     */ 
/*     */     
/*     */     private final MethodMetadata factoryMethodMetadata;
/*     */ 
/*     */ 
/*     */     
/*     */     public ConfigurationClassBeanDefinition(ConfigurationClass configClass, MethodMetadata beanMethodMetadata) {
/* 382 */       this.annotationMetadata = configClass.getMetadata();
/* 383 */       this.factoryMethodMetadata = beanMethodMetadata;
/* 384 */       setLenientConstructorResolution(false);
/*     */     }
/*     */ 
/*     */     
/*     */     public ConfigurationClassBeanDefinition(RootBeanDefinition original, ConfigurationClass configClass, MethodMetadata beanMethodMetadata) {
/* 389 */       super(original);
/* 390 */       this.annotationMetadata = configClass.getMetadata();
/* 391 */       this.factoryMethodMetadata = beanMethodMetadata;
/*     */     }
/*     */     
/*     */     private ConfigurationClassBeanDefinition(ConfigurationClassBeanDefinition original) {
/* 395 */       super(original);
/* 396 */       this.annotationMetadata = original.annotationMetadata;
/* 397 */       this.factoryMethodMetadata = original.factoryMethodMetadata;
/*     */     }
/*     */ 
/*     */     
/*     */     public AnnotationMetadata getMetadata() {
/* 402 */       return this.annotationMetadata;
/*     */     }
/*     */ 
/*     */     
/*     */     public MethodMetadata getFactoryMethodMetadata() {
/* 407 */       return this.factoryMethodMetadata;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isFactoryMethod(Method candidate) {
/* 412 */       return (super.isFactoryMethod(candidate) && BeanAnnotationHelper.isBeanAnnotated(candidate));
/*     */     }
/*     */ 
/*     */     
/*     */     public ConfigurationClassBeanDefinition cloneBeanDefinition() {
/* 417 */       return new ConfigurationClassBeanDefinition(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class TrackedConditionEvaluator
/*     */   {
/* 428 */     private final Map<ConfigurationClass, Boolean> skipped = new HashMap<>();
/*     */     
/*     */     public boolean shouldSkip(ConfigurationClass configClass) {
/* 431 */       Boolean skip = this.skipped.get(configClass);
/* 432 */       if (skip == null) {
/* 433 */         if (configClass.isImported()) {
/* 434 */           boolean allSkipped = true;
/* 435 */           for (ConfigurationClass importedBy : configClass.getImportedBy()) {
/* 436 */             if (!shouldSkip(importedBy)) {
/* 437 */               allSkipped = false;
/*     */               break;
/*     */             } 
/*     */           } 
/* 441 */           if (allSkipped)
/*     */           {
/* 443 */             skip = Boolean.valueOf(true);
/*     */           }
/*     */         } 
/* 446 */         if (skip == null) {
/* 447 */           skip = Boolean.valueOf(ConfigurationClassBeanDefinitionReader.this.conditionEvaluator.shouldSkip((AnnotatedTypeMetadata)configClass.getMetadata(), ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN));
/*     */         }
/* 449 */         this.skipped.put(configClass, skip);
/*     */       } 
/* 451 */       return skip.booleanValue();
/*     */     }
/*     */     
/*     */     private TrackedConditionEvaluator() {}
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/ConfigurationClassBeanDefinitionReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */