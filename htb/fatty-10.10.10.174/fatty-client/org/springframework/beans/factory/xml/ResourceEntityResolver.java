/*     */ package org.springframework.beans.factory.xml;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.net.URLDecoder;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourceEntityResolver
/*     */   extends DelegatingEntityResolver
/*     */ {
/*  56 */   private static final Log logger = LogFactory.getLog(ResourceEntityResolver.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ResourceLoader resourceLoader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceEntityResolver(ResourceLoader resourceLoader) {
/*  68 */     super(resourceLoader.getClassLoader());
/*  69 */     this.resourceLoader = resourceLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public InputSource resolveEntity(@Nullable String publicId, @Nullable String systemId) throws SAXException, IOException {
/*  78 */     InputSource source = super.resolveEntity(publicId, systemId);
/*     */     
/*  80 */     if (source == null && systemId != null) {
/*  81 */       String resourcePath = null;
/*     */       try {
/*  83 */         String decodedSystemId = URLDecoder.decode(systemId, "UTF-8");
/*  84 */         String givenUrl = (new URL(decodedSystemId)).toString();
/*  85 */         String systemRootUrl = (new File("")).toURI().toURL().toString();
/*     */         
/*  87 */         if (givenUrl.startsWith(systemRootUrl)) {
/*  88 */           resourcePath = givenUrl.substring(systemRootUrl.length());
/*     */         }
/*     */       }
/*  91 */       catch (Exception ex) {
/*     */         
/*  93 */         if (logger.isDebugEnabled()) {
/*  94 */           logger.debug("Could not resolve XML entity [" + systemId + "] against system root URL", ex);
/*     */         }
/*     */         
/*  97 */         resourcePath = systemId;
/*     */       } 
/*  99 */       if (resourcePath != null) {
/* 100 */         if (logger.isTraceEnabled()) {
/* 101 */           logger.trace("Trying to locate XML entity [" + systemId + "] as resource [" + resourcePath + "]");
/*     */         }
/* 103 */         Resource resource = this.resourceLoader.getResource(resourcePath);
/* 104 */         source = new InputSource(resource.getInputStream());
/* 105 */         source.setPublicId(publicId);
/* 106 */         source.setSystemId(systemId);
/* 107 */         if (logger.isDebugEnabled()) {
/* 108 */           logger.debug("Found XML entity [" + systemId + "]: " + resource);
/*     */         }
/*     */       }
/* 111 */       else if (systemId.endsWith(".dtd") || systemId.endsWith(".xsd")) {
/*     */         
/* 113 */         String url = systemId;
/* 114 */         if (url.startsWith("http:")) {
/* 115 */           url = "https:" + url.substring(5);
/*     */         }
/*     */         try {
/* 118 */           source = new InputSource((new URL(url)).openStream());
/* 119 */           source.setPublicId(publicId);
/* 120 */           source.setSystemId(systemId);
/*     */         }
/* 122 */         catch (IOException ex) {
/* 123 */           if (logger.isDebugEnabled()) {
/* 124 */             logger.debug("Could not resolve XML entity [" + systemId + "] through URL [" + url + "]", ex);
/*     */           }
/*     */           
/* 127 */           source = null;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 132 */     return source;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/xml/ResourceEntityResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */