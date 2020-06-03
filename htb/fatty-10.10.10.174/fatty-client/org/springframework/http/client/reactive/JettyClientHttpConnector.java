/*     */ package org.springframework.http.client.reactive;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import org.eclipse.jetty.client.HttpClient;
/*     */ import org.eclipse.jetty.reactive.client.ContentChunk;
/*     */ import org.eclipse.jetty.reactive.client.ReactiveResponse;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.DataBufferFactory;
/*     */ import org.springframework.core.io.buffer.DefaultDataBufferFactory;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class JettyClientHttpConnector
/*     */   implements ClientHttpConnector
/*     */ {
/*     */   private final HttpClient httpClient;
/*  46 */   private DataBufferFactory bufferFactory = (DataBufferFactory)new DefaultDataBufferFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JettyClientHttpConnector() {
/*  53 */     this(new HttpClient());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JettyClientHttpConnector(JettyResourceFactory resourceFactory, @Nullable Consumer<HttpClient> customizer) {
/*  64 */     HttpClient httpClient = new HttpClient();
/*  65 */     httpClient.setExecutor(resourceFactory.getExecutor());
/*  66 */     httpClient.setByteBufferPool(resourceFactory.getByteBufferPool());
/*  67 */     httpClient.setScheduler(resourceFactory.getScheduler());
/*  68 */     if (customizer != null) {
/*  69 */       customizer.accept(httpClient);
/*     */     }
/*  71 */     this.httpClient = httpClient;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JettyClientHttpConnector(HttpClient httpClient) {
/*  78 */     Assert.notNull(httpClient, "HttpClient is required");
/*  79 */     this.httpClient = httpClient;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBufferFactory(DataBufferFactory bufferFactory) {
/*  84 */     this.bufferFactory = bufferFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mono<ClientHttpResponse> connect(HttpMethod method, URI uri, Function<? super ClientHttpRequest, Mono<Void>> requestCallback) {
/*  92 */     if (!uri.isAbsolute()) {
/*  93 */       return Mono.error(new IllegalArgumentException("URI is not absolute: " + uri));
/*     */     }
/*     */     
/*  96 */     if (!this.httpClient.isStarted()) {
/*     */       try {
/*  98 */         this.httpClient.start();
/*     */       }
/* 100 */       catch (Exception ex) {
/* 101 */         return Mono.error(ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 106 */     JettyClientHttpRequest clientHttpRequest = new JettyClientHttpRequest(this.httpClient.newRequest(uri).method(method.toString()), this.bufferFactory);
/*     */     
/* 108 */     return ((Mono)requestCallback.apply(clientHttpRequest)).then(Mono.from(clientHttpRequest
/* 109 */           .getReactiveRequest().response((response, chunks) -> {
/*     */               Flux<DataBuffer> content = Flux.from(chunks).map(this::toDataBuffer);
/*     */               return (Publisher)Mono.just(new JettyClientHttpResponse(response, (Publisher<DataBuffer>)content));
/*     */             })));
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
/*     */   private DataBuffer toDataBuffer(ContentChunk chunk) {
/* 124 */     DataBuffer buffer = this.bufferFactory.allocateBuffer(chunk.buffer.capacity());
/* 125 */     buffer.write(new ByteBuffer[] { chunk.buffer });
/* 126 */     chunk.callback.succeeded();
/* 127 */     return buffer;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/reactive/JettyClientHttpConnector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */