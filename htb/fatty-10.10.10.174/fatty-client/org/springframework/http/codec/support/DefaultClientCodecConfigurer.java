/*    */ package org.springframework.http.codec.support;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.springframework.http.codec.ClientCodecConfigurer;
/*    */ import org.springframework.http.codec.CodecConfigurer;
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
/*    */ public class DefaultClientCodecConfigurer
/*    */   extends BaseCodecConfigurer
/*    */   implements ClientCodecConfigurer
/*    */ {
/*    */   public DefaultClientCodecConfigurer() {
/* 30 */     super(new ClientDefaultCodecsImpl());
/* 31 */     ((ClientDefaultCodecsImpl)defaultCodecs()).setPartWritersSupplier(() -> getWritersInternal(true));
/*    */   }
/*    */ 
/*    */   
/*    */   public ClientCodecConfigurer.ClientDefaultCodecs defaultCodecs() {
/* 36 */     return (ClientCodecConfigurer.ClientDefaultCodecs)super.defaultCodecs();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/support/DefaultClientCodecConfigurer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */