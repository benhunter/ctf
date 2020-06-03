/*     */ package org.springframework.http.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.util.List;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.client.HttpClient;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.entity.ByteArrayEntity;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.springframework.http.HttpHeaders;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class HttpComponentsClientHttpRequest
/*     */   extends AbstractBufferingClientHttpRequest
/*     */ {
/*     */   private final HttpClient httpClient;
/*     */   private final HttpUriRequest httpRequest;
/*     */   private final HttpContext httpContext;
/*     */   
/*     */   HttpComponentsClientHttpRequest(HttpClient client, HttpUriRequest request, HttpContext context) {
/*  57 */     this.httpClient = client;
/*  58 */     this.httpRequest = request;
/*  59 */     this.httpContext = context;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMethodValue() {
/*  65 */     return this.httpRequest.getMethod();
/*     */   }
/*     */ 
/*     */   
/*     */   public URI getURI() {
/*  70 */     return this.httpRequest.getURI();
/*     */   }
/*     */   
/*     */   HttpContext getHttpContext() {
/*  74 */     return this.httpContext;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected ClientHttpResponse executeInternal(HttpHeaders headers, byte[] bufferedOutput) throws IOException {
/*  80 */     addHeaders(this.httpRequest, headers);
/*     */     
/*  82 */     if (this.httpRequest instanceof HttpEntityEnclosingRequest) {
/*  83 */       HttpEntityEnclosingRequest entityEnclosingRequest = (HttpEntityEnclosingRequest)this.httpRequest;
/*  84 */       ByteArrayEntity byteArrayEntity = new ByteArrayEntity(bufferedOutput);
/*  85 */       entityEnclosingRequest.setEntity((HttpEntity)byteArrayEntity);
/*     */     } 
/*  87 */     HttpResponse httpResponse = this.httpClient.execute(this.httpRequest, this.httpContext);
/*  88 */     return new HttpComponentsClientHttpResponse(httpResponse);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void addHeaders(HttpUriRequest httpRequest, HttpHeaders headers) {
/*  98 */     headers.forEach((headerName, headerValues) -> {
/*     */           if ("Cookie".equalsIgnoreCase(headerName)) {
/*     */             String headerValue = StringUtils.collectionToDelimitedString(headerValues, "; ");
/*     */             httpRequest.addHeader(headerName, headerValue);
/*     */           } else if (!"Content-Length".equalsIgnoreCase(headerName) && !"Transfer-Encoding".equalsIgnoreCase(headerName)) {
/*     */             for (String headerValue : headerValues)
/*     */               httpRequest.addHeader(headerName, headerValue); 
/*     */           } 
/*     */         });
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/HttpComponentsClientHttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */