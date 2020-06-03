/*     */ package org.springframework.scripting.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.support.EncodedResource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.scripting.ScriptSource;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.FileCopyUtils;
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
/*     */ public class ResourceScriptSource
/*     */   implements ScriptSource
/*     */ {
/*  51 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private EncodedResource resource;
/*     */   
/*  55 */   private long lastModified = -1L;
/*     */   
/*  57 */   private final Object lastModifiedMonitor = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceScriptSource(EncodedResource resource) {
/*  65 */     Assert.notNull(resource, "Resource must not be null");
/*  66 */     this.resource = resource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceScriptSource(Resource resource) {
/*  74 */     Assert.notNull(resource, "Resource must not be null");
/*  75 */     this.resource = new EncodedResource(resource, "UTF-8");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Resource getResource() {
/*  84 */     return this.resource.getResource();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncoding(@Nullable String encoding) {
/*  93 */     this.resource = new EncodedResource(this.resource.getResource(), encoding);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getScriptAsString() throws IOException {
/*  99 */     synchronized (this.lastModifiedMonitor) {
/* 100 */       this.lastModified = retrieveLastModifiedTime();
/*     */     } 
/* 102 */     Reader reader = this.resource.getReader();
/* 103 */     return FileCopyUtils.copyToString(reader);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isModified() {
/* 108 */     synchronized (this.lastModifiedMonitor) {
/* 109 */       return (this.lastModified < 0L || retrieveLastModifiedTime() > this.lastModified);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected long retrieveLastModifiedTime() {
/*     */     try {
/* 119 */       return getResource().lastModified();
/*     */     }
/* 121 */     catch (IOException ex) {
/* 122 */       if (this.logger.isDebugEnabled()) {
/* 123 */         this.logger.debug(getResource() + " could not be resolved in the file system - current timestamp not available for script modification check", ex);
/*     */       }
/*     */       
/* 126 */       return 0L;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String suggestedClassName() {
/* 133 */     String filename = getResource().getFilename();
/* 134 */     return (filename != null) ? StringUtils.stripFilenameExtension(filename) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 139 */     return this.resource.toString();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scripting/support/ResourceScriptSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */