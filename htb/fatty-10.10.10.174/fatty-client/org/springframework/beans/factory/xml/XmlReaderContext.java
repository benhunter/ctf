/*     */ package org.springframework.beans.factory.xml;
/*     */ 
/*     */ import java.io.StringReader;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.parsing.ProblemReporter;
/*     */ import org.springframework.beans.factory.parsing.ReaderContext;
/*     */ import org.springframework.beans.factory.parsing.ReaderEventListener;
/*     */ import org.springframework.beans.factory.parsing.SourceExtractor;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.w3c.dom.Document;
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
/*     */ public class XmlReaderContext
/*     */   extends ReaderContext
/*     */ {
/*     */   private final XmlBeanDefinitionReader reader;
/*     */   private final NamespaceHandlerResolver namespaceHandlerResolver;
/*     */   
/*     */   public XmlReaderContext(Resource resource, ProblemReporter problemReporter, ReaderEventListener eventListener, SourceExtractor sourceExtractor, XmlBeanDefinitionReader reader, NamespaceHandlerResolver namespaceHandlerResolver) {
/*  66 */     super(resource, problemReporter, eventListener, sourceExtractor);
/*  67 */     this.reader = reader;
/*  68 */     this.namespaceHandlerResolver = namespaceHandlerResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final XmlBeanDefinitionReader getReader() {
/*  76 */     return this.reader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final BeanDefinitionRegistry getRegistry() {
/*  84 */     return this.reader.getRegistry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final ResourceLoader getResourceLoader() {
/*  96 */     return this.reader.getResourceLoader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final ClassLoader getBeanClassLoader() {
/* 107 */     return this.reader.getBeanClassLoader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Environment getEnvironment() {
/* 115 */     return this.reader.getEnvironment();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final NamespaceHandlerResolver getNamespaceHandlerResolver() {
/* 123 */     return this.namespaceHandlerResolver;
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
/*     */   public String generateBeanName(BeanDefinition beanDefinition) {
/* 135 */     return this.reader.getBeanNameGenerator().generateBeanName(beanDefinition, getRegistry());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String registerWithGeneratedName(BeanDefinition beanDefinition) {
/* 146 */     String generatedName = generateBeanName(beanDefinition);
/* 147 */     getRegistry().registerBeanDefinition(generatedName, beanDefinition);
/* 148 */     return generatedName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Document readDocumentFromString(String documentContent) {
/* 156 */     InputSource is = new InputSource(new StringReader(documentContent));
/*     */     try {
/* 158 */       return this.reader.doLoadDocument(is, getResource());
/*     */     }
/* 160 */     catch (Exception ex) {
/* 161 */       throw new BeanDefinitionStoreException("Failed to read XML document", ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/xml/XmlReaderContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */