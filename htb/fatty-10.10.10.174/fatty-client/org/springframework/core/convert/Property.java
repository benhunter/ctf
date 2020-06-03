/*     */ package org.springframework.core.convert;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.core.GenericTypeResolver;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Property
/*     */ {
/*  51 */   private static Map<Property, Annotation[]> annotationCache = (Map<Property, Annotation[]>)new ConcurrentReferenceHashMap();
/*     */   
/*     */   private final Class<?> objectType;
/*     */   
/*     */   @Nullable
/*     */   private final Method readMethod;
/*     */   
/*     */   @Nullable
/*     */   private final Method writeMethod;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final MethodParameter methodParameter;
/*     */   
/*     */   @Nullable
/*     */   private Annotation[] annotations;
/*     */ 
/*     */   
/*     */   public Property(Class<?> objectType, @Nullable Method readMethod, @Nullable Method writeMethod) {
/*  70 */     this(objectType, readMethod, writeMethod, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Property(Class<?> objectType, @Nullable Method readMethod, @Nullable Method writeMethod, @Nullable String name) {
/*  76 */     this.objectType = objectType;
/*  77 */     this.readMethod = readMethod;
/*  78 */     this.writeMethod = writeMethod;
/*  79 */     this.methodParameter = resolveMethodParameter();
/*  80 */     this.name = (name != null) ? name : resolveName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/*  88 */     return this.objectType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  95 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getType() {
/* 102 */     return this.methodParameter.getParameterType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Method getReadMethod() {
/* 110 */     return this.readMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Method getWriteMethod() {
/* 118 */     return this.writeMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   MethodParameter getMethodParameter() {
/* 125 */     return this.methodParameter;
/*     */   }
/*     */   
/*     */   Annotation[] getAnnotations() {
/* 129 */     if (this.annotations == null) {
/* 130 */       this.annotations = resolveAnnotations();
/*     */     }
/* 132 */     return this.annotations;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String resolveName() {
/* 139 */     if (this.readMethod != null) {
/* 140 */       int index = this.readMethod.getName().indexOf("get");
/* 141 */       if (index != -1) {
/* 142 */         index += 3;
/*     */       } else {
/*     */         
/* 145 */         index = this.readMethod.getName().indexOf("is");
/* 146 */         if (index == -1) {
/* 147 */           throw new IllegalArgumentException("Not a getter method");
/*     */         }
/* 149 */         index += 2;
/*     */       } 
/* 151 */       return StringUtils.uncapitalize(this.readMethod.getName().substring(index));
/*     */     } 
/* 153 */     if (this.writeMethod != null) {
/* 154 */       int index = this.writeMethod.getName().indexOf("set");
/* 155 */       if (index == -1) {
/* 156 */         throw new IllegalArgumentException("Not a setter method");
/*     */       }
/* 158 */       index += 3;
/* 159 */       return StringUtils.uncapitalize(this.writeMethod.getName().substring(index));
/*     */     } 
/*     */     
/* 162 */     throw new IllegalStateException("Property is neither readable nor writeable");
/*     */   }
/*     */ 
/*     */   
/*     */   private MethodParameter resolveMethodParameter() {
/* 167 */     MethodParameter read = resolveReadMethodParameter();
/* 168 */     MethodParameter write = resolveWriteMethodParameter();
/* 169 */     if (write == null) {
/* 170 */       if (read == null) {
/* 171 */         throw new IllegalStateException("Property is neither readable nor writeable");
/*     */       }
/* 173 */       return read;
/*     */     } 
/* 175 */     if (read != null) {
/* 176 */       Class<?> readType = read.getParameterType();
/* 177 */       Class<?> writeType = write.getParameterType();
/* 178 */       if (!writeType.equals(readType) && writeType.isAssignableFrom(readType)) {
/* 179 */         return read;
/*     */       }
/*     */     } 
/* 182 */     return write;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private MethodParameter resolveReadMethodParameter() {
/* 187 */     if (getReadMethod() == null) {
/* 188 */       return null;
/*     */     }
/* 190 */     return resolveParameterType(new MethodParameter(getReadMethod(), -1));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private MethodParameter resolveWriteMethodParameter() {
/* 195 */     if (getWriteMethod() == null) {
/* 196 */       return null;
/*     */     }
/* 198 */     return resolveParameterType(new MethodParameter(getWriteMethod(), 0));
/*     */   }
/*     */ 
/*     */   
/*     */   private MethodParameter resolveParameterType(MethodParameter parameter) {
/* 203 */     GenericTypeResolver.resolveParameterType(parameter, getObjectType());
/* 204 */     return parameter;
/*     */   }
/*     */   
/*     */   private Annotation[] resolveAnnotations() {
/* 208 */     Annotation[] annotations = annotationCache.get(this);
/* 209 */     if (annotations == null) {
/* 210 */       Map<Class<? extends Annotation>, Annotation> annotationMap = new LinkedHashMap<>();
/* 211 */       addAnnotationsToMap(annotationMap, getReadMethod());
/* 212 */       addAnnotationsToMap(annotationMap, getWriteMethod());
/* 213 */       addAnnotationsToMap(annotationMap, getField());
/* 214 */       annotations = (Annotation[])annotationMap.values().toArray((Object[])new Annotation[0]);
/* 215 */       annotationCache.put(this, annotations);
/*     */     } 
/* 217 */     return annotations;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void addAnnotationsToMap(Map<Class<? extends Annotation>, Annotation> annotationMap, @Nullable AnnotatedElement object) {
/* 223 */     if (object != null) {
/* 224 */       for (Annotation annotation : object.getAnnotations()) {
/* 225 */         annotationMap.put(annotation.annotationType(), annotation);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Field getField() {
/* 232 */     String name = getName();
/* 233 */     if (!StringUtils.hasLength(name)) {
/* 234 */       return null;
/*     */     }
/* 236 */     Field field = null;
/* 237 */     Class<?> declaringClass = declaringClass();
/* 238 */     if (declaringClass != null) {
/* 239 */       field = ReflectionUtils.findField(declaringClass, name);
/* 240 */       if (field == null) {
/*     */         
/* 242 */         field = ReflectionUtils.findField(declaringClass, StringUtils.uncapitalize(name));
/* 243 */         if (field == null) {
/* 244 */           field = ReflectionUtils.findField(declaringClass, StringUtils.capitalize(name));
/*     */         }
/*     */       } 
/*     */     } 
/* 248 */     return field;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Class<?> declaringClass() {
/* 253 */     if (getReadMethod() != null) {
/* 254 */       return getReadMethod().getDeclaringClass();
/*     */     }
/* 256 */     if (getWriteMethod() != null) {
/* 257 */       return getWriteMethod().getDeclaringClass();
/*     */     }
/*     */     
/* 260 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 267 */     if (this == other) {
/* 268 */       return true;
/*     */     }
/* 270 */     if (!(other instanceof Property)) {
/* 271 */       return false;
/*     */     }
/* 273 */     Property otherProperty = (Property)other;
/* 274 */     return (ObjectUtils.nullSafeEquals(this.objectType, otherProperty.objectType) && 
/* 275 */       ObjectUtils.nullSafeEquals(this.name, otherProperty.name) && 
/* 276 */       ObjectUtils.nullSafeEquals(this.readMethod, otherProperty.readMethod) && 
/* 277 */       ObjectUtils.nullSafeEquals(this.writeMethod, otherProperty.writeMethod));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 282 */     return ObjectUtils.nullSafeHashCode(this.objectType) * 31 + ObjectUtils.nullSafeHashCode(this.name);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/convert/Property.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */