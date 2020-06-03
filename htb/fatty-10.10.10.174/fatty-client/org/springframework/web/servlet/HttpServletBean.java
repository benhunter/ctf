/*     */ package org.springframework.web.servlet;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.servlet.ServletConfig;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServlet;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeanWrapper;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.PropertyAccessorFactory;
/*     */ import org.springframework.beans.PropertyValue;
/*     */ import org.springframework.beans.PropertyValues;
/*     */ import org.springframework.context.EnvironmentAware;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
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
/*     */ import org.springframework.web.context.support.ServletContextResourceLoader;
/*     */ import org.springframework.web.context.support.StandardServletEnvironment;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class HttpServletBean
/*     */   extends HttpServlet
/*     */   implements EnvironmentCapable, EnvironmentAware
/*     */ {
/*  85 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   @Nullable
/*     */   private ConfigurableEnvironment environment;
/*     */   
/*  90 */   private final Set<String> requiredProperties = new HashSet<>(4);
/*     */ 
/*     */ 
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
/* 103 */     this.requiredProperties.add(property);
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
/*     */   public void setEnvironment(Environment environment) {
/* 115 */     Assert.isInstanceOf(ConfigurableEnvironment.class, environment, "ConfigurableEnvironment required");
/* 116 */     this.environment = (ConfigurableEnvironment)environment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurableEnvironment getEnvironment() {
/* 126 */     if (this.environment == null) {
/* 127 */       this.environment = createEnvironment();
/*     */     }
/* 129 */     return this.environment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ConfigurableEnvironment createEnvironment() {
/* 138 */     return (ConfigurableEnvironment)new StandardServletEnvironment();
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
/*     */   public final void init() throws ServletException {
/* 151 */     ServletConfigPropertyValues servletConfigPropertyValues = new ServletConfigPropertyValues(getServletConfig(), this.requiredProperties);
/* 152 */     if (!servletConfigPropertyValues.isEmpty()) {
/*     */       try {
/* 154 */         BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(this);
/* 155 */         ServletContextResourceLoader servletContextResourceLoader = new ServletContextResourceLoader(getServletContext());
/* 156 */         bw.registerCustomEditor(Resource.class, (PropertyEditor)new ResourceEditor((ResourceLoader)servletContextResourceLoader, (PropertyResolver)getEnvironment()));
/* 157 */         initBeanWrapper(bw);
/* 158 */         bw.setPropertyValues((PropertyValues)servletConfigPropertyValues, true);
/*     */       }
/* 160 */       catch (BeansException ex) {
/* 161 */         if (this.logger.isErrorEnabled()) {
/* 162 */           this.logger.error("Failed to set bean properties on servlet '" + getServletName() + "'", (Throwable)ex);
/*     */         }
/* 164 */         throw ex;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 169 */     initServletBean();
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
/*     */   protected void initServletBean() throws ServletException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getServletName() {
/* 201 */     return (getServletConfig() != null) ? getServletConfig().getServletName() : null;
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
/*     */   private static class ServletConfigPropertyValues
/*     */     extends MutablePropertyValues
/*     */   {
/*     */     public ServletConfigPropertyValues(ServletConfig config, Set<String> requiredProperties) throws ServletException {
/* 220 */       Set<String> missingProps = !CollectionUtils.isEmpty(requiredProperties) ? new HashSet<>(requiredProperties) : null;
/*     */ 
/*     */       
/* 223 */       Enumeration<String> paramNames = config.getInitParameterNames();
/* 224 */       while (paramNames.hasMoreElements()) {
/* 225 */         String property = paramNames.nextElement();
/* 226 */         Object value = config.getInitParameter(property);
/* 227 */         addPropertyValue(new PropertyValue(property, value));
/* 228 */         if (missingProps != null) {
/* 229 */           missingProps.remove(property);
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 234 */       if (!CollectionUtils.isEmpty(missingProps))
/* 235 */         throw new ServletException("Initialization from ServletConfig for servlet '" + config
/* 236 */             .getServletName() + "' failed; the following required properties were missing: " + 
/*     */             
/* 238 */             StringUtils.collectionToDelimitedString(missingProps, ", ")); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/HttpServletBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */