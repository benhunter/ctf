/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.nio.channels.Channels;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import org.springframework.core.NestedIOException;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractResource
/*     */   implements Resource
/*     */ {
/*     */   public boolean exists() {
/*     */     try {
/*  55 */       return getFile().exists();
/*     */     }
/*  57 */     catch (IOException ex) {
/*     */       
/*     */       try {
/*  60 */         getInputStream().close();
/*  61 */         return true;
/*     */       }
/*  63 */       catch (Throwable isEx) {
/*  64 */         return false;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReadable() {
/*  75 */     return exists();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/*  83 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFile() {
/*  91 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getURL() throws IOException {
/* 100 */     throw new FileNotFoundException(getDescription() + " cannot be resolved to URL");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getURI() throws IOException {
/* 109 */     URL url = getURL();
/*     */     try {
/* 111 */       return ResourceUtils.toURI(url);
/*     */     }
/* 113 */     catch (URISyntaxException ex) {
/* 114 */       throw new NestedIOException("Invalid URI [" + url + "]", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() throws IOException {
/* 124 */     throw new FileNotFoundException(getDescription() + " cannot be resolved to absolute file path");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReadableByteChannel readableChannel() throws IOException {
/* 135 */     return Channels.newChannel(getInputStream());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long contentLength() throws IOException {
/* 146 */     InputStream is = getInputStream();
/*     */     try {
/* 148 */       long size = 0L;
/* 149 */       byte[] buf = new byte[256];
/*     */       int read;
/* 151 */       while ((read = is.read(buf)) != -1) {
/* 152 */         size += read;
/*     */       }
/* 154 */       return size;
/*     */     } finally {
/*     */       
/*     */       try {
/* 158 */         is.close();
/*     */       }
/* 160 */       catch (IOException iOException) {}
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
/*     */   public long lastModified() throws IOException {
/* 172 */     File fileToCheck = getFileForLastModifiedCheck();
/* 173 */     long lastModified = fileToCheck.lastModified();
/* 174 */     if (lastModified == 0L && !fileToCheck.exists()) {
/* 175 */       throw new FileNotFoundException(getDescription() + " cannot be resolved in the file system for checking its last-modified timestamp");
/*     */     }
/*     */     
/* 178 */     return lastModified;
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
/*     */   protected File getFileForLastModifiedCheck() throws IOException {
/* 190 */     return getFile();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource createRelative(String relativePath) throws IOException {
/* 199 */     throw new FileNotFoundException("Cannot create a relative resource for " + getDescription());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getFilename() {
/* 209 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 219 */     return (this == other || (other instanceof Resource && ((Resource)other)
/* 220 */       .getDescription().equals(getDescription())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 229 */     return getDescription().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 238 */     return getDescription();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/io/AbstractResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */