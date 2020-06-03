/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Executable;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.factory.ObjectFactory;
/*     */ import org.springframework.beans.factory.config.TypedStringValue;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AutowireUtils
/*     */ {
/*     */   private static final Comparator<Executable> EXECUTABLE_COMPARATOR;
/*     */   
/*     */   static {
/*  54 */     EXECUTABLE_COMPARATOR = ((e1, e2) -> {
/*     */         boolean p1 = Modifier.isPublic(e1.getModifiers());
/*     */         boolean p2 = Modifier.isPublic(e2.getModifiers());
/*     */         if (p1 != p2) {
/*     */           return p1 ? -1 : 1;
/*     */         }
/*     */         int c1pl = e1.getParameterCount();
/*     */         int c2pl = e2.getParameterCount();
/*     */         return Integer.compare(c2pl, c1pl);
/*     */       });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortConstructors(Constructor<?>[] constructors) {
/*  74 */     Arrays.sort((Executable[])constructors, EXECUTABLE_COMPARATOR);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortFactoryMethods(Method[] factoryMethods) {
/*  85 */     Arrays.sort((Executable[])factoryMethods, EXECUTABLE_COMPARATOR);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isExcludedFromDependencyCheck(PropertyDescriptor pd) {
/*  95 */     Method wm = pd.getWriteMethod();
/*  96 */     if (wm == null) {
/*  97 */       return false;
/*     */     }
/*  99 */     if (!wm.getDeclaringClass().getName().contains("$$"))
/*     */     {
/* 101 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 105 */     Class<?> superclass = wm.getDeclaringClass().getSuperclass();
/* 106 */     return !ClassUtils.hasMethod(superclass, wm.getName(), wm.getParameterTypes());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isSetterDefinedInInterface(PropertyDescriptor pd, Set<Class<?>> interfaces) {
/* 117 */     Method setter = pd.getWriteMethod();
/* 118 */     if (setter != null) {
/* 119 */       Class<?> targetClass = setter.getDeclaringClass();
/* 120 */       for (Class<?> ifc : interfaces) {
/* 121 */         if (ifc.isAssignableFrom(targetClass) && 
/* 122 */           ClassUtils.hasMethod(ifc, setter.getName(), setter.getParameterTypes())) {
/* 123 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 127 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object resolveAutowiringValue(Object autowiringValue, Class<?> requiredType) {
/* 138 */     if (autowiringValue instanceof ObjectFactory && !requiredType.isInstance(autowiringValue)) {
/* 139 */       ObjectFactory<?> factory = (ObjectFactory)autowiringValue;
/* 140 */       if (autowiringValue instanceof Serializable && requiredType.isInterface()) {
/* 141 */         autowiringValue = Proxy.newProxyInstance(requiredType.getClassLoader(), new Class[] { requiredType }, new ObjectFactoryDelegatingInvocationHandler(factory));
/*     */       }
/*     */       else {
/*     */         
/* 145 */         return factory.getObject();
/*     */       } 
/*     */     } 
/* 148 */     return autowiringValue;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<?> resolveReturnTypeForFactoryMethod(Method method, Object[] args, @Nullable ClassLoader classLoader) {
/* 185 */     Assert.notNull(method, "Method must not be null");
/* 186 */     Assert.notNull(args, "Argument array must not be null");
/*     */     
/* 188 */     TypeVariable[] arrayOfTypeVariable = (TypeVariable[])method.getTypeParameters();
/* 189 */     Type genericReturnType = method.getGenericReturnType();
/* 190 */     Type[] methodParameterTypes = method.getGenericParameterTypes();
/* 191 */     Assert.isTrue((args.length == methodParameterTypes.length), "Argument array does not match parameter count");
/*     */ 
/*     */ 
/*     */     
/* 195 */     boolean locallyDeclaredTypeVariableMatchesReturnType = false;
/* 196 */     for (TypeVariable<Method> currentTypeVariable : arrayOfTypeVariable) {
/* 197 */       if (currentTypeVariable.equals(genericReturnType)) {
/* 198 */         locallyDeclaredTypeVariableMatchesReturnType = true;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 203 */     if (locallyDeclaredTypeVariableMatchesReturnType) {
/* 204 */       for (int i = 0; i < methodParameterTypes.length; i++) {
/* 205 */         Type methodParameterType = methodParameterTypes[i];
/* 206 */         Object arg = args[i];
/* 207 */         if (methodParameterType.equals(genericReturnType)) {
/* 208 */           if (arg instanceof TypedStringValue) {
/* 209 */             TypedStringValue typedValue = (TypedStringValue)arg;
/* 210 */             if (typedValue.hasTargetType()) {
/* 211 */               return typedValue.getTargetType();
/*     */             }
/*     */             try {
/* 214 */               Class<?> resolvedType = typedValue.resolveTargetType(classLoader);
/* 215 */               if (resolvedType != null) {
/* 216 */                 return resolvedType;
/*     */               }
/*     */             }
/* 219 */             catch (ClassNotFoundException ex) {
/* 220 */               throw new IllegalStateException("Failed to resolve value type [" + typedValue
/* 221 */                   .getTargetTypeName() + "] for factory method argument", ex);
/*     */             }
/*     */           
/* 224 */           } else if (arg != null && !(arg instanceof org.springframework.beans.BeanMetadataElement)) {
/*     */             
/* 226 */             return arg.getClass();
/*     */           } 
/* 228 */           return method.getReturnType();
/*     */         } 
/* 230 */         if (methodParameterType instanceof ParameterizedType) {
/* 231 */           ParameterizedType parameterizedType = (ParameterizedType)methodParameterType;
/* 232 */           Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
/* 233 */           for (Type typeArg : actualTypeArguments) {
/* 234 */             if (typeArg.equals(genericReturnType)) {
/* 235 */               if (arg instanceof Class) {
/* 236 */                 return (Class)arg;
/*     */               }
/*     */               
/* 239 */               String className = null;
/* 240 */               if (arg instanceof String) {
/* 241 */                 className = (String)arg;
/*     */               }
/* 243 */               else if (arg instanceof TypedStringValue) {
/* 244 */                 TypedStringValue typedValue = (TypedStringValue)arg;
/* 245 */                 String targetTypeName = typedValue.getTargetTypeName();
/* 246 */                 if (targetTypeName == null || Class.class.getName().equals(targetTypeName)) {
/* 247 */                   className = typedValue.getValue();
/*     */                 }
/*     */               } 
/* 250 */               if (className != null) {
/*     */                 try {
/* 252 */                   return ClassUtils.forName(className, classLoader);
/*     */                 }
/* 254 */                 catch (ClassNotFoundException ex) {
/* 255 */                   throw new IllegalStateException("Could not resolve class name [" + arg + "] for factory method argument", ex);
/*     */                 } 
/*     */               }
/*     */ 
/*     */ 
/*     */               
/* 261 */               return method.getReturnType();
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 270 */     return method.getReturnType();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ObjectFactoryDelegatingInvocationHandler
/*     */     implements InvocationHandler, Serializable
/*     */   {
/*     */     private final ObjectFactory<?> objectFactory;
/*     */ 
/*     */ 
/*     */     
/*     */     public ObjectFactoryDelegatingInvocationHandler(ObjectFactory<?> objectFactory) {
/* 283 */       this.objectFactory = objectFactory;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 288 */       String methodName = method.getName();
/* 289 */       if (methodName.equals("equals"))
/*     */       {
/* 291 */         return Boolean.valueOf((proxy == args[0]));
/*     */       }
/* 293 */       if (methodName.equals("hashCode"))
/*     */       {
/* 295 */         return Integer.valueOf(System.identityHashCode(proxy));
/*     */       }
/* 297 */       if (methodName.equals("toString")) {
/* 298 */         return this.objectFactory.toString();
/*     */       }
/*     */       try {
/* 301 */         return method.invoke(this.objectFactory.getObject(), args);
/*     */       }
/* 303 */       catch (InvocationTargetException ex) {
/* 304 */         throw ex.getTargetException();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/AutowireUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */