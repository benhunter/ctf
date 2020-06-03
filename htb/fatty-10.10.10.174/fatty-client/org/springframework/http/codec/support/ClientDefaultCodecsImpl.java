/*     */ package org.springframework.http.codec.support;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.function.Supplier;
/*     */ import org.springframework.core.codec.Decoder;
/*     */ import org.springframework.core.codec.Encoder;
/*     */ import org.springframework.http.codec.ClientCodecConfigurer;
/*     */ import org.springframework.http.codec.EncoderHttpMessageWriter;
/*     */ import org.springframework.http.codec.FormHttpMessageWriter;
/*     */ import org.springframework.http.codec.HttpMessageReader;
/*     */ import org.springframework.http.codec.HttpMessageWriter;
/*     */ import org.springframework.http.codec.ServerSentEventHttpMessageReader;
/*     */ import org.springframework.http.codec.multipart.MultipartHttpMessageWriter;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ClientDefaultCodecsImpl
/*     */   extends BaseDefaultCodecs
/*     */   implements ClientCodecConfigurer.ClientDefaultCodecs
/*     */ {
/*     */   @Nullable
/*     */   private DefaultMultipartCodecs multipartCodecs;
/*     */   @Nullable
/*     */   private Decoder<?> sseDecoder;
/*     */   @Nullable
/*     */   private Supplier<List<HttpMessageWriter<?>>> partWritersSupplier;
/*     */   
/*     */   void setPartWritersSupplier(Supplier<List<HttpMessageWriter<?>>> supplier) {
/*  59 */     this.partWritersSupplier = supplier;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClientCodecConfigurer.MultipartCodecs multipartCodecs() {
/*  65 */     if (this.multipartCodecs == null) {
/*  66 */       this.multipartCodecs = new DefaultMultipartCodecs();
/*     */     }
/*  68 */     return this.multipartCodecs;
/*     */   }
/*     */ 
/*     */   
/*     */   public void serverSentEventDecoder(Decoder<?> decoder) {
/*  73 */     this.sseDecoder = decoder;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void extendObjectReaders(List<HttpMessageReader<?>> objectReaders) {
/*  79 */     objectReaders.add(new ServerSentEventHttpMessageReader(getSseDecoder()));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Decoder<?> getSseDecoder() {
/*  84 */     return (this.sseDecoder != null) ? this.sseDecoder : (jackson2Present ? getJackson2JsonDecoder() : null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void extendTypedWriters(List<HttpMessageWriter<?>> typedWriters) {
/*  90 */     FormHttpMessageWriter formWriter = new FormHttpMessageWriter();
/*  91 */     formWriter.setEnableLoggingRequestDetails(isEnableLoggingRequestDetails());
/*     */     
/*  93 */     MultipartHttpMessageWriter multipartWriter = new MultipartHttpMessageWriter(getPartWriters(), (HttpMessageWriter)formWriter);
/*  94 */     multipartWriter.setEnableLoggingRequestDetails(isEnableLoggingRequestDetails());
/*     */     
/*  96 */     typedWriters.add(multipartWriter);
/*     */   }
/*     */   
/*     */   private List<HttpMessageWriter<?>> getPartWriters() {
/* 100 */     if (this.multipartCodecs != null) {
/* 101 */       return this.multipartCodecs.getWriters();
/*     */     }
/* 103 */     if (this.partWritersSupplier != null) {
/* 104 */       return this.partWritersSupplier.get();
/*     */     }
/*     */     
/* 107 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class DefaultMultipartCodecs
/*     */     implements ClientCodecConfigurer.MultipartCodecs
/*     */   {
/* 117 */     private final List<HttpMessageWriter<?>> writers = new ArrayList<>();
/*     */ 
/*     */     
/*     */     public ClientCodecConfigurer.MultipartCodecs encoder(Encoder<?> encoder) {
/* 121 */       writer((HttpMessageWriter<?>)new EncoderHttpMessageWriter(encoder));
/* 122 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ClientCodecConfigurer.MultipartCodecs writer(HttpMessageWriter<?> writer) {
/* 127 */       this.writers.add(writer);
/* 128 */       return this;
/*     */     }
/*     */     
/*     */     List<HttpMessageWriter<?>> getWriters() {
/* 132 */       return this.writers;
/*     */     }
/*     */     
/*     */     private DefaultMultipartCodecs() {}
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/support/ClientDefaultCodecsImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */