/*     */ package org.springframework.core.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class AnnotationAttributes
/*     */   extends LinkedHashMap<String, Object>
/*     */ {
/*     */   private static final String UNKNOWN = "unknown";
/*     */   @Nullable
/*     */   private final Class<? extends Annotation> annotationType;
/*     */   final String displayName;
/*     */   boolean validated = false;
/*     */   
/*     */   public AnnotationAttributes() {
/*  63 */     this.annotationType = null;
/*  64 */     this.displayName = "unknown";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationAttributes(int initialCapacity) {
/*  73 */     super(initialCapacity);
/*  74 */     this.annotationType = null;
/*  75 */     this.displayName = "unknown";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationAttributes(Map<String, Object> map) {
/*  85 */     super(map);
/*  86 */     this.annotationType = null;
/*  87 */     this.displayName = "unknown";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationAttributes(AnnotationAttributes other) {
/*  97 */     super(other);
/*  98 */     this.annotationType = other.annotationType;
/*  99 */     this.displayName = other.displayName;
/* 100 */     this.validated = other.validated;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationAttributes(Class<? extends Annotation> annotationType) {
/* 111 */     Assert.notNull(annotationType, "'annotationType' must not be null");
/* 112 */     this.annotationType = annotationType;
/* 113 */     this.displayName = annotationType.getName();
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
/*     */   public AnnotationAttributes(String annotationType, @Nullable ClassLoader classLoader) {
/* 126 */     Assert.notNull(annotationType, "'annotationType' must not be null");
/* 127 */     this.annotationType = getAnnotationType(annotationType, classLoader);
/* 128 */     this.displayName = annotationType;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static Class<? extends Annotation> getAnnotationType(String annotationType, @Nullable ClassLoader classLoader) {
/* 134 */     if (classLoader != null) {
/*     */       try {
/* 136 */         return (Class)classLoader.loadClass(annotationType);
/*     */       }
/* 138 */       catch (ClassNotFoundException classNotFoundException) {}
/*     */     }
/*     */ 
/*     */     
/* 142 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<? extends Annotation> annotationType() {
/* 153 */     return this.annotationType;
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
/*     */   public String getString(String attributeName) {
/* 165 */     return getRequiredAttribute(attributeName, String.class);
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
/*     */   public String[] getStringArray(String attributeName) {
/* 181 */     return getRequiredAttribute(attributeName, (Class)String[].class);
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
/*     */   public boolean getBoolean(String attributeName) {
/* 193 */     return ((Boolean)getRequiredAttribute(attributeName, Boolean.class)).booleanValue();
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
/*     */   public <N extends Number> N getNumber(String attributeName) {
/* 206 */     return (N)getRequiredAttribute(attributeName, Number.class);
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
/*     */   public <E extends Enum<?>> E getEnum(String attributeName) {
/* 219 */     return (E)getRequiredAttribute(attributeName, Enum.class);
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
/*     */   public <T> Class<? extends T> getClass(String attributeName) {
/* 232 */     return getRequiredAttribute(attributeName, (Class)Class.class);
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
/*     */   public Class<?>[] getClassArray(String attributeName) {
/* 247 */     return getRequiredAttribute(attributeName, (Class)Class[].class);
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
/*     */   public AnnotationAttributes getAnnotation(String attributeName) {
/* 262 */     return getRequiredAttribute(attributeName, AnnotationAttributes.class);
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
/*     */   public <A extends Annotation> A getAnnotation(String attributeName, Class<A> annotationType) {
/* 277 */     return (A)getRequiredAttribute(attributeName, annotationType);
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
/*     */   public AnnotationAttributes[] getAnnotationArray(String attributeName) {
/* 295 */     return getRequiredAttribute(attributeName, (Class)AnnotationAttributes[].class);
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
/*     */   public <A extends Annotation> A[] getAnnotationArray(String attributeName, Class<A> annotationType) {
/* 314 */     Object array = Array.newInstance(annotationType, 0);
/* 315 */     return (A[])getRequiredAttribute(attributeName, array.getClass());
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
/*     */   private <T> T getRequiredAttribute(String attributeName, Class<T> expectedType) {
/* 335 */     Assert.hasText(attributeName, "'attributeName' must not be null or empty");
/* 336 */     Object value = get(attributeName);
/* 337 */     assertAttributePresence(attributeName, value);
/* 338 */     assertNotException(attributeName, value);
/* 339 */     if (!expectedType.isInstance(value) && expectedType.isArray() && expectedType
/* 340 */       .getComponentType().isInstance(value)) {
/* 341 */       Object array = Array.newInstance(expectedType.getComponentType(), 1);
/* 342 */       Array.set(array, 0, value);
/* 343 */       value = array;
/*     */     } 
/* 345 */     assertAttributeType(attributeName, value, expectedType);
/* 346 */     return (T)value;
/*     */   }
/*     */   
/*     */   private void assertAttributePresence(String attributeName, Object attributeValue) {
/* 350 */     Assert.notNull(attributeValue, () -> String.format("Attribute '%s' not found in attributes for annotation [%s]", new Object[] { attributeName, this.displayName }));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void assertNotException(String attributeName, Object attributeValue) {
/* 356 */     if (attributeValue instanceof Exception) {
/* 357 */       throw new IllegalArgumentException(String.format("Attribute '%s' for annotation [%s] was not resolvable due to exception [%s]", new Object[] { attributeName, this.displayName, attributeValue }), (Exception)attributeValue);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void assertAttributeType(String attributeName, Object attributeValue, Class<?> expectedType) {
/* 364 */     if (!expectedType.isInstance(attributeValue)) {
/* 365 */       throw new IllegalArgumentException(String.format("Attribute '%s' is of type %s, but %s was expected in attributes for annotation [%s]", new Object[] { attributeName, attributeValue
/*     */               
/* 367 */               .getClass().getSimpleName(), expectedType.getSimpleName(), this.displayName }));
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
/*     */   public Object putIfAbsent(String key, Object value) {
/* 385 */     Object obj = get(key);
/* 386 */     if (obj == null) {
/* 387 */       obj = put(key, value);
/*     */     }
/* 389 */     return obj;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 394 */     Iterator<Map.Entry<String, Object>> entries = entrySet().iterator();
/* 395 */     StringBuilder sb = new StringBuilder("{");
/* 396 */     while (entries.hasNext()) {
/* 397 */       Map.Entry<String, Object> entry = entries.next();
/* 398 */       sb.append(entry.getKey());
/* 399 */       sb.append('=');
/* 400 */       sb.append(valueToString(entry.getValue()));
/* 401 */       sb.append(entries.hasNext() ? ", " : "");
/*     */     } 
/* 403 */     sb.append("}");
/* 404 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private String valueToString(Object value) {
/* 408 */     if (value == this) {
/* 409 */       return "(this Map)";
/*     */     }
/* 411 */     if (value instanceof Object[]) {
/* 412 */       return "[" + StringUtils.arrayToDelimitedString((Object[])value, ", ") + "]";
/*     */     }
/* 414 */     return String.valueOf(value);
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
/*     */   @Nullable
/*     */   public static AnnotationAttributes fromMap(@Nullable Map<String, Object> map) {
/* 428 */     if (map == null) {
/* 429 */       return null;
/*     */     }
/* 431 */     if (map instanceof AnnotationAttributes) {
/* 432 */       return (AnnotationAttributes)map;
/*     */     }
/* 434 */     return new AnnotationAttributes(map);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/annotation/AnnotationAttributes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */