/*     */ package org.springframework.core.io.support;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.JarURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.net.URLConnection;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Enumeration;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.zip.ZipException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.io.DefaultResourceLoader;
/*     */ import org.springframework.core.io.FileSystemResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.io.UrlResource;
/*     */ import org.springframework.core.io.VfsResource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.AntPathMatcher;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.PathMatcher;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.ResourceUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PathMatchingResourcePatternResolver
/*     */   implements ResourcePatternResolver
/*     */ {
/* 185 */   private static final Log logger = LogFactory.getLog(PathMatchingResourcePatternResolver.class);
/*     */   
/*     */   @Nullable
/*     */   private static Method equinoxResolveMethod;
/*     */   private final ResourceLoader resourceLoader;
/*     */   
/*     */   static {
/*     */     try {
/* 193 */       Class<?> fileLocatorClass = ClassUtils.forName("org.eclipse.core.runtime.FileLocator", PathMatchingResourcePatternResolver.class
/* 194 */           .getClassLoader());
/* 195 */       equinoxResolveMethod = fileLocatorClass.getMethod("resolve", new Class[] { URL.class });
/* 196 */       logger.trace("Found Equinox FileLocator for OSGi bundle URL resolution");
/*     */     }
/* 198 */     catch (Throwable ex) {
/* 199 */       equinoxResolveMethod = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 206 */   private PathMatcher pathMatcher = (PathMatcher)new AntPathMatcher();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathMatchingResourcePatternResolver() {
/* 215 */     this.resourceLoader = (ResourceLoader)new DefaultResourceLoader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathMatchingResourcePatternResolver(ResourceLoader resourceLoader) {
/* 225 */     Assert.notNull(resourceLoader, "ResourceLoader must not be null");
/* 226 */     this.resourceLoader = resourceLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathMatchingResourcePatternResolver(@Nullable ClassLoader classLoader) {
/* 237 */     this.resourceLoader = (ResourceLoader)new DefaultResourceLoader(classLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceLoader getResourceLoader() {
/* 245 */     return this.resourceLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ClassLoader getClassLoader() {
/* 251 */     return getResourceLoader().getClassLoader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPathMatcher(PathMatcher pathMatcher) {
/* 260 */     Assert.notNull(pathMatcher, "PathMatcher must not be null");
/* 261 */     this.pathMatcher = pathMatcher;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathMatcher getPathMatcher() {
/* 268 */     return this.pathMatcher;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource getResource(String location) {
/* 274 */     return getResourceLoader().getResource(location);
/*     */   }
/*     */ 
/*     */   
/*     */   public Resource[] getResources(String locationPattern) throws IOException {
/* 279 */     Assert.notNull(locationPattern, "Location pattern must not be null");
/* 280 */     if (locationPattern.startsWith("classpath*:")) {
/*     */       
/* 282 */       if (getPathMatcher().isPattern(locationPattern.substring("classpath*:".length())))
/*     */       {
/* 284 */         return findPathMatchingResources(locationPattern);
/*     */       }
/*     */ 
/*     */       
/* 288 */       return findAllClassPathResources(locationPattern.substring("classpath*:".length()));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 295 */     int prefixEnd = locationPattern.startsWith("war:") ? (locationPattern.indexOf("*/") + 1) : (locationPattern.indexOf(':') + 1);
/* 296 */     if (getPathMatcher().isPattern(locationPattern.substring(prefixEnd)))
/*     */     {
/* 298 */       return findPathMatchingResources(locationPattern);
/*     */     }
/*     */ 
/*     */     
/* 302 */     return new Resource[] { getResourceLoader().getResource(locationPattern) };
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
/*     */   protected Resource[] findAllClassPathResources(String location) throws IOException {
/* 317 */     String path = location;
/* 318 */     if (path.startsWith("/")) {
/* 319 */       path = path.substring(1);
/*     */     }
/* 321 */     Set<Resource> result = doFindAllClassPathResources(path);
/* 322 */     if (logger.isTraceEnabled()) {
/* 323 */       logger.trace("Resolved classpath location [" + location + "] to resources " + result);
/*     */     }
/* 325 */     return result.<Resource>toArray(new Resource[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Set<Resource> doFindAllClassPathResources(String path) throws IOException {
/* 336 */     Set<Resource> result = new LinkedHashSet<>(16);
/* 337 */     ClassLoader cl = getClassLoader();
/* 338 */     Enumeration<URL> resourceUrls = (cl != null) ? cl.getResources(path) : ClassLoader.getSystemResources(path);
/* 339 */     while (resourceUrls.hasMoreElements()) {
/* 340 */       URL url = resourceUrls.nextElement();
/* 341 */       result.add(convertClassLoaderURL(url));
/*     */     } 
/* 343 */     if ("".equals(path))
/*     */     {
/*     */       
/* 346 */       addAllClassLoaderJarRoots(cl, result);
/*     */     }
/* 348 */     return result;
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
/*     */   protected Resource convertClassLoaderURL(URL url) {
/* 360 */     return (Resource)new UrlResource(url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAllClassLoaderJarRoots(@Nullable ClassLoader classLoader, Set<Resource> result) {
/* 371 */     if (classLoader instanceof URLClassLoader) {
/*     */       try {
/* 373 */         for (URL url : ((URLClassLoader)classLoader).getURLs()) {
/*     */           try {
/* 375 */             UrlResource jarResource = "jar".equals(url.getProtocol()) ? new UrlResource(url) : new UrlResource("jar:" + url + "!/");
/*     */ 
/*     */             
/* 378 */             if (jarResource.exists()) {
/* 379 */               result.add(jarResource);
/*     */             }
/*     */           }
/* 382 */           catch (MalformedURLException ex) {
/* 383 */             if (logger.isDebugEnabled()) {
/* 384 */               logger.debug("Cannot search for matching files underneath [" + url + "] because it cannot be converted to a valid 'jar:' URL: " + ex
/* 385 */                   .getMessage());
/*     */             }
/*     */           }
/*     */         
/*     */         } 
/* 390 */       } catch (Exception ex) {
/* 391 */         if (logger.isDebugEnabled()) {
/* 392 */           logger.debug("Cannot introspect jar files since ClassLoader [" + classLoader + "] does not support 'getURLs()': " + ex);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 398 */     if (classLoader == ClassLoader.getSystemClassLoader())
/*     */     {
/* 400 */       addClassPathManifestEntries(result);
/*     */     }
/*     */     
/* 403 */     if (classLoader != null) {
/*     */       
/*     */       try {
/* 406 */         addAllClassLoaderJarRoots(classLoader.getParent(), result);
/*     */       }
/* 408 */       catch (Exception ex) {
/* 409 */         if (logger.isDebugEnabled()) {
/* 410 */           logger.debug("Cannot introspect jar files in parent ClassLoader since [" + classLoader + "] does not support 'getParent()': " + ex);
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
/*     */   
/*     */   protected void addClassPathManifestEntries(Set<Resource> result) {
/*     */     try {
/* 425 */       String javaClassPathProperty = System.getProperty("java.class.path");
/* 426 */       for (String path : StringUtils.delimitedListToStringArray(javaClassPathProperty, 
/* 427 */           System.getProperty("path.separator"))) {
/*     */         try {
/* 429 */           String filePath = (new File(path)).getAbsolutePath();
/* 430 */           int prefixIndex = filePath.indexOf(':');
/* 431 */           if (prefixIndex == 1)
/*     */           {
/* 433 */             filePath = StringUtils.capitalize(filePath);
/*     */           }
/* 435 */           UrlResource jarResource = new UrlResource("jar:file:" + filePath + "!/");
/*     */ 
/*     */           
/* 438 */           if (!result.contains(jarResource) && !hasDuplicate(filePath, result) && jarResource.exists()) {
/* 439 */             result.add(jarResource);
/*     */           }
/*     */         }
/* 442 */         catch (MalformedURLException ex) {
/* 443 */           if (logger.isDebugEnabled()) {
/* 444 */             logger.debug("Cannot search for matching files underneath [" + path + "] because it cannot be converted to a valid 'jar:' URL: " + ex
/* 445 */                 .getMessage());
/*     */           }
/*     */         }
/*     */       
/*     */       } 
/* 450 */     } catch (Exception ex) {
/* 451 */       if (logger.isDebugEnabled()) {
/* 452 */         logger.debug("Failed to evaluate 'java.class.path' manifest entries: " + ex);
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
/*     */ 
/*     */   
/*     */   private boolean hasDuplicate(String filePath, Set<Resource> result) {
/* 466 */     if (result.isEmpty()) {
/* 467 */       return false;
/*     */     }
/* 469 */     String duplicatePath = filePath.startsWith("/") ? filePath.substring(1) : ("/" + filePath);
/*     */     try {
/* 471 */       return result.contains(new UrlResource("jar:file:" + duplicatePath + "!/"));
/*     */     
/*     */     }
/* 474 */     catch (MalformedURLException ex) {
/*     */       
/* 476 */       return false;
/*     */     } 
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
/*     */   protected Resource[] findPathMatchingResources(String locationPattern) throws IOException {
/* 492 */     String rootDirPath = determineRootDir(locationPattern);
/* 493 */     String subPattern = locationPattern.substring(rootDirPath.length());
/* 494 */     Resource[] rootDirResources = getResources(rootDirPath);
/* 495 */     Set<Resource> result = new LinkedHashSet<>(16);
/* 496 */     for (Resource rootDirResource : rootDirResources) {
/* 497 */       UrlResource urlResource; rootDirResource = resolveRootDirResource(rootDirResource);
/* 498 */       URL rootDirUrl = rootDirResource.getURL();
/* 499 */       if (equinoxResolveMethod != null && rootDirUrl.getProtocol().startsWith("bundle")) {
/* 500 */         URL resolvedUrl = (URL)ReflectionUtils.invokeMethod(equinoxResolveMethod, null, new Object[] { rootDirUrl });
/* 501 */         if (resolvedUrl != null) {
/* 502 */           rootDirUrl = resolvedUrl;
/*     */         }
/* 504 */         urlResource = new UrlResource(rootDirUrl);
/*     */       } 
/* 506 */       if (rootDirUrl.getProtocol().startsWith("vfs")) {
/* 507 */         result.addAll(VfsResourceMatchingDelegate.findMatchingResources(rootDirUrl, subPattern, getPathMatcher()));
/*     */       }
/* 509 */       else if (ResourceUtils.isJarURL(rootDirUrl) || isJarResource((Resource)urlResource)) {
/* 510 */         result.addAll(doFindPathMatchingJarResources((Resource)urlResource, rootDirUrl, subPattern));
/*     */       } else {
/*     */         
/* 513 */         result.addAll(doFindPathMatchingFileResources((Resource)urlResource, subPattern));
/*     */       } 
/*     */     } 
/* 516 */     if (logger.isTraceEnabled()) {
/* 517 */       logger.trace("Resolved location pattern [" + locationPattern + "] to resources " + result);
/*     */     }
/* 519 */     return result.<Resource>toArray(new Resource[0]);
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
/*     */   protected String determineRootDir(String location) {
/* 535 */     int prefixEnd = location.indexOf(':') + 1;
/* 536 */     int rootDirEnd = location.length();
/* 537 */     while (rootDirEnd > prefixEnd && getPathMatcher().isPattern(location.substring(prefixEnd, rootDirEnd))) {
/* 538 */       rootDirEnd = location.lastIndexOf('/', rootDirEnd - 2) + 1;
/*     */     }
/* 540 */     if (rootDirEnd == 0) {
/* 541 */       rootDirEnd = prefixEnd;
/*     */     }
/* 543 */     return location.substring(0, rootDirEnd);
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
/*     */   protected Resource resolveRootDirResource(Resource original) throws IOException {
/* 557 */     return original;
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
/*     */   protected boolean isJarResource(Resource resource) throws IOException {
/* 573 */     return false;
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
/*     */   protected Set<Resource> doFindPathMatchingJarResources(Resource rootDirResource, URL rootDirURL, String subPattern) throws IOException {
/*     */     JarFile jarFile;
/*     */     String jarFileUrl, rootEntryPath;
/*     */     boolean closeJarFile;
/* 591 */     URLConnection con = rootDirURL.openConnection();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 597 */     if (con instanceof JarURLConnection) {
/*     */       
/* 599 */       JarURLConnection jarCon = (JarURLConnection)con;
/* 600 */       ResourceUtils.useCachesIfNecessary(jarCon);
/* 601 */       jarFile = jarCon.getJarFile();
/* 602 */       jarFileUrl = jarCon.getJarFileURL().toExternalForm();
/* 603 */       JarEntry jarEntry = jarCon.getJarEntry();
/* 604 */       rootEntryPath = (jarEntry != null) ? jarEntry.getName() : "";
/* 605 */       closeJarFile = !jarCon.getUseCaches();
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 612 */       String urlFile = rootDirURL.getFile();
/*     */       try {
/* 614 */         int separatorIndex = urlFile.indexOf("*/");
/* 615 */         if (separatorIndex == -1) {
/* 616 */           separatorIndex = urlFile.indexOf("!/");
/*     */         }
/* 618 */         if (separatorIndex != -1) {
/* 619 */           jarFileUrl = urlFile.substring(0, separatorIndex);
/* 620 */           rootEntryPath = urlFile.substring(separatorIndex + 2);
/* 621 */           jarFile = getJarFile(jarFileUrl);
/*     */         } else {
/*     */           
/* 624 */           jarFile = new JarFile(urlFile);
/* 625 */           jarFileUrl = urlFile;
/* 626 */           rootEntryPath = "";
/*     */         } 
/* 628 */         closeJarFile = true;
/*     */       }
/* 630 */       catch (ZipException ex) {
/* 631 */         if (logger.isDebugEnabled()) {
/* 632 */           logger.debug("Skipping invalid jar classpath entry [" + urlFile + "]");
/*     */         }
/* 634 */         return Collections.emptySet();
/*     */       } 
/*     */     } 
/*     */     
/*     */     try {
/* 639 */       if (logger.isTraceEnabled()) {
/* 640 */         logger.trace("Looking for matching resources in jar file [" + jarFileUrl + "]");
/*     */       }
/* 642 */       if (!"".equals(rootEntryPath) && !rootEntryPath.endsWith("/"))
/*     */       {
/*     */         
/* 645 */         rootEntryPath = rootEntryPath + "/";
/*     */       }
/* 647 */       Set<Resource> result = new LinkedHashSet<>(8);
/* 648 */       for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements(); ) {
/* 649 */         JarEntry entry = entries.nextElement();
/* 650 */         String entryPath = entry.getName();
/* 651 */         if (entryPath.startsWith(rootEntryPath)) {
/* 652 */           String relativePath = entryPath.substring(rootEntryPath.length());
/* 653 */           if (getPathMatcher().match(subPattern, relativePath)) {
/* 654 */             result.add(rootDirResource.createRelative(relativePath));
/*     */           }
/*     */         } 
/*     */       } 
/* 658 */       return result;
/*     */     } finally {
/*     */       
/* 661 */       if (closeJarFile) {
/* 662 */         jarFile.close();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JarFile getJarFile(String jarFileUrl) throws IOException {
/* 671 */     if (jarFileUrl.startsWith("file:")) {
/*     */       try {
/* 673 */         return new JarFile(ResourceUtils.toURI(jarFileUrl).getSchemeSpecificPart());
/*     */       }
/* 675 */       catch (URISyntaxException ex) {
/*     */         
/* 677 */         return new JarFile(jarFileUrl.substring("file:".length()));
/*     */       } 
/*     */     }
/*     */     
/* 681 */     return new JarFile(jarFileUrl);
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
/*     */     File rootDir;
/*     */     try {
/* 700 */       rootDir = rootDirResource.getFile().getAbsoluteFile();
/*     */     }
/* 702 */     catch (FileNotFoundException ex) {
/* 703 */       if (logger.isDebugEnabled()) {
/* 704 */         logger.debug("Cannot search for matching files underneath " + rootDirResource + " in the file system: " + ex
/* 705 */             .getMessage());
/*     */       }
/* 707 */       return Collections.emptySet();
/*     */     }
/* 709 */     catch (Exception ex) {
/* 710 */       if (logger.isInfoEnabled()) {
/* 711 */         logger.info("Failed to resolve " + rootDirResource + " in the file system: " + ex);
/*     */       }
/* 713 */       return Collections.emptySet();
/*     */     } 
/* 715 */     return doFindMatchingFileSystemResources(rootDir, subPattern);
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
/*     */   protected Set<Resource> doFindMatchingFileSystemResources(File rootDir, String subPattern) throws IOException {
/* 729 */     if (logger.isTraceEnabled()) {
/* 730 */       logger.trace("Looking for matching resources in directory tree [" + rootDir.getPath() + "]");
/*     */     }
/* 732 */     Set<File> matchingFiles = retrieveMatchingFiles(rootDir, subPattern);
/* 733 */     Set<Resource> result = new LinkedHashSet<>(matchingFiles.size());
/* 734 */     for (File file : matchingFiles) {
/* 735 */       result.add(new FileSystemResource(file));
/*     */     }
/* 737 */     return result;
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
/*     */   protected Set<File> retrieveMatchingFiles(File rootDir, String pattern) throws IOException {
/* 750 */     if (!rootDir.exists()) {
/*     */       
/* 752 */       if (logger.isDebugEnabled()) {
/* 753 */         logger.debug("Skipping [" + rootDir.getAbsolutePath() + "] because it does not exist");
/*     */       }
/* 755 */       return Collections.emptySet();
/*     */     } 
/* 757 */     if (!rootDir.isDirectory()) {
/*     */       
/* 759 */       if (logger.isInfoEnabled()) {
/* 760 */         logger.info("Skipping [" + rootDir.getAbsolutePath() + "] because it does not denote a directory");
/*     */       }
/* 762 */       return Collections.emptySet();
/*     */     } 
/* 764 */     if (!rootDir.canRead()) {
/* 765 */       if (logger.isInfoEnabled()) {
/* 766 */         logger.info("Skipping search for matching files underneath directory [" + rootDir.getAbsolutePath() + "] because the application is not allowed to read the directory");
/*     */       }
/*     */       
/* 769 */       return Collections.emptySet();
/*     */     } 
/* 771 */     String fullPattern = StringUtils.replace(rootDir.getAbsolutePath(), File.separator, "/");
/* 772 */     if (!pattern.startsWith("/")) {
/* 773 */       fullPattern = fullPattern + "/";
/*     */     }
/* 775 */     fullPattern = fullPattern + StringUtils.replace(pattern, File.separator, "/");
/* 776 */     Set<File> result = new LinkedHashSet<>(8);
/* 777 */     doRetrieveMatchingFiles(fullPattern, rootDir, result);
/* 778 */     return result;
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
/*     */   protected void doRetrieveMatchingFiles(String fullPattern, File dir, Set<File> result) throws IOException {
/* 791 */     if (logger.isTraceEnabled()) {
/* 792 */       logger.trace("Searching directory [" + dir.getAbsolutePath() + "] for files matching pattern [" + fullPattern + "]");
/*     */     }
/*     */     
/* 795 */     for (File content : listDirectory(dir)) {
/* 796 */       String currPath = StringUtils.replace(content.getAbsolutePath(), File.separator, "/");
/* 797 */       if (content.isDirectory() && getPathMatcher().matchStart(fullPattern, currPath + "/")) {
/* 798 */         if (!content.canRead()) {
/* 799 */           if (logger.isDebugEnabled()) {
/* 800 */             logger.debug("Skipping subdirectory [" + dir.getAbsolutePath() + "] because the application is not allowed to read the directory");
/*     */           }
/*     */         }
/*     */         else {
/*     */           
/* 805 */           doRetrieveMatchingFiles(fullPattern, content, result);
/*     */         } 
/*     */       }
/* 808 */       if (getPathMatcher().match(fullPattern, currPath)) {
/* 809 */         result.add(content);
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
/*     */   
/*     */   protected File[] listDirectory(File dir) {
/* 822 */     File[] files = dir.listFiles();
/* 823 */     if (files == null) {
/* 824 */       if (logger.isInfoEnabled()) {
/* 825 */         logger.info("Could not retrieve contents of directory [" + dir.getAbsolutePath() + "]");
/*     */       }
/* 827 */       return new File[0];
/*     */     } 
/* 829 */     Arrays.sort(files, Comparator.comparing(File::getName));
/* 830 */     return files;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class VfsResourceMatchingDelegate
/*     */   {
/*     */     public static Set<Resource> findMatchingResources(URL rootDirURL, String locationPattern, PathMatcher pathMatcher) throws IOException {
/* 842 */       Object root = VfsPatternUtils.findRoot(rootDirURL);
/*     */       
/* 844 */       PathMatchingResourcePatternResolver.PatternVirtualFileVisitor visitor = new PathMatchingResourcePatternResolver.PatternVirtualFileVisitor(VfsPatternUtils.getPath(root), locationPattern, pathMatcher);
/* 845 */       VfsPatternUtils.visit(root, visitor);
/* 846 */       return visitor.getResources();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class PatternVirtualFileVisitor
/*     */     implements InvocationHandler
/*     */   {
/*     */     private final String subPattern;
/*     */ 
/*     */     
/*     */     private final PathMatcher pathMatcher;
/*     */ 
/*     */     
/*     */     private final String rootPath;
/*     */     
/* 863 */     private final Set<Resource> resources = new LinkedHashSet<>();
/*     */     
/*     */     public PatternVirtualFileVisitor(String rootPath, String subPattern, PathMatcher pathMatcher) {
/* 866 */       this.subPattern = subPattern;
/* 867 */       this.pathMatcher = pathMatcher;
/* 868 */       this.rootPath = (rootPath.isEmpty() || rootPath.endsWith("/")) ? rootPath : (rootPath + "/");
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 874 */       String methodName = method.getName();
/* 875 */       if (Object.class == method.getDeclaringClass()) {
/* 876 */         if (methodName.equals("equals"))
/*     */         {
/* 878 */           return Boolean.valueOf((proxy == args[0]));
/*     */         }
/* 880 */         if (methodName.equals("hashCode")) {
/* 881 */           return Integer.valueOf(System.identityHashCode(proxy));
/*     */         }
/*     */       } else {
/* 884 */         if ("getAttributes".equals(methodName)) {
/* 885 */           return getAttributes();
/*     */         }
/* 887 */         if ("visit".equals(methodName)) {
/* 888 */           visit(args[0]);
/* 889 */           return null;
/*     */         } 
/* 891 */         if ("toString".equals(methodName)) {
/* 892 */           return toString();
/*     */         }
/*     */       } 
/* 895 */       throw new IllegalStateException("Unexpected method invocation: " + method);
/*     */     }
/*     */     
/*     */     public void visit(Object vfsResource) {
/* 899 */       if (this.pathMatcher.match(this.subPattern, 
/* 900 */           VfsPatternUtils.getPath(vfsResource).substring(this.rootPath.length()))) {
/* 901 */         this.resources.add(new VfsResource(vfsResource));
/*     */       }
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public Object getAttributes() {
/* 907 */       return VfsPatternUtils.getVisitorAttributes();
/*     */     }
/*     */     
/*     */     public Set<Resource> getResources() {
/* 911 */       return this.resources;
/*     */     }
/*     */     
/*     */     public int size() {
/* 915 */       return this.resources.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 920 */       return "sub-pattern: " + this.subPattern + ", resources: " + this.resources;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/io/support/PathMatchingResourcePatternResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */