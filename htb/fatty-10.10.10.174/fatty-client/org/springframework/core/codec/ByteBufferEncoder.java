/*    */ package org.springframework.core.codec;
/*    */ 
/*    */ import java.nio.ByteBuffer;
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
/*    */ public class ByteBufferEncoder
/*    */   extends AbstractEncoder<ByteBuffer>
/*    */ {
/*    */   public ByteBufferEncoder() {
/* 41 */     super(new MimeType[] { MimeTypeUtils.ALL });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canEncode(ResolvableType elementType, @Nullable MimeType mimeType) {
/* 47 */     Class<?> clazz = elementType.toClass();
/* 48 */     return (super.canEncode(elementType, mimeType) && ByteBuffer.class.isAssignableFrom(clazz));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Flux<DataBuffer> encode(Publisher<? extends ByteBuffer> inputStream, DataBufferFactory bufferFactory, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/* 56 */     return Flux.from(inputStream).map(byteBuffer -> {
/*    */           DataBuffer dataBuffer = bufferFactory.wrap(byteBuffer);
/*    */           if (this.logger.isDebugEnabled() && !Hints.isLoggingSuppressed(hints)) {
/*    */             String logPrefix = Hints.getLogPrefix(hints);
/*    */             this.logger.debug(logPrefix + "Writing " + dataBuffer.readableByteCount() + " bytes");
/*    */           } 
/*    */           return dataBuffer;
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/codec/ByteBufferEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */