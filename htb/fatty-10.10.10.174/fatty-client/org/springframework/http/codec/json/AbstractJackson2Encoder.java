/*     */ package org.springframework.http.codec.json;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonEncoding;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.ObjectWriter;
/*     */ import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.codec.CodecException;
/*     */ import org.springframework.core.codec.EncodingException;
/*     */ import org.springframework.core.codec.Hints;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.DataBufferFactory;
/*     */ import org.springframework.core.io.buffer.DataBufferUtils;
/*     */ import org.springframework.core.log.LogFormatUtils;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.codec.HttpMessageEncoder;
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
/*     */ public abstract class AbstractJackson2Encoder
/*     */   extends Jackson2CodecSupport
/*     */   implements HttpMessageEncoder<Object>
/*     */ {
/*  68 */   private static final byte[] NEWLINE_SEPARATOR = new byte[] { 10 };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   private static final Map<MediaType, byte[]> STREAM_SEPARATORS = (Map)new HashMap<>(); static {
/*  74 */     STREAM_SEPARATORS.put(MediaType.APPLICATION_STREAM_JSON, NEWLINE_SEPARATOR);
/*  75 */     STREAM_SEPARATORS.put(MediaType.parseMediaType("application/stream+x-jackson-smile"), new byte[0]);
/*     */   }
/*     */ 
/*     */   
/*  79 */   private final List<MediaType> streamingMediaTypes = new ArrayList<>(1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractJackson2Encoder(ObjectMapper mapper, MimeType... mimeTypes) {
/*  86 */     super(mapper, mimeTypes);
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
/*     */   public void setStreamingMediaTypes(List<MediaType> mediaTypes) {
/*  98 */     this.streamingMediaTypes.clear();
/*  99 */     this.streamingMediaTypes.addAll(mediaTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canEncode(ResolvableType elementType, @Nullable MimeType mimeType) {
/* 105 */     Class<?> clazz = elementType.toClass();
/* 106 */     return (supportsMimeType(mimeType) && (Object.class == clazz || (
/* 107 */       !String.class.isAssignableFrom(elementType.resolve(clazz)) && getObjectMapper().canSerialize(clazz))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Flux<DataBuffer> encode(Publisher<?> inputStream, DataBufferFactory bufferFactory, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/* 114 */     Assert.notNull(inputStream, "'inputStream' must not be null");
/* 115 */     Assert.notNull(bufferFactory, "'bufferFactory' must not be null");
/* 116 */     Assert.notNull(elementType, "'elementType' must not be null");
/*     */     
/* 118 */     JsonEncoding encoding = getJsonEncoding(mimeType);
/*     */     
/* 120 */     if (inputStream instanceof Mono) {
/* 121 */       return Mono.from(inputStream).map(value -> encodeValue(value, mimeType, bufferFactory, elementType, hints, encoding))
/* 122 */         .flux();
/*     */     }
/*     */     
/* 125 */     return this.streamingMediaTypes.stream()
/* 126 */       .filter(mediaType -> mediaType.isCompatibleWith(mimeType))
/* 127 */       .findFirst()
/* 128 */       .map(mediaType -> {
/*     */           byte[] separator = STREAM_SEPARATORS.getOrDefault(mediaType, NEWLINE_SEPARATOR);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           return Flux.from(inputStream).map(());
/* 139 */         }).orElseGet(() -> {
/*     */           ResolvableType listType = ResolvableType.forClassWithGenerics(List.class, new ResolvableType[] { elementType });
/*     */           return Flux.from(inputStream).collectList().map(()).flux();
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DataBuffer encodeValue(Object value, @Nullable MimeType mimeType, DataBufferFactory bufferFactory, ResolvableType elementType, @Nullable Map<String, Object> hints, JsonEncoding encoding) {
/* 150 */     if (!Hints.isLoggingSuppressed(hints)) {
/* 151 */       LogFormatUtils.traceDebug(this.logger, traceOn -> {
/*     */             String formatted = LogFormatUtils.formatValue(value, !traceOn.booleanValue());
/*     */             
/*     */             return Hints.getLogPrefix(hints) + "Encoding [" + formatted + "]";
/*     */           });
/*     */     }
/* 157 */     JavaType javaType = getJavaType(elementType.getType(), null);
/* 158 */     Class<?> jsonView = (hints != null) ? (Class)hints.get(Jackson2CodecSupport.JSON_VIEW_HINT) : null;
/*     */     
/* 160 */     ObjectWriter writer = (jsonView != null) ? getObjectMapper().writerWithView(jsonView) : getObjectMapper().writer();
/*     */     
/* 162 */     if (javaType.isContainerType()) {
/* 163 */       writer = writer.forType(javaType);
/*     */     }
/*     */     
/* 166 */     writer = customizeWriter(writer, mimeType, elementType, hints);
/*     */     
/* 168 */     DataBuffer buffer = bufferFactory.allocateBuffer();
/* 169 */     boolean release = true;
/* 170 */     OutputStream outputStream = buffer.asOutputStream();
/*     */     
/*     */     try {
/* 173 */       JsonGenerator generator = getObjectMapper().getFactory().createGenerator(outputStream, encoding);
/* 174 */       writer.writeValue(generator, value);
/* 175 */       generator.flush();
/* 176 */       release = false;
/*     */     }
/* 178 */     catch (InvalidDefinitionException ex) {
/* 179 */       throw new CodecException("Type definition error: " + ex.getType(), ex);
/*     */     }
/* 181 */     catch (JsonProcessingException ex) {
/* 182 */       throw new EncodingException("JSON encoding error: " + ex.getOriginalMessage(), ex);
/*     */     }
/* 184 */     catch (IOException ex) {
/* 185 */       throw new IllegalStateException("Unexpected I/O error while writing to data buffer", ex);
/*     */     }
/*     */     finally {
/*     */       
/* 189 */       if (release) {
/* 190 */         DataBufferUtils.release(buffer);
/*     */       }
/*     */     } 
/*     */     
/* 194 */     return buffer;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected ObjectWriter customizeWriter(ObjectWriter writer, @Nullable MimeType mimeType, ResolvableType elementType, @Nullable Map<String, Object> hints) {
/* 200 */     return writer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JsonEncoding getJsonEncoding(@Nullable MimeType mimeType) {
/* 210 */     if (mimeType != null && mimeType.getCharset() != null) {
/* 211 */       Charset charset = mimeType.getCharset();
/* 212 */       for (JsonEncoding encoding : JsonEncoding.values()) {
/* 213 */         if (charset.name().equals(encoding.getJavaName())) {
/* 214 */           return encoding;
/*     */         }
/*     */       } 
/*     */     } 
/* 218 */     return JsonEncoding.UTF8;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<MimeType> getEncodableMimeTypes() {
/* 226 */     return getMimeTypes();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<MediaType> getStreamingMediaTypes() {
/* 231 */     return Collections.unmodifiableList(this.streamingMediaTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Object> getEncodeHints(@Nullable ResolvableType actualType, ResolvableType elementType, @Nullable MediaType mediaType, ServerHttpRequest request, ServerHttpResponse response) {
/* 238 */     return (actualType != null) ? getHints(actualType) : Hints.none();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <A extends java.lang.annotation.Annotation> A getAnnotation(MethodParameter parameter, Class<A> annotType) {
/* 245 */     return (A)parameter.getMethodAnnotation(annotType);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/json/AbstractJackson2Encoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */