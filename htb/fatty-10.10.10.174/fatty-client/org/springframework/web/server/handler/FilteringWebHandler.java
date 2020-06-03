/*    */ package org.springframework.web.server.handler;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.springframework.web.server.ServerWebExchange;
/*    */ import org.springframework.web.server.WebFilter;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FilteringWebHandler
/*    */   extends WebHandlerDecorator
/*    */ {
/*    */   private final DefaultWebFilterChain chain;
/*    */   
/*    */   public FilteringWebHandler(WebHandler handler, List<WebFilter> filters) {
/* 44 */     super(handler);
/* 45 */     this.chain = new DefaultWebFilterChain(handler, filters);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<WebFilter> getFilters() {
/* 53 */     return this.chain.getFilters();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Mono<Void> handle(ServerWebExchange exchange) {
/* 59 */     return this.chain.filter(exchange);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/server/handler/FilteringWebHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */