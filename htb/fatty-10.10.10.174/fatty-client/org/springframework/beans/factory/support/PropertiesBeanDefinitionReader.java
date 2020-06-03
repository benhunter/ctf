/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.ResourceBundle;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.CannotLoadBeanClassException;
/*     */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*     */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.support.EncodedResource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.DefaultPropertiesPersister;
/*     */ import org.springframework.util.PropertiesPersister;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PropertiesBeanDefinitionReader
/*     */   extends AbstractBeanDefinitionReader
/*     */ {
/*     */   public static final String TRUE_VALUE = "true";
/*     */   public static final String SEPARATOR = ".";
/*     */   public static final String CLASS_KEY = "(class)";
/*     */   public static final String PARENT_KEY = "(parent)";
/*     */   public static final String SCOPE_KEY = "(scope)";
/*     */   public static final String SINGLETON_KEY = "(singleton)";
/*     */   public static final String ABSTRACT_KEY = "(abstract)";
/*     */   public static final String LAZY_INIT_KEY = "(lazy-init)";
/*     */   public static final String REF_SUFFIX = "(ref)";
/*     */   public static final String REF_PREFIX = "*";
/*     */   public static final String CONSTRUCTOR_ARG_PREFIX = "$";
/*     */   @Nullable
/*     */   private String defaultParentBean;
/* 147 */   private PropertiesPersister propertiesPersister = (PropertiesPersister)new DefaultPropertiesPersister();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertiesBeanDefinitionReader(BeanDefinitionRegistry registry) {
/* 156 */     super(registry);
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
/*     */   public void setDefaultParentBean(@Nullable String defaultParentBean) {
/* 173 */     this.defaultParentBean = defaultParentBean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getDefaultParentBean() {
/* 181 */     return this.defaultParentBean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPropertiesPersister(@Nullable PropertiesPersister propertiesPersister) {
/* 190 */     this.propertiesPersister = (propertiesPersister != null) ? propertiesPersister : (PropertiesPersister)new DefaultPropertiesPersister();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertiesPersister getPropertiesPersister() {
/* 198 */     return this.propertiesPersister;
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
/*     */   public int loadBeanDefinitions(Resource resource) throws BeanDefinitionStoreException {
/* 212 */     return loadBeanDefinitions(new EncodedResource(resource), (String)null);
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
/*     */   public int loadBeanDefinitions(Resource resource, @Nullable String prefix) throws BeanDefinitionStoreException {
/* 224 */     return loadBeanDefinitions(new EncodedResource(resource), prefix);
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
/* 235 */     return loadBeanDefinitions(encodedResource, (String)null);
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
/*     */   public int loadBeanDefinitions(EncodedResource encodedResource, @Nullable String prefix) throws BeanDefinitionStoreException {
/* 250 */     if (this.logger.isTraceEnabled()) {
/* 251 */       this.logger.trace("Loading properties bean definitions from " + encodedResource);
/*     */     }
/*     */     
/* 254 */     Properties props = new Properties();
/*     */     try {
/* 256 */       try (InputStream is = encodedResource.getResource().getInputStream()) {
/* 257 */         if (encodedResource.getEncoding() != null) {
/* 258 */           getPropertiesPersister().load(props, new InputStreamReader(is, encodedResource.getEncoding()));
/*     */         } else {
/*     */           
/* 261 */           getPropertiesPersister().load(props, is);
/*     */         } 
/*     */       } 
/*     */       
/* 265 */       int count = registerBeanDefinitions(props, prefix, encodedResource.getResource().getDescription());
/* 266 */       if (this.logger.isDebugEnabled()) {
/* 267 */         this.logger.debug("Loaded " + count + " bean definitions from " + encodedResource);
/*     */       }
/* 269 */       return count;
/*     */     }
/* 271 */     catch (IOException ex) {
/* 272 */       throw new BeanDefinitionStoreException("Could not parse properties from " + encodedResource.getResource(), ex);
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
/*     */   public int registerBeanDefinitions(ResourceBundle rb) throws BeanDefinitionStoreException {
/* 285 */     return registerBeanDefinitions(rb, (String)null);
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
/*     */   public int registerBeanDefinitions(ResourceBundle rb, @Nullable String prefix) throws BeanDefinitionStoreException {
/* 300 */     Map<String, Object> map = new HashMap<>();
/* 301 */     Enumeration<String> keys = rb.getKeys();
/* 302 */     while (keys.hasMoreElements()) {
/* 303 */       String key = keys.nextElement();
/* 304 */       map.put(key, rb.getObject(key));
/*     */     } 
/* 306 */     return registerBeanDefinitions(map, prefix);
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
/*     */   public int registerBeanDefinitions(Map<?, ?> map) throws BeansException {
/* 321 */     return registerBeanDefinitions(map, (String)null);
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
/*     */   public int registerBeanDefinitions(Map<?, ?> map, @Nullable String prefix) throws BeansException {
/* 336 */     return registerBeanDefinitions(map, prefix, "Map " + map);
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
/*     */   public int registerBeanDefinitions(Map<?, ?> map, @Nullable String prefix, String resourceDescription) throws BeansException {
/* 356 */     if (prefix == null) {
/* 357 */       prefix = "";
/*     */     }
/* 359 */     int beanCount = 0;
/*     */     
/* 361 */     for (Object key : map.keySet()) {
/* 362 */       if (!(key instanceof String)) {
/* 363 */         throw new IllegalArgumentException("Illegal key [" + key + "]: only Strings allowed");
/*     */       }
/* 365 */       String keyString = (String)key;
/* 366 */       if (keyString.startsWith(prefix)) {
/*     */         
/* 368 */         String nameAndProperty = keyString.substring(prefix.length());
/*     */         
/* 370 */         int sepIdx = -1;
/* 371 */         int propKeyIdx = nameAndProperty.indexOf("[");
/* 372 */         if (propKeyIdx != -1) {
/* 373 */           sepIdx = nameAndProperty.lastIndexOf(".", propKeyIdx);
/*     */         } else {
/*     */           
/* 376 */           sepIdx = nameAndProperty.lastIndexOf(".");
/*     */         } 
/* 378 */         if (sepIdx != -1) {
/* 379 */           String beanName = nameAndProperty.substring(0, sepIdx);
/* 380 */           if (this.logger.isTraceEnabled()) {
/* 381 */             this.logger.trace("Found bean name '" + beanName + "'");
/*     */           }
/* 383 */           if (!getRegistry().containsBeanDefinition(beanName)) {
/*     */             
/* 385 */             registerBeanDefinition(beanName, map, prefix + beanName, resourceDescription);
/* 386 */             beanCount++;
/*     */           } 
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/* 392 */         if (this.logger.isDebugEnabled()) {
/* 393 */           this.logger.debug("Invalid bean name and property [" + nameAndProperty + "]");
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 399 */     return beanCount;
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
/*     */   protected void registerBeanDefinition(String beanName, Map<?, ?> map, String prefix, String resourceDescription) throws BeansException {
/* 415 */     String className = null;
/* 416 */     String parent = null;
/* 417 */     String scope = "singleton";
/* 418 */     boolean isAbstract = false;
/* 419 */     boolean lazyInit = false;
/*     */     
/* 421 */     ConstructorArgumentValues cas = new ConstructorArgumentValues();
/* 422 */     MutablePropertyValues pvs = new MutablePropertyValues();
/*     */     
/* 424 */     for (Map.Entry<?, ?> entry : map.entrySet()) {
/* 425 */       String key = StringUtils.trimWhitespace((String)entry.getKey());
/* 426 */       if (key.startsWith(prefix + ".")) {
/* 427 */         String property = key.substring(prefix.length() + ".".length());
/* 428 */         if ("(class)".equals(property)) {
/* 429 */           className = StringUtils.trimWhitespace((String)entry.getValue()); continue;
/*     */         } 
/* 431 */         if ("(parent)".equals(property)) {
/* 432 */           parent = StringUtils.trimWhitespace((String)entry.getValue()); continue;
/*     */         } 
/* 434 */         if ("(abstract)".equals(property)) {
/* 435 */           String val = StringUtils.trimWhitespace((String)entry.getValue());
/* 436 */           isAbstract = "true".equals(val); continue;
/*     */         } 
/* 438 */         if ("(scope)".equals(property)) {
/*     */           
/* 440 */           scope = StringUtils.trimWhitespace((String)entry.getValue()); continue;
/*     */         } 
/* 442 */         if ("(singleton)".equals(property)) {
/*     */           
/* 444 */           String val = StringUtils.trimWhitespace((String)entry.getValue());
/* 445 */           scope = ("".equals(val) || "true".equals(val)) ? "singleton" : "prototype";
/*     */           continue;
/*     */         } 
/* 448 */         if ("(lazy-init)".equals(property)) {
/* 449 */           String val = StringUtils.trimWhitespace((String)entry.getValue());
/* 450 */           lazyInit = "true".equals(val); continue;
/*     */         } 
/* 452 */         if (property.startsWith("$")) {
/* 453 */           if (property.endsWith("(ref)")) {
/* 454 */             int i = Integer.parseInt(property.substring(1, property.length() - "(ref)".length()));
/* 455 */             cas.addIndexedArgumentValue(i, new RuntimeBeanReference(entry.getValue().toString()));
/*     */             continue;
/*     */           } 
/* 458 */           int index = Integer.parseInt(property.substring(1));
/* 459 */           cas.addIndexedArgumentValue(index, readValue(entry));
/*     */           continue;
/*     */         } 
/* 462 */         if (property.endsWith("(ref)")) {
/*     */ 
/*     */           
/* 465 */           property = property.substring(0, property.length() - "(ref)".length());
/* 466 */           String ref = StringUtils.trimWhitespace((String)entry.getValue());
/*     */ 
/*     */ 
/*     */           
/* 470 */           Object val = new RuntimeBeanReference(ref);
/* 471 */           pvs.add(property, val);
/*     */           
/*     */           continue;
/*     */         } 
/* 475 */         pvs.add(property, readValue(entry));
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 480 */     if (this.logger.isTraceEnabled()) {
/* 481 */       this.logger.trace("Registering bean definition for bean name '" + beanName + "' with " + pvs);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 487 */     if (parent == null && className == null && !beanName.equals(this.defaultParentBean)) {
/* 488 */       parent = this.defaultParentBean;
/*     */     }
/*     */     
/*     */     try {
/* 492 */       AbstractBeanDefinition bd = BeanDefinitionReaderUtils.createBeanDefinition(parent, className, 
/* 493 */           getBeanClassLoader());
/* 494 */       bd.setScope(scope);
/* 495 */       bd.setAbstract(isAbstract);
/* 496 */       bd.setLazyInit(lazyInit);
/* 497 */       bd.setConstructorArgumentValues(cas);
/* 498 */       bd.setPropertyValues(pvs);
/* 499 */       getRegistry().registerBeanDefinition(beanName, bd);
/*     */     }
/* 501 */     catch (ClassNotFoundException ex) {
/* 502 */       throw new CannotLoadBeanClassException(resourceDescription, beanName, className, ex);
/*     */     }
/* 504 */     catch (LinkageError err) {
/* 505 */       throw new CannotLoadBeanClassException(resourceDescription, beanName, className, err);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object readValue(Map.Entry<?, ?> entry) {
/* 514 */     Object val = entry.getValue();
/* 515 */     if (val instanceof String) {
/* 516 */       String strVal = (String)val;
/*     */       
/* 518 */       if (strVal.startsWith("*")) {
/*     */         
/* 520 */         String targetName = strVal.substring(1);
/* 521 */         if (targetName.startsWith("*")) {
/*     */           
/* 523 */           val = targetName;
/*     */         } else {
/*     */           
/* 526 */           val = new RuntimeBeanReference(targetName);
/*     */         } 
/*     */       } 
/*     */     } 
/* 530 */     return val;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/PropertiesBeanDefinitionReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */