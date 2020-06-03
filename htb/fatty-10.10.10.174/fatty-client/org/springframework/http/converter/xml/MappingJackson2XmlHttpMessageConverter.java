/*    */ package org.springframework.http.converter.xml;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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
/*    */ public class MappingJackson2XmlHttpMessageConverter
/*    */   extends AbstractJackson2HttpMessageConverter
/*    */ {
/*    */   public MappingJackson2XmlHttpMessageConverter() {
/* 50 */     this(Jackson2ObjectMapperBuilder.xml().build());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MappingJackson2XmlHttpMessageConverter(ObjectMapper objectMapper) {
/* 60 */     super(objectMapper, new MediaType[] { new MediaType("application", "xml"), new MediaType("text", "xml"), new MediaType("application", "*+xml") });
/*    */ 
/*    */     
/* 63 */     Assert.isInstanceOf(XmlMapper.class, objectMapper, "XmlMapper required");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setObjectMapper(ObjectMapper objectMapper) {
/* 73 */     Assert.isInstanceOf(XmlMapper.class, objectMapper, "XmlMapper required");
/* 74 */     super.setObjectMapper(objectMapper);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/converter/xml/MappingJackson2XmlHttpMessageConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */