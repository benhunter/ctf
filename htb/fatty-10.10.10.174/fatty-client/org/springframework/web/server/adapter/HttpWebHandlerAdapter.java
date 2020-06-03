/*     */ package org.springframework.web.server.adapter;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.core.NestedExceptionUtils;
/*     */ import org.springframework.core.log.LogFormatUtils;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.codec.HttpMessageReader;
/*     */ import org.springframework.http.codec.LoggingCodecSupport;
/*     */ import org.springframework.http.codec.ServerCodecConfigurer;
/*     */ import org.springframework.http.server.reactive.HttpHandler;
/*     */ import org.springframework.http.server.reactive.ServerHttpRequest;
/*     */ import org.springframework.http.server.reactive.ServerHttpResponse;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.server.ServerWebExchange;
/*     */ import org.springframework.web.server.WebHandler;
/*     */ import org.springframework.web.server.handler.WebHandlerDecorator;
/*     */ import org.springframework.web.server.i18n.AcceptHeaderLocaleContextResolver;
/*     */ import org.springframework.web.server.i18n.LocaleContextResolver;
/*     */ import org.springframework.web.server.session.DefaultWebSessionManager;
/*     */ import org.springframework.web.server.session.WebSessionManager;
/*     */ import reactor.core.publisher.Mono;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpWebHandlerAdapter
/*     */   extends WebHandlerDecorator
/*     */   implements HttpHandler
/*     */ {
/*     */   private static final String DISCONNECTED_CLIENT_LOG_CATEGORY = "org.springframework.web.server.DisconnectedClient";
/*  73 */   private static final Set<String> DISCONNECTED_CLIENT_EXCEPTIONS = new HashSet<>(
/*  74 */       Arrays.asList(new String[] { "AbortedException", "ClientAbortException", "EOFException", "EofException" }));
/*     */ 
/*     */   
/*  77 */   private static final Log logger = LogFactory.getLog(HttpWebHandlerAdapter.class);
/*     */   
/*  79 */   private static final Log lostClientLogger = LogFactory.getLog("org.springframework.web.server.DisconnectedClient");
/*     */ 
/*     */   
/*  82 */   private WebSessionManager sessionManager = (WebSessionManager)new DefaultWebSessionManager();
/*     */   
/*  84 */   private ServerCodecConfigurer codecConfigurer = ServerCodecConfigurer.create();
/*     */   
/*  86 */   private LocaleContextResolver localeContextResolver = (LocaleContextResolver)new AcceptHeaderLocaleContextResolver();
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ForwardedHeaderTransformer forwardedHeaderTransformer;
/*     */   
/*     */   @Nullable
/*     */   private ApplicationContext applicationContext;
/*     */   
/*     */   private boolean enableLoggingRequestDetails = false;
/*     */ 
/*     */   
/*     */   public HttpWebHandlerAdapter(WebHandler delegate) {
/*  99 */     super(delegate);
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
/*     */   public void setSessionManager(WebSessionManager sessionManager) {
/* 111 */     Assert.notNull(sessionManager, "WebSessionManager must not be null");
/* 112 */     this.sessionManager = sessionManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebSessionManager getSessionManager() {
/* 119 */     return this.sessionManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCodecConfigurer(ServerCodecConfigurer codecConfigurer) {
/* 129 */     Assert.notNull(codecConfigurer, "ServerCodecConfigurer is required");
/* 130 */     this.codecConfigurer = codecConfigurer;
/*     */     
/* 132 */     this.enableLoggingRequestDetails = false;
/* 133 */     this.codecConfigurer.getReaders().stream()
/* 134 */       .filter(LoggingCodecSupport.class::isInstance)
/* 135 */       .forEach(reader -> {
/*     */           if (((LoggingCodecSupport)reader).isEnableLoggingRequestDetails()) {
/*     */             this.enableLoggingRequestDetails = true;
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServerCodecConfigurer getCodecConfigurer() {
/* 146 */     return this.codecConfigurer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocaleContextResolver(LocaleContextResolver resolver) {
/* 157 */     Assert.notNull(resolver, "LocaleContextResolver is required");
/* 158 */     this.localeContextResolver = resolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LocaleContextResolver getLocaleContextResolver() {
/* 165 */     return this.localeContextResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setForwardedHeaderTransformer(ForwardedHeaderTransformer transformer) {
/* 176 */     Assert.notNull(transformer, "ForwardedHeaderTransformer is required");
/* 177 */     this.forwardedHeaderTransformer = transformer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ForwardedHeaderTransformer getForwardedHeaderTransformer() {
/* 186 */     return this.forwardedHeaderTransformer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setApplicationContext(ApplicationContext applicationContext) {
/* 197 */     this.applicationContext = applicationContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ApplicationContext getApplicationContext() {
/* 206 */     return this.applicationContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 214 */     if (logger.isDebugEnabled()) {
/* 215 */       String value = this.enableLoggingRequestDetails ? "shown which may lead to unsafe logging of potentially sensitive data" : "masked to prevent unsafe logging of potentially sensitive data";
/*     */ 
/*     */       
/* 218 */       logger.debug("enableLoggingRequestDetails='" + this.enableLoggingRequestDetails + "': form data and headers will be " + value);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mono<Void> handle(ServerHttpRequest request, ServerHttpResponse response) {
/* 226 */     if (this.forwardedHeaderTransformer != null) {
/* 227 */       request = this.forwardedHeaderTransformer.apply(request);
/*     */     }
/* 229 */     ServerWebExchange exchange = createExchange(request, response);
/*     */     
/* 231 */     LogFormatUtils.traceDebug(logger, traceOn -> exchange.getLogPrefix() + formatRequest(exchange.getRequest()) + (traceOn.booleanValue() ? (", headers=" + formatHeaders(exchange.getRequest().getHeaders())) : ""));
/*     */ 
/*     */ 
/*     */     
/* 235 */     return getDelegate().handle(exchange)
/* 236 */       .doOnSuccess(aVoid -> logResponse(exchange))
/* 237 */       .onErrorResume(ex -> handleUnresolvedError(exchange, ex))
/* 238 */       .then(Mono.defer(response::setComplete));
/*     */   }
/*     */   
/*     */   protected ServerWebExchange createExchange(ServerHttpRequest request, ServerHttpResponse response) {
/* 242 */     return new DefaultServerWebExchange(request, response, this.sessionManager, 
/* 243 */         getCodecConfigurer(), getLocaleContextResolver(), this.applicationContext);
/*     */   }
/*     */   
/*     */   private String formatRequest(ServerHttpRequest request) {
/* 247 */     String rawQuery = request.getURI().getRawQuery();
/* 248 */     String query = StringUtils.hasText(rawQuery) ? ("?" + rawQuery) : "";
/* 249 */     return "HTTP " + request.getMethod() + " \"" + request.getPath() + query + "\"";
/*     */   }
/*     */   
/*     */   private void logResponse(ServerWebExchange exchange) {
/* 253 */     LogFormatUtils.traceDebug(logger, traceOn -> {
/*     */           HttpStatus status = exchange.getResponse().getStatusCode();
/*     */           return exchange.getLogPrefix() + "Completed " + ((status != null) ? (String)status : "200 OK") + (traceOn.booleanValue() ? (", headers=" + formatHeaders(exchange.getResponse().getHeaders())) : "");
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private String formatHeaders(HttpHeaders responseHeaders) {
/* 261 */     return this.enableLoggingRequestDetails ? responseHeaders
/* 262 */       .toString() : (responseHeaders.isEmpty() ? "{}" : "{masked}");
/*     */   }
/*     */   
/*     */   private Mono<Void> handleUnresolvedError(ServerWebExchange exchange, Throwable ex) {
/* 266 */     ServerHttpRequest request = exchange.getRequest();
/* 267 */     ServerHttpResponse response = exchange.getResponse();
/* 268 */     String logPrefix = exchange.getLogPrefix();
/*     */     
/* 270 */     if (isDisconnectedClientError(ex)) {
/* 271 */       if (lostClientLogger.isTraceEnabled()) {
/* 272 */         lostClientLogger.trace(logPrefix + "Client went away", ex);
/*     */       }
/* 274 */       else if (lostClientLogger.isDebugEnabled()) {
/* 275 */         lostClientLogger.debug(logPrefix + "Client went away: " + ex + " (stacktrace at TRACE level for '" + "org.springframework.web.server.DisconnectedClient" + "')");
/*     */       } 
/*     */       
/* 278 */       return Mono.empty();
/*     */     } 
/* 280 */     if (response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)) {
/* 281 */       logger.error(logPrefix + "500 Server Error for " + formatRequest(request), ex);
/* 282 */       return Mono.empty();
/*     */     } 
/*     */ 
/*     */     
/* 286 */     logger.error(logPrefix + "Error [" + ex + "] for " + formatRequest(request) + ", but ServerHttpResponse already committed (" + response
/* 287 */         .getStatusCode() + ")");
/* 288 */     return Mono.error(ex);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isDisconnectedClientError(Throwable ex) {
/* 293 */     String message = NestedExceptionUtils.getMostSpecificCause(ex).getMessage();
/* 294 */     if (message != null) {
/* 295 */       String text = message.toLowerCase();
/* 296 */       if (text.contains("broken pipe") || text.contains("connection reset by peer")) {
/* 297 */         return true;
/*     */       }
/*     */     } 
/* 300 */     return DISCONNECTED_CLIENT_EXCEPTIONS.contains(ex.getClass().getSimpleName());
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/server/adapter/HttpWebHandlerAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */