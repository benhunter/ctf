/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.env.EnvironmentCapable;
/*     */ import org.springframework.core.env.StandardEnvironment;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
/*     */ import org.springframework.core.io.support.ResourcePatternResolver;
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
/*     */ 
/*     */ 
/*     */ public abstract class AbstractBeanDefinitionReader
/*     */   implements BeanDefinitionReader, EnvironmentCapable
/*     */ {
/*  52 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private final BeanDefinitionRegistry registry;
/*     */   
/*     */   @Nullable
/*     */   private ResourceLoader resourceLoader;
/*     */   
/*     */   @Nullable
/*     */   private ClassLoader beanClassLoader;
/*     */   
/*     */   private Environment environment;
/*     */   
/*  64 */   private BeanNameGenerator beanNameGenerator = new DefaultBeanNameGenerator();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {
/*  85 */     Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
/*  86 */     this.registry = registry;
/*     */ 
/*     */     
/*  89 */     if (this.registry instanceof ResourceLoader) {
/*  90 */       this.resourceLoader = (ResourceLoader)this.registry;
/*     */     } else {
/*     */       
/*  93 */       this.resourceLoader = (ResourceLoader)new PathMatchingResourcePatternResolver();
/*     */     } 
/*     */ 
/*     */     
/*  97 */     if (this.registry instanceof EnvironmentCapable) {
/*  98 */       this.environment = ((EnvironmentCapable)this.registry).getEnvironment();
/*     */     } else {
/*     */       
/* 101 */       this.environment = (Environment)new StandardEnvironment();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final BeanDefinitionRegistry getBeanFactory() {
/* 107 */     return this.registry;
/*     */   }
/*     */ 
/*     */   
/*     */   public final BeanDefinitionRegistry getRegistry() {
/* 112 */     return this.registry;
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
/*     */   public void setResourceLoader(@Nullable ResourceLoader resourceLoader) {
/* 127 */     this.resourceLoader = resourceLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ResourceLoader getResourceLoader() {
/* 133 */     return this.resourceLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(@Nullable ClassLoader beanClassLoader) {
/* 144 */     this.beanClassLoader = beanClassLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ClassLoader getBeanClassLoader() {
/* 150 */     return this.beanClassLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnvironment(Environment environment) {
/* 159 */     Assert.notNull(environment, "Environment must not be null");
/* 160 */     this.environment = environment;
/*     */   }
/*     */ 
/*     */   
/*     */   public Environment getEnvironment() {
/* 165 */     return this.environment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanNameGenerator(@Nullable BeanNameGenerator beanNameGenerator) {
/* 174 */     this.beanNameGenerator = (beanNameGenerator != null) ? beanNameGenerator : new DefaultBeanNameGenerator();
/*     */   }
/*     */ 
/*     */   
/*     */   public BeanNameGenerator getBeanNameGenerator() {
/* 179 */     return this.beanNameGenerator;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int loadBeanDefinitions(Resource... resources) throws BeanDefinitionStoreException {
/* 185 */     Assert.notNull(resources, "Resource array must not be null");
/* 186 */     int count = 0;
/* 187 */     for (Resource resource : resources) {
/* 188 */       count += loadBeanDefinitions(resource);
/*     */     }
/* 190 */     return count;
/*     */   }
/*     */ 
/*     */   
/*     */   public int loadBeanDefinitions(String location) throws BeanDefinitionStoreException {
/* 195 */     return loadBeanDefinitions(location, null);
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
/*     */   public int loadBeanDefinitions(String location, @Nullable Set<Resource> actualResources) throws BeanDefinitionStoreException {
/* 214 */     ResourceLoader resourceLoader = getResourceLoader();
/* 215 */     if (resourceLoader == null) {
/* 216 */       throw new BeanDefinitionStoreException("Cannot load bean definitions from location [" + location + "]: no ResourceLoader available");
/*     */     }
/*     */ 
/*     */     
/* 220 */     if (resourceLoader instanceof ResourcePatternResolver) {
/*     */       
/*     */       try {
/* 223 */         Resource[] resources = ((ResourcePatternResolver)resourceLoader).getResources(location);
/* 224 */         int i = loadBeanDefinitions(resources);
/* 225 */         if (actualResources != null) {
/* 226 */           Collections.addAll(actualResources, resources);
/*     */         }
/* 228 */         if (this.logger.isTraceEnabled()) {
/* 229 */           this.logger.trace("Loaded " + i + " bean definitions from location pattern [" + location + "]");
/*     */         }
/* 231 */         return i;
/*     */       }
/* 233 */       catch (IOException ex) {
/* 234 */         throw new BeanDefinitionStoreException("Could not resolve bean definition resource pattern [" + location + "]", ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 240 */     Resource resource = resourceLoader.getResource(location);
/* 241 */     int count = loadBeanDefinitions(resource);
/* 242 */     if (actualResources != null) {
/* 243 */       actualResources.add(resource);
/*     */     }
/* 245 */     if (this.logger.isTraceEnabled()) {
/* 246 */       this.logger.trace("Loaded " + count + " bean definitions from location [" + location + "]");
/*     */     }
/* 248 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int loadBeanDefinitions(String... locations) throws BeanDefinitionStoreException {
/* 254 */     Assert.notNull(locations, "Location array must not be null");
/* 255 */     int count = 0;
/* 256 */     for (String location : locations) {
/* 257 */       count += loadBeanDefinitions(location);
/*     */     }
/* 259 */     return count;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/AbstractBeanDefinitionReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */