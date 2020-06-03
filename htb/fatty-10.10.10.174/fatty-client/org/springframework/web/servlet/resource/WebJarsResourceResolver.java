/*     */ package org.springframework.web.servlet.resource;
/*     */ 
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.webjars.WebJarAssetLocator;
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
/*     */ 
/*     */ 
/*     */ public class WebJarsResourceResolver
/*     */   extends AbstractResourceResolver
/*     */ {
/*     */   private static final String WEBJARS_LOCATION = "META-INF/resources/webjars/";
/*  50 */   private static final int WEBJARS_LOCATION_LENGTH = "META-INF/resources/webjars/".length();
/*     */ 
/*     */ 
/*     */   
/*     */   private final WebJarAssetLocator webJarAssetLocator;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebJarsResourceResolver() {
/*  60 */     this(new WebJarAssetLocator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebJarsResourceResolver(WebJarAssetLocator webJarAssetLocator) {
/*  69 */     this.webJarAssetLocator = webJarAssetLocator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Resource resolveResourceInternal(@Nullable HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
/*  77 */     Resource resolved = chain.resolveResource(request, requestPath, locations);
/*  78 */     if (resolved == null) {
/*  79 */       String webJarResourcePath = findWebJarResourcePath(requestPath);
/*  80 */       if (webJarResourcePath != null) {
/*  81 */         return chain.resolveResource(request, webJarResourcePath, locations);
/*     */       }
/*     */     } 
/*  84 */     return resolved;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String resolveUrlPathInternal(String resourceUrlPath, List<? extends Resource> locations, ResourceResolverChain chain) {
/*  91 */     String path = chain.resolveUrlPath(resourceUrlPath, locations);
/*  92 */     if (path == null) {
/*  93 */       String webJarResourcePath = findWebJarResourcePath(resourceUrlPath);
/*  94 */       if (webJarResourcePath != null) {
/*  95 */         return chain.resolveUrlPath(webJarResourcePath, locations);
/*     */       }
/*     */     } 
/*  98 */     return path;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected String findWebJarResourcePath(String path) {
/* 103 */     int startOffset = path.startsWith("/") ? 1 : 0;
/* 104 */     int endOffset = path.indexOf('/', 1);
/* 105 */     if (endOffset != -1) {
/* 106 */       String webjar = path.substring(startOffset, endOffset);
/* 107 */       String partialPath = path.substring(endOffset + 1);
/* 108 */       String webJarPath = this.webJarAssetLocator.getFullPathExact(webjar, partialPath);
/* 109 */       if (webJarPath != null) {
/* 110 */         return webJarPath.substring(WEBJARS_LOCATION_LENGTH);
/*     */       }
/*     */     } 
/* 113 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/resource/WebJarsResourceResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */