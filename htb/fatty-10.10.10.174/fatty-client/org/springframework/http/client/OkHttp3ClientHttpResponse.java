/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import okhttp3.Response;
/*    */ import okhttp3.ResponseBody;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
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
/*    */ class OkHttp3ClientHttpResponse
/*    */   extends AbstractClientHttpResponse
/*    */ {
/*    */   private final Response response;
/*    */   @Nullable
/*    */   private volatile HttpHeaders headers;
/*    */   
/*    */   public OkHttp3ClientHttpResponse(Response response) {
/* 47 */     Assert.notNull(response, "Response must not be null");
/* 48 */     this.response = response;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getRawStatusCode() {
/* 54 */     return this.response.code();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getStatusText() {
/* 59 */     return this.response.message();
/*    */   }
/*    */ 
/*    */   
/*    */   public InputStream getBody() throws IOException {
/* 64 */     ResponseBody body = this.response.body();
/* 65 */     return (body != null) ? body.byteStream() : StreamUtils.emptyInput();
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpHeaders getHeaders() {
/* 70 */     HttpHeaders headers = this.headers;
/* 71 */     if (headers == null) {
/* 72 */       headers = new HttpHeaders();
/* 73 */       for (String headerName : this.response.headers().names()) {
/* 74 */         for (String headerValue : this.response.headers(headerName)) {
/* 75 */           headers.add(headerName, headerValue);
/*    */         }
/*    */       } 
/* 78 */       this.headers = headers;
/*    */     } 
/* 80 */     return headers;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 85 */     ResponseBody body = this.response.body();
/* 86 */     if (body != null)
/* 87 */       body.close(); 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/OkHttp3ClientHttpResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */