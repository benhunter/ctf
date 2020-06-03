/*     */ package org.springframework.core.io.support;
/*     */ 
/*     */ import java.beans.PropertyEditorSupport;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.env.PropertyResolver;
/*     */ import org.springframework.core.env.StandardEnvironment;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourceArrayPropertyEditor
/*     */   extends PropertyEditorSupport
/*     */ {
/*  59 */   private static final Log logger = LogFactory.getLog(ResourceArrayPropertyEditor.class);
/*     */ 
/*     */ 
/*     */   
/*     */   private final ResourcePatternResolver resourcePatternResolver;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private PropertyResolver propertyResolver;
/*     */ 
/*     */   
/*     */   private final boolean ignoreUnresolvablePlaceholders;
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceArrayPropertyEditor() {
/*  76 */     this(new PathMatchingResourcePatternResolver(), null, true);
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
/*     */   public ResourceArrayPropertyEditor(ResourcePatternResolver resourcePatternResolver, @Nullable PropertyResolver propertyResolver) {
/*  88 */     this(resourcePatternResolver, propertyResolver, true);
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
/*     */   public ResourceArrayPropertyEditor(ResourcePatternResolver resourcePatternResolver, @Nullable PropertyResolver propertyResolver, boolean ignoreUnresolvablePlaceholders) {
/* 102 */     Assert.notNull(resourcePatternResolver, "ResourcePatternResolver must not be null");
/* 103 */     this.resourcePatternResolver = resourcePatternResolver;
/* 104 */     this.propertyResolver = propertyResolver;
/* 105 */     this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAsText(String text) {
/* 114 */     String pattern = resolvePath(text).trim();
/*     */     try {
/* 116 */       setValue(this.resourcePatternResolver.getResources(pattern));
/*     */     }
/* 118 */     catch (IOException ex) {
/* 119 */       throw new IllegalArgumentException("Could not resolve resource location pattern [" + pattern + "]: " + ex
/* 120 */           .getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(Object value) throws IllegalArgumentException {
/* 130 */     if (value instanceof Collection || (value instanceof Object[] && !(value instanceof Resource[]))) {
/* 131 */       Collection<?> input = (value instanceof Collection) ? (Collection)value : Arrays.asList((Object[])value);
/* 132 */       List<Resource> merged = new ArrayList<>();
/* 133 */       for (Object element : input) {
/* 134 */         if (element instanceof String) {
/*     */ 
/*     */           
/* 137 */           String pattern = resolvePath((String)element).trim();
/*     */           try {
/* 139 */             Resource[] resources = this.resourcePatternResolver.getResources(pattern);
/* 140 */             for (Resource resource : resources) {
/* 141 */               if (!merged.contains(resource)) {
/* 142 */                 merged.add(resource);
/*     */               }
/*     */             }
/*     */           
/* 146 */           } catch (IOException ex) {
/*     */             
/* 148 */             if (logger.isDebugEnabled())
/* 149 */               logger.debug("Could not retrieve resources for pattern '" + pattern + "'", ex); 
/*     */           } 
/*     */           continue;
/*     */         } 
/* 153 */         if (element instanceof Resource) {
/*     */           
/* 155 */           Resource resource = (Resource)element;
/* 156 */           if (!merged.contains(resource)) {
/* 157 */             merged.add(resource);
/*     */           }
/*     */           continue;
/*     */         } 
/* 161 */         throw new IllegalArgumentException("Cannot convert element [" + element + "] to [" + Resource.class
/* 162 */             .getName() + "]: only location String and Resource object supported");
/*     */       } 
/*     */       
/* 165 */       super.setValue(merged.toArray(new Resource[0]));
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 171 */       super.setValue(value);
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
/*     */   protected String resolvePath(String path) {
/* 184 */     if (this.propertyResolver == null) {
/* 185 */       this.propertyResolver = (PropertyResolver)new StandardEnvironment();
/*     */     }
/* 187 */     return this.ignoreUnresolvablePlaceholders ? this.propertyResolver.resolvePlaceholders(path) : this.propertyResolver
/* 188 */       .resolveRequiredPlaceholders(path);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/io/support/ResourceArrayPropertyEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */