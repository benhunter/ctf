/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionCustomizer;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.context.support.GenericApplicationContext;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.core.env.Environment;
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
/*     */ public class AnnotationConfigApplicationContext
/*     */   extends GenericApplicationContext
/*     */   implements AnnotationConfigRegistry
/*     */ {
/*     */   private final AnnotatedBeanDefinitionReader reader;
/*     */   private final ClassPathBeanDefinitionScanner scanner;
/*     */   
/*     */   public AnnotationConfigApplicationContext() {
/*  65 */     this.reader = new AnnotatedBeanDefinitionReader((BeanDefinitionRegistry)this);
/*  66 */     this.scanner = new ClassPathBeanDefinitionScanner((BeanDefinitionRegistry)this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationConfigApplicationContext(DefaultListableBeanFactory beanFactory) {
/*  74 */     super(beanFactory);
/*  75 */     this.reader = new AnnotatedBeanDefinitionReader((BeanDefinitionRegistry)this);
/*  76 */     this.scanner = new ClassPathBeanDefinitionScanner((BeanDefinitionRegistry)this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationConfigApplicationContext(Class<?>... annotatedClasses) {
/*  86 */     this();
/*  87 */     register(annotatedClasses);
/*  88 */     refresh();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationConfigApplicationContext(String... basePackages) {
/*  97 */     this();
/*  98 */     scan(basePackages);
/*  99 */     refresh();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnvironment(ConfigurableEnvironment environment) {
/* 109 */     super.setEnvironment(environment);
/* 110 */     this.reader.setEnvironment((Environment)environment);
/* 111 */     this.scanner.setEnvironment((Environment)environment);
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
/*     */   public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator) {
/* 124 */     this.reader.setBeanNameGenerator(beanNameGenerator);
/* 125 */     this.scanner.setBeanNameGenerator(beanNameGenerator);
/* 126 */     getBeanFactory().registerSingleton("org.springframework.context.annotation.internalConfigurationBeanNameGenerator", beanNameGenerator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScopeMetadataResolver(ScopeMetadataResolver scopeMetadataResolver) {
/* 137 */     this.reader.setScopeMetadataResolver(scopeMetadataResolver);
/* 138 */     this.scanner.setScopeMetadataResolver(scopeMetadataResolver);
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
/*     */   public void register(Class<?>... annotatedClasses) {
/* 156 */     Assert.notEmpty((Object[])annotatedClasses, "At least one annotated class must be specified");
/* 157 */     this.reader.register(annotatedClasses);
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
/*     */   public void scan(String... basePackages) {
/* 169 */     Assert.notEmpty((Object[])basePackages, "At least one base package must be specified");
/* 170 */     this.scanner.scan(basePackages);
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
/*     */   public <T> void registerBean(Class<T> annotatedClass, Object... constructorArguments) {
/* 191 */     registerBean((String)null, annotatedClass, constructorArguments);
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
/*     */   public <T> void registerBean(@Nullable String beanName, Class<T> annotatedClass, Object... constructorArguments) {
/* 207 */     this.reader.doRegisterBean(annotatedClass, null, beanName, null, new BeanDefinitionCustomizer[] { bd -> {
/*     */             for (Object arg : constructorArguments) {
/*     */               bd.getConstructorArgumentValues().addGenericArgumentValue(arg);
/*     */             }
/*     */           } });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> void registerBean(@Nullable String beanName, Class<T> beanClass, @Nullable Supplier<T> supplier, BeanDefinitionCustomizer... customizers) {
/* 219 */     this.reader.doRegisterBean(beanClass, supplier, beanName, null, customizers);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/AnnotationConfigApplicationContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */