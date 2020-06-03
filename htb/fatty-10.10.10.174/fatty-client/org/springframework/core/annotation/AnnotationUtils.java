/*      */ package org.springframework.core.annotation;
/*      */ 
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.lang.annotation.Repeatable;
/*      */ import java.lang.reflect.AnnotatedElement;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.InvocationHandler;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Member;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.lang.reflect.Proxy;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.apache.commons.logging.LogFactory;
/*      */ import org.springframework.core.BridgeMethodResolver;
/*      */ import org.springframework.core.ResolvableType;
/*      */ import org.springframework.lang.Nullable;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.ConcurrentReferenceHashMap;
/*      */ import org.springframework.util.ObjectUtils;
/*      */ import org.springframework.util.ReflectionUtils;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class AnnotationUtils
/*      */ {
/*      */   public static final String VALUE = "value";
/*  122 */   private static final Map<AnnotationCacheKey, Annotation> findAnnotationCache = (Map<AnnotationCacheKey, Annotation>)new ConcurrentReferenceHashMap(256);
/*      */ 
/*      */   
/*  125 */   private static final Map<AnnotationCacheKey, Boolean> metaPresentCache = (Map<AnnotationCacheKey, Boolean>)new ConcurrentReferenceHashMap(256);
/*      */ 
/*      */   
/*  128 */   private static final Map<AnnotatedElement, Annotation[]> declaredAnnotationsCache = (Map<AnnotatedElement, Annotation[]>)new ConcurrentReferenceHashMap(256);
/*      */ 
/*      */   
/*  131 */   private static final Map<Class<?>, Set<Method>> annotatedBaseTypeCache = (Map<Class<?>, Set<Method>>)new ConcurrentReferenceHashMap(256);
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*  136 */   private static final Map<Class<?>, ?> annotatedInterfaceCache = annotatedBaseTypeCache;
/*      */   
/*  138 */   private static final Map<Class<? extends Annotation>, Boolean> synthesizableCache = (Map<Class<? extends Annotation>, Boolean>)new ConcurrentReferenceHashMap(256);
/*      */ 
/*      */   
/*  141 */   private static final Map<Class<? extends Annotation>, Map<String, List<String>>> attributeAliasesCache = (Map<Class<? extends Annotation>, Map<String, List<String>>>)new ConcurrentReferenceHashMap(256);
/*      */ 
/*      */   
/*  144 */   private static final Map<Class<? extends Annotation>, List<Method>> attributeMethodsCache = (Map<Class<? extends Annotation>, List<Method>>)new ConcurrentReferenceHashMap(256);
/*      */ 
/*      */   
/*  147 */   private static final Map<Method, AliasDescriptor> aliasDescriptorCache = (Map<Method, AliasDescriptor>)new ConcurrentReferenceHashMap(256);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private static transient Log logger;
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
/*      */   public static <A extends Annotation> A getAnnotation(Annotation annotation, Class<A> annotationType) {
/*  169 */     if (annotationType.isInstance(annotation)) {
/*  170 */       return synthesizeAnnotation((A)annotation);
/*      */     }
/*  172 */     Class<? extends Annotation> annotatedElement = annotation.annotationType();
/*      */     try {
/*  174 */       A metaAnn = annotatedElement.getAnnotation(annotationType);
/*  175 */       return (metaAnn != null) ? synthesizeAnnotation(metaAnn, annotatedElement) : null;
/*      */     }
/*  177 */     catch (Throwable ex) {
/*  178 */       handleIntrospectionFailure(annotatedElement, ex);
/*  179 */       return null;
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
/*      */   @Nullable
/*      */   public static <A extends Annotation> A getAnnotation(AnnotatedElement annotatedElement, Class<A> annotationType) {
/*      */     try {
/*  198 */       A annotation = annotatedElement.getAnnotation(annotationType);
/*  199 */       if (annotation == null) {
/*  200 */         for (Annotation metaAnn : annotatedElement.getAnnotations()) {
/*  201 */           annotation = metaAnn.annotationType().getAnnotation(annotationType);
/*  202 */           if (annotation != null) {
/*      */             break;
/*      */           }
/*      */         } 
/*      */       }
/*  207 */       return (annotation != null) ? synthesizeAnnotation(annotation, annotatedElement) : null;
/*      */     }
/*  209 */     catch (Throwable ex) {
/*  210 */       handleIntrospectionFailure(annotatedElement, ex);
/*  211 */       return null;
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
/*      */   public static <A extends Annotation> A getAnnotation(Method method, Class<A> annotationType) {
/*  231 */     Method resolvedMethod = BridgeMethodResolver.findBridgedMethod(method);
/*  232 */     return getAnnotation(resolvedMethod, annotationType);
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
/*      */   @Nullable
/*      */   public static Annotation[] getAnnotations(AnnotatedElement annotatedElement) {
/*      */     try {
/*  249 */       return synthesizeAnnotationArray(annotatedElement.getAnnotations(), annotatedElement);
/*      */     }
/*  251 */     catch (Throwable ex) {
/*  252 */       handleIntrospectionFailure(annotatedElement, ex);
/*  253 */       return null;
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
/*      */   @Nullable
/*      */   public static Annotation[] getAnnotations(Method method) {
/*      */     try {
/*  272 */       return synthesizeAnnotationArray(BridgeMethodResolver.findBridgedMethod(method).getAnnotations(), method);
/*      */     }
/*  274 */     catch (Throwable ex) {
/*  275 */       handleIntrospectionFailure(method, ex);
/*  276 */       return null;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <A extends Annotation> Set<A> getRepeatableAnnotations(AnnotatedElement annotatedElement, Class<A> annotationType) {
/*  310 */     return getRepeatableAnnotations(annotatedElement, annotationType, null);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <A extends Annotation> Set<A> getRepeatableAnnotations(AnnotatedElement annotatedElement, Class<A> annotationType, @Nullable Class<? extends Annotation> containerAnnotationType) {
/*  346 */     Set<A> annotations = getDeclaredRepeatableAnnotations(annotatedElement, annotationType, containerAnnotationType);
/*  347 */     if (annotations.isEmpty() && annotatedElement instanceof Class) {
/*  348 */       Class<?> superclass = ((Class)annotatedElement).getSuperclass();
/*  349 */       if (superclass != null && superclass != Object.class) {
/*  350 */         return getRepeatableAnnotations(superclass, annotationType, containerAnnotationType);
/*      */       }
/*      */     } 
/*  353 */     return annotations;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <A extends Annotation> Set<A> getDeclaredRepeatableAnnotations(AnnotatedElement annotatedElement, Class<A> annotationType) {
/*  387 */     return getDeclaredRepeatableAnnotations(annotatedElement, annotationType, null);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <A extends Annotation> Set<A> getDeclaredRepeatableAnnotations(AnnotatedElement annotatedElement, Class<A> annotationType, @Nullable Class<? extends Annotation> containerAnnotationType) {
/*      */     try {
/*  424 */       if (annotatedElement instanceof Method) {
/*  425 */         annotatedElement = BridgeMethodResolver.findBridgedMethod((Method)annotatedElement);
/*      */       }
/*  427 */       return (new AnnotationCollector<>(annotationType, containerAnnotationType)).getResult(annotatedElement);
/*      */     }
/*  429 */     catch (Throwable ex) {
/*  430 */       handleIntrospectionFailure(annotatedElement, ex);
/*  431 */       return Collections.emptySet();
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
/*      */   @Nullable
/*      */   public static <A extends Annotation> A findAnnotation(AnnotatedElement annotatedElement, @Nullable Class<A> annotationType) {
/*  455 */     if (annotationType == null) {
/*  456 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  461 */     A ann = findAnnotation(annotatedElement, annotationType, new HashSet<>());
/*  462 */     return (ann != null) ? synthesizeAnnotation(ann, annotatedElement) : null;
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
/*      */   @Nullable
/*      */   private static <A extends Annotation> A findAnnotation(AnnotatedElement annotatedElement, Class<A> annotationType, Set<Annotation> visited) {
/*      */     try {
/*  479 */       A annotation = annotatedElement.getDeclaredAnnotation(annotationType);
/*  480 */       if (annotation != null) {
/*  481 */         return annotation;
/*      */       }
/*  483 */       for (Annotation declaredAnn : getDeclaredAnnotations(annotatedElement)) {
/*  484 */         Class<? extends Annotation> declaredType = declaredAnn.annotationType();
/*  485 */         if (!isInJavaLangAnnotationPackage(declaredType) && visited.add(declaredAnn)) {
/*  486 */           annotation = findAnnotation(declaredType, annotationType, visited);
/*  487 */           if (annotation != null) {
/*  488 */             return annotation;
/*      */           }
/*      */         }
/*      */       
/*      */       } 
/*  493 */     } catch (Throwable ex) {
/*  494 */       handleIntrospectionFailure(annotatedElement, ex);
/*      */     } 
/*  496 */     return null;
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
/*      */   @Nullable
/*      */   public static <A extends Annotation> A findAnnotation(Method method, @Nullable Class<A> annotationType) {
/*  517 */     Assert.notNull(method, "Method must not be null");
/*  518 */     if (annotationType == null) {
/*  519 */       return null;
/*      */     }
/*      */     
/*  522 */     AnnotationCacheKey cacheKey = new AnnotationCacheKey(method, annotationType);
/*  523 */     Annotation annotation = findAnnotationCache.get(cacheKey);
/*      */     
/*  525 */     if (annotation == null) {
/*  526 */       Method resolvedMethod = BridgeMethodResolver.findBridgedMethod(method);
/*  527 */       annotation = findAnnotation(resolvedMethod, annotationType);
/*  528 */       if (annotation == null) {
/*  529 */         annotation = searchOnInterfaces(method, annotationType, method.getDeclaringClass().getInterfaces());
/*      */       }
/*      */       
/*  532 */       Class<?> clazz = method.getDeclaringClass();
/*  533 */       while (annotation == null) {
/*  534 */         clazz = clazz.getSuperclass();
/*  535 */         if (clazz == null || clazz == Object.class) {
/*      */           break;
/*      */         }
/*  538 */         Set<Method> annotatedMethods = getAnnotatedMethodsInBaseType(clazz);
/*  539 */         if (!annotatedMethods.isEmpty()) {
/*  540 */           for (Method annotatedMethod : annotatedMethods) {
/*  541 */             if (isOverride(method, annotatedMethod)) {
/*  542 */               Method resolvedSuperMethod = BridgeMethodResolver.findBridgedMethod(annotatedMethod);
/*  543 */               annotation = findAnnotation(resolvedSuperMethod, annotationType);
/*  544 */               if (annotation != null) {
/*      */                 break;
/*      */               }
/*      */             } 
/*      */           } 
/*      */         }
/*  550 */         if (annotation == null) {
/*  551 */           annotation = searchOnInterfaces(method, annotationType, clazz.getInterfaces());
/*      */         }
/*      */       } 
/*      */       
/*  555 */       if (annotation != null) {
/*  556 */         annotation = synthesizeAnnotation(annotation, method);
/*  557 */         findAnnotationCache.put(cacheKey, annotation);
/*      */       } 
/*      */     } 
/*      */     
/*  561 */     return (A)annotation;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private static <A extends Annotation> A searchOnInterfaces(Method method, Class<A> annotationType, Class<?>... ifcs) {
/*  566 */     for (Class<?> ifc : ifcs) {
/*  567 */       Set<Method> annotatedMethods = getAnnotatedMethodsInBaseType(ifc);
/*  568 */       if (!annotatedMethods.isEmpty()) {
/*  569 */         for (Method annotatedMethod : annotatedMethods) {
/*  570 */           if (isOverride(method, annotatedMethod)) {
/*  571 */             A annotation = getAnnotation(annotatedMethod, annotationType);
/*  572 */             if (annotation != null) {
/*  573 */               return annotation;
/*      */             }
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/*  579 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean isOverride(Method method, Method candidate) {
/*  589 */     if (!candidate.getName().equals(method.getName()) || candidate
/*  590 */       .getParameterCount() != method.getParameterCount()) {
/*  591 */       return false;
/*      */     }
/*  593 */     Class<?>[] paramTypes = method.getParameterTypes();
/*  594 */     if (Arrays.equals((Object[])candidate.getParameterTypes(), (Object[])paramTypes)) {
/*  595 */       return true;
/*      */     }
/*  597 */     for (int i = 0; i < paramTypes.length; i++) {
/*  598 */       if (paramTypes[i] != ResolvableType.forMethodParameter(candidate, i, method.getDeclaringClass()).resolve()) {
/*  599 */         return false;
/*      */       }
/*      */     } 
/*  602 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Set<Method> getAnnotatedMethodsInBaseType(Class<?> baseType) {
/*  612 */     boolean ifcCheck = baseType.isInterface();
/*  613 */     if (ifcCheck && ClassUtils.isJavaLanguageInterface(baseType)) {
/*  614 */       return Collections.emptySet();
/*      */     }
/*      */     
/*  617 */     Set<Method> annotatedMethods = annotatedBaseTypeCache.get(baseType);
/*  618 */     if (annotatedMethods != null) {
/*  619 */       return annotatedMethods;
/*      */     }
/*  621 */     Method[] methods = ifcCheck ? baseType.getMethods() : baseType.getDeclaredMethods();
/*  622 */     for (Method baseMethod : methods) {
/*      */ 
/*      */       
/*      */       try {
/*  626 */         if ((ifcCheck || !Modifier.isPrivate(baseMethod.getModifiers())) && 
/*  627 */           hasSearchableAnnotations(baseMethod)) {
/*  628 */           if (annotatedMethods == null) {
/*  629 */             annotatedMethods = new HashSet<>();
/*      */           }
/*  631 */           annotatedMethods.add(baseMethod);
/*      */         }
/*      */       
/*  634 */       } catch (Throwable ex) {
/*  635 */         handleIntrospectionFailure(baseMethod, ex);
/*      */       } 
/*      */     } 
/*  638 */     if (annotatedMethods == null) {
/*  639 */       annotatedMethods = Collections.emptySet();
/*      */     }
/*  641 */     annotatedBaseTypeCache.put(baseType, annotatedMethods);
/*  642 */     return annotatedMethods;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean hasSearchableAnnotations(Method ifcMethod) {
/*  653 */     Annotation[] anns = getDeclaredAnnotations(ifcMethod);
/*  654 */     if (anns.length == 0) {
/*  655 */       return false;
/*      */     }
/*  657 */     for (Annotation ann : anns) {
/*  658 */       String name = ann.annotationType().getName();
/*  659 */       if (!name.startsWith("java.lang.") && !name.startsWith("org.springframework.lang.")) {
/*  660 */         return true;
/*      */       }
/*      */     } 
/*  663 */     return false;
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
/*      */   static Annotation[] getDeclaredAnnotations(AnnotatedElement element) {
/*  675 */     if (element instanceof Class || element instanceof Member)
/*      */     {
/*      */       
/*  678 */       return declaredAnnotationsCache.computeIfAbsent(element, AnnotatedElement::getDeclaredAnnotations);
/*      */     }
/*  680 */     return element.getDeclaredAnnotations();
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
/*      */   
/*      */   @Nullable
/*      */   public static <A extends Annotation> A findAnnotation(Class<?> clazz, @Nullable Class<A> annotationType) {
/*  707 */     return findAnnotation(clazz, annotationType, true);
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
/*      */   private static <A extends Annotation> A findAnnotation(Class<?> clazz, @Nullable Class<A> annotationType, boolean synthesize) {
/*  725 */     Assert.notNull(clazz, "Class must not be null");
/*  726 */     if (annotationType == null) {
/*  727 */       return null;
/*      */     }
/*      */     
/*  730 */     AnnotationCacheKey cacheKey = new AnnotationCacheKey(clazz, annotationType);
/*  731 */     Annotation annotation = findAnnotationCache.get(cacheKey);
/*  732 */     if (annotation == null) {
/*  733 */       annotation = findAnnotation(clazz, annotationType, new HashSet<>());
/*  734 */       if (annotation != null && synthesize) {
/*  735 */         annotation = synthesizeAnnotation(annotation, clazz);
/*  736 */         findAnnotationCache.put(cacheKey, annotation);
/*      */       } 
/*      */     } 
/*  739 */     return (A)annotation;
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
/*      */   private static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType, Set<Annotation> visited) {
/*      */     try {
/*  754 */       A annotation = clazz.getDeclaredAnnotation(annotationType);
/*  755 */       if (annotation != null) {
/*  756 */         return annotation;
/*      */       }
/*  758 */       for (Annotation declaredAnn : getDeclaredAnnotations(clazz)) {
/*  759 */         Class<? extends Annotation> declaredType = declaredAnn.annotationType();
/*  760 */         if (!isInJavaLangAnnotationPackage(declaredType) && visited.add(declaredAnn)) {
/*  761 */           annotation = findAnnotation(declaredType, annotationType, visited);
/*  762 */           if (annotation != null) {
/*  763 */             return annotation;
/*      */           }
/*      */         }
/*      */       
/*      */       } 
/*  768 */     } catch (Throwable ex) {
/*  769 */       handleIntrospectionFailure(clazz, ex);
/*  770 */       return null;
/*      */     } 
/*      */     
/*  773 */     for (Class<?> ifc : clazz.getInterfaces()) {
/*  774 */       A annotation = findAnnotation(ifc, annotationType, visited);
/*  775 */       if (annotation != null) {
/*  776 */         return annotation;
/*      */       }
/*      */     } 
/*      */     
/*  780 */     Class<?> superclass = clazz.getSuperclass();
/*  781 */     if (superclass == null || superclass == Object.class) {
/*  782 */       return null;
/*      */     }
/*  784 */     return findAnnotation(superclass, annotationType, visited);
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
/*      */   
/*      */   @Nullable
/*      */   public static Class<?> findAnnotationDeclaringClass(Class<? extends Annotation> annotationType, @Nullable Class<?> clazz) {
/*  811 */     if (clazz == null || clazz == Object.class) {
/*  812 */       return null;
/*      */     }
/*  814 */     if (isAnnotationDeclaredLocally(annotationType, clazz)) {
/*  815 */       return clazz;
/*      */     }
/*  817 */     return findAnnotationDeclaringClass(annotationType, clazz.getSuperclass());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Class<?> findAnnotationDeclaringClassForTypes(List<Class<? extends Annotation>> annotationTypes, @Nullable Class<?> clazz) {
/*  848 */     if (clazz == null || clazz == Object.class) {
/*  849 */       return null;
/*      */     }
/*  851 */     for (Class<? extends Annotation> annotationType : annotationTypes) {
/*  852 */       if (isAnnotationDeclaredLocally(annotationType, clazz)) {
/*  853 */         return clazz;
/*      */       }
/*      */     } 
/*  856 */     return findAnnotationDeclaringClassForTypes(annotationTypes, clazz.getSuperclass());
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
/*      */   public static boolean isAnnotationDeclaredLocally(Class<? extends Annotation> annotationType, Class<?> clazz) {
/*      */     try {
/*  879 */       return (clazz.getDeclaredAnnotation(annotationType) != null);
/*      */     }
/*  881 */     catch (Throwable ex) {
/*  882 */       handleIntrospectionFailure(clazz, ex);
/*  883 */       return false;
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
/*      */   public static boolean isAnnotationInherited(Class<? extends Annotation> annotationType, Class<?> clazz) {
/*  907 */     return (clazz.isAnnotationPresent(annotationType) && !isAnnotationDeclaredLocally(annotationType, clazz));
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
/*      */   public static boolean isAnnotationMetaPresent(Class<? extends Annotation> annotationType, @Nullable Class<? extends Annotation> metaAnnotationType) {
/*  921 */     Assert.notNull(annotationType, "Annotation type must not be null");
/*  922 */     if (metaAnnotationType == null) {
/*  923 */       return false;
/*      */     }
/*      */     
/*  926 */     AnnotationCacheKey cacheKey = new AnnotationCacheKey(annotationType, metaAnnotationType);
/*  927 */     Boolean metaPresent = metaPresentCache.get(cacheKey);
/*  928 */     if (metaPresent != null) {
/*  929 */       return metaPresent.booleanValue();
/*      */     }
/*  931 */     metaPresent = Boolean.FALSE;
/*  932 */     if (findAnnotation(annotationType, metaAnnotationType, false) != null) {
/*  933 */       metaPresent = Boolean.TRUE;
/*      */     }
/*  935 */     metaPresentCache.put(cacheKey, metaPresent);
/*  936 */     return metaPresent.booleanValue();
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
/*      */   static boolean hasPlainJavaAnnotationsOnly(@Nullable Object annotatedElement) {
/*      */     Class<?> clazz;
/*  949 */     if (annotatedElement instanceof Class) {
/*  950 */       clazz = (Class)annotatedElement;
/*      */     }
/*  952 */     else if (annotatedElement instanceof Member) {
/*  953 */       clazz = ((Member)annotatedElement).getDeclaringClass();
/*      */     } else {
/*      */       
/*  956 */       return false;
/*      */     } 
/*  958 */     String name = clazz.getName();
/*  959 */     return (name.startsWith("java.") || name.startsWith("org.springframework.lang."));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isInJavaLangAnnotationPackage(@Nullable Annotation annotation) {
/*  969 */     return (annotation != null && isInJavaLangAnnotationPackage(annotation.annotationType()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean isInJavaLangAnnotationPackage(@Nullable Class<? extends Annotation> annotationType) {
/*  980 */     return (annotationType != null && isInJavaLangAnnotationPackage(annotationType.getName()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isInJavaLangAnnotationPackage(@Nullable String annotationType) {
/*  991 */     return (annotationType != null && annotationType.startsWith("java.lang.annotation"));
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
/*      */   public static void validateAnnotation(Annotation annotation) {
/* 1007 */     for (Method method : getAttributeMethods(annotation.annotationType())) {
/* 1008 */       Class<?> returnType = method.getReturnType();
/* 1009 */       if (returnType == Class.class || returnType == Class[].class) {
/*      */         try {
/* 1011 */           method.invoke(annotation, new Object[0]);
/*      */         }
/* 1013 */         catch (Throwable ex) {
/* 1014 */           throw new IllegalStateException("Could not obtain annotation attribute value for " + method, ex);
/*      */         } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Map<String, Object> getAnnotationAttributes(Annotation annotation) {
/* 1036 */     return getAnnotationAttributes((AnnotatedElement)null, annotation);
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
/*      */   public static Map<String, Object> getAnnotationAttributes(Annotation annotation, boolean classValuesAsString) {
/* 1056 */     return getAnnotationAttributes(annotation, classValuesAsString, false);
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
/*      */   public static AnnotationAttributes getAnnotationAttributes(Annotation annotation, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
/* 1078 */     return getAnnotationAttributes((AnnotatedElement)null, annotation, classValuesAsString, nestedAnnotationsAsMap);
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
/*      */   public static AnnotationAttributes getAnnotationAttributes(@Nullable AnnotatedElement annotatedElement, Annotation annotation) {
/* 1097 */     return getAnnotationAttributes(annotatedElement, annotation, false, false);
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
/*      */   public static AnnotationAttributes getAnnotationAttributes(@Nullable AnnotatedElement annotatedElement, Annotation annotation, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
/* 1121 */     return getAnnotationAttributes(annotatedElement, annotation, classValuesAsString, nestedAnnotationsAsMap);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static AnnotationAttributes getAnnotationAttributes(@Nullable Object annotatedElement, Annotation annotation, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
/* 1129 */     AnnotationAttributes attributes = retrieveAnnotationAttributes(annotatedElement, annotation, classValuesAsString, nestedAnnotationsAsMap);
/* 1130 */     postProcessAnnotationAttributes(annotatedElement, attributes, classValuesAsString, nestedAnnotationsAsMap);
/* 1131 */     return attributes;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static AnnotationAttributes retrieveAnnotationAttributes(@Nullable Object annotatedElement, Annotation annotation, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
/* 1165 */     Class<? extends Annotation> annotationType = annotation.annotationType();
/* 1166 */     AnnotationAttributes attributes = new AnnotationAttributes(annotationType);
/*      */     
/* 1168 */     for (Method method : getAttributeMethods(annotationType)) {
/*      */       try {
/* 1170 */         Object attributeValue = method.invoke(annotation, new Object[0]);
/* 1171 */         Object defaultValue = method.getDefaultValue();
/* 1172 */         if (defaultValue != null && ObjectUtils.nullSafeEquals(attributeValue, defaultValue)) {
/* 1173 */           attributeValue = new DefaultValueHolder(defaultValue);
/*      */         }
/* 1175 */         attributes.put(method.getName(), 
/* 1176 */             adaptValue(annotatedElement, attributeValue, classValuesAsString, nestedAnnotationsAsMap));
/*      */       }
/* 1178 */       catch (Throwable ex) {
/* 1179 */         if (ex instanceof InvocationTargetException) {
/* 1180 */           Throwable targetException = ((InvocationTargetException)ex).getTargetException();
/* 1181 */           rethrowAnnotationConfigurationException(targetException);
/*      */         } 
/* 1183 */         throw new IllegalStateException("Could not obtain annotation attribute value for " + method, ex);
/*      */       } 
/*      */     } 
/*      */     
/* 1187 */     return attributes;
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
/*      */   @Nullable
/*      */   static Object adaptValue(@Nullable Object annotatedElement, @Nullable Object value, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
/* 1210 */     if (classValuesAsString) {
/* 1211 */       if (value instanceof Class) {
/* 1212 */         return ((Class)value).getName();
/*      */       }
/* 1214 */       if (value instanceof Class[]) {
/* 1215 */         Class<?>[] clazzArray = (Class[])value;
/* 1216 */         String[] classNames = new String[clazzArray.length];
/* 1217 */         for (int i = 0; i < clazzArray.length; i++) {
/* 1218 */           classNames[i] = clazzArray[i].getName();
/*      */         }
/* 1220 */         return classNames;
/*      */       } 
/*      */     } 
/*      */     
/* 1224 */     if (value instanceof Annotation) {
/* 1225 */       Annotation annotation = (Annotation)value;
/* 1226 */       if (nestedAnnotationsAsMap) {
/* 1227 */         return getAnnotationAttributes(annotatedElement, annotation, classValuesAsString, true);
/*      */       }
/*      */       
/* 1230 */       return synthesizeAnnotation(annotation, annotatedElement);
/*      */     } 
/*      */ 
/*      */     
/* 1234 */     if (value instanceof Annotation[]) {
/* 1235 */       Annotation[] annotations = (Annotation[])value;
/* 1236 */       if (nestedAnnotationsAsMap) {
/* 1237 */         AnnotationAttributes[] mappedAnnotations = new AnnotationAttributes[annotations.length];
/* 1238 */         for (int i = 0; i < annotations.length; i++) {
/* 1239 */           mappedAnnotations[i] = 
/* 1240 */             getAnnotationAttributes(annotatedElement, annotations[i], classValuesAsString, true);
/*      */         }
/* 1242 */         return mappedAnnotations;
/*      */       } 
/*      */       
/* 1245 */       return synthesizeAnnotationArray(annotations, annotatedElement);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1250 */     return value;
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
/*      */   public static void registerDefaultValues(AnnotationAttributes attributes) {
/* 1263 */     Class<? extends Annotation> annotationType = attributes.annotationType();
/* 1264 */     if (annotationType != null && Modifier.isPublic(annotationType.getModifiers()))
/*      */     {
/* 1266 */       for (Method annotationAttribute : getAttributeMethods(annotationType)) {
/* 1267 */         String attributeName = annotationAttribute.getName();
/* 1268 */         Object defaultValue = annotationAttribute.getDefaultValue();
/* 1269 */         if (defaultValue != null && !attributes.containsKey(attributeName)) {
/* 1270 */           if (defaultValue instanceof Annotation) {
/* 1271 */             defaultValue = getAnnotationAttributes((Annotation)defaultValue, false, true);
/*      */           }
/* 1273 */           else if (defaultValue instanceof Annotation[]) {
/* 1274 */             Annotation[] realAnnotations = (Annotation[])defaultValue;
/* 1275 */             AnnotationAttributes[] mappedAnnotations = new AnnotationAttributes[realAnnotations.length];
/* 1276 */             for (int i = 0; i < realAnnotations.length; i++) {
/* 1277 */               mappedAnnotations[i] = getAnnotationAttributes(realAnnotations[i], false, true);
/*      */             }
/* 1279 */             defaultValue = mappedAnnotations;
/*      */           } 
/* 1281 */           attributes.put(attributeName, new DefaultValueHolder(defaultValue));
/*      */         } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void postProcessAnnotationAttributes(@Nullable Object annotatedElement, AnnotationAttributes attributes, boolean classValuesAsString) {
/* 1307 */     postProcessAnnotationAttributes(annotatedElement, attributes, classValuesAsString, false);
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
/*      */   
/*      */   static void postProcessAnnotationAttributes(@Nullable Object annotatedElement, @Nullable AnnotationAttributes attributes, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
/* 1333 */     if (attributes == null) {
/*      */       return;
/*      */     }
/*      */     
/* 1337 */     Class<? extends Annotation> annotationType = attributes.annotationType();
/*      */ 
/*      */ 
/*      */     
/* 1341 */     Set<String> valuesAlreadyReplaced = new HashSet<>();
/*      */     
/* 1343 */     if (!attributes.validated) {
/*      */       
/* 1345 */       Map<String, List<String>> aliasMap = getAttributeAliasMap(annotationType);
/* 1346 */       aliasMap.forEach((attributeName, aliasedAttributeNames) -> {
/*      */             if (valuesAlreadyReplaced.contains(attributeName)) {
/*      */               return;
/*      */             }
/*      */             Object value = attributes.get(attributeName);
/* 1351 */             boolean valuePresent = (value != null && !(value instanceof DefaultValueHolder));
/*      */             for (String aliasedAttributeName : aliasedAttributeNames) {
/*      */               if (valuesAlreadyReplaced.contains(aliasedAttributeName)) {
/*      */                 continue;
/*      */               }
/*      */               Object aliasedValue = attributes.get(aliasedAttributeName);
/* 1357 */               boolean aliasPresent = (aliasedValue != null && !(aliasedValue instanceof DefaultValueHolder));
/*      */ 
/*      */               
/*      */               if (valuePresent || aliasPresent) {
/*      */                 if (valuePresent && aliasPresent) {
/*      */                   if (!ObjectUtils.nullSafeEquals(value, aliasedValue)) {
/*      */                     String elementAsString = (annotatedElement != null) ? annotatedElement.toString() : "unknown element";
/*      */ 
/*      */                     
/*      */                     throw new AnnotationConfigurationException(String.format("In AnnotationAttributes for annotation [%s] declared on %s, attribute '%s' and its alias '%s' are declared with values of [%s] and [%s], but only one is permitted.", new Object[] { attributes.displayName, elementAsString, attributeName, aliasedAttributeName, ObjectUtils.nullSafeToString(value), ObjectUtils.nullSafeToString(aliasedValue) }));
/*      */                   } 
/*      */ 
/*      */                   
/*      */                   continue;
/*      */                 } 
/*      */                 
/*      */                 if (aliasPresent) {
/*      */                   attributes.put(attributeName, adaptValue(annotatedElement, aliasedValue, classValuesAsString, nestedAnnotationsAsMap));
/*      */                   
/*      */                   valuesAlreadyReplaced.add(attributeName);
/*      */                   
/*      */                   continue;
/*      */                 } 
/*      */                 
/*      */                 attributes.put(aliasedAttributeName, adaptValue(annotatedElement, value, classValuesAsString, nestedAnnotationsAsMap));
/*      */                 
/*      */                 valuesAlreadyReplaced.add(aliasedAttributeName);
/*      */               } 
/*      */             } 
/*      */           });
/*      */       
/* 1388 */       attributes.validated = true;
/*      */     } 
/*      */ 
/*      */     
/* 1392 */     for (Map.Entry<String, Object> attributeEntry : attributes.entrySet()) {
/* 1393 */       String attributeName = attributeEntry.getKey();
/* 1394 */       if (valuesAlreadyReplaced.contains(attributeName)) {
/*      */         continue;
/*      */       }
/* 1397 */       Object value = attributeEntry.getValue();
/* 1398 */       if (value instanceof DefaultValueHolder) {
/* 1399 */         value = ((DefaultValueHolder)value).defaultValue;
/* 1400 */         attributes.put(attributeName, 
/* 1401 */             adaptValue(annotatedElement, value, classValuesAsString, nestedAnnotationsAsMap));
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
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Object getValue(Annotation annotation) {
/* 1417 */     return getValue(annotation, "value");
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
/*      */   public static Object getValue(@Nullable Annotation annotation, @Nullable String attributeName) {
/* 1432 */     if (annotation == null || !StringUtils.hasText(attributeName)) {
/* 1433 */       return null;
/*      */     }
/*      */     try {
/* 1436 */       Method method = annotation.annotationType().getDeclaredMethod(attributeName, new Class[0]);
/* 1437 */       ReflectionUtils.makeAccessible(method);
/* 1438 */       return method.invoke(annotation, new Object[0]);
/*      */     }
/* 1440 */     catch (NoSuchMethodException ex) {
/* 1441 */       return null;
/*      */     }
/* 1443 */     catch (InvocationTargetException ex) {
/* 1444 */       rethrowAnnotationConfigurationException(ex.getTargetException());
/* 1445 */       throw new IllegalStateException("Could not obtain value for annotation attribute '" + attributeName + "' in " + annotation, ex);
/*      */     
/*      */     }
/* 1448 */     catch (Throwable ex) {
/* 1449 */       handleIntrospectionFailure(annotation.getClass(), ex);
/* 1450 */       return null;
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
/*      */   @Nullable
/*      */   public static Object getDefaultValue(Annotation annotation) {
/* 1463 */     return getDefaultValue(annotation, "value");
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
/*      */   public static Object getDefaultValue(@Nullable Annotation annotation, @Nullable String attributeName) {
/* 1475 */     return (annotation != null) ? getDefaultValue(annotation.annotationType(), attributeName) : null;
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
/*      */   public static Object getDefaultValue(Class<? extends Annotation> annotationType) {
/* 1487 */     return getDefaultValue(annotationType, "value");
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
/*      */   public static Object getDefaultValue(@Nullable Class<? extends Annotation> annotationType, @Nullable String attributeName) {
/* 1502 */     if (annotationType == null || !StringUtils.hasText(attributeName)) {
/* 1503 */       return null;
/*      */     }
/*      */     try {
/* 1506 */       return annotationType.getDeclaredMethod(attributeName, new Class[0]).getDefaultValue();
/*      */     }
/* 1508 */     catch (Throwable ex) {
/* 1509 */       handleIntrospectionFailure(annotationType, ex);
/* 1510 */       return null;
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
/*      */   static <A extends Annotation> A synthesizeAnnotation(A annotation) {
/* 1529 */     return synthesizeAnnotation(annotation, (AnnotatedElement)null);
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
/*      */   public static <A extends Annotation> A synthesizeAnnotation(A annotation, @Nullable AnnotatedElement annotatedElement) {
/* 1552 */     return synthesizeAnnotation(annotation, annotatedElement);
/*      */   }
/*      */ 
/*      */   
/*      */   static <A extends Annotation> A synthesizeAnnotation(A annotation, @Nullable Object annotatedElement) {
/* 1557 */     if (annotation instanceof SynthesizedAnnotation || hasPlainJavaAnnotationsOnly(annotatedElement)) {
/* 1558 */       return annotation;
/*      */     }
/*      */     
/* 1561 */     Class<? extends Annotation> annotationType = annotation.annotationType();
/* 1562 */     if (!isSynthesizable(annotationType)) {
/* 1563 */       return annotation;
/*      */     }
/*      */     
/* 1566 */     DefaultAnnotationAttributeExtractor attributeExtractor = new DefaultAnnotationAttributeExtractor((Annotation)annotation, annotatedElement);
/*      */     
/* 1568 */     InvocationHandler handler = new SynthesizedAnnotationInvocationHandler(attributeExtractor);
/*      */ 
/*      */ 
/*      */     
/* 1572 */     Class<?>[] exposedInterfaces = new Class[] { annotationType, SynthesizedAnnotation.class };
/* 1573 */     return (A)Proxy.newProxyInstance(annotation.getClass().getClassLoader(), exposedInterfaces, handler);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <A extends Annotation> A synthesizeAnnotation(Map<String, Object> attributes, Class<A> annotationType, @Nullable AnnotatedElement annotatedElement) {
/* 1609 */     MapAnnotationAttributeExtractor attributeExtractor = new MapAnnotationAttributeExtractor(attributes, annotationType, annotatedElement);
/*      */     
/* 1611 */     InvocationHandler handler = new SynthesizedAnnotationInvocationHandler(attributeExtractor);
/* 1612 */     (new Class[2])[0] = annotationType; (new Class[2])[1] = SynthesizedAnnotation.class; (new Class[1])[0] = annotationType; Class<?>[] exposedInterfaces = canExposeSynthesizedMarker(annotationType) ? new Class[2] : new Class[1];
/*      */     
/* 1614 */     return (A)Proxy.newProxyInstance(annotationType.getClassLoader(), exposedInterfaces, handler);
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
/*      */   public static <A extends Annotation> A synthesizeAnnotation(Class<A> annotationType) {
/* 1633 */     return synthesizeAnnotation(Collections.emptyMap(), annotationType, null);
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
/*      */   static Annotation[] synthesizeAnnotationArray(Annotation[] annotations, @Nullable Object annotatedElement) {
/* 1653 */     if (hasPlainJavaAnnotationsOnly(annotatedElement)) {
/* 1654 */       return annotations;
/*      */     }
/*      */     
/* 1657 */     Annotation[] synthesized = (Annotation[])Array.newInstance(annotations
/* 1658 */         .getClass().getComponentType(), annotations.length);
/* 1659 */     for (int i = 0; i < annotations.length; i++) {
/* 1660 */       synthesized[i] = synthesizeAnnotation(annotations[i], annotatedElement);
/*      */     }
/* 1662 */     return synthesized;
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
/*      */   @Nullable
/*      */   static <A extends Annotation> A[] synthesizeAnnotationArray(@Nullable Map<String, Object>[] maps, Class<A> annotationType) {
/* 1687 */     if (maps == null) {
/* 1688 */       return null;
/*      */     }
/*      */     
/* 1691 */     Annotation[] arrayOfAnnotation = (Annotation[])Array.newInstance(annotationType, maps.length);
/* 1692 */     for (int i = 0; i < maps.length; i++) {
/* 1693 */       arrayOfAnnotation[i] = synthesizeAnnotation(maps[i], annotationType, null);
/*      */     }
/* 1695 */     return (A[])arrayOfAnnotation;
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
/*      */   static Map<String, List<String>> getAttributeAliasMap(@Nullable Class<? extends Annotation> annotationType) {
/* 1718 */     if (annotationType == null) {
/* 1719 */       return Collections.emptyMap();
/*      */     }
/*      */     
/* 1722 */     Map<String, List<String>> map = attributeAliasesCache.get(annotationType);
/* 1723 */     if (map != null) {
/* 1724 */       return map;
/*      */     }
/*      */     
/* 1727 */     map = new LinkedHashMap<>();
/* 1728 */     for (Method attribute : getAttributeMethods(annotationType)) {
/* 1729 */       List<String> aliasNames = getAttributeAliasNames(attribute);
/* 1730 */       if (!aliasNames.isEmpty()) {
/* 1731 */         map.put(attribute.getName(), aliasNames);
/*      */       }
/*      */     } 
/*      */     
/* 1735 */     attributeAliasesCache.put(annotationType, map);
/* 1736 */     return map;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean canExposeSynthesizedMarker(Class<? extends Annotation> annotationType) {
/*      */     try {
/* 1745 */       return (Class.forName(SynthesizedAnnotation.class.getName(), false, annotationType.getClassLoader()) == SynthesizedAnnotation.class);
/*      */     
/*      */     }
/* 1748 */     catch (ClassNotFoundException ex) {
/* 1749 */       return false;
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
/*      */   private static boolean isSynthesizable(Class<? extends Annotation> annotationType) {
/* 1768 */     if (hasPlainJavaAnnotationsOnly(annotationType)) {
/* 1769 */       return false;
/*      */     }
/*      */     
/* 1772 */     Boolean synthesizable = synthesizableCache.get(annotationType);
/* 1773 */     if (synthesizable != null) {
/* 1774 */       return synthesizable.booleanValue();
/*      */     }
/*      */     
/* 1777 */     synthesizable = Boolean.FALSE;
/* 1778 */     for (Method attribute : getAttributeMethods(annotationType)) {
/* 1779 */       if (!getAttributeAliasNames(attribute).isEmpty()) {
/* 1780 */         synthesizable = Boolean.TRUE;
/*      */         break;
/*      */       } 
/* 1783 */       Class<?> returnType = attribute.getReturnType();
/* 1784 */       if (Annotation[].class.isAssignableFrom(returnType)) {
/*      */         
/* 1786 */         Class<? extends Annotation> nestedAnnotationType = (Class)returnType.getComponentType();
/* 1787 */         if (isSynthesizable(nestedAnnotationType)) {
/* 1788 */           synthesizable = Boolean.TRUE; break;
/*      */         } 
/*      */         continue;
/*      */       } 
/* 1792 */       if (Annotation.class.isAssignableFrom(returnType)) {
/* 1793 */         Class<? extends Annotation> nestedAnnotationType = (Class)returnType;
/* 1794 */         if (isSynthesizable(nestedAnnotationType)) {
/* 1795 */           synthesizable = Boolean.TRUE;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/* 1801 */     synthesizableCache.put(annotationType, synthesizable);
/* 1802 */     return synthesizable.booleanValue();
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
/*      */   static List<String> getAttributeAliasNames(Method attribute) {
/* 1819 */     AliasDescriptor descriptor = AliasDescriptor.from(attribute);
/* 1820 */     return (descriptor != null) ? descriptor.getAttributeAliasNames() : Collections.<String>emptyList();
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
/*      */   @Nullable
/*      */   static String getAttributeOverrideName(Method attribute, @Nullable Class<? extends Annotation> metaAnnotationType) {
/* 1841 */     AliasDescriptor descriptor = AliasDescriptor.from(attribute);
/* 1842 */     return (descriptor != null && metaAnnotationType != null) ? descriptor
/* 1843 */       .getAttributeOverrideName(metaAnnotationType) : null;
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
/*      */   static List<Method> getAttributeMethods(Class<? extends Annotation> annotationType) {
/* 1858 */     List<Method> methods = attributeMethodsCache.get(annotationType);
/* 1859 */     if (methods != null) {
/* 1860 */       return methods;
/*      */     }
/*      */     
/* 1863 */     methods = new ArrayList<>();
/* 1864 */     for (Method method : annotationType.getDeclaredMethods()) {
/* 1865 */       if (isAttributeMethod(method)) {
/* 1866 */         ReflectionUtils.makeAccessible(method);
/* 1867 */         methods.add(method);
/*      */       } 
/*      */     } 
/*      */     
/* 1871 */     attributeMethodsCache.put(annotationType, methods);
/* 1872 */     return methods;
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
/*      */   static Annotation getAnnotation(AnnotatedElement element, String annotationName) {
/* 1886 */     for (Annotation annotation : element.getAnnotations()) {
/* 1887 */       if (annotation.annotationType().getName().equals(annotationName)) {
/* 1888 */         return annotation;
/*      */       }
/*      */     } 
/* 1891 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean isAttributeMethod(@Nullable Method method) {
/* 1901 */     return (method != null && method.getParameterCount() == 0 && method.getReturnType() != void.class);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean isAnnotationTypeMethod(@Nullable Method method) {
/* 1911 */     return (method != null && method.getName().equals("annotationType") && method.getParameterCount() == 0);
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
/*      */   static Class<? extends Annotation> resolveContainerAnnotationType(Class<? extends Annotation> annotationType) {
/* 1924 */     Repeatable repeatable = getAnnotation(annotationType, Repeatable.class);
/* 1925 */     return (repeatable != null) ? repeatable.value() : null;
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
/*      */   static void rethrowAnnotationConfigurationException(Throwable ex) {
/* 1937 */     if (ex instanceof AnnotationConfigurationException) {
/* 1938 */       throw (AnnotationConfigurationException)ex;
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
/*      */   static void handleIntrospectionFailure(@Nullable AnnotatedElement element, Throwable ex) {
/* 1957 */     rethrowAnnotationConfigurationException(ex);
/*      */     
/* 1959 */     Log loggerToUse = logger;
/* 1960 */     if (loggerToUse == null) {
/* 1961 */       loggerToUse = LogFactory.getLog(AnnotationUtils.class);
/* 1962 */       logger = loggerToUse;
/*      */     } 
/* 1964 */     if (element instanceof Class && Annotation.class.isAssignableFrom((Class)element)) {
/*      */       
/* 1966 */       if (loggerToUse.isDebugEnabled()) {
/* 1967 */         loggerToUse.debug("Failed to meta-introspect annotation " + element + ": " + ex);
/*      */       
/*      */       }
/*      */     
/*      */     }
/* 1972 */     else if (loggerToUse.isInfoEnabled()) {
/* 1973 */       loggerToUse.info("Failed to introspect annotations on " + element + ": " + ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void clearCache() {
/* 1983 */     findAnnotationCache.clear();
/* 1984 */     metaPresentCache.clear();
/* 1985 */     declaredAnnotationsCache.clear();
/* 1986 */     annotatedBaseTypeCache.clear();
/* 1987 */     synthesizableCache.clear();
/* 1988 */     attributeAliasesCache.clear();
/* 1989 */     attributeMethodsCache.clear();
/* 1990 */     aliasDescriptorCache.clear();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class AnnotationCacheKey
/*      */     implements Comparable<AnnotationCacheKey>
/*      */   {
/*      */     private final AnnotatedElement element;
/*      */     
/*      */     private final Class<? extends Annotation> annotationType;
/*      */ 
/*      */     
/*      */     public AnnotationCacheKey(AnnotatedElement element, Class<? extends Annotation> annotationType) {
/* 2004 */       this.element = element;
/* 2005 */       this.annotationType = annotationType;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object other) {
/* 2010 */       if (this == other) {
/* 2011 */         return true;
/*      */       }
/* 2013 */       if (!(other instanceof AnnotationCacheKey)) {
/* 2014 */         return false;
/*      */       }
/* 2016 */       AnnotationCacheKey otherKey = (AnnotationCacheKey)other;
/* 2017 */       return (this.element.equals(otherKey.element) && this.annotationType.equals(otherKey.annotationType));
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 2022 */       return this.element.hashCode() * 29 + this.annotationType.hashCode();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 2027 */       return "@" + this.annotationType + " on " + this.element;
/*      */     }
/*      */ 
/*      */     
/*      */     public int compareTo(AnnotationCacheKey other) {
/* 2032 */       int result = this.element.toString().compareTo(other.element.toString());
/* 2033 */       if (result == 0) {
/* 2034 */         result = this.annotationType.getName().compareTo(other.annotationType.getName());
/*      */       }
/* 2036 */       return result;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class AnnotationCollector<A extends Annotation>
/*      */   {
/*      */     private final Class<A> annotationType;
/*      */     
/*      */     @Nullable
/*      */     private final Class<? extends Annotation> containerAnnotationType;
/*      */     
/* 2048 */     private final Set<AnnotatedElement> visited = new HashSet<>();
/*      */     
/* 2050 */     private final Set<A> result = new LinkedHashSet<>();
/*      */     
/*      */     AnnotationCollector(Class<A> annotationType, @Nullable Class<? extends Annotation> containerAnnotationType) {
/* 2053 */       this.annotationType = annotationType;
/* 2054 */       this
/* 2055 */         .containerAnnotationType = (containerAnnotationType != null) ? containerAnnotationType : AnnotationUtils.resolveContainerAnnotationType(annotationType);
/*      */     }
/*      */     
/*      */     Set<A> getResult(AnnotatedElement element) {
/* 2059 */       process(element);
/* 2060 */       return Collections.unmodifiableSet(this.result);
/*      */     }
/*      */ 
/*      */     
/*      */     private void process(AnnotatedElement element) {
/* 2065 */       if (this.visited.add(element)) {
/*      */         try {
/* 2067 */           Annotation[] annotations = AnnotationUtils.getDeclaredAnnotations(element);
/* 2068 */           for (Annotation ann : annotations) {
/* 2069 */             Class<? extends Annotation> currentAnnotationType = ann.annotationType();
/* 2070 */             if (ObjectUtils.nullSafeEquals(this.annotationType, currentAnnotationType)) {
/* 2071 */               this.result.add(AnnotationUtils.synthesizeAnnotation((A)ann, element));
/*      */             }
/* 2073 */             else if (ObjectUtils.nullSafeEquals(this.containerAnnotationType, currentAnnotationType)) {
/* 2074 */               this.result.addAll(getValue(element, ann));
/*      */             }
/* 2076 */             else if (!AnnotationUtils.isInJavaLangAnnotationPackage(currentAnnotationType)) {
/* 2077 */               process(currentAnnotationType);
/*      */             }
/*      */           
/*      */           } 
/* 2081 */         } catch (Throwable ex) {
/* 2082 */           AnnotationUtils.handleIntrospectionFailure(element, ex);
/*      */         } 
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     private List<A> getValue(AnnotatedElement element, Annotation annotation) {
/*      */       try {
/* 2090 */         List<A> synthesizedAnnotations = new ArrayList<>();
/* 2091 */         Annotation[] arrayOfAnnotation = (Annotation[])AnnotationUtils.getValue(annotation);
/* 2092 */         if (arrayOfAnnotation != null) {
/* 2093 */           for (Annotation annotation1 : arrayOfAnnotation) {
/* 2094 */             synthesizedAnnotations.add(AnnotationUtils.synthesizeAnnotation((A)annotation1, element));
/*      */           }
/*      */         }
/* 2097 */         return synthesizedAnnotations;
/*      */       }
/* 2099 */       catch (Throwable ex) {
/* 2100 */         AnnotationUtils.handleIntrospectionFailure(element, ex);
/*      */ 
/*      */         
/* 2103 */         return Collections.emptyList();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class AliasDescriptor
/*      */   {
/*      */     private final Method sourceAttribute;
/*      */ 
/*      */ 
/*      */     
/*      */     private final Class<? extends Annotation> sourceAnnotationType;
/*      */ 
/*      */ 
/*      */     
/*      */     private final String sourceAttributeName;
/*      */ 
/*      */ 
/*      */     
/*      */     private final Method aliasedAttribute;
/*      */ 
/*      */ 
/*      */     
/*      */     private final Class<? extends Annotation> aliasedAnnotationType;
/*      */ 
/*      */ 
/*      */     
/*      */     private final String aliasedAttributeName;
/*      */ 
/*      */ 
/*      */     
/*      */     private final boolean isAliasPair;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public static AliasDescriptor from(Method attribute) {
/* 2145 */       AliasDescriptor descriptor = (AliasDescriptor)AnnotationUtils.aliasDescriptorCache.get(attribute);
/* 2146 */       if (descriptor != null) {
/* 2147 */         return descriptor;
/*      */       }
/*      */       
/* 2150 */       AliasFor aliasFor = attribute.<AliasFor>getAnnotation(AliasFor.class);
/* 2151 */       if (aliasFor == null) {
/* 2152 */         return null;
/*      */       }
/*      */       
/* 2155 */       descriptor = new AliasDescriptor(attribute, aliasFor);
/* 2156 */       descriptor.validate();
/* 2157 */       AnnotationUtils.aliasDescriptorCache.put(attribute, descriptor);
/* 2158 */       return descriptor;
/*      */     }
/*      */ 
/*      */     
/*      */     private AliasDescriptor(Method sourceAttribute, AliasFor aliasFor) {
/* 2163 */       Class<?> declaringClass = sourceAttribute.getDeclaringClass();
/*      */       
/* 2165 */       this.sourceAttribute = sourceAttribute;
/* 2166 */       this.sourceAnnotationType = (Class)declaringClass;
/* 2167 */       this.sourceAttributeName = sourceAttribute.getName();
/*      */       
/* 2169 */       this
/* 2170 */         .aliasedAnnotationType = (Annotation.class == aliasFor.annotation()) ? this.sourceAnnotationType : aliasFor.annotation();
/* 2171 */       this.aliasedAttributeName = getAliasedAttributeName(aliasFor, sourceAttribute);
/* 2172 */       if (this.aliasedAnnotationType == this.sourceAnnotationType && this.aliasedAttributeName
/* 2173 */         .equals(this.sourceAttributeName)) {
/* 2174 */         String msg = String.format("@AliasFor declaration on attribute '%s' in annotation [%s] points to itself. Specify 'annotation' to point to a same-named attribute on a meta-annotation.", new Object[] { sourceAttribute
/*      */               
/* 2176 */               .getName(), declaringClass.getName() });
/* 2177 */         throw new AnnotationConfigurationException(msg);
/*      */       } 
/*      */       try {
/* 2180 */         this.aliasedAttribute = this.aliasedAnnotationType.getDeclaredMethod(this.aliasedAttributeName, new Class[0]);
/*      */       }
/* 2182 */       catch (NoSuchMethodException ex) {
/* 2183 */         String msg = String.format("Attribute '%s' in annotation [%s] is declared as an @AliasFor nonexistent attribute '%s' in annotation [%s].", new Object[] { this.sourceAttributeName, this.sourceAnnotationType
/*      */               
/* 2185 */               .getName(), this.aliasedAttributeName, this.aliasedAnnotationType
/* 2186 */               .getName() });
/* 2187 */         throw new AnnotationConfigurationException(msg, ex);
/*      */       } 
/*      */       
/* 2190 */       this.isAliasPair = (this.sourceAnnotationType == this.aliasedAnnotationType);
/*      */     }
/*      */ 
/*      */     
/*      */     private void validate() {
/* 2195 */       if (!this.isAliasPair && !AnnotationUtils.isAnnotationMetaPresent(this.sourceAnnotationType, this.aliasedAnnotationType)) {
/* 2196 */         String msg = String.format("@AliasFor declaration on attribute '%s' in annotation [%s] declares an alias for attribute '%s' in meta-annotation [%s] which is not meta-present.", new Object[] { this.sourceAttributeName, this.sourceAnnotationType
/*      */               
/* 2198 */               .getName(), this.aliasedAttributeName, this.aliasedAnnotationType
/* 2199 */               .getName() });
/* 2200 */         throw new AnnotationConfigurationException(msg);
/*      */       } 
/*      */       
/* 2203 */       if (this.isAliasPair) {
/* 2204 */         AliasFor mirrorAliasFor = this.aliasedAttribute.<AliasFor>getAnnotation(AliasFor.class);
/* 2205 */         if (mirrorAliasFor == null) {
/* 2206 */           String msg = String.format("Attribute '%s' in annotation [%s] must be declared as an @AliasFor [%s].", new Object[] { this.aliasedAttributeName, this.sourceAnnotationType
/* 2207 */                 .getName(), this.sourceAttributeName });
/* 2208 */           throw new AnnotationConfigurationException(msg);
/*      */         } 
/*      */         
/* 2211 */         String mirrorAliasedAttributeName = getAliasedAttributeName(mirrorAliasFor, this.aliasedAttribute);
/* 2212 */         if (!this.sourceAttributeName.equals(mirrorAliasedAttributeName)) {
/* 2213 */           String msg = String.format("Attribute '%s' in annotation [%s] must be declared as an @AliasFor [%s], not [%s].", new Object[] { this.aliasedAttributeName, this.sourceAnnotationType
/* 2214 */                 .getName(), this.sourceAttributeName, mirrorAliasedAttributeName });
/*      */           
/* 2216 */           throw new AnnotationConfigurationException(msg);
/*      */         } 
/*      */       } 
/*      */       
/* 2220 */       Class<?> returnType = this.sourceAttribute.getReturnType();
/* 2221 */       Class<?> aliasedReturnType = this.aliasedAttribute.getReturnType();
/* 2222 */       if (returnType != aliasedReturnType && (
/* 2223 */         !aliasedReturnType.isArray() || returnType != aliasedReturnType.getComponentType())) {
/* 2224 */         String msg = String.format("Misconfigured aliases: attribute '%s' in annotation [%s] and attribute '%s' in annotation [%s] must declare the same return type.", new Object[] { this.sourceAttributeName, this.sourceAnnotationType
/*      */               
/* 2226 */               .getName(), this.aliasedAttributeName, this.aliasedAnnotationType
/* 2227 */               .getName() });
/* 2228 */         throw new AnnotationConfigurationException(msg);
/*      */       } 
/*      */       
/* 2231 */       if (this.isAliasPair) {
/* 2232 */         validateDefaultValueConfiguration(this.aliasedAttribute);
/*      */       }
/*      */     }
/*      */     
/*      */     private void validateDefaultValueConfiguration(Method aliasedAttribute) {
/* 2237 */       Object defaultValue = this.sourceAttribute.getDefaultValue();
/* 2238 */       Object aliasedDefaultValue = aliasedAttribute.getDefaultValue();
/*      */       
/* 2240 */       if (defaultValue == null || aliasedDefaultValue == null) {
/* 2241 */         String msg = String.format("Misconfigured aliases: attribute '%s' in annotation [%s] and attribute '%s' in annotation [%s] must declare default values.", new Object[] { this.sourceAttributeName, this.sourceAnnotationType
/*      */               
/* 2243 */               .getName(), aliasedAttribute.getName(), aliasedAttribute
/* 2244 */               .getDeclaringClass().getName() });
/* 2245 */         throw new AnnotationConfigurationException(msg);
/*      */       } 
/*      */       
/* 2248 */       if (!ObjectUtils.nullSafeEquals(defaultValue, aliasedDefaultValue)) {
/* 2249 */         String msg = String.format("Misconfigured aliases: attribute '%s' in annotation [%s] and attribute '%s' in annotation [%s] must declare the same default value.", new Object[] { this.sourceAttributeName, this.sourceAnnotationType
/*      */               
/* 2251 */               .getName(), aliasedAttribute.getName(), aliasedAttribute
/* 2252 */               .getDeclaringClass().getName() });
/* 2253 */         throw new AnnotationConfigurationException(msg);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void validateAgainst(AliasDescriptor otherDescriptor) {
/* 2264 */       validateDefaultValueConfiguration(otherDescriptor.sourceAttribute);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean isOverrideFor(Class<? extends Annotation> metaAnnotationType) {
/* 2273 */       return (this.aliasedAnnotationType == metaAnnotationType);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean isAliasFor(AliasDescriptor otherDescriptor) {
/* 2288 */       for (AliasDescriptor lhs = this; lhs != null; lhs = lhs.getAttributeOverrideDescriptor()) {
/* 2289 */         for (AliasDescriptor rhs = otherDescriptor; rhs != null; rhs = rhs.getAttributeOverrideDescriptor()) {
/* 2290 */           if (lhs.aliasedAttribute.equals(rhs.aliasedAttribute)) {
/* 2291 */             return true;
/*      */           }
/*      */         } 
/*      */       } 
/* 2295 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public List<String> getAttributeAliasNames() {
/* 2300 */       if (this.isAliasPair) {
/* 2301 */         return Collections.singletonList(this.aliasedAttributeName);
/*      */       }
/*      */ 
/*      */       
/* 2305 */       List<String> aliases = new ArrayList<>();
/* 2306 */       for (AliasDescriptor otherDescriptor : getOtherDescriptors()) {
/* 2307 */         if (isAliasFor(otherDescriptor)) {
/* 2308 */           validateAgainst(otherDescriptor);
/* 2309 */           aliases.add(otherDescriptor.sourceAttributeName);
/*      */         } 
/*      */       } 
/* 2312 */       return aliases;
/*      */     }
/*      */     
/*      */     private List<AliasDescriptor> getOtherDescriptors() {
/* 2316 */       List<AliasDescriptor> otherDescriptors = new ArrayList<>();
/* 2317 */       for (Method currentAttribute : AnnotationUtils.getAttributeMethods(this.sourceAnnotationType)) {
/* 2318 */         if (!this.sourceAttribute.equals(currentAttribute)) {
/* 2319 */           AliasDescriptor otherDescriptor = from(currentAttribute);
/* 2320 */           if (otherDescriptor != null) {
/* 2321 */             otherDescriptors.add(otherDescriptor);
/*      */           }
/*      */         } 
/*      */       } 
/* 2325 */       return otherDescriptors;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public String getAttributeOverrideName(Class<? extends Annotation> metaAnnotationType) {
/* 2331 */       for (AliasDescriptor desc = this; desc != null; desc = desc.getAttributeOverrideDescriptor()) {
/* 2332 */         if (desc.isOverrideFor(metaAnnotationType)) {
/* 2333 */           return desc.aliasedAttributeName;
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 2338 */       return null;
/*      */     }
/*      */     
/*      */     @Nullable
/*      */     private AliasDescriptor getAttributeOverrideDescriptor() {
/* 2343 */       if (this.isAliasPair) {
/* 2344 */         return null;
/*      */       }
/* 2346 */       return from(this.aliasedAttribute);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private String getAliasedAttributeName(AliasFor aliasFor, Method attribute) {
/* 2366 */       String attributeName = aliasFor.attribute();
/* 2367 */       String value = aliasFor.value();
/* 2368 */       boolean attributeDeclared = StringUtils.hasText(attributeName);
/* 2369 */       boolean valueDeclared = StringUtils.hasText(value);
/*      */ 
/*      */       
/* 2372 */       if (attributeDeclared && valueDeclared) {
/* 2373 */         String msg = String.format("In @AliasFor declared on attribute '%s' in annotation [%s], attribute 'attribute' and its alias 'value' are present with values of [%s] and [%s], but only one is permitted.", new Object[] { attribute
/*      */               
/* 2375 */               .getName(), attribute.getDeclaringClass().getName(), attributeName, value });
/* 2376 */         throw new AnnotationConfigurationException(msg);
/*      */       } 
/*      */ 
/*      */       
/* 2380 */       attributeName = attributeDeclared ? attributeName : value;
/* 2381 */       return StringUtils.hasText(attributeName) ? attributeName.trim() : attribute.getName();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 2386 */       return String.format("%s: @%s(%s) is an alias for @%s(%s)", new Object[] { getClass().getSimpleName(), this.sourceAnnotationType
/* 2387 */             .getSimpleName(), this.sourceAttributeName, this.aliasedAnnotationType
/* 2388 */             .getSimpleName(), this.aliasedAttributeName });
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class DefaultValueHolder
/*      */   {
/*      */     final Object defaultValue;
/*      */     
/*      */     public DefaultValueHolder(Object defaultValue) {
/* 2398 */       this.defaultValue = defaultValue;
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/annotation/AnnotationUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */