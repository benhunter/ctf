/*     */ package org.springframework.beans.factory.xml;
/*     */ 
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.io.ClassPathResource;
/*     */ import org.springframework.core.io.support.PropertiesLoaderUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.InputSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PluggableSchemaResolver
/*     */   implements EntityResolver
/*     */ {
/*     */   public static final String DEFAULT_SCHEMA_MAPPINGS_LOCATION = "META-INF/spring.schemas";
/*  67 */   private static final Log logger = LogFactory.getLog(PluggableSchemaResolver.class);
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private final ClassLoader classLoader;
/*     */ 
/*     */ 
/*     */   
/*     */   private final String schemaMappingsLocation;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private volatile Map<String, String> schemaMappings;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PluggableSchemaResolver(@Nullable ClassLoader classLoader) {
/*  87 */     this.classLoader = classLoader;
/*  88 */     this.schemaMappingsLocation = "META-INF/spring.schemas";
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
/*     */   public PluggableSchemaResolver(@Nullable ClassLoader classLoader, String schemaMappingsLocation) {
/* 101 */     Assert.hasText(schemaMappingsLocation, "'schemaMappingsLocation' must not be empty");
/* 102 */     this.classLoader = classLoader;
/* 103 */     this.schemaMappingsLocation = schemaMappingsLocation;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public InputSource resolveEntity(@Nullable String publicId, @Nullable String systemId) throws IOException {
/* 110 */     if (logger.isTraceEnabled()) {
/* 111 */       logger.trace("Trying to resolve XML entity with public id [" + publicId + "] and system id [" + systemId + "]");
/*     */     }
/*     */ 
/*     */     
/* 115 */     if (systemId != null) {
/* 116 */       String resourceLocation = getSchemaMappings().get(systemId);
/* 117 */       if (resourceLocation == null && systemId.startsWith("https:"))
/*     */       {
/* 119 */         resourceLocation = getSchemaMappings().get("http:" + systemId.substring(6));
/*     */       }
/* 121 */       if (resourceLocation != null) {
/* 122 */         ClassPathResource classPathResource = new ClassPathResource(resourceLocation, this.classLoader);
/*     */         try {
/* 124 */           InputSource source = new InputSource(classPathResource.getInputStream());
/* 125 */           source.setPublicId(publicId);
/* 126 */           source.setSystemId(systemId);
/* 127 */           if (logger.isTraceEnabled()) {
/* 128 */             logger.trace("Found XML schema [" + systemId + "] in classpath: " + resourceLocation);
/*     */           }
/* 130 */           return source;
/*     */         }
/* 132 */         catch (FileNotFoundException ex) {
/* 133 */           if (logger.isDebugEnabled()) {
/* 134 */             logger.debug("Could not find XML schema [" + systemId + "]: " + classPathResource, ex);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 141 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, String> getSchemaMappings() {
/* 148 */     Map<String, String> schemaMappings = this.schemaMappings;
/* 149 */     if (schemaMappings == null) {
/* 150 */       synchronized (this) {
/* 151 */         schemaMappings = this.schemaMappings;
/* 152 */         if (schemaMappings == null) {
/* 153 */           if (logger.isTraceEnabled()) {
/* 154 */             logger.trace("Loading schema mappings from [" + this.schemaMappingsLocation + "]");
/*     */           }
/*     */           
/*     */           try {
/* 158 */             Properties mappings = PropertiesLoaderUtils.loadAllProperties(this.schemaMappingsLocation, this.classLoader);
/* 159 */             if (logger.isTraceEnabled()) {
/* 160 */               logger.trace("Loaded schema mappings: " + mappings);
/*     */             }
/* 162 */             schemaMappings = new ConcurrentHashMap<>(mappings.size());
/* 163 */             CollectionUtils.mergePropertiesIntoMap(mappings, schemaMappings);
/* 164 */             this.schemaMappings = schemaMappings;
/*     */           }
/* 166 */           catch (IOException ex) {
/* 167 */             throw new IllegalStateException("Unable to load schema mappings from location [" + this.schemaMappingsLocation + "]", ex);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 173 */     return schemaMappings;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 179 */     return "EntityResolver using schema mappings " + getSchemaMappings();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/xml/PluggableSchemaResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */