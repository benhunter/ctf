/*     */ package org.springframework.http.server.reactive;
/*     */ 
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.reactivestreams.Subscriber;
/*     */ import org.reactivestreams.Subscription;
/*     */ import org.springframework.core.log.LogDelegateFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import reactor.core.publisher.Operators;
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
/*     */ 
/*     */ class WriteResultPublisher
/*     */   implements Publisher<Void>
/*     */ {
/*  48 */   private static final Log rsWriteResultLogger = LogDelegateFactory.getHiddenLog(WriteResultPublisher.class);
/*     */ 
/*     */   
/*  51 */   private final AtomicReference<State> state = new AtomicReference<>(State.UNSUBSCRIBED);
/*     */   
/*     */   @Nullable
/*     */   private volatile Subscriber<? super Void> subscriber;
/*     */   
/*     */   private volatile boolean completedBeforeSubscribed;
/*     */   
/*     */   @Nullable
/*     */   private volatile Throwable errorBeforeSubscribed;
/*     */   
/*     */   private final String logPrefix;
/*     */ 
/*     */   
/*     */   public WriteResultPublisher(String logPrefix) {
/*  65 */     this.logPrefix = logPrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void subscribe(Subscriber<? super Void> subscriber) {
/*  71 */     if (rsWriteResultLogger.isTraceEnabled()) {
/*  72 */       rsWriteResultLogger.trace(this.logPrefix + this.state + " subscribe: " + subscriber);
/*     */     }
/*  74 */     ((State)this.state.get()).subscribe(this, subscriber);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void publishComplete() {
/*  81 */     if (rsWriteResultLogger.isTraceEnabled()) {
/*  82 */       rsWriteResultLogger.trace(this.logPrefix + this.state + " publishComplete");
/*     */     }
/*  84 */     ((State)this.state.get()).publishComplete(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void publishError(Throwable t) {
/*  91 */     if (rsWriteResultLogger.isTraceEnabled()) {
/*  92 */       rsWriteResultLogger.trace(this.logPrefix + this.state + " publishError: " + t);
/*     */     }
/*  94 */     ((State)this.state.get()).publishError(this, t);
/*     */   }
/*     */   
/*     */   private boolean changeState(State oldState, State newState) {
/*  98 */     return this.state.compareAndSet(oldState, newState);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class WriteResultSubscription
/*     */     implements Subscription
/*     */   {
/*     */     private final WriteResultPublisher publisher;
/*     */ 
/*     */ 
/*     */     
/*     */     public WriteResultSubscription(WriteResultPublisher publisher) {
/* 111 */       this.publisher = publisher;
/*     */     }
/*     */ 
/*     */     
/*     */     public final void request(long n) {
/* 116 */       if (WriteResultPublisher.rsWriteResultLogger.isTraceEnabled()) {
/* 117 */         WriteResultPublisher.rsWriteResultLogger.trace(this.publisher.logPrefix + state() + " request: " + n);
/*     */       }
/* 119 */       state().request(this.publisher, n);
/*     */     }
/*     */ 
/*     */     
/*     */     public final void cancel() {
/* 124 */       if (WriteResultPublisher.rsWriteResultLogger.isTraceEnabled()) {
/* 125 */         WriteResultPublisher.rsWriteResultLogger.trace(this.publisher.logPrefix + state() + " cancel");
/*     */       }
/* 127 */       state().cancel(this.publisher);
/*     */     }
/*     */     
/*     */     private WriteResultPublisher.State state() {
/* 131 */       return this.publisher.state.get();
/*     */     }
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
/*     */   private enum State
/*     */   {
/* 153 */     UNSUBSCRIBED
/*     */     {
/*     */       void subscribe(WriteResultPublisher publisher, Subscriber<? super Void> subscriber) {
/* 156 */         Assert.notNull(subscriber, "Subscriber must not be null");
/* 157 */         if (publisher.changeState(this, SUBSCRIBING)) {
/* 158 */           Subscription subscription = new WriteResultPublisher.WriteResultSubscription(publisher);
/* 159 */           publisher.subscriber = subscriber;
/* 160 */           subscriber.onSubscribe(subscription);
/* 161 */           publisher.changeState(SUBSCRIBING, SUBSCRIBED);
/*     */           
/* 163 */           if (publisher.completedBeforeSubscribed) {
/* 164 */             publisher.publishComplete();
/*     */           }
/* 166 */           Throwable publisherError = publisher.errorBeforeSubscribed;
/* 167 */           if (publisherError != null) {
/* 168 */             publisher.publishError(publisherError);
/*     */           }
/*     */         } else {
/*     */           
/* 172 */           throw new IllegalStateException(toString());
/*     */         } 
/*     */       }
/*     */       
/*     */       void publishComplete(WriteResultPublisher publisher) {
/* 177 */         publisher.completedBeforeSubscribed = true;
/*     */       }
/*     */       
/*     */       void publishError(WriteResultPublisher publisher, Throwable ex) {
/* 181 */         publisher.errorBeforeSubscribed = ex;
/*     */       }
/*     */     },
/*     */     
/* 185 */     SUBSCRIBING
/*     */     {
/*     */       void request(WriteResultPublisher publisher, long n) {
/* 188 */         Operators.validate(n);
/*     */       }
/*     */       
/*     */       void publishComplete(WriteResultPublisher publisher) {
/* 192 */         publisher.completedBeforeSubscribed = true;
/*     */       }
/*     */       
/*     */       void publishError(WriteResultPublisher publisher, Throwable ex) {
/* 196 */         publisher.errorBeforeSubscribed = ex;
/*     */       }
/*     */     },
/*     */     
/* 200 */     SUBSCRIBED
/*     */     {
/*     */       void request(WriteResultPublisher publisher, long n) {
/* 203 */         Operators.validate(n);
/*     */       }
/*     */     },
/*     */     
/* 207 */     COMPLETED
/*     */     {
/*     */       void request(WriteResultPublisher publisher, long n) {}
/*     */ 
/*     */ 
/*     */       
/*     */       void cancel(WriteResultPublisher publisher) {}
/*     */ 
/*     */ 
/*     */       
/*     */       void publishComplete(WriteResultPublisher publisher) {}
/*     */ 
/*     */ 
/*     */       
/*     */       void publishError(WriteResultPublisher publisher, Throwable t) {}
/*     */     };
/*     */ 
/*     */ 
/*     */     
/*     */     void subscribe(WriteResultPublisher publisher, Subscriber<? super Void> subscriber) {
/* 227 */       throw new IllegalStateException(toString());
/*     */     }
/*     */     
/*     */     void request(WriteResultPublisher publisher, long n) {
/* 231 */       throw new IllegalStateException(toString());
/*     */     }
/*     */     
/*     */     void cancel(WriteResultPublisher publisher) {
/* 235 */       if (!publisher.changeState(this, COMPLETED)) {
/* 236 */         ((State)publisher.state.get()).cancel(publisher);
/*     */       }
/*     */     }
/*     */     
/*     */     void publishComplete(WriteResultPublisher publisher) {
/* 241 */       if (publisher.changeState(this, COMPLETED)) {
/* 242 */         Subscriber<? super Void> s = publisher.subscriber;
/* 243 */         Assert.state((s != null), "No subscriber");
/* 244 */         s.onComplete();
/*     */       } else {
/*     */         
/* 247 */         ((State)publisher.state.get()).publishComplete(publisher);
/*     */       } 
/*     */     }
/*     */     
/*     */     void publishError(WriteResultPublisher publisher, Throwable t) {
/* 252 */       if (publisher.changeState(this, COMPLETED)) {
/* 253 */         Subscriber<? super Void> s = publisher.subscriber;
/* 254 */         Assert.state((s != null), "No subscriber");
/* 255 */         s.onError(t);
/*     */       } else {
/*     */         
/* 258 */         ((State)publisher.state.get()).publishError(publisher, t);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/reactive/WriteResultPublisher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */