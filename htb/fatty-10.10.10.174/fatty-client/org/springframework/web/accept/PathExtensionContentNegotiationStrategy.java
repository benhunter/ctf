/*     */ package org.springframework.web.accept;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.MediaTypeFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.util.UriUtils;
/*     */ import org.springframework.web.util.UrlPathHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PathExtensionContentNegotiationStrategy
/*     */   extends AbstractMappingContentNegotiationStrategy
/*     */ {
/*  46 */   private UrlPathHelper urlPathHelper = new UrlPathHelper();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathExtensionContentNegotiationStrategy() {
/*  54 */     this((Map<String, MediaType>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathExtensionContentNegotiationStrategy(@Nullable Map<String, MediaType> mediaTypes) {
/*  61 */     super(mediaTypes);
/*  62 */     setUseRegisteredExtensionsOnly(false);
/*  63 */     setIgnoreUnknownExtensions(true);
/*  64 */     this.urlPathHelper.setUrlDecode(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
/*  74 */     this.urlPathHelper = urlPathHelper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setUseJaf(boolean useJaf) {
/*  84 */     setUseRegisteredExtensionsOnly(!useJaf);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getMediaTypeKey(NativeWebRequest webRequest) {
/*  90 */     HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/*  91 */     if (request == null) {
/*  92 */       return null;
/*     */     }
/*  94 */     String path = this.urlPathHelper.getLookupPathForRequest(request);
/*  95 */     String extension = UriUtils.extractFileExtension(path);
/*  96 */     return StringUtils.hasText(extension) ? extension.toLowerCase(Locale.ENGLISH) : null;
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
/*     */   @Nullable
/*     */   public MediaType getMediaTypeForResource(Resource resource) {
/* 110 */     Assert.notNull(resource, "Resource must not be null");
/* 111 */     MediaType mediaType = null;
/* 112 */     String filename = resource.getFilename();
/* 113 */     String extension = StringUtils.getFilenameExtension(filename);
/* 114 */     if (extension != null) {
/* 115 */       mediaType = lookupMediaType(extension);
/*     */     }
/* 117 */     if (mediaType == null) {
/* 118 */       mediaType = MediaTypeFactory.getMediaType(filename).orElse(null);
/*     */     }
/* 120 */     return mediaType;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/accept/PathExtensionContentNegotiationStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */