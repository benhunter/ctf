/*     */ package org.springframework.web.servlet.resource;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.core.io.AbstractResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class EncodedResourceResolver
/*     */   extends AbstractResourceResolver
/*     */ {
/*  59 */   public static final List<String> DEFAULT_CODINGS = Arrays.asList(new String[] { "br", "gzip" });
/*     */ 
/*     */   
/*  62 */   private final List<String> contentCodings = new ArrayList<>(DEFAULT_CODINGS);
/*     */   
/*  64 */   private final Map<String, String> extensions = new LinkedHashMap<>();
/*     */ 
/*     */   
/*     */   public EncodedResourceResolver() {
/*  68 */     this.extensions.put("gzip", ".gz");
/*  69 */     this.extensions.put("br", ".br");
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
/*     */   public void setContentCodings(List<String> codings) {
/*  87 */     Assert.notEmpty(codings, "At least one content coding expected");
/*  88 */     this.contentCodings.clear();
/*  89 */     this.contentCodings.addAll(codings);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getContentCodings() {
/*  96 */     return Collections.unmodifiableList(this.contentCodings);
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
/*     */   public void setExtensions(Map<String, String> extensions) {
/* 108 */     extensions.forEach(this::registerExtension);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> getExtensions() {
/* 115 */     return Collections.unmodifiableMap(this.extensions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerExtension(String coding, String extension) {
/* 124 */     this.extensions.put(coding, extension.startsWith(".") ? extension : ("." + extension));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Resource resolveResourceInternal(@Nullable HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
/* 132 */     Resource resource = chain.resolveResource(request, requestPath, locations);
/* 133 */     if (resource == null || request == null) {
/* 134 */       return resource;
/*     */     }
/*     */     
/* 137 */     String acceptEncoding = getAcceptEncoding(request);
/* 138 */     if (acceptEncoding == null) {
/* 139 */       return resource;
/*     */     }
/*     */     
/* 142 */     for (String coding : this.contentCodings) {
/* 143 */       if (acceptEncoding.contains(coding)) {
/*     */         try {
/* 145 */           String extension = getExtension(coding);
/* 146 */           Resource encoded = new EncodedResource(resource, coding, extension);
/* 147 */           if (encoded.exists()) {
/* 148 */             return encoded;
/*     */           }
/*     */         }
/* 151 */         catch (IOException ex) {
/* 152 */           if (this.logger.isTraceEnabled()) {
/* 153 */             this.logger.trace("No " + coding + " resource for [" + resource.getFilename() + "]", ex);
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 159 */     return resource;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private String getAcceptEncoding(HttpServletRequest request) {
/* 164 */     String header = request.getHeader("Accept-Encoding");
/* 165 */     return (header != null) ? header.toLowerCase() : null;
/*     */   }
/*     */   
/*     */   private String getExtension(String coding) {
/* 169 */     String extension = this.extensions.get(coding);
/* 170 */     if (extension == null) {
/* 171 */       throw new IllegalStateException("No file extension associated with content coding " + coding);
/*     */     }
/* 173 */     return extension;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String resolveUrlPathInternal(String resourceUrlPath, List<? extends Resource> locations, ResourceResolverChain chain) {
/* 180 */     return chain.resolveUrlPath(resourceUrlPath, locations);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class EncodedResource
/*     */     extends AbstractResource
/*     */     implements HttpResource
/*     */   {
/*     */     private final Resource original;
/*     */     
/*     */     private final String coding;
/*     */     
/*     */     private final Resource encoded;
/*     */ 
/*     */     
/*     */     EncodedResource(Resource original, String coding, String extension) throws IOException {
/* 196 */       this.original = original;
/* 197 */       this.coding = coding;
/* 198 */       this.encoded = original.createRelative(original.getFilename() + extension);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public InputStream getInputStream() throws IOException {
/* 204 */       return this.encoded.getInputStream();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean exists() {
/* 209 */       return this.encoded.exists();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isReadable() {
/* 214 */       return this.encoded.isReadable();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isOpen() {
/* 219 */       return this.encoded.isOpen();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isFile() {
/* 224 */       return this.encoded.isFile();
/*     */     }
/*     */ 
/*     */     
/*     */     public URL getURL() throws IOException {
/* 229 */       return this.encoded.getURL();
/*     */     }
/*     */ 
/*     */     
/*     */     public URI getURI() throws IOException {
/* 234 */       return this.encoded.getURI();
/*     */     }
/*     */ 
/*     */     
/*     */     public File getFile() throws IOException {
/* 239 */       return this.encoded.getFile();
/*     */     }
/*     */ 
/*     */     
/*     */     public long contentLength() throws IOException {
/* 244 */       return this.encoded.contentLength();
/*     */     }
/*     */ 
/*     */     
/*     */     public long lastModified() throws IOException {
/* 249 */       return this.encoded.lastModified();
/*     */     }
/*     */ 
/*     */     
/*     */     public Resource createRelative(String relativePath) throws IOException {
/* 254 */       return this.encoded.createRelative(relativePath);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getFilename() {
/* 260 */       return this.original.getFilename();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getDescription() {
/* 265 */       return this.encoded.getDescription();
/*     */     }
/*     */ 
/*     */     
/*     */     public HttpHeaders getResponseHeaders() {
/*     */       HttpHeaders headers;
/* 271 */       if (this.original instanceof HttpResource) {
/* 272 */         headers = ((HttpResource)this.original).getResponseHeaders();
/*     */       } else {
/*     */         
/* 275 */         headers = new HttpHeaders();
/*     */       } 
/* 277 */       headers.add("Content-Encoding", this.coding);
/* 278 */       headers.add("Vary", "Accept-Encoding");
/* 279 */       return headers;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/resource/EncodedResourceResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */