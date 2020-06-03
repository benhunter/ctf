/*     */ package org.springframework.http.server.reactive;
/*     */ 
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.URI;
/*     */ import java.util.Arrays;
/*     */ import java.util.function.Consumer;
/*     */ import org.springframework.http.HttpCookie;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.HttpRequest;
/*     */ import org.springframework.http.ReactiveHttpInputMessage;
/*     */ import org.springframework.http.server.RequestPath;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface ServerHttpRequest
/*     */   extends HttpRequest, ReactiveHttpInputMessage
/*     */ {
/*     */   String getId();
/*     */   
/*     */   RequestPath getPath();
/*     */   
/*     */   MultiValueMap<String, String> getQueryParams();
/*     */   
/*     */   MultiValueMap<String, HttpCookie> getCookies();
/*     */   
/*     */   @Nullable
/*     */   default InetSocketAddress getRemoteAddress() {
/*  73 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   default SslInfo getSslInfo() {
/*  84 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default Builder mutate() {
/*  93 */     return new DefaultServerHttpRequestBuilder(this);
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
/*     */   public static interface Builder
/*     */   {
/*     */     Builder method(HttpMethod param1HttpMethod);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Builder uri(URI param1URI);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Builder path(String param1String);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Builder contextPath(String param1String);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     Builder header(String param1String1, String param1String2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Builder header(String headerName, String... headerValues) {
/* 163 */       return headers(httpHeaders -> httpHeaders.put(headerName, Arrays.asList(headerValues)));
/*     */     }
/*     */     
/*     */     Builder headers(Consumer<HttpHeaders> param1Consumer);
/*     */     
/*     */     Builder sslInfo(SslInfo param1SslInfo);
/*     */     
/*     */     ServerHttpRequest build();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/reactive/ServerHttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */