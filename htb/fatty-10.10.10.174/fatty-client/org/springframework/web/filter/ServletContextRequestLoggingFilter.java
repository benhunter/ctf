/*    */ package org.springframework.web.filter;
/*    */ 
/*    */ import javax.servlet.http.HttpServletRequest;
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
/*    */ public class ServletContextRequestLoggingFilter
/*    */   extends AbstractRequestLoggingFilter
/*    */ {
/*    */   protected void beforeRequest(HttpServletRequest request, String message) {
/* 41 */     getServletContext().log(message);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void afterRequest(HttpServletRequest request, String message) {
/* 49 */     getServletContext().log(message);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/filter/ServletContextRequestLoggingFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */