/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.net.HttpURLConnection;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.StreamUtils;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ final class SimpleClientHttpResponse
/*    */   extends AbstractClientHttpResponse
/*    */ {
/*    */   private final HttpURLConnection connection;
/*    */   @Nullable
/*    */   private HttpHeaders headers;
/*    */   @Nullable
/*    */   private InputStream responseStream;
/*    */   
/*    */   SimpleClientHttpResponse(HttpURLConnection connection) {
/* 49 */     this.connection = connection;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getRawStatusCode() throws IOException {
/* 55 */     return this.connection.getResponseCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getStatusText() throws IOException {
/* 60 */     return this.connection.getResponseMessage();
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpHeaders getHeaders() {
/* 65 */     if (this.headers == null) {
/* 66 */       this.headers = new HttpHeaders();
/*    */       
/* 68 */       String name = this.connection.getHeaderFieldKey(0);
/* 69 */       if (StringUtils.hasLength(name)) {
/* 70 */         this.headers.add(name, this.connection.getHeaderField(0));
/*    */       }
/* 72 */       int i = 1;
/*    */       while (true) {
/* 74 */         name = this.connection.getHeaderFieldKey(i);
/* 75 */         if (!StringUtils.hasLength(name)) {
/*    */           break;
/*    */         }
/* 78 */         this.headers.add(name, this.connection.getHeaderField(i));
/* 79 */         i++;
/*    */       } 
/*    */     } 
/* 82 */     return this.headers;
/*    */   }
/*    */ 
/*    */   
/*    */   public InputStream getBody() throws IOException {
/* 87 */     InputStream errorStream = this.connection.getErrorStream();
/* 88 */     this.responseStream = (errorStream != null) ? errorStream : this.connection.getInputStream();
/* 89 */     return this.responseStream;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/*    */     try {
/* 95 */       if (this.responseStream == null) {
/* 96 */         getBody();
/*    */       }
/* 98 */       StreamUtils.drain(this.responseStream);
/* 99 */       this.responseStream.close();
/*    */     }
/* :1 */     catch (Exception exception) {}
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/SimpleClientHttpResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */