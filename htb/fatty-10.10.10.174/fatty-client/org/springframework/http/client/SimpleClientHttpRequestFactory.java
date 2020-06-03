/*     */ package org.springframework.http.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.Proxy;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import org.springframework.core.task.AsyncListenableTaskExecutor;
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
/*     */ public class SimpleClientHttpRequestFactory
/*     */   implements ClientHttpRequestFactory, AsyncClientHttpRequestFactory
/*     */ {
/*     */   private static final int DEFAULT_CHUNK_SIZE = 4096;
/*     */   @Nullable
/*     */   private Proxy proxy;
/*     */   private boolean bufferRequestBody = true;
/*  51 */   private int chunkSize = 4096;
/*     */   
/*  53 */   private int connectTimeout = -1;
/*     */   
/*  55 */   private int readTimeout = -1;
/*     */ 
/*     */   
/*     */   private boolean outputStreaming = true;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private AsyncListenableTaskExecutor taskExecutor;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProxy(Proxy proxy) {
/*  67 */     this.proxy = proxy;
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
/*     */   public void setBufferRequestBody(boolean bufferRequestBody) {
/*  84 */     this.bufferRequestBody = bufferRequestBody;
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
/*     */   public void setChunkSize(int chunkSize) {
/*  97 */     this.chunkSize = chunkSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConnectTimeout(int connectTimeout) {
/* 107 */     this.connectTimeout = connectTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReadTimeout(int readTimeout) {
/* 117 */     this.readTimeout = readTimeout;
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
/*     */   public void setOutputStreaming(boolean outputStreaming) {
/* 130 */     this.outputStreaming = outputStreaming;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTaskExecutor(AsyncListenableTaskExecutor taskExecutor) {
/* 139 */     this.taskExecutor = taskExecutor;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
/* 145 */     HttpURLConnection connection = openConnection(uri.toURL(), this.proxy);
/* 146 */     prepareConnection(connection, httpMethod.name());
/*     */     
/* 148 */     if (this.bufferRequestBody) {
/* 149 */       return new SimpleBufferingClientHttpRequest(connection, this.outputStreaming);
/*     */     }
/*     */     
/* 152 */     return new SimpleStreamingClientHttpRequest(connection, this.chunkSize, this.outputStreaming);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AsyncClientHttpRequest createAsyncRequest(URI uri, HttpMethod httpMethod) throws IOException {
/* 162 */     Assert.state((this.taskExecutor != null), "Asynchronous execution requires TaskExecutor to be set");
/*     */     
/* 164 */     HttpURLConnection connection = openConnection(uri.toURL(), this.proxy);
/* 165 */     prepareConnection(connection, httpMethod.name());
/*     */     
/* 167 */     if (this.bufferRequestBody) {
/* 168 */       return new SimpleBufferingAsyncClientHttpRequest(connection, this.outputStreaming, this.taskExecutor);
/*     */     }
/*     */ 
/*     */     
/* 172 */     return new SimpleStreamingAsyncClientHttpRequest(connection, this.chunkSize, this.outputStreaming, this.taskExecutor);
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
/*     */   protected HttpURLConnection openConnection(URL url, @Nullable Proxy proxy) throws IOException {
/* 187 */     URLConnection urlConnection = (proxy != null) ? url.openConnection(proxy) : url.openConnection();
/* 188 */     if (!HttpURLConnection.class.isInstance(urlConnection)) {
/* 189 */       throw new IllegalStateException("HttpURLConnection required for [" + url + "] but got: " + urlConnection);
/*     */     }
/* 191 */     return (HttpURLConnection)urlConnection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
/* 202 */     if (this.connectTimeout >= 0) {
/* 203 */       connection.setConnectTimeout(this.connectTimeout);
/*     */     }
/* 205 */     if (this.readTimeout >= 0) {
/* 206 */       connection.setReadTimeout(this.readTimeout);
/*     */     }
/*     */     
/* 209 */     connection.setDoInput(true);
/*     */     
/* 211 */     if ("GET".equals(httpMethod)) {
/* 212 */       connection.setInstanceFollowRedirects(true);
/*     */     } else {
/*     */       
/* 215 */       connection.setInstanceFollowRedirects(false);
/*     */     } 
/*     */     
/* 218 */     if ("POST".equals(httpMethod) || "PUT".equals(httpMethod) || "PATCH"
/* 219 */       .equals(httpMethod) || "DELETE".equals(httpMethod)) {
/* 220 */       connection.setDoOutput(true);
/*     */     } else {
/*     */       
/* 223 */       connection.setDoOutput(false);
/*     */     } 
/*     */     
/* 226 */     connection.setRequestMethod(httpMethod);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/SimpleClientHttpRequestFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */