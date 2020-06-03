/*     */ package org.springframework.core.convert;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Stream;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class TypeDescriptor
/*     */   implements Serializable
/*     */ {
/*  52 */   private static final Annotation[] EMPTY_ANNOTATION_ARRAY = new Annotation[0];
/*     */   
/*  54 */   private static final Map<Class<?>, TypeDescriptor> commonTypesCache = new HashMap<>(32);
/*     */   
/*  56 */   private static final Class<?>[] CACHED_COMMON_TYPES = new Class[] { boolean.class, Boolean.class, byte.class, Byte.class, char.class, Character.class, double.class, Double.class, float.class, Float.class, int.class, Integer.class, long.class, Long.class, short.class, Short.class, String.class, Object.class };
/*     */   private final Class<?> type;
/*     */   private final ResolvableType resolvableType;
/*     */   private final AnnotatedElementAdapter annotatedElement;
/*     */   
/*     */   static {
/*  62 */     for (Class<?> preCachedClass : CACHED_COMMON_TYPES) {
/*  63 */       commonTypesCache.put(preCachedClass, valueOf(preCachedClass));
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
/*     */   public TypeDescriptor(MethodParameter methodParameter) {
/*  82 */     this.resolvableType = ResolvableType.forMethodParameter(methodParameter);
/*  83 */     this.type = this.resolvableType.resolve(methodParameter.getNestedParameterType());
/*  84 */     this
/*  85 */       .annotatedElement = new AnnotatedElementAdapter((methodParameter.getParameterIndex() == -1) ? methodParameter.getMethodAnnotations() : methodParameter.getParameterAnnotations());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeDescriptor(Field field) {
/*  94 */     this.resolvableType = ResolvableType.forField(field);
/*  95 */     this.type = this.resolvableType.resolve(field.getType());
/*  96 */     this.annotatedElement = new AnnotatedElementAdapter(field.getAnnotations());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeDescriptor(Property property) {
/* 106 */     Assert.notNull(property, "Property must not be null");
/* 107 */     this.resolvableType = ResolvableType.forMethodParameter(property.getMethodParameter());
/* 108 */     this.type = this.resolvableType.resolve(property.getType());
/* 109 */     this.annotatedElement = new AnnotatedElementAdapter(property.getAnnotations());
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
/*     */   public TypeDescriptor(ResolvableType resolvableType, @Nullable Class<?> type, @Nullable Annotation[] annotations) {
/* 123 */     this.resolvableType = resolvableType;
/* 124 */     this.type = (type != null) ? type : resolvableType.toClass();
/* 125 */     this.annotatedElement = new AnnotatedElementAdapter(annotations);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/* 136 */     return ClassUtils.resolvePrimitiveIfNecessary(getType());
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
/*     */   public Class<?> getType() {
/* 148 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResolvableType getResolvableType() {
/* 156 */     return this.resolvableType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getSource() {
/* 167 */     return this.resolvableType.getSource();
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
/*     */   public TypeDescriptor narrow(@Nullable Object value) {
/* 187 */     if (value == null) {
/* 188 */       return this;
/*     */     }
/* 190 */     ResolvableType narrowed = ResolvableType.forType(value.getClass(), getResolvableType());
/* 191 */     return new TypeDescriptor(narrowed, value.getClass(), getAnnotations());
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
/*     */   @Nullable
/*     */   public TypeDescriptor upcast(@Nullable Class<?> superType) {
/* 204 */     if (superType == null) {
/* 205 */       return null;
/*     */     }
/* 207 */     Assert.isAssignable(superType, getType());
/* 208 */     return new TypeDescriptor(getResolvableType().as(superType), superType, getAnnotations());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 215 */     return ClassUtils.getQualifiedName(getType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPrimitive() {
/* 222 */     return getType().isPrimitive();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Annotation[] getAnnotations() {
/* 230 */     return this.annotatedElement.getAnnotations();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasAnnotation(Class<? extends Annotation> annotationType) {
/* 241 */     if (this.annotatedElement.isEmpty())
/*     */     {
/*     */       
/* 244 */       return false;
/*     */     }
/* 246 */     return AnnotatedElementUtils.isAnnotated(this.annotatedElement, annotationType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
/* 257 */     if (this.annotatedElement.isEmpty())
/*     */     {
/*     */       
/* 260 */       return null;
/*     */     }
/* 262 */     return (T)AnnotatedElementUtils.getMergedAnnotation(this.annotatedElement, annotationType);
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
/*     */   public boolean isAssignableTo(TypeDescriptor typeDescriptor) {
/* 280 */     boolean typesAssignable = typeDescriptor.getObjectType().isAssignableFrom(getObjectType());
/* 281 */     if (!typesAssignable) {
/* 282 */       return false;
/*     */     }
/* 284 */     if (isArray() && typeDescriptor.isArray()) {
/* 285 */       return isNestedAssignable(getElementTypeDescriptor(), typeDescriptor.getElementTypeDescriptor());
/*     */     }
/* 287 */     if (isCollection() && typeDescriptor.isCollection()) {
/* 288 */       return isNestedAssignable(getElementTypeDescriptor(), typeDescriptor.getElementTypeDescriptor());
/*     */     }
/* 290 */     if (isMap() && typeDescriptor.isMap()) {
/* 291 */       return (isNestedAssignable(getMapKeyTypeDescriptor(), typeDescriptor.getMapKeyTypeDescriptor()) && 
/* 292 */         isNestedAssignable(getMapValueTypeDescriptor(), typeDescriptor.getMapValueTypeDescriptor()));
/*     */     }
/*     */     
/* 295 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isNestedAssignable(@Nullable TypeDescriptor nestedTypeDescriptor, @Nullable TypeDescriptor otherNestedTypeDescriptor) {
/* 302 */     return (nestedTypeDescriptor == null || otherNestedTypeDescriptor == null || nestedTypeDescriptor
/* 303 */       .isAssignableTo(otherNestedTypeDescriptor));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCollection() {
/* 310 */     return Collection.class.isAssignableFrom(getType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isArray() {
/* 317 */     return getType().isArray();
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
/*     */   public TypeDescriptor getElementTypeDescriptor() {
/* 331 */     if (getResolvableType().isArray()) {
/* 332 */       return new TypeDescriptor(getResolvableType().getComponentType(), null, getAnnotations());
/*     */     }
/* 334 */     if (Stream.class.isAssignableFrom(getType())) {
/* 335 */       return getRelatedIfResolvable(this, getResolvableType().as(Stream.class).getGeneric(new int[] { 0 }));
/*     */     }
/* 337 */     return getRelatedIfResolvable(this, getResolvableType().asCollection().getGeneric(new int[] { 0 }));
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
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public TypeDescriptor elementTypeDescriptor(Object element) {
/* 360 */     return narrow(element, getElementTypeDescriptor());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMap() {
/* 367 */     return Map.class.isAssignableFrom(getType());
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
/*     */   @Nullable
/*     */   public TypeDescriptor getMapKeyTypeDescriptor() {
/* 380 */     Assert.state(isMap(), "Not a [java.util.Map]");
/* 381 */     return getRelatedIfResolvable(this, getResolvableType().asMap().getGeneric(new int[] { 0 }));
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
/*     */   
/*     */   @Nullable
/*     */   public TypeDescriptor getMapKeyTypeDescriptor(Object mapKey) {
/* 403 */     return narrow(mapKey, getMapKeyTypeDescriptor());
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
/*     */   public TypeDescriptor getMapValueTypeDescriptor() {
/* 417 */     Assert.state(isMap(), "Not a [java.util.Map]");
/* 418 */     return getRelatedIfResolvable(this, getResolvableType().asMap().getGeneric(new int[] { 1 }));
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
/*     */   
/*     */   @Nullable
/*     */   public TypeDescriptor getMapValueTypeDescriptor(Object mapValue) {
/* 440 */     return narrow(mapValue, getMapValueTypeDescriptor());
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private TypeDescriptor narrow(@Nullable Object value, @Nullable TypeDescriptor typeDescriptor) {
/* 445 */     if (typeDescriptor != null) {
/* 446 */       return typeDescriptor.narrow(value);
/*     */     }
/* 448 */     if (value != null) {
/* 449 */       return narrow(value);
/*     */     }
/* 451 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 456 */     if (this == other) {
/* 457 */       return true;
/*     */     }
/* 459 */     if (!(other instanceof TypeDescriptor)) {
/* 460 */       return false;
/*     */     }
/* 462 */     TypeDescriptor otherDesc = (TypeDescriptor)other;
/* 463 */     if (getType() != otherDesc.getType()) {
/* 464 */       return false;
/*     */     }
/* 466 */     if (!annotationsMatch(otherDesc)) {
/* 467 */       return false;
/*     */     }
/* 469 */     if (isCollection() || isArray()) {
/* 470 */       return ObjectUtils.nullSafeEquals(getElementTypeDescriptor(), otherDesc.getElementTypeDescriptor());
/*     */     }
/* 472 */     if (isMap()) {
/* 473 */       return (ObjectUtils.nullSafeEquals(getMapKeyTypeDescriptor(), otherDesc.getMapKeyTypeDescriptor()) && 
/* 474 */         ObjectUtils.nullSafeEquals(getMapValueTypeDescriptor(), otherDesc.getMapValueTypeDescriptor()));
/*     */     }
/*     */     
/* 477 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean annotationsMatch(TypeDescriptor otherDesc) {
/* 482 */     Annotation[] anns = getAnnotations();
/* 483 */     Annotation[] otherAnns = otherDesc.getAnnotations();
/* 484 */     if (anns == otherAnns) {
/* 485 */       return true;
/*     */     }
/* 487 */     if (anns.length != otherAnns.length) {
/* 488 */       return false;
/*     */     }
/* 490 */     if (anns.length > 0) {
/* 491 */       for (int i = 0; i < anns.length; i++) {
/* 492 */         if (!annotationEquals(anns[i], otherAnns[i])) {
/* 493 */           return false;
/*     */         }
/*     */       } 
/*     */     }
/* 497 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean annotationEquals(Annotation ann, Annotation otherAnn) {
/* 502 */     return (ann == otherAnn || (ann.getClass() == otherAnn.getClass() && ann.equals(otherAnn)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 507 */     return getType().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 512 */     StringBuilder builder = new StringBuilder();
/* 513 */     for (Annotation ann : getAnnotations()) {
/* 514 */       builder.append("@").append(ann.annotationType().getName()).append(' ');
/*     */     }
/* 516 */     builder.append(getResolvableType().toString());
/* 517 */     return builder.toString();
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
/*     */   @Nullable
/*     */   public static TypeDescriptor forObject(@Nullable Object source) {
/* 532 */     return (source != null) ? valueOf(source.getClass()) : null;
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
/*     */   public static TypeDescriptor valueOf(@Nullable Class<?> type) {
/* 546 */     if (type == null) {
/* 547 */       type = Object.class;
/*     */     }
/* 549 */     TypeDescriptor desc = commonTypesCache.get(type);
/* 550 */     return (desc != null) ? desc : new TypeDescriptor(ResolvableType.forClass(type), null, null);
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
/*     */   public static TypeDescriptor collection(Class<?> collectionType, @Nullable TypeDescriptor elementTypeDescriptor) {
/* 566 */     Assert.notNull(collectionType, "Collection type must not be null");
/* 567 */     if (!Collection.class.isAssignableFrom(collectionType)) {
/* 568 */       throw new IllegalArgumentException("Collection type must be a [java.util.Collection]");
/*     */     }
/* 570 */     ResolvableType element = (elementTypeDescriptor != null) ? elementTypeDescriptor.resolvableType : null;
/* 571 */     return new TypeDescriptor(ResolvableType.forClassWithGenerics(collectionType, new ResolvableType[] { element }), null, null);
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
/*     */   public static TypeDescriptor map(Class<?> mapType, @Nullable TypeDescriptor keyTypeDescriptor, @Nullable TypeDescriptor valueTypeDescriptor) {
/* 591 */     Assert.notNull(mapType, "Map type must not be null");
/* 592 */     if (!Map.class.isAssignableFrom(mapType)) {
/* 593 */       throw new IllegalArgumentException("Map type must be a [java.util.Map]");
/*     */     }
/* 595 */     ResolvableType key = (keyTypeDescriptor != null) ? keyTypeDescriptor.resolvableType : null;
/* 596 */     ResolvableType value = (valueTypeDescriptor != null) ? valueTypeDescriptor.resolvableType : null;
/* 597 */     return new TypeDescriptor(ResolvableType.forClassWithGenerics(mapType, new ResolvableType[] { key, value }), null, null);
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
/*     */   @Nullable
/*     */   public static TypeDescriptor array(@Nullable TypeDescriptor elementTypeDescriptor) {
/* 612 */     if (elementTypeDescriptor == null) {
/* 613 */       return null;
/*     */     }
/* 615 */     return new TypeDescriptor(ResolvableType.forArrayComponent(elementTypeDescriptor.resolvableType), null, elementTypeDescriptor
/* 616 */         .getAnnotations());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static TypeDescriptor nested(MethodParameter methodParameter, int nestingLevel) {
/* 643 */     if (methodParameter.getNestingLevel() != 1) {
/* 644 */       throw new IllegalArgumentException("MethodParameter nesting level must be 1: use the nestingLevel parameter to specify the desired nestingLevel for nested type traversal");
/*     */     }
/*     */     
/* 647 */     return nested(new TypeDescriptor(methodParameter), nestingLevel);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static TypeDescriptor nested(Field field, int nestingLevel) {
/* 673 */     return nested(new TypeDescriptor(field), nestingLevel);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static TypeDescriptor nested(Property property, int nestingLevel) {
/* 699 */     return nested(new TypeDescriptor(property), nestingLevel);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static TypeDescriptor nested(TypeDescriptor typeDescriptor, int nestingLevel) {
/* 704 */     ResolvableType nested = typeDescriptor.resolvableType;
/* 705 */     for (int i = 0; i < nestingLevel; i++) {
/* 706 */       if (Object.class != nested.getType())
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 711 */         nested = nested.getNested(2);
/*     */       }
/*     */     } 
/* 714 */     if (nested == ResolvableType.NONE) {
/* 715 */       return null;
/*     */     }
/* 717 */     return getRelatedIfResolvable(typeDescriptor, nested);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static TypeDescriptor getRelatedIfResolvable(TypeDescriptor source, ResolvableType type) {
/* 722 */     if (type.resolve() == null) {
/* 723 */       return null;
/*     */     }
/* 725 */     return new TypeDescriptor(type, null, source.getAnnotations());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class AnnotatedElementAdapter
/*     */     implements AnnotatedElement, Serializable
/*     */   {
/*     */     @Nullable
/*     */     private final Annotation[] annotations;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public AnnotatedElementAdapter(Annotation[] annotations) {
/* 741 */       this.annotations = annotations;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
/* 746 */       for (Annotation annotation : getAnnotations()) {
/* 747 */         if (annotation.annotationType() == annotationClass) {
/* 748 */           return true;
/*     */         }
/*     */       } 
/* 751 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
/* 758 */       for (Annotation annotation : getAnnotations()) {
/* 759 */         if (annotation.annotationType() == annotationClass) {
/* 760 */           return (T)annotation;
/*     */         }
/*     */       } 
/* 763 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Annotation[] getAnnotations() {
/* 768 */       return (this.annotations != null) ? this.annotations : TypeDescriptor.EMPTY_ANNOTATION_ARRAY;
/*     */     }
/*     */ 
/*     */     
/*     */     public Annotation[] getDeclaredAnnotations() {
/* 773 */       return getAnnotations();
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 777 */       return ObjectUtils.isEmpty((Object[])this.annotations);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 782 */       return (this == other || (other instanceof AnnotatedElementAdapter && 
/* 783 */         Arrays.equals((Object[])this.annotations, (Object[])((AnnotatedElementAdapter)other).annotations)));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 788 */       return Arrays.hashCode((Object[])this.annotations);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 793 */       return TypeDescriptor.this.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/convert/TypeDescriptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */