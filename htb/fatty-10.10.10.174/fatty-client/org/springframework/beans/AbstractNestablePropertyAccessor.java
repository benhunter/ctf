/*      */ package org.springframework.beans;
/*      */ 
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.apache.commons.logging.LogFactory;
/*      */ import org.springframework.core.CollectionFactory;
/*      */ import org.springframework.core.ResolvableType;
/*      */ import org.springframework.core.convert.ConversionException;
/*      */ import org.springframework.core.convert.ConverterNotFoundException;
/*      */ import org.springframework.core.convert.TypeDescriptor;
/*      */ import org.springframework.lang.Nullable;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ObjectUtils;
/*      */ import org.springframework.util.StringUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class AbstractNestablePropertyAccessor
/*      */   extends AbstractPropertyAccessor
/*      */ {
/*   77 */   private static final Log logger = LogFactory.getLog(AbstractNestablePropertyAccessor.class);
/*      */   
/*   79 */   private int autoGrowCollectionLimit = Integer.MAX_VALUE;
/*      */   
/*      */   @Nullable
/*      */   Object wrappedObject;
/*      */   
/*   84 */   private String nestedPath = "";
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   Object rootObject;
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private Map<String, AbstractNestablePropertyAccessor> nestedPropertyAccessors;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractNestablePropertyAccessor() {
/*  100 */     this(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractNestablePropertyAccessor(boolean registerDefaultEditors) {
/*  110 */     if (registerDefaultEditors) {
/*  111 */       registerDefaultEditors();
/*      */     }
/*  113 */     this.typeConverterDelegate = new TypeConverterDelegate(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractNestablePropertyAccessor(Object object) {
/*  121 */     registerDefaultEditors();
/*  122 */     setWrappedInstance(object);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractNestablePropertyAccessor(Class<?> clazz) {
/*  130 */     registerDefaultEditors();
/*  131 */     setWrappedInstance(BeanUtils.instantiateClass(clazz));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractNestablePropertyAccessor(Object object, String nestedPath, Object rootObject) {
/*  142 */     registerDefaultEditors();
/*  143 */     setWrappedInstance(object, nestedPath, rootObject);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractNestablePropertyAccessor(Object object, String nestedPath, AbstractNestablePropertyAccessor parent) {
/*  154 */     setWrappedInstance(object, nestedPath, parent.getWrappedInstance());
/*  155 */     setExtractOldValueForEditor(parent.isExtractOldValueForEditor());
/*  156 */     setAutoGrowNestedPaths(parent.isAutoGrowNestedPaths());
/*  157 */     setAutoGrowCollectionLimit(parent.getAutoGrowCollectionLimit());
/*  158 */     setConversionService(parent.getConversionService());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAutoGrowCollectionLimit(int autoGrowCollectionLimit) {
/*  167 */     this.autoGrowCollectionLimit = autoGrowCollectionLimit;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getAutoGrowCollectionLimit() {
/*  174 */     return this.autoGrowCollectionLimit;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setWrappedInstance(Object object) {
/*  183 */     setWrappedInstance(object, "", (Object)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setWrappedInstance(Object object, @Nullable String nestedPath, @Nullable Object rootObject) {
/*  194 */     this.wrappedObject = ObjectUtils.unwrapOptional(object);
/*  195 */     Assert.notNull(this.wrappedObject, "Target object must not be null");
/*  196 */     this.nestedPath = (nestedPath != null) ? nestedPath : "";
/*  197 */     this.rootObject = !this.nestedPath.isEmpty() ? rootObject : this.wrappedObject;
/*  198 */     this.nestedPropertyAccessors = null;
/*  199 */     this.typeConverterDelegate = new TypeConverterDelegate(this, this.wrappedObject);
/*      */   }
/*      */   
/*      */   public final Object getWrappedInstance() {
/*  203 */     Assert.state((this.wrappedObject != null), "No wrapped object");
/*  204 */     return this.wrappedObject;
/*      */   }
/*      */   
/*      */   public final Class<?> getWrappedClass() {
/*  208 */     return getWrappedInstance().getClass();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getNestedPath() {
/*  215 */     return this.nestedPath;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Object getRootInstance() {
/*  223 */     Assert.state((this.rootObject != null), "No root object");
/*  224 */     return this.rootObject;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Class<?> getRootClass() {
/*  232 */     return getRootInstance().getClass();
/*      */   }
/*      */ 
/*      */   
/*      */   public void setPropertyValue(String propertyName, @Nullable Object value) throws BeansException {
/*      */     AbstractNestablePropertyAccessor nestedPa;
/*      */     try {
/*  239 */       nestedPa = getPropertyAccessorForPropertyPath(propertyName);
/*      */     }
/*  241 */     catch (NotReadablePropertyException ex) {
/*  242 */       throw new NotWritablePropertyException(getRootClass(), this.nestedPath + propertyName, "Nested property in path '" + propertyName + "' does not exist", ex);
/*      */     } 
/*      */     
/*  245 */     PropertyTokenHolder tokens = getPropertyNameTokens(getFinalPath(nestedPa, propertyName));
/*  246 */     nestedPa.setPropertyValue(tokens, new PropertyValue(propertyName, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public void setPropertyValue(PropertyValue pv) throws BeansException {
/*  251 */     PropertyTokenHolder tokens = (PropertyTokenHolder)pv.resolvedTokens;
/*  252 */     if (tokens == null) {
/*  253 */       AbstractNestablePropertyAccessor nestedPa; String propertyName = pv.getName();
/*      */       
/*      */       try {
/*  256 */         nestedPa = getPropertyAccessorForPropertyPath(propertyName);
/*      */       }
/*  258 */       catch (NotReadablePropertyException ex) {
/*  259 */         throw new NotWritablePropertyException(getRootClass(), this.nestedPath + propertyName, "Nested property in path '" + propertyName + "' does not exist", ex);
/*      */       } 
/*      */       
/*  262 */       tokens = getPropertyNameTokens(getFinalPath(nestedPa, propertyName));
/*  263 */       if (nestedPa == this) {
/*  264 */         (pv.getOriginalPropertyValue()).resolvedTokens = tokens;
/*      */       }
/*  266 */       nestedPa.setPropertyValue(tokens, pv);
/*      */     } else {
/*      */       
/*  269 */       setPropertyValue(tokens, pv);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void setPropertyValue(PropertyTokenHolder tokens, PropertyValue pv) throws BeansException {
/*  274 */     if (tokens.keys != null) {
/*  275 */       processKeyedProperty(tokens, pv);
/*      */     } else {
/*      */       
/*  278 */       processLocalProperty(tokens, pv);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void processKeyedProperty(PropertyTokenHolder tokens, PropertyValue pv) {
/*  284 */     Object propValue = getPropertyHoldingValue(tokens);
/*  285 */     PropertyHandler ph = getLocalPropertyHandler(tokens.actualName);
/*  286 */     if (ph == null) {
/*  287 */       throw new InvalidPropertyException(
/*  288 */           getRootClass(), this.nestedPath + tokens.actualName, "No property handler found");
/*      */     }
/*  290 */     Assert.state((tokens.keys != null), "No token keys");
/*  291 */     String lastKey = tokens.keys[tokens.keys.length - 1];
/*      */     
/*  293 */     if (propValue.getClass().isArray()) {
/*  294 */       Class<?> requiredType = propValue.getClass().getComponentType();
/*  295 */       int arrayIndex = Integer.parseInt(lastKey);
/*  296 */       Object oldValue = null;
/*      */       try {
/*  298 */         if (isExtractOldValueForEditor() && arrayIndex < Array.getLength(propValue)) {
/*  299 */           oldValue = Array.get(propValue, arrayIndex);
/*      */         }
/*  301 */         Object convertedValue = convertIfNecessary(tokens.canonicalName, oldValue, pv.getValue(), requiredType, ph
/*  302 */             .nested(tokens.keys.length));
/*  303 */         int length = Array.getLength(propValue);
/*  304 */         if (arrayIndex >= length && arrayIndex < this.autoGrowCollectionLimit) {
/*  305 */           Class<?> componentType = propValue.getClass().getComponentType();
/*  306 */           Object newArray = Array.newInstance(componentType, arrayIndex + 1);
/*  307 */           System.arraycopy(propValue, 0, newArray, 0, length);
/*  308 */           setPropertyValue(tokens.actualName, newArray);
/*  309 */           propValue = getPropertyValue(tokens.actualName);
/*      */         } 
/*  311 */         Array.set(propValue, arrayIndex, convertedValue);
/*      */       }
/*  313 */       catch (IndexOutOfBoundsException ex) {
/*  314 */         throw new InvalidPropertyException(getRootClass(), this.nestedPath + tokens.canonicalName, "Invalid array index in property path '" + tokens.canonicalName + "'", ex);
/*      */       
/*      */       }
/*      */     
/*      */     }
/*  319 */     else if (propValue instanceof List) {
/*  320 */       Class<?> requiredType = ph.getCollectionType(tokens.keys.length);
/*  321 */       List<Object> list = (List<Object>)propValue;
/*  322 */       int index = Integer.parseInt(lastKey);
/*  323 */       Object oldValue = null;
/*  324 */       if (isExtractOldValueForEditor() && index < list.size()) {
/*  325 */         oldValue = list.get(index);
/*      */       }
/*  327 */       Object convertedValue = convertIfNecessary(tokens.canonicalName, oldValue, pv.getValue(), requiredType, ph
/*  328 */           .nested(tokens.keys.length));
/*  329 */       int size = list.size();
/*  330 */       if (index >= size && index < this.autoGrowCollectionLimit) {
/*  331 */         for (int i = size; i < index; i++) {
/*      */           try {
/*  333 */             list.add(null);
/*      */           }
/*  335 */           catch (NullPointerException ex) {
/*  336 */             throw new InvalidPropertyException(getRootClass(), this.nestedPath + tokens.canonicalName, "Cannot set element with index " + index + " in List of size " + size + ", accessed using property path '" + tokens.canonicalName + "': List does not support filling up gaps with null elements");
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*  342 */         list.add(convertedValue);
/*      */       } else {
/*      */         
/*      */         try {
/*  346 */           list.set(index, convertedValue);
/*      */         }
/*  348 */         catch (IndexOutOfBoundsException ex) {
/*  349 */           throw new InvalidPropertyException(getRootClass(), this.nestedPath + tokens.canonicalName, "Invalid list index in property path '" + tokens.canonicalName + "'", ex);
/*      */         }
/*      */       
/*      */       }
/*      */     
/*      */     }
/*  355 */     else if (propValue instanceof Map) {
/*  356 */       Class<?> mapKeyType = ph.getMapKeyType(tokens.keys.length);
/*  357 */       Class<?> mapValueType = ph.getMapValueType(tokens.keys.length);
/*  358 */       Map<Object, Object> map = (Map<Object, Object>)propValue;
/*      */ 
/*      */       
/*  361 */       TypeDescriptor typeDescriptor = TypeDescriptor.valueOf(mapKeyType);
/*  362 */       Object convertedMapKey = convertIfNecessary((String)null, (Object)null, lastKey, mapKeyType, typeDescriptor);
/*  363 */       Object oldValue = null;
/*  364 */       if (isExtractOldValueForEditor()) {
/*  365 */         oldValue = map.get(convertedMapKey);
/*      */       }
/*      */ 
/*      */       
/*  369 */       Object convertedMapValue = convertIfNecessary(tokens.canonicalName, oldValue, pv.getValue(), mapValueType, ph
/*  370 */           .nested(tokens.keys.length));
/*  371 */       map.put(convertedMapKey, convertedMapValue);
/*      */     }
/*      */     else {
/*      */       
/*  375 */       throw new InvalidPropertyException(getRootClass(), this.nestedPath + tokens.canonicalName, "Property referenced in indexed property path '" + tokens.canonicalName + "' is neither an array nor a List nor a Map; returned value was [" + propValue + "]");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Object getPropertyHoldingValue(PropertyTokenHolder tokens) {
/*      */     Object propValue;
/*  383 */     Assert.state((tokens.keys != null), "No token keys");
/*  384 */     PropertyTokenHolder getterTokens = new PropertyTokenHolder(tokens.actualName);
/*  385 */     getterTokens.canonicalName = tokens.canonicalName;
/*  386 */     getterTokens.keys = new String[tokens.keys.length - 1];
/*  387 */     System.arraycopy(tokens.keys, 0, getterTokens.keys, 0, tokens.keys.length - 1);
/*      */ 
/*      */     
/*      */     try {
/*  391 */       propValue = getPropertyValue(getterTokens);
/*      */     }
/*  393 */     catch (NotReadablePropertyException ex) {
/*  394 */       throw new NotWritablePropertyException(getRootClass(), this.nestedPath + tokens.canonicalName, "Cannot access indexed value in property referenced in indexed property path '" + tokens.canonicalName + "'", ex);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  399 */     if (propValue == null)
/*      */     {
/*  401 */       if (isAutoGrowNestedPaths()) {
/*  402 */         int lastKeyIndex = tokens.canonicalName.lastIndexOf('[');
/*  403 */         getterTokens.canonicalName = tokens.canonicalName.substring(0, lastKeyIndex);
/*  404 */         propValue = setDefaultValue(getterTokens);
/*      */       } else {
/*      */         
/*  407 */         throw new NullValueInNestedPathException(getRootClass(), this.nestedPath + tokens.canonicalName, "Cannot access indexed value in property referenced in indexed property path '" + tokens.canonicalName + "': returned null");
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  412 */     return propValue;
/*      */   }
/*      */   
/*      */   private void processLocalProperty(PropertyTokenHolder tokens, PropertyValue pv) {
/*  416 */     PropertyHandler ph = getLocalPropertyHandler(tokens.actualName);
/*  417 */     if (ph == null || !ph.isWritable()) {
/*  418 */       if (pv.isOptional()) {
/*  419 */         if (logger.isDebugEnabled()) {
/*  420 */           logger.debug("Ignoring optional value for property '" + tokens.actualName + "' - property not found on bean class [" + 
/*  421 */               getRootClass().getName() + "]");
/*      */         }
/*      */         
/*      */         return;
/*      */       } 
/*  426 */       throw createNotWritablePropertyException(tokens.canonicalName);
/*      */     } 
/*      */ 
/*      */     
/*  430 */     Object oldValue = null;
/*      */     try {
/*  432 */       Object originalValue = pv.getValue();
/*  433 */       Object valueToApply = originalValue;
/*  434 */       if (!Boolean.FALSE.equals(pv.conversionNecessary)) {
/*  435 */         if (pv.isConverted()) {
/*  436 */           valueToApply = pv.getConvertedValue();
/*      */         } else {
/*      */           
/*  439 */           if (isExtractOldValueForEditor() && ph.isReadable()) {
/*      */             try {
/*  441 */               oldValue = ph.getValue();
/*      */             }
/*  443 */             catch (Exception ex) {
/*  444 */               if (ex instanceof PrivilegedActionException) {
/*  445 */                 ex = ((PrivilegedActionException)ex).getException();
/*      */               }
/*  447 */               if (logger.isDebugEnabled()) {
/*  448 */                 logger.debug("Could not read previous value of property '" + this.nestedPath + tokens.canonicalName + "'", ex);
/*      */               }
/*      */             } 
/*      */           }
/*      */           
/*  453 */           valueToApply = convertForProperty(tokens.canonicalName, oldValue, originalValue, ph
/*  454 */               .toTypeDescriptor());
/*      */         } 
/*  456 */         (pv.getOriginalPropertyValue()).conversionNecessary = Boolean.valueOf((valueToApply != originalValue));
/*      */       } 
/*  458 */       ph.setValue(valueToApply);
/*      */     }
/*  460 */     catch (TypeMismatchException ex) {
/*  461 */       throw ex;
/*      */     }
/*  463 */     catch (InvocationTargetException ex) {
/*      */       
/*  465 */       PropertyChangeEvent propertyChangeEvent = new PropertyChangeEvent(getRootInstance(), this.nestedPath + tokens.canonicalName, oldValue, pv.getValue());
/*  466 */       if (ex.getTargetException() instanceof ClassCastException) {
/*  467 */         throw new TypeMismatchException(propertyChangeEvent, ph.getPropertyType(), ex.getTargetException());
/*      */       }
/*      */       
/*  470 */       Throwable cause = ex.getTargetException();
/*  471 */       if (cause instanceof java.lang.reflect.UndeclaredThrowableException)
/*      */       {
/*  473 */         cause = cause.getCause();
/*      */       }
/*  475 */       throw new MethodInvocationException(propertyChangeEvent, cause);
/*      */     
/*      */     }
/*  478 */     catch (Exception ex) {
/*      */       
/*  480 */       PropertyChangeEvent pce = new PropertyChangeEvent(getRootInstance(), this.nestedPath + tokens.canonicalName, oldValue, pv.getValue());
/*  481 */       throw new MethodInvocationException(pce, ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Class<?> getPropertyType(String propertyName) throws BeansException {
/*      */     try {
/*  489 */       PropertyHandler ph = getPropertyHandler(propertyName);
/*  490 */       if (ph != null) {
/*  491 */         return ph.getPropertyType();
/*      */       }
/*      */ 
/*      */       
/*  495 */       Object value = getPropertyValue(propertyName);
/*  496 */       if (value != null) {
/*  497 */         return value.getClass();
/*      */       }
/*      */ 
/*      */       
/*  501 */       Class<?> editorType = guessPropertyTypeFromEditors(propertyName);
/*  502 */       if (editorType != null) {
/*  503 */         return editorType;
/*      */       
/*      */       }
/*      */     }
/*  507 */     catch (InvalidPropertyException invalidPropertyException) {}
/*      */ 
/*      */     
/*  510 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public TypeDescriptor getPropertyTypeDescriptor(String propertyName) throws BeansException {
/*      */     try {
/*  517 */       AbstractNestablePropertyAccessor nestedPa = getPropertyAccessorForPropertyPath(propertyName);
/*  518 */       String finalPath = getFinalPath(nestedPa, propertyName);
/*  519 */       PropertyTokenHolder tokens = getPropertyNameTokens(finalPath);
/*  520 */       PropertyHandler ph = nestedPa.getLocalPropertyHandler(tokens.actualName);
/*  521 */       if (ph != null) {
/*  522 */         if (tokens.keys != null) {
/*  523 */           if (ph.isReadable() || ph.isWritable()) {
/*  524 */             return ph.nested(tokens.keys.length);
/*      */           
/*      */           }
/*      */         }
/*  528 */         else if (ph.isReadable() || ph.isWritable()) {
/*  529 */           return ph.toTypeDescriptor();
/*      */         }
/*      */       
/*      */       }
/*      */     }
/*  534 */     catch (InvalidPropertyException invalidPropertyException) {}
/*      */ 
/*      */     
/*  537 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isReadableProperty(String propertyName) {
/*      */     try {
/*  543 */       PropertyHandler ph = getPropertyHandler(propertyName);
/*  544 */       if (ph != null) {
/*  545 */         return ph.isReadable();
/*      */       }
/*      */ 
/*      */       
/*  549 */       getPropertyValue(propertyName);
/*  550 */       return true;
/*      */     
/*      */     }
/*  553 */     catch (InvalidPropertyException invalidPropertyException) {
/*      */ 
/*      */       
/*  556 */       return false;
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean isWritableProperty(String propertyName) {
/*      */     try {
/*  562 */       PropertyHandler ph = getPropertyHandler(propertyName);
/*  563 */       if (ph != null) {
/*  564 */         return ph.isWritable();
/*      */       }
/*      */ 
/*      */       
/*  568 */       getPropertyValue(propertyName);
/*  569 */       return true;
/*      */     
/*      */     }
/*  572 */     catch (InvalidPropertyException invalidPropertyException) {
/*      */ 
/*      */       
/*  575 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private Object convertIfNecessary(@Nullable String propertyName, @Nullable Object oldValue, @Nullable Object newValue, @Nullable Class<?> requiredType, @Nullable TypeDescriptor td) throws TypeMismatchException {
/*  583 */     Assert.state((this.typeConverterDelegate != null), "No TypeConverterDelegate");
/*      */     try {
/*  585 */       return this.typeConverterDelegate.convertIfNecessary(propertyName, oldValue, newValue, requiredType, td);
/*      */     }
/*  587 */     catch (ConverterNotFoundException|IllegalStateException ex) {
/*      */       
/*  589 */       PropertyChangeEvent pce = new PropertyChangeEvent(getRootInstance(), this.nestedPath + propertyName, oldValue, newValue);
/*  590 */       throw new ConversionNotSupportedException(pce, requiredType, ex);
/*      */     }
/*  592 */     catch (ConversionException|IllegalArgumentException ex) {
/*      */       
/*  594 */       PropertyChangeEvent pce = new PropertyChangeEvent(getRootInstance(), this.nestedPath + propertyName, oldValue, newValue);
/*  595 */       throw new TypeMismatchException(pce, requiredType, ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected Object convertForProperty(String propertyName, @Nullable Object oldValue, @Nullable Object newValue, TypeDescriptor td) throws TypeMismatchException {
/*  604 */     return convertIfNecessary(propertyName, oldValue, newValue, td.getType(), td);
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Object getPropertyValue(String propertyName) throws BeansException {
/*  610 */     AbstractNestablePropertyAccessor nestedPa = getPropertyAccessorForPropertyPath(propertyName);
/*  611 */     PropertyTokenHolder tokens = getPropertyNameTokens(getFinalPath(nestedPa, propertyName));
/*  612 */     return nestedPa.getPropertyValue(tokens);
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected Object getPropertyValue(PropertyTokenHolder tokens) throws BeansException {
/*  618 */     String propertyName = tokens.canonicalName;
/*  619 */     String actualName = tokens.actualName;
/*  620 */     PropertyHandler ph = getLocalPropertyHandler(actualName);
/*  621 */     if (ph == null || !ph.isReadable()) {
/*  622 */       throw new NotReadablePropertyException(getRootClass(), this.nestedPath + propertyName);
/*      */     }
/*      */     try {
/*  625 */       Object value = ph.getValue();
/*  626 */       if (tokens.keys != null) {
/*  627 */         if (value == null) {
/*  628 */           if (isAutoGrowNestedPaths()) {
/*  629 */             value = setDefaultValue(new PropertyTokenHolder(tokens.actualName));
/*      */           } else {
/*      */             
/*  632 */             throw new NullValueInNestedPathException(getRootClass(), this.nestedPath + propertyName, "Cannot access indexed value of property referenced in indexed property path '" + propertyName + "': returned null");
/*      */           } 
/*      */         }
/*      */ 
/*      */         
/*  637 */         StringBuilder indexedPropertyName = new StringBuilder(tokens.actualName);
/*      */         
/*  639 */         for (int i = 0; i < tokens.keys.length; i++) {
/*  640 */           String key = tokens.keys[i];
/*  641 */           if (value == null) {
/*  642 */             throw new NullValueInNestedPathException(getRootClass(), this.nestedPath + propertyName, "Cannot access indexed value of property referenced in indexed property path '" + propertyName + "': returned null");
/*      */           }
/*      */ 
/*      */           
/*  646 */           if (value.getClass().isArray()) {
/*  647 */             int index = Integer.parseInt(key);
/*  648 */             value = growArrayIfNecessary(value, index, indexedPropertyName.toString());
/*  649 */             value = Array.get(value, index);
/*      */           }
/*  651 */           else if (value instanceof List) {
/*  652 */             int index = Integer.parseInt(key);
/*  653 */             List<Object> list = (List<Object>)value;
/*  654 */             growCollectionIfNecessary(list, index, indexedPropertyName.toString(), ph, i + 1);
/*  655 */             value = list.get(index);
/*      */           }
/*  657 */           else if (value instanceof Set) {
/*      */             
/*  659 */             Set<Object> set = (Set<Object>)value;
/*  660 */             int index = Integer.parseInt(key);
/*  661 */             if (index < 0 || index >= set.size()) {
/*  662 */               throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, "Cannot get element with index " + index + " from Set of size " + set
/*      */                   
/*  664 */                   .size() + ", accessed using property path '" + propertyName + "'");
/*      */             }
/*  666 */             Iterator<Object> it = set.iterator();
/*  667 */             for (int j = 0; it.hasNext(); j++) {
/*  668 */               Object elem = it.next();
/*  669 */               if (j == index) {
/*  670 */                 value = elem;
/*      */                 
/*      */                 break;
/*      */               } 
/*      */             } 
/*  675 */           } else if (value instanceof Map) {
/*  676 */             Map<Object, Object> map = (Map<Object, Object>)value;
/*  677 */             Class<?> mapKeyType = ph.getResolvableType().getNested(i + 1).asMap().resolveGeneric(new int[] { 0 });
/*      */ 
/*      */             
/*  680 */             TypeDescriptor typeDescriptor = TypeDescriptor.valueOf(mapKeyType);
/*  681 */             Object convertedMapKey = convertIfNecessary((String)null, (Object)null, key, mapKeyType, typeDescriptor);
/*  682 */             value = map.get(convertedMapKey);
/*      */           } else {
/*      */             
/*  685 */             throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, "Property referenced in indexed property path '" + propertyName + "' is neither an array nor a List nor a Set nor a Map; returned value was [" + value + "]");
/*      */           } 
/*      */ 
/*      */           
/*  689 */           indexedPropertyName.append("[").append(key).append("]");
/*      */         } 
/*      */       } 
/*  692 */       return value;
/*      */     }
/*  694 */     catch (IndexOutOfBoundsException ex) {
/*  695 */       throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, "Index of out of bounds in property path '" + propertyName + "'", ex);
/*      */     
/*      */     }
/*  698 */     catch (NumberFormatException|TypeMismatchException ex) {
/*  699 */       throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, "Invalid index in property path '" + propertyName + "'", ex);
/*      */     
/*      */     }
/*  702 */     catch (InvocationTargetException ex) {
/*  703 */       throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, "Getter for property '" + actualName + "' threw exception", ex);
/*      */     
/*      */     }
/*  706 */     catch (Exception ex) {
/*  707 */       throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, "Illegal attempt to get property '" + actualName + "' threw exception", ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected PropertyHandler getPropertyHandler(String propertyName) throws BeansException {
/*  723 */     Assert.notNull(propertyName, "Property name must not be null");
/*  724 */     AbstractNestablePropertyAccessor nestedPa = getPropertyAccessorForPropertyPath(propertyName);
/*  725 */     return nestedPa.getLocalPropertyHandler(getFinalPath(nestedPa, propertyName));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected abstract PropertyHandler getLocalPropertyHandler(String paramString);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract AbstractNestablePropertyAccessor newNestedPropertyAccessor(Object paramObject, String paramString);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract NotWritablePropertyException createNotWritablePropertyException(String paramString);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Object growArrayIfNecessary(Object array, int index, String name) {
/*  753 */     if (!isAutoGrowNestedPaths()) {
/*  754 */       return array;
/*      */     }
/*  756 */     int length = Array.getLength(array);
/*  757 */     if (index >= length && index < this.autoGrowCollectionLimit) {
/*  758 */       Class<?> componentType = array.getClass().getComponentType();
/*  759 */       Object newArray = Array.newInstance(componentType, index + 1);
/*  760 */       System.arraycopy(array, 0, newArray, 0, length);
/*  761 */       for (int i = length; i < Array.getLength(newArray); i++) {
/*  762 */         Array.set(newArray, i, newValue(componentType, (TypeDescriptor)null, name));
/*      */       }
/*  764 */       setPropertyValue(name, newArray);
/*  765 */       Object defaultValue = getPropertyValue(name);
/*  766 */       Assert.state((defaultValue != null), "Default value must not be null");
/*  767 */       return defaultValue;
/*      */     } 
/*      */     
/*  770 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void growCollectionIfNecessary(Collection<Object> collection, int index, String name, PropertyHandler ph, int nestingLevel) {
/*  777 */     if (!isAutoGrowNestedPaths()) {
/*      */       return;
/*      */     }
/*  780 */     int size = collection.size();
/*  781 */     if (index >= size && index < this.autoGrowCollectionLimit) {
/*  782 */       Class<?> elementType = ph.getResolvableType().getNested(nestingLevel).asCollection().resolveGeneric(new int[0]);
/*  783 */       if (elementType != null) {
/*  784 */         for (int i = collection.size(); i < index + 1; i++) {
/*  785 */           collection.add(newValue(elementType, (TypeDescriptor)null, name));
/*      */         }
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getFinalPath(AbstractNestablePropertyAccessor pa, String nestedPath) {
/*  798 */     if (pa == this) {
/*  799 */       return nestedPath;
/*      */     }
/*  801 */     return nestedPath.substring(PropertyAccessorUtils.getLastNestedPropertySeparatorIndex(nestedPath) + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractNestablePropertyAccessor getPropertyAccessorForPropertyPath(String propertyPath) {
/*  811 */     int pos = PropertyAccessorUtils.getFirstNestedPropertySeparatorIndex(propertyPath);
/*      */     
/*  813 */     if (pos > -1) {
/*  814 */       String nestedProperty = propertyPath.substring(0, pos);
/*  815 */       String nestedPath = propertyPath.substring(pos + 1);
/*  816 */       AbstractNestablePropertyAccessor nestedPa = getNestedPropertyAccessor(nestedProperty);
/*  817 */       return nestedPa.getPropertyAccessorForPropertyPath(nestedPath);
/*      */     } 
/*      */     
/*  820 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private AbstractNestablePropertyAccessor getNestedPropertyAccessor(String nestedProperty) {
/*  833 */     if (this.nestedPropertyAccessors == null) {
/*  834 */       this.nestedPropertyAccessors = new HashMap<>();
/*      */     }
/*      */     
/*  837 */     PropertyTokenHolder tokens = getPropertyNameTokens(nestedProperty);
/*  838 */     String canonicalName = tokens.canonicalName;
/*  839 */     Object value = getPropertyValue(tokens);
/*  840 */     if (value == null || (value instanceof Optional && !((Optional)value).isPresent())) {
/*  841 */       if (isAutoGrowNestedPaths()) {
/*  842 */         value = setDefaultValue(tokens);
/*      */       } else {
/*      */         
/*  845 */         throw new NullValueInNestedPathException(getRootClass(), this.nestedPath + canonicalName);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  850 */     AbstractNestablePropertyAccessor nestedPa = this.nestedPropertyAccessors.get(canonicalName);
/*  851 */     if (nestedPa == null || nestedPa.getWrappedInstance() != ObjectUtils.unwrapOptional(value)) {
/*  852 */       if (logger.isTraceEnabled()) {
/*  853 */         logger.trace("Creating new nested " + getClass().getSimpleName() + " for property '" + canonicalName + "'");
/*      */       }
/*  855 */       nestedPa = newNestedPropertyAccessor(value, this.nestedPath + canonicalName + ".");
/*      */       
/*  857 */       copyDefaultEditorsTo(nestedPa);
/*  858 */       copyCustomEditorsTo(nestedPa, canonicalName);
/*  859 */       this.nestedPropertyAccessors.put(canonicalName, nestedPa);
/*      */     
/*      */     }
/*  862 */     else if (logger.isTraceEnabled()) {
/*  863 */       logger.trace("Using cached nested property accessor for property '" + canonicalName + "'");
/*      */     } 
/*      */     
/*  866 */     return nestedPa;
/*      */   }
/*      */   
/*      */   private Object setDefaultValue(PropertyTokenHolder tokens) {
/*  870 */     PropertyValue pv = createDefaultPropertyValue(tokens);
/*  871 */     setPropertyValue(tokens, pv);
/*  872 */     Object defaultValue = getPropertyValue(tokens);
/*  873 */     Assert.state((defaultValue != null), "Default value must not be null");
/*  874 */     return defaultValue;
/*      */   }
/*      */   
/*      */   private PropertyValue createDefaultPropertyValue(PropertyTokenHolder tokens) {
/*  878 */     TypeDescriptor desc = getPropertyTypeDescriptor(tokens.canonicalName);
/*  879 */     if (desc == null) {
/*  880 */       throw new NullValueInNestedPathException(getRootClass(), this.nestedPath + tokens.canonicalName, "Could not determine property type for auto-growing a default value");
/*      */     }
/*      */     
/*  883 */     Object defaultValue = newValue(desc.getType(), desc, tokens.canonicalName);
/*  884 */     return new PropertyValue(tokens.canonicalName, defaultValue);
/*      */   }
/*      */   
/*      */   private Object newValue(Class<?> type, @Nullable TypeDescriptor desc, String name) {
/*      */     try {
/*  889 */       if (type.isArray()) {
/*  890 */         Class<?> componentType = type.getComponentType();
/*      */         
/*  892 */         if (componentType.isArray()) {
/*  893 */           Object array = Array.newInstance(componentType, 1);
/*  894 */           Array.set(array, 0, Array.newInstance(componentType.getComponentType(), 0));
/*  895 */           return array;
/*      */         } 
/*      */         
/*  898 */         return Array.newInstance(componentType, 0);
/*      */       } 
/*      */       
/*  901 */       if (Collection.class.isAssignableFrom(type)) {
/*  902 */         TypeDescriptor elementDesc = (desc != null) ? desc.getElementTypeDescriptor() : null;
/*  903 */         return CollectionFactory.createCollection(type, (elementDesc != null) ? elementDesc.getType() : null, 16);
/*      */       } 
/*  905 */       if (Map.class.isAssignableFrom(type)) {
/*  906 */         TypeDescriptor keyDesc = (desc != null) ? desc.getMapKeyTypeDescriptor() : null;
/*  907 */         return CollectionFactory.createMap(type, (keyDesc != null) ? keyDesc.getType() : null, 16);
/*      */       } 
/*      */       
/*  910 */       Constructor<?> ctor = type.getDeclaredConstructor(new Class[0]);
/*  911 */       if (Modifier.isPrivate(ctor.getModifiers())) {
/*  912 */         throw new IllegalAccessException("Auto-growing not allowed with private constructor: " + ctor);
/*      */       }
/*  914 */       return BeanUtils.instantiateClass(ctor, new Object[0]);
/*      */     
/*      */     }
/*  917 */     catch (Throwable ex) {
/*  918 */       throw new NullValueInNestedPathException(getRootClass(), this.nestedPath + name, "Could not instantiate property type [" + type
/*  919 */           .getName() + "] to auto-grow nested property path", ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private PropertyTokenHolder getPropertyNameTokens(String propertyName) {
/*  929 */     String actualName = null;
/*  930 */     List<String> keys = new ArrayList<>(2);
/*  931 */     int searchIndex = 0;
/*  932 */     while (searchIndex != -1) {
/*  933 */       int keyStart = propertyName.indexOf("[", searchIndex);
/*  934 */       searchIndex = -1;
/*  935 */       if (keyStart != -1) {
/*  936 */         int keyEnd = propertyName.indexOf("]", keyStart + "[".length());
/*  937 */         if (keyEnd != -1) {
/*  938 */           if (actualName == null) {
/*  939 */             actualName = propertyName.substring(0, keyStart);
/*      */           }
/*  941 */           String key = propertyName.substring(keyStart + "[".length(), keyEnd);
/*  942 */           if ((key.length() > 1 && key.startsWith("'") && key.endsWith("'")) || (key
/*  943 */             .startsWith("\"") && key.endsWith("\""))) {
/*  944 */             key = key.substring(1, key.length() - 1);
/*      */           }
/*  946 */           keys.add(key);
/*  947 */           searchIndex = keyEnd + "]".length();
/*      */         } 
/*      */       } 
/*      */     } 
/*  951 */     PropertyTokenHolder tokens = new PropertyTokenHolder((actualName != null) ? actualName : propertyName);
/*  952 */     if (!keys.isEmpty()) {
/*  953 */       tokens
/*  954 */         .canonicalName = tokens.canonicalName + "[" + StringUtils.collectionToDelimitedString(keys, "][") + "]";
/*      */       
/*  956 */       tokens.keys = StringUtils.toStringArray(keys);
/*      */     } 
/*  958 */     return tokens;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  964 */     StringBuilder sb = new StringBuilder(getClass().getName());
/*  965 */     if (this.wrappedObject != null) {
/*  966 */       sb.append(": wrapping object [").append(ObjectUtils.identityToString(this.wrappedObject)).append("]");
/*      */     } else {
/*      */       
/*  969 */       sb.append(": no wrapped object set");
/*      */     } 
/*  971 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected static abstract class PropertyHandler
/*      */   {
/*      */     private final Class<?> propertyType;
/*      */ 
/*      */     
/*      */     private final boolean readable;
/*      */     
/*      */     private final boolean writable;
/*      */ 
/*      */     
/*      */     public PropertyHandler(Class<?> propertyType, boolean readable, boolean writable) {
/*  987 */       this.propertyType = propertyType;
/*  988 */       this.readable = readable;
/*  989 */       this.writable = writable;
/*      */     }
/*      */     
/*      */     public Class<?> getPropertyType() {
/*  993 */       return this.propertyType;
/*      */     }
/*      */     
/*      */     public boolean isReadable() {
/*  997 */       return this.readable;
/*      */     }
/*      */     
/*      */     public boolean isWritable() {
/* 1001 */       return this.writable;
/*      */     }
/*      */     
/*      */     public abstract TypeDescriptor toTypeDescriptor();
/*      */     
/*      */     public abstract ResolvableType getResolvableType();
/*      */     
/*      */     @Nullable
/*      */     public Class<?> getMapKeyType(int nestingLevel) {
/* 1010 */       return getResolvableType().getNested(nestingLevel).asMap().resolveGeneric(new int[] { 0 });
/*      */     }
/*      */     
/*      */     @Nullable
/*      */     public Class<?> getMapValueType(int nestingLevel) {
/* 1015 */       return getResolvableType().getNested(nestingLevel).asMap().resolveGeneric(new int[] { 1 });
/*      */     }
/*      */     
/*      */     @Nullable
/*      */     public Class<?> getCollectionType(int nestingLevel) {
/* 1020 */       return getResolvableType().getNested(nestingLevel).asCollection().resolveGeneric(new int[0]);
/*      */     }
/*      */     
/*      */     @Nullable
/*      */     public abstract TypeDescriptor nested(int param1Int);
/*      */     
/*      */     @Nullable
/*      */     public abstract Object getValue() throws Exception;
/*      */     
/*      */     public abstract void setValue(@Nullable Object param1Object) throws Exception;
/*      */   }
/*      */   
/*      */   protected static class PropertyTokenHolder {
/*      */     public String actualName;
/*      */     public String canonicalName;
/*      */     @Nullable
/*      */     public String[] keys;
/*      */     
/*      */     public PropertyTokenHolder(String name) {
/* 1039 */       this.actualName = name;
/* 1040 */       this.canonicalName = name;
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/AbstractNestablePropertyAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */