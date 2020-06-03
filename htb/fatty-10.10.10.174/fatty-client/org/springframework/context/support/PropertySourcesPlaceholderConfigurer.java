/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Properties;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanInitializationException;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.config.PlaceholderConfigurerSupport;
/*     */ import org.springframework.context.EnvironmentAware;
/*     */ import org.springframework.core.env.ConfigurablePropertyResolver;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.env.MutablePropertySources;
/*     */ import org.springframework.core.env.PropertiesPropertySource;
/*     */ import org.springframework.core.env.PropertySource;
/*     */ import org.springframework.core.env.PropertySources;
/*     */ import org.springframework.core.env.PropertySourcesPropertyResolver;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class PropertySourcesPlaceholderConfigurer
/*     */   extends PlaceholderConfigurerSupport
/*     */   implements EnvironmentAware
/*     */ {
/*     */   public static final String LOCAL_PROPERTIES_PROPERTY_SOURCE_NAME = "localProperties";
/*     */   public static final String ENVIRONMENT_PROPERTIES_PROPERTY_SOURCE_NAME = "environmentProperties";
/*     */   @Nullable
/*     */   private MutablePropertySources propertySources;
/*     */   @Nullable
/*     */   private PropertySources appliedPropertySources;
/*     */   @Nullable
/*     */   private Environment environment;
/*     */   
/*     */   public void setPropertySources(PropertySources propertySources) {
/*  97 */     this.propertySources = new MutablePropertySources(propertySources);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnvironment(Environment environment) {
/* 108 */     this.environment = environment;
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
/*     */   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
/* 129 */     if (this.propertySources == null) {
/* 130 */       this.propertySources = new MutablePropertySources();
/* 131 */       if (this.environment != null) {
/* 132 */         this.propertySources.addLast(new PropertySource<Environment>("environmentProperties", this.environment)
/*     */             {
/*     */               @Nullable
/*     */               public String getProperty(String key)
/*     */               {
/* 137 */                 return ((Environment)this.source).getProperty(key);
/*     */               }
/*     */             });
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/* 144 */         PropertiesPropertySource propertiesPropertySource = new PropertiesPropertySource("localProperties", mergeProperties());
/* 145 */         if (this.localOverride) {
/* 146 */           this.propertySources.addFirst((PropertySource)propertiesPropertySource);
/*     */         } else {
/*     */           
/* 149 */           this.propertySources.addLast((PropertySource)propertiesPropertySource);
/*     */         }
/*     */       
/* 152 */       } catch (IOException ex) {
/* 153 */         throw new BeanInitializationException("Could not load properties", ex);
/*     */       } 
/*     */     } 
/*     */     
/* 157 */     processProperties(beanFactory, (ConfigurablePropertyResolver)new PropertySourcesPropertyResolver((PropertySources)this.propertySources));
/* 158 */     this.appliedPropertySources = (PropertySources)this.propertySources;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, ConfigurablePropertyResolver propertyResolver) throws BeansException {
/* 168 */     propertyResolver.setPlaceholderPrefix(this.placeholderPrefix);
/* 169 */     propertyResolver.setPlaceholderSuffix(this.placeholderSuffix);
/* 170 */     propertyResolver.setValueSeparator(this.valueSeparator);
/*     */     
/* 172 */     StringValueResolver valueResolver = strVal -> {
/*     */         String resolved = this.ignoreUnresolvablePlaceholders ? propertyResolver.resolvePlaceholders(strVal) : propertyResolver.resolveRequiredPlaceholders(strVal);
/*     */         
/*     */         if (this.trimValues) {
/*     */           resolved = resolved.trim();
/*     */         }
/*     */         
/*     */         return resolved.equals(this.nullValue) ? null : resolved;
/*     */       };
/*     */     
/* 182 */     doProcessProperties(beanFactoryToProcess, valueResolver);
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
/*     */   @Deprecated
/*     */   protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) {
/* 195 */     throw new UnsupportedOperationException("Call processProperties(ConfigurableListableBeanFactory, ConfigurablePropertyResolver) instead");
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
/*     */   public PropertySources getAppliedPropertySources() throws IllegalStateException {
/* 207 */     Assert.state((this.appliedPropertySources != null), "PropertySources have not yet been applied");
/* 208 */     return this.appliedPropertySources;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/support/PropertySourcesPlaceholderConfigurer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */