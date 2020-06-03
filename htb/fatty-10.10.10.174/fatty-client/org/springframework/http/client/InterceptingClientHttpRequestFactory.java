/*    */ package org.springframework.http.client;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import org.springframework.http.HttpMethod;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ public class InterceptingClientHttpRequestFactory
/*    */   extends AbstractClientHttpRequestFactoryWrapper
/*    */ {
/*    */   private final List<ClientHttpRequestInterceptor> interceptors;
/*    */   
/*    */   public InterceptingClientHttpRequestFactory(ClientHttpRequestFactory requestFactory, @Nullable List<ClientHttpRequestInterceptor> interceptors) {
/* 48 */     super(requestFactory);
/* 49 */     this.interceptors = (interceptors != null) ? interceptors : Collections.<ClientHttpRequestInterceptor>emptyList();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod, ClientHttpRequestFactory requestFactory) {
/* 55 */     return new InterceptingClientHttpRequest(requestFactory, this.interceptors, uri, httpMethod);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/InterceptingClientHttpRequestFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */