/*     */ package org.springframework.http.codec;
/*     */ 
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.time.Duration;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.codec.CodecException;
/*     */ import org.springframework.core.codec.Encoder;
/*     */ import org.springframework.core.codec.Hints;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.DataBufferFactory;
/*     */ import org.springframework.core.io.buffer.DataBufferUtils;
/*     */ import org.springframework.core.io.buffer.PooledDataBuffer;
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
/*     */ public class ServerSentEventHttpMessageWriter
/*     */   implements HttpMessageWriter<Object>
/*     */ {
/*  55 */   private static final MediaType DEFAULT_MEDIA_TYPE = new MediaType("text", "event-stream", StandardCharsets.UTF_8);
/*     */   
/*  57 */   private static final List<MediaType> WRITABLE_MEDIA_TYPES = Collections.singletonList(MediaType.TEXT_EVENT_STREAM);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private final Encoder<?> encoder;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServerSentEventHttpMessageWriter() {
/*  69 */     this(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServerSentEventHttpMessageWriter(@Nullable Encoder<?> encoder) {
/*  78 */     this.encoder = encoder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Encoder<?> getEncoder() {
/*  87 */     return this.encoder;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<MediaType> getWritableMediaTypes() {
/*  92 */     return WRITABLE_MEDIA_TYPES;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canWrite(ResolvableType elementType, @Nullable MediaType mediaType) {
/*  98 */     return (mediaType == null || MediaType.TEXT_EVENT_STREAM.includes(mediaType) || ServerSentEvent.class
/*  99 */       .isAssignableFrom(elementType.toClass()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mono<Void> write(Publisher<?> input, ResolvableType elementType, @Nullable MediaType mediaType, ReactiveHttpOutputMessage message, Map<String, Object> hints) {
/* 106 */     mediaType = (mediaType != null && mediaType.getCharset() != null) ? mediaType : DEFAULT_MEDIA_TYPE;
/* 107 */     DataBufferFactory bufferFactory = message.bufferFactory();
/*     */     
/* 109 */     message.getHeaders().setContentType(mediaType);
/* 110 */     return message.writeAndFlushWith((Publisher)encode(input, elementType, mediaType, bufferFactory, hints));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Flux<Publisher<DataBuffer>> encode(Publisher<?> input, ResolvableType elementType, MediaType mediaType, DataBufferFactory factory, Map<String, Object> hints) {
/* 117 */     ResolvableType valueType = ServerSentEvent.class.isAssignableFrom(elementType.toClass()) ? elementType.getGeneric(new int[0]) : elementType;
/*     */     
/* 119 */     return Flux.from(input).map(element -> {
/*     */           ServerSentEvent<?> sse = (element instanceof ServerSentEvent) ? (ServerSentEvent)element : ServerSentEvent.builder().data(element).build();
/*     */           StringBuilder sb = new StringBuilder();
/*     */           String id = sse.id();
/*     */           String event = sse.event();
/*     */           Duration retry = sse.retry();
/*     */           String comment = sse.comment();
/*     */           Object data = sse.data();
/*     */           if (id != null) {
/*     */             writeField("id", id, sb);
/*     */           }
/*     */           if (event != null) {
/*     */             writeField("event", event, sb);
/*     */           }
/*     */           if (retry != null) {
/*     */             writeField("retry", Long.valueOf(retry.toMillis()), sb);
/*     */           }
/*     */           if (comment != null) {
/*     */             sb.append(':').append(StringUtils.replace(comment, "\n", "\n:")).append("\n");
/*     */           }
/*     */           if (data != null) {
/*     */             sb.append("data:");
/*     */           }
/*     */           Flux<DataBuffer> flux = Flux.concat(new Publisher[] { (Publisher)encodeText(sb, mediaType, factory), (Publisher)encodeData(data, valueType, mediaType, factory, hints), (Publisher)encodeText("\n", mediaType, factory) });
/*     */           return (Publisher)flux.doOnDiscard(PooledDataBuffer.class, DataBufferUtils::release);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeField(String fieldName, Object fieldValue, StringBuilder sb) {
/* 156 */     sb.append(fieldName);
/* 157 */     sb.append(':');
/* 158 */     sb.append(fieldValue.toString());
/* 159 */     sb.append("\n");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private <T> Flux<DataBuffer> encodeData(@Nullable T dataValue, ResolvableType valueType, MediaType mediaType, DataBufferFactory factory, Map<String, Object> hints) {
/* 166 */     if (dataValue == null) {
/* 167 */       return Flux.empty();
/*     */     }
/*     */     
/* 170 */     if (dataValue instanceof String) {
/* 171 */       String text = (String)dataValue;
/* 172 */       return Flux.from((Publisher)encodeText(StringUtils.replace(text, "\n", "\ndata:") + "\n", mediaType, factory));
/*     */     } 
/*     */     
/* 175 */     if (this.encoder == null) {
/* 176 */       return Flux.error((Throwable)new CodecException("No SSE encoder configured and the data is not String."));
/*     */     }
/*     */     
/* 179 */     return this.encoder
/* 180 */       .encode((Publisher)Mono.just(dataValue), factory, valueType, (MimeType)mediaType, hints)
/* 181 */       .concatWith((Publisher)encodeText("\n", mediaType, factory));
/*     */   }
/*     */   
/*     */   private Mono<DataBuffer> encodeText(CharSequence text, MediaType mediaType, DataBufferFactory bufferFactory) {
/* 185 */     Assert.notNull(mediaType.getCharset(), "Expected MediaType with charset");
/* 186 */     byte[] bytes = text.toString().getBytes(mediaType.getCharset());
/* 187 */     return Mono.just(bufferFactory.wrap(bytes));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mono<Void> write(Publisher<?> input, ResolvableType actualType, ResolvableType elementType, @Nullable MediaType mediaType, ServerHttpRequest request, ServerHttpResponse response, Map<String, Object> hints) {
/* 195 */     Map<String, Object> allHints = Hints.merge(hints, 
/* 196 */         getEncodeHints(actualType, elementType, mediaType, request, response));
/*     */     
/* 198 */     return write(input, elementType, mediaType, (ReactiveHttpOutputMessage)response, allHints);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, Object> getEncodeHints(ResolvableType actualType, ResolvableType elementType, @Nullable MediaType mediaType, ServerHttpRequest request, ServerHttpResponse response) {
/* 204 */     if (this.encoder instanceof HttpMessageEncoder) {
/* 205 */       HttpMessageEncoder<?> encoder = (HttpMessageEncoder)this.encoder;
/* 206 */       return encoder.getEncodeHints(actualType, elementType, mediaType, request, response);
/*     */     } 
/* 208 */     return Hints.none();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/ServerSentEventHttpMessageWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */