/*    */ package org.springframework.web.server.handler;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import org.springframework.web.server.ServerWebExchange;
/*    */ import org.springframework.web.server.WebExceptionHandler;
/*    */ import org.springframework.web.server.WebHandler;
/*    */ import reactor.core.publisher.Mono;
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
/*    */ public class ExceptionHandlingWebHandler
/*    */   extends WebHandlerDecorator
/*    */ {
/*    */   private final List<WebExceptionHandler> exceptionHandlers;
/*    */   
/*    */   public ExceptionHandlingWebHandler(WebHandler delegate, List<WebExceptionHandler> handlers) {
/* 43 */     super(delegate);
/* 44 */     this.exceptionHandlers = Collections.unmodifiableList(new ArrayList<>(handlers));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<WebExceptionHandler> getExceptionHandlers() {
/* 52 */     return this.exceptionHandlers;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Mono<Void> handle(ServerWebExchange exchange) {
/*    */     Mono<Void> completion;
/*    */     try {
/* 61 */       completion = super.handle(exchange);
/*    */     }
/* 63 */     catch (Throwable ex) {
/* 64 */       completion = Mono.error(ex);
/*    */     } 
/*    */     
/* 67 */     for (WebExceptionHandler handler : this.exceptionHandlers) {
/* 68 */       completion = completion.onErrorResume(ex -> handler.handle(exchange, ex));
/*    */     }
/*    */     
/* 71 */     return completion;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/server/handler/ExceptionHandlingWebHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */