/*     */ package org.springframework.web.accept;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.MediaTypeFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.HttpMediaTypeNotAcceptableException;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractMappingContentNegotiationStrategy
/*     */   extends MappingMediaTypeFileExtensionResolver
/*     */   implements ContentNegotiationStrategy
/*     */ {
/*  55 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */   
/*     */   private boolean useRegisteredExtensionsOnly = false;
/*     */ 
/*     */   
/*     */   private boolean ignoreUnknownExtensions = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractMappingContentNegotiationStrategy(@Nullable Map<String, MediaType> mediaTypes) {
/*  66 */     super(mediaTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUseRegisteredExtensionsOnly(boolean useRegisteredExtensionsOnly) {
/*  76 */     this.useRegisteredExtensionsOnly = useRegisteredExtensionsOnly;
/*     */   }
/*     */   
/*     */   public boolean isUseRegisteredExtensionsOnly() {
/*  80 */     return this.useRegisteredExtensionsOnly;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIgnoreUnknownExtensions(boolean ignoreUnknownExtensions) {
/*  90 */     this.ignoreUnknownExtensions = ignoreUnknownExtensions;
/*     */   }
/*     */   
/*     */   public boolean isIgnoreUnknownExtensions() {
/*  94 */     return this.ignoreUnknownExtensions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<MediaType> resolveMediaTypes(NativeWebRequest webRequest) throws HttpMediaTypeNotAcceptableException {
/* 102 */     return resolveMediaTypeKey(webRequest, getMediaTypeKey(webRequest));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<MediaType> resolveMediaTypeKey(NativeWebRequest webRequest, @Nullable String key) throws HttpMediaTypeNotAcceptableException {
/* 113 */     if (StringUtils.hasText(key)) {
/* 114 */       MediaType mediaType = lookupMediaType(key);
/* 115 */       if (mediaType != null) {
/* 116 */         handleMatch(key, mediaType);
/* 117 */         return Collections.singletonList(mediaType);
/*     */       } 
/* 119 */       mediaType = handleNoMatch(webRequest, key);
/* 120 */       if (mediaType != null) {
/* 121 */         addMapping(key, mediaType);
/* 122 */         return Collections.singletonList(mediaType);
/*     */       } 
/*     */     } 
/* 125 */     return MEDIA_TYPE_ALL_LIST;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected abstract String getMediaTypeKey(NativeWebRequest paramNativeWebRequest);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleMatch(String key, MediaType mediaType) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected MediaType handleNoMatch(NativeWebRequest request, String key) throws HttpMediaTypeNotAcceptableException {
/* 153 */     if (!isUseRegisteredExtensionsOnly()) {
/* 154 */       Optional<MediaType> mediaType = MediaTypeFactory.getMediaType("file." + key);
/* 155 */       if (mediaType.isPresent()) {
/* 156 */         return mediaType.get();
/*     */       }
/*     */     } 
/* 159 */     if (isIgnoreUnknownExtensions()) {
/* 160 */       return null;
/*     */     }
/* 162 */     throw new HttpMediaTypeNotAcceptableException(getAllMediaTypes());
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/accept/AbstractMappingContentNegotiationStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */