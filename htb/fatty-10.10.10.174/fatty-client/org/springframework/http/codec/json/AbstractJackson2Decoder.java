/*     */ package org.springframework.http.codec.json;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonFactory;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.ObjectCodec;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.ObjectReader;
/*     */ import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
/*     */ import com.fasterxml.jackson.databind.util.TokenBuffer;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.codec.CodecException;
/*     */ import org.springframework.core.codec.DecodingException;
/*     */ import org.springframework.core.codec.Hints;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.log.LogFormatUtils;
/*     */ import org.springframework.http.codec.HttpMessageDecoder;
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
/*     */ public abstract class AbstractJackson2Decoder
/*     */   extends Jackson2CodecSupport
/*     */   implements HttpMessageDecoder<Object>
/*     */ {
/*     */   private final JsonFactory jsonFactory;
/*     */   
/*     */   protected AbstractJackson2Decoder(ObjectMapper mapper, MimeType... mimeTypes) {
/*  71 */     super(mapper, mimeTypes);
/*  72 */     this
/*  73 */       .jsonFactory = mapper.getFactory().copy().disable(JsonFactory.Feature.USE_THREAD_LOCAL_FOR_BUFFER_RECYCLING);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canDecode(ResolvableType elementType, @Nullable MimeType mimeType) {
/*  79 */     JavaType javaType = getObjectMapper().getTypeFactory().constructType(elementType.getType());
/*     */     
/*  81 */     return (!CharSequence.class.isAssignableFrom(elementType.toClass()) && 
/*  82 */       getObjectMapper().canDeserialize(javaType) && supportsMimeType(mimeType));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Flux<Object> decode(Publisher<DataBuffer> input, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/*  89 */     Flux<TokenBuffer> tokens = Jackson2Tokenizer.tokenize(
/*  90 */         Flux.from(input), this.jsonFactory, getObjectMapper(), true);
/*  91 */     return decodeInternal(tokens, elementType, mimeType, hints);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mono<Object> decodeToMono(Publisher<DataBuffer> input, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/*  98 */     Flux<TokenBuffer> tokens = Jackson2Tokenizer.tokenize(
/*  99 */         Flux.from(input), this.jsonFactory, getObjectMapper(), false);
/* 100 */     return decodeInternal(tokens, elementType, mimeType, hints).singleOrEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Flux<Object> decodeInternal(Flux<TokenBuffer> tokens, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/* 106 */     Assert.notNull(tokens, "'tokens' must not be null");
/* 107 */     Assert.notNull(elementType, "'elementType' must not be null");
/*     */     
/* 109 */     MethodParameter param = getParameter(elementType);
/* 110 */     Class<?> contextClass = (param != null) ? param.getContainingClass() : null;
/* 111 */     JavaType javaType = getJavaType(elementType.getType(), contextClass);
/* 112 */     Class<?> jsonView = (hints != null) ? (Class)hints.get(Jackson2CodecSupport.JSON_VIEW_HINT) : null;
/*     */ 
/*     */ 
/*     */     
/* 116 */     ObjectReader reader = (jsonView != null) ? getObjectMapper().readerWithView(jsonView).forType(javaType) : getObjectMapper().readerFor(javaType);
/*     */     
/* 118 */     return tokens.flatMap(tokenBuffer -> {
/*     */           try {
/*     */             Object value = reader.readValue(tokenBuffer.asParser((ObjectCodec)getObjectMapper()));
/*     */ 
/*     */             
/*     */             if (!Hints.isLoggingSuppressed(hints)) {
/*     */               LogFormatUtils.traceDebug(this.logger, ());
/*     */             }
/*     */ 
/*     */             
/*     */             return (Publisher)Mono.justOrEmpty(value);
/* 129 */           } catch (InvalidDefinitionException ex) {
/*     */             
/*     */             return (Publisher)Mono.error((Throwable)new CodecException("Type definition error: " + ex.getType(), (Throwable)ex));
/* 132 */           } catch (JsonProcessingException ex) {
/*     */             
/*     */             return (Publisher)Mono.error((Throwable)new DecodingException("JSON decoding error: " + ex.getOriginalMessage(), (Throwable)ex));
/* 135 */           } catch (IOException ex) {
/*     */             return (Publisher)Mono.error((Throwable)new DecodingException("I/O error while parsing input stream", ex));
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
/*     */   public Map<String, Object> getDecodeHints(ResolvableType actualType, ResolvableType elementType, ServerHttpRequest request, ServerHttpResponse response) {
/* 148 */     return getHints(actualType);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<MimeType> getDecodableMimeTypes() {
/* 153 */     return getMimeTypes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <A extends java.lang.annotation.Annotation> A getAnnotation(MethodParameter parameter, Class<A> annotType) {
/* 160 */     return (A)parameter.getParameterAnnotation(annotType);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/json/AbstractJackson2Decoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */