/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.util.Set;
/*     */ import java.util.regex.Pattern;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.parsing.BeanComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.ComponentDefinition;
/*     */ import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
/*     */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*     */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*     */ import org.springframework.beans.factory.xml.ParserContext;
/*     */ import org.springframework.beans.factory.xml.XmlReaderContext;
/*     */ import org.springframework.core.type.filter.AnnotationTypeFilter;
/*     */ import org.springframework.core.type.filter.AspectJTypeFilter;
/*     */ import org.springframework.core.type.filter.AssignableTypeFilter;
/*     */ import org.springframework.core.type.filter.RegexPatternTypeFilter;
/*     */ import org.springframework.core.type.filter.TypeFilter;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class ComponentScanBeanDefinitionParser
/*     */   implements BeanDefinitionParser
/*     */ {
/*     */   private static final String BASE_PACKAGE_ATTRIBUTE = "base-package";
/*     */   private static final String RESOURCE_PATTERN_ATTRIBUTE = "resource-pattern";
/*     */   private static final String USE_DEFAULT_FILTERS_ATTRIBUTE = "use-default-filters";
/*     */   private static final String ANNOTATION_CONFIG_ATTRIBUTE = "annotation-config";
/*     */   private static final String NAME_GENERATOR_ATTRIBUTE = "name-generator";
/*     */   private static final String SCOPE_RESOLVER_ATTRIBUTE = "scope-resolver";
/*     */   private static final String SCOPED_PROXY_ATTRIBUTE = "scoped-proxy";
/*     */   private static final String EXCLUDE_FILTER_ELEMENT = "exclude-filter";
/*     */   private static final String INCLUDE_FILTER_ELEMENT = "include-filter";
/*     */   private static final String FILTER_TYPE_ATTRIBUTE = "type";
/*     */   private static final String FILTER_EXPRESSION_ATTRIBUTE = "expression";
/*     */   
/*     */   @Nullable
/*     */   public BeanDefinition parse(Element element, ParserContext parserContext) {
/*  83 */     String basePackage = element.getAttribute("base-package");
/*  84 */     basePackage = parserContext.getReaderContext().getEnvironment().resolvePlaceholders(basePackage);
/*  85 */     String[] basePackages = StringUtils.tokenizeToStringArray(basePackage, ",; \t\n");
/*     */ 
/*     */ 
/*     */     
/*  89 */     ClassPathBeanDefinitionScanner scanner = configureScanner(parserContext, element);
/*  90 */     Set<BeanDefinitionHolder> beanDefinitions = scanner.doScan(basePackages);
/*  91 */     registerComponents(parserContext.getReaderContext(), beanDefinitions, element);
/*     */     
/*  93 */     return null;
/*     */   }
/*     */   
/*     */   protected ClassPathBeanDefinitionScanner configureScanner(ParserContext parserContext, Element element) {
/*  97 */     boolean useDefaultFilters = true;
/*  98 */     if (element.hasAttribute("use-default-filters")) {
/*  99 */       useDefaultFilters = Boolean.valueOf(element.getAttribute("use-default-filters")).booleanValue();
/*     */     }
/*     */ 
/*     */     
/* 103 */     ClassPathBeanDefinitionScanner scanner = createScanner(parserContext.getReaderContext(), useDefaultFilters);
/* 104 */     scanner.setBeanDefinitionDefaults(parserContext.getDelegate().getBeanDefinitionDefaults());
/* 105 */     scanner.setAutowireCandidatePatterns(parserContext.getDelegate().getAutowireCandidatePatterns());
/*     */     
/* 107 */     if (element.hasAttribute("resource-pattern")) {
/* 108 */       scanner.setResourcePattern(element.getAttribute("resource-pattern"));
/*     */     }
/*     */     
/*     */     try {
/* 112 */       parseBeanNameGenerator(element, scanner);
/*     */     }
/* 114 */     catch (Exception ex) {
/* 115 */       parserContext.getReaderContext().error(ex.getMessage(), parserContext.extractSource(element), ex.getCause());
/*     */     } 
/*     */     
/*     */     try {
/* 119 */       parseScope(element, scanner);
/*     */     }
/* 121 */     catch (Exception ex) {
/* 122 */       parserContext.getReaderContext().error(ex.getMessage(), parserContext.extractSource(element), ex.getCause());
/*     */     } 
/*     */     
/* 125 */     parseTypeFilters(element, scanner, parserContext);
/*     */     
/* 127 */     return scanner;
/*     */   }
/*     */   
/*     */   protected ClassPathBeanDefinitionScanner createScanner(XmlReaderContext readerContext, boolean useDefaultFilters) {
/* 131 */     return new ClassPathBeanDefinitionScanner(readerContext.getRegistry(), useDefaultFilters, readerContext
/* 132 */         .getEnvironment(), readerContext.getResourceLoader());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerComponents(XmlReaderContext readerContext, Set<BeanDefinitionHolder> beanDefinitions, Element element) {
/* 138 */     Object source = readerContext.extractSource(element);
/* 139 */     CompositeComponentDefinition compositeDef = new CompositeComponentDefinition(element.getTagName(), source);
/*     */     
/* 141 */     for (BeanDefinitionHolder beanDefHolder : beanDefinitions) {
/* 142 */       compositeDef.addNestedComponent((ComponentDefinition)new BeanComponentDefinition(beanDefHolder));
/*     */     }
/*     */ 
/*     */     
/* 146 */     boolean annotationConfig = true;
/* 147 */     if (element.hasAttribute("annotation-config")) {
/* 148 */       annotationConfig = Boolean.valueOf(element.getAttribute("annotation-config")).booleanValue();
/*     */     }
/* 150 */     if (annotationConfig) {
/*     */       
/* 152 */       Set<BeanDefinitionHolder> processorDefinitions = AnnotationConfigUtils.registerAnnotationConfigProcessors(readerContext.getRegistry(), source);
/* 153 */       for (BeanDefinitionHolder processorDefinition : processorDefinitions) {
/* 154 */         compositeDef.addNestedComponent((ComponentDefinition)new BeanComponentDefinition(processorDefinition));
/*     */       }
/*     */     } 
/*     */     
/* 158 */     readerContext.fireComponentRegistered((ComponentDefinition)compositeDef);
/*     */   }
/*     */   
/*     */   protected void parseBeanNameGenerator(Element element, ClassPathBeanDefinitionScanner scanner) {
/* 162 */     if (element.hasAttribute("name-generator")) {
/* 163 */       BeanNameGenerator beanNameGenerator = (BeanNameGenerator)instantiateUserDefinedStrategy(element
/* 164 */           .getAttribute("name-generator"), BeanNameGenerator.class, scanner
/* 165 */           .getResourceLoader().getClassLoader());
/* 166 */       scanner.setBeanNameGenerator(beanNameGenerator);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void parseScope(Element element, ClassPathBeanDefinitionScanner scanner) {
/* 172 */     if (element.hasAttribute("scope-resolver")) {
/* 173 */       if (element.hasAttribute("scoped-proxy")) {
/* 174 */         throw new IllegalArgumentException("Cannot define both 'scope-resolver' and 'scoped-proxy' on <component-scan> tag");
/*     */       }
/*     */       
/* 177 */       ScopeMetadataResolver scopeMetadataResolver = (ScopeMetadataResolver)instantiateUserDefinedStrategy(element
/* 178 */           .getAttribute("scope-resolver"), ScopeMetadataResolver.class, scanner
/* 179 */           .getResourceLoader().getClassLoader());
/* 180 */       scanner.setScopeMetadataResolver(scopeMetadataResolver);
/*     */     } 
/*     */     
/* 183 */     if (element.hasAttribute("scoped-proxy")) {
/* 184 */       String mode = element.getAttribute("scoped-proxy");
/* 185 */       if ("targetClass".equals(mode)) {
/* 186 */         scanner.setScopedProxyMode(ScopedProxyMode.TARGET_CLASS);
/*     */       }
/* 188 */       else if ("interfaces".equals(mode)) {
/* 189 */         scanner.setScopedProxyMode(ScopedProxyMode.INTERFACES);
/*     */       }
/* 191 */       else if ("no".equals(mode)) {
/* 192 */         scanner.setScopedProxyMode(ScopedProxyMode.NO);
/*     */       } else {
/*     */         
/* 195 */         throw new IllegalArgumentException("scoped-proxy only supports 'no', 'interfaces' and 'targetClass'");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void parseTypeFilters(Element element, ClassPathBeanDefinitionScanner scanner, ParserContext parserContext) {
/* 202 */     ClassLoader classLoader = scanner.getResourceLoader().getClassLoader();
/* 203 */     NodeList nodeList = element.getChildNodes();
/* 204 */     for (int i = 0; i < nodeList.getLength(); i++) {
/* 205 */       Node node = nodeList.item(i);
/* 206 */       if (node.getNodeType() == 1) {
/* 207 */         String localName = parserContext.getDelegate().getLocalName(node);
/*     */         try {
/* 209 */           if ("include-filter".equals(localName)) {
/* 210 */             TypeFilter typeFilter = createTypeFilter((Element)node, classLoader, parserContext);
/* 211 */             scanner.addIncludeFilter(typeFilter);
/*     */           }
/* 213 */           else if ("exclude-filter".equals(localName)) {
/* 214 */             TypeFilter typeFilter = createTypeFilter((Element)node, classLoader, parserContext);
/* 215 */             scanner.addExcludeFilter(typeFilter);
/*     */           }
/*     */         
/* 218 */         } catch (ClassNotFoundException ex) {
/* 219 */           parserContext.getReaderContext().warning("Ignoring non-present type filter class: " + ex, parserContext
/* 220 */               .extractSource(element));
/*     */         }
/* 222 */         catch (Exception ex) {
/* 223 */           parserContext.getReaderContext().error(ex
/* 224 */               .getMessage(), parserContext.extractSource(element), ex.getCause());
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TypeFilter createTypeFilter(Element element, @Nullable ClassLoader classLoader, ParserContext parserContext) throws ClassNotFoundException {
/* 234 */     String filterType = element.getAttribute("type");
/* 235 */     String expression = element.getAttribute("expression");
/* 236 */     expression = parserContext.getReaderContext().getEnvironment().resolvePlaceholders(expression);
/* 237 */     if ("annotation".equals(filterType)) {
/* 238 */       return (TypeFilter)new AnnotationTypeFilter(ClassUtils.forName(expression, classLoader));
/*     */     }
/* 240 */     if ("assignable".equals(filterType)) {
/* 241 */       return (TypeFilter)new AssignableTypeFilter(ClassUtils.forName(expression, classLoader));
/*     */     }
/* 243 */     if ("aspectj".equals(filterType)) {
/* 244 */       return (TypeFilter)new AspectJTypeFilter(expression, classLoader);
/*     */     }
/* 246 */     if ("regex".equals(filterType)) {
/* 247 */       return (TypeFilter)new RegexPatternTypeFilter(Pattern.compile(expression));
/*     */     }
/* 249 */     if ("custom".equals(filterType)) {
/* 250 */       Class<?> filterClass = ClassUtils.forName(expression, classLoader);
/* 251 */       if (!TypeFilter.class.isAssignableFrom(filterClass)) {
/* 252 */         throw new IllegalArgumentException("Class is not assignable to [" + TypeFilter.class
/* 253 */             .getName() + "]: " + expression);
/*     */       }
/* 255 */       return (TypeFilter)BeanUtils.instantiateClass(filterClass);
/*     */     } 
/*     */     
/* 258 */     throw new IllegalArgumentException("Unsupported filter type: " + filterType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object instantiateUserDefinedStrategy(String className, Class<?> strategyType, @Nullable ClassLoader classLoader) {
/*     */     Object result;
/*     */     try {
/* 268 */       result = ReflectionUtils.accessibleConstructor(ClassUtils.forName(className, classLoader), new Class[0]).newInstance(new Object[0]);
/*     */     }
/* 270 */     catch (ClassNotFoundException ex) {
/* 271 */       throw new IllegalArgumentException("Class [" + className + "] for strategy [" + strategyType
/* 272 */           .getName() + "] not found", ex);
/*     */     }
/* 274 */     catch (Throwable ex) {
/* 275 */       throw new IllegalArgumentException("Unable to instantiate class [" + className + "] for strategy [" + strategyType
/* 276 */           .getName() + "]: a zero-argument constructor is required", ex);
/*     */     } 
/*     */     
/* 279 */     if (!strategyType.isAssignableFrom(result.getClass())) {
/* 280 */       throw new IllegalArgumentException("Provided class name must be an implementation of " + strategyType);
/*     */     }
/* 282 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/ComponentScanBeanDefinitionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */