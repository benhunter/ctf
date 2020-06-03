/*    */ package org.springframework.web.server.handler;
/*    */ 
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.web.server.ServerWebExchange;
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
/*    */ public class WebHandlerDecorator
/*    */   implements WebHandler
/*    */ {
/*    */   private final WebHandler delegate;
/*    */   
/*    */   public WebHandlerDecorator(WebHandler delegate) {
/* 37 */     Assert.notNull(delegate, "'delegate' must not be null");
/* 38 */     this.delegate = delegate;
/*    */   }
/*    */ 
/*    */   
/*    */   public WebHandler getDelegate() {
/* 43 */     return this.delegate;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Mono<Void> handle(ServerWebExchange exchange) {
/* 49 */     return this.delegate.handle(exchange);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 54 */     return getClass().getSimpleName() + " [delegate=" + this.delegate + "]";
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/server/handler/WebHandlerDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */