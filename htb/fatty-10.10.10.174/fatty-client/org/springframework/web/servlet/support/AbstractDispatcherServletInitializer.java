/*     */ package org.springframework.web.servlet.support;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import javax.servlet.DispatcherType;
/*     */ import javax.servlet.Filter;
/*     */ import javax.servlet.FilterRegistration;
/*     */ import javax.servlet.Servlet;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRegistration;
/*     */ import org.springframework.context.ApplicationContextInitializer;
/*     */ import org.springframework.core.Conventions;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.web.context.AbstractContextLoaderInitializer;
/*     */ import org.springframework.web.context.WebApplicationContext;
/*     */ import org.springframework.web.servlet.DispatcherServlet;
/*     */ import org.springframework.web.servlet.FrameworkServlet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractDispatcherServletInitializer
/*     */   extends AbstractContextLoaderInitializer
/*     */ {
/*     */   public static final String DEFAULT_SERVLET_NAME = "dispatcher";
/*     */   
/*     */   public void onStartup(ServletContext servletContext) throws ServletException {
/*  62 */     super.onStartup(servletContext);
/*  63 */     registerDispatcherServlet(servletContext);
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
/*     */   protected void registerDispatcherServlet(ServletContext servletContext) {
/*  78 */     String servletName = getServletName();
/*  79 */     Assert.hasLength(servletName, "getServletName() must not return null or empty");
/*     */     
/*  81 */     WebApplicationContext servletAppContext = createServletApplicationContext();
/*  82 */     Assert.notNull(servletAppContext, "createServletApplicationContext() must not return null");
/*     */     
/*  84 */     FrameworkServlet dispatcherServlet = createDispatcherServlet(servletAppContext);
/*  85 */     Assert.notNull(dispatcherServlet, "createDispatcherServlet(WebApplicationContext) must not return null");
/*  86 */     dispatcherServlet.setContextInitializers((ApplicationContextInitializer[])getServletApplicationContextInitializers());
/*     */     
/*  88 */     ServletRegistration.Dynamic registration = servletContext.addServlet(servletName, (Servlet)dispatcherServlet);
/*  89 */     if (registration == null) {
/*  90 */       throw new IllegalStateException("Failed to register servlet with name '" + servletName + "'. Check if there is another servlet registered under the same name.");
/*     */     }
/*     */ 
/*     */     
/*  94 */     registration.setLoadOnStartup(1);
/*  95 */     registration.addMapping(getServletMappings());
/*  96 */     registration.setAsyncSupported(isAsyncSupported());
/*     */     
/*  98 */     Filter[] filters = getServletFilters();
/*  99 */     if (!ObjectUtils.isEmpty((Object[])filters)) {
/* 100 */       for (Filter filter : filters) {
/* 101 */         registerServletFilter(servletContext, filter);
/*     */       }
/*     */     }
/*     */     
/* 105 */     customizeRegistration(registration);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getServletName() {
/* 114 */     return "dispatcher";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract WebApplicationContext createServletApplicationContext();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FrameworkServlet createDispatcherServlet(WebApplicationContext servletAppContext) {
/* 134 */     return (FrameworkServlet)new DispatcherServlet(servletAppContext);
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
/*     */   @Nullable
/*     */   protected ApplicationContextInitializer<?>[] getServletApplicationContextInitializers() {
/* 147 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract String[] getServletMappings();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Filter[] getServletFilters() {
/* 164 */     return null;
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected FilterRegistration.Dynamic registerServletFilter(ServletContext servletContext, Filter filter) {
/* 185 */     String filterName = Conventions.getVariableName(filter);
/* 186 */     FilterRegistration.Dynamic registration = servletContext.addFilter(filterName, filter);
/*     */     
/* 188 */     if (registration == null) {
/* 189 */       int counter = 0;
/* 190 */       while (registration == null) {
/* 191 */         if (counter == 100) {
/* 192 */           throw new IllegalStateException("Failed to register filter with name '" + filterName + "'. Check if there is another filter registered under the same name.");
/*     */         }
/*     */         
/* 195 */         registration = servletContext.addFilter(filterName + "#" + counter, filter);
/* 196 */         counter++;
/*     */       } 
/*     */     } 
/*     */     
/* 200 */     registration.setAsyncSupported(isAsyncSupported());
/* 201 */     registration.addMappingForServletNames(getDispatcherTypes(), false, new String[] { getServletName() });
/* 202 */     return registration;
/*     */   }
/*     */   
/*     */   private EnumSet<DispatcherType> getDispatcherTypes() {
/* 206 */     return isAsyncSupported() ? 
/* 207 */       EnumSet.<DispatcherType>of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.ASYNC) : 
/* 208 */       EnumSet.<DispatcherType>of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isAsyncSupported() {
/* 217 */     return true;
/*     */   }
/*     */   
/*     */   protected void customizeRegistration(ServletRegistration.Dynamic registration) {}
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/support/AbstractDispatcherServletInitializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */