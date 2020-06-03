/*     */ package org.springframework.web.servlet.resource;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URLDecoder;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.core.io.ClassPathResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.context.support.ServletContextResource;
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
/*     */ public class PathResourceResolver
/*     */   extends AbstractResourceResolver
/*     */ {
/*     */   @Nullable
/*     */   private Resource[] allowedLocations;
/*  58 */   private final Map<Resource, Charset> locationCharsets = new HashMap<>(4);
/*     */ 
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
/*     */   private UrlPathHelper urlPathHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllowedLocations(@Nullable Resource... locations) {
/*  82 */     this.allowedLocations = locations;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Resource[] getAllowedLocations() {
/*  87 */     return this.allowedLocations;
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
/*     */   public void setLocationCharsets(Map<Resource, Charset> locationCharsets) {
/* 100 */     this.locationCharsets.clear();
/* 101 */     this.locationCharsets.putAll(locationCharsets);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<Resource, Charset> getLocationCharsets() {
/* 109 */     return Collections.unmodifiableMap(this.locationCharsets);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUrlPathHelper(@Nullable UrlPathHelper urlPathHelper) {
/* 119 */     this.urlPathHelper = urlPathHelper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public UrlPathHelper getUrlPathHelper() {
/* 128 */     return this.urlPathHelper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Resource resolveResourceInternal(@Nullable HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
/* 136 */     return getResource(requestPath, request, locations);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String resolveUrlPathInternal(String resourcePath, List<? extends Resource> locations, ResourceResolverChain chain) {
/* 143 */     return (StringUtils.hasText(resourcePath) && 
/* 144 */       getResource(resourcePath, null, locations) != null) ? resourcePath : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Resource getResource(String resourcePath, @Nullable HttpServletRequest request, List<? extends Resource> locations) {
/* 151 */     for (Resource location : locations) {
/*     */       try {
/* 153 */         String pathToUse = encodeIfNecessary(resourcePath, request, location);
/* 154 */         Resource resource = getResource(pathToUse, location);
/* 155 */         if (resource != null) {
/* 156 */           return resource;
/*     */         }
/*     */       }
/* 159 */       catch (IOException ex) {
/* 160 */         if (this.logger.isDebugEnabled()) {
/* 161 */           String error = "Skip location [" + location + "] due to error";
/* 162 */           if (this.logger.isTraceEnabled()) {
/* 163 */             this.logger.trace(error, ex);
/*     */             continue;
/*     */           } 
/* 166 */           this.logger.debug(error + ": " + ex.getMessage());
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 171 */     return null;
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
/*     */   protected Resource getResource(String resourcePath, Resource location) throws IOException {
/* 184 */     Resource resource = location.createRelative(resourcePath);
/* 185 */     if (resource.isReadable()) {
/* 186 */       if (checkResource(resource, location)) {
/* 187 */         return resource;
/*     */       }
/* 189 */       if (this.logger.isWarnEnabled()) {
/* 190 */         Resource[] allowedLocations = getAllowedLocations();
/* 191 */         this.logger.warn("Resource path \"" + resourcePath + "\" was successfully resolved but resource \"" + resource
/* 192 */             .getURL() + "\" is neither under the current location \"" + location
/* 193 */             .getURL() + "\" nor under any of the allowed locations " + ((allowedLocations != null) ? 
/* 194 */             Arrays.<Resource>asList(allowedLocations) : "[]"));
/*     */       } 
/*     */     } 
/* 197 */     return null;
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
/*     */   protected boolean checkResource(Resource resource, Resource location) throws IOException {
/* 211 */     if (isResourceUnderLocation(resource, location)) {
/* 212 */       return true;
/*     */     }
/* 214 */     Resource[] allowedLocations = getAllowedLocations();
/* 215 */     if (allowedLocations != null) {
/* 216 */       for (Resource current : allowedLocations) {
/* 217 */         if (isResourceUnderLocation(resource, current)) {
/* 218 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/* 222 */     return false;
/*     */   }
/*     */   private boolean isResourceUnderLocation(Resource resource, Resource location) throws IOException {
/*     */     String resourcePath;
/* 226 */     if (resource.getClass() != location.getClass()) {
/* 227 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 233 */     if (resource instanceof org.springframework.core.io.UrlResource) {
/* 234 */       resourcePath = resource.getURL().toExternalForm();
/* 235 */       locationPath = StringUtils.cleanPath(location.getURL().toString());
/*     */     }
/* 237 */     else if (resource instanceof ClassPathResource) {
/* 238 */       resourcePath = ((ClassPathResource)resource).getPath();
/* 239 */       locationPath = StringUtils.cleanPath(((ClassPathResource)location).getPath());
/*     */     }
/* 241 */     else if (resource instanceof ServletContextResource) {
/* 242 */       resourcePath = ((ServletContextResource)resource).getPath();
/* 243 */       locationPath = StringUtils.cleanPath(((ServletContextResource)location).getPath());
/*     */     } else {
/*     */       
/* 246 */       resourcePath = resource.getURL().getPath();
/* 247 */       locationPath = StringUtils.cleanPath(location.getURL().getPath());
/*     */     } 
/*     */     
/* 250 */     if (locationPath.equals(resourcePath)) {
/* 251 */       return true;
/*     */     }
/* 253 */     String locationPath = (locationPath.endsWith("/") || locationPath.isEmpty()) ? locationPath : (locationPath + "/");
/* 254 */     return (resourcePath.startsWith(locationPath) && !isInvalidEncodedPath(resourcePath));
/*     */   }
/*     */   
/*     */   private String encodeIfNecessary(String path, @Nullable HttpServletRequest request, Resource location) {
/* 258 */     if (shouldEncodeRelativePath(location) && request != null) {
/* 259 */       Charset charset = this.locationCharsets.getOrDefault(location, StandardCharsets.UTF_8);
/* 260 */       StringBuilder sb = new StringBuilder();
/* 261 */       StringTokenizer tokenizer = new StringTokenizer(path, "/");
/* 262 */       while (tokenizer.hasMoreTokens()) {
/* 263 */         String value = UriUtils.encode(tokenizer.nextToken(), charset);
/* 264 */         sb.append(value);
/* 265 */         sb.append("/");
/*     */       } 
/* 267 */       if (!path.endsWith("/")) {
/* 268 */         sb.setLength(sb.length() - 1);
/*     */       }
/* 270 */       return sb.toString();
/*     */     } 
/*     */     
/* 273 */     return path;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean shouldEncodeRelativePath(Resource location) {
/* 278 */     return (location instanceof org.springframework.core.io.UrlResource && this.urlPathHelper != null && this.urlPathHelper.isUrlDecode());
/*     */   }
/*     */   
/*     */   private boolean isInvalidEncodedPath(String resourcePath) {
/* 282 */     if (resourcePath.contains("%")) {
/*     */       
/*     */       try {
/* 285 */         String decodedPath = URLDecoder.decode(resourcePath, "UTF-8");
/* 286 */         if (decodedPath.contains("../") || decodedPath.contains("..\\")) {
/* 287 */           this.logger.warn("Resolved resource path contains encoded \"../\" or \"..\\\": " + resourcePath);
/* 288 */           return true;
/*     */         }
/*     */       
/* 291 */       } catch (UnsupportedEncodingException unsupportedEncodingException) {}
/*     */     }
/*     */ 
/*     */     
/* 295 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/resource/PathResourceResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */