/*    */ package org.springframework.web.servlet.mvc;
/*    */ 
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.web.HttpRequestHandler;
/*    */ import org.springframework.web.servlet.HandlerAdapter;
/*    */ import org.springframework.web.servlet.ModelAndView;
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
/*    */ public class HttpRequestHandlerAdapter
/*    */   implements HandlerAdapter
/*    */ {
/*    */   public boolean supports(Object handler) {
/* 45 */     return handler instanceof HttpRequestHandler;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
/* 53 */     ((HttpRequestHandler)handler).handleRequest(request, response);
/* 54 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getLastModified(HttpServletRequest request, Object handler) {
/* 59 */     if (handler instanceof LastModified) {
/* 60 */       return ((LastModified)handler).getLastModified(request);
/*    */     }
/* 62 */     return -1L;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/HttpRequestHandlerAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */