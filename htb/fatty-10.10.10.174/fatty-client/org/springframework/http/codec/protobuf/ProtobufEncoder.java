/*     */ package org.springframework.http.codec.protobuf;
/*     */ 
/*     */ import com.google.protobuf.Message;
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Collectors;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.DataBufferFactory;
/*     */ import org.springframework.core.io.buffer.DataBufferUtils;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.codec.HttpMessageEncoder;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.MimeType;
/*     */ import reactor.core.publisher.Flux;
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
/*     */ public class ProtobufEncoder
/*     */   extends ProtobufCodecSupport
/*     */   implements HttpMessageEncoder<Message>
/*     */ {
/*     */   private static final List<MediaType> streamingMediaTypes;
/*     */   
/*     */   static {
/*  64 */     streamingMediaTypes = (List<MediaType>)MIME_TYPES.stream().map(mimeType -> new MediaType(mimeType.getType(), mimeType.getSubtype(), Collections.singletonMap("delimited", "true"))).collect(Collectors.toList());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canEncode(ResolvableType elementType, @Nullable MimeType mimeType) {
/*  69 */     return (Message.class.isAssignableFrom(elementType.toClass()) && supportsMimeType(mimeType));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Flux<DataBuffer> encode(Publisher<? extends Message> inputStream, DataBufferFactory bufferFactory, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/*  76 */     return Flux.from(inputStream)
/*  77 */       .map(message -> {
/*     */           DataBuffer buffer = bufferFactory.allocateBuffer();
/*     */           
/*     */           boolean release = true;
/*     */           
/*     */           try {
/*     */             if (!(inputStream instanceof reactor.core.publisher.Mono)) {
/*     */               message.writeDelimitedTo(buffer.asOutputStream());
/*     */             } else {
/*     */               message.writeTo(buffer.asOutputStream());
/*     */             } 
/*     */             release = false;
/*     */             return buffer;
/*  90 */           } catch (IOException ex) {
/*     */             throw new IllegalStateException("Unexpected I/O error while writing to data buffer", ex);
/*     */           } finally {
/*     */             if (release) {
/*     */               DataBufferUtils.release(buffer);
/*     */             }
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<MediaType> getStreamingMediaTypes() {
/* 103 */     return streamingMediaTypes;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<MimeType> getEncodableMimeTypes() {
/* 108 */     return getMimeTypes();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/protobuf/ProtobufEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */