/*    */ package org.springframework.http.converter.smile;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import com.fasterxml.jackson.dataformat.smile.SmileFactory;
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
/*    */ public class MappingJackson2SmileHttpMessageConverter
/*    */   extends AbstractJackson2HttpMessageConverter
/*    */ {
/*    */   public MappingJackson2SmileHttpMessageConverter() {
/* 50 */     this(Jackson2ObjectMapperBuilder.smile().build());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MappingJackson2SmileHttpMessageConverter(ObjectMapper objectMapper) {
/* 60 */     super(objectMapper, new MediaType("application", "x-jackson-smile"));
/* 61 */     Assert.isInstanceOf(SmileFactory.class, objectMapper.getFactory(), "SmileFactory required");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setObjectMapper(ObjectMapper objectMapper) {
/* 71 */     Assert.isInstanceOf(SmileFactory.class, objectMapper.getFactory(), "SmileFactory required");
/* 72 */     super.setObjectMapper(objectMapper);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/converter/smile/MappingJackson2SmileHttpMessageConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */