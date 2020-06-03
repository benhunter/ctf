/*     */ package org.springframework.http.server.reactive;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.reactivestreams.Processor;
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
/*     */ 
/*     */ 
/*     */ public abstract class AbstractListenerWriteProcessor<T>
/*     */   implements Processor<T, Void>
/*     */ {
/*  54 */   protected static final Log rsWriteLogger = LogDelegateFactory.getHiddenLog(AbstractListenerWriteProcessor.class);
/*     */ 
/*     */   
/*  57 */   private final AtomicReference<State> state = new AtomicReference<>(State.UNSUBSCRIBED);
/*     */   
/*     */   @Nullable
/*     */   private Subscription subscription;
/*     */   
/*     */   @Nullable
/*     */   private volatile T currentData;
/*     */   
/*     */   private volatile boolean subscriberCompleted;
/*     */   
/*     */   private final WriteResultPublisher resultPublisher;
/*     */   
/*     */   private final String logPrefix;
/*     */ 
/*     */   
/*     */   public AbstractListenerWriteProcessor() {
/*  73 */     this("");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractListenerWriteProcessor(String logPrefix) {
/*  81 */     this.logPrefix = logPrefix;
/*  82 */     this.resultPublisher = new WriteResultPublisher(logPrefix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLogPrefix() {
/*  91 */     return this.logPrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void onSubscribe(Subscription subscription) {
/*  99 */     ((State)this.state.get()).onSubscribe(this, subscription);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void onNext(T data) {
/* 104 */     if (rsWriteLogger.isTraceEnabled()) {
/* 105 */       rsWriteLogger.trace(getLogPrefix() + "Item to write");
/*     */     }
/* 107 */     ((State)this.state.get()).onNext(this, data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void onError(Throwable ex) {
/* 116 */     if (rsWriteLogger.isTraceEnabled()) {
/* 117 */       rsWriteLogger.trace(getLogPrefix() + "Write source error: " + ex);
/*     */     }
/* 119 */     ((State)this.state.get()).onError(this, ex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void onComplete() {
/* 128 */     if (rsWriteLogger.isTraceEnabled()) {
/* 129 */       rsWriteLogger.trace(getLogPrefix() + "No more items to write");
/*     */     }
/* 131 */     ((State)this.state.get()).onComplete(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void onWritePossible() {
/* 140 */     if (rsWriteLogger.isTraceEnabled()) {
/* 141 */       rsWriteLogger.trace(getLogPrefix() + "onWritePossible");
/*     */     }
/* 143 */     ((State)this.state.get()).onWritePossible(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cancel() {
/* 151 */     rsWriteLogger.trace(getLogPrefix() + "Cancellation");
/* 152 */     if (this.subscription != null) {
/* 153 */       this.subscription.cancel();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void subscribe(Subscriber<? super Void> subscriber) {
/* 164 */     this.resultPublisher.subscribe(subscriber);
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
/*     */   protected void dataReceived(T data) {
/* 182 */     T prev = this.currentData;
/* 183 */     if (prev != null) {
/*     */ 
/*     */ 
/*     */       
/* 187 */       discardData(data);
/* 188 */       cancel();
/* 189 */       onError(new IllegalStateException("Received new data while current not processed yet."));
/*     */     } 
/* 191 */     this.currentData = data;
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
/*     */   @Deprecated
/*     */   protected void writingPaused() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writingComplete() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writingFailed(Throwable ex) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/* 252 */     boolean result = this.state.compareAndSet(oldState, newState);
/* 253 */     if (result && rsWriteLogger.isTraceEnabled()) {
/* 254 */       rsWriteLogger.trace(getLogPrefix() + oldState + " -> " + newState);
/*     */     }
/* 256 */     return result;
/*     */   }
/*     */   
/*     */   private void changeStateToReceived(State oldState) {
/* 260 */     if (changeState(oldState, State.RECEIVED)) {
/* 261 */       writeIfPossible();
/*     */     }
/*     */   }
/*     */   
/*     */   private void changeStateToComplete(State oldState) {
/* 266 */     if (changeState(oldState, State.COMPLETED)) {
/* 267 */       discardCurrentData();
/* 268 */       writingComplete();
/* 269 */       this.resultPublisher.publishComplete();
/*     */     } else {
/*     */       
/* 272 */       ((State)this.state.get()).onComplete(this);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeIfPossible() {
/* 277 */     boolean result = isWritePossible();
/* 278 */     if (!result && rsWriteLogger.isTraceEnabled()) {
/* 279 */       rsWriteLogger.trace(getLogPrefix() + "isWritePossible: false");
/*     */     }
/* 281 */     if (result) {
/* 282 */       onWritePossible();
/*     */     }
/*     */   }
/*     */   
/*     */   private void discardCurrentData() {
/* 287 */     T data = this.currentData;
/* 288 */     this.currentData = null;
/* 289 */     if (data != null) {
/* 290 */       discardData(data);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean isDataEmpty(T paramT);
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean isWritePossible();
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean write(T paramT) throws IOException;
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void discardData(T paramT);
/*     */ 
/*     */   
/*     */   private enum State
/*     */   {
/* 313 */     UNSUBSCRIBED
/*     */     {
/*     */       public <T> void onSubscribe(AbstractListenerWriteProcessor<T> processor, Subscription subscription) {
/* 316 */         Assert.notNull(subscription, "Subscription must not be null");
/* 317 */         if (processor.changeState(this, REQUESTED)) {
/* 318 */           processor.subscription = subscription;
/* 319 */           subscription.request(1L);
/*     */         } else {
/*     */           
/* 322 */           super.onSubscribe(processor, subscription);
/*     */         }
/*     */       
/*     */       }
/*     */     },
/* 327 */     REQUESTED
/*     */     {
/*     */       public <T> void onNext(AbstractListenerWriteProcessor<T> processor, T data) {
/* 330 */         if (processor.isDataEmpty(data)) {
/* 331 */           Assert.state((processor.subscription != null), "No subscription");
/* 332 */           processor.subscription.request(1L);
/*     */         } else {
/*     */           
/* 335 */           processor.dataReceived(data);
/* 336 */           processor.changeStateToReceived(this);
/*     */         } 
/*     */       }
/*     */       
/*     */       public <T> void onComplete(AbstractListenerWriteProcessor<T> processor) {
/* 341 */         processor.changeStateToComplete(this);
/*     */       }
/*     */     },
/*     */     
/* 345 */     RECEIVED
/*     */     {
/*     */       public <T> void onWritePossible(AbstractListenerWriteProcessor<T> processor)
/*     */       {
/* 349 */         if (processor.changeState(this, WRITING)) {
/* 350 */           T data = processor.currentData;
/* 351 */           Assert.state((data != null), "No data");
/*     */           try {
/* 353 */             if (processor.write(data)) {
/* 354 */               if (processor.changeState(WRITING, REQUESTED)) {
/* 355 */                 processor.currentData = null;
/* 356 */                 if (processor.subscriberCompleted) {
/* 357 */                   processor.changeStateToComplete(REQUESTED);
/*     */                 } else {
/*     */                   
/* 360 */                   processor.writingPaused();
/* 361 */                   Assert.state((processor.subscription != null), "No subscription");
/* 362 */                   processor.subscription.request(1L);
/*     */                 } 
/*     */               } 
/*     */             } else {
/*     */               
/* 367 */               processor.changeStateToReceived(WRITING);
/*     */             }
/*     */           
/* 370 */           } catch (IOException ex) {
/* 371 */             processor.writingFailed(ex);
/*     */           } 
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> void onComplete(AbstractListenerWriteProcessor<T> processor) {
/* 378 */         processor.subscriberCompleted = true;
/*     */       }
/*     */     },
/*     */     
/* 382 */     WRITING
/*     */     {
/*     */       public <T> void onComplete(AbstractListenerWriteProcessor<T> processor) {
/* 385 */         processor.subscriberCompleted = true;
/*     */       }
/*     */     },
/*     */     
/* 389 */     COMPLETED
/*     */     {
/*     */       public <T> void onNext(AbstractListenerWriteProcessor<T> processor, T data) {}
/*     */ 
/*     */ 
/*     */       
/*     */       public <T> void onError(AbstractListenerWriteProcessor<T> processor, Throwable ex) {}
/*     */ 
/*     */ 
/*     */       
/*     */       public <T> void onComplete(AbstractListenerWriteProcessor<T> processor) {}
/*     */     };
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> void onSubscribe(AbstractListenerWriteProcessor<T> processor, Subscription subscription) {
/* 405 */       subscription.cancel();
/*     */     }
/*     */     
/*     */     public <T> void onNext(AbstractListenerWriteProcessor<T> processor, T data) {
/* 409 */       processor.discardData(data);
/* 410 */       processor.cancel();
/* 411 */       processor.onError(new IllegalStateException("Illegal onNext without demand"));
/*     */     }
/*     */     
/*     */     public <T> void onError(AbstractListenerWriteProcessor<T> processor, Throwable ex) {
/* 415 */       if (processor.changeState(this, COMPLETED)) {
/* 416 */         processor.discardCurrentData();
/* 417 */         processor.writingComplete();
/* 418 */         processor.resultPublisher.publishError(ex);
/*     */       } else {
/*     */         
/* 421 */         ((State)processor.state.get()).onError(processor, ex);
/*     */       } 
/*     */     }
/*     */     
/*     */     public <T> void onComplete(AbstractListenerWriteProcessor<T> processor) {
/* 426 */       throw new IllegalStateException(toString());
/*     */     }
/*     */     
/*     */     public <T> void onWritePossible(AbstractListenerWriteProcessor<T> processor) {}
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/reactive/AbstractListenerWriteProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */