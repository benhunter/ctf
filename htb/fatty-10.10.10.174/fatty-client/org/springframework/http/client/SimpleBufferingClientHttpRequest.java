/*     */ package org.springframework.http.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.List;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.util.FileCopyUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ final class SimpleBufferingClientHttpRequest
/*     */   extends AbstractBufferingClientHttpRequest
/*     */ {
/*     */   private final HttpURLConnection connection;
/*     */   private final boolean outputStreaming;
/*     */   
/*     */   SimpleBufferingClientHttpRequest(HttpURLConnection connection, boolean outputStreaming) {
/*  46 */     this.connection = connection;
/*  47 */     this.outputStreaming = outputStreaming;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMethodValue() {
/*  53 */     return this.connection.getRequestMethod();
/*     */   }
/*     */ 
/*     */   
/*     */   public URI getURI() {
/*     */     try {
/*  59 */       return this.connection.getURL().toURI();
/*     */     }
/*  61 */     catch (URISyntaxException ex) {
/*  62 */       throw new IllegalStateException("Could not get HttpURLConnection URI: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected ClientHttpResponse executeInternal(HttpHeaders headers, byte[] bufferedOutput) throws IOException {
/*  68 */     addHeaders(this.connection, headers);
/*     */     
/*  70 */     if (getMethod() == HttpMethod.DELETE && bufferedOutput.length == 0) {
/*  71 */       this.connection.setDoOutput(false);
/*     */     }
/*  73 */     if (this.connection.getDoOutput() && this.outputStreaming) {
/*  74 */       this.connection.setFixedLengthStreamingMode(bufferedOutput.length);
/*     */     }
/*  76 */     this.connection.connect();
/*  77 */     if (this.connection.getDoOutput()) {
/*  78 */       FileCopyUtils.copy(bufferedOutput, this.connection.getOutputStream());
/*     */     }
/*     */     else {
/*     */       
/*  82 */       this.connection.getResponseCode();
/*     */     } 
/*  84 */     return new SimpleClientHttpResponse(this.connection);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void addHeaders(HttpURLConnection connection, HttpHeaders headers) {
/*  94 */     headers.forEach((headerName, headerValues) -> {
/*     */           if ("Cookie".equalsIgnoreCase(headerName)) {
/*     */             String headerValue = StringUtils.collectionToDelimitedString(headerValues, "; ");
/*     */             connection.setRequestProperty(headerName, headerValue);
/*     */           } else {
/*     */             for (String headerValue : headerValues) {
/*     */               String actualHeaderValue = (headerValue != null) ? headerValue : "";
/*     */               connection.addRequestProperty(headerName, actualHeaderValue);
/*     */             } 
/*     */           } 
/*     */         });
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/SimpleBufferingClientHttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */