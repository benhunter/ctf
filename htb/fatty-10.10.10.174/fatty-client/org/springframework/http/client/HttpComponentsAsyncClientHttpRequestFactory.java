/*     */ package org.springframework.http.client;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import org.apache.http.client.HttpClient;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.methods.Configurable;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.client.protocol.HttpClientContext;
/*     */ import org.apache.http.impl.client.CloseableHttpClient;
/*     */ import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
/*     */ import org.apache.http.impl.nio.client.HttpAsyncClients;
/*     */ import org.apache.http.nio.client.HttpAsyncClient;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class HttpComponentsAsyncClientHttpRequestFactory
/*     */   extends HttpComponentsClientHttpRequestFactory
/*     */   implements AsyncClientHttpRequestFactory, InitializingBean
/*     */ {
/*     */   private HttpAsyncClient asyncClient;
/*     */   
/*     */   public HttpComponentsAsyncClientHttpRequestFactory() {
/*  62 */     this.asyncClient = (HttpAsyncClient)HttpAsyncClients.createSystem();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpComponentsAsyncClientHttpRequestFactory(HttpAsyncClient asyncClient) {
/*  73 */     this.asyncClient = asyncClient;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpComponentsAsyncClientHttpRequestFactory(CloseableHttpAsyncClient asyncClient) {
/*  83 */     this.asyncClient = (HttpAsyncClient)asyncClient;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpComponentsAsyncClientHttpRequestFactory(HttpClient httpClient, HttpAsyncClient asyncClient) {
/*  94 */     super(httpClient);
/*  95 */     this.asyncClient = asyncClient;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpComponentsAsyncClientHttpRequestFactory(CloseableHttpClient httpClient, CloseableHttpAsyncClient asyncClient) {
/* 107 */     super((HttpClient)httpClient);
/* 108 */     this.asyncClient = (HttpAsyncClient)asyncClient;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAsyncClient(HttpAsyncClient asyncClient) {
/* 119 */     Assert.notNull(asyncClient, "HttpAsyncClient must not be null");
/* 120 */     this.asyncClient = asyncClient;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpAsyncClient getAsyncClient() {
/* 130 */     return this.asyncClient;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setHttpAsyncClient(CloseableHttpAsyncClient asyncClient) {
/* 140 */     this.asyncClient = (HttpAsyncClient)asyncClient;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public CloseableHttpAsyncClient getHttpAsyncClient() {
/* 150 */     Assert.state(this.asyncClient instanceof CloseableHttpAsyncClient, "No CloseableHttpAsyncClient - use getAsyncClient() instead");
/*     */     
/* 152 */     return (CloseableHttpAsyncClient)this.asyncClient;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 158 */     startAsyncClient();
/*     */   }
/*     */   
/*     */   private HttpAsyncClient startAsyncClient() {
/* 162 */     HttpAsyncClient client = getAsyncClient();
/* 163 */     if (client instanceof CloseableHttpAsyncClient) {
/* 164 */       CloseableHttpAsyncClient closeableAsyncClient = (CloseableHttpAsyncClient)client;
/* 165 */       if (!closeableAsyncClient.isRunning()) {
/* 166 */         closeableAsyncClient.start();
/*     */       }
/*     */     } 
/* 169 */     return client;
/*     */   }
/*     */   
/*     */   public AsyncClientHttpRequest createAsyncRequest(URI uri, HttpMethod httpMethod) throws IOException {
/*     */     HttpClientContext httpClientContext;
/* 174 */     HttpAsyncClient client = startAsyncClient();
/*     */     
/* 176 */     HttpUriRequest httpRequest = createHttpUriRequest(httpMethod, uri);
/* 177 */     postProcessHttpRequest(httpRequest);
/* 178 */     HttpContext context = createHttpContext(httpMethod, uri);
/* 179 */     if (context == null) {
/* 180 */       httpClientContext = HttpClientContext.create();
/*     */     }
/*     */ 
/*     */     
/* 184 */     if (httpClientContext.getAttribute("http.request-config") == null) {
/*     */       
/* 186 */       RequestConfig config = null;
/* 187 */       if (httpRequest instanceof Configurable) {
/* 188 */         config = ((Configurable)httpRequest).getConfig();
/*     */       }
/* 190 */       if (config == null) {
/* 191 */         config = createRequestConfig(client);
/*     */       }
/* 193 */       if (config != null) {
/* 194 */         httpClientContext.setAttribute("http.request-config", config);
/*     */       }
/*     */     } 
/*     */     
/* 198 */     return new HttpComponentsAsyncClientHttpRequest(client, httpRequest, (HttpContext)httpClientContext);
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroy() throws Exception {
/*     */     try {
/* 204 */       super.destroy();
/*     */     } finally {
/*     */       
/* 207 */       HttpAsyncClient asyncClient = getAsyncClient();
/* 208 */       if (asyncClient instanceof Closeable)
/* 209 */         ((Closeable)asyncClient).close(); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/HttpComponentsAsyncClientHttpRequestFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */