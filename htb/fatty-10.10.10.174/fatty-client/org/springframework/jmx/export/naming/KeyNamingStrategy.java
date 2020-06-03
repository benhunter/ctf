/*     */ package org.springframework.jmx.export.naming;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Properties;
/*     */ import javax.management.MalformedObjectNameException;
/*     */ import javax.management.ObjectName;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.support.PropertiesLoaderUtils;
/*     */ import org.springframework.jmx.support.ObjectNameManager;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ public class KeyNamingStrategy
/*     */   implements ObjectNamingStrategy, InitializingBean
/*     */ {
/*  59 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Properties mappings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Resource[] mappingLocations;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Properties mergedMappings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMappings(Properties mappings) {
/*  89 */     this.mappings = mappings;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMappingLocation(Resource location) {
/*  97 */     this.mappingLocations = new Resource[] { location };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMappingLocations(Resource... mappingLocations) {
/* 105 */     this.mappingLocations = mappingLocations;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws IOException {
/* 116 */     this.mergedMappings = new Properties();
/* 117 */     CollectionUtils.mergePropertiesIntoMap(this.mappings, this.mergedMappings);
/*     */     
/* 119 */     if (this.mappingLocations != null) {
/* 120 */       for (Resource location : this.mappingLocations) {
/* 121 */         if (this.logger.isDebugEnabled()) {
/* 122 */           this.logger.debug("Loading JMX object name mappings file from " + location);
/*     */         }
/* 124 */         PropertiesLoaderUtils.fillProperties(this.mergedMappings, location);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectName getObjectName(Object managedBean, @Nullable String beanKey) throws MalformedObjectNameException {
/* 136 */     Assert.notNull(beanKey, "KeyNamingStrategy requires bean key");
/* 137 */     String objectName = null;
/* 138 */     if (this.mergedMappings != null) {
/* 139 */       objectName = this.mergedMappings.getProperty(beanKey);
/*     */     }
/* 141 */     if (objectName == null) {
/* 142 */       objectName = beanKey;
/*     */     }
/* 144 */     return ObjectNameManager.getInstance(objectName);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/export/naming/KeyNamingStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */