/*     */ package org.springframework.cache.annotation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.springframework.context.annotation.AdviceMode;
/*     */ import org.springframework.context.annotation.AdviceModeImportSelector;
/*     */ import org.springframework.context.annotation.AutoProxyRegistrar;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class CachingConfigurationSelector
/*     */   extends AdviceModeImportSelector<EnableCaching>
/*     */ {
/*     */   private static final String PROXY_JCACHE_CONFIGURATION_CLASS = "org.springframework.cache.jcache.config.ProxyJCacheConfiguration";
/*     */   private static final String CACHE_ASPECT_CONFIGURATION_CLASS_NAME = "org.springframework.cache.aspectj.AspectJCachingConfiguration";
/*     */   private static final String JCACHE_ASPECT_CONFIGURATION_CLASS_NAME = "org.springframework.cache.aspectj.AspectJJCacheConfiguration";
/*     */   private static final boolean jsr107Present;
/*     */   private static final boolean jcacheImplPresent;
/*     */   
/*     */   static {
/*  58 */     ClassLoader classLoader = CachingConfigurationSelector.class.getClassLoader();
/*  59 */     jsr107Present = ClassUtils.isPresent("javax.cache.Cache", classLoader);
/*  60 */     jcacheImplPresent = ClassUtils.isPresent("org.springframework.cache.jcache.config.ProxyJCacheConfiguration", classLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] selectImports(AdviceMode adviceMode) {
/*  71 */     switch (adviceMode) {
/*     */       case PROXY:
/*  73 */         return getProxyImports();
/*     */       case ASPECTJ:
/*  75 */         return getAspectJImports();
/*     */     } 
/*  77 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String[] getProxyImports() {
/*  86 */     List<String> result = new ArrayList<>(3);
/*  87 */     result.add(AutoProxyRegistrar.class.getName());
/*  88 */     result.add(ProxyCachingConfiguration.class.getName());
/*  89 */     if (jsr107Present && jcacheImplPresent) {
/*  90 */       result.add("org.springframework.cache.jcache.config.ProxyJCacheConfiguration");
/*     */     }
/*  92 */     return StringUtils.toStringArray(result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String[] getAspectJImports() {
/* 100 */     List<String> result = new ArrayList<>(2);
/* 101 */     result.add("org.springframework.cache.aspectj.AspectJCachingConfiguration");
/* 102 */     if (jsr107Present && jcacheImplPresent) {
/* 103 */       result.add("org.springframework.cache.aspectj.AspectJJCacheConfiguration");
/*     */     }
/* 105 */     return StringUtils.toStringArray(result);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/annotation/CachingConfigurationSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */