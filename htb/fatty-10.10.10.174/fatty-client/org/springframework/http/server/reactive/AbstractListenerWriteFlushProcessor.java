/*     */ package org.springframework.http.server.reactive;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.reactivestreams.Processor;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.reactivestreams.Subscriber;
/*     */ import org.reactivestreams.Subscription;
/*     */ import org.springframework.core.log.LogDelegateFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public abstract class AbstractListenerWriteFlushProcessor<T>
/*     */   implements Processor<Publisher<? extends T>, Void>
/*     */ {
/*  53 */   protected static final Log rsWriteFlushLogger = LogDelegateFactory.getHiddenLog(AbstractListenerWriteFlushProcessor.class);
/*     */ 
/*     */   
/*  56 */   private final AtomicReference<State> state = new AtomicReference<>(State.UNSUBSCRIBED);
/*     */   
/*     */   @Nullable
/*     */   private Subscription subscription;
/*     */   
/*     */   private volatile boolean subscriberCompleted;
/*     */   
/*     */   private final WriteResultPublisher resultPublisher;
/*     */   
/*     */   private final String logPrefix;
/*     */ 
/*     */   
/*     */   public AbstractListenerWriteFlushProcessor() {
/*  69 */     this("");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractListenerWriteFlushProcessor(String logPrefix) {
/*  77 */     this.logPrefix = logPrefix;
/*  78 */     this.resultPublisher = new WriteResultPublisher(logPrefix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLogPrefix() {
/*  87 */     return this.logPrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void onSubscribe(Subscription subscription) {
/*  95 */     ((State)this.state.get()).onSubscribe(this, subscription);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void onNext(Publisher<? extends T> publisher) {
/* 100 */     if (rsWriteFlushLogger.isTraceEnabled()) {
/* 101 */       rsWriteFlushLogger.trace(getLogPrefix() + "Received onNext publisher");
/*     */     }
/* 103 */     ((State)this.state.get()).onNext(this, publisher);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void onError(Throwable ex) {
/* 112 */     if (rsWriteFlushLogger.isTraceEnabled()) {
/* 113 */       rsWriteFlushLogger.trace(getLogPrefix() + "Received onError: " + ex);
/*     */     }
/* 115 */     ((State)this.state.get()).onError(this, ex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void onComplete() {
/* 124 */     if (rsWriteFlushLogger.isTraceEnabled()) {
/* 125 */       rsWriteFlushLogger.trace(getLogPrefix() + "Received onComplete");
/*     */     }
/* 127 */     ((State)this.state.get()).onComplete(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void onFlushPossible() {
/* 136 */     ((State)this.state.get()).onFlushPossible(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void cancel() {
/* 144 */     if (rsWriteFlushLogger.isTraceEnabled()) {
/* 145 */       rsWriteFlushLogger.trace(getLogPrefix() + "Received request to cancel");
/*     */     }
/* 147 */     if (this.subscription != null) {
/* 148 */       this.subscription.cancel();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void subscribe(Subscriber<? super Void> subscriber) {
/* 157 */     this.resultPublisher.subscribe(subscriber);
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
/*     */   protected void flushingFailed(Throwable t) {}
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
/*     */   private boolean changeState(State oldState, State newState) {
/* 201 */     boolean result = this.state.compareAndSet(oldState, newState);
/* 202 */     if (result && rsWriteFlushLogger.isTraceEnabled()) {
/* 203 */       rsWriteFlushLogger.trace(getLogPrefix() + oldState + " -> " + newState);
/*     */     }
/* 205 */     return result;
/*     */   }
/*     */   
/*     */   private void flushIfPossible() {
/* 209 */     boolean result = isWritePossible();
/* 210 */     if (rsWriteFlushLogger.isTraceEnabled()) {
/* 211 */       rsWriteFlushLogger.trace(getLogPrefix() + "isWritePossible[" + result + "]");
/*     */     }
/* 213 */     if (result) {
/* 214 */       onFlushPossible();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Processor<? super T, Void> createWriteProcessor();
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean isWritePossible();
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void flush() throws IOException;
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean isFlushPending();
/*     */ 
/*     */   
/*     */   private enum State
/*     */   {
/* 237 */     UNSUBSCRIBED
/*     */     {
/*     */       public <T> void onSubscribe(AbstractListenerWriteFlushProcessor<T> processor, Subscription subscription) {
/* 240 */         Assert.notNull(subscription, "Subscription must not be null");
/* 241 */         if (processor.changeState(this, REQUESTED)) {
/* 242 */           processor.subscription = subscription;
/* 243 */           subscription.request(1L);
/*     */         } else {
/*     */           
/* 246 */           super.onSubscribe(processor, subscription);
/*     */         }
/*     */       
/*     */       }
/*     */     },
/* 251 */     REQUESTED
/*     */     {
/*     */       
/*     */       public <T> void onNext(AbstractListenerWriteFlushProcessor<T> processor, Publisher<? extends T> currentPublisher)
/*     */       {
/* 256 */         if (processor.changeState(this, RECEIVED)) {
/* 257 */           Processor<? super T, Void> currentProcessor = processor.createWriteProcessor();
/* 258 */           currentPublisher.subscribe((Subscriber)currentProcessor);
/* 259 */           currentProcessor.subscribe(new WriteResultSubscriber(processor));
/*     */         } 
/*     */       }
/*     */       
/*     */       public <T> void onComplete(AbstractListenerWriteFlushProcessor<T> processor) {
/* 264 */         if (processor.changeState(this, COMPLETED)) {
/* 265 */           processor.resultPublisher.publishComplete();
/*     */         } else {
/*     */           
/* 268 */           ((State)processor.state.get()).onComplete(processor);
/*     */         }
/*     */       
/*     */       }
/*     */     },
/* 273 */     RECEIVED
/*     */     {
/*     */       public <T> void writeComplete(AbstractListenerWriteFlushProcessor<T> processor) {
/*     */         try {
/* 277 */           processor.flush();
/*     */         }
/* 279 */         catch (Throwable ex) {
/* 280 */           processor.flushingFailed(ex);
/*     */           return;
/*     */         } 
/* 283 */         if (processor.changeState(this, REQUESTED)) {
/* 284 */           if (processor.subscriberCompleted) {
/* 285 */             if (processor.isFlushPending()) {
/*     */               
/* 287 */               processor.changeState(REQUESTED, FLUSHING);
/* 288 */               processor.flushIfPossible();
/*     */             }
/* 290 */             else if (processor.changeState(REQUESTED, COMPLETED)) {
/* 291 */               processor.resultPublisher.publishComplete();
/*     */             } else {
/*     */               
/* 294 */               ((State)processor.state.get()).onComplete(processor);
/*     */             } 
/*     */           } else {
/*     */             
/* 298 */             Assert.state((processor.subscription != null), "No subscription");
/* 299 */             processor.subscription.request(1L);
/*     */           } 
/*     */         }
/*     */       }
/*     */       
/*     */       public <T> void onComplete(AbstractListenerWriteFlushProcessor<T> processor) {
/* 305 */         processor.subscriberCompleted = true;
/*     */       }
/*     */     },
/*     */     
/* 309 */     FLUSHING
/*     */     {
/*     */       public <T> void onFlushPossible(AbstractListenerWriteFlushProcessor<T> processor) {
/*     */         try {
/* 313 */           processor.flush();
/*     */         }
/* 315 */         catch (Throwable ex) {
/* 316 */           processor.flushingFailed(ex);
/*     */           return;
/*     */         } 
/* 319 */         if (processor.changeState(this, COMPLETED)) {
/* 320 */           processor.resultPublisher.publishComplete();
/*     */         } else {
/*     */           
/* 323 */           ((State)processor.state.get()).onComplete(processor);
/*     */         } 
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public <T> void onNext(AbstractListenerWriteFlushProcessor<T> proc, Publisher<? extends T> pub) {}
/*     */ 
/*     */ 
/*     */       
/*     */       public <T> void onComplete(AbstractListenerWriteFlushProcessor<T> processor) {}
/*     */     },
/* 336 */     COMPLETED
/*     */     {
/*     */       public <T> void onNext(AbstractListenerWriteFlushProcessor<T> proc, Publisher<? extends T> pub) {}
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public <T> void onError(AbstractListenerWriteFlushProcessor<T> processor, Throwable t) {}
/*     */ 
/*     */ 
/*     */       
/*     */       public <T> void onComplete(AbstractListenerWriteFlushProcessor<T> processor) {}
/*     */     };
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> void onSubscribe(AbstractListenerWriteFlushProcessor<T> proc, Subscription subscription) {
/* 353 */       subscription.cancel();
/*     */     }
/*     */     
/*     */     public <T> void onNext(AbstractListenerWriteFlushProcessor<T> proc, Publisher<? extends T> pub) {
/* 357 */       throw new IllegalStateException(toString());
/*     */     }
/*     */     
/*     */     public <T> void onError(AbstractListenerWriteFlushProcessor<T> processor, Throwable ex) {
/* 361 */       if (processor.changeState(this, COMPLETED)) {
/* 362 */         processor.resultPublisher.publishError(ex);
/*     */       } else {
/*     */         
/* 365 */         ((State)processor.state.get()).onError(processor, ex);
/*     */       } 
/*     */     }
/*     */     
/*     */     public <T> void onComplete(AbstractListenerWriteFlushProcessor<T> processor) {
/* 370 */       throw new IllegalStateException(toString());
/*     */     }
/*     */     
/*     */     public <T> void writeComplete(AbstractListenerWriteFlushProcessor<T> processor) {
/* 374 */       throw new IllegalStateException(toString());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> void onFlushPossible(AbstractListenerWriteFlushProcessor<T> processor) {}
/*     */ 
/*     */ 
/*     */     
/*     */     private static class WriteResultSubscriber
/*     */       implements Subscriber<Void>
/*     */     {
/*     */       private final AbstractListenerWriteFlushProcessor<?> processor;
/*     */ 
/*     */ 
/*     */       
/*     */       public WriteResultSubscriber(AbstractListenerWriteFlushProcessor<?> processor) {
/* 392 */         this.processor = processor;
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSubscribe(Subscription subscription) {
/* 397 */         subscription.request(Long.MAX_VALUE);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void onNext(Void aVoid) {}
/*     */ 
/*     */       
/*     */       public void onError(Throwable ex) {
/* 406 */         this.processor.cancel();
/* 407 */         this.processor.onError(ex);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onComplete() {
/* 412 */         if (AbstractListenerWriteFlushProcessor.rsWriteFlushLogger.isTraceEnabled()) {
/* 413 */           AbstractListenerWriteFlushProcessor.rsWriteFlushLogger.trace(this.processor.getLogPrefix() + this.processor.state + " writeComplete");
/*     */         }
/* 415 */         ((AbstractListenerWriteFlushProcessor.State)this.processor.state.get()).writeComplete(this.processor);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/reactive/AbstractListenerWriteFlushProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */