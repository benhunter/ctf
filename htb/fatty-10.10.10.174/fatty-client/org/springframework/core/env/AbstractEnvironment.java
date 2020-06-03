/*     */ package org.springframework.core.env;
/*     */ 
/*     */ import java.security.AccessControlException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.SpringProperties;
/*     */ import org.springframework.core.convert.support.ConfigurableConversionService;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractEnvironment
/*     */   implements ConfigurableEnvironment
/*     */ {
/*     */   public static final String IGNORE_GETENV_PROPERTY_NAME = "spring.getenv.ignore";
/*     */   public static final String ACTIVE_PROFILES_PROPERTY_NAME = "spring.profiles.active";
/*     */   public static final String DEFAULT_PROFILES_PROPERTY_NAME = "spring.profiles.default";
/*     */   protected static final String RESERVED_DEFAULT_PROFILE_NAME = "default";
/* 104 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/* 106 */   private final Set<String> activeProfiles = new LinkedHashSet<>();
/*     */   
/* 108 */   private final Set<String> defaultProfiles = new LinkedHashSet<>(getReservedDefaultProfiles());
/*     */   
/* 110 */   private final MutablePropertySources propertySources = new MutablePropertySources();
/*     */   
/* 112 */   private final ConfigurablePropertyResolver propertyResolver = new PropertySourcesPropertyResolver(this.propertySources);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractEnvironment() {
/* 124 */     customizePropertySources(this.propertySources);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void customizePropertySources(MutablePropertySources propertySources) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Set<String> getReservedDefaultProfiles() {
/* 214 */     return Collections.singleton("default");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getActiveProfiles() {
/* 224 */     return StringUtils.toStringArray(doGetActiveProfiles());
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
/*     */   protected Set<String> doGetActiveProfiles() {
/* 236 */     synchronized (this.activeProfiles) {
/* 237 */       if (this.activeProfiles.isEmpty()) {
/* 238 */         String profiles = getProperty("spring.profiles.active");
/* 239 */         if (StringUtils.hasText(profiles)) {
/* 240 */           setActiveProfiles(StringUtils.commaDelimitedListToStringArray(
/* 241 */                 StringUtils.trimAllWhitespace(profiles)));
/*     */         }
/*     */       } 
/* 244 */       return this.activeProfiles;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setActiveProfiles(String... profiles) {
/* 250 */     Assert.notNull(profiles, "Profile array must not be null");
/* 251 */     if (this.logger.isDebugEnabled()) {
/* 252 */       this.logger.debug("Activating profiles " + Arrays.<String>asList(profiles));
/*     */     }
/* 254 */     synchronized (this.activeProfiles) {
/* 255 */       this.activeProfiles.clear();
/* 256 */       for (String profile : profiles) {
/* 257 */         validateProfile(profile);
/* 258 */         this.activeProfiles.add(profile);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addActiveProfile(String profile) {
/* 265 */     if (this.logger.isDebugEnabled()) {
/* 266 */       this.logger.debug("Activating profile '" + profile + "'");
/*     */     }
/* 268 */     validateProfile(profile);
/* 269 */     doGetActiveProfiles();
/* 270 */     synchronized (this.activeProfiles) {
/* 271 */       this.activeProfiles.add(profile);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getDefaultProfiles() {
/* 278 */     return StringUtils.toStringArray(doGetDefaultProfiles());
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
/*     */   protected Set<String> doGetDefaultProfiles() {
/* 294 */     synchronized (this.defaultProfiles) {
/* 295 */       if (this.defaultProfiles.equals(getReservedDefaultProfiles())) {
/* 296 */         String profiles = getProperty("spring.profiles.default");
/* 297 */         if (StringUtils.hasText(profiles)) {
/* 298 */           setDefaultProfiles(StringUtils.commaDelimitedListToStringArray(
/* 299 */                 StringUtils.trimAllWhitespace(profiles)));
/*     */         }
/*     */       } 
/* 302 */       return this.defaultProfiles;
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
/*     */   public void setDefaultProfiles(String... profiles) {
/* 316 */     Assert.notNull(profiles, "Profile array must not be null");
/* 317 */     synchronized (this.defaultProfiles) {
/* 318 */       this.defaultProfiles.clear();
/* 319 */       for (String profile : profiles) {
/* 320 */         validateProfile(profile);
/* 321 */         this.defaultProfiles.add(profile);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public boolean acceptsProfiles(String... profiles) {
/* 329 */     Assert.notEmpty((Object[])profiles, "Must specify at least one profile");
/* 330 */     for (String profile : profiles) {
/* 331 */       if (StringUtils.hasLength(profile) && profile.charAt(0) == '!') {
/* 332 */         if (!isProfileActive(profile.substring(1))) {
/* 333 */           return true;
/*     */         }
/*     */       }
/* 336 */       else if (isProfileActive(profile)) {
/* 337 */         return true;
/*     */       } 
/*     */     } 
/* 340 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean acceptsProfiles(Profiles profiles) {
/* 345 */     Assert.notNull(profiles, "Profiles must not be null");
/* 346 */     return profiles.matches(this::isProfileActive);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isProfileActive(String profile) {
/* 355 */     validateProfile(profile);
/* 356 */     Set<String> currentActiveProfiles = doGetActiveProfiles();
/* 357 */     return (currentActiveProfiles.contains(profile) || (currentActiveProfiles
/* 358 */       .isEmpty() && doGetDefaultProfiles().contains(profile)));
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
/*     */   protected void validateProfile(String profile) {
/* 372 */     if (!StringUtils.hasText(profile)) {
/* 373 */       throw new IllegalArgumentException("Invalid profile [" + profile + "]: must contain text");
/*     */     }
/* 375 */     if (profile.charAt(0) == '!') {
/* 376 */       throw new IllegalArgumentException("Invalid profile [" + profile + "]: must not begin with ! operator");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public MutablePropertySources getPropertySources() {
/* 382 */     return this.propertySources;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Object> getSystemProperties() {
/*     */     try {
/* 389 */       return System.getProperties();
/*     */     }
/* 391 */     catch (AccessControlException ex) {
/* 392 */       return new ReadOnlySystemAttributesMap()
/*     */         {
/*     */           @Nullable
/*     */           protected String getSystemAttribute(String attributeName) {
/*     */             try {
/* 397 */               return System.getProperty(attributeName);
/*     */             }
/* 399 */             catch (AccessControlException ex) {
/* 400 */               if (AbstractEnvironment.this.logger.isInfoEnabled()) {
/* 401 */                 AbstractEnvironment.this.logger.info("Caught AccessControlException when accessing system property '" + attributeName + "'; its value will be returned [null]. Reason: " + ex
/* 402 */                     .getMessage());
/*     */               }
/* 404 */               return null;
/*     */             } 
/*     */           }
/*     */         };
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Object> getSystemEnvironment() {
/* 414 */     if (suppressGetenvAccess()) {
/* 415 */       return Collections.emptyMap();
/*     */     }
/*     */     try {
/* 418 */       return (Map)System.getenv();
/*     */     }
/* 420 */     catch (AccessControlException ex) {
/* 421 */       return new ReadOnlySystemAttributesMap()
/*     */         {
/*     */           @Nullable
/*     */           protected String getSystemAttribute(String attributeName) {
/*     */             try {
/* 426 */               return System.getenv(attributeName);
/*     */             }
/* 428 */             catch (AccessControlException ex) {
/* 429 */               if (AbstractEnvironment.this.logger.isInfoEnabled()) {
/* 430 */                 AbstractEnvironment.this.logger.info("Caught AccessControlException when accessing system environment variable '" + attributeName + "'; its value will be returned [null]. Reason: " + ex
/* 431 */                     .getMessage());
/*     */               }
/* 433 */               return null;
/*     */             } 
/*     */           }
/*     */         };
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
/*     */ 
/*     */   
/*     */   protected boolean suppressGetenvAccess() {
/* 452 */     return SpringProperties.getFlag("spring.getenv.ignore");
/*     */   }
/*     */ 
/*     */   
/*     */   public void merge(ConfigurableEnvironment parent) {
/* 457 */     for (PropertySource<?> ps : (Iterable<PropertySource<?>>)parent.getPropertySources()) {
/* 458 */       if (!this.propertySources.contains(ps.getName())) {
/* 459 */         this.propertySources.addLast(ps);
/*     */       }
/*     */     } 
/* 462 */     String[] parentActiveProfiles = parent.getActiveProfiles();
/* 463 */     if (!ObjectUtils.isEmpty((Object[])parentActiveProfiles)) {
/* 464 */       synchronized (this.activeProfiles) {
/* 465 */         for (String profile : parentActiveProfiles) {
/* 466 */           this.activeProfiles.add(profile);
/*     */         }
/*     */       } 
/*     */     }
/* 470 */     String[] parentDefaultProfiles = parent.getDefaultProfiles();
/* 471 */     if (!ObjectUtils.isEmpty((Object[])parentDefaultProfiles)) {
/* 472 */       synchronized (this.defaultProfiles) {
/* 473 */         this.defaultProfiles.remove("default");
/* 474 */         for (String profile : parentDefaultProfiles) {
/* 475 */           this.defaultProfiles.add(profile);
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
/*     */   
/*     */   public ConfigurableConversionService getConversionService() {
/* 488 */     return this.propertyResolver.getConversionService();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setConversionService(ConfigurableConversionService conversionService) {
/* 493 */     this.propertyResolver.setConversionService(conversionService);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlaceholderPrefix(String placeholderPrefix) {
/* 498 */     this.propertyResolver.setPlaceholderPrefix(placeholderPrefix);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlaceholderSuffix(String placeholderSuffix) {
/* 503 */     this.propertyResolver.setPlaceholderSuffix(placeholderSuffix);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValueSeparator(@Nullable String valueSeparator) {
/* 508 */     this.propertyResolver.setValueSeparator(valueSeparator);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIgnoreUnresolvableNestedPlaceholders(boolean ignoreUnresolvableNestedPlaceholders) {
/* 513 */     this.propertyResolver.setIgnoreUnresolvableNestedPlaceholders(ignoreUnresolvableNestedPlaceholders);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRequiredProperties(String... requiredProperties) {
/* 518 */     this.propertyResolver.setRequiredProperties(requiredProperties);
/*     */   }
/*     */ 
/*     */   
/*     */   public void validateRequiredProperties() throws MissingRequiredPropertiesException {
/* 523 */     this.propertyResolver.validateRequiredProperties();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsProperty(String key) {
/* 533 */     return this.propertyResolver.containsProperty(key);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getProperty(String key) {
/* 539 */     return this.propertyResolver.getProperty(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getProperty(String key, String defaultValue) {
/* 544 */     return this.propertyResolver.getProperty(key, defaultValue);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T> T getProperty(String key, Class<T> targetType) {
/* 550 */     return this.propertyResolver.getProperty(key, targetType);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
/* 555 */     return this.propertyResolver.getProperty(key, targetType, defaultValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRequiredProperty(String key) throws IllegalStateException {
/* 560 */     return this.propertyResolver.getRequiredProperty(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException {
/* 565 */     return this.propertyResolver.getRequiredProperty(key, targetType);
/*     */   }
/*     */ 
/*     */   
/*     */   public String resolvePlaceholders(String text) {
/* 570 */     return this.propertyResolver.resolvePlaceholders(text);
/*     */   }
/*     */ 
/*     */   
/*     */   public String resolveRequiredPlaceholders(String text) throws IllegalArgumentException {
/* 575 */     return this.propertyResolver.resolveRequiredPlaceholders(text);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 581 */     return getClass().getSimpleName() + " {activeProfiles=" + this.activeProfiles + ", defaultProfiles=" + this.defaultProfiles + ", propertySources=" + this.propertySources + "}";
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/env/AbstractEnvironment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */