/*    */ package org.springframework.core.codec;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.util.Map;
/*    */ import org.reactivestreams.Publisher;
/*    */ import org.springframework.core.ResolvableType;
/*    */ import org.springframework.core.io.ByteArrayResource;
/*    */ import org.springframework.core.io.InputStreamResource;
/*    */ import org.springframework.core.io.Resource;
/*    */ import org.springframework.core.io.buffer.DataBuffer;
/*    */ import org.springframework.core.io.buffer.DataBufferUtils;
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
/*    */ 
/*    */ public class ResourceDecoder
/*    */   extends AbstractDataBufferDecoder<Resource>
/*    */ {
/*    */   public ResourceDecoder() {
/* 45 */     super(new MimeType[] { MimeTypeUtils.ALL });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canDecode(ResolvableType elementType, @Nullable MimeType mimeType) {
/* 51 */     return (Resource.class.isAssignableFrom(elementType.toClass()) && super
/* 52 */       .canDecode(elementType, mimeType));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Flux<Resource> decode(Publisher<DataBuffer> inputStream, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/* 59 */     return Flux.from((Publisher)decodeToMono(inputStream, elementType, mimeType, hints));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Resource decodeDataBuffer(DataBuffer dataBuffer, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/* 66 */     byte[] bytes = new byte[dataBuffer.readableByteCount()];
/* 67 */     dataBuffer.read(bytes);
/* 68 */     DataBufferUtils.release(dataBuffer);
/*    */     
/* 70 */     if (this.logger.isDebugEnabled()) {
/* 71 */       this.logger.debug(Hints.getLogPrefix(hints) + "Read " + bytes.length + " bytes");
/*    */     }
/*    */     
/* 74 */     Class<?> clazz = elementType.toClass();
/* 75 */     if (clazz == InputStreamResource.class) {
/* 76 */       return (Resource)new InputStreamResource(new ByteArrayInputStream(bytes));
/*    */     }
/* 78 */     if (Resource.class.isAssignableFrom(clazz)) {
/* 79 */       return (Resource)new ByteArrayResource(bytes);
/*    */     }
/*    */     
/* 82 */     throw new IllegalStateException("Unsupported resource class: " + clazz);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/codec/ResourceDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */