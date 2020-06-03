/*     */ package org.springframework.web.servlet.resource;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public abstract class ResourceTransformerSupport
/*     */   implements ResourceTransformer
/*     */ {
/*     */   @Nullable
/*     */   private ResourceUrlProvider resourceUrlProvider;
/*     */   
/*     */   public void setResourceUrlProvider(@Nullable ResourceUrlProvider resourceUrlProvider) {
/*  49 */     this.resourceUrlProvider = resourceUrlProvider;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ResourceUrlProvider getResourceUrlProvider() {
/*  57 */     return this.resourceUrlProvider;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String resolveUrlPath(String resourcePath, HttpServletRequest request, Resource resource, ResourceTransformerChain transformerChain) {
/*  76 */     if (resourcePath.startsWith("/")) {
/*     */       
/*  78 */       ResourceUrlProvider urlProvider = findResourceUrlProvider(request);
/*  79 */       return (urlProvider != null) ? urlProvider.getForRequestUrl(request, resourcePath) : null;
/*     */     } 
/*     */ 
/*     */     
/*  83 */     return transformerChain.getResolverChain().resolveUrlPath(resourcePath, 
/*  84 */         Collections.singletonList(resource));
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
/*     */   protected String toAbsolutePath(String path, HttpServletRequest request) {
/*  97 */     String absolutePath = path;
/*  98 */     if (!path.startsWith("/")) {
/*  99 */       ResourceUrlProvider urlProvider = findResourceUrlProvider(request);
/* 100 */       Assert.state((urlProvider != null), "No ResourceUrlProvider");
/* 101 */       String requestPath = urlProvider.getUrlPathHelper().getRequestUri(request);
/* 102 */       absolutePath = StringUtils.applyRelativePath(requestPath, path);
/*     */     } 
/* 104 */     return StringUtils.cleanPath(absolutePath);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private ResourceUrlProvider findResourceUrlProvider(HttpServletRequest request) {
/* 109 */     if (this.resourceUrlProvider != null) {
/* 110 */       return this.resourceUrlProvider;
/*     */     }
/* 112 */     return (ResourceUrlProvider)request.getAttribute(ResourceUrlProviderExposingInterceptor.RESOURCE_URL_PROVIDER_ATTR);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/resource/ResourceTransformerSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */