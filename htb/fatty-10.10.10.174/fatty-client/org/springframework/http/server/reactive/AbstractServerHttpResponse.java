/*     */ package org.springframework.http.server.reactive;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.DataBufferFactory;
/*     */ import org.springframework.core.io.buffer.DataBufferUtils;
/*     */ import org.springframework.core.io.buffer.PooledDataBuffer;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpLogging;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.ResponseCookie;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractServerHttpResponse
/*     */   implements ServerHttpResponse
/*     */ {
/*     */   private enum State
/*     */   {
/*  61 */     NEW, COMMITTING, COMMITTED;
/*     */   }
/*  63 */   protected final Log logger = HttpLogging.forLogName(getClass());
/*     */ 
/*     */   
/*     */   private final DataBufferFactory dataBufferFactory;
/*     */   
/*     */   @Nullable
/*     */   private Integer statusCode;
/*     */   
/*     */   private final HttpHeaders headers;
/*     */   
/*     */   private final MultiValueMap<String, ResponseCookie> cookies;
/*     */   
/*  75 */   private final AtomicReference<State> state = new AtomicReference<>(State.NEW);
/*     */   
/*  77 */   private final List<Supplier<? extends Mono<Void>>> commitActions = new ArrayList<>(4);
/*     */ 
/*     */   
/*     */   public AbstractServerHttpResponse(DataBufferFactory dataBufferFactory) {
/*  81 */     this(dataBufferFactory, new HttpHeaders());
/*     */   }
/*     */   
/*     */   public AbstractServerHttpResponse(DataBufferFactory dataBufferFactory, HttpHeaders headers) {
/*  85 */     Assert.notNull(dataBufferFactory, "DataBufferFactory must not be null");
/*  86 */     Assert.notNull(headers, "HttpHeaders must not be null");
/*  87 */     this.dataBufferFactory = dataBufferFactory;
/*  88 */     this.headers = headers;
/*  89 */     this.cookies = (MultiValueMap<String, ResponseCookie>)new LinkedMultiValueMap();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final DataBufferFactory bufferFactory() {
/*  95 */     return this.dataBufferFactory;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setStatusCode(@Nullable HttpStatus status) {
/* 100 */     if (this.state.get() == State.COMMITTED) {
/* 101 */       return false;
/*     */     }
/*     */     
/* 104 */     this.statusCode = (status != null) ? Integer.valueOf(status.value()) : null;
/* 105 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public HttpStatus getStatusCode() {
/* 112 */     return (this.statusCode != null) ? HttpStatus.resolve(this.statusCode.intValue()) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStatusCodeValue(@Nullable Integer statusCode) {
/* 121 */     this.statusCode = statusCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Integer getStatusCodeValue() {
/* 131 */     return this.statusCode;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpHeaders getHeaders() {
/* 136 */     return (this.state.get() == State.COMMITTED) ? 
/* 137 */       HttpHeaders.readOnlyHttpHeaders(this.headers) : this.headers;
/*     */   }
/*     */ 
/*     */   
/*     */   public MultiValueMap<String, ResponseCookie> getCookies() {
/* 142 */     return (this.state.get() == State.COMMITTED) ? 
/* 143 */       CollectionUtils.unmodifiableMultiValueMap(this.cookies) : this.cookies;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addCookie(ResponseCookie cookie) {
/* 148 */     Assert.notNull(cookie, "ResponseCookie must not be null");
/*     */     
/* 150 */     if (this.state.get() == State.COMMITTED) {
/* 151 */       throw new IllegalStateException("Can't add the cookie " + cookie + "because the HTTP response has already been committed");
/*     */     }
/*     */ 
/*     */     
/* 155 */     getCookies().add(cookie.getName(), cookie);
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
/*     */   public void beforeCommit(Supplier<? extends Mono<Void>> action) {
/* 169 */     this.commitActions.add(action);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCommitted() {
/* 174 */     return (this.state.get() != State.NEW);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
/* 182 */     if (body instanceof Mono) {
/* 183 */       return ((Mono)body).flatMap(buffer -> doCommit(()).doOnDiscard(PooledDataBuffer.class, DataBufferUtils::release));
/*     */     }
/*     */ 
/*     */     
/* 187 */     return (new ChannelSendOperator(body, inner -> doCommit(())))
/* 188 */       .doOnError(t -> removeContentLength());
/*     */   }
/*     */ 
/*     */   
/*     */   public final Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
/* 193 */     return (new ChannelSendOperator(body, inner -> doCommit(())))
/* 194 */       .doOnError(t -> removeContentLength());
/*     */   }
/*     */   
/*     */   private void removeContentLength() {
/* 198 */     if (!isCommitted()) {
/* 199 */       getHeaders().remove("Content-Length");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Mono<Void> setComplete() {
/* 205 */     return !isCommitted() ? doCommit(null) : Mono.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Mono<Void> doCommit() {
/* 213 */     return doCommit(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Mono<Void> doCommit(@Nullable Supplier<? extends Mono<Void>> writeAction) {
/* 223 */     if (!this.state.compareAndSet(State.NEW, State.COMMITTING)) {
/* 224 */       return Mono.empty();
/*     */     }
/*     */     
/* 227 */     this.commitActions.add(() -> Mono.fromRunnable(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 235 */     if (writeAction != null) {
/* 236 */       this.commitActions.add(writeAction);
/*     */     }
/*     */ 
/*     */     
/* 240 */     List<? extends Mono<Void>> actions = (List<? extends Mono<Void>>)this.commitActions.stream().map(Supplier::get).collect(Collectors.toList());
/*     */     
/* 242 */     return Flux.concat(actions).then();
/*     */   }
/*     */   
/*     */   public abstract <T> T getNativeResponse();
/*     */   
/*     */   protected abstract Mono<Void> writeWithInternal(Publisher<? extends DataBuffer> paramPublisher);
/*     */   
/*     */   protected abstract Mono<Void> writeAndFlushWithInternal(Publisher<? extends Publisher<? extends DataBuffer>> paramPublisher);
/*     */   
/*     */   protected abstract void applyStatusCode();
/*     */   
/*     */   protected abstract void applyHeaders();
/*     */   
/*     */   protected abstract void applyCookies();
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/reactive/AbstractServerHttpResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */