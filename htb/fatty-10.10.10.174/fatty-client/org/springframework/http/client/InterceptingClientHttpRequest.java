/*     */ package org.springframework.http.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URI;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.HttpRequest;
/*     */ import org.springframework.http.StreamingHttpOutputMessage;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StreamUtils;
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
/*     */ class InterceptingClientHttpRequest
/*     */   extends AbstractBufferingClientHttpRequest
/*     */ {
/*     */   private final ClientHttpRequestFactory requestFactory;
/*     */   private final List<ClientHttpRequestInterceptor> interceptors;
/*     */   private HttpMethod method;
/*     */   private URI uri;
/*     */   
/*     */   protected InterceptingClientHttpRequest(ClientHttpRequestFactory requestFactory, List<ClientHttpRequestInterceptor> interceptors, URI uri, HttpMethod method) {
/*  51 */     this.requestFactory = requestFactory;
/*  52 */     this.interceptors = interceptors;
/*  53 */     this.method = method;
/*  54 */     this.uri = uri;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpMethod getMethod() {
/*  60 */     return this.method;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMethodValue() {
/*  65 */     return this.method.name();
/*     */   }
/*     */ 
/*     */   
/*     */   public URI getURI() {
/*  70 */     return this.uri;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final ClientHttpResponse executeInternal(HttpHeaders headers, byte[] bufferedOutput) throws IOException {
/*  75 */     InterceptingRequestExecution requestExecution = new InterceptingRequestExecution();
/*  76 */     return requestExecution.execute(this, bufferedOutput);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class InterceptingRequestExecution
/*     */     implements ClientHttpRequestExecution
/*     */   {
/*  85 */     private final Iterator<ClientHttpRequestInterceptor> iterator = InterceptingClientHttpRequest.this.interceptors.iterator();
/*     */ 
/*     */ 
/*     */     
/*     */     public ClientHttpResponse execute(HttpRequest request, byte[] body) throws IOException {
/*  90 */       if (this.iterator.hasNext()) {
/*  91 */         ClientHttpRequestInterceptor nextInterceptor = this.iterator.next();
/*  92 */         return nextInterceptor.intercept(request, body, this);
/*     */       } 
/*     */       
/*  95 */       HttpMethod method = request.getMethod();
/*  96 */       Assert.state((method != null), "No standard HTTP method");
/*  97 */       ClientHttpRequest delegate = InterceptingClientHttpRequest.this.requestFactory.createRequest(request.getURI(), method);
/*  98 */       request.getHeaders().forEach((key, value) -> delegate.getHeaders().addAll(key, value));
/*  99 */       if (body.length > 0) {
/* 100 */         if (delegate instanceof StreamingHttpOutputMessage) {
/* 101 */           StreamingHttpOutputMessage streamingOutputMessage = (StreamingHttpOutputMessage)delegate;
/* 102 */           streamingOutputMessage.setBody(outputStream -> StreamUtils.copy(body, outputStream));
/*     */         } else {
/*     */           
/* 105 */           StreamUtils.copy(body, delegate.getBody());
/*     */         } 
/*     */       }
/* 108 */       return delegate.execute();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/InterceptingClientHttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */