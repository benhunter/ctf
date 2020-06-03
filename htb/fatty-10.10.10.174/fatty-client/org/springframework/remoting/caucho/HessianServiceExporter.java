/*    */ package org.springframework.remoting.caucho;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import javax.servlet.ServletException;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.web.HttpRequestHandler;
/*    */ import org.springframework.web.HttpRequestMethodNotSupportedException;
/*    */ import org.springframework.web.util.NestedServletException;
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
/*    */ public class HessianServiceExporter
/*    */   extends HessianExporter
/*    */   implements HttpRequestHandler
/*    */ {
/*    */   public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/* 56 */     if (!"POST".equals(request.getMethod())) {
/* 57 */       throw new HttpRequestMethodNotSupportedException(request.getMethod(), new String[] { "POST" }, "HessianServiceExporter only supports POST requests");
/*    */     }
/*    */ 
/*    */     
/* 61 */     response.setContentType("application/x-hessian");
/*    */     try {
/* 63 */       invoke((InputStream)request.getInputStream(), (OutputStream)response.getOutputStream());
/*    */     }
/* 65 */     catch (Throwable ex) {
/* 66 */       throw new NestedServletException("Hessian skeleton invocation failed", ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/caucho/HessianServiceExporter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */