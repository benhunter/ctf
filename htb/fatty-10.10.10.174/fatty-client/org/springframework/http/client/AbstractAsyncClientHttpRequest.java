/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.concurrent.ListenableFuture;
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
/*    */ abstract class AbstractAsyncClientHttpRequest
/*    */   implements AsyncClientHttpRequest
/*    */ {
/* 37 */   private final HttpHeaders headers = new HttpHeaders();
/*    */ 
/*    */   
/*    */   private boolean executed = false;
/*    */ 
/*    */   
/*    */   public final HttpHeaders getHeaders() {
/* 44 */     return this.executed ? HttpHeaders.readOnlyHttpHeaders(this.headers) : this.headers;
/*    */   }
/*    */ 
/*    */   
/*    */   public final OutputStream getBody() throws IOException {
/* 49 */     assertNotExecuted();
/* 50 */     return getBodyInternal(this.headers);
/*    */   }
/*    */ 
/*    */   
/*    */   public ListenableFuture<ClientHttpResponse> executeAsync() throws IOException {
/* 55 */     assertNotExecuted();
/* 56 */     ListenableFuture<ClientHttpResponse> result = executeInternal(this.headers);
/* 57 */     this.executed = true;
/* 58 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void assertNotExecuted() {
/* 66 */     Assert.state(!this.executed, "ClientHttpRequest already executed");
/*    */   }
/*    */   
/*    */   protected abstract OutputStream getBodyInternal(HttpHeaders paramHttpHeaders) throws IOException;
/*    */   
/*    */   protected abstract ListenableFuture<ClientHttpResponse> executeInternal(HttpHeaders paramHttpHeaders) throws IOException;
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/AbstractAsyncClientHttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */