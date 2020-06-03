/*    */ package org.springframework.http.codec.support;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.springframework.http.codec.CodecConfigurer;
/*    */ import org.springframework.http.codec.ServerCodecConfigurer;
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
/*    */ public class DefaultServerCodecConfigurer
/*    */   extends BaseCodecConfigurer
/*    */   implements ServerCodecConfigurer
/*    */ {
/*    */   public DefaultServerCodecConfigurer() {
/* 30 */     super(new ServerDefaultCodecsImpl());
/*    */   }
/*    */ 
/*    */   
/*    */   public ServerCodecConfigurer.ServerDefaultCodecs defaultCodecs() {
/* 35 */     return (ServerCodecConfigurer.ServerDefaultCodecs)super.defaultCodecs();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/support/DefaultServerCodecConfigurer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */