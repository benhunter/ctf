/*      */ package org.springframework.core.annotation;
/*      */ 
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.lang.reflect.AnnotatedElement;
/*      */ import java.lang.reflect.Method;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import org.springframework.core.BridgeMethodResolver;
/*      */ import org.springframework.lang.Nullable;
/*      */ import org.springframework.util.CollectionUtils;
/*      */ import org.springframework.util.LinkedMultiValueMap;
/*      */ import org.springframework.util.MultiValueMap;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class AnnotatedElementUtils
/*      */ {
/*      */   @Nullable
/*  104 */   private static final Boolean CONTINUE = null;
/*      */   
/*  106 */   private static final Annotation[] EMPTY_ANNOTATION_ARRAY = new Annotation[0];
/*      */   
/*  108 */   private static final Processor<Boolean> alwaysTrueAnnotationProcessor = new AlwaysTrueBooleanAnnotationProcessor();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static AnnotatedElement forAnnotations(Annotation... annotations) {
/*  118 */     return new AnnotatedElement()
/*      */       {
/*      */         @Nullable
/*      */         public <T extends Annotation> T getAnnotation(Class<T> annotationClass)
/*      */         {
/*  123 */           for (Annotation ann : annotations) {
/*  124 */             if (ann.annotationType() == annotationClass) {
/*  125 */               return (T)ann;
/*      */             }
/*      */           } 
/*  128 */           return null;
/*      */         }
/*      */         
/*      */         public Annotation[] getAnnotations() {
/*  132 */           return annotations;
/*      */         }
/*      */         
/*      */         public Annotation[] getDeclaredAnnotations() {
/*  136 */           return annotations;
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
/*      */ 
/*      */ 
/*      */   
/*      */   public static Set<String> getMetaAnnotationTypes(AnnotatedElement element, Class<? extends Annotation> annotationType) {
/*  156 */     return getMetaAnnotationTypes(element, element.getAnnotation((Class)annotationType));
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
/*      */   public static Set<String> getMetaAnnotationTypes(AnnotatedElement element, String annotationName) {
/*  174 */     return getMetaAnnotationTypes(element, AnnotationUtils.getAnnotation(element, annotationName));
/*      */   }
/*      */   
/*      */   private static Set<String> getMetaAnnotationTypes(AnnotatedElement element, @Nullable Annotation composed) {
/*  178 */     if (composed == null) {
/*  179 */       return Collections.emptySet();
/*      */     }
/*      */     
/*      */     try {
/*  183 */       final Set<String> types = new LinkedHashSet<>();
/*  184 */       searchWithGetSemantics(composed.annotationType(), Collections.emptySet(), null, null, new SimpleAnnotationProcessor(true)
/*      */           {
/*      */             @Nullable
/*      */             public Object process(@Nullable AnnotatedElement annotatedElement, Annotation annotation, int metaDepth) {
/*  188 */               types.add(annotation.annotationType().getName());
/*  189 */               return AnnotatedElementUtils.CONTINUE;
/*      */             }
/*      */           }new HashSet<>(), 1);
/*  192 */       return types;
/*      */     }
/*  194 */     catch (Throwable ex) {
/*  195 */       AnnotationUtils.rethrowAnnotationConfigurationException(ex);
/*  196 */       throw new IllegalStateException("Failed to introspect annotations on " + element, ex);
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
/*      */   public static boolean hasMetaAnnotationTypes(AnnotatedElement element, Class<? extends Annotation> annotationType) {
/*  213 */     return hasMetaAnnotationTypes(element, annotationType, null);
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
/*      */   public static boolean hasMetaAnnotationTypes(AnnotatedElement element, String annotationName) {
/*  229 */     return hasMetaAnnotationTypes(element, null, annotationName);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean hasMetaAnnotationTypes(AnnotatedElement element, @Nullable Class<? extends Annotation> annotationType, @Nullable String annotationName) {
/*  235 */     return Boolean.TRUE.equals(
/*  236 */         searchWithGetSemantics(element, annotationType, annotationName, new SimpleAnnotationProcessor<Boolean>()
/*      */           {
/*      */             @Nullable
/*      */             public Boolean process(@Nullable AnnotatedElement annotatedElement, Annotation annotation, int metaDepth) {
/*  240 */               return (metaDepth > 0) ? Boolean.TRUE : AnnotatedElementUtils.CONTINUE;
/*      */             }
/*      */           }));
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
/*      */   public static boolean isAnnotated(AnnotatedElement element, Class<? extends Annotation> annotationType) {
/*  261 */     if (element.isAnnotationPresent(annotationType)) {
/*  262 */       return true;
/*      */     }
/*  264 */     return Boolean.TRUE.equals(searchWithGetSemantics(element, annotationType, null, alwaysTrueAnnotationProcessor));
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
/*      */   public static boolean isAnnotated(AnnotatedElement element, String annotationName) {
/*  280 */     return Boolean.TRUE.equals(searchWithGetSemantics(element, null, annotationName, alwaysTrueAnnotationProcessor));
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
/*      */   @Nullable
/*      */   public static AnnotationAttributes getMergedAnnotationAttributes(AnnotatedElement element, Class<? extends Annotation> annotationType) {
/*  304 */     AnnotationAttributes attributes = searchWithGetSemantics(element, annotationType, null, new MergedAnnotationAttributesProcessor());
/*      */     
/*  306 */     AnnotationUtils.postProcessAnnotationAttributes(element, attributes, false, false);
/*  307 */     return attributes;
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
/*      */   public static AnnotationAttributes getMergedAnnotationAttributes(AnnotatedElement element, String annotationName) {
/*  330 */     return getMergedAnnotationAttributes(element, annotationName, false, false);
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
/*      */   @Nullable
/*      */   public static AnnotationAttributes getMergedAnnotationAttributes(AnnotatedElement element, String annotationName, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
/*  363 */     AnnotationAttributes attributes = searchWithGetSemantics(element, null, annotationName, new MergedAnnotationAttributesProcessor(classValuesAsString, nestedAnnotationsAsMap));
/*      */     
/*  365 */     AnnotationUtils.postProcessAnnotationAttributes(element, attributes, classValuesAsString, nestedAnnotationsAsMap);
/*  366 */     return attributes;
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
/*      */   @Nullable
/*      */   public static <A extends Annotation> A getMergedAnnotation(AnnotatedElement element, Class<A> annotationType) {
/*  390 */     A annotation = element.getDeclaredAnnotation(annotationType);
/*  391 */     if (annotation != null) {
/*  392 */       return AnnotationUtils.synthesizeAnnotation(annotation, element);
/*      */     }
/*      */ 
/*      */     
/*  396 */     if (AnnotationUtils.hasPlainJavaAnnotationsOnly(element)) {
/*  397 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  401 */     AnnotationAttributes attributes = getMergedAnnotationAttributes(element, annotationType);
/*  402 */     return (attributes != null) ? AnnotationUtils.<A>synthesizeAnnotation(attributes, annotationType, element) : null;
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
/*      */   public static <A extends Annotation> Set<A> getAllMergedAnnotations(AnnotatedElement element, Class<A> annotationType) {
/*  426 */     MergedAnnotationAttributesProcessor processor = new MergedAnnotationAttributesProcessor(false, false, true);
/*  427 */     searchWithGetSemantics(element, annotationType, null, processor);
/*  428 */     return postProcessAndSynthesizeAggregatedResults(element, processor.getAggregatedResults());
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
/*      */   public static Set<Annotation> getAllMergedAnnotations(AnnotatedElement element, Set<Class<? extends Annotation>> annotationTypes) {
/*  450 */     MergedAnnotationAttributesProcessor processor = new MergedAnnotationAttributesProcessor(false, false, true);
/*  451 */     searchWithGetSemantics(element, annotationTypes, null, null, processor);
/*  452 */     return postProcessAndSynthesizeAggregatedResults(element, processor.getAggregatedResults());
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
/*      */   public static <A extends Annotation> Set<A> getMergedRepeatableAnnotations(AnnotatedElement element, Class<A> annotationType) {
/*  482 */     return getMergedRepeatableAnnotations(element, annotationType, null);
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
/*      */   public static <A extends Annotation> Set<A> getMergedRepeatableAnnotations(AnnotatedElement element, Class<A> annotationType, @Nullable Class<? extends Annotation> containerType) {
/*  514 */     if (containerType == null) {
/*  515 */       containerType = resolveContainerType(annotationType);
/*      */     } else {
/*      */       
/*  518 */       validateContainerType(annotationType, containerType);
/*      */     } 
/*      */     
/*  521 */     MergedAnnotationAttributesProcessor processor = new MergedAnnotationAttributesProcessor(false, false, true);
/*  522 */     searchWithGetSemantics(element, (Set)Collections.singleton(annotationType), null, containerType, processor);
/*  523 */     return postProcessAndSynthesizeAggregatedResults(element, processor.getAggregatedResults());
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
/*      */   public static MultiValueMap<String, Object> getAllAnnotationAttributes(AnnotatedElement element, String annotationName) {
/*  542 */     return getAllAnnotationAttributes(element, annotationName, false, false);
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
/*      */   public static MultiValueMap<String, Object> getAllAnnotationAttributes(AnnotatedElement element, String annotationName, final boolean classValuesAsString, final boolean nestedAnnotationsAsMap) {
/*  567 */     final LinkedMultiValueMap attributesMap = new LinkedMultiValueMap();
/*      */     
/*  569 */     searchWithGetSemantics(element, null, annotationName, new SimpleAnnotationProcessor()
/*      */         {
/*      */           @Nullable
/*      */           public Object process(@Nullable AnnotatedElement annotatedElement, Annotation annotation, int metaDepth) {
/*  573 */             AnnotationAttributes annotationAttributes = AnnotationUtils.getAnnotationAttributes(annotation, classValuesAsString, nestedAnnotationsAsMap);
/*      */             
/*  575 */             annotationAttributes.forEach(attributesMap::add);
/*  576 */             return AnnotatedElementUtils.CONTINUE;
/*      */           }
/*      */         });
/*      */     
/*  580 */     return !linkedMultiValueMap.isEmpty() ? (MultiValueMap<String, Object>)linkedMultiValueMap : null;
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
/*      */   public static boolean hasAnnotation(AnnotatedElement element, Class<? extends Annotation> annotationType) {
/*  599 */     if (element.isAnnotationPresent(annotationType)) {
/*  600 */       return true;
/*      */     }
/*  602 */     return Boolean.TRUE.equals(searchWithFindSemantics(element, annotationType, null, alwaysTrueAnnotationProcessor));
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
/*      */   @Nullable
/*      */   public static AnnotationAttributes findMergedAnnotationAttributes(AnnotatedElement element, Class<? extends Annotation> annotationType, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
/*  636 */     AnnotationAttributes attributes = searchWithFindSemantics(element, annotationType, null, new MergedAnnotationAttributesProcessor(classValuesAsString, nestedAnnotationsAsMap));
/*      */     
/*  638 */     AnnotationUtils.postProcessAnnotationAttributes(element, attributes, classValuesAsString, nestedAnnotationsAsMap);
/*  639 */     return attributes;
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
/*      */   @Nullable
/*      */   public static AnnotationAttributes findMergedAnnotationAttributes(AnnotatedElement element, String annotationName, boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
/*  673 */     AnnotationAttributes attributes = searchWithFindSemantics(element, null, annotationName, new MergedAnnotationAttributesProcessor(classValuesAsString, nestedAnnotationsAsMap));
/*      */     
/*  675 */     AnnotationUtils.postProcessAnnotationAttributes(element, attributes, classValuesAsString, nestedAnnotationsAsMap);
/*  676 */     return attributes;
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
/*      */   @Nullable
/*      */   public static <A extends Annotation> A findMergedAnnotation(AnnotatedElement element, Class<A> annotationType) {
/*  700 */     A annotation = element.getDeclaredAnnotation(annotationType);
/*  701 */     if (annotation != null) {
/*  702 */       return AnnotationUtils.synthesizeAnnotation(annotation, element);
/*      */     }
/*      */ 
/*      */     
/*  706 */     if (AnnotationUtils.hasPlainJavaAnnotationsOnly(element)) {
/*  707 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  711 */     AnnotationAttributes attributes = findMergedAnnotationAttributes(element, annotationType, false, false);
/*  712 */     return (attributes != null) ? AnnotationUtils.<A>synthesizeAnnotation(attributes, annotationType, element) : null;
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
/*      */   public static <A extends Annotation> Set<A> findAllMergedAnnotations(AnnotatedElement element, Class<A> annotationType) {
/*  735 */     MergedAnnotationAttributesProcessor processor = new MergedAnnotationAttributesProcessor(false, false, true);
/*  736 */     searchWithFindSemantics(element, annotationType, null, processor);
/*  737 */     return postProcessAndSynthesizeAggregatedResults(element, processor.getAggregatedResults());
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
/*      */   public static Set<Annotation> findAllMergedAnnotations(AnnotatedElement element, Set<Class<? extends Annotation>> annotationTypes) {
/*  759 */     MergedAnnotationAttributesProcessor processor = new MergedAnnotationAttributesProcessor(false, false, true);
/*  760 */     searchWithFindSemantics(element, annotationTypes, null, null, processor);
/*  761 */     return postProcessAndSynthesizeAggregatedResults(element, processor.getAggregatedResults());
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
/*      */   public static <A extends Annotation> Set<A> findMergedRepeatableAnnotations(AnnotatedElement element, Class<A> annotationType) {
/*  791 */     return findMergedRepeatableAnnotations(element, annotationType, null);
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
/*      */   public static <A extends Annotation> Set<A> findMergedRepeatableAnnotations(AnnotatedElement element, Class<A> annotationType, @Nullable Class<? extends Annotation> containerType) {
/*  823 */     if (containerType == null) {
/*  824 */       containerType = resolveContainerType(annotationType);
/*      */     } else {
/*      */       
/*  827 */       validateContainerType(annotationType, containerType);
/*      */     } 
/*      */     
/*  830 */     MergedAnnotationAttributesProcessor processor = new MergedAnnotationAttributesProcessor(false, false, true);
/*  831 */     searchWithFindSemantics(element, (Set)Collections.singleton(annotationType), null, containerType, processor);
/*  832 */     return postProcessAndSynthesizeAggregatedResults(element, processor.getAggregatedResults());
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
/*      */   private static <T> T searchWithGetSemantics(AnnotatedElement element, @Nullable Class<? extends Annotation> annotationType, @Nullable String annotationName, Processor<T> processor) {
/*  851 */     return searchWithGetSemantics(element, (annotationType != null) ? 
/*  852 */         Collections.<Class<? extends Annotation>>singleton(annotationType) : Collections.<Class<? extends Annotation>>emptySet(), annotationName, null, processor);
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
/*      */   private static <T> T searchWithGetSemantics(AnnotatedElement element, Set<Class<? extends Annotation>> annotationTypes, @Nullable String annotationName, @Nullable Class<? extends Annotation> containerType, Processor<T> processor) {
/*      */     try {
/*  876 */       return searchWithGetSemantics(element, annotationTypes, annotationName, containerType, processor, new HashSet<>(), 0);
/*      */     
/*      */     }
/*  879 */     catch (Throwable ex) {
/*  880 */       AnnotationUtils.rethrowAnnotationConfigurationException(ex);
/*  881 */       throw new IllegalStateException("Failed to introspect annotations on " + element, ex);
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
/*      */   @Nullable
/*      */   private static <T> T searchWithGetSemantics(AnnotatedElement element, Set<Class<? extends Annotation>> annotationTypes, @Nullable String annotationName, @Nullable Class<? extends Annotation> containerType, Processor<T> processor, Set<AnnotatedElement> visited, int metaDepth) {
/*  908 */     if (visited.add(element)) {
/*      */       
/*      */       try {
/*  911 */         List<Annotation> declaredAnnotations = Arrays.asList(AnnotationUtils.getDeclaredAnnotations(element));
/*  912 */         T result = searchWithGetSemanticsInAnnotations(element, declaredAnnotations, annotationTypes, annotationName, containerType, processor, visited, metaDepth);
/*      */         
/*  914 */         if (result != null) {
/*  915 */           return result;
/*      */         }
/*      */         
/*  918 */         if (element instanceof Class) {
/*  919 */           Class<?> superclass = ((Class)element).getSuperclass();
/*  920 */           if (superclass != null && superclass != Object.class) {
/*  921 */             List<Annotation> inheritedAnnotations = new LinkedList<>();
/*  922 */             for (Annotation annotation : element.getAnnotations()) {
/*  923 */               if (!declaredAnnotations.contains(annotation)) {
/*  924 */                 inheritedAnnotations.add(annotation);
/*      */               }
/*      */             } 
/*      */             
/*  928 */             result = searchWithGetSemanticsInAnnotations(element, inheritedAnnotations, annotationTypes, annotationName, containerType, processor, visited, metaDepth);
/*      */             
/*  930 */             if (result != null) {
/*  931 */               return result;
/*      */             }
/*      */           }
/*      */         
/*      */         } 
/*  936 */       } catch (Throwable ex) {
/*  937 */         AnnotationUtils.handleIntrospectionFailure(element, ex);
/*      */       } 
/*      */     }
/*      */     
/*  941 */     return null;
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
/*      */   @Nullable
/*      */   private static <T> T searchWithGetSemanticsInAnnotations(@Nullable AnnotatedElement element, List<Annotation> annotations, Set<Class<? extends Annotation>> annotationTypes, @Nullable String annotationName, @Nullable Class<? extends Annotation> containerType, Processor<T> processor, Set<AnnotatedElement> visited, int metaDepth) {
/*  973 */     for (Annotation annotation : annotations) {
/*  974 */       Class<? extends Annotation> currentAnnotationType = annotation.annotationType();
/*  975 */       if (!AnnotationUtils.isInJavaLangAnnotationPackage(currentAnnotationType)) {
/*  976 */         if (annotationTypes.contains(currentAnnotationType) || currentAnnotationType
/*  977 */           .getName().equals(annotationName) || processor
/*  978 */           .alwaysProcesses()) {
/*  979 */           T result = processor.process(element, annotation, metaDepth);
/*  980 */           if (result != null) {
/*  981 */             if (processor.aggregates() && metaDepth == 0) {
/*  982 */               processor.getAggregatedResults().add(result);
/*      */               continue;
/*      */             } 
/*  985 */             return result;
/*      */           } 
/*      */           
/*      */           continue;
/*      */         } 
/*  990 */         if (currentAnnotationType == containerType) {
/*  991 */           for (Object object : getRawAnnotationsFromContainer(element, annotation)) {
/*  992 */             T result = processor.process(element, (Annotation)object, metaDepth);
/*  993 */             if (result != null)
/*      */             {
/*      */               
/*  996 */               processor.getAggregatedResults().add(result);
/*      */             }
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1004 */     for (Annotation annotation : annotations) {
/* 1005 */       Class<? extends Annotation> currentAnnotationType = annotation.annotationType();
/* 1006 */       if (!AnnotationUtils.hasPlainJavaAnnotationsOnly(currentAnnotationType)) {
/* 1007 */         T result = searchWithGetSemantics(currentAnnotationType, annotationTypes, annotationName, containerType, processor, visited, metaDepth + 1);
/*      */         
/* 1009 */         if (result != null) {
/* 1010 */           processor.postProcess(element, annotation, result);
/* 1011 */           if (processor.aggregates() && metaDepth == 0) {
/* 1012 */             processor.getAggregatedResults().add(result);
/*      */             continue;
/*      */           } 
/* 1015 */           return result;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1021 */     return null;
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
/*      */   private static <T> T searchWithFindSemantics(AnnotatedElement element, @Nullable Class<? extends Annotation> annotationType, @Nullable String annotationName, Processor<T> processor) {
/* 1041 */     return searchWithFindSemantics(element, (annotationType != null) ? 
/* 1042 */         Collections.<Class<? extends Annotation>>singleton(annotationType) : Collections.<Class<? extends Annotation>>emptySet(), annotationName, null, processor);
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
/*      */   private static <T> T searchWithFindSemantics(AnnotatedElement element, Set<Class<? extends Annotation>> annotationTypes, @Nullable String annotationName, @Nullable Class<? extends Annotation> containerType, Processor<T> processor) {
/* 1065 */     if (containerType != null && !processor.aggregates()) {
/* 1066 */       throw new IllegalArgumentException("Searches for repeatable annotations must supply an aggregating Processor");
/*      */     }
/*      */ 
/*      */     
/*      */     try {
/* 1071 */       return searchWithFindSemantics(element, annotationTypes, annotationName, containerType, processor, new HashSet<>(), 0);
/*      */     
/*      */     }
/* 1074 */     catch (Throwable ex) {
/* 1075 */       AnnotationUtils.rethrowAnnotationConfigurationException(ex);
/* 1076 */       throw new IllegalStateException("Failed to introspect annotations on " + element, ex);
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
/*      */   @Nullable
/*      */   private static <T> T searchWithFindSemantics(AnnotatedElement element, Set<Class<? extends Annotation>> annotationTypes, @Nullable String annotationName, @Nullable Class<? extends Annotation> containerType, Processor<T> processor, Set<AnnotatedElement> visited, int metaDepth) {
/* 1104 */     if (visited.add(element)) {
/*      */       
/*      */       try {
/* 1107 */         Annotation[] annotations = AnnotationUtils.getDeclaredAnnotations(element);
/* 1108 */         if (annotations.length > 0) {
/* 1109 */           List<T> aggregatedResults = processor.aggregates() ? new ArrayList<>() : null;
/*      */ 
/*      */           
/* 1112 */           for (Annotation annotation : annotations) {
/* 1113 */             Class<? extends Annotation> currentAnnotationType = annotation.annotationType();
/* 1114 */             if (!AnnotationUtils.isInJavaLangAnnotationPackage(currentAnnotationType)) {
/* 1115 */               if (annotationTypes.contains(currentAnnotationType) || currentAnnotationType
/* 1116 */                 .getName().equals(annotationName) || processor
/* 1117 */                 .alwaysProcesses()) {
/* 1118 */                 T result = processor.process(element, annotation, metaDepth);
/* 1119 */                 if (result != null) {
/* 1120 */                   if (aggregatedResults != null && metaDepth == 0) {
/* 1121 */                     aggregatedResults.add(result);
/*      */                   } else {
/*      */                     
/* 1124 */                     return result;
/*      */                   }
/*      */                 
/*      */                 }
/*      */               }
/* 1129 */               else if (currentAnnotationType == containerType) {
/* 1130 */                 for (Object object : getRawAnnotationsFromContainer(element, annotation)) {
/* 1131 */                   T result = processor.process(element, (Annotation)object, metaDepth);
/* 1132 */                   if (aggregatedResults != null && result != null)
/*      */                   {
/*      */                     
/* 1135 */                     aggregatedResults.add(result);
/*      */                   }
/*      */                 } 
/*      */               } 
/*      */             }
/*      */           } 
/*      */ 
/*      */           
/* 1143 */           for (Annotation annotation : annotations) {
/* 1144 */             Class<? extends Annotation> currentAnnotationType = annotation.annotationType();
/* 1145 */             if (!AnnotationUtils.hasPlainJavaAnnotationsOnly(currentAnnotationType)) {
/* 1146 */               T result = searchWithFindSemantics(currentAnnotationType, annotationTypes, annotationName, containerType, processor, visited, metaDepth + 1);
/*      */               
/* 1148 */               if (result != null) {
/* 1149 */                 processor.postProcess(currentAnnotationType, annotation, result);
/* 1150 */                 if (aggregatedResults != null && metaDepth == 0) {
/* 1151 */                   aggregatedResults.add(result);
/*      */                 } else {
/*      */                   
/* 1154 */                   return result;
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } 
/*      */           
/* 1160 */           if (!CollectionUtils.isEmpty(aggregatedResults))
/*      */           {
/* 1162 */             processor.getAggregatedResults().addAll(0, aggregatedResults);
/*      */           }
/*      */         } 
/*      */         
/* 1166 */         if (element instanceof Method) {
/* 1167 */           Method method = (Method)element;
/*      */ 
/*      */ 
/*      */           
/* 1171 */           Method resolvedMethod = BridgeMethodResolver.findBridgedMethod(method);
/* 1172 */           if (resolvedMethod != method) {
/* 1173 */             T result = searchWithFindSemantics(resolvedMethod, annotationTypes, annotationName, containerType, processor, visited, metaDepth);
/*      */             
/* 1175 */             if (result != null) {
/* 1176 */               return result;
/*      */             }
/*      */           } 
/*      */ 
/*      */           
/* 1181 */           Class<?>[] ifcs = method.getDeclaringClass().getInterfaces();
/* 1182 */           if (ifcs.length > 0) {
/* 1183 */             T result = searchOnInterfaces(method, annotationTypes, annotationName, containerType, processor, visited, metaDepth, ifcs);
/*      */             
/* 1185 */             if (result != null) {
/* 1186 */               return result;
/*      */             }
/*      */           } 
/*      */ 
/*      */           
/* 1191 */           Class<?> clazz = method.getDeclaringClass();
/*      */           while (true) {
/* 1193 */             clazz = clazz.getSuperclass();
/* 1194 */             if (clazz == null || clazz == Object.class) {
/*      */               break;
/*      */             }
/* 1197 */             Set<Method> annotatedMethods = AnnotationUtils.getAnnotatedMethodsInBaseType(clazz);
/* 1198 */             if (!annotatedMethods.isEmpty()) {
/* 1199 */               for (Method annotatedMethod : annotatedMethods) {
/* 1200 */                 if (AnnotationUtils.isOverride(method, annotatedMethod)) {
/* 1201 */                   Method resolvedSuperMethod = BridgeMethodResolver.findBridgedMethod(annotatedMethod);
/* 1202 */                   T t = searchWithFindSemantics(resolvedSuperMethod, annotationTypes, annotationName, containerType, processor, visited, metaDepth);
/*      */                   
/* 1204 */                   if (t != null) {
/* 1205 */                     return t;
/*      */                   }
/*      */                 } 
/*      */               } 
/*      */             }
/*      */             
/* 1211 */             T result = searchOnInterfaces(method, annotationTypes, annotationName, containerType, processor, visited, metaDepth, clazz
/* 1212 */                 .getInterfaces());
/* 1213 */             if (result != null) {
/* 1214 */               return result;
/*      */             }
/*      */           }
/*      */         
/* 1218 */         } else if (element instanceof Class) {
/* 1219 */           Class<?> clazz = (Class)element;
/* 1220 */           if (!Annotation.class.isAssignableFrom(clazz)) {
/*      */             
/* 1222 */             for (Class<?> ifc : clazz.getInterfaces()) {
/* 1223 */               T result = searchWithFindSemantics(ifc, annotationTypes, annotationName, containerType, processor, visited, metaDepth);
/*      */               
/* 1225 */               if (result != null) {
/* 1226 */                 return result;
/*      */               }
/*      */             } 
/*      */             
/* 1230 */             Class<?> superclass = clazz.getSuperclass();
/* 1231 */             if (superclass != null && superclass != Object.class) {
/* 1232 */               T result = searchWithFindSemantics(superclass, annotationTypes, annotationName, containerType, processor, visited, metaDepth);
/*      */               
/* 1234 */               if (result != null) {
/* 1235 */                 return result;
/*      */               }
/*      */             }
/*      */           
/*      */           } 
/*      */         } 
/* 1241 */       } catch (Throwable ex) {
/* 1242 */         AnnotationUtils.handleIntrospectionFailure(element, ex);
/*      */       } 
/*      */     }
/* 1245 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private static <T> T searchOnInterfaces(Method method, Set<Class<? extends Annotation>> annotationTypes, @Nullable String annotationName, @Nullable Class<? extends Annotation> containerType, Processor<T> processor, Set<AnnotatedElement> visited, int metaDepth, Class<?>[] ifcs) {
/* 1253 */     for (Class<?> ifc : ifcs) {
/* 1254 */       Set<Method> annotatedMethods = AnnotationUtils.getAnnotatedMethodsInBaseType(ifc);
/* 1255 */       if (!annotatedMethods.isEmpty()) {
/* 1256 */         for (Method annotatedMethod : annotatedMethods) {
/* 1257 */           if (AnnotationUtils.isOverride(method, annotatedMethod)) {
/* 1258 */             T result = searchWithFindSemantics(annotatedMethod, annotationTypes, annotationName, containerType, processor, visited, metaDepth);
/*      */             
/* 1260 */             if (result != null) {
/* 1261 */               return result;
/*      */             }
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/* 1268 */     return null;
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
/*      */   private static <A extends Annotation> A[] getRawAnnotationsFromContainer(@Nullable AnnotatedElement element, Annotation container) {
/*      */     try {
/* 1281 */       Annotation[] arrayOfAnnotation = (Annotation[])AnnotationUtils.getValue(container);
/* 1282 */       if (arrayOfAnnotation != null) {
/* 1283 */         return (A[])arrayOfAnnotation;
/*      */       }
/*      */     }
/* 1286 */     catch (Throwable ex) {
/* 1287 */       AnnotationUtils.handleIntrospectionFailure(element, ex);
/*      */     } 
/*      */     
/* 1290 */     return (A[])EMPTY_ANNOTATION_ARRAY;
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
/*      */   private static Class<? extends Annotation> resolveContainerType(Class<? extends Annotation> annotationType) {
/* 1302 */     Class<? extends Annotation> containerType = AnnotationUtils.resolveContainerAnnotationType(annotationType);
/* 1303 */     if (containerType == null) {
/* 1304 */       throw new IllegalArgumentException("Annotation type must be a repeatable annotation: failed to resolve container type for " + annotationType
/*      */           
/* 1306 */           .getName());
/*      */     }
/* 1308 */     return containerType;
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
/*      */   private static void validateContainerType(Class<? extends Annotation> annotationType, Class<? extends Annotation> containerType) {
/*      */     try {
/* 1324 */       Method method = containerType.getDeclaredMethod("value", new Class[0]);
/* 1325 */       Class<?> returnType = method.getReturnType();
/* 1326 */       if (!returnType.isArray() || returnType.getComponentType() != annotationType) {
/* 1327 */         String msg = String.format("Container type [%s] must declare a 'value' attribute for an array of type [%s]", new Object[] { containerType
/*      */               
/* 1329 */               .getName(), annotationType.getName() });
/* 1330 */         throw new AnnotationConfigurationException(msg);
/*      */       }
/*      */     
/* 1333 */     } catch (Throwable ex) {
/* 1334 */       AnnotationUtils.rethrowAnnotationConfigurationException(ex);
/* 1335 */       String msg = String.format("Invalid declaration of container type [%s] for repeatable annotation [%s]", new Object[] { containerType
/* 1336 */             .getName(), annotationType.getName() });
/* 1337 */       throw new AnnotationConfigurationException(msg, ex);
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
/*      */   private static <A extends Annotation> Set<A> postProcessAndSynthesizeAggregatedResults(AnnotatedElement element, List<AnnotationAttributes> aggregatedResults) {
/* 1351 */     Set<A> annotations = new LinkedHashSet<>();
/* 1352 */     for (AnnotationAttributes attributes : aggregatedResults) {
/* 1353 */       AnnotationUtils.postProcessAnnotationAttributes(element, attributes, false, false);
/* 1354 */       Class<? extends Annotation> annType = attributes.annotationType();
/* 1355 */       if (annType != null) {
/* 1356 */         annotations.add(AnnotationUtils.synthesizeAnnotation(attributes, (Class)annType, element));
/*      */       }
/*      */     } 
/* 1359 */     return annotations;
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
/*      */   private static interface Processor<T>
/*      */   {
/*      */     @Nullable
/*      */     T process(@Nullable AnnotatedElement param1AnnotatedElement, Annotation param1Annotation, int param1Int);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void postProcess(@Nullable AnnotatedElement param1AnnotatedElement, Annotation param1Annotation, T param1T);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean alwaysProcesses();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean aggregates();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     List<T> getAggregatedResults();
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
/*      */   private static abstract class SimpleAnnotationProcessor<T>
/*      */     implements Processor<T>
/*      */   {
/*      */     private final boolean alwaysProcesses;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SimpleAnnotationProcessor() {
/* 1472 */       this(false);
/*      */     }
/*      */     
/*      */     public SimpleAnnotationProcessor(boolean alwaysProcesses) {
/* 1476 */       this.alwaysProcesses = alwaysProcesses;
/*      */     }
/*      */ 
/*      */     
/*      */     public final boolean alwaysProcesses() {
/* 1481 */       return this.alwaysProcesses;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public final void postProcess(@Nullable AnnotatedElement annotatedElement, Annotation annotation, T result) {}
/*      */ 
/*      */ 
/*      */     
/*      */     public final boolean aggregates() {
/* 1491 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public final List<T> getAggregatedResults() {
/* 1496 */       throw new UnsupportedOperationException("SimpleAnnotationProcessor does not support aggregated results");
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
/*      */   static class AlwaysTrueBooleanAnnotationProcessor
/*      */     extends SimpleAnnotationProcessor<Boolean>
/*      */   {
/*      */     public final Boolean process(@Nullable AnnotatedElement annotatedElement, Annotation annotation, int metaDepth) {
/* 1511 */       return Boolean.TRUE;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class MergedAnnotationAttributesProcessor
/*      */     implements Processor<AnnotationAttributes>
/*      */   {
/*      */     private final boolean classValuesAsString;
/*      */ 
/*      */ 
/*      */     
/*      */     private final boolean nestedAnnotationsAsMap;
/*      */ 
/*      */ 
/*      */     
/*      */     private final boolean aggregates;
/*      */ 
/*      */ 
/*      */     
/*      */     private final List<AnnotationAttributes> aggregatedResults;
/*      */ 
/*      */ 
/*      */     
/*      */     MergedAnnotationAttributesProcessor() {
/* 1538 */       this(false, false, false);
/*      */     }
/*      */     
/*      */     MergedAnnotationAttributesProcessor(boolean classValuesAsString, boolean nestedAnnotationsAsMap) {
/* 1542 */       this(classValuesAsString, nestedAnnotationsAsMap, false);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     MergedAnnotationAttributesProcessor(boolean classValuesAsString, boolean nestedAnnotationsAsMap, boolean aggregates) {
/* 1548 */       this.classValuesAsString = classValuesAsString;
/* 1549 */       this.nestedAnnotationsAsMap = nestedAnnotationsAsMap;
/* 1550 */       this.aggregates = aggregates;
/* 1551 */       this.aggregatedResults = aggregates ? new ArrayList<>() : Collections.<AnnotationAttributes>emptyList();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean alwaysProcesses() {
/* 1556 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean aggregates() {
/* 1561 */       return this.aggregates;
/*      */     }
/*      */ 
/*      */     
/*      */     public List<AnnotationAttributes> getAggregatedResults() {
/* 1566 */       return this.aggregatedResults;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public AnnotationAttributes process(@Nullable AnnotatedElement annotatedElement, Annotation annotation, int metaDepth) {
/* 1572 */       return AnnotationUtils.retrieveAnnotationAttributes(annotatedElement, annotation, this.classValuesAsString, this.nestedAnnotationsAsMap);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void postProcess(@Nullable AnnotatedElement element, Annotation annotation, AnnotationAttributes attributes) {
/* 1578 */       annotation = AnnotationUtils.synthesizeAnnotation(annotation, element);
/* 1579 */       Class<? extends Annotation> targetAnnotationType = attributes.annotationType();
/*      */ 
/*      */ 
/*      */       
/* 1583 */       Set<String> valuesAlreadyReplaced = new HashSet<>();
/*      */       
/* 1585 */       for (Method attributeMethod : AnnotationUtils.getAttributeMethods(annotation.annotationType())) {
/* 1586 */         String attributeName = attributeMethod.getName();
/* 1587 */         String attributeOverrideName = AnnotationUtils.getAttributeOverrideName(attributeMethod, targetAnnotationType);
/*      */ 
/*      */         
/* 1590 */         if (attributeOverrideName != null) {
/* 1591 */           if (valuesAlreadyReplaced.contains(attributeOverrideName)) {
/*      */             continue;
/*      */           }
/*      */           
/* 1595 */           List<String> targetAttributeNames = new ArrayList<>();
/* 1596 */           targetAttributeNames.add(attributeOverrideName);
/* 1597 */           valuesAlreadyReplaced.add(attributeOverrideName);
/*      */ 
/*      */           
/* 1600 */           List<String> aliases = AnnotationUtils.getAttributeAliasMap(targetAnnotationType).get(attributeOverrideName);
/* 1601 */           if (aliases != null) {
/* 1602 */             for (String alias : aliases) {
/* 1603 */               if (!valuesAlreadyReplaced.contains(alias)) {
/* 1604 */                 targetAttributeNames.add(alias);
/* 1605 */                 valuesAlreadyReplaced.add(alias);
/*      */               } 
/*      */             } 
/*      */           }
/*      */           
/* 1610 */           overrideAttributes(element, annotation, attributes, attributeName, targetAttributeNames);
/*      */           continue;
/*      */         } 
/* 1613 */         if (!"value".equals(attributeName) && attributes.containsKey(attributeName)) {
/* 1614 */           overrideAttribute(element, annotation, attributes, attributeName, attributeName);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private void overrideAttributes(@Nullable AnnotatedElement element, Annotation annotation, AnnotationAttributes attributes, String sourceAttributeName, List<String> targetAttributeNames) {
/* 1622 */       Object adaptedValue = getAdaptedValue(element, annotation, sourceAttributeName);
/*      */       
/* 1624 */       for (String targetAttributeName : targetAttributeNames) {
/* 1625 */         attributes.put(targetAttributeName, adaptedValue);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private void overrideAttribute(@Nullable AnnotatedElement element, Annotation annotation, AnnotationAttributes attributes, String sourceAttributeName, String targetAttributeName) {
/* 1632 */       attributes.put(targetAttributeName, getAdaptedValue(element, annotation, sourceAttributeName));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     private Object getAdaptedValue(@Nullable AnnotatedElement element, Annotation annotation, String sourceAttributeName) {
/* 1639 */       Object value = AnnotationUtils.getValue(annotation, sourceAttributeName);
/* 1640 */       return AnnotationUtils.adaptValue(element, value, this.classValuesAsString, this.nestedAnnotationsAsMap);
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/annotation/AnnotatedElementUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */