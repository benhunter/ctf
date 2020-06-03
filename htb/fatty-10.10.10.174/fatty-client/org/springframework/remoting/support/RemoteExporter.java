/*     */ package org.springframework.remoting.support;
/*     */ 
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.springframework.aop.framework.ProxyFactory;
/*     */ import org.springframework.aop.framework.adapter.AdvisorAdapterRegistry;
/*     */ import org.springframework.aop.framework.adapter.GlobalAdvisorAdapterRegistry;
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
/*     */ public abstract class RemoteExporter
/*     */   extends RemotingSupport
/*     */ {
/*     */   private Object service;
/*     */   private Class<?> serviceInterface;
/*     */   private Boolean registerTraceInterceptor;
/*     */   private Object[] interceptors;
/*     */   
/*     */   public void setService(Object service) {
/*  52 */     this.service = service;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getService() {
/*  59 */     return this.service;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServiceInterface(Class<?> serviceInterface) {
/*  67 */     Assert.notNull(serviceInterface, "'serviceInterface' must not be null");
/*  68 */     Assert.isTrue(serviceInterface.isInterface(), "'serviceInterface' must be an interface");
/*  69 */     this.serviceInterface = serviceInterface;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getServiceInterface() {
/*  76 */     return this.serviceInterface;
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
/*     */   public void setRegisterTraceInterceptor(boolean registerTraceInterceptor) {
/*  92 */     this.registerTraceInterceptor = Boolean.valueOf(registerTraceInterceptor);
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
/*     */   public void setInterceptors(Object[] interceptors) {
/* 104 */     this.interceptors = interceptors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkService() throws IllegalArgumentException {
/* 113 */     Assert.notNull(getService(), "Property 'service' is required");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkServiceInterface() throws IllegalArgumentException {
/* 123 */     Class<?> serviceInterface = getServiceInterface();
/* 124 */     Assert.notNull(serviceInterface, "Property 'serviceInterface' is required");
/*     */     
/* 126 */     Object service = getService();
/* 127 */     if (service instanceof String) {
/* 128 */       throw new IllegalArgumentException("Service [" + service + "] is a String rather than an actual service reference: Have you accidentally specified the service bean name as value instead of as reference?");
/*     */     }
/*     */ 
/*     */     
/* 132 */     if (!serviceInterface.isInstance(service)) {
/* 133 */       throw new IllegalArgumentException("Service interface [" + serviceInterface.getName() + "] needs to be implemented by service [" + service + "] of class [" + service
/*     */           
/* 135 */           .getClass().getName() + "]");
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
/*     */   protected Object getProxyForService() {
/* 151 */     checkService();
/* 152 */     checkServiceInterface();
/*     */     
/* 154 */     ProxyFactory proxyFactory = new ProxyFactory();
/* 155 */     proxyFactory.addInterface(getServiceInterface());
/*     */     
/* 157 */     if ((this.registerTraceInterceptor != null) ? this.registerTraceInterceptor.booleanValue() : (this.interceptors == null)) {
/* 158 */       proxyFactory.addAdvice((Advice)new RemoteInvocationTraceInterceptor(getExporterName()));
/*     */     }
/* 160 */     if (this.interceptors != null) {
/* 161 */       AdvisorAdapterRegistry adapterRegistry = GlobalAdvisorAdapterRegistry.getInstance();
/* 162 */       for (Object interceptor : this.interceptors) {
/* 163 */         proxyFactory.addAdvisor(adapterRegistry.wrap(interceptor));
/*     */       }
/*     */     } 
/*     */     
/* 167 */     proxyFactory.setTarget(getService());
/* 168 */     proxyFactory.setOpaque(true);
/*     */     
/* 170 */     return proxyFactory.getProxy(getBeanClassLoader());
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
/*     */   protected String getExporterName() {
/* 183 */     return ClassUtils.getShortName(getClass());
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/support/RemoteExporter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */