/*     */ package org.springframework.web.filter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.springframework.http.server.ServletServerHttpRequest;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.util.ContentCachingRequestWrapper;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractRequestLoggingFilter
/*     */   extends OncePerRequestFilter
/*     */ {
/*     */   public static final String DEFAULT_BEFORE_MESSAGE_PREFIX = "Before request [";
/*     */   public static final String DEFAULT_BEFORE_MESSAGE_SUFFIX = "]";
/*     */   public static final String DEFAULT_AFTER_MESSAGE_PREFIX = "After request [";
/*     */   public static final String DEFAULT_AFTER_MESSAGE_SUFFIX = "]";
/*     */   private static final int DEFAULT_MAX_PAYLOAD_LENGTH = 50;
/*     */   private boolean includeQueryString = false;
/*     */   private boolean includeClientInfo = false;
/*     */   private boolean includeHeaders = false;
/*     */   private boolean includePayload = false;
/*  98 */   private int maxPayloadLength = 50;
/*     */   
/* 100 */   private String beforeMessagePrefix = "Before request [";
/*     */   
/* 102 */   private String beforeMessageSuffix = "]";
/*     */   
/* 104 */   private String afterMessagePrefix = "After request [";
/*     */   
/* 106 */   private String afterMessageSuffix = "]";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIncludeQueryString(boolean includeQueryString) {
/* 115 */     this.includeQueryString = includeQueryString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isIncludeQueryString() {
/* 122 */     return this.includeQueryString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIncludeClientInfo(boolean includeClientInfo) {
/* 132 */     this.includeClientInfo = includeClientInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isIncludeClientInfo() {
/* 140 */     return this.includeClientInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIncludeHeaders(boolean includeHeaders) {
/* 150 */     this.includeHeaders = includeHeaders;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isIncludeHeaders() {
/* 158 */     return this.includeHeaders;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIncludePayload(boolean includePayload) {
/* 168 */     this.includePayload = includePayload;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isIncludePayload() {
/* 176 */     return this.includePayload;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxPayloadLength(int maxPayloadLength) {
/* 185 */     Assert.isTrue((maxPayloadLength >= 0), "'maxPayloadLength' should be larger than or equal to 0");
/* 186 */     this.maxPayloadLength = maxPayloadLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getMaxPayloadLength() {
/* 194 */     return this.maxPayloadLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeforeMessagePrefix(String beforeMessagePrefix) {
/* 202 */     this.beforeMessagePrefix = beforeMessagePrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeforeMessageSuffix(String beforeMessageSuffix) {
/* 210 */     this.beforeMessageSuffix = beforeMessageSuffix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAfterMessagePrefix(String afterMessagePrefix) {
/* 218 */     this.afterMessagePrefix = afterMessagePrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAfterMessageSuffix(String afterMessageSuffix) {
/* 226 */     this.afterMessageSuffix = afterMessageSuffix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean shouldNotFilterAsyncDispatch() {
/* 237 */     return false;
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
/*     */   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
/*     */     ContentCachingRequestWrapper contentCachingRequestWrapper;
/* 250 */     boolean isFirstRequest = !isAsyncDispatch(request);
/* 251 */     HttpServletRequest requestToUse = request;
/*     */     
/* 253 */     if (isIncludePayload() && isFirstRequest && !(request instanceof ContentCachingRequestWrapper)) {
/* 254 */       contentCachingRequestWrapper = new ContentCachingRequestWrapper(request, getMaxPayloadLength());
/*     */     }
/*     */     
/* 257 */     boolean shouldLog = shouldLog((HttpServletRequest)contentCachingRequestWrapper);
/* 258 */     if (shouldLog && isFirstRequest) {
/* 259 */       beforeRequest((HttpServletRequest)contentCachingRequestWrapper, getBeforeMessage((HttpServletRequest)contentCachingRequestWrapper));
/*     */     }
/*     */     try {
/* 262 */       filterChain.doFilter((ServletRequest)contentCachingRequestWrapper, (ServletResponse)response);
/*     */     } finally {
/*     */       
/* 265 */       if (shouldLog && !isAsyncStarted((HttpServletRequest)contentCachingRequestWrapper)) {
/* 266 */         afterRequest((HttpServletRequest)contentCachingRequestWrapper, getAfterMessage((HttpServletRequest)contentCachingRequestWrapper));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getBeforeMessage(HttpServletRequest request) {
/* 276 */     return createMessage(request, this.beforeMessagePrefix, this.beforeMessageSuffix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getAfterMessage(HttpServletRequest request) {
/* 284 */     return createMessage(request, this.afterMessagePrefix, this.afterMessageSuffix);
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
/*     */   protected String createMessage(HttpServletRequest request, String prefix, String suffix) {
/* 296 */     StringBuilder msg = new StringBuilder();
/* 297 */     msg.append(prefix);
/* 298 */     msg.append("uri=").append(request.getRequestURI());
/*     */     
/* 300 */     if (isIncludeQueryString()) {
/* 301 */       String queryString = request.getQueryString();
/* 302 */       if (queryString != null) {
/* 303 */         msg.append('?').append(queryString);
/*     */       }
/*     */     } 
/*     */     
/* 307 */     if (isIncludeClientInfo()) {
/* 308 */       String client = request.getRemoteAddr();
/* 309 */       if (StringUtils.hasLength(client)) {
/* 310 */         msg.append(";client=").append(client);
/*     */       }
/* 312 */       HttpSession session = request.getSession(false);
/* 313 */       if (session != null) {
/* 314 */         msg.append(";session=").append(session.getId());
/*     */       }
/* 316 */       String user = request.getRemoteUser();
/* 317 */       if (user != null) {
/* 318 */         msg.append(";user=").append(user);
/*     */       }
/*     */     } 
/*     */     
/* 322 */     if (isIncludeHeaders()) {
/* 323 */       msg.append(";headers=").append((new ServletServerHttpRequest(request)).getHeaders());
/*     */     }
/*     */     
/* 326 */     if (isIncludePayload()) {
/* 327 */       String payload = getMessagePayload(request);
/* 328 */       if (payload != null) {
/* 329 */         msg.append(";payload=").append(payload);
/*     */       }
/*     */     } 
/*     */     
/* 333 */     msg.append(suffix);
/* 334 */     return msg.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getMessagePayload(HttpServletRequest request) {
/* 346 */     ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper)WebUtils.getNativeRequest((ServletRequest)request, ContentCachingRequestWrapper.class);
/* 347 */     if (wrapper != null) {
/* 348 */       byte[] buf = wrapper.getContentAsByteArray();
/* 349 */       if (buf.length > 0) {
/* 350 */         int length = Math.min(buf.length, getMaxPayloadLength());
/*     */         try {
/* 352 */           return new String(buf, 0, length, wrapper.getCharacterEncoding());
/*     */         }
/* 354 */         catch (UnsupportedEncodingException ex) {
/* 355 */           return "[unknown]";
/*     */         } 
/*     */       } 
/*     */     } 
/* 359 */     return null;
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
/*     */   protected boolean shouldLog(HttpServletRequest request) {
/* 375 */     return true;
/*     */   }
/*     */   
/*     */   protected abstract void beforeRequest(HttpServletRequest paramHttpServletRequest, String paramString);
/*     */   
/*     */   protected abstract void afterRequest(HttpServletRequest paramHttpServletRequest, String paramString);
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/filter/AbstractRequestLoggingFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */