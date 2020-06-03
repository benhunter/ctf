/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Pattern;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.type.filter.AbstractTypeHierarchyTraversingFilter;
/*     */ import org.springframework.core.type.filter.AnnotationTypeFilter;
/*     */ import org.springframework.core.type.filter.AspectJTypeFilter;
/*     */ import org.springframework.core.type.filter.AssignableTypeFilter;
/*     */ import org.springframework.core.type.filter.RegexPatternTypeFilter;
/*     */ import org.springframework.core.type.filter.TypeFilter;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ class ComponentScanAnnotationParser
/*     */ {
/*     */   private final Environment environment;
/*     */   private final ResourceLoader resourceLoader;
/*     */   private final BeanNameGenerator beanNameGenerator;
/*     */   private final BeanDefinitionRegistry registry;
/*     */   
/*     */   public ComponentScanAnnotationParser(Environment environment, ResourceLoader resourceLoader, BeanNameGenerator beanNameGenerator, BeanDefinitionRegistry registry) {
/*  69 */     this.environment = environment;
/*  70 */     this.resourceLoader = resourceLoader;
/*  71 */     this.beanNameGenerator = beanNameGenerator;
/*  72 */     this.registry = registry;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<BeanDefinitionHolder> parse(AnnotationAttributes componentScan, final String declaringClass) {
/*  78 */     ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(this.registry, componentScan.getBoolean("useDefaultFilters"), this.environment, this.resourceLoader);
/*     */     
/*  80 */     Class<? extends BeanNameGenerator> generatorClass = componentScan.getClass("nameGenerator");
/*  81 */     boolean useInheritedGenerator = (BeanNameGenerator.class == generatorClass);
/*  82 */     scanner.setBeanNameGenerator(useInheritedGenerator ? this.beanNameGenerator : 
/*  83 */         (BeanNameGenerator)BeanUtils.instantiateClass(generatorClass));
/*     */     
/*  85 */     ScopedProxyMode scopedProxyMode = (ScopedProxyMode)componentScan.getEnum("scopedProxy");
/*  86 */     if (scopedProxyMode != ScopedProxyMode.DEFAULT) {
/*  87 */       scanner.setScopedProxyMode(scopedProxyMode);
/*     */     } else {
/*     */       
/*  90 */       Class<? extends ScopeMetadataResolver> resolverClass = componentScan.getClass("scopeResolver");
/*  91 */       scanner.setScopeMetadataResolver((ScopeMetadataResolver)BeanUtils.instantiateClass(resolverClass));
/*     */     } 
/*     */     
/*  94 */     scanner.setResourcePattern(componentScan.getString("resourcePattern"));
/*     */     
/*  96 */     for (AnnotationAttributes filter : componentScan.getAnnotationArray("includeFilters")) {
/*  97 */       for (TypeFilter typeFilter : typeFiltersFor(filter)) {
/*  98 */         scanner.addIncludeFilter(typeFilter);
/*     */       }
/*     */     } 
/* 101 */     for (AnnotationAttributes filter : componentScan.getAnnotationArray("excludeFilters")) {
/* 102 */       for (TypeFilter typeFilter : typeFiltersFor(filter)) {
/* 103 */         scanner.addExcludeFilter(typeFilter);
/*     */       }
/*     */     } 
/*     */     
/* 107 */     boolean lazyInit = componentScan.getBoolean("lazyInit");
/* 108 */     if (lazyInit) {
/* 109 */       scanner.getBeanDefinitionDefaults().setLazyInit(true);
/*     */     }
/*     */     
/* 112 */     Set<String> basePackages = new LinkedHashSet<>();
/* 113 */     String[] basePackagesArray = componentScan.getStringArray("basePackages");
/* 114 */     for (String pkg : basePackagesArray) {
/* 115 */       String[] tokenized = StringUtils.tokenizeToStringArray(this.environment.resolvePlaceholders(pkg), ",; \t\n");
/*     */       
/* 117 */       Collections.addAll(basePackages, tokenized);
/*     */     } 
/* 119 */     for (Class<?> clazz : componentScan.getClassArray("basePackageClasses")) {
/* 120 */       basePackages.add(ClassUtils.getPackageName(clazz));
/*     */     }
/* 122 */     if (basePackages.isEmpty()) {
/* 123 */       basePackages.add(ClassUtils.getPackageName(declaringClass));
/*     */     }
/*     */     
/* 126 */     scanner.addExcludeFilter((TypeFilter)new AbstractTypeHierarchyTraversingFilter(false, false)
/*     */         {
/*     */           protected boolean matchClassName(String className) {
/* 129 */             return declaringClass.equals(className);
/*     */           }
/*     */         });
/* 132 */     return scanner.doScan(StringUtils.toStringArray(basePackages));
/*     */   }
/*     */   
/*     */   private List<TypeFilter> typeFiltersFor(AnnotationAttributes filterAttributes) {
/* 136 */     List<TypeFilter> typeFilters = new ArrayList<>();
/* 137 */     FilterType filterType = (FilterType)filterAttributes.getEnum("type");
/*     */     
/* 139 */     for (Class<?> filterClass : filterAttributes.getClassArray("classes")) {
/* 140 */       Class<Annotation> annotationType; TypeFilter filter; switch (filterType) {
/*     */         case ANNOTATION:
/* 142 */           Assert.isAssignable(Annotation.class, filterClass, "@ComponentScan ANNOTATION type filter requires an annotation type");
/*     */ 
/*     */           
/* 145 */           annotationType = (Class)filterClass;
/* 146 */           typeFilters.add(new AnnotationTypeFilter(annotationType));
/*     */           break;
/*     */         case ASSIGNABLE_TYPE:
/* 149 */           typeFilters.add(new AssignableTypeFilter(filterClass));
/*     */           break;
/*     */         case CUSTOM:
/* 152 */           Assert.isAssignable(TypeFilter.class, filterClass, "@ComponentScan CUSTOM type filter requires a TypeFilter implementation");
/*     */           
/* 154 */           filter = (TypeFilter)BeanUtils.instantiateClass(filterClass, TypeFilter.class);
/* 155 */           ParserStrategyUtils.invokeAwareMethods(filter, this.environment, this.resourceLoader, this.registry);
/*     */           
/* 157 */           typeFilters.add(filter);
/*     */           break;
/*     */         default:
/* 160 */           throw new IllegalArgumentException("Filter type not supported with Class value: " + filterType);
/*     */       } 
/*     */     
/*     */     } 
/* 164 */     for (String expression : filterAttributes.getStringArray("pattern")) {
/* 165 */       switch (filterType) {
/*     */         case ASPECTJ:
/* 167 */           typeFilters.add(new AspectJTypeFilter(expression, this.resourceLoader.getClassLoader()));
/*     */           break;
/*     */         case REGEX:
/* 170 */           typeFilters.add(new RegexPatternTypeFilter(Pattern.compile(expression)));
/*     */           break;
/*     */         default:
/* 173 */           throw new IllegalArgumentException("Filter type not supported with String pattern: " + filterType);
/*     */       } 
/*     */     
/*     */     } 
/* 177 */     return typeFilters;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/ComponentScanAnnotationParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */