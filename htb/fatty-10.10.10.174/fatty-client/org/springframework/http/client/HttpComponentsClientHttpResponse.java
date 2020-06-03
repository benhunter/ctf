/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import java.io.Closeable;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.apache.http.Header;
/*    */ import org.apache.http.HttpEntity;
/*    */ import org.apache.http.HttpResponse;
/*    */ import org.apache.http.util.EntityUtils;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.StreamUtils;
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
/*    */ final class HttpComponentsClientHttpResponse
/*    */   extends AbstractClientHttpResponse
/*    */ {
/*    */   private final HttpResponse httpResponse;
/*    */   @Nullable
/*    */   private HttpHeaders headers;
/*    */   
/*    */   HttpComponentsClientHttpResponse(HttpResponse httpResponse) {
/* 52 */     this.httpResponse = httpResponse;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getRawStatusCode() throws IOException {
/* 58 */     return this.httpResponse.getStatusLine().getStatusCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getStatusText() throws IOException {
/* 63 */     return this.httpResponse.getStatusLine().getReasonPhrase();
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpHeaders getHeaders() {
/* 68 */     if (this.headers == null) {
/* 69 */       this.headers = new HttpHeaders();
/* 70 */       for (Header header : this.httpResponse.getAllHeaders()) {
/* 71 */         this.headers.add(header.getName(), header.getValue());
/*    */       }
/*    */     } 
/* 74 */     return this.headers;
/*    */   }
/*    */ 
/*    */   
/*    */   public InputStream getBody() throws IOException {
/* 79 */     HttpEntity entity = this.httpResponse.getEntity();
/* 80 */     return (entity != null) ? entity.getContent() : StreamUtils.emptyInput();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() {
/*    */     try {
/*    */       try {
/* 89 */         EntityUtils.consume(this.httpResponse.getEntity());
/*    */       } finally {
/*    */         
/* 92 */         if (this.httpResponse instanceof Closeable) {
/* 93 */           ((Closeable)this.httpResponse).close();
/*    */         }
/*    */       }
/*    */     
/* 97 */     } catch (IOException iOException) {}
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/HttpComponentsClientHttpResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */