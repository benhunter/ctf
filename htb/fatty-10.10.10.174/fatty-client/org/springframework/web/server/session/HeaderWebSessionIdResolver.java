/*    */ package org.springframework.web.server.session;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.web.server.ServerWebExchange;
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
/*    */ public class HeaderWebSessionIdResolver
/*    */   implements WebSessionIdResolver
/*    */ {
/*    */   public static final String DEFAULT_HEADER_NAME = "SESSION";
/* 39 */   private String headerName = "SESSION";
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setHeaderName(String headerName) {
/* 50 */     Assert.hasText(headerName, "'headerName' must not be empty");
/* 51 */     this.headerName = headerName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getHeaderName() {
/* 59 */     return this.headerName;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public List<String> resolveSessionIds(ServerWebExchange exchange) {
/* 65 */     HttpHeaders headers = exchange.getRequest().getHeaders();
/* 66 */     return (List<String>)headers.getOrDefault(getHeaderName(), Collections.emptyList());
/*    */   }
/*    */ 
/*    */   
/*    */   public void setSessionId(ServerWebExchange exchange, String id) {
/* 71 */     Assert.notNull(id, "'id' is required.");
/* 72 */     exchange.getResponse().getHeaders().set(getHeaderName(), id);
/*    */   }
/*    */ 
/*    */   
/*    */   public void expireSession(ServerWebExchange exchange) {
/* 77 */     setSessionId(exchange, "");
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/server/session/HeaderWebSessionIdResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */