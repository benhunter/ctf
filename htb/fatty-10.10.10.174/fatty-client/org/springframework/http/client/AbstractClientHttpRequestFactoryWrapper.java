/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URI;
/*    */ import org.springframework.http.HttpMethod;
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
/*    */ public abstract class AbstractClientHttpRequestFactoryWrapper
/*    */   implements ClientHttpRequestFactory
/*    */ {
/*    */   private final ClientHttpRequestFactory requestFactory;
/*    */   
/*    */   protected AbstractClientHttpRequestFactoryWrapper(ClientHttpRequestFactory requestFactory) {
/* 42 */     Assert.notNull(requestFactory, "ClientHttpRequestFactory must not be null");
/* 43 */     this.requestFactory = requestFactory;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
/* 54 */     return createRequest(uri, httpMethod, this.requestFactory);
/*    */   }
/*    */   
/*    */   protected abstract ClientHttpRequest createRequest(URI paramURI, HttpMethod paramHttpMethod, ClientHttpRequestFactory paramClientHttpRequestFactory) throws IOException;
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/AbstractClientHttpRequestFactoryWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */