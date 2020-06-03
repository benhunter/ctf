/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.function.Supplier;
/*     */ import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
/*     */ import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionCustomizer;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.support.AutowireCandidateQualifier;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.env.EnvironmentCapable;
/*     */ import org.springframework.core.env.StandardEnvironment;
/*     */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnnotatedBeanDefinitionReader
/*     */ {
/*     */   private final BeanDefinitionRegistry registry;
/*  52 */   private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();
/*     */   
/*  54 */   private ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ConditionEvaluator conditionEvaluator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry registry) {
/*  70 */     this(registry, getOrCreateEnvironment(registry));
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
/*     */   public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry registry, Environment environment) {
/*  83 */     Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
/*  84 */     Assert.notNull(environment, "Environment must not be null");
/*  85 */     this.registry = registry;
/*  86 */     this.conditionEvaluator = new ConditionEvaluator(registry, environment, null);
/*  87 */     AnnotationConfigUtils.registerAnnotationConfigProcessors(this.registry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final BeanDefinitionRegistry getRegistry() {
/*  95 */     return this.registry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnvironment(Environment environment) {
/* 105 */     this.conditionEvaluator = new ConditionEvaluator(this.registry, environment, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanNameGenerator(@Nullable BeanNameGenerator beanNameGenerator) {
/* 113 */     this.beanNameGenerator = (beanNameGenerator != null) ? beanNameGenerator : new AnnotationBeanNameGenerator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScopeMetadataResolver(@Nullable ScopeMetadataResolver scopeMetadataResolver) {
/* 121 */     this.scopeMetadataResolver = (scopeMetadataResolver != null) ? scopeMetadataResolver : new AnnotationScopeMetadataResolver();
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
/*     */   public void register(Class<?>... annotatedClasses) {
/* 134 */     for (Class<?> annotatedClass : annotatedClasses) {
/* 135 */       registerBean(annotatedClass);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerBean(Class<?> annotatedClass) {
/* 145 */     doRegisterBean(annotatedClass, null, null, null, new BeanDefinitionCustomizer[0]);
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
/*     */   public <T> void registerBean(Class<T> annotatedClass, @Nullable Supplier<T> instanceSupplier) {
/* 158 */     doRegisterBean(annotatedClass, instanceSupplier, null, null, new BeanDefinitionCustomizer[0]);
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
/*     */   public <T> void registerBean(Class<T> annotatedClass, String name, @Nullable Supplier<T> instanceSupplier) {
/* 172 */     doRegisterBean(annotatedClass, instanceSupplier, name, null, new BeanDefinitionCustomizer[0]);
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
/*     */   public void registerBean(Class<?> annotatedClass, Class<? extends Annotation>... qualifiers) {
/* 184 */     doRegisterBean(annotatedClass, null, null, qualifiers, new BeanDefinitionCustomizer[0]);
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
/*     */   public void registerBean(Class<?> annotatedClass, String name, Class<? extends Annotation>... qualifiers) {
/* 197 */     doRegisterBean(annotatedClass, null, name, qualifiers, new BeanDefinitionCustomizer[0]);
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
/*     */   <T> void doRegisterBean(Class<T> annotatedClass, @Nullable Supplier<T> instanceSupplier, @Nullable String name, @Nullable Class<? extends Annotation>[] qualifiers, BeanDefinitionCustomizer... definitionCustomizers) {
/* 216 */     AnnotatedGenericBeanDefinition abd = new AnnotatedGenericBeanDefinition(annotatedClass);
/* 217 */     if (this.conditionEvaluator.shouldSkip((AnnotatedTypeMetadata)abd.getMetadata())) {
/*     */       return;
/*     */     }
/*     */     
/* 221 */     abd.setInstanceSupplier(instanceSupplier);
/* 222 */     ScopeMetadata scopeMetadata = this.scopeMetadataResolver.resolveScopeMetadata((BeanDefinition)abd);
/* 223 */     abd.setScope(scopeMetadata.getScopeName());
/* 224 */     String beanName = (name != null) ? name : this.beanNameGenerator.generateBeanName((BeanDefinition)abd, this.registry);
/*     */     
/* 226 */     AnnotationConfigUtils.processCommonDefinitionAnnotations((AnnotatedBeanDefinition)abd);
/* 227 */     if (qualifiers != null) {
/* 228 */       for (Class<? extends Annotation> qualifier : qualifiers) {
/* 229 */         if (Primary.class == qualifier) {
/* 230 */           abd.setPrimary(true);
/*     */         }
/* 232 */         else if (Lazy.class == qualifier) {
/* 233 */           abd.setLazyInit(true);
/*     */         } else {
/*     */           
/* 236 */           abd.addQualifier(new AutowireCandidateQualifier(qualifier));
/*     */         } 
/*     */       } 
/*     */     }
/* 240 */     for (BeanDefinitionCustomizer customizer : definitionCustomizers) {
/* 241 */       customizer.customize((BeanDefinition)abd);
/*     */     }
/*     */     
/* 244 */     BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder((BeanDefinition)abd, beanName);
/* 245 */     definitionHolder = AnnotationConfigUtils.applyScopedProxyMode(scopeMetadata, definitionHolder, this.registry);
/* 246 */     BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, this.registry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Environment getOrCreateEnvironment(BeanDefinitionRegistry registry) {
/* 255 */     Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
/* 256 */     if (registry instanceof EnvironmentCapable) {
/* 257 */       return ((EnvironmentCapable)registry).getEnvironment();
/*     */     }
/* 259 */     return (Environment)new StandardEnvironment();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/AnnotatedBeanDefinitionReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */