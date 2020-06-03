/*     */ package org.springframework.web.servlet.view.xslt;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.xml.transform.ErrorListener;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.Templates;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerConfigurationException;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.TransformerFactoryConfigurationError;
/*     */ import javax.xml.transform.URIResolver;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.context.ApplicationContextException;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.util.xml.SimpleTransformErrorListener;
/*     */ import org.springframework.util.xml.TransformerUtils;
/*     */ import org.springframework.web.servlet.view.AbstractUrlBasedView;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XsltView
/*     */   extends AbstractUrlBasedView
/*     */ {
/*     */   @Nullable
/*     */   private Class<? extends TransformerFactory> transformerFactoryClass;
/*     */   @Nullable
/*     */   private String sourceKey;
/*     */   @Nullable
/*     */   private URIResolver uriResolver;
/*  87 */   private ErrorListener errorListener = (ErrorListener)new SimpleTransformErrorListener(this.logger);
/*     */ 
/*     */   
/*     */   private boolean indent = true;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Properties outputProperties;
/*     */ 
/*     */   
/*     */   private boolean cacheTemplates = true;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private TransformerFactory transformerFactory;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Templates cachedTemplates;
/*     */ 
/*     */   
/*     */   public void setTransformerFactoryClass(Class<? extends TransformerFactory> transformerFactoryClass) {
/* 109 */     this.transformerFactoryClass = transformerFactoryClass;
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
/*     */   public void setSourceKey(String sourceKey) {
/* 122 */     this.sourceKey = sourceKey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUriResolver(URIResolver uriResolver) {
/* 130 */     this.uriResolver = uriResolver;
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
/*     */   public void setErrorListener(@Nullable ErrorListener errorListener) {
/* 143 */     this.errorListener = (errorListener != null) ? errorListener : (ErrorListener)new SimpleTransformErrorListener(this.logger);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIndent(boolean indent) {
/* 154 */     this.indent = indent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOutputProperties(Properties outputProperties) {
/* 164 */     this.outputProperties = outputProperties;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCacheTemplates(boolean cacheTemplates) {
/* 173 */     this.cacheTemplates = cacheTemplates;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initApplicationContext() throws BeansException {
/* 182 */     this.transformerFactory = newTransformerFactory(this.transformerFactoryClass);
/* 183 */     this.transformerFactory.setErrorListener(this.errorListener);
/* 184 */     if (this.uriResolver != null) {
/* 185 */       this.transformerFactory.setURIResolver(this.uriResolver);
/*     */     }
/* 187 */     if (this.cacheTemplates) {
/* 188 */       this.cachedTemplates = loadTemplates();
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
/*     */   
/*     */   protected TransformerFactory newTransformerFactory(@Nullable Class<? extends TransformerFactory> transformerFactoryClass) {
/* 208 */     if (transformerFactoryClass != null) {
/*     */       try {
/* 210 */         return ReflectionUtils.accessibleConstructor(transformerFactoryClass, new Class[0]).newInstance(new Object[0]);
/*     */       }
/* 212 */       catch (Exception ex) {
/* 213 */         throw new TransformerFactoryConfigurationError(ex, "Could not instantiate TransformerFactory");
/*     */       } 
/*     */     }
/*     */     
/* 217 */     return TransformerFactory.newInstance();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final TransformerFactory getTransformerFactory() {
/* 226 */     Assert.state((this.transformerFactory != null), "No TransformerFactory available");
/* 227 */     return this.transformerFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 236 */     Templates templates = this.cachedTemplates;
/* 237 */     if (templates == null) {
/* 238 */       templates = loadTemplates();
/*     */     }
/*     */     
/* 241 */     Transformer transformer = createTransformer(templates);
/* 242 */     configureTransformer(model, response, transformer);
/* 243 */     configureResponse(model, response, transformer);
/* 244 */     Source source = null;
/*     */     try {
/* 246 */       source = locateSource(model);
/* 247 */       if (source == null) {
/* 248 */         throw new IllegalArgumentException("Unable to locate Source object in model: " + model);
/*     */       }
/* 250 */       transformer.transform(source, createResult(response));
/*     */     } finally {
/*     */       
/* 253 */       closeSourceIfNecessary(source);
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
/*     */   protected Result createResult(HttpServletResponse response) throws Exception {
/* 266 */     return new StreamResult((OutputStream)response.getOutputStream());
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
/*     */   @Nullable
/*     */   protected Source locateSource(Map<String, Object> model) throws Exception {
/* 283 */     if (this.sourceKey != null) {
/* 284 */       return convertSource(model.get(this.sourceKey));
/*     */     }
/* 286 */     Object source = CollectionUtils.findValueOfType(model.values(), getSourceTypes());
/* 287 */     return (source != null) ? convertSource(source) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<?>[] getSourceTypes() {
/* 298 */     return new Class[] { Source.class, Document.class, Node.class, Reader.class, InputStream.class, Resource.class };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Source convertSource(Object source) throws Exception {
/* 309 */     if (source instanceof Source) {
/* 310 */       return (Source)source;
/*     */     }
/* 312 */     if (source instanceof Document) {
/* 313 */       return new DOMSource(((Document)source).getDocumentElement());
/*     */     }
/* 315 */     if (source instanceof Node) {
/* 316 */       return new DOMSource((Node)source);
/*     */     }
/* 318 */     if (source instanceof Reader) {
/* 319 */       return new StreamSource((Reader)source);
/*     */     }
/* 321 */     if (source instanceof InputStream) {
/* 322 */       return new StreamSource((InputStream)source);
/*     */     }
/* 324 */     if (source instanceof Resource) {
/* 325 */       Resource resource = (Resource)source;
/* 326 */       return new StreamSource(resource.getInputStream(), resource.getURI().toASCIIString());
/*     */     } 
/*     */     
/* 329 */     throw new IllegalArgumentException("Value '" + source + "' cannot be converted to XSLT Source");
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected void configureTransformer(Map<String, Object> model, HttpServletResponse response, Transformer transformer) {
/* 350 */     copyModelParameters(model, transformer);
/* 351 */     copyOutputProperties(transformer);
/* 352 */     configureIndentation(transformer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void configureIndentation(Transformer transformer) {
/* 362 */     if (this.indent) {
/* 363 */       TransformerUtils.enableIndenting(transformer);
/*     */     } else {
/*     */       
/* 366 */       TransformerUtils.disableIndenting(transformer);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void copyOutputProperties(Transformer transformer) {
/* 377 */     if (this.outputProperties != null) {
/* 378 */       Enumeration<?> en = this.outputProperties.propertyNames();
/* 379 */       while (en.hasMoreElements()) {
/* 380 */         String name = (String)en.nextElement();
/* 381 */         transformer.setOutputProperty(name, this.outputProperties.getProperty(name));
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
/*     */   protected final void copyModelParameters(Map<String, Object> model, Transformer transformer) {
/* 394 */     model.forEach(transformer::setParameter);
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
/*     */   protected void configureResponse(Map<String, Object> model, HttpServletResponse response, Transformer transformer) {
/* 409 */     String contentType = getContentType();
/* 410 */     String mediaType = transformer.getOutputProperty("media-type");
/* 411 */     String encoding = transformer.getOutputProperty("encoding");
/* 412 */     if (StringUtils.hasText(mediaType)) {
/* 413 */       contentType = mediaType;
/*     */     }
/* 415 */     if (StringUtils.hasText(encoding))
/*     */     {
/* 417 */       if (contentType != null && !contentType.toLowerCase().contains(";charset=")) {
/* 418 */         contentType = contentType + ";charset=" + encoding;
/*     */       }
/*     */     }
/* 421 */     response.setContentType(contentType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Templates loadTemplates() throws ApplicationContextException {
/* 428 */     Source stylesheetSource = getStylesheetSource();
/*     */     try {
/* 430 */       Templates templates = getTransformerFactory().newTemplates(stylesheetSource);
/* 431 */       return templates;
/*     */     }
/* 433 */     catch (TransformerConfigurationException ex) {
/* 434 */       throw new ApplicationContextException("Can't load stylesheet from '" + getUrl() + "'", ex);
/*     */     } finally {
/*     */       
/* 437 */       closeSourceIfNecessary(stylesheetSource);
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
/*     */   protected Transformer createTransformer(Templates templates) throws TransformerConfigurationException {
/* 450 */     Transformer transformer = templates.newTransformer();
/* 451 */     if (this.uriResolver != null) {
/* 452 */       transformer.setURIResolver(this.uriResolver);
/*     */     }
/* 454 */     return transformer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Source getStylesheetSource() {
/* 462 */     String url = getUrl();
/* 463 */     Assert.state((url != null), "'url' not set");
/*     */     
/* 465 */     if (this.logger.isDebugEnabled()) {
/* 466 */       this.logger.debug("Applying stylesheet [" + url + "]");
/*     */     }
/*     */     try {
/* 469 */       Resource resource = obtainApplicationContext().getResource(url);
/* 470 */       return new StreamSource(resource.getInputStream(), resource.getURI().toASCIIString());
/*     */     }
/* 472 */     catch (IOException ex) {
/* 473 */       throw new ApplicationContextException("Can't load XSLT stylesheet from '" + url + "'", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void closeSourceIfNecessary(@Nullable Source source) {
/* 483 */     if (source instanceof StreamSource) {
/* 484 */       StreamSource streamSource = (StreamSource)source;
/* 485 */       if (streamSource.getReader() != null) {
/*     */         try {
/* 487 */           streamSource.getReader().close();
/*     */         }
/* 489 */         catch (IOException iOException) {}
/*     */       }
/*     */ 
/*     */       
/* 493 */       if (streamSource.getInputStream() != null)
/*     */         try {
/* 495 */           streamSource.getInputStream().close();
/*     */         }
/* 497 */         catch (IOException iOException) {} 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/xslt/XsltView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */