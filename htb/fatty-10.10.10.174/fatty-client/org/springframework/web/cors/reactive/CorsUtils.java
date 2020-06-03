/*    */ package org.springframework.web.cors.reactive;
/*    */ 
/*    */ import java.net.URI;
/*    */ import org.springframework.http.HttpMethod;
/*    */ import org.springframework.http.server.reactive.ServerHttpRequest;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.web.util.UriComponents;
/*    */ import org.springframework.web.util.UriComponentsBuilder;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class CorsUtils
/*    */ {
/*    */   public static boolean isCorsRequest(ServerHttpRequest request) {
/* 42 */     return (request.getHeaders().get("Origin") != null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean isPreFlightRequest(ServerHttpRequest request) {
/* 49 */     return (request.getMethod() == HttpMethod.OPTIONS && isCorsRequest(request) && request
/* 50 */       .getHeaders().get("Access-Control-Request-Method") != null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean isSameOrigin(ServerHttpRequest request) {
/* 66 */     String origin = request.getHeaders().getOrigin();
/* 67 */     if (origin == null) {
/* 68 */       return true;
/*    */     }
/*    */     
/* 71 */     URI uri = request.getURI();
/* 72 */     String actualScheme = uri.getScheme();
/* 73 */     String actualHost = uri.getHost();
/* 74 */     int actualPort = getPort(uri.getScheme(), uri.getPort());
/* 75 */     Assert.notNull(actualScheme, "Actual request scheme must not be null");
/* 76 */     Assert.notNull(actualHost, "Actual request host must not be null");
/* 77 */     Assert.isTrue((actualPort != -1), "Actual request port must not be undefined");
/*    */     
/* 79 */     UriComponents originUrl = UriComponentsBuilder.fromOriginHeader(origin).build();
/* 80 */     return (actualScheme.equals(originUrl.getScheme()) && actualHost
/* 81 */       .equals(originUrl.getHost()) && actualPort == 
/* 82 */       getPort(originUrl.getScheme(), originUrl.getPort()));
/*    */   }
/*    */   
/*    */   private static int getPort(@Nullable String scheme, int port) {
/* 86 */     if (port == -1) {
/* 87 */       if ("http".equals(scheme) || "ws".equals(scheme)) {
/* 88 */         port = 80;
/*    */       }
/* 90 */       else if ("https".equals(scheme) || "wss".equals(scheme)) {
/* 91 */         port = 443;
/*    */       } 
/*    */     }
/* 94 */     return port;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/cors/reactive/CorsUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */