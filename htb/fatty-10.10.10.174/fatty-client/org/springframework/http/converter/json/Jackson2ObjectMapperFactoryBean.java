/*     */ package org.springframework.http.converter.json;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonInclude;
/*     */ import com.fasterxml.jackson.core.JsonFactory;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.Module;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.PropertyNamingStrategy;
/*     */ import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*     */ import com.fasterxml.jackson.databind.ser.FilterProvider;
/*     */ import java.text.DateFormat;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Jackson2ObjectMapperFactoryBean
/*     */   implements FactoryBean<ObjectMapper>, BeanClassLoaderAware, ApplicationContextAware, InitializingBean
/*     */ {
/* 151 */   private final Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ObjectMapper objectMapper;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setObjectMapper(ObjectMapper objectMapper) {
/* 162 */     this.objectMapper = objectMapper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCreateXmlMapper(boolean createXmlMapper) {
/* 171 */     this.builder.createXmlMapper(createXmlMapper);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFactory(JsonFactory factory) {
/* 180 */     this.builder.factory(factory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDateFormat(DateFormat dateFormat) {
/* 190 */     this.builder.dateFormat(dateFormat);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSimpleDateFormat(String format) {
/* 200 */     this.builder.simpleDateFormat(format);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocale(Locale locale) {
/* 209 */     this.builder.locale(locale);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeZone(TimeZone timeZone) {
/* 218 */     this.builder.timeZone(timeZone);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAnnotationIntrospector(AnnotationIntrospector annotationIntrospector) {
/* 225 */     this.builder.annotationIntrospector(annotationIntrospector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPropertyNamingStrategy(PropertyNamingStrategy propertyNamingStrategy) {
/* 234 */     this.builder.propertyNamingStrategy(propertyNamingStrategy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultTyping(TypeResolverBuilder<?> typeResolverBuilder) {
/* 242 */     this.builder.defaultTyping(typeResolverBuilder);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSerializationInclusion(JsonInclude.Include serializationInclusion) {
/* 250 */     this.builder.serializationInclusion(serializationInclusion);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFilters(FilterProvider filters) {
/* 259 */     this.builder.filters(filters);
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
/*     */   public void setMixIns(Map<Class<?>, Class<?>> mixIns) {
/* 271 */     this.builder.mixIns(mixIns);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSerializers(JsonSerializer<?>... serializers) {
/* 280 */     this.builder.serializers(serializers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSerializersByType(Map<Class<?>, JsonSerializer<?>> serializers) {
/* 288 */     this.builder.serializersByType(serializers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDeserializers(JsonDeserializer<?>... deserializers) {
/* 298 */     this.builder.deserializers(deserializers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDeserializersByType(Map<Class<?>, JsonDeserializer<?>> deserializers) {
/* 305 */     this.builder.deserializersByType(deserializers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutoDetectFields(boolean autoDetectFields) {
/* 312 */     this.builder.autoDetectFields(autoDetectFields);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutoDetectGettersSetters(boolean autoDetectGettersSetters) {
/* 321 */     this.builder.autoDetectGettersSetters(autoDetectGettersSetters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultViewInclusion(boolean defaultViewInclusion) {
/* 329 */     this.builder.defaultViewInclusion(defaultViewInclusion);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFailOnUnknownProperties(boolean failOnUnknownProperties) {
/* 337 */     this.builder.failOnUnknownProperties(failOnUnknownProperties);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFailOnEmptyBeans(boolean failOnEmptyBeans) {
/* 344 */     this.builder.failOnEmptyBeans(failOnEmptyBeans);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIndentOutput(boolean indentOutput) {
/* 351 */     this.builder.indentOutput(indentOutput);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultUseWrapper(boolean defaultUseWrapper) {
/* 360 */     this.builder.defaultUseWrapper(defaultUseWrapper);
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
/*     */   public void setFeaturesToEnable(Object... featuresToEnable) {
/* 372 */     this.builder.featuresToEnable(featuresToEnable);
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
/*     */   public void setFeaturesToDisable(Object... featuresToDisable) {
/* 384 */     this.builder.featuresToDisable(featuresToDisable);
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
/*     */   public void setModules(List<Module> modules) {
/* 398 */     this.builder.modules(modules);
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
/*     */   public void setModulesToInstall(Class<? extends Module>... modules) {
/* 414 */     this.builder.modulesToInstall(modules);
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
/*     */   public void setFindModulesViaServiceLoader(boolean findModules) {
/* 427 */     this.builder.findModulesViaServiceLoader(findModules);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader beanClassLoader) {
/* 432 */     this.builder.moduleClassLoader(beanClassLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHandlerInstantiator(HandlerInstantiator handlerInstantiator) {
/* 443 */     this.builder.handlerInstantiator(handlerInstantiator);
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
/*     */   public void setApplicationContext(ApplicationContext applicationContext) {
/* 456 */     this.builder.applicationContext(applicationContext);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 462 */     if (this.objectMapper != null) {
/* 463 */       this.builder.configure(this.objectMapper);
/*     */     } else {
/*     */       
/* 466 */       this.objectMapper = this.builder.build();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ObjectMapper getObject() {
/* 476 */     return this.objectMapper;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/* 481 */     return (this.objectMapper != null) ? this.objectMapper.getClass() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 486 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/converter/json/Jackson2ObjectMapperFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */