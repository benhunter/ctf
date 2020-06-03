/*     */ package org.springframework.core.io;
/*     */ 
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ public class ClassPathResource
/*     */   extends AbstractFileResolvingResource
/*     */ {
/*     */   private final String path;
/*     */   @Nullable
/*     */   private ClassLoader classLoader;
/*     */   @Nullable
/*     */   private Class<?> clazz;
/*     */   
/*     */   public ClassPathResource(String path) {
/*  66 */     this(path, (ClassLoader)null);
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
/*     */   public ClassPathResource(String path, @Nullable ClassLoader classLoader) {
/*  79 */     Assert.notNull(path, "Path must not be null");
/*  80 */     String pathToUse = StringUtils.cleanPath(path);
/*  81 */     if (pathToUse.startsWith("/")) {
/*  82 */       pathToUse = pathToUse.substring(1);
/*     */     }
/*  84 */     this.path = pathToUse;
/*  85 */     this.classLoader = (classLoader != null) ? classLoader : ClassUtils.getDefaultClassLoader();
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
/*     */   public ClassPathResource(String path, @Nullable Class<?> clazz) {
/*  97 */     Assert.notNull(path, "Path must not be null");
/*  98 */     this.path = StringUtils.cleanPath(path);
/*  99 */     this.clazz = clazz;
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
/*     */   @Deprecated
/*     */   protected ClassPathResource(String path, @Nullable ClassLoader classLoader, @Nullable Class<?> clazz) {
/* 113 */     this.path = StringUtils.cleanPath(path);
/* 114 */     this.classLoader = classLoader;
/* 115 */     this.clazz = clazz;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getPath() {
/* 123 */     return this.path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final ClassLoader getClassLoader() {
/* 131 */     return (this.clazz != null) ? this.clazz.getClassLoader() : this.classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean exists() {
/* 142 */     return (resolveURL() != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected URL resolveURL() {
/* 151 */     if (this.clazz != null) {
/* 152 */       return this.clazz.getResource(this.path);
/*     */     }
/* 154 */     if (this.classLoader != null) {
/* 155 */       return this.classLoader.getResource(this.path);
/*     */     }
/*     */     
/* 158 */     return ClassLoader.getSystemResource(this.path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() throws IOException {
/*     */     InputStream is;
/* 170 */     if (this.clazz != null) {
/* 171 */       is = this.clazz.getResourceAsStream(this.path);
/*     */     }
/* 173 */     else if (this.classLoader != null) {
/* 174 */       is = this.classLoader.getResourceAsStream(this.path);
/*     */     } else {
/*     */       
/* 177 */       is = ClassLoader.getSystemResourceAsStream(this.path);
/*     */     } 
/* 179 */     if (is == null) {
/* 180 */       throw new FileNotFoundException(getDescription() + " cannot be opened because it does not exist");
/*     */     }
/* 182 */     return is;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getURL() throws IOException {
/* 193 */     URL url = resolveURL();
/* 194 */     if (url == null) {
/* 195 */       throw new FileNotFoundException(getDescription() + " cannot be resolved to URL because it does not exist");
/*     */     }
/* 197 */     return url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource createRelative(String relativePath) {
/* 207 */     String pathToUse = StringUtils.applyRelativePath(this.path, relativePath);
/* 208 */     return (this.clazz != null) ? new ClassPathResource(pathToUse, this.clazz) : new ClassPathResource(pathToUse, this.classLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getFilename() {
/* 220 */     return StringUtils.getFilename(this.path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 228 */     StringBuilder builder = new StringBuilder("class path resource [");
/* 229 */     String pathToUse = this.path;
/* 230 */     if (this.clazz != null && !pathToUse.startsWith("/")) {
/* 231 */       builder.append(ClassUtils.classPackageAsResourcePath(this.clazz));
/* 232 */       builder.append('/');
/*     */     } 
/* 234 */     if (pathToUse.startsWith("/")) {
/* 235 */       pathToUse = pathToUse.substring(1);
/*     */     }
/* 237 */     builder.append(pathToUse);
/* 238 */     builder.append(']');
/* 239 */     return builder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 248 */     if (this == other) {
/* 249 */       return true;
/*     */     }
/* 251 */     if (!(other instanceof ClassPathResource)) {
/* 252 */       return false;
/*     */     }
/* 254 */     ClassPathResource otherRes = (ClassPathResource)other;
/* 255 */     return (this.path.equals(otherRes.path) && 
/* 256 */       ObjectUtils.nullSafeEquals(this.classLoader, otherRes.classLoader) && 
/* 257 */       ObjectUtils.nullSafeEquals(this.clazz, otherRes.clazz));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 266 */     return this.path.hashCode();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/io/ClassPathResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */