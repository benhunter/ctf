/*     */ package org.springframework.web.filter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletRequestWrapper;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HiddenHttpMethodFilter
/*     */   extends OncePerRequestFilter
/*     */ {
/*  59 */   private static final List<String> ALLOWED_METHODS = Collections.unmodifiableList(Arrays.asList(new String[] { HttpMethod.PUT.name(), HttpMethod.DELETE
/*  60 */           .name(), HttpMethod.PATCH.name() }));
/*     */ 
/*     */   
/*     */   public static final String DEFAULT_METHOD_PARAM = "_method";
/*     */   
/*  65 */   private String methodParam = "_method";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMethodParam(String methodParam) {
/*  73 */     Assert.hasText(methodParam, "'methodParam' must not be empty");
/*  74 */     this.methodParam = methodParam;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
/*     */     HttpMethodRequestWrapper httpMethodRequestWrapper;
/*  81 */     HttpServletRequest requestToUse = request;
/*     */     
/*  83 */     if ("POST".equals(request.getMethod()) && request.getAttribute("javax.servlet.error.exception") == null) {
/*  84 */       String paramValue = request.getParameter(this.methodParam);
/*  85 */       if (StringUtils.hasLength(paramValue)) {
/*  86 */         String method = paramValue.toUpperCase(Locale.ENGLISH);
/*  87 */         if (ALLOWED_METHODS.contains(method)) {
/*  88 */           httpMethodRequestWrapper = new HttpMethodRequestWrapper(request, method);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  93 */     filterChain.doFilter((ServletRequest)httpMethodRequestWrapper, (ServletResponse)response);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class HttpMethodRequestWrapper
/*     */     extends HttpServletRequestWrapper
/*     */   {
/*     */     private final String method;
/*     */ 
/*     */ 
/*     */     
/*     */     public HttpMethodRequestWrapper(HttpServletRequest request, String method) {
/* 106 */       super(request);
/* 107 */       this.method = method;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getMethod() {
/* 112 */       return this.method;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/filter/HiddenHttpMethodFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */