/*     */ package org.springframework.http.converter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.Charset;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class ObjectToStringHttpMessageConverter
/*     */   extends AbstractHttpMessageConverter<Object>
/*     */ {
/*     */   private final ConversionService conversionService;
/*     */   private final StringHttpMessageConverter stringHttpMessageConverter;
/*     */   
/*     */   public ObjectToStringHttpMessageConverter(ConversionService conversionService) {
/*  66 */     this(conversionService, StringHttpMessageConverter.DEFAULT_CHARSET);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectToStringHttpMessageConverter(ConversionService conversionService, Charset defaultCharset) {
/*  75 */     super(defaultCharset, new MediaType[] { MediaType.TEXT_PLAIN });
/*     */     
/*  77 */     Assert.notNull(conversionService, "ConversionService is required");
/*  78 */     this.conversionService = conversionService;
/*  79 */     this.stringHttpMessageConverter = new StringHttpMessageConverter(defaultCharset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWriteAcceptCharset(boolean writeAcceptCharset) {
/*  88 */     this.stringHttpMessageConverter.setWriteAcceptCharset(writeAcceptCharset);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canRead(Class<?> clazz, @Nullable MediaType mediaType) {
/*  94 */     return (canRead(mediaType) && this.conversionService.canConvert(String.class, clazz));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canWrite(Class<?> clazz, @Nullable MediaType mediaType) {
/*  99 */     return (canWrite(mediaType) && this.conversionService.canConvert(clazz, String.class));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean supports(Class<?> clazz) {
/* 105 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
/* 112 */     String value = this.stringHttpMessageConverter.readInternal(String.class, inputMessage);
/* 113 */     Object result = this.conversionService.convert(value, clazz);
/* 114 */     if (result == null) {
/* 115 */       throw new HttpMessageNotReadableException("Unexpected null conversion result for '" + value + "' to " + clazz, inputMessage);
/*     */     }
/*     */ 
/*     */     
/* 119 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException {
/* 124 */     String value = (String)this.conversionService.convert(obj, String.class);
/* 125 */     if (value != null) {
/* 126 */       this.stringHttpMessageConverter.writeInternal(value, outputMessage);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected Long getContentLength(Object obj, @Nullable MediaType contentType) {
/* 132 */     String value = (String)this.conversionService.convert(obj, String.class);
/* 133 */     if (value == null) {
/* 134 */       return Long.valueOf(0L);
/*     */     }
/* 136 */     return this.stringHttpMessageConverter.getContentLength(value, contentType);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/converter/ObjectToStringHttpMessageConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */