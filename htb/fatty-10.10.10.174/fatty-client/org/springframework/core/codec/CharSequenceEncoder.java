/*     */ package org.springframework.core.codec;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CoderMalfunctionError;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.DataBufferFactory;
/*     */ import org.springframework.core.io.buffer.DataBufferUtils;
/*     */ import org.springframework.core.log.LogFormatUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.MimeType;
/*     */ import org.springframework.util.MimeTypeUtils;
/*     */ import reactor.core.publisher.Flux;
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
/*     */ public final class CharSequenceEncoder
/*     */   extends AbstractEncoder<CharSequence>
/*     */ {
/*  52 */   public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
/*     */   
/*  54 */   private final ConcurrentMap<Charset, Float> charsetToMaxBytesPerChar = new ConcurrentHashMap<>(3);
/*     */ 
/*     */ 
/*     */   
/*     */   private CharSequenceEncoder(MimeType... mimeTypes) {
/*  59 */     super(mimeTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canEncode(ResolvableType elementType, @Nullable MimeType mimeType) {
/*  65 */     Class<?> clazz = elementType.toClass();
/*  66 */     return (super.canEncode(elementType, mimeType) && CharSequence.class.isAssignableFrom(clazz));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Flux<DataBuffer> encode(Publisher<? extends CharSequence> inputStream, DataBufferFactory bufferFactory, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/*  74 */     Charset charset = getCharset(mimeType);
/*     */     
/*  76 */     return Flux.from(inputStream).map(charSequence -> {
/*     */           if (!Hints.isLoggingSuppressed(hints)) {
/*     */             LogFormatUtils.traceDebug(this.logger, ());
/*     */           }
/*     */           
/*     */           boolean release = true;
/*     */           
/*     */           int capacity = calculateCapacity(charSequence, charset);
/*     */           
/*     */           DataBuffer dataBuffer = bufferFactory.allocateBuffer(capacity);
/*     */           
/*     */           try {
/*     */             dataBuffer.write(charSequence, charset);
/*     */             release = false;
/*  90 */           } catch (CoderMalfunctionError ex) {
/*     */             throw new EncodingException("String encoding error: " + ex.getMessage(), ex);
/*     */           } finally {
/*     */             if (release) {
/*     */               DataBufferUtils.release(dataBuffer);
/*     */             }
/*     */           } 
/*     */           return dataBuffer;
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   int calculateCapacity(CharSequence sequence, Charset charset) {
/* 104 */     float maxBytesPerChar = ((Float)this.charsetToMaxBytesPerChar.computeIfAbsent(charset, cs -> Float.valueOf(cs.newEncoder().maxBytesPerChar()))).floatValue();
/* 105 */     float maxBytesForSequence = sequence.length() * maxBytesPerChar;
/* 106 */     return (int)Math.ceil(maxBytesForSequence);
/*     */   }
/*     */   
/*     */   private Charset getCharset(@Nullable MimeType mimeType) {
/* 110 */     if (mimeType != null && mimeType.getCharset() != null) {
/* 111 */       return mimeType.getCharset();
/*     */     }
/*     */     
/* 114 */     return DEFAULT_CHARSET;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharSequenceEncoder textPlainOnly() {
/* 123 */     return new CharSequenceEncoder(new MimeType[] { new MimeType("text", "plain", DEFAULT_CHARSET) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharSequenceEncoder allMimeTypes() {
/* 130 */     return new CharSequenceEncoder(new MimeType[] { new MimeType("text", "plain", DEFAULT_CHARSET), MimeTypeUtils.ALL });
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/codec/CharSequenceEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */