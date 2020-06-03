/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.apache.http.Header;
/*    */ import org.apache.http.HttpEntity;
/*    */ import org.apache.http.HttpResponse;
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
/*    */ 
/*    */ @Deprecated
/*    */ final class HttpComponentsAsyncClientHttpResponse
/*    */   extends AbstractClientHttpResponse
/*    */ {
/*    */   private final HttpResponse httpResponse;
/*    */   @Nullable
/*    */   private HttpHeaders headers;
/*    */   
/*    */   HttpComponentsAsyncClientHttpResponse(HttpResponse httpResponse) {
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
/*    */   public void close() {}
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/HttpComponentsAsyncClientHttpResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */