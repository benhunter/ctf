/*     */ package org.springframework.beans.propertyeditors;
/*     */ 
/*     */ import java.beans.PropertyEditorSupport;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import org.springframework.core.io.ClassPathResource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class URIEditor
/*     */   extends PropertyEditorSupport
/*     */ {
/*     */   @Nullable
/*     */   private final ClassLoader classLoader;
/*     */   private final boolean encode;
/*     */   
/*     */   public URIEditor() {
/*  65 */     this(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIEditor(boolean encode) {
/*  75 */     this.classLoader = null;
/*  76 */     this.encode = encode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URIEditor(@Nullable ClassLoader classLoader) {
/*  86 */     this(classLoader, true);
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
/*     */   public URIEditor(@Nullable ClassLoader classLoader, boolean encode) {
/*  98 */     this.classLoader = (classLoader != null) ? classLoader : ClassUtils.getDefaultClassLoader();
/*  99 */     this.encode = encode;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAsText(String text) throws IllegalArgumentException {
/* 105 */     if (StringUtils.hasText(text)) {
/* 106 */       String uri = text.trim();
/* 107 */       if (this.classLoader != null && uri.startsWith("classpath:")) {
/*     */         
/* 109 */         ClassPathResource resource = new ClassPathResource(uri.substring("classpath:".length()), this.classLoader);
/*     */         try {
/* 111 */           setValue(resource.getURI());
/*     */         }
/* 113 */         catch (IOException ex) {
/* 114 */           throw new IllegalArgumentException("Could not retrieve URI for " + resource + ": " + ex.getMessage());
/*     */         } 
/*     */       } else {
/*     */         
/*     */         try {
/* 119 */           setValue(createURI(uri));
/*     */         }
/* 121 */         catch (URISyntaxException ex) {
/* 122 */           throw new IllegalArgumentException("Invalid URI syntax: " + ex);
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 127 */       setValue(null);
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
/*     */   protected URI createURI(String value) throws URISyntaxException {
/* 139 */     int colonIndex = value.indexOf(':');
/* 140 */     if (this.encode && colonIndex != -1) {
/* 141 */       int fragmentIndex = value.indexOf('#', colonIndex + 1);
/* 142 */       String scheme = value.substring(0, colonIndex);
/* 143 */       String ssp = value.substring(colonIndex + 1, (fragmentIndex > 0) ? fragmentIndex : value.length());
/* 144 */       String fragment = (fragmentIndex > 0) ? value.substring(fragmentIndex + 1) : null;
/* 145 */       return new URI(scheme, ssp, fragment);
/*     */     } 
/*     */ 
/*     */     
/* 149 */     return new URI(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAsText() {
/* 156 */     URI value = (URI)getValue();
/* 157 */     return (value != null) ? value.toString() : "";
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/propertyeditors/URIEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */