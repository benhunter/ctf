/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.http.HttpStatus;
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
/*    */ final class BufferingClientHttpResponseWrapper
/*    */   implements ClientHttpResponse
/*    */ {
/*    */   private final ClientHttpResponse response;
/*    */   @Nullable
/*    */   private byte[] body;
/*    */   
/*    */   BufferingClientHttpResponseWrapper(ClientHttpResponse response) {
/* 44 */     this.response = response;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpStatus getStatusCode() throws IOException {
/* 50 */     return this.response.getStatusCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public int getRawStatusCode() throws IOException {
/* 55 */     return this.response.getRawStatusCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getStatusText() throws IOException {
/* 60 */     return this.response.getStatusText();
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpHeaders getHeaders() {
/* 65 */     return this.response.getHeaders();
/*    */   }
/*    */ 
/*    */   
/*    */   public InputStream getBody() throws IOException {
/* 70 */     if (this.body == null) {
/* 71 */       this.body = StreamUtils.copyToByteArray(this.response.getBody());
/*    */     }
/* 73 */     return new ByteArrayInputStream(this.body);
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 78 */     this.response.close();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/BufferingClientHttpResponseWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */