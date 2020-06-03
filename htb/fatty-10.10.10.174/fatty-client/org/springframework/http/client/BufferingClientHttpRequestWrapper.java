/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URI;
/*    */ import java.util.Map;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.http.HttpMethod;
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
/*    */ final class BufferingClientHttpRequestWrapper
/*    */   extends AbstractBufferingClientHttpRequest
/*    */ {
/*    */   private final ClientHttpRequest request;
/*    */   
/*    */   BufferingClientHttpRequestWrapper(ClientHttpRequest request) {
/* 39 */     this.request = request;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public HttpMethod getMethod() {
/* 46 */     return this.request.getMethod();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMethodValue() {
/* 51 */     return this.request.getMethodValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public URI getURI() {
/* 56 */     return this.request.getURI();
/*    */   }
/*    */ 
/*    */   
/*    */   protected ClientHttpResponse executeInternal(HttpHeaders headers, byte[] bufferedOutput) throws IOException {
/* 61 */     this.request.getHeaders().putAll((Map)headers);
/* 62 */     StreamUtils.copy(bufferedOutput, this.request.getBody());
/* 63 */     ClientHttpResponse response = this.request.execute();
/* 64 */     return new BufferingClientHttpResponseWrapper(response);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/BufferingClientHttpRequestWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */