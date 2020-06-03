/*     */ package org.springframework.core.convert.support;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import org.springframework.core.DecoratingProxy;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.convert.ConversionFailedException;
/*     */ import org.springframework.core.convert.ConverterNotFoundException;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.core.convert.converter.ConditionalConverter;
/*     */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/*     */ import org.springframework.core.convert.converter.Converter;
/*     */ import org.springframework.core.convert.converter.ConverterFactory;
/*     */ import org.springframework.core.convert.converter.GenericConverter;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
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
/*     */ public class GenericConversionService
/*     */   implements ConfigurableConversionService
/*     */ {
/*  68 */   private static final GenericConverter NO_OP_CONVERTER = new NoOpConverter("NO_OP");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  74 */   private static final GenericConverter NO_MATCH = new NoOpConverter("NO_MATCH");
/*     */ 
/*     */   
/*  77 */   private final Converters converters = new Converters();
/*     */   
/*  79 */   private final Map<ConverterCacheKey, GenericConverter> converterCache = (Map<ConverterCacheKey, GenericConverter>)new ConcurrentReferenceHashMap(64);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConverter(Converter<?, ?> converter) {
/*  86 */     ResolvableType[] typeInfo = getRequiredTypeInfo(converter.getClass(), Converter.class);
/*  87 */     if (typeInfo == null && converter instanceof DecoratingProxy) {
/*  88 */       typeInfo = getRequiredTypeInfo(((DecoratingProxy)converter).getDecoratedClass(), Converter.class);
/*     */     }
/*  90 */     if (typeInfo == null) {
/*  91 */       throw new IllegalArgumentException("Unable to determine source type <S> and target type <T> for your Converter [" + converter
/*  92 */           .getClass().getName() + "]; does the class parameterize those types?");
/*     */     }
/*  94 */     addConverter((GenericConverter)new ConverterAdapter(converter, typeInfo[0], typeInfo[1]));
/*     */   }
/*     */ 
/*     */   
/*     */   public <S, T> void addConverter(Class<S> sourceType, Class<T> targetType, Converter<? super S, ? extends T> converter) {
/*  99 */     addConverter((GenericConverter)new ConverterAdapter(converter, 
/* 100 */           ResolvableType.forClass(sourceType), ResolvableType.forClass(targetType)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addConverter(GenericConverter converter) {
/* 105 */     this.converters.add(converter);
/* 106 */     invalidateCache();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addConverterFactory(ConverterFactory<?, ?> factory) {
/* 111 */     ResolvableType[] typeInfo = getRequiredTypeInfo(factory.getClass(), ConverterFactory.class);
/* 112 */     if (typeInfo == null && factory instanceof DecoratingProxy) {
/* 113 */       typeInfo = getRequiredTypeInfo(((DecoratingProxy)factory).getDecoratedClass(), ConverterFactory.class);
/*     */     }
/* 115 */     if (typeInfo == null) {
/* 116 */       throw new IllegalArgumentException("Unable to determine source type <S> and target type <T> for your ConverterFactory [" + factory
/* 117 */           .getClass().getName() + "]; does the class parameterize those types?");
/*     */     }
/* 119 */     addConverter((GenericConverter)new ConverterFactoryAdapter(factory, new GenericConverter.ConvertiblePair(typeInfo[0]
/* 120 */             .toClass(), typeInfo[1].toClass())));
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeConvertible(Class<?> sourceType, Class<?> targetType) {
/* 125 */     this.converters.remove(sourceType, targetType);
/* 126 */     invalidateCache();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canConvert(@Nullable Class<?> sourceType, Class<?> targetType) {
/* 134 */     Assert.notNull(targetType, "Target type to convert to cannot be null");
/* 135 */     return canConvert((sourceType != null) ? TypeDescriptor.valueOf(sourceType) : null, 
/* 136 */         TypeDescriptor.valueOf(targetType));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canConvert(@Nullable TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 141 */     Assert.notNull(targetType, "Target type to convert to cannot be null");
/* 142 */     if (sourceType == null) {
/* 143 */       return true;
/*     */     }
/* 145 */     GenericConverter converter = getConverter(sourceType, targetType);
/* 146 */     return (converter != null);
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
/*     */   public boolean canBypassConvert(@Nullable TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 161 */     Assert.notNull(targetType, "Target type to convert to cannot be null");
/* 162 */     if (sourceType == null) {
/* 163 */       return true;
/*     */     }
/* 165 */     GenericConverter converter = getConverter(sourceType, targetType);
/* 166 */     return (converter == NO_OP_CONVERTER);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T> T convert(@Nullable Object source, Class<T> targetType) {
/* 173 */     Assert.notNull(targetType, "Target type to convert to cannot be null");
/* 174 */     return (T)convert(source, TypeDescriptor.forObject(source), TypeDescriptor.valueOf(targetType));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object convert(@Nullable Object source, @Nullable TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 180 */     Assert.notNull(targetType, "Target type to convert to cannot be null");
/* 181 */     if (sourceType == null) {
/* 182 */       Assert.isTrue((source == null), "Source must be [null] if source type == [null]");
/* 183 */       return handleResult(null, targetType, convertNullSource(null, targetType));
/*     */     } 
/* 185 */     if (source != null && !sourceType.getObjectType().isInstance(source)) {
/* 186 */       throw new IllegalArgumentException("Source to convert from must be an instance of [" + sourceType + "]; instead it was a [" + source
/* 187 */           .getClass().getName() + "]");
/*     */     }
/* 189 */     GenericConverter converter = getConverter(sourceType, targetType);
/* 190 */     if (converter != null) {
/* 191 */       Object result = ConversionUtils.invokeConverter(converter, source, sourceType, targetType);
/* 192 */       return handleResult(sourceType, targetType, result);
/*     */     } 
/* 194 */     return handleConverterNotFound(source, sourceType, targetType);
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
/*     */   public Object convert(@Nullable Object source, TypeDescriptor targetType) {
/* 212 */     return convert(source, TypeDescriptor.forObject(source), targetType);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 217 */     return this.converters.toString();
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
/*     */   protected Object convertNullSource(@Nullable TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 235 */     if (targetType.getObjectType() == Optional.class) {
/* 236 */       return Optional.empty();
/*     */     }
/* 238 */     return null;
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
/*     */   @Nullable
/*     */   protected GenericConverter getConverter(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 254 */     ConverterCacheKey key = new ConverterCacheKey(sourceType, targetType);
/* 255 */     GenericConverter converter = this.converterCache.get(key);
/* 256 */     if (converter != null) {
/* 257 */       return (converter != NO_MATCH) ? converter : null;
/*     */     }
/*     */     
/* 260 */     converter = this.converters.find(sourceType, targetType);
/* 261 */     if (converter == null) {
/* 262 */       converter = getDefaultConverter(sourceType, targetType);
/*     */     }
/*     */     
/* 265 */     if (converter != null) {
/* 266 */       this.converterCache.put(key, converter);
/* 267 */       return converter;
/*     */     } 
/*     */     
/* 270 */     this.converterCache.put(key, NO_MATCH);
/* 271 */     return null;
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
/*     */   protected GenericConverter getDefaultConverter(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 284 */     return sourceType.isAssignableTo(targetType) ? NO_OP_CONVERTER : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ResolvableType[] getRequiredTypeInfo(Class<?> converterClass, Class<?> genericIfc) {
/* 292 */     ResolvableType resolvableType = ResolvableType.forClass(converterClass).as(genericIfc);
/* 293 */     ResolvableType[] generics = resolvableType.getGenerics();
/* 294 */     if (generics.length < 2) {
/* 295 */       return null;
/*     */     }
/* 297 */     Class<?> sourceType = generics[0].resolve();
/* 298 */     Class<?> targetType = generics[1].resolve();
/* 299 */     if (sourceType == null || targetType == null) {
/* 300 */       return null;
/*     */     }
/* 302 */     return generics;
/*     */   }
/*     */   
/*     */   private void invalidateCache() {
/* 306 */     this.converterCache.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Object handleConverterNotFound(@Nullable Object source, @Nullable TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 313 */     if (source == null) {
/* 314 */       assertNotPrimitiveTargetType(sourceType, targetType);
/* 315 */       return null;
/*     */     } 
/* 317 */     if ((sourceType == null || sourceType.isAssignableTo(targetType)) && targetType
/* 318 */       .getObjectType().isInstance(source)) {
/* 319 */       return source;
/*     */     }
/* 321 */     throw new ConverterNotFoundException(sourceType, targetType);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Object handleResult(@Nullable TypeDescriptor sourceType, TypeDescriptor targetType, @Nullable Object result) {
/* 326 */     if (result == null) {
/* 327 */       assertNotPrimitiveTargetType(sourceType, targetType);
/*     */     }
/* 329 */     return result;
/*     */   }
/*     */   
/*     */   private void assertNotPrimitiveTargetType(@Nullable TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 333 */     if (targetType.isPrimitive()) {
/* 334 */       throw new ConversionFailedException(sourceType, targetType, null, new IllegalArgumentException("A null value cannot be assigned to a primitive type"));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final class ConverterAdapter
/*     */     implements ConditionalGenericConverter
/*     */   {
/*     */     private final Converter<Object, Object> converter;
/*     */ 
/*     */     
/*     */     private final GenericConverter.ConvertiblePair typeInfo;
/*     */ 
/*     */     
/*     */     private final ResolvableType targetType;
/*     */ 
/*     */     
/*     */     public ConverterAdapter(Converter<?, ?> converter, ResolvableType sourceType, ResolvableType targetType) {
/* 353 */       this.converter = (Converter)converter;
/* 354 */       this.typeInfo = new GenericConverter.ConvertiblePair(sourceType.toClass(), targetType.toClass());
/* 355 */       this.targetType = targetType;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 360 */       return Collections.singleton(this.typeInfo);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 366 */       if (this.typeInfo.getTargetType() != targetType.getObjectType()) {
/* 367 */         return false;
/*     */       }
/*     */       
/* 370 */       ResolvableType rt = targetType.getResolvableType();
/* 371 */       if (!(rt.getType() instanceof Class) && !rt.isAssignableFrom(this.targetType) && 
/* 372 */         !this.targetType.hasUnresolvableGenerics()) {
/* 373 */         return false;
/*     */       }
/* 375 */       return (!(this.converter instanceof ConditionalConverter) || ((ConditionalConverter)this.converter)
/* 376 */         .matches(sourceType, targetType));
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 382 */       if (source == null) {
/* 383 */         return GenericConversionService.this.convertNullSource(sourceType, targetType);
/*     */       }
/* 385 */       return this.converter.convert(source);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 390 */       return this.typeInfo + " : " + this.converter;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final class ConverterFactoryAdapter
/*     */     implements ConditionalGenericConverter
/*     */   {
/*     */     private final ConverterFactory<Object, Object> converterFactory;
/*     */ 
/*     */     
/*     */     private final GenericConverter.ConvertiblePair typeInfo;
/*     */ 
/*     */     
/*     */     public ConverterFactoryAdapter(ConverterFactory<?, ?> converterFactory, GenericConverter.ConvertiblePair typeInfo) {
/* 406 */       this.converterFactory = (ConverterFactory)converterFactory;
/* 407 */       this.typeInfo = typeInfo;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 412 */       return Collections.singleton(this.typeInfo);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 417 */       boolean matches = true;
/* 418 */       if (this.converterFactory instanceof ConditionalConverter) {
/* 419 */         matches = ((ConditionalConverter)this.converterFactory).matches(sourceType, targetType);
/*     */       }
/* 421 */       if (matches) {
/* 422 */         Converter<?, ?> converter = this.converterFactory.getConverter(targetType.getType());
/* 423 */         if (converter instanceof ConditionalConverter) {
/* 424 */           matches = ((ConditionalConverter)converter).matches(sourceType, targetType);
/*     */         }
/*     */       } 
/* 427 */       return matches;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 433 */       if (source == null) {
/* 434 */         return GenericConversionService.this.convertNullSource(sourceType, targetType);
/*     */       }
/* 436 */       return this.converterFactory.getConverter(targetType.getObjectType()).convert(source);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 441 */       return this.typeInfo + " : " + this.converterFactory;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ConverterCacheKey
/*     */     implements Comparable<ConverterCacheKey>
/*     */   {
/*     */     private final TypeDescriptor sourceType;
/*     */     
/*     */     private final TypeDescriptor targetType;
/*     */ 
/*     */     
/*     */     public ConverterCacheKey(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 456 */       this.sourceType = sourceType;
/* 457 */       this.targetType = targetType;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 462 */       if (this == other) {
/* 463 */         return true;
/*     */       }
/* 465 */       if (!(other instanceof ConverterCacheKey)) {
/* 466 */         return false;
/*     */       }
/* 468 */       ConverterCacheKey otherKey = (ConverterCacheKey)other;
/* 469 */       return (this.sourceType.equals(otherKey.sourceType) && this.targetType
/* 470 */         .equals(otherKey.targetType));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 475 */       return this.sourceType.hashCode() * 29 + this.targetType.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 480 */       return "ConverterCacheKey [sourceType = " + this.sourceType + ", targetType = " + this.targetType + "]";
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int compareTo(ConverterCacheKey other) {
/* 486 */       int result = this.sourceType.getResolvableType().toString().compareTo(other.sourceType
/* 487 */           .getResolvableType().toString());
/* 488 */       if (result == 0) {
/* 489 */         result = this.targetType.getResolvableType().toString().compareTo(other.targetType
/* 490 */             .getResolvableType().toString());
/*     */       }
/* 492 */       return result;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class Converters
/*     */   {
/* 502 */     private final Set<GenericConverter> globalConverters = new LinkedHashSet<>();
/*     */     
/* 504 */     private final Map<GenericConverter.ConvertiblePair, GenericConversionService.ConvertersForPair> converters = new LinkedHashMap<>(36);
/*     */     
/*     */     public void add(GenericConverter converter) {
/* 507 */       Set<GenericConverter.ConvertiblePair> convertibleTypes = converter.getConvertibleTypes();
/* 508 */       if (convertibleTypes == null) {
/* 509 */         Assert.state(converter instanceof ConditionalConverter, "Only conditional converters may return null convertible types");
/*     */         
/* 511 */         this.globalConverters.add(converter);
/*     */       } else {
/*     */         
/* 514 */         for (GenericConverter.ConvertiblePair convertiblePair : convertibleTypes) {
/* 515 */           GenericConversionService.ConvertersForPair convertersForPair = getMatchableConverters(convertiblePair);
/* 516 */           convertersForPair.add(converter);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private GenericConversionService.ConvertersForPair getMatchableConverters(GenericConverter.ConvertiblePair convertiblePair) {
/* 522 */       GenericConversionService.ConvertersForPair convertersForPair = this.converters.get(convertiblePair);
/* 523 */       if (convertersForPair == null) {
/* 524 */         convertersForPair = new GenericConversionService.ConvertersForPair();
/* 525 */         this.converters.put(convertiblePair, convertersForPair);
/*     */       } 
/* 527 */       return convertersForPair;
/*     */     }
/*     */     
/*     */     public void remove(Class<?> sourceType, Class<?> targetType) {
/* 531 */       this.converters.remove(new GenericConverter.ConvertiblePair(sourceType, targetType));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public GenericConverter find(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 545 */       List<Class<?>> sourceCandidates = getClassHierarchy(sourceType.getType());
/* 546 */       List<Class<?>> targetCandidates = getClassHierarchy(targetType.getType());
/* 547 */       for (Class<?> sourceCandidate : sourceCandidates) {
/* 548 */         for (Class<?> targetCandidate : targetCandidates) {
/* 549 */           GenericConverter.ConvertiblePair convertiblePair = new GenericConverter.ConvertiblePair(sourceCandidate, targetCandidate);
/* 550 */           GenericConverter converter = getRegisteredConverter(sourceType, targetType, convertiblePair);
/* 551 */           if (converter != null) {
/* 552 */             return converter;
/*     */           }
/*     */         } 
/*     */       } 
/* 556 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private GenericConverter getRegisteredConverter(TypeDescriptor sourceType, TypeDescriptor targetType, GenericConverter.ConvertiblePair convertiblePair) {
/* 564 */       GenericConversionService.ConvertersForPair convertersForPair = this.converters.get(convertiblePair);
/* 565 */       if (convertersForPair != null) {
/* 566 */         GenericConverter converter = convertersForPair.getConverter(sourceType, targetType);
/* 567 */         if (converter != null) {
/* 568 */           return converter;
/*     */         }
/*     */       } 
/*     */       
/* 572 */       for (GenericConverter globalConverter : this.globalConverters) {
/* 573 */         if (((ConditionalConverter)globalConverter).matches(sourceType, targetType)) {
/* 574 */           return globalConverter;
/*     */         }
/*     */       } 
/* 577 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private List<Class<?>> getClassHierarchy(Class<?> type) {
/* 586 */       List<Class<?>> hierarchy = new ArrayList<>(20);
/* 587 */       Set<Class<?>> visited = new HashSet<>(20);
/* 588 */       addToClassHierarchy(0, ClassUtils.resolvePrimitiveIfNecessary(type), false, hierarchy, visited);
/* 589 */       boolean array = type.isArray();
/*     */       
/* 591 */       int i = 0;
/* 592 */       while (i < hierarchy.size()) {
/* 593 */         Class<?> candidate = hierarchy.get(i);
/* 594 */         candidate = array ? candidate.getComponentType() : ClassUtils.resolvePrimitiveIfNecessary(candidate);
/* 595 */         Class<?> superclass = candidate.getSuperclass();
/* 596 */         if (superclass != null && superclass != Object.class && superclass != Enum.class) {
/* 597 */           addToClassHierarchy(i + 1, candidate.getSuperclass(), array, hierarchy, visited);
/*     */         }
/* 599 */         addInterfacesToClassHierarchy(candidate, array, hierarchy, visited);
/* 600 */         i++;
/*     */       } 
/*     */       
/* 603 */       if (Enum.class.isAssignableFrom(type)) {
/* 604 */         addToClassHierarchy(hierarchy.size(), Enum.class, array, hierarchy, visited);
/* 605 */         addToClassHierarchy(hierarchy.size(), Enum.class, false, hierarchy, visited);
/* 606 */         addInterfacesToClassHierarchy(Enum.class, array, hierarchy, visited);
/*     */       } 
/*     */       
/* 609 */       addToClassHierarchy(hierarchy.size(), Object.class, array, hierarchy, visited);
/* 610 */       addToClassHierarchy(hierarchy.size(), Object.class, false, hierarchy, visited);
/* 611 */       return hierarchy;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private void addInterfacesToClassHierarchy(Class<?> type, boolean asArray, List<Class<?>> hierarchy, Set<Class<?>> visited) {
/* 617 */       for (Class<?> implementedInterface : type.getInterfaces()) {
/* 618 */         addToClassHierarchy(hierarchy.size(), implementedInterface, asArray, hierarchy, visited);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private void addToClassHierarchy(int index, Class<?> type, boolean asArray, List<Class<?>> hierarchy, Set<Class<?>> visited) {
/* 625 */       if (asArray) {
/* 626 */         type = Array.newInstance(type, 0).getClass();
/*     */       }
/* 628 */       if (visited.add(type)) {
/* 629 */         hierarchy.add(index, type);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 635 */       StringBuilder builder = new StringBuilder();
/* 636 */       builder.append("ConversionService converters =\n");
/* 637 */       for (String converterString : getConverterStrings()) {
/* 638 */         builder.append('\t').append(converterString).append('\n');
/*     */       }
/* 640 */       return builder.toString();
/*     */     }
/*     */     
/*     */     private List<String> getConverterStrings() {
/* 644 */       List<String> converterStrings = new ArrayList<>();
/* 645 */       for (GenericConversionService.ConvertersForPair convertersForPair : this.converters.values()) {
/* 646 */         converterStrings.add(convertersForPair.toString());
/*     */       }
/* 648 */       Collections.sort(converterStrings);
/* 649 */       return converterStrings;
/*     */     }
/*     */ 
/*     */     
/*     */     private Converters() {}
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ConvertersForPair
/*     */   {
/* 659 */     private final LinkedList<GenericConverter> converters = new LinkedList<>();
/*     */     
/*     */     public void add(GenericConverter converter) {
/* 662 */       this.converters.addFirst(converter);
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public GenericConverter getConverter(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 667 */       for (GenericConverter converter : this.converters) {
/* 668 */         if (!(converter instanceof ConditionalGenericConverter) || ((ConditionalGenericConverter)converter)
/* 669 */           .matches(sourceType, targetType)) {
/* 670 */           return converter;
/*     */         }
/*     */       } 
/* 673 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 678 */       return StringUtils.collectionToCommaDelimitedString(this.converters);
/*     */     }
/*     */ 
/*     */     
/*     */     private ConvertersForPair() {}
/*     */   }
/*     */   
/*     */   private static class NoOpConverter
/*     */     implements GenericConverter
/*     */   {
/*     */     private final String name;
/*     */     
/*     */     public NoOpConverter(String name) {
/* 691 */       this.name = name;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 696 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 702 */       return source;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 707 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/convert/support/GenericConversionService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */