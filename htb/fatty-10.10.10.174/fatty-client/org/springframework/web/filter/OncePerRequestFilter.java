/*     */ package org.springframework.web.filter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.servlet.DispatcherType;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.web.context.request.async.WebAsyncUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class OncePerRequestFilter
/*     */   extends GenericFilterBean
/*     */ {
/*     */   public static final String ALREADY_FILTERED_SUFFIX = ".FILTERED";
/*     */   
/*     */   public final void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
/*  90 */     if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
/*  91 */       throw new ServletException("OncePerRequestFilter just supports HTTP requests");
/*     */     }
/*  93 */     HttpServletRequest httpRequest = (HttpServletRequest)request;
/*  94 */     HttpServletResponse httpResponse = (HttpServletResponse)response;
/*     */     
/*  96 */     String alreadyFilteredAttributeName = getAlreadyFilteredAttributeName();
/*  97 */     boolean hasAlreadyFilteredAttribute = (request.getAttribute(alreadyFilteredAttributeName) != null);
/*     */     
/*  99 */     if (skipDispatch(httpRequest) || shouldNotFilter(httpRequest)) {
/*     */ 
/*     */       
/* 102 */       filterChain.doFilter(request, response);
/*     */     }
/* 104 */     else if (hasAlreadyFilteredAttribute) {
/*     */       
/* 106 */       if (DispatcherType.ERROR.equals(request.getDispatcherType())) {
/* 107 */         doFilterNestedErrorDispatch(httpRequest, httpResponse, filterChain);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 112 */       filterChain.doFilter(request, response);
/*     */     }
/*     */     else {
/*     */       
/* 116 */       request.setAttribute(alreadyFilteredAttributeName, Boolean.TRUE);
/*     */       try {
/* 118 */         doFilterInternal(httpRequest, httpResponse, filterChain);
/*     */       }
/*     */       finally {
/*     */         
/* 122 */         request.removeAttribute(alreadyFilteredAttributeName);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean skipDispatch(HttpServletRequest request) {
/* 128 */     if (isAsyncDispatch(request) && shouldNotFilterAsyncDispatch()) {
/* 129 */       return true;
/*     */     }
/* 131 */     if (request.getAttribute("javax.servlet.error.request_uri") != null && shouldNotFilterErrorDispatch()) {
/* 132 */       return true;
/*     */     }
/* 134 */     return false;
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
/*     */   protected boolean isAsyncDispatch(HttpServletRequest request) {
/* 147 */     return WebAsyncUtils.getAsyncManager((ServletRequest)request).hasConcurrentResult();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isAsyncStarted(HttpServletRequest request) {
/* 158 */     return WebAsyncUtils.getAsyncManager((ServletRequest)request).isConcurrentHandlingStarted();
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
/*     */   protected String getAlreadyFilteredAttributeName() {
/* 171 */     String name = getFilterName();
/* 172 */     if (name == null) {
/* 173 */       name = getClass().getName();
/*     */     }
/* 175 */     return name + ".FILTERED";
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
/*     */   protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
/* 187 */     return false;
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
/*     */   protected boolean shouldNotFilterAsyncDispatch() {
/* 208 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean shouldNotFilterErrorDispatch() {
/* 219 */     return true;
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
/*     */   protected abstract void doFilterInternal(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, FilterChain paramFilterChain) throws ServletException, IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doFilterNestedErrorDispatch(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
/* 250 */     doFilter((ServletRequest)request, (ServletResponse)response, filterChain);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/filter/OncePerRequestFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */