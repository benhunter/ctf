/*     */ package org.springframework.web.filter;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.servlet.Filter;
/*     */ import javax.servlet.FilterConfig;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeanWrapper;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.PropertyAccessorFactory;
/*     */ import org.springframework.beans.PropertyValue;
/*     */ import org.springframework.beans.PropertyValues;
/*     */ import org.springframework.beans.factory.BeanNameAware;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.context.EnvironmentAware;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.env.EnvironmentCapable;
/*     */ import org.springframework.core.env.PropertyResolver;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceEditor;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.context.ServletContextAware;
/*     */ import org.springframework.web.context.support.ServletContextResourceLoader;
/*     */ import org.springframework.web.context.support.StandardServletEnvironment;
/*     */ import org.springframework.web.util.NestedServletException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class GenericFilterBean
/*     */   implements Filter, BeanNameAware, EnvironmentAware, EnvironmentCapable, ServletContextAware, InitializingBean, DisposableBean
/*     */ {
/*  85 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   @Nullable
/*     */   private String beanName;
/*     */   
/*     */   @Nullable
/*     */   private Environment environment;
/*     */   
/*     */   @Nullable
/*     */   private ServletContext servletContext;
/*     */   
/*     */   @Nullable
/*     */   private FilterConfig filterConfig;
/*     */   
/*  99 */   private final Set<String> requiredProperties = new HashSet<>(4);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanName(String beanName) {
/* 111 */     this.beanName = beanName;
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
/*     */   
/*     */   public void setEnvironment(Environment environment) {
/* 124 */     this.environment = environment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Environment getEnvironment() {
/* 135 */     if (this.environment == null) {
/* 136 */       this.environment = createEnvironment();
/*     */     }
/* 138 */     return this.environment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Environment createEnvironment() {
/* 148 */     return (Environment)new StandardServletEnvironment();
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
/*     */   public void setServletContext(ServletContext servletContext) {
/* 160 */     this.servletContext = servletContext;
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
/*     */   
/*     */   public void afterPropertiesSet() throws ServletException {
/* 173 */     initFilterBean();
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
/*     */   
/*     */   public void destroy() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void addRequiredProperty(String property) {
/* 197 */     this.requiredProperties.add(property);
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
/*     */ 
/*     */   
/*     */   public final void init(FilterConfig filterConfig) throws ServletException {
/* 211 */     Assert.notNull(filterConfig, "FilterConfig must not be null");
/*     */     
/* 213 */     this.filterConfig = filterConfig;
/*     */ 
/*     */     
/* 216 */     FilterConfigPropertyValues filterConfigPropertyValues = new FilterConfigPropertyValues(filterConfig, this.requiredProperties);
/* 217 */     if (!filterConfigPropertyValues.isEmpty()) {
/*     */       try {
/* 219 */         StandardServletEnvironment standardServletEnvironment; BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(this);
/* 220 */         ServletContextResourceLoader servletContextResourceLoader = new ServletContextResourceLoader(filterConfig.getServletContext());
/* 221 */         Environment env = this.environment;
/* 222 */         if (env == null) {
/* 223 */           standardServletEnvironment = new StandardServletEnvironment();
/*     */         }
/* 225 */         bw.registerCustomEditor(Resource.class, (PropertyEditor)new ResourceEditor((ResourceLoader)servletContextResourceLoader, (PropertyResolver)standardServletEnvironment));
/* 226 */         initBeanWrapper(bw);
/* 227 */         bw.setPropertyValues((PropertyValues)filterConfigPropertyValues, true);
/*     */       }
/* 229 */       catch (BeansException ex) {
/*     */         
/* 231 */         String msg = "Failed to set bean properties on filter '" + filterConfig.getFilterName() + "': " + ex.getMessage();
/* 232 */         this.logger.error(msg, (Throwable)ex);
/* 233 */         throw new NestedServletException(msg, ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 238 */     initFilterBean();
/*     */     
/* 240 */     if (this.logger.isDebugEnabled()) {
/* 241 */       this.logger.debug("Filter '" + filterConfig.getFilterName() + "' configured for use");
/*     */     }
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
/*     */ 
/*     */   
/*     */   protected void initBeanWrapper(BeanWrapper bw) throws BeansException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initFilterBean() throws ServletException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public FilterConfig getFilterConfig() {
/* 281 */     return this.filterConfig;
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
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getFilterName() {
/* 297 */     return (this.filterConfig != null) ? this.filterConfig.getFilterName() : this.beanName;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ServletContext getServletContext() {
/* 313 */     if (this.filterConfig != null) {
/* 314 */       return this.filterConfig.getServletContext();
/*     */     }
/* 316 */     if (this.servletContext != null) {
/* 317 */       return this.servletContext;
/*     */     }
/*     */     
/* 320 */     throw new IllegalStateException("No ServletContext");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FilterConfigPropertyValues
/*     */     extends MutablePropertyValues
/*     */   {
/*     */     public FilterConfigPropertyValues(FilterConfig config, Set<String> requiredProperties) throws ServletException {
/* 341 */       Set<String> missingProps = !CollectionUtils.isEmpty(requiredProperties) ? new HashSet<>(requiredProperties) : null;
/*     */ 
/*     */       
/* 344 */       Enumeration<String> paramNames = config.getInitParameterNames();
/* 345 */       while (paramNames.hasMoreElements()) {
/* 346 */         String property = paramNames.nextElement();
/* 347 */         Object value = config.getInitParameter(property);
/* 348 */         addPropertyValue(new PropertyValue(property, value));
/* 349 */         if (missingProps != null) {
/* 350 */           missingProps.remove(property);
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 355 */       if (!CollectionUtils.isEmpty(missingProps))
/* 356 */         throw new ServletException("Initialization from FilterConfig for filter '" + config
/* 357 */             .getFilterName() + "' failed; the following required properties were missing: " + 
/*     */             
/* 359 */             StringUtils.collectionToDelimitedString(missingProps, ", ")); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/filter/GenericFilterBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */