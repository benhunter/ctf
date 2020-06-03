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
/*     */ import org.springframework.core.codec.Decoder;
/*     */ import org.springframework.core.codec.StringDecoder;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.DataBufferFactory;
/*     */ import org.springframework.core.io.buffer.DefaultDataBufferFactory;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.ReactiveHttpInputMessage;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class ServerSentEventHttpMessageReader
/*     */   implements HttpMessageReader<Object>
/*     */ {
/*  49 */   private static final DataBufferFactory bufferFactory = (DataBufferFactory)new DefaultDataBufferFactory();
/*     */   
/*  51 */   private static final StringDecoder stringDecoder = StringDecoder.textPlainOnly();
/*     */   
/*  53 */   private static final ResolvableType STRING_TYPE = ResolvableType.forClass(String.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private final Decoder<?> decoder;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServerSentEventHttpMessageReader() {
/*  65 */     this(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServerSentEventHttpMessageReader(@Nullable Decoder<?> decoder) {
/*  73 */     this.decoder = decoder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Decoder<?> getDecoder() {
/*  82 */     return this.decoder;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<MediaType> getReadableMediaTypes() {
/*  87 */     return Collections.singletonList(MediaType.TEXT_EVENT_STREAM);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canRead(ResolvableType elementType, @Nullable MediaType mediaType) {
/*  92 */     return (MediaType.TEXT_EVENT_STREAM.includes(mediaType) || isServerSentEvent(elementType));
/*     */   }
/*     */   
/*     */   private boolean isServerSentEvent(ResolvableType elementType) {
/*  96 */     return ServerSentEvent.class.isAssignableFrom(elementType.toClass());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Flux<Object> read(ResolvableType elementType, ReactiveHttpInputMessage message, Map<String, Object> hints) {
/* 104 */     boolean shouldWrap = isServerSentEvent(elementType);
/* 105 */     ResolvableType valueType = shouldWrap ? elementType.getGeneric(new int[0]) : elementType;
/*     */     
/* 107 */     return stringDecoder.decode((Publisher)message.getBody(), STRING_TYPE, null, hints)
/* 108 */       .bufferUntil(line -> line.equals(""))
/* 109 */       .concatMap(lines -> buildEvent(lines, valueType, shouldWrap, hints));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Mono<?> buildEvent(List<String> lines, ResolvableType valueType, boolean shouldWrap, Map<String, Object> hints) {
/* 115 */     ServerSentEvent.Builder<Object> sseBuilder = shouldWrap ? ServerSentEvent.<Object>builder() : null;
/* 116 */     StringBuilder data = null;
/* 117 */     StringBuilder comment = null;
/*     */     
/* 119 */     for (String line : lines) {
/* 120 */       if (line.startsWith("data:")) {
/* 121 */         data = (data != null) ? data : new StringBuilder();
/* 122 */         data.append(line.substring(5).trim()).append("\n");
/*     */       } 
/* 124 */       if (shouldWrap) {
/* 125 */         if (line.startsWith("id:")) {
/* 126 */           sseBuilder.id(line.substring(3).trim()); continue;
/*     */         } 
/* 128 */         if (line.startsWith("event:")) {
/* 129 */           sseBuilder.event(line.substring(6).trim()); continue;
/*     */         } 
/* 131 */         if (line.startsWith("retry:")) {
/* 132 */           sseBuilder.retry(Duration.ofMillis(Long.valueOf(line.substring(6).trim()).longValue())); continue;
/*     */         } 
/* 134 */         if (line.startsWith(":")) {
/* 135 */           comment = (comment != null) ? comment : new StringBuilder();
/* 136 */           comment.append(line.substring(1).trim()).append("\n");
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 141 */     Mono<?> decodedData = (data != null) ? decodeData(data.toString(), valueType, hints) : Mono.empty();
/*     */     
/* 143 */     if (shouldWrap) {
/* 144 */       if (comment != null) {
/* 145 */         sseBuilder.comment(comment.toString().substring(0, comment.length() - 1));
/*     */       }
/* 147 */       return decodedData.map(o -> {
/*     */             sseBuilder.data(o);
/*     */             
/*     */             return sseBuilder.build();
/*     */           });
/*     */     } 
/* 153 */     return decodedData;
/*     */   }
/*     */ 
/*     */   
/*     */   private Mono<?> decodeData(String data, ResolvableType dataType, Map<String, Object> hints) {
/* 158 */     if (String.class == dataType.resolve()) {
/* 159 */       return Mono.just(data.substring(0, data.length() - 1));
/*     */     }
/*     */     
/* 162 */     if (this.decoder == null) {
/* 163 */       return Mono.error((Throwable)new CodecException("No SSE decoder configured and the data is not String."));
/*     */     }
/*     */     
/* 166 */     byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
/* 167 */     DataBuffer buffer = bufferFactory.wrap(bytes);
/* 168 */     return this.decoder.decodeToMono((Publisher)Mono.just(buffer), dataType, (MimeType)MediaType.TEXT_EVENT_STREAM, hints);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mono<Object> readMono(ResolvableType elementType, ReactiveHttpInputMessage message, Map<String, Object> hints) {
/* 178 */     if (elementType.resolve() == String.class) {
/* 179 */       Flux<DataBuffer> body = message.getBody();
/* 180 */       return stringDecoder.decodeToMono((Publisher)body, elementType, null, null).cast(Object.class);
/*     */     } 
/*     */     
/* 183 */     return Mono.error(new UnsupportedOperationException("ServerSentEventHttpMessageReader only supports reading stream of events as a Flux"));
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/ServerSentEventHttpMessageReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */