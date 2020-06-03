/*     */ package org.springframework.aop.framework.autoproxy;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.aop.TargetSource;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractAdvisorAutoProxyCreator
/*     */   extends AbstractAutoProxyCreator
/*     */ {
/*     */   @Nullable
/*     */   private BeanFactoryAdvisorRetrievalHelper advisorRetrievalHelper;
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/*  58 */     super.setBeanFactory(beanFactory);
/*  59 */     if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
/*  60 */       throw new IllegalArgumentException("AdvisorAutoProxyCreator requires a ConfigurableListableBeanFactory: " + beanFactory);
/*     */     }
/*     */     
/*  63 */     initBeanFactory((ConfigurableListableBeanFactory)beanFactory);
/*     */   }
/*     */   
/*     */   protected void initBeanFactory(ConfigurableListableBeanFactory beanFactory) {
/*  67 */     this.advisorRetrievalHelper = new BeanFactoryAdvisorRetrievalHelperAdapter(beanFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, @Nullable TargetSource targetSource) {
/*  76 */     List<Advisor> advisors = findEligibleAdvisors(beanClass, beanName);
/*  77 */     if (advisors.isEmpty()) {
/*  78 */       return DO_NOT_PROXY;
/*     */     }
/*  80 */     return advisors.toArray();
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
/*     */   protected List<Advisor> findEligibleAdvisors(Class<?> beanClass, String beanName) {
/*  94 */     List<Advisor> candidateAdvisors = findCandidateAdvisors();
/*  95 */     List<Advisor> eligibleAdvisors = findAdvisorsThatCanApply(candidateAdvisors, beanClass, beanName);
/*  96 */     extendAdvisors(eligibleAdvisors);
/*  97 */     if (!eligibleAdvisors.isEmpty()) {
/*  98 */       eligibleAdvisors = sortAdvisors(eligibleAdvisors);
/*     */     }
/* 100 */     return eligibleAdvisors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<Advisor> findCandidateAdvisors() {
/* 108 */     Assert.state((this.advisorRetrievalHelper != null), "No BeanFactoryAdvisorRetrievalHelper available");
/* 109 */     return this.advisorRetrievalHelper.findAdvisorBeans();
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
/*     */   protected List<Advisor> findAdvisorsThatCanApply(List<Advisor> candidateAdvisors, Class<?> beanClass, String beanName) {
/* 124 */     ProxyCreationContext.setCurrentProxiedBeanName(beanName);
/*     */     try {
/* 126 */       return AopUtils.findAdvisorsThatCanApply(candidateAdvisors, beanClass);
/*     */     } finally {
/*     */       
/* 129 */       ProxyCreationContext.setCurrentProxiedBeanName(null);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isEligibleAdvisorBean(String beanName) {
/* 140 */     return true;
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
/*     */   protected List<Advisor> sortAdvisors(List<Advisor> advisors) {
/* 153 */     AnnotationAwareOrderComparator.sort(advisors);
/* 154 */     return advisors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void extendAdvisors(List<Advisor> candidateAdvisors) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean advisorsPreFiltered() {
/* 174 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class BeanFactoryAdvisorRetrievalHelperAdapter
/*     */     extends BeanFactoryAdvisorRetrievalHelper
/*     */   {
/*     */     public BeanFactoryAdvisorRetrievalHelperAdapter(ConfigurableListableBeanFactory beanFactory) {
/* 185 */       super(beanFactory);
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isEligibleBean(String beanName) {
/* 190 */       return AbstractAdvisorAutoProxyCreator.this.isEligibleAdvisorBean(beanName);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/autoproxy/AbstractAdvisorAutoProxyCreator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */