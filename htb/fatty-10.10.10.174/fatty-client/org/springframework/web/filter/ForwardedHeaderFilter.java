/*     */ package org.springframework.web.filter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletRequestWrapper;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpServletResponseWrapper;
/*     */ import org.springframework.http.HttpRequest;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.server.ServletServerHttpRequest;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.LinkedCaseInsensitiveMap;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.util.UriComponents;
/*     */ import org.springframework.web.util.UriComponentsBuilder;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ForwardedHeaderFilter
/*     */   extends OncePerRequestFilter
/*     */ {
/*  69 */   private static final Set<String> FORWARDED_HEADER_NAMES = Collections.newSetFromMap((Map<String, Boolean>)new LinkedCaseInsensitiveMap(6, Locale.ENGLISH));
/*     */   
/*     */   static {
/*  72 */     FORWARDED_HEADER_NAMES.add("Forwarded");
/*  73 */     FORWARDED_HEADER_NAMES.add("X-Forwarded-Host");
/*  74 */     FORWARDED_HEADER_NAMES.add("X-Forwarded-Port");
/*  75 */     FORWARDED_HEADER_NAMES.add("X-Forwarded-Proto");
/*  76 */     FORWARDED_HEADER_NAMES.add("X-Forwarded-Prefix");
/*  77 */     FORWARDED_HEADER_NAMES.add("X-Forwarded-Ssl");
/*     */   }
/*     */ 
/*     */   
/*     */   private final UrlPathHelper pathHelper;
/*     */   
/*     */   private boolean removeOnly;
/*     */   
/*     */   private boolean relativeRedirects;
/*     */ 
/*     */   
/*     */   public ForwardedHeaderFilter() {
/*  89 */     this.pathHelper = new UrlPathHelper();
/*  90 */     this.pathHelper.setUrlDecode(false);
/*  91 */     this.pathHelper.setRemoveSemicolonContent(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemoveOnly(boolean removeOnly) {
/* 102 */     this.removeOnly = removeOnly;
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
/*     */   public void setRelativeRedirects(boolean relativeRedirects) {
/* 117 */     this.relativeRedirects = relativeRedirects;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean shouldNotFilter(HttpServletRequest request) {
/* 123 */     for (String headerName : FORWARDED_HEADER_NAMES) {
/* 124 */       if (request.getHeader(headerName) != null) {
/* 125 */         return false;
/*     */       }
/*     */     } 
/* 128 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldNotFilterAsyncDispatch() {
/* 133 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldNotFilterErrorDispatch() {
/* 138 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
/* 145 */     if (this.removeOnly) {
/* 146 */       ForwardedHeaderRemovingRequest wrappedRequest = new ForwardedHeaderRemovingRequest(request);
/* 147 */       filterChain.doFilter((ServletRequest)wrappedRequest, (ServletResponse)response);
/*     */     } else {
/*     */       
/* 150 */       ForwardedHeaderExtractingRequest forwardedHeaderExtractingRequest = new ForwardedHeaderExtractingRequest(request, this.pathHelper);
/*     */ 
/*     */ 
/*     */       
/* 154 */       HttpServletResponse wrappedResponse = this.relativeRedirects ? RelativeRedirectResponseWrapper.wrapIfNecessary(response, HttpStatus.SEE_OTHER) : (HttpServletResponse)new ForwardedHeaderExtractingResponse(response, (HttpServletRequest)forwardedHeaderExtractingRequest);
/*     */ 
/*     */       
/* 157 */       filterChain.doFilter((ServletRequest)forwardedHeaderExtractingRequest, (ServletResponse)wrappedResponse);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doFilterNestedErrorDispatch(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
/* 165 */     doFilterInternal(request, response, filterChain);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ForwardedHeaderRemovingRequest
/*     */     extends HttpServletRequestWrapper
/*     */   {
/*     */     private final Map<String, List<String>> headers;
/*     */ 
/*     */     
/*     */     public ForwardedHeaderRemovingRequest(HttpServletRequest request) {
/* 176 */       super(request);
/* 177 */       this.headers = initHeaders(request);
/*     */     }
/*     */     
/*     */     private static Map<String, List<String>> initHeaders(HttpServletRequest request) {
/* 181 */       LinkedCaseInsensitiveMap linkedCaseInsensitiveMap = new LinkedCaseInsensitiveMap(Locale.ENGLISH);
/* 182 */       Enumeration<String> names = request.getHeaderNames();
/* 183 */       while (names.hasMoreElements()) {
/* 184 */         String name = names.nextElement();
/* 185 */         if (!ForwardedHeaderFilter.FORWARDED_HEADER_NAMES.contains(name)) {
/* 186 */           linkedCaseInsensitiveMap.put(name, Collections.list(request.getHeaders(name)));
/*     */         }
/*     */       } 
/* 189 */       return (Map<String, List<String>>)linkedCaseInsensitiveMap;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getHeader(String name) {
/* 197 */       List<String> value = this.headers.get(name);
/* 198 */       return CollectionUtils.isEmpty(value) ? null : value.get(0);
/*     */     }
/*     */ 
/*     */     
/*     */     public Enumeration<String> getHeaders(String name) {
/* 203 */       List<String> value = this.headers.get(name);
/* 204 */       return Collections.enumeration((value != null) ? value : Collections.<String>emptySet());
/*     */     }
/*     */ 
/*     */     
/*     */     public Enumeration<String> getHeaderNames() {
/* 209 */       return Collections.enumeration(this.headers.keySet());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ForwardedHeaderExtractingRequest
/*     */     extends ForwardedHeaderRemovingRequest
/*     */   {
/*     */     @Nullable
/*     */     private final String scheme;
/*     */ 
/*     */     
/*     */     private final boolean secure;
/*     */     
/*     */     @Nullable
/*     */     private final String host;
/*     */     
/*     */     private final int port;
/*     */     
/*     */     private final ForwardedHeaderFilter.ForwardedPrefixExtractor forwardedPrefixExtractor;
/*     */ 
/*     */     
/*     */     ForwardedHeaderExtractingRequest(HttpServletRequest request, UrlPathHelper pathHelper) {
/* 233 */       super(request);
/*     */       
/* 235 */       ServletServerHttpRequest servletServerHttpRequest = new ServletServerHttpRequest(request);
/* 236 */       UriComponents uriComponents = UriComponentsBuilder.fromHttpRequest((HttpRequest)servletServerHttpRequest).build();
/* 237 */       int port = uriComponents.getPort();
/*     */       
/* 239 */       this.scheme = uriComponents.getScheme();
/* 240 */       this.secure = "https".equals(this.scheme);
/* 241 */       this.host = uriComponents.getHost();
/* 242 */       this.port = (port == -1) ? (this.secure ? 443 : 80) : port;
/*     */       
/* 244 */       String baseUrl = this.scheme + "://" + this.host + ((port == -1) ? "" : (":" + port));
/* 245 */       Supplier<HttpServletRequest> delegateRequest = () -> (HttpServletRequest)getRequest();
/* 246 */       this.forwardedPrefixExtractor = new ForwardedHeaderFilter.ForwardedPrefixExtractor(delegateRequest, pathHelper, baseUrl);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getScheme() {
/* 253 */       return this.scheme;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getServerName() {
/* 259 */       return this.host;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getServerPort() {
/* 264 */       return this.port;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isSecure() {
/* 269 */       return this.secure;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getContextPath() {
/* 274 */       return this.forwardedPrefixExtractor.getContextPath();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getRequestURI() {
/* 279 */       return this.forwardedPrefixExtractor.getRequestUri();
/*     */     }
/*     */ 
/*     */     
/*     */     public StringBuffer getRequestURL() {
/* 284 */       return this.forwardedPrefixExtractor.getRequestUrl();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ForwardedPrefixExtractor
/*     */   {
/*     */     private final Supplier<HttpServletRequest> delegate;
/*     */ 
/*     */ 
/*     */     
/*     */     private final UrlPathHelper pathHelper;
/*     */ 
/*     */ 
/*     */     
/*     */     private final String baseUrl;
/*     */ 
/*     */ 
/*     */     
/*     */     private String actualRequestUri;
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private final String forwardedPrefix;
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private String requestUri;
/*     */ 
/*     */ 
/*     */     
/*     */     private String requestUrl;
/*     */ 
/*     */ 
/*     */     
/*     */     public ForwardedPrefixExtractor(Supplier<HttpServletRequest> delegateRequest, UrlPathHelper pathHelper, String baseUrl) {
/* 324 */       this.delegate = delegateRequest;
/* 325 */       this.pathHelper = pathHelper;
/* 326 */       this.baseUrl = baseUrl;
/* 327 */       this.actualRequestUri = ((HttpServletRequest)delegateRequest.get()).getRequestURI();
/*     */       
/* 329 */       this.forwardedPrefix = initForwardedPrefix(delegateRequest.get());
/* 330 */       this.requestUri = initRequestUri();
/* 331 */       this.requestUrl = initRequestUrl();
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     private static String initForwardedPrefix(HttpServletRequest request) {
/* 336 */       String result = null;
/* 337 */       Enumeration<String> names = request.getHeaderNames();
/* 338 */       while (names.hasMoreElements()) {
/* 339 */         String name = names.nextElement();
/* 340 */         if ("X-Forwarded-Prefix".equalsIgnoreCase(name)) {
/* 341 */           result = request.getHeader(name);
/*     */         }
/*     */       } 
/* 344 */       if (result != null) {
/* 345 */         while (result.endsWith("/")) {
/* 346 */           result = result.substring(0, result.length() - 1);
/*     */         }
/*     */       }
/* 349 */       return result;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     private String initRequestUri() {
/* 354 */       if (this.forwardedPrefix != null) {
/* 355 */         return this.forwardedPrefix + this.pathHelper.getPathWithinApplication(this.delegate.get());
/*     */       }
/* 357 */       return null;
/*     */     }
/*     */     
/*     */     private String initRequestUrl() {
/* 361 */       return this.baseUrl + ((this.requestUri != null) ? this.requestUri : ((HttpServletRequest)this.delegate.get()).getRequestURI());
/*     */     }
/*     */ 
/*     */     
/*     */     public String getContextPath() {
/* 366 */       return (this.forwardedPrefix == null) ? ((HttpServletRequest)this.delegate.get()).getContextPath() : this.forwardedPrefix;
/*     */     }
/*     */     
/*     */     public String getRequestUri() {
/* 370 */       if (this.requestUri == null) {
/* 371 */         return ((HttpServletRequest)this.delegate.get()).getRequestURI();
/*     */       }
/* 373 */       recalculatePathsIfNecessary();
/* 374 */       return this.requestUri;
/*     */     }
/*     */     
/*     */     public StringBuffer getRequestUrl() {
/* 378 */       recalculatePathsIfNecessary();
/* 379 */       return new StringBuffer(this.requestUrl);
/*     */     }
/*     */     
/*     */     private void recalculatePathsIfNecessary() {
/* 383 */       if (!this.actualRequestUri.equals(((HttpServletRequest)this.delegate.get()).getRequestURI())) {
/*     */         
/* 385 */         this.actualRequestUri = ((HttpServletRequest)this.delegate.get()).getRequestURI();
/* 386 */         this.requestUri = initRequestUri();
/* 387 */         this.requestUrl = initRequestUrl();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ForwardedHeaderExtractingResponse
/*     */     extends HttpServletResponseWrapper
/*     */   {
/*     */     private static final String FOLDER_SEPARATOR = "/";
/*     */     
/*     */     private final HttpServletRequest request;
/*     */     
/*     */     ForwardedHeaderExtractingResponse(HttpServletResponse response, HttpServletRequest request) {
/* 401 */       super(response);
/* 402 */       this.request = request;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void sendRedirect(String location) throws IOException {
/* 409 */       UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(location);
/* 410 */       UriComponents uriComponents = builder.build();
/*     */ 
/*     */       
/* 413 */       if (uriComponents.getScheme() != null) {
/* 414 */         super.sendRedirect(location);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 419 */       if (location.startsWith("//")) {
/* 420 */         String scheme = this.request.getScheme();
/* 421 */         super.sendRedirect(builder.scheme(scheme).toUriString());
/*     */         
/*     */         return;
/*     */       } 
/* 425 */       String path = uriComponents.getPath();
/* 426 */       if (path != null)
/*     */       {
/*     */         
/* 429 */         path = path.startsWith("/") ? path : StringUtils.applyRelativePath(this.request.getRequestURI(), path);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 437 */       String result = UriComponentsBuilder.fromHttpRequest((HttpRequest)new ServletServerHttpRequest(this.request)).replacePath(path).replaceQuery(uriComponents.getQuery()).fragment(uriComponents.getFragment()).build().normalize().toUriString();
/*     */       
/* 439 */       super.sendRedirect(result);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/filter/ForwardedHeaderFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */