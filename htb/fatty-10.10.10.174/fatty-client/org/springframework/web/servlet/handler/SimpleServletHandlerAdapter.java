/*    */ package org.springframework.web.servlet.handler;
/*    */ 
/*    */ import javax.servlet.Servlet;
/*    */ import javax.servlet.ServletRequest;
/*    */ import javax.servlet.ServletResponse;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ public class SimpleServletHandlerAdapter
/*    */   implements HandlerAdapter
/*    */ {
/*    */   public boolean supports(Object handler) {
/* 59 */     return handler instanceof Servlet;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
/* 67 */     ((Servlet)handler).service((ServletRequest)request, (ServletResponse)response);
/* 68 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getLastModified(HttpServletRequest request, Object handler) {
/* 73 */     return -1L;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/handler/SimpleServletHandlerAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */