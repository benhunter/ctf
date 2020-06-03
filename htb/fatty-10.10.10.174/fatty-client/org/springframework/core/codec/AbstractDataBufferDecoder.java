/*    */ package org.springframework.core.codec;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.reactivestreams.Publisher;
/*    */ import org.springframework.core.ResolvableType;
/*    */ import org.springframework.core.io.buffer.DataBuffer;
/*    */ import org.springframework.core.io.buffer.DataBufferUtils;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.MimeType;
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
/*    */ public abstract class AbstractDataBufferDecoder<T>
/*    */   extends AbstractDecoder<T>
/*    */ {
/*    */   protected AbstractDataBufferDecoder(MimeType... supportedMimeTypes) {
/* 52 */     super(supportedMimeTypes);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Flux<T> decode(Publisher<DataBuffer> input, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/* 60 */     return Flux.from(input).map(buffer -> decodeDataBuffer(buffer, elementType, mimeType, hints));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Mono<T> decodeToMono(Publisher<DataBuffer> input, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/* 67 */     return DataBufferUtils.join(input)
/* 68 */       .map(buffer -> decodeDataBuffer(buffer, elementType, mimeType, hints));
/*    */   }
/*    */   
/*    */   protected abstract T decodeDataBuffer(DataBuffer paramDataBuffer, ResolvableType paramResolvableType, @Nullable MimeType paramMimeType, @Nullable Map<String, Object> paramMap);
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/codec/AbstractDataBufferDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */