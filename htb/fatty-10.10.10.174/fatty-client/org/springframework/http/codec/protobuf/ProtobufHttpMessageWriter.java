/*     */ package org.springframework.http.codec.protobuf;
/*     */ 
/*     */ import com.google.protobuf.Descriptors;
/*     */ import com.google.protobuf.Message;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.codec.DecodingException;
/*     */ import org.springframework.core.codec.Encoder;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.ReactiveHttpOutputMessage;
/*     */ import org.springframework.http.codec.EncoderHttpMessageWriter;
/*     */ import org.springframework.http.codec.HttpMessageEncoder;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
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
/*     */ public class ProtobufHttpMessageWriter
/*     */   extends EncoderHttpMessageWriter<Message>
/*     */ {
/*     */   private static final String X_PROTOBUF_SCHEMA_HEADER = "X-Protobuf-Schema";
/*     */   private static final String X_PROTOBUF_MESSAGE_HEADER = "X-Protobuf-Message";
/*  58 */   private static final ConcurrentMap<Class<?>, Method> methodCache = (ConcurrentMap<Class<?>, Method>)new ConcurrentReferenceHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProtobufHttpMessageWriter() {
/*  65 */     super((Encoder)new ProtobufEncoder());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProtobufHttpMessageWriter(Encoder<Message> encoder) {
/*  73 */     super(encoder);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mono<Void> write(Publisher<? extends Message> inputStream, ResolvableType elementType, @Nullable MediaType mediaType, ReactiveHttpOutputMessage message, Map<String, Object> hints) {
/*     */     try {
/*  83 */       Message.Builder builder = getMessageBuilder(elementType.toClass());
/*  84 */       Descriptors.Descriptor descriptor = builder.getDescriptorForType();
/*  85 */       message.getHeaders().add("X-Protobuf-Schema", descriptor.getFile().getName());
/*  86 */       message.getHeaders().add("X-Protobuf-Message", descriptor.getFullName());
/*  87 */       if (inputStream instanceof reactor.core.publisher.Flux) {
/*  88 */         if (mediaType == null) {
/*  89 */           message.getHeaders().setContentType(((HttpMessageEncoder)getEncoder()).getStreamingMediaTypes().get(0));
/*     */         }
/*  91 */         else if (!"true".equals(mediaType.getParameters().get("delimited"))) {
/*  92 */           Map<String, String> parameters = new HashMap<>(mediaType.getParameters());
/*  93 */           parameters.put("delimited", "true");
/*  94 */           message.getHeaders().setContentType(new MediaType(mediaType.getType(), mediaType.getSubtype(), parameters));
/*     */         } 
/*     */       }
/*  97 */       return super.write(inputStream, elementType, mediaType, message, hints);
/*     */     }
/*  99 */     catch (Exception ex) {
/* 100 */       return Mono.error((Throwable)new DecodingException("Could not read Protobuf message: " + ex.getMessage(), ex));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Message.Builder getMessageBuilder(Class<?> clazz) throws Exception {
/* 109 */     Method method = methodCache.get(clazz);
/* 110 */     if (method == null) {
/* 111 */       method = clazz.getMethod("newBuilder", new Class[0]);
/* 112 */       methodCache.put(clazz, method);
/*     */     } 
/* 114 */     return (Message.Builder)method.invoke(clazz, new Object[0]);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/protobuf/ProtobufHttpMessageWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */