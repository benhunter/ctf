/*     */ package org.springframework.http.server.reactive;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.atomic.AtomicLongFieldUpdater;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractListenerReadPublisher<T>
/*     */   implements Publisher<T>
/*     */ {
/*  57 */   protected static Log rsReadLogger = LogDelegateFactory.getHiddenLog(AbstractListenerReadPublisher.class);
/*     */ 
/*     */   
/*  60 */   private final AtomicReference<State> state = new AtomicReference<>(State.UNSUBSCRIBED);
/*     */ 
/*     */   
/*     */   private volatile long demand;
/*     */ 
/*     */   
/*  66 */   private static final AtomicLongFieldUpdater<AbstractListenerReadPublisher> DEMAND_FIELD_UPDATER = AtomicLongFieldUpdater.newUpdater(AbstractListenerReadPublisher.class, "demand");
/*     */   
/*     */   @Nullable
/*     */   private volatile Subscriber<? super T> subscriber;
/*     */   
/*     */   private volatile boolean completionBeforeDemand;
/*     */   
/*     */   @Nullable
/*     */   private volatile Throwable errorBeforeDemand;
/*     */   
/*     */   private final String logPrefix;
/*     */ 
/*     */   
/*     */   public AbstractListenerReadPublisher() {
/*  80 */     this("");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractListenerReadPublisher(String logPrefix) {
/*  88 */     this.logPrefix = logPrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLogPrefix() {
/*  97 */     return this.logPrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void subscribe(Subscriber<? super T> subscriber) {
/* 105 */     ((State)this.state.get()).subscribe(this, subscriber);
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
/*     */   public final void onDataAvailable() {
/* 117 */     rsReadLogger.trace(getLogPrefix() + "onDataAvailable");
/* 118 */     ((State)this.state.get()).onDataAvailable(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onAllDataRead() {
/* 126 */     rsReadLogger.trace(getLogPrefix() + "onAllDataRead");
/* 127 */     ((State)this.state.get()).onAllDataRead(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void onError(Throwable ex) {
/* 134 */     if (rsReadLogger.isTraceEnabled()) {
/* 135 */       rsReadLogger.trace(getLogPrefix() + "Connection error: " + ex);
/*     */     }
/* 137 */     ((State)this.state.get()).onError(this, ex);
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
/*     */   private boolean readAndPublish() throws IOException {
/*     */     long r;
/* 185 */     while ((r = this.demand) > 0L && !((State)this.state.get()).equals(State.COMPLETED)) {
/* 186 */       T data = read();
/* 187 */       if (data != null) {
/* 188 */         if (r != Long.MAX_VALUE) {
/* 189 */           DEMAND_FIELD_UPDATER.addAndGet(this, -1L);
/*     */         }
/* 191 */         Subscriber<? super T> subscriber = this.subscriber;
/* 192 */         Assert.state((subscriber != null), "No subscriber");
/* 193 */         if (rsReadLogger.isTraceEnabled()) {
/* 194 */           rsReadLogger.trace(getLogPrefix() + "Publishing data read");
/*     */         }
/* 196 */         subscriber.onNext(data);
/*     */         continue;
/*     */       } 
/* 199 */       if (rsReadLogger.isTraceEnabled()) {
/* 200 */         rsReadLogger.trace(getLogPrefix() + "No more data to read");
/*     */       }
/* 202 */       return true;
/*     */     } 
/*     */     
/* 205 */     return false;
/*     */   }
/*     */   
/*     */   private boolean changeState(State oldState, State newState) {
/* 209 */     boolean result = this.state.compareAndSet(oldState, newState);
/* 210 */     if (result && rsReadLogger.isTraceEnabled()) {
/* 211 */       rsReadLogger.trace(getLogPrefix() + oldState + " -> " + newState);
/*     */     }
/* 213 */     return result;
/*     */   }
/*     */   
/*     */   private void changeToDemandState(State oldState) {
/* 217 */     if (changeState(oldState, State.DEMAND))
/*     */     {
/*     */ 
/*     */       
/* 221 */       if (!oldState.equals(State.READING))
/* 222 */         checkOnDataAvailable();  } 
/*     */   }
/*     */   protected abstract void checkOnDataAvailable();
/*     */   @Nullable
/*     */   protected abstract T read() throws IOException;
/*     */   private Subscription createSubscription() {
/* 228 */     return new ReadSubscription();
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract void readingPaused();
/*     */   
/*     */   protected abstract void discardData();
/*     */   
/*     */   private final class ReadSubscription
/*     */     implements Subscription
/*     */   {
/*     */     public final void request(long n) {
/* 240 */       if (AbstractListenerReadPublisher.rsReadLogger.isTraceEnabled()) {
/* 241 */         AbstractListenerReadPublisher.rsReadLogger.trace(AbstractListenerReadPublisher.this.getLogPrefix() + n + " requested");
/*     */       }
/* 243 */       ((AbstractListenerReadPublisher.State)AbstractListenerReadPublisher.this.state.get()).request(AbstractListenerReadPublisher.this, n);
/*     */     }
/*     */     private ReadSubscription() {}
/*     */     
/*     */     public final void cancel() {
/* 248 */       if (AbstractListenerReadPublisher.rsReadLogger.isTraceEnabled()) {
/* 249 */         AbstractListenerReadPublisher.rsReadLogger.trace(AbstractListenerReadPublisher.this.getLogPrefix() + "Cancellation");
/*     */       }
/* 251 */       ((AbstractListenerReadPublisher.State)AbstractListenerReadPublisher.this.state.get()).cancel(AbstractListenerReadPublisher.this);
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
/*     */ 
/*     */ 
/*     */   
/*     */   private enum State
/*     */   {
/* 276 */     UNSUBSCRIBED
/*     */     {
/*     */       <T> void subscribe(AbstractListenerReadPublisher<T> publisher, Subscriber<? super T> subscriber) {
/* 279 */         Assert.notNull(publisher, "Publisher must not be null");
/* 280 */         Assert.notNull(subscriber, "Subscriber must not be null");
/* 281 */         if (publisher.changeState(this, SUBSCRIBING)) {
/* 282 */           Subscription subscription = publisher.createSubscription();
/* 283 */           publisher.subscriber = subscriber;
/* 284 */           subscriber.onSubscribe(subscription);
/* 285 */           publisher.changeState(SUBSCRIBING, NO_DEMAND);
/*     */           
/* 287 */           String logPrefix = publisher.getLogPrefix();
/* 288 */           if (publisher.completionBeforeDemand) {
/* 289 */             AbstractListenerReadPublisher.rsReadLogger.trace(logPrefix + "Completed before demand");
/* 290 */             ((State)publisher.state.get()).onAllDataRead(publisher);
/*     */           } 
/* 292 */           Throwable ex = publisher.errorBeforeDemand;
/* 293 */           if (ex != null) {
/* 294 */             if (AbstractListenerReadPublisher.rsReadLogger.isTraceEnabled()) {
/* 295 */               AbstractListenerReadPublisher.rsReadLogger.trace(logPrefix + "Completed with error before demand: " + ex);
/*     */             }
/* 297 */             ((State)publisher.state.get()).onError(publisher, ex);
/*     */           } 
/*     */         } else {
/*     */           
/* 301 */           throw new IllegalStateException("Failed to transition to SUBSCRIBING, subscriber: " + subscriber);
/*     */         } 
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       <T> void onAllDataRead(AbstractListenerReadPublisher<T> publisher) {
/* 308 */         publisher.completionBeforeDemand = true;
/*     */       }
/*     */ 
/*     */       
/*     */       <T> void onError(AbstractListenerReadPublisher<T> publisher, Throwable ex) {
/* 313 */         publisher.errorBeforeDemand = ex;
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 321 */     SUBSCRIBING
/*     */     {
/*     */       <T> void request(AbstractListenerReadPublisher<T> publisher, long n) {
/* 324 */         if (Operators.validate(n)) {
/* 325 */           Operators.addCap(AbstractListenerReadPublisher.DEMAND_FIELD_UPDATER, publisher, n);
/* 326 */           publisher.changeToDemandState(this);
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*     */       <T> void onAllDataRead(AbstractListenerReadPublisher<T> publisher) {
/* 332 */         publisher.completionBeforeDemand = true;
/*     */       }
/*     */ 
/*     */       
/*     */       <T> void onError(AbstractListenerReadPublisher<T> publisher, Throwable ex) {
/* 337 */         publisher.errorBeforeDemand = ex;
/*     */       }
/*     */     },
/*     */     
/* 341 */     NO_DEMAND
/*     */     {
/*     */       <T> void request(AbstractListenerReadPublisher<T> publisher, long n) {
/* 344 */         if (Operators.validate(n)) {
/* 345 */           Operators.addCap(AbstractListenerReadPublisher.DEMAND_FIELD_UPDATER, publisher, n);
/* 346 */           publisher.changeToDemandState(this);
/*     */         }
/*     */       
/*     */       }
/*     */     },
/* 351 */     DEMAND
/*     */     {
/*     */       <T> void request(AbstractListenerReadPublisher<T> publisher, long n) {
/* 354 */         if (Operators.validate(n)) {
/* 355 */           Operators.addCap(AbstractListenerReadPublisher.DEMAND_FIELD_UPDATER, publisher, n);
/*     */           
/* 357 */           publisher.changeToDemandState(NO_DEMAND);
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*     */       <T> void onDataAvailable(AbstractListenerReadPublisher<T> publisher) {
/* 363 */         if (publisher.changeState(this, READING)) {
/*     */           try {
/* 365 */             boolean demandAvailable = publisher.readAndPublish();
/* 366 */             if (demandAvailable) {
/* 367 */               publisher.changeToDemandState(READING);
/*     */             } else {
/*     */               
/* 370 */               publisher.readingPaused();
/* 371 */               if (publisher.changeState(READING, NO_DEMAND))
/*     */               {
/* 373 */                 long r = publisher.demand;
/* 374 */                 if (r > 0L) {
/* 375 */                   publisher.changeToDemandState(NO_DEMAND);
/*     */                 }
/*     */               }
/*     */             
/*     */             } 
/* 380 */           } catch (IOException ex) {
/* 381 */             publisher.onError(ex);
/*     */           
/*     */           }
/*     */         
/*     */         }
/*     */       }
/*     */     },
/* 388 */     READING
/*     */     {
/*     */       <T> void request(AbstractListenerReadPublisher<T> publisher, long n) {
/* 391 */         if (Operators.validate(n)) {
/* 392 */           Operators.addCap(AbstractListenerReadPublisher.DEMAND_FIELD_UPDATER, publisher, n);
/*     */           
/* 394 */           publisher.changeToDemandState(NO_DEMAND);
/*     */         }
/*     */       
/*     */       }
/*     */     },
/* 399 */     COMPLETED
/*     */     {
/*     */       <T> void request(AbstractListenerReadPublisher<T> publisher, long n) {}
/*     */ 
/*     */ 
/*     */       
/*     */       <T> void cancel(AbstractListenerReadPublisher<T> publisher) {}
/*     */ 
/*     */ 
/*     */       
/*     */       <T> void onAllDataRead(AbstractListenerReadPublisher<T> publisher) {}
/*     */ 
/*     */ 
/*     */       
/*     */       <T> void onError(AbstractListenerReadPublisher<T> publisher, Throwable t) {}
/*     */     };
/*     */ 
/*     */ 
/*     */     
/*     */     <T> void subscribe(AbstractListenerReadPublisher<T> publisher, Subscriber<? super T> subscriber) {
/* 419 */       throw new IllegalStateException(toString());
/*     */     }
/*     */     
/*     */     <T> void request(AbstractListenerReadPublisher<T> publisher, long n) {
/* 423 */       throw new IllegalStateException(toString());
/*     */     }
/*     */     
/*     */     <T> void cancel(AbstractListenerReadPublisher<T> publisher) {
/* 427 */       if (publisher.changeState(this, COMPLETED)) {
/* 428 */         publisher.discardData();
/*     */       } else {
/*     */         
/* 431 */         ((State)publisher.state.get()).cancel(publisher);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     <T> void onDataAvailable(AbstractListenerReadPublisher<T> publisher) {}
/*     */ 
/*     */     
/*     */     <T> void onAllDataRead(AbstractListenerReadPublisher<T> publisher) {
/* 440 */       if (publisher.changeState(this, COMPLETED)) {
/* 441 */         Subscriber<? super T> s = publisher.subscriber;
/* 442 */         if (s != null) {
/* 443 */           s.onComplete();
/*     */         }
/*     */       } else {
/*     */         
/* 447 */         ((State)publisher.state.get()).onAllDataRead(publisher);
/*     */       } 
/*     */     }
/*     */     
/*     */     <T> void onError(AbstractListenerReadPublisher<T> publisher, Throwable t) {
/* 452 */       if (publisher.changeState(this, COMPLETED)) {
/* 453 */         publisher.discardData();
/* 454 */         Subscriber<? super T> s = publisher.subscriber;
/* 455 */         if (s != null) {
/* 456 */           s.onError(t);
/*     */         }
/*     */       } else {
/*     */         
/* 460 */         ((State)publisher.state.get()).onError(publisher, t);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/reactive/AbstractListenerReadPublisher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */