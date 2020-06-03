/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
/*     */ import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.context.ApplicationListener;
/*     */ import org.springframework.context.event.ApplicationEventMulticaster;
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
/*     */ 
/*     */ class ApplicationListenerDetector
/*     */   implements DestructionAwareBeanPostProcessor, MergedBeanDefinitionPostProcessor
/*     */ {
/*  47 */   private static final Log logger = LogFactory.getLog(ApplicationListenerDetector.class);
/*     */   
/*     */   private final transient AbstractApplicationContext applicationContext;
/*     */   
/*  51 */   private final transient Map<String, Boolean> singletonNames = new ConcurrentHashMap<>(256);
/*     */ 
/*     */   
/*     */   public ApplicationListenerDetector(AbstractApplicationContext applicationContext) {
/*  55 */     this.applicationContext = applicationContext;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
/*  61 */     this.singletonNames.put(beanName, Boolean.valueOf(beanDefinition.isSingleton()));
/*     */   }
/*     */ 
/*     */   
/*     */   public Object postProcessBeforeInitialization(Object bean, String beanName) {
/*  66 */     return bean;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object postProcessAfterInitialization(Object bean, String beanName) {
/*  71 */     if (bean instanceof ApplicationListener) {
/*     */       
/*  73 */       Boolean flag = this.singletonNames.get(beanName);
/*  74 */       if (Boolean.TRUE.equals(flag)) {
/*     */         
/*  76 */         this.applicationContext.addApplicationListener((ApplicationListener)bean);
/*     */       }
/*  78 */       else if (Boolean.FALSE.equals(flag)) {
/*  79 */         if (logger.isWarnEnabled() && !this.applicationContext.containsBean(beanName))
/*     */         {
/*  81 */           logger.warn("Inner bean '" + beanName + "' implements ApplicationListener interface but is not reachable for event multicasting by its containing ApplicationContext because it does not have singleton scope. Only top-level listener beans are allowed to be of non-singleton scope.");
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*  86 */         this.singletonNames.remove(beanName);
/*     */       } 
/*     */     } 
/*  89 */     return bean;
/*     */   }
/*     */ 
/*     */   
/*     */   public void postProcessBeforeDestruction(Object bean, String beanName) {
/*  94 */     if (bean instanceof ApplicationListener) {
/*     */       try {
/*  96 */         ApplicationEventMulticaster multicaster = this.applicationContext.getApplicationEventMulticaster();
/*  97 */         multicaster.removeApplicationListener((ApplicationListener)bean);
/*  98 */         multicaster.removeApplicationListenerBean(beanName);
/*     */       }
/* 100 */       catch (IllegalStateException illegalStateException) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean requiresDestruction(Object bean) {
/* 108 */     return bean instanceof ApplicationListener;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 114 */     return (this == other || (other instanceof ApplicationListenerDetector && this.applicationContext == ((ApplicationListenerDetector)other).applicationContext));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 120 */     return ObjectUtils.nullSafeHashCode(this.applicationContext);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/support/ApplicationListenerDetector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */