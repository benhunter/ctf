/*     */ package org.springframework.web.filter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintWriter;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletOutputStream;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.DigestUtils;
/*     */ import org.springframework.web.util.ContentCachingResponseWrapper;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ShallowEtagHeaderFilter
/*     */   extends OncePerRequestFilter
/*     */ {
/*     */   private static final String HEADER_ETAG = "ETag";
/*     */   private static final String HEADER_IF_NONE_MATCH = "If-None-Match";
/*     */   private static final String HEADER_CACHE_CONTROL = "Cache-Control";
/*     */   private static final String DIRECTIVE_NO_STORE = "no-store";
/*  64 */   private static final String STREAMING_ATTRIBUTE = ShallowEtagHeaderFilter.class.getName() + ".STREAMING";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean writeWeakETag = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWriteWeakETag(boolean writeWeakETag) {
/*  78 */     this.writeWeakETag = writeWeakETag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWriteWeakETag() {
/*  86 */     return this.writeWeakETag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean shouldNotFilterAsyncDispatch() {
/*  96 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
/*     */     HttpStreamingAwareContentCachingResponseWrapper httpStreamingAwareContentCachingResponseWrapper;
/* 103 */     HttpServletResponse responseToUse = response;
/* 104 */     if (!isAsyncDispatch(request) && !(response instanceof ContentCachingResponseWrapper)) {
/* 105 */       httpStreamingAwareContentCachingResponseWrapper = new HttpStreamingAwareContentCachingResponseWrapper(response, request);
/*     */     }
/*     */     
/* 108 */     filterChain.doFilter((ServletRequest)request, (ServletResponse)httpStreamingAwareContentCachingResponseWrapper);
/*     */     
/* 110 */     if (!isAsyncStarted(request) && !isContentCachingDisabled(request)) {
/* 111 */       updateResponse(request, (HttpServletResponse)httpStreamingAwareContentCachingResponseWrapper);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
/* 117 */     ContentCachingResponseWrapper responseWrapper = (ContentCachingResponseWrapper)WebUtils.getNativeResponse((ServletResponse)response, ContentCachingResponseWrapper.class);
/* 118 */     Assert.notNull(responseWrapper, "ContentCachingResponseWrapper not found");
/* 119 */     HttpServletResponse rawResponse = (HttpServletResponse)responseWrapper.getResponse();
/* 120 */     int statusCode = responseWrapper.getStatusCode();
/*     */     
/* 122 */     if (rawResponse.isCommitted()) {
/* 123 */       responseWrapper.copyBodyToResponse();
/*     */     }
/* 125 */     else if (isEligibleForEtag(request, (HttpServletResponse)responseWrapper, statusCode, responseWrapper.getContentInputStream())) {
/* 126 */       String responseETag = generateETagHeaderValue(responseWrapper.getContentInputStream(), this.writeWeakETag);
/* 127 */       rawResponse.setHeader("ETag", responseETag);
/* 128 */       String requestETag = request.getHeader("If-None-Match");
/* 129 */       if (requestETag != null && ("*".equals(requestETag) || compareETagHeaderValue(requestETag, responseETag))) {
/* 130 */         rawResponse.setStatus(304);
/*     */       } else {
/*     */         
/* 133 */         responseWrapper.copyBodyToResponse();
/*     */       } 
/*     */     } else {
/*     */       
/* 137 */       responseWrapper.copyBodyToResponse();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isEligibleForEtag(HttpServletRequest request, HttpServletResponse response, int responseStatusCode, InputStream inputStream) {
/* 158 */     String method = request.getMethod();
/* 159 */     if (responseStatusCode >= 200 && responseStatusCode < 300 && HttpMethod.GET.matches(method)) {
/* 160 */       String cacheControl = response.getHeader("Cache-Control");
/* 161 */       return (cacheControl == null || !cacheControl.contains("no-store"));
/*     */     } 
/* 163 */     return false;
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
/*     */   protected String generateETagHeaderValue(InputStream inputStream, boolean isWeak) throws IOException {
/* 176 */     StringBuilder builder = new StringBuilder(37);
/* 177 */     if (isWeak) {
/* 178 */       builder.append("W/");
/*     */     }
/* 180 */     builder.append("\"0");
/* 181 */     DigestUtils.appendMd5DigestAsHex(inputStream, builder);
/* 182 */     builder.append('"');
/* 183 */     return builder.toString();
/*     */   }
/*     */   
/*     */   private boolean compareETagHeaderValue(String requestETag, String responseETag) {
/* 187 */     if (requestETag.startsWith("W/")) {
/* 188 */       requestETag = requestETag.substring(2);
/*     */     }
/* 190 */     if (responseETag.startsWith("W/")) {
/* 191 */       responseETag = responseETag.substring(2);
/*     */     }
/* 193 */     return requestETag.equals(responseETag);
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
/*     */   public static void disableContentCaching(ServletRequest request) {
/* 205 */     Assert.notNull(request, "ServletRequest must not be null");
/* 206 */     request.setAttribute(STREAMING_ATTRIBUTE, Boolean.valueOf(true));
/*     */   }
/*     */   
/*     */   private static boolean isContentCachingDisabled(HttpServletRequest request) {
/* 210 */     return (request.getAttribute(STREAMING_ATTRIBUTE) != null);
/*     */   }
/*     */   
/*     */   private static class HttpStreamingAwareContentCachingResponseWrapper
/*     */     extends ContentCachingResponseWrapper
/*     */   {
/*     */     private final HttpServletRequest request;
/*     */     
/*     */     public HttpStreamingAwareContentCachingResponseWrapper(HttpServletResponse response, HttpServletRequest request) {
/* 219 */       super(response);
/* 220 */       this.request = request;
/*     */     }
/*     */ 
/*     */     
/*     */     public ServletOutputStream getOutputStream() throws IOException {
/* 225 */       return useRawResponse() ? getResponse().getOutputStream() : super.getOutputStream();
/*     */     }
/*     */ 
/*     */     
/*     */     public PrintWriter getWriter() throws IOException {
/* 230 */       return useRawResponse() ? getResponse().getWriter() : super.getWriter();
/*     */     }
/*     */     
/*     */     private boolean useRawResponse() {
/* 234 */       return ShallowEtagHeaderFilter.isContentCachingDisabled(this.request);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/filter/ShallowEtagHeaderFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */