/*     */ package org.springframework.http.converter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URLDecoder;
/*     */ import java.net.URLEncoder;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.mail.internet.MimeUtility;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.http.HttpEntity;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.StreamingHttpOutputMessage;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MimeTypeUtils;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.StreamUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class FormHttpMessageConverter
/*     */   implements HttpMessageConverter<MultiValueMap<String, ?>>
/*     */ {
/* 100 */   public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
/*     */   
/* 102 */   private static final MediaType DEFAULT_FORM_DATA_MEDIA_TYPE = new MediaType(MediaType.APPLICATION_FORM_URLENCODED, DEFAULT_CHARSET);
/*     */ 
/*     */ 
/*     */   
/* 106 */   private List<MediaType> supportedMediaTypes = new ArrayList<>();
/*     */   
/* 108 */   private List<HttpMessageConverter<?>> partConverters = new ArrayList<>();
/*     */   
/* 110 */   private Charset charset = DEFAULT_CHARSET;
/*     */   
/*     */   @Nullable
/*     */   private Charset multipartCharset;
/*     */ 
/*     */   
/*     */   public FormHttpMessageConverter() {
/* 117 */     this.supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
/* 118 */     this.supportedMediaTypes.add(MediaType.MULTIPART_FORM_DATA);
/*     */     
/* 120 */     StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
/* 121 */     stringHttpMessageConverter.setWriteAcceptCharset(false);
/*     */     
/* 123 */     this.partConverters.add(new ByteArrayHttpMessageConverter());
/* 124 */     this.partConverters.add(stringHttpMessageConverter);
/* 125 */     this.partConverters.add(new ResourceHttpMessageConverter());
/*     */     
/* 127 */     applyDefaultCharset();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSupportedMediaTypes(List<MediaType> supportedMediaTypes) {
/* 135 */     this.supportedMediaTypes = supportedMediaTypes;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<MediaType> getSupportedMediaTypes() {
/* 140 */     return Collections.unmodifiableList(this.supportedMediaTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPartConverters(List<HttpMessageConverter<?>> partConverters) {
/* 148 */     Assert.notEmpty(partConverters, "'partConverters' must not be empty");
/* 149 */     this.partConverters = partConverters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPartConverter(HttpMessageConverter<?> partConverter) {
/* 157 */     Assert.notNull(partConverter, "'partConverter' must not be null");
/* 158 */     this.partConverters.add(partConverter);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCharset(@Nullable Charset charset) {
/* 174 */     if (charset != this.charset) {
/* 175 */       this.charset = (charset != null) ? charset : DEFAULT_CHARSET;
/* 176 */       applyDefaultCharset();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void applyDefaultCharset() {
/* 184 */     for (HttpMessageConverter<?> candidate : this.partConverters) {
/* 185 */       if (candidate instanceof AbstractHttpMessageConverter) {
/* 186 */         AbstractHttpMessageConverter<?> converter = (AbstractHttpMessageConverter)candidate;
/*     */         
/* 188 */         if (converter.getDefaultCharset() != null) {
/* 189 */           converter.setDefaultCharset(this.charset);
/*     */         }
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
/*     */   
/*     */   public void setMultipartCharset(Charset charset) {
/* 206 */     this.multipartCharset = charset;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canRead(Class<?> clazz, @Nullable MediaType mediaType) {
/* 212 */     if (!MultiValueMap.class.isAssignableFrom(clazz)) {
/* 213 */       return false;
/*     */     }
/* 215 */     if (mediaType == null) {
/* 216 */       return true;
/*     */     }
/* 218 */     for (MediaType supportedMediaType : getSupportedMediaTypes()) {
/*     */       
/* 220 */       if (!supportedMediaType.equals(MediaType.MULTIPART_FORM_DATA) && supportedMediaType.includes(mediaType)) {
/* 221 */         return true;
/*     */       }
/*     */     } 
/* 224 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canWrite(Class<?> clazz, @Nullable MediaType mediaType) {
/* 229 */     if (!MultiValueMap.class.isAssignableFrom(clazz)) {
/* 230 */       return false;
/*     */     }
/* 232 */     if (mediaType == null || MediaType.ALL.equals(mediaType)) {
/* 233 */       return true;
/*     */     }
/* 235 */     for (MediaType supportedMediaType : getSupportedMediaTypes()) {
/* 236 */       if (supportedMediaType.isCompatibleWith(mediaType)) {
/* 237 */         return true;
/*     */       }
/*     */     } 
/* 240 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MultiValueMap<String, String> read(@Nullable Class<? extends MultiValueMap<String, ?>> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
/* 247 */     MediaType contentType = inputMessage.getHeaders().getContentType();
/*     */     
/* 249 */     Charset charset = (contentType != null && contentType.getCharset() != null) ? contentType.getCharset() : this.charset;
/* 250 */     String body = StreamUtils.copyToString(inputMessage.getBody(), charset);
/*     */     
/* 252 */     String[] pairs = StringUtils.tokenizeToStringArray(body, "&");
/* 253 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap(pairs.length);
/* 254 */     for (String pair : pairs) {
/* 255 */       int idx = pair.indexOf('=');
/* 256 */       if (idx == -1) {
/* 257 */         linkedMultiValueMap.add(URLDecoder.decode(pair, charset.name()), null);
/*     */       } else {
/*     */         
/* 260 */         String name = URLDecoder.decode(pair.substring(0, idx), charset.name());
/* 261 */         String value = URLDecoder.decode(pair.substring(idx + 1), charset.name());
/* 262 */         linkedMultiValueMap.add(name, value);
/*     */       } 
/*     */     } 
/* 265 */     return (MultiValueMap<String, String>)linkedMultiValueMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(MultiValueMap<String, ?> map, @Nullable MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
/* 273 */     if (!isMultipart(map, contentType)) {
/* 274 */       writeForm((MultiValueMap)map, contentType, outputMessage);
/*     */     } else {
/*     */       
/* 277 */       writeMultipart((MultiValueMap)map, outputMessage);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isMultipart(MultiValueMap<String, ?> map, @Nullable MediaType contentType) {
/* 283 */     if (contentType != null) {
/* 284 */       return MediaType.MULTIPART_FORM_DATA.includes(contentType);
/*     */     }
/* 286 */     for (List<?> values : (Iterable<List<?>>)map.values()) {
/* 287 */       for (Object value : values) {
/* 288 */         if (value != null && !(value instanceof String)) {
/* 289 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 293 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeForm(MultiValueMap<String, Object> formData, @Nullable MediaType contentType, HttpOutputMessage outputMessage) throws IOException {
/* 299 */     contentType = getMediaType(contentType);
/* 300 */     outputMessage.getHeaders().setContentType(contentType);
/*     */     
/* 302 */     Charset charset = contentType.getCharset();
/* 303 */     Assert.notNull(charset, "No charset");
/*     */     
/* 305 */     byte[] bytes = serializeForm(formData, charset).getBytes(charset);
/* 306 */     outputMessage.getHeaders().setContentLength(bytes.length);
/*     */     
/* 308 */     if (outputMessage instanceof StreamingHttpOutputMessage) {
/* 309 */       StreamingHttpOutputMessage streamingOutputMessage = (StreamingHttpOutputMessage)outputMessage;
/* 310 */       streamingOutputMessage.setBody(outputStream -> StreamUtils.copy(bytes, outputStream));
/*     */     } else {
/*     */       
/* 313 */       StreamUtils.copy(bytes, outputMessage.getBody());
/*     */     } 
/*     */   }
/*     */   
/*     */   private MediaType getMediaType(@Nullable MediaType mediaType) {
/* 318 */     if (mediaType == null) {
/* 319 */       return DEFAULT_FORM_DATA_MEDIA_TYPE;
/*     */     }
/* 321 */     if (mediaType.getCharset() == null) {
/* 322 */       return new MediaType(mediaType, this.charset);
/*     */     }
/*     */     
/* 325 */     return mediaType;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String serializeForm(MultiValueMap<String, Object> formData, Charset charset) {
/* 330 */     StringBuilder builder = new StringBuilder();
/* 331 */     formData.forEach((name, values) -> values.forEach(()));
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
/* 348 */     return builder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeMultipart(MultiValueMap<String, Object> parts, HttpOutputMessage outputMessage) throws IOException {
/* 354 */     byte[] boundary = generateMultipartBoundary();
/* 355 */     Map<String, String> parameters = new LinkedHashMap<>(2);
/* 356 */     if (!isFilenameCharsetSet()) {
/* 357 */       parameters.put("charset", this.charset.name());
/*     */     }
/* 359 */     parameters.put("boundary", new String(boundary, StandardCharsets.US_ASCII));
/*     */     
/* 361 */     MediaType contentType = new MediaType(MediaType.MULTIPART_FORM_DATA, parameters);
/* 362 */     HttpHeaders headers = outputMessage.getHeaders();
/* 363 */     headers.setContentType(contentType);
/*     */     
/* 365 */     if (outputMessage instanceof StreamingHttpOutputMessage) {
/* 366 */       StreamingHttpOutputMessage streamingOutputMessage = (StreamingHttpOutputMessage)outputMessage;
/* 367 */       streamingOutputMessage.setBody(outputStream -> {
/*     */             writeParts(outputStream, parts, boundary);
/*     */             
/*     */             writeEnd(outputStream, boundary);
/*     */           });
/*     */     } else {
/* 373 */       writeParts(outputMessage.getBody(), parts, boundary);
/* 374 */       writeEnd(outputMessage.getBody(), boundary);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isFilenameCharsetSet() {
/* 384 */     return (this.multipartCharset != null);
/*     */   }
/*     */   
/*     */   private void writeParts(OutputStream os, MultiValueMap<String, Object> parts, byte[] boundary) throws IOException {
/* 388 */     for (Map.Entry<String, List<Object>> entry : (Iterable<Map.Entry<String, List<Object>>>)parts.entrySet()) {
/* 389 */       String name = entry.getKey();
/* 390 */       for (Object part : entry.getValue()) {
/* 391 */         if (part != null) {
/* 392 */           writeBoundary(os, boundary);
/* 393 */           writePart(name, getHttpEntity(part), os);
/* 394 */           writeNewLine(os);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void writePart(String name, HttpEntity<?> partEntity, OutputStream os) throws IOException {
/* 402 */     Object partBody = partEntity.getBody();
/* 403 */     if (partBody == null) {
/* 404 */       throw new IllegalStateException("Empty body for part '" + name + "': " + partEntity);
/*     */     }
/* 406 */     Class<?> partType = partBody.getClass();
/* 407 */     HttpHeaders partHeaders = partEntity.getHeaders();
/* 408 */     MediaType partContentType = partHeaders.getContentType();
/* 409 */     for (HttpMessageConverter<?> messageConverter : this.partConverters) {
/* 410 */       if (messageConverter.canWrite(partType, partContentType)) {
/* 411 */         Charset charset = isFilenameCharsetSet() ? StandardCharsets.US_ASCII : this.charset;
/* 412 */         HttpOutputMessage multipartMessage = new MultipartHttpOutputMessage(os, charset);
/* 413 */         multipartMessage.getHeaders().setContentDispositionFormData(name, getFilename(partBody));
/* 414 */         if (!partHeaders.isEmpty()) {
/* 415 */           multipartMessage.getHeaders().putAll((Map)partHeaders);
/*     */         }
/* 417 */         messageConverter.write(partBody, partContentType, multipartMessage);
/*     */         return;
/*     */       } 
/*     */     } 
/* 421 */     throw new HttpMessageNotWritableException("Could not write request: no suitable HttpMessageConverter found for request type [" + partType
/* 422 */         .getName() + "]");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected byte[] generateMultipartBoundary() {
/* 431 */     return MimeTypeUtils.generateMultipartBoundary();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpEntity<?> getHttpEntity(Object part) {
/* 441 */     return (part instanceof HttpEntity) ? (HttpEntity)part : new HttpEntity(part);
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
/*     */   @Nullable
/*     */   protected String getFilename(Object part) {
/* 454 */     if (part instanceof Resource) {
/* 455 */       Resource resource = (Resource)part;
/* 456 */       String filename = resource.getFilename();
/* 457 */       if (filename != null && this.multipartCharset != null) {
/* 458 */         filename = MimeDelegate.encode(filename, this.multipartCharset.name());
/*     */       }
/* 460 */       return filename;
/*     */     } 
/*     */     
/* 463 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeBoundary(OutputStream os, byte[] boundary) throws IOException {
/* 469 */     os.write(45);
/* 470 */     os.write(45);
/* 471 */     os.write(boundary);
/* 472 */     writeNewLine(os);
/*     */   }
/*     */   
/*     */   private static void writeEnd(OutputStream os, byte[] boundary) throws IOException {
/* 476 */     os.write(45);
/* 477 */     os.write(45);
/* 478 */     os.write(boundary);
/* 479 */     os.write(45);
/* 480 */     os.write(45);
/* 481 */     writeNewLine(os);
/*     */   }
/*     */   
/*     */   private static void writeNewLine(OutputStream os) throws IOException {
/* 485 */     os.write(13);
/* 486 */     os.write(10);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class MultipartHttpOutputMessage
/*     */     implements HttpOutputMessage
/*     */   {
/*     */     private final OutputStream outputStream;
/*     */ 
/*     */     
/*     */     private final Charset charset;
/*     */ 
/*     */     
/* 500 */     private final HttpHeaders headers = new HttpHeaders();
/*     */     
/*     */     private boolean headersWritten = false;
/*     */     
/*     */     public MultipartHttpOutputMessage(OutputStream outputStream, Charset charset) {
/* 505 */       this.outputStream = outputStream;
/* 506 */       this.charset = charset;
/*     */     }
/*     */ 
/*     */     
/*     */     public HttpHeaders getHeaders() {
/* 511 */       return this.headersWritten ? HttpHeaders.readOnlyHttpHeaders(this.headers) : this.headers;
/*     */     }
/*     */ 
/*     */     
/*     */     public OutputStream getBody() throws IOException {
/* 516 */       writeHeaders();
/* 517 */       return this.outputStream;
/*     */     }
/*     */     
/*     */     private void writeHeaders() throws IOException {
/* 521 */       if (!this.headersWritten) {
/* 522 */         for (Map.Entry<String, List<String>> entry : (Iterable<Map.Entry<String, List<String>>>)this.headers.entrySet()) {
/* 523 */           byte[] headerName = getBytes(entry.getKey());
/* 524 */           for (String headerValueString : entry.getValue()) {
/* 525 */             byte[] headerValue = getBytes(headerValueString);
/* 526 */             this.outputStream.write(headerName);
/* 527 */             this.outputStream.write(58);
/* 528 */             this.outputStream.write(32);
/* 529 */             this.outputStream.write(headerValue);
/* 530 */             FormHttpMessageConverter.writeNewLine(this.outputStream);
/*     */           } 
/*     */         } 
/* 533 */         FormHttpMessageConverter.writeNewLine(this.outputStream);
/* 534 */         this.headersWritten = true;
/*     */       } 
/*     */     }
/*     */     
/*     */     private byte[] getBytes(String name) {
/* 539 */       return name.getBytes(this.charset);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class MimeDelegate
/*     */   {
/*     */     public static String encode(String value, String charset) {
/*     */       try {
/* 551 */         return MimeUtility.encodeText(value, charset, null);
/*     */       }
/* 553 */       catch (UnsupportedEncodingException ex) {
/* 554 */         throw new IllegalStateException(ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/converter/FormHttpMessageConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */