/*     */ package org.springframework.web.servlet.resource;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.servlet.RequestDispatcher;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.HttpRequestHandler;
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
/*     */ public class DefaultServletHttpRequestHandler
/*     */   implements HttpRequestHandler, ServletContextAware
/*     */ {
/*     */   private static final String COMMON_DEFAULT_SERVLET_NAME = "default";
/*     */   private static final String GAE_DEFAULT_SERVLET_NAME = "_ah_default";
/*     */   private static final String RESIN_DEFAULT_SERVLET_NAME = "resin-file";
/*     */   private static final String WEBLOGIC_DEFAULT_SERVLET_NAME = "FileServlet";
/*     */   private static final String WEBSPHERE_DEFAULT_SERVLET_NAME = "SimpleFileServlet";
/*     */   @Nullable
/*     */   private String defaultServletName;
/*     */   @Nullable
/*     */   private ServletContext servletContext;
/*     */   
/*     */   public void setDefaultServletName(String defaultServletName) {
/*  82 */     this.defaultServletName = defaultServletName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServletContext(ServletContext servletContext) {
/*  92 */     this.servletContext = servletContext;
/*  93 */     if (!StringUtils.hasText(this.defaultServletName)) {
/*  94 */       if (this.servletContext.getNamedDispatcher("default") != null) {
/*  95 */         this.defaultServletName = "default";
/*     */       }
/*  97 */       else if (this.servletContext.getNamedDispatcher("_ah_default") != null) {
/*  98 */         this.defaultServletName = "_ah_default";
/*     */       }
/* 100 */       else if (this.servletContext.getNamedDispatcher("resin-file") != null) {
/* 101 */         this.defaultServletName = "resin-file";
/*     */       }
/* 103 */       else if (this.servletContext.getNamedDispatcher("FileServlet") != null) {
/* 104 */         this.defaultServletName = "FileServlet";
/*     */       }
/* 106 */       else if (this.servletContext.getNamedDispatcher("SimpleFileServlet") != null) {
/* 107 */         this.defaultServletName = "SimpleFileServlet";
/*     */       } else {
/*     */         
/* 110 */         throw new IllegalStateException("Unable to locate the default servlet for serving static content. Please set the 'defaultServletName' property explicitly.");
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/* 121 */     Assert.state((this.servletContext != null), "No ServletContext set");
/* 122 */     RequestDispatcher rd = this.servletContext.getNamedDispatcher(this.defaultServletName);
/* 123 */     if (rd == null) {
/* 124 */       throw new IllegalStateException("A RequestDispatcher could not be located for the default servlet '" + this.defaultServletName + "'");
/*     */     }
/*     */     
/* 127 */     rd.forward((ServletRequest)request, (ServletResponse)response);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/resource/DefaultServletHttpRequestHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */