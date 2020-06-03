/*     */ package org.springframework.http.client.reactive;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.handler.codec.http.HttpMethod;
/*     */ import java.net.URI;
/*     */ import java.util.function.Function;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.util.Assert;
/*     */ import reactor.core.publisher.Mono;
/*     */ import reactor.netty.Connection;
/*     */ import reactor.netty.NettyInbound;
/*     */ import reactor.netty.NettyOutbound;
/*     */ import reactor.netty.http.client.HttpClient;
/*     */ import reactor.netty.http.client.HttpClientRequest;
/*     */ import reactor.netty.http.client.HttpClientResponse;
/*     */ import reactor.netty.resources.ConnectionProvider;
/*     */ import reactor.netty.resources.LoopResources;
/*     */ import reactor.netty.tcp.TcpClient;
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
/*     */ public class ReactorClientHttpConnector
/*     */   implements ClientHttpConnector
/*     */ {
/*     */   private static final Function<HttpClient, HttpClient> defaultInitializer;
/*     */   private final HttpClient httpClient;
/*     */   
/*     */   static {
/*  44 */     defaultInitializer = (client -> client.compress(true));
/*     */   }
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
/*     */   public ReactorClientHttpConnector() {
/*  57 */     this.httpClient = defaultInitializer.apply(HttpClient.create());
/*     */   }
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
/*     */   public ReactorClientHttpConnector(ReactorResourceFactory factory, Function<HttpClient, HttpClient> mapper) {
/*  77 */     this.httpClient = defaultInitializer.<HttpClient>andThen(mapper).apply(initHttpClient(factory));
/*     */   }
/*     */   
/*     */   private static HttpClient initHttpClient(ReactorResourceFactory resourceFactory) {
/*  81 */     ConnectionProvider provider = resourceFactory.getConnectionProvider();
/*  82 */     LoopResources resources = resourceFactory.getLoopResources();
/*  83 */     Assert.notNull(provider, "No ConnectionProvider: is ReactorResourceFactory not initialized yet?");
/*  84 */     Assert.notNull(resources, "No LoopResources: is ReactorResourceFactory not initialized yet?");
/*  85 */     return HttpClient.create(provider).tcpConfiguration(tcpClient -> tcpClient.runOn(resources));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReactorClientHttpConnector(HttpClient httpClient) {
/*  94 */     Assert.notNull(httpClient, "HttpClient is required");
/*  95 */     this.httpClient = httpClient;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mono<ClientHttpResponse> connect(HttpMethod method, URI uri, Function<? super ClientHttpRequest, Mono<Void>> requestCallback) {
/* 103 */     if (!uri.isAbsolute()) {
/* 104 */       return Mono.error(new IllegalArgumentException("URI is not absolute: " + uri));
/*     */     }
/*     */     
/* 107 */     return ((HttpClient.RequestSender)this.httpClient
/* 108 */       .request(HttpMethod.valueOf(method.name()))
/* 109 */       .uri(uri.toString()))
/* 110 */       .send((request, outbound) -> (Publisher)requestCallback.apply(adaptRequest(method, uri, request, outbound)))
/* 111 */       .responseConnection((res, con) -> Mono.just(adaptResponse(res, con.inbound(), con.outbound().alloc())))
/* 112 */       .next();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ReactorClientHttpRequest adaptRequest(HttpMethod method, URI uri, HttpClientRequest request, NettyOutbound nettyOutbound) {
/* 118 */     return new ReactorClientHttpRequest(method, uri, request, nettyOutbound);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ClientHttpResponse adaptResponse(HttpClientResponse response, NettyInbound nettyInbound, ByteBufAllocator allocator) {
/* 124 */     return new ReactorClientHttpResponse(response, nettyInbound, allocator);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/reactive/ReactorClientHttpConnector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */