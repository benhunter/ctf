/*     */ package org.springframework.http.codec;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URLEncoder;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.codec.Hints;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.log.LogFormatUtils;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.ReactiveHttpOutputMessage;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import reactor.core.publisher.Mono;
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
/*     */ public class FormHttpMessageWriter
/*     */   extends LoggingCodecSupport
/*     */   implements HttpMessageWriter<MultiValueMap<String, String>>
/*     */ {
/*  67 */   public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
/*     */   
/*  69 */   private static final MediaType DEFAULT_FORM_DATA_MEDIA_TYPE = new MediaType(MediaType.APPLICATION_FORM_URLENCODED, DEFAULT_CHARSET);
/*     */ 
/*     */ 
/*     */   
/*  73 */   private static final List<MediaType> MEDIA_TYPES = Collections.singletonList(MediaType.APPLICATION_FORM_URLENCODED);
/*     */ 
/*     */   
/*  76 */   private static final ResolvableType MULTIVALUE_TYPE = ResolvableType.forClassWithGenerics(MultiValueMap.class, new Class[] { String.class, String.class });
/*     */ 
/*     */   
/*  79 */   private Charset defaultCharset = DEFAULT_CHARSET;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultCharset(Charset charset) {
/*  88 */     Assert.notNull(charset, "Charset must not be null");
/*  89 */     this.defaultCharset = charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Charset getDefaultCharset() {
/*  96 */     return this.defaultCharset;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<MediaType> getWritableMediaTypes() {
/* 102 */     return MEDIA_TYPES;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canWrite(ResolvableType elementType, @Nullable MediaType mediaType) {
/* 108 */     if (!MultiValueMap.class.isAssignableFrom(elementType.toClass())) {
/* 109 */       return false;
/*     */     }
/* 111 */     if (MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType))
/*     */     {
/* 113 */       return true;
/*     */     }
/* 115 */     if (mediaType == null)
/*     */     {
/* 117 */       return MULTIVALUE_TYPE.isAssignableFrom(elementType);
/*     */     }
/* 119 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mono<Void> write(Publisher<? extends MultiValueMap<String, String>> inputStream, ResolvableType elementType, @Nullable MediaType mediaType, ReactiveHttpOutputMessage message, Map<String, Object> hints) {
/* 127 */     mediaType = getMediaType(mediaType);
/* 128 */     message.getHeaders().setContentType(mediaType);
/*     */     
/* 130 */     Charset charset = mediaType.getCharset();
/* 131 */     Assert.notNull(charset, "No charset");
/*     */     
/* 133 */     return Mono.from(inputStream).flatMap(form -> {
/*     */           logFormData(form, hints);
/*     */           String value = serializeForm(form, charset);
/*     */           ByteBuffer byteBuffer = charset.encode(value);
/*     */           DataBuffer buffer = message.bufferFactory().wrap(byteBuffer);
/*     */           message.getHeaders().setContentLength(byteBuffer.remaining());
/*     */           return message.writeWith((Publisher)Mono.just(buffer));
/*     */         });
/*     */   }
/*     */   
/*     */   private MediaType getMediaType(@Nullable MediaType mediaType) {
/* 144 */     if (mediaType == null) {
/* 145 */       return DEFAULT_FORM_DATA_MEDIA_TYPE;
/*     */     }
/* 147 */     if (mediaType.getCharset() == null) {
/* 148 */       return new MediaType(mediaType, getDefaultCharset());
/*     */     }
/*     */     
/* 151 */     return mediaType;
/*     */   }
/*     */ 
/*     */   
/*     */   private void logFormData(MultiValueMap<String, String> form, Map<String, Object> hints) {
/* 156 */     LogFormatUtils.traceDebug(this.logger, traceOn -> Hints.getLogPrefix(hints) + "Writing " + (isEnableLoggingRequestDetails() ? LogFormatUtils.formatValue(form, !traceOn.booleanValue()) : ("form fields " + form.keySet() + " (content masked)")));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String serializeForm(MultiValueMap<String, String> formData, Charset charset) {
/* 163 */     StringBuilder builder = new StringBuilder();
/* 164 */     formData.forEach((name, values) -> values.forEach(()));
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
/* 180 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/FormHttpMessageWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */