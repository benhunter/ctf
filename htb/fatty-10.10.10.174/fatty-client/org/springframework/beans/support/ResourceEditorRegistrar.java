/*     */ package org.springframework.beans.support;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import java.io.File;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.nio.file.Path;
/*     */ import org.springframework.beans.PropertyEditorRegistrar;
/*     */ import org.springframework.beans.PropertyEditorRegistry;
/*     */ import org.springframework.beans.PropertyEditorRegistrySupport;
/*     */ import org.springframework.beans.propertyeditors.ClassArrayEditor;
/*     */ import org.springframework.beans.propertyeditors.ClassEditor;
/*     */ import org.springframework.beans.propertyeditors.FileEditor;
/*     */ import org.springframework.beans.propertyeditors.InputSourceEditor;
/*     */ import org.springframework.beans.propertyeditors.InputStreamEditor;
/*     */ import org.springframework.beans.propertyeditors.PathEditor;
/*     */ import org.springframework.beans.propertyeditors.ReaderEditor;
/*     */ import org.springframework.beans.propertyeditors.URIEditor;
/*     */ import org.springframework.beans.propertyeditors.URLEditor;
/*     */ import org.springframework.core.env.PropertyResolver;
/*     */ import org.springframework.core.io.ContextResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceEditor;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.io.support.ResourceArrayPropertyEditor;
/*     */ import org.springframework.core.io.support.ResourcePatternResolver;
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
/*     */ public class ResourceEditorRegistrar
/*     */   implements PropertyEditorRegistrar
/*     */ {
/*     */   private final PropertyResolver propertyResolver;
/*     */   private final ResourceLoader resourceLoader;
/*     */   
/*     */   public ResourceEditorRegistrar(ResourceLoader resourceLoader, PropertyResolver propertyResolver) {
/*  79 */     this.resourceLoader = resourceLoader;
/*  80 */     this.propertyResolver = propertyResolver;
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
/*     */   
/*     */   public void registerCustomEditors(PropertyEditorRegistry registry) {
/* 102 */     ResourceEditor baseEditor = new ResourceEditor(this.resourceLoader, this.propertyResolver);
/* 103 */     doRegisterEditor(registry, Resource.class, (PropertyEditor)baseEditor);
/* 104 */     doRegisterEditor(registry, ContextResource.class, (PropertyEditor)baseEditor);
/* 105 */     doRegisterEditor(registry, InputStream.class, (PropertyEditor)new InputStreamEditor(baseEditor));
/* 106 */     doRegisterEditor(registry, InputSource.class, (PropertyEditor)new InputSourceEditor(baseEditor));
/* 107 */     doRegisterEditor(registry, File.class, (PropertyEditor)new FileEditor(baseEditor));
/* 108 */     doRegisterEditor(registry, Path.class, (PropertyEditor)new PathEditor(baseEditor));
/* 109 */     doRegisterEditor(registry, Reader.class, (PropertyEditor)new ReaderEditor(baseEditor));
/* 110 */     doRegisterEditor(registry, URL.class, (PropertyEditor)new URLEditor(baseEditor));
/*     */     
/* 112 */     ClassLoader classLoader = this.resourceLoader.getClassLoader();
/* 113 */     doRegisterEditor(registry, URI.class, (PropertyEditor)new URIEditor(classLoader));
/* 114 */     doRegisterEditor(registry, Class.class, (PropertyEditor)new ClassEditor(classLoader));
/* 115 */     doRegisterEditor(registry, Class[].class, (PropertyEditor)new ClassArrayEditor(classLoader));
/*     */     
/* 117 */     if (this.resourceLoader instanceof ResourcePatternResolver) {
/* 118 */       doRegisterEditor(registry, Resource[].class, (PropertyEditor)new ResourceArrayPropertyEditor((ResourcePatternResolver)this.resourceLoader, this.propertyResolver));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void doRegisterEditor(PropertyEditorRegistry registry, Class<?> requiredType, PropertyEditor editor) {
/* 128 */     if (registry instanceof PropertyEditorRegistrySupport) {
/* 129 */       ((PropertyEditorRegistrySupport)registry).overrideDefaultEditor(requiredType, editor);
/*     */     } else {
/*     */       
/* 132 */       registry.registerCustomEditor(requiredType, editor);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/support/ResourceEditorRegistrar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */