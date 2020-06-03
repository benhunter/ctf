/*     */ package org.springframework.http.client.reactive;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.handler.codec.http.cookie.Cookie;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.reactivestreams.Subscription;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.NettyDataBufferFactory;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.ResponseCookie;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import reactor.core.publisher.Flux;
/*     */ import reactor.netty.NettyInbound;
/*     */ import reactor.netty.http.client.HttpClientResponse;
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
/*     */ class ReactorClientHttpResponse
/*     */   implements ClientHttpResponse
/*     */ {
/*     */   private final NettyDataBufferFactory bufferFactory;
/*     */   private final HttpClientResponse response;
/*     */   private final NettyInbound inbound;
/*  51 */   private final AtomicBoolean rejectSubscribers = new AtomicBoolean();
/*     */ 
/*     */   
/*     */   public ReactorClientHttpResponse(HttpClientResponse response, NettyInbound inbound, ByteBufAllocator alloc) {
/*  55 */     this.response = response;
/*  56 */     this.inbound = inbound;
/*  57 */     this.bufferFactory = new NettyDataBufferFactory(alloc);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Flux<DataBuffer> getBody() {
/*  63 */     return this.inbound.receive()
/*  64 */       .doOnSubscribe(s -> {
/*     */           
/*     */           if (this.rejectSubscribers.get()) {
/*     */             throw new IllegalStateException("The client response body can only be consumed once.");
/*     */           }
/*  69 */         }).doOnCancel(() -> this.rejectSubscribers.set(true))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  75 */       .map(byteBuf -> {
/*     */           byteBuf.retain();
/*     */           return (DataBuffer)this.bufferFactory.wrap(byteBuf);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHeaders getHeaders() {
/*  83 */     HttpHeaders headers = new HttpHeaders();
/*  84 */     this.response.responseHeaders().entries().forEach(e -> headers.add((String)e.getKey(), (String)e.getValue()));
/*  85 */     return headers;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpStatus getStatusCode() {
/*  90 */     return HttpStatus.valueOf(getRawStatusCode());
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRawStatusCode() {
/*  95 */     return this.response.status().code();
/*     */   }
/*     */ 
/*     */   
/*     */   public MultiValueMap<String, ResponseCookie> getCookies() {
/* 100 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap();
/* 101 */     this.response.cookies().values().stream().flatMap(Collection::stream)
/* 102 */       .forEach(cookie -> result.add(cookie.name(), ResponseCookie.from(cookie.name(), cookie.value()).domain(cookie.domain()).path(cookie.path()).maxAge(cookie.maxAge()).secure(cookie.isSecure()).httpOnly(cookie.isHttpOnly()).build()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 110 */     return CollectionUtils.unmodifiableMultiValueMap((MultiValueMap)linkedMultiValueMap);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 115 */     return "ReactorClientHttpResponse{request=[" + this.response
/* 116 */       .method().name() + " " + this.response.uri() + "],status=" + 
/* 117 */       getRawStatusCode() + '}';
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/reactive/ReactorClientHttpResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */