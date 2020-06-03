/*    */ package org.springframework.beans.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collections;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import org.springframework.beans.BeanWrapper;
/*    */ import org.springframework.beans.PropertyAccessorFactory;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.ReflectionUtils;
/*    */ import org.springframework.util.StringValueResolver;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AnnotationBeanUtils
/*    */ {
/*    */   public static void copyPropertiesToBean(Annotation ann, Object bean, String... excludedProperties) {
/* 50 */     copyPropertiesToBean(ann, bean, null, excludedProperties);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void copyPropertiesToBean(Annotation ann, Object bean, @Nullable StringValueResolver valueResolver, String... excludedProperties) {
/* 67 */     Set<String> excluded = (excludedProperties.length == 0) ? Collections.<String>emptySet() : new HashSet<>(Arrays.asList(excludedProperties));
/* 68 */     Method[] annotationProperties = ann.annotationType().getDeclaredMethods();
/* 69 */     BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(bean);
/* 70 */     for (Method annotationProperty : annotationProperties) {
/* 71 */       String propertyName = annotationProperty.getName();
/* 72 */       if (!excluded.contains(propertyName) && bw.isWritableProperty(propertyName)) {
/* 73 */         Object value = ReflectionUtils.invokeMethod(annotationProperty, ann);
/* 74 */         if (valueResolver != null && value instanceof String) {
/* 75 */           value = valueResolver.resolveStringValue((String)value);
/*    */         }
/* 77 */         bw.setPropertyValue(propertyName, value);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/annotation/AnnotationBeanUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */