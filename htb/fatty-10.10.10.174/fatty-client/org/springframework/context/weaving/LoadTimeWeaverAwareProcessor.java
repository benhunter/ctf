/*     */ package org.springframework.context.weaving;
/*     */ 
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*     */ import org.springframework.instrument.classloading.LoadTimeWeaver;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LoadTimeWeaverAwareProcessor
/*     */   implements BeanPostProcessor, BeanFactoryAware
/*     */ {
/*     */   @Nullable
/*     */   private LoadTimeWeaver loadTimeWeaver;
/*     */   @Nullable
/*     */   private BeanFactory beanFactory;
/*     */   
/*     */   public LoadTimeWeaverAwareProcessor() {}
/*     */   
/*     */   public LoadTimeWeaverAwareProcessor(@Nullable LoadTimeWeaver loadTimeWeaver) {
/*  72 */     this.loadTimeWeaver = loadTimeWeaver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoadTimeWeaverAwareProcessor(BeanFactory beanFactory) {
/*  83 */     this.beanFactory = beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/*  89 */     this.beanFactory = beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
/*  95 */     if (bean instanceof LoadTimeWeaverAware) {
/*  96 */       LoadTimeWeaver ltw = this.loadTimeWeaver;
/*  97 */       if (ltw == null) {
/*  98 */         Assert.state((this.beanFactory != null), "BeanFactory required if no LoadTimeWeaver explicitly specified");
/*     */         
/* 100 */         ltw = (LoadTimeWeaver)this.beanFactory.getBean("loadTimeWeaver", LoadTimeWeaver.class);
/*     */       } 
/*     */       
/* 103 */       ((LoadTimeWeaverAware)bean).setLoadTimeWeaver(ltw);
/*     */     } 
/* 105 */     return bean;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object postProcessAfterInitialization(Object bean, String name) {
/* 110 */     return bean;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/weaving/LoadTimeWeaverAwareProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */