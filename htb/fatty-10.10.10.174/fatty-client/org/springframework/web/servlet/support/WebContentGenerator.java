/*     */ package org.springframework.web.servlet.support;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.http.CacheControl;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.HttpRequestMethodNotSupportedException;
/*     */ import org.springframework.web.HttpSessionRequiredException;
/*     */ import org.springframework.web.context.support.WebApplicationObjectSupport;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class WebContentGenerator
/*     */   extends WebApplicationObjectSupport
/*     */ {
/*     */   public static final String METHOD_GET = "GET";
/*     */   public static final String METHOD_HEAD = "HEAD";
/*     */   public static final String METHOD_POST = "POST";
/*     */   private static final String HEADER_PRAGMA = "Pragma";
/*     */   private static final String HEADER_EXPIRES = "Expires";
/*     */   protected static final String HEADER_CACHE_CONTROL = "Cache-Control";
/*     */   @Nullable
/*     */   private Set<String> supportedMethods;
/*     */   @Nullable
/*     */   private String allowHeader;
/*     */   private boolean requireSession = false;
/*     */   @Nullable
/*     */   private CacheControl cacheControl;
/*  96 */   private int cacheSeconds = -1;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String[] varyByRequestHeaders;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean useExpiresHeader = false;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean useCacheControlHeader = true;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean useCacheControlNoStore = true;
/*     */ 
/*     */   
/*     */   private boolean alwaysMustRevalidate = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public WebContentGenerator() {
/* 121 */     this(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebContentGenerator(boolean restrictDefaultSupportedMethods) {
/* 131 */     if (restrictDefaultSupportedMethods) {
/* 132 */       this.supportedMethods = new LinkedHashSet<>(4);
/* 133 */       this.supportedMethods.add("GET");
/* 134 */       this.supportedMethods.add("HEAD");
/* 135 */       this.supportedMethods.add("POST");
/*     */     } 
/* 137 */     initAllowHeader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebContentGenerator(String... supportedMethods) {
/* 145 */     setSupportedMethods(supportedMethods);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setSupportedMethods(@Nullable String... methods) {
/* 155 */     if (!ObjectUtils.isEmpty((Object[])methods)) {
/* 156 */       this.supportedMethods = new LinkedHashSet<>(Arrays.asList(methods));
/*     */     } else {
/*     */       
/* 159 */       this.supportedMethods = null;
/*     */     } 
/* 161 */     initAllowHeader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final String[] getSupportedMethods() {
/* 169 */     return (this.supportedMethods != null) ? StringUtils.toStringArray(this.supportedMethods) : null;
/*     */   }
/*     */   
/*     */   private void initAllowHeader() {
/*     */     Collection<String> allowedMethods;
/* 174 */     if (this.supportedMethods == null) {
/* 175 */       allowedMethods = new ArrayList<>((HttpMethod.values()).length - 1);
/* 176 */       for (HttpMethod method : HttpMethod.values()) {
/* 177 */         if (method != HttpMethod.TRACE) {
/* 178 */           allowedMethods.add(method.name());
/*     */         }
/*     */       }
/*     */     
/* 182 */     } else if (this.supportedMethods.contains(HttpMethod.OPTIONS.name())) {
/* 183 */       allowedMethods = this.supportedMethods;
/*     */     } else {
/*     */       
/* 186 */       allowedMethods = new ArrayList<>(this.supportedMethods);
/* 187 */       allowedMethods.add(HttpMethod.OPTIONS.name());
/*     */     } 
/*     */     
/* 190 */     this.allowHeader = StringUtils.collectionToCommaDelimitedString(allowedMethods);
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
/*     */   @Nullable
/*     */   protected String getAllowHeader() {
/* 204 */     return this.allowHeader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setRequireSession(boolean requireSession) {
/* 211 */     this.requireSession = requireSession;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isRequireSession() {
/* 218 */     return this.requireSession;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setCacheControl(@Nullable CacheControl cacheControl) {
/* 227 */     this.cacheControl = cacheControl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final CacheControl getCacheControl() {
/* 237 */     return this.cacheControl;
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
/*     */   public final void setCacheSeconds(int seconds) {
/* 253 */     this.cacheSeconds = seconds;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getCacheSeconds() {
/* 260 */     return this.cacheSeconds;
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
/*     */   public final void setVaryByRequestHeaders(@Nullable String... varyByRequestHeaders) {
/* 273 */     this.varyByRequestHeaders = varyByRequestHeaders;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final String[] getVaryByRequestHeaders() {
/* 282 */     return this.varyByRequestHeaders;
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
/*     */   @Deprecated
/*     */   public final void setUseExpiresHeader(boolean useExpiresHeader) {
/* 295 */     this.useExpiresHeader = useExpiresHeader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final boolean isUseExpiresHeader() {
/* 304 */     return this.useExpiresHeader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final void setUseCacheControlHeader(boolean useCacheControlHeader) {
/* 316 */     this.useCacheControlHeader = useCacheControlHeader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final boolean isUseCacheControlHeader() {
/* 325 */     return this.useCacheControlHeader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final void setUseCacheControlNoStore(boolean useCacheControlNoStore) {
/* 335 */     this.useCacheControlNoStore = useCacheControlNoStore;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final boolean isUseCacheControlNoStore() {
/* 344 */     return this.useCacheControlNoStore;
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
/*     */   @Deprecated
/*     */   public final void setAlwaysMustRevalidate(boolean mustRevalidate) {
/* 357 */     this.alwaysMustRevalidate = mustRevalidate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final boolean isAlwaysMustRevalidate() {
/* 366 */     return this.alwaysMustRevalidate;
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
/*     */   protected final void checkRequest(HttpServletRequest request) throws ServletException {
/* 378 */     String method = request.getMethod();
/* 379 */     if (this.supportedMethods != null && !this.supportedMethods.contains(method)) {
/* 380 */       throw new HttpRequestMethodNotSupportedException(method, this.supportedMethods);
/*     */     }
/*     */ 
/*     */     
/* 384 */     if (this.requireSession && request.getSession(false) == null) {
/* 385 */       throw new HttpSessionRequiredException("Pre-existing session required but none found");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void prepareResponse(HttpServletResponse response) {
/* 396 */     if (this.cacheControl != null) {
/* 397 */       applyCacheControl(response, this.cacheControl);
/*     */     } else {
/*     */       
/* 400 */       applyCacheSeconds(response, this.cacheSeconds);
/*     */     } 
/* 402 */     if (this.varyByRequestHeaders != null) {
/* 403 */       for (String value : getVaryRequestHeadersToAdd(response, this.varyByRequestHeaders)) {
/* 404 */         response.addHeader("Vary", value);
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
/*     */   protected final void applyCacheControl(HttpServletResponse response, CacheControl cacheControl) {
/* 416 */     String ccValue = cacheControl.getHeaderValue();
/* 417 */     if (ccValue != null) {
/*     */       
/* 419 */       response.setHeader("Cache-Control", ccValue);
/*     */       
/* 421 */       if (response.containsHeader("Pragma"))
/*     */       {
/* 423 */         response.setHeader("Pragma", "");
/*     */       }
/* 425 */       if (response.containsHeader("Expires"))
/*     */       {
/* 427 */         response.setHeader("Expires", "");
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
/*     */ 
/*     */   
/*     */   protected final void applyCacheSeconds(HttpServletResponse response, int cacheSeconds) {
/* 443 */     if (this.useExpiresHeader || !this.useCacheControlHeader) {
/*     */       
/* 445 */       if (cacheSeconds > 0) {
/* 446 */         cacheForSeconds(response, cacheSeconds);
/*     */       }
/* 448 */       else if (cacheSeconds == 0) {
/* 449 */         preventCaching(response);
/*     */       } 
/*     */     } else {
/*     */       CacheControl cControl;
/*     */       
/* 454 */       if (cacheSeconds > 0) {
/* 455 */         cControl = CacheControl.maxAge(cacheSeconds, TimeUnit.SECONDS);
/* 456 */         if (this.alwaysMustRevalidate) {
/* 457 */           cControl = cControl.mustRevalidate();
/*     */         }
/*     */       }
/* 460 */       else if (cacheSeconds == 0) {
/* 461 */         cControl = this.useCacheControlNoStore ? CacheControl.noStore() : CacheControl.noCache();
/*     */       } else {
/*     */         
/* 464 */         cControl = CacheControl.empty();
/*     */       } 
/* 466 */       applyCacheControl(response, cControl);
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
/*     */   @Deprecated
/*     */   protected final void checkAndPrepare(HttpServletRequest request, HttpServletResponse response, boolean lastModified) throws ServletException {
/* 483 */     checkRequest(request);
/* 484 */     prepareResponse(response);
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
/*     */   @Deprecated
/*     */   protected final void checkAndPrepare(HttpServletRequest request, HttpServletResponse response, int cacheSeconds, boolean lastModified) throws ServletException {
/* 500 */     checkRequest(request);
/* 501 */     applyCacheSeconds(response, cacheSeconds);
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
/*     */   @Deprecated
/*     */   protected final void applyCacheSeconds(HttpServletResponse response, int cacheSeconds, boolean mustRevalidate) {
/* 519 */     if (cacheSeconds > 0) {
/* 520 */       cacheForSeconds(response, cacheSeconds, mustRevalidate);
/*     */     }
/* 522 */     else if (cacheSeconds == 0) {
/* 523 */       preventCaching(response);
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
/*     */   @Deprecated
/*     */   protected final void cacheForSeconds(HttpServletResponse response, int seconds) {
/* 537 */     cacheForSeconds(response, seconds, false);
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
/*     */   @Deprecated
/*     */   protected final void cacheForSeconds(HttpServletResponse response, int seconds, boolean mustRevalidate) {
/* 553 */     if (this.useExpiresHeader) {
/*     */       
/* 555 */       response.setDateHeader("Expires", System.currentTimeMillis() + seconds * 1000L);
/*     */     }
/* 557 */     else if (response.containsHeader("Expires")) {
/*     */       
/* 559 */       response.setHeader("Expires", "");
/*     */     } 
/*     */     
/* 562 */     if (this.useCacheControlHeader) {
/*     */       
/* 564 */       String headerValue = "max-age=" + seconds;
/* 565 */       if (mustRevalidate || this.alwaysMustRevalidate) {
/* 566 */         headerValue = headerValue + ", must-revalidate";
/*     */       }
/* 568 */       response.setHeader("Cache-Control", headerValue);
/*     */     } 
/*     */     
/* 571 */     if (response.containsHeader("Pragma"))
/*     */     {
/* 573 */       response.setHeader("Pragma", "");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected final void preventCaching(HttpServletResponse response) {
/* 585 */     response.setHeader("Pragma", "no-cache");
/*     */     
/* 587 */     if (this.useExpiresHeader)
/*     */     {
/* 589 */       response.setDateHeader("Expires", 1L);
/*     */     }
/*     */     
/* 592 */     if (this.useCacheControlHeader) {
/*     */ 
/*     */       
/* 595 */       response.setHeader("Cache-Control", "no-cache");
/* 596 */       if (this.useCacheControlNoStore) {
/* 597 */         response.addHeader("Cache-Control", "no-store");
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private Collection<String> getVaryRequestHeadersToAdd(HttpServletResponse response, String[] varyByRequestHeaders) {
/* 604 */     if (!response.containsHeader("Vary")) {
/* 605 */       return Arrays.asList(varyByRequestHeaders);
/*     */     }
/* 607 */     Collection<String> result = new ArrayList<>(varyByRequestHeaders.length);
/* 608 */     Collections.addAll(result, varyByRequestHeaders);
/* 609 */     for (String header : response.getHeaders("Vary")) {
/* 610 */       for (String existing : StringUtils.tokenizeToStringArray(header, ",")) {
/* 611 */         if ("*".equals(existing)) {
/* 612 */           return Collections.emptyList();
/*     */         }
/* 614 */         for (String value : varyByRequestHeaders) {
/* 615 */           if (value.equalsIgnoreCase(existing)) {
/* 616 */             result.remove(value);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 621 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/support/WebContentGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */