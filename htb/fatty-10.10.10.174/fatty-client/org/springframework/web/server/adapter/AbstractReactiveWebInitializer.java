/*     */ package org.springframework.web.server.adapter;
/*     */ 
/*     */ import java.util.EventListener;
/*     */ import javax.servlet.Servlet;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletContextEvent;
/*     */ import javax.servlet.ServletContextListener;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRegistration;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.context.annotation.AnnotationConfigApplicationContext;
/*     */ import org.springframework.http.server.reactive.HttpHandler;
/*     */ import org.springframework.http.server.reactive.ServletHttpHandlerAdapter;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.WebApplicationInitializer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractReactiveWebInitializer
/*     */   implements WebApplicationInitializer
/*     */ {
/*     */   public static final String DEFAULT_SERVLET_NAME = "http-handler-adapter";
/*     */   
/*     */   public void onStartup(ServletContext servletContext) throws ServletException {
/*  56 */     String servletName = getServletName();
/*  57 */     Assert.hasLength(servletName, "getServletName() must not return null or empty");
/*     */     
/*  59 */     ApplicationContext applicationContext = createApplicationContext();
/*  60 */     Assert.notNull(applicationContext, "createApplicationContext() must not return null");
/*     */     
/*  62 */     refreshApplicationContext(applicationContext);
/*  63 */     registerCloseListener(servletContext, applicationContext);
/*     */     
/*  65 */     HttpHandler httpHandler = WebHttpHandlerBuilder.applicationContext(applicationContext).build();
/*  66 */     ServletHttpHandlerAdapter servlet = new ServletHttpHandlerAdapter(httpHandler);
/*     */     
/*  68 */     ServletRegistration.Dynamic registration = servletContext.addServlet(servletName, (Servlet)servlet);
/*  69 */     if (registration == null) {
/*  70 */       throw new IllegalStateException("Failed to register servlet with name '" + servletName + "'. Check if there is another servlet registered under the same name.");
/*     */     }
/*     */ 
/*     */     
/*  74 */     registration.setLoadOnStartup(1);
/*  75 */     registration.addMapping(new String[] { getServletMapping() });
/*  76 */     registration.setAsyncSupported(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getServletName() {
/*  84 */     return "http-handler-adapter";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ApplicationContext createApplicationContext() {
/*  92 */     AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
/*  93 */     Class<?>[] configClasses = getConfigClasses();
/*  94 */     Assert.notEmpty((Object[])configClasses, "No Spring configuration provided through getConfigClasses()");
/*  95 */     context.register(configClasses);
/*  96 */     return (ApplicationContext)context;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Class<?>[] getConfigClasses();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void refreshApplicationContext(ApplicationContext context) {
/* 111 */     if (context instanceof ConfigurableApplicationContext) {
/* 112 */       ConfigurableApplicationContext cac = (ConfigurableApplicationContext)context;
/* 113 */       if (!cac.isActive()) {
/* 114 */         cac.refresh();
/*     */       }
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
/*     */   protected void registerCloseListener(ServletContext servletContext, ApplicationContext applicationContext) {
/* 127 */     if (applicationContext instanceof ConfigurableApplicationContext) {
/* 128 */       ConfigurableApplicationContext cac = (ConfigurableApplicationContext)applicationContext;
/* 129 */       ServletContextDestroyedListener listener = new ServletContextDestroyedListener(cac);
/* 130 */       servletContext.addListener((EventListener)listener);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getServletMapping() {
/* 140 */     return "/";
/*     */   }
/*     */   
/*     */   private static class ServletContextDestroyedListener
/*     */     implements ServletContextListener
/*     */   {
/*     */     private final ConfigurableApplicationContext applicationContext;
/*     */     
/*     */     public ServletContextDestroyedListener(ConfigurableApplicationContext applicationContext) {
/* 149 */       this.applicationContext = applicationContext;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void contextInitialized(ServletContextEvent sce) {}
/*     */ 
/*     */     
/*     */     public void contextDestroyed(ServletContextEvent sce) {
/* 158 */       this.applicationContext.close();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/server/adapter/AbstractReactiveWebInitializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */