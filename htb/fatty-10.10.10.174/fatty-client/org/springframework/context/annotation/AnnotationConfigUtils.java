/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
/*     */ import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.support.AutowireCandidateResolver;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.context.event.DefaultEventListenerFactory;
/*     */ import org.springframework.context.event.EventListenerMethodProcessor;
/*     */ import org.springframework.context.support.GenericApplicationContext;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AnnotationConfigUtils
/*     */ {
/*     */   public static final String CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME = "org.springframework.context.annotation.internalConfigurationAnnotationProcessor";
/*     */   public static final String CONFIGURATION_BEAN_NAME_GENERATOR = "org.springframework.context.annotation.internalConfigurationBeanNameGenerator";
/*     */   public static final String AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME = "org.springframework.context.annotation.internalAutowiredAnnotationProcessor";
/*     */   @Deprecated
/*     */   public static final String REQUIRED_ANNOTATION_PROCESSOR_BEAN_NAME = "org.springframework.context.annotation.internalRequiredAnnotationProcessor";
/*     */   public static final String COMMON_ANNOTATION_PROCESSOR_BEAN_NAME = "org.springframework.context.annotation.internalCommonAnnotationProcessor";
/*     */   public static final String PERSISTENCE_ANNOTATION_PROCESSOR_BEAN_NAME = "org.springframework.context.annotation.internalPersistenceAnnotationProcessor";
/*     */   private static final String PERSISTENCE_ANNOTATION_PROCESSOR_CLASS_NAME = "org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor";
/*     */   public static final String EVENT_LISTENER_PROCESSOR_BEAN_NAME = "org.springframework.context.event.internalEventListenerProcessor";
/*     */   public static final String EVENT_LISTENER_FACTORY_BEAN_NAME = "org.springframework.context.event.internalEventListenerFactory";
/*     */   private static final boolean jsr250Present;
/*     */   private static final boolean jpaPresent;
/*     */   
/*     */   static {
/* 125 */     ClassLoader classLoader = AnnotationConfigUtils.class.getClassLoader();
/* 126 */     jsr250Present = ClassUtils.isPresent("javax.annotation.Resource", classLoader);
/*     */     
/* 128 */     jpaPresent = (ClassUtils.isPresent("javax.persistence.EntityManagerFactory", classLoader) && ClassUtils.isPresent("org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor", classLoader));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void registerAnnotationConfigProcessors(BeanDefinitionRegistry registry) {
/* 137 */     registerAnnotationConfigProcessors(registry, null);
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
/*     */   public static Set<BeanDefinitionHolder> registerAnnotationConfigProcessors(BeanDefinitionRegistry registry, @Nullable Object source) {
/* 151 */     DefaultListableBeanFactory beanFactory = unwrapDefaultListableBeanFactory(registry);
/* 152 */     if (beanFactory != null) {
/* 153 */       if (!(beanFactory.getDependencyComparator() instanceof AnnotationAwareOrderComparator)) {
/* 154 */         beanFactory.setDependencyComparator((Comparator)AnnotationAwareOrderComparator.INSTANCE);
/*     */       }
/* 156 */       if (!(beanFactory.getAutowireCandidateResolver() instanceof ContextAnnotationAutowireCandidateResolver)) {
/* 157 */         beanFactory.setAutowireCandidateResolver((AutowireCandidateResolver)new ContextAnnotationAutowireCandidateResolver());
/*     */       }
/*     */     } 
/*     */     
/* 161 */     Set<BeanDefinitionHolder> beanDefs = new LinkedHashSet<>(8);
/*     */     
/* 163 */     if (!registry.containsBeanDefinition("org.springframework.context.annotation.internalConfigurationAnnotationProcessor")) {
/* 164 */       RootBeanDefinition def = new RootBeanDefinition(ConfigurationClassPostProcessor.class);
/* 165 */       def.setSource(source);
/* 166 */       beanDefs.add(registerPostProcessor(registry, def, "org.springframework.context.annotation.internalConfigurationAnnotationProcessor"));
/*     */     } 
/*     */     
/* 169 */     if (!registry.containsBeanDefinition("org.springframework.context.annotation.internalAutowiredAnnotationProcessor")) {
/* 170 */       RootBeanDefinition def = new RootBeanDefinition(AutowiredAnnotationBeanPostProcessor.class);
/* 171 */       def.setSource(source);
/* 172 */       beanDefs.add(registerPostProcessor(registry, def, "org.springframework.context.annotation.internalAutowiredAnnotationProcessor"));
/*     */     } 
/*     */ 
/*     */     
/* 176 */     if (jsr250Present && !registry.containsBeanDefinition("org.springframework.context.annotation.internalCommonAnnotationProcessor")) {
/* 177 */       RootBeanDefinition def = new RootBeanDefinition(CommonAnnotationBeanPostProcessor.class);
/* 178 */       def.setSource(source);
/* 179 */       beanDefs.add(registerPostProcessor(registry, def, "org.springframework.context.annotation.internalCommonAnnotationProcessor"));
/*     */     } 
/*     */ 
/*     */     
/* 183 */     if (jpaPresent && !registry.containsBeanDefinition("org.springframework.context.annotation.internalPersistenceAnnotationProcessor")) {
/* 184 */       RootBeanDefinition def = new RootBeanDefinition();
/*     */       try {
/* 186 */         def.setBeanClass(ClassUtils.forName("org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor", AnnotationConfigUtils.class
/* 187 */               .getClassLoader()));
/*     */       }
/* 189 */       catch (ClassNotFoundException ex) {
/* 190 */         throw new IllegalStateException("Cannot load optional framework class: org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor", ex);
/*     */       } 
/*     */       
/* 193 */       def.setSource(source);
/* 194 */       beanDefs.add(registerPostProcessor(registry, def, "org.springframework.context.annotation.internalPersistenceAnnotationProcessor"));
/*     */     } 
/*     */     
/* 197 */     if (!registry.containsBeanDefinition("org.springframework.context.event.internalEventListenerProcessor")) {
/* 198 */       RootBeanDefinition def = new RootBeanDefinition(EventListenerMethodProcessor.class);
/* 199 */       def.setSource(source);
/* 200 */       beanDefs.add(registerPostProcessor(registry, def, "org.springframework.context.event.internalEventListenerProcessor"));
/*     */     } 
/*     */     
/* 203 */     if (!registry.containsBeanDefinition("org.springframework.context.event.internalEventListenerFactory")) {
/* 204 */       RootBeanDefinition def = new RootBeanDefinition(DefaultEventListenerFactory.class);
/* 205 */       def.setSource(source);
/* 206 */       beanDefs.add(registerPostProcessor(registry, def, "org.springframework.context.event.internalEventListenerFactory"));
/*     */     } 
/*     */     
/* 209 */     return beanDefs;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static BeanDefinitionHolder registerPostProcessor(BeanDefinitionRegistry registry, RootBeanDefinition definition, String beanName) {
/* 215 */     definition.setRole(2);
/* 216 */     registry.registerBeanDefinition(beanName, (BeanDefinition)definition);
/* 217 */     return new BeanDefinitionHolder((BeanDefinition)definition, beanName);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static DefaultListableBeanFactory unwrapDefaultListableBeanFactory(BeanDefinitionRegistry registry) {
/* 222 */     if (registry instanceof DefaultListableBeanFactory) {
/* 223 */       return (DefaultListableBeanFactory)registry;
/*     */     }
/* 225 */     if (registry instanceof GenericApplicationContext) {
/* 226 */       return ((GenericApplicationContext)registry).getDefaultListableBeanFactory();
/*     */     }
/*     */     
/* 229 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void processCommonDefinitionAnnotations(AnnotatedBeanDefinition abd) {
/* 234 */     processCommonDefinitionAnnotations(abd, (AnnotatedTypeMetadata)abd.getMetadata());
/*     */   }
/*     */   
/*     */   static void processCommonDefinitionAnnotations(AnnotatedBeanDefinition abd, AnnotatedTypeMetadata metadata) {
/* 238 */     AnnotationAttributes lazy = attributesFor(metadata, Lazy.class);
/* 239 */     if (lazy != null) {
/* 240 */       abd.setLazyInit(lazy.getBoolean("value"));
/*     */     }
/* 242 */     else if (abd.getMetadata() != metadata) {
/* 243 */       lazy = attributesFor((AnnotatedTypeMetadata)abd.getMetadata(), Lazy.class);
/* 244 */       if (lazy != null) {
/* 245 */         abd.setLazyInit(lazy.getBoolean("value"));
/*     */       }
/*     */     } 
/*     */     
/* 249 */     if (metadata.isAnnotated(Primary.class.getName())) {
/* 250 */       abd.setPrimary(true);
/*     */     }
/* 252 */     AnnotationAttributes dependsOn = attributesFor(metadata, DependsOn.class);
/* 253 */     if (dependsOn != null) {
/* 254 */       abd.setDependsOn(dependsOn.getStringArray("value"));
/*     */     }
/*     */     
/* 257 */     AnnotationAttributes role = attributesFor(metadata, Role.class);
/* 258 */     if (role != null) {
/* 259 */       abd.setRole(role.getNumber("value").intValue());
/*     */     }
/* 261 */     AnnotationAttributes description = attributesFor(metadata, Description.class);
/* 262 */     if (description != null) {
/* 263 */       abd.setDescription(description.getString("value"));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static BeanDefinitionHolder applyScopedProxyMode(ScopeMetadata metadata, BeanDefinitionHolder definition, BeanDefinitionRegistry registry) {
/* 270 */     ScopedProxyMode scopedProxyMode = metadata.getScopedProxyMode();
/* 271 */     if (scopedProxyMode.equals(ScopedProxyMode.NO)) {
/* 272 */       return definition;
/*     */     }
/* 274 */     boolean proxyTargetClass = scopedProxyMode.equals(ScopedProxyMode.TARGET_CLASS);
/* 275 */     return ScopedProxyCreator.createScopedProxy(definition, registry, proxyTargetClass);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   static AnnotationAttributes attributesFor(AnnotatedTypeMetadata metadata, Class<?> annotationClass) {
/* 280 */     return attributesFor(metadata, annotationClass.getName());
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   static AnnotationAttributes attributesFor(AnnotatedTypeMetadata metadata, String annotationClassName) {
/* 285 */     return AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(annotationClassName, false));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static Set<AnnotationAttributes> attributesForRepeatable(AnnotationMetadata metadata, Class<?> containerClass, Class<?> annotationClass) {
/* 291 */     return attributesForRepeatable(metadata, containerClass.getName(), annotationClass.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Set<AnnotationAttributes> attributesForRepeatable(AnnotationMetadata metadata, String containerClassName, String annotationClassName) {
/* 298 */     Set<AnnotationAttributes> result = new LinkedHashSet<>();
/*     */ 
/*     */     
/* 301 */     addAttributesIfNotNull(result, metadata.getAnnotationAttributes(annotationClassName, false));
/*     */ 
/*     */     
/* 304 */     Map<String, Object> container = metadata.getAnnotationAttributes(containerClassName, false);
/* 305 */     if (container != null && container.containsKey("value")) {
/* 306 */       for (Map<String, Object> containedAttributes : (Map[])container.get("value")) {
/* 307 */         addAttributesIfNotNull(result, containedAttributes);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 312 */     return Collections.unmodifiableSet(result);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void addAttributesIfNotNull(Set<AnnotationAttributes> result, @Nullable Map<String, Object> attributes) {
/* 318 */     if (attributes != null)
/* 319 */       result.add(AnnotationAttributes.fromMap(attributes)); 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/AnnotationConfigUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */