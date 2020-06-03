/*    */ package org.springframework.http.codec;
/*    */ 
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
/*    */ public interface ServerCodecConfigurer
/*    */   extends CodecConfigurer
/*    */ {
/*    */   static ServerCodecConfigurer create() {
/* 70 */     return CodecConfigurerFactory.<ServerCodecConfigurer>create(ServerCodecConfigurer.class);
/*    */   }
/*    */   
/*    */   ServerDefaultCodecs defaultCodecs();
/*    */   
/*    */   public static interface ServerDefaultCodecs extends CodecConfigurer.DefaultCodecs {
/*    */     void serverSentEventEncoder(Encoder<?> param1Encoder);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/ServerCodecConfigurer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */