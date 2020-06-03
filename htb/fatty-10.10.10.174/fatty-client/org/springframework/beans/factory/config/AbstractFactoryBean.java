/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.SimpleTypeConverter;
/*     */ import org.springframework.beans.TypeConverter;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.FactoryBeanNotInitializedException;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractFactoryBean<T>
/*     */   implements FactoryBean<T>, BeanClassLoaderAware, BeanFactoryAware, InitializingBean, DisposableBean
/*     */ {
/*  67 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private boolean singleton = true;
/*     */   
/*     */   @Nullable
/*  72 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private BeanFactory beanFactory;
/*     */ 
/*     */   
/*     */   private boolean initialized = false;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private T singletonInstance;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private T earlySingletonInstance;
/*     */ 
/*     */   
/*     */   public void setSingleton(boolean singleton) {
/*  91 */     this.singleton = singleton;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/*  96 */     return this.singleton;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/* 101 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanFactory(@Nullable BeanFactory beanFactory) {
/* 106 */     this.beanFactory = beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected BeanFactory getBeanFactory() {
/* 114 */     return this.beanFactory;
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
/*     */   protected TypeConverter getBeanTypeConverter() {
/* 126 */     BeanFactory beanFactory = getBeanFactory();
/* 127 */     if (beanFactory instanceof ConfigurableBeanFactory) {
/* 128 */       return ((ConfigurableBeanFactory)beanFactory).getTypeConverter();
/*     */     }
/*     */     
/* 131 */     return (TypeConverter)new SimpleTypeConverter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws Exception {
/* 140 */     if (isSingleton()) {
/* 141 */       this.initialized = true;
/* 142 */       this.singletonInstance = createInstance();
/* 143 */       this.earlySingletonInstance = null;
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
/*     */   public final T getObject() throws Exception {
/* 155 */     if (isSingleton()) {
/* 156 */       return this.initialized ? this.singletonInstance : getEarlySingletonInstance();
/*     */     }
/*     */     
/* 159 */     return createInstance();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private T getEarlySingletonInstance() throws Exception {
/* 169 */     Class<?>[] ifcs = getEarlySingletonInterfaces();
/* 170 */     if (ifcs == null) {
/* 171 */       throw new FactoryBeanNotInitializedException(
/* 172 */           getClass().getName() + " does not support circular references");
/*     */     }
/* 174 */     if (this.earlySingletonInstance == null) {
/* 175 */       this.earlySingletonInstance = (T)Proxy.newProxyInstance(this.beanClassLoader, ifcs, new EarlySingletonInvocationHandler());
/*     */     }
/*     */     
/* 178 */     return this.earlySingletonInstance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private T getSingletonInstance() throws IllegalStateException {
/* 188 */     Assert.state(this.initialized, "Singleton instance not initialized yet");
/* 189 */     return this.singletonInstance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() throws Exception {
/* 198 */     if (isSingleton()) {
/* 199 */       destroyInstance(this.singletonInstance);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Class<?>[] getEarlySingletonInterfaces() {
/* 238 */     Class<?> type = getObjectType();
/* 239 */     (new Class[1])[0] = type; return (type != null && type.isInterface()) ? new Class[1] : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void destroyInstance(@Nullable T instance) throws Exception {}
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public abstract Class<?> getObjectType();
/*     */ 
/*     */   
/*     */   protected abstract T createInstance() throws Exception;
/*     */ 
/*     */   
/*     */   private class EarlySingletonInvocationHandler
/*     */     implements InvocationHandler
/*     */   {
/*     */     private EarlySingletonInvocationHandler() {}
/*     */ 
/*     */     
/*     */     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 262 */       if (ReflectionUtils.isEqualsMethod(method))
/*     */       {
/* 264 */         return Boolean.valueOf((proxy == args[0]));
/*     */       }
/* 266 */       if (ReflectionUtils.isHashCodeMethod(method))
/*     */       {
/* 268 */         return Integer.valueOf(System.identityHashCode(proxy));
/*     */       }
/* 270 */       if (!AbstractFactoryBean.this.initialized && ReflectionUtils.isToStringMethod(method)) {
/* 271 */         return "Early singleton proxy for interfaces " + 
/* 272 */           ObjectUtils.nullSafeToString((Object[])AbstractFactoryBean.this.getEarlySingletonInterfaces());
/*     */       }
/*     */       try {
/* 275 */         return method.invoke(AbstractFactoryBean.this.getSingletonInstance(), args);
/*     */       }
/* 277 */       catch (InvocationTargetException ex) {
/* 278 */         throw ex.getTargetException();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/AbstractFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */