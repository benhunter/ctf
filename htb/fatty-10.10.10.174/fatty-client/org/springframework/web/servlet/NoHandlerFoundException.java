/*    */ package org.springframework.web.servlet;
/*    */ 
/*    */ import javax.servlet.ServletException;
/*    */ import org.springframework.http.HttpHeaders;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NoHandlerFoundException
/*    */   extends ServletException
/*    */ {
/*    */   private final String httpMethod;
/*    */   private final String requestURL;
/*    */   private final HttpHeaders headers;
/*    */   
/*    */   public NoHandlerFoundException(String httpMethod, String requestURL, HttpHeaders headers) {
/* 53 */     super("No handler found for " + httpMethod + " " + requestURL);
/* 54 */     this.httpMethod = httpMethod;
/* 55 */     this.requestURL = requestURL;
/* 56 */     this.headers = headers;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getHttpMethod() {
/* 61 */     return this.httpMethod;
/*    */   }
/*    */   
/*    */   public String getRequestURL() {
/* 65 */     return this.requestURL;
/*    */   }
/*    */   
/*    */   public HttpHeaders getHeaders() {
/* 69 */     return this.headers;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/NoHandlerFoundException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */