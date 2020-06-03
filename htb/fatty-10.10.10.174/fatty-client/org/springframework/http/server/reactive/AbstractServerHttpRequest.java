/*     */ package org.springframework.http.server.reactive;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URI;
/*     */ import java.net.URLDecoder;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.http.HttpCookie;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpLogging;
/*     */ import org.springframework.http.server.RequestPath;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ public abstract class AbstractServerHttpRequest
/*     */   implements ServerHttpRequest
/*     */ {
/*  46 */   private static final Pattern QUERY_PATTERN = Pattern.compile("([^&=]+)(=?)([^&]+)?");
/*     */ 
/*     */   
/*  49 */   protected final Log logger = HttpLogging.forLogName(getClass());
/*     */ 
/*     */   
/*     */   private final URI uri;
/*     */ 
/*     */   
/*     */   private final RequestPath path;
/*     */ 
/*     */   
/*     */   private final HttpHeaders headers;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private MultiValueMap<String, String> queryParams;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private MultiValueMap<String, HttpCookie> cookies;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private SslInfo sslInfo;
/*     */   
/*     */   @Nullable
/*     */   private String id;
/*     */   
/*     */   @Nullable
/*     */   private String logPrefix;
/*     */ 
/*     */   
/*     */   public AbstractServerHttpRequest(URI uri, @Nullable String contextPath, HttpHeaders headers) {
/*  80 */     this.uri = uri;
/*  81 */     this.path = RequestPath.parse(uri, contextPath);
/*  82 */     this.headers = HttpHeaders.readOnlyHttpHeaders(headers);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getId() {
/*  87 */     if (this.id == null) {
/*  88 */       this.id = initId();
/*  89 */       if (this.id == null) {
/*  90 */         this.id = ObjectUtils.getIdentityHexString(this);
/*     */       }
/*     */     } 
/*  93 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String initId() {
/* 103 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public URI getURI() {
/* 108 */     return this.uri;
/*     */   }
/*     */ 
/*     */   
/*     */   public RequestPath getPath() {
/* 113 */     return this.path;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHeaders getHeaders() {
/* 118 */     return this.headers;
/*     */   }
/*     */ 
/*     */   
/*     */   public MultiValueMap<String, String> getQueryParams() {
/* 123 */     if (this.queryParams == null) {
/* 124 */       this.queryParams = CollectionUtils.unmodifiableMultiValueMap(initQueryParams());
/*     */     }
/* 126 */     return this.queryParams;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MultiValueMap<String, String> initQueryParams() {
/* 137 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap();
/* 138 */     String query = getURI().getRawQuery();
/* 139 */     if (query != null) {
/* 140 */       Matcher matcher = QUERY_PATTERN.matcher(query);
/* 141 */       while (matcher.find()) {
/* 142 */         String name = decodeQueryParam(matcher.group(1));
/* 143 */         String eq = matcher.group(2);
/* 144 */         String value = matcher.group(3);
/* 145 */         value = (value != null) ? decodeQueryParam(value) : (StringUtils.hasLength(eq) ? "" : null);
/* 146 */         linkedMultiValueMap.add(name, value);
/*     */       } 
/*     */     } 
/* 149 */     return (MultiValueMap<String, String>)linkedMultiValueMap;
/*     */   }
/*     */ 
/*     */   
/*     */   private String decodeQueryParam(String value) {
/*     */     try {
/* 155 */       return URLDecoder.decode(value, "UTF-8");
/*     */     }
/* 157 */     catch (UnsupportedEncodingException ex) {
/* 158 */       if (this.logger.isWarnEnabled()) {
/* 159 */         this.logger.warn(getLogPrefix() + "Could not decode query value [" + value + "] as 'UTF-8'. Falling back on default encoding: " + ex
/* 160 */             .getMessage());
/*     */       }
/* 162 */       return URLDecoder.decode(value);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public MultiValueMap<String, HttpCookie> getCookies() {
/* 168 */     if (this.cookies == null) {
/* 169 */       this.cookies = CollectionUtils.unmodifiableMultiValueMap(initCookies());
/*     */     }
/* 171 */     return this.cookies;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract MultiValueMap<String, HttpCookie> initCookies();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public SslInfo getSslInfo() {
/* 188 */     if (this.sslInfo == null) {
/* 189 */       this.sslInfo = initSslInfo();
/*     */     }
/* 191 */     return this.sslInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected abstract SslInfo initSslInfo();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract <T> T getNativeRequest();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String getLogPrefix() {
/* 214 */     if (this.logPrefix == null) {
/* 215 */       this.logPrefix = "[" + getId() + "] ";
/*     */     }
/* 217 */     return this.logPrefix;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/reactive/AbstractServerHttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */