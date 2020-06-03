/*     */ package org.springframework.web.context.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.io.UrlResource;
/*     */ import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
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
/*     */ public class ServletContextResourcePatternResolver
/*     */   extends PathMatchingResourcePatternResolver
/*     */ {
/*  48 */   private static final Log logger = LogFactory.getLog(ServletContextResourcePatternResolver.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletContextResourcePatternResolver(ServletContext servletContext) {
/*  57 */     super((ResourceLoader)new ServletContextResourceLoader(servletContext));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServletContextResourcePatternResolver(ResourceLoader resourceLoader) {
/*  66 */     super(resourceLoader);
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
/*     */   protected Set<Resource> doFindPathMatchingFileResources(Resource rootDirResource, String subPattern) throws IOException {
/*  83 */     if (rootDirResource instanceof ServletContextResource) {
/*  84 */       ServletContextResource scResource = (ServletContextResource)rootDirResource;
/*  85 */       ServletContext sc = scResource.getServletContext();
/*  86 */       String fullPattern = scResource.getPath() + subPattern;
/*  87 */       Set<Resource> result = new LinkedHashSet<>(8);
/*  88 */       doRetrieveMatchingServletContextResources(sc, fullPattern, scResource.getPath(), result);
/*  89 */       return result;
/*     */     } 
/*     */     
/*  92 */     return super.doFindPathMatchingFileResources(rootDirResource, subPattern);
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
/*     */   protected void doRetrieveMatchingServletContextResources(ServletContext servletContext, String fullPattern, String dir, Set<Resource> result) throws IOException {
/* 112 */     Set<String> candidates = servletContext.getResourcePaths(dir);
/* 113 */     if (candidates != null) {
/* 114 */       boolean dirDepthNotFixed = fullPattern.contains("**");
/* 115 */       int jarFileSep = fullPattern.indexOf("!/");
/* 116 */       String jarFilePath = null;
/* 117 */       String pathInJarFile = null;
/* 118 */       if (jarFileSep > 0 && jarFileSep + "!/".length() < fullPattern.length()) {
/* 119 */         jarFilePath = fullPattern.substring(0, jarFileSep);
/* 120 */         pathInJarFile = fullPattern.substring(jarFileSep + "!/".length());
/*     */       } 
/* 122 */       for (String currPath : candidates) {
/* 123 */         if (!currPath.startsWith(dir)) {
/*     */ 
/*     */           
/* 126 */           int dirIndex = currPath.indexOf(dir);
/* 127 */           if (dirIndex != -1) {
/* 128 */             currPath = currPath.substring(dirIndex);
/*     */           }
/*     */         } 
/* 131 */         if (currPath.endsWith("/") && (dirDepthNotFixed || StringUtils.countOccurrencesOf(currPath, "/") <= 
/* 132 */           StringUtils.countOccurrencesOf(fullPattern, "/")))
/*     */         {
/*     */           
/* 135 */           doRetrieveMatchingServletContextResources(servletContext, fullPattern, currPath, result);
/*     */         }
/* 137 */         if (jarFilePath != null && getPathMatcher().match(jarFilePath, currPath)) {
/*     */           
/* 139 */           String absoluteJarPath = servletContext.getRealPath(currPath);
/* 140 */           if (absoluteJarPath != null) {
/* 141 */             doRetrieveMatchingJarEntries(absoluteJarPath, pathInJarFile, result);
/*     */           }
/*     */         } 
/* 144 */         if (getPathMatcher().match(fullPattern, currPath)) {
/* 145 */           result.add(new ServletContextResource(servletContext, currPath));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void doRetrieveMatchingJarEntries(String jarFilePath, String entryPattern, Set<Resource> result) {
/* 158 */     if (logger.isDebugEnabled()) {
/* 159 */       logger.debug("Searching jar file [" + jarFilePath + "] for entries matching [" + entryPattern + "]");
/*     */     }
/*     */     try {
/* 162 */       JarFile jarFile = new JarFile(jarFilePath);
/*     */       try {
/* 164 */         for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements(); ) {
/* 165 */           JarEntry entry = entries.nextElement();
/* 166 */           String entryPath = entry.getName();
/* 167 */           if (getPathMatcher().match(entryPattern, entryPath)) {
/* 168 */             result.add(new UrlResource("jar", "file:" + jarFilePath + "!/" + entryPath));
/*     */           }
/*     */         }
/*     */       
/*     */       }
/*     */       finally {
/*     */         
/* 175 */         jarFile.close();
/*     */       }
/*     */     
/* 178 */     } catch (IOException ex) {
/* 179 */       if (logger.isWarnEnabled())
/* 180 */         logger.warn("Cannot search for matching resources in jar file [" + jarFilePath + "] because the jar cannot be opened through the file system", ex); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/support/ServletContextResourcePatternResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */