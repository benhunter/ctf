/*     */ package org.springframework.aop.aspectj.annotation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.regex.Pattern;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnnotationAwareAspectJAutoProxyCreator
/*     */   extends AspectJAwareAdvisorAutoProxyCreator
/*     */ {
/*     */   @Nullable
/*     */   private List<Pattern> includePatterns;
/*     */   @Nullable
/*     */   private AspectJAdvisorFactory aspectJAdvisorFactory;
/*     */   @Nullable
/*     */   private BeanFactoryAspectJAdvisorsBuilder aspectJAdvisorsBuilder;
/*     */   
/*     */   public void setIncludePatterns(List<String> patterns) {
/*  67 */     this.includePatterns = new ArrayList<>(patterns.size());
/*  68 */     for (String patternText : patterns) {
/*  69 */       this.includePatterns.add(Pattern.compile(patternText));
/*     */     }
/*     */   }
/*     */   
/*     */   public void setAspectJAdvisorFactory(AspectJAdvisorFactory aspectJAdvisorFactory) {
/*  74 */     Assert.notNull(aspectJAdvisorFactory, "AspectJAdvisorFactory must not be null");
/*  75 */     this.aspectJAdvisorFactory = aspectJAdvisorFactory;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void initBeanFactory(ConfigurableListableBeanFactory beanFactory) {
/*  80 */     super.initBeanFactory(beanFactory);
/*  81 */     if (this.aspectJAdvisorFactory == null) {
/*  82 */       this.aspectJAdvisorFactory = new ReflectiveAspectJAdvisorFactory((BeanFactory)beanFactory);
/*     */     }
/*  84 */     this.aspectJAdvisorsBuilder = new BeanFactoryAspectJAdvisorsBuilderAdapter((ListableBeanFactory)beanFactory, this.aspectJAdvisorFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<Advisor> findCandidateAdvisors() {
/*  92 */     List<Advisor> advisors = super.findCandidateAdvisors();
/*     */     
/*  94 */     if (this.aspectJAdvisorsBuilder != null) {
/*  95 */       advisors.addAll(this.aspectJAdvisorsBuilder.buildAspectJAdvisors());
/*     */     }
/*  97 */     return advisors;
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
/*     */   protected boolean isInfrastructureClass(Class<?> beanClass) {
/* 110 */     return (super.isInfrastructureClass(beanClass) || (this.aspectJAdvisorFactory != null && this.aspectJAdvisorFactory
/* 111 */       .isAspect(beanClass)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isEligibleAspectBean(String beanName) {
/* 121 */     if (this.includePatterns == null) {
/* 122 */       return true;
/*     */     }
/*     */     
/* 125 */     for (Pattern pattern : this.includePatterns) {
/* 126 */       if (pattern.matcher(beanName).matches()) {
/* 127 */         return true;
/*     */       }
/*     */     } 
/* 130 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class BeanFactoryAspectJAdvisorsBuilderAdapter
/*     */     extends BeanFactoryAspectJAdvisorsBuilder
/*     */   {
/*     */     public BeanFactoryAspectJAdvisorsBuilderAdapter(ListableBeanFactory beanFactory, AspectJAdvisorFactory advisorFactory) {
/* 144 */       super(beanFactory, advisorFactory);
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isEligibleBean(String beanName) {
/* 149 */       return AnnotationAwareAspectJAutoProxyCreator.this.isEligibleAspectBean(beanName);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/annotation/AnnotationAwareAspectJAutoProxyCreator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */