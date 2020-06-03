/*    */ package org.springframework.http.server.reactive;
/*    */ 
/*    */ import org.reactivestreams.Publisher;
/*    */ import org.springframework.core.io.buffer.DataBuffer;
/*    */ import org.springframework.core.io.buffer.DataBufferUtils;
/*    */ import reactor.core.publisher.Flux;
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
/*    */ 
/*    */ 
/*    */ public class HttpHeadResponseDecorator
/*    */   extends ServerHttpResponseDecorator
/*    */ {
/*    */   public HttpHeadResponseDecorator(ServerHttpResponse delegate) {
/* 38 */     super(delegate);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
/* 49 */     return Flux.from(body)
/* 50 */       .reduce(Integer.valueOf(0), (current, buffer) -> {
/*    */           int next = current.intValue() + buffer.readableByteCount();
/*    */           
/*    */           DataBufferUtils.release(buffer);
/*    */           return Integer.valueOf(next);
/* 55 */         }).doOnNext(count -> getHeaders().setContentLength(count.intValue()))
/* 56 */       .then();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
/* 68 */     return setComplete();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/reactive/HttpHeadResponseDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */