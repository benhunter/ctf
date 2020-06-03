/*     */ package org.springframework.web.context.support;
/*     */ 
/*     */ import javax.servlet.ServletConfig;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.web.context.ServletConfigAware;
/*     */ import org.springframework.web.context.ServletContextAware;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServletContextAwareProcessor
/*     */   implements BeanPostProcessor
/*     */ {
/*     */   @Nullable
/*     */   private ServletContext servletContext;
/*     */   @Nullable
/*     */   private ServletConfig servletConfig;
/*     */   
/*     */   protected ServletContextAwareProcessor() {}
/*     */   
/*     */   public ServletContextAwareProcessor(ServletContext servletContext) {
/*  63 */     this(servletContext, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletContextAwareProcessor(ServletConfig servletConfig) {
/*  70 */     this(null, servletConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletContextAwareProcessor(@Nullable ServletContext servletContext, @Nullable ServletConfig servletConfig) {
/*  77 */     this.servletContext = servletContext;
/*  78 */     this.servletConfig = servletConfig;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected ServletContext getServletContext() {
/*  89 */     if (this.servletContext == null && getServletConfig() != null) {
/*  90 */       return getServletConfig().getServletContext();
/*     */     }
/*  92 */     return this.servletContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected ServletConfig getServletConfig() {
/* 102 */     return this.servletConfig;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
/* 107 */     if (getServletContext() != null && bean instanceof ServletContextAware) {
/* 108 */       ((ServletContextAware)bean).setServletContext(getServletContext());
/*     */     }
/* 110 */     if (getServletConfig() != null && bean instanceof ServletConfigAware) {
/* 111 */       ((ServletConfigAware)bean).setServletConfig(getServletConfig());
/*     */     }
/* 113 */     return bean;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object postProcessAfterInitialization(Object bean, String beanName) {
/* 118 */     return bean;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/support/ServletContextAwareProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */