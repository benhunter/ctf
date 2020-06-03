/*     */ package org.springframework.http.server.reactive;
/*     */ 
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.cookie.Cookie;
/*     */ import io.netty.handler.ssl.SslHandler;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.NettyDataBufferFactory;
/*     */ import org.springframework.http.HttpCookie;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import reactor.core.publisher.Flux;
/*     */ import reactor.netty.Connection;
/*     */ import reactor.netty.http.server.HttpServerRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ReactorServerHttpRequest
/*     */   extends AbstractServerHttpRequest
/*     */ {
/*     */   private final HttpServerRequest request;
/*     */   private final NettyDataBufferFactory bufferFactory;
/*     */   
/*     */   public ReactorServerHttpRequest(HttpServerRequest request, NettyDataBufferFactory bufferFactory) throws URISyntaxException {
/*  57 */     super(initUri(request), "", initHeaders(request));
/*  58 */     Assert.notNull(bufferFactory, "DataBufferFactory must not be null");
/*  59 */     this.request = request;
/*  60 */     this.bufferFactory = bufferFactory;
/*     */   }
/*     */   
/*     */   private static URI initUri(HttpServerRequest request) throws URISyntaxException {
/*  64 */     Assert.notNull(request, "HttpServerRequest must not be null");
/*  65 */     return new URI(resolveBaseUrl(request).toString() + resolveRequestUri(request));
/*     */   }
/*     */   
/*     */   private static URI resolveBaseUrl(HttpServerRequest request) throws URISyntaxException {
/*  69 */     String scheme = getScheme(request);
/*  70 */     String header = request.requestHeaders().get((CharSequence)HttpHeaderNames.HOST);
/*  71 */     if (header != null) {
/*     */       int portIndex;
/*  73 */       if (header.startsWith("[")) {
/*  74 */         portIndex = header.indexOf(':', header.indexOf(']'));
/*     */       } else {
/*     */         
/*  77 */         portIndex = header.indexOf(':');
/*     */       } 
/*  79 */       if (portIndex != -1) {
/*     */         try {
/*  81 */           return new URI(scheme, null, header.substring(0, portIndex), 
/*  82 */               Integer.parseInt(header.substring(portIndex + 1)), null, null, null);
/*     */         }
/*  84 */         catch (NumberFormatException ex) {
/*  85 */           throw new URISyntaxException(header, "Unable to parse port", portIndex);
/*     */         } 
/*     */       }
/*     */       
/*  89 */       return new URI(scheme, header, null, null);
/*     */     } 
/*     */ 
/*     */     
/*  93 */     InetSocketAddress localAddress = request.hostAddress();
/*  94 */     return new URI(scheme, null, localAddress.getHostString(), localAddress
/*  95 */         .getPort(), null, null, null);
/*     */   }
/*     */ 
/*     */   
/*     */   private static String getScheme(HttpServerRequest request) {
/* 100 */     return request.scheme();
/*     */   }
/*     */   
/*     */   private static String resolveRequestUri(HttpServerRequest request) {
/* 104 */     String uri = request.uri();
/* 105 */     for (int i = 0; i < uri.length(); i++) {
/* 106 */       char c = uri.charAt(i);
/* 107 */       if (c == '/' || c == '?' || c == '#') {
/*     */         break;
/*     */       }
/* 110 */       if (c == ':' && i + 2 < uri.length() && 
/* 111 */         uri.charAt(i + 1) == '/' && uri.charAt(i + 2) == '/') {
/* 112 */         for (int j = i + 3; j < uri.length(); j++) {
/* 113 */           c = uri.charAt(j);
/* 114 */           if (c == '/' || c == '?' || c == '#') {
/* 115 */             return uri.substring(j);
/*     */           }
/*     */         } 
/* 118 */         return "";
/*     */       } 
/*     */     } 
/*     */     
/* 122 */     return uri;
/*     */   }
/*     */   
/*     */   private static HttpHeaders initHeaders(HttpServerRequest channel) {
/* 126 */     NettyHeadersAdapter headersMap = new NettyHeadersAdapter(channel.requestHeaders());
/* 127 */     return new HttpHeaders(headersMap);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMethodValue() {
/* 133 */     return this.request.method().name();
/*     */   }
/*     */ 
/*     */   
/*     */   protected MultiValueMap<String, HttpCookie> initCookies() {
/* 138 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap();
/* 139 */     for (CharSequence name : this.request.cookies().keySet()) {
/* 140 */       for (Cookie cookie : this.request.cookies().get(name)) {
/* 141 */         HttpCookie httpCookie = new HttpCookie(name.toString(), cookie.value());
/* 142 */         linkedMultiValueMap.add(name.toString(), httpCookie);
/*     */       } 
/*     */     } 
/* 145 */     return (MultiValueMap<String, HttpCookie>)linkedMultiValueMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public InetSocketAddress getRemoteAddress() {
/* 150 */     return this.request.remoteAddress();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected SslInfo initSslInfo() {
/* 156 */     SslHandler sslHandler = (SslHandler)((Connection)this.request).channel().pipeline().get(SslHandler.class);
/* 157 */     if (sslHandler != null) {
/* 158 */       SSLSession session = sslHandler.engine().getSession();
/* 159 */       return new DefaultSslInfo(session);
/*     */     } 
/* 161 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Flux<DataBuffer> getBody() {
/* 166 */     return this.request.receive().retain().map(this.bufferFactory::wrap);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getNativeRequest() {
/* 172 */     return (T)this.request;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String initId() {
/* 178 */     return (this.request instanceof Connection) ? ((Connection)this.request)
/* 179 */       .channel().id().asShortText() : null;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/reactive/ReactorServerHttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */