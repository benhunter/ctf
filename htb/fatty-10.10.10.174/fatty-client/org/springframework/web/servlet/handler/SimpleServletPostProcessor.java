/*     */ package org.springframework.web.servlet.handler;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import javax.servlet.Servlet;
/*     */ import javax.servlet.ServletConfig;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletException;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanInitializationException;
/*     */ import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleServletPostProcessor
/*     */   implements DestructionAwareBeanPostProcessor, ServletContextAware, ServletConfigAware
/*     */ {
/*     */   private boolean useSharedServletConfig = true;
/*     */   @Nullable
/*     */   private ServletContext servletContext;
/*     */   @Nullable
/*     */   private ServletConfig servletConfig;
/*     */   
/*     */   public void setUseSharedServletConfig(boolean useSharedServletConfig) {
/*  88 */     this.useSharedServletConfig = useSharedServletConfig;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setServletContext(ServletContext servletContext) {
/*  93 */     this.servletContext = servletContext;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setServletConfig(ServletConfig servletConfig) {
/*  98 */     this.servletConfig = servletConfig;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
/* 104 */     return bean;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
/* 109 */     if (bean instanceof Servlet) {
/* 110 */       ServletConfig config = this.servletConfig;
/* 111 */       if (config == null || !this.useSharedServletConfig) {
/* 112 */         config = new DelegatingServletConfig(beanName, this.servletContext);
/*     */       }
/*     */       try {
/* 115 */         ((Servlet)bean).init(config);
/*     */       }
/* 117 */       catch (ServletException ex) {
/* 118 */         throw new BeanInitializationException("Servlet.init threw exception", ex);
/*     */       } 
/*     */     } 
/* 121 */     return bean;
/*     */   }
/*     */ 
/*     */   
/*     */   public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
/* 126 */     if (bean instanceof Servlet) {
/* 127 */       ((Servlet)bean).destroy();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requiresDestruction(Object bean) {
/* 133 */     return bean instanceof Servlet;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class DelegatingServletConfig
/*     */     implements ServletConfig
/*     */   {
/*     */     private final String servletName;
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private final ServletContext servletContext;
/*     */ 
/*     */     
/*     */     public DelegatingServletConfig(String servletName, @Nullable ServletContext servletContext) {
/* 149 */       this.servletName = servletName;
/* 150 */       this.servletContext = servletContext;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getServletName() {
/* 155 */       return this.servletName;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public ServletContext getServletContext() {
/* 161 */       return this.servletContext;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getInitParameter(String paramName) {
/* 167 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Enumeration<String> getInitParameterNames() {
/* 172 */       return Collections.enumeration(Collections.emptySet());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/handler/SimpleServletPostProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */