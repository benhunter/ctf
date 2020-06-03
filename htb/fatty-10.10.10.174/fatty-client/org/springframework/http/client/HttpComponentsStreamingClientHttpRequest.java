/*     */ package org.springframework.http.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URI;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.client.HttpClient;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.message.BasicHeader;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.StreamingHttpOutputMessage;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ final class HttpComponentsStreamingClientHttpRequest
/*     */   extends AbstractClientHttpRequest
/*     */   implements StreamingHttpOutputMessage
/*     */ {
/*     */   private final HttpClient httpClient;
/*     */   private final HttpUriRequest httpRequest;
/*     */   private final HttpContext httpContext;
/*     */   @Nullable
/*     */   private StreamingHttpOutputMessage.Body body;
/*     */   
/*     */   HttpComponentsStreamingClientHttpRequest(HttpClient client, HttpUriRequest request, HttpContext context) {
/*  62 */     this.httpClient = client;
/*  63 */     this.httpRequest = request;
/*  64 */     this.httpContext = context;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMethodValue() {
/*  70 */     return this.httpRequest.getMethod();
/*     */   }
/*     */ 
/*     */   
/*     */   public URI getURI() {
/*  75 */     return this.httpRequest.getURI();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBody(StreamingHttpOutputMessage.Body body) {
/*  80 */     assertNotExecuted();
/*  81 */     this.body = body;
/*     */   }
/*     */ 
/*     */   
/*     */   protected OutputStream getBodyInternal(HttpHeaders headers) throws IOException {
/*  86 */     throw new UnsupportedOperationException("getBody not supported");
/*     */   }
/*     */ 
/*     */   
/*     */   protected ClientHttpResponse executeInternal(HttpHeaders headers) throws IOException {
/*  91 */     HttpComponentsClientHttpRequest.addHeaders(this.httpRequest, headers);
/*     */     
/*  93 */     if (this.httpRequest instanceof HttpEntityEnclosingRequest && this.body != null) {
/*  94 */       HttpEntityEnclosingRequest entityEnclosingRequest = (HttpEntityEnclosingRequest)this.httpRequest;
/*  95 */       HttpEntity requestEntity = new StreamingHttpEntity(getHeaders(), this.body);
/*  96 */       entityEnclosingRequest.setEntity(requestEntity);
/*     */     } 
/*     */     
/*  99 */     HttpResponse httpResponse = this.httpClient.execute(this.httpRequest, this.httpContext);
/* 100 */     return new HttpComponentsClientHttpResponse(httpResponse);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class StreamingHttpEntity
/*     */     implements HttpEntity
/*     */   {
/*     */     private final HttpHeaders headers;
/*     */     private final StreamingHttpOutputMessage.Body body;
/*     */     
/*     */     public StreamingHttpEntity(HttpHeaders headers, StreamingHttpOutputMessage.Body body) {
/* 111 */       this.headers = headers;
/* 112 */       this.body = body;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isRepeatable() {
/* 117 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isChunked() {
/* 122 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public long getContentLength() {
/* 127 */       return this.headers.getContentLength();
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Header getContentType() {
/* 133 */       MediaType contentType = this.headers.getContentType();
/* 134 */       return (contentType != null) ? (Header)new BasicHeader("Content-Type", contentType.toString()) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Header getContentEncoding() {
/* 140 */       String contentEncoding = this.headers.getFirst("Content-Encoding");
/* 141 */       return (contentEncoding != null) ? (Header)new BasicHeader("Content-Encoding", contentEncoding) : null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public InputStream getContent() throws IOException, IllegalStateException {
/* 147 */       throw new IllegalStateException("No content available");
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeTo(OutputStream outputStream) throws IOException {
/* 152 */       this.body.writeTo(outputStream);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isStreaming() {
/* 157 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public void consumeContent() throws IOException {
/* 163 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/HttpComponentsStreamingClientHttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */