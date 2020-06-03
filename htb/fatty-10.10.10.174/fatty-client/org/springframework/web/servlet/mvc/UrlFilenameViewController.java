/*     */ package org.springframework.web.servlet.mvc;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.servlet.HandlerMapping;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UrlFilenameViewController
/*     */   extends AbstractUrlViewController
/*     */ {
/*  52 */   private String prefix = "";
/*     */   
/*  54 */   private String suffix = "";
/*     */ 
/*     */   
/*  57 */   private final Map<String, String> viewNameCache = new ConcurrentHashMap<>(256);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrefix(@Nullable String prefix) {
/*  65 */     this.prefix = (prefix != null) ? prefix : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getPrefix() {
/*  72 */     return this.prefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSuffix(@Nullable String suffix) {
/*  80 */     this.suffix = (suffix != null) ? suffix : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getSuffix() {
/*  87 */     return this.suffix;
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
/*     */   protected String getViewNameForRequest(HttpServletRequest request) {
/* 100 */     String uri = extractOperableUrl(request);
/* 101 */     return getViewNameForUrlPath(uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String extractOperableUrl(HttpServletRequest request) {
/* 111 */     String urlPath = (String)request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
/* 112 */     if (!StringUtils.hasText(urlPath)) {
/* 113 */       urlPath = getUrlPathHelper().getLookupPathForRequest(request);
/*     */     }
/* 115 */     return urlPath;
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
/*     */   protected String getViewNameForUrlPath(String uri) {
/* 127 */     String viewName = this.viewNameCache.get(uri);
/* 128 */     if (viewName == null) {
/* 129 */       viewName = extractViewNameFromUrlPath(uri);
/* 130 */       viewName = postProcessViewName(viewName);
/* 131 */       this.viewNameCache.put(uri, viewName);
/*     */     } 
/* 133 */     return viewName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String extractViewNameFromUrlPath(String uri) {
/* 142 */     int start = (uri.charAt(0) == '/') ? 1 : 0;
/* 143 */     int lastIndex = uri.lastIndexOf('.');
/* 144 */     int end = (lastIndex < 0) ? uri.length() : lastIndex;
/* 145 */     return uri.substring(start, end);
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
/*     */   
/*     */   protected String postProcessViewName(String viewName) {
/* 160 */     return getPrefix() + viewName + getSuffix();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/UrlFilenameViewController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */