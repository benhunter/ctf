/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionCustomizer;
/*     */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BeanDefinitionBuilder
/*     */ {
/*     */   private final AbstractBeanDefinition beanDefinition;
/*     */   private int constructorArgIndex;
/*     */   
/*     */   public static BeanDefinitionBuilder genericBeanDefinition() {
/*  43 */     return new BeanDefinitionBuilder(new GenericBeanDefinition());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BeanDefinitionBuilder genericBeanDefinition(String beanClassName) {
/*  51 */     BeanDefinitionBuilder builder = new BeanDefinitionBuilder(new GenericBeanDefinition());
/*  52 */     builder.beanDefinition.setBeanClassName(beanClassName);
/*  53 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BeanDefinitionBuilder genericBeanDefinition(Class<?> beanClass) {
/*  61 */     BeanDefinitionBuilder builder = new BeanDefinitionBuilder(new GenericBeanDefinition());
/*  62 */     builder.beanDefinition.setBeanClass(beanClass);
/*  63 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> BeanDefinitionBuilder genericBeanDefinition(Class<T> beanClass, Supplier<T> instanceSupplier) {
/*  73 */     BeanDefinitionBuilder builder = new BeanDefinitionBuilder(new GenericBeanDefinition());
/*  74 */     builder.beanDefinition.setBeanClass(beanClass);
/*  75 */     builder.beanDefinition.setInstanceSupplier(instanceSupplier);
/*  76 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BeanDefinitionBuilder rootBeanDefinition(String beanClassName) {
/*  84 */     return rootBeanDefinition(beanClassName, (String)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BeanDefinitionBuilder rootBeanDefinition(String beanClassName, @Nullable String factoryMethodName) {
/*  93 */     BeanDefinitionBuilder builder = new BeanDefinitionBuilder(new RootBeanDefinition());
/*  94 */     builder.beanDefinition.setBeanClassName(beanClassName);
/*  95 */     builder.beanDefinition.setFactoryMethodName(factoryMethodName);
/*  96 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BeanDefinitionBuilder rootBeanDefinition(Class<?> beanClass) {
/* 104 */     return rootBeanDefinition(beanClass, (String)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BeanDefinitionBuilder rootBeanDefinition(Class<?> beanClass, @Nullable String factoryMethodName) {
/* 113 */     BeanDefinitionBuilder builder = new BeanDefinitionBuilder(new RootBeanDefinition());
/* 114 */     builder.beanDefinition.setBeanClass(beanClass);
/* 115 */     builder.beanDefinition.setFactoryMethodName(factoryMethodName);
/* 116 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BeanDefinitionBuilder childBeanDefinition(String parentName) {
/* 124 */     return new BeanDefinitionBuilder(new ChildBeanDefinition(parentName));
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
/*     */   private BeanDefinitionBuilder(AbstractBeanDefinition beanDefinition) {
/* 143 */     this.beanDefinition = beanDefinition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractBeanDefinition getRawBeanDefinition() {
/* 151 */     return this.beanDefinition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractBeanDefinition getBeanDefinition() {
/* 158 */     this.beanDefinition.validate();
/* 159 */     return this.beanDefinition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionBuilder setParentName(String parentName) {
/* 167 */     this.beanDefinition.setParentName(parentName);
/* 168 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionBuilder setFactoryMethod(String factoryMethod) {
/* 176 */     this.beanDefinition.setFactoryMethodName(factoryMethod);
/* 177 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionBuilder setFactoryMethodOnBean(String factoryMethod, String factoryBean) {
/* 188 */     this.beanDefinition.setFactoryMethodName(factoryMethod);
/* 189 */     this.beanDefinition.setFactoryBeanName(factoryBean);
/* 190 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionBuilder addConstructorArgValue(@Nullable Object value) {
/* 198 */     this.beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(this.constructorArgIndex++, value);
/*     */     
/* 200 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionBuilder addConstructorArgReference(String beanName) {
/* 208 */     this.beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(this.constructorArgIndex++, new RuntimeBeanReference(beanName));
/*     */     
/* 210 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionBuilder addPropertyValue(String name, @Nullable Object value) {
/* 217 */     this.beanDefinition.getPropertyValues().add(name, value);
/* 218 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionBuilder addPropertyReference(String name, String beanName) {
/* 227 */     this.beanDefinition.getPropertyValues().add(name, new RuntimeBeanReference(beanName));
/* 228 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionBuilder setInitMethodName(@Nullable String methodName) {
/* 235 */     this.beanDefinition.setInitMethodName(methodName);
/* 236 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionBuilder setDestroyMethodName(@Nullable String methodName) {
/* 243 */     this.beanDefinition.setDestroyMethodName(methodName);
/* 244 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionBuilder setScope(@Nullable String scope) {
/* 254 */     this.beanDefinition.setScope(scope);
/* 255 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionBuilder setAbstract(boolean flag) {
/* 262 */     this.beanDefinition.setAbstract(flag);
/* 263 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionBuilder setLazyInit(boolean lazy) {
/* 270 */     this.beanDefinition.setLazyInit(lazy);
/* 271 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionBuilder setAutowireMode(int autowireMode) {
/* 278 */     this.beanDefinition.setAutowireMode(autowireMode);
/* 279 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionBuilder setDependencyCheck(int dependencyCheck) {
/* 286 */     this.beanDefinition.setDependencyCheck(dependencyCheck);
/* 287 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionBuilder addDependsOn(String beanName) {
/* 295 */     if (this.beanDefinition.getDependsOn() == null) {
/* 296 */       this.beanDefinition.setDependsOn(new String[] { beanName });
/*     */     } else {
/*     */       
/* 299 */       String[] added = (String[])ObjectUtils.addObjectToArray((Object[])this.beanDefinition.getDependsOn(), beanName);
/* 300 */       this.beanDefinition.setDependsOn(added);
/*     */     } 
/* 302 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionBuilder setRole(int role) {
/* 309 */     this.beanDefinition.setRole(role);
/* 310 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanDefinitionBuilder applyCustomizers(BeanDefinitionCustomizer... customizers) {
/* 318 */     for (BeanDefinitionCustomizer customizer : customizers) {
/* 319 */       customizer.customize(this.beanDefinition);
/*     */     }
/* 321 */     return this;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/BeanDefinitionBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */