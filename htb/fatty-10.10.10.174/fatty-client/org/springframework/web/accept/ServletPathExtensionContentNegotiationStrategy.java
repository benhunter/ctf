/*     */ package org.springframework.web.accept;
/*     */ 
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class ServletPathExtensionContentNegotiationStrategy
/*     */   extends PathExtensionContentNegotiationStrategy
/*     */ {
/*     */   private final ServletContext servletContext;
/*     */   
/*     */   public ServletPathExtensionContentNegotiationStrategy(ServletContext context) {
/*  49 */     this(context, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletPathExtensionContentNegotiationStrategy(ServletContext servletContext, @Nullable Map<String, MediaType> mediaTypes) {
/*  58 */     super(mediaTypes);
/*  59 */     Assert.notNull(servletContext, "ServletContext is required");
/*  60 */     this.servletContext = servletContext;
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
/*     */   protected MediaType handleNoMatch(NativeWebRequest webRequest, String extension) throws HttpMediaTypeNotAcceptableException {
/*  74 */     MediaType mediaType = null;
/*  75 */     String mimeType = this.servletContext.getMimeType("file." + extension);
/*  76 */     if (StringUtils.hasText(mimeType)) {
/*  77 */       mediaType = MediaType.parseMediaType(mimeType);
/*     */     }
/*  79 */     if (mediaType == null || MediaType.APPLICATION_OCTET_STREAM.equals(mediaType)) {
/*  80 */       MediaType superMediaType = super.handleNoMatch(webRequest, extension);
/*  81 */       if (superMediaType != null) {
/*  82 */         mediaType = superMediaType;
/*     */       }
/*     */     } 
/*  85 */     return mediaType;
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
/*     */   public MediaType getMediaTypeForResource(Resource resource) {
/*  98 */     MediaType mediaType = null;
/*  99 */     String mimeType = this.servletContext.getMimeType(resource.getFilename());
/* 100 */     if (StringUtils.hasText(mimeType)) {
/* 101 */       mediaType = MediaType.parseMediaType(mimeType);
/*     */     }
/* 103 */     if (mediaType == null || MediaType.APPLICATION_OCTET_STREAM.equals(mediaType)) {
/* 104 */       MediaType superMediaType = super.getMediaTypeForResource(resource);
/* 105 */       if (superMediaType != null) {
/* 106 */         mediaType = superMediaType;
/*     */       }
/*     */     } 
/* 109 */     return mediaType;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/accept/ServletPathExtensionContentNegotiationStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */