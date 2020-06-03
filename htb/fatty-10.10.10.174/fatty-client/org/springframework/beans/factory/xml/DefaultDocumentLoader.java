/*     */ package org.springframework.beans.factory.xml;
/*     */ 
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.w3c.dom.Document;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
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
/*     */ public class DefaultDocumentLoader
/*     */   implements DocumentLoader
/*     */ {
/*     */   private static final String SCHEMA_LANGUAGE_ATTRIBUTE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
/*     */   private static final String XSD_SCHEMA_LANGUAGE = "http://www.w3.org/2001/XMLSchema";
/*  61 */   private static final Log logger = LogFactory.getLog(DefaultDocumentLoader.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Document loadDocument(InputSource inputSource, EntityResolver entityResolver, ErrorHandler errorHandler, int validationMode, boolean namespaceAware) throws Exception {
/*  72 */     DocumentBuilderFactory factory = createDocumentBuilderFactory(validationMode, namespaceAware);
/*  73 */     if (logger.isTraceEnabled()) {
/*  74 */       logger.trace("Using JAXP provider [" + factory.getClass().getName() + "]");
/*     */     }
/*  76 */     DocumentBuilder builder = createDocumentBuilder(factory, entityResolver, errorHandler);
/*  77 */     return builder.parse(inputSource);
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
/*     */   protected DocumentBuilderFactory createDocumentBuilderFactory(int validationMode, boolean namespaceAware) throws ParserConfigurationException {
/*  91 */     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/*  92 */     factory.setNamespaceAware(namespaceAware);
/*     */     
/*  94 */     if (validationMode != 0) {
/*  95 */       factory.setValidating(true);
/*  96 */       if (validationMode == 3) {
/*     */         
/*  98 */         factory.setNamespaceAware(true);
/*     */         try {
/* 100 */           factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
/*     */         }
/* 102 */         catch (IllegalArgumentException ex) {
/* 103 */           ParserConfigurationException pcex = new ParserConfigurationException("Unable to validate using XSD: Your JAXP provider [" + factory + "] does not support XML Schema. Are you running on Java 1.4 with Apache Crimson? Upgrade to Apache Xerces (or Java 1.5) for full XSD support.");
/*     */ 
/*     */ 
/*     */           
/* 107 */           pcex.initCause(ex);
/* 108 */           throw pcex;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 113 */     return factory;
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
/*     */   protected DocumentBuilder createDocumentBuilder(DocumentBuilderFactory factory, @Nullable EntityResolver entityResolver, @Nullable ErrorHandler errorHandler) throws ParserConfigurationException {
/* 131 */     DocumentBuilder docBuilder = factory.newDocumentBuilder();
/* 132 */     if (entityResolver != null) {
/* 133 */       docBuilder.setEntityResolver(entityResolver);
/*     */     }
/* 135 */     if (errorHandler != null) {
/* 136 */       docBuilder.setErrorHandler(errorHandler);
/*     */     }
/* 138 */     return docBuilder;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/xml/DefaultDocumentLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */