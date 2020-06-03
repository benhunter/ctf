/*     */ package org.springframework.web.servlet.resource;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.core.io.AbstractResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.AntPathMatcher;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VersionResourceResolver
/*     */   extends AbstractResourceResolver
/*     */ {
/*  67 */   private AntPathMatcher pathMatcher = new AntPathMatcher();
/*     */ 
/*     */   
/*  70 */   private final Map<String, VersionStrategy> versionStrategyMap = new LinkedHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStrategyMap(Map<String, VersionStrategy> map) {
/*  80 */     this.versionStrategyMap.clear();
/*  81 */     this.versionStrategyMap.putAll(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, VersionStrategy> getStrategyMap() {
/*  88 */     return this.versionStrategyMap;
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
/*     */   public VersionResourceResolver addContentVersionStrategy(String... pathPatterns) {
/* 104 */     addVersionStrategy(new ContentVersionStrategy(), pathPatterns);
/* 105 */     return this;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public VersionResourceResolver addFixedVersionStrategy(String version, String... pathPatterns) {
/* 127 */     List<String> patternsList = Arrays.asList(pathPatterns);
/* 128 */     List<String> prefixedPatterns = new ArrayList<>(pathPatterns.length);
/* 129 */     String versionPrefix = "/" + version;
/* 130 */     for (String pattern : patternsList) {
/* 131 */       prefixedPatterns.add(pattern);
/* 132 */       if (!pattern.startsWith(versionPrefix) && !patternsList.contains(versionPrefix + pattern)) {
/* 133 */         prefixedPatterns.add(versionPrefix + pattern);
/*     */       }
/*     */     } 
/* 136 */     return addVersionStrategy(new FixedVersionStrategy(version), StringUtils.toStringArray(prefixedPatterns));
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
/*     */   public VersionResourceResolver addVersionStrategy(VersionStrategy strategy, String... pathPatterns) {
/* 149 */     for (String pattern : pathPatterns) {
/* 150 */       getStrategyMap().put(pattern, strategy);
/*     */     }
/* 152 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Resource resolveResourceInternal(@Nullable HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
/* 160 */     Resource resolved = chain.resolveResource(request, requestPath, locations);
/* 161 */     if (resolved != null) {
/* 162 */       return resolved;
/*     */     }
/*     */     
/* 165 */     VersionStrategy versionStrategy = getStrategyForPath(requestPath);
/* 166 */     if (versionStrategy == null) {
/* 167 */       return null;
/*     */     }
/*     */     
/* 170 */     String candidateVersion = versionStrategy.extractVersion(requestPath);
/* 171 */     if (!StringUtils.hasLength(candidateVersion)) {
/* 172 */       return null;
/*     */     }
/*     */     
/* 175 */     String simplePath = versionStrategy.removeVersion(requestPath, candidateVersion);
/* 176 */     Resource baseResource = chain.resolveResource(request, simplePath, locations);
/* 177 */     if (baseResource == null) {
/* 178 */       return null;
/*     */     }
/*     */     
/* 181 */     String actualVersion = versionStrategy.getResourceVersion(baseResource);
/* 182 */     if (candidateVersion.equals(actualVersion)) {
/* 183 */       return new FileNameVersionedResource(baseResource, candidateVersion);
/*     */     }
/*     */     
/* 186 */     if (this.logger.isTraceEnabled()) {
/* 187 */       this.logger.trace("Found resource for \"" + requestPath + "\", but version [" + candidateVersion + "] does not match");
/*     */     }
/*     */     
/* 190 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String resolveUrlPathInternal(String resourceUrlPath, List<? extends Resource> locations, ResourceResolverChain chain) {
/* 198 */     String baseUrl = chain.resolveUrlPath(resourceUrlPath, locations);
/* 199 */     if (StringUtils.hasText(baseUrl)) {
/* 200 */       VersionStrategy versionStrategy = getStrategyForPath(resourceUrlPath);
/* 201 */       if (versionStrategy == null) {
/* 202 */         return baseUrl;
/*     */       }
/* 204 */       Resource resource = chain.resolveResource(null, baseUrl, locations);
/* 205 */       Assert.state((resource != null), "Unresolvable resource");
/* 206 */       String version = versionStrategy.getResourceVersion(resource);
/* 207 */       return versionStrategy.addVersion(baseUrl, version);
/*     */     } 
/* 209 */     return baseUrl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected VersionStrategy getStrategyForPath(String requestPath) {
/* 218 */     String path = "/".concat(requestPath);
/* 219 */     List<String> matchingPatterns = new ArrayList<>();
/* 220 */     for (String pattern : this.versionStrategyMap.keySet()) {
/* 221 */       if (this.pathMatcher.match(pattern, path)) {
/* 222 */         matchingPatterns.add(pattern);
/*     */       }
/*     */     } 
/* 225 */     if (!matchingPatterns.isEmpty()) {
/* 226 */       Comparator<String> comparator = this.pathMatcher.getPatternComparator(path);
/* 227 */       matchingPatterns.sort(comparator);
/* 228 */       return this.versionStrategyMap.get(matchingPatterns.get(0));
/*     */     } 
/* 230 */     return null;
/*     */   }
/*     */   
/*     */   private class FileNameVersionedResource
/*     */     extends AbstractResource
/*     */     implements HttpResource
/*     */   {
/*     */     private final Resource original;
/*     */     private final String version;
/*     */     
/*     */     public FileNameVersionedResource(Resource original, String version) {
/* 241 */       this.original = original;
/* 242 */       this.version = version;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean exists() {
/* 247 */       return this.original.exists();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isReadable() {
/* 252 */       return this.original.isReadable();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isOpen() {
/* 257 */       return this.original.isOpen();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isFile() {
/* 262 */       return this.original.isFile();
/*     */     }
/*     */ 
/*     */     
/*     */     public URL getURL() throws IOException {
/* 267 */       return this.original.getURL();
/*     */     }
/*     */ 
/*     */     
/*     */     public URI getURI() throws IOException {
/* 272 */       return this.original.getURI();
/*     */     }
/*     */ 
/*     */     
/*     */     public File getFile() throws IOException {
/* 277 */       return this.original.getFile();
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getFilename() {
/* 283 */       return this.original.getFilename();
/*     */     }
/*     */ 
/*     */     
/*     */     public long contentLength() throws IOException {
/* 288 */       return this.original.contentLength();
/*     */     }
/*     */ 
/*     */     
/*     */     public long lastModified() throws IOException {
/* 293 */       return this.original.lastModified();
/*     */     }
/*     */ 
/*     */     
/*     */     public Resource createRelative(String relativePath) throws IOException {
/* 298 */       return this.original.createRelative(relativePath);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getDescription() {
/* 303 */       return this.original.getDescription();
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream getInputStream() throws IOException {
/* 308 */       return this.original.getInputStream();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public HttpHeaders getResponseHeaders() {
/* 314 */       HttpHeaders headers = (this.original instanceof HttpResource) ? ((HttpResource)this.original).getResponseHeaders() : new HttpHeaders();
/* 315 */       headers.setETag("\"" + this.version + "\"");
/* 316 */       return headers;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/resource/VersionResourceResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */