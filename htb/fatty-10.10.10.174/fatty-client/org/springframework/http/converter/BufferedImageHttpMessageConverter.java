/*     */ package org.springframework.http.converter;
/*     */ 
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.imageio.IIOImage;
/*     */ import javax.imageio.ImageIO;
/*     */ import javax.imageio.ImageReadParam;
/*     */ import javax.imageio.ImageReader;
/*     */ import javax.imageio.ImageWriteParam;
/*     */ import javax.imageio.ImageWriter;
/*     */ import javax.imageio.stream.FileCacheImageInputStream;
/*     */ import javax.imageio.stream.FileCacheImageOutputStream;
/*     */ import javax.imageio.stream.ImageInputStream;
/*     */ import javax.imageio.stream.ImageOutputStream;
/*     */ import javax.imageio.stream.MemoryCacheImageInputStream;
/*     */ import javax.imageio.stream.MemoryCacheImageOutputStream;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.StreamingHttpOutputMessage;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.MimeType;
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
/*     */ public class BufferedImageHttpMessageConverter
/*     */   implements HttpMessageConverter<BufferedImage>
/*     */ {
/*  71 */   private final List<MediaType> readableMediaTypes = new ArrayList<>();
/*     */   
/*     */   @Nullable
/*     */   private MediaType defaultContentType;
/*     */   
/*     */   @Nullable
/*     */   private File cacheDir;
/*     */ 
/*     */   
/*     */   public BufferedImageHttpMessageConverter() {
/*  81 */     String[] readerMediaTypes = ImageIO.getReaderMIMETypes();
/*  82 */     for (String mediaType : readerMediaTypes) {
/*  83 */       if (StringUtils.hasText(mediaType)) {
/*  84 */         this.readableMediaTypes.add(MediaType.parseMediaType(mediaType));
/*     */       }
/*     */     } 
/*     */     
/*  88 */     String[] writerMediaTypes = ImageIO.getWriterMIMETypes();
/*  89 */     for (String mediaType : writerMediaTypes) {
/*  90 */       if (StringUtils.hasText(mediaType)) {
/*  91 */         this.defaultContentType = MediaType.parseMediaType(mediaType);
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultContentType(@Nullable MediaType defaultContentType) {
/* 103 */     if (defaultContentType != null) {
/* 104 */       Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByMIMEType(defaultContentType.toString());
/* 105 */       if (!imageWriters.hasNext()) {
/* 106 */         throw new IllegalArgumentException("Content-Type [" + defaultContentType + "] is not supported by the Java Image I/O API");
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 111 */     this.defaultContentType = defaultContentType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public MediaType getDefaultContentType() {
/* 120 */     return this.defaultContentType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCacheDir(File cacheDir) {
/* 128 */     Assert.notNull(cacheDir, "'cacheDir' must not be null");
/* 129 */     Assert.isTrue(cacheDir.isDirectory(), "'cacheDir' is not a directory");
/* 130 */     this.cacheDir = cacheDir;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canRead(Class<?> clazz, @Nullable MediaType mediaType) {
/* 136 */     return (BufferedImage.class == clazz && isReadable(mediaType));
/*     */   }
/*     */   
/*     */   private boolean isReadable(@Nullable MediaType mediaType) {
/* 140 */     if (mediaType == null) {
/* 141 */       return true;
/*     */     }
/* 143 */     Iterator<ImageReader> imageReaders = ImageIO.getImageReadersByMIMEType(mediaType.toString());
/* 144 */     return imageReaders.hasNext();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canWrite(Class<?> clazz, @Nullable MediaType mediaType) {
/* 149 */     return (BufferedImage.class == clazz && isWritable(mediaType));
/*     */   }
/*     */   
/*     */   private boolean isWritable(@Nullable MediaType mediaType) {
/* 153 */     if (mediaType == null || MediaType.ALL.equalsTypeAndSubtype((MimeType)mediaType)) {
/* 154 */       return true;
/*     */     }
/* 156 */     Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByMIMEType(mediaType.toString());
/* 157 */     return imageWriters.hasNext();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<MediaType> getSupportedMediaTypes() {
/* 162 */     return Collections.unmodifiableList(this.readableMediaTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BufferedImage read(@Nullable Class<? extends BufferedImage> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
/* 169 */     ImageInputStream imageInputStream = null;
/* 170 */     ImageReader imageReader = null;
/*     */     try {
/* 172 */       imageInputStream = createImageInputStream(inputMessage.getBody());
/* 173 */       MediaType contentType = inputMessage.getHeaders().getContentType();
/* 174 */       if (contentType == null) {
/* 175 */         throw new HttpMessageNotReadableException("No Content-Type header", inputMessage);
/*     */       }
/* 177 */       Iterator<ImageReader> imageReaders = ImageIO.getImageReadersByMIMEType(contentType.toString());
/* 178 */       if (imageReaders.hasNext()) {
/* 179 */         imageReader = imageReaders.next();
/* 180 */         ImageReadParam irp = imageReader.getDefaultReadParam();
/* 181 */         process(irp);
/* 182 */         imageReader.setInput(imageInputStream, true);
/* 183 */         return imageReader.read(0, irp);
/*     */       } 
/*     */       
/* 186 */       throw new HttpMessageNotReadableException("Could not find javax.imageio.ImageReader for Content-Type [" + contentType + "]", inputMessage);
/*     */     
/*     */     }
/*     */     finally {
/*     */ 
/*     */       
/* 192 */       if (imageReader != null) {
/* 193 */         imageReader.dispose();
/*     */       }
/* 195 */       if (imageInputStream != null) {
/*     */         try {
/* 197 */           imageInputStream.close();
/*     */         }
/* 199 */         catch (IOException iOException) {}
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ImageInputStream createImageInputStream(InputStream is) throws IOException {
/* 207 */     if (this.cacheDir != null) {
/* 208 */       return new FileCacheImageInputStream(is, this.cacheDir);
/*     */     }
/*     */     
/* 211 */     return new MemoryCacheImageInputStream(is);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(BufferedImage image, @Nullable MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
/* 220 */     MediaType selectedContentType = getContentType(contentType);
/* 221 */     outputMessage.getHeaders().setContentType(selectedContentType);
/*     */     
/* 223 */     if (outputMessage instanceof StreamingHttpOutputMessage) {
/* 224 */       StreamingHttpOutputMessage streamingOutputMessage = (StreamingHttpOutputMessage)outputMessage;
/* 225 */       streamingOutputMessage.setBody(outputStream -> writeInternal(image, selectedContentType, outputStream));
/*     */     } else {
/*     */       
/* 228 */       writeInternal(image, selectedContentType, outputMessage.getBody());
/*     */     } 
/*     */   }
/*     */   
/*     */   private MediaType getContentType(@Nullable MediaType contentType) {
/* 233 */     if (contentType == null || contentType.isWildcardType() || contentType.isWildcardSubtype()) {
/* 234 */       contentType = getDefaultContentType();
/*     */     }
/* 236 */     Assert.notNull(contentType, "Could not select Content-Type. Please specify one through the 'defaultContentType' property.");
/*     */     
/* 238 */     return contentType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeInternal(BufferedImage image, MediaType contentType, OutputStream body) throws IOException, HttpMessageNotWritableException {
/* 244 */     ImageOutputStream imageOutputStream = null;
/* 245 */     ImageWriter imageWriter = null;
/*     */     try {
/* 247 */       Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByMIMEType(contentType.toString());
/* 248 */       if (imageWriters.hasNext()) {
/* 249 */         imageWriter = imageWriters.next();
/* 250 */         ImageWriteParam iwp = imageWriter.getDefaultWriteParam();
/* 251 */         process(iwp);
/* 252 */         imageOutputStream = createImageOutputStream(body);
/* 253 */         imageWriter.setOutput(imageOutputStream);
/* 254 */         imageWriter.write(null, new IIOImage(image, null, null), iwp);
/*     */       } else {
/*     */         
/* 257 */         throw new HttpMessageNotWritableException("Could not find javax.imageio.ImageWriter for Content-Type [" + contentType + "]");
/*     */       }
/*     */     
/*     */     } finally {
/*     */       
/* 262 */       if (imageWriter != null) {
/* 263 */         imageWriter.dispose();
/*     */       }
/* 265 */       if (imageOutputStream != null) {
/*     */         try {
/* 267 */           imageOutputStream.close();
/*     */         }
/* 269 */         catch (IOException iOException) {}
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ImageOutputStream createImageOutputStream(OutputStream os) throws IOException {
/* 277 */     if (this.cacheDir != null) {
/* 278 */       return new FileCacheImageOutputStream(os, this.cacheDir);
/*     */     }
/*     */     
/* 281 */     return new MemoryCacheImageOutputStream(os);
/*     */   }
/*     */   
/*     */   protected void process(ImageReadParam irp) {}
/*     */   
/*     */   protected void process(ImageWriteParam iwp) {}
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/converter/BufferedImageHttpMessageConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */