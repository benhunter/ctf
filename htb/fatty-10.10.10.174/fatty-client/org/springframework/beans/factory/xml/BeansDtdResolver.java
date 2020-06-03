/*    */ package org.springframework.beans.factory.xml;
/*    */ 
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.core.io.ClassPathResource;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.xml.sax.EntityResolver;
/*    */ import org.xml.sax.InputSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BeansDtdResolver
/*    */   implements EntityResolver
/*    */ {
/*    */   private static final String DTD_EXTENSION = ".dtd";
/*    */   private static final String DTD_NAME = "spring-beans";
/* 51 */   private static final Log logger = LogFactory.getLog(BeansDtdResolver.class);
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public InputSource resolveEntity(@Nullable String publicId, @Nullable String systemId) throws IOException {
/* 57 */     if (logger.isTraceEnabled()) {
/* 58 */       logger.trace("Trying to resolve XML entity with public ID [" + publicId + "] and system ID [" + systemId + "]");
/*    */     }
/*    */ 
/*    */     
/* 62 */     if (systemId != null && systemId.endsWith(".dtd")) {
/* 63 */       int lastPathSeparator = systemId.lastIndexOf('/');
/* 64 */       int dtdNameStart = systemId.indexOf("spring-beans", lastPathSeparator);
/* 65 */       if (dtdNameStart != -1) {
/* 66 */         String dtdFile = "spring-beans.dtd";
/* 67 */         if (logger.isTraceEnabled()) {
/* 68 */           logger.trace("Trying to locate [" + dtdFile + "] in Spring jar on classpath");
/*    */         }
/*    */         try {
/* 71 */           ClassPathResource classPathResource = new ClassPathResource(dtdFile, getClass());
/* 72 */           InputSource source = new InputSource(classPathResource.getInputStream());
/* 73 */           source.setPublicId(publicId);
/* 74 */           source.setSystemId(systemId);
/* 75 */           if (logger.isTraceEnabled()) {
/* 76 */             logger.trace("Found beans DTD [" + systemId + "] in classpath: " + dtdFile);
/*    */           }
/* 78 */           return source;
/*    */         }
/* 80 */         catch (FileNotFoundException ex) {
/* 81 */           if (logger.isDebugEnabled()) {
/* 82 */             logger.debug("Could not resolve beans DTD [" + systemId + "]: not found in classpath", ex);
/*    */           }
/*    */         } 
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 89 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 95 */     return "EntityResolver for spring-beans DTD";
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/xml/BeansDtdResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */