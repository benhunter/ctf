/*     */ package org.springframework.http.codec;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.ReactiveHttpInputMessage;
/*     */ import org.springframework.http.server.reactive.ServerHttpRequest;
/*     */ import org.springframework.http.server.reactive.ServerHttpResponse;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public interface HttpMessageReader<T>
/*     */ {
/*     */   List<MediaType> getReadableMediaTypes();
/*     */   
/*     */   boolean canRead(ResolvableType paramResolvableType, @Nullable MediaType paramMediaType);
/*     */   
/*     */   Flux<T> read(ResolvableType paramResolvableType, ReactiveHttpInputMessage paramReactiveHttpInputMessage, Map<String, Object> paramMap);
/*     */   
/*     */   Mono<T> readMono(ResolvableType paramResolvableType, ReactiveHttpInputMessage paramReactiveHttpInputMessage, Map<String, Object> paramMap);
/*     */   
/*     */   default Flux<T> read(ResolvableType actualType, ResolvableType elementType, ServerHttpRequest request, ServerHttpResponse response, Map<String, Object> hints) {
/*  94 */     return read(elementType, (ReactiveHttpInputMessage)request, hints);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default Mono<T> readMono(ResolvableType actualType, ResolvableType elementType, ServerHttpRequest request, ServerHttpResponse response, Map<String, Object> hints) {
/* 113 */     return readMono(elementType, (ReactiveHttpInputMessage)request, hints);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/HttpMessageReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */