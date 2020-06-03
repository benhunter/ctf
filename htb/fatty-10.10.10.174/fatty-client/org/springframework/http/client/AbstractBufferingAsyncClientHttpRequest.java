/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import org.springframework.http.HttpHeaders;
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
/*    */ abstract class AbstractBufferingAsyncClientHttpRequest
/*    */   extends AbstractAsyncClientHttpRequest
/*    */ {
/* 37 */   private ByteArrayOutputStream bufferedOutput = new ByteArrayOutputStream(1024);
/*    */ 
/*    */ 
/*    */   
/*    */   protected OutputStream getBodyInternal(HttpHeaders headers) throws IOException {
/* 42 */     return this.bufferedOutput;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ListenableFuture<ClientHttpResponse> executeInternal(HttpHeaders headers) throws IOException {
/* 47 */     byte[] bytes = this.bufferedOutput.toByteArray();
/* 48 */     if (headers.getContentLength() < 0L) {
/* 49 */       headers.setContentLength(bytes.length);
/*    */     }
/* 51 */     ListenableFuture<ClientHttpResponse> result = executeInternal(headers, bytes);
/* 52 */     this.bufferedOutput = new ByteArrayOutputStream(0);
/* 53 */     return result;
/*    */   }
/*    */   
/*    */   protected abstract ListenableFuture<ClientHttpResponse> executeInternal(HttpHeaders paramHttpHeaders, byte[] paramArrayOfbyte) throws IOException;
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/AbstractBufferingAsyncClientHttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */