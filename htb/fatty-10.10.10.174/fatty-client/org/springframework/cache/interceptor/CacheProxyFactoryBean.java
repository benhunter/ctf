/*     */ package org.springframework.cache.interceptor;
/*     */ 
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.springframework.aop.Pointcut;
/*     */ import org.springframework.aop.framework.AbstractSingletonProxyFactoryBean;
/*     */ import org.springframework.aop.support.DefaultPointcutAdvisor;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.SmartInitializingSingleton;
/*     */ import org.springframework.cache.CacheManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CacheProxyFactoryBean
/*     */   extends AbstractSingletonProxyFactoryBean
/*     */   implements BeanFactoryAware, SmartInitializingSingleton
/*     */ {
/*  51 */   private final CacheInterceptor cacheInterceptor = new CacheInterceptor();
/*     */   
/*  53 */   private Pointcut pointcut = Pointcut.TRUE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCacheOperationSources(CacheOperationSource... cacheOperationSources) {
/*  61 */     this.cacheInterceptor.setCacheOperationSources(cacheOperationSources);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeyGenerator(KeyGenerator keyGenerator) {
/*  72 */     this.cacheInterceptor.setKeyGenerator(keyGenerator);
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
/*     */   public void setCacheResolver(CacheResolver cacheResolver) {
/*  84 */     this.cacheInterceptor.setCacheResolver(cacheResolver);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCacheManager(CacheManager cacheManager) {
/*  94 */     this.cacheInterceptor.setCacheManager(cacheManager);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPointcut(Pointcut pointcut) {
/* 105 */     this.pointcut = pointcut;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 110 */     this.cacheInterceptor.setBeanFactory(beanFactory);
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterSingletonsInstantiated() {
/* 115 */     this.cacheInterceptor.afterSingletonsInstantiated();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object createMainInterceptor() {
/* 121 */     this.cacheInterceptor.afterPropertiesSet();
/* 122 */     return new DefaultPointcutAdvisor(this.pointcut, (Advice)this.cacheInterceptor);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/interceptor/CacheProxyFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */