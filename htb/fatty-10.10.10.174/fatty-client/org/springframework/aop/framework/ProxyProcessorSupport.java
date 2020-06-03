/*     */ package org.springframework.aop.framework;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import org.springframework.beans.factory.Aware;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ 
/*     */ public class ProxyProcessorSupport
/*     */   extends ProxyConfig
/*     */   implements Ordered, BeanClassLoaderAware, AopInfrastructureBean
/*     */ {
/*  46 */   private int order = Integer.MAX_VALUE;
/*     */   
/*     */   @Nullable
/*  49 */   private ClassLoader proxyClassLoader = ClassUtils.getDefaultClassLoader();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean classLoaderConfigured = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOrder(int order) {
/*  61 */     this.order = order;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/*  66 */     return this.order;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProxyClassLoader(@Nullable ClassLoader classLoader) {
/*  76 */     this.proxyClassLoader = classLoader;
/*  77 */     this.classLoaderConfigured = (classLoader != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected ClassLoader getProxyClassLoader() {
/*  85 */     return this.proxyClassLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/*  90 */     if (!this.classLoaderConfigured) {
/*  91 */       this.proxyClassLoader = classLoader;
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
/*     */   protected void evaluateProxyInterfaces(Class<?> beanClass, ProxyFactory proxyFactory) {
/* 105 */     Class<?>[] targetInterfaces = ClassUtils.getAllInterfacesForClass(beanClass, getProxyClassLoader());
/* 106 */     boolean hasReasonableProxyInterface = false;
/* 107 */     for (Class<?> ifc : targetInterfaces) {
/* 108 */       if (!isConfigurationCallbackInterface(ifc) && !isInternalLanguageInterface(ifc) && (ifc
/* 109 */         .getMethods()).length > 0) {
/* 110 */         hasReasonableProxyInterface = true;
/*     */         break;
/*     */       } 
/*     */     } 
/* 114 */     if (hasReasonableProxyInterface) {
/*     */       
/* 116 */       for (Class<?> ifc : targetInterfaces) {
/* 117 */         proxyFactory.addInterface(ifc);
/*     */       }
/*     */     } else {
/*     */       
/* 121 */       proxyFactory.setProxyTargetClass(true);
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
/*     */   protected boolean isConfigurationCallbackInterface(Class<?> ifc) {
/* 134 */     return (InitializingBean.class == ifc || DisposableBean.class == ifc || Closeable.class == ifc || AutoCloseable.class == ifc || 
/* 135 */       ObjectUtils.containsElement((Object[])ifc.getInterfaces(), Aware.class));
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
/*     */   protected boolean isInternalLanguageInterface(Class<?> ifc) {
/* 147 */     return (ifc.getName().equals("groovy.lang.GroovyObject") || ifc
/* 148 */       .getName().endsWith(".cglib.proxy.Factory") || ifc
/* 149 */       .getName().endsWith(".bytebuddy.MockAccess"));
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/ProxyProcessorSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */