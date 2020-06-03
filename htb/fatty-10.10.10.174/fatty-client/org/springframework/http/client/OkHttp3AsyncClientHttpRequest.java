/*     */ package org.springframework.http.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import okhttp3.Call;
/*     */ import okhttp3.Callback;
/*     */ import okhttp3.OkHttpClient;
/*     */ import okhttp3.Request;
/*     */ import okhttp3.Response;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.util.concurrent.ListenableFuture;
/*     */ import org.springframework.util.concurrent.SettableListenableFuture;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ class OkHttp3AsyncClientHttpRequest
/*     */   extends AbstractBufferingAsyncClientHttpRequest
/*     */ {
/*     */   private final OkHttpClient client;
/*     */   private final URI uri;
/*     */   private final HttpMethod method;
/*     */   
/*     */   public OkHttp3AsyncClientHttpRequest(OkHttpClient client, URI uri, HttpMethod method) {
/*  55 */     this.client = client;
/*  56 */     this.uri = uri;
/*  57 */     this.method = method;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpMethod getMethod() {
/*  63 */     return this.method;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMethodValue() {
/*  68 */     return this.method.name();
/*     */   }
/*     */ 
/*     */   
/*     */   public URI getURI() {
/*  73 */     return this.uri;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ListenableFuture<ClientHttpResponse> executeInternal(HttpHeaders headers, byte[] content) throws IOException {
/*  80 */     Request request = OkHttp3ClientHttpRequestFactory.buildRequest(headers, content, this.uri, this.method);
/*  81 */     return (ListenableFuture<ClientHttpResponse>)new OkHttpListenableFuture(this.client.newCall(request));
/*     */   }
/*     */   
/*     */   private static class OkHttpListenableFuture
/*     */     extends SettableListenableFuture<ClientHttpResponse>
/*     */   {
/*     */     private final Call call;
/*     */     
/*     */     public OkHttpListenableFuture(Call call) {
/*  90 */       this.call = call;
/*  91 */       this.call.enqueue(new Callback()
/*     */           {
/*     */             public void onResponse(Call call, Response response) {
/*  94 */               OkHttp3AsyncClientHttpRequest.OkHttpListenableFuture.this.set(new OkHttp3ClientHttpResponse(response));
/*     */             }
/*     */             
/*     */             public void onFailure(Call call, IOException ex) {
/*  98 */               OkHttp3AsyncClientHttpRequest.OkHttpListenableFuture.this.setException(ex);
/*     */             }
/*     */           });
/*     */     }
/*     */ 
/*     */     
/*     */     protected void interruptTask() {
/* 105 */       this.call.cancel();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/OkHttp3AsyncClientHttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */