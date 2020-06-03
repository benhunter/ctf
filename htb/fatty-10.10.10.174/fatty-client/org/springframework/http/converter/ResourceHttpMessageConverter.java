/*     */ package org.springframework.http.converter;
/*     */ 
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.springframework.core.io.ByteArrayResource;
/*     */ import org.springframework.core.io.InputStreamResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.MediaTypeFactory;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourceHttpMessageConverter
/*     */   extends AbstractHttpMessageConverter<Resource>
/*     */ {
/*     */   private final boolean supportsReadStreaming;
/*     */   
/*     */   public ResourceHttpMessageConverter() {
/*  56 */     super(MediaType.ALL);
/*  57 */     this.supportsReadStreaming = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceHttpMessageConverter(boolean supportsReadStreaming) {
/*  67 */     super(MediaType.ALL);
/*  68 */     this.supportsReadStreaming = supportsReadStreaming;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean supports(Class<?> clazz) {
/*  74 */     return Resource.class.isAssignableFrom(clazz);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Resource readInternal(Class<? extends Resource> clazz, final HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
/*  81 */     if (this.supportsReadStreaming && InputStreamResource.class == clazz) {
/*  82 */       return (Resource)new InputStreamResource(inputMessage.getBody())
/*     */         {
/*     */           public String getFilename() {
/*  85 */             return inputMessage.getHeaders().getContentDisposition().getFilename();
/*     */           }
/*     */         };
/*     */     }
/*  89 */     if (Resource.class == clazz || ByteArrayResource.class.isAssignableFrom(clazz)) {
/*  90 */       byte[] body = StreamUtils.copyToByteArray(inputMessage.getBody());
/*  91 */       return (Resource)new ByteArrayResource(body)
/*     */         {
/*     */           @Nullable
/*     */           public String getFilename() {
/*  95 */             return inputMessage.getHeaders().getContentDisposition().getFilename();
/*     */           }
/*     */         };
/*     */     } 
/*     */     
/* 100 */     throw new HttpMessageNotReadableException("Unsupported resource class: " + clazz, inputMessage);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected MediaType getDefaultContentType(Resource resource) {
/* 106 */     return MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Long getContentLength(Resource resource, @Nullable MediaType contentType) throws IOException {
/* 113 */     if (InputStreamResource.class == resource.getClass()) {
/* 114 */       return null;
/*     */     }
/* 116 */     long contentLength = resource.contentLength();
/* 117 */     return (contentLength < 0L) ? null : Long.valueOf(contentLength);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeInternal(Resource resource, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
/* 124 */     writeContent(resource, outputMessage);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeContent(Resource resource, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
/*     */     try {
/* 130 */       InputStream in = resource.getInputStream();
/*     */       
/* 132 */       try { StreamUtils.copy(in, outputMessage.getBody()); }
/*     */       
/* 134 */       catch (NullPointerException nullPointerException)
/*     */       
/*     */       { 
/*     */         try {
/*     */           
/* 139 */           in.close();
/*     */         }
/* 141 */         catch (Throwable throwable) {} } finally { try { in.close(); } catch (Throwable throwable) {}
/*     */          }
/*     */ 
/*     */     
/*     */     }
/* 146 */     catch (FileNotFoundException fileNotFoundException) {}
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/converter/ResourceHttpMessageConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */