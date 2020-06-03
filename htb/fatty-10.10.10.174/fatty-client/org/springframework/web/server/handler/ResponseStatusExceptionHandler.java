/*     */ package org.springframework.web.server.handler;
/*     */ 
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.server.reactive.ServerHttpRequest;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.web.server.ResponseStatusException;
/*     */ import org.springframework.web.server.ServerWebExchange;
/*     */ import org.springframework.web.server.WebExceptionHandler;
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
/*     */ public class ResponseStatusExceptionHandler
/*     */   implements WebExceptionHandler
/*     */ {
/*  43 */   private static final Log logger = LogFactory.getLog(ResponseStatusExceptionHandler.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Log warnLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWarnLogCategory(String loggerName) {
/*  59 */     this.warnLogger = LogFactory.getLog(loggerName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
/*  65 */     HttpStatus status = resolveStatus(ex);
/*  66 */     if (status == null || !exchange.getResponse().setStatusCode(status)) {
/*  67 */       return Mono.error(ex);
/*     */     }
/*     */ 
/*     */     
/*  71 */     String logPrefix = exchange.getLogPrefix();
/*  72 */     if (this.warnLogger != null && this.warnLogger.isWarnEnabled()) {
/*  73 */       this.warnLogger.warn(logPrefix + formatError(ex, exchange.getRequest()), ex);
/*     */     }
/*  75 */     else if (logger.isDebugEnabled()) {
/*  76 */       logger.debug(logPrefix + formatError(ex, exchange.getRequest()));
/*     */     } 
/*     */     
/*  79 */     return exchange.getResponse().setComplete();
/*     */   }
/*     */ 
/*     */   
/*     */   private String formatError(Throwable ex, ServerHttpRequest request) {
/*  84 */     String reason = ex.getClass().getSimpleName() + ": " + ex.getMessage();
/*  85 */     String path = request.getURI().getRawPath();
/*  86 */     return "Resolved [" + reason + "] for HTTP " + request.getMethod() + " " + path;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private HttpStatus resolveStatus(Throwable ex) {
/*  91 */     HttpStatus status = determineStatus(ex);
/*  92 */     if (status == null) {
/*  93 */       Throwable cause = ex.getCause();
/*  94 */       if (cause != null) {
/*  95 */         status = resolveStatus(cause);
/*     */       }
/*     */     } 
/*  98 */     return status;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected HttpStatus determineStatus(Throwable ex) {
/* 109 */     if (ex instanceof ResponseStatusException) {
/* 110 */       return ((ResponseStatusException)ex).getStatus();
/*     */     }
/* 112 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/server/handler/ResponseStatusExceptionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */