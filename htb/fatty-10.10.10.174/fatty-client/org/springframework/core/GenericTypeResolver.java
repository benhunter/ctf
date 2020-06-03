/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class GenericTypeResolver
/*     */ {
/*  48 */   private static final Map<Class<?>, Map<TypeVariable, Type>> typeVariableCache = (Map<Class<?>, Map<TypeVariable, Type>>)new ConcurrentReferenceHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<?> resolveParameterType(MethodParameter methodParameter, Class<?> implementationClass) {
/*  62 */     Assert.notNull(methodParameter, "MethodParameter must not be null");
/*  63 */     Assert.notNull(implementationClass, "Class must not be null");
/*  64 */     methodParameter.setContainingClass(implementationClass);
/*  65 */     ResolvableType.resolveMethodParameter(methodParameter);
/*  66 */     return methodParameter.getParameterType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<?> resolveReturnType(Method method, Class<?> clazz) {
/*  77 */     Assert.notNull(method, "Method must not be null");
/*  78 */     Assert.notNull(clazz, "Class must not be null");
/*  79 */     return ResolvableType.forMethodReturnType(method, clazz).resolve(method.getReturnType());
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
/*     */   public static Class<?> resolveReturnTypeArgument(Method method, Class<?> genericIfc) {
/*  93 */     Assert.notNull(method, "Method must not be null");
/*  94 */     ResolvableType resolvableType = ResolvableType.forMethodReturnType(method).as(genericIfc);
/*  95 */     if (!resolvableType.hasGenerics() || resolvableType.getType() instanceof java.lang.reflect.WildcardType) {
/*  96 */       return null;
/*     */     }
/*  98 */     return getSingleGeneric(resolvableType);
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
/*     */   public static Class<?> resolveTypeArgument(Class<?> clazz, Class<?> genericIfc) {
/* 111 */     ResolvableType resolvableType = ResolvableType.forClass(clazz).as(genericIfc);
/* 112 */     if (!resolvableType.hasGenerics()) {
/* 113 */       return null;
/*     */     }
/* 115 */     return getSingleGeneric(resolvableType);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static Class<?> getSingleGeneric(ResolvableType resolvableType) {
/* 120 */     Assert.isTrue(((resolvableType.getGenerics()).length == 1), () -> "Expected 1 type argument on generic interface [" + resolvableType + "] but found " + (resolvableType.getGenerics()).length);
/*     */ 
/*     */     
/* 123 */     return resolvableType.getGeneric(new int[0]).resolve();
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
/*     */   public static Class<?>[] resolveTypeArguments(Class<?> clazz, Class<?> genericIfc) {
/* 138 */     ResolvableType type = ResolvableType.forClass(clazz).as(genericIfc);
/* 139 */     if (!type.hasGenerics() || type.isEntirelyUnresolvable()) {
/* 140 */       return null;
/*     */     }
/* 142 */     return type.resolveGenerics(Object.class);
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
/*     */   public static Type resolveType(Type genericType, @Nullable Class<?> contextClass) {
/* 155 */     if (contextClass != null) {
/* 156 */       if (genericType instanceof TypeVariable) {
/* 157 */         ResolvableType resolvedTypeVariable = resolveVariable((TypeVariable)genericType, 
/* 158 */             ResolvableType.forClass(contextClass));
/* 159 */         if (resolvedTypeVariable != ResolvableType.NONE) {
/* 160 */           Class<?> resolved = resolvedTypeVariable.resolve();
/* 161 */           if (resolved != null) {
/* 162 */             return resolved;
/*     */           }
/*     */         }
/*     */       
/* 166 */       } else if (genericType instanceof ParameterizedType) {
/* 167 */         ResolvableType resolvedType = ResolvableType.forType(genericType);
/* 168 */         if (resolvedType.hasUnresolvableGenerics()) {
/* 169 */           ParameterizedType parameterizedType = (ParameterizedType)genericType;
/* 170 */           Class<?>[] generics = new Class[(parameterizedType.getActualTypeArguments()).length];
/* 171 */           Type[] typeArguments = parameterizedType.getActualTypeArguments();
/* 172 */           for (int i = 0; i < typeArguments.length; i++) {
/* 173 */             Type typeArgument = typeArguments[i];
/* 174 */             if (typeArgument instanceof TypeVariable) {
/* 175 */               ResolvableType resolvedTypeArgument = resolveVariable((TypeVariable)typeArgument, 
/* 176 */                   ResolvableType.forClass(contextClass));
/* 177 */               if (resolvedTypeArgument != ResolvableType.NONE) {
/* 178 */                 generics[i] = resolvedTypeArgument.resolve();
/*     */               } else {
/*     */                 
/* 181 */                 generics[i] = ResolvableType.forType(typeArgument).resolve();
/*     */               } 
/*     */             } else {
/*     */               
/* 185 */               generics[i] = ResolvableType.forType(typeArgument).resolve();
/*     */             } 
/*     */           } 
/* 188 */           Class<?> rawClass = resolvedType.getRawClass();
/* 189 */           if (rawClass != null) {
/* 190 */             return ResolvableType.forClassWithGenerics(rawClass, generics).getType();
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/* 195 */     return genericType;
/*     */   }
/*     */ 
/*     */   
/*     */   private static ResolvableType resolveVariable(TypeVariable<?> typeVariable, ResolvableType contextType) {
/* 200 */     if (contextType.hasGenerics()) {
/* 201 */       ResolvableType resolvedType = ResolvableType.forType(typeVariable, contextType);
/* 202 */       if (resolvedType.resolve() != null) {
/* 203 */         return resolvedType;
/*     */       }
/*     */     } 
/*     */     
/* 207 */     ResolvableType superType = contextType.getSuperType();
/* 208 */     if (superType != ResolvableType.NONE) {
/* 209 */       ResolvableType resolvedType = resolveVariable(typeVariable, superType);
/* 210 */       if (resolvedType.resolve() != null) {
/* 211 */         return resolvedType;
/*     */       }
/*     */     } 
/* 214 */     for (ResolvableType ifc : contextType.getInterfaces()) {
/* 215 */       ResolvableType resolvedType = resolveVariable(typeVariable, ifc);
/* 216 */       if (resolvedType.resolve() != null) {
/* 217 */         return resolvedType;
/*     */       }
/*     */     } 
/* 220 */     return ResolvableType.NONE;
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
/*     */   public static Class<?> resolveType(Type genericType, Map<TypeVariable, Type> map) {
/* 232 */     return ResolvableType.forType(genericType, new TypeVariableMapVariableResolver(map)).toClass();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<TypeVariable, Type> getTypeVariableMap(Class<?> clazz) {
/* 243 */     Map<TypeVariable, Type> typeVariableMap = typeVariableCache.get(clazz);
/* 244 */     if (typeVariableMap == null) {
/* 245 */       typeVariableMap = new HashMap<>();
/* 246 */       buildTypeVariableMap(ResolvableType.forClass(clazz), typeVariableMap);
/* 247 */       typeVariableCache.put(clazz, Collections.unmodifiableMap(typeVariableMap));
/*     */     } 
/* 249 */     return typeVariableMap;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void buildTypeVariableMap(ResolvableType type, Map<TypeVariable, Type> typeVariableMap) {
/* 254 */     if (type != ResolvableType.NONE) {
/* 255 */       Class<?> resolved = type.resolve();
/* 256 */       if (resolved != null && type.getType() instanceof ParameterizedType) {
/* 257 */         TypeVariable[] arrayOfTypeVariable = (TypeVariable[])resolved.getTypeParameters();
/* 258 */         for (int i = 0; i < arrayOfTypeVariable.length; i++) {
/* 259 */           ResolvableType generic = type.getGeneric(new int[] { i });
/* 260 */           while (generic.getType() instanceof TypeVariable) {
/* 261 */             generic = generic.resolveType();
/*     */           }
/* 263 */           if (generic != ResolvableType.NONE) {
/* 264 */             typeVariableMap.put(arrayOfTypeVariable[i], generic.getType());
/*     */           }
/*     */         } 
/*     */       } 
/* 268 */       buildTypeVariableMap(type.getSuperType(), typeVariableMap);
/* 269 */       for (ResolvableType interfaceType : type.getInterfaces()) {
/* 270 */         buildTypeVariableMap(interfaceType, typeVariableMap);
/*     */       }
/* 272 */       if (resolved != null && resolved.isMemberClass()) {
/* 273 */         buildTypeVariableMap(ResolvableType.forClass(resolved.getEnclosingClass()), typeVariableMap);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static class TypeVariableMapVariableResolver
/*     */     implements ResolvableType.VariableResolver
/*     */   {
/*     */     private final Map<TypeVariable, Type> typeVariableMap;
/*     */     
/*     */     public TypeVariableMapVariableResolver(Map<TypeVariable, Type> typeVariableMap) {
/* 285 */       this.typeVariableMap = typeVariableMap;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public ResolvableType resolveVariable(TypeVariable<?> variable) {
/* 291 */       Type type = this.typeVariableMap.get(variable);
/* 292 */       return (type != null) ? ResolvableType.forType(type) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getSource() {
/* 297 */       return this.typeVariableMap;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/GenericTypeResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */