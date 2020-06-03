/*     */ package org.springframework.beans.factory.xml;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.parsing.EmptyReaderEventListener;
/*     */ import org.springframework.beans.factory.parsing.FailFastProblemReporter;
/*     */ import org.springframework.beans.factory.parsing.NullSourceExtractor;
/*     */ import org.springframework.beans.factory.parsing.ProblemReporter;
/*     */ import org.springframework.beans.factory.parsing.ReaderEventListener;
/*     */ import org.springframework.beans.factory.parsing.SourceExtractor;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinitionReader;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.core.Constants;
/*     */ import org.springframework.core.NamedThreadLocal;
/*     */ import org.springframework.core.io.DescriptiveResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.io.support.EncodedResource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.xml.SimpleSaxErrorHandler;
/*     */ import org.springframework.util.xml.XmlValidationModeDetector;
/*     */ import org.w3c.dom.Document;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XmlBeanDefinitionReader
/*     */   extends AbstractBeanDefinitionReader
/*     */ {
/*     */   public static final int VALIDATION_NONE = 0;
/*     */   public static final int VALIDATION_AUTO = 1;
/*     */   public static final int VALIDATION_DTD = 2;
/*     */   public static final int VALIDATION_XSD = 3;
/* 102 */   private static final Constants constants = new Constants(XmlBeanDefinitionReader.class);
/*     */   
/* 104 */   private int validationMode = 1;
/*     */   
/*     */   private boolean namespaceAware = false;
/*     */   
/* 108 */   private Class<? extends BeanDefinitionDocumentReader> documentReaderClass = (Class)DefaultBeanDefinitionDocumentReader.class;
/*     */ 
/*     */   
/* 111 */   private ProblemReporter problemReporter = (ProblemReporter)new FailFastProblemReporter();
/*     */   
/* 113 */   private ReaderEventListener eventListener = (ReaderEventListener)new EmptyReaderEventListener();
/*     */   
/* 115 */   private SourceExtractor sourceExtractor = (SourceExtractor)new NullSourceExtractor();
/*     */   
/*     */   @Nullable
/*     */   private NamespaceHandlerResolver namespaceHandlerResolver;
/*     */   
/* 120 */   private DocumentLoader documentLoader = new DefaultDocumentLoader();
/*     */   
/*     */   @Nullable
/*     */   private EntityResolver entityResolver;
/*     */   
/* 125 */   private ErrorHandler errorHandler = (ErrorHandler)new SimpleSaxErrorHandler(this.logger);
/*     */   
/* 127 */   private final XmlValidationModeDetector validationModeDetector = new XmlValidationModeDetector();
/*     */   
/* 129 */   private final ThreadLocal<Set<EncodedResource>> resourcesCurrentlyBeingLoaded = (ThreadLocal<Set<EncodedResource>>)new NamedThreadLocal("XML bean definition resources currently being loaded");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
/* 139 */     super(registry);
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
/*     */   public void setValidating(boolean validating) {
/* 151 */     this.validationMode = validating ? 1 : 0;
/* 152 */     this.namespaceAware = !validating;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValidationModeName(String validationModeName) {
/* 160 */     setValidationMode(constants.asNumber(validationModeName).intValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValidationMode(int validationMode) {
/* 170 */     this.validationMode = validationMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getValidationMode() {
/* 177 */     return this.validationMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNamespaceAware(boolean namespaceAware) {
/* 188 */     this.namespaceAware = namespaceAware;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNamespaceAware() {
/* 195 */     return this.namespaceAware;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProblemReporter(@Nullable ProblemReporter problemReporter) {
/* 205 */     this.problemReporter = (problemReporter != null) ? problemReporter : (ProblemReporter)new FailFastProblemReporter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEventListener(@Nullable ReaderEventListener eventListener) {
/* 215 */     this.eventListener = (eventListener != null) ? eventListener : (ReaderEventListener)new EmptyReaderEventListener();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSourceExtractor(@Nullable SourceExtractor sourceExtractor) {
/* 225 */     this.sourceExtractor = (sourceExtractor != null) ? sourceExtractor : (SourceExtractor)new NullSourceExtractor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNamespaceHandlerResolver(@Nullable NamespaceHandlerResolver namespaceHandlerResolver) {
/* 234 */     this.namespaceHandlerResolver = namespaceHandlerResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDocumentLoader(@Nullable DocumentLoader documentLoader) {
/* 243 */     this.documentLoader = (documentLoader != null) ? documentLoader : new DefaultDocumentLoader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEntityResolver(@Nullable EntityResolver entityResolver) {
/* 252 */     this.entityResolver = entityResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected EntityResolver getEntityResolver() {
/* 260 */     if (this.entityResolver == null) {
/*     */       
/* 262 */       ResourceLoader resourceLoader = getResourceLoader();
/* 263 */       if (resourceLoader != null) {
/* 264 */         this.entityResolver = new ResourceEntityResolver(resourceLoader);
/*     */       } else {
/*     */         
/* 267 */         this.entityResolver = new DelegatingEntityResolver(getBeanClassLoader());
/*     */       } 
/*     */     } 
/* 270 */     return this.entityResolver;
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
/*     */   public void setErrorHandler(ErrorHandler errorHandler) {
/* 282 */     this.errorHandler = errorHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDocumentReaderClass(Class<? extends BeanDefinitionDocumentReader> documentReaderClass) {
/* 292 */     this.documentReaderClass = documentReaderClass;
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
/*     */   public int loadBeanDefinitions(Resource resource) throws BeanDefinitionStoreException {
/* 304 */     return loadBeanDefinitions(new EncodedResource(resource));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int loadBeanDefinitions(EncodedResource encodedResource) throws BeanDefinitionStoreException {
/* 315 */     Assert.notNull(encodedResource, "EncodedResource must not be null");
/* 316 */     if (this.logger.isTraceEnabled()) {
/* 317 */       this.logger.trace("Loading XML bean definitions from " + encodedResource);
/*     */     }
/*     */     
/* 320 */     Set<EncodedResource> currentResources = this.resourcesCurrentlyBeingLoaded.get();
/* 321 */     if (currentResources == null) {
/* 322 */       currentResources = new HashSet<>(4);
/* 323 */       this.resourcesCurrentlyBeingLoaded.set(currentResources);
/*     */     } 
/* 325 */     if (!currentResources.add(encodedResource)) {
/* 326 */       throw new BeanDefinitionStoreException("Detected cyclic loading of " + encodedResource + " - check your import definitions!");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*     */     
/* 342 */     } catch (IOException ex) {
/* 343 */       throw new BeanDefinitionStoreException("IOException parsing XML document from " + encodedResource
/* 344 */           .getResource(), ex);
/*     */     } finally {
/*     */       
/* 347 */       currentResources.remove(encodedResource);
/* 348 */       if (currentResources.isEmpty()) {
/* 349 */         this.resourcesCurrentlyBeingLoaded.remove();
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
/*     */   public int loadBeanDefinitions(InputSource inputSource) throws BeanDefinitionStoreException {
/* 361 */     return loadBeanDefinitions(inputSource, "resource loaded through SAX InputSource");
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
/*     */   public int loadBeanDefinitions(InputSource inputSource, @Nullable String resourceDescription) throws BeanDefinitionStoreException {
/* 375 */     return doLoadBeanDefinitions(inputSource, (Resource)new DescriptiveResource(resourceDescription));
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
/*     */   protected int doLoadBeanDefinitions(InputSource inputSource, Resource resource) throws BeanDefinitionStoreException {
/*     */     try {
/* 392 */       Document doc = doLoadDocument(inputSource, resource);
/* 393 */       int count = registerBeanDefinitions(doc, resource);
/* 394 */       if (this.logger.isDebugEnabled()) {
/* 395 */         this.logger.debug("Loaded " + count + " bean definitions from " + resource);
/*     */       }
/* 397 */       return count;
/*     */     }
/* 399 */     catch (BeanDefinitionStoreException ex) {
/* 400 */       throw ex;
/*     */     }
/* 402 */     catch (SAXParseException ex) {
/* 403 */       throw new XmlBeanDefinitionStoreException(resource.getDescription(), "Line " + ex
/* 404 */           .getLineNumber() + " in XML document from " + resource + " is invalid", ex);
/*     */     }
/* 406 */     catch (SAXException ex) {
/* 407 */       throw new XmlBeanDefinitionStoreException(resource.getDescription(), "XML document from " + resource + " is invalid", ex);
/*     */     
/*     */     }
/* 410 */     catch (ParserConfigurationException ex) {
/* 411 */       throw new BeanDefinitionStoreException(resource.getDescription(), "Parser configuration exception parsing XML from " + resource, ex);
/*     */     
/*     */     }
/* 414 */     catch (IOException ex) {
/* 415 */       throw new BeanDefinitionStoreException(resource.getDescription(), "IOException parsing XML document from " + resource, ex);
/*     */     
/*     */     }
/* 418 */     catch (Throwable ex) {
/* 419 */       throw new BeanDefinitionStoreException(resource.getDescription(), "Unexpected exception parsing XML document from " + resource, ex);
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
/*     */   protected Document doLoadDocument(InputSource inputSource, Resource resource) throws Exception {
/* 434 */     return this.documentLoader.loadDocument(inputSource, getEntityResolver(), this.errorHandler, 
/* 435 */         getValidationModeForResource(resource), isNamespaceAware());
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
/*     */   protected int getValidationModeForResource(Resource resource) {
/* 447 */     int validationModeToUse = getValidationMode();
/* 448 */     if (validationModeToUse != 1) {
/* 449 */       return validationModeToUse;
/*     */     }
/* 451 */     int detectedMode = detectValidationMode(resource);
/* 452 */     if (detectedMode != 1) {
/* 453 */       return detectedMode;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 458 */     return 3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int detectValidationMode(Resource resource) {
/*     */     InputStream inputStream;
/* 469 */     if (resource.isOpen()) {
/* 470 */       throw new BeanDefinitionStoreException("Passed-in Resource [" + resource + "] contains an open stream: cannot determine validation mode automatically. Either pass in a Resource that is able to create fresh streams, or explicitly specify the validationMode on your XmlBeanDefinitionReader instance.");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 479 */       inputStream = resource.getInputStream();
/*     */     }
/* 481 */     catch (IOException ex) {
/* 482 */       throw new BeanDefinitionStoreException("Unable to determine validation mode for [" + resource + "]: cannot open InputStream. Did you attempt to load directly from a SAX InputSource without specifying the validationMode on your XmlBeanDefinitionReader instance?", ex);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 489 */       return this.validationModeDetector.detectValidationMode(inputStream);
/*     */     }
/* 491 */     catch (IOException ex) {
/* 492 */       throw new BeanDefinitionStoreException("Unable to determine validation mode for [" + resource + "]: an error occurred whilst reading from the InputStream.", ex);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public int registerBeanDefinitions(Document doc, Resource resource) throws BeanDefinitionStoreException {
/* 511 */     BeanDefinitionDocumentReader documentReader = createBeanDefinitionDocumentReader();
/* 512 */     int countBefore = getRegistry().getBeanDefinitionCount();
/* 513 */     documentReader.registerBeanDefinitions(doc, createReaderContext(resource));
/* 514 */     return getRegistry().getBeanDefinitionCount() - countBefore;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanDefinitionDocumentReader createBeanDefinitionDocumentReader() {
/* 524 */     return (BeanDefinitionDocumentReader)BeanUtils.instantiateClass(this.documentReaderClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlReaderContext createReaderContext(Resource resource) {
/* 531 */     return new XmlReaderContext(resource, this.problemReporter, this.eventListener, this.sourceExtractor, this, 
/* 532 */         getNamespaceHandlerResolver());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NamespaceHandlerResolver getNamespaceHandlerResolver() {
/* 540 */     if (this.namespaceHandlerResolver == null) {
/* 541 */       this.namespaceHandlerResolver = createDefaultNamespaceHandlerResolver();
/*     */     }
/* 543 */     return this.namespaceHandlerResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected NamespaceHandlerResolver createDefaultNamespaceHandlerResolver() {
/* 552 */     ClassLoader cl = (getResourceLoader() != null) ? getResourceLoader().getClassLoader() : getBeanClassLoader();
/* 553 */     return new DefaultNamespaceHandlerResolver(cl);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/xml/XmlBeanDefinitionReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */