/*     */ package org.springframework.beans.propertyeditors;
/*     */ 
/*     */ import java.beans.PropertyEditorSupport;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceEditor;
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
/*     */ 
/*     */ 
/*     */ public class PathEditor
/*     */   extends PropertyEditorSupport
/*     */ {
/*     */   private final ResourceEditor resourceEditor;
/*     */   
/*     */   public PathEditor() {
/*  62 */     this.resourceEditor = new ResourceEditor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathEditor(ResourceEditor resourceEditor) {
/*  70 */     Assert.notNull(resourceEditor, "ResourceEditor must not be null");
/*  71 */     this.resourceEditor = resourceEditor;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAsText(String text) throws IllegalArgumentException {
/*  77 */     boolean nioPathCandidate = !text.startsWith("classpath:");
/*  78 */     if (nioPathCandidate && !text.startsWith("/")) {
/*     */       try {
/*  80 */         URI uri = new URI(text);
/*  81 */         if (uri.getScheme() != null) {
/*  82 */           nioPathCandidate = false;
/*     */           
/*  84 */           setValue(Paths.get(uri).normalize());
/*     */           
/*     */           return;
/*     */         } 
/*  88 */       } catch (URISyntaxException|java.nio.file.FileSystemNotFoundException uRISyntaxException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  95 */     this.resourceEditor.setAsText(text);
/*  96 */     Resource resource = (Resource)this.resourceEditor.getValue();
/*  97 */     if (resource == null) {
/*  98 */       setValue(null);
/*     */     }
/* 100 */     else if (!resource.exists() && nioPathCandidate) {
/* 101 */       setValue(Paths.get(text, new String[0]).normalize());
/*     */     } else {
/*     */       
/*     */       try {
/* 105 */         setValue(resource.getFile().toPath());
/*     */       }
/* 107 */       catch (IOException ex) {
/* 108 */         throw new IllegalArgumentException("Failed to retrieve file for " + resource, ex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAsText() {
/* 115 */     Path value = (Path)getValue();
/* 116 */     return (value != null) ? value.toString() : "";
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/propertyeditors/PathEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */