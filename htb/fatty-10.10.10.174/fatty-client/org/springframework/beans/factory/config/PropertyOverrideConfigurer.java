/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.PropertyValue;
/*     */ import org.springframework.beans.factory.BeanInitializationException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PropertyOverrideConfigurer
/*     */   extends PropertyResourceConfigurer
/*     */ {
/*     */   public static final String DEFAULT_BEAN_NAME_SEPARATOR = ".";
/*  73 */   private String beanNameSeparator = ".";
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean ignoreInvalidKeys = false;
/*     */ 
/*     */   
/*  80 */   private final Set<String> beanNames = Collections.newSetFromMap(new ConcurrentHashMap<>(16));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanNameSeparator(String beanNameSeparator) {
/*  88 */     this.beanNameSeparator = beanNameSeparator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIgnoreInvalidKeys(boolean ignoreInvalidKeys) {
/*  98 */     this.ignoreInvalidKeys = ignoreInvalidKeys;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) throws BeansException {
/* 106 */     for (Enumeration<?> names = props.propertyNames(); names.hasMoreElements(); ) {
/* 107 */       String key = (String)names.nextElement();
/*     */       try {
/* 109 */         processKey(beanFactory, key, props.getProperty(key));
/*     */       }
/* 111 */       catch (BeansException ex) {
/* 112 */         String msg = "Could not process key '" + key + "' in PropertyOverrideConfigurer";
/* 113 */         if (!this.ignoreInvalidKeys) {
/* 114 */           throw new BeanInitializationException(msg, ex);
/*     */         }
/* 116 */         if (this.logger.isDebugEnabled()) {
/* 117 */           this.logger.debug(msg, (Throwable)ex);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processKey(ConfigurableListableBeanFactory factory, String key, String value) throws BeansException {
/* 129 */     int separatorIndex = key.indexOf(this.beanNameSeparator);
/* 130 */     if (separatorIndex == -1) {
/* 131 */       throw new BeanInitializationException("Invalid key '" + key + "': expected 'beanName" + this.beanNameSeparator + "property'");
/*     */     }
/*     */     
/* 134 */     String beanName = key.substring(0, separatorIndex);
/* 135 */     String beanProperty = key.substring(separatorIndex + 1);
/* 136 */     this.beanNames.add(beanName);
/* 137 */     applyPropertyValue(factory, beanName, beanProperty, value);
/* 138 */     if (this.logger.isDebugEnabled()) {
/* 139 */       this.logger.debug("Property '" + key + "' set to value [" + value + "]");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void applyPropertyValue(ConfigurableListableBeanFactory factory, String beanName, String property, String value) {
/* 149 */     BeanDefinition bd = factory.getBeanDefinition(beanName);
/* 150 */     BeanDefinition bdToUse = bd;
/* 151 */     while (bd != null) {
/* 152 */       bdToUse = bd;
/* 153 */       bd = bd.getOriginatingBeanDefinition();
/*     */     } 
/* 155 */     PropertyValue pv = new PropertyValue(property, value);
/* 156 */     pv.setOptional(this.ignoreInvalidKeys);
/* 157 */     bdToUse.getPropertyValues().addPropertyValue(pv);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasPropertyOverridesFor(String beanName) {
/* 168 */     return this.beanNames.contains(beanName);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/PropertyOverrideConfigurer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */