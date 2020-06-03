/*    */ package org.springframework.http.server.reactive;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicBoolean;
/*    */ import org.reactivestreams.Processor;
/*    */ import org.reactivestreams.Publisher;
/*    */ import org.reactivestreams.Subscriber;
/*    */ import org.springframework.core.io.buffer.DataBuffer;
/*    */ import org.springframework.core.io.buffer.DataBufferFactory;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import reactor.core.publisher.Mono;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractListenerServerHttpResponse
/*    */   extends AbstractServerHttpResponse
/*    */ {
/* 38 */   private final AtomicBoolean writeCalled = new AtomicBoolean();
/*    */ 
/*    */   
/*    */   public AbstractListenerServerHttpResponse(DataBufferFactory dataBufferFactory) {
/* 42 */     super(dataBufferFactory);
/*    */   }
/*    */   
/*    */   public AbstractListenerServerHttpResponse(DataBufferFactory dataBufferFactory, HttpHeaders headers) {
/* 46 */     super(dataBufferFactory, headers);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected final Mono<Void> writeWithInternal(Publisher<? extends DataBuffer> body) {
/* 52 */     return writeAndFlushWithInternal((Publisher<? extends Publisher<? extends DataBuffer>>)Mono.just(body));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected final Mono<Void> writeAndFlushWithInternal(Publisher<? extends Publisher<? extends DataBuffer>> body) {
/* 59 */     if (this.writeCalled.compareAndSet(false, true)) {
/* 60 */       Processor<? super Publisher<? extends DataBuffer>, Void> processor = createBodyFlushProcessor();
/* 61 */       return Mono.from(subscriber -> {
/*    */             body.subscribe((Subscriber)processor);
/*    */             processor.subscribe(subscriber);
/*    */           });
/*    */     } 
/* 66 */     return Mono.error(new IllegalStateException("writeWith() or writeAndFlushWith() has already been called"));
/*    */   }
/*    */   
/*    */   protected abstract Processor<? super Publisher<? extends DataBuffer>, Void> createBodyFlushProcessor();
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/reactive/AbstractListenerServerHttpResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */