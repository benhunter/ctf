/*    */ package org.springframework.http.server.reactive;
/*    */ 
/*    */ import io.netty.handler.codec.http.HttpResponseStatus;
/*    */ import java.net.URISyntaxException;
/*    */ import java.util.function.BiFunction;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.springframework.core.io.buffer.DataBufferFactory;
/*    */ import org.springframework.core.io.buffer.NettyDataBufferFactory;
/*    */ import org.springframework.http.HttpLogging;
/*    */ import org.springframework.http.HttpMethod;
/*    */ import org.springframework.util.Assert;
/*    */ import reactor.core.publisher.Mono;
/*    */ import reactor.netty.http.server.HttpServerRequest;
/*    */ import reactor.netty.http.server.HttpServerResponse;
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
/*    */ public class ReactorHttpHandlerAdapter
/*    */   implements BiFunction<HttpServerRequest, HttpServerResponse, Mono<Void>>
/*    */ {
/* 42 */   private static final Log logger = HttpLogging.forLogName(ReactorHttpHandlerAdapter.class);
/*    */ 
/*    */   
/*    */   private final HttpHandler httpHandler;
/*    */ 
/*    */   
/*    */   public ReactorHttpHandlerAdapter(HttpHandler httpHandler) {
/* 49 */     Assert.notNull(httpHandler, "HttpHandler must not be null");
/* 50 */     this.httpHandler = httpHandler;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Mono<Void> apply(HttpServerRequest reactorRequest, HttpServerResponse reactorResponse) {
/* 56 */     NettyDataBufferFactory bufferFactory = new NettyDataBufferFactory(reactorResponse.alloc());
/*    */     try {
/* 58 */       ReactorServerHttpRequest request = new ReactorServerHttpRequest(reactorRequest, bufferFactory);
/* 59 */       ServerHttpResponse response = new ReactorServerHttpResponse(reactorResponse, (DataBufferFactory)bufferFactory);
/*    */       
/* 61 */       if (request.getMethod() == HttpMethod.HEAD) {
/* 62 */         response = new HttpHeadResponseDecorator(response);
/*    */       }
/*    */       
/* 65 */       return this.httpHandler.handle(request, response)
/* 66 */         .doOnError(ex -> logger.trace(request.getLogPrefix() + "Failed to complete: " + ex.getMessage()))
/* 67 */         .doOnSuccess(aVoid -> logger.trace(request.getLogPrefix() + "Handling completed"));
/*    */     }
/* 69 */     catch (URISyntaxException ex) {
/* 70 */       if (logger.isDebugEnabled()) {
/* 71 */         logger.debug("Failed to get request URI: " + ex.getMessage());
/*    */       }
/* 73 */       reactorResponse.status(HttpResponseStatus.BAD_REQUEST);
/* 74 */       return Mono.empty();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/reactive/ReactorHttpHandlerAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */