/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.factory.parsing.Location;
/*     */ import org.springframework.beans.factory.parsing.Problem;
/*     */ import org.springframework.beans.factory.parsing.ProblemReporter;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionReader;
/*     */ import org.springframework.core.io.DescriptiveResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.core.type.StandardAnnotationMetadata;
/*     */ import org.springframework.core.type.classreading.MetadataReader;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class ConfigurationClass
/*     */ {
/*     */   private final AnnotationMetadata metadata;
/*     */   private final Resource resource;
/*     */   @Nullable
/*     */   private String beanName;
/*  59 */   private final Set<ConfigurationClass> importedBy = new LinkedHashSet<>(1);
/*     */   
/*  61 */   private final Set<BeanMethod> beanMethods = new LinkedHashSet<>();
/*     */   
/*  63 */   private final Map<String, Class<? extends BeanDefinitionReader>> importedResources = new LinkedHashMap<>();
/*     */ 
/*     */   
/*  66 */   private final Map<ImportBeanDefinitionRegistrar, AnnotationMetadata> importBeanDefinitionRegistrars = new LinkedHashMap<>();
/*     */ 
/*     */   
/*  69 */   final Set<String> skippedBeanMethods = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurationClass(MetadataReader metadataReader, String beanName) {
/*  79 */     Assert.notNull(beanName, "Bean name must not be null");
/*  80 */     this.metadata = metadataReader.getAnnotationMetadata();
/*  81 */     this.resource = metadataReader.getResource();
/*  82 */     this.beanName = beanName;
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
/*     */   public ConfigurationClass(MetadataReader metadataReader, @Nullable ConfigurationClass importedBy) {
/*  94 */     this.metadata = metadataReader.getAnnotationMetadata();
/*  95 */     this.resource = metadataReader.getResource();
/*  96 */     this.importedBy.add(importedBy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurationClass(Class<?> clazz, String beanName) {
/* 106 */     Assert.notNull(beanName, "Bean name must not be null");
/* 107 */     this.metadata = (AnnotationMetadata)new StandardAnnotationMetadata(clazz, true);
/* 108 */     this.resource = (Resource)new DescriptiveResource(clazz.getName());
/* 109 */     this.beanName = beanName;
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
/*     */   public ConfigurationClass(Class<?> clazz, @Nullable ConfigurationClass importedBy) {
/* 121 */     this.metadata = (AnnotationMetadata)new StandardAnnotationMetadata(clazz, true);
/* 122 */     this.resource = (Resource)new DescriptiveResource(clazz.getName());
/* 123 */     this.importedBy.add(importedBy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurationClass(AnnotationMetadata metadata, String beanName) {
/* 133 */     Assert.notNull(beanName, "Bean name must not be null");
/* 134 */     this.metadata = metadata;
/* 135 */     this.resource = (Resource)new DescriptiveResource(metadata.getClassName());
/* 136 */     this.beanName = beanName;
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotationMetadata getMetadata() {
/* 141 */     return this.metadata;
/*     */   }
/*     */   
/*     */   public Resource getResource() {
/* 145 */     return this.resource;
/*     */   }
/*     */   
/*     */   public String getSimpleName() {
/* 149 */     return ClassUtils.getShortName(getMetadata().getClassName());
/*     */   }
/*     */   
/*     */   public void setBeanName(String beanName) {
/* 153 */     this.beanName = beanName;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public String getBeanName() {
/* 158 */     return this.beanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isImported() {
/* 168 */     return !this.importedBy.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mergeImportedBy(ConfigurationClass otherConfigClass) {
/* 176 */     this.importedBy.addAll(otherConfigClass.importedBy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<ConfigurationClass> getImportedBy() {
/* 186 */     return this.importedBy;
/*     */   }
/*     */   
/*     */   public void addBeanMethod(BeanMethod method) {
/* 190 */     this.beanMethods.add(method);
/*     */   }
/*     */   
/*     */   public Set<BeanMethod> getBeanMethods() {
/* 194 */     return this.beanMethods;
/*     */   }
/*     */   
/*     */   public void addImportedResource(String importedResource, Class<? extends BeanDefinitionReader> readerClass) {
/* 198 */     this.importedResources.put(importedResource, readerClass);
/*     */   }
/*     */   
/*     */   public void addImportBeanDefinitionRegistrar(ImportBeanDefinitionRegistrar registrar, AnnotationMetadata importingClassMetadata) {
/* 202 */     this.importBeanDefinitionRegistrars.put(registrar, importingClassMetadata);
/*     */   }
/*     */   
/*     */   public Map<ImportBeanDefinitionRegistrar, AnnotationMetadata> getImportBeanDefinitionRegistrars() {
/* 206 */     return this.importBeanDefinitionRegistrars;
/*     */   }
/*     */   
/*     */   public Map<String, Class<? extends BeanDefinitionReader>> getImportedResources() {
/* 210 */     return this.importedResources;
/*     */   }
/*     */ 
/*     */   
/*     */   public void validate(ProblemReporter problemReporter) {
/* 215 */     if (getMetadata().isAnnotated(Configuration.class.getName()) && 
/* 216 */       getMetadata().isFinal()) {
/* 217 */       problemReporter.error(new FinalConfigurationProblem());
/*     */     }
/*     */ 
/*     */     
/* 221 */     for (BeanMethod beanMethod : this.beanMethods) {
/* 222 */       beanMethod.validate(problemReporter);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 228 */     return (this == other || (other instanceof ConfigurationClass && 
/* 229 */       getMetadata().getClassName().equals(((ConfigurationClass)other).getMetadata().getClassName())));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 234 */     return getMetadata().getClassName().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 239 */     return "ConfigurationClass: beanName '" + this.beanName + "', " + this.resource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class FinalConfigurationProblem
/*     */     extends Problem
/*     */   {
/*     */     public FinalConfigurationProblem() {
/* 249 */       super(String.format("@Configuration class '%s' may not be final. Remove the final modifier to continue.", new Object[] { this$0
/* 250 */               .getSimpleName() }), new Location(ConfigurationClass.this.getResource(), ConfigurationClass.this.getMetadata()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/ConfigurationClass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */