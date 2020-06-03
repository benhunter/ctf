/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionDefaults;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.env.EnvironmentCapable;
/*     */ import org.springframework.core.env.StandardEnvironment;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.PatternMatchUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassPathBeanDefinitionScanner
/*     */   extends ClassPathScanningCandidateComponentProvider
/*     */ {
/*     */   private final BeanDefinitionRegistry registry;
/*  67 */   private BeanDefinitionDefaults beanDefinitionDefaults = new BeanDefinitionDefaults();
/*     */   
/*     */   @Nullable
/*     */   private String[] autowireCandidatePatterns;
/*     */   
/*  72 */   private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();
/*     */   
/*  74 */   private ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean includeAnnotationConfig = true;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
/*  85 */     this(registry, true);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
/* 113 */     this(registry, useDefaultFilters, getOrCreateEnvironment(registry));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters, Environment environment) {
/* 140 */     this(registry, useDefaultFilters, environment, (registry instanceof ResourceLoader) ? (ResourceLoader)registry : null);
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
/*     */   
/*     */   public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters, Environment environment, @Nullable ResourceLoader resourceLoader) {
/* 162 */     Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
/* 163 */     this.registry = registry;
/*     */     
/* 165 */     if (useDefaultFilters) {
/* 166 */       registerDefaultFilters();
/*     */     }
/* 168 */     setEnvironment(environment);
/* 169 */     setResourceLoader(resourceLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final BeanDefinitionRegistry getRegistry() {
/* 178 */     return this.registry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanDefinitionDefaults(@Nullable BeanDefinitionDefaults beanDefinitionDefaults) {
/* 186 */     this.beanDefinitionDefaults = (beanDefinitionDefaults != null) ? beanDefinitionDefaults : new BeanDefinitionDefaults();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionDefaults getBeanDefinitionDefaults() {
/* 195 */     return this.beanDefinitionDefaults;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutowireCandidatePatterns(@Nullable String... autowireCandidatePatterns) {
/* 203 */     this.autowireCandidatePatterns = autowireCandidatePatterns;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanNameGenerator(@Nullable BeanNameGenerator beanNameGenerator) {
/* 211 */     this.beanNameGenerator = (beanNameGenerator != null) ? beanNameGenerator : new AnnotationBeanNameGenerator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScopeMetadataResolver(@Nullable ScopeMetadataResolver scopeMetadataResolver) {
/* 221 */     this.scopeMetadataResolver = (scopeMetadataResolver != null) ? scopeMetadataResolver : new AnnotationScopeMetadataResolver();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScopedProxyMode(ScopedProxyMode scopedProxyMode) {
/* 232 */     this.scopeMetadataResolver = new AnnotationScopeMetadataResolver(scopedProxyMode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIncludeAnnotationConfig(boolean includeAnnotationConfig) {
/* 241 */     this.includeAnnotationConfig = includeAnnotationConfig;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int scan(String... basePackages) {
/* 251 */     int beanCountAtScanStart = this.registry.getBeanDefinitionCount();
/*     */     
/* 253 */     doScan(basePackages);
/*     */ 
/*     */     
/* 256 */     if (this.includeAnnotationConfig) {
/* 257 */       AnnotationConfigUtils.registerAnnotationConfigProcessors(this.registry);
/*     */     }
/*     */     
/* 260 */     return this.registry.getBeanDefinitionCount() - beanCountAtScanStart;
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
/*     */   protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
/* 272 */     Assert.notEmpty((Object[])basePackages, "At least one base package must be specified");
/* 273 */     Set<BeanDefinitionHolder> beanDefinitions = new LinkedHashSet<>();
/* 274 */     for (String basePackage : basePackages) {
/* 275 */       Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
/* 276 */       for (BeanDefinition candidate : candidates) {
/* 277 */         ScopeMetadata scopeMetadata = this.scopeMetadataResolver.resolveScopeMetadata(candidate);
/* 278 */         candidate.setScope(scopeMetadata.getScopeName());
/* 279 */         String beanName = this.beanNameGenerator.generateBeanName(candidate, this.registry);
/* 280 */         if (candidate instanceof AbstractBeanDefinition) {
/* 281 */           postProcessBeanDefinition((AbstractBeanDefinition)candidate, beanName);
/*     */         }
/* 283 */         if (candidate instanceof AnnotatedBeanDefinition) {
/* 284 */           AnnotationConfigUtils.processCommonDefinitionAnnotations((AnnotatedBeanDefinition)candidate);
/*     */         }
/* 286 */         if (checkCandidate(beanName, candidate)) {
/* 287 */           BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(candidate, beanName);
/*     */           
/* 289 */           definitionHolder = AnnotationConfigUtils.applyScopedProxyMode(scopeMetadata, definitionHolder, this.registry);
/* 290 */           beanDefinitions.add(definitionHolder);
/* 291 */           registerBeanDefinition(definitionHolder, this.registry);
/*     */         } 
/*     */       } 
/*     */     } 
/* 295 */     return beanDefinitions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void postProcessBeanDefinition(AbstractBeanDefinition beanDefinition, String beanName) {
/* 305 */     beanDefinition.applyDefaults(this.beanDefinitionDefaults);
/* 306 */     if (this.autowireCandidatePatterns != null) {
/* 307 */       beanDefinition.setAutowireCandidate(PatternMatchUtils.simpleMatch(this.autowireCandidatePatterns, beanName));
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
/*     */   protected void registerBeanDefinition(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry) {
/* 319 */     BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry);
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
/*     */   protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) throws IllegalStateException {
/* 335 */     if (!this.registry.containsBeanDefinition(beanName)) {
/* 336 */       return true;
/*     */     }
/* 338 */     BeanDefinition existingDef = this.registry.getBeanDefinition(beanName);
/* 339 */     BeanDefinition originatingDef = existingDef.getOriginatingBeanDefinition();
/* 340 */     if (originatingDef != null) {
/* 341 */       existingDef = originatingDef;
/*     */     }
/* 343 */     if (isCompatible(beanDefinition, existingDef)) {
/* 344 */       return false;
/*     */     }
/* 346 */     throw new ConflictingBeanDefinitionException("Annotation-specified bean name '" + beanName + "' for bean class [" + beanDefinition
/* 347 */         .getBeanClassName() + "] conflicts with existing, non-compatible bean definition of same name and class [" + existingDef
/* 348 */         .getBeanClassName() + "]");
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
/*     */   protected boolean isCompatible(BeanDefinition newDefinition, BeanDefinition existingDefinition) {
/* 363 */     return (!(existingDefinition instanceof ScannedGenericBeanDefinition) || (newDefinition
/* 364 */       .getSource() != null && newDefinition.getSource().equals(existingDefinition.getSource())) || newDefinition
/* 365 */       .equals(existingDefinition));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Environment getOrCreateEnvironment(BeanDefinitionRegistry registry) {
/* 374 */     Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
/* 375 */     if (registry instanceof EnvironmentCapable) {
/* 376 */       return ((EnvironmentCapable)registry).getEnvironment();
/*     */     }
/* 378 */     return (Environment)new StandardEnvironment();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/ClassPathBeanDefinitionScanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */