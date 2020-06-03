/*     */ package org.springframework.http.codec.protobuf;
/*     */ 
/*     */ import com.google.protobuf.CodedInputStream;
/*     */ import com.google.protobuf.ExtensionRegistry;
/*     */ import com.google.protobuf.ExtensionRegistryLite;
/*     */ import com.google.protobuf.Message;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.function.Function;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.codec.Decoder;
/*     */ import org.springframework.core.codec.DecodingException;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.DataBufferUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
/*     */ import org.springframework.util.MimeType;
/*     */ import reactor.core.publisher.Flux;
/*     */ import reactor.core.publisher.Mono;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProtobufDecoder
/*     */   extends ProtobufCodecSupport
/*     */   implements Decoder<Message>
/*     */ {
/*     */   protected static final int DEFAULT_MESSAGE_MAX_SIZE = 65536;
/*  78 */   private static final ConcurrentMap<Class<?>, Method> methodCache = (ConcurrentMap<Class<?>, Method>)new ConcurrentReferenceHashMap();
/*     */ 
/*     */   
/*     */   private final ExtensionRegistry extensionRegistry;
/*     */   
/*  83 */   private int maxMessageSize = 65536;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProtobufDecoder() {
/*  90 */     this(ExtensionRegistry.newInstance());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProtobufDecoder(ExtensionRegistry extensionRegistry) {
/*  99 */     Assert.notNull(extensionRegistry, "ExtensionRegistry must not be null");
/* 100 */     this.extensionRegistry = extensionRegistry;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxMessageSize(int maxMessageSize) {
/* 105 */     this.maxMessageSize = maxMessageSize;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canDecode(ResolvableType elementType, @Nullable MimeType mimeType) {
/* 111 */     return (Message.class.isAssignableFrom(elementType.toClass()) && supportsMimeType(mimeType));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Flux<Message> decode(Publisher<DataBuffer> inputStream, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/* 118 */     MessageDecoderFunction decoderFunction = new MessageDecoderFunction(elementType, this.maxMessageSize);
/*     */ 
/*     */     
/* 121 */     return Flux.from(inputStream)
/* 122 */       .flatMapIterable(decoderFunction)
/* 123 */       .doOnTerminate(decoderFunction::discard);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mono<Message> decodeToMono(Publisher<DataBuffer> inputStream, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/* 130 */     return DataBufferUtils.join(inputStream).map(dataBuffer -> {
/*     */           try {
/*     */             Message.Builder builder = getMessageBuilder(elementType.toClass());
/*     */             
/*     */             ByteBuffer buffer = dataBuffer.asByteBuffer();
/*     */             builder.mergeFrom(CodedInputStream.newInstance(buffer), (ExtensionRegistryLite)this.extensionRegistry);
/*     */             return builder.build();
/* 137 */           } catch (IOException ex) {
/*     */             
/*     */             throw new DecodingException("I/O error while parsing input stream", ex);
/* 140 */           } catch (Exception ex) {
/*     */             throw new DecodingException("Could not read Protobuf message: " + ex.getMessage(), ex);
/*     */           } finally {
/*     */             DataBufferUtils.release(dataBuffer);
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Message.Builder getMessageBuilder(Class<?> clazz) throws Exception {
/* 155 */     Method method = methodCache.get(clazz);
/* 156 */     if (method == null) {
/* 157 */       method = clazz.getMethod("newBuilder", new Class[0]);
/* 158 */       methodCache.put(clazz, method);
/*     */     } 
/* 160 */     return (Message.Builder)method.invoke(clazz, new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<MimeType> getDecodableMimeTypes() {
/* 165 */     return getMimeTypes();
/*     */   }
/*     */ 
/*     */   
/*     */   private class MessageDecoderFunction
/*     */     implements Function<DataBuffer, Iterable<? extends Message>>
/*     */   {
/*     */     private final ResolvableType elementType;
/*     */     
/*     */     private final int maxMessageSize;
/*     */     
/*     */     @Nullable
/*     */     private DataBuffer output;
/*     */     
/*     */     private int messageBytesToRead;
/*     */     
/*     */     private int offset;
/*     */     
/*     */     public MessageDecoderFunction(ResolvableType elementType, int maxMessageSize) {
/* 184 */       this.elementType = elementType;
/* 185 */       this.maxMessageSize = maxMessageSize;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Iterable<? extends Message> apply(DataBuffer input) {
/*     */       try {
/* 192 */         List<Message> messages = new ArrayList<>();
/*     */ 
/*     */ 
/*     */         
/*     */         while (true) {
/* 197 */           if (this.output == null) {
/* 198 */             if (!readMessageSize(input)) {
/* 199 */               return messages;
/*     */             }
/* 201 */             if (this.messageBytesToRead > this.maxMessageSize) {
/* 202 */               throw new DecodingException("The number of bytes to read from the incoming stream (" + this.messageBytesToRead + ") exceeds the configured limit (" + this.maxMessageSize + ")");
/*     */             }
/*     */ 
/*     */ 
/*     */             
/* 207 */             this.output = input.factory().allocateBuffer(this.messageBytesToRead);
/*     */           } 
/*     */ 
/*     */           
/* 211 */           int chunkBytesToRead = (this.messageBytesToRead >= input.readableByteCount()) ? input.readableByteCount() : this.messageBytesToRead;
/* 212 */           int remainingBytesToRead = input.readableByteCount() - chunkBytesToRead;
/*     */           
/* 214 */           byte[] bytesToWrite = new byte[chunkBytesToRead];
/* 215 */           input.read(bytesToWrite, 0, chunkBytesToRead);
/* 216 */           this.output.write(bytesToWrite);
/* 217 */           this.messageBytesToRead -= chunkBytesToRead;
/*     */           
/* 219 */           if (this.messageBytesToRead == 0) {
/* 220 */             CodedInputStream stream = CodedInputStream.newInstance(this.output.asByteBuffer());
/* 221 */             DataBufferUtils.release(this.output);
/* 222 */             this.output = null;
/*     */ 
/*     */             
/* 225 */             Message message = ProtobufDecoder.getMessageBuilder(this.elementType.toClass()).mergeFrom(stream, (ExtensionRegistryLite)ProtobufDecoder.this.extensionRegistry).build();
/* 226 */             messages.add(message);
/*     */           } 
/* 228 */           if (remainingBytesToRead <= 0)
/* 229 */             return messages; 
/*     */         } 
/* 231 */       } catch (DecodingException ex) {
/* 232 */         throw ex;
/*     */       }
/* 234 */       catch (IOException ex) {
/* 235 */         throw new DecodingException("I/O error while parsing input stream", ex);
/*     */       }
/* 237 */       catch (Exception ex) {
/* 238 */         throw new DecodingException("Could not read Protobuf message: " + ex.getMessage(), ex);
/*     */       } finally {
/*     */         
/* 241 */         DataBufferUtils.release(input);
/*     */       } 
/*     */     }
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
/*     */     private boolean readMessageSize(DataBuffer input) {
/* 255 */       if (this.offset == 0) {
/* 256 */         if (input.readableByteCount() == 0) {
/* 257 */           return false;
/*     */         }
/* 259 */         int firstByte = input.read();
/* 260 */         if ((firstByte & 0x80) == 0) {
/* 261 */           this.messageBytesToRead = firstByte;
/* 262 */           return true;
/*     */         } 
/* 264 */         this.messageBytesToRead = firstByte & 0x7F;
/* 265 */         this.offset = 7;
/*     */       } 
/*     */       
/* 268 */       if (this.offset < 32) {
/* 269 */         for (; this.offset < 32; this.offset += 7) {
/* 270 */           if (input.readableByteCount() == 0) {
/* 271 */             return false;
/*     */           }
/* 273 */           int b = input.read();
/* 274 */           this.messageBytesToRead |= (b & 0x7F) << this.offset;
/* 275 */           if ((b & 0x80) == 0) {
/* 276 */             this.offset = 0;
/* 277 */             return true;
/*     */           } 
/*     */         } 
/*     */       }
/*     */       
/* 282 */       for (; this.offset < 64; this.offset += 7) {
/* 283 */         if (input.readableByteCount() == 0) {
/* 284 */           return false;
/*     */         }
/* 286 */         int b = input.read();
/* 287 */         if ((b & 0x80) == 0) {
/* 288 */           this.offset = 0;
/* 289 */           return true;
/*     */         } 
/*     */       } 
/* 292 */       this.offset = 0;
/* 293 */       throw new DecodingException("Cannot parse message size: malformed varint");
/*     */     }
/*     */     
/*     */     public void discard() {
/* 297 */       if (this.output != null)
/* 298 */         DataBufferUtils.release(this.output); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/protobuf/ProtobufDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */