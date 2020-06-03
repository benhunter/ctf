/*     */ package org.springframework.web.servlet.resource;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletRequestWrapper;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpServletResponseWrapper;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.web.filter.GenericFilterBean;
/*     */ import org.springframework.web.util.UrlPathHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourceUrlEncodingFilter
/*     */   extends GenericFilterBean
/*     */ {
/*  49 */   private static final Log logger = LogFactory.getLog(ResourceUrlEncodingFilter.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
/*  56 */     if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
/*  57 */       throw new ServletException("ResourceUrlEncodingFilter only supports HTTP requests");
/*     */     }
/*  59 */     ResourceUrlEncodingRequestWrapper wrappedRequest = new ResourceUrlEncodingRequestWrapper((HttpServletRequest)request);
/*     */     
/*  61 */     ResourceUrlEncodingResponseWrapper wrappedResponse = new ResourceUrlEncodingResponseWrapper(wrappedRequest, (HttpServletResponse)response);
/*     */     
/*  63 */     filterChain.doFilter((ServletRequest)wrappedRequest, (ServletResponse)wrappedResponse);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ResourceUrlEncodingRequestWrapper
/*     */     extends HttpServletRequestWrapper
/*     */   {
/*     */     @Nullable
/*     */     private ResourceUrlProvider resourceUrlProvider;
/*     */     
/*     */     @Nullable
/*     */     private Integer indexLookupPath;
/*  75 */     private String prefixLookupPath = "";
/*     */     
/*     */     ResourceUrlEncodingRequestWrapper(HttpServletRequest request) {
/*  78 */       super(request);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setAttribute(String name, Object value) {
/*  83 */       super.setAttribute(name, value);
/*  84 */       if (ResourceUrlProviderExposingInterceptor.RESOURCE_URL_PROVIDER_ATTR.equals(name) && 
/*  85 */         value instanceof ResourceUrlProvider) {
/*  86 */         initLookupPath((ResourceUrlProvider)value);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     private void initLookupPath(ResourceUrlProvider urlProvider) {
/*  92 */       this.resourceUrlProvider = urlProvider;
/*  93 */       if (this.indexLookupPath == null) {
/*  94 */         UrlPathHelper pathHelper = this.resourceUrlProvider.getUrlPathHelper();
/*  95 */         String requestUri = pathHelper.getRequestUri((HttpServletRequest)this);
/*  96 */         String lookupPath = pathHelper.getLookupPathForRequest((HttpServletRequest)this);
/*  97 */         this.indexLookupPath = Integer.valueOf(requestUri.lastIndexOf(lookupPath));
/*  98 */         if (this.indexLookupPath.intValue() == -1) {
/*  99 */           throw new IllegalStateException("Failed to find lookupPath '" + lookupPath + "' within requestUri '" + requestUri + "'. Does the path have invalid encoded characters for characterEncoding '" + 
/*     */ 
/*     */               
/* 102 */               getRequest().getCharacterEncoding() + "'?");
/*     */         }
/* 104 */         this.prefixLookupPath = requestUri.substring(0, this.indexLookupPath.intValue());
/* 105 */         if ("/".equals(lookupPath) && !"/".equals(requestUri)) {
/* 106 */           String contextPath = pathHelper.getContextPath((HttpServletRequest)this);
/* 107 */           if (requestUri.equals(contextPath)) {
/* 108 */             this.indexLookupPath = Integer.valueOf(requestUri.length());
/* 109 */             this.prefixLookupPath = requestUri;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public String resolveUrlPath(String url) {
/* 117 */       if (this.resourceUrlProvider == null) {
/* 118 */         ResourceUrlEncodingFilter.logger.trace("ResourceUrlProvider not available via request attribute " + ResourceUrlProviderExposingInterceptor.RESOURCE_URL_PROVIDER_ATTR);
/*     */         
/* 120 */         return null;
/*     */       } 
/* 122 */       if (this.indexLookupPath != null && url.startsWith(this.prefixLookupPath)) {
/* 123 */         int suffixIndex = getEndPathIndex(url);
/* 124 */         String suffix = url.substring(suffixIndex);
/* 125 */         String lookupPath = url.substring(this.indexLookupPath.intValue(), suffixIndex);
/* 126 */         lookupPath = this.resourceUrlProvider.getForLookupPath(lookupPath);
/* 127 */         if (lookupPath != null) {
/* 128 */           return this.prefixLookupPath + lookupPath + suffix;
/*     */         }
/*     */       } 
/* 131 */       return null;
/*     */     }
/*     */     
/*     */     private int getEndPathIndex(String path) {
/* 135 */       int end = path.indexOf('?');
/* 136 */       int fragmentIndex = path.indexOf('#');
/* 137 */       if (fragmentIndex != -1 && (end == -1 || fragmentIndex < end)) {
/* 138 */         end = fragmentIndex;
/*     */       }
/* 140 */       if (end == -1) {
/* 141 */         end = path.length();
/*     */       }
/* 143 */       return end;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ResourceUrlEncodingResponseWrapper
/*     */     extends HttpServletResponseWrapper
/*     */   {
/*     */     private final ResourceUrlEncodingFilter.ResourceUrlEncodingRequestWrapper request;
/*     */     
/*     */     ResourceUrlEncodingResponseWrapper(ResourceUrlEncodingFilter.ResourceUrlEncodingRequestWrapper request, HttpServletResponse wrapped) {
/* 153 */       super(wrapped);
/* 154 */       this.request = request;
/*     */     }
/*     */ 
/*     */     
/*     */     public String encodeURL(String url) {
/* 159 */       String urlPath = this.request.resolveUrlPath(url);
/* 160 */       if (urlPath != null) {
/* 161 */         return super.encodeURL(urlPath);
/*     */       }
/* 163 */       return super.encodeURL(url);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/resource/ResourceUrlEncodingFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */