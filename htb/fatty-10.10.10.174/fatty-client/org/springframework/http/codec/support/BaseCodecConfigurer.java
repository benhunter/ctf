/*     */ package org.springframework.http.codec.support;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.codec.Decoder;
/*     */ import org.springframework.core.codec.Encoder;
/*     */ import org.springframework.http.codec.CodecConfigurer;
/*     */ import org.springframework.http.codec.DecoderHttpMessageReader;
/*     */ import org.springframework.http.codec.EncoderHttpMessageWriter;
/*     */ import org.springframework.http.codec.HttpMessageReader;
/*     */ import org.springframework.http.codec.HttpMessageWriter;
/*     */ import org.springframework.util.Assert;
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
/*     */ class BaseCodecConfigurer
/*     */   implements CodecConfigurer
/*     */ {
/*     */   private final BaseDefaultCodecs defaultCodecs;
/*  43 */   private final DefaultCustomCodecs customCodecs = new DefaultCustomCodecs();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   BaseCodecConfigurer(BaseDefaultCodecs defaultCodecs) {
/*  51 */     Assert.notNull(defaultCodecs, "'defaultCodecs' is required");
/*  52 */     this.defaultCodecs = defaultCodecs;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CodecConfigurer.DefaultCodecs defaultCodecs() {
/*  58 */     return this.defaultCodecs;
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerDefaults(boolean shouldRegister) {
/*  63 */     this.defaultCodecs.registerDefaults(shouldRegister);
/*     */   }
/*     */ 
/*     */   
/*     */   public CodecConfigurer.CustomCodecs customCodecs() {
/*  68 */     return this.customCodecs;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<HttpMessageReader<?>> getReaders() {
/*  73 */     List<HttpMessageReader<?>> result = new ArrayList<>();
/*     */     
/*  75 */     result.addAll(this.defaultCodecs.getTypedReaders());
/*  76 */     result.addAll(this.customCodecs.getTypedReaders());
/*     */     
/*  78 */     result.addAll(this.defaultCodecs.getObjectReaders());
/*  79 */     result.addAll(this.customCodecs.getObjectReaders());
/*     */     
/*  81 */     result.addAll(this.defaultCodecs.getCatchAllReaders());
/*  82 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<HttpMessageWriter<?>> getWriters() {
/*  87 */     return getWritersInternal(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<HttpMessageWriter<?>> getWritersInternal(boolean forMultipart) {
/*  97 */     List<HttpMessageWriter<?>> result = new ArrayList<>();
/*     */     
/*  99 */     result.addAll(this.defaultCodecs.getTypedWriters(forMultipart));
/* 100 */     result.addAll(this.customCodecs.getTypedWriters());
/*     */     
/* 102 */     result.addAll(this.defaultCodecs.getObjectWriters(forMultipart));
/* 103 */     result.addAll(this.customCodecs.getObjectWriters());
/*     */     
/* 105 */     result.addAll(this.defaultCodecs.getCatchAllWriters());
/* 106 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class DefaultCustomCodecs
/*     */     implements CodecConfigurer.CustomCodecs
/*     */   {
/* 115 */     private final List<HttpMessageReader<?>> typedReaders = new ArrayList<>();
/*     */     
/* 117 */     private final List<HttpMessageWriter<?>> typedWriters = new ArrayList<>();
/*     */     
/* 119 */     private final List<HttpMessageReader<?>> objectReaders = new ArrayList<>();
/*     */     
/* 121 */     private final List<HttpMessageWriter<?>> objectWriters = new ArrayList<>();
/*     */ 
/*     */ 
/*     */     
/*     */     public void decoder(Decoder<?> decoder) {
/* 126 */       reader((HttpMessageReader<?>)new DecoderHttpMessageReader(decoder));
/*     */     }
/*     */ 
/*     */     
/*     */     public void encoder(Encoder<?> encoder) {
/* 131 */       writer((HttpMessageWriter<?>)new EncoderHttpMessageWriter(encoder));
/*     */     }
/*     */ 
/*     */     
/*     */     public void reader(HttpMessageReader<?> reader) {
/* 136 */       boolean canReadToObject = reader.canRead(ResolvableType.forClass(Object.class), null);
/* 137 */       (canReadToObject ? this.objectReaders : this.typedReaders).add(reader);
/*     */     }
/*     */ 
/*     */     
/*     */     public void writer(HttpMessageWriter<?> writer) {
/* 142 */       boolean canWriteObject = writer.canWrite(ResolvableType.forClass(Object.class), null);
/* 143 */       (canWriteObject ? this.objectWriters : this.typedWriters).add(writer);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     List<HttpMessageReader<?>> getTypedReaders() {
/* 150 */       return this.typedReaders;
/*     */     }
/*     */     
/*     */     List<HttpMessageWriter<?>> getTypedWriters() {
/* 154 */       return this.typedWriters;
/*     */     }
/*     */     
/*     */     List<HttpMessageReader<?>> getObjectReaders() {
/* 158 */       return this.objectReaders;
/*     */     }
/*     */     
/*     */     List<HttpMessageWriter<?>> getObjectWriters() {
/* 162 */       return this.objectWriters;
/*     */     }
/*     */     
/*     */     private DefaultCustomCodecs() {}
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/support/BaseCodecConfigurer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */