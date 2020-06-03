/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Executable;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Parameter;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Collectors;
/*     */ import kotlin.reflect.KFunction;
/*     */ import kotlin.reflect.KParameter;
/*     */ import kotlin.reflect.jvm.ReflectJvmMapping;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MethodParameter
/*     */ {
/*  63 */   private static final Annotation[] EMPTY_ANNOTATION_ARRAY = new Annotation[0];
/*     */ 
/*     */   
/*     */   private final Executable executable;
/*     */ 
/*     */   
/*     */   private final int parameterIndex;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private volatile Parameter parameter;
/*     */ 
/*     */   
/*     */   private int nestingLevel;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   Map<Integer, Integer> typeIndexesPerLevel;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private volatile Class<?> containingClass;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private volatile Class<?> parameterType;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private volatile Type genericParameterType;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private volatile Annotation[] parameterAnnotations;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private volatile ParameterNameDiscoverer parameterNameDiscoverer;
/*     */   
/*     */   @Nullable
/*     */   private volatile String parameterName;
/*     */   
/*     */   @Nullable
/*     */   private volatile MethodParameter nestedMethodParameter;
/*     */ 
/*     */   
/*     */   public MethodParameter(Method method, int parameterIndex) {
/* 110 */     this(method, parameterIndex, 1);
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
/*     */   public MethodParameter(Method method, int parameterIndex, int nestingLevel) {
/* 124 */     Assert.notNull(method, "Method must not be null");
/* 125 */     this.executable = method;
/* 126 */     this.parameterIndex = validateIndex(method, parameterIndex);
/* 127 */     this.nestingLevel = nestingLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodParameter(Constructor<?> constructor, int parameterIndex) {
/* 136 */     this(constructor, parameterIndex, 1);
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
/*     */   public MethodParameter(Constructor<?> constructor, int parameterIndex, int nestingLevel) {
/* 148 */     Assert.notNull(constructor, "Constructor must not be null");
/* 149 */     this.executable = constructor;
/* 150 */     this.parameterIndex = validateIndex(constructor, parameterIndex);
/* 151 */     this.nestingLevel = nestingLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodParameter(MethodParameter original) {
/* 160 */     Assert.notNull(original, "Original must not be null");
/* 161 */     this.executable = original.executable;
/* 162 */     this.parameterIndex = original.parameterIndex;
/* 163 */     this.parameter = original.parameter;
/* 164 */     this.nestingLevel = original.nestingLevel;
/* 165 */     this.typeIndexesPerLevel = original.typeIndexesPerLevel;
/* 166 */     this.containingClass = original.containingClass;
/* 167 */     this.parameterType = original.parameterType;
/* 168 */     this.genericParameterType = original.genericParameterType;
/* 169 */     this.parameterAnnotations = original.parameterAnnotations;
/* 170 */     this.parameterNameDiscoverer = original.parameterNameDiscoverer;
/* 171 */     this.parameterName = original.parameterName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Method getMethod() {
/* 182 */     return (this.executable instanceof Method) ? (Method)this.executable : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Constructor<?> getConstructor() {
/* 192 */     return (this.executable instanceof Constructor) ? (Constructor)this.executable : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getDeclaringClass() {
/* 199 */     return this.executable.getDeclaringClass();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Member getMember() {
/* 207 */     return this.executable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotatedElement getAnnotatedElement() {
/* 217 */     return this.executable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Executable getExecutable() {
/* 226 */     return this.executable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Parameter getParameter() {
/* 234 */     if (this.parameterIndex < 0) {
/* 235 */       throw new IllegalStateException("Cannot retrieve Parameter descriptor for method return type");
/*     */     }
/* 237 */     Parameter parameter = this.parameter;
/* 238 */     if (parameter == null) {
/* 239 */       parameter = getExecutable().getParameters()[this.parameterIndex];
/* 240 */       this.parameter = parameter;
/*     */     } 
/* 242 */     return parameter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getParameterIndex() {
/* 250 */     return this.parameterIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void increaseNestingLevel() {
/* 258 */     this.nestingLevel++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void decreaseNestingLevel() {
/* 266 */     getTypeIndexesPerLevel().remove(Integer.valueOf(this.nestingLevel));
/* 267 */     this.nestingLevel--;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNestingLevel() {
/* 276 */     return this.nestingLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTypeIndexForCurrentLevel(int typeIndex) {
/* 286 */     getTypeIndexesPerLevel().put(Integer.valueOf(this.nestingLevel), Integer.valueOf(typeIndex));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Integer getTypeIndexForCurrentLevel() {
/* 297 */     return getTypeIndexForLevel(this.nestingLevel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Integer getTypeIndexForLevel(int nestingLevel) {
/* 308 */     return getTypeIndexesPerLevel().get(Integer.valueOf(nestingLevel));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<Integer, Integer> getTypeIndexesPerLevel() {
/* 315 */     if (this.typeIndexesPerLevel == null) {
/* 316 */       this.typeIndexesPerLevel = new HashMap<>(4);
/*     */     }
/* 318 */     return this.typeIndexesPerLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodParameter nested() {
/* 329 */     MethodParameter nestedParam = this.nestedMethodParameter;
/* 330 */     if (nestedParam != null) {
/* 331 */       return nestedParam;
/*     */     }
/* 333 */     nestedParam = clone();
/* 334 */     this.nestingLevel++;
/* 335 */     this.nestedMethodParameter = nestedParam;
/* 336 */     return nestedParam;
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
/*     */   public boolean isOptional() {
/* 348 */     return (getParameterType() == Optional.class || hasNullableAnnotation() || (
/* 349 */       KotlinDetector.isKotlinReflectPresent() && 
/* 350 */       KotlinDetector.isKotlinType(getContainingClass()) && 
/* 351 */       KotlinDelegate.isOptional(this)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasNullableAnnotation() {
/* 360 */     for (Annotation ann : getParameterAnnotations()) {
/* 361 */       if ("Nullable".equals(ann.annotationType().getSimpleName())) {
/* 362 */         return true;
/*     */       }
/*     */     } 
/* 365 */     return false;
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
/*     */   public MethodParameter nestedIfOptional() {
/* 377 */     return (getParameterType() == Optional.class) ? nested() : this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setContainingClass(Class<?> containingClass) {
/* 384 */     this.containingClass = containingClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getContainingClass() {
/* 394 */     Class<?> containingClass = this.containingClass;
/* 395 */     return (containingClass != null) ? containingClass : getDeclaringClass();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setParameterType(@Nullable Class<?> parameterType) {
/* 402 */     this.parameterType = parameterType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getParameterType() {
/* 410 */     Class<?> paramType = this.parameterType;
/* 411 */     if (paramType == null) {
/* 412 */       if (this.parameterIndex < 0) {
/* 413 */         Method method = getMethod();
/* 414 */         paramType = (method != null) ? method.getReturnType() : void.class;
/*     */       } else {
/*     */         
/* 417 */         paramType = this.executable.getParameterTypes()[this.parameterIndex];
/*     */       } 
/* 419 */       this.parameterType = paramType;
/*     */     } 
/* 421 */     return paramType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type getGenericParameterType() {
/* 430 */     Type paramType = this.genericParameterType;
/* 431 */     if (paramType == null) {
/* 432 */       if (this.parameterIndex < 0) {
/* 433 */         Method method = getMethod();
/* 434 */         paramType = (method != null) ? method.getGenericReturnType() : void.class;
/*     */       } else {
/*     */         
/* 437 */         Type[] genericParameterTypes = this.executable.getGenericParameterTypes();
/* 438 */         int index = this.parameterIndex;
/* 439 */         if (this.executable instanceof Constructor && 
/* 440 */           ClassUtils.isInnerClass(this.executable.getDeclaringClass()) && genericParameterTypes.length == this.executable
/* 441 */           .getParameterCount() - 1)
/*     */         {
/*     */ 
/*     */           
/* 445 */           index = this.parameterIndex - 1;
/*     */         }
/*     */         
/* 448 */         paramType = (index >= 0 && index < genericParameterTypes.length) ? genericParameterTypes[index] : getParameterType();
/*     */       } 
/* 450 */       this.genericParameterType = paramType;
/*     */     } 
/* 452 */     return paramType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getNestedParameterType() {
/* 462 */     if (this.nestingLevel > 1) {
/* 463 */       Type type = getGenericParameterType();
/* 464 */       for (int i = 2; i <= this.nestingLevel; i++) {
/* 465 */         if (type instanceof ParameterizedType) {
/* 466 */           Type[] args = ((ParameterizedType)type).getActualTypeArguments();
/* 467 */           Integer index = getTypeIndexForLevel(i);
/* 468 */           type = args[(index != null) ? index.intValue() : (args.length - 1)];
/*     */         } 
/*     */       } 
/*     */       
/* 472 */       if (type instanceof Class) {
/* 473 */         return (Class)type;
/*     */       }
/* 475 */       if (type instanceof ParameterizedType) {
/* 476 */         Type arg = ((ParameterizedType)type).getRawType();
/* 477 */         if (arg instanceof Class) {
/* 478 */           return (Class)arg;
/*     */         }
/*     */       } 
/* 481 */       return Object.class;
/*     */     } 
/*     */     
/* 484 */     return getParameterType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type getNestedGenericParameterType() {
/* 495 */     if (this.nestingLevel > 1) {
/* 496 */       Type type = getGenericParameterType();
/* 497 */       for (int i = 2; i <= this.nestingLevel; i++) {
/* 498 */         if (type instanceof ParameterizedType) {
/* 499 */           Type[] args = ((ParameterizedType)type).getActualTypeArguments();
/* 500 */           Integer index = getTypeIndexForLevel(i);
/* 501 */           type = args[(index != null) ? index.intValue() : (args.length - 1)];
/*     */         } 
/*     */       } 
/* 504 */       return type;
/*     */     } 
/*     */     
/* 507 */     return getGenericParameterType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Annotation[] getMethodAnnotations() {
/* 515 */     return adaptAnnotationArray(getAnnotatedElement().getAnnotations());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <A extends Annotation> A getMethodAnnotation(Class<A> annotationType) {
/* 525 */     A annotation = getAnnotatedElement().getAnnotation(annotationType);
/* 526 */     return (annotation != null) ? adaptAnnotation(annotation) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Annotation> boolean hasMethodAnnotation(Class<A> annotationType) {
/* 536 */     return getAnnotatedElement().isAnnotationPresent(annotationType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Annotation[] getParameterAnnotations() {
/* 543 */     Annotation[] paramAnns = this.parameterAnnotations;
/* 544 */     if (paramAnns == null) {
/* 545 */       Annotation[][] annotationArray = this.executable.getParameterAnnotations();
/* 546 */       int index = this.parameterIndex;
/* 547 */       if (this.executable instanceof Constructor && 
/* 548 */         ClassUtils.isInnerClass(this.executable.getDeclaringClass()) && annotationArray.length == this.executable
/* 549 */         .getParameterCount() - 1)
/*     */       {
/*     */         
/* 552 */         index = this.parameterIndex - 1;
/*     */       }
/*     */       
/* 555 */       paramAnns = (index >= 0 && index < annotationArray.length) ? adaptAnnotationArray(annotationArray[index]) : EMPTY_ANNOTATION_ARRAY;
/* 556 */       this.parameterAnnotations = paramAnns;
/*     */     } 
/* 558 */     return paramAnns;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasParameterAnnotations() {
/* 567 */     return ((getParameterAnnotations()).length != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <A extends Annotation> A getParameterAnnotation(Class<A> annotationType) {
/* 578 */     Annotation[] anns = getParameterAnnotations();
/* 579 */     for (Annotation ann : anns) {
/* 580 */       if (annotationType.isInstance(ann)) {
/* 581 */         return (A)ann;
/*     */       }
/*     */     } 
/* 584 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Annotation> boolean hasParameterAnnotation(Class<A> annotationType) {
/* 593 */     return (getParameterAnnotation(annotationType) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initParameterNameDiscovery(@Nullable ParameterNameDiscoverer parameterNameDiscoverer) {
/* 603 */     this.parameterNameDiscoverer = parameterNameDiscoverer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getParameterName() {
/* 615 */     if (this.parameterIndex < 0) {
/* 616 */       return null;
/*     */     }
/* 618 */     ParameterNameDiscoverer discoverer = this.parameterNameDiscoverer;
/* 619 */     if (discoverer != null) {
/* 620 */       String[] parameterNames = null;
/* 621 */       if (this.executable instanceof Method) {
/* 622 */         parameterNames = discoverer.getParameterNames((Method)this.executable);
/*     */       }
/* 624 */       else if (this.executable instanceof Constructor) {
/* 625 */         parameterNames = discoverer.getParameterNames((Constructor)this.executable);
/*     */       } 
/* 627 */       if (parameterNames != null) {
/* 628 */         this.parameterName = parameterNames[this.parameterIndex];
/*     */       }
/* 630 */       this.parameterNameDiscoverer = null;
/*     */     } 
/* 632 */     return this.parameterName;
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
/*     */   protected <A extends Annotation> A adaptAnnotation(A annotation) {
/* 645 */     return annotation;
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
/*     */   protected Annotation[] adaptAnnotationArray(Annotation[] annotations) {
/* 657 */     return annotations;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 663 */     if (this == other) {
/* 664 */       return true;
/*     */     }
/* 666 */     if (!(other instanceof MethodParameter)) {
/* 667 */       return false;
/*     */     }
/* 669 */     MethodParameter otherParam = (MethodParameter)other;
/* 670 */     return (getContainingClass() == otherParam.getContainingClass() && 
/* 671 */       ObjectUtils.nullSafeEquals(this.typeIndexesPerLevel, otherParam.typeIndexesPerLevel) && this.nestingLevel == otherParam.nestingLevel && this.parameterIndex == otherParam.parameterIndex && this.executable
/*     */ 
/*     */       
/* 674 */       .equals(otherParam.executable));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 679 */     return 31 * this.executable.hashCode() + this.parameterIndex;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 684 */     Method method = getMethod();
/* 685 */     return ((method != null) ? ("method '" + method.getName() + "'") : "constructor") + " parameter " + this.parameterIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodParameter clone() {
/* 691 */     return new MethodParameter(this);
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
/*     */   @Deprecated
/*     */   public static MethodParameter forMethodOrConstructor(Object methodOrConstructor, int parameterIndex) {
/* 706 */     if (!(methodOrConstructor instanceof Executable)) {
/* 707 */       throw new IllegalArgumentException("Given object [" + methodOrConstructor + "] is neither a Method nor a Constructor");
/*     */     }
/*     */     
/* 710 */     return forExecutable((Executable)methodOrConstructor, parameterIndex);
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
/*     */   public static MethodParameter forExecutable(Executable executable, int parameterIndex) {
/* 723 */     if (executable instanceof Method) {
/* 724 */       return new MethodParameter((Method)executable, parameterIndex);
/*     */     }
/* 726 */     if (executable instanceof Constructor) {
/* 727 */       return new MethodParameter((Constructor)executable, parameterIndex);
/*     */     }
/*     */     
/* 730 */     throw new IllegalArgumentException("Not a Method/Constructor: " + executable);
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
/*     */   public static MethodParameter forParameter(Parameter parameter) {
/* 743 */     return forExecutable(parameter.getDeclaringExecutable(), findParameterIndex(parameter));
/*     */   }
/*     */   
/*     */   protected static int findParameterIndex(Parameter parameter) {
/* 747 */     Executable executable = parameter.getDeclaringExecutable();
/* 748 */     Parameter[] allParams = executable.getParameters();
/*     */     int i;
/* 750 */     for (i = 0; i < allParams.length; i++) {
/* 751 */       if (parameter == allParams[i]) {
/* 752 */         return i;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 757 */     for (i = 0; i < allParams.length; i++) {
/* 758 */       if (parameter.equals(allParams[i])) {
/* 759 */         return i;
/*     */       }
/*     */     } 
/* 762 */     throw new IllegalArgumentException("Given parameter [" + parameter + "] does not match any parameter in the declaring executable");
/*     */   }
/*     */ 
/*     */   
/*     */   private static int validateIndex(Executable executable, int parameterIndex) {
/* 767 */     int count = executable.getParameterCount();
/* 768 */     Assert.isTrue((parameterIndex >= -1 && parameterIndex < count), () -> "Parameter index needs to be between -1 and " + (count - 1));
/*     */     
/* 770 */     return parameterIndex;
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
/*     */   private static class KotlinDelegate
/*     */   {
/*     */     public static boolean isOptional(MethodParameter param) {
/* 784 */       Method method = param.getMethod();
/* 785 */       Constructor<?> ctor = param.getConstructor();
/* 786 */       int index = param.getParameterIndex();
/* 787 */       if (method != null && index == -1) {
/* 788 */         KFunction<?> kFunction = ReflectJvmMapping.getKotlinFunction(method);
/* 789 */         return (kFunction != null && kFunction.getReturnType().isMarkedNullable());
/*     */       } 
/*     */       
/* 792 */       KFunction<?> function = null;
/* 793 */       Predicate<KParameter> predicate = null;
/* 794 */       if (method != null) {
/* 795 */         function = ReflectJvmMapping.getKotlinFunction(method);
/* 796 */         predicate = (p -> KParameter.Kind.VALUE.equals(p.getKind()));
/*     */       }
/* 798 */       else if (ctor != null) {
/* 799 */         function = ReflectJvmMapping.getKotlinFunction(ctor);
/* 800 */         predicate = (p -> (KParameter.Kind.VALUE.equals(p.getKind()) || KParameter.Kind.INSTANCE.equals(p.getKind())));
/*     */       } 
/*     */       
/* 803 */       if (function != null) {
/* 804 */         List<KParameter> parameters = function.getParameters();
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 809 */         KParameter parameter = ((List<KParameter>)parameters.stream().filter(predicate).collect(Collectors.toList())).get(index);
/* 810 */         return (parameter.getType().isMarkedNullable() || parameter.isOptional());
/*     */       } 
/*     */       
/* 813 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/MethodParameter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */