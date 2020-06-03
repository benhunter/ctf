/*     */ package org.springframework.web.cors;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.HttpRequest;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.server.ServerHttpRequest;
/*     */ import org.springframework.http.server.ServerHttpResponse;
/*     */ import org.springframework.http.server.ServletServerHttpRequest;
/*     */ import org.springframework.http.server.ServletServerHttpResponse;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.CollectionUtils;
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
/*     */ public class DefaultCorsProcessor
/*     */   implements CorsProcessor
/*     */ {
/*  57 */   private static final Log logger = LogFactory.getLog(DefaultCorsProcessor.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean processRequest(@Nullable CorsConfiguration config, HttpServletRequest request, HttpServletResponse response) throws IOException {
/*  65 */     if (!CorsUtils.isCorsRequest(request)) {
/*  66 */       return true;
/*     */     }
/*     */     
/*  69 */     ServletServerHttpResponse serverResponse = new ServletServerHttpResponse(response);
/*  70 */     if (responseHasCors((ServerHttpResponse)serverResponse)) {
/*  71 */       logger.trace("Skip: response already contains \"Access-Control-Allow-Origin\"");
/*  72 */       return true;
/*     */     } 
/*     */     
/*  75 */     ServletServerHttpRequest serverRequest = new ServletServerHttpRequest(request);
/*  76 */     if (WebUtils.isSameOrigin((HttpRequest)serverRequest)) {
/*  77 */       logger.trace("Skip: request is from same origin");
/*  78 */       return true;
/*     */     } 
/*     */     
/*  81 */     boolean preFlightRequest = CorsUtils.isPreFlightRequest(request);
/*  82 */     if (config == null) {
/*  83 */       if (preFlightRequest) {
/*  84 */         rejectRequest((ServerHttpResponse)serverResponse);
/*  85 */         return false;
/*     */       } 
/*     */       
/*  88 */       return true;
/*     */     } 
/*     */ 
/*     */     
/*  92 */     return handleInternal((ServerHttpRequest)serverRequest, (ServerHttpResponse)serverResponse, config, preFlightRequest);
/*     */   }
/*     */   
/*     */   private boolean responseHasCors(ServerHttpResponse response) {
/*     */     try {
/*  97 */       return (response.getHeaders().getAccessControlAllowOrigin() != null);
/*     */     }
/*  99 */     catch (NullPointerException npe) {
/*     */       
/* 101 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void rejectRequest(ServerHttpResponse response) throws IOException {
/* 111 */     response.setStatusCode(HttpStatus.FORBIDDEN);
/* 112 */     response.getBody().write("Invalid CORS request".getBytes(StandardCharsets.UTF_8));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean handleInternal(ServerHttpRequest request, ServerHttpResponse response, CorsConfiguration config, boolean preFlightRequest) throws IOException {
/* 121 */     String requestOrigin = request.getHeaders().getOrigin();
/* 122 */     String allowOrigin = checkOrigin(config, requestOrigin);
/* 123 */     HttpHeaders responseHeaders = response.getHeaders();
/*     */     
/* 125 */     responseHeaders.addAll("Vary", Arrays.asList(new String[] { "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers" }));
/*     */ 
/*     */     
/* 128 */     if (allowOrigin == null) {
/* 129 */       logger.debug("Reject: '" + requestOrigin + "' origin is not allowed");
/* 130 */       rejectRequest(response);
/* 131 */       return false;
/*     */     } 
/*     */     
/* 134 */     HttpMethod requestMethod = getMethodToUse(request, preFlightRequest);
/* 135 */     List<HttpMethod> allowMethods = checkMethods(config, requestMethod);
/* 136 */     if (allowMethods == null) {
/* 137 */       logger.debug("Reject: HTTP '" + requestMethod + "' is not allowed");
/* 138 */       rejectRequest(response);
/* 139 */       return false;
/*     */     } 
/*     */     
/* 142 */     List<String> requestHeaders = getHeadersToUse(request, preFlightRequest);
/* 143 */     List<String> allowHeaders = checkHeaders(config, requestHeaders);
/* 144 */     if (preFlightRequest && allowHeaders == null) {
/* 145 */       logger.debug("Reject: headers '" + requestHeaders + "' are not allowed");
/* 146 */       rejectRequest(response);
/* 147 */       return false;
/*     */     } 
/*     */     
/* 150 */     responseHeaders.setAccessControlAllowOrigin(allowOrigin);
/*     */     
/* 152 */     if (preFlightRequest) {
/* 153 */       responseHeaders.setAccessControlAllowMethods(allowMethods);
/*     */     }
/*     */     
/* 156 */     if (preFlightRequest && !allowHeaders.isEmpty()) {
/* 157 */       responseHeaders.setAccessControlAllowHeaders(allowHeaders);
/*     */     }
/*     */     
/* 160 */     if (!CollectionUtils.isEmpty(config.getExposedHeaders())) {
/* 161 */       responseHeaders.setAccessControlExposeHeaders(config.getExposedHeaders());
/*     */     }
/*     */     
/* 164 */     if (Boolean.TRUE.equals(config.getAllowCredentials())) {
/* 165 */       responseHeaders.setAccessControlAllowCredentials(true);
/*     */     }
/*     */     
/* 168 */     if (preFlightRequest && config.getMaxAge() != null) {
/* 169 */       responseHeaders.setAccessControlMaxAge(config.getMaxAge().longValue());
/*     */     }
/*     */     
/* 172 */     response.flush();
/* 173 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String checkOrigin(CorsConfiguration config, @Nullable String requestOrigin) {
/* 183 */     return config.checkOrigin(requestOrigin);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected List<HttpMethod> checkMethods(CorsConfiguration config, @Nullable HttpMethod requestMethod) {
/* 193 */     return config.checkHttpMethod(requestMethod);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private HttpMethod getMethodToUse(ServerHttpRequest request, boolean isPreFlight) {
/* 198 */     return isPreFlight ? request.getHeaders().getAccessControlRequestMethod() : request.getMethod();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected List<String> checkHeaders(CorsConfiguration config, List<String> requestHeaders) {
/* 208 */     return config.checkHeaders(requestHeaders);
/*     */   }
/*     */   
/*     */   private List<String> getHeadersToUse(ServerHttpRequest request, boolean isPreFlight) {
/* 212 */     HttpHeaders headers = request.getHeaders();
/* 213 */     return isPreFlight ? headers.getAccessControlRequestHeaders() : new ArrayList<>(headers.keySet());
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/cors/DefaultCorsProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */