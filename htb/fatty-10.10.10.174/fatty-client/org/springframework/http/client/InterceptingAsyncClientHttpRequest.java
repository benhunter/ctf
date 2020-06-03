/*     */ package org.springframework.http.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.HttpRequest;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StreamUtils;
/*     */ import org.springframework.util.concurrent.ListenableFuture;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ class InterceptingAsyncClientHttpRequest
/*     */   extends AbstractBufferingAsyncClientHttpRequest
/*     */ {
/*     */   private AsyncClientHttpRequestFactory requestFactory;
/*     */   private List<AsyncClientHttpRequestInterceptor> interceptors;
/*     */   private URI uri;
/*     */   private HttpMethod httpMethod;
/*     */   
/*     */   public InterceptingAsyncClientHttpRequest(AsyncClientHttpRequestFactory requestFactory, List<AsyncClientHttpRequestInterceptor> interceptors, URI uri, HttpMethod httpMethod) {
/*  62 */     this.requestFactory = requestFactory;
/*  63 */     this.interceptors = interceptors;
/*  64 */     this.uri = uri;
/*  65 */     this.httpMethod = httpMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ListenableFuture<ClientHttpResponse> executeInternal(HttpHeaders headers, byte[] body) throws IOException {
/*  73 */     return (new AsyncRequestExecution()).executeAsync(this, body);
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpMethod getMethod() {
/*  78 */     return this.httpMethod;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMethodValue() {
/*  83 */     return this.httpMethod.name();
/*     */   }
/*     */ 
/*     */   
/*     */   public URI getURI() {
/*  88 */     return this.uri;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class AsyncRequestExecution
/*     */     implements AsyncClientHttpRequestExecution
/*     */   {
/*  97 */     private Iterator<AsyncClientHttpRequestInterceptor> iterator = InterceptingAsyncClientHttpRequest.this.interceptors.iterator();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ListenableFuture<ClientHttpResponse> executeAsync(HttpRequest request, byte[] body) throws IOException {
/* 104 */       if (this.iterator.hasNext()) {
/* 105 */         AsyncClientHttpRequestInterceptor interceptor = this.iterator.next();
/* 106 */         return interceptor.intercept(request, body, this);
/*     */       } 
/*     */       
/* 109 */       URI uri = request.getURI();
/* 110 */       HttpMethod method = request.getMethod();
/* 111 */       HttpHeaders headers = request.getHeaders();
/*     */       
/* 113 */       Assert.state((method != null), "No standard HTTP method");
/* 114 */       AsyncClientHttpRequest delegate = InterceptingAsyncClientHttpRequest.this.requestFactory.createAsyncRequest(uri, method);
/* 115 */       delegate.getHeaders().putAll((Map)headers);
/* 116 */       if (body.length > 0) {
/* 117 */         StreamUtils.copy(body, delegate.getBody());
/*     */       }
/*     */       
/* 120 */       return delegate.executeAsync();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/InterceptingAsyncClientHttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */