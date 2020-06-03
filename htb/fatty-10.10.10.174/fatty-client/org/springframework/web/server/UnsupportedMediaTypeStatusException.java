/*     */ package org.springframework.web.server;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.http.HttpStatus;
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
/*     */ 
/*     */ 
/*     */ public class UnsupportedMediaTypeStatusException
/*     */   extends ResponseStatusException
/*     */ {
/*     */   @Nullable
/*     */   private final MediaType contentType;
/*     */   private final List<MediaType> supportedMediaTypes;
/*     */   @Nullable
/*     */   private final ResolvableType bodyType;
/*     */   
/*     */   public UnsupportedMediaTypeStatusException(@Nullable String reason) {
/*  49 */     super(HttpStatus.UNSUPPORTED_MEDIA_TYPE, reason);
/*  50 */     this.contentType = null;
/*  51 */     this.supportedMediaTypes = Collections.emptyList();
/*  52 */     this.bodyType = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnsupportedMediaTypeStatusException(@Nullable MediaType contentType, List<MediaType> supportedTypes) {
/*  59 */     this(contentType, supportedTypes, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnsupportedMediaTypeStatusException(@Nullable MediaType contentType, List<MediaType> supportedTypes, @Nullable ResolvableType bodyType) {
/*  69 */     super(HttpStatus.UNSUPPORTED_MEDIA_TYPE, initReason(contentType, bodyType));
/*  70 */     this.contentType = contentType;
/*  71 */     this.supportedMediaTypes = Collections.unmodifiableList(supportedTypes);
/*  72 */     this.bodyType = bodyType;
/*     */   }
/*     */   
/*     */   private static String initReason(@Nullable MediaType contentType, @Nullable ResolvableType bodyType) {
/*  76 */     return "Content type '" + ((contentType != null) ? (String)contentType : "") + "' not supported" + ((bodyType != null) ? (" for bodyType=" + bodyType
/*  77 */       .toString()) : "");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public MediaType getContentType() {
/*  87 */     return this.contentType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<MediaType> getSupportedMediaTypes() {
/*  95 */     return this.supportedMediaTypes;
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
/*     */   public ResolvableType getBodyType() {
/* 107 */     return this.bodyType;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/server/UnsupportedMediaTypeStatusException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */