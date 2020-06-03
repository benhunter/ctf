/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import org.springframework.beans.BeanInstantiationException;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ public class SimpleInstantiationStrategy
/*     */   implements InstantiationStrategy
/*     */ {
/*  46 */   private static final ThreadLocal<Method> currentlyInvokedFactoryMethod = new ThreadLocal<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static Method getCurrentlyInvokedFactoryMethod() {
/*  56 */     return currentlyInvokedFactoryMethod.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object instantiate(RootBeanDefinition bd, @Nullable String beanName, BeanFactory owner) {
/*  63 */     if (!bd.hasMethodOverrides()) {
/*     */       Constructor<?> constructorToUse;
/*  65 */       synchronized (bd.constructorArgumentLock) {
/*  66 */         constructorToUse = (Constructor)bd.resolvedConstructorOrFactoryMethod;
/*  67 */         if (constructorToUse == null) {
/*  68 */           Class<?> clazz = bd.getBeanClass();
/*  69 */           if (clazz.isInterface()) {
/*  70 */             throw new BeanInstantiationException(clazz, "Specified class is an interface");
/*     */           }
/*     */           try {
/*  73 */             if (System.getSecurityManager() != null) {
/*  74 */               constructorToUse = AccessController.<Constructor>doPrivileged(() -> rec$.getDeclaredConstructor(new Class[0]));
/*     */             }
/*     */             else {
/*     */               
/*  78 */               constructorToUse = clazz.getDeclaredConstructor(new Class[0]);
/*     */             } 
/*  80 */             bd.resolvedConstructorOrFactoryMethod = constructorToUse;
/*     */           }
/*  82 */           catch (Throwable ex) {
/*  83 */             throw new BeanInstantiationException(clazz, "No default constructor found", ex);
/*     */           } 
/*     */         } 
/*     */       } 
/*  87 */       return BeanUtils.instantiateClass(constructorToUse, new Object[0]);
/*     */     } 
/*     */ 
/*     */     
/*  91 */     return instantiateWithMethodInjection(bd, beanName, owner);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object instantiateWithMethodInjection(RootBeanDefinition bd, @Nullable String beanName, BeanFactory owner) {
/* 102 */     throw new UnsupportedOperationException("Method Injection not supported in SimpleInstantiationStrategy");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object instantiate(RootBeanDefinition bd, @Nullable String beanName, BeanFactory owner, Constructor<?> ctor, Object... args) {
/* 109 */     if (!bd.hasMethodOverrides()) {
/* 110 */       if (System.getSecurityManager() != null)
/*     */       {
/* 112 */         AccessController.doPrivileged(() -> {
/*     */               ReflectionUtils.makeAccessible(ctor);
/*     */               return null;
/*     */             });
/*     */       }
/* 117 */       return BeanUtils.instantiateClass(ctor, args);
/*     */     } 
/*     */     
/* 120 */     return instantiateWithMethodInjection(bd, beanName, owner, ctor, args);
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
/*     */   protected Object instantiateWithMethodInjection(RootBeanDefinition bd, @Nullable String beanName, BeanFactory owner, @Nullable Constructor<?> ctor, Object... args) {
/* 133 */     throw new UnsupportedOperationException("Method Injection not supported in SimpleInstantiationStrategy");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object instantiate(RootBeanDefinition bd, @Nullable String beanName, BeanFactory owner, @Nullable Object factoryBean, Method factoryMethod, Object... args) {
/*     */     try {
/* 141 */       if (System.getSecurityManager() != null) {
/* 142 */         AccessController.doPrivileged(() -> {
/*     */               ReflectionUtils.makeAccessible(factoryMethod);
/*     */               
/*     */               return null;
/*     */             });
/*     */       } else {
/* 148 */         ReflectionUtils.makeAccessible(factoryMethod);
/*     */       } 
/*     */       
/* 151 */       Method priorInvokedFactoryMethod = currentlyInvokedFactoryMethod.get();
/*     */       try {
/* 153 */         currentlyInvokedFactoryMethod.set(factoryMethod);
/* 154 */         Object result = factoryMethod.invoke(factoryBean, args);
/* 155 */         if (result == null) {
/* 156 */           result = new NullBean();
/*     */         }
/* 158 */         return result;
/*     */       } finally {
/*     */         
/* 161 */         if (priorInvokedFactoryMethod != null) {
/* 162 */           currentlyInvokedFactoryMethod.set(priorInvokedFactoryMethod);
/*     */         } else {
/*     */           
/* 165 */           currentlyInvokedFactoryMethod.remove();
/*     */         }
/*     */       
/*     */       } 
/* 169 */     } catch (IllegalArgumentException ex) {
/* 170 */       throw new BeanInstantiationException(factoryMethod, "Illegal arguments to factory method '" + factoryMethod
/* 171 */           .getName() + "'; args: " + 
/* 172 */           StringUtils.arrayToCommaDelimitedString(args), ex);
/*     */     }
/* 174 */     catch (IllegalAccessException ex) {
/* 175 */       throw new BeanInstantiationException(factoryMethod, "Cannot access factory method '" + factoryMethod
/* 176 */           .getName() + "'; is it public?", ex);
/*     */     }
/* 178 */     catch (InvocationTargetException ex) {
/* 179 */       String msg = "Factory method '" + factoryMethod.getName() + "' threw exception";
/* 180 */       if (bd.getFactoryBeanName() != null && owner instanceof ConfigurableBeanFactory && ((ConfigurableBeanFactory)owner)
/* 181 */         .isCurrentlyInCreation(bd.getFactoryBeanName())) {
/* 182 */         msg = "Circular reference involving containing bean '" + bd.getFactoryBeanName() + "' - consider declaring the factory method as static for independence from its containing instance. " + msg;
/*     */       }
/*     */       
/* 185 */       throw new BeanInstantiationException(factoryMethod, msg, ex.getTargetException());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/SimpleInstantiationStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */