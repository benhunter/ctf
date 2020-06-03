/*     */ package org.springframework.http.codec;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.codec.AbstractEncoder;
/*     */ import org.springframework.core.codec.Encoder;
/*     */ import org.springframework.core.codec.Hints;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.PooledDataBuffer;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpLogging;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.ReactiveHttpOutputMessage;
/*     */ import org.springframework.http.server.reactive.ServerHttpRequest;
/*     */ import org.springframework.http.server.reactive.ServerHttpResponse;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.MimeType;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class EncoderHttpMessageWriter<T>
/*     */   implements HttpMessageWriter<T>
/*     */ {
/*     */   private final Encoder<T> encoder;
/*     */   private final List<MediaType> mediaTypes;
/*     */   @Nullable
/*     */   private final MediaType defaultMediaType;
/*     */   
/*     */   public EncoderHttpMessageWriter(Encoder<T> encoder) {
/*  72 */     Assert.notNull(encoder, "Encoder is required");
/*  73 */     initLogger(encoder);
/*  74 */     this.encoder = encoder;
/*  75 */     this.mediaTypes = MediaType.asMediaTypes(encoder.getEncodableMimeTypes());
/*  76 */     this.defaultMediaType = initDefaultMediaType(this.mediaTypes);
/*     */   }
/*     */   
/*     */   private static void initLogger(Encoder<?> encoder) {
/*  80 */     if (encoder instanceof AbstractEncoder && encoder
/*  81 */       .getClass().getName().startsWith("org.springframework.core.codec")) {
/*  82 */       Log logger = HttpLogging.forLog(((AbstractEncoder)encoder).getLogger());
/*  83 */       ((AbstractEncoder)encoder).setLogger(logger);
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static MediaType initDefaultMediaType(List<MediaType> mediaTypes) {
/*  89 */     return mediaTypes.stream().filter(MimeType::isConcrete).findFirst().orElse(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Encoder<T> getEncoder() {
/*  97 */     return this.encoder;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<MediaType> getWritableMediaTypes() {
/* 102 */     return this.mediaTypes;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canWrite(ResolvableType elementType, @Nullable MediaType mediaType) {
/* 108 */     return this.encoder.canEncode(elementType, (MimeType)mediaType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mono<Void> write(Publisher<? extends T> inputStream, ResolvableType elementType, @Nullable MediaType mediaType, ReactiveHttpOutputMessage message, Map<String, Object> hints) {
/* 116 */     MediaType contentType = updateContentType(message, mediaType);
/*     */     
/* 118 */     Flux<DataBuffer> body = this.encoder.encode(inputStream, message
/* 119 */         .bufferFactory(), elementType, (MimeType)contentType, hints);
/*     */     
/* 121 */     if (inputStream instanceof Mono) {
/* 122 */       HttpHeaders headers = message.getHeaders();
/* 123 */       return body
/* 124 */         .singleOrEmpty()
/* 125 */         .switchIfEmpty(Mono.defer(() -> {
/*     */               headers.setContentLength(0L);
/*     */               
/*     */               return message.setComplete().then(Mono.empty());
/* 129 */             })).flatMap(buffer -> {
/*     */             headers.setContentLength(buffer.readableByteCount());
/*     */             
/*     */             return message.writeWith((Publisher)Mono.just(buffer).doOnDiscard(PooledDataBuffer.class, PooledDataBuffer::release));
/*     */           });
/*     */     } 
/*     */     
/* 136 */     if (isStreamingMediaType(contentType)) {
/* 137 */       return message.writeAndFlushWith((Publisher)body.map(buffer -> Mono.just(buffer).doOnDiscard(PooledDataBuffer.class, PooledDataBuffer::release)));
/*     */     }
/*     */ 
/*     */     
/* 141 */     return message.writeWith((Publisher)body);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private MediaType updateContentType(ReactiveHttpOutputMessage message, @Nullable MediaType mediaType) {
/* 146 */     MediaType result = message.getHeaders().getContentType();
/* 147 */     if (result != null) {
/* 148 */       return result;
/*     */     }
/* 150 */     MediaType fallback = this.defaultMediaType;
/* 151 */     result = useFallback(mediaType, fallback) ? fallback : mediaType;
/* 152 */     if (result != null) {
/* 153 */       result = addDefaultCharset(result, fallback);
/* 154 */       message.getHeaders().setContentType(result);
/*     */     } 
/* 156 */     return result;
/*     */   }
/*     */   
/*     */   private static boolean useFallback(@Nullable MediaType main, @Nullable MediaType fallback) {
/* 160 */     return (main == null || !main.isConcrete() || (main
/* 161 */       .equals(MediaType.APPLICATION_OCTET_STREAM) && fallback != null));
/*     */   }
/*     */   
/*     */   private static MediaType addDefaultCharset(MediaType main, @Nullable MediaType defaultType) {
/* 165 */     if (main.getCharset() == null && defaultType != null && defaultType.getCharset() != null) {
/* 166 */       return new MediaType(main, defaultType.getCharset());
/*     */     }
/* 168 */     return main;
/*     */   }
/*     */   
/*     */   private boolean isStreamingMediaType(@Nullable MediaType mediaType) {
/* 172 */     if (mediaType == null || !(this.encoder instanceof HttpMessageEncoder)) {
/* 173 */       return false;
/*     */     }
/* 175 */     for (MediaType streamingMediaType : ((HttpMessageEncoder)this.encoder).getStreamingMediaTypes()) {
/* 176 */       if (mediaType.isCompatibleWith(streamingMediaType) && matchParameters(mediaType, streamingMediaType)) {
/* 177 */         return true;
/*     */       }
/*     */     } 
/* 180 */     return false;
/*     */   }
/*     */   
/*     */   private boolean matchParameters(MediaType streamingMediaType, MediaType mediaType) {
/* 184 */     for (String name : streamingMediaType.getParameters().keySet()) {
/* 185 */       String s1 = streamingMediaType.getParameter(name);
/* 186 */       String s2 = mediaType.getParameter(name);
/* 187 */       if (StringUtils.hasText(s1) && StringUtils.hasText(s2) && !s1.equalsIgnoreCase(s2)) {
/* 188 */         return false;
/*     */       }
/*     */     } 
/* 191 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mono<Void> write(Publisher<? extends T> inputStream, ResolvableType actualType, ResolvableType elementType, @Nullable MediaType mediaType, ServerHttpRequest request, ServerHttpResponse response, Map<String, Object> hints) {
/* 202 */     Map<String, Object> allHints = Hints.merge(hints, 
/* 203 */         getWriteHints(actualType, elementType, mediaType, request, response));
/*     */     
/* 205 */     return write(inputStream, elementType, mediaType, (ReactiveHttpOutputMessage)response, allHints);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map<String, Object> getWriteHints(ResolvableType streamType, ResolvableType elementType, @Nullable MediaType mediaType, ServerHttpRequest request, ServerHttpResponse response) {
/* 216 */     if (this.encoder instanceof HttpMessageEncoder) {
/* 217 */       HttpMessageEncoder<?> encoder = (HttpMessageEncoder)this.encoder;
/* 218 */       return encoder.getEncodeHints(streamType, elementType, mediaType, request, response);
/*     */     } 
/* 220 */     return Hints.none();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/EncoderHttpMessageWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */