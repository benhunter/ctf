/*     */ package org.springframework.web.servlet.tags;
/*     */ 
/*     */ import javax.servlet.jsp.JspException;
/*     */ import javax.servlet.jsp.JspTagException;
/*     */ import javax.servlet.jsp.tagext.TagSupport;
/*     */ import javax.servlet.jsp.tagext.TryCatchFinally;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.servlet.support.JspAwareRequestContext;
/*     */ import org.springframework.web.servlet.support.RequestContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class RequestContextAwareTag
/*     */   extends TagSupport
/*     */   implements TryCatchFinally
/*     */ {
/*     */   public static final String REQUEST_CONTEXT_PAGE_ATTRIBUTE = "org.springframework.web.servlet.tags.REQUEST_CONTEXT";
/*  62 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private RequestContext requestContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int doStartTag() throws JspException {
/*     */     try {
/*  78 */       this.requestContext = (RequestContext)this.pageContext.getAttribute("org.springframework.web.servlet.tags.REQUEST_CONTEXT");
/*  79 */       if (this.requestContext == null) {
/*  80 */         this.requestContext = (RequestContext)new JspAwareRequestContext(this.pageContext);
/*  81 */         this.pageContext.setAttribute("org.springframework.web.servlet.tags.REQUEST_CONTEXT", this.requestContext);
/*     */       } 
/*  83 */       return doStartTagInternal();
/*     */     }
/*  85 */     catch (JspException|RuntimeException ex) {
/*  86 */       this.logger.error(ex.getMessage(), ex);
/*  87 */       throw ex;
/*     */     }
/*  89 */     catch (Exception ex) {
/*  90 */       this.logger.error(ex.getMessage(), ex);
/*  91 */       throw new JspTagException(ex.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final RequestContext getRequestContext() {
/*  99 */     Assert.state((this.requestContext != null), "No current RequestContext");
/* 100 */     return this.requestContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract int doStartTagInternal() throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doCatch(Throwable throwable) throws Throwable {
/* 115 */     throw throwable;
/*     */   }
/*     */ 
/*     */   
/*     */   public void doFinally() {
/* 120 */     this.requestContext = null;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/RequestContextAwareTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */