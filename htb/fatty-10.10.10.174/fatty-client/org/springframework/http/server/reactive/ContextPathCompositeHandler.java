/*    */ package org.springframework.http.server.reactive;
/*    */ 
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ import org.springframework.http.HttpStatus;
/*    */ import org.springframework.util.Assert;
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
/*    */ 
/*    */ public class ContextPathCompositeHandler
/*    */   implements HttpHandler
/*    */ {
/*    */   private final Map<String, HttpHandler> handlerMap;
/*    */   
/*    */   public ContextPathCompositeHandler(Map<String, ? extends HttpHandler> handlerMap) {
/* 45 */     Assert.notEmpty(handlerMap, "Handler map must not be empty");
/* 46 */     this.handlerMap = initHandlers(handlerMap);
/*    */   }
/*    */   
/*    */   private static Map<String, HttpHandler> initHandlers(Map<String, ? extends HttpHandler> map) {
/* 50 */     map.keySet().forEach(ContextPathCompositeHandler::assertValidContextPath);
/* 51 */     return new LinkedHashMap<>(map);
/*    */   }
/*    */   
/*    */   private static void assertValidContextPath(String contextPath) {
/* 55 */     Assert.hasText(contextPath, "Context path must not be empty");
/* 56 */     if (contextPath.equals("/")) {
/*    */       return;
/*    */     }
/* 59 */     Assert.isTrue(contextPath.startsWith("/"), "Context path must begin with '/'");
/* 60 */     Assert.isTrue(!contextPath.endsWith("/"), "Context path must not end with '/'");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Mono<Void> handle(ServerHttpRequest request, ServerHttpResponse response) {
/* 67 */     String path = request.getPath().pathWithinApplication().value();
/* 68 */     return this.handlerMap.entrySet().stream()
/* 69 */       .filter(entry -> path.startsWith((String)entry.getKey()))
/* 70 */       .findFirst()
/* 71 */       .map(entry -> {
/*    */           String contextPath = request.getPath().contextPath().value() + (String)entry.getKey();
/*    */           
/*    */           ServerHttpRequest newRequest = request.mutate().contextPath(contextPath).build();
/*    */           return ((HttpHandler)entry.getValue()).handle(newRequest, response);
/* 76 */         }).orElseGet(() -> {
/*    */           response.setStatusCode(HttpStatus.NOT_FOUND);
/*    */           return response.setComplete();
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/reactive/ContextPathCompositeHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */