/*     */ package org.springframework.web.servlet.view.tiles3;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Locale;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.apache.tiles.request.ApplicationResource;
/*     */ import org.apache.tiles.request.locale.URLApplicationResource;
/*     */ import org.apache.tiles.request.servlet.ServletApplicationContext;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.support.ResourcePatternResolver;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.web.context.support.ServletContextResourcePatternResolver;
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
/*     */ public class SpringWildcardServletTilesApplicationContext
/*     */   extends ServletApplicationContext
/*     */ {
/*     */   private final ResourcePatternResolver resolver;
/*     */   
/*     */   public SpringWildcardServletTilesApplicationContext(ServletContext servletContext) {
/*  51 */     super(servletContext);
/*  52 */     this.resolver = (ResourcePatternResolver)new ServletContextResourcePatternResolver(servletContext);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ApplicationResource getResource(String localePath) {
/*  59 */     Collection<ApplicationResource> urlSet = getResources(localePath);
/*  60 */     if (!CollectionUtils.isEmpty(urlSet)) {
/*  61 */       return urlSet.iterator().next();
/*     */     }
/*  63 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ApplicationResource getResource(ApplicationResource base, Locale locale) {
/*  69 */     Collection<ApplicationResource> urlSet = getResources(base.getLocalePath(locale));
/*  70 */     if (!CollectionUtils.isEmpty(urlSet)) {
/*  71 */       return urlSet.iterator().next();
/*     */     }
/*  73 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<ApplicationResource> getResources(String path) {
/*     */     Resource[] resources;
/*     */     try {
/*  80 */       resources = this.resolver.getResources(path);
/*     */     }
/*  82 */     catch (IOException ex) {
/*  83 */       ((ServletContext)getContext()).log("Resource retrieval failed for path: " + path, ex);
/*  84 */       return Collections.emptyList();
/*     */     } 
/*  86 */     if (ObjectUtils.isEmpty((Object[])resources)) {
/*  87 */       ((ServletContext)getContext()).log("No resources found for path pattern: " + path);
/*  88 */       return Collections.emptyList();
/*     */     } 
/*     */     
/*  91 */     Collection<ApplicationResource> resourceList = new ArrayList<>(resources.length);
/*  92 */     for (Resource resource : resources) {
/*     */       try {
/*  94 */         URL url = resource.getURL();
/*  95 */         resourceList.add(new URLApplicationResource(url.toExternalForm(), url));
/*     */       }
/*  97 */       catch (IOException ex) {
/*     */         
/*  99 */         throw new IllegalArgumentException("No URL for " + resource, ex);
/*     */       } 
/*     */     } 
/* 102 */     return resourceList;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/tiles3/SpringWildcardServletTilesApplicationContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */