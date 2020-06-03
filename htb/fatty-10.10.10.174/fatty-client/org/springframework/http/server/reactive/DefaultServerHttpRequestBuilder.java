/*     */ package org.springframework.http.server.reactive;
/*     */ 
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.http.HttpCookie;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.StringUtils;
/*     */ import reactor.core.publisher.Flux;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class DefaultServerHttpRequestBuilder
/*     */   implements ServerHttpRequest.Builder
/*     */ {
/*     */   private URI uri;
/*     */   private HttpHeaders httpHeaders;
/*     */   private String httpMethodValue;
/*     */   private final MultiValueMap<String, HttpCookie> cookies;
/*     */   @Nullable
/*     */   private String uriPath;
/*     */   @Nullable
/*     */   private String contextPath;
/*     */   @Nullable
/*     */   private SslInfo sslInfo;
/*     */   private Flux<DataBuffer> body;
/*     */   private final ServerHttpRequest originalRequest;
/*     */   
/*     */   public DefaultServerHttpRequestBuilder(ServerHttpRequest original) {
/*  69 */     Assert.notNull(original, "ServerHttpRequest is required");
/*     */     
/*  71 */     this.uri = original.getURI();
/*  72 */     this.httpMethodValue = original.getMethodValue();
/*  73 */     this.body = original.getBody();
/*     */     
/*  75 */     this.httpHeaders = HttpHeaders.writableHttpHeaders(original.getHeaders());
/*     */     
/*  77 */     this.cookies = (MultiValueMap<String, HttpCookie>)new LinkedMultiValueMap(original.getCookies().size());
/*  78 */     copyMultiValueMap(original.getCookies(), this.cookies);
/*     */     
/*  80 */     this.originalRequest = original;
/*     */   }
/*     */   
/*     */   private static <K, V> void copyMultiValueMap(MultiValueMap<K, V> source, MultiValueMap<K, V> target) {
/*  84 */     source.forEach((key, value) -> (List)target.put(key, new LinkedList(value)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ServerHttpRequest.Builder method(HttpMethod httpMethod) {
/*  90 */     this.httpMethodValue = httpMethod.name();
/*  91 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerHttpRequest.Builder uri(URI uri) {
/*  96 */     this.uri = uri;
/*  97 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerHttpRequest.Builder path(String path) {
/* 102 */     Assert.isTrue(path.startsWith("/"), "The path does not have a leading slash.");
/* 103 */     this.uriPath = path;
/* 104 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerHttpRequest.Builder contextPath(String contextPath) {
/* 109 */     this.contextPath = contextPath;
/* 110 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ServerHttpRequest.Builder header(String key, String value) {
/* 116 */     this.httpHeaders.add(key, value);
/* 117 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerHttpRequest.Builder headers(Consumer<HttpHeaders> headersConsumer) {
/* 122 */     Assert.notNull(headersConsumer, "'headersConsumer' must not be null");
/* 123 */     headersConsumer.accept(this.httpHeaders);
/* 124 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerHttpRequest.Builder sslInfo(SslInfo sslInfo) {
/* 129 */     this.sslInfo = sslInfo;
/* 130 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerHttpRequest build() {
/* 135 */     return new MutatedServerHttpRequest(getUriToUse(), this.contextPath, this.httpHeaders, this.httpMethodValue, this.cookies, this.sslInfo, this.body, this.originalRequest);
/*     */   }
/*     */ 
/*     */   
/*     */   private URI getUriToUse() {
/* 140 */     if (this.uriPath == null) {
/* 141 */       return this.uri;
/*     */     }
/*     */     
/* 144 */     StringBuilder uriBuilder = new StringBuilder();
/* 145 */     if (this.uri.getScheme() != null) {
/* 146 */       uriBuilder.append(this.uri.getScheme()).append(':');
/*     */     }
/* 148 */     if (this.uri.getRawUserInfo() != null || this.uri.getHost() != null) {
/* 149 */       uriBuilder.append("//");
/* 150 */       if (this.uri.getRawUserInfo() != null) {
/* 151 */         uriBuilder.append(this.uri.getRawUserInfo()).append('@');
/*     */       }
/* 153 */       if (this.uri.getHost() != null) {
/* 154 */         uriBuilder.append(this.uri.getHost());
/*     */       }
/* 156 */       if (this.uri.getPort() != -1) {
/* 157 */         uriBuilder.append(':').append(this.uri.getPort());
/*     */       }
/*     */     } 
/* 160 */     if (StringUtils.hasLength(this.uriPath)) {
/* 161 */       uriBuilder.append(this.uriPath);
/*     */     }
/* 163 */     if (this.uri.getRawQuery() != null) {
/* 164 */       uriBuilder.append('?').append(this.uri.getRawQuery());
/*     */     }
/* 166 */     if (this.uri.getRawFragment() != null) {
/* 167 */       uriBuilder.append('#').append(this.uri.getRawFragment());
/*     */     }
/*     */     try {
/* 170 */       return new URI(uriBuilder.toString());
/*     */     }
/* 172 */     catch (URISyntaxException ex) {
/* 173 */       throw new IllegalStateException("Invalid URI path: \"" + this.uriPath + "\"", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class MutatedServerHttpRequest
/*     */     extends AbstractServerHttpRequest
/*     */   {
/*     */     private final String methodValue;
/*     */ 
/*     */     
/*     */     private final MultiValueMap<String, HttpCookie> cookies;
/*     */     
/*     */     @Nullable
/*     */     private final InetSocketAddress remoteAddress;
/*     */     
/*     */     @Nullable
/*     */     private final SslInfo sslInfo;
/*     */     
/*     */     private final Flux<DataBuffer> body;
/*     */     
/*     */     private final ServerHttpRequest originalRequest;
/*     */ 
/*     */     
/*     */     public MutatedServerHttpRequest(URI uri, @Nullable String contextPath, HttpHeaders headers, String methodValue, MultiValueMap<String, HttpCookie> cookies, @Nullable SslInfo sslInfo, Flux<DataBuffer> body, ServerHttpRequest originalRequest) {
/* 199 */       super(uri, contextPath, headers);
/* 200 */       this.methodValue = methodValue;
/* 201 */       this.cookies = cookies;
/* 202 */       this.remoteAddress = originalRequest.getRemoteAddress();
/* 203 */       this.sslInfo = (sslInfo != null) ? sslInfo : originalRequest.getSslInfo();
/* 204 */       this.body = body;
/* 205 */       this.originalRequest = originalRequest;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getMethodValue() {
/* 210 */       return this.methodValue;
/*     */     }
/*     */ 
/*     */     
/*     */     protected MultiValueMap<String, HttpCookie> initCookies() {
/* 215 */       return this.cookies;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public InetSocketAddress getRemoteAddress() {
/* 221 */       return this.remoteAddress;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     protected SslInfo initSslInfo() {
/* 227 */       return this.sslInfo;
/*     */     }
/*     */ 
/*     */     
/*     */     public Flux<DataBuffer> getBody() {
/* 232 */       return this.body;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> T getNativeRequest() {
/* 238 */       return (T)this.originalRequest;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getId() {
/* 243 */       return this.originalRequest.getId();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/reactive/DefaultServerHttpRequestBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */