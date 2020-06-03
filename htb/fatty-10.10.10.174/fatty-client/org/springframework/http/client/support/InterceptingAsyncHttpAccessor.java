/*    */ package org.springframework.http.client.support;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.springframework.http.client.AsyncClientHttpRequestFactory;
/*    */ import org.springframework.http.client.AsyncClientHttpRequestInterceptor;
/*    */ import org.springframework.http.client.InterceptingAsyncClientHttpRequestFactory;
/*    */ import org.springframework.util.CollectionUtils;
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
/*    */ public abstract class InterceptingAsyncHttpAccessor
/*    */   extends AsyncHttpAccessor
/*    */ {
/* 36 */   private List<AsyncClientHttpRequestInterceptor> interceptors = new ArrayList<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setInterceptors(List<AsyncClientHttpRequestInterceptor> interceptors) {
/* 45 */     this.interceptors = interceptors;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<AsyncClientHttpRequestInterceptor> getInterceptors() {
/* 52 */     return this.interceptors;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public AsyncClientHttpRequestFactory getAsyncRequestFactory() {
/* 58 */     AsyncClientHttpRequestFactory delegate = super.getAsyncRequestFactory();
/* 59 */     if (!CollectionUtils.isEmpty(getInterceptors())) {
/* 60 */       return (AsyncClientHttpRequestFactory)new InterceptingAsyncClientHttpRequestFactory(delegate, getInterceptors());
/*    */     }
/*    */     
/* 63 */     return delegate;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/support/InterceptingAsyncHttpAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */