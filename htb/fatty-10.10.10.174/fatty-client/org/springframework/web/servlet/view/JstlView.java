/*     */ package org.springframework.web.servlet.view;
/*     */ 
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.context.MessageSource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.web.servlet.support.JstlUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JstlView
/*     */   extends InternalResourceView
/*     */ {
/*     */   @Nullable
/*     */   private MessageSource messageSource;
/*     */   
/*     */   public JstlView() {}
/*     */   
/*     */   public JstlView(String url) {
/*  96 */     super(url);
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
/*     */   public JstlView(String url, MessageSource messageSource) {
/* 108 */     this(url);
/* 109 */     this.messageSource = messageSource;
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
/*     */   protected void initServletContext(ServletContext servletContext) {
/* 121 */     if (this.messageSource != null) {
/* 122 */       this.messageSource = JstlUtils.getJstlAwareMessageSource(servletContext, this.messageSource);
/*     */     }
/* 124 */     super.initServletContext(servletContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void exposeHelpers(HttpServletRequest request) throws Exception {
/* 133 */     if (this.messageSource != null) {
/* 134 */       JstlUtils.exposeLocalizationContext(request, this.messageSource);
/*     */     } else {
/*     */       
/* 137 */       JstlUtils.exposeLocalizationContext(new RequestContext(request, getServletContext()));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/JstlView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */