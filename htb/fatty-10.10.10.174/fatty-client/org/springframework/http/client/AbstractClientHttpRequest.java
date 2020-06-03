/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.util.Assert;
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
/*    */ public abstract class AbstractClientHttpRequest
/*    */   implements ClientHttpRequest
/*    */ {
/* 34 */   private final HttpHeaders headers = new HttpHeaders();
/*    */ 
/*    */   
/*    */   private boolean executed = false;
/*    */ 
/*    */   
/*    */   public final HttpHeaders getHeaders() {
/* 41 */     return this.executed ? HttpHeaders.readOnlyHttpHeaders(this.headers) : this.headers;
/*    */   }
/*    */ 
/*    */   
/*    */   public final OutputStream getBody() throws IOException {
/* 46 */     assertNotExecuted();
/* 47 */     return getBodyInternal(this.headers);
/*    */   }
/*    */ 
/*    */   
/*    */   public final ClientHttpResponse execute() throws IOException {
/* 52 */     assertNotExecuted();
/* 53 */     ClientHttpResponse result = executeInternal(this.headers);
/* 54 */     this.executed = true;
/* 55 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void assertNotExecuted() {
/* 63 */     Assert.state(!this.executed, "ClientHttpRequest already executed");
/*    */   }
/*    */   
/*    */   protected abstract OutputStream getBodyInternal(HttpHeaders paramHttpHeaders) throws IOException;
/*    */   
/*    */   protected abstract ClientHttpResponse executeInternal(HttpHeaders paramHttpHeaders) throws IOException;
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/AbstractClientHttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */