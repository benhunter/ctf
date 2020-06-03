/*     */ package org.springframework.http.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.concurrent.FutureCallback;
/*     */ import org.apache.http.nio.client.HttpAsyncClient;
/*     */ import org.apache.http.nio.entity.NByteArrayEntity;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.util.concurrent.FailureCallback;
/*     */ import org.springframework.util.concurrent.FutureAdapter;
/*     */ import org.springframework.util.concurrent.ListenableFuture;
/*     */ import org.springframework.util.concurrent.ListenableFutureCallback;
/*     */ import org.springframework.util.concurrent.ListenableFutureCallbackRegistry;
/*     */ import org.springframework.util.concurrent.SuccessCallback;
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
/*     */ 
/*     */ @Deprecated
/*     */ final class HttpComponentsAsyncClientHttpRequest
/*     */   extends AbstractBufferingAsyncClientHttpRequest
/*     */ {
/*     */   private final HttpAsyncClient httpClient;
/*     */   private final HttpUriRequest httpRequest;
/*     */   private final HttpContext httpContext;
/*     */   
/*     */   HttpComponentsAsyncClientHttpRequest(HttpAsyncClient client, HttpUriRequest request, HttpContext context) {
/*  64 */     this.httpClient = client;
/*  65 */     this.httpRequest = request;
/*  66 */     this.httpContext = context;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMethodValue() {
/*  72 */     return this.httpRequest.getMethod();
/*     */   }
/*     */ 
/*     */   
/*     */   public URI getURI() {
/*  77 */     return this.httpRequest.getURI();
/*     */   }
/*     */   
/*     */   HttpContext getHttpContext() {
/*  81 */     return this.httpContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ListenableFuture<ClientHttpResponse> executeInternal(HttpHeaders headers, byte[] bufferedOutput) throws IOException {
/*  88 */     HttpComponentsClientHttpRequest.addHeaders(this.httpRequest, headers);
/*     */     
/*  90 */     if (this.httpRequest instanceof HttpEntityEnclosingRequest) {
/*  91 */       HttpEntityEnclosingRequest entityEnclosingRequest = (HttpEntityEnclosingRequest)this.httpRequest;
/*  92 */       NByteArrayEntity nByteArrayEntity = new NByteArrayEntity(bufferedOutput);
/*  93 */       entityEnclosingRequest.setEntity((HttpEntity)nByteArrayEntity);
/*     */     } 
/*     */     
/*  96 */     HttpResponseFutureCallback callback = new HttpResponseFutureCallback(this.httpRequest);
/*  97 */     Future<HttpResponse> futureResponse = this.httpClient.execute(this.httpRequest, this.httpContext, callback);
/*  98 */     return new ClientHttpResponseFuture(futureResponse, callback);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class HttpResponseFutureCallback
/*     */     implements FutureCallback<HttpResponse>
/*     */   {
/*     */     private final HttpUriRequest request;
/* 106 */     private final ListenableFutureCallbackRegistry<ClientHttpResponse> callbacks = new ListenableFutureCallbackRegistry();
/*     */ 
/*     */     
/*     */     public HttpResponseFutureCallback(HttpUriRequest request) {
/* 110 */       this.request = request;
/*     */     }
/*     */     
/*     */     public void addCallback(ListenableFutureCallback<? super ClientHttpResponse> callback) {
/* 114 */       this.callbacks.addCallback(callback);
/*     */     }
/*     */     
/*     */     public void addSuccessCallback(SuccessCallback<? super ClientHttpResponse> callback) {
/* 118 */       this.callbacks.addSuccessCallback(callback);
/*     */     }
/*     */     
/*     */     public void addFailureCallback(FailureCallback callback) {
/* 122 */       this.callbacks.addFailureCallback(callback);
/*     */     }
/*     */ 
/*     */     
/*     */     public void completed(HttpResponse result) {
/* 127 */       this.callbacks.success(new HttpComponentsAsyncClientHttpResponse(result));
/*     */     }
/*     */ 
/*     */     
/*     */     public void failed(Exception ex) {
/* 132 */       this.callbacks.failure(ex);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancelled() {
/* 137 */       this.request.abort();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ClientHttpResponseFuture
/*     */     extends FutureAdapter<ClientHttpResponse, HttpResponse>
/*     */     implements ListenableFuture<ClientHttpResponse>
/*     */   {
/*     */     private final HttpComponentsAsyncClientHttpRequest.HttpResponseFutureCallback callback;
/*     */     
/*     */     public ClientHttpResponseFuture(Future<HttpResponse> response, HttpComponentsAsyncClientHttpRequest.HttpResponseFutureCallback callback) {
/* 148 */       super(response);
/* 149 */       this.callback = callback;
/*     */     }
/*     */ 
/*     */     
/*     */     protected ClientHttpResponse adapt(HttpResponse response) {
/* 154 */       return new HttpComponentsAsyncClientHttpResponse(response);
/*     */     }
/*     */ 
/*     */     
/*     */     public void addCallback(ListenableFutureCallback<? super ClientHttpResponse> callback) {
/* 159 */       this.callback.addCallback(callback);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void addCallback(SuccessCallback<? super ClientHttpResponse> successCallback, FailureCallback failureCallback) {
/* 166 */       this.callback.addSuccessCallback(successCallback);
/* 167 */       this.callback.addFailureCallback(failureCallback);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/HttpComponentsAsyncClientHttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */