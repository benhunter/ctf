/*     */ package org.springframework.web.filter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.context.i18n.LocaleContextHolder;
/*     */ import org.springframework.web.context.request.RequestAttributes;
/*     */ import org.springframework.web.context.request.RequestContextHolder;
/*     */ import org.springframework.web.context.request.ServletRequestAttributes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RequestContextFilter
/*     */   extends OncePerRequestFilter
/*     */ {
/*     */   private boolean threadContextInheritable = false;
/*     */   
/*     */   public void setThreadContextInheritable(boolean threadContextInheritable) {
/*  68 */     this.threadContextInheritable = threadContextInheritable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean shouldNotFilterAsyncDispatch() {
/*  78 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean shouldNotFilterErrorDispatch() {
/*  87 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
/*  95 */     ServletRequestAttributes attributes = new ServletRequestAttributes(request, response);
/*  96 */     initContextHolders(request, attributes);
/*     */     
/*     */     try {
/*  99 */       filterChain.doFilter((ServletRequest)request, (ServletResponse)response);
/*     */     } finally {
/*     */       
/* 102 */       resetContextHolders();
/* 103 */       if (this.logger.isTraceEnabled()) {
/* 104 */         this.logger.trace("Cleared thread-bound request context: " + request);
/*     */       }
/* 106 */       attributes.requestCompleted();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void initContextHolders(HttpServletRequest request, ServletRequestAttributes requestAttributes) {
/* 111 */     LocaleContextHolder.setLocale(request.getLocale(), this.threadContextInheritable);
/* 112 */     RequestContextHolder.setRequestAttributes((RequestAttributes)requestAttributes, this.threadContextInheritable);
/* 113 */     if (this.logger.isTraceEnabled()) {
/* 114 */       this.logger.trace("Bound request context to thread: " + request);
/*     */     }
/*     */   }
/*     */   
/*     */   private void resetContextHolders() {
/* 119 */     LocaleContextHolder.resetLocaleContext();
/* 120 */     RequestContextHolder.resetRequestAttributes();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/filter/RequestContextFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */