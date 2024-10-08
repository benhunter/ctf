/*    */ package org.springframework.core.codec;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.reactivestreams.Publisher;
/*    */ import org.springframework.core.ResolvableType;
/*    */ import org.springframework.core.io.buffer.DataBuffer;
/*    */ import org.springframework.core.io.buffer.DataBufferFactory;
/*    */ import org.springframework.core.io.buffer.PooledDataBuffer;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.MimeType;
/*    */ import reactor.core.publisher.Flux;
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
/*    */ 
/*    */ public abstract class AbstractSingleValueEncoder<T>
/*    */   extends AbstractEncoder<T>
/*    */ {
/*    */   public AbstractSingleValueEncoder(MimeType... supportedMimeTypes) {
/* 43 */     super(supportedMimeTypes);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final Flux<DataBuffer> encode(Publisher<? extends T> inputStream, DataBufferFactory bufferFactory, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/* 51 */     return Flux.from(inputStream)
/* 52 */       .take(1L)
/* 53 */       .concatMap(value -> encode((T)value, bufferFactory, elementType, mimeType, hints))
/* 54 */       .doOnDiscard(PooledDataBuffer.class, PooledDataBuffer::release);
/*    */   }
/*    */   
/*    */   protected abstract Flux<DataBuffer> encode(T paramT, DataBufferFactory paramDataBufferFactory, ResolvableType paramResolvableType, @Nullable MimeType paramMimeType, @Nullable Map<String, Object> paramMap);
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/codec/AbstractSingleValueEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */