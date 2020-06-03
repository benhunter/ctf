/*     */ package org.springframework.http.codec.multipart;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Collectors;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.codec.Hints;
/*     */ import org.springframework.core.log.LogFormatUtils;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.ReactiveHttpInputMessage;
/*     */ import org.springframework.http.codec.HttpMessageReader;
/*     */ import org.springframework.http.codec.LoggingCodecSupport;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ public class MultipartHttpMessageReader
/*     */   extends LoggingCodecSupport
/*     */   implements HttpMessageReader<MultiValueMap<String, Part>>
/*     */ {
/*  55 */   private static final ResolvableType MULTIPART_VALUE_TYPE = ResolvableType.forClassWithGenerics(MultiValueMap.class, new Class[] { String.class, Part.class });
/*     */ 
/*     */   
/*     */   private final HttpMessageReader<Part> partReader;
/*     */ 
/*     */ 
/*     */   
/*     */   public MultipartHttpMessageReader(HttpMessageReader<Part> partReader) {
/*  63 */     Assert.notNull(partReader, "'partReader' is required");
/*  64 */     this.partReader = partReader;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<MediaType> getReadableMediaTypes() {
/*  70 */     return Collections.singletonList(MediaType.MULTIPART_FORM_DATA);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canRead(ResolvableType elementType, @Nullable MediaType mediaType) {
/*  75 */     return (MULTIPART_VALUE_TYPE.isAssignableFrom(elementType) && (mediaType == null || MediaType.MULTIPART_FORM_DATA
/*  76 */       .isCompatibleWith(mediaType)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Flux<MultiValueMap<String, Part>> read(ResolvableType elementType, ReactiveHttpInputMessage message, Map<String, Object> hints) {
/*  84 */     return Flux.from((Publisher)readMono(elementType, message, hints));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mono<MultiValueMap<String, Part>> readMono(ResolvableType elementType, ReactiveHttpInputMessage inputMessage, Map<String, Object> hints) {
/*  92 */     Map<String, Object> allHints = Hints.merge(hints, Hints.SUPPRESS_LOGGING_HINT, Boolean.valueOf(true));
/*     */     
/*  94 */     return this.partReader.read(elementType, inputMessage, allHints)
/*  95 */       .collectMultimap(Part::name)
/*  96 */       .doOnNext(map -> LogFormatUtils.traceDebug(this.logger, ()))
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 101 */       .map(this::toMultiValueMap);
/*     */   }
/*     */   
/*     */   private LinkedMultiValueMap<String, Part> toMultiValueMap(Map<String, Collection<Part>> map) {
/* 105 */     return new LinkedMultiValueMap((Map)map.entrySet().stream()
/* 106 */         .collect(Collectors.toMap(Map.Entry::getKey, e -> toList((Collection<Part>)e.getValue()))));
/*     */   }
/*     */   
/*     */   private List<Part> toList(Collection<Part> collection) {
/* 110 */     return (collection instanceof List) ? (List<Part>)collection : new ArrayList<>(collection);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/multipart/MultipartHttpMessageReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */