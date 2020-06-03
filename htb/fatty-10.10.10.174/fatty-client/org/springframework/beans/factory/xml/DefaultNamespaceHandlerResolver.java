/*     */ package org.springframework.beans.factory.xml;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.FatalBeanException;
/*     */ import org.springframework.core.io.support.PropertiesLoaderUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultNamespaceHandlerResolver
/*     */   implements NamespaceHandlerResolver
/*     */ {
/*     */   public static final String DEFAULT_HANDLER_MAPPINGS_LOCATION = "META-INF/spring.handlers";
/*  59 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private final ClassLoader classLoader;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String handlerMappingsLocation;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private volatile Map<String, Object> handlerMappings;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultNamespaceHandlerResolver() {
/*  81 */     this(null, "META-INF/spring.handlers");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultNamespaceHandlerResolver(@Nullable ClassLoader classLoader) {
/*  92 */     this(classLoader, "META-INF/spring.handlers");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultNamespaceHandlerResolver(@Nullable ClassLoader classLoader, String handlerMappingsLocation) {
/* 103 */     Assert.notNull(handlerMappingsLocation, "Handler mappings location must not be null");
/* 104 */     this.classLoader = (classLoader != null) ? classLoader : ClassUtils.getDefaultClassLoader();
/* 105 */     this.handlerMappingsLocation = handlerMappingsLocation;
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
/*     */   @Nullable
/*     */   public NamespaceHandler resolve(String namespaceUri) {
/* 118 */     Map<String, Object> handlerMappings = getHandlerMappings();
/* 119 */     Object handlerOrClassName = handlerMappings.get(namespaceUri);
/* 120 */     if (handlerOrClassName == null) {
/* 121 */       return null;
/*     */     }
/* 123 */     if (handlerOrClassName instanceof NamespaceHandler) {
/* 124 */       return (NamespaceHandler)handlerOrClassName;
/*     */     }
/*     */     
/* 127 */     String className = (String)handlerOrClassName;
/*     */     try {
/* 129 */       Class<?> handlerClass = ClassUtils.forName(className, this.classLoader);
/* 130 */       if (!NamespaceHandler.class.isAssignableFrom(handlerClass)) {
/* 131 */         throw new FatalBeanException("Class [" + className + "] for namespace [" + namespaceUri + "] does not implement the [" + NamespaceHandler.class
/* 132 */             .getName() + "] interface");
/*     */       }
/* 134 */       NamespaceHandler namespaceHandler = (NamespaceHandler)BeanUtils.instantiateClass(handlerClass);
/* 135 */       namespaceHandler.init();
/* 136 */       handlerMappings.put(namespaceUri, namespaceHandler);
/* 137 */       return namespaceHandler;
/*     */     }
/* 139 */     catch (ClassNotFoundException ex) {
/* 140 */       throw new FatalBeanException("Could not find NamespaceHandler class [" + className + "] for namespace [" + namespaceUri + "]", ex);
/*     */     
/*     */     }
/* 143 */     catch (LinkageError err) {
/* 144 */       throw new FatalBeanException("Unresolvable class definition for NamespaceHandler class [" + className + "] for namespace [" + namespaceUri + "]", err);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, Object> getHandlerMappings() {
/* 154 */     Map<String, Object> handlerMappings = this.handlerMappings;
/* 155 */     if (handlerMappings == null) {
/* 156 */       synchronized (this) {
/* 157 */         handlerMappings = this.handlerMappings;
/* 158 */         if (handlerMappings == null) {
/* 159 */           if (this.logger.isTraceEnabled()) {
/* 160 */             this.logger.trace("Loading NamespaceHandler mappings from [" + this.handlerMappingsLocation + "]");
/*     */           }
/*     */           
/*     */           try {
/* 164 */             Properties mappings = PropertiesLoaderUtils.loadAllProperties(this.handlerMappingsLocation, this.classLoader);
/* 165 */             if (this.logger.isTraceEnabled()) {
/* 166 */               this.logger.trace("Loaded NamespaceHandler mappings: " + mappings);
/*     */             }
/* 168 */             handlerMappings = new ConcurrentHashMap<>(mappings.size());
/* 169 */             CollectionUtils.mergePropertiesIntoMap(mappings, handlerMappings);
/* 170 */             this.handlerMappings = handlerMappings;
/*     */           }
/* 172 */           catch (IOException ex) {
/* 173 */             throw new IllegalStateException("Unable to load NamespaceHandler mappings from location [" + this.handlerMappingsLocation + "]", ex);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 179 */     return handlerMappings;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 185 */     return "NamespaceHandlerResolver using mappings " + getHandlerMappings();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/xml/DefaultNamespaceHandlerResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */