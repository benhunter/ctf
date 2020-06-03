/*     */ package org.springframework.http.server.reactive;
/*     */ 
/*     */ import io.undertow.server.HttpHandler;
/*     */ import io.undertow.server.HttpServerExchange;
/*     */ import java.io.IOException;
/*     */ import java.net.URISyntaxException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.reactivestreams.Subscriber;
/*     */ import org.reactivestreams.Subscription;
/*     */ import org.springframework.core.io.buffer.DataBufferFactory;
/*     */ import org.springframework.core.io.buffer.DefaultDataBufferFactory;
/*     */ import org.springframework.http.HttpLogging;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UndertowHttpHandlerAdapter
/*     */   implements HttpHandler
/*     */ {
/*  43 */   private static final Log logger = HttpLogging.forLogName(UndertowHttpHandlerAdapter.class);
/*     */ 
/*     */   
/*     */   private final HttpHandler httpHandler;
/*     */   
/*  48 */   private DataBufferFactory bufferFactory = (DataBufferFactory)new DefaultDataBufferFactory(false);
/*     */ 
/*     */   
/*     */   public UndertowHttpHandlerAdapter(HttpHandler httpHandler) {
/*  52 */     Assert.notNull(httpHandler, "HttpHandler must not be null");
/*  53 */     this.httpHandler = httpHandler;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDataBufferFactory(DataBufferFactory bufferFactory) {
/*  58 */     Assert.notNull(bufferFactory, "DataBufferFactory must not be null");
/*  59 */     this.bufferFactory = bufferFactory;
/*     */   }
/*     */   
/*     */   public DataBufferFactory getDataBufferFactory() {
/*  63 */     return this.bufferFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleRequest(HttpServerExchange exchange) {
/*  69 */     UndertowServerHttpRequest request = null;
/*     */     try {
/*  71 */       request = new UndertowServerHttpRequest(exchange, getDataBufferFactory());
/*     */     }
/*  73 */     catch (URISyntaxException ex) {
/*  74 */       if (logger.isWarnEnabled()) {
/*  75 */         logger.debug("Failed to get request URI: " + ex.getMessage());
/*     */       }
/*  77 */       exchange.setStatusCode(400);
/*     */       return;
/*     */     } 
/*  80 */     ServerHttpResponse response = new UndertowServerHttpResponse(exchange, getDataBufferFactory(), request);
/*     */     
/*  82 */     if (request.getMethod() == HttpMethod.HEAD) {
/*  83 */       response = new HttpHeadResponseDecorator(response);
/*     */     }
/*     */     
/*  86 */     HandlerResultSubscriber resultSubscriber = new HandlerResultSubscriber(exchange, request);
/*  87 */     this.httpHandler.handle(request, response).subscribe(resultSubscriber);
/*     */   }
/*     */ 
/*     */   
/*     */   private class HandlerResultSubscriber
/*     */     implements Subscriber<Void>
/*     */   {
/*     */     private final HttpServerExchange exchange;
/*     */     
/*     */     private final String logPrefix;
/*     */     
/*     */     public HandlerResultSubscriber(HttpServerExchange exchange, UndertowServerHttpRequest request) {
/*  99 */       this.exchange = exchange;
/* 100 */       this.logPrefix = request.getLogPrefix();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription subscription) {
/* 105 */       subscription.request(Long.MAX_VALUE);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onNext(Void aVoid) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void onError(Throwable ex) {
/* 115 */       UndertowHttpHandlerAdapter.logger.trace(this.logPrefix + "Failed to complete: " + ex.getMessage());
/* 116 */       if (this.exchange.isResponseStarted()) {
/*     */         try {
/* 118 */           UndertowHttpHandlerAdapter.logger.debug(this.logPrefix + "Closing connection");
/* 119 */           this.exchange.getConnection().close();
/*     */         }
/* 121 */         catch (IOException iOException) {}
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 126 */         UndertowHttpHandlerAdapter.logger.debug(this.logPrefix + "Setting HttpServerExchange status to 500 Server Error");
/* 127 */         this.exchange.setStatusCode(500);
/* 128 */         this.exchange.endExchange();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 134 */       UndertowHttpHandlerAdapter.logger.trace(this.logPrefix + "Handling completed");
/* 135 */       this.exchange.endExchange();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/reactive/UndertowHttpHandlerAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */