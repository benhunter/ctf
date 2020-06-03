/*     */ package org.springframework.web.servlet.mvc;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Properties;
/*     */ import javax.servlet.Servlet;
/*     */ import javax.servlet.ServletConfig;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.beans.factory.BeanNameAware;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.web.servlet.ModelAndView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServletWrappingController
/*     */   extends AbstractController
/*     */   implements BeanNameAware, InitializingBean, DisposableBean
/*     */ {
/*     */   @Nullable
/*     */   private Class<? extends Servlet> servletClass;
/*     */   @Nullable
/*     */   private String servletName;
/*  94 */   private Properties initParameters = new Properties();
/*     */   
/*     */   @Nullable
/*     */   private String beanName;
/*     */   
/*     */   @Nullable
/*     */   private Servlet servletInstance;
/*     */ 
/*     */   
/*     */   public ServletWrappingController() {
/* 104 */     super(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServletClass(Class<? extends Servlet> servletClass) {
/* 114 */     this.servletClass = servletClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServletName(String servletName) {
/* 122 */     this.servletName = servletName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInitParameters(Properties initParameters) {
/* 130 */     this.initParameters = initParameters;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanName(String name) {
/* 135 */     this.beanName = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws Exception {
/* 145 */     if (this.servletClass == null) {
/* 146 */       throw new IllegalArgumentException("'servletClass' is required");
/*     */     }
/* 148 */     if (this.servletName == null) {
/* 149 */       this.servletName = this.beanName;
/*     */     }
/* 151 */     this.servletInstance = ReflectionUtils.accessibleConstructor(this.servletClass, new Class[0]).newInstance(new Object[0]);
/* 152 */     this.servletInstance.init(new DelegatingServletConfig());
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
/*     */   protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 164 */     Assert.state((this.servletInstance != null), "No Servlet instance");
/* 165 */     this.servletInstance.service((ServletRequest)request, (ServletResponse)response);
/* 166 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 176 */     if (this.servletInstance != null) {
/* 177 */       this.servletInstance.destroy();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class DelegatingServletConfig
/*     */     implements ServletConfig
/*     */   {
/*     */     private DelegatingServletConfig() {}
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getServletName() {
/* 192 */       return ServletWrappingController.this.servletName;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public ServletContext getServletContext() {
/* 198 */       return ServletWrappingController.this.getServletContext();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getInitParameter(String paramName) {
/* 203 */       return ServletWrappingController.this.initParameters.getProperty(paramName);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Enumeration<String> getInitParameterNames() {
/* 209 */       return (Enumeration)ServletWrappingController.this.initParameters.keys();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/ServletWrappingController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */