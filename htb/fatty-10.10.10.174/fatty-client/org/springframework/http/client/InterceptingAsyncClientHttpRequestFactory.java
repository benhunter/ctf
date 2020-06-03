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
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public class InterceptingAsyncClientHttpRequestFactory
/*    */   implements AsyncClientHttpRequestFactory
/*    */ {
/*    */   private AsyncClientHttpRequestFactory delegate;
/*    */   private List<AsyncClientHttpRequestInterceptor> interceptors;
/*    */   
/*    */   public InterceptingAsyncClientHttpRequestFactory(AsyncClientHttpRequestFactory delegate, @Nullable List<AsyncClientHttpRequestInterceptor> interceptors) {
/* 52 */     this.delegate = delegate;
/* 53 */     this.interceptors = (interceptors != null) ? interceptors : Collections.<AsyncClientHttpRequestInterceptor>emptyList();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public AsyncClientHttpRequest createAsyncRequest(URI uri, HttpMethod method) {
/* 59 */     return new InterceptingAsyncClientHttpRequest(this.delegate, this.interceptors, uri, method);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/InterceptingAsyncClientHttpRequestFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */