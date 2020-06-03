/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ResourceUtils;
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
/*     */ public class UrlResource
/*     */   extends AbstractFileResolvingResource
/*     */ {
/*     */   @Nullable
/*     */   private final URI uri;
/*     */   private final URL url;
/*     */   private final URL cleanedUrl;
/*     */   
/*     */   public UrlResource(URI uri) throws MalformedURLException {
/*  69 */     Assert.notNull(uri, "URI must not be null");
/*  70 */     this.uri = uri;
/*  71 */     this.url = uri.toURL();
/*  72 */     this.cleanedUrl = getCleanedUrl(this.url, uri.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UrlResource(URL url) {
/*  80 */     Assert.notNull(url, "URL must not be null");
/*  81 */     this.url = url;
/*  82 */     this.cleanedUrl = getCleanedUrl(this.url, url.toString());
/*  83 */     this.uri = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UrlResource(String path) throws MalformedURLException {
/*  94 */     Assert.notNull(path, "Path must not be null");
/*  95 */     this.uri = null;
/*  96 */     this.url = new URL(path);
/*  97 */     this.cleanedUrl = getCleanedUrl(this.url, path);
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
/*     */   public UrlResource(String protocol, String location) throws MalformedURLException {
/* 111 */     this(protocol, location, null);
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
/*     */   public UrlResource(String protocol, String location, @Nullable String fragment) throws MalformedURLException {
/*     */     try {
/* 128 */       this.uri = new URI(protocol, location, fragment);
/* 129 */       this.url = this.uri.toURL();
/* 130 */       this.cleanedUrl = getCleanedUrl(this.url, this.uri.toString());
/*     */     }
/* 132 */     catch (URISyntaxException ex) {
/* 133 */       MalformedURLException exToThrow = new MalformedURLException(ex.getMessage());
/* 134 */       exToThrow.initCause(ex);
/* 135 */       throw exToThrow;
/*     */     } 
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
/*     */   private URL getCleanedUrl(URL originalUrl, String originalPath) {
/* 148 */     String cleanedPath = StringUtils.cleanPath(originalPath);
/* 149 */     if (!cleanedPath.equals(originalPath)) {
/*     */       try {
/* 151 */         return new URL(cleanedPath);
/*     */       }
/* 153 */       catch (MalformedURLException malformedURLException) {}
/*     */     }
/*     */ 
/*     */     
/* 157 */     return originalUrl;
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
/*     */   public InputStream getInputStream() throws IOException {
/* 170 */     URLConnection con = this.url.openConnection();
/* 171 */     ResourceUtils.useCachesIfNecessary(con);
/*     */     try {
/* 173 */       return con.getInputStream();
/*     */     }
/* 175 */     catch (IOException ex) {
/*     */       
/* 177 */       if (con instanceof HttpURLConnection) {
/* 178 */         ((HttpURLConnection)con).disconnect();
/*     */       }
/* 180 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getURL() {
/* 189 */     return this.url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getURI() throws IOException {
/* 198 */     if (this.uri != null) {
/* 199 */       return this.uri;
/*     */     }
/*     */     
/* 202 */     return super.getURI();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFile() {
/* 208 */     if (this.uri != null) {
/* 209 */       return isFile(this.uri);
/*     */     }
/*     */     
/* 212 */     return super.isFile();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() throws IOException {
/* 223 */     if (this.uri != null) {
/* 224 */       return getFile(this.uri);
/*     */     }
/*     */     
/* 227 */     return super.getFile();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource createRelative(String relativePath) throws MalformedURLException {
/* 238 */     if (relativePath.startsWith("/")) {
/* 239 */       relativePath = relativePath.substring(1);
/*     */     }
/* 241 */     return new UrlResource(new URL(this.url, relativePath));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFilename() {
/* 250 */     return StringUtils.getFilename(this.cleanedUrl.getPath());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 258 */     return "URL [" + this.url + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 267 */     return (this == other || (other instanceof UrlResource && this.cleanedUrl
/* 268 */       .equals(((UrlResource)other).cleanedUrl)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 276 */     return this.cleanedUrl.hashCode();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/io/UrlResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */