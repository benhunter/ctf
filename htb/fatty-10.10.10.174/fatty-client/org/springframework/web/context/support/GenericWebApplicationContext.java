/*     */ package org.springframework.web.context.support;
/*     */ 
/*     */ import javax.servlet.ServletConfig;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.support.GenericApplicationContext;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.io.support.ResourcePatternResolver;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.ui.context.Theme;
/*     */ import org.springframework.ui.context.ThemeSource;
/*     */ import org.springframework.ui.context.support.UiApplicationContextUtils;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.context.ConfigurableWebApplicationContext;
/*     */ import org.springframework.web.context.ConfigurableWebEnvironment;
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
/*     */ public class GenericWebApplicationContext
/*     */   extends GenericApplicationContext
/*     */   implements ConfigurableWebApplicationContext, ThemeSource
/*     */ {
/*     */   @Nullable
/*     */   private ServletContext servletContext;
/*     */   @Nullable
/*     */   private ThemeSource themeSource;
/*     */   
/*     */   public GenericWebApplicationContext() {}
/*     */   
/*     */   public GenericWebApplicationContext(ServletContext servletContext) {
/*  91 */     this.servletContext = servletContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GenericWebApplicationContext(DefaultListableBeanFactory beanFactory) {
/* 102 */     super(beanFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GenericWebApplicationContext(DefaultListableBeanFactory beanFactory, ServletContext servletContext) {
/* 113 */     super(beanFactory);
/* 114 */     this.servletContext = servletContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServletContext(@Nullable ServletContext servletContext) {
/* 123 */     this.servletContext = servletContext;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ServletContext getServletContext() {
/* 129 */     return this.servletContext;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getApplicationName() {
/* 134 */     return (this.servletContext != null) ? this.servletContext.getContextPath() : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ConfigurableEnvironment createEnvironment() {
/* 142 */     return (ConfigurableEnvironment)new StandardServletEnvironment();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
/* 151 */     if (this.servletContext != null) {
/* 152 */       beanFactory.addBeanPostProcessor(new ServletContextAwareProcessor(this.servletContext));
/* 153 */       beanFactory.ignoreDependencyInterface(ServletContextAware.class);
/*     */     } 
/* 155 */     WebApplicationContextUtils.registerWebApplicationScopes(beanFactory, this.servletContext);
/* 156 */     WebApplicationContextUtils.registerEnvironmentBeans(beanFactory, this.servletContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Resource getResourceByPath(String path) {
/* 165 */     Assert.state((this.servletContext != null), "No ServletContext available");
/* 166 */     return (Resource)new ServletContextResource(this.servletContext, path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ResourcePatternResolver getResourcePatternResolver() {
/* 175 */     return (ResourcePatternResolver)new ServletContextResourcePatternResolver((ResourceLoader)this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onRefresh() {
/* 183 */     this.themeSource = UiApplicationContextUtils.initThemeSource((ApplicationContext)this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initPropertySources() {
/* 192 */     ConfigurableEnvironment env = getEnvironment();
/* 193 */     if (env instanceof ConfigurableWebEnvironment) {
/* 194 */       ((ConfigurableWebEnvironment)env).initPropertySources(this.servletContext, null);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Theme getTheme(String themeName) {
/* 201 */     Assert.state((this.themeSource != null), "No ThemeSource available");
/* 202 */     return this.themeSource.getTheme(themeName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServletConfig(@Nullable ServletConfig servletConfig) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ServletConfig getServletConfig() {
/* 218 */     throw new UnsupportedOperationException("GenericWebApplicationContext does not support getServletConfig()");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNamespace(@Nullable String namespace) {}
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getNamespace() {
/* 230 */     throw new UnsupportedOperationException("GenericWebApplicationContext does not support getNamespace()");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConfigLocation(String configLocation) {
/* 236 */     if (StringUtils.hasText(configLocation)) {
/* 237 */       throw new UnsupportedOperationException("GenericWebApplicationContext does not support setConfigLocation(). Do you still have an 'contextConfigLocations' init-param set?");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConfigLocations(String... configLocations) {
/* 245 */     if (!ObjectUtils.isEmpty((Object[])configLocations)) {
/* 246 */       throw new UnsupportedOperationException("GenericWebApplicationContext does not support setConfigLocations(). Do you still have an 'contextConfigLocations' init-param set?");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getConfigLocations() {
/* 254 */     throw new UnsupportedOperationException("GenericWebApplicationContext does not support getConfigLocations()");
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/support/GenericWebApplicationContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */