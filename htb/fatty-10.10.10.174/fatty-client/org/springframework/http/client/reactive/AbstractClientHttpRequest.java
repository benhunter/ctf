/*     */ package org.springframework.http.client.reactive;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.http.HttpCookie;
/*     */ import org.springframework.http.HttpHeaders;
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
/*     */ public abstract class AbstractClientHttpRequest
/*     */   implements ClientHttpRequest
/*     */ {
/*     */   private final HttpHeaders headers;
/*     */   private final MultiValueMap<String, HttpCookie> cookies;
/*     */   
/*     */   private enum State
/*     */   {
/*  52 */     NEW, COMMITTING, COMMITTED;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   private final AtomicReference<State> state = new AtomicReference<>(State.NEW);
/*     */   
/*  61 */   private final List<Supplier<? extends Publisher<Void>>> commitActions = new ArrayList<>(4);
/*     */ 
/*     */   
/*     */   public AbstractClientHttpRequest() {
/*  65 */     this(new HttpHeaders());
/*     */   }
/*     */   
/*     */   public AbstractClientHttpRequest(HttpHeaders headers) {
/*  69 */     Assert.notNull(headers, "HttpHeaders must not be null");
/*  70 */     this.headers = headers;
/*  71 */     this.cookies = (MultiValueMap<String, HttpCookie>)new LinkedMultiValueMap();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpHeaders getHeaders() {
/*  77 */     if (State.COMMITTED.equals(this.state.get())) {
/*  78 */       return HttpHeaders.readOnlyHttpHeaders(this.headers);
/*     */     }
/*  80 */     return this.headers;
/*     */   }
/*     */ 
/*     */   
/*     */   public MultiValueMap<String, HttpCookie> getCookies() {
/*  85 */     if (State.COMMITTED.equals(this.state.get())) {
/*  86 */       return CollectionUtils.unmodifiableMultiValueMap(this.cookies);
/*     */     }
/*  88 */     return this.cookies;
/*     */   }
/*     */ 
/*     */   
/*     */   public void beforeCommit(Supplier<? extends Mono<Void>> action) {
/*  93 */     Assert.notNull(action, "Action must not be null");
/*  94 */     this.commitActions.add(action);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCommitted() {
/*  99 */     return (this.state.get() != State.NEW);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Mono<Void> doCommit() {
/* 107 */     return doCommit(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Mono<Void> doCommit(@Nullable Supplier<? extends Publisher<Void>> writeAction) {
/* 117 */     if (!this.state.compareAndSet(State.NEW, State.COMMITTING)) {
/* 118 */       return Mono.empty();
/*     */     }
/*     */     
/* 121 */     this.commitActions.add(() -> Mono.fromRunnable(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 128 */     if (writeAction != null) {
/* 129 */       this.commitActions.add(writeAction);
/*     */     }
/*     */ 
/*     */     
/* 133 */     List<? extends Publisher<Void>> actions = (List<? extends Publisher<Void>>)this.commitActions.stream().map(Supplier::get).collect(Collectors.toList());
/*     */     
/* 135 */     return Flux.concat(actions).then();
/*     */   }
/*     */   
/*     */   protected abstract void applyHeaders();
/*     */   
/*     */   protected abstract void applyCookies();
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/reactive/AbstractClientHttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */