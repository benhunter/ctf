/*     */ package org.springframework.web.accept;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MappingMediaTypeFileExtensionResolver
/*     */   implements MediaTypeFileExtensionResolver
/*     */ {
/*  44 */   private final ConcurrentMap<String, MediaType> mediaTypes = new ConcurrentHashMap<>(64);
/*     */   
/*  46 */   private final ConcurrentMap<MediaType, List<String>> fileExtensions = new ConcurrentHashMap<>(64);
/*     */   
/*  48 */   private final List<String> allFileExtensions = new CopyOnWriteArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MappingMediaTypeFileExtensionResolver(@Nullable Map<String, MediaType> mediaTypes) {
/*  55 */     if (mediaTypes != null) {
/*  56 */       List<String> allFileExtensions = new ArrayList<>();
/*  57 */       mediaTypes.forEach((extension, mediaType) -> {
/*     */             String lowerCaseExtension = extension.toLowerCase(Locale.ENGLISH);
/*     */             this.mediaTypes.put(lowerCaseExtension, mediaType);
/*     */             addFileExtension(mediaType, lowerCaseExtension);
/*     */             allFileExtensions.add(lowerCaseExtension);
/*     */           });
/*  63 */       this.allFileExtensions.addAll(allFileExtensions);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, MediaType> getMediaTypes() {
/*  69 */     return this.mediaTypes;
/*     */   }
/*     */   
/*     */   protected List<MediaType> getAllMediaTypes() {
/*  73 */     return new ArrayList<>(this.mediaTypes.values());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addMapping(String extension, MediaType mediaType) {
/*  80 */     MediaType previous = this.mediaTypes.putIfAbsent(extension, mediaType);
/*  81 */     if (previous == null) {
/*  82 */       addFileExtension(mediaType, extension);
/*  83 */       this.allFileExtensions.add(extension);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addFileExtension(MediaType mediaType, String extension) {
/*  88 */     List<String> newList = new CopyOnWriteArrayList<>();
/*  89 */     List<String> oldList = this.fileExtensions.putIfAbsent(mediaType, newList);
/*  90 */     ((oldList != null) ? oldList : newList).add(extension);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> resolveFileExtensions(MediaType mediaType) {
/*  96 */     List<String> fileExtensions = this.fileExtensions.get(mediaType);
/*  97 */     return (fileExtensions != null) ? fileExtensions : Collections.<String>emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> getAllFileExtensions() {
/* 102 */     return Collections.unmodifiableList(this.allFileExtensions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected MediaType lookupMediaType(String extension) {
/* 111 */     return this.mediaTypes.get(extension.toLowerCase(Locale.ENGLISH));
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/accept/MappingMediaTypeFileExtensionResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */