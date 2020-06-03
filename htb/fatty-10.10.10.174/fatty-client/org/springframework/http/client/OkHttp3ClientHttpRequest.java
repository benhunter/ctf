/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URI;
/*    */ import okhttp3.OkHttpClient;
/*    */ import okhttp3.Request;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.http.HttpMethod;
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
/*    */ class OkHttp3ClientHttpRequest
/*    */   extends AbstractBufferingClientHttpRequest
/*    */ {
/*    */   private final OkHttpClient client;
/*    */   private final URI uri;
/*    */   private final HttpMethod method;
/*    */   
/*    */   public OkHttp3ClientHttpRequest(OkHttpClient client, URI uri, HttpMethod method) {
/* 48 */     this.client = client;
/* 49 */     this.uri = uri;
/* 50 */     this.method = method;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpMethod getMethod() {
/* 56 */     return this.method;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMethodValue() {
/* 61 */     return this.method.name();
/*    */   }
/*    */ 
/*    */   
/*    */   public URI getURI() {
/* 66 */     return this.uri;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected ClientHttpResponse executeInternal(HttpHeaders headers, byte[] content) throws IOException {
/* 72 */     Request request = OkHttp3ClientHttpRequestFactory.buildRequest(headers, content, this.uri, this.method);
/* 73 */     return new OkHttp3ClientHttpResponse(this.client.newCall(request).execute());
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/OkHttp3ClientHttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */