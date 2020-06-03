/*     */ package org.springframework.aop.framework.autoproxy;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.beans.factory.BeanCreationException;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
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
/*     */ public class BeanFactoryAdvisorRetrievalHelper
/*     */ {
/*  43 */   private static final Log logger = LogFactory.getLog(BeanFactoryAdvisorRetrievalHelper.class);
/*     */ 
/*     */ 
/*     */   
/*     */   private final ConfigurableListableBeanFactory beanFactory;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private volatile String[] cachedAdvisorBeanNames;
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanFactoryAdvisorRetrievalHelper(ConfigurableListableBeanFactory beanFactory) {
/*  56 */     Assert.notNull(beanFactory, "ListableBeanFactory must not be null");
/*  57 */     this.beanFactory = beanFactory;
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
/*     */   public List<Advisor> findAdvisorBeans() {
/*  69 */     String[] advisorNames = this.cachedAdvisorBeanNames;
/*  70 */     if (advisorNames == null) {
/*     */ 
/*     */       
/*  73 */       advisorNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors((ListableBeanFactory)this.beanFactory, Advisor.class, true, false);
/*     */       
/*  75 */       this.cachedAdvisorBeanNames = advisorNames;
/*     */     } 
/*  77 */     if (advisorNames.length == 0) {
/*  78 */       return new ArrayList<>();
/*     */     }
/*     */     
/*  81 */     List<Advisor> advisors = new ArrayList<>();
/*  82 */     for (String name : advisorNames) {
/*  83 */       if (isEligibleBean(name))
/*  84 */         if (this.beanFactory.isCurrentlyInCreation(name)) {
/*  85 */           if (logger.isTraceEnabled()) {
/*  86 */             logger.trace("Skipping currently created advisor '" + name + "'");
/*     */           }
/*     */         } else {
/*     */           
/*     */           try {
/*  91 */             advisors.add(this.beanFactory.getBean(name, Advisor.class));
/*     */           }
/*  93 */           catch (BeanCreationException ex) {
/*  94 */             Throwable rootCause = ex.getMostSpecificCause();
/*  95 */             if (rootCause instanceof org.springframework.beans.factory.BeanCurrentlyInCreationException)
/*  96 */             { BeanCreationException bce = (BeanCreationException)rootCause;
/*  97 */               String bceBeanName = bce.getBeanName();
/*  98 */               if (bceBeanName != null && this.beanFactory.isCurrentlyInCreation(bceBeanName))
/*  99 */               { if (logger.isTraceEnabled()) {
/* 100 */                   logger.trace("Skipping advisor '" + name + "' with dependency on currently created bean: " + ex
/* 101 */                       .getMessage());
/*     */                 
/*     */                 }
/*     */                  }
/*     */               
/*     */               else
/*     */               
/* 108 */               { throw ex; }  } else { throw ex; }
/*     */           
/*     */           } 
/*     */         }  
/*     */     } 
/* 113 */     return advisors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isEligibleBean(String beanName) {
/* 123 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/autoproxy/BeanFactoryAdvisorRetrievalHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */