/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import org.springframework.util.ResourceUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractFileResolvingResource
/*     */   extends AbstractResource
/*     */ {
/*     */   public boolean exists() {
/*     */     try {
/*  48 */       URL url = getURL();
/*  49 */       if (ResourceUtils.isFileURL(url))
/*     */       {
/*  51 */         return getFile().exists();
/*     */       }
/*     */ 
/*     */       
/*  55 */       URLConnection con = url.openConnection();
/*  56 */       customizeConnection(con);
/*  57 */       HttpURLConnection httpCon = (con instanceof HttpURLConnection) ? (HttpURLConnection)con : null;
/*     */       
/*  59 */       if (httpCon != null) {
/*  60 */         int code = httpCon.getResponseCode();
/*  61 */         if (code == 200) {
/*  62 */           return true;
/*     */         }
/*  64 */         if (code == 404) {
/*  65 */           return false;
/*     */         }
/*     */       } 
/*  68 */       if (con.getContentLengthLong() > 0L) {
/*  69 */         return true;
/*     */       }
/*  71 */       if (httpCon != null) {
/*     */         
/*  73 */         httpCon.disconnect();
/*  74 */         return false;
/*     */       } 
/*     */ 
/*     */       
/*  78 */       getInputStream().close();
/*  79 */       return true;
/*     */ 
/*     */     
/*     */     }
/*  83 */     catch (IOException ex) {
/*  84 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isReadable() {
/*     */     try {
/*  91 */       URL url = getURL();
/*  92 */       if (ResourceUtils.isFileURL(url)) {
/*     */         
/*  94 */         File file = getFile();
/*  95 */         return (file.canRead() && !file.isDirectory());
/*     */       } 
/*     */ 
/*     */       
/*  99 */       URLConnection con = url.openConnection();
/* 100 */       customizeConnection(con);
/* 101 */       if (con instanceof HttpURLConnection) {
/* 102 */         HttpURLConnection httpCon = (HttpURLConnection)con;
/* 103 */         int code = httpCon.getResponseCode();
/* 104 */         if (code != 200) {
/* 105 */           httpCon.disconnect();
/* 106 */           return false;
/*     */         } 
/*     */       } 
/* 109 */       long contentLength = con.getContentLengthLong();
/* 110 */       if (contentLength > 0L) {
/* 111 */         return true;
/*     */       }
/* 113 */       if (contentLength == 0L)
/*     */       {
/* 115 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 119 */       getInputStream().close();
/* 120 */       return true;
/*     */ 
/*     */     
/*     */     }
/* 124 */     catch (IOException ex) {
/* 125 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFile() {
/*     */     try {
/* 132 */       URL url = getURL();
/* 133 */       if (url.getProtocol().startsWith("vfs")) {
/* 134 */         return VfsResourceDelegate.getResource(url).isFile();
/*     */       }
/* 136 */       return "file".equals(url.getProtocol());
/*     */     }
/* 138 */     catch (IOException ex) {
/* 139 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() throws IOException {
/* 150 */     URL url = getURL();
/* 151 */     if (url.getProtocol().startsWith("vfs")) {
/* 152 */       return VfsResourceDelegate.getResource(url).getFile();
/*     */     }
/* 154 */     return ResourceUtils.getFile(url, getDescription());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected File getFileForLastModifiedCheck() throws IOException {
/* 163 */     URL url = getURL();
/* 164 */     if (ResourceUtils.isJarURL(url)) {
/* 165 */       URL actualUrl = ResourceUtils.extractArchiveURL(url);
/* 166 */       if (actualUrl.getProtocol().startsWith("vfs")) {
/* 167 */         return VfsResourceDelegate.getResource(actualUrl).getFile();
/*     */       }
/* 169 */       return ResourceUtils.getFile(actualUrl, "Jar URL");
/*     */     } 
/*     */     
/* 172 */     return getFile();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isFile(URI uri) {
/*     */     try {
/* 184 */       if (uri.getScheme().startsWith("vfs")) {
/* 185 */         return VfsResourceDelegate.getResource(uri).isFile();
/*     */       }
/* 187 */       return "file".equals(uri.getScheme());
/*     */     }
/* 189 */     catch (IOException ex) {
/* 190 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected File getFile(URI uri) throws IOException {
/* 200 */     if (uri.getScheme().startsWith("vfs")) {
/* 201 */       return VfsResourceDelegate.getResource(uri).getFile();
/*     */     }
/* 203 */     return ResourceUtils.getFile(uri, getDescription());
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
/*     */   public ReadableByteChannel readableChannel() throws IOException {
/*     */     try {
/* 216 */       return FileChannel.open(getFile().toPath(), new OpenOption[] { StandardOpenOption.READ });
/*     */     }
/* 218 */     catch (FileNotFoundException|java.nio.file.NoSuchFileException ex) {
/*     */       
/* 220 */       return super.readableChannel();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public long contentLength() throws IOException {
/* 226 */     URL url = getURL();
/* 227 */     if (ResourceUtils.isFileURL(url)) {
/*     */       
/* 229 */       File file = getFile();
/* 230 */       long length = file.length();
/* 231 */       if (length == 0L && !file.exists()) {
/* 232 */         throw new FileNotFoundException(getDescription() + " cannot be resolved in the file system for checking its content length");
/*     */       }
/*     */       
/* 235 */       return length;
/*     */     } 
/*     */ 
/*     */     
/* 239 */     URLConnection con = url.openConnection();
/* 240 */     customizeConnection(con);
/* 241 */     return con.getContentLengthLong();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long lastModified() throws IOException {
/* 247 */     URL url = getURL();
/* 248 */     boolean fileCheck = false;
/* 249 */     if (ResourceUtils.isFileURL(url) || ResourceUtils.isJarURL(url)) {
/*     */       
/* 251 */       fileCheck = true;
/*     */       try {
/* 253 */         File fileToCheck = getFileForLastModifiedCheck();
/* 254 */         long l = fileToCheck.lastModified();
/* 255 */         if (l > 0L || fileToCheck.exists()) {
/* 256 */           return l;
/*     */         }
/*     */       }
/* 259 */       catch (FileNotFoundException fileNotFoundException) {}
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 264 */     URLConnection con = url.openConnection();
/* 265 */     customizeConnection(con);
/* 266 */     long lastModified = con.getLastModified();
/* 267 */     if (fileCheck && lastModified == 0L && con.getContentLengthLong() <= 0L) {
/* 268 */       throw new FileNotFoundException(getDescription() + " cannot be resolved in the file system for checking its last-modified timestamp");
/*     */     }
/*     */     
/* 271 */     return lastModified;
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
/*     */   protected void customizeConnection(URLConnection con) throws IOException {
/* 284 */     ResourceUtils.useCachesIfNecessary(con);
/* 285 */     if (con instanceof HttpURLConnection) {
/* 286 */       customizeConnection((HttpURLConnection)con);
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
/*     */   protected void customizeConnection(HttpURLConnection con) throws IOException {
/* 298 */     con.setRequestMethod("HEAD");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class VfsResourceDelegate
/*     */   {
/*     */     public static Resource getResource(URL url) throws IOException {
/* 308 */       return new VfsResource(VfsUtils.getRoot(url));
/*     */     }
/*     */     
/*     */     public static Resource getResource(URI uri) throws IOException {
/* 312 */       return new VfsResource(VfsUtils.getRoot(uri));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/io/AbstractFileResolvingResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */