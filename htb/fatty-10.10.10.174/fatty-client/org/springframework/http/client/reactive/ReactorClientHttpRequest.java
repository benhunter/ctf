/*     */ package org.springframework.http.client.reactive;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.handler.codec.http.cookie.DefaultCookie;
/*     */ import java.net.URI;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.DataBufferFactory;
/*     */ import org.springframework.core.io.buffer.NettyDataBufferFactory;
/*     */ import org.springframework.http.HttpCookie;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.ZeroCopyHttpOutputMessage;
/*     */ import reactor.core.publisher.Flux;
/*     */ import reactor.core.publisher.Mono;
/*     */ import reactor.netty.NettyOutbound;
/*     */ import reactor.netty.http.client.HttpClientRequest;
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
/*     */ class ReactorClientHttpRequest
/*     */   extends AbstractClientHttpRequest
/*     */   implements ZeroCopyHttpOutputMessage
/*     */ {
/*     */   private final HttpMethod httpMethod;
/*     */   private final URI uri;
/*     */   private final HttpClientRequest request;
/*     */   private final NettyOutbound outbound;
/*     */   private final NettyDataBufferFactory bufferFactory;
/*     */   
/*     */   public ReactorClientHttpRequest(HttpMethod method, URI uri, HttpClientRequest request, NettyOutbound outbound) {
/*  58 */     this.httpMethod = method;
/*  59 */     this.uri = uri;
/*  60 */     this.request = request;
/*  61 */     this.outbound = outbound;
/*  62 */     this.bufferFactory = new NettyDataBufferFactory(outbound.alloc());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DataBufferFactory bufferFactory() {
/*  68 */     return (DataBufferFactory)this.bufferFactory;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpMethod getMethod() {
/*  73 */     return this.httpMethod;
/*     */   }
/*     */ 
/*     */   
/*     */   public URI getURI() {
/*  78 */     return this.uri;
/*     */   }
/*     */ 
/*     */   
/*     */   public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
/*  83 */     return doCommit(() -> {
/*     */           if (body instanceof Mono) {
/*     */             Mono<ByteBuf> byteBufMono = Mono.from(body).map(NettyDataBufferFactory::toByteBuf);
/*     */             return (Publisher)this.outbound.send((Publisher)byteBufMono).then();
/*     */           } 
/*     */           Flux<ByteBuf> byteBufFlux = Flux.from(body).map(NettyDataBufferFactory::toByteBuf);
/*     */           return (Publisher)this.outbound.send((Publisher)byteBufFlux).then();
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
/*  99 */     Flux flux = Flux.from(body).map(ReactorClientHttpRequest::toByteBufs);
/* 100 */     return doCommit(() -> this.outbound.sendGroups(byteBufs).then());
/*     */   }
/*     */   
/*     */   private static Publisher<ByteBuf> toByteBufs(Publisher<? extends DataBuffer> dataBuffers) {
/* 104 */     return (Publisher<ByteBuf>)Flux.from(dataBuffers).map(NettyDataBufferFactory::toByteBuf);
/*     */   }
/*     */ 
/*     */   
/*     */   public Mono<Void> writeWith(Path file, long position, long count) {
/* 109 */     return doCommit(() -> this.outbound.sendFile(file, position, count).then());
/*     */   }
/*     */ 
/*     */   
/*     */   public Mono<Void> setComplete() {
/* 114 */     return doCommit(this.outbound::then);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyHeaders() {
/* 119 */     getHeaders().forEach((key, value) -> this.request.requestHeaders().set(key, value));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyCookies() {
/* 124 */     getCookies().values().stream().flatMap(Collection::stream)
/* 125 */       .map(cookie -> new DefaultCookie(cookie.getName(), cookie.getValue()))
/* 126 */       .forEach(this.request::addCookie);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/reactive/ReactorClientHttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */