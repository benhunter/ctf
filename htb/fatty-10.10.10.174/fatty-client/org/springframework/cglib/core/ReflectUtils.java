/*     */ package org.springframework.cglib.core;
/*     */ 
/*     */ import java.beans.BeanInfo;
/*     */ import java.beans.IntrospectionException;
/*     */ import java.beans.Introspector;
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.invoke.MethodHandles;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.asm.Attribute;
/*     */ import org.springframework.asm.Type;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReflectUtils
/*     */ {
/*     */   static {
/*     */     Method privateLookupIn, lookupDefineClass, classLoaderDefineClass;
/*     */     ProtectionDomain protectionDomain;
/*     */   }
/*     */   
/*  53 */   private static final Map primitives = new HashMap<>(8);
/*     */   
/*  55 */   private static final Map transforms = new HashMap<>(8);
/*     */   
/*  57 */   private static final ClassLoader defaultLoader = ReflectUtils.class.getClassLoader();
/*     */ 
/*     */   
/*     */   private static final Method privateLookupInMethod;
/*     */   
/*     */   private static final Method lookupDefineClassMethod;
/*     */   
/*     */   private static final Method classLoaderDefineClassMethod;
/*     */   
/*     */   private static final ProtectionDomain PROTECTION_DOMAIN;
/*     */   
/*     */   private static final Throwable THROWABLE;
/*     */   
/*  70 */   private static final List<Method> OBJECT_METHODS = new ArrayList<>();
/*     */ 
/*     */   
/*     */   private static final String[] CGLIB_PACKAGES;
/*     */ 
/*     */   
/*     */   static {
/*  77 */     Throwable throwable = null;
/*     */     try {
/*  79 */       privateLookupIn = AccessController.<Method>doPrivileged(new PrivilegedExceptionAction<Method>() {
/*     */             public Object run() throws Exception {
/*     */               try {
/*  82 */                 return MethodHandles.class.getMethod("privateLookupIn", new Class[] { Class.class, MethodHandles.Lookup.class });
/*     */               }
/*  84 */               catch (NoSuchMethodException ex) {
/*  85 */                 return null;
/*     */               } 
/*     */             }
/*     */           });
/*  89 */       lookupDefineClass = AccessController.<Method>doPrivileged(new PrivilegedExceptionAction<Method>() {
/*     */             public Object run() throws Exception {
/*     */               try {
/*  92 */                 return MethodHandles.Lookup.class.getMethod("defineClass", new Class[] { byte[].class });
/*     */               }
/*  94 */               catch (NoSuchMethodException ex) {
/*  95 */                 return null;
/*     */               } 
/*     */             }
/*     */           });
/*  99 */       classLoaderDefineClass = AccessController.<Method>doPrivileged(new PrivilegedExceptionAction<Method>() {
/*     */             public Object run() throws Exception {
/* 101 */               return ClassLoader.class.getDeclaredMethod("defineClass", new Class[] { String.class, byte[].class, int.class, int.class, ProtectionDomain.class });
/*     */             }
/*     */           });
/*     */       
/* 105 */       protectionDomain = getProtectionDomain(ReflectUtils.class);
/* 106 */       AccessController.doPrivileged(new PrivilegedExceptionAction() {
/*     */             public Object run() throws Exception {
/* 108 */               Method[] methods = Object.class.getDeclaredMethods();
/* 109 */               for (Method method : methods) {
/* 110 */                 if (!"finalize".equals(method.getName()) && (method
/* 111 */                   .getModifiers() & 0x18) <= 0)
/*     */                 {
/*     */                   
/* 114 */                   ReflectUtils.OBJECT_METHODS.add(method); } 
/*     */               } 
/* 116 */               return null;
/*     */             }
/*     */           });
/*     */     }
/* 120 */     catch (Throwable t) {
/* 121 */       privateLookupIn = null;
/* 122 */       lookupDefineClass = null;
/* 123 */       classLoaderDefineClass = null;
/* 124 */       protectionDomain = null;
/* 125 */       throwable = t;
/*     */     } 
/* 127 */     privateLookupInMethod = privateLookupIn;
/* 128 */     lookupDefineClassMethod = lookupDefineClass;
/* 129 */     classLoaderDefineClassMethod = classLoaderDefineClass;
/* 130 */     PROTECTION_DOMAIN = protectionDomain;
/* 131 */     THROWABLE = throwable;
/*     */ 
/*     */ 
/*     */     
/* 135 */     CGLIB_PACKAGES = new String[] { "java.lang" };
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 140 */     primitives.put("byte", byte.class);
/* 141 */     primitives.put("char", char.class);
/* 142 */     primitives.put("double", double.class);
/* 143 */     primitives.put("float", float.class);
/* 144 */     primitives.put("int", int.class);
/* 145 */     primitives.put("long", long.class);
/* 146 */     primitives.put("short", short.class);
/* 147 */     primitives.put("boolean", boolean.class);
/*     */     
/* 149 */     transforms.put("byte", "B");
/* 150 */     transforms.put("char", "C");
/* 151 */     transforms.put("double", "D");
/* 152 */     transforms.put("float", "F");
/* 153 */     transforms.put("int", "I");
/* 154 */     transforms.put("long", "J");
/* 155 */     transforms.put("short", "S");
/* 156 */     transforms.put("boolean", "Z");
/*     */   }
/*     */   
/*     */   public static ProtectionDomain getProtectionDomain(final Class source) {
/* 160 */     if (source == null) {
/* 161 */       return null;
/*     */     }
/* 163 */     return AccessController.<ProtectionDomain>doPrivileged(new PrivilegedAction<ProtectionDomain>() {
/*     */           public Object run() {
/* 165 */             return source.getProtectionDomain();
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public static Type[] getExceptionTypes(Member member) {
/* 171 */     if (member instanceof Method) {
/* 172 */       return TypeUtils.getTypes(((Method)member).getExceptionTypes());
/*     */     }
/* 174 */     if (member instanceof Constructor) {
/* 175 */       return TypeUtils.getTypes(((Constructor)member).getExceptionTypes());
/*     */     }
/*     */     
/* 178 */     throw new IllegalArgumentException("Cannot get exception types of a field");
/*     */   }
/*     */ 
/*     */   
/*     */   public static Signature getSignature(Member member) {
/* 183 */     if (member instanceof Method) {
/* 184 */       return new Signature(member.getName(), Type.getMethodDescriptor((Method)member));
/*     */     }
/* 186 */     if (member instanceof Constructor) {
/* 187 */       Type[] types = TypeUtils.getTypes(((Constructor)member).getParameterTypes());
/* 188 */       return new Signature("<init>", 
/* 189 */           Type.getMethodDescriptor(Type.VOID_TYPE, types));
/*     */     } 
/*     */ 
/*     */     
/* 193 */     throw new IllegalArgumentException("Cannot get signature of a field");
/*     */   }
/*     */ 
/*     */   
/*     */   public static Constructor findConstructor(String desc) {
/* 198 */     return findConstructor(desc, defaultLoader);
/*     */   }
/*     */   
/*     */   public static Constructor findConstructor(String desc, ClassLoader loader) {
/*     */     try {
/* 203 */       int lparen = desc.indexOf('(');
/* 204 */       String className = desc.substring(0, lparen).trim();
/* 205 */       return getClass(className, loader).getConstructor(parseTypes(desc, loader));
/*     */     }
/* 207 */     catch (ClassNotFoundException|NoSuchMethodException ex) {
/* 208 */       throw new CodeGenerationException(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Method findMethod(String desc) {
/* 213 */     return findMethod(desc, defaultLoader);
/*     */   }
/*     */   
/*     */   public static Method findMethod(String desc, ClassLoader loader) {
/*     */     try {
/* 218 */       int lparen = desc.indexOf('(');
/* 219 */       int dot = desc.lastIndexOf('.', lparen);
/* 220 */       String className = desc.substring(0, dot).trim();
/* 221 */       String methodName = desc.substring(dot + 1, lparen).trim();
/* 222 */       return getClass(className, loader).getDeclaredMethod(methodName, parseTypes(desc, loader));
/*     */     }
/* 224 */     catch (ClassNotFoundException|NoSuchMethodException ex) {
/* 225 */       throw new CodeGenerationException(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Class[] parseTypes(String desc, ClassLoader loader) throws ClassNotFoundException {
/* 230 */     int lparen = desc.indexOf('(');
/* 231 */     int rparen = desc.indexOf(')', lparen);
/* 232 */     List<String> params = new ArrayList();
/* 233 */     int start = lparen + 1;
/*     */     while (true) {
/* 235 */       int comma = desc.indexOf(',', start);
/* 236 */       if (comma < 0) {
/*     */         break;
/*     */       }
/* 239 */       params.add(desc.substring(start, comma).trim());
/* 240 */       start = comma + 1;
/*     */     } 
/* 242 */     if (start < rparen) {
/* 243 */       params.add(desc.substring(start, rparen).trim());
/*     */     }
/* 245 */     Class[] types = new Class[params.size()];
/* 246 */     for (int i = 0; i < types.length; i++) {
/* 247 */       types[i] = getClass(params.get(i), loader);
/*     */     }
/* 249 */     return types;
/*     */   }
/*     */   
/*     */   private static Class getClass(String className, ClassLoader loader) throws ClassNotFoundException {
/* 253 */     return getClass(className, loader, CGLIB_PACKAGES);
/*     */   }
/*     */   
/*     */   private static Class getClass(String className, ClassLoader loader, String[] packages) throws ClassNotFoundException {
/* 257 */     String save = className;
/* 258 */     int dimensions = 0;
/* 259 */     int index = 0;
/* 260 */     while ((index = className.indexOf("[]", index) + 1) > 0) {
/* 261 */       dimensions++;
/*     */     }
/* 263 */     StringBuffer brackets = new StringBuffer(className.length() - dimensions);
/* 264 */     for (int i = 0; i < dimensions; i++) {
/* 265 */       brackets.append('[');
/*     */     }
/* 267 */     className = className.substring(0, className.length() - 2 * dimensions);
/*     */     
/* 269 */     String prefix = (dimensions > 0) ? (brackets + "L") : "";
/* 270 */     String suffix = (dimensions > 0) ? ";" : "";
/*     */     try {
/* 272 */       return Class.forName(prefix + className + suffix, false, loader);
/*     */     }
/* 274 */     catch (ClassNotFoundException classNotFoundException) {
/*     */       
/* 276 */       for (int j = 0; j < packages.length; j++) {
/*     */         try {
/* 278 */           return Class.forName(prefix + packages[j] + '.' + className + suffix, false, loader);
/*     */         }
/* 280 */         catch (ClassNotFoundException classNotFoundException1) {}
/*     */       } 
/*     */       
/* 283 */       if (dimensions == 0) {
/* 284 */         Class c = (Class)primitives.get(className);
/* 285 */         if (c != null) {
/* 286 */           return c;
/*     */         }
/*     */       } else {
/*     */         
/* 290 */         String transform = (String)transforms.get(className);
/* 291 */         if (transform != null) {
/*     */           try {
/* 293 */             return Class.forName(brackets + transform, false, loader);
/*     */           }
/* 295 */           catch (ClassNotFoundException classNotFoundException1) {}
/*     */         }
/*     */       } 
/*     */       
/* 299 */       throw new ClassNotFoundException(save);
/*     */     } 
/*     */   }
/*     */   public static Object newInstance(Class type) {
/* 303 */     return newInstance(type, Constants.EMPTY_CLASS_ARRAY, null);
/*     */   }
/*     */   
/*     */   public static Object newInstance(Class type, Class[] parameterTypes, Object[] args) {
/* 307 */     return newInstance(getConstructor(type, parameterTypes), args);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Object newInstance(Constructor cstruct, Object[] args) {
/* 312 */     boolean flag = cstruct.isAccessible();
/*     */     try {
/* 314 */       if (!flag) {
/* 315 */         cstruct.setAccessible(true);
/*     */       }
/* 317 */       Object result = cstruct.newInstance(args);
/* 318 */       return result;
/*     */     }
/* 320 */     catch (InstantiationException e) {
/* 321 */       throw new CodeGenerationException(e);
/*     */     }
/* 323 */     catch (IllegalAccessException e) {
/* 324 */       throw new CodeGenerationException(e);
/*     */     }
/* 326 */     catch (InvocationTargetException e) {
/* 327 */       throw new CodeGenerationException(e.getTargetException());
/*     */     } finally {
/*     */       
/* 330 */       if (!flag) {
/* 331 */         cstruct.setAccessible(flag);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Constructor getConstructor(Class type, Class[] parameterTypes) {
/*     */     try {
/* 338 */       Constructor constructor = type.getDeclaredConstructor(parameterTypes);
/* 339 */       constructor.setAccessible(true);
/* 340 */       return constructor;
/*     */     }
/* 342 */     catch (NoSuchMethodException e) {
/* 343 */       throw new CodeGenerationException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String[] getNames(Class[] classes) {
/* 348 */     if (classes == null)
/* 349 */       return null; 
/* 350 */     String[] names = new String[classes.length];
/* 351 */     for (int i = 0; i < names.length; i++) {
/* 352 */       names[i] = classes[i].getName();
/*     */     }
/* 354 */     return names;
/*     */   }
/*     */   
/*     */   public static Class[] getClasses(Object[] objects) {
/* 358 */     Class[] classes = new Class[objects.length];
/* 359 */     for (int i = 0; i < objects.length; i++) {
/* 360 */       classes[i] = objects[i].getClass();
/*     */     }
/* 362 */     return classes;
/*     */   }
/*     */   
/*     */   public static Method findNewInstance(Class iface) {
/* 366 */     Method m = findInterfaceMethod(iface);
/* 367 */     if (!m.getName().equals("newInstance")) {
/* 368 */       throw new IllegalArgumentException(iface + " missing newInstance method");
/*     */     }
/* 370 */     return m;
/*     */   }
/*     */   
/*     */   public static Method[] getPropertyMethods(PropertyDescriptor[] properties, boolean read, boolean write) {
/* 374 */     Set<Method> methods = new HashSet();
/* 375 */     for (int i = 0; i < properties.length; i++) {
/* 376 */       PropertyDescriptor pd = properties[i];
/* 377 */       if (read) {
/* 378 */         methods.add(pd.getReadMethod());
/*     */       }
/* 380 */       if (write) {
/* 381 */         methods.add(pd.getWriteMethod());
/*     */       }
/*     */     } 
/* 384 */     methods.remove(null);
/* 385 */     return methods.<Method>toArray(new Method[methods.size()]);
/*     */   }
/*     */   
/*     */   public static PropertyDescriptor[] getBeanProperties(Class type) {
/* 389 */     return getPropertiesHelper(type, true, true);
/*     */   }
/*     */   
/*     */   public static PropertyDescriptor[] getBeanGetters(Class type) {
/* 393 */     return getPropertiesHelper(type, true, false);
/*     */   }
/*     */   
/*     */   public static PropertyDescriptor[] getBeanSetters(Class type) {
/* 397 */     return getPropertiesHelper(type, false, true);
/*     */   }
/*     */   
/*     */   private static PropertyDescriptor[] getPropertiesHelper(Class<?> type, boolean read, boolean write) {
/*     */     try {
/* 402 */       BeanInfo info = Introspector.getBeanInfo(type, Object.class);
/* 403 */       PropertyDescriptor[] all = info.getPropertyDescriptors();
/* 404 */       if (read && write) {
/* 405 */         return all;
/*     */       }
/* 407 */       List<PropertyDescriptor> properties = new ArrayList(all.length);
/* 408 */       for (int i = 0; i < all.length; i++) {
/* 409 */         PropertyDescriptor pd = all[i];
/* 410 */         if ((read && pd.getReadMethod() != null) || (write && pd
/* 411 */           .getWriteMethod() != null)) {
/* 412 */           properties.add(pd);
/*     */         }
/*     */       } 
/* 415 */       return properties.<PropertyDescriptor>toArray(new PropertyDescriptor[properties.size()]);
/*     */     }
/* 417 */     catch (IntrospectionException e) {
/* 418 */       throw new CodeGenerationException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Method findDeclaredMethod(Class type, String methodName, Class[] parameterTypes) throws NoSuchMethodException {
/* 426 */     Class cl = type;
/* 427 */     while (cl != null) {
/*     */       try {
/* 429 */         return cl.getDeclaredMethod(methodName, parameterTypes);
/*     */       }
/* 431 */       catch (NoSuchMethodException e) {
/* 432 */         cl = cl.getSuperclass();
/*     */       } 
/*     */     } 
/* 435 */     throw new NoSuchMethodException(methodName);
/*     */   }
/*     */   
/*     */   public static List addAllMethods(Class<Object> type, List<Method> list) {
/* 439 */     if (type == Object.class) {
/* 440 */       list.addAll(OBJECT_METHODS);
/*     */     } else {
/*     */       
/* 443 */       list.addAll(Arrays.asList(type.getDeclaredMethods()));
/*     */     } 
/* 445 */     Class<? super Object> superclass = type.getSuperclass();
/* 446 */     if (superclass != null) {
/* 447 */       addAllMethods(superclass, list);
/*     */     }
/* 449 */     Class[] interfaces = type.getInterfaces();
/* 450 */     for (int i = 0; i < interfaces.length; i++) {
/* 451 */       addAllMethods(interfaces[i], list);
/*     */     }
/*     */     
/* 454 */     return list;
/*     */   }
/*     */   
/*     */   public static List addAllInterfaces(Class type, List list) {
/* 458 */     Class superclass = type.getSuperclass();
/* 459 */     if (superclass != null) {
/* 460 */       list.addAll(Arrays.asList(type.getInterfaces()));
/* 461 */       addAllInterfaces(superclass, list);
/*     */     } 
/* 463 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Method findInterfaceMethod(Class iface) {
/* 468 */     if (!iface.isInterface()) {
/* 469 */       throw new IllegalArgumentException(iface + " is not an interface");
/*     */     }
/* 471 */     Method[] methods = iface.getDeclaredMethods();
/* 472 */     if (methods.length != 1) {
/* 473 */       throw new IllegalArgumentException("expecting exactly 1 method in " + iface);
/*     */     }
/* 475 */     return methods[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public static Class defineClass(String className, byte[] b, ClassLoader loader) throws Exception {
/* 480 */     return defineClass(className, b, loader, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class defineClass(String className, byte[] b, ClassLoader loader, ProtectionDomain protectionDomain) throws Exception {
/* 486 */     return defineClass(className, b, loader, protectionDomain, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class defineClass(String className, byte[] b, ClassLoader loader, ProtectionDomain protectionDomain, Class<?> contextClass) throws Exception {
/* 493 */     Class c = null;
/*     */ 
/*     */     
/* 496 */     if (contextClass != null && contextClass.getClassLoader() == loader && privateLookupInMethod != null && lookupDefineClassMethod != null) {
/*     */       
/*     */       try {
/*     */         
/* 500 */         MethodHandles.Lookup lookup = (MethodHandles.Lookup)privateLookupInMethod.invoke((Object)null, new Object[] { contextClass, MethodHandles.lookup() });
/* 501 */         c = (Class)lookupDefineClassMethod.invoke(lookup, new Object[] { b });
/*     */       }
/* 503 */       catch (InvocationTargetException ex) {
/* 504 */         Throwable target = ex.getTargetException();
/* 505 */         if (target.getClass() != LinkageError.class && target.getClass() != IllegalArgumentException.class) {
/* 506 */           throw new CodeGenerationException(target);
/*     */ 
/*     */         
/*     */         }
/*     */       
/*     */       }
/* 512 */       catch (Throwable ex) {
/* 513 */         throw new CodeGenerationException(ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 518 */     if (c == null && classLoaderDefineClassMethod != null) {
/* 519 */       if (protectionDomain == null) {
/* 520 */         protectionDomain = PROTECTION_DOMAIN;
/*     */       }
/* 522 */       Object[] args = { className, b, Integer.valueOf(0), Integer.valueOf(b.length), protectionDomain };
/*     */       try {
/* 524 */         if (!classLoaderDefineClassMethod.isAccessible()) {
/* 525 */           classLoaderDefineClassMethod.setAccessible(true);
/*     */         }
/* 527 */         c = (Class)classLoaderDefineClassMethod.invoke(loader, args);
/*     */       }
/* 529 */       catch (InvocationTargetException ex) {
/* 530 */         throw new CodeGenerationException(ex.getTargetException());
/*     */       }
/* 532 */       catch (Throwable ex) {
/*     */ 
/*     */         
/* 535 */         if (!ex.getClass().getName().endsWith("InaccessibleObjectException")) {
/* 536 */           throw new CodeGenerationException(ex);
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 542 */     if (c == null && contextClass != null && contextClass.getClassLoader() != loader && privateLookupInMethod != null && lookupDefineClassMethod != null) {
/*     */       
/*     */       try {
/*     */         
/* 546 */         MethodHandles.Lookup lookup = (MethodHandles.Lookup)privateLookupInMethod.invoke((Object)null, new Object[] { contextClass, MethodHandles.lookup() });
/* 547 */         c = (Class)lookupDefineClassMethod.invoke(lookup, new Object[] { b });
/*     */       }
/* 549 */       catch (InvocationTargetException ex) {
/* 550 */         throw new CodeGenerationException(ex.getTargetException());
/*     */       }
/* 552 */       catch (Throwable ex) {
/* 553 */         throw new CodeGenerationException(ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 558 */     if (c == null) {
/* 559 */       throw new CodeGenerationException(THROWABLE);
/*     */     }
/*     */ 
/*     */     
/* 563 */     Class.forName(className, true, loader);
/* 564 */     return c;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int findPackageProtected(Class[] classes) {
/* 569 */     for (int i = 0; i < classes.length; i++) {
/* 570 */       if (!Modifier.isPublic(classes[i].getModifiers())) {
/* 571 */         return i;
/*     */       }
/*     */     } 
/* 574 */     return 0;
/*     */   }
/*     */   
/*     */   public static MethodInfo getMethodInfo(final Member member, final int modifiers) {
/* 578 */     final Signature sig = getSignature(member);
/* 579 */     return new MethodInfo() {
/*     */         private ClassInfo ci;
/*     */         
/*     */         public ClassInfo getClassInfo() {
/* 583 */           if (this.ci == null)
/* 584 */             this.ci = ReflectUtils.getClassInfo(member.getDeclaringClass()); 
/* 585 */           return this.ci;
/*     */         }
/*     */         
/*     */         public int getModifiers() {
/* 589 */           return modifiers;
/*     */         }
/*     */         
/*     */         public Signature getSignature() {
/* 593 */           return sig;
/*     */         }
/*     */         
/*     */         public Type[] getExceptionTypes() {
/* 597 */           return ReflectUtils.getExceptionTypes(member);
/*     */         }
/*     */         
/*     */         public Attribute getAttribute() {
/* 601 */           return null;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static MethodInfo getMethodInfo(Member member) {
/* 607 */     return getMethodInfo(member, member.getModifiers());
/*     */   }
/*     */   
/*     */   public static ClassInfo getClassInfo(final Class clazz) {
/* 611 */     final Type type = Type.getType(clazz);
/* 612 */     final Type sc = (clazz.getSuperclass() == null) ? null : Type.getType(clazz.getSuperclass());
/* 613 */     return new ClassInfo() {
/*     */         public Type getType() {
/* 615 */           return type;
/*     */         }
/*     */         public Type getSuperType() {
/* 618 */           return sc;
/*     */         }
/*     */         public Type[] getInterfaces() {
/* 621 */           return TypeUtils.getTypes(clazz.getInterfaces());
/*     */         }
/*     */         public int getModifiers() {
/* 624 */           return clazz.getModifiers();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static Method[] findMethods(String[] namesAndDescriptors, Method[] methods) {
/* 631 */     Map<Object, Object> map = new HashMap<>();
/* 632 */     for (int i = 0; i < methods.length; i++) {
/* 633 */       Method method = methods[i];
/* 634 */       map.put(method.getName() + Type.getMethodDescriptor(method), method);
/*     */     } 
/* 636 */     Method[] result = new Method[namesAndDescriptors.length / 2];
/* 637 */     for (int j = 0; j < result.length; j++) {
/* 638 */       result[j] = (Method)map.get(namesAndDescriptors[j * 2] + namesAndDescriptors[j * 2 + 1]);
/* 639 */       if (result[j] == null);
/*     */     } 
/*     */ 
/*     */     
/* 643 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cglib/core/ReflectUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */