/*     */ package org.springframework.core.env;
/*     */ 
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.convert.support.ConfigurableConversionService;
/*     */ import org.springframework.core.convert.support.DefaultConversionService;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.PropertyPlaceholderHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractPropertyResolver
/*     */   implements ConfigurablePropertyResolver
/*     */ {
/*  43 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   @Nullable
/*     */   private volatile ConfigurableConversionService conversionService;
/*     */   
/*     */   @Nullable
/*     */   private PropertyPlaceholderHelper nonStrictHelper;
/*     */   
/*     */   @Nullable
/*     */   private PropertyPlaceholderHelper strictHelper;
/*     */   
/*     */   private boolean ignoreUnresolvableNestedPlaceholders = false;
/*     */   
/*  56 */   private String placeholderPrefix = "${";
/*     */   
/*  58 */   private String placeholderSuffix = "}";
/*     */   @Nullable
/*  60 */   private String valueSeparator = ":";
/*     */ 
/*     */   
/*  63 */   private final Set<String> requiredProperties = new LinkedHashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurableConversionService getConversionService() {
/*     */     DefaultConversionService defaultConversionService;
/*  70 */     ConfigurableConversionService cs = this.conversionService;
/*  71 */     if (cs == null) {
/*  72 */       synchronized (this) {
/*  73 */         cs = this.conversionService;
/*  74 */         if (cs == null) {
/*  75 */           defaultConversionService = new DefaultConversionService();
/*  76 */           this.conversionService = (ConfigurableConversionService)defaultConversionService;
/*     */         } 
/*     */       } 
/*     */     }
/*  80 */     return (ConfigurableConversionService)defaultConversionService;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setConversionService(ConfigurableConversionService conversionService) {
/*  85 */     Assert.notNull(conversionService, "ConversionService must not be null");
/*  86 */     this.conversionService = conversionService;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPlaceholderPrefix(String placeholderPrefix) {
/*  96 */     Assert.notNull(placeholderPrefix, "'placeholderPrefix' must not be null");
/*  97 */     this.placeholderPrefix = placeholderPrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPlaceholderSuffix(String placeholderSuffix) {
/* 107 */     Assert.notNull(placeholderSuffix, "'placeholderSuffix' must not be null");
/* 108 */     this.placeholderSuffix = placeholderSuffix;
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
/*     */   public void setValueSeparator(@Nullable String valueSeparator) {
/* 120 */     this.valueSeparator = valueSeparator;
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
/*     */   public void setIgnoreUnresolvableNestedPlaceholders(boolean ignoreUnresolvableNestedPlaceholders) {
/* 134 */     this.ignoreUnresolvableNestedPlaceholders = ignoreUnresolvableNestedPlaceholders;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRequiredProperties(String... requiredProperties) {
/* 139 */     for (String key : requiredProperties) {
/* 140 */       this.requiredProperties.add(key);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void validateRequiredProperties() {
/* 146 */     MissingRequiredPropertiesException ex = new MissingRequiredPropertiesException();
/* 147 */     for (String key : this.requiredProperties) {
/* 148 */       if (getProperty(key) == null) {
/* 149 */         ex.addMissingRequiredProperty(key);
/*     */       }
/*     */     } 
/* 152 */     if (!ex.getMissingRequiredProperties().isEmpty()) {
/* 153 */       throw ex;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsProperty(String key) {
/* 159 */     return (getProperty(key) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getProperty(String key) {
/* 165 */     return getProperty(key, String.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getProperty(String key, String defaultValue) {
/* 170 */     String value = getProperty(key);
/* 171 */     return (value != null) ? value : defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
/* 176 */     T value = getProperty(key, targetType);
/* 177 */     return (value != null) ? value : defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRequiredProperty(String key) throws IllegalStateException {
/* 182 */     String value = getProperty(key);
/* 183 */     if (value == null) {
/* 184 */       throw new IllegalStateException("Required key '" + key + "' not found");
/*     */     }
/* 186 */     return value;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getRequiredProperty(String key, Class<T> valueType) throws IllegalStateException {
/* 191 */     T value = getProperty(key, valueType);
/* 192 */     if (value == null) {
/* 193 */       throw new IllegalStateException("Required key '" + key + "' not found");
/*     */     }
/* 195 */     return value;
/*     */   }
/*     */ 
/*     */   
/*     */   public String resolvePlaceholders(String text) {
/* 200 */     if (this.nonStrictHelper == null) {
/* 201 */       this.nonStrictHelper = createPlaceholderHelper(true);
/*     */     }
/* 203 */     return doResolvePlaceholders(text, this.nonStrictHelper);
/*     */   }
/*     */ 
/*     */   
/*     */   public String resolveRequiredPlaceholders(String text) throws IllegalArgumentException {
/* 208 */     if (this.strictHelper == null) {
/* 209 */       this.strictHelper = createPlaceholderHelper(false);
/*     */     }
/* 211 */     return doResolvePlaceholders(text, this.strictHelper);
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
/*     */   protected String resolveNestedPlaceholders(String value) {
/* 227 */     return this.ignoreUnresolvableNestedPlaceholders ? 
/* 228 */       resolvePlaceholders(value) : resolveRequiredPlaceholders(value);
/*     */   }
/*     */   
/*     */   private PropertyPlaceholderHelper createPlaceholderHelper(boolean ignoreUnresolvablePlaceholders) {
/* 232 */     return new PropertyPlaceholderHelper(this.placeholderPrefix, this.placeholderSuffix, this.valueSeparator, ignoreUnresolvablePlaceholders);
/*     */   }
/*     */ 
/*     */   
/*     */   private String doResolvePlaceholders(String text, PropertyPlaceholderHelper helper) {
/* 237 */     return helper.replacePlaceholders(text, this::getPropertyAsRawString);
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
/*     */   protected <T> T convertValueIfNecessary(Object value, @Nullable Class<T> targetType) {
/*     */     ConversionService conversionService;
/* 251 */     if (targetType == null) {
/* 252 */       return (T)value;
/*     */     }
/* 254 */     ConfigurableConversionService configurableConversionService = this.conversionService;
/* 255 */     if (configurableConversionService == null) {
/*     */ 
/*     */       
/* 258 */       if (ClassUtils.isAssignableValue(targetType, value)) {
/* 259 */         return (T)value;
/*     */       }
/* 261 */       conversionService = DefaultConversionService.getSharedInstance();
/*     */     } 
/* 263 */     return (T)conversionService.convert(value, targetType);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected abstract String getPropertyAsRawString(String paramString);
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/env/AbstractPropertyResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */