/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Properties;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.FatalBeanException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServiceLocatorFactoryBean
/*     */   implements FactoryBean<Object>, BeanFactoryAware, InitializingBean
/*     */ {
/*     */   @Nullable
/*     */   private Class<?> serviceLocatorInterface;
/*     */   @Nullable
/*     */   private Constructor<Exception> serviceLocatorExceptionConstructor;
/*     */   @Nullable
/*     */   private Properties serviceMappings;
/*     */   @Nullable
/*     */   private ListableBeanFactory beanFactory;
/*     */   @Nullable
/*     */   private Object proxy;
/*     */   
/*     */   public void setServiceLocatorInterface(Class<?> interfaceType) {
/* 217 */     this.serviceLocatorInterface = interfaceType;
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
/*     */   public void setServiceLocatorExceptionClass(Class<? extends Exception> serviceLocatorExceptionClass) {
/* 233 */     this
/* 234 */       .serviceLocatorExceptionConstructor = determineServiceLocatorExceptionConstructor(serviceLocatorExceptionClass);
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
/*     */   public void setServiceMappings(Properties serviceMappings) {
/* 248 */     this.serviceMappings = serviceMappings;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
/* 253 */     if (!(beanFactory instanceof ListableBeanFactory)) {
/* 254 */       throw new FatalBeanException("ServiceLocatorFactoryBean needs to run in a BeanFactory that is a ListableBeanFactory");
/*     */     }
/*     */     
/* 257 */     this.beanFactory = (ListableBeanFactory)beanFactory;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 262 */     if (this.serviceLocatorInterface == null) {
/* 263 */       throw new IllegalArgumentException("Property 'serviceLocatorInterface' is required");
/*     */     }
/*     */ 
/*     */     
/* 267 */     this.proxy = Proxy.newProxyInstance(this.serviceLocatorInterface
/* 268 */         .getClassLoader(), new Class[] { this.serviceLocatorInterface }, new ServiceLocatorInvocationHandler());
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
/*     */   protected Constructor<Exception> determineServiceLocatorExceptionConstructor(Class<? extends Exception> exceptionClass) {
/*     */     try {
/* 287 */       return (Constructor)exceptionClass.getConstructor(new Class[] { String.class, Throwable.class });
/*     */     }
/* 289 */     catch (NoSuchMethodException ex) {
/*     */       try {
/* 291 */         return (Constructor)exceptionClass.getConstructor(new Class[] { Throwable.class });
/*     */       }
/* 293 */       catch (NoSuchMethodException ex2) {
/*     */         try {
/* 295 */           return (Constructor)exceptionClass.getConstructor(new Class[] { String.class });
/*     */         }
/* 297 */         catch (NoSuchMethodException ex3) {
/* 298 */           throw new IllegalArgumentException("Service locator exception [" + exceptionClass
/* 299 */               .getName() + "] neither has a (String, Throwable) constructor nor a (String) constructor");
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
/*     */   protected Exception createServiceLocatorException(Constructor<Exception> exceptionConstructor, BeansException cause) {
/* 317 */     Class<?>[] paramTypes = exceptionConstructor.getParameterTypes();
/* 318 */     Object[] args = new Object[paramTypes.length];
/* 319 */     for (int i = 0; i < paramTypes.length; i++) {
/* 320 */       if (String.class == paramTypes[i]) {
/* 321 */         args[i] = cause.getMessage();
/*     */       }
/* 323 */       else if (paramTypes[i].isInstance(cause)) {
/* 324 */         args[i] = cause;
/*     */       } 
/*     */     } 
/* 327 */     return (Exception)BeanUtils.instantiateClass(exceptionConstructor, args);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getObject() {
/* 334 */     return this.proxy;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/* 339 */     return this.serviceLocatorInterface;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 344 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private class ServiceLocatorInvocationHandler
/*     */     implements InvocationHandler
/*     */   {
/*     */     private ServiceLocatorInvocationHandler() {}
/*     */ 
/*     */     
/*     */     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 355 */       if (ReflectionUtils.isEqualsMethod(method))
/*     */       {
/* 357 */         return Boolean.valueOf((proxy == args[0]));
/*     */       }
/* 359 */       if (ReflectionUtils.isHashCodeMethod(method))
/*     */       {
/* 361 */         return Integer.valueOf(System.identityHashCode(proxy));
/*     */       }
/* 363 */       if (ReflectionUtils.isToStringMethod(method)) {
/* 364 */         return "Service locator: " + ServiceLocatorFactoryBean.this.serviceLocatorInterface;
/*     */       }
/*     */       
/* 367 */       return invokeServiceLocatorMethod(method, args);
/*     */     }
/*     */ 
/*     */     
/*     */     private Object invokeServiceLocatorMethod(Method method, Object[] args) throws Exception {
/* 372 */       Class<?> serviceLocatorMethodReturnType = getServiceLocatorMethodReturnType(method);
/*     */       try {
/* 374 */         String beanName = tryGetBeanName(args);
/* 375 */         Assert.state((ServiceLocatorFactoryBean.this.beanFactory != null), "No BeanFactory available");
/* 376 */         if (StringUtils.hasLength(beanName))
/*     */         {
/* 378 */           return ServiceLocatorFactoryBean.this.beanFactory.getBean(beanName, serviceLocatorMethodReturnType);
/*     */         }
/*     */ 
/*     */         
/* 382 */         return ServiceLocatorFactoryBean.this.beanFactory.getBean(serviceLocatorMethodReturnType);
/*     */       
/*     */       }
/* 385 */       catch (BeansException ex) {
/* 386 */         if (ServiceLocatorFactoryBean.this.serviceLocatorExceptionConstructor != null) {
/* 387 */           throw ServiceLocatorFactoryBean.this.createServiceLocatorException(ServiceLocatorFactoryBean.this.serviceLocatorExceptionConstructor, ex);
/*     */         }
/* 389 */         throw ex;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private String tryGetBeanName(@Nullable Object[] args) {
/* 397 */       String beanName = "";
/* 398 */       if (args != null && args.length == 1 && args[0] != null) {
/* 399 */         beanName = args[0].toString();
/*     */       }
/*     */       
/* 402 */       if (ServiceLocatorFactoryBean.this.serviceMappings != null) {
/* 403 */         String mappedName = ServiceLocatorFactoryBean.this.serviceMappings.getProperty(beanName);
/* 404 */         if (mappedName != null) {
/* 405 */           beanName = mappedName;
/*     */         }
/*     */       } 
/* 408 */       return beanName;
/*     */     }
/*     */     
/*     */     private Class<?> getServiceLocatorMethodReturnType(Method method) throws NoSuchMethodException {
/* 412 */       Assert.state((ServiceLocatorFactoryBean.this.serviceLocatorInterface != null), "No service locator interface specified");
/* 413 */       Class<?>[] paramTypes = method.getParameterTypes();
/* 414 */       Method interfaceMethod = ServiceLocatorFactoryBean.this.serviceLocatorInterface.getMethod(method.getName(), paramTypes);
/* 415 */       Class<?> serviceLocatorReturnType = interfaceMethod.getReturnType();
/*     */ 
/*     */       
/* 418 */       if (paramTypes.length > 1 || void.class == serviceLocatorReturnType) {
/* 419 */         throw new UnsupportedOperationException("May only call methods with signature '<type> xxx()' or '<type> xxx(<idtype> id)' on factory interface, but tried to call: " + interfaceMethod);
/*     */       }
/*     */ 
/*     */       
/* 423 */       return serviceLocatorReturnType;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/ServiceLocatorFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */