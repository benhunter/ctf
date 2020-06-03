/*     */ package org.springframework.web.context.support;
/*     */ 
/*     */ import javax.servlet.ServletConfig;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.support.StaticApplicationContext;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.io.support.ResourcePatternResolver;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.ui.context.Theme;
/*     */ import org.springframework.ui.context.ThemeSource;
/*     */ import org.springframework.ui.context.support.UiApplicationContextUtils;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.context.ConfigurableWebApplicationContext;
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
/*     */ public class StaticWebApplicationContext
/*     */   extends StaticApplicationContext
/*     */   implements ConfigurableWebApplicationContext, ThemeSource
/*     */ {
/*     */   @Nullable
/*     */   private ServletContext servletContext;
/*     */   @Nullable
/*     */   private ServletConfig servletConfig;
/*     */   @Nullable
/*     */   private String namespace;
/*     */   @Nullable
/*     */   private ThemeSource themeSource;
/*     */   
/*     */   public StaticWebApplicationContext() {
/*  75 */     setDisplayName("Root WebApplicationContext");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServletContext(@Nullable ServletContext servletContext) {
/*  84 */     this.servletContext = servletContext;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ServletContext getServletContext() {
/*  90 */     return this.servletContext;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setServletConfig(@Nullable ServletConfig servletConfig) {
/*  95 */     this.servletConfig = servletConfig;
/*  96 */     if (servletConfig != null && this.servletContext == null) {
/*  97 */       this.servletContext = servletConfig.getServletContext();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ServletConfig getServletConfig() {
/* 104 */     return this.servletConfig;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNamespace(@Nullable String namespace) {
/* 109 */     this.namespace = namespace;
/* 110 */     if (namespace != null) {
/* 111 */       setDisplayName("WebApplicationContext for namespace '" + namespace + "'");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getNamespace() {
/* 118 */     return this.namespace;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConfigLocation(String configLocation) {
/* 127 */     throw new UnsupportedOperationException("StaticWebApplicationContext does not support config locations");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConfigLocations(String... configLocations) {
/* 136 */     throw new UnsupportedOperationException("StaticWebApplicationContext does not support config locations");
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getConfigLocations() {
/* 141 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
/* 150 */     beanFactory.addBeanPostProcessor(new ServletContextAwareProcessor(this.servletContext, this.servletConfig));
/* 151 */     beanFactory.ignoreDependencyInterface(ServletContextAware.class);
/* 152 */     beanFactory.ignoreDependencyInterface(ServletConfigAware.class);
/*     */     
/* 154 */     WebApplicationContextUtils.registerWebApplicationScopes(beanFactory, this.servletContext);
/* 155 */     WebApplicationContextUtils.registerEnvironmentBeans(beanFactory, this.servletContext, this.servletConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Resource getResourceByPath(String path) {
/* 164 */     Assert.state((this.servletContext != null), "No ServletContext available");
/* 165 */     return (Resource)new ServletContextResource(this.servletContext, path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ResourcePatternResolver getResourcePatternResolver() {
/* 174 */     return (ResourcePatternResolver)new ServletContextResourcePatternResolver((ResourceLoader)this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ConfigurableEnvironment createEnvironment() {
/* 182 */     return (ConfigurableEnvironment)new StandardServletEnvironment();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onRefresh() {
/* 190 */     this.themeSource = UiApplicationContextUtils.initThemeSource((ApplicationContext)this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void initPropertySources() {
/* 195 */     WebApplicationContextUtils.initServletPropertySources(getEnvironment().getPropertySources(), this.servletContext, this.servletConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Theme getTheme(String themeName) {
/* 202 */     Assert.state((this.themeSource != null), "No ThemeSource available");
/* 203 */     return this.themeSource.getTheme(themeName);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/support/StaticWebApplicationContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */