/*     */ package org.springframework.http.server.reactive;
/*     */ 
/*     */ import java.util.function.Function;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.reactivestreams.Subscriber;
/*     */ import org.reactivestreams.Subscription;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.DataBufferUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import reactor.core.CoreSubscriber;
/*     */ import reactor.core.Scannable;
/*     */ import reactor.core.publisher.Flux;
/*     */ import reactor.core.publisher.Mono;
/*     */ import reactor.core.publisher.Operators;
/*     */ import reactor.util.context.Context;
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
/*     */ 
/*     */ public class ChannelSendOperator<T>
/*     */   extends Mono<Void>
/*     */   implements Scannable
/*     */ {
/*     */   private final Function<Publisher<T>, Publisher<Void>> writeFunction;
/*     */   private final Flux<T> source;
/*     */   
/*     */   public ChannelSendOperator(Publisher<? extends T> source, Function<Publisher<T>, Publisher<Void>> writeFunction) {
/*  57 */     this.source = Flux.from(source);
/*  58 */     this.writeFunction = writeFunction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object scanUnsafe(Scannable.Attr key) {
/*  66 */     if (key == Scannable.Attr.PREFETCH) {
/*  67 */       return Integer.valueOf(2147483647);
/*     */     }
/*  69 */     if (key == Scannable.Attr.PARENT) {
/*  70 */       return this.source;
/*     */     }
/*  72 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribe(CoreSubscriber<? super Void> actual) {
/*  77 */     this.source.subscribe(new WriteBarrier(actual));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private enum State
/*     */   {
/*  84 */     NEW,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  90 */     FIRST_SIGNAL_RECEIVED,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  96 */     EMITTING_CACHED_SIGNALS,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 103 */     READY_TO_WRITE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class WriteBarrier
/*     */     implements CoreSubscriber<T>, Subscription, Publisher<T>
/*     */   {
/*     */     private final ChannelSendOperator<T>.WriteCompletionBarrier writeCompletionBarrier;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private Subscription subscription;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private T item;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private Throwable error;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean completed = false;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private long demandBeforeReadyToWrite;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 147 */     private ChannelSendOperator.State state = ChannelSendOperator.State.NEW;
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private Subscriber<? super T> writeSubscriber;
/*     */ 
/*     */     
/*     */     WriteBarrier(CoreSubscriber<? super Void> completionSubscriber) {
/* 155 */       this.writeCompletionBarrier = new ChannelSendOperator.WriteCompletionBarrier(completionSubscriber, this);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final void onSubscribe(Subscription s) {
/* 163 */       if (Operators.validate(this.subscription, s)) {
/* 164 */         this.subscription = s;
/* 165 */         this.writeCompletionBarrier.connect();
/* 166 */         s.request(1L);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public final void onNext(T item) {
/* 172 */       if (this.state == ChannelSendOperator.State.READY_TO_WRITE) {
/* 173 */         requiredWriteSubscriber().onNext(item);
/*     */         
/*     */         return;
/*     */       } 
/* 177 */       synchronized (this) {
/* 178 */         if (this.state == ChannelSendOperator.State.READY_TO_WRITE) {
/* 179 */           requiredWriteSubscriber().onNext(item);
/*     */         }
/* 181 */         else if (this.state == ChannelSendOperator.State.NEW) {
/* 182 */           Publisher<Void> result; this.item = item;
/* 183 */           this.state = ChannelSendOperator.State.FIRST_SIGNAL_RECEIVED;
/*     */           
/*     */           try {
/* 186 */             result = ChannelSendOperator.this.writeFunction.apply(this);
/*     */           }
/* 188 */           catch (Throwable ex) {
/* 189 */             this.writeCompletionBarrier.onError(ex);
/*     */             return;
/*     */           } 
/* 192 */           result.subscribe((Subscriber)this.writeCompletionBarrier);
/*     */         } else {
/*     */           
/* 195 */           if (this.subscription != null) {
/* 196 */             this.subscription.cancel();
/*     */           }
/* 198 */           this.writeCompletionBarrier.onError(new IllegalStateException("Unexpected item."));
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private Subscriber<? super T> requiredWriteSubscriber() {
/* 204 */       Assert.state((this.writeSubscriber != null), "No write subscriber");
/* 205 */       return this.writeSubscriber;
/*     */     }
/*     */ 
/*     */     
/*     */     public final void onError(Throwable ex) {
/* 210 */       if (this.state == ChannelSendOperator.State.READY_TO_WRITE) {
/* 211 */         requiredWriteSubscriber().onError(ex);
/*     */         return;
/*     */       } 
/* 214 */       synchronized (this) {
/* 215 */         if (this.state == ChannelSendOperator.State.READY_TO_WRITE) {
/* 216 */           requiredWriteSubscriber().onError(ex);
/*     */         }
/* 218 */         else if (this.state == ChannelSendOperator.State.NEW) {
/* 219 */           this.state = ChannelSendOperator.State.FIRST_SIGNAL_RECEIVED;
/* 220 */           this.writeCompletionBarrier.onError(ex);
/*     */         } else {
/*     */           
/* 223 */           this.error = ex;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public final void onComplete() {
/* 230 */       if (this.state == ChannelSendOperator.State.READY_TO_WRITE) {
/* 231 */         requiredWriteSubscriber().onComplete();
/*     */         return;
/*     */       } 
/* 234 */       synchronized (this) {
/* 235 */         if (this.state == ChannelSendOperator.State.READY_TO_WRITE) {
/* 236 */           requiredWriteSubscriber().onComplete();
/*     */         }
/* 238 */         else if (this.state == ChannelSendOperator.State.NEW) {
/* 239 */           Publisher<Void> result; this.completed = true;
/* 240 */           this.state = ChannelSendOperator.State.FIRST_SIGNAL_RECEIVED;
/*     */           
/*     */           try {
/* 243 */             result = ChannelSendOperator.this.writeFunction.apply(this);
/*     */           }
/* 245 */           catch (Throwable ex) {
/* 246 */             this.writeCompletionBarrier.onError(ex);
/*     */             return;
/*     */           } 
/* 249 */           result.subscribe((Subscriber)this.writeCompletionBarrier);
/*     */         } else {
/*     */           
/* 252 */           this.completed = true;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Context currentContext() {
/* 259 */       return this.writeCompletionBarrier.currentContext();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 267 */       Subscription s = this.subscription;
/* 268 */       if (s == null) {
/*     */         return;
/*     */       }
/* 271 */       if (this.state == ChannelSendOperator.State.READY_TO_WRITE) {
/* 272 */         s.request(n);
/*     */         return;
/*     */       } 
/* 275 */       synchronized (this) {
/* 276 */         if (this.writeSubscriber != null) {
/* 277 */           if (this.state == ChannelSendOperator.State.EMITTING_CACHED_SIGNALS) {
/* 278 */             this.demandBeforeReadyToWrite = n;
/*     */             return;
/*     */           } 
/*     */           try {
/* 282 */             this.state = ChannelSendOperator.State.EMITTING_CACHED_SIGNALS;
/* 283 */             if (emitCachedSignals()) {
/*     */               return;
/*     */             }
/* 286 */             n = n + this.demandBeforeReadyToWrite - 1L;
/* 287 */             if (n == 0L) {
/*     */               return;
/*     */             }
/*     */           } finally {
/*     */             
/* 292 */             this.state = ChannelSendOperator.State.READY_TO_WRITE;
/*     */           } 
/*     */         } 
/*     */       } 
/* 296 */       s.request(n);
/*     */     }
/*     */     
/*     */     private boolean emitCachedSignals() {
/* 300 */       if (this.error != null) {
/*     */         try {
/* 302 */           requiredWriteSubscriber().onError(this.error);
/*     */         } finally {
/*     */           
/* 305 */           releaseCachedItem();
/*     */         } 
/* 307 */         return true;
/*     */       } 
/* 309 */       T item = this.item;
/* 310 */       this.item = null;
/* 311 */       if (item != null) {
/* 312 */         requiredWriteSubscriber().onNext(item);
/*     */       }
/* 314 */       if (this.completed) {
/* 315 */         requiredWriteSubscriber().onComplete();
/* 316 */         return true;
/*     */       } 
/* 318 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 323 */       Subscription s = this.subscription;
/* 324 */       if (s != null) {
/* 325 */         this.subscription = null;
/*     */         try {
/* 327 */           s.cancel();
/*     */         } finally {
/*     */           
/* 330 */           releaseCachedItem();
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private void releaseCachedItem() {
/* 336 */       synchronized (this) {
/* 337 */         Object item = this.item;
/* 338 */         if (item instanceof DataBuffer) {
/* 339 */           DataBufferUtils.release((DataBuffer)item);
/*     */         }
/* 341 */         this.item = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void subscribe(Subscriber<? super T> writeSubscriber) {
/* 350 */       synchronized (this) {
/* 351 */         Assert.state((this.writeSubscriber == null), "Only one write subscriber supported");
/* 352 */         this.writeSubscriber = writeSubscriber;
/* 353 */         if (this.error != null || this.completed) {
/* 354 */           this.writeSubscriber.onSubscribe(Operators.emptySubscription());
/* 355 */           emitCachedSignals();
/*     */         } else {
/*     */           
/* 358 */           this.writeSubscriber.onSubscribe(this);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class WriteCompletionBarrier
/*     */     implements CoreSubscriber<Void>, Subscription
/*     */   {
/*     */     private final CoreSubscriber<? super Void> completionSubscriber;
/*     */ 
/*     */ 
/*     */     
/*     */     private final ChannelSendOperator<T>.WriteBarrier writeBarrier;
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private Subscription subscription;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public WriteCompletionBarrier(CoreSubscriber<? super Void> subscriber, ChannelSendOperator<T>.WriteBarrier writeBarrier) {
/* 386 */       this.completionSubscriber = subscriber;
/* 387 */       this.writeBarrier = writeBarrier;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void connect() {
/* 396 */       this.completionSubscriber.onSubscribe(this);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription subscription) {
/* 403 */       this.subscription = subscription;
/* 404 */       subscription.request(Long.MAX_VALUE);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onNext(Void aVoid) {}
/*     */ 
/*     */     
/*     */     public void onError(Throwable ex) {
/*     */       try {
/* 414 */         this.completionSubscriber.onError(ex);
/*     */       } finally {
/*     */         
/* 417 */         this.writeBarrier.releaseCachedItem();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 423 */       this.completionSubscriber.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public Context currentContext() {
/* 428 */       return this.completionSubscriber.currentContext();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void request(long n) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 439 */       this.writeBarrier.cancel();
/* 440 */       Subscription subscription = this.subscription;
/* 441 */       if (subscription != null)
/* 442 */         subscription.cancel(); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/reactive/ChannelSendOperator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */