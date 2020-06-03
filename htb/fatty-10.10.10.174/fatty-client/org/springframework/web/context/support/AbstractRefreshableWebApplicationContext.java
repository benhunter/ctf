/*     */ package org.springframework.web.context.support;
/*     */ 
/*     */ import javax.servlet.ServletConfig;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
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
/*     */ import org.springframework.web.context.ConfigurableWebEnvironment;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractRefreshableWebApplicationContext
/*     */   extends AbstractRefreshableConfigApplicationContext
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
/*     */   public AbstractRefreshableWebApplicationContext() {
/* 102 */     setDisplayName("Root WebApplicationContext");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServletContext(@Nullable ServletContext servletContext) {
/* 108 */     this.servletContext = servletContext;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ServletContext getServletContext() {
/* 114 */     return this.servletContext;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setServletConfig(@Nullable ServletConfig servletConfig) {
/* 119 */     this.servletConfig = servletConfig;
/* 120 */     if (servletConfig != null && this.servletContext == null) {
/* 121 */       setServletContext(servletConfig.getServletContext());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ServletConfig getServletConfig() {
/* 128 */     return this.servletConfig;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNamespace(@Nullable String namespace) {
/* 133 */     this.namespace = namespace;
/* 134 */     if (namespace != null) {
/* 135 */       setDisplayName("WebApplicationContext for namespace '" + namespace + "'");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getNamespace() {
/* 142 */     return this.namespace;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getConfigLocations() {
/* 147 */     return super.getConfigLocations();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getApplicationName() {
/* 152 */     return (this.servletContext != null) ? this.servletContext.getContextPath() : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ConfigurableEnvironment createEnvironment() {
/* 161 */     return (ConfigurableEnvironment)new StandardServletEnvironment();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
/* 169 */     beanFactory.addBeanPostProcessor(new ServletContextAwareProcessor(this.servletContext, this.servletConfig));
/* 170 */     beanFactory.ignoreDependencyInterface(ServletContextAware.class);
/* 171 */     beanFactory.ignoreDependencyInterface(ServletConfigAware.class);
/*     */     
/* 173 */     WebApplicationContextUtils.registerWebApplicationScopes(beanFactory, this.servletContext);
/* 174 */     WebApplicationContextUtils.registerEnvironmentBeans(beanFactory, this.servletContext, this.servletConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Resource getResourceByPath(String path) {
/* 183 */     Assert.state((this.servletContext != null), "No ServletContext available");
/* 184 */     return (Resource)new ServletContextResource(this.servletContext, path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ResourcePatternResolver getResourcePatternResolver() {
/* 193 */     return (ResourcePatternResolver)new ServletContextResourcePatternResolver((ResourceLoader)this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onRefresh() {
/* 201 */     this.themeSource = UiApplicationContextUtils.initThemeSource((ApplicationContext)this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initPropertySources() {
/* 210 */     ConfigurableEnvironment env = getEnvironment();
/* 211 */     if (env instanceof ConfigurableWebEnvironment) {
/* 212 */       ((ConfigurableWebEnvironment)env).initPropertySources(this.servletContext, this.servletConfig);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Theme getTheme(String themeName) {
/* 219 */     Assert.state((this.themeSource != null), "No ThemeSource available");
/* 220 */     return this.themeSource.getTheme(themeName);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/support/AbstractRefreshableWebApplicationContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */