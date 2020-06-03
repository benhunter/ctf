/*     */ package org.springframework.context.index;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.SpringProperties;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.UrlResource;
/*     */ import org.springframework.core.io.support.PropertiesLoaderUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CandidateComponentsIndexLoader
/*     */ {
/*     */   public static final String COMPONENTS_RESOURCE_LOCATION = "META-INF/spring.components";
/*     */   public static final String IGNORE_INDEX = "spring.index.ignore";
/*  62 */   private static final boolean shouldIgnoreIndex = SpringProperties.getFlag("spring.index.ignore");
/*     */   
/*  64 */   private static final Log logger = LogFactory.getLog(CandidateComponentsIndexLoader.class);
/*     */   
/*  66 */   private static final ConcurrentMap<ClassLoader, CandidateComponentsIndex> cache = (ConcurrentMap<ClassLoader, CandidateComponentsIndex>)new ConcurrentReferenceHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public static CandidateComponentsIndex loadIndex(@Nullable ClassLoader classLoader) {
/*  85 */     ClassLoader classLoaderToUse = classLoader;
/*  86 */     if (classLoaderToUse == null) {
/*  87 */       classLoaderToUse = CandidateComponentsIndexLoader.class.getClassLoader();
/*     */     }
/*  89 */     return cache.computeIfAbsent(classLoaderToUse, CandidateComponentsIndexLoader::doLoadIndex);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static CandidateComponentsIndex doLoadIndex(ClassLoader classLoader) {
/*  94 */     if (shouldIgnoreIndex) {
/*  95 */       return null;
/*     */     }
/*     */     
/*     */     try {
/*  99 */       Enumeration<URL> urls = classLoader.getResources("META-INF/spring.components");
/* 100 */       if (!urls.hasMoreElements()) {
/* 101 */         return null;
/*     */       }
/* 103 */       List<Properties> result = new ArrayList<>();
/* 104 */       while (urls.hasMoreElements()) {
/* 105 */         URL url = urls.nextElement();
/* 106 */         Properties properties = PropertiesLoaderUtils.loadProperties((Resource)new UrlResource(url));
/* 107 */         result.add(properties);
/*     */       } 
/* 109 */       if (logger.isDebugEnabled()) {
/* 110 */         logger.debug("Loaded " + result.size() + "] index(es)");
/*     */       }
/* 112 */       int totalCount = result.stream().mapToInt(Hashtable::size).sum();
/* 113 */       return (totalCount > 0) ? new CandidateComponentsIndex(result) : null;
/*     */     }
/* 115 */     catch (IOException ex) {
/* 116 */       throw new IllegalStateException("Unable to load indexes from location [META-INF/spring.components]", ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/index/CandidateComponentsIndexLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */