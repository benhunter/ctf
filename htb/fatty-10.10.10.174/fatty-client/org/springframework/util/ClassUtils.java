/*      */ package org.springframework.util;
/*      */ 
/*      */ import java.beans.Introspector;
/*      */ import java.io.Closeable;
/*      */ import java.io.Externalizable;
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.lang.reflect.Proxy;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.IdentityHashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import org.springframework.lang.Nullable;
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
/*      */ public abstract class ClassUtils
/*      */ {
/*      */   public static final String ARRAY_SUFFIX = "[]";
/*      */   private static final String INTERNAL_ARRAY_PREFIX = "[";
/*      */   private static final String NON_PRIMITIVE_ARRAY_PREFIX = "[L";
/*      */   private static final char PACKAGE_SEPARATOR = '.';
/*      */   private static final char PATH_SEPARATOR = '/';
/*      */   private static final char INNER_CLASS_SEPARATOR = '$';
/*      */   public static final String CGLIB_CLASS_SEPARATOR = "$$";
/*      */   public static final String CLASS_FILE_SUFFIX = ".class";
/*   87 */   private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new IdentityHashMap<>(8);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   93 */   private static final Map<Class<?>, Class<?>> primitiveTypeToWrapperMap = new IdentityHashMap<>(8);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   99 */   private static final Map<String, Class<?>> primitiveTypeNameMap = new HashMap<>(32);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  105 */   private static final Map<String, Class<?>> commonClassCache = new HashMap<>(64);
/*      */ 
/*      */ 
/*      */   
/*      */   private static final Set<Class<?>> javaLanguageInterfaces;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static {
/*  115 */     primitiveWrapperTypeMap.put(Boolean.class, boolean.class);
/*  116 */     primitiveWrapperTypeMap.put(Byte.class, byte.class);
/*  117 */     primitiveWrapperTypeMap.put(Character.class, char.class);
/*  118 */     primitiveWrapperTypeMap.put(Double.class, double.class);
/*  119 */     primitiveWrapperTypeMap.put(Float.class, float.class);
/*  120 */     primitiveWrapperTypeMap.put(Integer.class, int.class);
/*  121 */     primitiveWrapperTypeMap.put(Long.class, long.class);
/*  122 */     primitiveWrapperTypeMap.put(Short.class, short.class);
/*      */ 
/*      */     
/*  125 */     for (Map.Entry<Class<?>, Class<?>> entry : primitiveWrapperTypeMap.entrySet()) {
/*  126 */       primitiveTypeToWrapperMap.put(entry.getValue(), entry.getKey());
/*  127 */       registerCommonClasses(new Class[] { entry.getKey() });
/*      */     } 
/*      */     
/*  130 */     Set<Class<?>> primitiveTypes = new HashSet<>(32);
/*  131 */     primitiveTypes.addAll(primitiveWrapperTypeMap.values());
/*  132 */     Collections.addAll(primitiveTypes, new Class[] { boolean[].class, byte[].class, char[].class, double[].class, float[].class, int[].class, long[].class, short[].class });
/*      */     
/*  134 */     primitiveTypes.add(void.class);
/*  135 */     for (Class<?> primitiveType : primitiveTypes) {
/*  136 */       primitiveTypeNameMap.put(primitiveType.getName(), primitiveType);
/*      */     }
/*      */     
/*  139 */     registerCommonClasses(new Class[] { Boolean[].class, Byte[].class, Character[].class, Double[].class, Float[].class, Integer[].class, Long[].class, Short[].class });
/*      */     
/*  141 */     registerCommonClasses(new Class[] { Number.class, Number[].class, String.class, String[].class, Class.class, Class[].class, Object.class, Object[].class });
/*      */     
/*  143 */     registerCommonClasses(new Class[] { Throwable.class, Exception.class, RuntimeException.class, Error.class, StackTraceElement.class, StackTraceElement[].class });
/*      */     
/*  145 */     registerCommonClasses(new Class[] { Enum.class, Iterable.class, Iterator.class, Enumeration.class, Collection.class, List.class, Set.class, Map.class, Map.Entry.class, Optional.class });
/*      */ 
/*      */     
/*  148 */     Class<?>[] javaLanguageInterfaceArray = new Class[] { Serializable.class, Externalizable.class, Closeable.class, AutoCloseable.class, Cloneable.class, Comparable.class };
/*      */     
/*  150 */     registerCommonClasses(javaLanguageInterfaceArray);
/*  151 */     javaLanguageInterfaces = new HashSet<>(Arrays.asList(javaLanguageInterfaceArray));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void registerCommonClasses(Class<?>... commonClasses) {
/*  159 */     for (Class<?> clazz : commonClasses) {
/*  160 */       commonClassCache.put(clazz.getName(), clazz);
/*      */     }
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
/*      */   @Nullable
/*      */   public static ClassLoader getDefaultClassLoader() {
/*  180 */     ClassLoader cl = null;
/*      */     try {
/*  182 */       cl = Thread.currentThread().getContextClassLoader();
/*      */     }
/*  184 */     catch (Throwable throwable) {}
/*      */ 
/*      */     
/*  187 */     if (cl == null) {
/*      */       
/*  189 */       cl = ClassUtils.class.getClassLoader();
/*  190 */       if (cl == null) {
/*      */         
/*      */         try {
/*  193 */           cl = ClassLoader.getSystemClassLoader();
/*      */         }
/*  195 */         catch (Throwable throwable) {}
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  200 */     return cl;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static ClassLoader overrideThreadContextClassLoader(@Nullable ClassLoader classLoaderToUse) {
/*  212 */     Thread currentThread = Thread.currentThread();
/*  213 */     ClassLoader threadContextClassLoader = currentThread.getContextClassLoader();
/*  214 */     if (classLoaderToUse != null && !classLoaderToUse.equals(threadContextClassLoader)) {
/*  215 */       currentThread.setContextClassLoader(classLoaderToUse);
/*  216 */       return threadContextClassLoader;
/*      */     } 
/*      */     
/*  219 */     return null;
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
/*      */   public static Class<?> forName(String name, @Nullable ClassLoader classLoader) throws ClassNotFoundException, LinkageError {
/*  239 */     Assert.notNull(name, "Name must not be null");
/*      */     
/*  241 */     Class<?> clazz = resolvePrimitiveClassName(name);
/*  242 */     if (clazz == null) {
/*  243 */       clazz = commonClassCache.get(name);
/*      */     }
/*  245 */     if (clazz != null) {
/*  246 */       return clazz;
/*      */     }
/*      */ 
/*      */     
/*  250 */     if (name.endsWith("[]")) {
/*  251 */       String elementClassName = name.substring(0, name.length() - "[]".length());
/*  252 */       Class<?> elementClass = forName(elementClassName, classLoader);
/*  253 */       return Array.newInstance(elementClass, 0).getClass();
/*      */     } 
/*      */ 
/*      */     
/*  257 */     if (name.startsWith("[L") && name.endsWith(";")) {
/*  258 */       String elementName = name.substring("[L".length(), name.length() - 1);
/*  259 */       Class<?> elementClass = forName(elementName, classLoader);
/*  260 */       return Array.newInstance(elementClass, 0).getClass();
/*      */     } 
/*      */ 
/*      */     
/*  264 */     if (name.startsWith("[")) {
/*  265 */       String elementName = name.substring("[".length());
/*  266 */       Class<?> elementClass = forName(elementName, classLoader);
/*  267 */       return Array.newInstance(elementClass, 0).getClass();
/*      */     } 
/*      */     
/*  270 */     ClassLoader clToUse = classLoader;
/*  271 */     if (clToUse == null) {
/*  272 */       clToUse = getDefaultClassLoader();
/*      */     }
/*      */     try {
/*  275 */       return Class.forName(name, false, clToUse);
/*      */     }
/*  277 */     catch (ClassNotFoundException ex) {
/*  278 */       int lastDotIndex = name.lastIndexOf('.');
/*  279 */       if (lastDotIndex != -1) {
/*      */         
/*  281 */         String innerClassName = name.substring(0, lastDotIndex) + '$' + name.substring(lastDotIndex + 1);
/*      */         try {
/*  283 */           return Class.forName(innerClassName, false, clToUse);
/*      */         }
/*  285 */         catch (ClassNotFoundException classNotFoundException) {}
/*      */       } 
/*      */ 
/*      */       
/*  289 */       throw ex;
/*      */     } 
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
/*      */   public static Class<?> resolveClassName(String className, @Nullable ClassLoader classLoader) throws IllegalArgumentException {
/*      */     try {
/*  315 */       return forName(className, classLoader);
/*      */     }
/*  317 */     catch (IllegalAccessError err) {
/*  318 */       throw new IllegalStateException("Readability mismatch in inheritance hierarchy of class [" + className + "]: " + err
/*  319 */           .getMessage(), err);
/*      */     }
/*  321 */     catch (LinkageError err) {
/*  322 */       throw new IllegalArgumentException("Unresolvable class definition for class [" + className + "]", err);
/*      */     }
/*  324 */     catch (ClassNotFoundException ex) {
/*  325 */       throw new IllegalArgumentException("Could not find class [" + className + "]", ex);
/*      */     } 
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
/*      */   public static boolean isPresent(String className, @Nullable ClassLoader classLoader) {
/*      */     try {
/*  345 */       forName(className, classLoader);
/*  346 */       return true;
/*      */     }
/*  348 */     catch (IllegalAccessError err) {
/*  349 */       throw new IllegalStateException("Readability mismatch in inheritance hierarchy of class [" + className + "]: " + err
/*  350 */           .getMessage(), err);
/*      */     }
/*  352 */     catch (Throwable ex) {
/*      */       
/*  354 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isVisible(Class<?> clazz, @Nullable ClassLoader classLoader) {
/*  365 */     if (classLoader == null) {
/*  366 */       return true;
/*      */     }
/*      */     try {
/*  369 */       if (clazz.getClassLoader() == classLoader) {
/*  370 */         return true;
/*      */       }
/*      */     }
/*  373 */     catch (SecurityException securityException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  378 */     return isLoadable(clazz, classLoader);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isCacheSafe(Class<?> clazz, @Nullable ClassLoader classLoader) {
/*  389 */     Assert.notNull(clazz, "Class must not be null");
/*      */     try {
/*  391 */       ClassLoader target = clazz.getClassLoader();
/*      */       
/*  393 */       if (target == classLoader || target == null) {
/*  394 */         return true;
/*      */       }
/*  396 */       if (classLoader == null) {
/*  397 */         return false;
/*      */       }
/*      */       
/*  400 */       ClassLoader current = classLoader;
/*  401 */       while (current != null) {
/*  402 */         current = current.getParent();
/*  403 */         if (current == target) {
/*  404 */           return true;
/*      */         }
/*      */       } 
/*      */       
/*  408 */       while (target != null) {
/*  409 */         target = target.getParent();
/*  410 */         if (target == classLoader) {
/*  411 */           return false;
/*      */         }
/*      */       }
/*      */     
/*  415 */     } catch (SecurityException securityException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  421 */     return (classLoader != null && isLoadable(clazz, classLoader));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isLoadable(Class<?> clazz, ClassLoader classLoader) {
/*      */     try {
/*  432 */       return (clazz == classLoader.loadClass(clazz.getName()));
/*      */     
/*      */     }
/*  435 */     catch (ClassNotFoundException ex) {
/*      */       
/*  437 */       return false;
/*      */     } 
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
/*      */   @Nullable
/*      */   public static Class<?> resolvePrimitiveClassName(@Nullable String name) {
/*  453 */     Class<?> result = null;
/*      */ 
/*      */     
/*  456 */     if (name != null && name.length() <= 8)
/*      */     {
/*  458 */       result = primitiveTypeNameMap.get(name);
/*      */     }
/*  460 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isPrimitiveWrapper(Class<?> clazz) {
/*  470 */     Assert.notNull(clazz, "Class must not be null");
/*  471 */     return primitiveWrapperTypeMap.containsKey(clazz);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
/*  482 */     Assert.notNull(clazz, "Class must not be null");
/*  483 */     return (clazz.isPrimitive() || isPrimitiveWrapper(clazz));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isPrimitiveArray(Class<?> clazz) {
/*  493 */     Assert.notNull(clazz, "Class must not be null");
/*  494 */     return (clazz.isArray() && clazz.getComponentType().isPrimitive());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isPrimitiveWrapperArray(Class<?> clazz) {
/*  504 */     Assert.notNull(clazz, "Class must not be null");
/*  505 */     return (clazz.isArray() && isPrimitiveWrapper(clazz.getComponentType()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> resolvePrimitiveIfNecessary(Class<?> clazz) {
/*  515 */     Assert.notNull(clazz, "Class must not be null");
/*  516 */     return (clazz.isPrimitive() && clazz != void.class) ? primitiveTypeToWrapperMap.get(clazz) : clazz;
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
/*      */   public static boolean isAssignable(Class<?> lhsType, Class<?> rhsType) {
/*  529 */     Assert.notNull(lhsType, "Left-hand side type must not be null");
/*  530 */     Assert.notNull(rhsType, "Right-hand side type must not be null");
/*  531 */     if (lhsType.isAssignableFrom(rhsType)) {
/*  532 */       return true;
/*      */     }
/*  534 */     if (lhsType.isPrimitive()) {
/*  535 */       Class<?> resolvedPrimitive = primitiveWrapperTypeMap.get(rhsType);
/*  536 */       if (lhsType == resolvedPrimitive) {
/*  537 */         return true;
/*      */       }
/*      */     } else {
/*      */       
/*  541 */       Class<?> resolvedWrapper = primitiveTypeToWrapperMap.get(rhsType);
/*  542 */       if (resolvedWrapper != null && lhsType.isAssignableFrom(resolvedWrapper)) {
/*  543 */         return true;
/*      */       }
/*      */     } 
/*  546 */     return false;
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
/*      */   public static boolean isAssignableValue(Class<?> type, @Nullable Object value) {
/*  558 */     Assert.notNull(type, "Type must not be null");
/*  559 */     return (value != null) ? isAssignable(type, value.getClass()) : (!type.isPrimitive());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String convertResourcePathToClassName(String resourcePath) {
/*  568 */     Assert.notNull(resourcePath, "Resource path must not be null");
/*  569 */     return resourcePath.replace('/', '.');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String convertClassNameToResourcePath(String className) {
/*  578 */     Assert.notNull(className, "Class name must not be null");
/*  579 */     return className.replace('.', '/');
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
/*      */   public static String addResourcePathToPackagePath(Class<?> clazz, String resourceName) {
/*  599 */     Assert.notNull(resourceName, "Resource name must not be null");
/*  600 */     if (!resourceName.startsWith("/")) {
/*  601 */       return classPackageAsResourcePath(clazz) + '/' + resourceName;
/*      */     }
/*  603 */     return classPackageAsResourcePath(clazz) + resourceName;
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
/*      */   public static String classPackageAsResourcePath(@Nullable Class<?> clazz) {
/*  621 */     if (clazz == null) {
/*  622 */       return "";
/*      */     }
/*  624 */     String className = clazz.getName();
/*  625 */     int packageEndIndex = className.lastIndexOf('.');
/*  626 */     if (packageEndIndex == -1) {
/*  627 */       return "";
/*      */     }
/*  629 */     String packageName = className.substring(0, packageEndIndex);
/*  630 */     return packageName.replace('.', '/');
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
/*      */   public static String classNamesToString(Class<?>... classes) {
/*  643 */     return classNamesToString(Arrays.asList(classes));
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
/*      */   public static String classNamesToString(@Nullable Collection<Class<?>> classes) {
/*  656 */     if (CollectionUtils.isEmpty(classes)) {
/*  657 */       return "[]";
/*      */     }
/*  659 */     StringBuilder sb = new StringBuilder("[");
/*  660 */     for (Iterator<Class<?>> it = classes.iterator(); it.hasNext(); ) {
/*  661 */       Class<?> clazz = it.next();
/*  662 */       sb.append(clazz.getName());
/*  663 */       if (it.hasNext()) {
/*  664 */         sb.append(", ");
/*      */       }
/*      */     } 
/*  667 */     sb.append("]");
/*  668 */     return sb.toString();
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
/*      */   public static Class<?>[] toClassArray(Collection<Class<?>> collection) {
/*  680 */     return (Class[])collection.<Class<?>[]>toArray((Class<?>[][])new Class[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?>[] getAllInterfaces(Object instance) {
/*  690 */     Assert.notNull(instance, "Instance must not be null");
/*  691 */     return getAllInterfacesForClass(instance.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?>[] getAllInterfacesForClass(Class<?> clazz) {
/*  702 */     return getAllInterfacesForClass(clazz, null);
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
/*      */   public static Class<?>[] getAllInterfacesForClass(Class<?> clazz, @Nullable ClassLoader classLoader) {
/*  715 */     return toClassArray(getAllInterfacesForClassAsSet(clazz, classLoader));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Set<Class<?>> getAllInterfacesAsSet(Object instance) {
/*  725 */     Assert.notNull(instance, "Instance must not be null");
/*  726 */     return getAllInterfacesForClassAsSet(instance.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Set<Class<?>> getAllInterfacesForClassAsSet(Class<?> clazz) {
/*  737 */     return getAllInterfacesForClassAsSet(clazz, null);
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
/*      */   public static Set<Class<?>> getAllInterfacesForClassAsSet(Class<?> clazz, @Nullable ClassLoader classLoader) {
/*  750 */     Assert.notNull(clazz, "Class must not be null");
/*  751 */     if (clazz.isInterface() && isVisible(clazz, classLoader)) {
/*  752 */       return Collections.singleton(clazz);
/*      */     }
/*  754 */     Set<Class<?>> interfaces = new LinkedHashSet<>();
/*  755 */     Class<?> current = clazz;
/*  756 */     while (current != null) {
/*  757 */       Class<?>[] ifcs = current.getInterfaces();
/*  758 */       for (Class<?> ifc : ifcs) {
/*  759 */         if (isVisible(ifc, classLoader)) {
/*  760 */           interfaces.add(ifc);
/*      */         }
/*      */       } 
/*  763 */       current = current.getSuperclass();
/*      */     } 
/*  765 */     return interfaces;
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
/*      */   public static Class<?> createCompositeInterface(Class<?>[] interfaces, @Nullable ClassLoader classLoader) {
/*  781 */     Assert.notEmpty((Object[])interfaces, "Interface array must not be empty");
/*  782 */     return Proxy.getProxyClass(classLoader, interfaces);
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
/*      */   public static Class<?> determineCommonAncestor(@Nullable Class<?> clazz1, @Nullable Class<?> clazz2) {
/*  796 */     if (clazz1 == null) {
/*  797 */       return clazz2;
/*      */     }
/*  799 */     if (clazz2 == null) {
/*  800 */       return clazz1;
/*      */     }
/*  802 */     if (clazz1.isAssignableFrom(clazz2)) {
/*  803 */       return clazz1;
/*      */     }
/*  805 */     if (clazz2.isAssignableFrom(clazz1)) {
/*  806 */       return clazz2;
/*      */     }
/*  808 */     Class<?> ancestor = clazz1;
/*      */     while (true) {
/*  810 */       ancestor = ancestor.getSuperclass();
/*  811 */       if (ancestor == null || Object.class == ancestor) {
/*  812 */         return null;
/*      */       }
/*      */       
/*  815 */       if (ancestor.isAssignableFrom(clazz2)) {
/*  816 */         return ancestor;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isJavaLanguageInterface(Class<?> ifc) {
/*  829 */     return javaLanguageInterfaces.contains(ifc);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isInnerClass(Class<?> clazz) {
/*  840 */     return (clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isCglibProxy(Object object) {
/*  850 */     return isCglibProxyClass(object.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isCglibProxyClass(@Nullable Class<?> clazz) {
/*  859 */     return (clazz != null && isCglibProxyClassName(clazz.getName()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isCglibProxyClassName(@Nullable String className) {
/*  867 */     return (className != null && className.contains("$$"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> getUserClass(Object instance) {
/*  878 */     Assert.notNull(instance, "Instance must not be null");
/*  879 */     return getUserClass(instance.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> getUserClass(Class<?> clazz) {
/*  889 */     if (clazz.getName().contains("$$")) {
/*  890 */       Class<?> superclass = clazz.getSuperclass();
/*  891 */       if (superclass != null && superclass != Object.class) {
/*  892 */         return superclass;
/*      */       }
/*      */     } 
/*  895 */     return clazz;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static String getDescriptiveType(@Nullable Object value) {
/*  907 */     if (value == null) {
/*  908 */       return null;
/*      */     }
/*  910 */     Class<?> clazz = value.getClass();
/*  911 */     if (Proxy.isProxyClass(clazz)) {
/*  912 */       StringBuilder result = new StringBuilder(clazz.getName());
/*  913 */       result.append(" implementing ");
/*  914 */       Class<?>[] ifcs = clazz.getInterfaces();
/*  915 */       for (int i = 0; i < ifcs.length; i++) {
/*  916 */         result.append(ifcs[i].getName());
/*  917 */         if (i < ifcs.length - 1) {
/*  918 */           result.append(',');
/*      */         }
/*      */       } 
/*  921 */       return result.toString();
/*      */     } 
/*      */     
/*  924 */     return clazz.getTypeName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean matchesTypeName(Class<?> clazz, @Nullable String typeName) {
/*  934 */     return (typeName != null && (typeName
/*  935 */       .equals(clazz.getTypeName()) || typeName.equals(clazz.getSimpleName())));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getShortName(String className) {
/*  945 */     Assert.hasLength(className, "Class name must not be empty");
/*  946 */     int lastDotIndex = className.lastIndexOf('.');
/*  947 */     int nameEndIndex = className.indexOf("$$");
/*  948 */     if (nameEndIndex == -1) {
/*  949 */       nameEndIndex = className.length();
/*      */     }
/*  951 */     String shortName = className.substring(lastDotIndex + 1, nameEndIndex);
/*  952 */     shortName = shortName.replace('$', '.');
/*  953 */     return shortName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getShortName(Class<?> clazz) {
/*  962 */     return getShortName(getQualifiedName(clazz));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getShortNameAsProperty(Class<?> clazz) {
/*  973 */     String shortName = getShortName(clazz);
/*  974 */     int dotIndex = shortName.lastIndexOf('.');
/*  975 */     shortName = (dotIndex != -1) ? shortName.substring(dotIndex + 1) : shortName;
/*  976 */     return Introspector.decapitalize(shortName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getClassFileName(Class<?> clazz) {
/*  986 */     Assert.notNull(clazz, "Class must not be null");
/*  987 */     String className = clazz.getName();
/*  988 */     int lastDotIndex = className.lastIndexOf('.');
/*  989 */     return className.substring(lastDotIndex + 1) + ".class";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getPackageName(Class<?> clazz) {
/* 1000 */     Assert.notNull(clazz, "Class must not be null");
/* 1001 */     return getPackageName(clazz.getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getPackageName(String fqClassName) {
/* 1012 */     Assert.notNull(fqClassName, "Class name must not be null");
/* 1013 */     int lastDotIndex = fqClassName.lastIndexOf('.');
/* 1014 */     return (lastDotIndex != -1) ? fqClassName.substring(0, lastDotIndex) : "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getQualifiedName(Class<?> clazz) {
/* 1024 */     Assert.notNull(clazz, "Class must not be null");
/* 1025 */     return clazz.getTypeName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getQualifiedMethodName(Method method) {
/* 1035 */     return getQualifiedMethodName(method, null);
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
/*      */   public static String getQualifiedMethodName(Method method, @Nullable Class<?> clazz) {
/* 1048 */     Assert.notNull(method, "Method must not be null");
/* 1049 */     return ((clazz != null) ? clazz : method.getDeclaringClass()).getName() + '.' + method.getName();
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
/*      */   public static boolean hasConstructor(Class<?> clazz, Class<?>... paramTypes) {
/* 1061 */     return (getConstructorIfAvailable(clazz, paramTypes) != null);
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
/*      */   public static <T> Constructor<T> getConstructorIfAvailable(Class<T> clazz, Class<?>... paramTypes) {
/* 1075 */     Assert.notNull(clazz, "Class must not be null");
/*      */     try {
/* 1077 */       return clazz.getConstructor(paramTypes);
/*      */     }
/* 1079 */     catch (NoSuchMethodException ex) {
/* 1080 */       return null;
/*      */     } 
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
/*      */   public static boolean hasMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
/* 1094 */     return (getMethodIfAvailable(clazz, methodName, paramTypes) != null);
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
/*      */   public static Method getMethod(Class<?> clazz, String methodName, @Nullable Class<?>... paramTypes) {
/* 1112 */     Assert.notNull(clazz, "Class must not be null");
/* 1113 */     Assert.notNull(methodName, "Method name must not be null");
/* 1114 */     if (paramTypes != null) {
/*      */       try {
/* 1116 */         return clazz.getMethod(methodName, paramTypes);
/*      */       }
/* 1118 */       catch (NoSuchMethodException ex) {
/* 1119 */         throw new IllegalStateException("Expected method not found: " + ex);
/*      */       } 
/*      */     }
/*      */     
/* 1123 */     Set<Method> candidates = new HashSet<>(1);
/* 1124 */     Method[] methods = clazz.getMethods();
/* 1125 */     for (Method method : methods) {
/* 1126 */       if (methodName.equals(method.getName())) {
/* 1127 */         candidates.add(method);
/*      */       }
/*      */     } 
/* 1130 */     if (candidates.size() == 1) {
/* 1131 */       return candidates.iterator().next();
/*      */     }
/* 1133 */     if (candidates.isEmpty()) {
/* 1134 */       throw new IllegalStateException("Expected method not found: " + clazz.getName() + '.' + methodName);
/*      */     }
/*      */     
/* 1137 */     throw new IllegalStateException("No unique method found: " + clazz.getName() + '.' + methodName);
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
/*      */   @Nullable
/*      */   public static Method getMethodIfAvailable(Class<?> clazz, String methodName, @Nullable Class<?>... paramTypes) {
/* 1157 */     Assert.notNull(clazz, "Class must not be null");
/* 1158 */     Assert.notNull(methodName, "Method name must not be null");
/* 1159 */     if (paramTypes != null) {
/*      */       try {
/* 1161 */         return clazz.getMethod(methodName, paramTypes);
/*      */       }
/* 1163 */       catch (NoSuchMethodException ex) {
/* 1164 */         return null;
/*      */       } 
/*      */     }
/*      */     
/* 1168 */     Set<Method> candidates = new HashSet<>(1);
/* 1169 */     Method[] methods = clazz.getMethods();
/* 1170 */     for (Method method : methods) {
/* 1171 */       if (methodName.equals(method.getName())) {
/* 1172 */         candidates.add(method);
/*      */       }
/*      */     } 
/* 1175 */     if (candidates.size() == 1) {
/* 1176 */       return candidates.iterator().next();
/*      */     }
/* 1178 */     return null;
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
/*      */   public static int getMethodCountForName(Class<?> clazz, String methodName) {
/* 1190 */     Assert.notNull(clazz, "Class must not be null");
/* 1191 */     Assert.notNull(methodName, "Method name must not be null");
/* 1192 */     int count = 0;
/* 1193 */     Method[] declaredMethods = clazz.getDeclaredMethods();
/* 1194 */     for (Method method : declaredMethods) {
/* 1195 */       if (methodName.equals(method.getName())) {
/* 1196 */         count++;
/*      */       }
/*      */     } 
/* 1199 */     Class<?>[] ifcs = clazz.getInterfaces();
/* 1200 */     for (Class<?> ifc : ifcs) {
/* 1201 */       count += getMethodCountForName(ifc, methodName);
/*      */     }
/* 1203 */     if (clazz.getSuperclass() != null) {
/* 1204 */       count += getMethodCountForName(clazz.getSuperclass(), methodName);
/*      */     }
/* 1206 */     return count;
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
/*      */   public static boolean hasAtLeastOneMethodWithName(Class<?> clazz, String methodName) {
/* 1218 */     Assert.notNull(clazz, "Class must not be null");
/* 1219 */     Assert.notNull(methodName, "Method name must not be null");
/* 1220 */     Method[] declaredMethods = clazz.getDeclaredMethods();
/* 1221 */     for (Method method : declaredMethods) {
/* 1222 */       if (method.getName().equals(methodName)) {
/* 1223 */         return true;
/*      */       }
/*      */     } 
/* 1226 */     Class<?>[] ifcs = clazz.getInterfaces();
/* 1227 */     for (Class<?> ifc : ifcs) {
/* 1228 */       if (hasAtLeastOneMethodWithName(ifc, methodName)) {
/* 1229 */         return true;
/*      */       }
/*      */     } 
/* 1232 */     return (clazz.getSuperclass() != null && hasAtLeastOneMethodWithName(clazz.getSuperclass(), methodName));
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
/*      */   
/*      */   public static Method getMostSpecificMethod(Method method, @Nullable Class<?> targetClass) {
/* 1257 */     if (targetClass != null && targetClass != method.getDeclaringClass() && isOverridable(method, targetClass)) {
/*      */       try {
/* 1259 */         if (Modifier.isPublic(method.getModifiers())) {
/*      */           try {
/* 1261 */             return targetClass.getMethod(method.getName(), method.getParameterTypes());
/*      */           }
/* 1263 */           catch (NoSuchMethodException ex) {
/* 1264 */             return method;
/*      */           } 
/*      */         }
/*      */ 
/*      */         
/* 1269 */         Method specificMethod = ReflectionUtils.findMethod(targetClass, method.getName(), method.getParameterTypes());
/* 1270 */         return (specificMethod != null) ? specificMethod : method;
/*      */       
/*      */       }
/* 1273 */       catch (SecurityException securityException) {}
/*      */     }
/*      */ 
/*      */     
/* 1277 */     return method;
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
/*      */   public static Method getInterfaceMethodIfPossible(Method method) {
/* 1290 */     if (Modifier.isPublic(method.getModifiers()) && !method.getDeclaringClass().isInterface()) {
/* 1291 */       Class<?> current = method.getDeclaringClass();
/* 1292 */       while (current != null && current != Object.class) {
/* 1293 */         Class<?>[] ifcs = current.getInterfaces();
/* 1294 */         for (Class<?> ifc : ifcs) {
/*      */           try {
/* 1296 */             return ifc.getMethod(method.getName(), method.getParameterTypes());
/*      */           }
/* 1298 */           catch (NoSuchMethodException noSuchMethodException) {}
/*      */         } 
/*      */ 
/*      */         
/* 1302 */         current = current.getSuperclass();
/*      */       } 
/*      */     } 
/* 1305 */     return method;
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
/*      */   public static boolean isUserLevelMethod(Method method) {
/* 1320 */     Assert.notNull(method, "Method must not be null");
/* 1321 */     return (method.isBridge() || (!method.isSynthetic() && !isGroovyObjectMethod(method)));
/*      */   }
/*      */   
/*      */   private static boolean isGroovyObjectMethod(Method method) {
/* 1325 */     return method.getDeclaringClass().getName().equals("groovy.lang.GroovyObject");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isOverridable(Method method, @Nullable Class<?> targetClass) {
/* 1334 */     if (Modifier.isPrivate(method.getModifiers())) {
/* 1335 */       return false;
/*      */     }
/* 1337 */     if (Modifier.isPublic(method.getModifiers()) || Modifier.isProtected(method.getModifiers())) {
/* 1338 */       return true;
/*      */     }
/* 1340 */     return (targetClass == null || 
/* 1341 */       getPackageName(method.getDeclaringClass()).equals(getPackageName(targetClass)));
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
/*      */   @Nullable
/*      */   public static Method getStaticMethod(Class<?> clazz, String methodName, Class<?>... args) {
/* 1354 */     Assert.notNull(clazz, "Class must not be null");
/* 1355 */     Assert.notNull(methodName, "Method name must not be null");
/*      */     try {
/* 1357 */       Method method = clazz.getMethod(methodName, args);
/* 1358 */       return Modifier.isStatic(method.getModifiers()) ? method : null;
/*      */     }
/* 1360 */     catch (NoSuchMethodException ex) {
/* 1361 */       return null;
/*      */     } 
/*      */   }
/*      */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/ClassUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */