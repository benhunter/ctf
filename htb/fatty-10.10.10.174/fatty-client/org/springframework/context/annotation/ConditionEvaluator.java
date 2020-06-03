/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.env.EnvironmentCapable;
/*     */ import org.springframework.core.env.StandardEnvironment;
/*     */ import org.springframework.core.io.DefaultResourceLoader;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ConditionEvaluator
/*     */ {
/*     */   private final ConditionContextImpl context;
/*     */   
/*     */   public ConditionEvaluator(@Nullable BeanDefinitionRegistry registry, @Nullable Environment environment, @Nullable ResourceLoader resourceLoader) {
/*  59 */     this.context = new ConditionContextImpl(registry, environment, resourceLoader);
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
/*     */   public boolean shouldSkip(AnnotatedTypeMetadata metadata) {
/*  71 */     return shouldSkip(metadata, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldSkip(@Nullable AnnotatedTypeMetadata metadata, @Nullable ConfigurationCondition.ConfigurationPhase phase) {
/*  81 */     if (metadata == null || !metadata.isAnnotated(Conditional.class.getName())) {
/*  82 */       return false;
/*     */     }
/*     */     
/*  85 */     if (phase == null) {
/*  86 */       if (metadata instanceof AnnotationMetadata && 
/*  87 */         ConfigurationClassUtils.isConfigurationCandidate((AnnotationMetadata)metadata)) {
/*  88 */         return shouldSkip(metadata, ConfigurationCondition.ConfigurationPhase.PARSE_CONFIGURATION);
/*     */       }
/*  90 */       return shouldSkip(metadata, ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN);
/*     */     } 
/*     */     
/*  93 */     List<Condition> conditions = new ArrayList<>();
/*  94 */     for (String[] conditionClasses : getConditionClasses(metadata)) {
/*  95 */       for (String conditionClass : conditionClasses) {
/*  96 */         Condition condition = getCondition(conditionClass, this.context.getClassLoader());
/*  97 */         conditions.add(condition);
/*     */       } 
/*     */     } 
/*     */     
/* 101 */     AnnotationAwareOrderComparator.sort(conditions);
/*     */     
/* 103 */     for (Condition condition : conditions) {
/* 104 */       ConfigurationCondition.ConfigurationPhase requiredPhase = null;
/* 105 */       if (condition instanceof ConfigurationCondition) {
/* 106 */         requiredPhase = ((ConfigurationCondition)condition).getConfigurationPhase();
/*     */       }
/* 108 */       if ((requiredPhase == null || requiredPhase == phase) && !condition.matches(this.context, metadata)) {
/* 109 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 113 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private List<String[]> getConditionClasses(AnnotatedTypeMetadata metadata) {
/* 118 */     MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(Conditional.class.getName(), true);
/* 119 */     Object values = (attributes != null) ? attributes.get("value") : null;
/* 120 */     return (values != null) ? (List<String[]>)values : (List)Collections.<String[]>emptyList();
/*     */   }
/*     */   
/*     */   private Condition getCondition(String conditionClassName, @Nullable ClassLoader classloader) {
/* 124 */     Class<?> conditionClass = ClassUtils.resolveClassName(conditionClassName, classloader);
/* 125 */     return (Condition)BeanUtils.instantiateClass(conditionClass);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ConditionContextImpl
/*     */     implements ConditionContext
/*     */   {
/*     */     @Nullable
/*     */     private final BeanDefinitionRegistry registry;
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private final ConfigurableListableBeanFactory beanFactory;
/*     */ 
/*     */     
/*     */     private final Environment environment;
/*     */     
/*     */     private final ResourceLoader resourceLoader;
/*     */     
/*     */     @Nullable
/*     */     private final ClassLoader classLoader;
/*     */ 
/*     */     
/*     */     public ConditionContextImpl(@Nullable BeanDefinitionRegistry registry, @Nullable Environment environment, @Nullable ResourceLoader resourceLoader) {
/* 150 */       this.registry = registry;
/* 151 */       this.beanFactory = deduceBeanFactory(registry);
/* 152 */       this.environment = (environment != null) ? environment : deduceEnvironment(registry);
/* 153 */       this.resourceLoader = (resourceLoader != null) ? resourceLoader : deduceResourceLoader(registry);
/* 154 */       this.classLoader = deduceClassLoader(resourceLoader, this.beanFactory);
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     private ConfigurableListableBeanFactory deduceBeanFactory(@Nullable BeanDefinitionRegistry source) {
/* 159 */       if (source instanceof ConfigurableListableBeanFactory) {
/* 160 */         return (ConfigurableListableBeanFactory)source;
/*     */       }
/* 162 */       if (source instanceof ConfigurableApplicationContext) {
/* 163 */         return ((ConfigurableApplicationContext)source).getBeanFactory();
/*     */       }
/* 165 */       return null;
/*     */     }
/*     */     
/*     */     private Environment deduceEnvironment(@Nullable BeanDefinitionRegistry source) {
/* 169 */       if (source instanceof EnvironmentCapable) {
/* 170 */         return ((EnvironmentCapable)source).getEnvironment();
/*     */       }
/* 172 */       return (Environment)new StandardEnvironment();
/*     */     }
/*     */     
/*     */     private ResourceLoader deduceResourceLoader(@Nullable BeanDefinitionRegistry source) {
/* 176 */       if (source instanceof ResourceLoader) {
/* 177 */         return (ResourceLoader)source;
/*     */       }
/* 179 */       return (ResourceLoader)new DefaultResourceLoader();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private ClassLoader deduceClassLoader(@Nullable ResourceLoader resourceLoader, @Nullable ConfigurableListableBeanFactory beanFactory) {
/* 186 */       if (resourceLoader != null) {
/* 187 */         ClassLoader classLoader = resourceLoader.getClassLoader();
/* 188 */         if (classLoader != null) {
/* 189 */           return classLoader;
/*     */         }
/*     */       } 
/* 192 */       if (beanFactory != null) {
/* 193 */         return beanFactory.getBeanClassLoader();
/*     */       }
/* 195 */       return ClassUtils.getDefaultClassLoader();
/*     */     }
/*     */ 
/*     */     
/*     */     public BeanDefinitionRegistry getRegistry() {
/* 200 */       Assert.state((this.registry != null), "No BeanDefinitionRegistry available");
/* 201 */       return this.registry;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public ConfigurableListableBeanFactory getBeanFactory() {
/* 207 */       return this.beanFactory;
/*     */     }
/*     */ 
/*     */     
/*     */     public Environment getEnvironment() {
/* 212 */       return this.environment;
/*     */     }
/*     */ 
/*     */     
/*     */     public ResourceLoader getResourceLoader() {
/* 217 */       return this.resourceLoader;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public ClassLoader getClassLoader() {
/* 223 */       return this.classLoader;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/ConditionEvaluator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */