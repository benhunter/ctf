/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.BeanNameAware;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.StringValueResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class PlaceholderConfigurerSupport
/*     */   extends PropertyResourceConfigurer
/*     */   implements BeanNameAware, BeanFactoryAware
/*     */ {
/*     */   public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";
/*     */   public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";
/*     */   public static final String DEFAULT_VALUE_SEPARATOR = ":";
/* 103 */   protected String placeholderPrefix = "${";
/*     */ 
/*     */   
/* 106 */   protected String placeholderSuffix = "}";
/*     */   
/*     */   @Nullable
/* 109 */   protected String valueSeparator = ":";
/*     */ 
/*     */   
/*     */   protected boolean trimValues = false;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String nullValue;
/*     */ 
/*     */   
/*     */   protected boolean ignoreUnresolvablePlaceholders = false;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String beanName;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private BeanFactory beanFactory;
/*     */ 
/*     */   
/*     */   public void setPlaceholderPrefix(String placeholderPrefix) {
/* 131 */     this.placeholderPrefix = placeholderPrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPlaceholderSuffix(String placeholderSuffix) {
/* 139 */     this.placeholderSuffix = placeholderSuffix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValueSeparator(@Nullable String valueSeparator) {
/* 149 */     this.valueSeparator = valueSeparator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTrimValues(boolean trimValues) {
/* 159 */     this.trimValues = trimValues;
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
/*     */   public void setNullValue(String nullValue) {
/* 172 */     this.nullValue = nullValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIgnoreUnresolvablePlaceholders(boolean ignoreUnresolvablePlaceholders) {
/* 183 */     this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
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
/*     */   public void setBeanName(String beanName) {
/* 196 */     this.beanName = beanName;
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
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/* 209 */     this.beanFactory = beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doProcessProperties(ConfigurableListableBeanFactory beanFactoryToProcess, StringValueResolver valueResolver) {
/* 216 */     BeanDefinitionVisitor visitor = new BeanDefinitionVisitor(valueResolver);
/*     */     
/* 218 */     String[] beanNames = beanFactoryToProcess.getBeanDefinitionNames();
/* 219 */     for (String curName : beanNames) {
/*     */ 
/*     */       
/* 222 */       if (!curName.equals(this.beanName) || !beanFactoryToProcess.equals(this.beanFactory)) {
/* 223 */         BeanDefinition bd = beanFactoryToProcess.getBeanDefinition(curName);
/*     */         try {
/* 225 */           visitor.visitBeanDefinition(bd);
/*     */         }
/* 227 */         catch (Exception ex) {
/* 228 */           throw new BeanDefinitionStoreException(bd.getResourceDescription(), curName, ex.getMessage(), ex);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 234 */     beanFactoryToProcess.resolveAliases(valueResolver);
/*     */ 
/*     */     
/* 237 */     beanFactoryToProcess.addEmbeddedValueResolver(valueResolver);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/PlaceholderConfigurerSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */