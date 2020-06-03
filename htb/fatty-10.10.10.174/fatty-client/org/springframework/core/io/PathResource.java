/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.NoSuchFileException;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.StandardOpenOption;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class PathResource
/*     */   extends AbstractResource
/*     */   implements WritableResource
/*     */ {
/*     */   private final Path path;
/*     */   
/*     */   public PathResource(Path path) {
/*  69 */     Assert.notNull(path, "Path must not be null");
/*  70 */     this.path = path.normalize();
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
/*     */   public PathResource(String path) {
/*  82 */     Assert.notNull(path, "Path must not be null");
/*  83 */     this.path = Paths.get(path, new String[0]).normalize();
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
/*     */   public PathResource(URI uri) {
/*  95 */     Assert.notNull(uri, "URI must not be null");
/*  96 */     this.path = Paths.get(uri).normalize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getPath() {
/* 104 */     return this.path.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean exists() {
/* 113 */     return Files.exists(this.path, new java.nio.file.LinkOption[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReadable() {
/* 124 */     return (Files.isReadable(this.path) && !Files.isDirectory(this.path, new java.nio.file.LinkOption[0]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() throws IOException {
/* 133 */     if (!exists()) {
/* 134 */       throw new FileNotFoundException(getPath() + " (no such file or directory)");
/*     */     }
/* 136 */     if (Files.isDirectory(this.path, new java.nio.file.LinkOption[0])) {
/* 137 */       throw new FileNotFoundException(getPath() + " (is a directory)");
/*     */     }
/* 139 */     return Files.newInputStream(this.path, new OpenOption[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWritable() {
/* 150 */     return (Files.isWritable(this.path) && !Files.isDirectory(this.path, new java.nio.file.LinkOption[0]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OutputStream getOutputStream() throws IOException {
/* 159 */     if (Files.isDirectory(this.path, new java.nio.file.LinkOption[0])) {
/* 160 */       throw new FileNotFoundException(getPath() + " (is a directory)");
/*     */     }
/* 162 */     return Files.newOutputStream(this.path, new OpenOption[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getURL() throws IOException {
/* 172 */     return this.path.toUri().toURL();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getURI() throws IOException {
/* 181 */     return this.path.toUri();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFile() {
/* 189 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() throws IOException {
/*     */     try {
/* 198 */       return this.path.toFile();
/*     */     }
/* 200 */     catch (UnsupportedOperationException ex) {
/*     */ 
/*     */       
/* 203 */       throw new FileNotFoundException(this.path + " cannot be resolved to absolute file path");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReadableByteChannel readableChannel() throws IOException {
/*     */     try {
/* 214 */       return Files.newByteChannel(this.path, new OpenOption[] { StandardOpenOption.READ });
/*     */     }
/* 216 */     catch (NoSuchFileException ex) {
/* 217 */       throw new FileNotFoundException(ex.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WritableByteChannel writableChannel() throws IOException {
/* 227 */     return Files.newByteChannel(this.path, new OpenOption[] { StandardOpenOption.WRITE });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long contentLength() throws IOException {
/* 235 */     return Files.size(this.path);
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
/* 246 */     return Files.getLastModifiedTime(this.path, new java.nio.file.LinkOption[0]).toMillis();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource createRelative(String relativePath) throws IOException {
/* 256 */     return new PathResource(this.path.resolve(relativePath));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFilename() {
/* 265 */     return this.path.getFileName().toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 270 */     return "path [" + this.path.toAbsolutePath() + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 279 */     return (this == other || (other instanceof PathResource && this.path
/* 280 */       .equals(((PathResource)other).path)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 288 */     return this.path.hashCode();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/io/PathResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */