/*    */ package org.springframework.http.converter;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import org.springframework.http.HttpInputMessage;
/*    */ import org.springframework.http.HttpOutputMessage;
/*    */ import org.springframework.http.MediaType;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.StreamUtils;
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
/*    */ public class ByteArrayHttpMessageConverter
/*    */   extends AbstractHttpMessageConverter<byte[]>
/*    */ {
/*    */   public ByteArrayHttpMessageConverter() {
/* 45 */     super(new MediaType[] { MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean supports(Class<?> clazz) {
/* 51 */     return (byte[].class == clazz);
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] readInternal(Class<? extends byte[]> clazz, HttpInputMessage inputMessage) throws IOException {
/* 56 */     long contentLength = inputMessage.getHeaders().getContentLength();
/* 57 */     ByteArrayOutputStream bos = new ByteArrayOutputStream((contentLength >= 0L) ? (int)contentLength : 4096);
/*    */     
/* 59 */     StreamUtils.copy(inputMessage.getBody(), bos);
/* 60 */     return bos.toByteArray();
/*    */   }
/*    */ 
/*    */   
/*    */   protected Long getContentLength(byte[] bytes, @Nullable MediaType contentType) {
/* 65 */     return Long.valueOf(bytes.length);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void writeInternal(byte[] bytes, HttpOutputMessage outputMessage) throws IOException {
/* 70 */     StreamUtils.copy(bytes, outputMessage.getBody());
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/converter/ByteArrayHttpMessageConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */