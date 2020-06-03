/*    */ package org.springframework.http.client.reactive;
/*    */ 
/*    */ import java.net.HttpCookie;
/*    */ import java.util.List;
/*    */ import org.eclipse.jetty.http.HttpField;
/*    */ import org.eclipse.jetty.reactive.client.ReactiveResponse;
/*    */ import org.reactivestreams.Publisher;
/*    */ import org.springframework.core.io.buffer.DataBuffer;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.http.HttpStatus;
/*    */ import org.springframework.http.ResponseCookie;
/*    */ import org.springframework.util.CollectionUtils;
/*    */ import org.springframework.util.LinkedMultiValueMap;
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
/*    */ 
/*    */ class JettyClientHttpResponse
/*    */   implements ClientHttpResponse
/*    */ {
/*    */   private final ReactiveResponse reactiveResponse;
/*    */   private final Flux<DataBuffer> content;
/*    */   
/*    */   public JettyClientHttpResponse(ReactiveResponse reactiveResponse, Publisher<DataBuffer> content) {
/* 49 */     this.reactiveResponse = reactiveResponse;
/* 50 */     this.content = Flux.from(content);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpStatus getStatusCode() {
/* 56 */     return HttpStatus.valueOf(getRawStatusCode());
/*    */   }
/*    */ 
/*    */   
/*    */   public int getRawStatusCode() {
/* 61 */     return this.reactiveResponse.getStatus();
/*    */   }
/*    */ 
/*    */   
/*    */   public MultiValueMap<String, ResponseCookie> getCookies() {
/* 66 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap();
/* 67 */     List<String> cookieHeader = getHeaders().get("Set-Cookie");
/* 68 */     if (cookieHeader != null) {
/* 69 */       cookieHeader.forEach(header -> HttpCookie.parse(header).forEach(()));
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 80 */     return CollectionUtils.unmodifiableMultiValueMap((MultiValueMap)linkedMultiValueMap);
/*    */   }
/*    */ 
/*    */   
/*    */   public Flux<DataBuffer> getBody() {
/* 85 */     return this.content;
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpHeaders getHeaders() {
/* 90 */     HttpHeaders headers = new HttpHeaders();
/* 91 */     this.reactiveResponse.getHeaders().stream()
/* 92 */       .forEach(field -> headers.add(field.getName(), field.getValue()));
/* 93 */     return headers;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/reactive/JettyClientHttpResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */