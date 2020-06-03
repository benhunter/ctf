/*     */ package org.springframework.beans.factory.wiring;
/*     */ 
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanCreationException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
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
/*     */ public class BeanConfigurerSupport
/*     */   implements BeanFactoryAware, InitializingBean, DisposableBean
/*     */ {
/*  53 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private volatile BeanWiringInfoResolver beanWiringInfoResolver;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private volatile ConfigurableListableBeanFactory beanFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanWiringInfoResolver(BeanWiringInfoResolver beanWiringInfoResolver) {
/*  70 */     Assert.notNull(beanWiringInfoResolver, "BeanWiringInfoResolver must not be null");
/*  71 */     this.beanWiringInfoResolver = beanWiringInfoResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/*  79 */     if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
/*  80 */       throw new IllegalArgumentException("Bean configurer aspect needs to run in a ConfigurableListableBeanFactory: " + beanFactory);
/*     */     }
/*     */     
/*  83 */     this.beanFactory = (ConfigurableListableBeanFactory)beanFactory;
/*  84 */     if (this.beanWiringInfoResolver == null) {
/*  85 */       this.beanWiringInfoResolver = createDefaultBeanWiringInfoResolver();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected BeanWiringInfoResolver createDefaultBeanWiringInfoResolver() {
/*  97 */     return new ClassNameBeanWiringInfoResolver();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 105 */     Assert.notNull(this.beanFactory, "BeanFactory must be set");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 114 */     this.beanFactory = null;
/* 115 */     this.beanWiringInfoResolver = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void configureBean(Object beanInstance) {
/* 126 */     if (this.beanFactory == null) {
/* 127 */       if (this.logger.isDebugEnabled()) {
/* 128 */         this.logger.debug("BeanFactory has not been set on " + ClassUtils.getShortName(getClass()) + ": Make sure this configurer runs in a Spring container. Unable to configure bean of type [" + 
/*     */             
/* 130 */             ClassUtils.getDescriptiveType(beanInstance) + "]. Proceeding without injection.");
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 135 */     BeanWiringInfoResolver bwiResolver = this.beanWiringInfoResolver;
/* 136 */     Assert.state((bwiResolver != null), "No BeanWiringInfoResolver available");
/* 137 */     BeanWiringInfo bwi = bwiResolver.resolveWiringInfo(beanInstance);
/* 138 */     if (bwi == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 144 */     ConfigurableListableBeanFactory beanFactory = this.beanFactory;
/* 145 */     Assert.state((beanFactory != null), "No BeanFactory available");
/*     */     try {
/* 147 */       String beanName = bwi.getBeanName();
/* 148 */       if (bwi.indicatesAutowiring() || (bwi.isDefaultBeanName() && beanName != null && 
/* 149 */         !beanFactory.containsBean(beanName)))
/*     */       {
/* 151 */         beanFactory.autowireBeanProperties(beanInstance, bwi.getAutowireMode(), bwi.getDependencyCheck());
/* 152 */         beanFactory.initializeBean(beanInstance, (beanName != null) ? beanName : "");
/*     */       }
/*     */       else
/*     */       {
/* 156 */         beanFactory.configureBean(beanInstance, (beanName != null) ? beanName : "");
/*     */       }
/*     */     
/* 159 */     } catch (BeanCreationException ex) {
/* 160 */       Throwable rootCause = ex.getMostSpecificCause();
/* 161 */       if (rootCause instanceof org.springframework.beans.factory.BeanCurrentlyInCreationException) {
/* 162 */         BeanCreationException bce = (BeanCreationException)rootCause;
/* 163 */         String bceBeanName = bce.getBeanName();
/* 164 */         if (bceBeanName != null && beanFactory.isCurrentlyInCreation(bceBeanName)) {
/* 165 */           if (this.logger.isDebugEnabled()) {
/* 166 */             this.logger.debug("Failed to create target bean '" + bce.getBeanName() + "' while configuring object of type [" + beanInstance
/* 167 */                 .getClass().getName() + "] - probably due to a circular reference. This is a common startup situation and usually not fatal. Proceeding without injection. Original exception: " + ex);
/*     */           }
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/*     */       
/* 174 */       throw ex;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/wiring/BeanConfigurerSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */