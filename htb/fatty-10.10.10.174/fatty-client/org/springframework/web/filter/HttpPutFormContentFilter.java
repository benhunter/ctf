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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class HttpPutFormContentFilter
/*     */   extends OncePerRequestFilter
/*     */ {
/*  67 */   private FormHttpMessageConverter formConverter = (FormHttpMessageConverter)new AllEncompassingFormHttpMessageConverter();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFormConverter(FormHttpMessageConverter converter) {
/*  75 */     Assert.notNull(converter, "FormHttpMessageConverter is required.");
/*  76 */     this.formConverter = converter;
/*     */   }
/*     */   
/*     */   public FormHttpMessageConverter getFormConverter() {
/*  80 */     return this.formConverter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCharset(Charset charset) {
/*  89 */     this.formConverter.setCharset(charset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doFilterInternal(final HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
/*  97 */     if (("PUT".equals(request.getMethod()) || "PATCH".equals(request.getMethod())) && isFormContentType(request)) {
/*  98 */       ServletServerHttpRequest servletServerHttpRequest = new ServletServerHttpRequest(request)
/*     */         {
/*     */           public InputStream getBody() throws IOException {
/* 101 */             return (InputStream)request.getInputStream();
/*     */           }
/*     */         };
/* 104 */       MultiValueMap<String, String> formParameters = this.formConverter.read(null, (HttpInputMessage)servletServerHttpRequest);
/* 105 */       if (!formParameters.isEmpty()) {
/* 106 */         HttpPutFormContentRequestWrapper httpPutFormContentRequestWrapper = new HttpPutFormContentRequestWrapper(request, formParameters);
/* 107 */         filterChain.doFilter((ServletRequest)httpPutFormContentRequestWrapper, (ServletResponse)response);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 112 */     filterChain.doFilter((ServletRequest)request, (ServletResponse)response);
/*     */   }
/*     */   
/*     */   private boolean isFormContentType(HttpServletRequest request) {
/* 116 */     String contentType = request.getContentType();
/* 117 */     if (contentType != null) {
/*     */       try {
/* 119 */         MediaType mediaType = MediaType.parseMediaType(contentType);
/* 120 */         return MediaType.APPLICATION_FORM_URLENCODED.includes(mediaType);
/*     */       }
/* 122 */       catch (IllegalArgumentException ex) {
/* 123 */         return false;
/*     */       } 
/*     */     }
/*     */     
/* 127 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class HttpPutFormContentRequestWrapper
/*     */     extends HttpServletRequestWrapper
/*     */   {
/*     */     private MultiValueMap<String, String> formParameters;
/*     */     
/*     */     public HttpPutFormContentRequestWrapper(HttpServletRequest request, MultiValueMap<String, String> parameters) {
/* 137 */       super(request);
/* 138 */       this.formParameters = parameters;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getParameter(String name) {
/* 144 */       String queryStringValue = super.getParameter(name);
/* 145 */       String formValue = (String)this.formParameters.getFirst(name);
/* 146 */       return (queryStringValue != null) ? queryStringValue : formValue;
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<String, String[]> getParameterMap() {
/* 151 */       Map<String, String[]> result = (Map)new LinkedHashMap<>();
/* 152 */       Enumeration<String> names = getParameterNames();
/* 153 */       while (names.hasMoreElements()) {
/* 154 */         String name = names.nextElement();
/* 155 */         result.put(name, getParameterValues(name));
/*     */       } 
/* 157 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public Enumeration<String> getParameterNames() {
/* 162 */       Set<String> names = new LinkedHashSet<>();
/* 163 */       names.addAll(Collections.list(super.getParameterNames()));
/* 164 */       names.addAll(this.formParameters.keySet());
/* 165 */       return Collections.enumeration(names);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String[] getParameterValues(String name) {
/* 171 */       String[] parameterValues = super.getParameterValues(name);
/* 172 */       List<String> formParam = (List<String>)this.formParameters.get(name);
/* 173 */       if (formParam == null) {
/* 174 */         return parameterValues;
/*     */       }
/* 176 */       if (parameterValues == null || getQueryString() == null) {
/* 177 */         return StringUtils.toStringArray(formParam);
/*     */       }
/*     */       
/* 180 */       List<String> result = new ArrayList<>(parameterValues.length + formParam.size());
/* 181 */       result.addAll(Arrays.asList(parameterValues));
/* 182 */       result.addAll(formParam);
/* 183 */       return StringUtils.toStringArray(result);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/filter/HttpPutFormContentFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */