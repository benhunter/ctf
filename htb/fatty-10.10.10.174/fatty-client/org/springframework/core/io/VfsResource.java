/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import org.springframework.core.NestedIOException;
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
/*     */ public class VfsResource
/*     */   extends AbstractResource
/*     */ {
/*     */   private final Object resource;
/*     */   
/*     */   public VfsResource(Object resource) {
/*  53 */     Assert.notNull(resource, "VirtualFile must not be null");
/*  54 */     this.resource = resource;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() throws IOException {
/*  60 */     return VfsUtils.getInputStream(this.resource);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean exists() {
/*  65 */     return VfsUtils.exists(this.resource);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isReadable() {
/*  70 */     return VfsUtils.isReadable(this.resource);
/*     */   }
/*     */ 
/*     */   
/*     */   public URL getURL() throws IOException {
/*     */     try {
/*  76 */       return VfsUtils.getURL(this.resource);
/*     */     }
/*  78 */     catch (Exception ex) {
/*  79 */       throw new NestedIOException("Failed to obtain URL for file " + this.resource, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public URI getURI() throws IOException {
/*     */     try {
/*  86 */       return VfsUtils.getURI(this.resource);
/*     */     }
/*  88 */     catch (Exception ex) {
/*  89 */       throw new NestedIOException("Failed to obtain URI for " + this.resource, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public File getFile() throws IOException {
/*  95 */     return VfsUtils.getFile(this.resource);
/*     */   }
/*     */ 
/*     */   
/*     */   public long contentLength() throws IOException {
/* 100 */     return VfsUtils.getSize(this.resource);
/*     */   }
/*     */ 
/*     */   
/*     */   public long lastModified() throws IOException {
/* 105 */     return VfsUtils.getLastModified(this.resource);
/*     */   }
/*     */ 
/*     */   
/*     */   public Resource createRelative(String relativePath) throws IOException {
/* 110 */     if (!relativePath.startsWith(".") && relativePath.contains("/")) {
/*     */       try {
/* 112 */         return new VfsResource(VfsUtils.getChild(this.resource, relativePath));
/*     */       }
/* 114 */       catch (IOException iOException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 119 */     return new VfsResource(VfsUtils.getRelative(new URL(getURL(), relativePath)));
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFilename() {
/* 124 */     return VfsUtils.getName(this.resource);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 129 */     return "VFS resource [" + this.resource + "]";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 134 */     return (this == other || (other instanceof VfsResource && this.resource
/* 135 */       .equals(((VfsResource)other).resource)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 140 */     return this.resource.hashCode();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/io/VfsResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */