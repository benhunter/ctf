/*     */ package org.springframework.http.client;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import org.apache.http.client.HttpClient;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.methods.Configurable;
/*     */ import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
/*     */ import org.apache.http.client.methods.HttpGet;
/*     */ import org.apache.http.client.methods.HttpHead;
/*     */ import org.apache.http.client.methods.HttpOptions;
/*     */ import org.apache.http.client.methods.HttpPatch;
/*     */ import org.apache.http.client.methods.HttpPost;
/*     */ import org.apache.http.client.methods.HttpPut;
/*     */ import org.apache.http.client.methods.HttpTrace;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.client.protocol.HttpClientContext;
/*     */ import org.apache.http.impl.client.HttpClients;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpComponentsClientHttpRequestFactory
/*     */   implements ClientHttpRequestFactory, DisposableBean
/*     */ {
/*     */   private HttpClient httpClient;
/*     */   @Nullable
/*     */   private RequestConfig requestConfig;
/*     */   private boolean bufferRequestBody = true;
/*     */   
/*     */   public HttpComponentsClientHttpRequestFactory() {
/*  75 */     this.httpClient = (HttpClient)HttpClients.createSystem();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpComponentsClientHttpRequestFactory(HttpClient httpClient) {
/*  84 */     this.httpClient = httpClient;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHttpClient(HttpClient httpClient) {
/*  93 */     Assert.notNull(httpClient, "HttpClient must not be null");
/*  94 */     this.httpClient = httpClient;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpClient getHttpClient() {
/* 102 */     return this.httpClient;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConnectTimeout(int timeout) {
/* 119 */     Assert.isTrue((timeout >= 0), "Timeout must be a non-negative value");
/* 120 */     this.requestConfig = requestConfigBuilder().setConnectTimeout(timeout).build();
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
/*     */   
/*     */   public void setConnectionRequestTimeout(int connectionRequestTimeout) {
/* 133 */     this
/* 134 */       .requestConfig = requestConfigBuilder().setConnectionRequestTimeout(connectionRequestTimeout).build();
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
/*     */   public void setReadTimeout(int timeout) {
/* 146 */     Assert.isTrue((timeout >= 0), "Timeout must be a non-negative value");
/* 147 */     this.requestConfig = requestConfigBuilder().setSocketTimeout(timeout).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBufferRequestBody(boolean bufferRequestBody) {
/* 157 */     this.bufferRequestBody = bufferRequestBody;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
/*     */     HttpClientContext httpClientContext;
/* 163 */     HttpClient client = getHttpClient();
/*     */     
/* 165 */     HttpUriRequest httpRequest = createHttpUriRequest(httpMethod, uri);
/* 166 */     postProcessHttpRequest(httpRequest);
/* 167 */     HttpContext context = createHttpContext(httpMethod, uri);
/* 168 */     if (context == null) {
/* 169 */       httpClientContext = HttpClientContext.create();
/*     */     }
/*     */ 
/*     */     
/* 173 */     if (httpClientContext.getAttribute("http.request-config") == null) {
/*     */       
/* 175 */       RequestConfig config = null;
/* 176 */       if (httpRequest instanceof Configurable) {
/* 177 */         config = ((Configurable)httpRequest).getConfig();
/*     */       }
/* 179 */       if (config == null) {
/* 180 */         config = createRequestConfig(client);
/*     */       }
/* 182 */       if (config != null) {
/* 183 */         httpClientContext.setAttribute("http.request-config", config);
/*     */       }
/*     */     } 
/*     */     
/* 187 */     if (this.bufferRequestBody) {
/* 188 */       return new HttpComponentsClientHttpRequest(client, httpRequest, (HttpContext)httpClientContext);
/*     */     }
/*     */     
/* 191 */     return new HttpComponentsStreamingClientHttpRequest(client, httpRequest, (HttpContext)httpClientContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private RequestConfig.Builder requestConfigBuilder() {
/* 201 */     return (this.requestConfig != null) ? RequestConfig.copy(this.requestConfig) : RequestConfig.custom();
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
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected RequestConfig createRequestConfig(Object client) {
/* 217 */     if (client instanceof Configurable) {
/* 218 */       RequestConfig clientRequestConfig = ((Configurable)client).getConfig();
/* 219 */       return mergeRequestConfig(clientRequestConfig);
/*     */     } 
/* 221 */     return this.requestConfig;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected RequestConfig mergeRequestConfig(RequestConfig clientConfig) {
/* 232 */     if (this.requestConfig == null) {
/* 233 */       return clientConfig;
/*     */     }
/*     */     
/* 236 */     RequestConfig.Builder builder = RequestConfig.copy(clientConfig);
/* 237 */     int connectTimeout = this.requestConfig.getConnectTimeout();
/* 238 */     if (connectTimeout >= 0) {
/* 239 */       builder.setConnectTimeout(connectTimeout);
/*     */     }
/* 241 */     int connectionRequestTimeout = this.requestConfig.getConnectionRequestTimeout();
/* 242 */     if (connectionRequestTimeout >= 0) {
/* 243 */       builder.setConnectionRequestTimeout(connectionRequestTimeout);
/*     */     }
/* 245 */     int socketTimeout = this.requestConfig.getSocketTimeout();
/* 246 */     if (socketTimeout >= 0) {
/* 247 */       builder.setSocketTimeout(socketTimeout);
/*     */     }
/* 249 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpUriRequest createHttpUriRequest(HttpMethod httpMethod, URI uri) {
/* 259 */     switch (httpMethod) {
/*     */       case GET:
/* 261 */         return (HttpUriRequest)new HttpGet(uri);
/*     */       case HEAD:
/* 263 */         return (HttpUriRequest)new HttpHead(uri);
/*     */       case POST:
/* 265 */         return (HttpUriRequest)new HttpPost(uri);
/*     */       case PUT:
/* 267 */         return (HttpUriRequest)new HttpPut(uri);
/*     */       case PATCH:
/* 269 */         return (HttpUriRequest)new HttpPatch(uri);
/*     */       case DELETE:
/* 271 */         return (HttpUriRequest)new HttpDelete(uri);
/*     */       case OPTIONS:
/* 273 */         return (HttpUriRequest)new HttpOptions(uri);
/*     */       case TRACE:
/* 275 */         return (HttpUriRequest)new HttpTrace(uri);
/*     */     } 
/* 277 */     throw new IllegalArgumentException("Invalid HTTP method: " + httpMethod);
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
/*     */   protected void postProcessHttpRequest(HttpUriRequest request) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri) {
/* 299 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() throws Exception {
/* 310 */     HttpClient httpClient = getHttpClient();
/* 311 */     if (httpClient instanceof Closeable) {
/* 312 */       ((Closeable)httpClient).close();
/*     */     }
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
/*     */   
/*     */   private static class HttpDelete
/*     */     extends HttpEntityEnclosingRequestBase
/*     */   {
/*     */     public HttpDelete(URI uri) {
/* 329 */       setURI(uri);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getMethod() {
/* 334 */       return "DELETE";
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/HttpComponentsClientHttpRequestFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */