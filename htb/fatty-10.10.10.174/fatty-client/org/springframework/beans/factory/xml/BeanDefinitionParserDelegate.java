/*      */ package org.springframework.beans.factory.xml;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.apache.commons.logging.LogFactory;
/*      */ import org.springframework.beans.BeanMetadataAttribute;
/*      */ import org.springframework.beans.BeanMetadataAttributeAccessor;
/*      */ import org.springframework.beans.PropertyValue;
/*      */ import org.springframework.beans.factory.config.BeanDefinition;
/*      */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*      */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*      */ import org.springframework.beans.factory.config.RuntimeBeanNameReference;
/*      */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*      */ import org.springframework.beans.factory.config.TypedStringValue;
/*      */ import org.springframework.beans.factory.parsing.BeanEntry;
/*      */ import org.springframework.beans.factory.parsing.ConstructorArgumentEntry;
/*      */ import org.springframework.beans.factory.parsing.ParseState;
/*      */ import org.springframework.beans.factory.parsing.PropertyEntry;
/*      */ import org.springframework.beans.factory.parsing.QualifierEntry;
/*      */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*      */ import org.springframework.beans.factory.support.AutowireCandidateQualifier;
/*      */ import org.springframework.beans.factory.support.BeanDefinitionDefaults;
/*      */ import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
/*      */ import org.springframework.beans.factory.support.LookupOverride;
/*      */ import org.springframework.beans.factory.support.ManagedArray;
/*      */ import org.springframework.beans.factory.support.ManagedList;
/*      */ import org.springframework.beans.factory.support.ManagedMap;
/*      */ import org.springframework.beans.factory.support.ManagedProperties;
/*      */ import org.springframework.beans.factory.support.ManagedSet;
/*      */ import org.springframework.beans.factory.support.MethodOverride;
/*      */ import org.springframework.beans.factory.support.MethodOverrides;
/*      */ import org.springframework.beans.factory.support.ReplaceOverride;
/*      */ import org.springframework.lang.Nullable;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.CollectionUtils;
/*      */ import org.springframework.util.ObjectUtils;
/*      */ import org.springframework.util.PatternMatchUtils;
/*      */ import org.springframework.util.StringUtils;
/*      */ import org.springframework.util.xml.DomUtils;
/*      */ import org.w3c.dom.Element;
/*      */ import org.w3c.dom.NamedNodeMap;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class BeanDefinitionParserDelegate
/*      */ {
/*      */   public static final String BEANS_NAMESPACE_URI = "http://www.springframework.org/schema/beans";
/*      */   public static final String MULTI_VALUE_ATTRIBUTE_DELIMITERS = ",; ";
/*      */   public static final String TRUE_VALUE = "true";
/*      */   public static final String FALSE_VALUE = "false";
/*      */   public static final String DEFAULT_VALUE = "default";
/*      */   public static final String DESCRIPTION_ELEMENT = "description";
/*      */   public static final String AUTOWIRE_NO_VALUE = "no";
/*      */   public static final String AUTOWIRE_BY_NAME_VALUE = "byName";
/*      */   public static final String AUTOWIRE_BY_TYPE_VALUE = "byType";
/*      */   public static final String AUTOWIRE_CONSTRUCTOR_VALUE = "constructor";
/*      */   public static final String AUTOWIRE_AUTODETECT_VALUE = "autodetect";
/*      */   public static final String NAME_ATTRIBUTE = "name";
/*      */   public static final String BEAN_ELEMENT = "bean";
/*      */   public static final String META_ELEMENT = "meta";
/*      */   public static final String ID_ATTRIBUTE = "id";
/*      */   public static final String PARENT_ATTRIBUTE = "parent";
/*      */   public static final String CLASS_ATTRIBUTE = "class";
/*      */   public static final String ABSTRACT_ATTRIBUTE = "abstract";
/*      */   public static final String SCOPE_ATTRIBUTE = "scope";
/*      */   private static final String SINGLETON_ATTRIBUTE = "singleton";
/*      */   public static final String LAZY_INIT_ATTRIBUTE = "lazy-init";
/*      */   public static final String AUTOWIRE_ATTRIBUTE = "autowire";
/*      */   public static final String AUTOWIRE_CANDIDATE_ATTRIBUTE = "autowire-candidate";
/*      */   public static final String PRIMARY_ATTRIBUTE = "primary";
/*      */   public static final String DEPENDS_ON_ATTRIBUTE = "depends-on";
/*      */   public static final String INIT_METHOD_ATTRIBUTE = "init-method";
/*      */   public static final String DESTROY_METHOD_ATTRIBUTE = "destroy-method";
/*      */   public static final String FACTORY_METHOD_ATTRIBUTE = "factory-method";
/*      */   public static final String FACTORY_BEAN_ATTRIBUTE = "factory-bean";
/*      */   public static final String CONSTRUCTOR_ARG_ELEMENT = "constructor-arg";
/*      */   public static final String INDEX_ATTRIBUTE = "index";
/*      */   public static final String TYPE_ATTRIBUTE = "type";
/*      */   public static final String VALUE_TYPE_ATTRIBUTE = "value-type";
/*      */   public static final String KEY_TYPE_ATTRIBUTE = "key-type";
/*      */   public static final String PROPERTY_ELEMENT = "property";
/*      */   public static final String REF_ATTRIBUTE = "ref";
/*      */   public static final String VALUE_ATTRIBUTE = "value";
/*      */   public static final String LOOKUP_METHOD_ELEMENT = "lookup-method";
/*      */   public static final String REPLACED_METHOD_ELEMENT = "replaced-method";
/*      */   public static final String REPLACER_ATTRIBUTE = "replacer";
/*      */   public static final String ARG_TYPE_ELEMENT = "arg-type";
/*      */   public static final String ARG_TYPE_MATCH_ATTRIBUTE = "match";
/*      */   public static final String REF_ELEMENT = "ref";
/*      */   public static final String IDREF_ELEMENT = "idref";
/*      */   public static final String BEAN_REF_ATTRIBUTE = "bean";
/*      */   public static final String PARENT_REF_ATTRIBUTE = "parent";
/*      */   public static final String VALUE_ELEMENT = "value";
/*      */   public static final String NULL_ELEMENT = "null";
/*      */   public static final String ARRAY_ELEMENT = "array";
/*      */   public static final String LIST_ELEMENT = "list";
/*      */   public static final String SET_ELEMENT = "set";
/*      */   public static final String MAP_ELEMENT = "map";
/*      */   public static final String ENTRY_ELEMENT = "entry";
/*      */   public static final String KEY_ELEMENT = "key";
/*      */   public static final String KEY_ATTRIBUTE = "key";
/*      */   public static final String KEY_REF_ATTRIBUTE = "key-ref";
/*      */   public static final String VALUE_REF_ATTRIBUTE = "value-ref";
/*      */   public static final String PROPS_ELEMENT = "props";
/*      */   public static final String PROP_ELEMENT = "prop";
/*      */   public static final String MERGE_ATTRIBUTE = "merge";
/*      */   public static final String QUALIFIER_ELEMENT = "qualifier";
/*      */   public static final String QUALIFIER_ATTRIBUTE_ELEMENT = "attribute";
/*      */   public static final String DEFAULT_LAZY_INIT_ATTRIBUTE = "default-lazy-init";
/*      */   public static final String DEFAULT_MERGE_ATTRIBUTE = "default-merge";
/*      */   public static final String DEFAULT_AUTOWIRE_ATTRIBUTE = "default-autowire";
/*      */   public static final String DEFAULT_AUTOWIRE_CANDIDATES_ATTRIBUTE = "default-autowire-candidates";
/*      */   public static final String DEFAULT_INIT_METHOD_ATTRIBUTE = "default-init-method";
/*      */   public static final String DEFAULT_DESTROY_METHOD_ATTRIBUTE = "default-destroy-method";
/*  228 */   protected final Log logger = LogFactory.getLog(getClass());
/*      */   
/*      */   private final XmlReaderContext readerContext;
/*      */   
/*  232 */   private final DocumentDefaultsDefinition defaults = new DocumentDefaultsDefinition();
/*      */   
/*  234 */   private final ParseState parseState = new ParseState();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  241 */   private final Set<String> usedNames = new HashSet<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BeanDefinitionParserDelegate(XmlReaderContext readerContext) {
/*  249 */     Assert.notNull(readerContext, "XmlReaderContext must not be null");
/*  250 */     this.readerContext = readerContext;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final XmlReaderContext getReaderContext() {
/*  258 */     return this.readerContext;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected Object extractSource(Element ele) {
/*  267 */     return this.readerContext.extractSource(ele);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void error(String message, Node source) {
/*  274 */     this.readerContext.error(message, source, this.parseState.snapshot());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void error(String message, Element source) {
/*  281 */     this.readerContext.error(message, source, this.parseState.snapshot());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void error(String message, Element source, Throwable cause) {
/*  288 */     this.readerContext.error(message, source, this.parseState.snapshot(), cause);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void initDefaults(Element root) {
/*  296 */     initDefaults(root, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void initDefaults(Element root, @Nullable BeanDefinitionParserDelegate parent) {
/*  308 */     populateDefaults(this.defaults, (parent != null) ? parent.defaults : null, root);
/*  309 */     this.readerContext.fireDefaultsRegistered(this.defaults);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void populateDefaults(DocumentDefaultsDefinition defaults, @Nullable DocumentDefaultsDefinition parentDefaults, Element root) {
/*  322 */     String lazyInit = root.getAttribute("default-lazy-init");
/*  323 */     if (isDefaultValue(lazyInit))
/*      */     {
/*  325 */       lazyInit = (parentDefaults != null) ? parentDefaults.getLazyInit() : "false";
/*      */     }
/*  327 */     defaults.setLazyInit(lazyInit);
/*      */     
/*  329 */     String merge = root.getAttribute("default-merge");
/*  330 */     if (isDefaultValue(merge))
/*      */     {
/*  332 */       merge = (parentDefaults != null) ? parentDefaults.getMerge() : "false";
/*      */     }
/*  334 */     defaults.setMerge(merge);
/*      */     
/*  336 */     String autowire = root.getAttribute("default-autowire");
/*  337 */     if (isDefaultValue(autowire))
/*      */     {
/*  339 */       autowire = (parentDefaults != null) ? parentDefaults.getAutowire() : "no";
/*      */     }
/*  341 */     defaults.setAutowire(autowire);
/*      */     
/*  343 */     if (root.hasAttribute("default-autowire-candidates")) {
/*  344 */       defaults.setAutowireCandidates(root.getAttribute("default-autowire-candidates"));
/*      */     }
/*  346 */     else if (parentDefaults != null) {
/*  347 */       defaults.setAutowireCandidates(parentDefaults.getAutowireCandidates());
/*      */     } 
/*      */     
/*  350 */     if (root.hasAttribute("default-init-method")) {
/*  351 */       defaults.setInitMethod(root.getAttribute("default-init-method"));
/*      */     }
/*  353 */     else if (parentDefaults != null) {
/*  354 */       defaults.setInitMethod(parentDefaults.getInitMethod());
/*      */     } 
/*      */     
/*  357 */     if (root.hasAttribute("default-destroy-method")) {
/*  358 */       defaults.setDestroyMethod(root.getAttribute("default-destroy-method"));
/*      */     }
/*  360 */     else if (parentDefaults != null) {
/*  361 */       defaults.setDestroyMethod(parentDefaults.getDestroyMethod());
/*      */     } 
/*      */     
/*  364 */     defaults.setSource(this.readerContext.extractSource(root));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DocumentDefaultsDefinition getDefaults() {
/*  371 */     return this.defaults;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BeanDefinitionDefaults getBeanDefinitionDefaults() {
/*  379 */     BeanDefinitionDefaults bdd = new BeanDefinitionDefaults();
/*  380 */     bdd.setLazyInit("true".equalsIgnoreCase(this.defaults.getLazyInit()));
/*  381 */     bdd.setAutowireMode(getAutowireMode("default"));
/*  382 */     bdd.setInitMethodName(this.defaults.getInitMethod());
/*  383 */     bdd.setDestroyMethodName(this.defaults.getDestroyMethod());
/*  384 */     return bdd;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public String[] getAutowireCandidatePatterns() {
/*  393 */     String candidatePattern = this.defaults.getAutowireCandidates();
/*  394 */     return (candidatePattern != null) ? StringUtils.commaDelimitedListToStringArray(candidatePattern) : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public BeanDefinitionHolder parseBeanDefinitionElement(Element ele) {
/*  405 */     return parseBeanDefinitionElement(ele, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public BeanDefinitionHolder parseBeanDefinitionElement(Element ele, @Nullable BeanDefinition containingBean) {
/*  415 */     String id = ele.getAttribute("id");
/*  416 */     String nameAttr = ele.getAttribute("name");
/*      */     
/*  418 */     List<String> aliases = new ArrayList<>();
/*  419 */     if (StringUtils.hasLength(nameAttr)) {
/*  420 */       String[] nameArr = StringUtils.tokenizeToStringArray(nameAttr, ",; ");
/*  421 */       aliases.addAll(Arrays.asList(nameArr));
/*      */     } 
/*      */     
/*  424 */     String beanName = id;
/*  425 */     if (!StringUtils.hasText(beanName) && !aliases.isEmpty()) {
/*  426 */       beanName = aliases.remove(0);
/*  427 */       if (this.logger.isTraceEnabled()) {
/*  428 */         this.logger.trace("No XML 'id' specified - using '" + beanName + "' as bean name and " + aliases + " as aliases");
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  433 */     if (containingBean == null) {
/*  434 */       checkNameUniqueness(beanName, aliases, ele);
/*      */     }
/*      */     
/*  437 */     AbstractBeanDefinition beanDefinition = parseBeanDefinitionElement(ele, beanName, containingBean);
/*  438 */     if (beanDefinition != null) {
/*  439 */       if (!StringUtils.hasText(beanName)) {
/*      */         try {
/*  441 */           if (containingBean != null) {
/*  442 */             beanName = BeanDefinitionReaderUtils.generateBeanName((BeanDefinition)beanDefinition, this.readerContext
/*  443 */                 .getRegistry(), true);
/*      */           } else {
/*      */             
/*  446 */             beanName = this.readerContext.generateBeanName((BeanDefinition)beanDefinition);
/*      */ 
/*      */ 
/*      */             
/*  450 */             String beanClassName = beanDefinition.getBeanClassName();
/*  451 */             if (beanClassName != null && beanName
/*  452 */               .startsWith(beanClassName) && beanName.length() > beanClassName.length() && 
/*  453 */               !this.readerContext.getRegistry().isBeanNameInUse(beanClassName)) {
/*  454 */               aliases.add(beanClassName);
/*      */             }
/*      */           } 
/*  457 */           if (this.logger.isTraceEnabled()) {
/*  458 */             this.logger.trace("Neither XML 'id' nor 'name' specified - using generated bean name [" + beanName + "]");
/*      */           
/*      */           }
/*      */         }
/*  462 */         catch (Exception ex) {
/*  463 */           error(ex.getMessage(), ele);
/*  464 */           return null;
/*      */         } 
/*      */       }
/*  467 */       String[] aliasesArray = StringUtils.toStringArray(aliases);
/*  468 */       return new BeanDefinitionHolder((BeanDefinition)beanDefinition, beanName, aliasesArray);
/*      */     } 
/*      */     
/*  471 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void checkNameUniqueness(String beanName, List<String> aliases, Element beanElement) {
/*  479 */     String foundName = null;
/*      */     
/*  481 */     if (StringUtils.hasText(beanName) && this.usedNames.contains(beanName)) {
/*  482 */       foundName = beanName;
/*      */     }
/*  484 */     if (foundName == null) {
/*  485 */       foundName = (String)CollectionUtils.findFirstMatch(this.usedNames, aliases);
/*      */     }
/*  487 */     if (foundName != null) {
/*  488 */       error("Bean name '" + foundName + "' is already used in this <beans> element", beanElement);
/*      */     }
/*      */     
/*  491 */     this.usedNames.add(beanName);
/*  492 */     this.usedNames.addAll(aliases);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public AbstractBeanDefinition parseBeanDefinitionElement(Element ele, String beanName, @Nullable BeanDefinition containingBean) {
/*  503 */     this.parseState.push((ParseState.Entry)new BeanEntry(beanName));
/*      */     
/*  505 */     String className = null;
/*  506 */     if (ele.hasAttribute("class")) {
/*  507 */       className = ele.getAttribute("class").trim();
/*      */     }
/*  509 */     String parent = null;
/*  510 */     if (ele.hasAttribute("parent")) {
/*  511 */       parent = ele.getAttribute("parent");
/*      */     }
/*      */     
/*      */     try {
/*  515 */       AbstractBeanDefinition bd = createBeanDefinition(className, parent);
/*      */       
/*  517 */       parseBeanDefinitionAttributes(ele, beanName, containingBean, bd);
/*  518 */       bd.setDescription(DomUtils.getChildElementValueByTagName(ele, "description"));
/*      */       
/*  520 */       parseMetaElements(ele, (BeanMetadataAttributeAccessor)bd);
/*  521 */       parseLookupOverrideSubElements(ele, bd.getMethodOverrides());
/*  522 */       parseReplacedMethodSubElements(ele, bd.getMethodOverrides());
/*      */       
/*  524 */       parseConstructorArgElements(ele, (BeanDefinition)bd);
/*  525 */       parsePropertyElements(ele, (BeanDefinition)bd);
/*  526 */       parseQualifierElements(ele, bd);
/*      */       
/*  528 */       bd.setResource(this.readerContext.getResource());
/*  529 */       bd.setSource(extractSource(ele));
/*      */       
/*  531 */       return bd;
/*      */     }
/*  533 */     catch (ClassNotFoundException ex) {
/*  534 */       error("Bean class [" + className + "] not found", ele, ex);
/*      */     }
/*  536 */     catch (NoClassDefFoundError err) {
/*  537 */       error("Class that bean class [" + className + "] depends on not found", ele, err);
/*      */     }
/*  539 */     catch (Throwable ex) {
/*  540 */       error("Unexpected failure during bean definition parsing", ele, ex);
/*      */     } finally {
/*      */       
/*  543 */       this.parseState.pop();
/*      */     } 
/*      */     
/*  546 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AbstractBeanDefinition parseBeanDefinitionAttributes(Element ele, String beanName, @Nullable BeanDefinition containingBean, AbstractBeanDefinition bd) {
/*  559 */     if (ele.hasAttribute("singleton")) {
/*  560 */       error("Old 1.x 'singleton' attribute in use - upgrade to 'scope' declaration", ele);
/*      */     }
/*  562 */     else if (ele.hasAttribute("scope")) {
/*  563 */       bd.setScope(ele.getAttribute("scope"));
/*      */     }
/*  565 */     else if (containingBean != null) {
/*      */       
/*  567 */       bd.setScope(containingBean.getScope());
/*      */     } 
/*      */     
/*  570 */     if (ele.hasAttribute("abstract")) {
/*  571 */       bd.setAbstract("true".equals(ele.getAttribute("abstract")));
/*      */     }
/*      */     
/*  574 */     String lazyInit = ele.getAttribute("lazy-init");
/*  575 */     if (isDefaultValue(lazyInit)) {
/*  576 */       lazyInit = this.defaults.getLazyInit();
/*      */     }
/*  578 */     bd.setLazyInit("true".equals(lazyInit));
/*      */     
/*  580 */     String autowire = ele.getAttribute("autowire");
/*  581 */     bd.setAutowireMode(getAutowireMode(autowire));
/*      */     
/*  583 */     if (ele.hasAttribute("depends-on")) {
/*  584 */       String dependsOn = ele.getAttribute("depends-on");
/*  585 */       bd.setDependsOn(StringUtils.tokenizeToStringArray(dependsOn, ",; "));
/*      */     } 
/*      */     
/*  588 */     String autowireCandidate = ele.getAttribute("autowire-candidate");
/*  589 */     if (isDefaultValue(autowireCandidate)) {
/*  590 */       String candidatePattern = this.defaults.getAutowireCandidates();
/*  591 */       if (candidatePattern != null) {
/*  592 */         String[] patterns = StringUtils.commaDelimitedListToStringArray(candidatePattern);
/*  593 */         bd.setAutowireCandidate(PatternMatchUtils.simpleMatch(patterns, beanName));
/*      */       } 
/*      */     } else {
/*      */       
/*  597 */       bd.setAutowireCandidate("true".equals(autowireCandidate));
/*      */     } 
/*      */     
/*  600 */     if (ele.hasAttribute("primary")) {
/*  601 */       bd.setPrimary("true".equals(ele.getAttribute("primary")));
/*      */     }
/*      */     
/*  604 */     if (ele.hasAttribute("init-method")) {
/*  605 */       String initMethodName = ele.getAttribute("init-method");
/*  606 */       bd.setInitMethodName(initMethodName);
/*      */     }
/*  608 */     else if (this.defaults.getInitMethod() != null) {
/*  609 */       bd.setInitMethodName(this.defaults.getInitMethod());
/*  610 */       bd.setEnforceInitMethod(false);
/*      */     } 
/*      */     
/*  613 */     if (ele.hasAttribute("destroy-method")) {
/*  614 */       String destroyMethodName = ele.getAttribute("destroy-method");
/*  615 */       bd.setDestroyMethodName(destroyMethodName);
/*      */     }
/*  617 */     else if (this.defaults.getDestroyMethod() != null) {
/*  618 */       bd.setDestroyMethodName(this.defaults.getDestroyMethod());
/*  619 */       bd.setEnforceDestroyMethod(false);
/*      */     } 
/*      */     
/*  622 */     if (ele.hasAttribute("factory-method")) {
/*  623 */       bd.setFactoryMethodName(ele.getAttribute("factory-method"));
/*      */     }
/*  625 */     if (ele.hasAttribute("factory-bean")) {
/*  626 */       bd.setFactoryBeanName(ele.getAttribute("factory-bean"));
/*      */     }
/*      */     
/*  629 */     return bd;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractBeanDefinition createBeanDefinition(@Nullable String className, @Nullable String parentName) throws ClassNotFoundException {
/*  642 */     return BeanDefinitionReaderUtils.createBeanDefinition(parentName, className, this.readerContext
/*  643 */         .getBeanClassLoader());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void parseMetaElements(Element ele, BeanMetadataAttributeAccessor attributeAccessor) {
/*  650 */     NodeList nl = ele.getChildNodes();
/*  651 */     for (int i = 0; i < nl.getLength(); i++) {
/*  652 */       Node node = nl.item(i);
/*  653 */       if (isCandidateElement(node) && nodeNameEquals(node, "meta")) {
/*  654 */         Element metaElement = (Element)node;
/*  655 */         String key = metaElement.getAttribute("key");
/*  656 */         String value = metaElement.getAttribute("value");
/*  657 */         BeanMetadataAttribute attribute = new BeanMetadataAttribute(key, value);
/*  658 */         attribute.setSource(extractSource(metaElement));
/*  659 */         attributeAccessor.addMetadataAttribute(attribute);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getAutowireMode(String attrValue) {
/*  670 */     String attr = attrValue;
/*  671 */     if (isDefaultValue(attr)) {
/*  672 */       attr = this.defaults.getAutowire();
/*      */     }
/*  674 */     int autowire = 0;
/*  675 */     if ("byName".equals(attr)) {
/*  676 */       autowire = 1;
/*      */     }
/*  678 */     else if ("byType".equals(attr)) {
/*  679 */       autowire = 2;
/*      */     }
/*  681 */     else if ("constructor".equals(attr)) {
/*  682 */       autowire = 3;
/*      */     }
/*  684 */     else if ("autodetect".equals(attr)) {
/*  685 */       autowire = 4;
/*      */     } 
/*      */     
/*  688 */     return autowire;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void parseConstructorArgElements(Element beanEle, BeanDefinition bd) {
/*  695 */     NodeList nl = beanEle.getChildNodes();
/*  696 */     for (int i = 0; i < nl.getLength(); i++) {
/*  697 */       Node node = nl.item(i);
/*  698 */       if (isCandidateElement(node) && nodeNameEquals(node, "constructor-arg")) {
/*  699 */         parseConstructorArgElement((Element)node, bd);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void parsePropertyElements(Element beanEle, BeanDefinition bd) {
/*  708 */     NodeList nl = beanEle.getChildNodes();
/*  709 */     for (int i = 0; i < nl.getLength(); i++) {
/*  710 */       Node node = nl.item(i);
/*  711 */       if (isCandidateElement(node) && nodeNameEquals(node, "property")) {
/*  712 */         parsePropertyElement((Element)node, bd);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void parseQualifierElements(Element beanEle, AbstractBeanDefinition bd) {
/*  721 */     NodeList nl = beanEle.getChildNodes();
/*  722 */     for (int i = 0; i < nl.getLength(); i++) {
/*  723 */       Node node = nl.item(i);
/*  724 */       if (isCandidateElement(node) && nodeNameEquals(node, "qualifier")) {
/*  725 */         parseQualifierElement((Element)node, bd);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void parseLookupOverrideSubElements(Element beanEle, MethodOverrides overrides) {
/*  734 */     NodeList nl = beanEle.getChildNodes();
/*  735 */     for (int i = 0; i < nl.getLength(); i++) {
/*  736 */       Node node = nl.item(i);
/*  737 */       if (isCandidateElement(node) && nodeNameEquals(node, "lookup-method")) {
/*  738 */         Element ele = (Element)node;
/*  739 */         String methodName = ele.getAttribute("name");
/*  740 */         String beanRef = ele.getAttribute("bean");
/*  741 */         LookupOverride override = new LookupOverride(methodName, beanRef);
/*  742 */         override.setSource(extractSource(ele));
/*  743 */         overrides.addOverride((MethodOverride)override);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void parseReplacedMethodSubElements(Element beanEle, MethodOverrides overrides) {
/*  752 */     NodeList nl = beanEle.getChildNodes();
/*  753 */     for (int i = 0; i < nl.getLength(); i++) {
/*  754 */       Node node = nl.item(i);
/*  755 */       if (isCandidateElement(node) && nodeNameEquals(node, "replaced-method")) {
/*  756 */         Element replacedMethodEle = (Element)node;
/*  757 */         String name = replacedMethodEle.getAttribute("name");
/*  758 */         String callback = replacedMethodEle.getAttribute("replacer");
/*  759 */         ReplaceOverride replaceOverride = new ReplaceOverride(name, callback);
/*      */         
/*  761 */         List<Element> argTypeEles = DomUtils.getChildElementsByTagName(replacedMethodEle, "arg-type");
/*  762 */         for (Element argTypeEle : argTypeEles) {
/*  763 */           String match = argTypeEle.getAttribute("match");
/*  764 */           match = StringUtils.hasText(match) ? match : DomUtils.getTextValue(argTypeEle);
/*  765 */           if (StringUtils.hasText(match)) {
/*  766 */             replaceOverride.addTypeIdentifier(match);
/*      */           }
/*      */         } 
/*  769 */         replaceOverride.setSource(extractSource(replacedMethodEle));
/*  770 */         overrides.addOverride((MethodOverride)replaceOverride);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void parseConstructorArgElement(Element ele, BeanDefinition bd) {
/*  779 */     String indexAttr = ele.getAttribute("index");
/*  780 */     String typeAttr = ele.getAttribute("type");
/*  781 */     String nameAttr = ele.getAttribute("name");
/*  782 */     if (StringUtils.hasLength(indexAttr)) {
/*      */       try {
/*  784 */         int index = Integer.parseInt(indexAttr);
/*  785 */         if (index < 0) {
/*  786 */           error("'index' cannot be lower than 0", ele);
/*      */         } else {
/*      */           
/*      */           try {
/*  790 */             this.parseState.push((ParseState.Entry)new ConstructorArgumentEntry(index));
/*  791 */             Object value = parsePropertyValue(ele, bd, null);
/*  792 */             ConstructorArgumentValues.ValueHolder valueHolder = new ConstructorArgumentValues.ValueHolder(value);
/*  793 */             if (StringUtils.hasLength(typeAttr)) {
/*  794 */               valueHolder.setType(typeAttr);
/*      */             }
/*  796 */             if (StringUtils.hasLength(nameAttr)) {
/*  797 */               valueHolder.setName(nameAttr);
/*      */             }
/*  799 */             valueHolder.setSource(extractSource(ele));
/*  800 */             if (bd.getConstructorArgumentValues().hasIndexedArgumentValue(index)) {
/*  801 */               error("Ambiguous constructor-arg entries for index " + index, ele);
/*      */             } else {
/*      */               
/*  804 */               bd.getConstructorArgumentValues().addIndexedArgumentValue(index, valueHolder);
/*      */             } 
/*      */           } finally {
/*      */             
/*  808 */             this.parseState.pop();
/*      */           }
/*      */         
/*      */         } 
/*  812 */       } catch (NumberFormatException ex) {
/*  813 */         error("Attribute 'index' of tag 'constructor-arg' must be an integer", ele);
/*      */       } 
/*      */     } else {
/*      */       
/*      */       try {
/*  818 */         this.parseState.push((ParseState.Entry)new ConstructorArgumentEntry());
/*  819 */         Object value = parsePropertyValue(ele, bd, null);
/*  820 */         ConstructorArgumentValues.ValueHolder valueHolder = new ConstructorArgumentValues.ValueHolder(value);
/*  821 */         if (StringUtils.hasLength(typeAttr)) {
/*  822 */           valueHolder.setType(typeAttr);
/*      */         }
/*  824 */         if (StringUtils.hasLength(nameAttr)) {
/*  825 */           valueHolder.setName(nameAttr);
/*      */         }
/*  827 */         valueHolder.setSource(extractSource(ele));
/*  828 */         bd.getConstructorArgumentValues().addGenericArgumentValue(valueHolder);
/*      */       } finally {
/*      */         
/*  831 */         this.parseState.pop();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void parsePropertyElement(Element ele, BeanDefinition bd) {
/*  840 */     String propertyName = ele.getAttribute("name");
/*  841 */     if (!StringUtils.hasLength(propertyName)) {
/*  842 */       error("Tag 'property' must have a 'name' attribute", ele);
/*      */       return;
/*      */     } 
/*  845 */     this.parseState.push((ParseState.Entry)new PropertyEntry(propertyName));
/*      */     try {
/*  847 */       if (bd.getPropertyValues().contains(propertyName)) {
/*  848 */         error("Multiple 'property' definitions for property '" + propertyName + "'", ele);
/*      */         return;
/*      */       } 
/*  851 */       Object val = parsePropertyValue(ele, bd, propertyName);
/*  852 */       PropertyValue pv = new PropertyValue(propertyName, val);
/*  853 */       parseMetaElements(ele, (BeanMetadataAttributeAccessor)pv);
/*  854 */       pv.setSource(extractSource(ele));
/*  855 */       bd.getPropertyValues().addPropertyValue(pv);
/*      */     } finally {
/*      */       
/*  858 */       this.parseState.pop();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void parseQualifierElement(Element ele, AbstractBeanDefinition bd) {
/*  866 */     String typeName = ele.getAttribute("type");
/*  867 */     if (!StringUtils.hasLength(typeName)) {
/*  868 */       error("Tag 'qualifier' must have a 'type' attribute", ele);
/*      */       return;
/*      */     } 
/*  871 */     this.parseState.push((ParseState.Entry)new QualifierEntry(typeName));
/*      */     try {
/*  873 */       AutowireCandidateQualifier qualifier = new AutowireCandidateQualifier(typeName);
/*  874 */       qualifier.setSource(extractSource(ele));
/*  875 */       String value = ele.getAttribute("value");
/*  876 */       if (StringUtils.hasLength(value)) {
/*  877 */         qualifier.setAttribute("value", value);
/*      */       }
/*  879 */       NodeList nl = ele.getChildNodes();
/*  880 */       for (int i = 0; i < nl.getLength(); i++) {
/*  881 */         Node node = nl.item(i);
/*  882 */         if (isCandidateElement(node) && nodeNameEquals(node, "attribute")) {
/*  883 */           Element attributeEle = (Element)node;
/*  884 */           String attributeName = attributeEle.getAttribute("key");
/*  885 */           String attributeValue = attributeEle.getAttribute("value");
/*  886 */           if (StringUtils.hasLength(attributeName) && StringUtils.hasLength(attributeValue)) {
/*  887 */             BeanMetadataAttribute attribute = new BeanMetadataAttribute(attributeName, attributeValue);
/*  888 */             attribute.setSource(extractSource(attributeEle));
/*  889 */             qualifier.addMetadataAttribute(attribute);
/*      */           } else {
/*      */             
/*  892 */             error("Qualifier 'attribute' tag must have a 'name' and 'value'", attributeEle);
/*      */             return;
/*      */           } 
/*      */         } 
/*      */       } 
/*  897 */       bd.addQualifier(qualifier);
/*      */     } finally {
/*      */       
/*  900 */       this.parseState.pop();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Object parsePropertyValue(Element ele, BeanDefinition bd, @Nullable String propertyName) {
/*  910 */     String elementName = (propertyName != null) ? ("<property> element for property '" + propertyName + "'") : "<constructor-arg> element";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  915 */     NodeList nl = ele.getChildNodes();
/*  916 */     Element subElement = null;
/*  917 */     for (int i = 0; i < nl.getLength(); i++) {
/*  918 */       Node node = nl.item(i);
/*  919 */       if (node instanceof Element && !nodeNameEquals(node, "description") && 
/*  920 */         !nodeNameEquals(node, "meta"))
/*      */       {
/*  922 */         if (subElement != null) {
/*  923 */           error(elementName + " must not contain more than one sub-element", ele);
/*      */         } else {
/*      */           
/*  926 */           subElement = (Element)node;
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/*  931 */     boolean hasRefAttribute = ele.hasAttribute("ref");
/*  932 */     boolean hasValueAttribute = ele.hasAttribute("value");
/*  933 */     if ((hasRefAttribute && hasValueAttribute) || ((hasRefAttribute || hasValueAttribute) && subElement != null))
/*      */     {
/*  935 */       error(elementName + " is only allowed to contain either 'ref' attribute OR 'value' attribute OR sub-element", ele);
/*      */     }
/*      */ 
/*      */     
/*  939 */     if (hasRefAttribute) {
/*  940 */       String refName = ele.getAttribute("ref");
/*  941 */       if (!StringUtils.hasText(refName)) {
/*  942 */         error(elementName + " contains empty 'ref' attribute", ele);
/*      */       }
/*  944 */       RuntimeBeanReference ref = new RuntimeBeanReference(refName);
/*  945 */       ref.setSource(extractSource(ele));
/*  946 */       return ref;
/*      */     } 
/*  948 */     if (hasValueAttribute) {
/*  949 */       TypedStringValue valueHolder = new TypedStringValue(ele.getAttribute("value"));
/*  950 */       valueHolder.setSource(extractSource(ele));
/*  951 */       return valueHolder;
/*      */     } 
/*  953 */     if (subElement != null) {
/*  954 */       return parsePropertySubElement(subElement, bd);
/*      */     }
/*      */ 
/*      */     
/*  958 */     error(elementName + " must specify a ref or value", ele);
/*  959 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Object parsePropertySubElement(Element ele, @Nullable BeanDefinition bd) {
/*  971 */     return parsePropertySubElement(ele, bd, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Object parsePropertySubElement(Element ele, @Nullable BeanDefinition bd, @Nullable String defaultValueType) {
/*  984 */     if (!isDefaultNamespace(ele)) {
/*  985 */       return parseNestedCustomElement(ele, bd);
/*      */     }
/*  987 */     if (nodeNameEquals(ele, "bean")) {
/*  988 */       BeanDefinitionHolder nestedBd = parseBeanDefinitionElement(ele, bd);
/*  989 */       if (nestedBd != null) {
/*  990 */         nestedBd = decorateBeanDefinitionIfRequired(ele, nestedBd, bd);
/*      */       }
/*  992 */       return nestedBd;
/*      */     } 
/*  994 */     if (nodeNameEquals(ele, "ref")) {
/*      */       
/*  996 */       String refName = ele.getAttribute("bean");
/*  997 */       boolean toParent = false;
/*  998 */       if (!StringUtils.hasLength(refName)) {
/*      */         
/* 1000 */         refName = ele.getAttribute("parent");
/* 1001 */         toParent = true;
/* 1002 */         if (!StringUtils.hasLength(refName)) {
/* 1003 */           error("'bean' or 'parent' is required for <ref> element", ele);
/* 1004 */           return null;
/*      */         } 
/*      */       } 
/* 1007 */       if (!StringUtils.hasText(refName)) {
/* 1008 */         error("<ref> element contains empty target attribute", ele);
/* 1009 */         return null;
/*      */       } 
/* 1011 */       RuntimeBeanReference ref = new RuntimeBeanReference(refName, toParent);
/* 1012 */       ref.setSource(extractSource(ele));
/* 1013 */       return ref;
/*      */     } 
/* 1015 */     if (nodeNameEquals(ele, "idref")) {
/* 1016 */       return parseIdRefElement(ele);
/*      */     }
/* 1018 */     if (nodeNameEquals(ele, "value")) {
/* 1019 */       return parseValueElement(ele, defaultValueType);
/*      */     }
/* 1021 */     if (nodeNameEquals(ele, "null")) {
/*      */ 
/*      */       
/* 1024 */       TypedStringValue nullHolder = new TypedStringValue(null);
/* 1025 */       nullHolder.setSource(extractSource(ele));
/* 1026 */       return nullHolder;
/*      */     } 
/* 1028 */     if (nodeNameEquals(ele, "array")) {
/* 1029 */       return parseArrayElement(ele, bd);
/*      */     }
/* 1031 */     if (nodeNameEquals(ele, "list")) {
/* 1032 */       return parseListElement(ele, bd);
/*      */     }
/* 1034 */     if (nodeNameEquals(ele, "set")) {
/* 1035 */       return parseSetElement(ele, bd);
/*      */     }
/* 1037 */     if (nodeNameEquals(ele, "map")) {
/* 1038 */       return parseMapElement(ele, bd);
/*      */     }
/* 1040 */     if (nodeNameEquals(ele, "props")) {
/* 1041 */       return parsePropsElement(ele);
/*      */     }
/*      */     
/* 1044 */     error("Unknown property sub-element: [" + ele.getNodeName() + "]", ele);
/* 1045 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Object parseIdRefElement(Element ele) {
/* 1055 */     String refName = ele.getAttribute("bean");
/* 1056 */     if (!StringUtils.hasLength(refName)) {
/* 1057 */       error("'bean' is required for <idref> element", ele);
/* 1058 */       return null;
/*      */     } 
/* 1060 */     if (!StringUtils.hasText(refName)) {
/* 1061 */       error("<idref> element contains empty target attribute", ele);
/* 1062 */       return null;
/*      */     } 
/* 1064 */     RuntimeBeanNameReference ref = new RuntimeBeanNameReference(refName);
/* 1065 */     ref.setSource(extractSource(ele));
/* 1066 */     return ref;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object parseValueElement(Element ele, @Nullable String defaultTypeName) {
/* 1074 */     String value = DomUtils.getTextValue(ele);
/* 1075 */     String specifiedTypeName = ele.getAttribute("type");
/* 1076 */     String typeName = specifiedTypeName;
/* 1077 */     if (!StringUtils.hasText(typeName)) {
/* 1078 */       typeName = defaultTypeName;
/*      */     }
/*      */     try {
/* 1081 */       TypedStringValue typedValue = buildTypedStringValue(value, typeName);
/* 1082 */       typedValue.setSource(extractSource(ele));
/* 1083 */       typedValue.setSpecifiedTypeName(specifiedTypeName);
/* 1084 */       return typedValue;
/*      */     }
/* 1086 */     catch (ClassNotFoundException ex) {
/* 1087 */       error("Type class [" + typeName + "] not found for <value> element", ele, ex);
/* 1088 */       return value;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected TypedStringValue buildTypedStringValue(String value, @Nullable String targetTypeName) throws ClassNotFoundException {
/*      */     TypedStringValue typedValue;
/* 1099 */     ClassLoader classLoader = this.readerContext.getBeanClassLoader();
/*      */     
/* 1101 */     if (!StringUtils.hasText(targetTypeName)) {
/* 1102 */       typedValue = new TypedStringValue(value);
/*      */     }
/* 1104 */     else if (classLoader != null) {
/* 1105 */       Class<?> targetType = ClassUtils.forName(targetTypeName, classLoader);
/* 1106 */       typedValue = new TypedStringValue(value, targetType);
/*      */     } else {
/*      */       
/* 1109 */       typedValue = new TypedStringValue(value, targetTypeName);
/*      */     } 
/* 1111 */     return typedValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object parseArrayElement(Element arrayEle, @Nullable BeanDefinition bd) {
/* 1118 */     String elementType = arrayEle.getAttribute("value-type");
/* 1119 */     NodeList nl = arrayEle.getChildNodes();
/* 1120 */     ManagedArray target = new ManagedArray(elementType, nl.getLength());
/* 1121 */     target.setSource(extractSource(arrayEle));
/* 1122 */     target.setElementTypeName(elementType);
/* 1123 */     target.setMergeEnabled(parseMergeAttribute(arrayEle));
/* 1124 */     parseCollectionElements(nl, (Collection<Object>)target, bd, elementType);
/* 1125 */     return target;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Object> parseListElement(Element collectionEle, @Nullable BeanDefinition bd) {
/* 1132 */     String defaultElementType = collectionEle.getAttribute("value-type");
/* 1133 */     NodeList nl = collectionEle.getChildNodes();
/* 1134 */     ManagedList<Object> target = new ManagedList(nl.getLength());
/* 1135 */     target.setSource(extractSource(collectionEle));
/* 1136 */     target.setElementTypeName(defaultElementType);
/* 1137 */     target.setMergeEnabled(parseMergeAttribute(collectionEle));
/* 1138 */     parseCollectionElements(nl, (Collection<Object>)target, bd, defaultElementType);
/* 1139 */     return (List<Object>)target;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<Object> parseSetElement(Element collectionEle, @Nullable BeanDefinition bd) {
/* 1146 */     String defaultElementType = collectionEle.getAttribute("value-type");
/* 1147 */     NodeList nl = collectionEle.getChildNodes();
/* 1148 */     ManagedSet<Object> target = new ManagedSet(nl.getLength());
/* 1149 */     target.setSource(extractSource(collectionEle));
/* 1150 */     target.setElementTypeName(defaultElementType);
/* 1151 */     target.setMergeEnabled(parseMergeAttribute(collectionEle));
/* 1152 */     parseCollectionElements(nl, (Collection<Object>)target, bd, defaultElementType);
/* 1153 */     return (Set<Object>)target;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void parseCollectionElements(NodeList elementNodes, Collection<Object> target, @Nullable BeanDefinition bd, String defaultElementType) {
/* 1159 */     for (int i = 0; i < elementNodes.getLength(); i++) {
/* 1160 */       Node node = elementNodes.item(i);
/* 1161 */       if (node instanceof Element && !nodeNameEquals(node, "description")) {
/* 1162 */         target.add(parsePropertySubElement((Element)node, bd, defaultElementType));
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<Object, Object> parseMapElement(Element mapEle, @Nullable BeanDefinition bd) {
/* 1171 */     String defaultKeyType = mapEle.getAttribute("key-type");
/* 1172 */     String defaultValueType = mapEle.getAttribute("value-type");
/*      */     
/* 1174 */     List<Element> entryEles = DomUtils.getChildElementsByTagName(mapEle, "entry");
/* 1175 */     ManagedMap<Object, Object> map = new ManagedMap(entryEles.size());
/* 1176 */     map.setSource(extractSource(mapEle));
/* 1177 */     map.setKeyTypeName(defaultKeyType);
/* 1178 */     map.setValueTypeName(defaultValueType);
/* 1179 */     map.setMergeEnabled(parseMergeAttribute(mapEle));
/*      */     
/* 1181 */     for (Element entryEle : entryEles) {
/*      */ 
/*      */       
/* 1184 */       NodeList entrySubNodes = entryEle.getChildNodes();
/* 1185 */       Element keyEle = null;
/* 1186 */       Element valueEle = null;
/* 1187 */       for (int j = 0; j < entrySubNodes.getLength(); j++) {
/* 1188 */         Node node = entrySubNodes.item(j);
/* 1189 */         if (node instanceof Element) {
/* 1190 */           Element candidateEle = (Element)node;
/* 1191 */           if (nodeNameEquals(candidateEle, "key")) {
/* 1192 */             if (keyEle != null) {
/* 1193 */               error("<entry> element is only allowed to contain one <key> sub-element", entryEle);
/*      */             } else {
/*      */               
/* 1196 */               keyEle = candidateEle;
/*      */             
/*      */             }
/*      */           
/*      */           }
/* 1201 */           else if (!nodeNameEquals(candidateEle, "description")) {
/*      */ 
/*      */             
/* 1204 */             if (valueEle != null) {
/* 1205 */               error("<entry> element must not contain more than one value sub-element", entryEle);
/*      */             } else {
/*      */               
/* 1208 */               valueEle = candidateEle;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1215 */       Object key = null;
/* 1216 */       boolean hasKeyAttribute = entryEle.hasAttribute("key");
/* 1217 */       boolean hasKeyRefAttribute = entryEle.hasAttribute("key-ref");
/* 1218 */       if ((hasKeyAttribute && hasKeyRefAttribute) || ((hasKeyAttribute || hasKeyRefAttribute) && keyEle != null))
/*      */       {
/* 1220 */         error("<entry> element is only allowed to contain either a 'key' attribute OR a 'key-ref' attribute OR a <key> sub-element", entryEle);
/*      */       }
/*      */       
/* 1223 */       if (hasKeyAttribute) {
/* 1224 */         key = buildTypedStringValueForMap(entryEle.getAttribute("key"), defaultKeyType, entryEle);
/*      */       }
/* 1226 */       else if (hasKeyRefAttribute) {
/* 1227 */         String refName = entryEle.getAttribute("key-ref");
/* 1228 */         if (!StringUtils.hasText(refName)) {
/* 1229 */           error("<entry> element contains empty 'key-ref' attribute", entryEle);
/*      */         }
/* 1231 */         RuntimeBeanReference ref = new RuntimeBeanReference(refName);
/* 1232 */         ref.setSource(extractSource(entryEle));
/* 1233 */         key = ref;
/*      */       }
/* 1235 */       else if (keyEle != null) {
/* 1236 */         key = parseKeyElement(keyEle, bd, defaultKeyType);
/*      */       } else {
/*      */         
/* 1239 */         error("<entry> element must specify a key", entryEle);
/*      */       } 
/*      */ 
/*      */       
/* 1243 */       Object value = null;
/* 1244 */       boolean hasValueAttribute = entryEle.hasAttribute("value");
/* 1245 */       boolean hasValueRefAttribute = entryEle.hasAttribute("value-ref");
/* 1246 */       boolean hasValueTypeAttribute = entryEle.hasAttribute("value-type");
/* 1247 */       if ((hasValueAttribute && hasValueRefAttribute) || ((hasValueAttribute || hasValueRefAttribute) && valueEle != null))
/*      */       {
/* 1249 */         error("<entry> element is only allowed to contain either 'value' attribute OR 'value-ref' attribute OR <value> sub-element", entryEle);
/*      */       }
/*      */       
/* 1252 */       if ((hasValueTypeAttribute && hasValueRefAttribute) || (hasValueTypeAttribute && !hasValueAttribute) || (hasValueTypeAttribute && valueEle != null))
/*      */       {
/*      */         
/* 1255 */         error("<entry> element is only allowed to contain a 'value-type' attribute when it has a 'value' attribute", entryEle);
/*      */       }
/*      */       
/* 1258 */       if (hasValueAttribute) {
/* 1259 */         String valueType = entryEle.getAttribute("value-type");
/* 1260 */         if (!StringUtils.hasText(valueType)) {
/* 1261 */           valueType = defaultValueType;
/*      */         }
/* 1263 */         value = buildTypedStringValueForMap(entryEle.getAttribute("value"), valueType, entryEle);
/*      */       }
/* 1265 */       else if (hasValueRefAttribute) {
/* 1266 */         String refName = entryEle.getAttribute("value-ref");
/* 1267 */         if (!StringUtils.hasText(refName)) {
/* 1268 */           error("<entry> element contains empty 'value-ref' attribute", entryEle);
/*      */         }
/* 1270 */         RuntimeBeanReference ref = new RuntimeBeanReference(refName);
/* 1271 */         ref.setSource(extractSource(entryEle));
/* 1272 */         value = ref;
/*      */       }
/* 1274 */       else if (valueEle != null) {
/* 1275 */         value = parsePropertySubElement(valueEle, bd, defaultValueType);
/*      */       } else {
/*      */         
/* 1278 */         error("<entry> element must specify a value", entryEle);
/*      */       } 
/*      */ 
/*      */       
/* 1282 */       map.put(key, value);
/*      */     } 
/*      */     
/* 1285 */     return (Map<Object, Object>)map;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final Object buildTypedStringValueForMap(String value, String defaultTypeName, Element entryEle) {
/*      */     try {
/* 1294 */       TypedStringValue typedValue = buildTypedStringValue(value, defaultTypeName);
/* 1295 */       typedValue.setSource(extractSource(entryEle));
/* 1296 */       return typedValue;
/*      */     }
/* 1298 */     catch (ClassNotFoundException ex) {
/* 1299 */       error("Type class [" + defaultTypeName + "] not found for Map key/value type", entryEle, ex);
/* 1300 */       return value;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected Object parseKeyElement(Element keyEle, @Nullable BeanDefinition bd, String defaultKeyTypeName) {
/* 1309 */     NodeList nl = keyEle.getChildNodes();
/* 1310 */     Element subElement = null;
/* 1311 */     for (int i = 0; i < nl.getLength(); i++) {
/* 1312 */       Node node = nl.item(i);
/* 1313 */       if (node instanceof Element)
/*      */       {
/* 1315 */         if (subElement != null) {
/* 1316 */           error("<key> element must not contain more than one value sub-element", keyEle);
/*      */         } else {
/*      */           
/* 1319 */           subElement = (Element)node;
/*      */         } 
/*      */       }
/*      */     } 
/* 1323 */     if (subElement == null) {
/* 1324 */       return null;
/*      */     }
/* 1326 */     return parsePropertySubElement(subElement, bd, defaultKeyTypeName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Properties parsePropsElement(Element propsEle) {
/* 1333 */     ManagedProperties props = new ManagedProperties();
/* 1334 */     props.setSource(extractSource(propsEle));
/* 1335 */     props.setMergeEnabled(parseMergeAttribute(propsEle));
/*      */     
/* 1337 */     List<Element> propEles = DomUtils.getChildElementsByTagName(propsEle, "prop");
/* 1338 */     for (Element propEle : propEles) {
/* 1339 */       String key = propEle.getAttribute("key");
/*      */ 
/*      */       
/* 1342 */       String value = DomUtils.getTextValue(propEle).trim();
/* 1343 */       TypedStringValue keyHolder = new TypedStringValue(key);
/* 1344 */       keyHolder.setSource(extractSource(propEle));
/* 1345 */       TypedStringValue valueHolder = new TypedStringValue(value);
/* 1346 */       valueHolder.setSource(extractSource(propEle));
/* 1347 */       props.put(keyHolder, valueHolder);
/*      */     } 
/*      */     
/* 1350 */     return (Properties)props;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean parseMergeAttribute(Element collectionElement) {
/* 1357 */     String value = collectionElement.getAttribute("merge");
/* 1358 */     if (isDefaultValue(value)) {
/* 1359 */       value = this.defaults.getMerge();
/*      */     }
/* 1361 */     return "true".equals(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public BeanDefinition parseCustomElement(Element ele) {
/* 1371 */     return parseCustomElement(ele, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public BeanDefinition parseCustomElement(Element ele, @Nullable BeanDefinition containingBd) {
/* 1382 */     String namespaceUri = getNamespaceURI(ele);
/* 1383 */     if (namespaceUri == null) {
/* 1384 */       return null;
/*      */     }
/* 1386 */     NamespaceHandler handler = this.readerContext.getNamespaceHandlerResolver().resolve(namespaceUri);
/* 1387 */     if (handler == null) {
/* 1388 */       error("Unable to locate Spring NamespaceHandler for XML schema namespace [" + namespaceUri + "]", ele);
/* 1389 */       return null;
/*      */     } 
/* 1391 */     return handler.parse(ele, new ParserContext(this.readerContext, this, containingBd));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BeanDefinitionHolder decorateBeanDefinitionIfRequired(Element ele, BeanDefinitionHolder originalDef) {
/* 1401 */     return decorateBeanDefinitionIfRequired(ele, originalDef, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BeanDefinitionHolder decorateBeanDefinitionIfRequired(Element ele, BeanDefinitionHolder originalDef, @Nullable BeanDefinition containingBd) {
/* 1414 */     BeanDefinitionHolder finalDefinition = originalDef;
/*      */ 
/*      */     
/* 1417 */     NamedNodeMap attributes = ele.getAttributes();
/* 1418 */     for (int i = 0; i < attributes.getLength(); i++) {
/* 1419 */       Node node = attributes.item(i);
/* 1420 */       finalDefinition = decorateIfRequired(node, finalDefinition, containingBd);
/*      */     } 
/*      */ 
/*      */     
/* 1424 */     NodeList children = ele.getChildNodes();
/* 1425 */     for (int j = 0; j < children.getLength(); j++) {
/* 1426 */       Node node = children.item(j);
/* 1427 */       if (node.getNodeType() == 1) {
/* 1428 */         finalDefinition = decorateIfRequired(node, finalDefinition, containingBd);
/*      */       }
/*      */     } 
/* 1431 */     return finalDefinition;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BeanDefinitionHolder decorateIfRequired(Node node, BeanDefinitionHolder originalDef, @Nullable BeanDefinition containingBd) {
/* 1445 */     String namespaceUri = getNamespaceURI(node);
/* 1446 */     if (namespaceUri != null && !isDefaultNamespace(namespaceUri)) {
/* 1447 */       NamespaceHandler handler = this.readerContext.getNamespaceHandlerResolver().resolve(namespaceUri);
/* 1448 */       if (handler != null) {
/*      */         
/* 1450 */         BeanDefinitionHolder decorated = handler.decorate(node, originalDef, new ParserContext(this.readerContext, this, containingBd));
/* 1451 */         if (decorated != null) {
/* 1452 */           return decorated;
/*      */         }
/*      */       }
/* 1455 */       else if (namespaceUri.startsWith("http://www.springframework.org/")) {
/* 1456 */         error("Unable to locate Spring NamespaceHandler for XML schema namespace [" + namespaceUri + "]", node);
/*      */ 
/*      */       
/*      */       }
/* 1460 */       else if (this.logger.isDebugEnabled()) {
/* 1461 */         this.logger.debug("No Spring NamespaceHandler found for XML schema namespace [" + namespaceUri + "]");
/*      */       } 
/*      */     } 
/*      */     
/* 1465 */     return originalDef;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private BeanDefinitionHolder parseNestedCustomElement(Element ele, @Nullable BeanDefinition containingBd) {
/* 1470 */     BeanDefinition innerDefinition = parseCustomElement(ele, containingBd);
/* 1471 */     if (innerDefinition == null) {
/* 1472 */       error("Incorrect usage of element '" + ele.getNodeName() + "' in a nested manner. This tag cannot be used nested inside <property>.", ele);
/*      */       
/* 1474 */       return null;
/*      */     } 
/*      */     
/* 1477 */     String id = ele.getNodeName() + "#" + ObjectUtils.getIdentityHexString(innerDefinition);
/* 1478 */     if (this.logger.isTraceEnabled()) {
/* 1479 */       this.logger.trace("Using generated bean name [" + id + "] for nested custom element '" + ele
/* 1480 */           .getNodeName() + "'");
/*      */     }
/* 1482 */     return new BeanDefinitionHolder(innerDefinition, id);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public String getNamespaceURI(Node node) {
/* 1495 */     return node.getNamespaceURI();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getLocalName(Node node) {
/* 1506 */     return node.getLocalName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean nodeNameEquals(Node node, String desiredName) {
/* 1519 */     return (desiredName.equals(node.getNodeName()) || desiredName.equals(getLocalName(node)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDefaultNamespace(@Nullable String namespaceUri) {
/* 1526 */     return (!StringUtils.hasLength(namespaceUri) || "http://www.springframework.org/schema/beans".equals(namespaceUri));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDefaultNamespace(Node node) {
/* 1533 */     return isDefaultNamespace(getNamespaceURI(node));
/*      */   }
/*      */   
/*      */   private boolean isDefaultValue(String value) {
/* 1537 */     return ("default".equals(value) || "".equals(value));
/*      */   }
/*      */   
/*      */   private boolean isCandidateElement(Node node) {
/* 1541 */     return (node instanceof Element && (isDefaultNamespace(node) || !isDefaultNamespace(node.getParentNode())));
/*      */   }
/*      */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/xml/BeanDefinitionParserDelegate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */