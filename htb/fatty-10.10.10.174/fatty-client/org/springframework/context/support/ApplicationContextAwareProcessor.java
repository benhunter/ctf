/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.beans.factory.config.EmbeddedValueResolver;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.context.ApplicationEventPublisher;
/*     */ import org.springframework.context.ApplicationEventPublisherAware;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.context.EmbeddedValueResolverAware;
/*     */ import org.springframework.context.EnvironmentAware;
/*     */ import org.springframework.context.MessageSource;
/*     */ import org.springframework.context.MessageSourceAware;
/*     */ import org.springframework.context.ResourceLoaderAware;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.StringValueResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ApplicationContextAwareProcessor
/*     */   implements BeanPostProcessor
/*     */ {
/*     */   private final ConfigurableApplicationContext applicationContext;
/*     */   private final StringValueResolver embeddedValueResolver;
/*     */   
/*     */   public ApplicationContextAwareProcessor(ConfigurableApplicationContext applicationContext) {
/*  72 */     this.applicationContext = applicationContext;
/*  73 */     this.embeddedValueResolver = (StringValueResolver)new EmbeddedValueResolver((ConfigurableBeanFactory)applicationContext.getBeanFactory());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
/*  80 */     AccessControlContext acc = null;
/*     */     
/*  82 */     if (System.getSecurityManager() != null && (bean instanceof EnvironmentAware || bean instanceof EmbeddedValueResolverAware || bean instanceof ResourceLoaderAware || bean instanceof ApplicationEventPublisherAware || bean instanceof MessageSourceAware || bean instanceof ApplicationContextAware))
/*     */     {
/*     */ 
/*     */       
/*  86 */       acc = this.applicationContext.getBeanFactory().getAccessControlContext();
/*     */     }
/*     */     
/*  89 */     if (acc != null) {
/*  90 */       AccessController.doPrivileged(() -> { invokeAwareInterfaces(bean); return null; }acc);
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/*  96 */       invokeAwareInterfaces(bean);
/*     */     } 
/*     */     
/*  99 */     return bean;
/*     */   }
/*     */   
/*     */   private void invokeAwareInterfaces(Object bean) {
/* 103 */     if (bean instanceof org.springframework.beans.factory.Aware) {
/* 104 */       if (bean instanceof EnvironmentAware) {
/* 105 */         ((EnvironmentAware)bean).setEnvironment((Environment)this.applicationContext.getEnvironment());
/*     */       }
/* 107 */       if (bean instanceof EmbeddedValueResolverAware) {
/* 108 */         ((EmbeddedValueResolverAware)bean).setEmbeddedValueResolver(this.embeddedValueResolver);
/*     */       }
/* 110 */       if (bean instanceof ResourceLoaderAware) {
/* 111 */         ((ResourceLoaderAware)bean).setResourceLoader((ResourceLoader)this.applicationContext);
/*     */       }
/* 113 */       if (bean instanceof ApplicationEventPublisherAware) {
/* 114 */         ((ApplicationEventPublisherAware)bean).setApplicationEventPublisher((ApplicationEventPublisher)this.applicationContext);
/*     */       }
/* 116 */       if (bean instanceof MessageSourceAware) {
/* 117 */         ((MessageSourceAware)bean).setMessageSource((MessageSource)this.applicationContext);
/*     */       }
/* 119 */       if (bean instanceof ApplicationContextAware) {
/* 120 */         ((ApplicationContextAware)bean).setApplicationContext((ApplicationContext)this.applicationContext);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Object postProcessAfterInitialization(Object bean, String beanName) {
/* 127 */     return bean;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/support/ApplicationContextAwareProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */