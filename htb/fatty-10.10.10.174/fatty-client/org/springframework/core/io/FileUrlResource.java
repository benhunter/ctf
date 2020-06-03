/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.StandardOpenOption;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileUrlResource
/*     */   extends UrlResource
/*     */   implements WritableResource
/*     */ {
/*     */   @Nullable
/*     */   private volatile File file;
/*     */   
/*     */   public FileUrlResource(URL url) {
/*  61 */     super(url);
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
/*     */   public FileUrlResource(String location) throws MalformedURLException {
/*  74 */     super("file", location);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() throws IOException {
/*  80 */     File file = this.file;
/*  81 */     if (file != null) {
/*  82 */       return file;
/*     */     }
/*  84 */     file = super.getFile();
/*  85 */     this.file = file;
/*  86 */     return file;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable() {
/*     */     try {
/*  92 */       URL url = getURL();
/*  93 */       if (ResourceUtils.isFileURL(url)) {
/*     */         
/*  95 */         File file = getFile();
/*  96 */         return (file.canWrite() && !file.isDirectory());
/*     */       } 
/*     */       
/*  99 */       return true;
/*     */     
/*     */     }
/* 102 */     catch (IOException ex) {
/* 103 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public OutputStream getOutputStream() throws IOException {
/* 109 */     return Files.newOutputStream(getFile().toPath(), new OpenOption[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   public WritableByteChannel writableChannel() throws IOException {
/* 114 */     return FileChannel.open(getFile().toPath(), new OpenOption[] { StandardOpenOption.WRITE });
/*     */   }
/*     */ 
/*     */   
/*     */   public Resource createRelative(String relativePath) throws MalformedURLException {
/* 119 */     if (relativePath.startsWith("/")) {
/* 120 */       relativePath = relativePath.substring(1);
/*     */     }
/* 122 */     return new FileUrlResource(new URL(getURL(), relativePath));
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/io/FileUrlResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */