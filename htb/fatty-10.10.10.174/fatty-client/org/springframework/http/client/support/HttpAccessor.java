/*    */ package org.springframework.http.client.support;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URI;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.springframework.http.HttpLogging;
/*    */ import org.springframework.http.HttpMethod;
/*    */ import org.springframework.http.client.ClientHttpRequest;
/*    */ import org.springframework.http.client.ClientHttpRequestFactory;
/*    */ import org.springframework.http.client.SimpleClientHttpRequestFactory;
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
/*    */ 
/*    */ 
/*    */ public abstract class HttpAccessor
/*    */ {
/* 48 */   protected final Log logger = HttpLogging.forLogName(getClass());
/*    */   
/* 50 */   private ClientHttpRequestFactory requestFactory = (ClientHttpRequestFactory)new SimpleClientHttpRequestFactory();
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
/*    */   public void setRequestFactory(ClientHttpRequestFactory requestFactory) {
/* 65 */     Assert.notNull(requestFactory, "ClientHttpRequestFactory must not be null");
/* 66 */     this.requestFactory = requestFactory;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ClientHttpRequestFactory getRequestFactory() {
/* 73 */     return this.requestFactory;
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
/*    */   protected ClientHttpRequest createRequest(URI url, HttpMethod method) throws IOException {
/* 87 */     ClientHttpRequest request = getRequestFactory().createRequest(url, method);
/* 88 */     if (this.logger.isDebugEnabled()) {
/* 89 */       this.logger.debug("HTTP " + method.name() + " " + url);
/*    */     }
/* 91 */     return request;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/support/HttpAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */