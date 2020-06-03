/*    */ package org.springframework.web.context.support;
/*    */ 
/*    */ import javax.servlet.ServletConfig;
/*    */ import javax.servlet.ServletContext;
/*    */ import org.springframework.core.env.MutablePropertySources;
/*    */ import org.springframework.core.env.PropertySource;
/*    */ import org.springframework.core.env.StandardEnvironment;
/*    */ import org.springframework.jndi.JndiLocatorDelegate;
/*    */ import org.springframework.jndi.JndiPropertySource;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.web.context.ConfigurableWebEnvironment;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StandardServletEnvironment
/*    */   extends StandardEnvironment
/*    */   implements ConfigurableWebEnvironment
/*    */ {
/*    */   public static final String SERVLET_CONTEXT_PROPERTY_SOURCE_NAME = "servletContextInitParams";
/*    */   public static final String SERVLET_CONFIG_PROPERTY_SOURCE_NAME = "servletConfigInitParams";
/*    */   public static final String JNDI_PROPERTY_SOURCE_NAME = "jndiProperties";
/*    */   
/*    */   protected void customizePropertySources(MutablePropertySources propertySources) {
/* 85 */     propertySources.addLast((PropertySource)new PropertySource.StubPropertySource("servletConfigInitParams"));
/* 86 */     propertySources.addLast((PropertySource)new PropertySource.StubPropertySource("servletContextInitParams"));
/* 87 */     if (JndiLocatorDelegate.isDefaultJndiEnvironmentAvailable()) {
/* 88 */       propertySources.addLast((PropertySource)new JndiPropertySource("jndiProperties"));
/*    */     }
/* 90 */     super.customizePropertySources(propertySources);
/*    */   }
/*    */ 
/*    */   
/*    */   public void initPropertySources(@Nullable ServletContext servletContext, @Nullable ServletConfig servletConfig) {
/* 95 */     WebApplicationContextUtils.initServletPropertySources(getPropertySources(), servletContext, servletConfig);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/support/StandardServletEnvironment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */