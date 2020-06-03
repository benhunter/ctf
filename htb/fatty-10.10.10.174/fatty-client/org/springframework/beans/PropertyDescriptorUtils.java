/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.beans.IntrospectionException;
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Enumeration;
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
/*     */ abstract class PropertyDescriptorUtils
/*     */ {
/*     */   public static void copyNonMethodProperties(PropertyDescriptor source, PropertyDescriptor target) {
/*  39 */     target.setExpert(source.isExpert());
/*  40 */     target.setHidden(source.isHidden());
/*  41 */     target.setPreferred(source.isPreferred());
/*  42 */     target.setName(source.getName());
/*  43 */     target.setShortDescription(source.getShortDescription());
/*  44 */     target.setDisplayName(source.getDisplayName());
/*     */ 
/*     */     
/*  47 */     Enumeration<String> keys = source.attributeNames();
/*  48 */     while (keys.hasMoreElements()) {
/*  49 */       String key = keys.nextElement();
/*  50 */       target.setValue(key, source.getValue(key));
/*     */     } 
/*     */ 
/*     */     
/*  54 */     target.setPropertyEditorClass(source.getPropertyEditorClass());
/*  55 */     target.setBound(source.isBound());
/*  56 */     target.setConstrained(source.isConstrained());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static Class<?> findPropertyType(@Nullable Method readMethod, @Nullable Method writeMethod) throws IntrospectionException {
/*  66 */     Class<?> propertyType = null;
/*     */     
/*  68 */     if (readMethod != null) {
/*  69 */       Class<?>[] params = readMethod.getParameterTypes();
/*  70 */       if (params.length != 0) {
/*  71 */         throw new IntrospectionException("Bad read method arg count: " + readMethod);
/*     */       }
/*  73 */       propertyType = readMethod.getReturnType();
/*  74 */       if (propertyType == void.class) {
/*  75 */         throw new IntrospectionException("Read method returns void: " + readMethod);
/*     */       }
/*     */     } 
/*     */     
/*  79 */     if (writeMethod != null) {
/*  80 */       Class<?>[] params = writeMethod.getParameterTypes();
/*  81 */       if (params.length != 1) {
/*  82 */         throw new IntrospectionException("Bad write method arg count: " + writeMethod);
/*     */       }
/*  84 */       if (propertyType != null) {
/*  85 */         if (propertyType.isAssignableFrom(params[0]))
/*     */         {
/*  87 */           propertyType = params[0];
/*     */         }
/*  89 */         else if (!params[0].isAssignableFrom(propertyType))
/*     */         {
/*     */ 
/*     */           
/*  93 */           throw new IntrospectionException("Type mismatch between read and write methods: " + readMethod + " - " + writeMethod);
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/*  98 */         propertyType = params[0];
/*     */       } 
/*     */     } 
/*     */     
/* 102 */     return propertyType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static Class<?> findIndexedPropertyType(String name, @Nullable Class<?> propertyType, @Nullable Method indexedReadMethod, @Nullable Method indexedWriteMethod) throws IntrospectionException {
/* 112 */     Class<?> indexedPropertyType = null;
/*     */     
/* 114 */     if (indexedReadMethod != null) {
/* 115 */       Class<?>[] params = indexedReadMethod.getParameterTypes();
/* 116 */       if (params.length != 1) {
/* 117 */         throw new IntrospectionException("Bad indexed read method arg count: " + indexedReadMethod);
/*     */       }
/* 119 */       if (params[0] != int.class) {
/* 120 */         throw new IntrospectionException("Non int index to indexed read method: " + indexedReadMethod);
/*     */       }
/* 122 */       indexedPropertyType = indexedReadMethod.getReturnType();
/* 123 */       if (indexedPropertyType == void.class) {
/* 124 */         throw new IntrospectionException("Indexed read method returns void: " + indexedReadMethod);
/*     */       }
/*     */     } 
/*     */     
/* 128 */     if (indexedWriteMethod != null) {
/* 129 */       Class<?>[] params = indexedWriteMethod.getParameterTypes();
/* 130 */       if (params.length != 2) {
/* 131 */         throw new IntrospectionException("Bad indexed write method arg count: " + indexedWriteMethod);
/*     */       }
/* 133 */       if (params[0] != int.class) {
/* 134 */         throw new IntrospectionException("Non int index to indexed write method: " + indexedWriteMethod);
/*     */       }
/* 136 */       if (indexedPropertyType != null) {
/* 137 */         if (indexedPropertyType.isAssignableFrom(params[1]))
/*     */         {
/* 139 */           indexedPropertyType = params[1];
/*     */         }
/* 141 */         else if (!params[1].isAssignableFrom(indexedPropertyType))
/*     */         {
/*     */ 
/*     */           
/* 145 */           throw new IntrospectionException("Type mismatch between indexed read and write methods: " + indexedReadMethod + " - " + indexedWriteMethod);
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 150 */         indexedPropertyType = params[1];
/*     */       } 
/*     */     } 
/*     */     
/* 154 */     if (propertyType != null && (!propertyType.isArray() || propertyType
/* 155 */       .getComponentType() != indexedPropertyType)) {
/* 156 */       throw new IntrospectionException("Type mismatch between indexed and non-indexed methods: " + indexedReadMethod + " - " + indexedWriteMethod);
/*     */     }
/*     */ 
/*     */     
/* 160 */     return indexedPropertyType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean equals(PropertyDescriptor pd, PropertyDescriptor otherPd) {
/* 170 */     return (ObjectUtils.nullSafeEquals(pd.getReadMethod(), otherPd.getReadMethod()) && 
/* 171 */       ObjectUtils.nullSafeEquals(pd.getWriteMethod(), otherPd.getWriteMethod()) && 
/* 172 */       ObjectUtils.nullSafeEquals(pd.getPropertyType(), otherPd.getPropertyType()) && 
/* 173 */       ObjectUtils.nullSafeEquals(pd.getPropertyEditorClass(), otherPd.getPropertyEditorClass()) && pd
/* 174 */       .isBound() == otherPd.isBound() && pd.isConstrained() == otherPd.isConstrained());
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/PropertyDescriptorUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */