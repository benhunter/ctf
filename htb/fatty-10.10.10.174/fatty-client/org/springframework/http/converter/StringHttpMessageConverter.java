/*     */ package org.springframework.http.converter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StreamUtils;
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
/*     */ public class StringHttpMessageConverter
/*     */   extends AbstractHttpMessageConverter<String>
/*     */ {
/*  49 */   public static final Charset DEFAULT_CHARSET = StandardCharsets.ISO_8859_1;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private volatile List<Charset> availableCharsets;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean writeAcceptCharset = true;
/*     */ 
/*     */ 
/*     */   
/*     */   public StringHttpMessageConverter() {
/*  63 */     this(DEFAULT_CHARSET);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringHttpMessageConverter(Charset defaultCharset) {
/*  71 */     super(defaultCharset, new MediaType[] { MediaType.TEXT_PLAIN, MediaType.ALL });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWriteAcceptCharset(boolean writeAcceptCharset) {
/*  82 */     this.writeAcceptCharset = writeAcceptCharset;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supports(Class<?> clazz) {
/*  88 */     return (String.class == clazz);
/*     */   }
/*     */ 
/*     */   
/*     */   protected String readInternal(Class<? extends String> clazz, HttpInputMessage inputMessage) throws IOException {
/*  93 */     Charset charset = getContentTypeCharset(inputMessage.getHeaders().getContentType());
/*  94 */     return StreamUtils.copyToString(inputMessage.getBody(), charset);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Long getContentLength(String str, @Nullable MediaType contentType) {
/*  99 */     Charset charset = getContentTypeCharset(contentType);
/* 100 */     return Long.valueOf((str.getBytes(charset)).length);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeInternal(String str, HttpOutputMessage outputMessage) throws IOException {
/* 105 */     HttpHeaders headers = outputMessage.getHeaders();
/* 106 */     if (this.writeAcceptCharset && headers.get("Accept-Charset") == null) {
/* 107 */       headers.setAcceptCharset(getAcceptedCharsets());
/*     */     }
/* 109 */     Charset charset = getContentTypeCharset(headers.getContentType());
/* 110 */     StreamUtils.copy(str, charset, outputMessage.getBody());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<Charset> getAcceptedCharsets() {
/* 121 */     List<Charset> charsets = this.availableCharsets;
/* 122 */     if (charsets == null) {
/* 123 */       charsets = new ArrayList<>(Charset.availableCharsets().values());
/* 124 */       this.availableCharsets = charsets;
/*     */     } 
/* 126 */     return charsets;
/*     */   }
/*     */   
/*     */   private Charset getContentTypeCharset(@Nullable MediaType contentType) {
/* 130 */     if (contentType != null && contentType.getCharset() != null) {
/* 131 */       return contentType.getCharset();
/*     */     }
/* 133 */     if (contentType != null && contentType.isCompatibleWith(MediaType.APPLICATION_JSON))
/*     */     {
/* 135 */       return StandardCharsets.UTF_8;
/*     */     }
/*     */     
/* 138 */     Charset charset = getDefaultCharset();
/* 139 */     Assert.state((charset != null), "No default charset");
/* 140 */     return charset;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/converter/StringHttpMessageConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */