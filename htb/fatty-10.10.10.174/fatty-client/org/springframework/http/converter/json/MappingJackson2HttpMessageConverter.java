/*    */ package org.springframework.http.converter.json;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import java.io.IOException;
/*    */ import org.springframework.http.MediaType;
/*    */ import org.springframework.lang.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MappingJackson2HttpMessageConverter
/*    */   extends AbstractJackson2HttpMessageConverter
/*    */ {
/*    */   @Nullable
/*    */   private String jsonPrefix;
/*    */   
/*    */   public MappingJackson2HttpMessageConverter() {
/* 59 */     this(Jackson2ObjectMapperBuilder.json().build());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
/* 68 */     super(objectMapper, new MediaType[] { MediaType.APPLICATION_JSON, new MediaType("application", "*+json") });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setJsonPrefix(String jsonPrefix) {
/* 78 */     this.jsonPrefix = jsonPrefix;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setPrefixJson(boolean prefixJson) {
/* 89 */     this.jsonPrefix = prefixJson ? ")]}', " : null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void writePrefix(JsonGenerator generator, Object object) throws IOException {
/* 95 */     if (this.jsonPrefix != null)
/* 96 */       generator.writeRaw(this.jsonPrefix); 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/converter/json/MappingJackson2HttpMessageConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */