/*    */ package org.springframework.http.client.support;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URI;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.springframework.http.HttpLogging;
/*    */ import org.springframework.http.HttpMethod;
/*    */ import org.springframework.http.client.AsyncClientHttpRequest;
/*    */ import org.springframework.http.client.AsyncClientHttpRequestFactory;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
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
/*    */ 
/*    */ @Deprecated
/*    */ public class AsyncHttpAccessor
/*    */ {
/* 47 */   protected final Log logger = HttpLogging.forLogName(getClass());
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   private AsyncClientHttpRequestFactory asyncRequestFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setAsyncRequestFactory(AsyncClientHttpRequestFactory asyncRequestFactory) {
/* 60 */     Assert.notNull(asyncRequestFactory, "AsyncClientHttpRequestFactory must not be null");
/* 61 */     this.asyncRequestFactory = asyncRequestFactory;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AsyncClientHttpRequestFactory getAsyncRequestFactory() {
/* 69 */     Assert.state((this.asyncRequestFactory != null), "No AsyncClientHttpRequestFactory set");
/* 70 */     return this.asyncRequestFactory;
/*    */   }
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
/*    */   protected AsyncClientHttpRequest createAsyncRequest(URI url, HttpMethod method) throws IOException {
/* 85 */     AsyncClientHttpRequest request = getAsyncRequestFactory().createAsyncRequest(url, method);
/* 86 */     if (this.logger.isDebugEnabled()) {
/* 87 */       this.logger.debug("Created asynchronous " + method.name() + " request for \"" + url + "\"");
/*    */     }
/* 89 */     return request;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/support/AsyncHttpAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */