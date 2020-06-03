/*     */ package org.springframework.core.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ class SynthesizedAnnotationInvocationHandler
/*     */   implements InvocationHandler
/*     */ {
/*     */   private final AnnotationAttributeExtractor<?> attributeExtractor;
/*  48 */   private final Map<String, Object> valueCache = new ConcurrentHashMap<>(8);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   SynthesizedAnnotationInvocationHandler(AnnotationAttributeExtractor<?> attributeExtractor) {
/*  57 */     Assert.notNull(attributeExtractor, "AnnotationAttributeExtractor must not be null");
/*  58 */     this.attributeExtractor = attributeExtractor;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/*  64 */     if (ReflectionUtils.isEqualsMethod(method)) {
/*  65 */       return Boolean.valueOf(annotationEquals(args[0]));
/*     */     }
/*  67 */     if (ReflectionUtils.isHashCodeMethod(method)) {
/*  68 */       return Integer.valueOf(annotationHashCode());
/*     */     }
/*  70 */     if (ReflectionUtils.isToStringMethod(method)) {
/*  71 */       return annotationToString();
/*     */     }
/*  73 */     if (AnnotationUtils.isAnnotationTypeMethod(method)) {
/*  74 */       return annotationType();
/*     */     }
/*  76 */     if (!AnnotationUtils.isAttributeMethod(method)) {
/*  77 */       throw new AnnotationConfigurationException(String.format("Method [%s] is unsupported for synthesized annotation type [%s]", new Object[] { method, 
/*  78 */               annotationType() }));
/*     */     }
/*  80 */     return getAttributeValue(method);
/*     */   }
/*     */   
/*     */   private Class<? extends Annotation> annotationType() {
/*  84 */     return this.attributeExtractor.getAnnotationType();
/*     */   }
/*     */   
/*     */   private Object getAttributeValue(Method attributeMethod) {
/*  88 */     String attributeName = attributeMethod.getName();
/*  89 */     Object value = this.valueCache.get(attributeName);
/*  90 */     if (value == null) {
/*  91 */       value = this.attributeExtractor.getAttributeValue(attributeMethod);
/*  92 */       if (value == null) {
/*  93 */         String msg = String.format("%s returned null for attribute name [%s] from attribute source [%s]", new Object[] { this.attributeExtractor
/*  94 */               .getClass().getName(), attributeName, this.attributeExtractor.getSource() });
/*  95 */         throw new IllegalStateException(msg);
/*     */       } 
/*     */ 
/*     */       
/*  99 */       if (value instanceof Annotation) {
/* 100 */         value = AnnotationUtils.synthesizeAnnotation((Annotation)value, this.attributeExtractor.getAnnotatedElement());
/*     */       }
/* 102 */       else if (value instanceof Annotation[]) {
/* 103 */         value = AnnotationUtils.synthesizeAnnotationArray((Annotation[])value, this.attributeExtractor.getAnnotatedElement());
/*     */       } 
/*     */       
/* 106 */       this.valueCache.put(attributeName, value);
/*     */     } 
/*     */ 
/*     */     
/* 110 */     if (value.getClass().isArray()) {
/* 111 */       value = cloneArray(value);
/*     */     }
/*     */     
/* 114 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object cloneArray(Object array) {
/* 123 */     if (array instanceof boolean[]) {
/* 124 */       return ((boolean[])array).clone();
/*     */     }
/* 126 */     if (array instanceof byte[]) {
/* 127 */       return ((byte[])array).clone();
/*     */     }
/* 129 */     if (array instanceof char[]) {
/* 130 */       return ((char[])array).clone();
/*     */     }
/* 132 */     if (array instanceof double[]) {
/* 133 */       return ((double[])array).clone();
/*     */     }
/* 135 */     if (array instanceof float[]) {
/* 136 */       return ((float[])array).clone();
/*     */     }
/* 138 */     if (array instanceof int[]) {
/* 139 */       return ((int[])array).clone();
/*     */     }
/* 141 */     if (array instanceof long[]) {
/* 142 */       return ((long[])array).clone();
/*     */     }
/* 144 */     if (array instanceof short[]) {
/* 145 */       return ((short[])array).clone();
/*     */     }
/*     */ 
/*     */     
/* 149 */     return ((Object[])array).clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean annotationEquals(Object other) {
/* 157 */     if (this == other) {
/* 158 */       return true;
/*     */     }
/* 160 */     if (!annotationType().isInstance(other)) {
/* 161 */       return false;
/*     */     }
/*     */     
/* 164 */     for (Method attributeMethod : AnnotationUtils.getAttributeMethods(annotationType())) {
/* 165 */       Object thisValue = getAttributeValue(attributeMethod);
/* 166 */       Object otherValue = ReflectionUtils.invokeMethod(attributeMethod, other);
/* 167 */       if (!ObjectUtils.nullSafeEquals(thisValue, otherValue)) {
/* 168 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 172 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int annotationHashCode() {
/* 179 */     int result = 0;
/*     */     
/* 181 */     for (Method attributeMethod : AnnotationUtils.getAttributeMethods(annotationType())) {
/* 182 */       int hashCode; Object value = getAttributeValue(attributeMethod);
/*     */       
/* 184 */       if (value.getClass().isArray()) {
/* 185 */         hashCode = hashCodeForArray(value);
/*     */       } else {
/*     */         
/* 188 */         hashCode = value.hashCode();
/*     */       } 
/* 190 */       result += 127 * attributeMethod.getName().hashCode() ^ hashCode;
/*     */     } 
/*     */     
/* 193 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int hashCodeForArray(Object array) {
/* 204 */     if (array instanceof boolean[]) {
/* 205 */       return Arrays.hashCode((boolean[])array);
/*     */     }
/* 207 */     if (array instanceof byte[]) {
/* 208 */       return Arrays.hashCode((byte[])array);
/*     */     }
/* 210 */     if (array instanceof char[]) {
/* 211 */       return Arrays.hashCode((char[])array);
/*     */     }
/* 213 */     if (array instanceof double[]) {
/* 214 */       return Arrays.hashCode((double[])array);
/*     */     }
/* 216 */     if (array instanceof float[]) {
/* 217 */       return Arrays.hashCode((float[])array);
/*     */     }
/* 219 */     if (array instanceof int[]) {
/* 220 */       return Arrays.hashCode((int[])array);
/*     */     }
/* 222 */     if (array instanceof long[]) {
/* 223 */       return Arrays.hashCode((long[])array);
/*     */     }
/* 225 */     if (array instanceof short[]) {
/* 226 */       return Arrays.hashCode((short[])array);
/*     */     }
/*     */ 
/*     */     
/* 230 */     return Arrays.hashCode((Object[])array);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String annotationToString() {
/* 237 */     StringBuilder sb = (new StringBuilder("@")).append(annotationType().getName()).append("(");
/*     */     
/* 239 */     Iterator<Method> iterator = AnnotationUtils.getAttributeMethods(annotationType()).iterator();
/* 240 */     while (iterator.hasNext()) {
/* 241 */       Method attributeMethod = iterator.next();
/* 242 */       sb.append(attributeMethod.getName());
/* 243 */       sb.append('=');
/* 244 */       sb.append(attributeValueToString(getAttributeValue(attributeMethod)));
/* 245 */       sb.append(iterator.hasNext() ? ", " : "");
/*     */     } 
/*     */     
/* 248 */     return sb.append(")").toString();
/*     */   }
/*     */   
/*     */   private String attributeValueToString(Object value) {
/* 252 */     if (value instanceof Object[]) {
/* 253 */       return "[" + StringUtils.arrayToDelimitedString((Object[])value, ", ") + "]";
/*     */     }
/* 255 */     return String.valueOf(value);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/annotation/SynthesizedAnnotationInvocationHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */