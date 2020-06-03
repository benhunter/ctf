/*     */ package org.springframework.web.servlet.support;
/*     */ 
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.context.request.RequestAttributes;
/*     */ import org.springframework.web.context.request.RequestContextHolder;
/*     */ import org.springframework.web.context.request.ServletRequestAttributes;
/*     */ import org.springframework.web.util.UriComponentsBuilder;
/*     */ import org.springframework.web.util.UriUtils;
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
/*     */ public class ServletUriComponentsBuilder
/*     */   extends UriComponentsBuilder
/*     */ {
/*     */   @Nullable
/*     */   private String originalPath;
/*     */   
/*     */   protected ServletUriComponentsBuilder() {}
/*     */   
/*     */   protected ServletUriComponentsBuilder(ServletUriComponentsBuilder other) {
/*  68 */     super(other);
/*  69 */     this.originalPath = other.originalPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ServletUriComponentsBuilder fromContextPath(HttpServletRequest request) {
/*  80 */     ServletUriComponentsBuilder builder = initFromRequest(request);
/*  81 */     builder.replacePath(request.getContextPath());
/*  82 */     return builder;
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
/*     */   public static ServletUriComponentsBuilder fromServletMapping(HttpServletRequest request) {
/*  94 */     ServletUriComponentsBuilder builder = fromContextPath(request);
/*  95 */     if (StringUtils.hasText((new UrlPathHelper()).getPathWithinServletMapping(request))) {
/*  96 */       builder.path(request.getServletPath());
/*     */     }
/*  98 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ServletUriComponentsBuilder fromRequestUri(HttpServletRequest request) {
/* 106 */     ServletUriComponentsBuilder builder = initFromRequest(request);
/* 107 */     builder.initPath(request.getRequestURI());
/* 108 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ServletUriComponentsBuilder fromRequest(HttpServletRequest request) {
/* 116 */     ServletUriComponentsBuilder builder = initFromRequest(request);
/* 117 */     builder.initPath(request.getRequestURI());
/* 118 */     builder.query(request.getQueryString());
/* 119 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ServletUriComponentsBuilder initFromRequest(HttpServletRequest request) {
/* 126 */     String scheme = request.getScheme();
/* 127 */     String host = request.getServerName();
/* 128 */     int port = request.getServerPort();
/*     */     
/* 130 */     ServletUriComponentsBuilder builder = new ServletUriComponentsBuilder();
/* 131 */     builder.scheme(scheme);
/* 132 */     builder.host(host);
/* 133 */     if (("http".equals(scheme) && port != 80) || ("https".equals(scheme) && port != 443)) {
/* 134 */       builder.port(port);
/*     */     }
/* 136 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ServletUriComponentsBuilder fromCurrentContextPath() {
/* 147 */     return fromContextPath(getCurrentRequest());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ServletUriComponentsBuilder fromCurrentServletMapping() {
/* 155 */     return fromServletMapping(getCurrentRequest());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ServletUriComponentsBuilder fromCurrentRequestUri() {
/* 163 */     return fromRequestUri(getCurrentRequest());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ServletUriComponentsBuilder fromCurrentRequest() {
/* 171 */     return fromRequest(getCurrentRequest());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static HttpServletRequest getCurrentRequest() {
/* 178 */     RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
/* 179 */     Assert.state(attrs instanceof ServletRequestAttributes, "No current ServletRequestAttributes");
/* 180 */     return ((ServletRequestAttributes)attrs).getRequest();
/*     */   }
/*     */ 
/*     */   
/*     */   private void initPath(String path) {
/* 185 */     this.originalPath = path;
/* 186 */     replacePath(path);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String removePathExtension() {
/* 206 */     String extension = null;
/* 207 */     if (this.originalPath != null) {
/* 208 */       extension = UriUtils.extractFileExtension(this.originalPath);
/* 209 */       if (StringUtils.hasLength(extension)) {
/* 210 */         int end = this.originalPath.length() - extension.length() + 1;
/* 211 */         replacePath(this.originalPath.substring(0, end));
/*     */       } 
/* 213 */       this.originalPath = null;
/*     */     } 
/* 215 */     return extension;
/*     */   }
/*     */ 
/*     */   
/*     */   public ServletUriComponentsBuilder cloneBuilder() {
/* 220 */     return new ServletUriComponentsBuilder(this);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/support/ServletUriComponentsBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */