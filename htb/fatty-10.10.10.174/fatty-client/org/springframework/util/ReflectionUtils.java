/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.UndeclaredThrowableException;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ReflectionUtils
/*     */ {
/*     */   @Deprecated
/*     */   public static final MethodFilter NON_BRIDGED_METHODS;
/*     */   public static final MethodFilter USER_DECLARED_METHODS;
/*     */   public static final FieldFilter COPYABLE_FIELDS;
/*     */   private static final String CGLIB_RENAMED_METHOD_PREFIX = "CGLIB$";
/*     */   
/*     */   static {
/*  55 */     NON_BRIDGED_METHODS = (method -> !method.isBridge());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  63 */     USER_DECLARED_METHODS = (method -> 
/*  64 */       (!method.isBridge() && !method.isSynthetic() && method.getDeclaringClass() != Object.class));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  69 */     COPYABLE_FIELDS = (field -> 
/*  70 */       (!Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  79 */   private static final Method[] EMPTY_METHOD_ARRAY = new Method[0];
/*     */   
/*  81 */   private static final Field[] EMPTY_FIELD_ARRAY = new Field[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   private static final Map<Class<?>, Method[]> declaredMethodsCache = (Map)new ConcurrentReferenceHashMap<>(256);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   private static final Map<Class<?>, Field[]> declaredFieldsCache = (Map)new ConcurrentReferenceHashMap<>(256);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void handleReflectionException(Exception ex) {
/* 108 */     if (ex instanceof NoSuchMethodException) {
/* 109 */       throw new IllegalStateException("Method not found: " + ex.getMessage());
/*     */     }
/* 111 */     if (ex instanceof IllegalAccessException) {
/* 112 */       throw new IllegalStateException("Could not access method: " + ex.getMessage());
/*     */     }
/* 114 */     if (ex instanceof InvocationTargetException) {
/* 115 */       handleInvocationTargetException((InvocationTargetException)ex);
/*     */     }
/* 117 */     if (ex instanceof RuntimeException) {
/* 118 */       throw (RuntimeException)ex;
/*     */     }
/* 120 */     throw new UndeclaredThrowableException(ex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void handleInvocationTargetException(InvocationTargetException ex) {
/* 131 */     rethrowRuntimeException(ex.getTargetException());
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
/*     */   public static void rethrowRuntimeException(Throwable ex) {
/* 146 */     if (ex instanceof RuntimeException) {
/* 147 */       throw (RuntimeException)ex;
/*     */     }
/* 149 */     if (ex instanceof Error) {
/* 150 */       throw (Error)ex;
/*     */     }
/* 152 */     throw new UndeclaredThrowableException(ex);
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
/*     */   public static void rethrowException(Throwable ex) throws Exception {
/* 167 */     if (ex instanceof Exception) {
/* 168 */       throw (Exception)ex;
/*     */     }
/* 170 */     if (ex instanceof Error) {
/* 171 */       throw (Error)ex;
/*     */     }
/* 173 */     throw new UndeclaredThrowableException(ex);
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
/*     */   public static <T> Constructor<T> accessibleConstructor(Class<T> clazz, Class<?>... parameterTypes) throws NoSuchMethodException {
/* 190 */     Constructor<T> ctor = clazz.getDeclaredConstructor(parameterTypes);
/* 191 */     makeAccessible(ctor);
/* 192 */     return ctor;
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
/*     */   public static void makeAccessible(Constructor<?> ctor) {
/* 205 */     if ((!Modifier.isPublic(ctor.getModifiers()) || 
/* 206 */       !Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) && !ctor.isAccessible()) {
/* 207 */       ctor.setAccessible(true);
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
/*     */   @Nullable
/*     */   public static Method findMethod(Class<?> clazz, String name) {
/* 224 */     return findMethod(clazz, name, new Class[0]);
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
/*     */   public static Method findMethod(Class<?> clazz, String name, @Nullable Class<?>... paramTypes) {
/* 239 */     Assert.notNull(clazz, "Class must not be null");
/* 240 */     Assert.notNull(name, "Method name must not be null");
/* 241 */     Class<?> searchType = clazz;
/* 242 */     while (searchType != null) {
/* 243 */       Method[] methods = searchType.isInterface() ? searchType.getMethods() : getDeclaredMethods(searchType);
/* 244 */       for (Method method : methods) {
/* 245 */         if (name.equals(method.getName()) && (paramTypes == null || 
/* 246 */           Arrays.equals((Object[])paramTypes, (Object[])method.getParameterTypes()))) {
/* 247 */           return method;
/*     */         }
/*     */       } 
/* 250 */       searchType = searchType.getSuperclass();
/*     */     } 
/* 252 */     return null;
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
/*     */   public static Object invokeMethod(Method method, @Nullable Object target) {
/* 266 */     return invokeMethod(method, target, new Object[0]);
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
/*     */   public static Object invokeMethod(Method method, @Nullable Object target, @Nullable Object... args) {
/*     */     try {
/* 282 */       return method.invoke(target, args);
/*     */     }
/* 284 */     catch (Exception ex) {
/* 285 */       handleReflectionException(ex);
/*     */       
/* 287 */       throw new IllegalStateException("Should never get here");
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
/*     */   @Deprecated
/*     */   @Nullable
/*     */   public static Object invokeJdbcMethod(Method method, @Nullable Object target) throws SQLException {
/* 303 */     return invokeJdbcMethod(method, target, new Object[0]);
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
/*     */   @Deprecated
/*     */   @Nullable
/*     */   public static Object invokeJdbcMethod(Method method, @Nullable Object target, @Nullable Object... args) throws SQLException {
/*     */     try {
/* 322 */       return method.invoke(target, args);
/*     */     }
/* 324 */     catch (IllegalAccessException ex) {
/* 325 */       handleReflectionException(ex);
/*     */     }
/* 327 */     catch (InvocationTargetException ex) {
/* 328 */       if (ex.getTargetException() instanceof SQLException) {
/* 329 */         throw (SQLException)ex.getTargetException();
/*     */       }
/* 331 */       handleInvocationTargetException(ex);
/*     */     } 
/* 333 */     throw new IllegalStateException("Should never get here");
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
/*     */   public static boolean declaresException(Method method, Class<?> exceptionType) {
/* 346 */     Assert.notNull(method, "Method must not be null");
/* 347 */     Class<?>[] declaredExceptions = method.getExceptionTypes();
/* 348 */     for (Class<?> declaredException : declaredExceptions) {
/* 349 */       if (declaredException.isAssignableFrom(exceptionType)) {
/* 350 */         return true;
/*     */       }
/*     */     } 
/* 353 */     return false;
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
/*     */   public static void doWithLocalMethods(Class<?> clazz, MethodCallback mc) {
/* 367 */     Method[] methods = getDeclaredMethods(clazz);
/* 368 */     for (Method method : methods) {
/*     */       try {
/* 370 */         mc.doWith(method);
/*     */       }
/* 372 */       catch (IllegalAccessException ex) {
/* 373 */         throw new IllegalStateException("Not allowed to access method '" + method.getName() + "': " + ex);
/*     */       } 
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
/*     */   public static void doWithMethods(Class<?> clazz, MethodCallback mc) {
/* 389 */     doWithMethods(clazz, mc, null);
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
/*     */   public static void doWithMethods(Class<?> clazz, MethodCallback mc, @Nullable MethodFilter mf) {
/* 404 */     Method[] methods = getDeclaredMethods(clazz);
/* 405 */     for (Method method : methods) {
/* 406 */       if (mf == null || mf.matches(method))
/*     */         
/*     */         try {
/*     */           
/* 410 */           mc.doWith(method);
/*     */         }
/* 412 */         catch (IllegalAccessException ex) {
/* 413 */           throw new IllegalStateException("Not allowed to access method '" + method.getName() + "': " + ex);
/*     */         }  
/*     */     } 
/* 416 */     if (clazz.getSuperclass() != null) {
/* 417 */       doWithMethods(clazz.getSuperclass(), mc, mf);
/*     */     }
/* 419 */     else if (clazz.isInterface()) {
/* 420 */       for (Class<?> superIfc : clazz.getInterfaces()) {
/* 421 */         doWithMethods(superIfc, mc, mf);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Method[] getAllDeclaredMethods(Class<?> leafClass) {
/* 433 */     List<Method> methods = new ArrayList<>(32);
/* 434 */     doWithMethods(leafClass, methods::add);
/* 435 */     return methods.<Method>toArray(EMPTY_METHOD_ARRAY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Method[] getUniqueDeclaredMethods(Class<?> leafClass) {
/* 446 */     List<Method> methods = new ArrayList<>(32);
/* 447 */     doWithMethods(leafClass, method -> {
/*     */           boolean knownSignature = false;
/*     */           
/*     */           Method methodBeingOverriddenWithCovariantReturnType = null;
/*     */           
/*     */           for (Method existingMethod : methods) {
/*     */             if (method.getName().equals(existingMethod.getName()) && Arrays.equals((Object[])method.getParameterTypes(), (Object[])existingMethod.getParameterTypes())) {
/*     */               if (existingMethod.getReturnType() != method.getReturnType() && existingMethod.getReturnType().isAssignableFrom(method.getReturnType())) {
/*     */                 methodBeingOverriddenWithCovariantReturnType = existingMethod;
/*     */                 
/*     */                 break;
/*     */               } 
/*     */               
/*     */               knownSignature = true;
/*     */               break;
/*     */             } 
/*     */           } 
/*     */           if (methodBeingOverriddenWithCovariantReturnType != null) {
/*     */             methods.remove(methodBeingOverriddenWithCovariantReturnType);
/*     */           }
/*     */           if (!knownSignature && !isCglibRenamedMethod(method)) {
/*     */             methods.add(method);
/*     */           }
/*     */         });
/* 471 */     return methods.<Method>toArray(EMPTY_METHOD_ARRAY);
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
/*     */   private static Method[] getDeclaredMethods(Class<?> clazz) {
/* 485 */     Assert.notNull(clazz, "Class must not be null");
/* 486 */     Method[] result = declaredMethodsCache.get(clazz);
/* 487 */     if (result == null) {
/*     */       try {
/* 489 */         Method[] declaredMethods = clazz.getDeclaredMethods();
/* 490 */         List<Method> defaultMethods = findConcreteMethodsOnInterfaces(clazz);
/* 491 */         if (defaultMethods != null) {
/* 492 */           result = new Method[declaredMethods.length + defaultMethods.size()];
/* 493 */           System.arraycopy(declaredMethods, 0, result, 0, declaredMethods.length);
/* 494 */           int index = declaredMethods.length;
/* 495 */           for (Method defaultMethod : defaultMethods) {
/* 496 */             result[index] = defaultMethod;
/* 497 */             index++;
/*     */           } 
/*     */         } else {
/*     */           
/* 501 */           result = declaredMethods;
/*     */         } 
/* 503 */         declaredMethodsCache.put(clazz, (result.length == 0) ? EMPTY_METHOD_ARRAY : result);
/*     */       }
/* 505 */       catch (Throwable ex) {
/* 506 */         throw new IllegalStateException("Failed to introspect Class [" + clazz.getName() + "] from ClassLoader [" + clazz
/* 507 */             .getClassLoader() + "]", ex);
/*     */       } 
/*     */     }
/* 510 */     return result;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static List<Method> findConcreteMethodsOnInterfaces(Class<?> clazz) {
/* 515 */     List<Method> result = null;
/* 516 */     for (Class<?> ifc : clazz.getInterfaces()) {
/* 517 */       for (Method ifcMethod : ifc.getMethods()) {
/* 518 */         if (!Modifier.isAbstract(ifcMethod.getModifiers())) {
/* 519 */           if (result == null) {
/* 520 */             result = new ArrayList<>();
/*     */           }
/* 522 */           result.add(ifcMethod);
/*     */         } 
/*     */       } 
/*     */     } 
/* 526 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEqualsMethod(@Nullable Method method) {
/* 534 */     if (method == null || !method.getName().equals("equals")) {
/* 535 */       return false;
/*     */     }
/* 537 */     Class<?>[] paramTypes = method.getParameterTypes();
/* 538 */     return (paramTypes.length == 1 && paramTypes[0] == Object.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isHashCodeMethod(@Nullable Method method) {
/* 546 */     return (method != null && method.getName().equals("hashCode") && method.getParameterCount() == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isToStringMethod(@Nullable Method method) {
/* 554 */     return (method != null && method.getName().equals("toString") && method.getParameterCount() == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isObjectMethod(@Nullable Method method) {
/* 561 */     return (method != null && (method.getDeclaringClass() == Object.class || 
/* 562 */       isEqualsMethod(method) || isHashCodeMethod(method) || isToStringMethod(method)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isCglibRenamedMethod(Method renamedMethod) {
/* 571 */     String name = renamedMethod.getName();
/* 572 */     if (name.startsWith("CGLIB$")) {
/* 573 */       int i = name.length() - 1;
/* 574 */       while (i >= 0 && Character.isDigit(name.charAt(i))) {
/* 575 */         i--;
/*     */       }
/* 577 */       return (i > "CGLIB$".length() && i < name.length() - 1 && name.charAt(i) == '$');
/*     */     } 
/* 579 */     return false;
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
/*     */   public static void makeAccessible(Method method) {
/* 592 */     if ((!Modifier.isPublic(method.getModifiers()) || 
/* 593 */       !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
/* 594 */       method.setAccessible(true);
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
/*     */   @Nullable
/*     */   public static Field findField(Class<?> clazz, String name) {
/* 610 */     return findField(clazz, name, null);
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
/*     */   public static Field findField(Class<?> clazz, @Nullable String name, @Nullable Class<?> type) {
/* 624 */     Assert.notNull(clazz, "Class must not be null");
/* 625 */     Assert.isTrue((name != null || type != null), "Either name or type of the field must be specified");
/* 626 */     Class<?> searchType = clazz;
/* 627 */     while (Object.class != searchType && searchType != null) {
/* 628 */       Field[] fields = getDeclaredFields(searchType);
/* 629 */       for (Field field : fields) {
/* 630 */         if ((name == null || name.equals(field.getName())) && (type == null || type
/* 631 */           .equals(field.getType()))) {
/* 632 */           return field;
/*     */         }
/*     */       } 
/* 635 */       searchType = searchType.getSuperclass();
/*     */     } 
/* 637 */     return null;
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
/*     */   public static void setField(Field field, @Nullable Object target, @Nullable Object value) {
/*     */     try {
/* 652 */       field.set(target, value);
/*     */     }
/* 654 */     catch (IllegalAccessException ex) {
/* 655 */       handleReflectionException(ex);
/* 656 */       throw new IllegalStateException("Unexpected reflection exception - " + ex
/* 657 */           .getClass().getName() + ": " + ex.getMessage());
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
/*     */   @Nullable
/*     */   public static Object getField(Field field, @Nullable Object target) {
/*     */     try {
/* 674 */       return field.get(target);
/*     */     }
/* 676 */     catch (IllegalAccessException ex) {
/* 677 */       handleReflectionException(ex);
/* 678 */       throw new IllegalStateException("Unexpected reflection exception - " + ex
/* 679 */           .getClass().getName() + ": " + ex.getMessage());
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
/*     */   public static void doWithLocalFields(Class<?> clazz, FieldCallback fc) {
/* 692 */     for (Field field : getDeclaredFields(clazz)) {
/*     */       try {
/* 694 */         fc.doWith(field);
/*     */       }
/* 696 */       catch (IllegalAccessException ex) {
/* 697 */         throw new IllegalStateException("Not allowed to access field '" + field.getName() + "': " + ex);
/*     */       } 
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
/*     */   public static void doWithFields(Class<?> clazz, FieldCallback fc) {
/* 710 */     doWithFields(clazz, fc, null);
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
/*     */   public static void doWithFields(Class<?> clazz, FieldCallback fc, @Nullable FieldFilter ff) {
/* 723 */     Class<?> targetClass = clazz;
/*     */     do {
/* 725 */       Field[] fields = getDeclaredFields(targetClass);
/* 726 */       for (Field field : fields) {
/* 727 */         if (ff == null || ff.matches(field))
/*     */           
/*     */           try {
/*     */             
/* 731 */             fc.doWith(field);
/*     */           }
/* 733 */           catch (IllegalAccessException ex) {
/* 734 */             throw new IllegalStateException("Not allowed to access field '" + field.getName() + "': " + ex);
/*     */           }  
/*     */       } 
/* 737 */       targetClass = targetClass.getSuperclass();
/*     */     }
/* 739 */     while (targetClass != null && targetClass != Object.class);
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
/*     */   private static Field[] getDeclaredFields(Class<?> clazz) {
/* 751 */     Assert.notNull(clazz, "Class must not be null");
/* 752 */     Field[] result = declaredFieldsCache.get(clazz);
/* 753 */     if (result == null) {
/*     */       try {
/* 755 */         result = clazz.getDeclaredFields();
/* 756 */         declaredFieldsCache.put(clazz, (result.length == 0) ? EMPTY_FIELD_ARRAY : result);
/*     */       }
/* 758 */       catch (Throwable ex) {
/* 759 */         throw new IllegalStateException("Failed to introspect Class [" + clazz.getName() + "] from ClassLoader [" + clazz
/* 760 */             .getClassLoader() + "]", ex);
/*     */       } 
/*     */     }
/* 763 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void shallowCopyFieldState(Object src, Object dest) {
/* 773 */     Assert.notNull(src, "Source for field copy cannot be null");
/* 774 */     Assert.notNull(dest, "Destination for field copy cannot be null");
/* 775 */     if (!src.getClass().isAssignableFrom(dest.getClass())) {
/* 776 */       throw new IllegalArgumentException("Destination class [" + dest.getClass().getName() + "] must be same or subclass as source class [" + src
/* 777 */           .getClass().getName() + "]");
/*     */     }
/* 779 */     doWithFields(src.getClass(), field -> { makeAccessible(field); Object srcValue = field.get(src); field.set(dest, srcValue); }COPYABLE_FIELDS);
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
/*     */   public static boolean isPublicStaticFinal(Field field) {
/* 791 */     int modifiers = field.getModifiers();
/* 792 */     return (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers));
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
/*     */   public static void makeAccessible(Field field) {
/* 805 */     if ((!Modifier.isPublic(field.getModifiers()) || 
/* 806 */       !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || 
/* 807 */       Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
/* 808 */       field.setAccessible(true);
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
/*     */   public static void clearCache() {
/* 820 */     declaredMethodsCache.clear();
/* 821 */     declaredFieldsCache.clear();
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface FieldFilter {
/*     */     boolean matches(Field param1Field);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface FieldCallback {
/*     */     void doWith(Field param1Field) throws IllegalArgumentException, IllegalAccessException;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface MethodFilter {
/*     */     boolean matches(Method param1Method);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface MethodCallback {
/*     */     void doWith(Method param1Method) throws IllegalArgumentException, IllegalAccessException;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/ReflectionUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */