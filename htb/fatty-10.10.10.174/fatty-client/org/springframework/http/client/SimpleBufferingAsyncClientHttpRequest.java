/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.HttpURLConnection;
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
/*    */ import java.util.concurrent.Callable;
/*    */ import org.springframework.core.task.AsyncListenableTaskExecutor;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.http.HttpMethod;
/*    */ import org.springframework.util.FileCopyUtils;
/*    */ import org.springframework.util.concurrent.ListenableFuture;
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
/*    */ final class SimpleBufferingAsyncClientHttpRequest
/*    */   extends AbstractBufferingAsyncClientHttpRequest
/*    */ {
/*    */   private final HttpURLConnection connection;
/*    */   private final boolean outputStreaming;
/*    */   private final AsyncListenableTaskExecutor taskExecutor;
/*    */   
/*    */   SimpleBufferingAsyncClientHttpRequest(HttpURLConnection connection, boolean outputStreaming, AsyncListenableTaskExecutor taskExecutor) {
/* 54 */     this.connection = connection;
/* 55 */     this.outputStreaming = outputStreaming;
/* 56 */     this.taskExecutor = taskExecutor;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMethodValue() {
/* 62 */     return this.connection.getRequestMethod();
/*    */   }
/*    */ 
/*    */   
/*    */   public URI getURI() {
/*    */     try {
/* 68 */       return this.connection.getURL().toURI();
/*    */     }
/* 70 */     catch (URISyntaxException ex) {
/* 71 */       throw new IllegalStateException("Could not get HttpURLConnection URI: " + ex.getMessage(), ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected ListenableFuture<ClientHttpResponse> executeInternal(final HttpHeaders headers, final byte[] bufferedOutput) throws IOException {
/* 79 */     return this.taskExecutor.submitListenable(new Callable<ClientHttpResponse>()
/*    */         {
/*    */           public ClientHttpResponse call() throws Exception {
/* 82 */             SimpleBufferingClientHttpRequest.addHeaders(SimpleBufferingAsyncClientHttpRequest.this.connection, headers);
/*    */             
/* 84 */             if (SimpleBufferingAsyncClientHttpRequest.this.getMethod() == HttpMethod.DELETE && bufferedOutput.length == 0) {
/* 85 */               SimpleBufferingAsyncClientHttpRequest.this.connection.setDoOutput(false);
/*    */             }
/* 87 */             if (SimpleBufferingAsyncClientHttpRequest.this.connection.getDoOutput() && SimpleBufferingAsyncClientHttpRequest.this.outputStreaming) {
/* 88 */               SimpleBufferingAsyncClientHttpRequest.this.connection.setFixedLengthStreamingMode(bufferedOutput.length);
/*    */             }
/* 90 */             SimpleBufferingAsyncClientHttpRequest.this.connection.connect();
/* 91 */             if (SimpleBufferingAsyncClientHttpRequest.this.connection.getDoOutput()) {
/* 92 */               FileCopyUtils.copy(bufferedOutput, SimpleBufferingAsyncClientHttpRequest.this.connection.getOutputStream());
/*    */             }
/*    */             else {
/*    */               
/* 96 */               SimpleBufferingAsyncClientHttpRequest.this.connection.getResponseCode();
/*    */             } 
/* 98 */             return new SimpleClientHttpResponse(SimpleBufferingAsyncClientHttpRequest.this.connection);
/*    */           }
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/SimpleBufferingAsyncClientHttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */