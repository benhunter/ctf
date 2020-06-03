/*    */ package org.springframework.http.codec.support;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.springframework.core.codec.Encoder;
/*    */ import org.springframework.http.codec.HttpMessageReader;
/*    */ import org.springframework.http.codec.HttpMessageWriter;
/*    */ import org.springframework.http.codec.ServerCodecConfigurer;
/*    */ import org.springframework.http.codec.ServerSentEventHttpMessageWriter;
/*    */ import org.springframework.http.codec.multipart.MultipartHttpMessageReader;
/*    */ import org.springframework.http.codec.multipart.SynchronossPartHttpMessageReader;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ class ServerDefaultCodecsImpl
/*    */   extends BaseDefaultCodecs
/*    */   implements ServerCodecConfigurer.ServerDefaultCodecs
/*    */ {
/* 38 */   private static final boolean synchronossMultipartPresent = ClassUtils.isPresent("org.synchronoss.cloud.nio.multipart.NioMultipartParser", DefaultServerCodecConfigurer.class
/* 39 */       .getClassLoader());
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   private Encoder<?> sseEncoder;
/*    */ 
/*    */ 
/*    */   
/*    */   public void serverSentEventEncoder(Encoder<?> encoder) {
/* 48 */     this.sseEncoder = encoder;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void extendTypedReaders(List<HttpMessageReader<?>> typedReaders) {
/* 54 */     if (synchronossMultipartPresent) {
/* 55 */       boolean enable = isEnableLoggingRequestDetails();
/*    */       
/* 57 */       SynchronossPartHttpMessageReader partReader = new SynchronossPartHttpMessageReader();
/* 58 */       partReader.setEnableLoggingRequestDetails(enable);
/* 59 */       typedReaders.add(partReader);
/*    */       
/* 61 */       MultipartHttpMessageReader reader = new MultipartHttpMessageReader((HttpMessageReader)partReader);
/* 62 */       reader.setEnableLoggingRequestDetails(enable);
/* 63 */       typedReaders.add(reader);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void extendObjectWriters(List<HttpMessageWriter<?>> objectWriters) {
/* 69 */     objectWriters.add(new ServerSentEventHttpMessageWriter(getSseEncoder()));
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   private Encoder<?> getSseEncoder() {
/* 74 */     return (this.sseEncoder != null) ? this.sseEncoder : (jackson2Present ? getJackson2JsonEncoder() : null);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/support/ServerDefaultCodecsImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */