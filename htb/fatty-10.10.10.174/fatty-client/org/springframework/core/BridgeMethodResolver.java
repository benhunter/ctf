/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BridgeMethodResolver
/*     */ {
/*     */   public static Method findBridgedMethod(Method bridgeMethod) {
/*  64 */     if (!bridgeMethod.isBridge()) {
/*  65 */       return bridgeMethod;
/*     */     }
/*     */ 
/*     */     
/*  69 */     List<Method> candidateMethods = new ArrayList<>();
/*  70 */     Method[] methods = ReflectionUtils.getAllDeclaredMethods(bridgeMethod.getDeclaringClass());
/*  71 */     for (Method candidateMethod : methods) {
/*  72 */       if (isBridgedCandidateFor(candidateMethod, bridgeMethod)) {
/*  73 */         candidateMethods.add(candidateMethod);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  78 */     if (candidateMethods.size() == 1) {
/*  79 */       return candidateMethods.get(0);
/*     */     }
/*     */ 
/*     */     
/*  83 */     Method bridgedMethod = searchCandidates(candidateMethods, bridgeMethod);
/*  84 */     if (bridgedMethod != null)
/*     */     {
/*  86 */       return bridgedMethod;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  91 */     return bridgeMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isBridgedCandidateFor(Method candidateMethod, Method bridgeMethod) {
/* 102 */     return (!candidateMethod.isBridge() && !candidateMethod.equals(bridgeMethod) && candidateMethod
/* 103 */       .getName().equals(bridgeMethod.getName()) && candidateMethod
/* 104 */       .getParameterCount() == bridgeMethod.getParameterCount());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static Method searchCandidates(List<Method> candidateMethods, Method bridgeMethod) {
/* 115 */     if (candidateMethods.isEmpty()) {
/* 116 */       return null;
/*     */     }
/* 118 */     Method previousMethod = null;
/* 119 */     boolean sameSig = true;
/* 120 */     for (Method candidateMethod : candidateMethods) {
/* 121 */       if (isBridgeMethodFor(bridgeMethod, candidateMethod, bridgeMethod.getDeclaringClass())) {
/* 122 */         return candidateMethod;
/*     */       }
/* 124 */       if (previousMethod != null)
/*     */       {
/* 126 */         sameSig = (sameSig && Arrays.equals((Object[])candidateMethod.getGenericParameterTypes(), (Object[])previousMethod.getGenericParameterTypes()));
/*     */       }
/* 128 */       previousMethod = candidateMethod;
/*     */     } 
/* 130 */     return sameSig ? candidateMethods.get(0) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isBridgeMethodFor(Method bridgeMethod, Method candidateMethod, Class<?> declaringClass) {
/* 138 */     if (isResolvedTypeMatch(candidateMethod, bridgeMethod, declaringClass)) {
/* 139 */       return true;
/*     */     }
/* 141 */     Method method = findGenericDeclaration(bridgeMethod);
/* 142 */     return (method != null && isResolvedTypeMatch(method, candidateMethod, declaringClass));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isResolvedTypeMatch(Method genericMethod, Method candidateMethod, Class<?> declaringClass) {
/* 152 */     Type[] genericParameters = genericMethod.getGenericParameterTypes();
/* 153 */     Class<?>[] candidateParameters = candidateMethod.getParameterTypes();
/* 154 */     if (genericParameters.length != candidateParameters.length) {
/* 155 */       return false;
/*     */     }
/* 157 */     for (int i = 0; i < candidateParameters.length; i++) {
/* 158 */       ResolvableType genericParameter = ResolvableType.forMethodParameter(genericMethod, i, declaringClass);
/* 159 */       Class<?> candidateParameter = candidateParameters[i];
/* 160 */       if (candidateParameter.isArray())
/*     */       {
/* 162 */         if (!candidateParameter.getComponentType().equals(genericParameter.getComponentType().toClass())) {
/* 163 */           return false;
/*     */         }
/*     */       }
/*     */       
/* 167 */       if (!candidateParameter.equals(genericParameter.toClass())) {
/* 168 */         return false;
/*     */       }
/*     */     } 
/* 171 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static Method findGenericDeclaration(Method bridgeMethod) {
/* 182 */     Class<?> superclass = bridgeMethod.getDeclaringClass().getSuperclass();
/* 183 */     while (superclass != null && Object.class != superclass) {
/* 184 */       Method method = searchForMatch(superclass, bridgeMethod);
/* 185 */       if (method != null && !method.isBridge()) {
/* 186 */         return method;
/*     */       }
/* 188 */       superclass = superclass.getSuperclass();
/*     */     } 
/*     */     
/* 191 */     Class<?>[] interfaces = ClassUtils.getAllInterfacesForClass(bridgeMethod.getDeclaringClass());
/* 192 */     return searchInterfaces(interfaces, bridgeMethod);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static Method searchInterfaces(Class<?>[] interfaces, Method bridgeMethod) {
/* 197 */     for (Class<?> ifc : interfaces) {
/* 198 */       Method method = searchForMatch(ifc, bridgeMethod);
/* 199 */       if (method != null && !method.isBridge()) {
/* 200 */         return method;
/*     */       }
/*     */       
/* 203 */       method = searchInterfaces(ifc.getInterfaces(), bridgeMethod);
/* 204 */       if (method != null) {
/* 205 */         return method;
/*     */       }
/*     */     } 
/*     */     
/* 209 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static Method searchForMatch(Class<?> type, Method bridgeMethod) {
/*     */     try {
/* 220 */       return type.getDeclaredMethod(bridgeMethod.getName(), bridgeMethod.getParameterTypes());
/*     */     }
/* 222 */     catch (NoSuchMethodException ex) {
/* 223 */       return null;
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
/*     */   public static boolean isVisibilityBridgeMethodPair(Method bridgeMethod, Method bridgedMethod) {
/* 235 */     if (bridgeMethod == bridgedMethod) {
/* 236 */       return true;
/*     */     }
/* 238 */     return (bridgeMethod.getReturnType().equals(bridgedMethod.getReturnType()) && 
/* 239 */       Arrays.equals((Object[])bridgeMethod.getParameterTypes(), (Object[])bridgedMethod.getParameterTypes()));
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/BridgeMethodResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */