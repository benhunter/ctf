/*    */ package org.springframework.web.cors;
/*    */ 
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.springframework.http.HttpMethod;
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
/*    */ public abstract class CorsUtils
/*    */ {
/*    */   public static boolean isCorsRequest(HttpServletRequest request) {
/* 37 */     return (request.getHeader("Origin") != null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean isPreFlightRequest(HttpServletRequest request) {
/* 44 */     return (isCorsRequest(request) && HttpMethod.OPTIONS.matches(request.getMethod()) && request
/* 45 */       .getHeader("Access-Control-Request-Method") != null);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/cors/CorsUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */