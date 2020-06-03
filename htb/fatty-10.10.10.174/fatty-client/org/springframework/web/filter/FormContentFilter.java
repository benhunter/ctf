/*     */ package org.springframework.web.filter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletRequestWrapper;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.FormHttpMessageConverter;
/*     */ import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
/*     */ import org.springframework.http.server.ServletServerHttpRequest;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ public class FormContentFilter
/*     */   extends OncePerRequestFilter
/*     */ {
/*  58 */   private static final List<String> HTTP_METHODS = Arrays.asList(new String[] { "PUT", "PATCH", "DELETE" });
/*     */   
/*  60 */   private FormHttpMessageConverter formConverter = (FormHttpMessageConverter)new AllEncompassingFormHttpMessageConverter();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFormConverter(FormHttpMessageConverter converter) {
/*  68 */     Assert.notNull(converter, "FormHttpMessageConverter is required");
/*  69 */     this.formConverter = converter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCharset(Charset charset) {
/*  78 */     this.formConverter.setCharset(charset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
/*  87 */     MultiValueMap<String, String> params = parseIfNecessary(request);
/*  88 */     if (!CollectionUtils.isEmpty((Map)params)) {
/*  89 */       filterChain.doFilter((ServletRequest)new FormContentRequestWrapper(request, params), (ServletResponse)response);
/*     */     } else {
/*     */       
/*  92 */       filterChain.doFilter((ServletRequest)request, (ServletResponse)response);
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private MultiValueMap<String, String> parseIfNecessary(final HttpServletRequest request) throws IOException {
/*  98 */     if (!shouldParse(request)) {
/*  99 */       return null;
/*     */     }
/*     */     
/* 102 */     ServletServerHttpRequest servletServerHttpRequest = new ServletServerHttpRequest(request)
/*     */       {
/*     */         public InputStream getBody() throws IOException {
/* 105 */           return (InputStream)request.getInputStream();
/*     */         }
/*     */       };
/* 108 */     return this.formConverter.read(null, (HttpInputMessage)servletServerHttpRequest);
/*     */   }
/*     */   
/*     */   private boolean shouldParse(HttpServletRequest request) {
/* 112 */     if (!HTTP_METHODS.contains(request.getMethod())) {
/* 113 */       return false;
/*     */     }
/*     */     try {
/* 116 */       MediaType mediaType = MediaType.parseMediaType(request.getContentType());
/* 117 */       return MediaType.APPLICATION_FORM_URLENCODED.includes(mediaType);
/*     */     }
/* 119 */     catch (IllegalArgumentException ex) {
/* 120 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static class FormContentRequestWrapper
/*     */     extends HttpServletRequestWrapper
/*     */   {
/*     */     private MultiValueMap<String, String> formParams;
/*     */     
/*     */     public FormContentRequestWrapper(HttpServletRequest request, MultiValueMap<String, String> params) {
/* 130 */       super(request);
/* 131 */       this.formParams = params;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getParameter(String name) {
/* 137 */       String queryStringValue = super.getParameter(name);
/* 138 */       String formValue = (String)this.formParams.getFirst(name);
/* 139 */       return (queryStringValue != null) ? queryStringValue : formValue;
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<String, String[]> getParameterMap() {
/* 144 */       Map<String, String[]> result = (Map)new LinkedHashMap<>();
/* 145 */       Enumeration<String> names = getParameterNames();
/* 146 */       while (names.hasMoreElements()) {
/* 147 */         String name = names.nextElement();
/* 148 */         result.put(name, getParameterValues(name));
/*     */       } 
/* 150 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public Enumeration<String> getParameterNames() {
/* 155 */       Set<String> names = new LinkedHashSet<>();
/* 156 */       names.addAll(Collections.list(super.getParameterNames()));
/* 157 */       names.addAll(this.formParams.keySet());
/* 158 */       return Collections.enumeration(names);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String[] getParameterValues(String name) {
/* 164 */       String[] parameterValues = super.getParameterValues(name);
/* 165 */       List<String> formParam = (List<String>)this.formParams.get(name);
/* 166 */       if (formParam == null) {
/* 167 */         return parameterValues;
/*     */       }
/* 169 */       if (parameterValues == null || getQueryString() == null) {
/* 170 */         return StringUtils.toStringArray(formParam);
/*     */       }
/*     */       
/* 173 */       List<String> result = new ArrayList<>(parameterValues.length + formParam.size());
/* 174 */       result.addAll(Arrays.asList(parameterValues));
/* 175 */       result.addAll(formParam);
/* 176 */       return StringUtils.toStringArray(result);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/filter/FormContentFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */