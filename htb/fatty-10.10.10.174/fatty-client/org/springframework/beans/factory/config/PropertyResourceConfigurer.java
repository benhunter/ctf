/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Properties;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanInitializationException;
/*     */ import org.springframework.core.PriorityOrdered;
/*     */ import org.springframework.core.io.support.PropertiesLoaderSupport;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class PropertyResourceConfigurer
/*     */   extends PropertiesLoaderSupport
/*     */   implements BeanFactoryPostProcessor, PriorityOrdered
/*     */ {
/*  55 */   private int order = Integer.MAX_VALUE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOrder(int order) {
/*  63 */     this.order = order;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/*  68 */     return this.order;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
/*     */     try {
/*  80 */       Properties mergedProps = mergeProperties();
/*     */ 
/*     */       
/*  83 */       convertProperties(mergedProps);
/*     */ 
/*     */       
/*  86 */       processProperties(beanFactory, mergedProps);
/*     */     }
/*  88 */     catch (IOException ex) {
/*  89 */       throw new BeanInitializationException("Could not load properties", ex);
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
/*     */   protected void convertProperties(Properties props) {
/* 102 */     Enumeration<?> propertyNames = props.propertyNames();
/* 103 */     while (propertyNames.hasMoreElements()) {
/* 104 */       String propertyName = (String)propertyNames.nextElement();
/* 105 */       String propertyValue = props.getProperty(propertyName);
/* 106 */       String convertedValue = convertProperty(propertyName, propertyValue);
/* 107 */       if (!ObjectUtils.nullSafeEquals(propertyValue, convertedValue)) {
/* 108 */         props.setProperty(propertyName, convertedValue);
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected String convertProperty(String propertyName, String propertyValue) {
/* 123 */     return convertPropertyValue(propertyValue);
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
/*     */   protected String convertPropertyValue(String originalValue) {
/* 141 */     return originalValue;
/*     */   }
/*     */   
/*     */   protected abstract void processProperties(ConfigurableListableBeanFactory paramConfigurableListableBeanFactory, Properties paramProperties) throws BeansException;
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/PropertyResourceConfigurer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */