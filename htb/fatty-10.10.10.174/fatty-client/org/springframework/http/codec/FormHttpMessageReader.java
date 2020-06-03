/*     */ package org.springframework.http.codec;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URLDecoder;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.codec.Hints;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.DataBufferUtils;
/*     */ import org.springframework.core.log.LogFormatUtils;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.ReactiveHttpInputMessage;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.StringUtils;
/*     */ import reactor.core.publisher.Flux;
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
/*     */ public class FormHttpMessageReader
/*     */   extends LoggingCodecSupport
/*     */   implements HttpMessageReader<MultiValueMap<String, String>>
/*     */ {
/*  57 */   public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
/*     */ 
/*     */   
/*  60 */   private static final ResolvableType MULTIVALUE_STRINGS_TYPE = ResolvableType.forClassWithGenerics(MultiValueMap.class, new Class[] { String.class, String.class });
/*     */ 
/*     */   
/*  63 */   private Charset defaultCharset = DEFAULT_CHARSET;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultCharset(Charset charset) {
/*  72 */     Assert.notNull(charset, "Charset must not be null");
/*  73 */     this.defaultCharset = charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Charset getDefaultCharset() {
/*  80 */     return this.defaultCharset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canRead(ResolvableType elementType, @Nullable MediaType mediaType) {
/*  88 */     boolean multiValueUnresolved = (elementType.hasUnresolvableGenerics() && MultiValueMap.class.isAssignableFrom(elementType.toClass()));
/*     */     
/*  90 */     return ((MULTIVALUE_STRINGS_TYPE.isAssignableFrom(elementType) || multiValueUnresolved) && (mediaType == null || MediaType.APPLICATION_FORM_URLENCODED
/*  91 */       .isCompatibleWith(mediaType)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Flux<MultiValueMap<String, String>> read(ResolvableType elementType, ReactiveHttpInputMessage message, Map<String, Object> hints) {
/*  98 */     return Flux.from((Publisher)readMono(elementType, message, hints));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mono<MultiValueMap<String, String>> readMono(ResolvableType elementType, ReactiveHttpInputMessage message, Map<String, Object> hints) {
/* 105 */     MediaType contentType = message.getHeaders().getContentType();
/* 106 */     Charset charset = getMediaTypeCharset(contentType);
/*     */     
/* 108 */     return DataBufferUtils.join((Publisher)message.getBody())
/* 109 */       .map(buffer -> {
/*     */           CharBuffer charBuffer = charset.decode(buffer.asByteBuffer());
/*     */           String body = charBuffer.toString();
/*     */           DataBufferUtils.release(buffer);
/*     */           MultiValueMap<String, String> formData = parseFormData(charset, body);
/*     */           logFormData(formData, hints);
/*     */           return formData;
/*     */         });
/*     */   }
/*     */   
/*     */   private void logFormData(MultiValueMap<String, String> formData, Map<String, Object> hints) {
/* 120 */     LogFormatUtils.traceDebug(this.logger, traceOn -> Hints.getLogPrefix(hints) + "Read " + (isEnableLoggingRequestDetails() ? LogFormatUtils.formatValue(formData, !traceOn.booleanValue()) : ("form fields " + formData.keySet() + " (content masked)")));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Charset getMediaTypeCharset(@Nullable MediaType mediaType) {
/* 127 */     if (mediaType != null && mediaType.getCharset() != null) {
/* 128 */       return mediaType.getCharset();
/*     */     }
/*     */     
/* 131 */     return getDefaultCharset();
/*     */   }
/*     */ 
/*     */   
/*     */   private MultiValueMap<String, String> parseFormData(Charset charset, String body) {
/* 136 */     String[] pairs = StringUtils.tokenizeToStringArray(body, "&");
/* 137 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap(pairs.length);
/*     */     try {
/* 139 */       for (String pair : pairs) {
/* 140 */         int idx = pair.indexOf('=');
/* 141 */         if (idx == -1) {
/* 142 */           linkedMultiValueMap.add(URLDecoder.decode(pair, charset.name()), null);
/*     */         } else {
/*     */           
/* 145 */           String name = URLDecoder.decode(pair.substring(0, idx), charset.name());
/* 146 */           String value = URLDecoder.decode(pair.substring(idx + 1), charset.name());
/* 147 */           linkedMultiValueMap.add(name, value);
/*     */         }
/*     */       
/*     */       } 
/* 151 */     } catch (UnsupportedEncodingException ex) {
/* 152 */       throw new IllegalStateException(ex);
/*     */     } 
/* 154 */     return (MultiValueMap<String, String>)linkedMultiValueMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<MediaType> getReadableMediaTypes() {
/* 159 */     return Collections.singletonList(MediaType.APPLICATION_FORM_URLENCODED);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/FormHttpMessageReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */