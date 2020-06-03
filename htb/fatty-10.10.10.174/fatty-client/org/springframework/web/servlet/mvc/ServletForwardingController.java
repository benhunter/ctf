/*     */ package org.springframework.web.servlet.mvc;
/*     */ 
/*     */ import javax.servlet.RequestDispatcher;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.beans.factory.BeanNameAware;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.servlet.ModelAndView;
/*     */ import org.springframework.web.util.WebUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServletForwardingController
/*     */   extends AbstractController
/*     */   implements BeanNameAware
/*     */ {
/*     */   @Nullable
/*     */   private String servletName;
/*     */   @Nullable
/*     */   private String beanName;
/*     */   
/*     */   public ServletForwardingController() {
/*  99 */     super(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServletName(String servletName) {
/* 109 */     this.servletName = servletName;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanName(String name) {
/* 114 */     this.beanName = name;
/* 115 */     if (this.servletName == null) {
/* 116 */       this.servletName = name;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 125 */     ServletContext servletContext = getServletContext();
/* 126 */     Assert.state((servletContext != null), "No ServletContext");
/* 127 */     RequestDispatcher rd = servletContext.getNamedDispatcher(this.servletName);
/* 128 */     if (rd == null) {
/* 129 */       throw new ServletException("No servlet with name '" + this.servletName + "' defined in web.xml");
/*     */     }
/*     */ 
/*     */     
/* 133 */     if (useInclude(request, response)) {
/* 134 */       rd.include((ServletRequest)request, (ServletResponse)response);
/* 135 */       if (this.logger.isTraceEnabled()) {
/* 136 */         this.logger.trace("Included servlet [" + this.servletName + "] in ServletForwardingController '" + this.beanName + "'");
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 141 */       rd.forward((ServletRequest)request, (ServletResponse)response);
/* 142 */       if (this.logger.isTraceEnabled()) {
/* 143 */         this.logger.trace("Forwarded to servlet [" + this.servletName + "] in ServletForwardingController '" + this.beanName + "'");
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 148 */     return null;
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
/*     */   protected boolean useInclude(HttpServletRequest request, HttpServletResponse response) {
/* 166 */     return (WebUtils.isIncludeRequest((ServletRequest)request) || response.isCommitted());
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/ServletForwardingController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */