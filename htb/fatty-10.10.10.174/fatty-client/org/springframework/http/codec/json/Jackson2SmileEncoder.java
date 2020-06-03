/*    */ package org.springframework.http.codec.json;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import com.fasterxml.jackson.dataformat.smile.SmileFactory;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.Collections;
/*    */ import org.springframework.http.MediaType;
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
/*    */ 
/*    */ 
/*    */ public class Jackson2SmileEncoder
/*    */   extends AbstractJackson2Encoder
/*    */ {
/* 43 */   private static final MimeType[] DEFAULT_SMILE_MIME_TYPES = new MimeType[] { new MimeType("application", "x-jackson-smile", StandardCharsets.UTF_8), new MimeType("application", "*+x-jackson-smile", StandardCharsets.UTF_8) };
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Jackson2SmileEncoder() {
/* 49 */     this(Jackson2ObjectMapperBuilder.smile().build(), DEFAULT_SMILE_MIME_TYPES);
/*    */   }
/*    */   
/*    */   public Jackson2SmileEncoder(ObjectMapper mapper, MimeType... mimeTypes) {
/* 53 */     super(mapper, mimeTypes);
/* 54 */     Assert.isAssignable(SmileFactory.class, mapper.getFactory().getClass());
/* 55 */     setStreamingMediaTypes(Collections.singletonList(new MediaType("application", "stream+x-jackson-smile")));
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/json/Jackson2SmileEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */