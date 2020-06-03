/*    */ package org.springframework.http.converter.cbor;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
/*    */ import org.springframework.http.MediaType;
/*    */ import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
/*    */ import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
/*    */ import org.springframework.util.Assert;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MappingJackson2CborHttpMessageConverter
/*    */   extends AbstractJackson2HttpMessageConverter
/*    */ {
/*    */   public MappingJackson2CborHttpMessageConverter() {
/* 50 */     this(Jackson2ObjectMapperBuilder.cbor().build());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MappingJackson2CborHttpMessageConverter(ObjectMapper objectMapper) {
/* 60 */     super(objectMapper, new MediaType("application", "cbor"));
/* 61 */     Assert.isInstanceOf(CBORFactory.class, objectMapper.getFactory(), "CBORFactory required");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setObjectMapper(ObjectMapper objectMapper) {
/* 71 */     Assert.isInstanceOf(CBORFactory.class, objectMapper.getFactory(), "CBORFactory required");
/* 72 */     super.setObjectMapper(objectMapper);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/converter/cbor/MappingJackson2CborHttpMessageConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */