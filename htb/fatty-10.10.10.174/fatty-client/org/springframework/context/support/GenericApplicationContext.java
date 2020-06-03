/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.function.Supplier;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionCustomizer;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GenericApplicationContext
/*     */   extends AbstractApplicationContext
/*     */   implements BeanDefinitionRegistry
/*     */ {
/*     */   private final DefaultListableBeanFactory beanFactory;
/*     */   @Nullable
/*     */   private ResourceLoader resourceLoader;
/*     */   private boolean customClassLoader = false;
/* 103 */   private final AtomicBoolean refreshed = new AtomicBoolean();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GenericApplicationContext() {
/* 112 */     this.beanFactory = new DefaultListableBeanFactory();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GenericApplicationContext(DefaultListableBeanFactory beanFactory) {
/* 122 */     Assert.notNull(beanFactory, "BeanFactory must not be null");
/* 123 */     this.beanFactory = beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GenericApplicationContext(@Nullable ApplicationContext parent) {
/* 133 */     this();
/* 134 */     setParent(parent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GenericApplicationContext(DefaultListableBeanFactory beanFactory, ApplicationContext parent) {
/* 145 */     this(beanFactory);
/* 146 */     setParent(parent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParent(@Nullable ApplicationContext parent) {
/* 157 */     super.setParent(parent);
/* 158 */     this.beanFactory.setParentBeanFactory(getInternalParentBeanFactory());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllowBeanDefinitionOverriding(boolean allowBeanDefinitionOverriding) {
/* 169 */     this.beanFactory.setAllowBeanDefinitionOverriding(allowBeanDefinitionOverriding);
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
/*     */   public void setAllowCircularReferences(boolean allowCircularReferences) {
/* 181 */     this.beanFactory.setAllowCircularReferences(allowCircularReferences);
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
/*     */   public void setResourceLoader(ResourceLoader resourceLoader) {
/* 203 */     this.resourceLoader = resourceLoader;
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
/*     */   public Resource getResource(String location) {
/* 218 */     if (this.resourceLoader != null) {
/* 219 */       return this.resourceLoader.getResource(location);
/*     */     }
/* 221 */     return super.getResource(location);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource[] getResources(String locationPattern) throws IOException {
/* 232 */     if (this.resourceLoader instanceof ResourcePatternResolver) {
/* 233 */       return ((ResourcePatternResolver)this.resourceLoader).getResources(locationPattern);
/*     */     }
/* 235 */     return super.getResources(locationPattern);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setClassLoader(@Nullable ClassLoader classLoader) {
/* 240 */     super.setClassLoader(classLoader);
/* 241 */     this.customClassLoader = true;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ClassLoader getClassLoader() {
/* 247 */     if (this.resourceLoader != null && !this.customClassLoader) {
/* 248 */       return this.resourceLoader.getClassLoader();
/*     */     }
/* 250 */     return super.getClassLoader();
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
/*     */   protected final void refreshBeanFactory() throws IllegalStateException {
/* 265 */     if (!this.refreshed.compareAndSet(false, true)) {
/* 266 */       throw new IllegalStateException("GenericApplicationContext does not support multiple refresh attempts: just call 'refresh' once");
/*     */     }
/*     */     
/* 269 */     this.beanFactory.setSerializationId(getId());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void cancelRefresh(BeansException ex) {
/* 274 */     this.beanFactory.setSerializationId(null);
/* 275 */     super.cancelRefresh(ex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void closeBeanFactory() {
/* 284 */     this.beanFactory.setSerializationId(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ConfigurableListableBeanFactory getBeanFactory() {
/* 293 */     return (ConfigurableListableBeanFactory)this.beanFactory;
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
/*     */   public final DefaultListableBeanFactory getDefaultListableBeanFactory() {
/* 305 */     return this.beanFactory;
/*     */   }
/*     */ 
/*     */   
/*     */   public AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException {
/* 310 */     assertBeanFactoryActive();
/* 311 */     return (AutowireCapableBeanFactory)this.beanFactory;
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
/*     */   public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException {
/* 323 */     this.beanFactory.registerBeanDefinition(beanName, beanDefinition);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
/* 328 */     this.beanFactory.removeBeanDefinition(beanName);
/*     */   }
/*     */ 
/*     */   
/*     */   public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
/* 333 */     return this.beanFactory.getBeanDefinition(beanName);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBeanNameInUse(String beanName) {
/* 338 */     return this.beanFactory.isBeanNameInUse(beanName);
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerAlias(String beanName, String alias) {
/* 343 */     this.beanFactory.registerAlias(beanName, alias);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAlias(String alias) {
/* 348 */     this.beanFactory.removeAlias(alias);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAlias(String beanName) {
/* 353 */     return this.beanFactory.isAlias(beanName);
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
/*     */   public final <T> void registerBean(Class<T> beanClass, BeanDefinitionCustomizer... customizers) {
/* 372 */     registerBean((String)null, beanClass, (Supplier<T>)null, customizers);
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
/*     */   public final <T> void registerBean(@Nullable String beanName, Class<T> beanClass, BeanDefinitionCustomizer... customizers) {
/* 389 */     registerBean(beanName, beanClass, (Supplier<T>)null, customizers);
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
/*     */   public final <T> void registerBean(Class<T> beanClass, Supplier<T> supplier, BeanDefinitionCustomizer... customizers) {
/* 407 */     registerBean((String)null, beanClass, supplier, customizers);
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
/*     */   public <T> void registerBean(@Nullable String beanName, Class<T> beanClass, @Nullable Supplier<T> supplier, BeanDefinitionCustomizer... customizers) {
/* 428 */     ClassDerivedBeanDefinition beanDefinition = new ClassDerivedBeanDefinition(beanClass);
/* 429 */     if (supplier != null) {
/* 430 */       beanDefinition.setInstanceSupplier(supplier);
/*     */     }
/* 432 */     for (BeanDefinitionCustomizer customizer : customizers) {
/* 433 */       customizer.customize((BeanDefinition)beanDefinition);
/*     */     }
/*     */     
/* 436 */     String nameToUse = (beanName != null) ? beanName : beanClass.getName();
/* 437 */     registerBeanDefinition(nameToUse, (BeanDefinition)beanDefinition);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ClassDerivedBeanDefinition
/*     */     extends RootBeanDefinition
/*     */   {
/*     */     public ClassDerivedBeanDefinition(Class<?> beanClass) {
/* 449 */       super(beanClass);
/*     */     }
/*     */     
/*     */     public ClassDerivedBeanDefinition(ClassDerivedBeanDefinition original) {
/* 453 */       super(original);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Constructor<?>[] getPreferredConstructors() {
/* 459 */       Class<?> clazz = getBeanClass();
/* 460 */       Constructor<?> primaryCtor = BeanUtils.findPrimaryConstructor(clazz);
/* 461 */       if (primaryCtor != null) {
/* 462 */         return (Constructor<?>[])new Constructor[] { primaryCtor };
/*     */       }
/* 464 */       Constructor[] arrayOfConstructor = (Constructor[])clazz.getConstructors();
/* 465 */       if (arrayOfConstructor.length > 0) {
/* 466 */         return (Constructor<?>[])arrayOfConstructor;
/*     */       }
/* 468 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public RootBeanDefinition cloneBeanDefinition() {
/* 473 */       return new ClassDerivedBeanDefinition(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/support/GenericApplicationContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */