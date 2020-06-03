/*     */ package org.springframework.http.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.concurrent.Callable;
/*     */ import org.springframework.core.task.AsyncListenableTaskExecutor;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.StreamUtils;
/*     */ import org.springframework.util.concurrent.ListenableFuture;
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
/*     */ final class SimpleStreamingAsyncClientHttpRequest
/*     */   extends AbstractAsyncClientHttpRequest
/*     */ {
/*     */   private final HttpURLConnection connection;
/*     */   private final int chunkSize;
/*     */   @Nullable
/*     */   private OutputStream body;
/*     */   private final boolean outputStreaming;
/*     */   private final AsyncListenableTaskExecutor taskExecutor;
/*     */   
/*     */   SimpleStreamingAsyncClientHttpRequest(HttpURLConnection connection, int chunkSize, boolean outputStreaming, AsyncListenableTaskExecutor taskExecutor) {
/*  62 */     this.connection = connection;
/*  63 */     this.chunkSize = chunkSize;
/*  64 */     this.outputStreaming = outputStreaming;
/*  65 */     this.taskExecutor = taskExecutor;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMethodValue() {
/*  71 */     return this.connection.getRequestMethod();
/*     */   }
/*     */ 
/*     */   
/*     */   public URI getURI() {
/*     */     try {
/*  77 */       return this.connection.getURL().toURI();
/*     */     }
/*  79 */     catch (URISyntaxException ex) {
/*  80 */       throw new IllegalStateException("Could not get HttpURLConnection URI: " + ex
/*  81 */           .getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected OutputStream getBodyInternal(HttpHeaders headers) throws IOException {
/*  87 */     if (this.body == null) {
/*  88 */       if (this.outputStreaming) {
/*  89 */         long contentLength = headers.getContentLength();
/*  90 */         if (contentLength >= 0L) {
/*  91 */           this.connection.setFixedLengthStreamingMode(contentLength);
/*     */         } else {
/*     */           
/*  94 */           this.connection.setChunkedStreamingMode(this.chunkSize);
/*     */         } 
/*     */       } 
/*  97 */       SimpleBufferingClientHttpRequest.addHeaders(this.connection, headers);
/*  98 */       this.connection.connect();
/*  99 */       this.body = this.connection.getOutputStream();
/*     */     } 
/* 101 */     return StreamUtils.nonClosing(this.body);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ListenableFuture<ClientHttpResponse> executeInternal(final HttpHeaders headers) throws IOException {
/* 106 */     return this.taskExecutor.submitListenable(new Callable<ClientHttpResponse>()
/*     */         {
/*     */           public ClientHttpResponse call() throws Exception {
/*     */             try {
/* 110 */               if (SimpleStreamingAsyncClientHttpRequest.this.body != null) {
/* 111 */                 SimpleStreamingAsyncClientHttpRequest.this.body.close();
/*     */               } else {
/*     */                 
/* 114 */                 SimpleBufferingClientHttpRequest.addHeaders(SimpleStreamingAsyncClientHttpRequest.this.connection, headers);
/* 115 */                 SimpleStreamingAsyncClientHttpRequest.this.connection.connect();
/*     */                 
/* 117 */                 SimpleStreamingAsyncClientHttpRequest.this.connection.getResponseCode();
/*     */               }
/*     */             
/* 120 */             } catch (IOException iOException) {}
/*     */ 
/*     */             
/* 123 */             return new SimpleClientHttpResponse(SimpleStreamingAsyncClientHttpRequest.this.connection);
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/SimpleStreamingAsyncClientHttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */