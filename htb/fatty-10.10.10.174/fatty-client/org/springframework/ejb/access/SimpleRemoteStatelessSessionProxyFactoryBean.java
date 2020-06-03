/*     */ package org.springframework.ejb.access;
/*     */ 
/*     */ import javax.naming.NamingException;
/*     */ import org.aopalliance.intercept.Interceptor;
/*     */ import org.springframework.aop.framework.ProxyFactory;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.FactoryBean;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleRemoteStatelessSessionProxyFactoryBean
/*     */   extends SimpleRemoteSlsbInvokerInterceptor
/*     */   implements FactoryBean<Object>, BeanClassLoaderAware
/*     */ {
/*     */   @Nullable
/*     */   private Class<?> businessInterface;
/*     */   @Nullable
/*  70 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Object proxy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBusinessInterface(@Nullable Class<?> businessInterface) {
/*  88 */     this.businessInterface = businessInterface;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getBusinessInterface() {
/*  96 */     return this.businessInterface;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/* 101 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws NamingException {
/* 106 */     super.afterPropertiesSet();
/* 107 */     if (this.businessInterface == null) {
/* 108 */       throw new IllegalArgumentException("businessInterface is required");
/*     */     }
/* 110 */     this.proxy = (new ProxyFactory(this.businessInterface, (Interceptor)this)).getProxy(this.beanClassLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getObject() {
/* 117 */     return this.proxy;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/* 122 */     return this.businessInterface;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 127 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/ejb/access/SimpleRemoteStatelessSessionProxyFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */