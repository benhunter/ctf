/*     */ package org.springframework.validation;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.ConfigurablePropertyAccessor;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.PropertyAccessException;
/*     */ import org.springframework.beans.PropertyAccessorUtils;
/*     */ import org.springframework.beans.PropertyBatchUpdateException;
/*     */ import org.springframework.beans.PropertyEditorRegistry;
/*     */ import org.springframework.beans.PropertyValue;
/*     */ import org.springframework.beans.PropertyValues;
/*     */ import org.springframework.beans.SimpleTypeConverter;
/*     */ import org.springframework.beans.TypeConverter;
/*     */ import org.springframework.beans.TypeMismatchException;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.format.Formatter;
/*     */ import org.springframework.format.support.FormatterPropertyEditorAdapter;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.PatternMatchUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DataBinder
/*     */   implements PropertyEditorRegistry, TypeConverter
/*     */ {
/*     */   public static final String DEFAULT_OBJECT_NAME = "target";
/*     */   public static final int DEFAULT_AUTO_GROW_COLLECTION_LIMIT = 256;
/* 122 */   protected static final Log logger = LogFactory.getLog(DataBinder.class);
/*     */   
/*     */   @Nullable
/*     */   private final Object target;
/*     */   
/*     */   private final String objectName;
/*     */   
/*     */   @Nullable
/*     */   private AbstractPropertyBindingResult bindingResult;
/*     */   
/*     */   @Nullable
/*     */   private SimpleTypeConverter typeConverter;
/*     */   
/*     */   private boolean ignoreUnknownFields = true;
/*     */   
/*     */   private boolean ignoreInvalidFields = false;
/*     */   
/*     */   private boolean autoGrowNestedPaths = true;
/*     */   
/* 141 */   private int autoGrowCollectionLimit = 256;
/*     */   
/*     */   @Nullable
/*     */   private String[] allowedFields;
/*     */   
/*     */   @Nullable
/*     */   private String[] disallowedFields;
/*     */   
/*     */   @Nullable
/*     */   private String[] requiredFields;
/*     */   
/*     */   @Nullable
/*     */   private ConversionService conversionService;
/*     */   
/*     */   @Nullable
/*     */   private MessageCodesResolver messageCodesResolver;
/*     */   
/* 158 */   private BindingErrorProcessor bindingErrorProcessor = new DefaultBindingErrorProcessor();
/*     */   
/* 160 */   private final List<Validator> validators = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DataBinder(@Nullable Object target) {
/* 170 */     this(target, "target");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DataBinder(@Nullable Object target, String objectName) {
/* 180 */     this.target = ObjectUtils.unwrapOptional(target);
/* 181 */     this.objectName = objectName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getTarget() {
/* 190 */     return this.target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getObjectName() {
/* 197 */     return this.objectName;
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
/*     */   public void setAutoGrowNestedPaths(boolean autoGrowNestedPaths) {
/* 211 */     Assert.state((this.bindingResult == null), "DataBinder is already initialized - call setAutoGrowNestedPaths before other configuration methods");
/*     */     
/* 213 */     this.autoGrowNestedPaths = autoGrowNestedPaths;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAutoGrowNestedPaths() {
/* 220 */     return this.autoGrowNestedPaths;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutoGrowCollectionLimit(int autoGrowCollectionLimit) {
/* 231 */     Assert.state((this.bindingResult == null), "DataBinder is already initialized - call setAutoGrowCollectionLimit before other configuration methods");
/*     */     
/* 233 */     this.autoGrowCollectionLimit = autoGrowCollectionLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAutoGrowCollectionLimit() {
/* 240 */     return this.autoGrowCollectionLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initBeanPropertyAccess() {
/* 250 */     Assert.state((this.bindingResult == null), "DataBinder is already initialized - call initBeanPropertyAccess before other configuration methods");
/*     */     
/* 252 */     this.bindingResult = createBeanPropertyBindingResult();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractPropertyBindingResult createBeanPropertyBindingResult() {
/* 262 */     BeanPropertyBindingResult result = new BeanPropertyBindingResult(getTarget(), getObjectName(), isAutoGrowNestedPaths(), getAutoGrowCollectionLimit());
/*     */     
/* 264 */     if (this.conversionService != null) {
/* 265 */       result.initConversion(this.conversionService);
/*     */     }
/* 267 */     if (this.messageCodesResolver != null) {
/* 268 */       result.setMessageCodesResolver(this.messageCodesResolver);
/*     */     }
/*     */     
/* 271 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initDirectFieldAccess() {
/* 281 */     Assert.state((this.bindingResult == null), "DataBinder is already initialized - call initDirectFieldAccess before other configuration methods");
/*     */     
/* 283 */     this.bindingResult = createDirectFieldBindingResult();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractPropertyBindingResult createDirectFieldBindingResult() {
/* 293 */     DirectFieldBindingResult result = new DirectFieldBindingResult(getTarget(), getObjectName(), isAutoGrowNestedPaths());
/*     */     
/* 295 */     if (this.conversionService != null) {
/* 296 */       result.initConversion(this.conversionService);
/*     */     }
/* 298 */     if (this.messageCodesResolver != null) {
/* 299 */       result.setMessageCodesResolver(this.messageCodesResolver);
/*     */     }
/*     */     
/* 302 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractPropertyBindingResult getInternalBindingResult() {
/* 310 */     if (this.bindingResult == null) {
/* 311 */       initBeanPropertyAccess();
/*     */     }
/* 313 */     return this.bindingResult;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ConfigurablePropertyAccessor getPropertyAccessor() {
/* 320 */     return getInternalBindingResult().getPropertyAccessor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SimpleTypeConverter getSimpleTypeConverter() {
/* 327 */     if (this.typeConverter == null) {
/* 328 */       this.typeConverter = new SimpleTypeConverter();
/* 329 */       if (this.conversionService != null) {
/* 330 */         this.typeConverter.setConversionService(this.conversionService);
/*     */       }
/*     */     } 
/* 333 */     return this.typeConverter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected PropertyEditorRegistry getPropertyEditorRegistry() {
/* 340 */     if (getTarget() != null) {
/* 341 */       return (PropertyEditorRegistry)getInternalBindingResult().getPropertyAccessor();
/*     */     }
/*     */     
/* 344 */     return (PropertyEditorRegistry)getSimpleTypeConverter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TypeConverter getTypeConverter() {
/* 352 */     if (getTarget() != null) {
/* 353 */       return (TypeConverter)getInternalBindingResult().getPropertyAccessor();
/*     */     }
/*     */     
/* 356 */     return (TypeConverter)getSimpleTypeConverter();
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
/*     */   public BindingResult getBindingResult() {
/* 370 */     return getInternalBindingResult();
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
/*     */   public void setIgnoreUnknownFields(boolean ignoreUnknownFields) {
/* 385 */     this.ignoreUnknownFields = ignoreUnknownFields;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIgnoreUnknownFields() {
/* 392 */     return this.ignoreUnknownFields;
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
/*     */   public void setIgnoreInvalidFields(boolean ignoreInvalidFields) {
/* 407 */     this.ignoreInvalidFields = ignoreInvalidFields;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIgnoreInvalidFields() {
/* 414 */     return this.ignoreInvalidFields;
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
/*     */   public void setAllowedFields(@Nullable String... allowedFields) {
/* 429 */     this.allowedFields = PropertyAccessorUtils.canonicalPropertyNames(allowedFields);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String[] getAllowedFields() {
/* 438 */     return this.allowedFields;
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
/*     */   public void setDisallowedFields(@Nullable String... disallowedFields) {
/* 453 */     this.disallowedFields = PropertyAccessorUtils.canonicalPropertyNames(disallowedFields);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String[] getDisallowedFields() {
/* 462 */     return this.disallowedFields;
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
/*     */   public void setRequiredFields(@Nullable String... requiredFields) {
/* 476 */     this.requiredFields = PropertyAccessorUtils.canonicalPropertyNames(requiredFields);
/* 477 */     if (logger.isDebugEnabled()) {
/* 478 */       logger.debug("DataBinder requires binding of required fields [" + 
/* 479 */           StringUtils.arrayToCommaDelimitedString((Object[])requiredFields) + "]");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String[] getRequiredFields() {
/* 489 */     return this.requiredFields;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessageCodesResolver(@Nullable MessageCodesResolver messageCodesResolver) {
/* 500 */     Assert.state((this.messageCodesResolver == null), "DataBinder is already initialized with MessageCodesResolver");
/* 501 */     this.messageCodesResolver = messageCodesResolver;
/* 502 */     if (this.bindingResult != null && messageCodesResolver != null) {
/* 503 */       this.bindingResult.setMessageCodesResolver(messageCodesResolver);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBindingErrorProcessor(BindingErrorProcessor bindingErrorProcessor) {
/* 514 */     Assert.notNull(bindingErrorProcessor, "BindingErrorProcessor must not be null");
/* 515 */     this.bindingErrorProcessor = bindingErrorProcessor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BindingErrorProcessor getBindingErrorProcessor() {
/* 522 */     return this.bindingErrorProcessor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValidator(@Nullable Validator validator) {
/* 531 */     assertValidators(new Validator[] { validator });
/* 532 */     this.validators.clear();
/* 533 */     if (validator != null) {
/* 534 */       this.validators.add(validator);
/*     */     }
/*     */   }
/*     */   
/*     */   private void assertValidators(Validator... validators) {
/* 539 */     Object target = getTarget();
/* 540 */     for (Validator validator : validators) {
/* 541 */       if (validator != null && target != null && !validator.supports(target.getClass())) {
/* 542 */         throw new IllegalStateException("Invalid target for Validator [" + validator + "]: " + target);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addValidators(Validator... validators) {
/* 553 */     assertValidators(validators);
/* 554 */     this.validators.addAll(Arrays.asList(validators));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void replaceValidators(Validator... validators) {
/* 563 */     assertValidators(validators);
/* 564 */     this.validators.clear();
/* 565 */     this.validators.addAll(Arrays.asList(validators));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Validator getValidator() {
/* 573 */     return !this.validators.isEmpty() ? this.validators.get(0) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Validator> getValidators() {
/* 580 */     return Collections.unmodifiableList(this.validators);
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
/*     */   public void setConversionService(@Nullable ConversionService conversionService) {
/* 593 */     Assert.state((this.conversionService == null), "DataBinder is already initialized with ConversionService");
/* 594 */     this.conversionService = conversionService;
/* 595 */     if (this.bindingResult != null && conversionService != null) {
/* 596 */       this.bindingResult.initConversion(conversionService);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ConversionService getConversionService() {
/* 605 */     return this.conversionService;
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
/*     */   public void addCustomFormatter(Formatter<?> formatter) {
/* 617 */     FormatterPropertyEditorAdapter adapter = new FormatterPropertyEditorAdapter(formatter);
/* 618 */     getPropertyEditorRegistry().registerCustomEditor(adapter.getFieldType(), (PropertyEditor)adapter);
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
/*     */   public void addCustomFormatter(Formatter<?> formatter, String... fields) {
/* 631 */     FormatterPropertyEditorAdapter adapter = new FormatterPropertyEditorAdapter(formatter);
/* 632 */     Class<?> fieldType = adapter.getFieldType();
/* 633 */     if (ObjectUtils.isEmpty((Object[])fields)) {
/* 634 */       getPropertyEditorRegistry().registerCustomEditor(fieldType, (PropertyEditor)adapter);
/*     */     } else {
/*     */       
/* 637 */       for (String field : fields) {
/* 638 */         getPropertyEditorRegistry().registerCustomEditor(fieldType, field, (PropertyEditor)adapter);
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
/*     */ 
/*     */   
/*     */   public void addCustomFormatter(Formatter<?> formatter, Class<?>... fieldTypes) {
/* 655 */     FormatterPropertyEditorAdapter adapter = new FormatterPropertyEditorAdapter(formatter);
/* 656 */     if (ObjectUtils.isEmpty((Object[])fieldTypes)) {
/* 657 */       getPropertyEditorRegistry().registerCustomEditor(adapter.getFieldType(), (PropertyEditor)adapter);
/*     */     } else {
/*     */       
/* 660 */       for (Class<?> fieldType : fieldTypes) {
/* 661 */         getPropertyEditorRegistry().registerCustomEditor(fieldType, (PropertyEditor)adapter);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor) {
/* 668 */     getPropertyEditorRegistry().registerCustomEditor(requiredType, propertyEditor);
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerCustomEditor(@Nullable Class<?> requiredType, @Nullable String field, PropertyEditor propertyEditor) {
/* 673 */     getPropertyEditorRegistry().registerCustomEditor(requiredType, field, propertyEditor);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public PropertyEditor findCustomEditor(@Nullable Class<?> requiredType, @Nullable String propertyPath) {
/* 679 */     return getPropertyEditorRegistry().findCustomEditor(requiredType, propertyPath);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T> T convertIfNecessary(@Nullable Object value, @Nullable Class<T> requiredType) throws TypeMismatchException {
/* 685 */     return (T)getTypeConverter().convertIfNecessary(value, requiredType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T> T convertIfNecessary(@Nullable Object value, @Nullable Class<T> requiredType, @Nullable MethodParameter methodParam) throws TypeMismatchException {
/* 693 */     return (T)getTypeConverter().convertIfNecessary(value, requiredType, methodParam);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T> T convertIfNecessary(@Nullable Object value, @Nullable Class<T> requiredType, @Nullable Field field) throws TypeMismatchException {
/* 701 */     return (T)getTypeConverter().convertIfNecessary(value, requiredType, field);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T> T convertIfNecessary(@Nullable Object value, @Nullable Class<T> requiredType, @Nullable TypeDescriptor typeDescriptor) throws TypeMismatchException {
/* 709 */     return (T)getTypeConverter().convertIfNecessary(value, requiredType, typeDescriptor);
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
/*     */   public void bind(PropertyValues pvs) {
/* 727 */     MutablePropertyValues mpvs = (pvs instanceof MutablePropertyValues) ? (MutablePropertyValues)pvs : new MutablePropertyValues(pvs);
/*     */     
/* 729 */     doBind(mpvs);
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
/*     */   protected void doBind(MutablePropertyValues mpvs) {
/* 742 */     checkAllowedFields(mpvs);
/* 743 */     checkRequiredFields(mpvs);
/* 744 */     applyPropertyValues(mpvs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkAllowedFields(MutablePropertyValues mpvs) {
/* 755 */     PropertyValue[] pvs = mpvs.getPropertyValues();
/* 756 */     for (PropertyValue pv : pvs) {
/* 757 */       String field = PropertyAccessorUtils.canonicalPropertyName(pv.getName());
/* 758 */       if (!isAllowed(field)) {
/* 759 */         mpvs.removePropertyValue(pv);
/* 760 */         getBindingResult().recordSuppressedField(field);
/* 761 */         if (logger.isDebugEnabled()) {
/* 762 */           logger.debug("Field [" + field + "] has been removed from PropertyValues and will not be bound, because it has not been found in the list of allowed fields");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isAllowed(String field) {
/* 784 */     String[] allowed = getAllowedFields();
/* 785 */     String[] disallowed = getDisallowedFields();
/* 786 */     return ((ObjectUtils.isEmpty((Object[])allowed) || PatternMatchUtils.simpleMatch(allowed, field)) && (
/* 787 */       ObjectUtils.isEmpty((Object[])disallowed) || !PatternMatchUtils.simpleMatch(disallowed, field)));
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
/*     */   protected void checkRequiredFields(MutablePropertyValues mpvs) {
/* 799 */     String[] requiredFields = getRequiredFields();
/* 800 */     if (!ObjectUtils.isEmpty((Object[])requiredFields)) {
/* 801 */       Map<String, PropertyValue> propertyValues = new HashMap<>();
/* 802 */       PropertyValue[] pvs = mpvs.getPropertyValues();
/* 803 */       for (PropertyValue pv : pvs) {
/* 804 */         String canonicalName = PropertyAccessorUtils.canonicalPropertyName(pv.getName());
/* 805 */         propertyValues.put(canonicalName, pv);
/*     */       } 
/* 807 */       for (String field : requiredFields) {
/* 808 */         PropertyValue pv = propertyValues.get(field);
/* 809 */         boolean empty = (pv == null || pv.getValue() == null);
/* 810 */         if (!empty) {
/* 811 */           if (pv.getValue() instanceof String) {
/* 812 */             empty = !StringUtils.hasText((String)pv.getValue());
/*     */           }
/* 814 */           else if (pv.getValue() instanceof String[]) {
/* 815 */             String[] values = (String[])pv.getValue();
/* 816 */             empty = (values.length == 0 || !StringUtils.hasText(values[0]));
/*     */           } 
/*     */         }
/* 819 */         if (empty) {
/*     */           
/* 821 */           getBindingErrorProcessor().processMissingFieldError(field, getInternalBindingResult());
/*     */ 
/*     */           
/* 824 */           if (pv != null) {
/* 825 */             mpvs.removePropertyValue(pv);
/* 826 */             propertyValues.remove(field);
/*     */           } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void applyPropertyValues(MutablePropertyValues mpvs) {
/*     */     try {
/* 848 */       getPropertyAccessor().setPropertyValues((PropertyValues)mpvs, isIgnoreUnknownFields(), isIgnoreInvalidFields());
/*     */     }
/* 850 */     catch (PropertyBatchUpdateException ex) {
/*     */       
/* 852 */       for (PropertyAccessException pae : ex.getPropertyAccessExceptions()) {
/* 853 */         getBindingErrorProcessor().processPropertyAccessException(pae, getInternalBindingResult());
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
/*     */   public void validate() {
/* 865 */     Object target = getTarget();
/* 866 */     Assert.state((target != null), "No target to validate");
/* 867 */     BindingResult bindingResult = getBindingResult();
/*     */     
/* 869 */     for (Validator validator : getValidators()) {
/* 870 */       validator.validate(target, bindingResult);
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
/*     */   public void validate(Object... validationHints) {
/* 883 */     Object target = getTarget();
/* 884 */     Assert.state((target != null), "No target to validate");
/* 885 */     BindingResult bindingResult = getBindingResult();
/*     */     
/* 887 */     for (Validator validator : getValidators()) {
/* 888 */       if (!ObjectUtils.isEmpty(validationHints) && validator instanceof SmartValidator) {
/* 889 */         ((SmartValidator)validator).validate(target, bindingResult, validationHints); continue;
/*     */       } 
/* 891 */       if (validator != null) {
/* 892 */         validator.validate(target, bindingResult);
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
/*     */   public Map<?, ?> close() throws BindException {
/* 905 */     if (getBindingResult().hasErrors()) {
/* 906 */       throw new BindException(getBindingResult());
/*     */     }
/* 908 */     return getBindingResult().getModel();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/validation/DataBinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */