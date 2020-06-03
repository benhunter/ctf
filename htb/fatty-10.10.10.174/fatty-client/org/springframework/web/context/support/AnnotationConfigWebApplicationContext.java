/*     */ package org.springframework.web.context.support;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
/*     */ import org.springframework.context.annotation.AnnotationConfigRegistry;
/*     */ import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
/*     */ import org.springframework.context.annotation.ScopeMetadataResolver;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class AnnotationConfigWebApplicationContext
/*     */   extends AbstractRefreshableWebApplicationContext
/*     */   implements AnnotationConfigRegistry
/*     */ {
/*     */   @Nullable
/*     */   private BeanNameGenerator beanNameGenerator;
/*     */   @Nullable
/*     */   private ScopeMetadataResolver scopeMetadataResolver;
/*  93 */   private final Set<Class<?>> annotatedClasses = new LinkedHashSet<>();
/*     */   
/*  95 */   private final Set<String> basePackages = new LinkedHashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanNameGenerator(@Nullable BeanNameGenerator beanNameGenerator) {
/* 106 */     this.beanNameGenerator = beanNameGenerator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected BeanNameGenerator getBeanNameGenerator() {
/* 115 */     return this.beanNameGenerator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScopeMetadataResolver(@Nullable ScopeMetadataResolver scopeMetadataResolver) {
/* 126 */     this.scopeMetadataResolver = scopeMetadataResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected ScopeMetadataResolver getScopeMetadataResolver() {
/* 135 */     return this.scopeMetadataResolver;
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
/*     */   public void register(Class<?>... annotatedClasses) {
/* 151 */     Assert.notEmpty((Object[])annotatedClasses, "At least one annotated class must be specified");
/* 152 */     Collections.addAll(this.annotatedClasses, annotatedClasses);
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
/*     */   public void scan(String... basePackages) {
/* 166 */     Assert.notEmpty((Object[])basePackages, "At least one base package must be specified");
/* 167 */     Collections.addAll(this.basePackages, basePackages);
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
/*     */   protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
/* 195 */     AnnotatedBeanDefinitionReader reader = getAnnotatedBeanDefinitionReader(beanFactory);
/* 196 */     ClassPathBeanDefinitionScanner scanner = getClassPathBeanDefinitionScanner(beanFactory);
/*     */     
/* 198 */     BeanNameGenerator beanNameGenerator = getBeanNameGenerator();
/* 199 */     if (beanNameGenerator != null) {
/* 200 */       reader.setBeanNameGenerator(beanNameGenerator);
/* 201 */       scanner.setBeanNameGenerator(beanNameGenerator);
/* 202 */       beanFactory.registerSingleton("org.springframework.context.annotation.internalConfigurationBeanNameGenerator", beanNameGenerator);
/*     */     } 
/*     */     
/* 205 */     ScopeMetadataResolver scopeMetadataResolver = getScopeMetadataResolver();
/* 206 */     if (scopeMetadataResolver != null) {
/* 207 */       reader.setScopeMetadataResolver(scopeMetadataResolver);
/* 208 */       scanner.setScopeMetadataResolver(scopeMetadataResolver);
/*     */     } 
/*     */     
/* 211 */     if (!this.annotatedClasses.isEmpty()) {
/* 212 */       if (this.logger.isDebugEnabled()) {
/* 213 */         this.logger.debug("Registering annotated classes: [" + 
/* 214 */             StringUtils.collectionToCommaDelimitedString(this.annotatedClasses) + "]");
/*     */       }
/* 216 */       reader.register(ClassUtils.toClassArray(this.annotatedClasses));
/*     */     } 
/*     */     
/* 219 */     if (!this.basePackages.isEmpty()) {
/* 220 */       if (this.logger.isDebugEnabled()) {
/* 221 */         this.logger.debug("Scanning base packages: [" + 
/* 222 */             StringUtils.collectionToCommaDelimitedString(this.basePackages) + "]");
/*     */       }
/* 224 */       scanner.scan(StringUtils.toStringArray(this.basePackages));
/*     */     } 
/*     */     
/* 227 */     String[] configLocations = getConfigLocations();
/* 228 */     if (configLocations != null) {
/* 229 */       for (String configLocation : configLocations) {
/*     */         try {
/* 231 */           Class<?> clazz = ClassUtils.forName(configLocation, getClassLoader());
/* 232 */           if (this.logger.isTraceEnabled()) {
/* 233 */             this.logger.trace("Registering [" + configLocation + "]");
/*     */           }
/* 235 */           reader.register(new Class[] { clazz });
/*     */         }
/* 237 */         catch (ClassNotFoundException ex) {
/* 238 */           if (this.logger.isTraceEnabled()) {
/* 239 */             this.logger.trace("Could not load class for config location [" + configLocation + "] - trying package scan. " + ex);
/*     */           }
/*     */           
/* 242 */           int count = scanner.scan(new String[] { configLocation });
/* 243 */           if (count == 0 && this.logger.isDebugEnabled()) {
/* 244 */             this.logger.debug("No annotated classes found for specified class/package [" + configLocation + "]");
/*     */           }
/*     */         } 
/*     */       } 
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
/*     */   protected AnnotatedBeanDefinitionReader getAnnotatedBeanDefinitionReader(DefaultListableBeanFactory beanFactory) {
/* 263 */     return new AnnotatedBeanDefinitionReader((BeanDefinitionRegistry)beanFactory, (Environment)getEnvironment());
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
/*     */   protected ClassPathBeanDefinitionScanner getClassPathBeanDefinitionScanner(DefaultListableBeanFactory beanFactory) {
/* 277 */     return new ClassPathBeanDefinitionScanner((BeanDefinitionRegistry)beanFactory, true, (Environment)getEnvironment());
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/support/AnnotationConfigWebApplicationContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */