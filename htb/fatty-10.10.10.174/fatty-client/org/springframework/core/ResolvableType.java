/*      */ package org.springframework.core;
/*      */ 
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.GenericArrayType;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.ParameterizedType;
/*      */ import java.lang.reflect.Type;
/*      */ import java.lang.reflect.TypeVariable;
/*      */ import java.lang.reflect.WildcardType;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.IdentityHashMap;
/*      */ import java.util.Map;
/*      */ import org.springframework.lang.Nullable;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.ConcurrentReferenceHashMap;
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
/*      */ public class ResolvableType
/*      */   implements Serializable
/*      */ {
/*   89 */   public static final ResolvableType NONE = new ResolvableType(EmptyType.INSTANCE, null, null, Integer.valueOf(0));
/*      */   
/*   91 */   private static final ResolvableType[] EMPTY_TYPES_ARRAY = new ResolvableType[0];
/*      */   
/*   93 */   private static final ConcurrentReferenceHashMap<ResolvableType, ResolvableType> cache = new ConcurrentReferenceHashMap(256);
/*      */ 
/*      */ 
/*      */   
/*      */   private final Type type;
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private final SerializableTypeWrapper.TypeProvider typeProvider;
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private final VariableResolver variableResolver;
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private final ResolvableType componentType;
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private final Integer hash;
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private Class<?> resolved;
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private volatile ResolvableType superType;
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private volatile ResolvableType[] interfaces;
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private volatile ResolvableType[] generics;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ResolvableType(Type type, @Nullable SerializableTypeWrapper.TypeProvider typeProvider, @Nullable VariableResolver variableResolver) {
/*  143 */     this.type = type;
/*  144 */     this.typeProvider = typeProvider;
/*  145 */     this.variableResolver = variableResolver;
/*  146 */     this.componentType = null;
/*  147 */     this.hash = Integer.valueOf(calculateHashCode());
/*  148 */     this.resolved = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ResolvableType(Type type, @Nullable SerializableTypeWrapper.TypeProvider typeProvider, @Nullable VariableResolver variableResolver, @Nullable Integer hash) {
/*  159 */     this.type = type;
/*  160 */     this.typeProvider = typeProvider;
/*  161 */     this.variableResolver = variableResolver;
/*  162 */     this.componentType = null;
/*  163 */     this.hash = hash;
/*  164 */     this.resolved = resolveClass();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ResolvableType(Type type, @Nullable SerializableTypeWrapper.TypeProvider typeProvider, @Nullable VariableResolver variableResolver, @Nullable ResolvableType componentType) {
/*  174 */     this.type = type;
/*  175 */     this.typeProvider = typeProvider;
/*  176 */     this.variableResolver = variableResolver;
/*  177 */     this.componentType = componentType;
/*  178 */     this.hash = null;
/*  179 */     this.resolved = resolveClass();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ResolvableType(@Nullable Class<?> clazz) {
/*  188 */     this.resolved = (clazz != null) ? clazz : Object.class;
/*  189 */     this.type = this.resolved;
/*  190 */     this.typeProvider = null;
/*  191 */     this.variableResolver = null;
/*  192 */     this.componentType = null;
/*  193 */     this.hash = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Type getType() {
/*  201 */     return SerializableTypeWrapper.unwrap(this.type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Class<?> getRawClass() {
/*  210 */     if (this.type == this.resolved) {
/*  211 */       return this.resolved;
/*      */     }
/*  213 */     Type rawType = this.type;
/*  214 */     if (rawType instanceof ParameterizedType) {
/*  215 */       rawType = ((ParameterizedType)rawType).getRawType();
/*      */     }
/*  217 */     return (rawType instanceof Class) ? (Class)rawType : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getSource() {
/*  228 */     Object source = (this.typeProvider != null) ? this.typeProvider.getSource() : null;
/*  229 */     return (source != null) ? source : this.type;
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
/*      */   public Class<?> toClass() {
/*  241 */     return resolve(Object.class);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isInstance(@Nullable Object obj) {
/*  251 */     return (obj != null && isAssignableFrom(obj.getClass()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAssignableFrom(Class<?> other) {
/*  262 */     return isAssignableFrom(forClass(other), null);
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
/*      */   
/*      */   public boolean isAssignableFrom(ResolvableType other) {
/*  277 */     return isAssignableFrom(other, null);
/*      */   }
/*      */   
/*      */   private boolean isAssignableFrom(ResolvableType other, @Nullable Map<Type, Type> matchedBefore) {
/*  281 */     Assert.notNull(other, "ResolvableType must not be null");
/*      */ 
/*      */     
/*  284 */     if (this == NONE || other == NONE) {
/*  285 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  289 */     if (isArray()) {
/*  290 */       return (other.isArray() && getComponentType().isAssignableFrom(other.getComponentType()));
/*      */     }
/*      */     
/*  293 */     if (matchedBefore != null && matchedBefore.get(this.type) == other.type) {
/*  294 */       return true;
/*      */     }
/*      */ 
/*      */     
/*  298 */     WildcardBounds ourBounds = WildcardBounds.get(this);
/*  299 */     WildcardBounds typeBounds = WildcardBounds.get(other);
/*      */ 
/*      */     
/*  302 */     if (typeBounds != null) {
/*  303 */       return (ourBounds != null && ourBounds.isSameKind(typeBounds) && ourBounds
/*  304 */         .isAssignableFrom(typeBounds.getBounds()));
/*      */     }
/*      */ 
/*      */     
/*  308 */     if (ourBounds != null) {
/*  309 */       return ourBounds.isAssignableFrom(new ResolvableType[] { other });
/*      */     }
/*      */ 
/*      */     
/*  313 */     boolean exactMatch = (matchedBefore != null);
/*  314 */     boolean checkGenerics = true;
/*  315 */     Class<?> ourResolved = null;
/*  316 */     if (this.type instanceof TypeVariable) {
/*  317 */       TypeVariable<?> variable = (TypeVariable)this.type;
/*      */       
/*  319 */       if (this.variableResolver != null) {
/*  320 */         ResolvableType resolved = this.variableResolver.resolveVariable(variable);
/*  321 */         if (resolved != null) {
/*  322 */           ourResolved = resolved.resolve();
/*      */         }
/*      */       } 
/*  325 */       if (ourResolved == null)
/*      */       {
/*  327 */         if (other.variableResolver != null) {
/*  328 */           ResolvableType resolved = other.variableResolver.resolveVariable(variable);
/*  329 */           if (resolved != null) {
/*  330 */             ourResolved = resolved.resolve();
/*  331 */             checkGenerics = false;
/*      */           } 
/*      */         } 
/*      */       }
/*  335 */       if (ourResolved == null)
/*      */       {
/*  337 */         exactMatch = false;
/*      */       }
/*      */     } 
/*  340 */     if (ourResolved == null) {
/*  341 */       ourResolved = resolve(Object.class);
/*      */     }
/*  343 */     Class<?> otherResolved = other.toClass();
/*      */ 
/*      */ 
/*      */     
/*  347 */     if (exactMatch ? !ourResolved.equals(otherResolved) : !ClassUtils.isAssignable(ourResolved, otherResolved)) {
/*  348 */       return false;
/*      */     }
/*      */     
/*  351 */     if (checkGenerics) {
/*      */       
/*  353 */       ResolvableType[] ourGenerics = getGenerics();
/*  354 */       ResolvableType[] typeGenerics = other.as(ourResolved).getGenerics();
/*  355 */       if (ourGenerics.length != typeGenerics.length) {
/*  356 */         return false;
/*      */       }
/*  358 */       if (matchedBefore == null) {
/*  359 */         matchedBefore = new IdentityHashMap<>(1);
/*      */       }
/*  361 */       matchedBefore.put(this.type, other.type);
/*  362 */       for (int i = 0; i < ourGenerics.length; i++) {
/*  363 */         if (!ourGenerics[i].isAssignableFrom(typeGenerics[i], matchedBefore)) {
/*  364 */           return false;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  369 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isArray() {
/*  377 */     if (this == NONE) {
/*  378 */       return false;
/*      */     }
/*  380 */     return ((this.type instanceof Class && ((Class)this.type).isArray()) || this.type instanceof GenericArrayType || 
/*  381 */       resolveType().isArray());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResolvableType getComponentType() {
/*  390 */     if (this == NONE) {
/*  391 */       return NONE;
/*      */     }
/*  393 */     if (this.componentType != null) {
/*  394 */       return this.componentType;
/*      */     }
/*  396 */     if (this.type instanceof Class) {
/*  397 */       Class<?> componentType = ((Class)this.type).getComponentType();
/*  398 */       return forType(componentType, this.variableResolver);
/*      */     } 
/*  400 */     if (this.type instanceof GenericArrayType) {
/*  401 */       return forType(((GenericArrayType)this.type).getGenericComponentType(), this.variableResolver);
/*      */     }
/*  403 */     return resolveType().getComponentType();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResolvableType asCollection() {
/*  414 */     return as(Collection.class);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResolvableType asMap() {
/*  425 */     return as(Map.class);
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
/*      */ 
/*      */ 
/*      */   
/*      */   public ResolvableType as(Class<?> type) {
/*  442 */     if (this == NONE) {
/*  443 */       return NONE;
/*      */     }
/*  445 */     Class<?> resolved = resolve();
/*  446 */     if (resolved == null || resolved == type) {
/*  447 */       return this;
/*      */     }
/*  449 */     for (ResolvableType interfaceType : getInterfaces()) {
/*  450 */       ResolvableType interfaceAsType = interfaceType.as(type);
/*  451 */       if (interfaceAsType != NONE) {
/*  452 */         return interfaceAsType;
/*      */       }
/*      */     } 
/*  455 */     return getSuperType().as(type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResolvableType getSuperType() {
/*  465 */     Class<?> resolved = resolve();
/*  466 */     if (resolved == null || resolved.getGenericSuperclass() == null) {
/*  467 */       return NONE;
/*      */     }
/*  469 */     ResolvableType superType = this.superType;
/*  470 */     if (superType == null) {
/*  471 */       superType = forType(resolved.getGenericSuperclass(), this);
/*  472 */       this.superType = superType;
/*      */     } 
/*  474 */     return superType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResolvableType[] getInterfaces() {
/*  485 */     Class<?> resolved = resolve();
/*  486 */     if (resolved == null) {
/*  487 */       return EMPTY_TYPES_ARRAY;
/*      */     }
/*  489 */     ResolvableType[] interfaces = this.interfaces;
/*  490 */     if (interfaces == null) {
/*  491 */       Type[] genericIfcs = resolved.getGenericInterfaces();
/*  492 */       interfaces = new ResolvableType[genericIfcs.length];
/*  493 */       for (int i = 0; i < genericIfcs.length; i++) {
/*  494 */         interfaces[i] = forType(genericIfcs[i], this);
/*      */       }
/*  496 */       this.interfaces = interfaces;
/*      */     } 
/*  498 */     return interfaces;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasGenerics() {
/*  507 */     return ((getGenerics()).length > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isEntirelyUnresolvable() {
/*  515 */     if (this == NONE) {
/*  516 */       return false;
/*      */     }
/*  518 */     ResolvableType[] generics = getGenerics();
/*  519 */     for (ResolvableType generic : generics) {
/*  520 */       if (!generic.isUnresolvableTypeVariable() && !generic.isWildcardWithoutBounds()) {
/*  521 */         return false;
/*      */       }
/*      */     } 
/*  524 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasUnresolvableGenerics() {
/*  535 */     if (this == NONE) {
/*  536 */       return false;
/*      */     }
/*  538 */     ResolvableType[] generics = getGenerics();
/*  539 */     for (ResolvableType generic : generics) {
/*  540 */       if (generic.isUnresolvableTypeVariable() || generic.isWildcardWithoutBounds()) {
/*  541 */         return true;
/*      */       }
/*      */     } 
/*  544 */     Class<?> resolved = resolve();
/*  545 */     if (resolved != null) {
/*  546 */       for (Type genericInterface : resolved.getGenericInterfaces()) {
/*  547 */         if (genericInterface instanceof Class && 
/*  548 */           forClass((Class)genericInterface).hasGenerics()) {
/*  549 */           return true;
/*      */         }
/*      */       } 
/*      */       
/*  553 */       return getSuperType().hasUnresolvableGenerics();
/*      */     } 
/*  555 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isUnresolvableTypeVariable() {
/*  563 */     if (this.type instanceof TypeVariable) {
/*  564 */       if (this.variableResolver == null) {
/*  565 */         return true;
/*      */       }
/*  567 */       TypeVariable<?> variable = (TypeVariable)this.type;
/*  568 */       ResolvableType resolved = this.variableResolver.resolveVariable(variable);
/*  569 */       if (resolved == null || resolved.isUnresolvableTypeVariable()) {
/*  570 */         return true;
/*      */       }
/*      */     } 
/*  573 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isWildcardWithoutBounds() {
/*  581 */     if (this.type instanceof WildcardType) {
/*  582 */       WildcardType wt = (WildcardType)this.type;
/*  583 */       if ((wt.getLowerBounds()).length == 0) {
/*  584 */         Type[] upperBounds = wt.getUpperBounds();
/*  585 */         if (upperBounds.length == 0 || (upperBounds.length == 1 && Object.class == upperBounds[0])) {
/*  586 */           return true;
/*      */         }
/*      */       } 
/*      */     } 
/*  590 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResolvableType getNested(int nestingLevel) {
/*  600 */     return getNested(nestingLevel, null);
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
/*      */   public ResolvableType getNested(int nestingLevel, @Nullable Map<Integer, Integer> typeIndexesPerLevel) {
/*  624 */     ResolvableType result = this;
/*  625 */     for (int i = 2; i <= nestingLevel; i++) {
/*  626 */       if (result.isArray()) {
/*  627 */         result = result.getComponentType();
/*      */       }
/*      */       else {
/*      */         
/*  631 */         while (result != NONE && !result.hasGenerics()) {
/*  632 */           result = result.getSuperType();
/*      */         }
/*  634 */         Integer index = (typeIndexesPerLevel != null) ? typeIndexesPerLevel.get(Integer.valueOf(i)) : null;
/*  635 */         index = Integer.valueOf((index == null) ? ((result.getGenerics()).length - 1) : index.intValue());
/*  636 */         result = result.getGeneric(new int[] { index.intValue() });
/*      */       } 
/*      */     } 
/*  639 */     return result;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResolvableType getGeneric(@Nullable int... indexes) {
/*  660 */     ResolvableType[] generics = getGenerics();
/*  661 */     if (indexes == null || indexes.length == 0) {
/*  662 */       return (generics.length == 0) ? NONE : generics[0];
/*      */     }
/*  664 */     ResolvableType generic = this;
/*  665 */     for (int index : indexes) {
/*  666 */       generics = generic.getGenerics();
/*  667 */       if (index < 0 || index >= generics.length) {
/*  668 */         return NONE;
/*      */       }
/*  670 */       generic = generics[index];
/*      */     } 
/*  672 */     return generic;
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
/*      */ 
/*      */ 
/*      */   
/*      */   public ResolvableType[] getGenerics() {
/*  689 */     if (this == NONE) {
/*  690 */       return EMPTY_TYPES_ARRAY;
/*      */     }
/*  692 */     ResolvableType[] generics = this.generics;
/*  693 */     if (generics == null) {
/*  694 */       if (this.type instanceof Class) {
/*  695 */         TypeVariable[] arrayOfTypeVariable = ((Class)this.type).getTypeParameters();
/*  696 */         generics = new ResolvableType[arrayOfTypeVariable.length];
/*  697 */         for (int i = 0; i < generics.length; i++) {
/*  698 */           generics[i] = forType(arrayOfTypeVariable[i], this);
/*      */         }
/*      */       }
/*  701 */       else if (this.type instanceof ParameterizedType) {
/*  702 */         Type[] actualTypeArguments = ((ParameterizedType)this.type).getActualTypeArguments();
/*  703 */         generics = new ResolvableType[actualTypeArguments.length];
/*  704 */         for (int i = 0; i < actualTypeArguments.length; i++) {
/*  705 */           generics[i] = forType(actualTypeArguments[i], this.variableResolver);
/*      */         }
/*      */       } else {
/*      */         
/*  709 */         generics = resolveType().getGenerics();
/*      */       } 
/*  711 */       this.generics = generics;
/*      */     } 
/*  713 */     return generics;
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
/*      */   public Class<?>[] resolveGenerics() {
/*  725 */     ResolvableType[] generics = getGenerics();
/*  726 */     Class<?>[] resolvedGenerics = new Class[generics.length];
/*  727 */     for (int i = 0; i < generics.length; i++) {
/*  728 */       resolvedGenerics[i] = generics[i].resolve();
/*      */     }
/*  730 */     return resolvedGenerics;
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
/*      */   public Class<?>[] resolveGenerics(Class<?> fallback) {
/*  743 */     ResolvableType[] generics = getGenerics();
/*  744 */     Class<?>[] resolvedGenerics = new Class[generics.length];
/*  745 */     for (int i = 0; i < generics.length; i++) {
/*  746 */       resolvedGenerics[i] = generics[i].resolve(fallback);
/*      */     }
/*  748 */     return resolvedGenerics;
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
/*      */   @Nullable
/*      */   public Class<?> resolveGeneric(int... indexes) {
/*  762 */     return getGeneric(indexes).resolve();
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
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Class<?> resolve() {
/*  780 */     return this.resolved;
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
/*      */   
/*      */   public Class<?> resolve(Class<?> fallback) {
/*  795 */     return (this.resolved != null) ? this.resolved : fallback;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private Class<?> resolveClass() {
/*  800 */     if (this.type == EmptyType.INSTANCE) {
/*  801 */       return null;
/*      */     }
/*  803 */     if (this.type instanceof Class) {
/*  804 */       return (Class)this.type;
/*      */     }
/*  806 */     if (this.type instanceof GenericArrayType) {
/*  807 */       Class<?> resolvedComponent = getComponentType().resolve();
/*  808 */       return (resolvedComponent != null) ? Array.newInstance(resolvedComponent, 0).getClass() : null;
/*      */     } 
/*  810 */     return resolveType().resolve();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   ResolvableType resolveType() {
/*  819 */     if (this.type instanceof ParameterizedType) {
/*  820 */       return forType(((ParameterizedType)this.type).getRawType(), this.variableResolver);
/*      */     }
/*  822 */     if (this.type instanceof WildcardType) {
/*  823 */       Type resolved = resolveBounds(((WildcardType)this.type).getUpperBounds());
/*  824 */       if (resolved == null) {
/*  825 */         resolved = resolveBounds(((WildcardType)this.type).getLowerBounds());
/*      */       }
/*  827 */       return forType(resolved, this.variableResolver);
/*      */     } 
/*  829 */     if (this.type instanceof TypeVariable) {
/*  830 */       TypeVariable<?> variable = (TypeVariable)this.type;
/*      */       
/*  832 */       if (this.variableResolver != null) {
/*  833 */         ResolvableType resolved = this.variableResolver.resolveVariable(variable);
/*  834 */         if (resolved != null) {
/*  835 */           return resolved;
/*      */         }
/*      */       } 
/*      */       
/*  839 */       return forType(resolveBounds(variable.getBounds()), this.variableResolver);
/*      */     } 
/*  841 */     return NONE;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private Type resolveBounds(Type[] bounds) {
/*  846 */     if (bounds.length == 0 || bounds[0] == Object.class) {
/*  847 */       return null;
/*      */     }
/*  849 */     return bounds[0];
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private ResolvableType resolveVariable(TypeVariable<?> variable) {
/*  854 */     if (this.type instanceof TypeVariable) {
/*  855 */       return resolveType().resolveVariable(variable);
/*      */     }
/*  857 */     if (this.type instanceof ParameterizedType) {
/*  858 */       ParameterizedType parameterizedType = (ParameterizedType)this.type;
/*  859 */       Class<?> resolved = resolve();
/*  860 */       if (resolved == null) {
/*  861 */         return null;
/*      */       }
/*  863 */       TypeVariable[] arrayOfTypeVariable = (TypeVariable[])resolved.getTypeParameters();
/*  864 */       for (int i = 0; i < arrayOfTypeVariable.length; i++) {
/*  865 */         if (ObjectUtils.nullSafeEquals(arrayOfTypeVariable[i].getName(), variable.getName())) {
/*  866 */           Type actualType = parameterizedType.getActualTypeArguments()[i];
/*  867 */           return forType(actualType, this.variableResolver);
/*      */         } 
/*      */       } 
/*  870 */       Type ownerType = parameterizedType.getOwnerType();
/*  871 */       if (ownerType != null) {
/*  872 */         return forType(ownerType, this.variableResolver).resolveVariable(variable);
/*      */       }
/*      */     } 
/*  875 */     if (this.variableResolver != null) {
/*  876 */       return this.variableResolver.resolveVariable(variable);
/*      */     }
/*  878 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object other) {
/*  884 */     if (this == other) {
/*  885 */       return true;
/*      */     }
/*  887 */     if (!(other instanceof ResolvableType)) {
/*  888 */       return false;
/*      */     }
/*      */     
/*  891 */     ResolvableType otherType = (ResolvableType)other;
/*  892 */     if (!ObjectUtils.nullSafeEquals(this.type, otherType.type)) {
/*  893 */       return false;
/*      */     }
/*  895 */     if (this.typeProvider != otherType.typeProvider && (this.typeProvider == null || otherType.typeProvider == null || 
/*      */       
/*  897 */       !ObjectUtils.nullSafeEquals(this.typeProvider.getType(), otherType.typeProvider.getType()))) {
/*  898 */       return false;
/*      */     }
/*  900 */     if (this.variableResolver != otherType.variableResolver && (this.variableResolver == null || otherType.variableResolver == null || 
/*      */       
/*  902 */       !ObjectUtils.nullSafeEquals(this.variableResolver.getSource(), otherType.variableResolver.getSource()))) {
/*  903 */       return false;
/*      */     }
/*  905 */     if (!ObjectUtils.nullSafeEquals(this.componentType, otherType.componentType)) {
/*  906 */       return false;
/*      */     }
/*  908 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  913 */     return (this.hash != null) ? this.hash.intValue() : calculateHashCode();
/*      */   }
/*      */   
/*      */   private int calculateHashCode() {
/*  917 */     int hashCode = ObjectUtils.nullSafeHashCode(this.type);
/*  918 */     if (this.typeProvider != null) {
/*  919 */       hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode(this.typeProvider.getType());
/*      */     }
/*  921 */     if (this.variableResolver != null) {
/*  922 */       hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode(this.variableResolver.getSource());
/*      */     }
/*  924 */     if (this.componentType != null) {
/*  925 */       hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode(this.componentType);
/*      */     }
/*  927 */     return hashCode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   VariableResolver asVariableResolver() {
/*  935 */     if (this == NONE) {
/*  936 */       return null;
/*      */     }
/*  938 */     return new DefaultVariableResolver();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Object readResolve() {
/*  945 */     return (this.type == EmptyType.INSTANCE) ? NONE : this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  954 */     if (isArray()) {
/*  955 */       return getComponentType() + "[]";
/*      */     }
/*  957 */     if (this.resolved == null) {
/*  958 */       return "?";
/*      */     }
/*  960 */     if (this.type instanceof TypeVariable) {
/*  961 */       TypeVariable<?> variable = (TypeVariable)this.type;
/*  962 */       if (this.variableResolver == null || this.variableResolver.resolveVariable(variable) == null)
/*      */       {
/*      */         
/*  965 */         return "?";
/*      */       }
/*      */     } 
/*  968 */     StringBuilder result = new StringBuilder(this.resolved.getName());
/*  969 */     if (hasGenerics()) {
/*  970 */       result.append('<');
/*  971 */       result.append(StringUtils.arrayToDelimitedString((Object[])getGenerics(), ", "));
/*  972 */       result.append('>');
/*      */     } 
/*  974 */     return result.toString();
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
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forClass(@Nullable Class<?> clazz) {
/*  991 */     return new ResolvableType(clazz);
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
/*      */ 
/*      */   
/*      */   public static ResolvableType forRawClass(@Nullable final Class<?> clazz) {
/* 1007 */     return new ResolvableType(clazz)
/*      */       {
/*      */         public ResolvableType[] getGenerics() {
/* 1010 */           return ResolvableType.EMPTY_TYPES_ARRAY;
/*      */         }
/*      */         
/*      */         public boolean isAssignableFrom(Class<?> other) {
/* 1014 */           return (clazz == null || ClassUtils.isAssignable(clazz, other));
/*      */         }
/*      */         
/*      */         public boolean isAssignableFrom(ResolvableType other) {
/* 1018 */           Class<?> otherClass = other.resolve();
/* 1019 */           return (otherClass != null && (clazz == null || ClassUtils.isAssignable(clazz, otherClass)));
/*      */         }
/*      */       };
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
/*      */   
/*      */   public static ResolvableType forClass(Class<?> baseType, Class<?> implementationClass) {
/* 1036 */     Assert.notNull(baseType, "Base type must not be null");
/* 1037 */     ResolvableType asType = forType(implementationClass).as(baseType);
/* 1038 */     return (asType == NONE) ? forType(baseType) : asType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forClassWithGenerics(Class<?> clazz, Class<?>... generics) {
/* 1049 */     Assert.notNull(clazz, "Class must not be null");
/* 1050 */     Assert.notNull(generics, "Generics array must not be null");
/* 1051 */     ResolvableType[] resolvableGenerics = new ResolvableType[generics.length];
/* 1052 */     for (int i = 0; i < generics.length; i++) {
/* 1053 */       resolvableGenerics[i] = forClass(generics[i]);
/*      */     }
/* 1055 */     return forClassWithGenerics(clazz, resolvableGenerics);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forClassWithGenerics(Class<?> clazz, ResolvableType... generics) {
/* 1066 */     Assert.notNull(clazz, "Class must not be null");
/* 1067 */     Assert.notNull(generics, "Generics array must not be null");
/* 1068 */     TypeVariable[] arrayOfTypeVariable = (TypeVariable[])clazz.getTypeParameters();
/* 1069 */     Assert.isTrue((arrayOfTypeVariable.length == generics.length), "Mismatched number of generics specified");
/*      */     
/* 1071 */     Type[] arguments = new Type[generics.length];
/* 1072 */     for (int i = 0; i < generics.length; i++) {
/* 1073 */       ResolvableType generic = generics[i];
/* 1074 */       Type argument = (generic != null) ? generic.getType() : null;
/* 1075 */       arguments[i] = (argument != null && !(argument instanceof TypeVariable)) ? argument : arrayOfTypeVariable[i];
/*      */     } 
/*      */     
/* 1078 */     ParameterizedType syntheticType = new SyntheticParameterizedType(clazz, arguments);
/* 1079 */     return forType(syntheticType, new TypeVariablesVariableResolver((TypeVariable<?>[])arrayOfTypeVariable, generics));
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
/*      */   public static ResolvableType forInstance(Object instance) {
/* 1093 */     Assert.notNull(instance, "Instance must not be null");
/* 1094 */     if (instance instanceof ResolvableTypeProvider) {
/* 1095 */       ResolvableType type = ((ResolvableTypeProvider)instance).getResolvableType();
/* 1096 */       if (type != null) {
/* 1097 */         return type;
/*      */       }
/*      */     } 
/* 1100 */     return forClass(instance.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forField(Field field) {
/* 1110 */     Assert.notNull(field, "Field must not be null");
/* 1111 */     return forType(null, new SerializableTypeWrapper.FieldTypeProvider(field), null);
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
/*      */   public static ResolvableType forField(Field field, Class<?> implementationClass) {
/* 1125 */     Assert.notNull(field, "Field must not be null");
/* 1126 */     ResolvableType owner = forType(implementationClass).as(field.getDeclaringClass());
/* 1127 */     return forType(null, new SerializableTypeWrapper.FieldTypeProvider(field), owner.asVariableResolver());
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
/*      */   public static ResolvableType forField(Field field, @Nullable ResolvableType implementationType) {
/* 1141 */     Assert.notNull(field, "Field must not be null");
/* 1142 */     ResolvableType owner = (implementationType != null) ? implementationType : NONE;
/* 1143 */     owner = owner.as(field.getDeclaringClass());
/* 1144 */     return forType(null, new SerializableTypeWrapper.FieldTypeProvider(field), owner.asVariableResolver());
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
/*      */   public static ResolvableType forField(Field field, int nestingLevel) {
/* 1156 */     Assert.notNull(field, "Field must not be null");
/* 1157 */     return forType(null, new SerializableTypeWrapper.FieldTypeProvider(field), null).getNested(nestingLevel);
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
/*      */ 
/*      */   
/*      */   public static ResolvableType forField(Field field, int nestingLevel, @Nullable Class<?> implementationClass) {
/* 1173 */     Assert.notNull(field, "Field must not be null");
/* 1174 */     ResolvableType owner = forType(implementationClass).as(field.getDeclaringClass());
/* 1175 */     return forType(null, new SerializableTypeWrapper.FieldTypeProvider(field), owner.asVariableResolver()).getNested(nestingLevel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forConstructorParameter(Constructor<?> constructor, int parameterIndex) {
/* 1186 */     Assert.notNull(constructor, "Constructor must not be null");
/* 1187 */     return forMethodParameter(new MethodParameter(constructor, parameterIndex));
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
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forConstructorParameter(Constructor<?> constructor, int parameterIndex, Class<?> implementationClass) {
/* 1204 */     Assert.notNull(constructor, "Constructor must not be null");
/* 1205 */     MethodParameter methodParameter = new MethodParameter(constructor, parameterIndex);
/* 1206 */     methodParameter.setContainingClass(implementationClass);
/* 1207 */     return forMethodParameter(methodParameter);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forMethodReturnType(Method method) {
/* 1217 */     Assert.notNull(method, "Method must not be null");
/* 1218 */     return forMethodParameter(new MethodParameter(method, -1));
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
/*      */   public static ResolvableType forMethodReturnType(Method method, Class<?> implementationClass) {
/* 1231 */     Assert.notNull(method, "Method must not be null");
/* 1232 */     MethodParameter methodParameter = new MethodParameter(method, -1);
/* 1233 */     methodParameter.setContainingClass(implementationClass);
/* 1234 */     return forMethodParameter(methodParameter);
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
/*      */   public static ResolvableType forMethodParameter(Method method, int parameterIndex) {
/* 1246 */     Assert.notNull(method, "Method must not be null");
/* 1247 */     return forMethodParameter(new MethodParameter(method, parameterIndex));
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
/*      */   
/*      */   public static ResolvableType forMethodParameter(Method method, int parameterIndex, Class<?> implementationClass) {
/* 1262 */     Assert.notNull(method, "Method must not be null");
/* 1263 */     MethodParameter methodParameter = new MethodParameter(method, parameterIndex);
/* 1264 */     methodParameter.setContainingClass(implementationClass);
/* 1265 */     return forMethodParameter(methodParameter);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forMethodParameter(MethodParameter methodParameter) {
/* 1275 */     return forMethodParameter(methodParameter, (Type)null);
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
/*      */   
/*      */   public static ResolvableType forMethodParameter(MethodParameter methodParameter, @Nullable ResolvableType implementationType) {
/* 1290 */     Assert.notNull(methodParameter, "MethodParameter must not be null");
/*      */     
/* 1292 */     implementationType = (implementationType != null) ? implementationType : forType(methodParameter.getContainingClass());
/* 1293 */     ResolvableType owner = implementationType.as(methodParameter.getDeclaringClass());
/* 1294 */     return forType(null, new SerializableTypeWrapper.MethodParameterTypeProvider(methodParameter), owner.asVariableResolver())
/* 1295 */       .getNested(methodParameter.getNestingLevel(), methodParameter.typeIndexesPerLevel);
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
/*      */   public static ResolvableType forMethodParameter(MethodParameter methodParameter, @Nullable Type targetType) {
/* 1307 */     Assert.notNull(methodParameter, "MethodParameter must not be null");
/* 1308 */     ResolvableType owner = forType(methodParameter.getContainingClass()).as(methodParameter.getDeclaringClass());
/* 1309 */     return forType(targetType, new SerializableTypeWrapper.MethodParameterTypeProvider(methodParameter), owner.asVariableResolver())
/* 1310 */       .getNested(methodParameter.getNestingLevel(), methodParameter.typeIndexesPerLevel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void resolveMethodParameter(MethodParameter methodParameter) {
/* 1320 */     Assert.notNull(methodParameter, "MethodParameter must not be null");
/* 1321 */     ResolvableType owner = forType(methodParameter.getContainingClass()).as(methodParameter.getDeclaringClass());
/* 1322 */     methodParameter.setParameterType(
/* 1323 */         forType(null, new SerializableTypeWrapper.MethodParameterTypeProvider(methodParameter), owner.asVariableResolver()).resolve());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forArrayComponent(ResolvableType componentType) {
/* 1332 */     Assert.notNull(componentType, "Component type must not be null");
/* 1333 */     Class<?> arrayClass = Array.newInstance(componentType.resolve(), 0).getClass();
/* 1334 */     return new ResolvableType(arrayClass, null, null, componentType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forType(@Nullable Type type) {
/* 1345 */     return forType(type, null, null);
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
/*      */   public static ResolvableType forType(@Nullable Type type, @Nullable ResolvableType owner) {
/* 1358 */     VariableResolver variableResolver = null;
/* 1359 */     if (owner != null) {
/* 1360 */       variableResolver = owner.asVariableResolver();
/*      */     }
/* 1362 */     return forType(type, variableResolver);
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
/*      */   public static ResolvableType forType(ParameterizedTypeReference<?> typeReference) {
/* 1375 */     return forType(typeReference.getType(), null, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static ResolvableType forType(@Nullable Type type, @Nullable VariableResolver variableResolver) {
/* 1386 */     return forType(type, null, variableResolver);
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
/*      */   static ResolvableType forType(@Nullable Type type, @Nullable SerializableTypeWrapper.TypeProvider typeProvider, @Nullable VariableResolver variableResolver) {
/* 1400 */     if (type == null && typeProvider != null) {
/* 1401 */       type = SerializableTypeWrapper.forTypeProvider(typeProvider);
/*      */     }
/* 1403 */     if (type == null) {
/* 1404 */       return NONE;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1409 */     if (type instanceof Class) {
/* 1410 */       return new ResolvableType(type, typeProvider, variableResolver, (ResolvableType)null);
/*      */     }
/*      */ 
/*      */     
/* 1414 */     cache.purgeUnreferencedEntries();
/*      */ 
/*      */     
/* 1417 */     ResolvableType resultType = new ResolvableType(type, typeProvider, variableResolver);
/* 1418 */     ResolvableType cachedType = (ResolvableType)cache.get(resultType);
/* 1419 */     if (cachedType == null) {
/* 1420 */       cachedType = new ResolvableType(type, typeProvider, variableResolver, resultType.hash);
/* 1421 */       cache.put(cachedType, cachedType);
/*      */     } 
/* 1423 */     resultType.resolved = cachedType.resolved;
/* 1424 */     return resultType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void clearCache() {
/* 1432 */     cache.clear();
/* 1433 */     SerializableTypeWrapper.cache.clear();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static interface VariableResolver
/*      */     extends Serializable
/*      */   {
/*      */     Object getSource();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     ResolvableType resolveVariable(TypeVariable<?> param1TypeVariable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class DefaultVariableResolver
/*      */     implements VariableResolver
/*      */   {
/*      */     private DefaultVariableResolver() {}
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public ResolvableType resolveVariable(TypeVariable<?> variable) {
/* 1463 */       return ResolvableType.this.resolveVariable(variable);
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getSource() {
/* 1468 */       return ResolvableType.this;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class TypeVariablesVariableResolver
/*      */     implements VariableResolver
/*      */   {
/*      */     private final TypeVariable<?>[] variables;
/*      */     
/*      */     private final ResolvableType[] generics;
/*      */     
/*      */     public TypeVariablesVariableResolver(TypeVariable<?>[] variables, ResolvableType[] generics) {
/* 1481 */       this.variables = variables;
/* 1482 */       this.generics = generics;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public ResolvableType resolveVariable(TypeVariable<?> variable) {
/* 1488 */       for (int i = 0; i < this.variables.length; i++) {
/* 1489 */         TypeVariable<?> v1 = SerializableTypeWrapper.<TypeVariable>unwrap(this.variables[i]);
/* 1490 */         TypeVariable<?> v2 = SerializableTypeWrapper.<TypeVariable>unwrap(variable);
/* 1491 */         if (ObjectUtils.nullSafeEquals(v1, v2)) {
/* 1492 */           return this.generics[i];
/*      */         }
/*      */       } 
/* 1495 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getSource() {
/* 1500 */       return this.generics;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class SyntheticParameterizedType
/*      */     implements ParameterizedType, Serializable
/*      */   {
/*      */     private final Type rawType;
/*      */     private final Type[] typeArguments;
/*      */     
/*      */     public SyntheticParameterizedType(Type rawType, Type[] typeArguments) {
/* 1512 */       this.rawType = rawType;
/* 1513 */       this.typeArguments = typeArguments;
/*      */     }
/*      */ 
/*      */     
/*      */     public String getTypeName() {
/* 1518 */       StringBuilder result = new StringBuilder(this.rawType.getTypeName());
/* 1519 */       if (this.typeArguments.length > 0) {
/* 1520 */         result.append('<');
/* 1521 */         for (int i = 0; i < this.typeArguments.length; i++) {
/* 1522 */           if (i > 0) {
/* 1523 */             result.append(", ");
/*      */           }
/* 1525 */           result.append(this.typeArguments[i].getTypeName());
/*      */         } 
/* 1527 */         result.append('>');
/*      */       } 
/* 1529 */       return result.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public Type getOwnerType() {
/* 1535 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public Type getRawType() {
/* 1540 */       return this.rawType;
/*      */     }
/*      */ 
/*      */     
/*      */     public Type[] getActualTypeArguments() {
/* 1545 */       return this.typeArguments;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object other) {
/* 1550 */       if (this == other) {
/* 1551 */         return true;
/*      */       }
/* 1553 */       if (!(other instanceof ParameterizedType)) {
/* 1554 */         return false;
/*      */       }
/* 1556 */       ParameterizedType otherType = (ParameterizedType)other;
/* 1557 */       return (otherType.getOwnerType() == null && this.rawType.equals(otherType.getRawType()) && 
/* 1558 */         Arrays.equals((Object[])this.typeArguments, (Object[])otherType.getActualTypeArguments()));
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1563 */       return this.rawType.hashCode() * 31 + Arrays.hashCode((Object[])this.typeArguments);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1568 */       return getTypeName();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class WildcardBounds
/*      */   {
/*      */     private final Kind kind;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final ResolvableType[] bounds;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public WildcardBounds(Kind kind, ResolvableType[] bounds) {
/* 1589 */       this.kind = kind;
/* 1590 */       this.bounds = bounds;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isSameKind(WildcardBounds bounds) {
/* 1597 */       return (this.kind == bounds.kind);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isAssignableFrom(ResolvableType... types) {
/* 1606 */       for (ResolvableType bound : this.bounds) {
/* 1607 */         for (ResolvableType type : types) {
/* 1608 */           if (!isAssignable(bound, type)) {
/* 1609 */             return false;
/*      */           }
/*      */         } 
/*      */       } 
/* 1613 */       return true;
/*      */     }
/*      */     
/*      */     private boolean isAssignable(ResolvableType source, ResolvableType from) {
/* 1617 */       return (this.kind == Kind.UPPER) ? source.isAssignableFrom(from) : from.isAssignableFrom(source);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ResolvableType[] getBounds() {
/* 1624 */       return this.bounds;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public static WildcardBounds get(ResolvableType type) {
/* 1635 */       ResolvableType resolveToWildcard = type;
/* 1636 */       while (!(resolveToWildcard.getType() instanceof WildcardType)) {
/* 1637 */         if (resolveToWildcard == ResolvableType.NONE) {
/* 1638 */           return null;
/*      */         }
/* 1640 */         resolveToWildcard = resolveToWildcard.resolveType();
/*      */       } 
/* 1642 */       WildcardType wildcardType = (WildcardType)resolveToWildcard.type;
/* 1643 */       Kind boundsType = ((wildcardType.getLowerBounds()).length > 0) ? Kind.LOWER : Kind.UPPER;
/* 1644 */       Type[] bounds = (boundsType == Kind.UPPER) ? wildcardType.getUpperBounds() : wildcardType.getLowerBounds();
/* 1645 */       ResolvableType[] resolvableBounds = new ResolvableType[bounds.length];
/* 1646 */       for (int i = 0; i < bounds.length; i++) {
/* 1647 */         resolvableBounds[i] = ResolvableType.forType(bounds[i], type.variableResolver);
/*      */       }
/* 1649 */       return new WildcardBounds(boundsType, resolvableBounds);
/*      */     }
/*      */ 
/*      */     
/*      */     enum Kind
/*      */     {
/* 1655 */       UPPER, LOWER;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class EmptyType
/*      */     implements Type, Serializable
/*      */   {
/* 1665 */     static final Type INSTANCE = new EmptyType();
/*      */     
/*      */     Object readResolve() {
/* 1668 */       return INSTANCE;
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/ResolvableType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */