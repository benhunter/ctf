/*     */ package org.springframework.http.converter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Collection;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.support.ResourceRegion;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.MediaTypeFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.MimeTypeUtils;
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
/*     */ public class ResourceRegionHttpMessageConverter
/*     */   extends AbstractGenericHttpMessageConverter<Object>
/*     */ {
/*     */   public ResourceRegionHttpMessageConverter() {
/*  50 */     super(MediaType.ALL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MediaType getDefaultContentType(Object object) {
/*  57 */     Resource resource = null;
/*  58 */     if (object instanceof ResourceRegion) {
/*  59 */       resource = ((ResourceRegion)object).getResource();
/*     */     } else {
/*     */       
/*  62 */       Collection<ResourceRegion> regions = (Collection<ResourceRegion>)object;
/*  63 */       if (!regions.isEmpty()) {
/*  64 */         resource = ((ResourceRegion)regions.iterator().next()).getResource();
/*     */       }
/*     */     } 
/*  67 */     return MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canRead(Class<?> clazz, @Nullable MediaType mediaType) {
/*  72 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canRead(Type type, @Nullable Class<?> contextClass, @Nullable MediaType mediaType) {
/*  77 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object read(Type type, @Nullable Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
/*  84 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ResourceRegion readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
/*  91 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canWrite(Class<?> clazz, @Nullable MediaType mediaType) {
/*  96 */     return canWrite(clazz, null, mediaType);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canWrite(@Nullable Type type, @Nullable Class<?> clazz, @Nullable MediaType mediaType) {
/* 101 */     if (!(type instanceof ParameterizedType)) {
/* 102 */       return (type instanceof Class && ResourceRegion.class.isAssignableFrom((Class)type));
/*     */     }
/*     */     
/* 105 */     ParameterizedType parameterizedType = (ParameterizedType)type;
/* 106 */     if (!(parameterizedType.getRawType() instanceof Class)) {
/* 107 */       return false;
/*     */     }
/* 109 */     Class<?> rawType = (Class)parameterizedType.getRawType();
/* 110 */     if (!Collection.class.isAssignableFrom(rawType)) {
/* 111 */       return false;
/*     */     }
/* 113 */     if ((parameterizedType.getActualTypeArguments()).length != 1) {
/* 114 */       return false;
/*     */     }
/* 116 */     Type typeArgument = parameterizedType.getActualTypeArguments()[0];
/* 117 */     if (!(typeArgument instanceof Class)) {
/* 118 */       return false;
/*     */     }
/*     */     
/* 121 */     Class<?> typeArgumentClass = (Class)typeArgument;
/* 122 */     return ResourceRegion.class.isAssignableFrom(typeArgumentClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeInternal(Object object, @Nullable Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
/* 130 */     if (object instanceof ResourceRegion) {
/* 131 */       writeResourceRegion((ResourceRegion)object, outputMessage);
/*     */     } else {
/*     */       
/* 134 */       Collection<ResourceRegion> regions = (Collection<ResourceRegion>)object;
/* 135 */       if (regions.size() == 1) {
/* 136 */         writeResourceRegion(regions.iterator().next(), outputMessage);
/*     */       } else {
/*     */         
/* 139 */         writeResourceRegionCollection((Collection<ResourceRegion>)object, outputMessage);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeResourceRegion(ResourceRegion region, HttpOutputMessage outputMessage) throws IOException {
/* 146 */     Assert.notNull(region, "ResourceRegion must not be null");
/* 147 */     HttpHeaders responseHeaders = outputMessage.getHeaders();
/*     */     
/* 149 */     long start = region.getPosition();
/* 150 */     long end = start + region.getCount() - 1L;
/* 151 */     Long resourceLength = Long.valueOf(region.getResource().contentLength());
/* 152 */     end = Math.min(end, resourceLength.longValue() - 1L);
/* 153 */     long rangeLength = end - start + 1L;
/* 154 */     responseHeaders.add("Content-Range", "bytes " + start + '-' + end + '/' + resourceLength);
/* 155 */     responseHeaders.setContentLength(rangeLength);
/*     */     
/* 157 */     InputStream in = region.getResource().getInputStream();
/*     */     try {
/* 159 */       StreamUtils.copyRange(in, outputMessage.getBody(), start, end);
/*     */     } finally {
/*     */       
/*     */       try {
/* 163 */         in.close();
/*     */       }
/* 165 */       catch (IOException iOException) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeResourceRegionCollection(Collection<ResourceRegion> resourceRegions, HttpOutputMessage outputMessage) throws IOException {
/* 174 */     Assert.notNull(resourceRegions, "Collection of ResourceRegion should not be null");
/* 175 */     HttpHeaders responseHeaders = outputMessage.getHeaders();
/*     */     
/* 177 */     MediaType contentType = responseHeaders.getContentType();
/* 178 */     String boundaryString = MimeTypeUtils.generateMultipartBoundaryString();
/* 179 */     responseHeaders.set("Content-Type", "multipart/byteranges; boundary=" + boundaryString);
/* 180 */     OutputStream out = outputMessage.getBody();
/*     */     
/* 182 */     for (ResourceRegion region : resourceRegions) {
/* 183 */       long start = region.getPosition();
/* 184 */       long end = start + region.getCount() - 1L;
/* 185 */       InputStream in = region.getResource().getInputStream();
/*     */       
/*     */       try {
/* 188 */         println(out);
/* 189 */         print(out, "--" + boundaryString);
/* 190 */         println(out);
/* 191 */         if (contentType != null) {
/* 192 */           print(out, "Content-Type: " + contentType.toString());
/* 193 */           println(out);
/*     */         } 
/* 195 */         Long resourceLength = Long.valueOf(region.getResource().contentLength());
/* 196 */         end = Math.min(end, resourceLength.longValue() - 1L);
/* 197 */         print(out, "Content-Range: bytes " + start + '-' + end + '/' + resourceLength);
/* 198 */         println(out);
/* 199 */         println(out);
/*     */         
/* 201 */         StreamUtils.copyRange(in, out, start, end);
/*     */       } finally {
/*     */         
/*     */         try {
/* 205 */           in.close();
/*     */         }
/* 207 */         catch (IOException iOException) {}
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 213 */     println(out);
/* 214 */     print(out, "--" + boundaryString + "--");
/*     */   }
/*     */   
/*     */   private static void println(OutputStream os) throws IOException {
/* 218 */     os.write(13);
/* 219 */     os.write(10);
/*     */   }
/*     */   
/*     */   private static void print(OutputStream os, String buf) throws IOException {
/* 223 */     os.write(buf.getBytes(StandardCharsets.US_ASCII));
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/converter/ResourceRegionHttpMessageConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */