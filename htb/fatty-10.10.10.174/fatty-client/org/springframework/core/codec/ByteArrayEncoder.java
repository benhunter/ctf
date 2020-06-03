/*    */ package org.springframework.core.codec;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.reactivestreams.Publisher;
/*    */ import org.springframework.core.ResolvableType;
/*    */ import org.springframework.core.io.buffer.DataBuffer;
/*    */ import org.springframework.core.io.buffer.DataBufferFactory;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.MimeType;
/*    */ import org.springframework.util.MimeTypeUtils;
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
/*    */ public class ByteArrayEncoder
/*    */   extends AbstractEncoder<byte[]>
/*    */ {
/*    */   public ByteArrayEncoder() {
/* 40 */     super(new MimeType[] { MimeTypeUtils.ALL });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canEncode(ResolvableType elementType, @Nullable MimeType mimeType) {
/* 46 */     Class<?> clazz = elementType.toClass();
/* 47 */     return (super.canEncode(elementType, mimeType) && byte[].class.isAssignableFrom(clazz));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Flux<DataBuffer> encode(Publisher<? extends byte[]> inputStream, DataBufferFactory bufferFactory, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/* 55 */     return Flux.from(inputStream).map(bytes -> {
/*    */           DataBuffer dataBuffer = bufferFactory.wrap(bytes);
/*    */           if (this.logger.isDebugEnabled() && !Hints.isLoggingSuppressed(hints)) {
/*    */             String logPrefix = Hints.getLogPrefix(hints);
/*    */             this.logger.debug(logPrefix + "Writing " + dataBuffer.readableByteCount() + " bytes");
/*    */           } 
/*    */           return dataBuffer;
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/codec/ByteArrayEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */