/*     */ package org.springframework.web.multipart.commons;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Serializable;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import org.apache.commons.fileupload.FileItem;
/*     */ import org.apache.commons.fileupload.FileUploadException;
/*     */ import org.apache.commons.fileupload.disk.DiskFileItem;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.log.LogFormatUtils;
/*     */ import org.springframework.util.FileCopyUtils;
/*     */ import org.springframework.util.StreamUtils;
/*     */ import org.springframework.web.multipart.MultipartFile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CommonsMultipartFile
/*     */   implements MultipartFile, Serializable
/*     */ {
/*  48 */   protected static final Log logger = LogFactory.getLog(CommonsMultipartFile.class);
/*     */ 
/*     */   
/*     */   private final FileItem fileItem;
/*     */ 
/*     */   
/*     */   private final long size;
/*     */ 
/*     */   
/*     */   private boolean preserveFilename = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public CommonsMultipartFile(FileItem fileItem) {
/*  62 */     this.fileItem = fileItem;
/*  63 */     this.size = this.fileItem.getSize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final FileItem getFileItem() {
/*  72 */     return this.fileItem;
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
/*     */   public void setPreserveFilename(boolean preserveFilename) {
/*  86 */     this.preserveFilename = preserveFilename;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  92 */     return this.fileItem.getFieldName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getOriginalFilename() {
/*  97 */     String filename = this.fileItem.getName();
/*  98 */     if (filename == null)
/*     */     {
/* 100 */       return "";
/*     */     }
/* 102 */     if (this.preserveFilename)
/*     */     {
/* 104 */       return filename;
/*     */     }
/*     */ 
/*     */     
/* 108 */     int unixSep = filename.lastIndexOf('/');
/*     */     
/* 110 */     int winSep = filename.lastIndexOf('\\');
/*     */     
/* 112 */     int pos = (winSep > unixSep) ? winSep : unixSep;
/* 113 */     if (pos != -1)
/*     */     {
/* 115 */       return filename.substring(pos + 1);
/*     */     }
/*     */ 
/*     */     
/* 119 */     return filename;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContentType() {
/* 125 */     return this.fileItem.getContentType();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 130 */     return (this.size == 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getSize() {
/* 135 */     return this.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getBytes() {
/* 140 */     if (!isAvailable()) {
/* 141 */       throw new IllegalStateException("File has been moved - cannot be read again");
/*     */     }
/* 143 */     byte[] bytes = this.fileItem.get();
/* 144 */     return (bytes != null) ? bytes : new byte[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() throws IOException {
/* 149 */     if (!isAvailable()) {
/* 150 */       throw new IllegalStateException("File has been moved - cannot be read again");
/*     */     }
/* 152 */     InputStream inputStream = this.fileItem.getInputStream();
/* 153 */     return (inputStream != null) ? inputStream : StreamUtils.emptyInput();
/*     */   }
/*     */ 
/*     */   
/*     */   public void transferTo(File dest) throws IOException, IllegalStateException {
/* 158 */     if (!isAvailable()) {
/* 159 */       throw new IllegalStateException("File has already been moved - cannot be transferred again");
/*     */     }
/*     */     
/* 162 */     if (dest.exists() && !dest.delete()) {
/* 163 */       throw new IOException("Destination file [" + dest
/* 164 */           .getAbsolutePath() + "] already exists and could not be deleted");
/*     */     }
/*     */     
/*     */     try {
/* 168 */       this.fileItem.write(dest);
/* 169 */       LogFormatUtils.traceDebug(logger, traceOn -> {
/*     */             String action = "transferred";
/*     */ 
/*     */             
/*     */             if (!this.fileItem.isInMemory()) {
/*     */               action = isAvailable() ? "copied" : "moved";
/*     */             }
/*     */             
/*     */             return "Part '" + getName() + "',  filename '" + getOriginalFilename() + "'" + (traceOn.booleanValue() ? (", stored " + getStorageDescription()) : "") + ": " + action + " to [" + dest.getAbsolutePath() + "]";
/*     */           });
/* 179 */     } catch (FileUploadException ex) {
/* 180 */       throw new IllegalStateException(ex.getMessage(), ex);
/*     */     }
/* 182 */     catch (IllegalStateException|IOException ex) {
/*     */ 
/*     */       
/* 185 */       throw ex;
/*     */     }
/* 187 */     catch (Exception ex) {
/* 188 */       throw new IOException("File transfer failed", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void transferTo(Path dest) throws IOException, IllegalStateException {
/* 194 */     if (!isAvailable()) {
/* 195 */       throw new IllegalStateException("File has already been moved - cannot be transferred again");
/*     */     }
/*     */     
/* 198 */     FileCopyUtils.copy(this.fileItem.getInputStream(), Files.newOutputStream(dest, new java.nio.file.OpenOption[0]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isAvailable() {
/* 207 */     if (this.fileItem.isInMemory()) {
/* 208 */       return true;
/*     */     }
/*     */     
/* 211 */     if (this.fileItem instanceof DiskFileItem) {
/* 212 */       return ((DiskFileItem)this.fileItem).getStoreLocation().exists();
/*     */     }
/*     */     
/* 215 */     return (this.fileItem.getSize() == this.size);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getStorageDescription() {
/* 224 */     if (this.fileItem.isInMemory()) {
/* 225 */       return "in memory";
/*     */     }
/* 227 */     if (this.fileItem instanceof DiskFileItem) {
/* 228 */       return "at [" + ((DiskFileItem)this.fileItem).getStoreLocation().getAbsolutePath() + "]";
/*     */     }
/*     */     
/* 231 */     return "on disk";
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/multipart/commons/CommonsMultipartFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */