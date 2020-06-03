/*    */ package org.springframework.http.converter.json;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.http.HttpInputMessage;
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
/*    */ public class MappingJacksonInputMessage
/*    */   implements HttpInputMessage
/*    */ {
/*    */   private final InputStream body;
/*    */   private final HttpHeaders headers;
/*    */   @Nullable
/*    */   private Class<?> deserializationView;
/*    */   
/*    */   public MappingJacksonInputMessage(InputStream body, HttpHeaders headers) {
/* 44 */     this.body = body;
/* 45 */     this.headers = headers;
/*    */   }
/*    */   
/*    */   public MappingJacksonInputMessage(InputStream body, HttpHeaders headers, Class<?> deserializationView) {
/* 49 */     this(body, headers);
/* 50 */     this.deserializationView = deserializationView;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public InputStream getBody() throws IOException {
/* 56 */     return this.body;
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpHeaders getHeaders() {
/* 61 */     return this.headers;
/*    */   }
/*    */   
/*    */   public void setDeserializationView(@Nullable Class<?> deserializationView) {
/* 65 */     this.deserializationView = deserializationView;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public Class<?> getDeserializationView() {
/* 70 */     return this.deserializationView;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/converter/json/MappingJacksonInputMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */