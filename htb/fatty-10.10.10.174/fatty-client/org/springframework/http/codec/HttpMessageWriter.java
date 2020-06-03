/*    */ package org.springframework.http.codec;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.reactivestreams.Publisher;
/*    */ import org.springframework.core.ResolvableType;
/*    */ import org.springframework.http.MediaType;
/*    */ import org.springframework.http.ReactiveHttpOutputMessage;
/*    */ import org.springframework.http.server.reactive.ServerHttpRequest;
/*    */ import org.springframework.http.server.reactive.ServerHttpResponse;
/*    */ import org.springframework.lang.Nullable;
/*    */ import reactor.core.publisher.Mono;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface HttpMessageWriter<T>
/*    */ {
/*    */   List<MediaType> getWritableMediaTypes();
/*    */   
/*    */   boolean canWrite(ResolvableType paramResolvableType, @Nullable MediaType paramMediaType);
/*    */   
/*    */   Mono<Void> write(Publisher<? extends T> paramPublisher, ResolvableType paramResolvableType, @Nullable MediaType paramMediaType, ReactiveHttpOutputMessage paramReactiveHttpOutputMessage, Map<String, Object> paramMap);
/*    */   
/*    */   default Mono<Void> write(Publisher<? extends T> inputStream, ResolvableType actualType, ResolvableType elementType, @Nullable MediaType mediaType, ServerHttpRequest request, ServerHttpResponse response, Map<String, Object> hints) {
/* 90 */     return write(inputStream, elementType, mediaType, (ReactiveHttpOutputMessage)response, hints);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/HttpMessageWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */