/*     */ package org.springframework.http.converter.json;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonAutoDetect;
/*     */ import com.fasterxml.jackson.annotation.JsonInclude;
/*     */ import com.fasterxml.jackson.annotation.PropertyAccessor;
/*     */ import com.fasterxml.jackson.core.JsonFactory;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.Module;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.PropertyNamingStrategy;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*     */ import com.fasterxml.jackson.databind.module.SimpleModule;
/*     */ import com.fasterxml.jackson.databind.ser.FilterProvider;
/*     */ import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
/*     */ import com.fasterxml.jackson.dataformat.smile.SmileFactory;
/*     */ import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
/*     */ import com.fasterxml.jackson.dataformat.xml.XmlFactory;
/*     */ import com.fasterxml.jackson.dataformat.xml.XmlMapper;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.FatalBeanException;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.core.KotlinDetector;
/*     */ import org.springframework.http.HttpLogging;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.util.xml.StaxUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Jackson2ObjectMapperBuilder
/*     */ {
/*     */   private static volatile boolean kotlinWarningLogged = false;
/* 108 */   private final Log logger = HttpLogging.forLogName(getClass());
/*     */   
/* 110 */   private final Map<Class<?>, Class<?>> mixIns = new LinkedHashMap<>();
/*     */   
/* 112 */   private final Map<Class<?>, JsonSerializer<?>> serializers = new LinkedHashMap<>();
/*     */   
/* 114 */   private final Map<Class<?>, JsonDeserializer<?>> deserializers = new LinkedHashMap<>();
/*     */   
/* 116 */   private final Map<PropertyAccessor, JsonAutoDetect.Visibility> visibilities = new LinkedHashMap<>();
/*     */   
/* 118 */   private final Map<Object, Boolean> features = new LinkedHashMap<>();
/*     */   
/*     */   private boolean createXmlMapper = false;
/*     */   
/*     */   @Nullable
/*     */   private JsonFactory factory;
/*     */   
/*     */   @Nullable
/*     */   private DateFormat dateFormat;
/*     */   
/*     */   @Nullable
/*     */   private Locale locale;
/*     */   
/*     */   @Nullable
/*     */   private TimeZone timeZone;
/*     */   
/*     */   @Nullable
/*     */   private AnnotationIntrospector annotationIntrospector;
/*     */   
/*     */   @Nullable
/*     */   private PropertyNamingStrategy propertyNamingStrategy;
/*     */   
/*     */   @Nullable
/*     */   private TypeResolverBuilder<?> defaultTyping;
/*     */   
/*     */   @Nullable
/*     */   private JsonInclude.Include serializationInclusion;
/*     */   
/*     */   @Nullable
/*     */   private FilterProvider filters;
/*     */   
/*     */   @Nullable
/*     */   private List<Module> modules;
/*     */   
/*     */   @Nullable
/*     */   private Class<? extends Module>[] moduleClasses;
/*     */   
/*     */   private boolean findModulesViaServiceLoader = false;
/*     */   
/*     */   private boolean findWellKnownModules = true;
/*     */   
/* 159 */   private ClassLoader moduleClassLoader = getClass().getClassLoader();
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private HandlerInstantiator handlerInstantiator;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ApplicationContext applicationContext;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Boolean defaultUseWrapper;
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder createXmlMapper(boolean createXmlMapper) {
/* 177 */     this.createXmlMapper = createXmlMapper;
/* 178 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder factory(JsonFactory factory) {
/* 187 */     this.factory = factory;
/* 188 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder dateFormat(DateFormat dateFormat) {
/* 198 */     this.dateFormat = dateFormat;
/* 199 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder simpleDateFormat(String format) {
/* 209 */     this.dateFormat = new SimpleDateFormat(format);
/* 210 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder locale(Locale locale) {
/* 219 */     this.locale = locale;
/* 220 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder locale(String localeString) {
/* 230 */     this.locale = StringUtils.parseLocale(localeString);
/* 231 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder timeZone(TimeZone timeZone) {
/* 240 */     this.timeZone = timeZone;
/* 241 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder timeZone(String timeZoneString) {
/* 251 */     this.timeZone = StringUtils.parseTimeZoneString(timeZoneString);
/* 252 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder annotationIntrospector(AnnotationIntrospector annotationIntrospector) {
/* 259 */     this.annotationIntrospector = annotationIntrospector;
/* 260 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder propertyNamingStrategy(PropertyNamingStrategy propertyNamingStrategy) {
/* 268 */     this.propertyNamingStrategy = propertyNamingStrategy;
/* 269 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder defaultTyping(TypeResolverBuilder<?> typeResolverBuilder) {
/* 277 */     this.defaultTyping = typeResolverBuilder;
/* 278 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder serializationInclusion(JsonInclude.Include serializationInclusion) {
/* 286 */     this.serializationInclusion = serializationInclusion;
/* 287 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder filters(FilterProvider filters) {
/* 296 */     this.filters = filters;
/* 297 */     return this;
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
/*     */   public Jackson2ObjectMapperBuilder mixIn(Class<?> target, Class<?> mixinSource) {
/* 309 */     this.mixIns.put(target, mixinSource);
/* 310 */     return this;
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
/*     */   public Jackson2ObjectMapperBuilder mixIns(Map<Class<?>, Class<?>> mixIns) {
/* 322 */     this.mixIns.putAll(mixIns);
/* 323 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder serializers(JsonSerializer<?>... serializers) {
/* 332 */     for (JsonSerializer<?> serializer : serializers) {
/* 333 */       Class<?> handledType = serializer.handledType();
/* 334 */       if (handledType == null || handledType == Object.class) {
/* 335 */         throw new IllegalArgumentException("Unknown handled type in " + serializer.getClass().getName());
/*     */       }
/* 337 */       this.serializers.put(serializer.handledType(), serializer);
/*     */     } 
/* 339 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder serializerByType(Class<?> type, JsonSerializer<?> serializer) {
/* 348 */     this.serializers.put(type, serializer);
/* 349 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder serializersByType(Map<Class<?>, JsonSerializer<?>> serializers) {
/* 357 */     this.serializers.putAll(serializers);
/* 358 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder deserializers(JsonDeserializer<?>... deserializers) {
/* 368 */     for (JsonDeserializer<?> deserializer : deserializers) {
/* 369 */       Class<?> handledType = deserializer.handledType();
/* 370 */       if (handledType == null || handledType == Object.class) {
/* 371 */         throw new IllegalArgumentException("Unknown handled type in " + deserializer.getClass().getName());
/*     */       }
/* 373 */       this.deserializers.put(deserializer.handledType(), deserializer);
/*     */     } 
/* 375 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder deserializerByType(Class<?> type, JsonDeserializer<?> deserializer) {
/* 383 */     this.deserializers.put(type, deserializer);
/* 384 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder deserializersByType(Map<Class<?>, JsonDeserializer<?>> deserializers) {
/* 391 */     this.deserializers.putAll(deserializers);
/* 392 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder autoDetectFields(boolean autoDetectFields) {
/* 399 */     this.features.put(MapperFeature.AUTO_DETECT_FIELDS, Boolean.valueOf(autoDetectFields));
/* 400 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder autoDetectGettersSetters(boolean autoDetectGettersSetters) {
/* 409 */     this.features.put(MapperFeature.AUTO_DETECT_GETTERS, Boolean.valueOf(autoDetectGettersSetters));
/* 410 */     this.features.put(MapperFeature.AUTO_DETECT_SETTERS, Boolean.valueOf(autoDetectGettersSetters));
/* 411 */     this.features.put(MapperFeature.AUTO_DETECT_IS_GETTERS, Boolean.valueOf(autoDetectGettersSetters));
/* 412 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder defaultViewInclusion(boolean defaultViewInclusion) {
/* 419 */     this.features.put(MapperFeature.DEFAULT_VIEW_INCLUSION, Boolean.valueOf(defaultViewInclusion));
/* 420 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder failOnUnknownProperties(boolean failOnUnknownProperties) {
/* 427 */     this.features.put(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, Boolean.valueOf(failOnUnknownProperties));
/* 428 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder failOnEmptyBeans(boolean failOnEmptyBeans) {
/* 435 */     this.features.put(SerializationFeature.FAIL_ON_EMPTY_BEANS, Boolean.valueOf(failOnEmptyBeans));
/* 436 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder indentOutput(boolean indentOutput) {
/* 443 */     this.features.put(SerializationFeature.INDENT_OUTPUT, Boolean.valueOf(indentOutput));
/* 444 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder defaultUseWrapper(boolean defaultUseWrapper) {
/* 453 */     this.defaultUseWrapper = Boolean.valueOf(defaultUseWrapper);
/* 454 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder visibility(PropertyAccessor accessor, JsonAutoDetect.Visibility visibility) {
/* 464 */     this.visibilities.put(accessor, visibility);
/* 465 */     return this;
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
/*     */   public Jackson2ObjectMapperBuilder featuresToEnable(Object... featuresToEnable) {
/* 477 */     for (Object feature : featuresToEnable) {
/* 478 */       this.features.put(feature, Boolean.TRUE);
/*     */     }
/* 480 */     return this;
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
/*     */   public Jackson2ObjectMapperBuilder featuresToDisable(Object... featuresToDisable) {
/* 492 */     for (Object feature : featuresToDisable) {
/* 493 */       this.features.put(feature, Boolean.FALSE);
/*     */     }
/* 495 */     return this;
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
/*     */   public Jackson2ObjectMapperBuilder modules(Module... modules) {
/* 512 */     return modules(Arrays.asList(modules));
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
/*     */   public Jackson2ObjectMapperBuilder modules(List<Module> modules) {
/* 528 */     this.modules = new LinkedList<>(modules);
/* 529 */     this.findModulesViaServiceLoader = false;
/* 530 */     this.findWellKnownModules = false;
/* 531 */     return this;
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
/*     */   public Jackson2ObjectMapperBuilder modulesToInstall(Module... modules) {
/* 547 */     this.modules = Arrays.asList(modules);
/* 548 */     this.findWellKnownModules = true;
/* 549 */     return this;
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
/*     */   public Jackson2ObjectMapperBuilder modulesToInstall(Class<? extends Module>... modules) {
/* 566 */     this.moduleClasses = modules;
/* 567 */     this.findWellKnownModules = true;
/* 568 */     return this;
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
/*     */   public Jackson2ObjectMapperBuilder findModulesViaServiceLoader(boolean findModules) {
/* 580 */     this.findModulesViaServiceLoader = findModules;
/* 581 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder moduleClassLoader(ClassLoader moduleClassLoader) {
/* 588 */     this.moduleClassLoader = moduleClassLoader;
/* 589 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder handlerInstantiator(HandlerInstantiator handlerInstantiator) {
/* 599 */     this.handlerInstantiator = handlerInstantiator;
/* 600 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jackson2ObjectMapperBuilder applicationContext(ApplicationContext applicationContext) {
/* 610 */     this.applicationContext = applicationContext;
/* 611 */     return this;
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
/*     */   public <T extends ObjectMapper> T build() {
/*     */     ObjectMapper mapper;
/* 625 */     if (this.createXmlMapper) {
/*     */ 
/*     */       
/* 628 */       mapper = (this.defaultUseWrapper != null) ? (new XmlObjectMapperInitializer()).create(this.defaultUseWrapper.booleanValue(), this.factory) : (new XmlObjectMapperInitializer()).create(this.factory);
/*     */     } else {
/*     */       
/* 631 */       mapper = (this.factory != null) ? new ObjectMapper(this.factory) : new ObjectMapper();
/*     */     } 
/* 633 */     configure(mapper);
/* 634 */     return (T)mapper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void configure(ObjectMapper objectMapper) {
/* 643 */     Assert.notNull(objectMapper, "ObjectMapper must not be null");
/*     */     
/* 645 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap();
/* 646 */     if (this.findModulesViaServiceLoader) {
/* 647 */       ObjectMapper.findModules(this.moduleClassLoader).forEach(module -> registerModule(module, modulesToRegister));
/*     */     }
/* 649 */     else if (this.findWellKnownModules) {
/* 650 */       registerWellKnownModulesIfAvailable((MultiValueMap<Object, Module>)linkedMultiValueMap);
/*     */     } 
/*     */     
/* 653 */     if (this.modules != null) {
/* 654 */       this.modules.forEach(module -> registerModule(module, modulesToRegister));
/*     */     }
/* 656 */     if (this.moduleClasses != null) {
/* 657 */       for (Class<? extends Module> moduleClass : this.moduleClasses) {
/* 658 */         registerModule((Module)BeanUtils.instantiateClass(moduleClass), (MultiValueMap<Object, Module>)linkedMultiValueMap);
/*     */       }
/*     */     }
/* 661 */     List<Module> modules = new ArrayList<>();
/* 662 */     for (List<Module> nestedModules : (Iterable<List<Module>>)linkedMultiValueMap.values()) {
/* 663 */       modules.addAll(nestedModules);
/*     */     }
/* 665 */     objectMapper.registerModules(modules);
/*     */     
/* 667 */     if (this.dateFormat != null) {
/* 668 */       objectMapper.setDateFormat(this.dateFormat);
/*     */     }
/* 670 */     if (this.locale != null) {
/* 671 */       objectMapper.setLocale(this.locale);
/*     */     }
/* 673 */     if (this.timeZone != null) {
/* 674 */       objectMapper.setTimeZone(this.timeZone);
/*     */     }
/*     */     
/* 677 */     if (this.annotationIntrospector != null) {
/* 678 */       objectMapper.setAnnotationIntrospector(this.annotationIntrospector);
/*     */     }
/* 680 */     if (this.propertyNamingStrategy != null) {
/* 681 */       objectMapper.setPropertyNamingStrategy(this.propertyNamingStrategy);
/*     */     }
/* 683 */     if (this.defaultTyping != null) {
/* 684 */       objectMapper.setDefaultTyping(this.defaultTyping);
/*     */     }
/* 686 */     if (this.serializationInclusion != null) {
/* 687 */       objectMapper.setSerializationInclusion(this.serializationInclusion);
/*     */     }
/*     */     
/* 690 */     if (this.filters != null) {
/* 691 */       objectMapper.setFilterProvider(this.filters);
/*     */     }
/*     */     
/* 694 */     this.mixIns.forEach(objectMapper::addMixIn);
/*     */     
/* 696 */     if (!this.serializers.isEmpty() || !this.deserializers.isEmpty()) {
/* 697 */       SimpleModule module = new SimpleModule();
/* 698 */       addSerializers(module);
/* 699 */       addDeserializers(module);
/* 700 */       objectMapper.registerModule((Module)module);
/*     */     } 
/*     */     
/* 703 */     this.visibilities.forEach(objectMapper::setVisibility);
/*     */     
/* 705 */     customizeDefaultFeatures(objectMapper);
/* 706 */     this.features.forEach((feature, enabled) -> configureFeature(objectMapper, feature, enabled.booleanValue()));
/*     */     
/* 708 */     if (this.handlerInstantiator != null) {
/* 709 */       objectMapper.setHandlerInstantiator(this.handlerInstantiator);
/*     */     }
/* 711 */     else if (this.applicationContext != null) {
/* 712 */       objectMapper.setHandlerInstantiator(new SpringHandlerInstantiator(this.applicationContext
/* 713 */             .getAutowireCapableBeanFactory()));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void registerModule(Module module, MultiValueMap<Object, Module> modulesToRegister) {
/* 718 */     if (module.getTypeId() == null) {
/* 719 */       modulesToRegister.add(SimpleModule.class.getName(), module);
/*     */     } else {
/*     */       
/* 722 */       modulesToRegister.set(module.getTypeId(), module);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void customizeDefaultFeatures(ObjectMapper objectMapper) {
/* 730 */     if (!this.features.containsKey(MapperFeature.DEFAULT_VIEW_INCLUSION)) {
/* 731 */       configureFeature(objectMapper, MapperFeature.DEFAULT_VIEW_INCLUSION, false);
/*     */     }
/* 733 */     if (!this.features.containsKey(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)) {
/* 734 */       configureFeature(objectMapper, DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private <T> void addSerializers(SimpleModule module) {
/* 740 */     this.serializers.forEach((type, serializer) -> module.addSerializer(type, serializer));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private <T> void addDeserializers(SimpleModule module) {
/* 746 */     this.deserializers.forEach((type, deserializer) -> module.addDeserializer(type, deserializer));
/*     */   }
/*     */ 
/*     */   
/*     */   private void configureFeature(ObjectMapper objectMapper, Object feature, boolean enabled) {
/* 751 */     if (feature instanceof JsonParser.Feature) {
/* 752 */       objectMapper.configure((JsonParser.Feature)feature, enabled);
/*     */     }
/* 754 */     else if (feature instanceof JsonGenerator.Feature) {
/* 755 */       objectMapper.configure((JsonGenerator.Feature)feature, enabled);
/*     */     }
/* 757 */     else if (feature instanceof SerializationFeature) {
/* 758 */       objectMapper.configure((SerializationFeature)feature, enabled);
/*     */     }
/* 760 */     else if (feature instanceof DeserializationFeature) {
/* 761 */       objectMapper.configure((DeserializationFeature)feature, enabled);
/*     */     }
/* 763 */     else if (feature instanceof MapperFeature) {
/* 764 */       objectMapper.configure((MapperFeature)feature, enabled);
/*     */     } else {
/*     */       
/* 767 */       throw new FatalBeanException("Unknown feature class: " + feature.getClass().getName());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void registerWellKnownModulesIfAvailable(MultiValueMap<Object, Module> modulesToRegister) {
/*     */     try {
/* 775 */       Class<? extends Module> jdk8ModuleClass = ClassUtils.forName("com.fasterxml.jackson.datatype.jdk8.Jdk8Module", this.moduleClassLoader);
/* 776 */       Module jdk8Module = (Module)BeanUtils.instantiateClass(jdk8ModuleClass);
/* 777 */       modulesToRegister.set(jdk8Module.getTypeId(), jdk8Module);
/*     */     }
/* 779 */     catch (ClassNotFoundException classNotFoundException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 785 */       Class<? extends Module> javaTimeModuleClass = ClassUtils.forName("com.fasterxml.jackson.datatype.jsr310.JavaTimeModule", this.moduleClassLoader);
/* 786 */       Module javaTimeModule = (Module)BeanUtils.instantiateClass(javaTimeModuleClass);
/* 787 */       modulesToRegister.set(javaTimeModule.getTypeId(), javaTimeModule);
/*     */     }
/* 789 */     catch (ClassNotFoundException classNotFoundException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 794 */     if (ClassUtils.isPresent("org.joda.time.LocalDate", this.moduleClassLoader)) {
/*     */       
/*     */       try {
/* 797 */         Class<? extends Module> jodaModuleClass = ClassUtils.forName("com.fasterxml.jackson.datatype.joda.JodaModule", this.moduleClassLoader);
/* 798 */         Module jodaModule = (Module)BeanUtils.instantiateClass(jodaModuleClass);
/* 799 */         modulesToRegister.set(jodaModule.getTypeId(), jodaModule);
/*     */       }
/* 801 */       catch (ClassNotFoundException classNotFoundException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 807 */     if (KotlinDetector.isKotlinPresent()) {
/*     */       
/*     */       try {
/* 810 */         Class<? extends Module> kotlinModuleClass = ClassUtils.forName("com.fasterxml.jackson.module.kotlin.KotlinModule", this.moduleClassLoader);
/* 811 */         Module kotlinModule = (Module)BeanUtils.instantiateClass(kotlinModuleClass);
/* 812 */         modulesToRegister.set(kotlinModule.getTypeId(), kotlinModule);
/*     */       }
/* 814 */       catch (ClassNotFoundException ex) {
/* 815 */         if (!kotlinWarningLogged) {
/* 816 */           kotlinWarningLogged = true;
/* 817 */           this.logger.warn("For Jackson Kotlin classes support please add \"com.fasterxml.jackson.module:jackson-module-kotlin\" to the classpath");
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
/*     */ 
/*     */   
/*     */   public static Jackson2ObjectMapperBuilder json() {
/* 832 */     return new Jackson2ObjectMapperBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Jackson2ObjectMapperBuilder xml() {
/* 840 */     return (new Jackson2ObjectMapperBuilder()).createXmlMapper(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Jackson2ObjectMapperBuilder smile() {
/* 849 */     return (new Jackson2ObjectMapperBuilder()).factory((new SmileFactoryInitializer()).create());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Jackson2ObjectMapperBuilder cbor() {
/* 858 */     return (new Jackson2ObjectMapperBuilder()).factory((new CborFactoryInitializer()).create());
/*     */   }
/*     */   
/*     */   private static class XmlObjectMapperInitializer {
/*     */     private XmlObjectMapperInitializer() {}
/*     */     
/*     */     public ObjectMapper create(@Nullable JsonFactory factory) {
/* 865 */       if (factory != null) {
/* 866 */         return (ObjectMapper)new XmlMapper((XmlFactory)factory);
/*     */       }
/*     */       
/* 869 */       return (ObjectMapper)new XmlMapper(StaxUtils.createDefensiveInputFactory());
/*     */     }
/*     */ 
/*     */     
/*     */     public ObjectMapper create(boolean defaultUseWrapper, @Nullable JsonFactory factory) {
/* 874 */       JacksonXmlModule module = new JacksonXmlModule();
/* 875 */       module.setDefaultUseWrapper(defaultUseWrapper);
/* 876 */       if (factory != null) {
/* 877 */         return (ObjectMapper)new XmlMapper((XmlFactory)factory, module);
/*     */       }
/*     */       
/* 880 */       return (ObjectMapper)new XmlMapper(new XmlFactory(StaxUtils.createDefensiveInputFactory()), module);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SmileFactoryInitializer
/*     */   {
/*     */     private SmileFactoryInitializer() {}
/*     */     
/*     */     public JsonFactory create() {
/* 889 */       return (JsonFactory)new SmileFactory();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CborFactoryInitializer {
/*     */     private CborFactoryInitializer() {}
/*     */     
/*     */     public JsonFactory create() {
/* 897 */       return (JsonFactory)new CBORFactory();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/converter/json/Jackson2ObjectMapperBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */