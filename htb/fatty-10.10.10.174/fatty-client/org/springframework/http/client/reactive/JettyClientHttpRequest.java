/*     */ package org.springframework.http.client.reactive;
/*     */ 
/*     */ import java.net.HttpCookie;
/*     */ import java.net.URI;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import org.eclipse.jetty.client.api.Request;
/*     */ import org.eclipse.jetty.reactive.client.ContentChunk;
/*     */ import org.eclipse.jetty.reactive.client.ReactiveRequest;
/*     */ import org.eclipse.jetty.util.Callback;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.DataBufferFactory;
/*     */ import org.springframework.core.io.buffer.DataBufferUtils;
/*     */ import org.springframework.core.io.buffer.PooledDataBuffer;
/*     */ import org.springframework.http.HttpCookie;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import reactor.core.Exceptions;
/*     */ import reactor.core.publisher.Flux;
/*     */ import reactor.core.publisher.Mono;
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
/*     */ class JettyClientHttpRequest
/*     */   extends AbstractClientHttpRequest
/*     */ {
/*     */   private final Request jettyRequest;
/*     */   private final DataBufferFactory bufferFactory;
/*     */   @Nullable
/*     */   private ReactiveRequest reactiveRequest;
/*     */   
/*     */   public JettyClientHttpRequest(Request jettyRequest, DataBufferFactory bufferFactory) {
/*  61 */     this.jettyRequest = jettyRequest;
/*  62 */     this.bufferFactory = bufferFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpMethod getMethod() {
/*  68 */     HttpMethod method = HttpMethod.resolve(this.jettyRequest.getMethod());
/*  69 */     Assert.state((method != null), "Method must not be null");
/*  70 */     return method;
/*     */   }
/*     */ 
/*     */   
/*     */   public URI getURI() {
/*  75 */     return this.jettyRequest.getURI();
/*     */   }
/*     */ 
/*     */   
/*     */   public Mono<Void> setComplete() {
/*  80 */     return doCommit(this::completes);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataBufferFactory bufferFactory() {
/*  85 */     return this.bufferFactory;
/*     */   }
/*     */ 
/*     */   
/*     */   public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
/*  90 */     Flux<ContentChunk> chunks = Flux.from(body).map(this::toContentChunk);
/*  91 */     ReactiveRequest.Content content = ReactiveRequest.Content.fromPublisher((Publisher)chunks, getContentType());
/*  92 */     this.reactiveRequest = ReactiveRequest.newBuilder(this.jettyRequest).content(content).build();
/*  93 */     return doCommit(this::completes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
/* 101 */     Flux<ContentChunk> chunks = Flux.from(body).flatMap(Function.identity()).doOnDiscard(PooledDataBuffer.class, DataBufferUtils::release).map(this::toContentChunk);
/* 102 */     ReactiveRequest.Content content = ReactiveRequest.Content.fromPublisher((Publisher)chunks, getContentType());
/* 103 */     this.reactiveRequest = ReactiveRequest.newBuilder(this.jettyRequest).content(content).build();
/* 104 */     return doCommit(this::completes);
/*     */   }
/*     */   
/*     */   private String getContentType() {
/* 108 */     MediaType contentType = getHeaders().getContentType();
/* 109 */     return (contentType != null) ? contentType.toString() : "application/octet-stream";
/*     */   }
/*     */   
/*     */   private Mono<Void> completes() {
/* 113 */     return Mono.empty();
/*     */   }
/*     */   
/*     */   private ContentChunk toContentChunk(final DataBuffer buffer) {
/* 117 */     return new ContentChunk(buffer.asByteBuffer(), new Callback()
/*     */         {
/*     */           public void succeeded() {
/* 120 */             DataBufferUtils.release(buffer);
/*     */           }
/*     */ 
/*     */           
/*     */           public void failed(Throwable x) {
/* 125 */             DataBufferUtils.release(buffer);
/* 126 */             throw Exceptions.propagate(x);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void applyCookies() {
/* 134 */     getCookies().values().stream().flatMap(Collection::stream)
/* 135 */       .map(cookie -> new HttpCookie(cookie.getName(), cookie.getValue()))
/* 136 */       .forEach(this.jettyRequest::cookie);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyHeaders() {
/* 141 */     HttpHeaders headers = getHeaders();
/* 142 */     headers.forEach((key, value) -> value.forEach(()));
/* 143 */     if (!headers.containsKey("Accept")) {
/* 144 */       this.jettyRequest.header("Accept", "*/*");
/*     */     }
/*     */   }
/*     */   
/*     */   ReactiveRequest getReactiveRequest() {
/* 149 */     if (this.reactiveRequest == null) {
/* 150 */       this.reactiveRequest = ReactiveRequest.newBuilder(this.jettyRequest).build();
/*     */     }
/* 152 */     return this.reactiveRequest;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/reactive/JettyClientHttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */