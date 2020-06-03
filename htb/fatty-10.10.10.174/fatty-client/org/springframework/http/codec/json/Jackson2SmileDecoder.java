/*    */ package org.springframework.http.codec.json;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import com.fasterxml.jackson.dataformat.smile.SmileFactory;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
/*    */ import org.springframework.util.Assert;
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
/*    */ 
/*    */ public class Jackson2SmileDecoder
/*    */   extends AbstractJackson2Decoder
/*    */ {
/* 39 */   private static final MimeType[] DEFAULT_SMILE_MIME_TYPES = new MimeType[] { new MimeType("application", "x-jackson-smile", StandardCharsets.UTF_8), new MimeType("application", "*+x-jackson-smile", StandardCharsets.UTF_8) };
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Jackson2SmileDecoder() {
/* 45 */     this(Jackson2ObjectMapperBuilder.smile().build(), DEFAULT_SMILE_MIME_TYPES);
/*    */   }
/*    */   
/*    */   public Jackson2SmileDecoder(ObjectMapper mapper, MimeType... mimeTypes) {
/* 49 */     super(mapper, mimeTypes);
/* 50 */     Assert.isAssignable(SmileFactory.class, mapper.getFactory().getClass());
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/json/Jackson2SmileDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */