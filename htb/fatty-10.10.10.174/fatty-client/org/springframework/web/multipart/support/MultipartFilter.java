/*     */ package org.springframework.web.multipart.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.web.context.WebApplicationContext;
/*     */ import org.springframework.web.context.support.WebApplicationContextUtils;
/*     */ import org.springframework.web.filter.OncePerRequestFilter;
/*     */ import org.springframework.web.multipart.MultipartHttpServletRequest;
/*     */ import org.springframework.web.multipart.MultipartResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MultipartFilter
/*     */   extends OncePerRequestFilter
/*     */ {
/*     */   public static final String DEFAULT_MULTIPART_RESOLVER_BEAN_NAME = "filterMultipartResolver";
/*  73 */   private final MultipartResolver defaultMultipartResolver = new StandardServletMultipartResolver();
/*     */   
/*  75 */   private String multipartResolverBeanName = "filterMultipartResolver";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMultipartResolverBeanName(String multipartResolverBeanName) {
/*  83 */     this.multipartResolverBeanName = multipartResolverBeanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getMultipartResolverBeanName() {
/*  91 */     return this.multipartResolverBeanName;
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
/*     */   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
/*     */     MultipartHttpServletRequest multipartHttpServletRequest;
/* 107 */     MultipartResolver multipartResolver = lookupMultipartResolver(request);
/*     */     
/* 109 */     HttpServletRequest processedRequest = request;
/* 110 */     if (multipartResolver.isMultipart(processedRequest)) {
/* 111 */       if (this.logger.isTraceEnabled()) {
/* 112 */         this.logger.trace("Resolving multipart request");
/*     */       }
/* 114 */       multipartHttpServletRequest = multipartResolver.resolveMultipart(processedRequest);
/*     */ 
/*     */     
/*     */     }
/* 118 */     else if (this.logger.isTraceEnabled()) {
/* 119 */       this.logger.trace("Not a multipart request");
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 124 */       filterChain.doFilter((ServletRequest)multipartHttpServletRequest, (ServletResponse)response);
/*     */     } finally {
/*     */       
/* 127 */       if (multipartHttpServletRequest instanceof MultipartHttpServletRequest) {
/* 128 */         multipartResolver.cleanupMultipart(multipartHttpServletRequest);
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
/*     */   protected MultipartResolver lookupMultipartResolver(HttpServletRequest request) {
/* 142 */     return lookupMultipartResolver();
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
/*     */   protected MultipartResolver lookupMultipartResolver() {
/* 154 */     WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
/* 155 */     String beanName = getMultipartResolverBeanName();
/* 156 */     if (wac != null && wac.containsBean(beanName)) {
/* 157 */       if (this.logger.isDebugEnabled()) {
/* 158 */         this.logger.debug("Using MultipartResolver '" + beanName + "' for MultipartFilter");
/*     */       }
/* 160 */       return (MultipartResolver)wac.getBean(beanName, MultipartResolver.class);
/*     */     } 
/*     */     
/* 163 */     return this.defaultMultipartResolver;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/multipart/support/MultipartFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */