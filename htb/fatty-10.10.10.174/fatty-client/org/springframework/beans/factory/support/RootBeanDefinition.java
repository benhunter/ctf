/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Executable;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*     */ import org.springframework.core.ResolvableType;
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
/*     */ public class RootBeanDefinition
/*     */   extends AbstractBeanDefinition
/*     */ {
/*     */   @Nullable
/*     */   private BeanDefinitionHolder decoratedDefinition;
/*     */   @Nullable
/*     */   private AnnotatedElement qualifiedElement;
/*     */   boolean allowCaching = true;
/*     */   boolean isFactoryMethodUnique = false;
/*     */   @Nullable
/*     */   volatile ResolvableType targetType;
/*     */   @Nullable
/*     */   volatile Class<?> resolvedTargetType;
/*     */   @Nullable
/*     */   volatile ResolvableType factoryMethodReturnType;
/*     */   @Nullable
/*     */   volatile Method factoryMethodToIntrospect;
/*  83 */   final Object constructorArgumentLock = new Object();
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   Executable resolvedConstructorOrFactoryMethod;
/*     */ 
/*     */   
/*     */   boolean constructorArgumentsResolved = false;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   Object[] resolvedConstructorArguments;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   Object[] preparedConstructorArguments;
/*     */ 
/*     */   
/* 101 */   final Object postProcessingLock = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean postProcessed = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   volatile Boolean beforeInstantiationResolved;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Set<Member> externallyManagedConfigMembers;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Set<String> externallyManagedInitMethods;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Set<String> externallyManagedDestroyMethods;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RootBeanDefinition(@Nullable Class<?> beanClass) {
/* 139 */     setBeanClass(beanClass);
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
/*     */   public <T> RootBeanDefinition(@Nullable Class<T> beanClass, @Nullable Supplier<T> instanceSupplier) {
/* 153 */     setBeanClass(beanClass);
/* 154 */     setInstanceSupplier(instanceSupplier);
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
/*     */   public <T> RootBeanDefinition(@Nullable Class<T> beanClass, String scope, @Nullable Supplier<T> instanceSupplier) {
/* 169 */     setBeanClass(beanClass);
/* 170 */     setScope(scope);
/* 171 */     setInstanceSupplier(instanceSupplier);
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
/*     */   public RootBeanDefinition(@Nullable Class<?> beanClass, int autowireMode, boolean dependencyCheck) {
/* 184 */     setBeanClass(beanClass);
/* 185 */     setAutowireMode(autowireMode);
/* 186 */     if (dependencyCheck && getResolvedAutowireMode() != 3) {
/* 187 */       setDependencyCheck(1);
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
/*     */   public RootBeanDefinition(@Nullable Class<?> beanClass, @Nullable ConstructorArgumentValues cargs, @Nullable MutablePropertyValues pvs) {
/* 201 */     super(cargs, pvs);
/* 202 */     setBeanClass(beanClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RootBeanDefinition(String beanClassName) {
/* 212 */     setBeanClassName(beanClassName);
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
/*     */   public RootBeanDefinition(String beanClassName, ConstructorArgumentValues cargs, MutablePropertyValues pvs) {
/* 224 */     super(cargs, pvs);
/* 225 */     setBeanClassName(beanClassName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RootBeanDefinition(RootBeanDefinition original) {
/* 234 */     super(original);
/* 235 */     this.decoratedDefinition = original.decoratedDefinition;
/* 236 */     this.qualifiedElement = original.qualifiedElement;
/* 237 */     this.allowCaching = original.allowCaching;
/* 238 */     this.isFactoryMethodUnique = original.isFactoryMethodUnique;
/* 239 */     this.targetType = original.targetType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   RootBeanDefinition(BeanDefinition original) {
/* 248 */     super(original);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParentName() {
/* 254 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setParentName(@Nullable String parentName) {
/* 259 */     if (parentName != null) {
/* 260 */       throw new IllegalArgumentException("Root bean cannot be changed into a child bean with parent reference");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDecoratedDefinition(@Nullable BeanDefinitionHolder decoratedDefinition) {
/* 268 */     this.decoratedDefinition = decoratedDefinition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BeanDefinitionHolder getDecoratedDefinition() {
/* 276 */     return this.decoratedDefinition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setQualifiedElement(@Nullable AnnotatedElement qualifiedElement) {
/* 287 */     this.qualifiedElement = qualifiedElement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public AnnotatedElement getQualifiedElement() {
/* 297 */     return this.qualifiedElement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetType(ResolvableType targetType) {
/* 305 */     this.targetType = targetType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetType(@Nullable Class<?> targetType) {
/* 313 */     this.targetType = (targetType != null) ? ResolvableType.forClass(targetType) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getTargetType() {
/* 323 */     if (this.resolvedTargetType != null) {
/* 324 */       return this.resolvedTargetType;
/*     */     }
/* 326 */     ResolvableType targetType = this.targetType;
/* 327 */     return (targetType != null) ? targetType.resolve() : null;
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
/*     */   public ResolvableType getResolvableType() {
/* 339 */     ResolvableType targetType = this.targetType;
/* 340 */     return (targetType != null) ? targetType : ResolvableType.forClass(getBeanClass());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Constructor<?>[] getPreferredConstructors() {
/* 352 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUniqueFactoryMethodName(String name) {
/* 359 */     Assert.hasText(name, "Factory method name must not be empty");
/* 360 */     setFactoryMethodName(name);
/* 361 */     this.isFactoryMethodUnique = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFactoryMethod(Method candidate) {
/* 368 */     return candidate.getName().equals(getFactoryMethodName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Method getResolvedFactoryMethod() {
/* 377 */     return this.factoryMethodToIntrospect;
/*     */   }
/*     */   
/*     */   public void registerExternallyManagedConfigMember(Member configMember) {
/* 381 */     synchronized (this.postProcessingLock) {
/* 382 */       if (this.externallyManagedConfigMembers == null) {
/* 383 */         this.externallyManagedConfigMembers = new HashSet<>(1);
/*     */       }
/* 385 */       this.externallyManagedConfigMembers.add(configMember);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isExternallyManagedConfigMember(Member configMember) {
/* 390 */     synchronized (this.postProcessingLock) {
/* 391 */       return (this.externallyManagedConfigMembers != null && this.externallyManagedConfigMembers
/* 392 */         .contains(configMember));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void registerExternallyManagedInitMethod(String initMethod) {
/* 397 */     synchronized (this.postProcessingLock) {
/* 398 */       if (this.externallyManagedInitMethods == null) {
/* 399 */         this.externallyManagedInitMethods = new HashSet<>(1);
/*     */       }
/* 401 */       this.externallyManagedInitMethods.add(initMethod);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isExternallyManagedInitMethod(String initMethod) {
/* 406 */     synchronized (this.postProcessingLock) {
/* 407 */       return (this.externallyManagedInitMethods != null && this.externallyManagedInitMethods
/* 408 */         .contains(initMethod));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void registerExternallyManagedDestroyMethod(String destroyMethod) {
/* 413 */     synchronized (this.postProcessingLock) {
/* 414 */       if (this.externallyManagedDestroyMethods == null) {
/* 415 */         this.externallyManagedDestroyMethods = new HashSet<>(1);
/*     */       }
/* 417 */       this.externallyManagedDestroyMethods.add(destroyMethod);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isExternallyManagedDestroyMethod(String destroyMethod) {
/* 422 */     synchronized (this.postProcessingLock) {
/* 423 */       return (this.externallyManagedDestroyMethods != null && this.externallyManagedDestroyMethods
/* 424 */         .contains(destroyMethod));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public RootBeanDefinition cloneBeanDefinition() {
/* 431 */     return new RootBeanDefinition(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 436 */     return (this == other || (other instanceof RootBeanDefinition && super.equals(other)));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 441 */     return "Root bean: " + super.toString();
/*     */   }
/*     */   
/*     */   public RootBeanDefinition() {}
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/RootBeanDefinition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */