/*     */ package org.springframework.jmx.export.naming;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import javax.management.MalformedObjectNameException;
/*     */ import javax.management.ObjectName;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.jmx.export.metadata.JmxAttributeSource;
/*     */ import org.springframework.jmx.export.metadata.ManagedResource;
/*     */ import org.springframework.jmx.support.ObjectNameManager;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MetadataNamingStrategy
/*     */   implements ObjectNamingStrategy, InitializingBean
/*     */ {
/*     */   @Nullable
/*     */   private JmxAttributeSource attributeSource;
/*     */   @Nullable
/*     */   private String defaultDomain;
/*     */   
/*     */   public MetadataNamingStrategy() {}
/*     */   
/*     */   public MetadataNamingStrategy(JmxAttributeSource attributeSource) {
/*  75 */     Assert.notNull(attributeSource, "JmxAttributeSource must not be null");
/*  76 */     this.attributeSource = attributeSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttributeSource(JmxAttributeSource attributeSource) {
/*  85 */     Assert.notNull(attributeSource, "JmxAttributeSource must not be null");
/*  86 */     this.attributeSource = attributeSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultDomain(String defaultDomain) {
/*  97 */     this.defaultDomain = defaultDomain;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 102 */     if (this.attributeSource == null) {
/* 103 */       throw new IllegalArgumentException("Property 'attributeSource' is required");
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
/* 114 */     Assert.state((this.attributeSource != null), "No JmxAttributeSource set");
/* 115 */     Class<?> managedClass = AopUtils.getTargetClass(managedBean);
/* 116 */     ManagedResource mr = this.attributeSource.getManagedResource(managedClass);
/*     */ 
/*     */     
/* 119 */     if (mr != null && StringUtils.hasText(mr.getObjectName())) {
/* 120 */       return ObjectNameManager.getInstance(mr.getObjectName());
/*     */     }
/*     */     
/* 123 */     Assert.state((beanKey != null), "No ManagedResource attribute and no bean key specified");
/*     */     try {
/* 125 */       return ObjectNameManager.getInstance(beanKey);
/*     */     }
/* 127 */     catch (MalformedObjectNameException ex) {
/* 128 */       String domain = this.defaultDomain;
/* 129 */       if (domain == null) {
/* 130 */         domain = ClassUtils.getPackageName(managedClass);
/*     */       }
/* 132 */       Hashtable<String, String> properties = new Hashtable<>();
/* 133 */       properties.put("type", ClassUtils.getShortName(managedClass));
/* 134 */       properties.put("name", beanKey);
/* 135 */       return ObjectNameManager.getInstance(domain, properties);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/export/naming/MetadataNamingStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */