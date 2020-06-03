/*     */ package org.springframework.web.cors.reactive;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.server.reactive.ServerHttpRequest;
/*     */ import org.springframework.http.server.reactive.ServerHttpResponse;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.web.cors.CorsConfiguration;
/*     */ import org.springframework.web.server.ServerWebExchange;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultCorsProcessor
/*     */   implements CorsProcessor
/*     */ {
/*  52 */   private static final Log logger = LogFactory.getLog(DefaultCorsProcessor.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean process(@Nullable CorsConfiguration config, ServerWebExchange exchange) {
/*  58 */     ServerHttpRequest request = exchange.getRequest();
/*  59 */     ServerHttpResponse response = exchange.getResponse();
/*     */     
/*  61 */     if (!CorsUtils.isCorsRequest(request)) {
/*  62 */       return true;
/*     */     }
/*     */     
/*  65 */     if (responseHasCors(response)) {
/*  66 */       logger.trace("Skip: response already contains \"Access-Control-Allow-Origin\"");
/*  67 */       return true;
/*     */     } 
/*     */     
/*  70 */     if (CorsUtils.isSameOrigin(request)) {
/*  71 */       logger.trace("Skip: request is from same origin");
/*  72 */       return true;
/*     */     } 
/*     */     
/*  75 */     boolean preFlightRequest = CorsUtils.isPreFlightRequest(request);
/*  76 */     if (config == null) {
/*  77 */       if (preFlightRequest) {
/*  78 */         rejectRequest(response);
/*  79 */         return false;
/*     */       } 
/*     */       
/*  82 */       return true;
/*     */     } 
/*     */ 
/*     */     
/*  86 */     return handleInternal(exchange, config, preFlightRequest);
/*     */   }
/*     */   
/*     */   private boolean responseHasCors(ServerHttpResponse response) {
/*  90 */     return (response.getHeaders().getFirst("Access-Control-Allow-Origin") != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void rejectRequest(ServerHttpResponse response) {
/*  97 */     response.setStatusCode(HttpStatus.FORBIDDEN);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean handleInternal(ServerWebExchange exchange, CorsConfiguration config, boolean preFlightRequest) {
/* 106 */     ServerHttpRequest request = exchange.getRequest();
/* 107 */     ServerHttpResponse response = exchange.getResponse();
/* 108 */     HttpHeaders responseHeaders = response.getHeaders();
/*     */     
/* 110 */     response.getHeaders().addAll("Vary", Arrays.asList(new String[] { "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers" }));
/*     */ 
/*     */     
/* 113 */     String requestOrigin = request.getHeaders().getOrigin();
/* 114 */     String allowOrigin = checkOrigin(config, requestOrigin);
/* 115 */     if (allowOrigin == null) {
/* 116 */       logger.debug("Reject: '" + requestOrigin + "' origin is not allowed");
/* 117 */       rejectRequest(response);
/* 118 */       return false;
/*     */     } 
/*     */     
/* 121 */     HttpMethod requestMethod = getMethodToUse(request, preFlightRequest);
/* 122 */     List<HttpMethod> allowMethods = checkMethods(config, requestMethod);
/* 123 */     if (allowMethods == null) {
/* 124 */       logger.debug("Reject: HTTP '" + requestMethod + "' is not allowed");
/* 125 */       rejectRequest(response);
/* 126 */       return false;
/*     */     } 
/*     */     
/* 129 */     List<String> requestHeaders = getHeadersToUse(request, preFlightRequest);
/* 130 */     List<String> allowHeaders = checkHeaders(config, requestHeaders);
/* 131 */     if (preFlightRequest && allowHeaders == null) {
/* 132 */       logger.debug("Reject: headers '" + requestHeaders + "' are not allowed");
/* 133 */       rejectRequest(response);
/* 134 */       return false;
/*     */     } 
/*     */     
/* 137 */     responseHeaders.setAccessControlAllowOrigin(allowOrigin);
/*     */     
/* 139 */     if (preFlightRequest) {
/* 140 */       responseHeaders.setAccessControlAllowMethods(allowMethods);
/*     */     }
/*     */     
/* 143 */     if (preFlightRequest && !allowHeaders.isEmpty()) {
/* 144 */       responseHeaders.setAccessControlAllowHeaders(allowHeaders);
/*     */     }
/*     */     
/* 147 */     if (!CollectionUtils.isEmpty(config.getExposedHeaders())) {
/* 148 */       responseHeaders.setAccessControlExposeHeaders(config.getExposedHeaders());
/*     */     }
/*     */     
/* 151 */     if (Boolean.TRUE.equals(config.getAllowCredentials())) {
/* 152 */       responseHeaders.setAccessControlAllowCredentials(true);
/*     */     }
/*     */     
/* 155 */     if (preFlightRequest && config.getMaxAge() != null) {
/* 156 */       responseHeaders.setAccessControlMaxAge(config.getMaxAge().longValue());
/*     */     }
/*     */     
/* 159 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String checkOrigin(CorsConfiguration config, @Nullable String requestOrigin) {
/* 169 */     return config.checkOrigin(requestOrigin);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected List<HttpMethod> checkMethods(CorsConfiguration config, @Nullable HttpMethod requestMethod) {
/* 179 */     return config.checkHttpMethod(requestMethod);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private HttpMethod getMethodToUse(ServerHttpRequest request, boolean isPreFlight) {
/* 184 */     return isPreFlight ? request.getHeaders().getAccessControlRequestMethod() : request.getMethod();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected List<String> checkHeaders(CorsConfiguration config, List<String> requestHeaders) {
/* 195 */     return config.checkHeaders(requestHeaders);
/*     */   }
/*     */   
/*     */   private List<String> getHeadersToUse(ServerHttpRequest request, boolean isPreFlight) {
/* 199 */     HttpHeaders headers = request.getHeaders();
/* 200 */     return isPreFlight ? headers.getAccessControlRequestHeaders() : new ArrayList<>(headers.keySet());
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/cors/reactive/DefaultCorsProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */