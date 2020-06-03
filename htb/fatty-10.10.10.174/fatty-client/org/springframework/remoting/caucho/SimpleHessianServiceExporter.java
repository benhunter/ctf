/*    */ package org.springframework.remoting.caucho;
/*    */ 
/*    */ import com.sun.net.httpserver.HttpExchange;
/*    */ import com.sun.net.httpserver.HttpHandler;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import org.springframework.lang.UsesSunHttpServer;
/*    */ import org.springframework.util.FileCopyUtils;
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
/*    */ @Deprecated
/*    */ @UsesSunHttpServer
/*    */ public class SimpleHessianServiceExporter
/*    */   extends HessianExporter
/*    */   implements HttpHandler
/*    */ {
/*    */   public void handle(HttpExchange exchange) throws IOException {
/* 56 */     if (!"POST".equals(exchange.getRequestMethod())) {
/* 57 */       exchange.getResponseHeaders().set("Allow", "POST");
/* 58 */       exchange.sendResponseHeaders(405, -1L);
/*    */       
/*    */       return;
/*    */     } 
/* 62 */     ByteArrayOutputStream output = new ByteArrayOutputStream(1024);
/*    */     try {
/* 64 */       invoke(exchange.getRequestBody(), output);
/*    */     }
/* 66 */     catch (Throwable ex) {
/* 67 */       exchange.sendResponseHeaders(500, -1L);
/* 68 */       this.logger.error("Hessian skeleton invocation failed", ex);
/*    */       
/*    */       return;
/*    */     } 
/* 72 */     exchange.getResponseHeaders().set("Content-Type", "application/x-hessian");
/* 73 */     exchange.sendResponseHeaders(200, output.size());
/* 74 */     FileCopyUtils.copy(output.toByteArray(), exchange.getResponseBody());
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/caucho/SimpleHessianServiceExporter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */