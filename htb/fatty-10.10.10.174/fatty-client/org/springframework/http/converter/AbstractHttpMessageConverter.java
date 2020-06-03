/*     */ package org.springframework.http.converter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpLogging;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.StreamingHttpOutputMessage;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.MimeType;
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
/*     */ public abstract class AbstractHttpMessageConverter<T>
/*     */   implements HttpMessageConverter<T>
/*     */ {
/*  54 */   protected final Log logger = HttpLogging.forLogName(getClass());
/*     */   
/*  56 */   private List<MediaType> supportedMediaTypes = Collections.emptyList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Charset defaultCharset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractHttpMessageConverter(MediaType supportedMediaType) {
/*  74 */     setSupportedMediaTypes(Collections.singletonList(supportedMediaType));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractHttpMessageConverter(MediaType... supportedMediaTypes) {
/*  82 */     setSupportedMediaTypes(Arrays.asList(supportedMediaTypes));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractHttpMessageConverter(Charset defaultCharset, MediaType... supportedMediaTypes) {
/*  93 */     this.defaultCharset = defaultCharset;
/*  94 */     setSupportedMediaTypes(Arrays.asList(supportedMediaTypes));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSupportedMediaTypes(List<MediaType> supportedMediaTypes) {
/* 102 */     Assert.notEmpty(supportedMediaTypes, "MediaType List must not be empty");
/* 103 */     this.supportedMediaTypes = new ArrayList<>(supportedMediaTypes);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<MediaType> getSupportedMediaTypes() {
/* 108 */     return Collections.unmodifiableList(this.supportedMediaTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultCharset(@Nullable Charset defaultCharset) {
/* 116 */     this.defaultCharset = defaultCharset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Charset getDefaultCharset() {
/* 125 */     return this.defaultCharset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canRead(Class<?> clazz, @Nullable MediaType mediaType) {
/* 136 */     return (supports(clazz) && canRead(mediaType));
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
/*     */   
/*     */   protected boolean canRead(@Nullable MediaType mediaType) {
/* 149 */     if (mediaType == null) {
/* 150 */       return true;
/*     */     }
/* 152 */     for (MediaType supportedMediaType : getSupportedMediaTypes()) {
/* 153 */       if (supportedMediaType.includes(mediaType)) {
/* 154 */         return true;
/*     */       }
/*     */     } 
/* 157 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canWrite(Class<?> clazz, @Nullable MediaType mediaType) {
/* 168 */     return (supports(clazz) && canWrite(mediaType));
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
/*     */   protected boolean canWrite(@Nullable MediaType mediaType) {
/* 180 */     if (mediaType == null || MediaType.ALL.equalsTypeAndSubtype((MimeType)mediaType)) {
/* 181 */       return true;
/*     */     }
/* 183 */     for (MediaType supportedMediaType : getSupportedMediaTypes()) {
/* 184 */       if (supportedMediaType.isCompatibleWith(mediaType)) {
/* 185 */         return true;
/*     */       }
/*     */     } 
/* 188 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final T read(Class<? extends T> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
/* 199 */     return readInternal(clazz, inputMessage);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void write(T t, @Nullable MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
/* 210 */     final HttpHeaders headers = outputMessage.getHeaders();
/* 211 */     addDefaultHeaders(headers, t, contentType);
/*     */     
/* 213 */     if (outputMessage instanceof StreamingHttpOutputMessage) {
/* 214 */       StreamingHttpOutputMessage streamingOutputMessage = (StreamingHttpOutputMessage)outputMessage;
/* 215 */       streamingOutputMessage.setBody(outputStream -> writeInternal((T)t, new HttpOutputMessage()
/*     */             {
/*     */               public OutputStream getBody() {
/* 218 */                 return outputStream;
/*     */               }
/*     */               
/*     */               public HttpHeaders getHeaders() {
/* 222 */                 return headers;
/*     */               }
/*     */             }));
/*     */     } else {
/*     */       
/* 227 */       writeInternal(t, outputMessage);
/* 228 */       outputMessage.getBody().flush();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addDefaultHeaders(HttpHeaders headers, T t, @Nullable MediaType contentType) throws IOException {
/* 240 */     if (headers.getContentType() == null) {
/* 241 */       MediaType contentTypeToUse = contentType;
/* 242 */       if (contentType == null || contentType.isWildcardType() || contentType.isWildcardSubtype()) {
/* 243 */         contentTypeToUse = getDefaultContentType(t);
/*     */       }
/* 245 */       else if (MediaType.APPLICATION_OCTET_STREAM.equals(contentType)) {
/* 246 */         MediaType mediaType = getDefaultContentType(t);
/* 247 */         contentTypeToUse = (mediaType != null) ? mediaType : contentTypeToUse;
/*     */       } 
/* 249 */       if (contentTypeToUse != null) {
/* 250 */         if (contentTypeToUse.getCharset() == null) {
/* 251 */           Charset defaultCharset = getDefaultCharset();
/* 252 */           if (defaultCharset != null) {
/* 253 */             contentTypeToUse = new MediaType(contentTypeToUse, defaultCharset);
/*     */           }
/*     */         } 
/* 256 */         headers.setContentType(contentTypeToUse);
/*     */       } 
/*     */     } 
/* 259 */     if (headers.getContentLength() < 0L && !headers.containsKey("Transfer-Encoding")) {
/* 260 */       Long contentLength = getContentLength(t, headers.getContentType());
/* 261 */       if (contentLength != null) {
/* 262 */         headers.setContentLength(contentLength.longValue());
/*     */       }
/*     */     } 
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
/*     */   
/*     */   @Nullable
/*     */   protected MediaType getDefaultContentType(T t) throws IOException {
/* 278 */     List<MediaType> mediaTypes = getSupportedMediaTypes();
/* 279 */     return !mediaTypes.isEmpty() ? mediaTypes.get(0) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Long getContentLength(T t, @Nullable MediaType contentType) throws IOException {
/* 291 */     return null;
/*     */   }
/*     */   
/*     */   protected AbstractHttpMessageConverter() {}
/*     */   
/*     */   protected abstract boolean supports(Class<?> paramClass);
/*     */   
/*     */   protected abstract T readInternal(Class<? extends T> paramClass, HttpInputMessage paramHttpInputMessage) throws IOException, HttpMessageNotReadableException;
/*     */   
/*     */   protected abstract void writeInternal(T paramT, HttpOutputMessage paramHttpOutputMessage) throws IOException, HttpMessageNotWritableException;
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/converter/AbstractHttpMessageConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */