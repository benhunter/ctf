/*     */ package org.springframework.http.codec.support;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.springframework.core.codec.ByteArrayDecoder;
/*     */ import org.springframework.core.codec.ByteArrayEncoder;
/*     */ import org.springframework.core.codec.ByteBufferDecoder;
/*     */ import org.springframework.core.codec.ByteBufferEncoder;
/*     */ import org.springframework.core.codec.CharSequenceEncoder;
/*     */ import org.springframework.core.codec.DataBufferDecoder;
/*     */ import org.springframework.core.codec.DataBufferEncoder;
/*     */ import org.springframework.core.codec.Decoder;
/*     */ import org.springframework.core.codec.Encoder;
/*     */ import org.springframework.core.codec.ResourceDecoder;
/*     */ import org.springframework.core.codec.StringDecoder;
/*     */ import org.springframework.http.codec.CodecConfigurer;
/*     */ import org.springframework.http.codec.DecoderHttpMessageReader;
/*     */ import org.springframework.http.codec.EncoderHttpMessageWriter;
/*     */ import org.springframework.http.codec.FormHttpMessageReader;
/*     */ import org.springframework.http.codec.HttpMessageReader;
/*     */ import org.springframework.http.codec.HttpMessageWriter;
/*     */ import org.springframework.http.codec.ResourceHttpMessageWriter;
/*     */ import org.springframework.http.codec.json.Jackson2JsonDecoder;
/*     */ import org.springframework.http.codec.json.Jackson2JsonEncoder;
/*     */ import org.springframework.http.codec.json.Jackson2SmileDecoder;
/*     */ import org.springframework.http.codec.json.Jackson2SmileEncoder;
/*     */ import org.springframework.http.codec.protobuf.ProtobufDecoder;
/*     */ import org.springframework.http.codec.protobuf.ProtobufEncoder;
/*     */ import org.springframework.http.codec.protobuf.ProtobufHttpMessageWriter;
/*     */ import org.springframework.http.codec.xml.Jaxb2XmlDecoder;
/*     */ import org.springframework.http.codec.xml.Jaxb2XmlEncoder;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ class BaseDefaultCodecs
/*     */   implements CodecConfigurer.DefaultCodecs
/*     */ {
/*     */   static final boolean jackson2Present;
/*     */   private static final boolean jackson2SmilePresent;
/*     */   private static final boolean jaxb2Present;
/*     */   private static final boolean protobufPresent;
/*     */   @Nullable
/*     */   private Decoder<?> jackson2JsonDecoder;
/*     */   @Nullable
/*     */   private Encoder<?> jackson2JsonEncoder;
/*     */   @Nullable
/*     */   private Decoder<?> protobufDecoder;
/*     */   @Nullable
/*     */   private Encoder<?> protobufEncoder;
/*     */   @Nullable
/*     */   private Decoder<?> jaxb2Decoder;
/*     */   @Nullable
/*     */   private Encoder<?> jaxb2Encoder;
/*     */   
/*     */   static {
/*  71 */     ClassLoader classLoader = BaseCodecConfigurer.class.getClassLoader();
/*     */     
/*  73 */     jackson2Present = (ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", classLoader) && ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", classLoader));
/*  74 */     jackson2SmilePresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.smile.SmileFactory", classLoader);
/*  75 */     jaxb2Present = ClassUtils.isPresent("javax.xml.bind.Binder", classLoader);
/*  76 */     protobufPresent = ClassUtils.isPresent("com.google.protobuf.Message", classLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean enableLoggingRequestDetails = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean registerDefaults = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void jackson2JsonDecoder(Decoder<?> decoder) {
/* 105 */     this.jackson2JsonDecoder = decoder;
/*     */   }
/*     */ 
/*     */   
/*     */   public void jackson2JsonEncoder(Encoder<?> encoder) {
/* 110 */     this.jackson2JsonEncoder = encoder;
/*     */   }
/*     */ 
/*     */   
/*     */   public void protobufDecoder(Decoder<?> decoder) {
/* 115 */     this.protobufDecoder = decoder;
/*     */   }
/*     */ 
/*     */   
/*     */   public void protobufEncoder(Encoder<?> encoder) {
/* 120 */     this.protobufEncoder = encoder;
/*     */   }
/*     */ 
/*     */   
/*     */   public void jaxb2Decoder(Decoder<?> decoder) {
/* 125 */     this.jaxb2Decoder = decoder;
/*     */   }
/*     */ 
/*     */   
/*     */   public void jaxb2Encoder(Encoder<?> encoder) {
/* 130 */     this.jaxb2Encoder = encoder;
/*     */   }
/*     */ 
/*     */   
/*     */   public void enableLoggingRequestDetails(boolean enable) {
/* 135 */     this.enableLoggingRequestDetails = enable;
/*     */   }
/*     */   
/*     */   protected boolean isEnableLoggingRequestDetails() {
/* 139 */     return this.enableLoggingRequestDetails;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void registerDefaults(boolean registerDefaults) {
/* 146 */     this.registerDefaults = registerDefaults;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final List<HttpMessageReader<?>> getTypedReaders() {
/* 154 */     if (!this.registerDefaults) {
/* 155 */       return Collections.emptyList();
/*     */     }
/* 157 */     List<HttpMessageReader<?>> readers = new ArrayList<>();
/* 158 */     readers.add(new DecoderHttpMessageReader((Decoder)new ByteArrayDecoder()));
/* 159 */     readers.add(new DecoderHttpMessageReader((Decoder)new ByteBufferDecoder()));
/* 160 */     readers.add(new DecoderHttpMessageReader((Decoder)new DataBufferDecoder()));
/* 161 */     readers.add(new DecoderHttpMessageReader((Decoder)new ResourceDecoder()));
/* 162 */     readers.add(new DecoderHttpMessageReader((Decoder)StringDecoder.textPlainOnly()));
/* 163 */     if (protobufPresent) {
/* 164 */       Decoder<?> decoder = (this.protobufDecoder != null) ? this.protobufDecoder : (Decoder<?>)new ProtobufDecoder();
/* 165 */       readers.add(new DecoderHttpMessageReader(decoder));
/*     */     } 
/*     */     
/* 168 */     FormHttpMessageReader formReader = new FormHttpMessageReader();
/* 169 */     formReader.setEnableLoggingRequestDetails(this.enableLoggingRequestDetails);
/* 170 */     readers.add(formReader);
/*     */     
/* 172 */     extendTypedReaders(readers);
/*     */     
/* 174 */     return readers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void extendTypedReaders(List<HttpMessageReader<?>> typedReaders) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final List<HttpMessageReader<?>> getObjectReaders() {
/* 187 */     if (!this.registerDefaults) {
/* 188 */       return Collections.emptyList();
/*     */     }
/* 190 */     List<HttpMessageReader<?>> readers = new ArrayList<>();
/* 191 */     if (jackson2Present) {
/* 192 */       readers.add(new DecoderHttpMessageReader(getJackson2JsonDecoder()));
/*     */     }
/* 194 */     if (jackson2SmilePresent) {
/* 195 */       readers.add(new DecoderHttpMessageReader((Decoder)new Jackson2SmileDecoder()));
/*     */     }
/* 197 */     if (jaxb2Present) {
/* 198 */       Decoder<?> decoder = (this.jaxb2Decoder != null) ? this.jaxb2Decoder : (Decoder<?>)new Jaxb2XmlDecoder();
/* 199 */       readers.add(new DecoderHttpMessageReader(decoder));
/*     */     } 
/* 201 */     extendObjectReaders(readers);
/* 202 */     return readers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void extendObjectReaders(List<HttpMessageReader<?>> objectReaders) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final List<HttpMessageReader<?>> getCatchAllReaders() {
/* 215 */     if (!this.registerDefaults) {
/* 216 */       return Collections.emptyList();
/*     */     }
/* 218 */     List<HttpMessageReader<?>> result = new ArrayList<>();
/* 219 */     result.add(new DecoderHttpMessageReader((Decoder)StringDecoder.allMimeTypes()));
/* 220 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final List<HttpMessageWriter<?>> getTypedWriters(boolean forMultipart) {
/* 231 */     if (!this.registerDefaults) {
/* 232 */       return Collections.emptyList();
/*     */     }
/* 234 */     List<HttpMessageWriter<?>> writers = new ArrayList<>();
/* 235 */     writers.add(new EncoderHttpMessageWriter((Encoder)new ByteArrayEncoder()));
/* 236 */     writers.add(new EncoderHttpMessageWriter((Encoder)new ByteBufferEncoder()));
/* 237 */     writers.add(new EncoderHttpMessageWriter((Encoder)new DataBufferEncoder()));
/* 238 */     writers.add(new ResourceHttpMessageWriter());
/* 239 */     writers.add(new EncoderHttpMessageWriter((Encoder)CharSequenceEncoder.textPlainOnly()));
/*     */     
/* 241 */     if (!forMultipart) {
/* 242 */       extendTypedWriters(writers);
/*     */     }
/* 244 */     if (protobufPresent) {
/* 245 */       Encoder<?> encoder = (this.protobufEncoder != null) ? this.protobufEncoder : (Encoder<?>)new ProtobufEncoder();
/* 246 */       writers.add(new ProtobufHttpMessageWriter(encoder));
/*     */     } 
/* 248 */     return writers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void extendTypedWriters(List<HttpMessageWriter<?>> typedWriters) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final List<HttpMessageWriter<?>> getObjectWriters(boolean forMultipart) {
/* 264 */     if (!this.registerDefaults) {
/* 265 */       return Collections.emptyList();
/*     */     }
/* 267 */     List<HttpMessageWriter<?>> writers = new ArrayList<>();
/* 268 */     if (jackson2Present) {
/* 269 */       writers.add(new EncoderHttpMessageWriter(getJackson2JsonEncoder()));
/*     */     }
/* 271 */     if (jackson2SmilePresent) {
/* 272 */       writers.add(new EncoderHttpMessageWriter((Encoder)new Jackson2SmileEncoder()));
/*     */     }
/* 274 */     if (jaxb2Present) {
/* 275 */       Encoder<?> encoder = (this.jaxb2Encoder != null) ? this.jaxb2Encoder : (Encoder<?>)new Jaxb2XmlEncoder();
/* 276 */       writers.add(new EncoderHttpMessageWriter(encoder));
/*     */     } 
/*     */     
/* 279 */     if (!forMultipart) {
/* 280 */       extendObjectWriters(writers);
/*     */     }
/* 282 */     return writers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void extendObjectWriters(List<HttpMessageWriter<?>> objectWriters) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   List<HttpMessageWriter<?>> getCatchAllWriters() {
/* 295 */     if (!this.registerDefaults) {
/* 296 */       return Collections.emptyList();
/*     */     }
/* 298 */     List<HttpMessageWriter<?>> result = new ArrayList<>();
/* 299 */     result.add(new EncoderHttpMessageWriter((Encoder)CharSequenceEncoder.allMimeTypes()));
/* 300 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Decoder<?> getJackson2JsonDecoder() {
/* 307 */     return (this.jackson2JsonDecoder != null) ? this.jackson2JsonDecoder : (Decoder<?>)new Jackson2JsonDecoder();
/*     */   }
/*     */   
/*     */   protected Encoder<?> getJackson2JsonEncoder() {
/* 311 */     return (this.jackson2JsonEncoder != null) ? this.jackson2JsonEncoder : (Encoder<?>)new Jackson2JsonEncoder();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/support/BaseDefaultCodecs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */