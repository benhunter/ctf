/*     */ package org.springframework.http.server;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.List;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ class DefaultRequestPath
/*     */   implements RequestPath
/*     */ {
/*     */   private final PathContainer fullPath;
/*     */   private final PathContainer contextPath;
/*     */   private final PathContainer pathWithinApplication;
/*     */   
/*     */   DefaultRequestPath(URI uri, @Nullable String contextPath) {
/*  41 */     this.fullPath = PathContainer.parsePath(uri.getRawPath());
/*  42 */     this.contextPath = initContextPath(this.fullPath, contextPath);
/*  43 */     this.pathWithinApplication = extractPathWithinApplication(this.fullPath, this.contextPath);
/*     */   }
/*     */   
/*     */   private DefaultRequestPath(RequestPath requestPath, String contextPath) {
/*  47 */     this.fullPath = requestPath;
/*  48 */     this.contextPath = initContextPath(this.fullPath, contextPath);
/*  49 */     this.pathWithinApplication = extractPathWithinApplication(this.fullPath, this.contextPath);
/*     */   }
/*     */   
/*     */   private static PathContainer initContextPath(PathContainer path, @Nullable String contextPath) {
/*  53 */     if (!StringUtils.hasText(contextPath) || "/".equals(contextPath)) {
/*  54 */       return PathContainer.parsePath("");
/*     */     }
/*     */     
/*  57 */     validateContextPath(path.value(), contextPath);
/*     */     
/*  59 */     int length = contextPath.length();
/*  60 */     int counter = 0;
/*     */     
/*  62 */     for (int i = 0; i < path.elements().size(); i++) {
/*  63 */       PathContainer.Element element = path.elements().get(i);
/*  64 */       counter += element.value().length();
/*  65 */       if (length == counter) {
/*  66 */         return path.subPath(0, i + 1);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  71 */     throw new IllegalStateException("Failed to initialize contextPath '" + contextPath + "' for requestPath '" + path
/*  72 */         .value() + "'");
/*     */   }
/*     */   
/*     */   private static void validateContextPath(String fullPath, String contextPath) {
/*  76 */     int length = contextPath.length();
/*  77 */     if (contextPath.charAt(0) != '/' || contextPath.charAt(length - 1) == '/') {
/*  78 */       throw new IllegalArgumentException("Invalid contextPath: '" + contextPath + "': must start with '/' and not end with '/'");
/*     */     }
/*     */     
/*  81 */     if (!fullPath.startsWith(contextPath)) {
/*  82 */       throw new IllegalArgumentException("Invalid contextPath '" + contextPath + "': must match the start of requestPath: '" + fullPath + "'");
/*     */     }
/*     */     
/*  85 */     if (fullPath.length() > length && fullPath.charAt(length) != '/') {
/*  86 */       throw new IllegalArgumentException("Invalid contextPath '" + contextPath + "': must match to full path segments for requestPath: '" + fullPath + "'");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static PathContainer extractPathWithinApplication(PathContainer fullPath, PathContainer contextPath) {
/*  92 */     return fullPath.subPath(contextPath.elements().size());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String value() {
/* 100 */     return this.fullPath.value();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<PathContainer.Element> elements() {
/* 105 */     return this.fullPath.elements();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathContainer contextPath() {
/* 113 */     return this.contextPath;
/*     */   }
/*     */ 
/*     */   
/*     */   public PathContainer pathWithinApplication() {
/* 118 */     return this.pathWithinApplication;
/*     */   }
/*     */ 
/*     */   
/*     */   public RequestPath modifyContextPath(String contextPath) {
/* 123 */     return new DefaultRequestPath(this, contextPath);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 129 */     if (this == other) {
/* 130 */       return true;
/*     */     }
/* 132 */     if (other == null || getClass() != other.getClass()) {
/* 133 */       return false;
/*     */     }
/* 135 */     DefaultRequestPath otherPath = (DefaultRequestPath)other;
/* 136 */     return (this.fullPath.equals(otherPath.fullPath) && this.contextPath
/* 137 */       .equals(otherPath.contextPath) && this.pathWithinApplication
/* 138 */       .equals(otherPath.pathWithinApplication));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 143 */     int result = this.fullPath.hashCode();
/* 144 */     result = 31 * result + this.contextPath.hashCode();
/* 145 */     result = 31 * result + this.pathWithinApplication.hashCode();
/* 146 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 151 */     return this.fullPath.toString();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/DefaultRequestPath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */