/*     */ package org.springframework.http.codec;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.codec.AbstractDecoder;
/*     */ import org.springframework.core.codec.Decoder;
/*     */ import org.springframework.core.codec.Hints;
/*     */ import org.springframework.http.HttpLogging;
/*     */ import org.springframework.http.HttpMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.ReactiveHttpInputMessage;
/*     */ import org.springframework.http.server.reactive.ServerHttpRequest;
/*     */ import org.springframework.http.server.reactive.ServerHttpResponse;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class DecoderHttpMessageReader<T>
/*     */   implements HttpMessageReader<T>
/*     */ {
/*     */   private final Decoder<T> decoder;
/*     */   private final List<MediaType> mediaTypes;
/*     */   
/*     */   public DecoderHttpMessageReader(Decoder<T> decoder) {
/*  63 */     Assert.notNull(decoder, "Decoder is required");
/*  64 */     initLogger(decoder);
/*  65 */     this.decoder = decoder;
/*  66 */     this.mediaTypes = MediaType.asMediaTypes(decoder.getDecodableMimeTypes());
/*     */   }
/*     */   
/*     */   private static void initLogger(Decoder<?> decoder) {
/*  70 */     if (decoder instanceof AbstractDecoder && decoder
/*  71 */       .getClass().getName().startsWith("org.springframework.core.codec")) {
/*  72 */       Log logger = HttpLogging.forLog(((AbstractDecoder)decoder).getLogger());
/*  73 */       ((AbstractDecoder)decoder).setLogger(logger);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Decoder<T> getDecoder() {
/*  82 */     return this.decoder;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<MediaType> getReadableMediaTypes() {
/*  87 */     return this.mediaTypes;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canRead(ResolvableType elementType, @Nullable MediaType mediaType) {
/*  93 */     return this.decoder.canDecode(elementType, (MimeType)mediaType);
/*     */   }
/*     */ 
/*     */   
/*     */   public Flux<T> read(ResolvableType elementType, ReactiveHttpInputMessage message, Map<String, Object> hints) {
/*  98 */     MediaType contentType = getContentType((HttpMessage)message);
/*  99 */     return this.decoder.decode((Publisher)message.getBody(), elementType, (MimeType)contentType, hints);
/*     */   }
/*     */ 
/*     */   
/*     */   public Mono<T> readMono(ResolvableType elementType, ReactiveHttpInputMessage message, Map<String, Object> hints) {
/* 104 */     MediaType contentType = getContentType((HttpMessage)message);
/* 105 */     return this.decoder.decodeToMono((Publisher)message.getBody(), elementType, (MimeType)contentType, hints);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected MediaType getContentType(HttpMessage inputMessage) {
/* 117 */     MediaType contentType = inputMessage.getHeaders().getContentType();
/* 118 */     return (contentType != null) ? contentType : MediaType.APPLICATION_OCTET_STREAM;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Flux<T> read(ResolvableType actualType, ResolvableType elementType, ServerHttpRequest request, ServerHttpResponse response, Map<String, Object> hints) {
/* 128 */     Map<String, Object> allHints = Hints.merge(hints, 
/* 129 */         getReadHints(actualType, elementType, request, response));
/*     */     
/* 131 */     return read(elementType, (ReactiveHttpInputMessage)request, allHints);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mono<T> readMono(ResolvableType actualType, ResolvableType elementType, ServerHttpRequest request, ServerHttpResponse response, Map<String, Object> hints) {
/* 138 */     Map<String, Object> allHints = Hints.merge(hints, 
/* 139 */         getReadHints(actualType, elementType, request, response));
/*     */     
/* 141 */     return readMono(elementType, (ReactiveHttpInputMessage)request, allHints);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map<String, Object> getReadHints(ResolvableType actualType, ResolvableType elementType, ServerHttpRequest request, ServerHttpResponse response) {
/* 152 */     if (this.decoder instanceof HttpMessageDecoder) {
/* 153 */       HttpMessageDecoder<?> decoder = (HttpMessageDecoder)this.decoder;
/* 154 */       return decoder.getDecodeHints(actualType, elementType, request, response);
/*     */     } 
/* 156 */     return Hints.none();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/DecoderHttpMessageReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */