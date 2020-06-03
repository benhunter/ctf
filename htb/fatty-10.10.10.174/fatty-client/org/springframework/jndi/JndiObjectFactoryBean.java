/*     */ package org.springframework.jndi;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.NamingException;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.aop.framework.ProxyFactory;
/*     */ import org.springframework.beans.SimpleTypeConverter;
/*     */ import org.springframework.beans.TypeConverter;
/*     */ import org.springframework.beans.TypeMismatchException;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class JndiObjectFactoryBean
/*     */   extends JndiObjectLocator
/*     */   implements FactoryBean<Object>, BeanFactoryAware, BeanClassLoaderAware
/*     */ {
/*     */   @Nullable
/*     */   private Class<?>[] proxyInterfaces;
/*     */   private boolean lookupOnStartup = true;
/*     */   private boolean cache = true;
/*     */   private boolean exposeAccessContext = false;
/*     */   @Nullable
/*     */   private Object defaultObject;
/*     */   @Nullable
/*     */   private ConfigurableBeanFactory beanFactory;
/*     */   @Nullable
/*  91 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Object jndiObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProxyInterface(Class<?> proxyInterface) {
/* 107 */     this.proxyInterfaces = new Class[] { proxyInterface };
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
/*     */   public void setProxyInterfaces(Class<?>... proxyInterfaces) {
/* 120 */     this.proxyInterfaces = proxyInterfaces;
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
/*     */   public void setLookupOnStartup(boolean lookupOnStartup) {
/* 132 */     this.lookupOnStartup = lookupOnStartup;
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
/*     */   public void setCache(boolean cache) {
/* 145 */     this.cache = cache;
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
/*     */   public void setExposeAccessContext(boolean exposeAccessContext) {
/* 158 */     this.exposeAccessContext = exposeAccessContext;
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
/*     */   public void setDefaultObject(Object defaultObject) {
/* 175 */     this.defaultObject = defaultObject;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 180 */     if (beanFactory instanceof ConfigurableBeanFactory)
/*     */     {
/*     */       
/* 183 */       this.beanFactory = (ConfigurableBeanFactory)beanFactory;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/* 189 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws IllegalArgumentException, NamingException {
/* 198 */     super.afterPropertiesSet();
/*     */     
/* 200 */     if (this.proxyInterfaces != null || !this.lookupOnStartup || !this.cache || this.exposeAccessContext) {
/*     */       
/* 202 */       if (this.defaultObject != null) {
/* 203 */         throw new IllegalArgumentException("'defaultObject' is not supported in combination with 'proxyInterface'");
/*     */       }
/*     */ 
/*     */       
/* 207 */       this.jndiObject = JndiObjectProxyFactory.createJndiObjectProxy(this);
/*     */     } else {
/*     */       
/* 210 */       if (this.defaultObject != null && getExpectedType() != null && 
/* 211 */         !getExpectedType().isInstance(this.defaultObject)) {
/*     */         
/* 213 */         TypeConverter converter = (this.beanFactory != null) ? this.beanFactory.getTypeConverter() : (TypeConverter)new SimpleTypeConverter();
/*     */         try {
/* 215 */           this.defaultObject = converter.convertIfNecessary(this.defaultObject, getExpectedType());
/*     */         }
/* 217 */         catch (TypeMismatchException ex) {
/* 218 */           throw new IllegalArgumentException("Default object [" + this.defaultObject + "] of type [" + this.defaultObject
/* 219 */               .getClass().getName() + "] is not of expected type [" + 
/* 220 */               getExpectedType().getName() + "] and cannot be converted either", ex);
/*     */         } 
/*     */       } 
/*     */       
/* 224 */       this.jndiObject = lookupWithFallback();
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
/*     */   protected Object lookupWithFallback() throws NamingException {
/* 236 */     ClassLoader originalClassLoader = ClassUtils.overrideThreadContextClassLoader(this.beanClassLoader);
/*     */     try {
/* 238 */       return lookup();
/*     */     }
/* 240 */     catch (TypeMismatchNamingException ex) {
/*     */ 
/*     */       
/* 243 */       throw ex;
/*     */     }
/* 245 */     catch (NamingException ex) {
/* 246 */       if (this.defaultObject != null) {
/* 247 */         if (this.logger.isTraceEnabled()) {
/* 248 */           this.logger.trace("JNDI lookup failed - returning specified default object instead", ex);
/*     */         }
/* 250 */         else if (this.logger.isDebugEnabled()) {
/* 251 */           this.logger.debug("JNDI lookup failed - returning specified default object instead: " + ex);
/*     */         } 
/* 253 */         return this.defaultObject;
/*     */       } 
/* 255 */       throw ex;
/*     */     } finally {
/*     */       
/* 258 */       if (originalClassLoader != null) {
/* 259 */         Thread.currentThread().setContextClassLoader(originalClassLoader);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getObject() {
/* 271 */     return this.jndiObject;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/* 276 */     if (this.proxyInterfaces != null) {
/* 277 */       if (this.proxyInterfaces.length == 1) {
/* 278 */         return this.proxyInterfaces[0];
/*     */       }
/* 280 */       if (this.proxyInterfaces.length > 1) {
/* 281 */         return createCompositeInterface(this.proxyInterfaces);
/*     */       }
/*     */     } 
/* 284 */     if (this.jndiObject != null) {
/* 285 */       return this.jndiObject.getClass();
/*     */     }
/*     */     
/* 288 */     return getExpectedType();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 294 */     return true;
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
/*     */   protected Class<?> createCompositeInterface(Class<?>[] interfaces) {
/* 308 */     return ClassUtils.createCompositeInterface(interfaces, this.beanClassLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class JndiObjectProxyFactory
/*     */   {
/*     */     private static Object createJndiObjectProxy(JndiObjectFactoryBean jof) throws NamingException {
/* 319 */       JndiObjectTargetSource targetSource = new JndiObjectTargetSource();
/* 320 */       targetSource.setJndiTemplate(jof.getJndiTemplate());
/* 321 */       String jndiName = jof.getJndiName();
/* 322 */       Assert.state((jndiName != null), "No JNDI name specified");
/* 323 */       targetSource.setJndiName(jndiName);
/* 324 */       targetSource.setExpectedType(jof.getExpectedType());
/* 325 */       targetSource.setResourceRef(jof.isResourceRef());
/* 326 */       targetSource.setLookupOnStartup(jof.lookupOnStartup);
/* 327 */       targetSource.setCache(jof.cache);
/* 328 */       targetSource.afterPropertiesSet();
/*     */ 
/*     */       
/* 331 */       ProxyFactory proxyFactory = new ProxyFactory();
/* 332 */       if (jof.proxyInterfaces != null) {
/* 333 */         proxyFactory.setInterfaces(jof.proxyInterfaces);
/*     */       } else {
/*     */         
/* 336 */         Class<?> targetClass = targetSource.getTargetClass();
/* 337 */         if (targetClass == null) {
/* 338 */           throw new IllegalStateException("Cannot deactivate 'lookupOnStartup' without specifying a 'proxyInterface' or 'expectedType'");
/*     */         }
/*     */         
/* 341 */         Class<?>[] ifcs = ClassUtils.getAllInterfacesForClass(targetClass, jof.beanClassLoader);
/* 342 */         for (Class<?> ifc : ifcs) {
/* 343 */           if (Modifier.isPublic(ifc.getModifiers())) {
/* 344 */             proxyFactory.addInterface(ifc);
/*     */           }
/*     */         } 
/*     */       } 
/* 348 */       if (jof.exposeAccessContext) {
/* 349 */         proxyFactory.addAdvice((Advice)new JndiObjectFactoryBean.JndiContextExposingInterceptor(jof.getJndiTemplate()));
/*     */       }
/* 351 */       proxyFactory.setTargetSource(targetSource);
/* 352 */       return proxyFactory.getProxy(jof.beanClassLoader);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class JndiContextExposingInterceptor
/*     */     implements MethodInterceptor
/*     */   {
/*     */     private final JndiTemplate jndiTemplate;
/*     */ 
/*     */ 
/*     */     
/*     */     public JndiContextExposingInterceptor(JndiTemplate jndiTemplate) {
/* 366 */       this.jndiTemplate = jndiTemplate;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object invoke(MethodInvocation invocation) throws Throwable {
/* 371 */       Context ctx = isEligible(invocation.getMethod()) ? this.jndiTemplate.getContext() : null;
/*     */       try {
/* 373 */         return invocation.proceed();
/*     */       } finally {
/*     */         
/* 376 */         this.jndiTemplate.releaseContext(ctx);
/*     */       } 
/*     */     }
/*     */     
/*     */     protected boolean isEligible(Method method) {
/* 381 */       return (Object.class != method.getDeclaringClass());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jndi/JndiObjectFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */