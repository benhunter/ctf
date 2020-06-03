/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.util.Properties;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.core.Constants;
/*     */ import org.springframework.core.SpringProperties;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.PropertyPlaceholderHelper;
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
/*     */ public class PropertyPlaceholderConfigurer
/*     */   extends PlaceholderConfigurerSupport
/*     */ {
/*     */   public static final int SYSTEM_PROPERTIES_MODE_NEVER = 0;
/*     */   public static final int SYSTEM_PROPERTIES_MODE_FALLBACK = 1;
/*     */   public static final int SYSTEM_PROPERTIES_MODE_OVERRIDE = 2;
/*  77 */   private static final Constants constants = new Constants(PropertyPlaceholderConfigurer.class);
/*     */   
/*  79 */   private int systemPropertiesMode = 1;
/*     */ 
/*     */   
/*  82 */   private boolean searchSystemEnvironment = !SpringProperties.getFlag("spring.getenv.ignore");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSystemPropertiesModeName(String constantName) throws IllegalArgumentException {
/*  92 */     this.systemPropertiesMode = constants.asNumber(constantName).intValue();
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
/*     */   public void setSystemPropertiesMode(int systemPropertiesMode) {
/* 108 */     this.systemPropertiesMode = systemPropertiesMode;
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
/*     */   public void setSearchSystemEnvironment(boolean searchSystemEnvironment) {
/* 125 */     this.searchSystemEnvironment = searchSystemEnvironment;
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
/*     */   @Nullable
/*     */   protected String resolvePlaceholder(String placeholder, Properties props, int systemPropertiesMode) {
/* 146 */     String propVal = null;
/* 147 */     if (systemPropertiesMode == 2) {
/* 148 */       propVal = resolveSystemProperty(placeholder);
/*     */     }
/* 150 */     if (propVal == null) {
/* 151 */       propVal = resolvePlaceholder(placeholder, props);
/*     */     }
/* 153 */     if (propVal == null && systemPropertiesMode == 1) {
/* 154 */       propVal = resolveSystemProperty(placeholder);
/*     */     }
/* 156 */     return propVal;
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
/*     */   @Nullable
/*     */   protected String resolvePlaceholder(String placeholder, Properties props) {
/* 174 */     return props.getProperty(placeholder);
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
/*     */   @Nullable
/*     */   protected String resolveSystemProperty(String key) {
/*     */     try {
/* 189 */       String value = System.getProperty(key);
/* 190 */       if (value == null && this.searchSystemEnvironment) {
/* 191 */         value = System.getenv(key);
/*     */       }
/* 193 */       return value;
/*     */     }
/* 195 */     catch (Throwable ex) {
/* 196 */       if (this.logger.isDebugEnabled()) {
/* 197 */         this.logger.debug("Could not access system property '" + key + "': " + ex);
/*     */       }
/* 199 */       return null;
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
/*     */   protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
/* 212 */     StringValueResolver valueResolver = new PlaceholderResolvingStringValueResolver(props);
/* 213 */     doProcessProperties(beanFactoryToProcess, valueResolver);
/*     */   }
/*     */ 
/*     */   
/*     */   private class PlaceholderResolvingStringValueResolver
/*     */     implements StringValueResolver
/*     */   {
/*     */     private final PropertyPlaceholderHelper helper;
/*     */     private final PropertyPlaceholderHelper.PlaceholderResolver resolver;
/*     */     
/*     */     public PlaceholderResolvingStringValueResolver(Properties props) {
/* 224 */       this.helper = new PropertyPlaceholderHelper(PropertyPlaceholderConfigurer.this.placeholderPrefix, PropertyPlaceholderConfigurer.this.placeholderSuffix, PropertyPlaceholderConfigurer.this.valueSeparator, PropertyPlaceholderConfigurer.this.ignoreUnresolvablePlaceholders);
/*     */       
/* 226 */       this.resolver = new PropertyPlaceholderConfigurer.PropertyPlaceholderConfigurerResolver(props);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String resolveStringValue(String strVal) throws BeansException {
/* 232 */       String resolved = this.helper.replacePlaceholders(strVal, this.resolver);
/* 233 */       if (PropertyPlaceholderConfigurer.this.trimValues) {
/* 234 */         resolved = resolved.trim();
/*     */       }
/* 236 */       return resolved.equals(PropertyPlaceholderConfigurer.this.nullValue) ? null : resolved;
/*     */     }
/*     */   }
/*     */   
/*     */   private final class PropertyPlaceholderConfigurerResolver
/*     */     implements PropertyPlaceholderHelper.PlaceholderResolver
/*     */   {
/*     */     private final Properties props;
/*     */     
/*     */     private PropertyPlaceholderConfigurerResolver(Properties props) {
/* 246 */       this.props = props;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String resolvePlaceholder(String placeholderName) {
/* 252 */       return PropertyPlaceholderConfigurer.this.resolvePlaceholder(placeholderName, this.props, PropertyPlaceholderConfigurer.this
/* 253 */           .systemPropertiesMode);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/PropertyPlaceholderConfigurer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */