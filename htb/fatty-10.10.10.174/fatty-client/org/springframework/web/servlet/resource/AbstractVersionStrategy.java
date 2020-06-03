/*     */ package org.springframework.web.servlet.resource;
/*     */ 
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
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
/*     */ public abstract class AbstractVersionStrategy
/*     */   implements VersionStrategy
/*     */ {
/*  47 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private final VersionPathStrategy pathStrategy;
/*     */ 
/*     */   
/*     */   protected AbstractVersionStrategy(VersionPathStrategy pathStrategy) {
/*  53 */     Assert.notNull(pathStrategy, "VersionPathStrategy is required");
/*  54 */     this.pathStrategy = pathStrategy;
/*     */   }
/*     */ 
/*     */   
/*     */   public VersionPathStrategy getVersionPathStrategy() {
/*  59 */     return this.pathStrategy;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String extractVersion(String requestPath) {
/*  66 */     return this.pathStrategy.extractVersion(requestPath);
/*     */   }
/*     */ 
/*     */   
/*     */   public String removeVersion(String requestPath, String version) {
/*  71 */     return this.pathStrategy.removeVersion(requestPath, version);
/*     */   }
/*     */ 
/*     */   
/*     */   public String addVersion(String requestPath, String version) {
/*  76 */     return this.pathStrategy.addVersion(requestPath, version);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class PrefixVersionPathStrategy
/*     */     implements VersionPathStrategy
/*     */   {
/*     */     private final String prefix;
/*     */ 
/*     */ 
/*     */     
/*     */     public PrefixVersionPathStrategy(String version) {
/*  89 */       Assert.hasText(version, "Version must not be empty");
/*  90 */       this.prefix = version;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String extractVersion(String requestPath) {
/*  96 */       return requestPath.startsWith(this.prefix) ? this.prefix : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public String removeVersion(String requestPath, String version) {
/* 101 */       return requestPath.substring(this.prefix.length());
/*     */     }
/*     */ 
/*     */     
/*     */     public String addVersion(String path, String version) {
/* 106 */       if (path.startsWith(".")) {
/* 107 */         return path;
/*     */       }
/*     */       
/* 110 */       return (this.prefix.endsWith("/") || path.startsWith("/")) ? (this.prefix + path) : (this.prefix + '/' + path);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class FileNameVersionPathStrategy
/*     */     implements VersionPathStrategy
/*     */   {
/* 123 */     private static final Pattern pattern = Pattern.compile("-(\\S*)\\.");
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String extractVersion(String requestPath) {
/* 128 */       Matcher matcher = pattern.matcher(requestPath);
/* 129 */       if (matcher.find()) {
/* 130 */         String match = matcher.group(1);
/* 131 */         return match.contains("-") ? match.substring(match.lastIndexOf('-') + 1) : match;
/*     */       } 
/*     */       
/* 134 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String removeVersion(String requestPath, String version) {
/* 140 */       return StringUtils.delete(requestPath, "-" + version);
/*     */     }
/*     */ 
/*     */     
/*     */     public String addVersion(String requestPath, String version) {
/* 145 */       String baseFilename = StringUtils.stripFilenameExtension(requestPath);
/* 146 */       String extension = StringUtils.getFilenameExtension(requestPath);
/* 147 */       return baseFilename + '-' + version + '.' + extension;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/resource/AbstractVersionStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */