/*    */ package org.springframework.web.context;
/*    */ 
/*    */ import javax.servlet.ServletConfig;
/*    */ import javax.servlet.ServletContext;
/*    */ import org.springframework.context.ConfigurableApplicationContext;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ public interface ConfigurableWebApplicationContext
/*    */   extends WebApplicationContext, ConfigurableApplicationContext
/*    */ {
/* 46 */   public static final String APPLICATION_CONTEXT_ID_PREFIX = WebApplicationContext.class.getName() + ":";
/*    */   public static final String SERVLET_CONFIG_BEAN_NAME = "servletConfig";
/*    */   
/*    */   void setServletContext(@Nullable ServletContext paramServletContext);
/*    */   
/*    */   void setServletConfig(@Nullable ServletConfig paramServletConfig);
/*    */   
/*    */   @Nullable
/*    */   ServletConfig getServletConfig();
/*    */   
/*    */   void setNamespace(@Nullable String paramString);
/*    */   
/*    */   @Nullable
/*    */   String getNamespace();
/*    */   
/*    */   void setConfigLocation(String paramString);
/*    */   
/*    */   void setConfigLocations(String... paramVarArgs);
/*    */   
/*    */   @Nullable
/*    */   String[] getConfigLocations();
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/ConfigurableWebApplicationContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */