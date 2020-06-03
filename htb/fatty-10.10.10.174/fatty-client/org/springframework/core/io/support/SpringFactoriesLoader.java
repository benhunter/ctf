/*     */ package org.springframework.core.io.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.UrlResource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ public final class SpringFactoriesLoader
/*     */ {
/*     */   public static final String FACTORIES_RESOURCE_LOCATION = "META-INF/spring.factories";
/*  71 */   private static final Log logger = LogFactory.getLog(SpringFactoriesLoader.class);
/*     */   
/*  73 */   private static final Map<ClassLoader, MultiValueMap<String, String>> cache = (Map<ClassLoader, MultiValueMap<String, String>>)new ConcurrentReferenceHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> List<T> loadFactories(Class<T> factoryClass, @Nullable ClassLoader classLoader) {
/*  93 */     Assert.notNull(factoryClass, "'factoryClass' must not be null");
/*  94 */     ClassLoader classLoaderToUse = classLoader;
/*  95 */     if (classLoaderToUse == null) {
/*  96 */       classLoaderToUse = SpringFactoriesLoader.class.getClassLoader();
/*     */     }
/*  98 */     List<String> factoryNames = loadFactoryNames(factoryClass, classLoaderToUse);
/*  99 */     if (logger.isTraceEnabled()) {
/* 100 */       logger.trace("Loaded [" + factoryClass.getName() + "] names: " + factoryNames);
/*     */     }
/* 102 */     List<T> result = new ArrayList<>(factoryNames.size());
/* 103 */     for (String factoryName : factoryNames) {
/* 104 */       result.add(instantiateFactory(factoryName, factoryClass, classLoaderToUse));
/*     */     }
/* 106 */     AnnotationAwareOrderComparator.sort(result);
/* 107 */     return result;
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
/*     */   public static List<String> loadFactoryNames(Class<?> factoryClass, @Nullable ClassLoader classLoader) {
/* 121 */     String factoryClassName = factoryClass.getName();
/* 122 */     return loadSpringFactories(classLoader).getOrDefault(factoryClassName, Collections.emptyList());
/*     */   }
/*     */   
/*     */   private static Map<String, List<String>> loadSpringFactories(@Nullable ClassLoader classLoader) {
/* 126 */     MultiValueMap<String, String> result = cache.get(classLoader);
/* 127 */     if (result != null) {
/* 128 */       return (Map)result;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 134 */       Enumeration<URL> urls = (classLoader != null) ? classLoader.getResources("META-INF/spring.factories") : ClassLoader.getSystemResources("META-INF/spring.factories");
/* 135 */       LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap();
/* 136 */       while (urls.hasMoreElements()) {
/* 137 */         URL url = urls.nextElement();
/* 138 */         UrlResource resource = new UrlResource(url);
/* 139 */         Properties properties = PropertiesLoaderUtils.loadProperties((Resource)resource);
/* 140 */         for (Map.Entry<?, ?> entry : properties.entrySet()) {
/* 141 */           String factoryClassName = ((String)entry.getKey()).trim();
/* 142 */           for (String factoryName : StringUtils.commaDelimitedListToStringArray((String)entry.getValue())) {
/* 143 */             linkedMultiValueMap.add(factoryClassName, factoryName.trim());
/*     */           }
/*     */         } 
/*     */       } 
/* 147 */       cache.put(classLoader, linkedMultiValueMap);
/* 148 */       return (Map<String, List<String>>)linkedMultiValueMap;
/*     */     }
/* 150 */     catch (IOException ex) {
/* 151 */       throw new IllegalArgumentException("Unable to load factories from location [META-INF/spring.factories]", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> T instantiateFactory(String instanceClassName, Class<T> factoryClass, ClassLoader classLoader) {
/*     */     try {
/* 159 */       Class<?> instanceClass = ClassUtils.forName(instanceClassName, classLoader);
/* 160 */       if (!factoryClass.isAssignableFrom(instanceClass)) {
/* 161 */         throw new IllegalArgumentException("Class [" + instanceClassName + "] is not assignable to [" + factoryClass
/* 162 */             .getName() + "]");
/*     */       }
/* 164 */       return ReflectionUtils.accessibleConstructor(instanceClass, new Class[0]).newInstance(new Object[0]);
/*     */     }
/* 166 */     catch (Throwable ex) {
/* 167 */       throw new IllegalArgumentException("Unable to instantiate factory class: " + factoryClass.getName(), ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/io/support/SpringFactoriesLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */