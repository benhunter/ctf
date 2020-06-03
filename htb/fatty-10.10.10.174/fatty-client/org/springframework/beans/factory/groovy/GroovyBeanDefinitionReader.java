/*     */ package org.springframework.beans.factory.groovy;
/*     */ 
/*     */ import groovy.lang.Binding;
/*     */ import groovy.lang.Closure;
/*     */ import groovy.lang.GroovyObject;
/*     */ import groovy.lang.GroovyObjectSupport;
/*     */ import groovy.lang.GroovyShell;
/*     */ import groovy.lang.GroovySystem;
/*     */ import groovy.lang.MetaClass;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.codehaus.groovy.runtime.DefaultGroovyMethods;
/*     */ import org.codehaus.groovy.runtime.InvokerHelper;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*     */ import org.springframework.beans.factory.parsing.BeanDefinitionParsingException;
/*     */ import org.springframework.beans.factory.parsing.Location;
/*     */ import org.springframework.beans.factory.parsing.Problem;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinitionReader;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.GenericBeanDefinition;
/*     */ import org.springframework.beans.factory.support.ManagedList;
/*     */ import org.springframework.beans.factory.support.ManagedMap;
/*     */ import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
/*     */ import org.springframework.beans.factory.xml.NamespaceHandler;
/*     */ import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
/*     */ import org.springframework.beans.factory.xml.XmlReaderContext;
/*     */ import org.springframework.core.io.DescriptiveResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.support.EncodedResource;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ public class GroovyBeanDefinitionReader
/*     */   extends AbstractBeanDefinitionReader
/*     */   implements GroovyObject
/*     */ {
/*     */   private final XmlBeanDefinitionReader standardXmlBeanDefinitionReader;
/*     */   private final XmlBeanDefinitionReader groovyDslXmlBeanDefinitionReader;
/* 146 */   private final Map<String, String> namespaces = new HashMap<>();
/*     */   
/* 148 */   private final Map<String, DeferredProperty> deferredProperties = new HashMap<>();
/*     */   
/* 150 */   private MetaClass metaClass = GroovySystem.getMetaClassRegistry().getMetaClass(getClass());
/*     */ 
/*     */ 
/*     */   
/*     */   private Binding binding;
/*     */ 
/*     */ 
/*     */   
/*     */   private GroovyBeanDefinitionWrapper currentBeanDefinition;
/*     */ 
/*     */ 
/*     */   
/*     */   public GroovyBeanDefinitionReader(BeanDefinitionRegistry registry) {
/* 163 */     super(registry);
/* 164 */     this.standardXmlBeanDefinitionReader = new XmlBeanDefinitionReader(registry);
/* 165 */     this.groovyDslXmlBeanDefinitionReader = new XmlBeanDefinitionReader(registry);
/* 166 */     this.groovyDslXmlBeanDefinitionReader.setValidating(false);
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
/*     */   public GroovyBeanDefinitionReader(XmlBeanDefinitionReader xmlBeanDefinitionReader) {
/* 179 */     super(xmlBeanDefinitionReader.getRegistry());
/* 180 */     this.standardXmlBeanDefinitionReader = new XmlBeanDefinitionReader(xmlBeanDefinitionReader.getRegistry());
/* 181 */     this.groovyDslXmlBeanDefinitionReader = xmlBeanDefinitionReader;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMetaClass(MetaClass metaClass) {
/* 186 */     this.metaClass = metaClass;
/*     */   }
/*     */   
/*     */   public MetaClass getMetaClass() {
/* 190 */     return this.metaClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBinding(Binding binding) {
/* 198 */     this.binding = binding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Binding getBinding() {
/* 205 */     return this.binding;
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
/*     */   public int loadBeanDefinitions(Resource resource) throws BeanDefinitionStoreException {
/* 220 */     return loadBeanDefinitions(new EncodedResource(resource));
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
/*     */   public int loadBeanDefinitions(EncodedResource encodedResource) throws BeanDefinitionStoreException {
/* 234 */     String filename = encodedResource.getResource().getFilename();
/* 235 */     if (StringUtils.endsWithIgnoreCase(filename, ".xml")) {
/* 236 */       return this.standardXmlBeanDefinitionReader.loadBeanDefinitions(encodedResource);
/*     */     }
/*     */     
/* 239 */     if (this.logger.isTraceEnabled()) {
/* 240 */       this.logger.trace("Loading Groovy bean definitions from " + encodedResource);
/*     */     }
/*     */     
/* 243 */     Closure beans = new Closure(this)
/*     */       {
/*     */         public Object call(Object[] args) {
/* 246 */           GroovyBeanDefinitionReader.this.invokeBeanDefiningClosure((Closure)args[0]);
/* 247 */           return null;
/*     */         }
/*     */       };
/* 250 */     Binding binding = new Binding()
/*     */       {
/*     */         public void setVariable(String name, Object value) {
/* 253 */           if (GroovyBeanDefinitionReader.this.currentBeanDefinition != null) {
/* 254 */             GroovyBeanDefinitionReader.this.applyPropertyToBeanDefinition(name, value);
/*     */           } else {
/*     */             
/* 257 */             super.setVariable(name, value);
/*     */           } 
/*     */         }
/*     */       };
/* 261 */     binding.setVariable("beans", beans);
/*     */     
/* 263 */     int countBefore = getRegistry().getBeanDefinitionCount();
/*     */     try {
/* 265 */       GroovyShell shell = new GroovyShell(getBeanClassLoader(), binding);
/* 266 */       shell.evaluate(encodedResource.getReader(), "beans");
/*     */     }
/* 268 */     catch (Throwable ex) {
/* 269 */       throw new BeanDefinitionParsingException(new Problem("Error evaluating Groovy script: " + ex.getMessage(), new Location(encodedResource
/* 270 */               .getResource()), null, ex));
/*     */     } 
/*     */     
/* 273 */     int count = getRegistry().getBeanDefinitionCount() - countBefore;
/* 274 */     if (this.logger.isDebugEnabled()) {
/* 275 */       this.logger.debug("Loaded " + count + " bean definitions from " + encodedResource);
/*     */     }
/* 277 */     return count;
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
/*     */   public GroovyBeanDefinitionReader beans(Closure closure) {
/* 289 */     return invokeBeanDefiningClosure(closure);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GenericBeanDefinition bean(Class<?> type) {
/* 298 */     GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
/* 299 */     beanDefinition.setBeanClass(type);
/* 300 */     return beanDefinition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractBeanDefinition bean(Class<?> type, Object... args) {
/* 310 */     GroovyBeanDefinitionWrapper current = this.currentBeanDefinition;
/*     */     try {
/* 312 */       Closure callable = null;
/* 313 */       Collection<Object> constructorArgs = null;
/* 314 */       if (!ObjectUtils.isEmpty(args)) {
/* 315 */         int index = args.length;
/* 316 */         Object lastArg = args[index - 1];
/* 317 */         if (lastArg instanceof Closure) {
/* 318 */           callable = (Closure)lastArg;
/* 319 */           index--;
/*     */         } 
/* 321 */         if (index > -1) {
/* 322 */           constructorArgs = resolveConstructorArguments(args, 0, index);
/*     */         }
/*     */       } 
/* 325 */       this.currentBeanDefinition = new GroovyBeanDefinitionWrapper(null, type, constructorArgs);
/* 326 */       if (callable != null) {
/* 327 */         callable.call(this.currentBeanDefinition);
/*     */       }
/* 329 */       return this.currentBeanDefinition.getBeanDefinition();
/*     */     }
/*     */     finally {
/*     */       
/* 333 */       this.currentBeanDefinition = current;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void xmlns(Map<String, String> definition) {
/* 342 */     if (!definition.isEmpty()) {
/* 343 */       for (Map.Entry<String, String> entry : definition.entrySet()) {
/* 344 */         String namespace = entry.getKey();
/* 345 */         String uri = entry.getValue();
/* 346 */         if (uri == null) {
/* 347 */           throw new IllegalArgumentException("Namespace definition must supply a non-null URI");
/*     */         }
/*     */         
/* 350 */         NamespaceHandler namespaceHandler = this.groovyDslXmlBeanDefinitionReader.getNamespaceHandlerResolver().resolve(uri);
/* 351 */         if (namespaceHandler == null) {
/* 352 */           throw new BeanDefinitionParsingException(new Problem("No namespace handler found for URI: " + uri, new Location(new DescriptiveResource("Groovy"))));
/*     */         }
/*     */         
/* 355 */         this.namespaces.put(namespace, uri);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void importBeans(String resourcePattern) throws IOException {
/* 366 */     loadBeanDefinitions(resourcePattern);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object invokeMethod(String name, Object arg) {
/* 377 */     Object[] args = (Object[])arg;
/* 378 */     if ("beans".equals(name) && args.length == 1 && args[0] instanceof Closure) {
/* 379 */       return beans((Closure)args[0]);
/*     */     }
/* 381 */     if ("ref".equals(name)) {
/*     */       String refName;
/* 383 */       if (args[0] == null) {
/* 384 */         throw new IllegalArgumentException("Argument to ref() is not a valid bean or was not found");
/*     */       }
/* 386 */       if (args[0] instanceof RuntimeBeanReference) {
/* 387 */         refName = ((RuntimeBeanReference)args[0]).getBeanName();
/*     */       } else {
/*     */         
/* 390 */         refName = args[0].toString();
/*     */       } 
/* 392 */       boolean parentRef = false;
/* 393 */       if (args.length > 1 && args[1] instanceof Boolean) {
/* 394 */         parentRef = ((Boolean)args[1]).booleanValue();
/*     */       }
/* 396 */       return new RuntimeBeanReference(refName, parentRef);
/*     */     } 
/* 398 */     if (this.namespaces.containsKey(name) && args.length > 0 && args[0] instanceof Closure) {
/* 399 */       GroovyDynamicElementReader reader = createDynamicElementReader(name);
/* 400 */       reader.invokeMethod("doCall", args);
/*     */     } else {
/* 402 */       if (args.length > 0 && args[0] instanceof Closure)
/*     */       {
/* 404 */         return invokeBeanDefiningMethod(name, args);
/*     */       }
/* 406 */       if (args.length > 0 && (args[0] instanceof Class || args[0] instanceof RuntimeBeanReference || args[0] instanceof Map))
/*     */       {
/* 408 */         return invokeBeanDefiningMethod(name, args);
/*     */       }
/* 410 */       if (args.length > 1 && args[args.length - 1] instanceof Closure)
/* 411 */         return invokeBeanDefiningMethod(name, args); 
/*     */     } 
/* 413 */     MetaClass mc = DefaultGroovyMethods.getMetaClass(getRegistry());
/* 414 */     if (!mc.respondsTo(getRegistry(), name, args).isEmpty()) {
/* 415 */       return mc.invokeMethod(getRegistry(), name, args);
/*     */     }
/* 417 */     return this;
/*     */   }
/*     */   
/*     */   private boolean addDeferredProperty(String property, Object newValue) {
/* 421 */     if (newValue instanceof List || newValue instanceof Map) {
/* 422 */       this.deferredProperties.put(this.currentBeanDefinition.getBeanName() + '.' + property, new DeferredProperty(this.currentBeanDefinition, property, newValue));
/*     */       
/* 424 */       return true;
/*     */     } 
/* 426 */     return false;
/*     */   }
/*     */   
/*     */   private void finalizeDeferredProperties() {
/* 430 */     for (DeferredProperty dp : this.deferredProperties.values()) {
/* 431 */       if (dp.value instanceof List) {
/* 432 */         dp.value = manageListIfNecessary((List)dp.value);
/*     */       }
/* 434 */       else if (dp.value instanceof Map) {
/* 435 */         dp.value = manageMapIfNecessary((Map<?, ?>)dp.value);
/*     */       } 
/* 437 */       dp.apply();
/*     */     } 
/* 439 */     this.deferredProperties.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected GroovyBeanDefinitionReader invokeBeanDefiningClosure(Closure callable) {
/* 448 */     callable.setDelegate(this);
/* 449 */     callable.call();
/* 450 */     finalizeDeferredProperties();
/* 451 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private GroovyBeanDefinitionWrapper invokeBeanDefiningMethod(String beanName, Object[] args) {
/* 462 */     boolean hasClosureArgument = args[args.length - 1] instanceof Closure;
/* 463 */     if (args[0] instanceof Class) {
/* 464 */       Class<?> beanClass = (Class)args[0];
/* 465 */       if (hasClosureArgument) {
/* 466 */         if (args.length - 1 != 1) {
/* 467 */           this
/* 468 */             .currentBeanDefinition = new GroovyBeanDefinitionWrapper(beanName, beanClass, resolveConstructorArguments(args, 1, args.length - 1));
/*     */         } else {
/*     */           
/* 471 */           this.currentBeanDefinition = new GroovyBeanDefinitionWrapper(beanName, beanClass);
/*     */         } 
/*     */       } else {
/*     */         
/* 475 */         this
/* 476 */           .currentBeanDefinition = new GroovyBeanDefinitionWrapper(beanName, beanClass, resolveConstructorArguments(args, 1, args.length));
/*     */       }
/*     */     
/* 479 */     } else if (args[0] instanceof RuntimeBeanReference) {
/* 480 */       this.currentBeanDefinition = new GroovyBeanDefinitionWrapper(beanName);
/* 481 */       this.currentBeanDefinition.getBeanDefinition().setFactoryBeanName(((RuntimeBeanReference)args[0]).getBeanName());
/*     */     }
/* 483 */     else if (args[0] instanceof Map) {
/*     */       
/* 485 */       if (args.length > 1 && args[1] instanceof Class) {
/* 486 */         List<Object> constructorArgs = resolveConstructorArguments(args, 2, hasClosureArgument ? (args.length - 1) : args.length);
/* 487 */         this.currentBeanDefinition = new GroovyBeanDefinitionWrapper(beanName, (Class)args[1], constructorArgs);
/* 488 */         Map namedArgs = (Map)args[0];
/* 489 */         for (Object o : namedArgs.keySet()) {
/* 490 */           String propName = (String)o;
/* 491 */           setProperty(propName, namedArgs.get(propName));
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 496 */         this.currentBeanDefinition = new GroovyBeanDefinitionWrapper(beanName);
/*     */         
/* 498 */         Map.Entry factoryBeanEntry = ((Map<?, ?>)args[0]).entrySet().iterator().next();
/*     */ 
/*     */         
/* 501 */         int constructorArgsTest = hasClosureArgument ? 2 : 1;
/*     */         
/* 503 */         if (args.length > constructorArgsTest) {
/*     */           
/* 505 */           int endOfConstructArgs = hasClosureArgument ? (args.length - 1) : args.length;
/* 506 */           this
/* 507 */             .currentBeanDefinition = new GroovyBeanDefinitionWrapper(beanName, null, resolveConstructorArguments(args, 1, endOfConstructArgs));
/*     */         } else {
/*     */           
/* 510 */           this.currentBeanDefinition = new GroovyBeanDefinitionWrapper(beanName);
/*     */         } 
/* 512 */         this.currentBeanDefinition.getBeanDefinition().setFactoryBeanName(factoryBeanEntry.getKey().toString());
/* 513 */         this.currentBeanDefinition.getBeanDefinition().setFactoryMethodName(factoryBeanEntry.getValue().toString());
/*     */       }
/*     */     
/*     */     }
/* 517 */     else if (args[0] instanceof Closure) {
/* 518 */       this.currentBeanDefinition = new GroovyBeanDefinitionWrapper(beanName);
/* 519 */       this.currentBeanDefinition.getBeanDefinition().setAbstract(true);
/*     */     } else {
/*     */       
/* 522 */       List<Object> constructorArgs = resolveConstructorArguments(args, 0, hasClosureArgument ? (args.length - 1) : args.length);
/* 523 */       this.currentBeanDefinition = new GroovyBeanDefinitionWrapper(beanName, null, constructorArgs);
/*     */     } 
/*     */     
/* 526 */     if (hasClosureArgument) {
/* 527 */       Closure callable = (Closure)args[args.length - 1];
/* 528 */       callable.setDelegate(this);
/* 529 */       callable.setResolveStrategy(1);
/* 530 */       callable.call(this.currentBeanDefinition);
/*     */     } 
/*     */     
/* 533 */     GroovyBeanDefinitionWrapper beanDefinition = this.currentBeanDefinition;
/* 534 */     this.currentBeanDefinition = null;
/* 535 */     beanDefinition.getBeanDefinition().setAttribute(GroovyBeanDefinitionWrapper.class.getName(), beanDefinition);
/* 536 */     getRegistry().registerBeanDefinition(beanName, (BeanDefinition)beanDefinition.getBeanDefinition());
/* 537 */     return beanDefinition;
/*     */   }
/*     */   
/*     */   protected List<Object> resolveConstructorArguments(Object[] args, int start, int end) {
/* 541 */     Object[] constructorArgs = Arrays.copyOfRange(args, start, end);
/* 542 */     for (int i = 0; i < constructorArgs.length; i++) {
/* 543 */       if (constructorArgs[i] instanceof groovy.lang.GString) {
/* 544 */         constructorArgs[i] = constructorArgs[i].toString();
/*     */       }
/* 546 */       else if (constructorArgs[i] instanceof List) {
/* 547 */         constructorArgs[i] = manageListIfNecessary((List)constructorArgs[i]);
/*     */       }
/* 549 */       else if (constructorArgs[i] instanceof Map) {
/* 550 */         constructorArgs[i] = manageMapIfNecessary((Map<?, ?>)constructorArgs[i]);
/*     */       } 
/*     */     } 
/* 553 */     return Arrays.asList(constructorArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object manageMapIfNecessary(Map<?, ?> map) {
/* 563 */     boolean containsRuntimeRefs = false;
/* 564 */     for (Object element : map.values()) {
/* 565 */       if (element instanceof RuntimeBeanReference) {
/* 566 */         containsRuntimeRefs = true;
/*     */         break;
/*     */       } 
/*     */     } 
/* 570 */     if (containsRuntimeRefs) {
/* 571 */       ManagedMap<?, ?> managedMap = new ManagedMap();
/* 572 */       managedMap.putAll(map);
/* 573 */       return managedMap;
/*     */     } 
/* 575 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object manageListIfNecessary(List<?> list) {
/* 585 */     boolean containsRuntimeRefs = false;
/* 586 */     for (Object element : list) {
/* 587 */       if (element instanceof RuntimeBeanReference) {
/* 588 */         containsRuntimeRefs = true;
/*     */         break;
/*     */       } 
/*     */     } 
/* 592 */     if (containsRuntimeRefs) {
/* 593 */       ManagedList<?> managedList = new ManagedList();
/* 594 */       managedList.addAll(list);
/* 595 */       return managedList;
/*     */     } 
/* 597 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperty(String name, Object value) {
/* 605 */     if (this.currentBeanDefinition != null) {
/* 606 */       applyPropertyToBeanDefinition(name, value);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void applyPropertyToBeanDefinition(String name, Object value) {
/* 611 */     if (value instanceof groovy.lang.GString) {
/* 612 */       value = value.toString();
/*     */     }
/* 614 */     if (addDeferredProperty(name, value)) {
/*     */       return;
/*     */     }
/* 617 */     if (value instanceof Closure) {
/* 618 */       GroovyBeanDefinitionWrapper current = this.currentBeanDefinition;
/*     */       try {
/* 620 */         Closure callable = (Closure)value;
/* 621 */         Class<?> parameterType = callable.getParameterTypes()[0];
/* 622 */         if (Object.class == parameterType) {
/* 623 */           this.currentBeanDefinition = new GroovyBeanDefinitionWrapper("");
/* 624 */           callable.call(this.currentBeanDefinition);
/*     */         } else {
/*     */           
/* 627 */           this.currentBeanDefinition = new GroovyBeanDefinitionWrapper(null, parameterType);
/* 628 */           callable.call(null);
/*     */         } 
/*     */         
/* 631 */         value = this.currentBeanDefinition.getBeanDefinition();
/*     */       } finally {
/*     */         
/* 634 */         this.currentBeanDefinition = current;
/*     */       } 
/*     */     } 
/* 637 */     this.currentBeanDefinition.addProperty(name, value);
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
/*     */   public Object getProperty(String name) {
/* 651 */     Binding binding = getBinding();
/* 652 */     if (binding != null && binding.hasVariable(name)) {
/* 653 */       return binding.getVariable(name);
/*     */     }
/*     */     
/* 656 */     if (this.namespaces.containsKey(name)) {
/* 657 */       return createDynamicElementReader(name);
/*     */     }
/* 659 */     if (getRegistry().containsBeanDefinition(name)) {
/*     */       
/* 661 */       GroovyBeanDefinitionWrapper beanDefinition = (GroovyBeanDefinitionWrapper)getRegistry().getBeanDefinition(name).getAttribute(GroovyBeanDefinitionWrapper.class.getName());
/* 662 */       if (beanDefinition != null) {
/* 663 */         return new GroovyRuntimeBeanReference(name, beanDefinition, false);
/*     */       }
/*     */       
/* 666 */       return new RuntimeBeanReference(name, false);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 671 */     if (this.currentBeanDefinition != null) {
/* 672 */       MutablePropertyValues pvs = this.currentBeanDefinition.getBeanDefinition().getPropertyValues();
/* 673 */       if (pvs.contains(name)) {
/* 674 */         return pvs.get(name);
/*     */       }
/*     */       
/* 677 */       DeferredProperty dp = this.deferredProperties.get(this.currentBeanDefinition.getBeanName() + name);
/* 678 */       if (dp != null) {
/* 679 */         return dp.value;
/*     */       }
/*     */       
/* 682 */       return getMetaClass().getProperty(this, name);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 687 */     return getMetaClass().getProperty(this, name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private GroovyDynamicElementReader createDynamicElementReader(String namespace) {
/* 693 */     XmlReaderContext readerContext = this.groovyDslXmlBeanDefinitionReader.createReaderContext((Resource)new DescriptiveResource("Groovy"));
/*     */     
/* 695 */     BeanDefinitionParserDelegate delegate = new BeanDefinitionParserDelegate(readerContext);
/* 696 */     boolean decorating = (this.currentBeanDefinition != null);
/* 697 */     if (!decorating) {
/* 698 */       this.currentBeanDefinition = new GroovyBeanDefinitionWrapper(namespace);
/*     */     }
/* 700 */     return new GroovyDynamicElementReader(namespace, this.namespaces, delegate, this.currentBeanDefinition, decorating)
/*     */       {
/*     */         protected void afterInvocation() {
/* 703 */           if (!this.decorating) {
/* 704 */             GroovyBeanDefinitionReader.this.currentBeanDefinition = null;
/*     */           }
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class DeferredProperty
/*     */   {
/*     */     private final GroovyBeanDefinitionWrapper beanDefinition;
/*     */ 
/*     */     
/*     */     private final String name;
/*     */ 
/*     */     
/*     */     public Object value;
/*     */ 
/*     */ 
/*     */     
/*     */     public DeferredProperty(GroovyBeanDefinitionWrapper beanDefinition, String name, Object value) {
/* 726 */       this.beanDefinition = beanDefinition;
/* 727 */       this.name = name;
/* 728 */       this.value = value;
/*     */     }
/*     */     
/*     */     public void apply() {
/* 732 */       this.beanDefinition.addProperty(this.name, this.value);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class GroovyRuntimeBeanReference
/*     */     extends RuntimeBeanReference
/*     */     implements GroovyObject
/*     */   {
/*     */     private final GroovyBeanDefinitionWrapper beanDefinition;
/*     */     
/*     */     private MetaClass metaClass;
/*     */ 
/*     */     
/*     */     public GroovyRuntimeBeanReference(String beanName, GroovyBeanDefinitionWrapper beanDefinition, boolean toParent) {
/* 747 */       super(beanName, toParent);
/* 748 */       this.beanDefinition = beanDefinition;
/* 749 */       this.metaClass = InvokerHelper.getMetaClass(this);
/*     */     }
/*     */     
/*     */     public MetaClass getMetaClass() {
/* 753 */       return this.metaClass;
/*     */     }
/*     */     
/*     */     public Object getProperty(String property) {
/* 757 */       if (property.equals("beanName")) {
/* 758 */         return getBeanName();
/*     */       }
/* 760 */       if (property.equals("source")) {
/* 761 */         return getSource();
/*     */       }
/* 763 */       if (this.beanDefinition != null) {
/* 764 */         return new GroovyPropertyValue(property, this.beanDefinition
/* 765 */             .getBeanDefinition().getPropertyValues().get(property));
/*     */       }
/*     */       
/* 768 */       return this.metaClass.getProperty(this, property);
/*     */     }
/*     */ 
/*     */     
/*     */     public Object invokeMethod(String name, Object args) {
/* 773 */       return this.metaClass.invokeMethod(this, name, args);
/*     */     }
/*     */     
/*     */     public void setMetaClass(MetaClass metaClass) {
/* 777 */       this.metaClass = metaClass;
/*     */     }
/*     */     
/*     */     public void setProperty(String property, Object newValue) {
/* 781 */       if (!GroovyBeanDefinitionReader.this.addDeferredProperty(property, newValue)) {
/* 782 */         this.beanDefinition.getBeanDefinition().getPropertyValues().add(property, newValue);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private class GroovyPropertyValue
/*     */       extends GroovyObjectSupport
/*     */     {
/*     */       private final String propertyName;
/*     */ 
/*     */       
/*     */       private final Object propertyValue;
/*     */ 
/*     */       
/*     */       public GroovyPropertyValue(String propertyName, Object propertyValue) {
/* 798 */         this.propertyName = propertyName;
/* 799 */         this.propertyValue = propertyValue;
/*     */       }
/*     */       
/*     */       public void leftShift(Object value) {
/* 803 */         InvokerHelper.invokeMethod(this.propertyValue, "leftShift", value);
/* 804 */         updateDeferredProperties(value);
/*     */       }
/*     */       
/*     */       public boolean add(Object value) {
/* 808 */         boolean retVal = ((Boolean)InvokerHelper.invokeMethod(this.propertyValue, "add", value)).booleanValue();
/* 809 */         updateDeferredProperties(value);
/* 810 */         return retVal;
/*     */       }
/*     */       
/*     */       public boolean addAll(Collection values) {
/* 814 */         boolean retVal = ((Boolean)InvokerHelper.invokeMethod(this.propertyValue, "addAll", values)).booleanValue();
/* 815 */         for (Object value : values) {
/* 816 */           updateDeferredProperties(value);
/*     */         }
/* 818 */         return retVal;
/*     */       }
/*     */ 
/*     */       
/*     */       public Object invokeMethod(String name, Object args) {
/* 823 */         return InvokerHelper.invokeMethod(this.propertyValue, name, args);
/*     */       }
/*     */ 
/*     */       
/*     */       public Object getProperty(String name) {
/* 828 */         return InvokerHelper.getProperty(this.propertyValue, name);
/*     */       }
/*     */ 
/*     */       
/*     */       public void setProperty(String name, Object value) {
/* 833 */         InvokerHelper.setProperty(this.propertyValue, name, value);
/*     */       }
/*     */       
/*     */       private void updateDeferredProperties(Object value) {
/* 837 */         if (value instanceof RuntimeBeanReference)
/* 838 */           GroovyBeanDefinitionReader.this.deferredProperties.put(GroovyBeanDefinitionReader.GroovyRuntimeBeanReference.this.beanDefinition.getBeanName(), new GroovyBeanDefinitionReader.DeferredProperty(GroovyBeanDefinitionReader.GroovyRuntimeBeanReference.this
/* 839 */                 .beanDefinition, this.propertyName, this.propertyValue)); 
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/groovy/GroovyBeanDefinitionReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */