/*     */ package org.springframework.remoting.httpinvoker;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Locale;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.NoHttpResponseException;
/*     */ import org.apache.http.StatusLine;
/*     */ import org.apache.http.client.HttpClient;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.methods.Configurable;
/*     */ import org.apache.http.client.methods.HttpPost;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.config.Registry;
/*     */ import org.apache.http.config.RegistryBuilder;
/*     */ import org.apache.http.conn.HttpClientConnectionManager;
/*     */ import org.apache.http.conn.socket.ConnectionSocketFactory;
/*     */ import org.apache.http.conn.socket.PlainConnectionSocketFactory;
/*     */ import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
/*     */ import org.apache.http.entity.ByteArrayEntity;
/*     */ import org.apache.http.impl.client.HttpClientBuilder;
/*     */ import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
/*     */ import org.springframework.context.i18n.LocaleContext;
/*     */ import org.springframework.context.i18n.LocaleContextHolder;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.remoting.support.RemoteInvocationResult;
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
/*     */ public class HttpComponentsHttpInvokerRequestExecutor
/*     */   extends AbstractHttpInvokerRequestExecutor
/*     */ {
/*     */   private static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 100;
/*     */   private static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 5;
/*     */   private static final int DEFAULT_READ_TIMEOUT_MILLISECONDS = 60000;
/*     */   private HttpClient httpClient;
/*     */   @Nullable
/*     */   private RequestConfig requestConfig;
/*     */   
/*     */   public HttpComponentsHttpInvokerRequestExecutor() {
/*  84 */     this(createDefaultHttpClient(), RequestConfig.custom()
/*  85 */         .setSocketTimeout(60000).build());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpComponentsHttpInvokerRequestExecutor(HttpClient httpClient) {
/*  94 */     this(httpClient, (RequestConfig)null);
/*     */   }
/*     */   
/*     */   private HttpComponentsHttpInvokerRequestExecutor(HttpClient httpClient, @Nullable RequestConfig requestConfig) {
/*  98 */     this.httpClient = httpClient;
/*  99 */     this.requestConfig = requestConfig;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static HttpClient createDefaultHttpClient() {
/* 107 */     Registry<ConnectionSocketFactory> schemeRegistry = RegistryBuilder.create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
/*     */     
/* 109 */     PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(schemeRegistry);
/* 110 */     connectionManager.setMaxTotal(100);
/* 111 */     connectionManager.setDefaultMaxPerRoute(5);
/*     */     
/* 113 */     return (HttpClient)HttpClientBuilder.create().setConnectionManager((HttpClientConnectionManager)connectionManager).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHttpClient(HttpClient httpClient) {
/* 121 */     this.httpClient = httpClient;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpClient getHttpClient() {
/* 128 */     return this.httpClient;
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
/*     */   public void setConnectTimeout(int timeout) {
/* 140 */     Assert.isTrue((timeout >= 0), "Timeout must be a non-negative value");
/* 141 */     this.requestConfig = cloneRequestConfig().setConnectTimeout(timeout).build();
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
/* 154 */     this.requestConfig = cloneRequestConfig().setConnectionRequestTimeout(connectionRequestTimeout).build();
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
/*     */   public void setReadTimeout(int timeout) {
/* 167 */     Assert.isTrue((timeout >= 0), "Timeout must be a non-negative value");
/* 168 */     this.requestConfig = cloneRequestConfig().setSocketTimeout(timeout).build();
/*     */   }
/*     */   
/*     */   private RequestConfig.Builder cloneRequestConfig() {
/* 172 */     return (this.requestConfig != null) ? RequestConfig.copy(this.requestConfig) : RequestConfig.custom();
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
/*     */ 
/*     */   
/*     */   protected RemoteInvocationResult doExecuteRequest(HttpInvokerClientConfiguration config, ByteArrayOutputStream baos) throws IOException, ClassNotFoundException {
/* 191 */     HttpPost postMethod = createHttpPost(config);
/* 192 */     setRequestBody(config, postMethod, baos);
/*     */     try {
/* 194 */       HttpResponse response = executeHttpPost(config, getHttpClient(), postMethod);
/* 195 */       validateResponse(config, response);
/* 196 */       InputStream responseBody = getResponseBody(config, response);
/* 197 */       return readRemoteInvocationResult(responseBody, config.getCodebaseUrl());
/*     */     } finally {
/*     */       
/* 200 */       postMethod.releaseConnection();
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
/*     */   protected HttpPost createHttpPost(HttpInvokerClientConfiguration config) throws IOException {
/* 214 */     HttpPost httpPost = new HttpPost(config.getServiceUrl());
/*     */     
/* 216 */     RequestConfig requestConfig = createRequestConfig(config);
/* 217 */     if (requestConfig != null) {
/* 218 */       httpPost.setConfig(requestConfig);
/*     */     }
/*     */     
/* 221 */     LocaleContext localeContext = LocaleContextHolder.getLocaleContext();
/* 222 */     if (localeContext != null) {
/* 223 */       Locale locale = localeContext.getLocale();
/* 224 */       if (locale != null) {
/* 225 */         httpPost.addHeader("Accept-Language", locale.toLanguageTag());
/*     */       }
/*     */     } 
/*     */     
/* 229 */     if (isAcceptGzipEncoding()) {
/* 230 */       httpPost.addHeader("Accept-Encoding", "gzip");
/*     */     }
/*     */     
/* 233 */     return httpPost;
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
/*     */   @Nullable
/*     */   protected RequestConfig createRequestConfig(HttpInvokerClientConfiguration config) {
/* 248 */     HttpClient client = getHttpClient();
/* 249 */     if (client instanceof Configurable) {
/* 250 */       RequestConfig clientRequestConfig = ((Configurable)client).getConfig();
/* 251 */       return mergeRequestConfig(clientRequestConfig);
/*     */     } 
/* 253 */     return this.requestConfig;
/*     */   }
/*     */   
/*     */   private RequestConfig mergeRequestConfig(RequestConfig defaultRequestConfig) {
/* 257 */     if (this.requestConfig == null) {
/* 258 */       return defaultRequestConfig;
/*     */     }
/*     */     
/* 261 */     RequestConfig.Builder builder = RequestConfig.copy(defaultRequestConfig);
/* 262 */     int connectTimeout = this.requestConfig.getConnectTimeout();
/* 263 */     if (connectTimeout >= 0) {
/* 264 */       builder.setConnectTimeout(connectTimeout);
/*     */     }
/* 266 */     int connectionRequestTimeout = this.requestConfig.getConnectionRequestTimeout();
/* 267 */     if (connectionRequestTimeout >= 0) {
/* 268 */       builder.setConnectionRequestTimeout(connectionRequestTimeout);
/*     */     }
/* 270 */     int socketTimeout = this.requestConfig.getSocketTimeout();
/* 271 */     if (socketTimeout >= 0) {
/* 272 */       builder.setSocketTimeout(socketTimeout);
/*     */     }
/* 274 */     return builder.build();
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
/*     */   
/*     */   protected void setRequestBody(HttpInvokerClientConfiguration config, HttpPost httpPost, ByteArrayOutputStream baos) throws IOException {
/* 292 */     ByteArrayEntity entity = new ByteArrayEntity(baos.toByteArray());
/* 293 */     entity.setContentType(getContentType());
/* 294 */     httpPost.setEntity((HttpEntity)entity);
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
/*     */   protected HttpResponse executeHttpPost(HttpInvokerClientConfiguration config, HttpClient httpClient, HttpPost httpPost) throws IOException {
/* 309 */     return httpClient.execute((HttpUriRequest)httpPost);
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
/*     */   protected void validateResponse(HttpInvokerClientConfiguration config, HttpResponse response) throws IOException {
/* 324 */     StatusLine status = response.getStatusLine();
/* 325 */     if (status.getStatusCode() >= 300) {
/* 326 */       throw new NoHttpResponseException("Did not receive successful HTTP response: status code = " + status
/* 327 */           .getStatusCode() + ", status message = [" + status
/* 328 */           .getReasonPhrase() + "]");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected InputStream getResponseBody(HttpInvokerClientConfiguration config, HttpResponse httpResponse) throws IOException {
/* 347 */     if (isGzipResponse(httpResponse)) {
/* 348 */       return new GZIPInputStream(httpResponse.getEntity().getContent());
/*     */     }
/*     */     
/* 351 */     return httpResponse.getEntity().getContent();
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
/*     */   protected boolean isGzipResponse(HttpResponse httpResponse) {
/* 363 */     Header encodingHeader = httpResponse.getFirstHeader("Content-Encoding");
/* 364 */     return (encodingHeader != null && encodingHeader.getValue() != null && encodingHeader
/* 365 */       .getValue().toLowerCase().contains("gzip"));
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/httpinvoker/HttpComponentsHttpInvokerRequestExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */