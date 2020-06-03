/*    */ package org.springframework.http.codec;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.springframework.core.ResolvableType;
/*    */ import org.springframework.core.codec.Encoder;
/*    */ import org.springframework.core.codec.Hints;
/*    */ import org.springframework.http.MediaType;
/*    */ import org.springframework.http.server.reactive.ServerHttpRequest;
/*    */ import org.springframework.http.server.reactive.ServerHttpResponse;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ public interface HttpMessageEncoder<T>
/*    */   extends Encoder<T>
/*    */ {
/*    */   List<MediaType> getStreamingMediaTypes();
/*    */   
/*    */   default Map<String, Object> getEncodeHints(ResolvableType actualType, ResolvableType elementType, @Nullable MediaType mediaType, ServerHttpRequest request, ServerHttpResponse response) {
/* 61 */     return Hints.none();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/HttpMessageEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */