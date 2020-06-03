/*    */ package org.springframework.http.codec;
/*    */ 
/*    */ import org.springframework.core.codec.Decoder;
/*    */ import org.springframework.core.codec.Encoder;
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
/*    */ public interface ClientCodecConfigurer
/*    */   extends CodecConfigurer
/*    */ {
/*    */   static ClientCodecConfigurer create() {
/* 71 */     return CodecConfigurerFactory.<ClientCodecConfigurer>create(ClientCodecConfigurer.class);
/*    */   }
/*    */   
/*    */   ClientDefaultCodecs defaultCodecs();
/*    */   
/*    */   public static interface MultipartCodecs {
/*    */     MultipartCodecs encoder(Encoder<?> param1Encoder);
/*    */     
/*    */     MultipartCodecs writer(HttpMessageWriter<?> param1HttpMessageWriter);
/*    */   }
/*    */   
/*    */   public static interface ClientDefaultCodecs extends CodecConfigurer.DefaultCodecs {
/*    */     ClientCodecConfigurer.MultipartCodecs multipartCodecs();
/*    */     
/*    */     void serverSentEventDecoder(Decoder<?> param1Decoder);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/ClientCodecConfigurer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */