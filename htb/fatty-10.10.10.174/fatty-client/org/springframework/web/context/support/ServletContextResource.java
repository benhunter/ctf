/*     */ package org.springframework.web.context.support;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.springframework.core.io.AbstractFileResolvingResource;
/*     */ import org.springframework.core.io.ContextResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ResourceUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.util.WebUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServletContextResource
/*     */   extends AbstractFileResolvingResource
/*     */   implements ContextResource
/*     */ {
/*     */   private final ServletContext servletContext;
/*     */   private final String path;
/*     */   
/*     */   public ServletContextResource(ServletContext servletContext, String path) {
/*  69 */     Assert.notNull(servletContext, "Cannot resolve ServletContextResource without ServletContext");
/*  70 */     this.servletContext = servletContext;
/*     */ 
/*     */     
/*  73 */     Assert.notNull(path, "Path is required");
/*  74 */     String pathToUse = StringUtils.cleanPath(path);
/*  75 */     if (!pathToUse.startsWith("/")) {
/*  76 */       pathToUse = "/" + pathToUse;
/*     */     }
/*  78 */     this.path = pathToUse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServletContext getServletContext() {
/*  86 */     return this.servletContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getPath() {
/*  93 */     return this.path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean exists() {
/*     */     try {
/* 103 */       URL url = this.servletContext.getResource(this.path);
/* 104 */       return (url != null);
/*     */     }
/* 106 */     catch (MalformedURLException ex) {
/* 107 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReadable() {
/* 118 */     InputStream is = this.servletContext.getResourceAsStream(this.path);
/* 119 */     if (is != null) {
/*     */       try {
/* 121 */         is.close();
/*     */       }
/* 123 */       catch (IOException iOException) {}
/*     */ 
/*     */       
/* 126 */       return true;
/*     */     } 
/*     */     
/* 129 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFile() {
/*     */     try {
/* 136 */       URL url = this.servletContext.getResource(this.path);
/* 137 */       if (url != null && ResourceUtils.isFileURL(url)) {
/* 138 */         return true;
/*     */       }
/*     */       
/* 141 */       return (this.servletContext.getRealPath(this.path) != null);
/*     */     
/*     */     }
/* 144 */     catch (MalformedURLException ex) {
/* 145 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() throws IOException {
/* 156 */     InputStream is = this.servletContext.getResourceAsStream(this.path);
/* 157 */     if (is == null) {
/* 158 */       throw new FileNotFoundException("Could not open " + getDescription());
/*     */     }
/* 160 */     return is;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getURL() throws IOException {
/* 170 */     URL url = this.servletContext.getResource(this.path);
/* 171 */     if (url == null) {
/* 172 */       throw new FileNotFoundException(
/* 173 */           getDescription() + " cannot be resolved to URL because it does not exist");
/*     */     }
/* 175 */     return url;
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
/*     */   public File getFile() throws IOException {
/* 187 */     URL url = this.servletContext.getResource(this.path);
/* 188 */     if (url != null && ResourceUtils.isFileURL(url))
/*     */     {
/* 190 */       return super.getFile();
/*     */     }
/*     */     
/* 193 */     String realPath = WebUtils.getRealPath(this.servletContext, this.path);
/* 194 */     return new File(realPath);
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
/* 205 */     String pathToUse = StringUtils.applyRelativePath(this.path, relativePath);
/* 206 */     return (Resource)new ServletContextResource(this.servletContext, pathToUse);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getFilename() {
/* 217 */     return StringUtils.getFilename(this.path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 226 */     return "ServletContext resource [" + this.path + "]";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPathWithinContext() {
/* 231 */     return this.path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 240 */     if (this == other) {
/* 241 */       return true;
/*     */     }
/* 243 */     if (!(other instanceof ServletContextResource)) {
/* 244 */       return false;
/*     */     }
/* 246 */     ServletContextResource otherRes = (ServletContextResource)other;
/* 247 */     return (this.servletContext.equals(otherRes.servletContext) && this.path.equals(otherRes.path));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 256 */     return this.path.hashCode();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/support/ServletContextResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */