/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.awt.Image;
/*     */ import java.beans.BeanDescriptor;
/*     */ import java.beans.BeanInfo;
/*     */ import java.beans.EventSetDescriptor;
/*     */ import java.beans.IndexedPropertyDescriptor;
/*     */ import java.beans.IntrospectionException;
/*     */ import java.beans.Introspector;
/*     */ import java.beans.MethodDescriptor;
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ExtendedBeanInfo
/*     */   implements BeanInfo
/*     */ {
/*  82 */   private static final Log logger = LogFactory.getLog(ExtendedBeanInfo.class);
/*     */   
/*     */   private final BeanInfo delegate;
/*     */   
/*  86 */   private final Set<PropertyDescriptor> propertyDescriptors = new TreeSet<>(new PropertyDescriptorComparator());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtendedBeanInfo(BeanInfo delegate) {
/* 100 */     this.delegate = delegate;
/* 101 */     for (PropertyDescriptor pd : delegate.getPropertyDescriptors()) {
/*     */       try {
/* 103 */         this.propertyDescriptors.add((pd instanceof IndexedPropertyDescriptor) ? new SimpleIndexedPropertyDescriptor((IndexedPropertyDescriptor)pd) : new SimplePropertyDescriptor(pd));
/*     */ 
/*     */       
/*     */       }
/* 107 */       catch (IntrospectionException ex) {
/*     */         
/* 109 */         if (logger.isDebugEnabled()) {
/* 110 */           logger.debug("Ignoring invalid bean property '" + pd.getName() + "': " + ex.getMessage());
/*     */         }
/*     */       } 
/*     */     } 
/* 114 */     MethodDescriptor[] methodDescriptors = delegate.getMethodDescriptors();
/* 115 */     if (methodDescriptors != null) {
/* 116 */       for (Method method : findCandidateWriteMethods(methodDescriptors)) {
/*     */         try {
/* 118 */           handleCandidateWriteMethod(method);
/*     */         }
/* 120 */         catch (IntrospectionException ex) {
/*     */           
/* 122 */           if (logger.isDebugEnabled()) {
/* 123 */             logger.debug("Ignoring candidate write method [" + method + "]: " + ex.getMessage());
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private List<Method> findCandidateWriteMethods(MethodDescriptor[] methodDescriptors) {
/* 132 */     List<Method> matches = new ArrayList<>();
/* 133 */     for (MethodDescriptor methodDescriptor : methodDescriptors) {
/* 134 */       Method method = methodDescriptor.getMethod();
/* 135 */       if (isCandidateWriteMethod(method)) {
/* 136 */         matches.add(method);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 142 */     matches.sort((m1, m2) -> m2.toString().compareTo(m1.toString()));
/* 143 */     return matches;
/*     */   }
/*     */   
/*     */   public static boolean isCandidateWriteMethod(Method method) {
/* 147 */     String methodName = method.getName();
/* 148 */     Class<?>[] parameterTypes = method.getParameterTypes();
/* 149 */     int nParams = parameterTypes.length;
/* 150 */     return (methodName.length() > 3 && methodName.startsWith("set") && Modifier.isPublic(method.getModifiers()) && (
/* 151 */       !void.class.isAssignableFrom(method.getReturnType()) || Modifier.isStatic(method.getModifiers())) && (nParams == 1 || (nParams == 2 && int.class == parameterTypes[0])));
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleCandidateWriteMethod(Method method) throws IntrospectionException {
/* 156 */     int nParams = method.getParameterCount();
/* 157 */     String propertyName = propertyNameFor(method);
/* 158 */     Class<?> propertyType = method.getParameterTypes()[nParams - 1];
/* 159 */     PropertyDescriptor existingPd = findExistingPropertyDescriptor(propertyName, propertyType);
/* 160 */     if (nParams == 1) {
/* 161 */       if (existingPd == null) {
/* 162 */         this.propertyDescriptors.add(new SimplePropertyDescriptor(propertyName, null, method));
/*     */       } else {
/*     */         
/* 165 */         existingPd.setWriteMethod(method);
/*     */       }
/*     */     
/* 168 */     } else if (nParams == 2) {
/* 169 */       if (existingPd == null) {
/* 170 */         this.propertyDescriptors.add(new SimpleIndexedPropertyDescriptor(propertyName, null, null, null, method));
/*     */       
/*     */       }
/* 173 */       else if (existingPd instanceof IndexedPropertyDescriptor) {
/* 174 */         ((IndexedPropertyDescriptor)existingPd).setIndexedWriteMethod(method);
/*     */       } else {
/*     */         
/* 177 */         this.propertyDescriptors.remove(existingPd);
/* 178 */         this.propertyDescriptors.add(new SimpleIndexedPropertyDescriptor(propertyName, existingPd
/* 179 */               .getReadMethod(), existingPd.getWriteMethod(), null, method));
/*     */       } 
/*     */     } else {
/*     */       
/* 183 */       throw new IllegalArgumentException("Write method must have exactly 1 or 2 parameters: " + method);
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private PropertyDescriptor findExistingPropertyDescriptor(String propertyName, Class<?> propertyType) {
/* 189 */     for (PropertyDescriptor pd : this.propertyDescriptors) {
/*     */       
/* 191 */       String candidateName = pd.getName();
/* 192 */       if (pd instanceof IndexedPropertyDescriptor) {
/* 193 */         IndexedPropertyDescriptor ipd = (IndexedPropertyDescriptor)pd;
/* 194 */         Class<?> clazz = ipd.getIndexedPropertyType();
/* 195 */         if (candidateName.equals(propertyName) && (clazz
/* 196 */           .equals(propertyType) || clazz.equals(propertyType.getComponentType()))) {
/* 197 */           return pd;
/*     */         }
/*     */         continue;
/*     */       } 
/* 201 */       Class<?> candidateType = pd.getPropertyType();
/* 202 */       if (candidateName.equals(propertyName) && (candidateType
/* 203 */         .equals(propertyType) || propertyType.equals(candidateType.getComponentType()))) {
/* 204 */         return pd;
/*     */       }
/*     */     } 
/*     */     
/* 208 */     return null;
/*     */   }
/*     */   
/*     */   private String propertyNameFor(Method method) {
/* 212 */     return Introspector.decapitalize(method.getName().substring(3, method.getName().length()));
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
/*     */   public PropertyDescriptor[] getPropertyDescriptors() {
/* 224 */     return this.propertyDescriptors.<PropertyDescriptor>toArray(new PropertyDescriptor[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   public BeanInfo[] getAdditionalBeanInfo() {
/* 229 */     return this.delegate.getAdditionalBeanInfo();
/*     */   }
/*     */ 
/*     */   
/*     */   public BeanDescriptor getBeanDescriptor() {
/* 234 */     return this.delegate.getBeanDescriptor();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDefaultEventIndex() {
/* 239 */     return this.delegate.getDefaultEventIndex();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDefaultPropertyIndex() {
/* 244 */     return this.delegate.getDefaultPropertyIndex();
/*     */   }
/*     */ 
/*     */   
/*     */   public EventSetDescriptor[] getEventSetDescriptors() {
/* 249 */     return this.delegate.getEventSetDescriptors();
/*     */   }
/*     */ 
/*     */   
/*     */   public Image getIcon(int iconKind) {
/* 254 */     return this.delegate.getIcon(iconKind);
/*     */   }
/*     */ 
/*     */   
/*     */   public MethodDescriptor[] getMethodDescriptors() {
/* 259 */     return this.delegate.getMethodDescriptors();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class SimplePropertyDescriptor
/*     */     extends PropertyDescriptor
/*     */   {
/*     */     @Nullable
/*     */     private Method readMethod;
/*     */     
/*     */     @Nullable
/*     */     private Method writeMethod;
/*     */     
/*     */     @Nullable
/*     */     private Class<?> propertyType;
/*     */     
/*     */     @Nullable
/*     */     private Class<?> propertyEditorClass;
/*     */ 
/*     */     
/*     */     public SimplePropertyDescriptor(PropertyDescriptor original) throws IntrospectionException {
/* 281 */       this(original.getName(), original.getReadMethod(), original.getWriteMethod());
/* 282 */       PropertyDescriptorUtils.copyNonMethodProperties(original, this);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public SimplePropertyDescriptor(String propertyName, @Nullable Method readMethod, Method writeMethod) throws IntrospectionException {
/* 288 */       super(propertyName, null, null);
/* 289 */       this.readMethod = readMethod;
/* 290 */       this.writeMethod = writeMethod;
/* 291 */       this.propertyType = PropertyDescriptorUtils.findPropertyType(readMethod, writeMethod);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Method getReadMethod() {
/* 297 */       return this.readMethod;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setReadMethod(@Nullable Method readMethod) {
/* 302 */       this.readMethod = readMethod;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Method getWriteMethod() {
/* 308 */       return this.writeMethod;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setWriteMethod(@Nullable Method writeMethod) {
/* 313 */       this.writeMethod = writeMethod;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Class<?> getPropertyType() {
/* 319 */       if (this.propertyType == null) {
/*     */         try {
/* 321 */           this.propertyType = PropertyDescriptorUtils.findPropertyType(this.readMethod, this.writeMethod);
/*     */         }
/* 323 */         catch (IntrospectionException introspectionException) {}
/*     */       }
/*     */ 
/*     */       
/* 327 */       return this.propertyType;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Class<?> getPropertyEditorClass() {
/* 333 */       return this.propertyEditorClass;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setPropertyEditorClass(@Nullable Class<?> propertyEditorClass) {
/* 338 */       this.propertyEditorClass = propertyEditorClass;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 343 */       return (this == other || (other instanceof PropertyDescriptor && 
/* 344 */         PropertyDescriptorUtils.equals(this, (PropertyDescriptor)other)));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 349 */       return ObjectUtils.nullSafeHashCode(getReadMethod()) * 29 + ObjectUtils.nullSafeHashCode(getWriteMethod());
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 354 */       return String.format("%s[name=%s, propertyType=%s, readMethod=%s, writeMethod=%s]", new Object[] {
/* 355 */             getClass().getSimpleName(), getName(), getPropertyType(), this.readMethod, this.writeMethod
/*     */           });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class SimpleIndexedPropertyDescriptor
/*     */     extends IndexedPropertyDescriptor
/*     */   {
/*     */     @Nullable
/*     */     private Method readMethod;
/*     */     
/*     */     @Nullable
/*     */     private Method writeMethod;
/*     */     
/*     */     @Nullable
/*     */     private Class<?> propertyType;
/*     */     
/*     */     @Nullable
/*     */     private Method indexedReadMethod;
/*     */     
/*     */     @Nullable
/*     */     private Method indexedWriteMethod;
/*     */     
/*     */     @Nullable
/*     */     private Class<?> indexedPropertyType;
/*     */     
/*     */     @Nullable
/*     */     private Class<?> propertyEditorClass;
/*     */ 
/*     */     
/*     */     public SimpleIndexedPropertyDescriptor(IndexedPropertyDescriptor original) throws IntrospectionException {
/* 387 */       this(original.getName(), original.getReadMethod(), original.getWriteMethod(), original
/* 388 */           .getIndexedReadMethod(), original.getIndexedWriteMethod());
/* 389 */       PropertyDescriptorUtils.copyNonMethodProperties(original, this);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SimpleIndexedPropertyDescriptor(String propertyName, @Nullable Method readMethod, @Nullable Method writeMethod, @Nullable Method indexedReadMethod, Method indexedWriteMethod) throws IntrospectionException {
/* 396 */       super(propertyName, (Method)null, (Method)null, (Method)null, (Method)null);
/* 397 */       this.readMethod = readMethod;
/* 398 */       this.writeMethod = writeMethod;
/* 399 */       this.propertyType = PropertyDescriptorUtils.findPropertyType(readMethod, writeMethod);
/* 400 */       this.indexedReadMethod = indexedReadMethod;
/* 401 */       this.indexedWriteMethod = indexedWriteMethod;
/* 402 */       this.indexedPropertyType = PropertyDescriptorUtils.findIndexedPropertyType(propertyName, this.propertyType, indexedReadMethod, indexedWriteMethod);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Method getReadMethod() {
/* 409 */       return this.readMethod;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setReadMethod(@Nullable Method readMethod) {
/* 414 */       this.readMethod = readMethod;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Method getWriteMethod() {
/* 420 */       return this.writeMethod;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setWriteMethod(@Nullable Method writeMethod) {
/* 425 */       this.writeMethod = writeMethod;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Class<?> getPropertyType() {
/* 431 */       if (this.propertyType == null) {
/*     */         try {
/* 433 */           this.propertyType = PropertyDescriptorUtils.findPropertyType(this.readMethod, this.writeMethod);
/*     */         }
/* 435 */         catch (IntrospectionException introspectionException) {}
/*     */       }
/*     */ 
/*     */       
/* 439 */       return this.propertyType;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Method getIndexedReadMethod() {
/* 445 */       return this.indexedReadMethod;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setIndexedReadMethod(@Nullable Method indexedReadMethod) throws IntrospectionException {
/* 450 */       this.indexedReadMethod = indexedReadMethod;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Method getIndexedWriteMethod() {
/* 456 */       return this.indexedWriteMethod;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setIndexedWriteMethod(@Nullable Method indexedWriteMethod) throws IntrospectionException {
/* 461 */       this.indexedWriteMethod = indexedWriteMethod;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Class<?> getIndexedPropertyType() {
/* 467 */       if (this.indexedPropertyType == null) {
/*     */         try {
/* 469 */           this.indexedPropertyType = PropertyDescriptorUtils.findIndexedPropertyType(
/* 470 */               getName(), getPropertyType(), this.indexedReadMethod, this.indexedWriteMethod);
/*     */         }
/* 472 */         catch (IntrospectionException introspectionException) {}
/*     */       }
/*     */ 
/*     */       
/* 476 */       return this.indexedPropertyType;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Class<?> getPropertyEditorClass() {
/* 482 */       return this.propertyEditorClass;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setPropertyEditorClass(@Nullable Class<?> propertyEditorClass) {
/* 487 */       this.propertyEditorClass = propertyEditorClass;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 495 */       if (this == other) {
/* 496 */         return true;
/*     */       }
/* 498 */       if (!(other instanceof IndexedPropertyDescriptor)) {
/* 499 */         return false;
/*     */       }
/* 501 */       IndexedPropertyDescriptor otherPd = (IndexedPropertyDescriptor)other;
/* 502 */       return (ObjectUtils.nullSafeEquals(getIndexedReadMethod(), otherPd.getIndexedReadMethod()) && 
/* 503 */         ObjectUtils.nullSafeEquals(getIndexedWriteMethod(), otherPd.getIndexedWriteMethod()) && 
/* 504 */         ObjectUtils.nullSafeEquals(getIndexedPropertyType(), otherPd.getIndexedPropertyType()) && 
/* 505 */         PropertyDescriptorUtils.equals(this, otherPd));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 510 */       int hashCode = ObjectUtils.nullSafeHashCode(getReadMethod());
/* 511 */       hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(getWriteMethod());
/* 512 */       hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(getIndexedReadMethod());
/* 513 */       hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(getIndexedWriteMethod());
/* 514 */       return hashCode;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 519 */       return String.format("%s[name=%s, propertyType=%s, indexedPropertyType=%s, readMethod=%s, writeMethod=%s, indexedReadMethod=%s, indexedWriteMethod=%s]", new Object[] {
/*     */             
/* 521 */             getClass().getSimpleName(), getName(), getPropertyType(), getIndexedPropertyType(), this.readMethod, this.writeMethod, this.indexedReadMethod, this.indexedWriteMethod
/*     */           });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class PropertyDescriptorComparator
/*     */     implements Comparator<PropertyDescriptor>
/*     */   {
/*     */     public int compare(PropertyDescriptor desc1, PropertyDescriptor desc2) {
/* 536 */       String left = desc1.getName();
/* 537 */       String right = desc2.getName();
/* 538 */       for (int i = 0; i < left.length(); i++) {
/* 539 */         if (right.length() == i) {
/* 540 */           return 1;
/*     */         }
/* 542 */         int result = left.getBytes()[i] - right.getBytes()[i];
/* 543 */         if (result != 0) {
/* 544 */           return result;
/*     */         }
/*     */       } 
/* 547 */       return left.length() - right.length();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/ExtendedBeanInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */