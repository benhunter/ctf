/*     */ package org.springframework.aop.scope;
/*     */ 
/*     */ import java.lang.reflect.Modifier;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.springframework.aop.TargetSource;
/*     */ import org.springframework.aop.framework.AopInfrastructureBean;
/*     */ import org.springframework.aop.framework.ProxyConfig;
/*     */ import org.springframework.aop.framework.ProxyFactory;
/*     */ import org.springframework.aop.support.DelegatingIntroductionInterceptor;
/*     */ import org.springframework.aop.target.SimpleBeanTargetSource;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.FactoryBeanNotInitializedException;
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
/*     */ public class ScopedProxyFactoryBean
/*     */   extends ProxyConfig
/*     */   implements FactoryBean<Object>, BeanFactoryAware, AopInfrastructureBean
/*     */ {
/*  59 */   private final SimpleBeanTargetSource scopedTargetSource = new SimpleBeanTargetSource();
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String targetBeanName;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Object proxy;
/*     */ 
/*     */ 
/*     */   
/*     */   public ScopedProxyFactoryBean() {
/*  74 */     setProxyTargetClass(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetBeanName(String targetBeanName) {
/*  82 */     this.targetBeanName = targetBeanName;
/*  83 */     this.scopedTargetSource.setTargetBeanName(targetBeanName);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/*  88 */     if (!(beanFactory instanceof ConfigurableBeanFactory)) {
/*  89 */       throw new IllegalStateException("Not running in a ConfigurableBeanFactory: " + beanFactory);
/*     */     }
/*  91 */     ConfigurableBeanFactory cbf = (ConfigurableBeanFactory)beanFactory;
/*     */     
/*  93 */     this.scopedTargetSource.setBeanFactory(beanFactory);
/*     */     
/*  95 */     ProxyFactory pf = new ProxyFactory();
/*  96 */     pf.copyFrom(this);
/*  97 */     pf.setTargetSource((TargetSource)this.scopedTargetSource);
/*     */     
/*  99 */     Assert.notNull(this.targetBeanName, "Property 'targetBeanName' is required");
/* 100 */     Class<?> beanType = beanFactory.getType(this.targetBeanName);
/* 101 */     if (beanType == null) {
/* 102 */       throw new IllegalStateException("Cannot create scoped proxy for bean '" + this.targetBeanName + "': Target type could not be determined at the time of proxy creation.");
/*     */     }
/*     */     
/* 105 */     if (!isProxyTargetClass() || beanType.isInterface() || Modifier.isPrivate(beanType.getModifiers())) {
/* 106 */       pf.setInterfaces(ClassUtils.getAllInterfacesForClass(beanType, cbf.getBeanClassLoader()));
/*     */     }
/*     */ 
/*     */     
/* 110 */     ScopedObject scopedObject = new DefaultScopedObject(cbf, this.scopedTargetSource.getTargetBeanName());
/* 111 */     pf.addAdvice((Advice)new DelegatingIntroductionInterceptor(scopedObject));
/*     */ 
/*     */ 
/*     */     
/* 115 */     pf.addInterface(AopInfrastructureBean.class);
/*     */     
/* 117 */     this.proxy = pf.getProxy(cbf.getBeanClassLoader());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getObject() {
/* 123 */     if (this.proxy == null) {
/* 124 */       throw new FactoryBeanNotInitializedException();
/*     */     }
/* 126 */     return this.proxy;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/* 131 */     if (this.proxy != null) {
/* 132 */       return this.proxy.getClass();
/*     */     }
/* 134 */     return this.scopedTargetSource.getTargetClass();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 139 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/scope/ScopedProxyFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */