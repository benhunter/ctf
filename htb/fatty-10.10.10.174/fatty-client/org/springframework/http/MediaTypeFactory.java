/*     */ package org.springframework.http;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Optional;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ public final class MediaTypeFactory
/*     */ {
/*     */   private static final String MIME_TYPES_FILE_NAME = "/org/springframework/http/mime.types";
/*  47 */   private static final MultiValueMap<String, MediaType> fileExtensionToMediaTypes = parseMimeTypes();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static MultiValueMap<String, MediaType> parseMimeTypes() {
/*  67 */     InputStream is = MediaTypeFactory.class.getResourceAsStream("/org/springframework/http/mime.types");
/*  68 */     try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.US_ASCII))) {
/*  69 */       LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap();
/*     */       String line;
/*  71 */       while ((line = reader.readLine()) != null) {
/*  72 */         if (line.isEmpty() || line.charAt(0) == '#') {
/*     */           continue;
/*     */         }
/*  75 */         String[] tokens = StringUtils.tokenizeToStringArray(line, " \t\n\r\f");
/*  76 */         MediaType mediaType = MediaType.parseMediaType(tokens[0]);
/*  77 */         for (int i = 1; i < tokens.length; i++) {
/*  78 */           String fileExtension = tokens[i].toLowerCase(Locale.ENGLISH);
/*  79 */           linkedMultiValueMap.add(fileExtension, mediaType);
/*     */         } 
/*     */       } 
/*  82 */       return (MultiValueMap<String, MediaType>)linkedMultiValueMap;
/*     */     }
/*  84 */     catch (IOException ex) {
/*  85 */       throw new IllegalStateException("Could not load '/org/springframework/http/mime.types'", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Optional<MediaType> getMediaType(@Nullable Resource resource) {
/*  95 */     return Optional.<Resource>ofNullable(resource)
/*  96 */       .map(Resource::getFilename)
/*  97 */       .flatMap(MediaTypeFactory::getMediaType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Optional<MediaType> getMediaType(@Nullable String filename) {
/* 106 */     return getMediaTypes(filename).stream().findFirst();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<MediaType> getMediaTypes(@Nullable String filename) {
/* 115 */     return Optional.<String>ofNullable(StringUtils.getFilenameExtension(filename))
/* 116 */       .map(s -> s.toLowerCase(Locale.ENGLISH))
/* 117 */       .map(fileExtensionToMediaTypes::get)
/* 118 */       .orElse(Collections.emptyList());
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/MediaTypeFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */