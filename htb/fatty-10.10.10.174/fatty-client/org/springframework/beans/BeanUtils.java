/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.beans.PropertyEditor;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import kotlin.jvm.JvmClassMappingKt;
/*     */ import kotlin.reflect.KFunction;
/*     */ import kotlin.reflect.KParameter;
/*     */ import kotlin.reflect.full.KClasses;
/*     */ import kotlin.reflect.jvm.ReflectJvmMapping;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.KotlinDetector;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
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
/*     */ public abstract class BeanUtils
/*     */ {
/*  68 */   private static final Log logger = LogFactory.getLog(BeanUtils.class);
/*     */ 
/*     */   
/*  71 */   private static final Set<Class<?>> unknownEditorTypes = Collections.newSetFromMap((Map<Class<?>, Boolean>)new ConcurrentReferenceHashMap(64));
/*     */ 
/*     */ 
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
/*     */   public static <T> T instantiate(Class<T> clazz) throws BeanInstantiationException {
/*  85 */     Assert.notNull(clazz, "Class must not be null");
/*  86 */     if (clazz.isInterface()) {
/*  87 */       throw new BeanInstantiationException(clazz, "Specified class is an interface");
/*     */     }
/*     */     try {
/*  90 */       return clazz.newInstance();
/*     */     }
/*  92 */     catch (InstantiationException ex) {
/*  93 */       throw new BeanInstantiationException(clazz, "Is it an abstract class?", ex);
/*     */     }
/*  95 */     catch (IllegalAccessException ex) {
/*  96 */       throw new BeanInstantiationException(clazz, "Is the constructor accessible?", ex);
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
/*     */ 
/*     */   
/*     */   public static <T> T instantiateClass(Class<T> clazz) throws BeanInstantiationException {
/* 117 */     Assert.notNull(clazz, "Class must not be null");
/* 118 */     if (clazz.isInterface()) {
/* 119 */       throw new BeanInstantiationException(clazz, "Specified class is an interface");
/*     */     }
/*     */     try {
/* 122 */       return instantiateClass(clazz.getDeclaredConstructor(new Class[0]), new Object[0]);
/*     */     }
/* 124 */     catch (NoSuchMethodException ex) {
/* 125 */       Constructor<T> ctor = findPrimaryConstructor(clazz);
/* 126 */       if (ctor != null) {
/* 127 */         return instantiateClass(ctor, new Object[0]);
/*     */       }
/* 129 */       throw new BeanInstantiationException(clazz, "No default constructor found", ex);
/*     */     }
/* 131 */     catch (LinkageError err) {
/* 132 */       throw new BeanInstantiationException(clazz, "Unresolvable class definition", err);
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
/*     */   public static <T> T instantiateClass(Class<?> clazz, Class<T> assignableTo) throws BeanInstantiationException {
/* 151 */     Assert.isAssignable(assignableTo, clazz);
/* 152 */     return instantiateClass((Class)clazz);
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
/*     */   public static <T> T instantiateClass(Constructor<T> ctor, Object... args) throws BeanInstantiationException {
/* 168 */     Assert.notNull(ctor, "Constructor must not be null");
/*     */     try {
/* 170 */       ReflectionUtils.makeAccessible(ctor);
/* 171 */       return (KotlinDetector.isKotlinReflectPresent() && KotlinDetector.isKotlinType(ctor.getDeclaringClass())) ? 
/* 172 */         KotlinDelegate.<T>instantiateClass(ctor, args) : ctor.newInstance(args);
/*     */     }
/* 174 */     catch (InstantiationException ex) {
/* 175 */       throw new BeanInstantiationException(ctor, "Is it an abstract class?", ex);
/*     */     }
/* 177 */     catch (IllegalAccessException ex) {
/* 178 */       throw new BeanInstantiationException(ctor, "Is the constructor accessible?", ex);
/*     */     }
/* 180 */     catch (IllegalArgumentException ex) {
/* 181 */       throw new BeanInstantiationException(ctor, "Illegal arguments for constructor", ex);
/*     */     }
/* 183 */     catch (InvocationTargetException ex) {
/* 184 */       throw new BeanInstantiationException(ctor, "Constructor threw exception", ex.getTargetException());
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
/*     */   public static <T> Constructor<T> findPrimaryConstructor(Class<T> clazz) {
/* 200 */     Assert.notNull(clazz, "Class must not be null");
/* 201 */     if (KotlinDetector.isKotlinReflectPresent() && KotlinDetector.isKotlinType(clazz)) {
/* 202 */       Constructor<T> kotlinPrimaryConstructor = KotlinDelegate.findPrimaryConstructor(clazz);
/* 203 */       if (kotlinPrimaryConstructor != null) {
/* 204 */         return kotlinPrimaryConstructor;
/*     */       }
/*     */     } 
/* 207 */     return null;
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
/*     */   @Nullable
/*     */   public static Method findMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
/*     */     try {
/* 227 */       return clazz.getMethod(methodName, paramTypes);
/*     */     }
/* 229 */     catch (NoSuchMethodException ex) {
/* 230 */       return findDeclaredMethod(clazz, methodName, paramTypes);
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
/*     */   public static Method findDeclaredMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
/*     */     try {
/* 248 */       return clazz.getDeclaredMethod(methodName, paramTypes);
/*     */     }
/* 250 */     catch (NoSuchMethodException ex) {
/* 251 */       if (clazz.getSuperclass() != null) {
/* 252 */         return findDeclaredMethod(clazz.getSuperclass(), methodName, paramTypes);
/*     */       }
/* 254 */       return null;
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
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static Method findMethodWithMinimalParameters(Class<?> clazz, String methodName) throws IllegalArgumentException {
/* 277 */     Method targetMethod = findMethodWithMinimalParameters(clazz.getMethods(), methodName);
/* 278 */     if (targetMethod == null) {
/* 279 */       targetMethod = findDeclaredMethodWithMinimalParameters(clazz, methodName);
/*     */     }
/* 281 */     return targetMethod;
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
/*     */   @Nullable
/*     */   public static Method findDeclaredMethodWithMinimalParameters(Class<?> clazz, String methodName) throws IllegalArgumentException {
/* 300 */     Method targetMethod = findMethodWithMinimalParameters(clazz.getDeclaredMethods(), methodName);
/* 301 */     if (targetMethod == null && clazz.getSuperclass() != null) {
/* 302 */       targetMethod = findDeclaredMethodWithMinimalParameters(clazz.getSuperclass(), methodName);
/*     */     }
/* 304 */     return targetMethod;
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
/*     */   public static Method findMethodWithMinimalParameters(Method[] methods, String methodName) throws IllegalArgumentException {
/* 320 */     Method targetMethod = null;
/* 321 */     int numMethodsFoundWithCurrentMinimumArgs = 0;
/* 322 */     for (Method method : methods) {
/* 323 */       if (method.getName().equals(methodName)) {
/* 324 */         int numParams = method.getParameterCount();
/* 325 */         if (targetMethod == null || numParams < targetMethod.getParameterCount()) {
/* 326 */           targetMethod = method;
/* 327 */           numMethodsFoundWithCurrentMinimumArgs = 1;
/*     */         }
/* 329 */         else if (!method.isBridge() && targetMethod.getParameterCount() == numParams) {
/* 330 */           if (targetMethod.isBridge()) {
/*     */             
/* 332 */             targetMethod = method;
/*     */           }
/*     */           else {
/*     */             
/* 336 */             numMethodsFoundWithCurrentMinimumArgs++;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 341 */     if (numMethodsFoundWithCurrentMinimumArgs > 1) {
/* 342 */       throw new IllegalArgumentException("Cannot resolve method '" + methodName + "' to a unique method. Attempted to resolve to overloaded method with the least number of parameters but there were " + numMethodsFoundWithCurrentMinimumArgs + " candidates.");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 347 */     return targetMethod;
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
/*     */   public static Method resolveSignature(String signature, Class<?> clazz) {
/* 370 */     Assert.hasText(signature, "'signature' must not be empty");
/* 371 */     Assert.notNull(clazz, "Class must not be null");
/* 372 */     int startParen = signature.indexOf('(');
/* 373 */     int endParen = signature.indexOf(')');
/* 374 */     if (startParen > -1 && endParen == -1) {
/* 375 */       throw new IllegalArgumentException("Invalid method signature '" + signature + "': expected closing ')' for args list");
/*     */     }
/*     */     
/* 378 */     if (startParen == -1 && endParen > -1) {
/* 379 */       throw new IllegalArgumentException("Invalid method signature '" + signature + "': expected opening '(' for args list");
/*     */     }
/*     */     
/* 382 */     if (startParen == -1) {
/* 383 */       return findMethodWithMinimalParameters(clazz, signature);
/*     */     }
/*     */     
/* 386 */     String methodName = signature.substring(0, startParen);
/*     */     
/* 388 */     String[] parameterTypeNames = StringUtils.commaDelimitedListToStringArray(signature.substring(startParen + 1, endParen));
/* 389 */     Class<?>[] parameterTypes = new Class[parameterTypeNames.length];
/* 390 */     for (int i = 0; i < parameterTypeNames.length; i++) {
/* 391 */       String parameterTypeName = parameterTypeNames[i].trim();
/*     */       try {
/* 393 */         parameterTypes[i] = ClassUtils.forName(parameterTypeName, clazz.getClassLoader());
/*     */       }
/* 395 */       catch (Throwable ex) {
/* 396 */         throw new IllegalArgumentException("Invalid method signature: unable to resolve type [" + parameterTypeName + "] for argument " + i + ". Root cause: " + ex);
/*     */       } 
/*     */     } 
/*     */     
/* 400 */     return findMethod(clazz, methodName, parameterTypes);
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
/*     */   public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) throws BeansException {
/* 412 */     CachedIntrospectionResults cr = CachedIntrospectionResults.forClass(clazz);
/* 413 */     return cr.getPropertyDescriptors();
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
/*     */   public static PropertyDescriptor getPropertyDescriptor(Class<?> clazz, String propertyName) throws BeansException {
/* 427 */     CachedIntrospectionResults cr = CachedIntrospectionResults.forClass(clazz);
/* 428 */     return cr.getPropertyDescriptor(propertyName);
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
/*     */   public static PropertyDescriptor findPropertyForMethod(Method method) throws BeansException {
/* 442 */     return findPropertyForMethod(method, method.getDeclaringClass());
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
/*     */   public static PropertyDescriptor findPropertyForMethod(Method method, Class<?> clazz) throws BeansException {
/* 457 */     Assert.notNull(method, "Method must not be null");
/* 458 */     PropertyDescriptor[] pds = getPropertyDescriptors(clazz);
/* 459 */     for (PropertyDescriptor pd : pds) {
/* 460 */       if (method.equals(pd.getReadMethod()) || method.equals(pd.getWriteMethod())) {
/* 461 */         return pd;
/*     */       }
/*     */     } 
/* 464 */     return null;
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
/*     */   public static PropertyEditor findEditorByConvention(@Nullable Class<?> targetType) {
/* 478 */     if (targetType == null || targetType.isArray() || unknownEditorTypes.contains(targetType)) {
/* 479 */       return null;
/*     */     }
/* 481 */     ClassLoader cl = targetType.getClassLoader();
/* 482 */     if (cl == null) {
/*     */       try {
/* 484 */         cl = ClassLoader.getSystemClassLoader();
/* 485 */         if (cl == null) {
/* 486 */           return null;
/*     */         }
/*     */       }
/* 489 */       catch (Throwable ex) {
/*     */         
/* 491 */         if (logger.isDebugEnabled()) {
/* 492 */           logger.debug("Could not access system ClassLoader: " + ex);
/*     */         }
/* 494 */         return null;
/*     */       } 
/*     */     }
/* 497 */     String editorName = targetType.getName() + "Editor";
/*     */     try {
/* 499 */       Class<?> editorClass = cl.loadClass(editorName);
/* 500 */       if (!PropertyEditor.class.isAssignableFrom(editorClass)) {
/* 501 */         if (logger.isInfoEnabled()) {
/* 502 */           logger.info("Editor class [" + editorName + "] does not implement [java.beans.PropertyEditor] interface");
/*     */         }
/*     */         
/* 505 */         unknownEditorTypes.add(targetType);
/* 506 */         return null;
/*     */       } 
/* 508 */       return (PropertyEditor)instantiateClass(editorClass);
/*     */     }
/* 510 */     catch (ClassNotFoundException ex) {
/* 511 */       if (logger.isTraceEnabled()) {
/* 512 */         logger.trace("No property editor [" + editorName + "] found for type " + targetType
/* 513 */             .getName() + " according to 'Editor' suffix convention");
/*     */       }
/* 515 */       unknownEditorTypes.add(targetType);
/* 516 */       return null;
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
/*     */   public static Class<?> findPropertyType(String propertyName, @Nullable Class<?>... beanClasses) {
/* 528 */     if (beanClasses != null) {
/* 529 */       for (Class<?> beanClass : beanClasses) {
/* 530 */         PropertyDescriptor pd = getPropertyDescriptor(beanClass, propertyName);
/* 531 */         if (pd != null) {
/* 532 */           return pd.getPropertyType();
/*     */         }
/*     */       } 
/*     */     }
/* 536 */     return Object.class;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MethodParameter getWriteMethodParameter(PropertyDescriptor pd) {
/* 546 */     if (pd instanceof GenericTypeAwarePropertyDescriptor) {
/* 547 */       return new MethodParameter(((GenericTypeAwarePropertyDescriptor)pd).getWriteMethodParameter());
/*     */     }
/*     */     
/* 550 */     Method writeMethod = pd.getWriteMethod();
/* 551 */     Assert.state((writeMethod != null), "No write method available");
/* 552 */     return new MethodParameter(writeMethod, 0);
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
/*     */   public static boolean isSimpleProperty(Class<?> clazz) {
/* 567 */     Assert.notNull(clazz, "Class must not be null");
/* 568 */     return (isSimpleValueType(clazz) || (clazz.isArray() && isSimpleValueType(clazz.getComponentType())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isSimpleValueType(Class<?> clazz) {
/* 579 */     return (ClassUtils.isPrimitiveOrWrapper(clazz) || Enum.class
/* 580 */       .isAssignableFrom(clazz) || CharSequence.class
/* 581 */       .isAssignableFrom(clazz) || Number.class
/* 582 */       .isAssignableFrom(clazz) || Date.class
/* 583 */       .isAssignableFrom(clazz) || URI.class == clazz || URL.class == clazz || Locale.class == clazz || Class.class == clazz);
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
/*     */   public static void copyProperties(Object source, Object target) throws BeansException {
/* 602 */     copyProperties(source, target, null, (String[])null);
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
/*     */   public static void copyProperties(Object source, Object target, Class<?> editable) throws BeansException {
/* 620 */     copyProperties(source, target, editable, (String[])null);
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
/*     */   public static void copyProperties(Object source, Object target, String... ignoreProperties) throws BeansException {
/* 638 */     copyProperties(source, target, null, ignoreProperties);
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
/*     */   private static void copyProperties(Object source, Object target, @Nullable Class<?> editable, @Nullable String... ignoreProperties) throws BeansException {
/* 656 */     Assert.notNull(source, "Source must not be null");
/* 657 */     Assert.notNull(target, "Target must not be null");
/*     */     
/* 659 */     Class<?> actualEditable = target.getClass();
/* 660 */     if (editable != null) {
/* 661 */       if (!editable.isInstance(target)) {
/* 662 */         throw new IllegalArgumentException("Target class [" + target.getClass().getName() + "] not assignable to Editable class [" + editable
/* 663 */             .getName() + "]");
/*     */       }
/* 665 */       actualEditable = editable;
/*     */     } 
/* 667 */     PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
/* 668 */     List<String> ignoreList = (ignoreProperties != null) ? Arrays.<String>asList(ignoreProperties) : null;
/*     */     
/* 670 */     for (PropertyDescriptor targetPd : targetPds) {
/* 671 */       Method writeMethod = targetPd.getWriteMethod();
/* 672 */       if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {
/* 673 */         PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
/* 674 */         if (sourcePd != null) {
/* 675 */           Method readMethod = sourcePd.getReadMethod();
/* 676 */           if (readMethod != null && 
/* 677 */             ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
/*     */             try {
/* 679 */               if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
/* 680 */                 readMethod.setAccessible(true);
/*     */               }
/* 682 */               Object value = readMethod.invoke(source, new Object[0]);
/* 683 */               if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
/* 684 */                 writeMethod.setAccessible(true);
/*     */               }
/* 686 */               writeMethod.invoke(target, new Object[] { value });
/*     */             }
/* 688 */             catch (Throwable ex) {
/* 689 */               throw new FatalBeanException("Could not copy property '" + targetPd
/* 690 */                   .getName() + "' from source to target", ex);
/*     */             } 
/*     */           }
/*     */         } 
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
/*     */   private static class KotlinDelegate
/*     */   {
/*     */     @Nullable
/*     */     public static <T> Constructor<T> findPrimaryConstructor(Class<T> clazz) {
/*     */       try {
/* 713 */         KFunction<T> primaryCtor = KClasses.getPrimaryConstructor(JvmClassMappingKt.getKotlinClass(clazz));
/* 714 */         if (primaryCtor == null) {
/* 715 */           return null;
/*     */         }
/* 717 */         Constructor<T> constructor = ReflectJvmMapping.getJavaConstructor(primaryCtor);
/* 718 */         if (constructor == null) {
/* 719 */           throw new IllegalStateException("Failed to find Java constructor for Kotlin primary constructor: " + clazz
/* 720 */               .getName());
/*     */         }
/* 722 */         return constructor;
/*     */       }
/* 724 */       catch (UnsupportedOperationException ex) {
/* 725 */         return null;
/*     */       } 
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
/*     */     public static <T> T instantiateClass(Constructor<T> ctor, Object... args) throws IllegalAccessException, InvocationTargetException, InstantiationException {
/* 738 */       KFunction<T> kotlinConstructor = ReflectJvmMapping.getKotlinFunction(ctor);
/* 739 */       if (kotlinConstructor == null) {
/* 740 */         return ctor.newInstance(args);
/*     */       }
/* 742 */       List<KParameter> parameters = kotlinConstructor.getParameters();
/* 743 */       Map<KParameter, Object> argParameters = new HashMap<>(parameters.size());
/* 744 */       Assert.isTrue((args.length <= parameters.size()), "Number of provided arguments should be less of equals than number of constructor parameters");
/*     */       
/* 746 */       for (int i = 0; i < args.length; i++) {
/* 747 */         if (!((KParameter)parameters.get(i)).isOptional() || args[i] != null) {
/* 748 */           argParameters.put(parameters.get(i), args[i]);
/*     */         }
/*     */       } 
/* 751 */       return (T)kotlinConstructor.callBy(argParameters);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/BeanUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */