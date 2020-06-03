/*     */ package org.springframework.web.servlet.resource;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.core.io.AbstractResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.http.HttpHeaders;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class GzipResourceResolver
/*     */   extends AbstractResourceResolver
/*     */ {
/*     */   protected Resource resolveResourceInternal(@Nullable HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
/*  52 */     Resource resource = chain.resolveResource(request, requestPath, locations);
/*  53 */     if (resource == null || (request != null && !isGzipAccepted(request))) {
/*  54 */       return resource;
/*     */     }
/*     */     
/*     */     try {
/*  58 */       Resource gzipped = new GzippedResource(resource);
/*  59 */       if (gzipped.exists()) {
/*  60 */         return gzipped;
/*     */       }
/*     */     }
/*  63 */     catch (IOException ex) {
/*  64 */       this.logger.trace("No gzip resource for [" + resource.getFilename() + "]", ex);
/*     */     } 
/*     */     
/*  67 */     return resource;
/*     */   }
/*     */   
/*     */   private boolean isGzipAccepted(HttpServletRequest request) {
/*  71 */     String value = request.getHeader("Accept-Encoding");
/*  72 */     return (value != null && value.toLowerCase().contains("gzip"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String resolveUrlPathInternal(String resourceUrlPath, List<? extends Resource> locations, ResourceResolverChain chain) {
/*  79 */     return chain.resolveUrlPath(resourceUrlPath, locations);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class GzippedResource
/*     */     extends AbstractResource
/*     */     implements HttpResource
/*     */   {
/*     */     private final Resource original;
/*     */     
/*     */     private final Resource gzipped;
/*     */ 
/*     */     
/*     */     public GzippedResource(Resource original) throws IOException {
/*  93 */       this.original = original;
/*  94 */       this.gzipped = original.createRelative(original.getFilename() + ".gz");
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream getInputStream() throws IOException {
/*  99 */       return this.gzipped.getInputStream();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean exists() {
/* 104 */       return this.gzipped.exists();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isReadable() {
/* 109 */       return this.gzipped.isReadable();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isOpen() {
/* 114 */       return this.gzipped.isOpen();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isFile() {
/* 119 */       return this.gzipped.isFile();
/*     */     }
/*     */ 
/*     */     
/*     */     public URL getURL() throws IOException {
/* 124 */       return this.gzipped.getURL();
/*     */     }
/*     */ 
/*     */     
/*     */     public URI getURI() throws IOException {
/* 129 */       return this.gzipped.getURI();
/*     */     }
/*     */ 
/*     */     
/*     */     public File getFile() throws IOException {
/* 134 */       return this.gzipped.getFile();
/*     */     }
/*     */ 
/*     */     
/*     */     public long contentLength() throws IOException {
/* 139 */       return this.gzipped.contentLength();
/*     */     }
/*     */ 
/*     */     
/*     */     public long lastModified() throws IOException {
/* 144 */       return this.gzipped.lastModified();
/*     */     }
/*     */ 
/*     */     
/*     */     public Resource createRelative(String relativePath) throws IOException {
/* 149 */       return this.gzipped.createRelative(relativePath);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getFilename() {
/* 155 */       return this.original.getFilename();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getDescription() {
/* 160 */       return this.gzipped.getDescription();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public HttpHeaders getResponseHeaders() {
/* 166 */       HttpHeaders headers = (this.original instanceof HttpResource) ? ((HttpResource)this.original).getResponseHeaders() : new HttpHeaders();
/* 167 */       headers.add("Content-Encoding", "gzip");
/* 168 */       headers.add("Vary", "Accept-Encoding");
/* 169 */       return headers;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/resource/GzipResourceResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */