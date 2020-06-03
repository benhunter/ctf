/*     */ package org.springframework.http.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.StreamUtils;
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
/*     */ final class SimpleStreamingClientHttpRequest
/*     */   extends AbstractClientHttpRequest
/*     */ {
/*     */   private final HttpURLConnection connection;
/*     */   private final int chunkSize;
/*     */   @Nullable
/*     */   private OutputStream body;
/*     */   private final boolean outputStreaming;
/*     */   
/*     */   SimpleStreamingClientHttpRequest(HttpURLConnection connection, int chunkSize, boolean outputStreaming) {
/*  53 */     this.connection = connection;
/*  54 */     this.chunkSize = chunkSize;
/*  55 */     this.outputStreaming = outputStreaming;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMethodValue() {
/*  61 */     return this.connection.getRequestMethod();
/*     */   }
/*     */ 
/*     */   
/*     */   public URI getURI() {
/*     */     try {
/*  67 */       return this.connection.getURL().toURI();
/*     */     }
/*  69 */     catch (URISyntaxException ex) {
/*  70 */       throw new IllegalStateException("Could not get HttpURLConnection URI: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected OutputStream getBodyInternal(HttpHeaders headers) throws IOException {
/*  76 */     if (this.body == null) {
/*  77 */       if (this.outputStreaming) {
/*  78 */         long contentLength = headers.getContentLength();
/*  79 */         if (contentLength >= 0L) {
/*  80 */           this.connection.setFixedLengthStreamingMode(contentLength);
/*     */         } else {
/*     */           
/*  83 */           this.connection.setChunkedStreamingMode(this.chunkSize);
/*     */         } 
/*     */       } 
/*  86 */       SimpleBufferingClientHttpRequest.addHeaders(this.connection, headers);
/*  87 */       this.connection.connect();
/*  88 */       this.body = this.connection.getOutputStream();
/*     */     } 
/*  90 */     return StreamUtils.nonClosing(this.body);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ClientHttpResponse executeInternal(HttpHeaders headers) throws IOException {
/*     */     try {
/*  96 */       if (this.body != null) {
/*  97 */         this.body.close();
/*     */       } else {
/*     */         
/* 100 */         SimpleBufferingClientHttpRequest.addHeaders(this.connection, headers);
/* 101 */         this.connection.connect();
/*     */         
/* 103 */         this.connection.getResponseCode();
/*     */       }
/*     */     
/* 106 */     } catch (IOException iOException) {}
/*     */ 
/*     */     
/* 109 */     return new SimpleClientHttpResponse(this.connection);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/SimpleStreamingClientHttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */