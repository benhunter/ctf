/*    */ package org.springframework.http.codec.json;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
/*    */ import org.springframework.util.MimeType;
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
/*    */ public class Jackson2JsonDecoder
/*    */   extends AbstractJackson2Decoder
/*    */ {
/*    */   public Jackson2JsonDecoder() {
/* 36 */     super(Jackson2ObjectMapperBuilder.json().build(), new MimeType[0]);
/*    */   }
/*    */   
/*    */   public Jackson2JsonDecoder(ObjectMapper mapper, MimeType... mimeTypes) {
/* 40 */     super(mapper, mimeTypes);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/json/Jackson2JsonDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */