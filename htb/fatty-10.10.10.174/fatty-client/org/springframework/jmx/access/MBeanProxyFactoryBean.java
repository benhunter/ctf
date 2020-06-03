/*     */ package org.springframework.jmx.access;
/*     */ 
/*     */ import org.aopalliance.intercept.Interceptor;
/*     */ import org.springframework.aop.framework.ProxyFactory;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.jmx.MBeanServerNotFoundException;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class MBeanProxyFactoryBean
/*     */   extends MBeanClientInterceptor
/*     */   implements FactoryBean<Object>, BeanClassLoaderAware, InitializingBean
/*     */ {
/*     */   @Nullable
/*     */   private Class<?> proxyInterface;
/*     */   @Nullable
/*  56 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Object mbeanProxy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProxyInterface(Class<?> proxyInterface) {
/*  70 */     this.proxyInterface = proxyInterface;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/*  75 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws MBeanServerNotFoundException, MBeanInfoRetrievalException {
/*  84 */     super.afterPropertiesSet();
/*     */     
/*  86 */     if (this.proxyInterface == null) {
/*  87 */       this.proxyInterface = getManagementInterface();
/*  88 */       if (this.proxyInterface == null) {
/*  89 */         throw new IllegalArgumentException("Property 'proxyInterface' or 'managementInterface' is required");
/*     */       
/*     */       }
/*     */     }
/*  93 */     else if (getManagementInterface() == null) {
/*  94 */       setManagementInterface(this.proxyInterface);
/*     */     } 
/*     */     
/*  97 */     this.mbeanProxy = (new ProxyFactory(this.proxyInterface, (Interceptor)this)).getProxy(this.beanClassLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getObject() {
/* 104 */     return this.mbeanProxy;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/* 109 */     return this.proxyInterface;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 114 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/access/MBeanProxyFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */