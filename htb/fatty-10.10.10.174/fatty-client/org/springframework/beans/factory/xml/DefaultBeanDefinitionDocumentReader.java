/*     */ package org.springframework.beans.factory.xml;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.ComponentDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.support.ResourcePatternUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ResourceUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultBeanDefinitionDocumentReader
/*     */   implements BeanDefinitionDocumentReader
/*     */ {
/*     */   public static final String BEAN_ELEMENT = "bean";
/*     */   public static final String NESTED_BEANS_ELEMENT = "beans";
/*     */   public static final String ALIAS_ELEMENT = "alias";
/*     */   public static final String NAME_ATTRIBUTE = "name";
/*     */   public static final String ALIAS_ATTRIBUTE = "alias";
/*     */   public static final String IMPORT_ELEMENT = "import";
/*     */   public static final String RESOURCE_ATTRIBUTE = "resource";
/*     */   public static final String PROFILE_ATTRIBUTE = "profile";
/*  78 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private XmlReaderContext readerContext;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private BeanDefinitionParserDelegate delegate;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerBeanDefinitions(Document doc, XmlReaderContext readerContext) {
/*  95 */     this.readerContext = readerContext;
/*  96 */     doRegisterBeanDefinitions(doc.getDocumentElement());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final XmlReaderContext getReaderContext() {
/* 103 */     Assert.state((this.readerContext != null), "No XmlReaderContext available");
/* 104 */     return this.readerContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object extractSource(Element ele) {
/* 113 */     return getReaderContext().extractSource(ele);
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
/*     */   protected void doRegisterBeanDefinitions(Element root) {
/* 128 */     BeanDefinitionParserDelegate parent = this.delegate;
/* 129 */     this.delegate = createDelegate(getReaderContext(), root, parent);
/*     */     
/* 131 */     if (this.delegate.isDefaultNamespace(root)) {
/* 132 */       String profileSpec = root.getAttribute("profile");
/* 133 */       if (StringUtils.hasText(profileSpec)) {
/* 134 */         String[] specifiedProfiles = StringUtils.tokenizeToStringArray(profileSpec, ",; ");
/*     */ 
/*     */ 
/*     */         
/* 138 */         if (!getReaderContext().getEnvironment().acceptsProfiles(specifiedProfiles)) {
/* 139 */           if (this.logger.isDebugEnabled()) {
/* 140 */             this.logger.debug("Skipped XML bean definition file due to specified profiles [" + profileSpec + "] not matching: " + 
/* 141 */                 getReaderContext().getResource());
/*     */           }
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/* 148 */     preProcessXml(root);
/* 149 */     parseBeanDefinitions(root, this.delegate);
/* 150 */     postProcessXml(root);
/*     */     
/* 152 */     this.delegate = parent;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanDefinitionParserDelegate createDelegate(XmlReaderContext readerContext, Element root, @Nullable BeanDefinitionParserDelegate parentDelegate) {
/* 158 */     BeanDefinitionParserDelegate delegate = new BeanDefinitionParserDelegate(readerContext);
/* 159 */     delegate.initDefaults(root, parentDelegate);
/* 160 */     return delegate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void parseBeanDefinitions(Element root, BeanDefinitionParserDelegate delegate) {
/* 169 */     if (delegate.isDefaultNamespace(root)) {
/* 170 */       NodeList nl = root.getChildNodes();
/* 171 */       for (int i = 0; i < nl.getLength(); i++) {
/* 172 */         Node node = nl.item(i);
/* 173 */         if (node instanceof Element) {
/* 174 */           Element ele = (Element)node;
/* 175 */           if (delegate.isDefaultNamespace(ele)) {
/* 176 */             parseDefaultElement(ele, delegate);
/*     */           } else {
/*     */             
/* 179 */             delegate.parseCustomElement(ele);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 185 */       delegate.parseCustomElement(root);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void parseDefaultElement(Element ele, BeanDefinitionParserDelegate delegate) {
/* 190 */     if (delegate.nodeNameEquals(ele, "import")) {
/* 191 */       importBeanDefinitionResource(ele);
/*     */     }
/* 193 */     else if (delegate.nodeNameEquals(ele, "alias")) {
/* 194 */       processAliasRegistration(ele);
/*     */     }
/* 196 */     else if (delegate.nodeNameEquals(ele, "bean")) {
/* 197 */       processBeanDefinition(ele, delegate);
/*     */     }
/* 199 */     else if (delegate.nodeNameEquals(ele, "beans")) {
/*     */       
/* 201 */       doRegisterBeanDefinitions(ele);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void importBeanDefinitionResource(Element ele) {
/* 210 */     String location = ele.getAttribute("resource");
/* 211 */     if (!StringUtils.hasText(location)) {
/* 212 */       getReaderContext().error("Resource location must not be empty", ele);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 217 */     location = getReaderContext().getEnvironment().resolveRequiredPlaceholders(location);
/*     */     
/* 219 */     Set<Resource> actualResources = new LinkedHashSet<>(4);
/*     */ 
/*     */     
/* 222 */     boolean absoluteLocation = false;
/*     */     try {
/* 224 */       absoluteLocation = (ResourcePatternUtils.isUrl(location) || ResourceUtils.toURI(location).isAbsolute());
/*     */     }
/* 226 */     catch (URISyntaxException uRISyntaxException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 232 */     if (absoluteLocation) {
/*     */       try {
/* 234 */         int importCount = getReaderContext().getReader().loadBeanDefinitions(location, actualResources);
/* 235 */         if (this.logger.isTraceEnabled()) {
/* 236 */           this.logger.trace("Imported " + importCount + " bean definitions from URL location [" + location + "]");
/*     */         }
/*     */       }
/* 239 */       catch (BeanDefinitionStoreException ex) {
/* 240 */         getReaderContext().error("Failed to import bean definitions from URL location [" + location + "]", ele, (Throwable)ex);
/*     */       } 
/*     */     } else {
/*     */       try {
/*     */         int importCount;
/*     */ 
/*     */ 
/*     */         
/* 248 */         Resource relativeResource = getReaderContext().getResource().createRelative(location);
/* 249 */         if (relativeResource.exists()) {
/* 250 */           importCount = getReaderContext().getReader().loadBeanDefinitions(relativeResource);
/* 251 */           actualResources.add(relativeResource);
/*     */         } else {
/*     */           
/* 254 */           String baseLocation = getReaderContext().getResource().getURL().toString();
/* 255 */           importCount = getReaderContext().getReader().loadBeanDefinitions(
/* 256 */               StringUtils.applyRelativePath(baseLocation, location), actualResources);
/*     */         } 
/* 258 */         if (this.logger.isTraceEnabled()) {
/* 259 */           this.logger.trace("Imported " + importCount + " bean definitions from relative location [" + location + "]");
/*     */         }
/*     */       }
/* 262 */       catch (IOException ex) {
/* 263 */         getReaderContext().error("Failed to resolve current resource location", ele, ex);
/*     */       }
/* 265 */       catch (BeanDefinitionStoreException ex) {
/* 266 */         getReaderContext().error("Failed to import bean definitions from relative location [" + location + "]", ele, (Throwable)ex);
/*     */       } 
/*     */     } 
/*     */     
/* 270 */     Resource[] actResArray = actualResources.<Resource>toArray(new Resource[0]);
/* 271 */     getReaderContext().fireImportProcessed(location, actResArray, extractSource(ele));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processAliasRegistration(Element ele) {
/* 278 */     String name = ele.getAttribute("name");
/* 279 */     String alias = ele.getAttribute("alias");
/* 280 */     boolean valid = true;
/* 281 */     if (!StringUtils.hasText(name)) {
/* 282 */       getReaderContext().error("Name must not be empty", ele);
/* 283 */       valid = false;
/*     */     } 
/* 285 */     if (!StringUtils.hasText(alias)) {
/* 286 */       getReaderContext().error("Alias must not be empty", ele);
/* 287 */       valid = false;
/*     */     } 
/* 289 */     if (valid) {
/*     */       try {
/* 291 */         getReaderContext().getRegistry().registerAlias(name, alias);
/*     */       }
/* 293 */       catch (Exception ex) {
/* 294 */         getReaderContext().error("Failed to register alias '" + alias + "' for bean with name '" + name + "'", ele, ex);
/*     */       } 
/*     */       
/* 297 */       getReaderContext().fireAliasRegistered(name, alias, extractSource(ele));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processBeanDefinition(Element ele, BeanDefinitionParserDelegate delegate) {
/* 306 */     BeanDefinitionHolder bdHolder = delegate.parseBeanDefinitionElement(ele);
/* 307 */     if (bdHolder != null) {
/* 308 */       bdHolder = delegate.decorateBeanDefinitionIfRequired(ele, bdHolder);
/*     */       
/*     */       try {
/* 311 */         BeanDefinitionReaderUtils.registerBeanDefinition(bdHolder, getReaderContext().getRegistry());
/*     */       }
/* 313 */       catch (BeanDefinitionStoreException ex) {
/* 314 */         getReaderContext().error("Failed to register bean definition with name '" + bdHolder
/* 315 */             .getBeanName() + "'", ele, (Throwable)ex);
/*     */       } 
/*     */       
/* 318 */       getReaderContext().fireComponentRegistered((ComponentDefinition)new BeanComponentDefinition(bdHolder));
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void preProcessXml(Element root) {}
/*     */   
/*     */   protected void postProcessXml(Element root) {}
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/xml/DefaultBeanDefinitionDocumentReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */