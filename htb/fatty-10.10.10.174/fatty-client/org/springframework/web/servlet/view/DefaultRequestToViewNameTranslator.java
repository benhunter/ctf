/*     */ package org.springframework.web.servlet.view;
/*     */ 
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.servlet.RequestToViewNameTranslator;
/*     */ import org.springframework.web.util.UrlPathHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultRequestToViewNameTranslator
/*     */   implements RequestToViewNameTranslator
/*     */ {
/*     */   private static final String SLASH = "/";
/*  62 */   private String prefix = "";
/*     */   
/*  64 */   private String suffix = "";
/*     */   
/*  66 */   private String separator = "/";
/*     */   
/*     */   private boolean stripLeadingSlash = true;
/*     */   
/*     */   private boolean stripTrailingSlash = true;
/*     */   
/*     */   private boolean stripExtension = true;
/*     */   
/*  74 */   private UrlPathHelper urlPathHelper = new UrlPathHelper();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrefix(@Nullable String prefix) {
/*  82 */     this.prefix = (prefix != null) ? prefix : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSuffix(@Nullable String suffix) {
/*  90 */     this.suffix = (suffix != null) ? suffix : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSeparator(String separator) {
/*  99 */     this.separator = separator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStripLeadingSlash(boolean stripLeadingSlash) {
/* 107 */     this.stripLeadingSlash = stripLeadingSlash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStripTrailingSlash(boolean stripTrailingSlash) {
/* 115 */     this.stripTrailingSlash = stripTrailingSlash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStripExtension(boolean stripExtension) {
/* 123 */     this.stripExtension = stripExtension;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAlwaysUseFullPath(boolean alwaysUseFullPath) {
/* 131 */     this.urlPathHelper.setAlwaysUseFullPath(alwaysUseFullPath);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUrlDecode(boolean urlDecode) {
/* 139 */     this.urlPathHelper.setUrlDecode(urlDecode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemoveSemicolonContent(boolean removeSemicolonContent) {
/* 147 */     this.urlPathHelper.setRemoveSemicolonContent(removeSemicolonContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
/* 157 */     Assert.notNull(urlPathHelper, "UrlPathHelper must not be null");
/* 158 */     this.urlPathHelper = urlPathHelper;
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
/*     */   public String getViewName(HttpServletRequest request) {
/* 170 */     String lookupPath = this.urlPathHelper.getLookupPathForRequest(request);
/* 171 */     return this.prefix + transformPath(lookupPath) + this.suffix;
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
/*     */   @Nullable
/*     */   protected String transformPath(String lookupPath) {
/* 184 */     String path = lookupPath;
/* 185 */     if (this.stripLeadingSlash && path.startsWith("/")) {
/* 186 */       path = path.substring(1);
/*     */     }
/* 188 */     if (this.stripTrailingSlash && path.endsWith("/")) {
/* 189 */       path = path.substring(0, path.length() - 1);
/*     */     }
/* 191 */     if (this.stripExtension) {
/* 192 */       path = StringUtils.stripFilenameExtension(path);
/*     */     }
/* 194 */     if (!"/".equals(this.separator)) {
/* 195 */       path = StringUtils.replace(path, "/", this.separator);
/*     */     }
/* 197 */     return path;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/DefaultRequestToViewNameTranslator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */