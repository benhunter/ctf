/*     */ package org.springframework.remoting.httpinvoker;
/*     */ 
/*     */ import com.sun.net.httpserver.HttpExchange;
/*     */ import com.sun.net.httpserver.HttpHandler;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import org.springframework.lang.UsesSunHttpServer;
/*     */ import org.springframework.remoting.rmi.RemoteInvocationSerializingExporter;
/*     */ import org.springframework.remoting.support.RemoteInvocation;
/*     */ import org.springframework.remoting.support.RemoteInvocationResult;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ @UsesSunHttpServer
/*     */ public class SimpleHttpInvokerServiceExporter
/*     */   extends RemoteInvocationSerializingExporter
/*     */   implements HttpHandler
/*     */ {
/*     */   public void handle(HttpExchange exchange) throws IOException {
/*     */     try {
/*  73 */       RemoteInvocation invocation = readRemoteInvocation(exchange);
/*  74 */       RemoteInvocationResult result = invokeAndCreateResult(invocation, getProxy());
/*  75 */       writeRemoteInvocationResult(exchange, result);
/*  76 */       exchange.close();
/*     */     }
/*  78 */     catch (ClassNotFoundException ex) {
/*  79 */       exchange.sendResponseHeaders(500, -1L);
/*  80 */       this.logger.error("Class not found during deserialization", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected RemoteInvocation readRemoteInvocation(HttpExchange exchange) throws IOException, ClassNotFoundException {
/*  96 */     return readRemoteInvocation(exchange, exchange.getRequestBody());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected RemoteInvocation readRemoteInvocation(HttpExchange exchange, InputStream is) throws IOException, ClassNotFoundException {
/* 115 */     ObjectInputStream ois = createObjectInputStream(decorateInputStream(exchange, is));
/* 116 */     return doReadRemoteInvocation(ois);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected InputStream decorateInputStream(HttpExchange exchange, InputStream is) throws IOException {
/* 130 */     return is;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeRemoteInvocationResult(HttpExchange exchange, RemoteInvocationResult result) throws IOException {
/* 142 */     exchange.getResponseHeaders().set("Content-Type", getContentType());
/* 143 */     exchange.sendResponseHeaders(200, 0L);
/* 144 */     writeRemoteInvocationResult(exchange, result, exchange.getResponseBody());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeRemoteInvocationResult(HttpExchange exchange, RemoteInvocationResult result, OutputStream os) throws IOException {
/* 164 */     ObjectOutputStream oos = createObjectOutputStream(decorateOutputStream(exchange, os));
/* 165 */     doWriteRemoteInvocationResult(result, oos);
/* 166 */     oos.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected OutputStream decorateOutputStream(HttpExchange exchange, OutputStream os) throws IOException {
/* 180 */     return os;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/httpinvoker/SimpleHttpInvokerServiceExporter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */