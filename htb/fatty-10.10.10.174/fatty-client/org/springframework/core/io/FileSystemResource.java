/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.NoSuchFileException;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardOpenOption;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileSystemResource
/*     */   extends AbstractResource
/*     */   implements WritableResource
/*     */ {
/*     */   private final String path;
/*     */   @Nullable
/*     */   private final File file;
/*     */   private final Path filePath;
/*     */   
/*     */   public FileSystemResource(String path) {
/*  79 */     Assert.notNull(path, "Path must not be null");
/*  80 */     this.path = StringUtils.cleanPath(path);
/*  81 */     this.file = new File(path);
/*  82 */     this.filePath = this.file.toPath();
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
/*     */   public FileSystemResource(File file) {
/*  99 */     Assert.notNull(file, "File must not be null");
/* 100 */     this.path = StringUtils.cleanPath(file.getPath());
/* 101 */     this.file = file;
/* 102 */     this.filePath = file.toPath();
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
/*     */   public FileSystemResource(Path filePath) {
/* 116 */     Assert.notNull(filePath, "Path must not be null");
/* 117 */     this.path = StringUtils.cleanPath(filePath.toString());
/* 118 */     this.file = null;
/* 119 */     this.filePath = filePath;
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
/*     */   public FileSystemResource(FileSystem fileSystem, String path) {
/* 133 */     Assert.notNull(fileSystem, "FileSystem must not be null");
/* 134 */     Assert.notNull(path, "Path must not be null");
/* 135 */     this.path = StringUtils.cleanPath(path);
/* 136 */     this.file = null;
/* 137 */     this.filePath = fileSystem.getPath(this.path, new String[0]).normalize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getPath() {
/* 145 */     return this.path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean exists() {
/* 154 */     return (this.file != null) ? this.file.exists() : Files.exists(this.filePath, new java.nio.file.LinkOption[0]);
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
/* 165 */     return (this.file != null) ? ((this.file.canRead() && !this.file.isDirectory())) : ((
/* 166 */       Files.isReadable(this.filePath) && !Files.isDirectory(this.filePath, new java.nio.file.LinkOption[0])));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() throws IOException {
/*     */     try {
/* 176 */       return Files.newInputStream(this.filePath, new OpenOption[0]);
/*     */     }
/* 178 */     catch (NoSuchFileException ex) {
/* 179 */       throw new FileNotFoundException(ex.getMessage());
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
/*     */   public boolean isWritable() {
/* 191 */     return (this.file != null) ? ((this.file.canWrite() && !this.file.isDirectory())) : ((
/* 192 */       Files.isWritable(this.filePath) && !Files.isDirectory(this.filePath, new java.nio.file.LinkOption[0])));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OutputStream getOutputStream() throws IOException {
/* 201 */     return Files.newOutputStream(this.filePath, new OpenOption[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getURL() throws IOException {
/* 210 */     return (this.file != null) ? this.file.toURI().toURL() : this.filePath.toUri().toURL();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getURI() throws IOException {
/* 219 */     return (this.file != null) ? this.file.toURI() : this.filePath.toUri();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFile() {
/* 227 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() {
/* 235 */     return (this.file != null) ? this.file : this.filePath.toFile();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReadableByteChannel readableChannel() throws IOException {
/*     */     try {
/* 245 */       return FileChannel.open(this.filePath, new OpenOption[] { StandardOpenOption.READ });
/*     */     }
/* 247 */     catch (NoSuchFileException ex) {
/* 248 */       throw new FileNotFoundException(ex.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WritableByteChannel writableChannel() throws IOException {
/* 258 */     return FileChannel.open(this.filePath, new OpenOption[] { StandardOpenOption.WRITE });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long contentLength() throws IOException {
/* 266 */     if (this.file != null) {
/* 267 */       long length = this.file.length();
/* 268 */       if (length == 0L && !this.file.exists()) {
/* 269 */         throw new FileNotFoundException(getDescription() + " cannot be resolved in the file system for checking its content length");
/*     */       }
/*     */       
/* 272 */       return length;
/*     */     } 
/*     */     
/*     */     try {
/* 276 */       return Files.size(this.filePath);
/*     */     }
/* 278 */     catch (NoSuchFileException ex) {
/* 279 */       throw new FileNotFoundException(ex.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long lastModified() throws IOException {
/* 289 */     if (this.file != null) {
/* 290 */       return super.lastModified();
/*     */     }
/*     */     
/*     */     try {
/* 294 */       return Files.getLastModifiedTime(this.filePath, new java.nio.file.LinkOption[0]).toMillis();
/*     */     }
/* 296 */     catch (NoSuchFileException ex) {
/* 297 */       throw new FileNotFoundException(ex.getMessage());
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
/*     */   public Resource createRelative(String relativePath) {
/* 309 */     String pathToUse = StringUtils.applyRelativePath(this.path, relativePath);
/* 310 */     return (this.file != null) ? new FileSystemResource(pathToUse) : new FileSystemResource(this.filePath
/* 311 */         .getFileSystem(), pathToUse);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFilename() {
/* 320 */     return (this.file != null) ? this.file.getName() : this.filePath.getFileName().toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 330 */     return "file [" + ((this.file != null) ? this.file.getAbsolutePath() : (String)this.filePath.toAbsolutePath()) + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 339 */     return (this == other || (other instanceof FileSystemResource && this.path
/* 340 */       .equals(((FileSystemResource)other).path)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 348 */     return this.path.hashCode();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/io/FileSystemResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */