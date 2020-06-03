/*     */ package org.springframework.core.io.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Properties;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.DefaultPropertiesPersister;
/*     */ import org.springframework.util.PropertiesPersister;
/*     */ import org.springframework.util.ResourceUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class PropertiesLoaderUtils
/*     */ {
/*     */   private static final String XML_FILE_EXTENSION = ".xml";
/*     */   
/*     */   public static Properties loadProperties(EncodedResource resource) throws IOException {
/*  58 */     Properties props = new Properties();
/*  59 */     fillProperties(props, resource);
/*  60 */     return props;
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
/*     */   public static void fillProperties(Properties props, EncodedResource resource) throws IOException {
/*  73 */     fillProperties(props, resource, (PropertiesPersister)new DefaultPropertiesPersister());
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
/*     */   static void fillProperties(Properties props, EncodedResource resource, PropertiesPersister persister) throws IOException {
/*  86 */     InputStream stream = null;
/*  87 */     Reader reader = null;
/*     */     try {
/*  89 */       String filename = resource.getResource().getFilename();
/*  90 */       if (filename != null && filename.endsWith(".xml")) {
/*  91 */         stream = resource.getInputStream();
/*  92 */         persister.loadFromXml(props, stream);
/*     */       }
/*  94 */       else if (resource.requiresReader()) {
/*  95 */         reader = resource.getReader();
/*  96 */         persister.load(props, reader);
/*     */       } else {
/*     */         
/*  99 */         stream = resource.getInputStream();
/* 100 */         persister.load(props, stream);
/*     */       } 
/*     */     } finally {
/*     */       
/* 104 */       if (stream != null) {
/* 105 */         stream.close();
/*     */       }
/* 107 */       if (reader != null) {
/* 108 */         reader.close();
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
/*     */   public static Properties loadProperties(Resource resource) throws IOException {
/* 121 */     Properties props = new Properties();
/* 122 */     fillProperties(props, resource);
/* 123 */     return props;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void fillProperties(Properties props, Resource resource) throws IOException {
/* 133 */     InputStream is = resource.getInputStream();
/*     */     try {
/* 135 */       String filename = resource.getFilename();
/* 136 */       if (filename != null && filename.endsWith(".xml")) {
/* 137 */         props.loadFromXML(is);
/*     */       } else {
/*     */         
/* 140 */         props.load(is);
/*     */       } 
/*     */     } finally {
/*     */       
/* 144 */       is.close();
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
/*     */   public static Properties loadAllProperties(String resourceName) throws IOException {
/* 158 */     return loadAllProperties(resourceName, null);
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
/*     */   public static Properties loadAllProperties(String resourceName, @Nullable ClassLoader classLoader) throws IOException {
/* 173 */     Assert.notNull(resourceName, "Resource name must not be null");
/* 174 */     ClassLoader classLoaderToUse = classLoader;
/* 175 */     if (classLoaderToUse == null) {
/* 176 */       classLoaderToUse = ClassUtils.getDefaultClassLoader();
/*     */     }
/*     */     
/* 179 */     Enumeration<URL> urls = (classLoaderToUse != null) ? classLoaderToUse.getResources(resourceName) : ClassLoader.getSystemResources(resourceName);
/* 180 */     Properties props = new Properties();
/* 181 */     while (urls.hasMoreElements()) {
/* 182 */       URL url = urls.nextElement();
/* 183 */       URLConnection con = url.openConnection();
/* 184 */       ResourceUtils.useCachesIfNecessary(con);
/* 185 */       InputStream is = con.getInputStream();
/*     */       try {
/* 187 */         if (resourceName.endsWith(".xml")) {
/* 188 */           props.loadFromXML(is);
/*     */         } else {
/*     */           
/* 191 */           props.load(is);
/*     */         } 
/*     */       } finally {
/*     */         
/* 195 */         is.close();
/*     */       } 
/*     */     } 
/* 198 */     return props;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/io/support/PropertiesLoaderUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */