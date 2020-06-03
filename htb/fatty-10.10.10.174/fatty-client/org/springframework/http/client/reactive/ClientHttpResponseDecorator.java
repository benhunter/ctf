/*    */ package org.springframework.http.client.reactive;
/*    */ 
/*    */ import org.springframework.core.io.buffer.DataBuffer;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.http.HttpStatus;
/*    */ import org.springframework.http.ResponseCookie;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.MultiValueMap;
/*    */ import reactor.core.publisher.Flux;
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
/*    */ public class ClientHttpResponseDecorator
/*    */   implements ClientHttpResponse
/*    */ {
/*    */   private final ClientHttpResponse delegate;
/*    */   
/*    */   public ClientHttpResponseDecorator(ClientHttpResponse delegate) {
/* 41 */     Assert.notNull(delegate, "Delegate is required");
/* 42 */     this.delegate = delegate;
/*    */   }
/*    */ 
/*    */   
/*    */   public ClientHttpResponse getDelegate() {
/* 47 */     return this.delegate;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpStatus getStatusCode() {
/* 55 */     return this.delegate.getStatusCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public int getRawStatusCode() {
/* 60 */     return this.delegate.getRawStatusCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpHeaders getHeaders() {
/* 65 */     return this.delegate.getHeaders();
/*    */   }
/*    */ 
/*    */   
/*    */   public MultiValueMap<String, ResponseCookie> getCookies() {
/* 70 */     return this.delegate.getCookies();
/*    */   }
/*    */ 
/*    */   
/*    */   public Flux<DataBuffer> getBody() {
/* 75 */     return this.delegate.getBody();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 80 */     return getClass().getSimpleName() + " [delegate=" + getDelegate() + "]";
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/reactive/ClientHttpResponseDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */