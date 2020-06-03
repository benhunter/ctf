/*     */ package org.springframework.aop.framework;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractAdvisingBeanPostProcessor
/*     */   extends ProxyProcessorSupport
/*     */   implements BeanPostProcessor
/*     */ {
/*     */   @Nullable
/*     */   protected Advisor advisor;
/*     */   protected boolean beforeExistingAdvisors = false;
/*  42 */   private final Map<Class<?>, Boolean> eligibleBeans = new ConcurrentHashMap<>(256);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeforeExistingAdvisors(boolean beforeExistingAdvisors) {
/*  55 */     this.beforeExistingAdvisors = beforeExistingAdvisors;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object postProcessBeforeInitialization(Object bean, String beanName) {
/*  61 */     return bean;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object postProcessAfterInitialization(Object bean, String beanName) {
/*  66 */     if (this.advisor == null || bean instanceof AopInfrastructureBean)
/*     */     {
/*  68 */       return bean;
/*     */     }
/*     */     
/*  71 */     if (bean instanceof Advised) {
/*  72 */       Advised advised = (Advised)bean;
/*  73 */       if (!advised.isFrozen() && isEligible(AopUtils.getTargetClass(bean))) {
/*     */         
/*  75 */         if (this.beforeExistingAdvisors) {
/*  76 */           advised.addAdvisor(0, this.advisor);
/*     */         } else {
/*     */           
/*  79 */           advised.addAdvisor(this.advisor);
/*     */         } 
/*  81 */         return bean;
/*     */       } 
/*     */     } 
/*     */     
/*  85 */     if (isEligible(bean, beanName)) {
/*  86 */       ProxyFactory proxyFactory = prepareProxyFactory(bean, beanName);
/*  87 */       if (!proxyFactory.isProxyTargetClass()) {
/*  88 */         evaluateProxyInterfaces(bean.getClass(), proxyFactory);
/*     */       }
/*  90 */       proxyFactory.addAdvisor(this.advisor);
/*  91 */       customizeProxyFactory(proxyFactory);
/*  92 */       return proxyFactory.getProxy(getProxyClassLoader());
/*     */     } 
/*     */ 
/*     */     
/*  96 */     return bean;
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
/*     */   protected boolean isEligible(Object bean, String beanName) {
/* 115 */     return isEligible(bean.getClass());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isEligible(Class<?> targetClass) {
/* 126 */     Boolean eligible = this.eligibleBeans.get(targetClass);
/* 127 */     if (eligible != null) {
/* 128 */       return eligible.booleanValue();
/*     */     }
/* 130 */     if (this.advisor == null) {
/* 131 */       return false;
/*     */     }
/* 133 */     eligible = Boolean.valueOf(AopUtils.canApply(this.advisor, targetClass));
/* 134 */     this.eligibleBeans.put(targetClass, eligible);
/* 135 */     return eligible.booleanValue();
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
/*     */   protected ProxyFactory prepareProxyFactory(Object bean, String beanName) {
/* 153 */     ProxyFactory proxyFactory = new ProxyFactory();
/* 154 */     proxyFactory.copyFrom(this);
/* 155 */     proxyFactory.setTarget(bean);
/* 156 */     return proxyFactory;
/*     */   }
/*     */   
/*     */   protected void customizeProxyFactory(ProxyFactory proxyFactory) {}
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/AbstractAdvisingBeanPostProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */