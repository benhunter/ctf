/*     */ package org.springframework.validation.beanvalidation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import javax.validation.Configuration;
/*     */ import javax.validation.ConstraintValidatorFactory;
/*     */ import javax.validation.MessageInterpolator;
/*     */ import javax.validation.ParameterNameProvider;
/*     */ import javax.validation.TraversableResolver;
/*     */ import javax.validation.Validation;
/*     */ import javax.validation.ValidationException;
/*     */ import javax.validation.ValidationProviderResolver;
/*     */ import javax.validation.Validator;
/*     */ import javax.validation.ValidatorContext;
/*     */ import javax.validation.ValidatorFactory;
/*     */ import javax.validation.bootstrap.GenericBootstrap;
/*     */ import javax.validation.bootstrap.ProviderSpecificBootstrap;
/*     */ import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.context.MessageSource;
/*     */ import org.springframework.core.DefaultParameterNameDiscoverer;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LocalValidatorFactoryBean
/*     */   extends SpringValidatorAdapter
/*     */   implements ValidatorFactory, ApplicationContextAware, InitializingBean, DisposableBean
/*     */ {
/*     */   @Nullable
/*     */   private Class providerClass;
/*     */   @Nullable
/*     */   private ValidationProviderResolver validationProviderResolver;
/*     */   @Nullable
/*     */   private MessageInterpolator messageInterpolator;
/*     */   @Nullable
/*     */   private TraversableResolver traversableResolver;
/*     */   @Nullable
/*     */   private ConstraintValidatorFactory constraintValidatorFactory;
/*     */   @Nullable
/* 105 */   private ParameterNameDiscoverer parameterNameDiscoverer = (ParameterNameDiscoverer)new DefaultParameterNameDiscoverer();
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Resource[] mappingLocations;
/*     */   
/* 111 */   private final Map<String, String> validationPropertyMap = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ApplicationContext applicationContext;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ValidatorFactory validatorFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProviderClass(Class providerClass) {
/* 128 */     this.providerClass = providerClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValidationProviderResolver(ValidationProviderResolver validationProviderResolver) {
/* 137 */     this.validationProviderResolver = validationProviderResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessageInterpolator(MessageInterpolator messageInterpolator) {
/* 145 */     this.messageInterpolator = messageInterpolator;
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
/*     */   public void setValidationMessageSource(MessageSource messageSource) {
/* 168 */     this.messageInterpolator = HibernateValidatorDelegate.buildMessageInterpolator(messageSource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTraversableResolver(TraversableResolver traversableResolver) {
/* 176 */     this.traversableResolver = traversableResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConstraintValidatorFactory(ConstraintValidatorFactory constraintValidatorFactory) {
/* 185 */     this.constraintValidatorFactory = constraintValidatorFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer) {
/* 194 */     this.parameterNameDiscoverer = parameterNameDiscoverer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMappingLocations(Resource... mappingLocations) {
/* 201 */     this.mappingLocations = mappingLocations;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValidationProperties(Properties jpaProperties) {
/* 211 */     CollectionUtils.mergePropertiesIntoMap(jpaProperties, this.validationPropertyMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValidationPropertyMap(@Nullable Map<String, String> validationProperties) {
/* 220 */     if (validationProperties != null) {
/* 221 */       this.validationPropertyMap.putAll(validationProperties);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> getValidationPropertyMap() {
/* 231 */     return this.validationPropertyMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setApplicationContext(ApplicationContext applicationContext) {
/* 236 */     this.applicationContext = applicationContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/*     */     Configuration<?> configuration;
/* 244 */     if (this.providerClass != null) {
/* 245 */       ProviderSpecificBootstrap bootstrap = Validation.byProvider(this.providerClass);
/* 246 */       if (this.validationProviderResolver != null) {
/* 247 */         bootstrap = bootstrap.providerResolver(this.validationProviderResolver);
/*     */       }
/* 249 */       configuration = bootstrap.configure();
/*     */     } else {
/*     */       
/* 252 */       GenericBootstrap bootstrap = Validation.byDefaultProvider();
/* 253 */       if (this.validationProviderResolver != null) {
/* 254 */         bootstrap = bootstrap.providerResolver(this.validationProviderResolver);
/*     */       }
/* 256 */       configuration = bootstrap.configure();
/*     */     } 
/*     */ 
/*     */     
/* 260 */     if (this.applicationContext != null) {
/*     */       try {
/* 262 */         Method eclMethod = configuration.getClass().getMethod("externalClassLoader", new Class[] { ClassLoader.class });
/* 263 */         ReflectionUtils.invokeMethod(eclMethod, configuration, new Object[] { this.applicationContext.getClassLoader() });
/*     */       }
/* 265 */       catch (NoSuchMethodException noSuchMethodException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 270 */     MessageInterpolator targetInterpolator = this.messageInterpolator;
/* 271 */     if (targetInterpolator == null) {
/* 272 */       targetInterpolator = configuration.getDefaultMessageInterpolator();
/*     */     }
/* 274 */     configuration.messageInterpolator(new LocaleContextMessageInterpolator(targetInterpolator));
/*     */     
/* 276 */     if (this.traversableResolver != null) {
/* 277 */       configuration.traversableResolver(this.traversableResolver);
/*     */     }
/*     */     
/* 280 */     ConstraintValidatorFactory targetConstraintValidatorFactory = this.constraintValidatorFactory;
/* 281 */     if (targetConstraintValidatorFactory == null && this.applicationContext != null)
/*     */     {
/* 283 */       targetConstraintValidatorFactory = new SpringConstraintValidatorFactory(this.applicationContext.getAutowireCapableBeanFactory());
/*     */     }
/* 285 */     if (targetConstraintValidatorFactory != null) {
/* 286 */       configuration.constraintValidatorFactory(targetConstraintValidatorFactory);
/*     */     }
/*     */     
/* 289 */     if (this.parameterNameDiscoverer != null) {
/* 290 */       configureParameterNameProvider(this.parameterNameDiscoverer, configuration);
/*     */     }
/*     */     
/* 293 */     if (this.mappingLocations != null) {
/* 294 */       for (Resource location : this.mappingLocations) {
/*     */         try {
/* 296 */           configuration.addMapping(location.getInputStream());
/*     */         }
/* 298 */         catch (IOException ex) {
/* 299 */           throw new IllegalStateException("Cannot read mapping resource: " + location);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 304 */     this.validationPropertyMap.forEach(configuration::addProperty);
/*     */ 
/*     */     
/* 307 */     postProcessConfiguration(configuration);
/*     */     
/* 309 */     this.validatorFactory = configuration.buildValidatorFactory();
/* 310 */     setTargetValidator(this.validatorFactory.getValidator());
/*     */   }
/*     */   
/*     */   private void configureParameterNameProvider(final ParameterNameDiscoverer discoverer, Configuration<?> configuration) {
/* 314 */     final ParameterNameProvider defaultProvider = configuration.getDefaultParameterNameProvider();
/* 315 */     configuration.parameterNameProvider(new ParameterNameProvider()
/*     */         {
/*     */           public List<String> getParameterNames(Constructor<?> constructor) {
/* 318 */             String[] paramNames = discoverer.getParameterNames(constructor);
/* 319 */             return (paramNames != null) ? Arrays.<String>asList(paramNames) : defaultProvider
/* 320 */               .getParameterNames(constructor);
/*     */           }
/*     */           
/*     */           public List<String> getParameterNames(Method method) {
/* 324 */             String[] paramNames = discoverer.getParameterNames(method);
/* 325 */             return (paramNames != null) ? Arrays.<String>asList(paramNames) : defaultProvider
/* 326 */               .getParameterNames(method);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void postProcessConfiguration(Configuration<?> configuration) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Validator getValidator() {
/* 344 */     Assert.notNull(this.validatorFactory, "No target ValidatorFactory set");
/* 345 */     return this.validatorFactory.getValidator();
/*     */   }
/*     */ 
/*     */   
/*     */   public ValidatorContext usingContext() {
/* 350 */     Assert.notNull(this.validatorFactory, "No target ValidatorFactory set");
/* 351 */     return this.validatorFactory.usingContext();
/*     */   }
/*     */ 
/*     */   
/*     */   public MessageInterpolator getMessageInterpolator() {
/* 356 */     Assert.notNull(this.validatorFactory, "No target ValidatorFactory set");
/* 357 */     return this.validatorFactory.getMessageInterpolator();
/*     */   }
/*     */ 
/*     */   
/*     */   public TraversableResolver getTraversableResolver() {
/* 362 */     Assert.notNull(this.validatorFactory, "No target ValidatorFactory set");
/* 363 */     return this.validatorFactory.getTraversableResolver();
/*     */   }
/*     */ 
/*     */   
/*     */   public ConstraintValidatorFactory getConstraintValidatorFactory() {
/* 368 */     Assert.notNull(this.validatorFactory, "No target ValidatorFactory set");
/* 369 */     return this.validatorFactory.getConstraintValidatorFactory();
/*     */   }
/*     */ 
/*     */   
/*     */   public ParameterNameProvider getParameterNameProvider() {
/* 374 */     Assert.notNull(this.validatorFactory, "No target ValidatorFactory set");
/* 375 */     return this.validatorFactory.getParameterNameProvider();
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
/*     */   public <T> T unwrap(@Nullable Class<T> type) {
/* 394 */     if (type == null || !ValidatorFactory.class.isAssignableFrom(type)) {
/*     */       try {
/* 396 */         return super.unwrap(type);
/*     */       }
/* 398 */       catch (ValidationException validationException) {}
/*     */     }
/*     */ 
/*     */     
/* 402 */     if (this.validatorFactory != null) {
/*     */       try {
/* 404 */         return (T)this.validatorFactory.unwrap(type);
/*     */       }
/* 406 */       catch (ValidationException ex) {
/*     */         
/* 408 */         if (ValidatorFactory.class == type) {
/* 409 */           return (T)this.validatorFactory;
/*     */         }
/* 411 */         throw ex;
/*     */       } 
/*     */     }
/* 414 */     throw new ValidationException("Cannot unwrap to " + type);
/*     */   }
/*     */   
/*     */   public void close() {
/* 418 */     if (this.validatorFactory != null) {
/* 419 */       this.validatorFactory.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 425 */     close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class HibernateValidatorDelegate
/*     */   {
/*     */     public static MessageInterpolator buildMessageInterpolator(MessageSource messageSource) {
/* 435 */       return (MessageInterpolator)new ResourceBundleMessageInterpolator(new MessageSourceResourceBundleLocator(messageSource));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/validation/beanvalidation/LocalValidatorFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */