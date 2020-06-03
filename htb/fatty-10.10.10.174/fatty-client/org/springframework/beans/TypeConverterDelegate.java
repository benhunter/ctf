/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.CollectionFactory;
/*     */ import org.springframework.core.convert.ConversionFailedException;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.NumberUtils;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ class TypeConverterDelegate
/*     */ {
/*  57 */   private static final Log logger = LogFactory.getLog(TypeConverterDelegate.class);
/*     */ 
/*     */ 
/*     */   
/*     */   private final PropertyEditorRegistrySupport propertyEditorRegistry;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private final Object targetObject;
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeConverterDelegate(PropertyEditorRegistrySupport propertyEditorRegistry) {
/*  70 */     this(propertyEditorRegistry, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeConverterDelegate(PropertyEditorRegistrySupport propertyEditorRegistry, @Nullable Object targetObject) {
/*  79 */     this.propertyEditorRegistry = propertyEditorRegistry;
/*  80 */     this.targetObject = targetObject;
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
/*     */   public <T> T convertIfNecessary(@Nullable String propertyName, @Nullable Object oldValue, Object newValue, @Nullable Class<T> requiredType) throws IllegalArgumentException {
/*  98 */     return convertIfNecessary(propertyName, oldValue, newValue, requiredType, TypeDescriptor.valueOf(requiredType));
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
/*     */   public <T> T convertIfNecessary(@Nullable String propertyName, @Nullable Object oldValue, @Nullable Object<?> newValue, @Nullable Class<T> requiredType, @Nullable TypeDescriptor typeDescriptor) throws IllegalArgumentException {
/* 119 */     PropertyEditor editor = this.propertyEditorRegistry.findCustomEditor(requiredType, propertyName);
/*     */     
/* 121 */     ConversionFailedException conversionAttemptEx = null;
/*     */ 
/*     */     
/* 124 */     ConversionService conversionService = this.propertyEditorRegistry.getConversionService();
/* 125 */     if (editor == null && conversionService != null && newValue != null && typeDescriptor != null) {
/* 126 */       TypeDescriptor sourceTypeDesc = TypeDescriptor.forObject(newValue);
/* 127 */       if (conversionService.canConvert(sourceTypeDesc, typeDescriptor)) {
/*     */         try {
/* 129 */           return (T)conversionService.convert(newValue, sourceTypeDesc, typeDescriptor);
/*     */         }
/* 131 */         catch (ConversionFailedException ex) {
/*     */           
/* 133 */           conversionAttemptEx = ex;
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 138 */     Object<?> convertedValue = newValue;
/*     */ 
/*     */     
/* 141 */     if (editor != null || (requiredType != null && !ClassUtils.isAssignableValue(requiredType, convertedValue))) {
/* 142 */       if (typeDescriptor != null && requiredType != null && Collection.class.isAssignableFrom(requiredType) && convertedValue instanceof String) {
/*     */         
/* 144 */         TypeDescriptor elementTypeDesc = typeDescriptor.getElementTypeDescriptor();
/* 145 */         if (elementTypeDesc != null) {
/* 146 */           Class<?> elementType = elementTypeDesc.getType();
/* 147 */           if (Class.class == elementType || Enum.class.isAssignableFrom(elementType)) {
/* 148 */             convertedValue = (Object<?>)StringUtils.commaDelimitedListToStringArray((String)convertedValue);
/*     */           }
/*     */         } 
/*     */       } 
/* 152 */       if (editor == null) {
/* 153 */         editor = findDefaultEditor(requiredType);
/*     */       }
/* 155 */       convertedValue = (Object<?>)doConvertValue(oldValue, convertedValue, requiredType, editor);
/*     */     } 
/*     */     
/* 158 */     boolean standardConversion = false;
/*     */     
/* 160 */     if (requiredType != null) {
/*     */ 
/*     */       
/* 163 */       if (convertedValue != null) {
/* 164 */         if (Object.class == requiredType) {
/* 165 */           return (T)convertedValue;
/*     */         }
/* 167 */         if (requiredType.isArray()) {
/*     */           
/* 169 */           if (convertedValue instanceof String && Enum.class.isAssignableFrom(requiredType.getComponentType())) {
/* 170 */             convertedValue = (Object<?>)StringUtils.commaDelimitedListToStringArray((String)convertedValue);
/*     */           }
/* 172 */           return (T)convertToTypedArray(convertedValue, propertyName, requiredType.getComponentType());
/*     */         } 
/* 174 */         if (convertedValue instanceof Collection) {
/*     */           
/* 176 */           convertedValue = (Object<?>)convertToTypedCollection((Collection)convertedValue, propertyName, requiredType, typeDescriptor);
/*     */           
/* 178 */           standardConversion = true;
/*     */         }
/* 180 */         else if (convertedValue instanceof Map) {
/*     */           
/* 182 */           convertedValue = (Object<?>)convertToTypedMap((Map)convertedValue, propertyName, requiredType, typeDescriptor);
/*     */           
/* 184 */           standardConversion = true;
/*     */         } 
/* 186 */         if (convertedValue.getClass().isArray() && Array.getLength(convertedValue) == 1) {
/* 187 */           convertedValue = (Object<?>)Array.get(convertedValue, 0);
/* 188 */           standardConversion = true;
/*     */         } 
/* 190 */         if (String.class == requiredType && ClassUtils.isPrimitiveOrWrapper(convertedValue.getClass()))
/*     */         {
/* 192 */           return (T)convertedValue.toString();
/*     */         }
/* 194 */         if (convertedValue instanceof String && !requiredType.isInstance(convertedValue)) {
/* 195 */           if (conversionAttemptEx == null && !requiredType.isInterface() && !requiredType.isEnum()) {
/*     */             try {
/* 197 */               Constructor<T> strCtor = requiredType.getConstructor(new Class[] { String.class });
/* 198 */               return BeanUtils.instantiateClass(strCtor, new Object[] { convertedValue });
/*     */             }
/* 200 */             catch (NoSuchMethodException ex) {
/*     */               
/* 202 */               if (logger.isTraceEnabled()) {
/* 203 */                 logger.trace("No String constructor found on type [" + requiredType.getName() + "]", ex);
/*     */               }
/*     */             }
/* 206 */             catch (Exception ex) {
/* 207 */               if (logger.isDebugEnabled()) {
/* 208 */                 logger.debug("Construction via String failed for type [" + requiredType.getName() + "]", ex);
/*     */               }
/*     */             } 
/*     */           }
/* 212 */           String trimmedValue = ((String)convertedValue).trim();
/* 213 */           if (requiredType.isEnum() && trimmedValue.isEmpty())
/*     */           {
/* 215 */             return null;
/*     */           }
/* 217 */           convertedValue = (Object<?>)attemptToConvertStringToEnum(requiredType, trimmedValue, convertedValue);
/* 218 */           standardConversion = true;
/*     */         }
/* 220 */         else if (convertedValue instanceof Number && Number.class.isAssignableFrom(requiredType)) {
/* 221 */           convertedValue = (Object<?>)NumberUtils.convertNumberToTargetClass((Number)convertedValue, requiredType);
/*     */           
/* 223 */           standardConversion = true;
/*     */         
/*     */         }
/*     */       
/*     */       }
/* 228 */       else if (requiredType == Optional.class) {
/* 229 */         convertedValue = Optional.empty();
/*     */       } 
/*     */ 
/*     */       
/* 233 */       if (!ClassUtils.isAssignableValue(requiredType, convertedValue)) {
/* 234 */         if (conversionAttemptEx != null)
/*     */         {
/* 236 */           throw conversionAttemptEx;
/*     */         }
/* 238 */         if (conversionService != null && typeDescriptor != null) {
/*     */ 
/*     */           
/* 241 */           TypeDescriptor sourceTypeDesc = TypeDescriptor.forObject(newValue);
/* 242 */           if (conversionService.canConvert(sourceTypeDesc, typeDescriptor)) {
/* 243 */             return (T)conversionService.convert(newValue, sourceTypeDesc, typeDescriptor);
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/* 248 */         StringBuilder msg = new StringBuilder();
/* 249 */         msg.append("Cannot convert value of type '").append(ClassUtils.getDescriptiveType(newValue));
/* 250 */         msg.append("' to required type '").append(ClassUtils.getQualifiedName(requiredType)).append("'");
/* 251 */         if (propertyName != null) {
/* 252 */           msg.append(" for property '").append(propertyName).append("'");
/*     */         }
/* 254 */         if (editor != null) {
/* 255 */           msg.append(": PropertyEditor [").append(editor.getClass().getName()).append("] returned inappropriate value of type '")
/* 256 */             .append(
/* 257 */               ClassUtils.getDescriptiveType(convertedValue)).append("'");
/* 258 */           throw new IllegalArgumentException(msg.toString());
/*     */         } 
/*     */         
/* 261 */         msg.append(": no matching editors or conversion strategy found");
/* 262 */         throw new IllegalStateException(msg.toString());
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 267 */     if (conversionAttemptEx != null) {
/* 268 */       if (editor == null && !standardConversion && requiredType != null && Object.class != requiredType) {
/* 269 */         throw conversionAttemptEx;
/*     */       }
/* 271 */       logger.debug("Original ConversionService attempt failed - ignored since PropertyEditor based conversion eventually succeeded", (Throwable)conversionAttemptEx);
/*     */     } 
/*     */ 
/*     */     
/* 275 */     return (T)convertedValue;
/*     */   }
/*     */   
/*     */   private Object attemptToConvertStringToEnum(Class<?> requiredType, String trimmedValue, Object currentConvertedValue) {
/* 279 */     Object convertedValue = currentConvertedValue;
/*     */     
/* 281 */     if (Enum.class == requiredType && this.targetObject != null) {
/*     */       
/* 283 */       int index = trimmedValue.lastIndexOf('.');
/* 284 */       if (index > -1) {
/* 285 */         String enumType = trimmedValue.substring(0, index);
/* 286 */         String fieldName = trimmedValue.substring(index + 1);
/* 287 */         ClassLoader cl = this.targetObject.getClass().getClassLoader();
/*     */         try {
/* 289 */           Class<?> enumValueType = ClassUtils.forName(enumType, cl);
/* 290 */           Field enumField = enumValueType.getField(fieldName);
/* 291 */           convertedValue = enumField.get(null);
/*     */         }
/* 293 */         catch (ClassNotFoundException ex) {
/* 294 */           if (logger.isTraceEnabled()) {
/* 295 */             logger.trace("Enum class [" + enumType + "] cannot be loaded", ex);
/*     */           }
/*     */         }
/* 298 */         catch (Throwable ex) {
/* 299 */           if (logger.isTraceEnabled()) {
/* 300 */             logger.trace("Field [" + fieldName + "] isn't an enum value for type [" + enumType + "]", ex);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 306 */     if (convertedValue == currentConvertedValue) {
/*     */       
/*     */       try {
/*     */ 
/*     */         
/* 311 */         Field enumField = requiredType.getField(trimmedValue);
/* 312 */         ReflectionUtils.makeAccessible(enumField);
/* 313 */         convertedValue = enumField.get(null);
/*     */       }
/* 315 */       catch (Throwable ex) {
/* 316 */         if (logger.isTraceEnabled()) {
/* 317 */           logger.trace("Field [" + convertedValue + "] isn't an enum value", ex);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 322 */     return convertedValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private PropertyEditor findDefaultEditor(@Nullable Class<?> requiredType) {
/* 331 */     PropertyEditor editor = null;
/* 332 */     if (requiredType != null) {
/*     */       
/* 334 */       editor = this.propertyEditorRegistry.getDefaultEditor(requiredType);
/* 335 */       if (editor == null && String.class != requiredType)
/*     */       {
/* 337 */         editor = BeanUtils.findEditorByConvention(requiredType);
/*     */       }
/*     */     } 
/* 340 */     return editor;
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
/*     */   private Object doConvertValue(@Nullable Object oldValue, @Nullable Object newValue, @Nullable Class<?> requiredType, @Nullable PropertyEditor editor) {
/* 358 */     Object convertedValue = newValue;
/*     */     
/* 360 */     if (editor != null && !(convertedValue instanceof String)) {
/*     */       
/*     */       try {
/*     */ 
/*     */ 
/*     */         
/* 366 */         editor.setValue(convertedValue);
/* 367 */         Object newConvertedValue = editor.getValue();
/* 368 */         if (newConvertedValue != convertedValue) {
/* 369 */           convertedValue = newConvertedValue;
/*     */ 
/*     */           
/* 372 */           editor = null;
/*     */         }
/*     */       
/* 375 */       } catch (Exception ex) {
/* 376 */         if (logger.isDebugEnabled()) {
/* 377 */           logger.debug("PropertyEditor [" + editor.getClass().getName() + "] does not support setValue call", ex);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 383 */     Object returnValue = convertedValue;
/*     */     
/* 385 */     if (requiredType != null && !requiredType.isArray() && convertedValue instanceof String[]) {
/*     */ 
/*     */ 
/*     */       
/* 389 */       if (logger.isTraceEnabled()) {
/* 390 */         logger.trace("Converting String array to comma-delimited String [" + convertedValue + "]");
/*     */       }
/* 392 */       convertedValue = StringUtils.arrayToCommaDelimitedString((Object[])convertedValue);
/*     */     } 
/*     */     
/* 395 */     if (convertedValue instanceof String) {
/* 396 */       if (editor != null) {
/*     */         
/* 398 */         if (logger.isTraceEnabled()) {
/* 399 */           logger.trace("Converting String to [" + requiredType + "] using property editor [" + editor + "]");
/*     */         }
/* 401 */         String newTextValue = (String)convertedValue;
/* 402 */         return doConvertTextValue(oldValue, newTextValue, editor);
/*     */       } 
/* 404 */       if (String.class == requiredType) {
/* 405 */         returnValue = convertedValue;
/*     */       }
/*     */     } 
/*     */     
/* 409 */     return returnValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object doConvertTextValue(@Nullable Object oldValue, String newTextValue, PropertyEditor editor) {
/*     */     try {
/* 421 */       editor.setValue(oldValue);
/*     */     }
/* 423 */     catch (Exception ex) {
/* 424 */       if (logger.isDebugEnabled()) {
/* 425 */         logger.debug("PropertyEditor [" + editor.getClass().getName() + "] does not support setValue call", ex);
/*     */       }
/*     */     } 
/*     */     
/* 429 */     editor.setAsText(newTextValue);
/* 430 */     return editor.getValue();
/*     */   }
/*     */   
/*     */   private Object convertToTypedArray(Object input, @Nullable String propertyName, Class<?> componentType) {
/* 434 */     if (input instanceof Collection) {
/*     */       
/* 436 */       Collection<?> coll = (Collection)input;
/* 437 */       Object object = Array.newInstance(componentType, coll.size());
/* 438 */       int i = 0;
/* 439 */       for (Iterator<?> it = coll.iterator(); it.hasNext(); i++) {
/* 440 */         Object object1 = convertIfNecessary(
/* 441 */             buildIndexedPropertyName(propertyName, i), null, it.next(), componentType);
/* 442 */         Array.set(object, i, object1);
/*     */       } 
/* 444 */       return object;
/*     */     } 
/* 446 */     if (input.getClass().isArray()) {
/*     */       
/* 448 */       if (componentType.equals(input.getClass().getComponentType()) && 
/* 449 */         !this.propertyEditorRegistry.hasCustomEditorForElement(componentType, propertyName)) {
/* 450 */         return input;
/*     */       }
/* 452 */       int arrayLength = Array.getLength(input);
/* 453 */       Object object = Array.newInstance(componentType, arrayLength);
/* 454 */       for (int i = 0; i < arrayLength; i++) {
/* 455 */         Object object1 = convertIfNecessary(
/* 456 */             buildIndexedPropertyName(propertyName, i), null, Array.get(input, i), componentType);
/* 457 */         Array.set(object, i, object1);
/*     */       } 
/* 459 */       return object;
/*     */     } 
/*     */ 
/*     */     
/* 463 */     Object result = Array.newInstance(componentType, 1);
/* 464 */     Object value = convertIfNecessary(
/* 465 */         buildIndexedPropertyName(propertyName, 0), null, input, componentType);
/* 466 */     Array.set(result, 0, value);
/* 467 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Collection<?> convertToTypedCollection(Collection<?> original, @Nullable String propertyName, Class<?> requiredType, @Nullable TypeDescriptor typeDescriptor) {
/*     */     Iterator<?> it;
/*     */     Collection<Object> convertedCopy;
/* 475 */     if (!Collection.class.isAssignableFrom(requiredType)) {
/* 476 */       return original;
/*     */     }
/*     */     
/* 479 */     boolean approximable = CollectionFactory.isApproximableCollectionType(requiredType);
/* 480 */     if (!approximable && !canCreateCopy(requiredType)) {
/* 481 */       if (logger.isDebugEnabled()) {
/* 482 */         logger.debug("Custom Collection type [" + original.getClass().getName() + "] does not allow for creating a copy - injecting original Collection as-is");
/*     */       }
/*     */       
/* 485 */       return original;
/*     */     } 
/*     */     
/* 488 */     boolean originalAllowed = requiredType.isInstance(original);
/* 489 */     TypeDescriptor elementType = (typeDescriptor != null) ? typeDescriptor.getElementTypeDescriptor() : null;
/* 490 */     if (elementType == null && originalAllowed && 
/* 491 */       !this.propertyEditorRegistry.hasCustomEditorForElement(null, propertyName)) {
/* 492 */       return original;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 497 */       it = original.iterator();
/*     */     }
/* 499 */     catch (Throwable ex) {
/* 500 */       if (logger.isDebugEnabled()) {
/* 501 */         logger.debug("Cannot access Collection of type [" + original.getClass().getName() + "] - injecting original Collection as-is: " + ex);
/*     */       }
/*     */       
/* 504 */       return original;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 509 */       if (approximable) {
/* 510 */         convertedCopy = CollectionFactory.createApproximateCollection(original, original.size());
/*     */       }
/*     */       else {
/*     */         
/* 514 */         convertedCopy = ReflectionUtils.accessibleConstructor(requiredType, new Class[0]).newInstance(new Object[0]);
/*     */       }
/*     */     
/* 517 */     } catch (Throwable ex) {
/* 518 */       if (logger.isDebugEnabled()) {
/* 519 */         logger.debug("Cannot create copy of Collection type [" + original.getClass().getName() + "] - injecting original Collection as-is: " + ex);
/*     */       }
/*     */       
/* 522 */       return original;
/*     */     } 
/*     */     
/* 525 */     for (int i = 0; it.hasNext(); i++) {
/* 526 */       Object element = it.next();
/* 527 */       String indexedPropertyName = buildIndexedPropertyName(propertyName, i);
/* 528 */       Object convertedElement = convertIfNecessary(indexedPropertyName, null, element, (elementType != null) ? elementType
/* 529 */           .getType() : null, elementType);
/*     */       try {
/* 531 */         convertedCopy.add(convertedElement);
/*     */       }
/* 533 */       catch (Throwable ex) {
/* 534 */         if (logger.isDebugEnabled()) {
/* 535 */           logger.debug("Collection type [" + original.getClass().getName() + "] seems to be read-only - injecting original Collection as-is: " + ex);
/*     */         }
/*     */         
/* 538 */         return original;
/*     */       } 
/* 540 */       originalAllowed = (originalAllowed && element == convertedElement);
/*     */     } 
/* 542 */     return originalAllowed ? original : convertedCopy;
/*     */   }
/*     */ 
/*     */   
/*     */   private Map<?, ?> convertToTypedMap(Map<?, ?> original, @Nullable String propertyName, Class<?> requiredType, @Nullable TypeDescriptor typeDescriptor) {
/*     */     Iterator<?> it;
/*     */     Map<Object, Object> convertedCopy;
/* 549 */     if (!Map.class.isAssignableFrom(requiredType)) {
/* 550 */       return original;
/*     */     }
/*     */     
/* 553 */     boolean approximable = CollectionFactory.isApproximableMapType(requiredType);
/* 554 */     if (!approximable && !canCreateCopy(requiredType)) {
/* 555 */       if (logger.isDebugEnabled()) {
/* 556 */         logger.debug("Custom Map type [" + original.getClass().getName() + "] does not allow for creating a copy - injecting original Map as-is");
/*     */       }
/*     */       
/* 559 */       return original;
/*     */     } 
/*     */     
/* 562 */     boolean originalAllowed = requiredType.isInstance(original);
/* 563 */     TypeDescriptor keyType = (typeDescriptor != null) ? typeDescriptor.getMapKeyTypeDescriptor() : null;
/* 564 */     TypeDescriptor valueType = (typeDescriptor != null) ? typeDescriptor.getMapValueTypeDescriptor() : null;
/* 565 */     if (keyType == null && valueType == null && originalAllowed && 
/* 566 */       !this.propertyEditorRegistry.hasCustomEditorForElement(null, propertyName)) {
/* 567 */       return original;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 572 */       it = original.entrySet().iterator();
/*     */     }
/* 574 */     catch (Throwable ex) {
/* 575 */       if (logger.isDebugEnabled()) {
/* 576 */         logger.debug("Cannot access Map of type [" + original.getClass().getName() + "] - injecting original Map as-is: " + ex);
/*     */       }
/*     */       
/* 579 */       return original;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 584 */       if (approximable) {
/* 585 */         convertedCopy = CollectionFactory.createApproximateMap(original, original.size());
/*     */       }
/*     */       else {
/*     */         
/* 589 */         convertedCopy = ReflectionUtils.accessibleConstructor(requiredType, new Class[0]).newInstance(new Object[0]);
/*     */       }
/*     */     
/* 592 */     } catch (Throwable ex) {
/* 593 */       if (logger.isDebugEnabled()) {
/* 594 */         logger.debug("Cannot create copy of Map type [" + original.getClass().getName() + "] - injecting original Map as-is: " + ex);
/*     */       }
/*     */       
/* 597 */       return original;
/*     */     } 
/*     */     
/* 600 */     while (it.hasNext()) {
/* 601 */       Map.Entry<?, ?> entry = (Map.Entry<?, ?>)it.next();
/* 602 */       Object key = entry.getKey();
/* 603 */       Object value = entry.getValue();
/* 604 */       String keyedPropertyName = buildKeyedPropertyName(propertyName, key);
/* 605 */       Object convertedKey = convertIfNecessary(keyedPropertyName, null, key, (keyType != null) ? keyType
/* 606 */           .getType() : null, keyType);
/* 607 */       Object convertedValue = convertIfNecessary(keyedPropertyName, null, value, (valueType != null) ? valueType
/* 608 */           .getType() : null, valueType);
/*     */       try {
/* 610 */         convertedCopy.put(convertedKey, convertedValue);
/*     */       }
/* 612 */       catch (Throwable ex) {
/* 613 */         if (logger.isDebugEnabled()) {
/* 614 */           logger.debug("Map type [" + original.getClass().getName() + "] seems to be read-only - injecting original Map as-is: " + ex);
/*     */         }
/*     */         
/* 617 */         return original;
/*     */       } 
/* 619 */       originalAllowed = (originalAllowed && key == convertedKey && value == convertedValue);
/*     */     } 
/* 621 */     return originalAllowed ? original : convertedCopy;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private String buildIndexedPropertyName(@Nullable String propertyName, int index) {
/* 626 */     return (propertyName != null) ? (propertyName + "[" + index + "]") : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String buildKeyedPropertyName(@Nullable String propertyName, Object key) {
/* 633 */     return (propertyName != null) ? (propertyName + "[" + key + "]") : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean canCreateCopy(Class<?> requiredType) {
/* 639 */     return (!requiredType.isInterface() && !Modifier.isAbstract(requiredType.getModifiers()) && 
/* 640 */       Modifier.isPublic(requiredType.getModifiers()) && ClassUtils.hasConstructor(requiredType, new Class[0]));
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/TypeConverterDelegate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */