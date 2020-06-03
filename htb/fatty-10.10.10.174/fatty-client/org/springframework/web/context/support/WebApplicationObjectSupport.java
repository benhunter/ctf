/*     */ package org.springframework.web.context.support;
/*     */ 
/*     */ import java.io.File;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.support.ApplicationObjectSupport;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.context.ServletContextAware;
/*     */ import org.springframework.web.context.WebApplicationContext;
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
/*     */ public abstract class WebApplicationObjectSupport
/*     */   extends ApplicationObjectSupport
/*     */   implements ServletContextAware
/*     */ {
/*     */   @Nullable
/*     */   private ServletContext servletContext;
/*     */   
/*     */   public final void setServletContext(ServletContext servletContext) {
/*  51 */     if (servletContext != this.servletContext) {
/*  52 */       this.servletContext = servletContext;
/*  53 */       initServletContext(servletContext);
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
/*     */   protected boolean isContextRequired() {
/*  68 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initApplicationContext(ApplicationContext context) {
/*  77 */     super.initApplicationContext(context);
/*  78 */     if (this.servletContext == null && context instanceof WebApplicationContext) {
/*  79 */       this.servletContext = ((WebApplicationContext)context).getServletContext();
/*  80 */       if (this.servletContext != null) {
/*  81 */         initServletContext(this.servletContext);
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initServletContext(ServletContext servletContext) {}
/*     */ 
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
/*     */   protected final WebApplicationContext getWebApplicationContext() throws IllegalStateException {
/* 109 */     ApplicationContext ctx = getApplicationContext();
/* 110 */     if (ctx instanceof WebApplicationContext) {
/* 111 */       return (WebApplicationContext)getApplicationContext();
/*     */     }
/* 113 */     if (isContextRequired()) {
/* 114 */       throw new IllegalStateException("WebApplicationObjectSupport instance [" + this + "] does not run in a WebApplicationContext but in: " + ctx);
/*     */     }
/*     */ 
/*     */     
/* 118 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected final ServletContext getServletContext() throws IllegalStateException {
/* 129 */     if (this.servletContext != null) {
/* 130 */       return this.servletContext;
/*     */     }
/* 132 */     ServletContext servletContext = null;
/* 133 */     WebApplicationContext wac = getWebApplicationContext();
/* 134 */     if (wac != null) {
/* 135 */       servletContext = wac.getServletContext();
/*     */     }
/* 137 */     if (servletContext == null && isContextRequired()) {
/* 138 */       throw new IllegalStateException("WebApplicationObjectSupport instance [" + this + "] does not run within a ServletContext. Make sure the object is fully configured!");
/*     */     }
/*     */     
/* 141 */     return servletContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final File getTempDir() throws IllegalStateException {
/* 152 */     ServletContext servletContext = getServletContext();
/* 153 */     Assert.state((servletContext != null), "ServletContext is required");
/* 154 */     return WebUtils.getTempDir(servletContext);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/support/WebApplicationObjectSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */